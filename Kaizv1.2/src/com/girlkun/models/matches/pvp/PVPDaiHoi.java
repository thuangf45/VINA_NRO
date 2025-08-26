package com.girlkun.models.matches.pvp;

import com.girlkun.models.matches.PVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho trận đấu PVP trong sự kiện Đại Hội Võ Thuật.
 * @author Lucifer
 */
public class PVPDaiHoi extends PVP {

    /**
     * Lượng vàng cược cho trận đấu.
     */
    private final int goldThachDau;

    /**
     * Lượng vàng phần thưởng khi thắng trận đấu.
     */
    private final long goldReward;

    /**
     * Đối tượng Đại Hội Võ Thuật liên quan đến trận đấu.
     */
    private final DaiHoiVoThuat dh;

    /**
     * Thời gian bắt đầu trận đấu PVP.
     */
    private long lastTimePVP;

    /**
     * Constructor khởi tạo trận đấu PVP trong Đại Hội Võ Thuật.
     * @param p1 Người chơi thứ nhất
     * @param p2 Người chơi thứ hai
     * @param goldThachDau Lượng vàng cược cho trận đấu
     * @param d Đối tượng Đại Hội Võ Thuật
     * @param l Thời gian bắt đầu trận đấu
     */
    public PVPDaiHoi(Player p1, Player p2, int goldThachDau, DaiHoiVoThuat d, long l) {
        super(TYPE_PVP.DAI_HOI_VO_THUAT, p1, p2);
        this.goldThachDau = goldThachDau;
        this.goldReward = goldThachDau;
        this.dh = d;
        this.lastTimePVP = l;
    }

    /**
     * Bắt đầu trận đấu PVP trong Đại Hội Võ Thuật.
     */
    @Override
    public void start() {
        super.start();
    }

    /**
     * Kết thúc trận đấu PVP (không thực hiện hành động cụ thể trong lớp này).
     */
    @Override
    public void finish() {
    }

    /**
     * Dọn dẹp tài nguyên và kết thúc trận đấu PVP.
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Cập nhật trạng thái trận đấu PVP, kiểm tra thời gian và xác định người thắng dựa trên HP hoặc hòa.
     */
    @Override
    public void update() {
        if (Util.canDoWithTime(lastTimePVP, 180000)) {
            if (p1.nPoint.hp > p2.nPoint.hp) {
                ChangeMapService.gI().changeMapBySpaceShip(p2, 21 + p2.gender, 0, -1);
                p1.pvp.lose(p2, TYPE_LOSE_PVP.DEAD);
            } else if (p2.nPoint.hp > p1.nPoint.hp) {
                ChangeMapService.gI().changeMapBySpaceShip(p1, 21 + p1.gender, 0, -1);
                p2.pvp.lose(p1, TYPE_LOSE_PVP.DEAD);
            } else {
                Service.gI().sendThongBao(p1, "Trận đấu hòa. Bạn nhận được " + Util.numberToMoney(this.goldReward) + " vàng");
                p1.inventory.gold += this.goldReward / 2;
                Service.gI().sendMoney(p1);
                dh.listReg.add(p1);
                p1.lastTimeWin = System.currentTimeMillis();
                p1.isWin = true;

                Service.gI().sendThongBao(p2, "Trận đấu hòa. Bạn nhận được " + Util.numberToMoney(this.goldReward) + " vàng");
                p2.inventory.gold += this.goldReward / 2;
                Service.gI().sendMoney(p2);
                dh.listReg.add(p2);
                p2.lastTimeWin = System.currentTimeMillis();
                p2.isWin = true;
            }
        }
    }

    /**
     * Trao phần thưởng cho người chơi thắng trận đấu.
     * @param plWin Người chơi thắng
     */
    @Override
    public void reward(Player plWin) {
        plWin.inventory.gold += this.goldReward;
        Service.gI().sendMoney(plWin);
        dh.listReg.add(plWin);
        plWin.lastTimeWin = System.currentTimeMillis();
        plWin.isWin = true;
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
            (p1.equals(plLose) ? p1 : p2).inventory.gold -= this.goldReward;
            dh.listReg.remove(plLose);
        } else if (typeLose == TYPE_LOSE_PVP.DEAD) {
            Service.gI().sendThongBao(p1.equals(plLose) ? p2 : p1, "Đối thủ kiệt sức. Bạn thắng nhận được " + Util.numberToMoney(this.goldReward) + " vàng");
            Service.gI().sendThongBao(p1.equals(plLose) ? p1 : p2, "Bạn bị xử thua vì kiệt sức");
            (p1.equals(plLose) ? p1 : p2).inventory.gold -= this.goldReward;
            dh.listReg.remove(plLose);
        }
        Service.gI().sendMoney(p1.equals(plLose) ? p1 : p2);
    }
}