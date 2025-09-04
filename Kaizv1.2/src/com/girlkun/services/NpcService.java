package com.girlkun.services;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcFactory;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.utils.Logger;

/**
 * Lớp NpcService quản lý các chức năng liên quan đến việc tương tác với NPC trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để tạo menu và hướng dẫn NPC.
 * 
 * @author Lucifer
 */
public class NpcService {

    /**
     * Thể hiện duy nhất của lớp NpcService (singleton pattern).
     */
    private static NpcService i;

    /**
     * Lấy thể hiện duy nhất của lớp NpcService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp NpcService.
     */
    public static NpcService gI() {
        if (i == null) {
            i = new NpcService();
        }
        return i;
    }

    /**
     * Tạo menu cho NPC Rồng Thiêng để người chơi tương tác.
     * 
     * @param player Người chơi tương tác với NPC.
     * @param indexMenu Chỉ số của menu.
     * @param npcSay Nội dung lời thoại của NPC.
     * @param menuSelect Các lựa chọn trong menu.
     */
    public void createMenuRongThieng(Player player, int indexMenu, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.RONG_THIENG, 123, npcSay, menuSelect);
    }

    /**
     * Tạo menu cho NPC Con Mèo với avatar cụ thể để người chơi tương tác.
     * 
     * @param player Người chơi tương tác với NPC.
     * @param indexMenu Chỉ số của menu.
     * @param avatar ID avatar của NPC.
     * @param npcSay Nội dung lời thoại của NPC.
     * @param menuSelect Các lựa chọn trong menu.
     */
    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.CON_MEO, avatar, npcSay, menuSelect);
    }

    /**
     * Tạo menu cho NPC Con Mèo với avatar và lưu trữ đối tượng liên quan.
     * 
     * @param player Người chơi tương tác với NPC.
     * @param indexMenu Chỉ số của menu.
     * @param avatar ID avatar của NPC.
     * @param npcSay Nội dung lời thoại của NPC.
     * @param menuSelect Các lựa chọn trong menu.
     * @param object Đối tượng liên quan đến người chơi.
     */
    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String[] menuSelect, Object object) {
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        createMenuConMeo(player, indexMenu, avatar, npcSay, menuSelect);
    }

    /**
     * Tạo menu chung cho NPC để hiển thị lời thoại và các lựa chọn.
     * 
     * @param player Người chơi tương tác với NPC.
     * @param indexMenu Chỉ số của menu.
     * @param npcTempId ID mẫu của NPC.
     * @param avatar ID avatar của NPC.
     * @param npcSay Nội dung lời thoại của NPC.
     * @param menuSelect Các lựa chọn trong menu.
     */
    private void createMenu(Player player, int indexMenu, byte npcTempId, int avatar, String npcSay, String... menuSelect) {
        if (player != null && player.iDMark != null) {
            Message msg;
            try {
                player.iDMark.setIndexMenu(indexMenu);
                msg = new Message(32);
                msg.writer().writeShort(npcTempId);
                msg.writer().writeUTF(npcSay);
                msg.writer().writeByte(menuSelect.length);
                for (String menu : menuSelect) {
                    msg.writer().writeUTF(menu);
                }
                if (avatar != -1) {
                    msg.writer().writeShort(avatar);
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(NpcService.class, e);
            }
        }
    }

    /**
     * Tạo hướng dẫn (tutorial) cho người chơi từ NPC Con Mèo.
     * 
     * @param player Người chơi nhận hướng dẫn.
     * @param avatar ID avatar của NPC.
     * @param npcSay Nội dung hướng dẫn của NPC.
     */
    public void createTutorial(Player player, int avatar, String npcSay) {
        Message msg;
        try {
            msg = new Message(38);
            msg.writer().writeShort(ConstNpc.CON_MEO);
            msg.writer().writeUTF(npcSay);
            if (avatar != -1) {
                msg.writer().writeShort(avatar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Lấy ID avatar của NPC dựa trên ID mẫu của NPC.
     * 
     * @param npcId ID mẫu của NPC.
     * @return ID avatar của NPC, hoặc 1139 nếu không tìm thấy.
     */
    public int getAvatar(int npcId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == npcId) {
                return npc.avartar;
            }
        }
        return 1139;
    }
}