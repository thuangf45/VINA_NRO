package com.girlkun.models.mob;

import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemMapService;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Lớp quản lý việc thả (drop) các vật phẩm phần thưởng trong game.
 * @author Lucifer
 */
public class ArrietyDrop {

    /**
     * Danh sách các vật phẩm đồ hủy diệt cho các hành tinh (Trái Đất, Namec, Xayda).
     */
    public static final int[][] list_do_huy_diet = {
        {650, 651, 657, 658, 656}, // Trái Đất
        {652, 653, 659, 660, 656}, // Namec
        {654, 655, 661, 662, 656}  // Xayda
    };

    /**
     * Thả một vật phẩm phần thưởng tại vị trí xác định.
     * @param player Người chơi nhận vật phẩm
     * @param idItem ID của vật phẩm
     * @param soluong Số lượng vật phẩm
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    public static void DropItemReWard(Player player, int idItem, int soluong, int x, int y) {
        ItemMap item = new ItemMap(player.zone, idItem, soluong, Util.nextInt((x - 50), (x + 50)), y, player.id);
        item.options.add(new Item.ItemOption(30, 0));
        Service.getInstance().dropItemMap(player.zone, item);
        if (player.charms.tdThuHut > System.currentTimeMillis()) {
            ItemMapService.gI().pickItem(player, item.itemMapId, true);
        }
    }

    /**
     * Thả vật phẩm đồ hủy diệt kích hoạt tại vị trí xác định.
     * @param player Người chơi nhận vật phẩm
     * @param soluong Số lượng vật phẩm
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    public static void DropItemReWardDoHuyDietKichHoat(Player player, int soluong, int x, int y) {
        Item itemHuyDiet = ArrietyDrop.randomCS_DHD(ArrietyDrop.list_do_huy_diet[player.gender][Util.nextInt(0, 4)], player.gender);
        ItemMap item = new ItemMap(player.zone, itemHuyDiet.template.id, soluong, Util.nextInt((x - 50), (x + 50)), y, player.id);
        item.options = itemHuyDiet.itemOptions;
        Service.getInstance().dropItemMap(player.zone, item);
    }

    /**
     * Thả mảnh thiên sứ tại vị trí xác định.
     * @param player Người chơi nhận vật phẩm
     * @param soluong Số lượng mảnh thiên sứ
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    public static void DropManhThienSu(Player player, int soluong, int x, int y) {
        Item mts = ItemService.gI().createNewItem((short) Util.nextInt(1066, 1070));
        ItemMap item = new ItemMap(player.zone, mts.template.id, soluong, Util.nextInt((x - 50), (x + 50)), y, player.id);
        item.options.add(new Item.ItemOption(73, 0));
        Service.getInstance().dropItemMap(player.zone, item);
    }

    /**
     * Kiểm tra xem vật phẩm có thuộc loại không cho phép giao dịch hay không.
     * @param id ID của vật phẩm
     * @return True nếu vật phẩm không cho phép giao dịch, false nếu ngược lại
     */
    public static boolean IsItemKhongChoGiaoDich(int id) {
        return (id >= 663 && id <= 667);
    }

    /**
     * Tạo ngẫu nhiên vật phẩm đồ hủy diệt kích hoạt với các thuộc tính phù hợp.
     * @param itemId ID của vật phẩm
     * @param gender Giới tính của người chơi (0: Trái Đất, 1: Namec, 2: Xayda)
     * @return Vật phẩm đồ hủy diệt đã được tạo
     */
    public static Item randomCS_DHD(int itemId, int gender) {
        Item it = ItemService.gI().createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;

        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(16) + 85))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(150) + 8500))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 17)); // chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 80)); // yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1)); // ko the gd
        return it;
    }
}