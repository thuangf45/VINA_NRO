package com.girlkun.models.boss.list_boss;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss Nhân Bản trong game.
 * @author Lucifer
 */
public class NhanBan extends Boss {

    /**
     * Constructor khởi tạo boss Nhân Bản với ID, dữ liệu boss và khu vực.
     * @param bossID ID của boss
     * @param bossData Dữ liệu cấu hình của boss
     * @param zone Khu vực mà boss xuất hiện
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public NhanBan(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss Nhân Bản.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        if (!plKill.getSession().actived) {
            Service.gI().sendThongBao(plKill, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
        } else {
            int[] itemIds = {1099, 1100, 1101, 1102, 1103, 1978, 1235, 1234, 1233}; // Các ID của vật phẩm cần rơi
            for (int itemId : itemIds) {
                ItemMap it = new ItemMap(this.zone, itemId, Util.nextInt(1, 2), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plKill.id);
                Service.gI().dropItemMap(this.zone, it);
            }
        }
    }

    /**
     * Kích hoạt hành vi của boss Nhân Bản.
     */
    @Override
    public void active() {
        super.active();
    }

    /**
     * Thêm boss Nhân Bản vào bản đồ.
     */
    @Override
    public void joinMap() {
        super.joinMap();
    }

    /**
     * Xóa boss Nhân Bản khỏi bản đồ và thực hiện dọn dẹp tài nguyên.
     */
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}

