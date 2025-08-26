package com.girlkun.models.matches.pvp;

import com.girlkun.models.matches.PVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho trận đấu thách đấu (PVP) giữa hai người chơi trong game.
 * @author Lucifer
 */
public class ThachDau extends PVP {

    /**
     * Lượng vàng cược cho trận đấu thách đấu.
     */
    private int goldThachDau;

    /**
     * Lượng vàng phần thưởng khi thắng trận đấu (80% của vàng cược).
     */
    private long goldReward;

    /**
     * Constructor khởi tạo trận đấu thách đấu giữa hai người chơi.
     * @param p1 Người chơi thứ nhất
     * @param p2 Người chơi thứ hai
     * @param goldThachDau Lượng vàng cược cho trận đấu
     */
    public ThachDau(Player p1, Player p2, int goldThachDau) {
        super(TYPE_PVP.THACH_DAU, p1, p2);
        this.goldThachDau = goldThachDau;
        this.goldReward = goldThachDau / 100 * 80;
    }

    /**
     * Bắt đầu trận đấu thách đấu, trừ vàng cược của cả hai người chơi và kích hoạt trạng thái PVP.
     */
    @Override
    public void start() {
        this.p1.inventory.gold -= this.goldThachDau;
        this.p2.inventory.gold -= this.goldThachDau;
        Service.gI().sendMoney(this.p1);
        Service.gI().sendMoney(this.p2);
        super.start();
    }

    /**
     * Kết thúc trận đấu thách đấu (không thực hiện hành động cụ thể trong lớp này).
     */
    @Override
    public void finish() {
    }

    /**
     * Dọn dẹp tài nguyên và kết thúc trận đấu thách đấu.
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Cập nhật trạng thái trận đấu thách đấu (hiện không thực hiện hành động cụ thể).
     */
    @Override
    public void update() {
    }

    /**
     * Trao phần thưởng vàng cho người chơi thắng trận đấu.
     * @param plWin Người chơi thắng
     */
    @Override
    public void reward(Player plWin) {
        plWin.inventory.gold += this.goldReward;
        Service.gI().sendMoney(plWin);
    }

    /**
     * Gửi kết quả trận đấu và xử lý phần thưởng hoặc hình phạt cho người chơi thua.
     * @param plLose Người chơi thua
     * @param typeLose Loại thua cuộc (bỏ chạy hoặc chết)
     */
    @Override
    public void sendResult(Player plLose, TYPE_LOSE_PVP typeLose) {
        if (typeLose == TYPE_LOSE_PVP.RUNS_AWAY) {
            Service.gI().sendThongBao(p1.equals(plLose) ? p2 : p1, "Đối thủ kiệt sức. Bạn thắng nhận được " + Util.numberToMoney(this.goldReward) + " vàng");
            Service.gI().sendThongBao(p1.equals(plLose) ? p1 : p2, "Bạn bị xử thua vì kiệt sức");
            (p1.equals(plLose) ? p1 : p2).inventory.gold -= this.goldThachDau;
        } else if (typeLose == TYPE_LOSE_PVP.DEAD) {
            Service.gI().sendThongBao(p1.equals(plLose) ? p2 : p1, "Đối thủ kiệt sức. Bạn thắng nhận được " + Util.numberToMoney(this.goldReward) + " vàng");
            Service.gI().sendThongBao(p1.equals(plLose) ? p1 : p2, "Bạn bị xử thua vì kiệt sức");
            (p1.equals(plLose) ? p1 : p2).inventory.gold -= this.goldThachDau;
        }
        Service.gI().sendMoney(p1.equals(plLose) ? p1 : p2);
    }
}