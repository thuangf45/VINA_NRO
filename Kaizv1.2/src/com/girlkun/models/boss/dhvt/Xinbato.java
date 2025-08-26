package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss Xinbato trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class Xinbato extends BossDHVT {

    /**
     * Constructor khởi tạo boss Xinbato với ID và dữ liệu từ BossesData
     * @param player Người chơi mục tiêu mà boss sẽ tấn công
     */
    public Xinbato(Player player) throws Exception {
        super(BossID.XINBATO, BossesData.XINBATO);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}