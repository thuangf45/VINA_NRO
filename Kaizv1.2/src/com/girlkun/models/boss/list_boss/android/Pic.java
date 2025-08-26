package com.girlkun.models.boss.list_boss.android;

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
 * Boss Pic (thuộc nhóm Android/Poc).
 *
 * Đặc điểm:
 * - Tỉ lệ rơi đồ: 30% rơi item ID 16 (số lượng 1).
 * - Thời gian tồn tại trên bản đồ tối đa: 15 phút (900.000ms).
 * - Khi chat kết thúc (doneChatE):
 *   + Nếu POC còn sống trong nhóm → chuyển POC sang trạng thái PK.
 *
 * Phần thưởng:
 * - Item sẽ rơi ở vị trí boss chết trên bản đồ.
 * - Gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ của người chơi.
 *
 * @author Lucifer
 */
public class Pic extends Boss {

    /** Thời gian Pic xuất hiện trên bản đồ */
    private long st;

    /**
     * Khởi tạo boss Pic.
     *
     * @throws Exception nếu lỗi load dữ liệu
     */
    public Pic() throws Exception {
        super(BossID.PIC, BossesData.PIC);
    }

    /**
     * Phần thưởng khi hạ gục Pic.
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
     * Khi kết thúc đoạn chat E → nếu boss POC còn sống trong nhóm thì chuyển sang trạng thái PK.
     */
    @Override
    public void doneChatE() {
        if (this.parentBoss == null
                || this.parentBoss.bossAppearTogether == null
                || this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.POC && !boss.isDie()) {
                boss.changeToTypePK();
                break;
            }
        }
    }

    /**
     * Khi Pic tham gia bản đồ → lưu lại thời gian xuất hiện.
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
}
