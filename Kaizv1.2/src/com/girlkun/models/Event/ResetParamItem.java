package com.girlkun.models.Event;

import com.girlkun.models.item.Item;

/**
 * Lớp xử lý các chức năng liên quan đến việc đặt lại thông số cơ bản của vật phẩm.
 * @author Lucifer
 */
public class ResetParamItem {

    /**
     * Mảng chứa các chỉ số cơ bản cho quần (HP) theo loại vật phẩm và giới tính.
     */
    public static int[][] ChiSoQuan = new int[][] {
        {48, 50, 52}, // Loại 0: Trái Đất, Namek, Saiyan
        {96, 100, 104}, // Loại 1: Trái Đất, Namek, Saiyan
        {150, 155, 160} // Loại 2: Trái Đất, Namek, Saiyan
    };

    /**
     * Mảng chứa các chỉ số cơ bản cho găng (tấn công) theo loại vật phẩm và giới tính.
     */
    public static int[][] ChiSoGang = new int[][] {
        {5000, 4500, 5200}, // Loại 0: Trái Đất, Namek, Saiyan
        {10000, 9600, 10400}, // Loại 1: Trái Đất, Namek, Saiyan
        {13500, 13000, 14000} // Loại 2: Trái Đất, Namek, Saiyan
    };

    /**
     * Lấy cấp độ của vật phẩm dựa trên tùy chọn ID 72.
     * @param item Vật phẩm cần kiểm tra
     * @return Cấp độ của vật phẩm, trả về 0 nếu không tìm thấy
     */
    public static int GetLevel(Item item) {
        if (item == null) {
            return 0;
        }
        for (Item.ItemOption op : item.itemOptions) {
            if (op.optionTemplate.id == 72) {
                return op.param; // Trả về giá trị tham số của tùy chọn ID 72
            }
        }
        return 0;
    }

    /**
     * Đặt lại chỉ số cơ bản của vật phẩm (quần hoặc găng) dựa trên loại và cấp độ.
     * @param item Vật phẩm cần đặt lại chỉ số
     */
    public static void SetBasicChiSo(Item item) {
        if (item == null || item.template == null) {
            return; // Thoát nếu vật phẩm hoặc template là null
        }
        if (item.template.type == 1 || item.template.type == 2) {
            int optionid = item.template.type == 1 ? 22 : 0; // ID tùy chọn: 22 cho quần (HP), 0 cho găng (tấn công)
            if (item.template.id >= 555 && item.template.id <= 567) {
                int i = GetBasicChiSo(item.template.type, GetLevel(item), 0, item.template.gender);
                for (Item.ItemOption op : item.itemOptions) {
                    if (op.optionTemplate.id == optionid && op.param > i) {
                        op.param = i; // Đặt lại chỉ số nếu vượt quá giá trị cơ bản
                    }
                }
            }
            if (item.template.id >= 650 && item.template.id <= 662) {
                int i = GetBasicChiSo(item.template.type, GetLevel(item), 1, item.template.gender);
                for (Item.ItemOption op : item.itemOptions) {
                    if (op.optionTemplate.id == optionid && op.param > i) {
                        op.param = i; // Đặt lại chỉ số nếu vượt quá giá trị cơ bản
                    }
                }
            }
            if (item.template.id >= 1048 && item.template.id <= 1062) {
                int i = GetBasicChiSo(item.template.type, GetLevel(item), 2, item.template.gender);
                for (Item.ItemOption op : item.itemOptions) {
                    if (op.optionTemplate.id == optionid && op.param > i) {
                        op.param = i; // Đặt lại chỉ số nếu vượt quá giá trị cơ bản
                    }
                }
            }
        }
    }

    /**
     * Tính toán chỉ số cơ bản của vật phẩm dựa trên loại, cấp độ, loại vật phẩm và giới tính.
     * @param type Loại vật phẩm (1: quần, 2: găng)
     * @param level Cấp độ của vật phẩm
     * @param typeitem Loại vật phẩm (0, 1, 2)
     * @param gender Giới tính (0: Trái Đất, 1: Namek, 2: Saiyan)
     * @return Giá trị chỉ số cơ bản
     */
    public static int GetBasicChiSo(int type, int level, int typeitem, int gender) {
        if (type == 1) {
            if (level == 0) {
                return ChiSoQuan[typeitem][gender]; // Trả về chỉ số cơ bản của quần
            }
            if (level > 0) {
                return (int) (GetBasicChiSo(type, level - 1, typeitem, gender) * 1.1); // Tăng 10% mỗi cấp
            }
        }
        if (type == 2) {
            if (level == 0) {
                return ChiSoGang[typeitem][gender]; // Trả về chỉ số cơ bản của găng
            }
            if (level > 0) {
                return (int) (GetBasicChiSo(type, level - 1, typeitem, gender) * 1.1); // Tăng 10% mỗi cấp
            }
        }
        return 0;
    }
}