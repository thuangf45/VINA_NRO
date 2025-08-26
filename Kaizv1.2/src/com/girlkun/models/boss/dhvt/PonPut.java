package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss PonPut trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class PonPut extends BossDHVT {

    /** Constructor khởi tạo boss PonPut với ID và dữ liệu từ BossesData */
    public PonPut(Player player) throws Exception {
        super(BossID.PON_PUT, BossesData.PON_PUT);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}