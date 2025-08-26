package com.girlkun.models.boss.list_boss.BLACK;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.Random;

/**
 * Lớp đại diện cho boss Super Broly 24 trong game.
 * @author Lucifer
 */
public class SuperBroly24 extends Boss {

    /**
     * Constructor khởi tạo boss Super Broly 24 với ID ngẫu nhiên và dữ liệu từ BossesData.
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public SuperBroly24() throws Exception {
        super(Util.randomBossId(), BossesData.SUPER_BROLY24);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss Super Broly 24.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        if (plKill.pet == null) {
            PetService.gI().createNormalPet(plKill);
            Service.getInstance().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
        } else if (Util.isTrue(2, 100)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    /**
     * Thêm boss Super Broly 24 vào bản đồ và ghi lại thời gian bắt đầu.
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
     * Kích hoạt hành vi của boss Super Broly 24, bao gồm tự động rời bản đồ sau 15 phút.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý sát thương mà boss Super Broly 24 nhận từ người chơi hoặc quái.
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
     * Di chuyển boss Super Broly 24 đến tọa độ cụ thể (bị vô hiệu hóa nếu ở cấp độ 1).
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
     * Thông báo khi boss Super Broly 24 tham gia bản đồ (bị vô hiệu hóa nếu ở cấp độ 1).
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