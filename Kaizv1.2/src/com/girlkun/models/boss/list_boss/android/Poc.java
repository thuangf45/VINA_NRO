package com.girlkun.models.boss.list_boss.android;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Boss Poc (thuộc nhóm Android/Pic).
 *
 * Đặc điểm:
 * - Tỉ lệ rơi đồ: 30% rơi item ID 16 (số lượng 1).
 * - Thời gian tồn tại trên bản đồ tối đa: 15 phút (900.000ms).
 * - Khi biến mất:
 *   + Nếu parentBoss (thường là Pic) vẫn còn sống → parentBoss chuyển sang trạng thái PK.
 *
 * Phần thưởng:
 * - Item sẽ rơi tại vị trí boss chết.
 * - Gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ của người chơi.
 *
 * @author Lucifer
 */
public class Poc extends Boss {

    /** Thời gian Poc xuất hiện trên bản đồ */
    private long st;

    /**
     * Khởi tạo boss Poc.
     *
     * @throws Exception nếu lỗi load dữ liệu
     */
    public Poc() throws Exception {
        super(BossID.POC, BossesData.POC);
    }

    /**
     * Phần thưởng khi hạ gục Poc.
     *
     * @param plKill người chơi hạ boss
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(30, 100)) { // 30% tỉ lệ rơi item 16
            ItemMap it = new ItemMap(this.zone, 16, 1,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Khi Poc tham gia bản đồ → lưu lại thời gian xuất hiện.
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
     * Khi Poc biến mất:
     * - Nếu parentBoss còn sống thì parentBoss sẽ chuyển sang trạng thái PK.
     */
    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss != null && !this.parentBoss.isDie()) {
            this.parentBoss.changeToTypePK();
        }
    }
}
