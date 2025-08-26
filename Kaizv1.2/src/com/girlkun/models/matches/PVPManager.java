package com.girlkun.models.matches;

import com.girlkun.models.player.Player;
import com.girlkun.server.ServerManager;
import java.util.ArrayList;

/**
 * Lớp quản lý các trận đấu PVP trong game theo mô hình Singleton.
 * @author Lucifer
 */
public class PVPManager implements Runnable {

    /**
     * Instance duy nhất của lớp PVPManager (Singleton).
     */
    private static PVPManager i;

    /**
     * Lấy instance của PVPManager (Singleton pattern).
     * @return Instance của PVPManager
     */
    public static PVPManager gI() {
        if (i == null) {
            i = new PVPManager();
        }
        return i;
    }

    /**
     * Danh sách các trận đấu PVP đang được quản lý.
     */
    private ArrayList<PVP> pvps;

    /**
     * Constructor khởi tạo PVPManager và bắt đầu luồng cập nhật trận đấu.
     */
    public PVPManager() {
        this.pvps = new ArrayList<>();
        new Thread(this, "Update pvp").start();
    }

    /**
     * Xóa một trận đấu PVP khỏi danh sách quản lý.
     * @param pvp Trận đấu PVP cần xóa
     */
    public void removePVP(PVP pvp) {
        this.pvps.remove(pvp);
    }

    /**
     * Thêm một trận đấu PVP vào danh sách quản lý.
     * @param pvp Trận đấu PVP cần thêm
     */
    public void addPVP(PVP pvp) {
        this.pvps.add(pvp);
    }

    /**
     * Tìm trận đấu PVP mà người chơi đang tham gia.
     * @param player Người chơi cần kiểm tra
     * @return Trận đấu PVP nếu người chơi đang tham gia, hoặc null nếu không tìm thấy
     */
    public PVP getPVP(Player player) {
        for (PVP pvp : this.pvps) {
            if (pvp.p1.equals(player) || pvp.p2.equals(player)) {
                return pvp;
            }
        }
        return null;
    }

    /**
     * Chạy luồng cập nhật liên tục cho các trận đấu PVP.
     */
    @Override
    public void run() {
        this.update();
    }

    /**
     * Cập nhật trạng thái của tất cả các trận đấu PVP đang được quản lý.
     */
    private void update() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (PVP pvp : pvps) {
                    pvp.update();
                }
                Thread.sleep(1000 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
            }
        }
    }
}