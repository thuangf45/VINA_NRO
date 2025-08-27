package com.girlkun.models.player;

import com.girlkun.services.MapService;
import com.girlkun.services.Service;

/**
 * Lớp quản lý điểm số và trạng thái của người chơi trong sự kiện đấu trường Ma Bư.
 * @author Lucifer
 */
public class FightMabu {
    
    /** Điểm tối đa có thể đạt được trong đấu trường Ma Bư. */
    public final byte POINT_MAX = 20;

    /** Điểm số hiện tại của người chơi trong đấu trường Ma Bư. */
    public int pointMabu = 0;

    /** Người chơi tham gia đấu trường Ma Bư. */
    private Player player;

    /**
     * Khởi tạo đối tượng FightMabu cho một người chơi.
     *
     * @param player Người chơi tham gia đấu trường.
     */
    public FightMabu(Player player) {
        this.player = player;
    }

    /**
     * Thay đổi điểm số của người chơi trong đấu trường Ma Bư.
     *
     * @param pointAdd Số điểm cần cộng thêm.
     */
    public void changePoint(byte pointAdd) {
        if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
            pointMabu += pointAdd;
            if (pointMabu >= POINT_MAX) {
                Service.gI().sendThongBao(player, "Bạn đã đủ điểm lên tầng tiếp theo");
            }
        }
    }

    /**
     * Đặt lại điểm số của người chơi về 0.
     */
    public void clear() {
        this.pointMabu = 0;
    }
}