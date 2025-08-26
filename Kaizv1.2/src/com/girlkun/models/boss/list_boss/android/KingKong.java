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
 * Boss KingKong (Khỉ khổng lồ).
 *
 * Đặc điểm:
 * - Thưởng rơi đồ có tỉ lệ 50%:
 *   + 50%: rơi item ID 16 (số lượng 1).
 *   + 50%: rơi item ID 15 (số lượng 1).
 * - Khi xuất hiện trên bản đồ → tính giờ sống tối đa 15 phút (900.000ms).
 * - Sau 15 phút nếu còn sống sẽ rời khỏi bản đồ ({@link BossStatus#LEAVE_MAP}).
 *
 * Phần thưởng:
 * - Khi chết, item rơi tại vị trí của boss trên bản đồ.
 * - Đồng thời gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ người chơi.
 *
 * Các đoạn code bị comment giữ nguyên (doneChatS → liên kết với Boss POC).
 *
 * @author Lucifer
 */
public class KingKong extends Boss {

    /** Thời gian KingKong bắt đầu join map */
    private long st;

    /**
     * Khởi tạo boss KingKong.
     *
     * @throws Exception nếu lỗi load dữ liệu
     */
    public KingKong() throws Exception {
        super(BossID.KING_KONG, BossesData.KING_KONG);
    }

    /**
     * Phần thưởng khi hạ gục KingKong.
     *
     * @param plKill người chơi hạ boss
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(50, 100)) { // 50% tỉ lệ rơi item 16
            ItemMap it = new ItemMap(this.zone, 16, 1,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        } else { // 50% tỉ lệ rơi item 15
            ItemMap it = new ItemMap(this.zone, 15, 1,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Khi KingKong tham gia bản đồ → đánh dấu thời gian xuất hiện.
     */
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    /**
     * Hành vi hoạt động của KingKong:
     * - Nếu sống quá 15 phút → tự động rời map.
     */
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) { // 900000ms = 15 phút
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

//    /**
//     * Khi KingKong chat xong → nếu boss POC còn sống thì chuyển POC sang trạng thái PK.
//     */
//    @Override
//    public void doneChatS() {
//        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
//            return;
//        }
//        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
//            if(boss.id == BossID.POC && !boss.isDie()){
//                boss.changeToTypePK();
//                break;
//            }
//        }
//    }
}
