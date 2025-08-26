package com.girlkun.models.boss.list_boss.Mabu12h;

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
 * Lớp đại diện cho boss Drabura2 trong sự kiện Mabu 12h.
 * Đây là phiên bản nâng cấp của Drabura, với khả năng phòng thủ và rơi vật phẩm tương tự.
 * Người chơi khi tiêu diệt sẽ nhận thưởng ngẫu nhiên (đồ TL, NR, đồ cấp 12 hoặc item đặc biệt).
 * Đồng thời được cộng điểm sự kiện Mabu.
 *
 * @author Lucifer
 */
public class Drabura2 extends Boss {

    /**
     * Khởi tạo boss Drabura2 với ID ngẫu nhiên và dữ liệu từ BossesData.
     *
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public Drabura2() throws Exception {
        super(Util.randomBossId(), BossesData.DRABURA_2);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt Drabura2.
     * - Tỉ lệ rơi đồ TL, NR, hoặc đồ cấp 12.
     * - Ngoài ra có thể rơi item đặc biệt (ID 561).
     * - Khi tiêu diệt sẽ cộng điểm sự kiện Mabu cho người chơi.
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length -1);

        if (Util.isTrue(1, 130)) {
            if (Util.isTrue(1, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else
        if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone,new ItemMap (Util.RaitiDoc12(zone, Manager.itemDC12[randomc12], 1, this.location.x, this.location.y, plKill.id)));
            return;
        }
        else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint((byte) 20);
    }

    /**
     * Xử lý khi Drabura2 bị tấn công.
     * - Có thể né đòn dựa theo tỉ lệ né.
     * - Nếu đang có khiên, giảm sát thương và có thể phá khiên.
     * - Giới hạn sát thương tối đa là 1,000,000.
     * - Nếu HP về 0, boss sẽ chết và gọi hàm die().
     *
     * @param plAtt Người chơi tấn công
     * @param damage Sát thương gây ra
     * @param piercing Có xuyên giáp hay không
     * @param isMobAttack Có phải mob tấn công hay không
     * @return Lượng sát thương thực tế đã trừ vào máu boss
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
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
            if (damage >= 1000000) {
                damage = 1000000;
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
