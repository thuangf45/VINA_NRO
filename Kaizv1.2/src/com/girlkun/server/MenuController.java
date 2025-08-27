package com.girlkun.server;

import java.io.IOException;
import com.girlkun.consts.ConstNpc;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.server.io.MySession;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.func.TransactionService;

/**
 * Lớp MenuController quản lý các tương tác menu giữa người chơi và NPC trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất, 
 * chịu trách nhiệm mở menu NPC và xử lý lựa chọn menu từ người chơi.
 * 
 * @author Lucifer
 */
public class MenuController {

    /**
     * Thể hiện duy nhất của lớp MenuController (singleton pattern).
     */
    private static MenuController instance;

    /**
     * Lấy thể hiện duy nhất của lớp MenuController.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp MenuController.
     */
    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    /**
     * Mở menu cơ bản của NPC cho người chơi.
     * Hủy giao dịch hiện tại của người chơi (nếu có) trước khi mở menu.
     * 
     * @param session Phiên kết nối của client.
     * @param idnpc ID của NPC cần mở menu.
     * @param player Người chơi tương tác với NPC.
     */
    public void openMenuNPC(MySession session, int idnpc, Player player) {
        TransactionService.gI().cancelTrade(player);
        Npc npc = null;
        if (idnpc == ConstNpc.CALICK && player.zone.map.mapId != 102) {
            npc = NpcManager.getNpc(ConstNpc.CALICK);
        } else {
            npc = player.zone.map.getNpc(player, idnpc);
        }
        if (npc != null) {
            npc.openBaseMenu(player);
        } else {
            Service.gI().hideWaitDialog(player);
        }
    }

    /**
     * Xử lý lựa chọn menu của người chơi khi tương tác với NPC.
     * Hủy giao dịch hiện tại (nếu có) và gọi hàm xác nhận lựa chọn của NPC tương ứng.
     * 
     * @param player Người chơi thực hiện lựa chọn.
     * @param npcId ID của NPC mà người chơi đang tương tác.
     * @param select Lựa chọn menu của người chơi.
     * @throws IOException Nếu có lỗi khi xử lý lựa chọn menu.
     */
    public void doSelectMenu(Player player, int npcId, int select) throws IOException {
        TransactionService.gI().cancelTrade(player);
        switch (npcId) {
            case ConstNpc.RONG_THIENG:
            case ConstNpc.CON_MEO:
                NpcManager.getNpc((byte) npcId).confirmMenu(player, select);
                break;
            default:
                Npc npc = null;
                if (npcId == ConstNpc.CALICK && player.zone.map.mapId != 102) {
                    npc = NpcManager.getNpc(ConstNpc.CALICK);
                } else {
                    npc = player.zone.map.getNpc(player, npcId);
                }
                if (npc != null) {
                    npc.confirmMenu(player, select);
                } else {
                    Service.gI().hideWaitDialog(player);
                }
                break;
        }
    }
}