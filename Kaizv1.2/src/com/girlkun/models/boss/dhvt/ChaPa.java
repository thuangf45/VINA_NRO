package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss ChaPa trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class ChaPa extends BossDHVT {

    /** Constructor khởi tạo boss ChaPa với ID và dữ liệu từ BossesData */
    public ChaPa(Player player) throws Exception {
        super(BossID.CHA_PA, BossesData.CHA_PA);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}