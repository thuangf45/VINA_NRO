package com.girlkun.models.player;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.utils.Util;

/**
 * Lớp quản lý trạng thái hợp thể của người chơi trong game.
 * @author Lucifer
 */
public class Fusion {

    /** Thời gian hiệu lực của trạng thái hợp thể (mặc định 600,000ms). */
    public static final int TIME_FUSION = 600000;

    /** Người chơi sở hữu trạng thái hợp thể. */
    private Player player;

    /** Loại hợp thể của người chơi (ví dụ: Lương Long Nhất Thể). */
    public byte typeFusion;

    /** Thời điểm cuối cùng bắt đầu hợp thể. */
    public long lastTimeFusion;

    /**
     * Khởi tạo đối tượng Fusion cho một người chơi.
     *
     * @param player Người chơi sở hữu trạng thái hợp thể.
     */
    public Fusion(Player player) {
        this.player = player;
    }

    /**
     * Cập nhật trạng thái hợp thể, kiểm tra thời gian hiệu lực và hủy hợp thể nếu hết thời gian.
     */
    public void update() {
        if (typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE && Util.canDoWithTime(lastTimeFusion, TIME_FUSION)) {
            this.player.pet.unFusion();
        }
    }
    
    /**
     * Giải phóng tài nguyên của đối tượng Fusion.
     */
    public void dispose() {
        this.player = null;
    }
}