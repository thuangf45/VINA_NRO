package com.girlkun.models.boss.list_boss.FideBack;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * Lớp đại diện cho boss King Cold trong game.
 * Boss có phần thưởng đa dạng khi bị tiêu diệt.
 * 
 * @author Lucifer
 */
public class Kingcold extends Boss {

    /**
     * Constructor khởi tạo boss King Cold với ID và dữ liệu từ BossesData.
     * 
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public Kingcold() throws Exception {
        super(BossID.VUA_COLD, BossesData.VUA_COLD);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt King Cold.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
        int[] NRs = new int[]{15, 16};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(15, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }
}
