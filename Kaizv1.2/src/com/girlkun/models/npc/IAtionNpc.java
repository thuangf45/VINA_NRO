package com.girlkun.models.npc;

import com.girlkun.models.player.Player;

/**
 * Giao diện định nghĩa các hành động của NPC trong game
 * @author Lucifer
 */
public interface IAtionNpc {

    /** Mở menu cơ bản của NPC cho người chơi */
    void openBaseMenu(Player player);

    /** Xác nhận lựa chọn menu của người chơi */
    void confirmMenu(Player player, int select);
}