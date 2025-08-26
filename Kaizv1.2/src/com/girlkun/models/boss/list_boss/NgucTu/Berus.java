package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * Boss Thần Hủy Diệt (Berus) trong ngục tù.
 *
 * Cơ chế:
 * - Khi bị tiêu diệt sẽ có tỉ lệ rơi ra các loại đồ quý hiếm.
 * - Thời gian tồn tại trên bản đồ là 15 phút, sau đó sẽ tự rời đi.
 * - Có khả năng né đòn, giảm sát thương theo phòng thủ và chịu ảnh hưởng của khiên.
 *
 * @author Lucifer
 */
public class Berus extends Boss {

    /**
     * Khởi tạo boss Berus (Thần Hủy Diệt) với dữ liệu từ {@link BossesData}.
     *
     * @throws Exception nếu có lỗi trong quá trình khởi tạo
     */
    public Berus() throws Exception {
        super(BossID.THAN_HUY_DIET, BossesData.THAN_HUY_DIET);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss.
     * - Có 1/30 cơ hội rơi ra item trong {@code itemDos1}.
     * - Có 30% cơ hội rơi ra item trong {@code itemDos2}.
     *
     * @param plKill Người chơi hạ gục boss
     */
    public void reward(Player plKill) {
        int[] itemDos1 = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
        int[] itemDos2 = new int[]{14, 15, 16, 17};
        int[] itemDos3 = new int[]{650, 652, 654, 651, 653, 655, 657, 659, 661, 658, 660, 662};
        int randomDo = new Random().nextInt(itemDos1.length);
        int randomDo1 = new Random().nextInt(itemDos2.length);
        int randomDo2 = new Random().nextInt(itemDos3.length);
        if (Util.isTrue(1, 30)) {
            Service.getInstance().dropItemMap(this.zone,
                    Util.ratiItem(zone, itemDos1[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
        if (Util.isTrue(30, 100)) {
            Service.getInstance().dropItemMap(this.zone,
                    Util.ratiItem(zone, itemDos2[randomDo1], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    /**
     * Khi boss tham gia bản đồ, bắt đầu đếm thời gian tồn tại (15 phút).
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /** Thời điểm boss xuất hiện trên bản đồ */
    private long st;

    /**
     * Kích hoạt vòng lặp hoạt động của boss.
     * Nếu boss đã tồn tại hơn 15 phút thì sẽ tự động rời bản đồ.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý khi boss bị tấn công và nhận sát thương.
     *
     * @param plAtt       Người tấn công
     * @param damage      Sát thương ban đầu
     * @param piercing    Có xuyên giáp hay không
     * @param isMobAttack Có phải đòn đánh từ quái không
     * @return Sát thương thực tế mà boss nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            // Giảm sát thương theo chỉ số phòng thủ
            damage = this.nPoint.subDameInjureWithDeff(damage);
            // Nếu có khiên thì giảm thêm sát thương và có thể phá khiên
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
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
