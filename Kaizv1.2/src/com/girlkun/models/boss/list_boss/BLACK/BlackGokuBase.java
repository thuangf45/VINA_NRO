package com.girlkun.models.boss.list_boss.BLACK;

import com.girlkun.models.boss.*;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss Super Black Goku trong game.
 * @author Lucifer
 */
public class BlackGokuBase extends Boss {

    /**
     * Constructor khởi tạo boss Super Black Goku với ID và dữ liệu từ BossesData.
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public BlackGokuBase() throws Exception {
        super(BossID.BLACK3, BossesData.SUPER_BLACK_GOKU);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss Super Black Goku.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        rewardFutureBoss(plKill);
    }

    /**
     * Xử lý sát thương mà boss Super Black Goku nhận từ người chơi hoặc quái.
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
     * Thêm boss Super Black Goku vào bản đồ và ghi lại thời gian bắt đầu.
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
     * Kích hoạt hành vi của boss Super Black Goku, bao gồm tự động rời bản đồ sau 15 phút.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Di chuyển boss Super Black Goku đến tọa độ cụ thể (bị vô hiệu hóa nếu ở cấp độ 1).
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
     * Thông báo khi boss Super Black Goku tham gia bản đồ (bị vô hiệu hóa nếu ở cấp độ 1).
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