package com.girlkun.server;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp ServerNotify quản lý việc gửi thông báo tới tất cả người chơi trong game.
 * Lớp này chạy một luồng nền để gửi các thông báo từ danh sách và thông báo định kỳ.
 * Sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class ServerNotify extends Thread {

    /**
     * Mảng byte chứa thông báo mặc định (chưa giải mã).
     */
    private byte[] gk = new byte[]{67, 104, -61, -96, 111, 32, 109, -31, -69, -85,
        110, 103, 32, 98, -31, -70, -95, 110, 32, -60, -111, -61, -93, 32, 116, -31,
        -69, -101, 105, 32, 118, -31, -69, -101, 105, 32, 109, -61, -95, 121, 32,
        99, 104, -31, -69, -89, 32, 71, 105, 114, 108, 107, 117, 110, 55, 53, 46,
        32, 67, 104, -61, -70, 99, 32, 99, -61, -95, 99, 32, 98, -31, -70, -95,
        110, 32, 99, 104, -58, -95, 105, 32, 103, 97, 109, 101, 32, 118, 117,
        105, 32, 118, -31, -70, -69, 46, 46};

    /**
     * Thời gian lần cuối gửi thông báo mặc định.
     */
    private long lastTimeGK;

    /**
     * Danh sách các thông báo cần gửi tới người chơi.
     */
    private final List<String> notifies;

    /**
     * Thể hiện duy nhất của lớp ServerNotify (singleton pattern).
     */
    private static ServerNotify i;

    /**
     * Khởi tạo một đối tượng ServerNotify và bắt đầu luồng gửi thông báo.
     */
    private ServerNotify() {
        this.notifies = new ArrayList<>();
        this.start();
    }

    /**
     * Lấy thể hiện duy nhất của lớp ServerNotify.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp ServerNotify.
     */
    public static ServerNotify gI() {
        if (i == null) {
            i = new ServerNotify();
        }
        return i;
    }

    /**
     * Chạy luồng nền để gửi thông báo từ danh sách notifies hoặc thông báo mặc định định kỳ.
     * Luồng này gửi thông báo mỗi 1000ms và thông báo mặc định mỗi 500000ms.
     */
    @Override
    public void run() {
        while (!Maintenance.isRuning) {
            try {
                while (!notifies.isEmpty()) {
                    sendThongBaoBenDuoi(notifies.remove(0));
                }
                if (Util.canDoWithTime(this.lastTimeGK, 500000)) {
                    sendThongBaoBenDuoi("Thông Báo");
                    this.lastTimeGK = System.currentTimeMillis();
                }
            } catch (Exception e) {
                // Bỏ qua lỗi để tiếp tục chạy luồng
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Bỏ qua lỗi gián đoạn để tiếp tục luồng
            }
        }
    }

    /**
     * Gửi thông báo tới tất cả người chơi trong game sử dụng tin nhắn loại 93.
     * 
     * @param text Nội dung thông báo cần gửi.
     */
    private void sendThongBaoBenDuoi(String text) {
        Message msg;
        try {
            msg = new Message(93);
            msg.writer().writeUTF(text);
            Service.gI().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi thông báo
        }
    }

    /**
     * Thêm một thông báo vào danh sách để gửi tới người chơi.
     * 
     * @param text Nội dung thông báo cần thêm.
     */
    public void notify(String text) {
        this.notifies.add(text);
    }

    /**
     * Gửi thông báo tab (loại 50) tới một người chơi cụ thể.
     * Thông báo được lấy từ danh sách thông báo trong Manager.
     * 
     * @param player Người chơi nhận thông báo.
     */
    public void sendNotifyTab(Player player) {
        Message msg;
        try {
            msg = new Message(50);
            msg.writer().writeByte(10);
            for (int i = 0; i < Manager.NOTIFY.size(); i++) {
                String[] arr = Manager.NOTIFY.get(0).split("<>");
                msg.writer().writeShort(i);
                msg.writer().writeUTF(arr[0]);
                msg.writer().writeUTF(arr[1]);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi thông báo
        }
    }
}