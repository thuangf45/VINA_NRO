package com.girlkun.models.map;

import java.util.ArrayList;
import java.util.List;
import com.girlkun.models.Template.ItemTemplate;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Util;
import com.girlkun.services.ItemMapService;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;

/**
 * Đại diện cho một vật phẩm xuất hiện trên bản đồ (ItemMap).
 */
public class ItemMap {

    /** Khu vực chứa item */
    public Zone zone;

    /** ID duy nhất của item trên bản đồ */
    public int itemMapId;

    /** Template của item */
    public ItemTemplate itemTemplate;

    /** Số lượng item */
    public int quantity;

    /** Tọa độ X trên bản đồ */
    public int x;

    /** Tọa độ Y trên bản đồ */
    public int y;

    /** ID người chơi nhặt được item (nếu có) */
    public long playerId;

    /** Các tùy chọn của item */
    public List<ItemOption> options;

    /** Thời gian item được tạo */
    private long createTime;

    /** Đánh dấu có phải Ngọc Rồng Đen không */
    public boolean isBlackBall;

    /** Đánh dấu có phải Ngọc Rồng Namec không */
    public boolean isNamecBall;

    /** Đánh dấu có phải vật phẩm Doanh trại không */
    public boolean isDoanhTraiBall;

    /** Đối tượng item gốc (nếu cần tham chiếu) */
    private Object item;

    /** Thời gian di chuyển item về phía người chơi */
    private final int timeMoveToPlayer = 10000;

    /** Lần cuối item di chuyển đến gần người chơi */
    private long lastTimeMoveToPlayer;

    /**
     * Constructor tạo item từ template ID.
     */
    public ItemMap(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        this.zone = zone;
        this.itemMapId = zone.countItemAppeaerd++;
        if (zone.countItemAppeaerd >= 2000000000) {
            zone.countItemAppeaerd = 0;
        }
        this.itemTemplate = ItemService.gI().getTemplate((short) tempId);
        this.quantity = quantity;
        this.x = x;
        this.y = y;
        this.playerId = playerId != -1 ? Math.abs(playerId) : playerId;
        this.createTime = System.currentTimeMillis();
        this.options = new ArrayList<>();
        this.isBlackBall = ItemMapService.gI().isBlackBall(this.itemTemplate.id);
        this.isNamecBall = ItemMapService.gI().isNamecBall(this.itemTemplate.id);
        this.lastTimeMoveToPlayer = System.currentTimeMillis();
        this.zone.addItem(this);
    }

    /**
     * Constructor tạo item từ một template có sẵn.
     */
    public ItemMap(Zone zone, ItemTemplate temp, int quantity, int x, int y, long playerId) {
        this.zone = zone;
        this.itemMapId = zone.countItemAppeaerd++;
        if (zone.countItemAppeaerd >= 2000000000) {
            zone.countItemAppeaerd = 0;
        }
        this.itemTemplate = temp;
        this.quantity = quantity;
        this.x = x;
        this.y = y;
        this.playerId = playerId != -1 ? Math.abs(playerId) : playerId;
        this.createTime = System.currentTimeMillis();
        this.options = new ArrayList<>();
        this.isBlackBall = ItemMapService.gI().isBlackBall(this.itemTemplate.id);
        this.isNamecBall = ItemMapService.gI().isNamecBall(this.itemTemplate.id);
        this.lastTimeMoveToPlayer = System.currentTimeMillis();
        this.zone.addItem(this);
    }

    /**
     * Constructor sao chép một itemMap khác.
     */
    public ItemMap(ItemMap itemMap) {
        this.zone = itemMap.zone;
        this.itemMapId = itemMap.itemMapId;
        this.itemTemplate = itemMap.itemTemplate;
        this.quantity = itemMap.quantity;
        this.x = itemMap.x;
        this.y = itemMap.y;
        this.playerId = itemMap.playerId;
        this.options = itemMap.options;
        this.isBlackBall = itemMap.isBlackBall;
        this.isNamecBall = itemMap.isNamecBall;
        this.lastTimeMoveToPlayer = itemMap.lastTimeMoveToPlayer;
        this.createTime = System.currentTimeMillis();
        this.zone.addItem(this);
    }

    /**
     * Cập nhật trạng thái item trên bản đồ theo thời gian.
     */
    public void update() {
        if (this.isBlackBall) {
            if (Util.canDoWithTime(lastTimeMoveToPlayer, timeMoveToPlayer)) {
                if (this.zone != null && !this.zone.getPlayers().isEmpty()) {
                    Player player = this.zone.getPlayers().get(0);
                    if (player.zone != null && player.zone.equals(this.zone)) {
                        this.x = player.location.x;
                        this.y = this.zone.map.yPhysicInTop(this.x, player.location.y - 24);
                        reAppearItem();
                        this.lastTimeMoveToPlayer = System.currentTimeMillis();
                    }
                }
            }
            return;
        }

        if (Util.canDoWithTime(createTime, 20000) && !this.isNamecBall) {
            if (this.zone.map.mapId != 21 && this.zone.map.mapId != 22
                    && this.zone.map.mapId != 23 && this.itemTemplate.id != 78) {
                ItemMapService.gI().removeItemMapAndSendClient(this);
            }
        }
        if (Util.canDoWithTime(createTime, 15000)) {
            this.playerId = -1;
        }
    }

    /**
     * Xuất hiện lại item trên bản đồ.
     */
    private void reAppearItem() {
        ItemMapService.gI().sendItemMapDisappear(this);
        Service.gI().dropItemMap(this.zone, this);
    }

    /**
     * Dọn dẹp tham chiếu để giải phóng bộ nhớ.
     */
    public void dispose() {
        this.zone = null;
        this.itemTemplate = null;
        this.options = null;
    }

    /**
     * Lấy đối tượng item gốc.
     */
    public Object getItem() {
        return this.item;
    }

}
