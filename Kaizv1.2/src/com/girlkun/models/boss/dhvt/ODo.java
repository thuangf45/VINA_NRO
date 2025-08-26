package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss ODo trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class ODo extends BossDHVT {

    /** Constructor khởi tạo boss ODo với ID và dữ liệu từ BossesData */
    public ODo(Player player) throws Exception {
        super(BossID.O_DO, BossesData.O_DO);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}