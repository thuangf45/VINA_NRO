package com.girlkun.models.boss.list_boss.Cooler;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.Random;

/**
 * Lớp đại diện cho boss Cooler trong game.
 * Cooler có thể biến đổi qua nhiều cấp độ và có phần thưởng đặc biệt khi bị tiêu diệt.
 * Xử lý hành vi, sát thương và cơ chế rời bản đồ.
 * 
 * @author Lucifer
 */
public class Cooler extends Boss {

    /**
     * Constructor khởi tạo boss Cooler với ID ngẫu nhiên và dữ liệu từ BossesData.
     * 
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public Cooler() throws Exception {
        super(Util.randomBossId(), BossesData.COOLER_1, BossesData.COOLER_2);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt Cooler.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{14, 15};
        int randomnro = new Random().nextInt(itemDos.length);
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        if (Util.isTrue(5, 100)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemDos[randomnro], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }

    /**
     * Thêm Cooler vào bản đồ và ghi lại thời gian bắt đầu.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /**
     * Thời gian bắt đầu khi Cooler tham gia bản đồ.
     */
    private long st;

    /**
     * Kích hoạt hành vi của Cooler, bao gồm tự động rời bản đồ sau 15 phút.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý sát thương mà Cooler nhận từ người chơi hoặc quái.
     * Bao gồm né đòn, chắn đòn và giảm sát thương.
     * 
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
                damage = 1;
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
}
