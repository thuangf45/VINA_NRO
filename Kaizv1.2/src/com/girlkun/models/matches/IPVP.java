package com.girlkun.models.matches;

import com.girlkun.models.player.Player;

/**
 * Interface IPVP định nghĩa các phương thức quản lý trận đấu PvP trong trò chơi.
 * @author Lucifer
 */
public interface IPVP {

    /**
     * Bắt đầu trận đấu PvP.
     */
    void start();

    /**
     * Kết thúc trận đấu PvP.
     */
    void finish();

    /**
     * Giải phóng tài nguyên của trận đấu PvP.
     */
    void dispose();

    /**
     * Cập nhật trạng thái của trận đấu PvP.
     */
    void update();

    /**
     * Trao phần thưởng cho người chơi chiến thắng.
     * @param plWin Người chơi chiến thắng.
     */
    void reward(Player plWin);

    /**
     * Gửi kết quả trận đấu cho người chơi thua cuộc.
     * @param plLose Người chơi thua cuộc.
     * @param typeLose Loại thua cuộc (TYPE_LOSE_PVP).
     */
    void sendResult(Player plLose, TYPE_LOSE_PVP typeLose);

    /**
     * Xử lý khi người chơi thua cuộc.
     * @param plLose Người chơi thua cuộc.
     * @param typeLose Loại thua cuộc (TYPE_LOSE_PVP).
     */
    void lose(Player plLose, TYPE_LOSE_PVP typeLose);

    /**
     * Kiểm tra xem người chơi có đang tham gia trận đấu PvP hay không.
     * @param pl Người chơi cần kiểm tra.
     * @return true nếu người chơi đang trong trận PvP, false nếu không.
     */
    boolean isInPVP(Player pl);
}