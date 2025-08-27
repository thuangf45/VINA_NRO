package com.girlkun.server;

import com.arriety.kygui.ShopKyGuiManager;
import com.girlkun.services.ClanService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;

/**
 * Lớp Maintenance quản lý quá trình bảo trì hệ thống của server game.
 * Lớp này chạy dưới dạng một luồng (thread) để thông báo và thực hiện đóng server sau một khoảng thời gian xác định.
 * Sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class Maintenance extends Thread {

    /**
     * Trạng thái của hệ thống bảo trì (true nếu đang chạy bảo trì).
     */
    public static boolean isRuning = false;

    /**
     * Cho phép sử dụng mã (code) trong thời gian bảo trì.
     */
    public boolean canUseCode;

    /**
     * Trạng thái bảo trì (true nếu server đang trong quá trình bảo trì).
     */
    public static boolean isBaoTri = false;

    /**
     * Thể hiện duy nhất của lớp Maintenance (singleton pattern).
     */
    private static Maintenance i;

    /**
     * Số giây còn lại trước khi server đóng để bảo trì.
     */
    private int min;

    /**
     * Khởi tạo một đối tượng Maintenance.
     * Constructor private để đảm bảo chỉ tạo thể hiện thông qua phương thức gI().
     */
    private Maintenance() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp Maintenance.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp Maintenance.
     */
    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    /**
     * Bắt đầu quá trình bảo trì với số giây xác định.
     * Chỉ bắt đầu nếu bảo trì chưa được kích hoạt.
     * 
     * @param min Số giây trước khi server đóng để bảo trì.
     */
    public void start(int min) {
        if (!isRuning) {
            isRuning = true;
            this.min = min;
            this.start();
        }
    }

    /**
     * Chạy luồng bảo trì, thông báo thời gian còn lại và đóng server khi hết thời gian.
     * Gửi thông báo đến tất cả người chơi và ghi log hệ thống mỗi giây.
     */
    @Override
    public void run() {
        while (this.min > 0) {
            isBaoTri = true;
            this.min--;
            Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + min
                    + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
            Logger.log(Logger.RED_BACKGROUND, "\nHệ thống tiến hành bảo trì sau " + min + "s");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        ServerManager.gI().close(100);
    }
}