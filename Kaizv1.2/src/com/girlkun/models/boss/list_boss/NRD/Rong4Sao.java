package com.girlkun.models.boss.list_boss.NRD;

import com.girlkun.models.player.Player;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Boss Rồng 4 Sao trong sự kiện NRD.
 *
 * Khi chết luôn rơi ra Ngọc Rồng 4 Sao (itemId = 375).
 * Có khả năng né đòn và chat "Xí hụt".
 * Sát thương nhận vào được giảm theo cơ chế đặc biệt:
 * - Sát thương cơ bản bị chia 7 lần.
 * - Nếu đang có khiên: sát thương tiếp tục chia 4 lần và khiên sẽ bị phá nếu sát thương vượt quá HP tối đa.
 *
 * @author Lucifer
 */
public class Rong4Sao extends Boss {

    /**
     * Khởi tạo boss Rồng 4 Sao với ID ngẫu nhiên từ dữ liệu BossesData.
     *
     * @throws Exception nếu có lỗi khi khởi tạo boss
     */
    public Rong4Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_4Sao);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss.
     * Boss luôn rơi ra Ngọc Rồng 4 Sao (itemId = 375).
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 375, 1, this.location.x, this.location.y, -1);
        Service.getInstance().dropItemMap(this.zone, it);
    }

    /**
     * Xử lý khi boss bị tấn công và nhận sát thương.
     *
     * @param plAtt       Người tấn công (có thể null nếu không phải người chơi)
     * @param damage      Sát thương ban đầu
     * @param piercing    Đòn đánh có xuyên giáp hay không
     * @param isMobAttack Có phải đòn đánh từ quái khác không
     * @return Sát thương thực tế mà boss nhận vào
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn với tỉ lệ cao
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            // Giảm sát thương cơ bản
            damage = this.nPoint.subDameInjureWithDeff(damage / 7);
            // Nếu boss đang có khiên thì giảm thêm và có thể phá khiên
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 4;
            }
            // Trừ máu thực tế
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
