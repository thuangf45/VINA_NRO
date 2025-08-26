package com.girlkun.models.boss.list_boss.TauHuyDiet;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Boss Thiên Sứ Whis (xuất hiện trong Tàu Hủy Diệt).
 *
 * Đặc điểm:
 * - Khởi tạo với dữ liệu từ {@link BossesData#THIEN_SU_WHIS}.
 * - Có khả năng né đòn và sử dụng khiên chắn giống các boss khác.
 *
 * Cơ chế chiến đấu:
 * - Tỉ lệ né đòn dựa theo {@code tlNeDon}.
 * - Nếu có khiên chắn, sát thương nhận vào giảm một nửa.
 *   + Khi đòn đánh vượt quá HP tối đa thì khiên sẽ vỡ.
 * - Khi chết, sẽ gọi hàm {@link #reward(Player)} để rơi thưởng.
 *
 * Cơ chế phần thưởng:
 * - Gọi {@link #rewardFutureBoss(Player)} để xử lý rơi đồ định nghĩa sẵn trong Boss cha.
 *
 * Lưu ý:
 * - Code có sẵn phần xử lý vòng đời (active/joinMap) nhưng đang bị comment lại.
 *   Nếu bật lại thì boss sẽ tự rời map sau ~1000s kể từ khi xuất hiện.
 *
 * @author 
 *  - Code gốc: team dev
 *  - JavaDoc & chú thích: Lucifer
 */
public class ThienSuWhis extends Boss {

    /**
     * Khởi tạo Thiên Sứ Whis với dữ liệu từ BossesData.
     *
     * @throws Exception nếu có lỗi khi khởi tạo boss
     */
    public ThienSuWhis() throws Exception {
        super(Util.randomBossId(), BossesData.THIEN_SU_WHIS);
    }

    /**
     * Xử lý phần thưởng khi boss bị tiêu diệt.
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        rewardFutureBoss(plKill);
    }

    /**
     * Xử lý sát thương boss nhận từ người chơi hoặc quái.
     *
     * @param plAtt       Người tấn công
     * @param damage      Sát thương gây ra
     * @param piercing    Có xuyên giáp hay không
     * @param isMobAttack Có phải đòn từ quái không
     * @return sát thương thực tế boss nhận vào
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

            // Trừ sát thương theo giáp
            damage = this.nPoint.subDameInjureWithDeff(damage);

            // Khiên chắn: giảm 1/2 sát thương
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }

            // Trừ máu boss
            this.nPoint.subHP(damage);

            // Nếu boss chết thì xử lý rơi đồ
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

//    @Override
//    public void active() {
//        super.active(); //To change body of generated methods, choose Tools | Templates.
//        if (Util.canDoWithTime(st, 1000000)) {
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//        st = System.currentTimeMillis();
//    }
//    private long st;
}
