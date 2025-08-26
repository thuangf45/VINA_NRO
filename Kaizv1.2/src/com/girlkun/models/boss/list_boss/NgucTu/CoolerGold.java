/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * Boss Cooler Vàng (Cooler Gold) trong Ngục Tù.
 *
 * Đặc điểm:
 * - Dữ liệu khởi tạo lấy từ {@link BossesData#COOLER_GOLD}.
 * - Tồn tại trong map tối đa 15 phút, sau đó tự rời đi.
 * - Khi rời map sẽ bị xóa khỏi {@link BossManager}.
 *
 * Phần thưởng khi bị hạ gục:
 * - Có 15% cơ hội rơi ra item trong danh sách itemDos, trong đó có tỉ lệ nhỏ (1/50) rơi item đặc biệt ID 561.
 * - Có 50% cơ hội rơi ra Ngọc Rồng (ID 16 hoặc 17).
 *
 * @author Lucifer
 */
public class CoolerGold extends Boss {

    /** Thời điểm boss tham gia map (ms) */
    private long st;

    /**
     * Khởi tạo boss Cooler Gold với ID định danh trong {@link BossID}.
     *
     * @throws Exception nếu có lỗi khi khởi tạo
     */
    public CoolerGold() throws Exception {
        super(BossID.COOLER_GOLD, BossesData.COOLER_GOLD);
    }

    /**
     * Khi boss tham gia map, lưu lại thời điểm xuất hiện để tính thời gian tồn tại.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /**
     * Vòng lặp hoạt động: nếu tồn tại quá 15 phút sẽ rời map.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) { // 15 phút = 900.000ms
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt boss.
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{1142, 1142, 1117, 1142, 1142}; // Danh sách vật phẩm có thể rơi
        int[] NRs = new int[]{17, 16}; // Ngọc Rồng (sao 6 và sao 7)
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);

        // Xác suất rơi item trong danh sách itemDos (15%)
        if (Util.isTrue(15, 100)) {
            // 1/50 trong số đó có thể rơi item đặc biệt ID 561
            if (Util.isTrue(1, 50)) {
                Service.gI().dropItemMap(this.zone,
                        Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone,
                    Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
        // Nếu không rơi đồ thì 50% cơ hội rơi Ngọc Rồng (ID 16 hoặc 17)
        else if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone,
                    new ItemMap(zone, NRs[randomNR], 1, this.location.x,
                            zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }

    /**
     * Khi boss rời map, xóa khỏi {@link BossManager} và giải phóng bộ nhớ.
     */
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        super.dispose();
    }
}
