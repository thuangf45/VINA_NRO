package com.girlkun.models.boss.iboss;

import com.girlkun.models.player.Player;

/**
 * Interface định nghĩa các hành vi liên quan đến sự kiện khi boss chết trong game.
 * @author Lucifer
 */
public interface IBossDie {
    
    /**
     * Thực hiện một số hành động cụ thể khi boss chết, chẳng hạn như xử lý logic đặc biệt.
     * @param playerKill Người chơi đã tiêu diệt boss
     */
    void doSomeThing(Player playerKill);

    /**
     * Thông báo rằng boss đã chết tới các đối tượng hoặc hệ thống liên quan.
     * @param playerKill Người chơi đã tiêu diệt boss
     */
    void notifyDie(Player playerKill);

    /**
     * Xử lý phần thưởng cho người chơi khi tiêu diệt boss.
     * @param playerKill Người chơi đã tiêu diệt boss
     */
    void rewards(Player playerKill);

    /**
     * Xóa boss khỏi bản đồ sau khi chết.
     */
    void leaveMap();
}