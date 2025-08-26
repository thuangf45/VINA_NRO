package com.girlkun.models.boss.list_boss.BLACK;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.Random;

/**
 * Lớp đại diện cho boss Zamas Kaio trong game.
 * Xử lý hành vi, phần thưởng và cơ chế hoạt động của boss này.
 * @author Lucifer
 */
public class ZamasKaio extends Boss {

    /**
     * Constructor khởi tạo boss Zamas Kaio với ID cố định và dữ liệu từ BossesData.
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public ZamasKaio() throws Exception {
        super(BossID.ZAMASZIN, BossesData.ZAMAS);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss Zamas Kaio.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        int random = Util.nextInt(100);
        if (random < 20) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 874, 1, this.location.x, this.location.y, plKill.id));
            return;
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    /**
     * Thêm boss Zamas Kaio vào bản đồ và ghi lại thời gian bắt đầu.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /**
     * Thời gian bắt đầu khi boss tham gia bản đồ.
     */
    private long st;

    /**
     * Kích hoạt hành vi của boss Zamas Kaio, bao gồm tự động rời bản đồ sau 15 phút.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý sát thương mà boss Zamas Kaio nhận từ người chơi hoặc quái.
     * @param plAtt Người chơi tấn công boss
     * @param damage Lượng sát thương gây ra
     * @param piercing True nếu sát thương bỏ qua phòng thủ
     * @param isMobAttack True nếu sát thương đến từ quái
     * @return Lượng sát thương thực tế được áp dụng
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Di chuyển boss Zamas Kaio đến tọa độ cụ thể (bị vô hiệu hóa nếu ở cấp độ 1).
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    /*
    @Override
    public void moveTo(int x, int y) {
        if(this.currentLevel == 1){
            return;
        }
        super.moveTo(x, y);
    }
    */

    /**
     * Thông báo phần thưởng của boss Zamas Kaio (bị vô hiệu hóa nếu ở cấp độ 1).
     * @param plKill Người chơi tiêu diệt boss
     */
    /*
    @Override
    public void reward(Player plKill) {
        if(this.currentLevel == 1){
            return;
        }
        super.reward(plKill);
    }
    */

    /**
     * Thông báo khi boss Zamas Kaio tham gia bản đồ (bị vô hiệu hóa nếu ở cấp độ 1).
     */
    /*
    @Override
    protected void notifyJoinMap() {
        if(this.currentLevel == 1){
            return;
        }
        super.notifyJoinMap();
    }
    */
}
