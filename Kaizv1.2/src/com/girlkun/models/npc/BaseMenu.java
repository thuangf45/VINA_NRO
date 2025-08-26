package com.girlkun.models.npc;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;

/**
 * Lớp quản lý menu cơ bản của NPC trong game
 * @author Lucifer
 */
public class BaseMenu {

    /** ID của NPC */
    public int npcId;

    /** Lời thoại của NPC */
    public String npcSay;

    /** Mảng chứa các lựa chọn menu */
    public String[] menuSelect;

    /** Hiển thị menu cho người chơi */
    public void openMenu(Player player) {
        Message msg;
        try {
            /** Tạo tin nhắn mới với mã lệnh 32 */
            msg = new Message(32);
            /** Ghi ID của NPC */
            msg.writer().writeShort(npcId);
            /** Ghi lời thoại của NPC */
            msg.writer().writeUTF(npcSay);
            /** Ghi số lượng lựa chọn menu */
            msg.writer().writeByte(menuSelect.length);
            /** Ghi từng lựa chọn menu */
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            /** Gửi tin nhắn đến người chơi */
            player.sendMessage(msg);
            /** Giải phóng tài nguyên */
            msg.cleanup();
        } catch (Exception e) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }
}