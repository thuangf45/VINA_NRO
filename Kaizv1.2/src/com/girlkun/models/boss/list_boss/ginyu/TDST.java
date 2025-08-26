package com.girlkun.models.boss.list_boss.ginyu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * Đại diện cho đội Trưởng Tiểu Đội Sát Thủ (TDST) thuộc team Ginyu.
 * Boss này có nhiều cấp độ khác nhau, di chuyển, rời bản đồ sau một khoảng thời gian
 * và có khả năng rơi vật phẩm khi bị tiêu diệt.
 *
 * Các boss con bao gồm: Số 4, Số 3, Số 2, Số 1 và Tiểu Đội Trưởng.
 *
 * @author Lucifer
 */
public class TDST extends Boss {

    /** Thời điểm Boss tham gia bản đồ (dùng để tính thời gian tồn tại) */
    private long st;

    /**
     * Khởi tạo Boss TDST với dữ liệu từ BossesData.
     *
     * @throws Exception nếu dữ liệu không hợp lệ
     */
    public TDST() throws Exception {
        super(BossID.TDST, BossesData.SO_4, BossesData.SO_3, BossesData.SO_2, BossesData.SO_1, BossesData.TIEU_DOI_TRUONG);
    }

    /**
     * Di chuyển Boss đến vị trí (x, y).
     * Nếu level đặc biệt thì bỏ qua không di chuyển.
     *
     * @param x tọa độ X
     * @param y tọa độ Y
     */
    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 100000) {
            return;
        }
        super.moveTo(x, y);
    }

    /**
     * Xử lý phần thưởng khi người chơi hạ gục Boss.
     * Boss có tỉ lệ rơi Item ngẫu nhiên xuống bản đồ.
     *
     * @param plKill Người chơi hạ gục Boss
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Thông báo khi Boss xuất hiện trong bản đồ.
     * Nếu level đặc biệt thì không gửi thông báo.
     */
    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1000000000) {
            return;
        }
        super.notifyJoinMap();
    }

    /**
     * Khi Boss tham gia bản đồ, lưu lại thời điểm bắt đầu.
     */
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    /**
     * Hành vi của Boss khi đang hoạt động trên bản đồ.
     * Nếu đã tồn tại quá lâu thì Boss sẽ rời khỏi bản đồ.
     */
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
}
