package com.girlkun.models.npc;

import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.TaskService;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý các NPC trong game.
 * <p>
 * Class này cung cấp các phương thức để truy xuất NPC theo id, theo map, 
 * hoặc lấy danh sách NPC liên quan đến một người chơi.
 * </p>
 * @author Lucifer
 */
public class NpcManager {

    /**
     * Lấy NPC theo id và mapId.
     * 
     * @param id id tạm của NPC (tempId)
     * @param mapId id của bản đồ mà NPC đang xuất hiện
     * @return NPC nếu tồn tại, ngược lại trả về null
     */
    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Lấy NPC theo tempId.
     * 
     * @param tempId id tạm của NPC
     * @return NPC nếu tồn tại, ngược lại trả về null
     */
    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Lấy danh sách NPC có trong map của người chơi.
     * <p>
     * Một số NPC sẽ bị loại bỏ khỏi danh sách nếu điều kiện game không thỏa, 
     * ví dụ NPC "QUA_TRUNG" khi người chơi chưa có trứng Mabu, hoặc NPC "CALICK" 
     * khi người chơi chưa đạt task yêu cầu.
     * </p>
     * 
     * @param player đối tượng người chơi
     * @return danh sách NPC hiện có trong map của người chơi
     */
    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null && player.zone.map.mapId == (21 + player.gender)) {
                    continue;
                } else if (npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    continue;
                }
                list.add(npc);
            }
        }
        return list;
    }
}
