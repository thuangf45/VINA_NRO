package com.girlkun.models.boss.list_boss.NRD;

import com.girlkun.models.player.Player;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Boss Rồng 7 Sao trong sự kiện NRD.
 *
 * Khi bị tiêu diệt sẽ luôn rơi ra Ngọc Rồng 7 Sao (itemId = 378).
 * Cơ chế chiến đấu:
 * - Có khả năng né đòn (chat "Xí hụt" khi né thành công).
 * - Sát thương nhận vào được giảm theo quy tắc:
 *   + Ban đầu sát thương bị chia 7 lần.
 *   + Nếu đang có khiên: sát thương tiếp tục chia 4 lần và khiên sẽ bị phá nếu sát thương vượt quá HP tối đa.
 *
 * @author Lucifer
 */
public class Rong7Sao extends Boss {

    /**
     * Khởi tạo boss Rồng 7 Sao với ID ngẫu nhiên từ dữ liệu BossesData.
     *
     * @throws Exception nếu có lỗi khi khởi tạo boss
     */
    public Rong7Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_7Sao);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss.
     * Boss luôn rơi ra Ngọc Rồng 7 Sao (itemId = 378).
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 378, 1, this.location.x, this.location.y, -1);
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
            // Né đòn nếu may mắn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            // Giảm sát thương theo chỉ số phòng thủ (chia 7 lần)
            damage = this.nPoint.subDameInjureWithDeff(damage / 7);
            // Nếu boss có khiên bảo vệ thì giảm thêm sát thương và có thể phá khiên
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
