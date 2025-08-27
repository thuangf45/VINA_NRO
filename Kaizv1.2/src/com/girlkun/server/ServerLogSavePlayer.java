package com.girlkun.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp ServerLogSavePlayer quản lý việc ghi log dữ liệu người chơi vào tệp SQL.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất,
 * chạy một luồng nền để ghi các thông tin log từ danh sách vào tệp.
 * 
 * @author Lucifer
 */
public class ServerLogSavePlayer implements Runnable {

    /**
     * Thể hiện duy nhất của lớp ServerLogSavePlayer (singleton pattern).
     */
    private static ServerLogSavePlayer i;

    /**
     * Danh sách các chuỗi log cần ghi vào tệp.
     */
    private List<String> list;

    /**
     * Đối tượng BufferedWriter để ghi dữ liệu vào tệp log.
     */
    private BufferedWriter bw;

    /**
     * Khởi tạo một đối tượng ServerLogSavePlayer.
     * Tạo tệp log mới với tên dựa trên thời gian hiện tại và khởi động luồng ghi log.
     * 
     * @throws IOException Nếu có lỗi khi tạo hoặc ghi vào tệp log.
     */
    private ServerLogSavePlayer() {
        this.list = new ArrayList<>();
        try {
            this.bw = new BufferedWriter(new FileWriter("log/log_save_player" + System.currentTimeMillis() + ".sql", true));
            new Thread(this, "Log file save player").start();
        } catch (IOException ex) {
            Logger.getLogger(ServerLogSavePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lấy thể hiện duy nhất của lớp ServerLogSavePlayer.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp ServerLogSavePlayer.
     */
    public static ServerLogSavePlayer gI() {
        if (i == null) {
            i = new ServerLogSavePlayer();
        }
        return i;
    }

    /**
     * Chạy luồng nền để ghi các chuỗi log từ danh sách vào tệp.
     * Luồng này liên tục kiểm tra danh sách log và ghi từng mục vào tệp, sau đó tạm dừng 200ms.
     */
    @Override
    public void run() {
        while (true) {
            while (!this.list.isEmpty()) {
                String text = this.list.remove(0);
                try {
                    bw.write(text.substring(text.indexOf(":") + 2, text.length()) + "\n");
                    bw.flush();
                } catch (IOException e) {
                    // Bỏ qua lỗi ghi tệp để tránh gián đoạn luồng
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                // Bỏ qua lỗi gián đoạn để tiếp tục luồng
            }
        }
    }

    /**
     * Thêm một chuỗi log vào danh sách để ghi vào tệp.
     * 
     * @param text Chuỗi log cần thêm.
     */
    public void add(String text) {
        list.add(text);
    }
}