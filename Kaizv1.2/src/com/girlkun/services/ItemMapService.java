package com.girlkun.services;

import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

/**
 * Lớp ItemMapService quản lý các chức năng liên quan đến vật phẩm trên bản đồ, bao gồm nhặt vật phẩm,
 * xóa vật phẩm, và gửi thông báo tới người chơi. Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class ItemMapService {

    /** Thể hiện duy nhất của lớp ItemMapService (singleton pattern) */
    private static ItemMapService i;

    /**
     * Lấy thể hiện duy nhất của lớp ItemMapService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp ItemMapService.
     */
    public static ItemMapService gI() {
        if (i == null) {
            i = new ItemMapService();
        }
        return i;
    }

    /**
     * Xử lý hành động nhặt vật phẩm trên bản đồ của người chơi.
     * 
     * @param player Người chơi thực hiện nhặt vật phẩm.
     * @param itemMapId ID của vật phẩm trên bản đồ.
     * @param isThuHut Kiểm tra xem nhặt vật phẩm có phải do tính năng thu hút hay không.
     */
    public void pickItem(Player player, int itemMapId, boolean isThuHut) {
        if (player == null || player.zone == null || player.iDMark == null) {
            return;
        }
        if (isThuHut || Util.canDoWithTime(player.iDMark.getLastTimePickItem(), 1000)) {
            player.zone.pickItem(player, itemMapId);
            player.iDMark.setLastTimePickItem(System.currentTimeMillis());
        }
    }

    /**
     * Xóa vật phẩm trên bản đồ và gửi thông báo biến mất tới tất cả người chơi trong khu vực.
     * 
     * @param itemMap Vật phẩm trên bản đồ cần xóa.
     */
    public void removeItemMapAndSendClient(ItemMap itemMap) {
        sendItemMapDisappear(itemMap);
        removeItemMap(itemMap);
    }

    /**
     * Gửi thông báo vật phẩm trên bản đồ biến mất tới tất cả người chơi trong khu vực.
     * 
     * @param itemMap Vật phẩm trên bản đồ cần thông báo biến mất.
     */
    public void sendItemMapDisappear(ItemMap itemMap) {
        Message msg;
        try {
            msg = new Message(-21);
            msg.writer().writeShort(itemMap.itemMapId);
            Service.gI().sendMessAllPlayerInMap(itemMap.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(ItemMapService.class, e);
        }
    }

    /**
     * Xóa vật phẩm khỏi bản đồ và giải phóng tài nguyên.
     * 
     * @param itemMap Vật phẩm trên bản đồ cần xóa.
     */
    public void removeItemMap(ItemMap itemMap) {
        itemMap.zone.removeItemMap(itemMap);
        itemMap.dispose();
    }

    /**
     * Kiểm tra xem vật phẩm có phải là ngọc rồng đen hay không.
     * 
     * @param tempId ID mẫu của vật phẩm.
     * @return true nếu là ngọc rồng đen, false nếu không.
     */
    public boolean isBlackBall(int tempId) {
        return tempId >= 372 && tempId <= 378;
    }

    /**
     * Kiểm tra xem vật phẩm có phải là ngọc rồng Namek hay không.
     * 
     * @param tempId ID mẫu của vật phẩm.
     * @return true nếu là ngọc rồng Namek, false nếu không.
     */
    public boolean isNamecBall(int tempId) {
        return tempId >= 353 && tempId <= 360;
    }
}