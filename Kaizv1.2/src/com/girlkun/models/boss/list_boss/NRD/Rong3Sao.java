package com.girlkun.models.boss.list_boss.NRD;

import com.girlkun.models.player.Player;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss Rồng 3 Sao trong sự kiện NRD.
 * - Khi bị hạ gục sẽ rơi ra Ngọc Rồng 3 Sao (itemId = 374).
 * - Cơ chế phòng thủ giống các Rồng Sao khác:
 *   + Có tỉ lệ né đòn ("xí hụt") hoàn toàn.
 *   + Sát thương nhận vào bị chia cho 7.
 *   + Nếu boss còn khiên, sát thương tiếp tục giảm /4 và có thể phá khiên.
 *
 * @author Lucifer
 */
public class Rong3Sao extends Boss {

    /**
     * Khởi tạo boss Rồng 3 Sao với ID ngẫu nhiên từ dữ liệu BossesData.
     *
     * @throws Exception nếu có lỗi khi khởi tạo
     */
    public Rong3Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_3Sao);
    }

    /**
     * Xử lý phần thưởng khi người chơi hạ gục Rồng 3 Sao.
     * - Boss luôn rơi ra Ngọc Rồng 3 Sao (itemId = 374).
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 374, 1, this.location.x, this.location.y, -1);
        Service.getInstance().dropItemMap(this.zone, it);
    }

    /**
     * Xử lý khi boss nhận sát thương.
     * - Có tỉ lệ né hoàn toàn (chat "Xí hụt").
     * - Nếu dính sát thương:
     *   + damage / 7
     *   + Nếu boss đang có khiên: damage / 4 và có thể phá khiên.
     * - Kiểm tra boss có chết sau khi trừ HP, nếu chết thì gọi die().
     *
     * @param plAtt Người tấn công
     * @param damage Sát thương ban đầu
     * @param piercing Đòn đánh có xuyên giáp không
     * @param isMobAttack Có phải đòn đánh từ quái không
     * @return Sát thương thực tế mà boss nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 7);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 4;
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
