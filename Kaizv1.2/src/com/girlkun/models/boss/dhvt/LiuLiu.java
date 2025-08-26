package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss LiuLiu trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class LiuLiu extends BossDHVT {

    /** Constructor khởi tạo boss LiuLiu với ID và dữ liệu từ BossesData */
    public LiuLiu(Player player) throws Exception {
        super(BossID.LIU_LIU, BossesData.LIU_LIU);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}