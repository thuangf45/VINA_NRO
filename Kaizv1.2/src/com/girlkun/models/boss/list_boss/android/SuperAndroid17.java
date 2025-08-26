package com.girlkun.models.boss.list_boss.android;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Boss Super Android 17.
 *
 * Đặc điểm:
 * - Có chỉ số phòng thủ (defg) được tính theo HP/1000.
 * - Tỉ lệ rơi đồ: 15% rơi item ID 1142 (số lượng 1).
 * - Thời gian tồn tại trên bản đồ tối đa: 15 phút (900.000ms).
 * - Khi rời bản đồ: tự hủy, giải phóng bộ nhớ thông qua {@link BossManager#removeBoss(Boss)}.
 *
 * Cơ chế chiến đấu:
 * - Có khả năng né đòn dựa vào {@code tlNeDon}.
 * - Có khiên (shield), nếu nhận sát thương > HP tối đa thì khiên sẽ bị vỡ.
 * - Sát thương nhận vào được giảm bởi phòng thủ (def).
 *
 * @author Lucifer
 */
public class SuperAndroid17 extends Boss {

    /** Thời gian xuất hiện trên bản đồ */
    private long st;

    /**
     * Khởi tạo Super Android 17 với dữ liệu từ {@link BossesData}.
     *
     * @throws Exception nếu lỗi load dữ liệu
     */
    public SuperAndroid17() throws Exception {
        super(BossID.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
        this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
        }
    }

    /**
     * Phần thưởng khi hạ gục Super Android 17.
     *
     * @param plKill người chơi hạ boss
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) { // 15% tỉ lệ rơi item 1142
            ItemMap it = new ItemMap(this.zone, 1142, 1,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    /**
     * Xử lý khi boss nhận sát thương.
     *
     * @param plAtt       người tấn công
     * @param damage      sát thương gây ra
     * @param piercing    có xuyên giáp không
     * @param isMobAttack có phải mob tấn công không
     * @return sát thương thực tế nhận vào
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

            // Giảm sát thương theo thủ
            damage = this.nPoint.subDameInjureWithDeff(damage);

            // Kiểm tra khiên chắn
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }

            // Trừ máu
            this.nPoint.subHP(damage);

            // Nếu chết → gọi xử lý die
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
     * Khi boss tham gia bản đồ → lưu lại thời gian xuất hiện.
     */
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    /**
     * Hành vi hoạt động:
     * - Kiểm tra nếu tồn tại quá 15 phút thì boss tự rời map.
     */
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) { // 900000ms = 15 phút
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Khi boss rời bản đồ:
     * - Gọi removeBoss trong {@link BossManager}.
     * - Giải phóng bộ nhớ bằng dispose().
     */
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
