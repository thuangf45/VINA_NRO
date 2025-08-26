package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss SoiHecQuyn trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class SoiHecQuyn extends BossDHVT {

    /** Constructor khởi tạo boss SoiHecQuyn với ID và dữ liệu từ BossesData */
    public SoiHecQuyn(Player player) throws Exception {
        super(BossID.SOI_HEC_QUYN, BossesData.SOI_HEC_QUYN);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}