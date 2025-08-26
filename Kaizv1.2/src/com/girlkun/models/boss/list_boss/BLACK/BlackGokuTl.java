package com.girlkun.models.boss.list_boss.BLACK;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss Black Goku TL trong game.
 * @author Lucifer
 */
public class BlackGokuTl extends Boss {

    /**
     * Constructor khởi tạo boss Black Goku TL với ID và dữ liệu từ BossesData.
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public BlackGokuTl() throws Exception {
        super(BossID.BLACK1, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss Black Goku TL.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        rewardFutureBoss(plKill);
    }

    /**
     * Xử lý sát thương mà boss Black Goku TL nhận từ người chơi hoặc quái.
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
                damage = 1;
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
     * Kích hoạt hành vi của boss Black Goku TL, bao gồm kiểm tra thời gian để rời bản đồ.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000000)) {
             this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Thêm boss Black Goku TL vào bản đồ và ghi lại thời gian bắt đầu.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /**
     * Thời gian bắt đầu khi boss tham gia bản đồ.
     */
    private long st;

    /**
     * Di chuyển boss Black Goku TL đến tọa độ cụ thể (bị vô hiệu hóa nếu ở cấp độ 1).
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    /*
    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }
    */

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss (bị vô hiệu hóa nếu ở cấp độ 1).
     * @param plKill Người chơi đã tiêu diệt boss
     */
    /*
    @Override
    public void reward(Player plKill) {
        if (this.currentLevel == 1) {
            return;
        }
        super.reward(plKill);
    }
    */

    /**
     * Thông báo khi boss Black Goku TL tham gia bản đồ (bị vô hiệu hóa nếu ở cấp độ 1).
     */
    /*
    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }
    */
}