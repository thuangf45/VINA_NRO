package com.girlkun.models.map;

import com.girlkun.models.player.Player;
import com.girlkun.services.PlayerService;
import com.girlkun.services.func.EffectMapService;
import com.girlkun.utils.Util;

/**
 * Đại diện cho các bẫy (trap) trên bản đồ có thể gây hiệu ứng hoặc sát thương cho người chơi
 * 
 * @author Lucifer
 */
public class TrapMap {

    /** Tọa độ X của bẫy */
    public int x;

    /** Tọa độ Y của bẫy */
    public int y;

    /** Chiều rộng của bẫy */
    public int w;

    /** Chiều cao của bẫy */
    public int h;

    /** ID hiệu ứng của bẫy */
    public int effectId;

    /** Lượng sát thương cơ bản của bẫy */
    public int dame;

    /**
     * Xử lý khi người chơi va chạm với bẫy
     *
     * @param player Người chơi bị tác động bởi bẫy
     */
    public void doPlayer(Player player) {
        switch (this.effectId) {
            case 49:
                if (!player.isDie() && Util.canDoWithTime(player.iDMark.getLastTimeAnXienTrapBDKB(), 1000) && !player.isBoss) {
                    int increasedDame = dame * 2; // Tăng lượng dame lên 100%
                    player.injured(null, increasedDame + (Util.nextInt(-10, 10) * increasedDame / 100), false, false);
                    PlayerService.gI().sendInfoHp(player);
                    EffectMapService.gI().sendEffectMapToAllInMap(player.zone, effectId, 2, 1, player.location.x - 32, 1040, 1);
                    player.iDMark.setLastTimeAnXienTrapBDKB(System.currentTimeMillis());
                }
                break;
        }
    }

}