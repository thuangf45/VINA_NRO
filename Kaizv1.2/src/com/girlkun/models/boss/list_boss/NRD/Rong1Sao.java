package com.girlkun.models.boss.list_boss.NRD;

import com.girlkun.models.player.Player;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss Rồng 1 Sao trong sự kiện NRD.
 * - Khi bị hạ gục sẽ rơi ra ngọc rồng 1 sao (itemId = 372).
 * - Có cơ chế giảm sát thương và né đòn theo tỉ lệ.
 * - Sát thương nhận vào được giảm so với boss thường:
 *   + Chia 7 trước khi trừ máu.
 *   + Nếu đang có khiên thì sát thương tiếp tục chia 4.
 *
 * @author Lucifer
 */
public class Rong1Sao extends Boss {

    /**
     * Khởi tạo boss Rồng 1 Sao với ID ngẫu nhiên từ dữ liệu BossesData.
     *
     * @throws Exception nếu có lỗi khi khởi tạo
     */
    public Rong1Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_1Sao);
    }

    /**
     * Xử lý phần thưởng khi người chơi hạ gục Rồng 1 Sao.
     * - Boss luôn rơi ra Ngọc Rồng 1 Sao (itemId = 372).
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 372, 1, this.location.x, this.location.y, -1);
        Service.getInstance().dropItemMap(this.zone, it);
    }

    /**
     * Xử lý khi boss nhận sát thương.
     * - Có tỉ lệ "xí hụt" né toàn bộ sát thương.
     * - Nếu dính sát thương: giảm theo công thức đặc biệt.
     *   + damage / 7
     *   + Nếu có khiên: damage / 4
     * - Kiểm tra boss có chết sau khi trừ máu để gọi die().
     *
     * @param plAtt Người tấn công
     * @param damage Lượng sát thương gây ra
     * @param piercing Đòn đánh có xuyên giáp hay không
     * @param isMobAttack Có phải đòn đánh từ quái không
     * @return Lượng sát thương thực tế boss phải nhận
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
