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
 * Lớp đại diện cho boss Mabu trong sự kiện Mabu 12h.
 * - Đây là boss chính, có khả năng đặc biệt (bản năng vô cực) giúp né đòn với tỉ lệ nhất định.
 * - Khi tiêu diệt sẽ rơi các vật phẩm ngẫu nhiên: đồ TL, NR, đồ cấp 12, hoặc item đặc biệt (ID 1142).
 * 
 * @author Lucifer
 */
public class MabuBoss extends Boss {

    /**
     * Khởi tạo boss Mabu với ID ngẫu nhiên và dữ liệu từ BossesData.
     *
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public MabuBoss() throws Exception {
        super(Util.randomBossId(), BossesData.MABU_12H);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt Mabu.
     * - Có tỉ lệ rơi đồ TL, NR hoặc đồ cấp 12.
     * - Ngoài ra có thể rơi item đặc biệt (ID 1142).
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
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1142, 1, this.location.x, this.location.y, plKill.id));
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
    }

    /**
     * Xử lý khi Mabu bị tấn công.
     * - Có cơ chế né đòn đặc biệt (bản năng vô cực), kèm theo các câu thoại khi kích hoạt.
     * - Nếu đang có khiên, sát thương sẽ được giảm và có thể phá khiên khi damage quá lớn.
     * - Khi HP về 0, boss sẽ chết và gọi hàm die().
     *
     * @param plAtt Người chơi tấn công
     * @param damage Sát thương gây ra
     * @param piercing Có xuyên giáp hay không
     * @param isMobAttack Có phải mob tấn công hay không
     * @return Lượng sát thương thực tế đã trừ vào máu boss
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(50, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 100000);
            if (Util.isTrue(10, 100)) {
                this.chat("Hãy để bản năng tự vận động");
                this.chat("Tránh các động tác thừa");
            } else if (Util.isTrue(20, 100)) {
                this.chat("Chậm lại,các ngươi quá nhanh rồi");
                this.chat("Chỉ cần hoàn thiện nó!");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(30, 100)) {
                this.chat("Đây chính là bản năng vô cực");
            }
            damage = 0;

        }
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
                damage = damage;
                 if (damage > nPoint.mpMax) {
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
}
