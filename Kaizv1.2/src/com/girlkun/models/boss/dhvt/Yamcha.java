package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss JackyChun trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class JackyChun extends BossDHVT {

    /**
     * Constructor khởi tạo boss JackyChun với ID và dữ liệu từ BossesData
     * @param player Người chơi mục tiêu mà boss sẽ tấn công
     */
    public JackyChun(Player player) throws Exception {
        super(BossID.JACKY_CHUN, BossesData.JACKY_CHUN);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}