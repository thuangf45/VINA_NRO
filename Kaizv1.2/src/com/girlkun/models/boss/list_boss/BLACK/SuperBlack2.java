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
 * Lớp đại diện cho boss Super Black Goku 2 trong game.
 * @author Lucifer
 */
public class SuperBlack2 extends Boss {

    /**
     * Constructor khởi tạo boss Super Black Goku 2 với ID ngẫu nhiên và dữ liệu từ BossesData.
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public SuperBlack2() throws Exception {
        super(Util.randomBossId(), BossesData.SUPER_BLACK_GOKU_2);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss Super Black Goku 2.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        int[] itemDos = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277, 281};
        int randomc12 = new Random().nextInt(itemDos.length);
        if (Util.isTrue(2, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(2, 5)) {
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos[randomc12], 1, this.location.x, this.location.y, plKill.id));
            return;
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    /**
     * Kích hoạt hành vi của boss Super Black Goku 2, bao gồm kiểm tra thời gian để rời bản đồ.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000000)) {
             this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Thêm boss Super Black Goku 2 vào bản đồ và ghi lại thời gian bắt đầu.
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
     * Xử lý sát thương mà boss Super Black Goku 2 nhận từ người chơi hoặc quái.
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
                damage = damage;
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
     * Di chuyển boss Super Black Goku 2 đến tọa độ cụ thể (bị vô hiệu hóa nếu ở cấp độ 1).
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    /*
    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }
    */

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss (bị vô hiệu hóa nếu ở cấp độ 1).
     * @param plKill Người chơi đã tiêu diệt boss
     */
    /*
    @Override
    public void reward(Player plKill) {
        if (this.currentLevel == 1) {
            return;
        }
        super.reward(plKill);
    }
    */

    /**
     * Thông báo khi boss Super Black Goku 2 tham gia bản đồ (bị vô hiệu hóa nếu ở cấp độ 1).
     */
    /*
    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }
    */
}