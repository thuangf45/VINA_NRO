package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss ChanXu trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class ChanXu extends BossDHVT {

    /** Constructor khởi tạo boss ChanXu với ID và dữ liệu từ BossesData */
    public ChanXu(Player player) throws Exception {
        super(BossID.CHAN_XU, BossesData.CHAN_XU);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}