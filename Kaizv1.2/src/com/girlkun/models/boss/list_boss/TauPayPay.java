package com.girlkun.models.boss.list_boss;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss Tàu Pảy Pảy trong game.
 * @author Lucifer
 */
public class TauPayPay extends Boss {

    /**
     * Constructor khởi tạo boss Tàu Pảy Pảy với ID và dữ liệu từ BossesData.
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public TauPayPay() throws Exception {
        super(BossID.TAU_PAY_PAY_M, BossesData.TAU_PAY_PAY_BASE);
    }

    /**
     * Kích hoạt hành vi của boss Tàu Pảy Pảy.
     */
    @Override
    public void active() {
        super.active();
    }

    /**
     * Xử lý sát thương mà boss Tàu Pảy Pảy nhận từ người chơi hoặc quái.
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
                damage = damage / 2;
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

    /**
     * Thêm boss Tàu Pảy Pảy vào bản đồ.
     */
    @Override
    public void joinMap() {
        super.joinMap();
    }
}