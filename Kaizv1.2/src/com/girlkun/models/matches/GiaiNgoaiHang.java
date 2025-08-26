package com.girlkun.models.matches;

import com.girlkun.server.ServerManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Lớp GiaiNgoaiHang quản lý giải đấu ngoại hạng trong trò chơi, chạy dưới dạng một luồng cập nhật liên tục.
 * @author Lucifer
 */
public class GiaiNgoaiHang implements Runnable {

    /**
     * Thể hiện duy nhất của lớp GiaiNgoaiHang (mô hình Singleton).
     */
    private static GiaiNgoaiHang I;

    /**
     * Danh sách các ID của người chơi đăng ký tham gia giải đấu.
     */
    private List<Long> subscribers;

    /**
     * Lấy thể hiện duy nhất của lớp GiaiNgoaiHang và khởi động luồng cập nhật nếu chưa tồn tại.
     * @return Thể hiện của GiaiNgoaiHang.
     */
    public static GiaiNgoaiHang gI() {
        if (GiaiNgoaiHang.I == null) {
            GiaiNgoaiHang.I = new GiaiNgoaiHang();
            new Thread(GiaiNgoaiHang.I, "Update giải đấu ngoại hạng").start();
        }
        return GiaiNgoaiHang.I;
    }

    /**
     * Khởi tạo đối tượng GiaiNgoaiHang với danh sách người chơi rỗng.
     */
    public GiaiNgoaiHang() {
        this.subscribers = new ArrayList<>();
    }

    /**
     * Phương thức chạy luồng, thực hiện cập nhật giải đấu liên tục.
     */
    @Override
    public void run() {
        this.update();
    }

    /**
     * Cập nhật trạng thái giải đấu ngoại hạng, chạy liên tục khi server hoạt động.
     */
    private void update() {
        while (ServerManager.isRunning) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}