package com.girlkun.models.boss.list_boss.fide;

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
 * Fide - Chuỗi boss Fide Đại Ca (1 -> 3)
 * Quản lý logic xuất hiện, phần thưởng, vòng đời boss
 * 
 * @author Lucifer
 */
public class Fide extends Boss {

    private long st; // thời điểm vào map

    /**
     * Khởi tạo boss Fide với 3 cấp độ
     * @throws Exception lỗi khởi tạo
     */
    public Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }

    /**
     * Rơi vật phẩm + kiểm tra nhiệm vụ khi bị tiêu diệt
     * @param plKill người chơi hạ gục
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x, 
                this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Boss tham gia bản đồ, bắt đầu tính thời gian sống
     */
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    /**
     * Hoạt động của boss (AI vòng đời)
     * - Sau 15 phút (900.000ms) boss sẽ rời bản đồ
     */
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
}
