package com.girlkun.models.Effect;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import java.util.List;

/**
 * Lớp xử lý các chức năng liên quan đến danh hiệu của người chơi.
 * @author Lucifer
 */
public class DanhHieu {

    /**
     * Kiểm tra xem người chơi có sở hữu danh hiệu với ID cụ thể hay không.
     * @param pl Người chơi cần kiểm tra
     * @param iddanhhieu ID của danh hiệu
     * @return true nếu người chơi sở hữu danh hiệu, false nếu không
     */
    public static boolean FindDanhhieu(Player pl, int iddanhhieu) {
        for (Item item : pl.ListDanhHieu) {
            if (item.isNotNullItem() && item.template.id == iddanhhieu) {
                return true;
            }
        }
        return false;
    }

    /**
     * Thêm hoặc xóa danh hiệu khỏi danh sách danh hiệu của người chơi.
     * @param pl Người chơi cần thêm/xóa danh hiệu
     * @param iddanhhieu Đối tượng danh hiệu cần thêm/xóa
     */
    public static void AddDanhHieu(Player pl, Item iddanhhieu) {
        if (!pl.ListDanhHieu.contains(iddanhhieu)) {
            /** Thêm danh hiệu nếu chưa có trong danh sách */
            pl.ListDanhHieu.add(iddanhhieu);
            if (pl.ListDanhHieu.size() > 5) {
                /** Xóa danh hiệu cũ nhất nếu danh sách vượt quá 5 danh hiệu */
                pl.ListDanhHieu.remove(0);
            }
        } else {
            /** Xóa danh hiệu nếu đã có trong danh sách */
            pl.ListDanhHieu.remove(iddanhhieu);
        }
        /** Gửi hiệu ứng danh hiệu đến bản đồ */
        EffectService.SendEffChartoMap(pl);
    }

    /**
     * Lấy ID hình ảnh của danh hiệu dựa trên ID vật phẩm.
     * @param item Đối tượng danh hiệu
     * @return ID hình ảnh tương ứng, hoặc -1 nếu không tìm thấy
     */
    public static int GetImgDanhHieu(Item item) {
        switch (item.template.id) {
            case 1266:
                return 21950;
            case 1267:
                return 21951;
            case 1268:
                return 21952;
            case 1269:
                return 21953;
            case 1270:
                return 21954;
            case 1271:
                return 21955;
            case 1272:
                return 21956;
            case 1273:
                return 21957;
            case 1274:
                return 21959;   
            case 1275:
                return 21960;
            case 1276:
                return 21961;
            case 1277:
                return 21962;
            case 1323:
                return 21998;
        }
        return -1;
    }
}