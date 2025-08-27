package com.girlkun.models.reward;

import com.girlkun.models.Template;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Lớp ItemMobReward quản lý các vật phẩm thưởng từ quái vật trong game.
 * Lớp này xác định các thông tin như mẫu vật phẩm, bản đồ thả, số lượng, tỷ lệ rớt,
 * và các tùy chọn bổ sung cho vật phẩm dựa trên giới tính của người chơi.
 * 
 * @author Lucifer
 */
@Data
public class ItemMobReward {

    /**
     * Mẫu vật phẩm (template) của vật phẩm thưởng, lấy từ Manager.ITEM_TEMPLATES.
     */
    private Template.ItemTemplate temp;

    /**
     * Danh sách ID của các bản đồ mà vật phẩm có thể được thả.
     */
    private int[] mapDrop;

    /**
     * Phạm vi số lượng vật phẩm được thả (giá trị tối thiểu và tối đa).
     */
    private int[] quantity;

    /**
     * Tỷ lệ rớt vật phẩm (phần tử đầu tiên là xác suất, phần tử thứ hai là tổng số).
     */
    private int[] ratio;

    /**
     * Giới tính yêu cầu để nhận vật phẩm (-1 nếu không yêu cầu giới tính cụ thể).
     */
    private int gender;

    /**
     * Danh sách các tùy chọn bổ sung (option) cho vật phẩm thưởng.
     */
    private List<ItemOptionMobReward> option;

    /**
     * Khởi tạo một đối tượng ItemMobReward với các thông tin về vật phẩm thưởng.
     * Đảm bảo số lượng tối thiểu và tối đa hợp lệ, đồng thời khởi tạo danh sách tùy chọn.
     * 
     * @param tempId ID của mẫu vật phẩm.
     * @param mapDrop Danh sách ID các bản đồ mà vật phẩm có thể được thả.
     * @param quantity Phạm vi số lượng vật phẩm (tối thiểu và tối đa).
     * @param ratio Tỷ lệ rớt vật phẩm (xác suất và tổng số).
     * @param gender Giới tính yêu cầu (-1 nếu không yêu cầu).
     */
    public ItemMobReward(int tempId, int[] mapDrop, int[] quantity, int[] ratio, int gender) {
        this.temp = Manager.ITEM_TEMPLATES.get(tempId);
        this.mapDrop = mapDrop;
        this.quantity = quantity;
        if (this.quantity[0] < 0) {
            this.quantity[0] = -this.quantity[0];
        } else if (this.quantity[0] == 0) {
            this.quantity[0] = 1;
        }
        if (this.quantity[1] < 0) {
            this.quantity[1] = -this.quantity[1];
        } else if (this.quantity[1] == 0) {
            this.quantity[1] = 1;
        }
        if (this.quantity[0] > this.quantity[1]) {
            int tempSwap = this.quantity[0];
            this.quantity[0] = this.quantity[1];
            this.quantity[1] = tempSwap;
        }
        this.ratio = ratio;
        this.gender = gender;
        this.option = new ArrayList<>();
    }

    /**
     * Tạo một vật phẩm thưởng trên bản đồ dựa trên thông tin của ItemMobReward.
     * Kiểm tra điều kiện bản đồ, giới tính, và tỷ lệ rớt trước khi tạo vật phẩm.
     * 
     * @param zone Khu vực (zone) nơi vật phẩm sẽ được thả.
     * @param player Người chơi nhận vật phẩm.
     * @param x Tọa độ x của vật phẩm trên bản đồ.
     * @param y Tọa độ y của vật phẩm trên bản đồ.
     * @return Đối tượng ItemMap nếu vật phẩm được tạo, hoặc null nếu không thỏa mãn điều kiện.
     */
    public ItemMap getItemMap(Zone zone, Player player, int x, int y) {
        for (int mapId : this.mapDrop) {
            if (mapId != -1 && mapId != zone.map.mapId) {
                continue;
            }
            if (this.gender != -1 && this.gender != player.gender) {
                break;
            }
            if (Util.isTrue(this.ratio[0], this.ratio[1])) {
                ItemMap itemMap = new ItemMap(zone, this.temp, Util.nextInt(this.quantity[0], this.quantity[1]), 
                        x, y, player.id);
                for (ItemOptionMobReward opt : this.option) {
                    if (!Util.isTrue(opt.getRatio()[0], opt.getRatio()[1])) {
                        continue;
                    }
                    itemMap.options.add(new Item.ItemOption(opt.getTemp(), Util.nextInt(opt.getParam()[0], opt.getParam()[1])));
                }
                return itemMap;
            }
        }
        return null;
    }
}