package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * Lớp đại diện cho boss TauPayPay trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class TauPayPay extends BossDHVT {

    /** Constructor khởi tạo boss TauPayPay với ID và dữ liệu từ BossesData */
    public TauPayPay(Player player) throws Exception {
        super(BossID.TAU_PAY_PAY, BossesData.TAU_PAY_PAY);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }
}