package com.girlkun.models.boss.list_boss.Mabu12h;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.Random;

/**
 * Lớp đại diện cho boss Bui Bui trong sự kiện Mabu 12h.
 * Boss có phần thưởng đa dạng bao gồm đồ TL, NR, và đồ cấp 12.
 * Ngoài ra, tiêu diệt boss sẽ cộng điểm sự kiện cho người chơi.
 * 
 * @author Lucifer
 */
public class BuiBui extends Boss {

    /**
     * Constructor khởi tạo boss Bui Bui với ID ngẫu nhiên và dữ liệu từ BossesData.
     * 
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public BuiBui() throws Exception {
        super(Util.randomBossId(), BossesData.BUI_BUI);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt Bui Bui.
     * Người chơi có thể nhận đồ TL, đồ cấp 12, hoặc ngọc rồng.
     * Đồng thời, người chơi được cộng điểm sự kiện Mabu.
     * 
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        if (Util.isTrue(1, 130)) {
            if (Util.isTrue(1, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1142, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(Util.RaitiDoc12(zone, Manager.itemDC12[randomc12], 1, this.location.x, this.location.y, plKill.id)));
            return;
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint((byte) 40);
    }

    /**
     * Kích hoạt hành vi của boss Bui Bui (đã bị vô hiệu hóa).
     * Boss sẽ rời bản đồ sau 5 phút nếu không bị tiêu diệt.
     */
    /*
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 300000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    */

    /**
     * Thêm Bui Bui vào bản đồ và ghi lại thời gian bắt đầu (đã bị vô hiệu hóa).
     */
    /*
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }
    private long st;
    */
}
