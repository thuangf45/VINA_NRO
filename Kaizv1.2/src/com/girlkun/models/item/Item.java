package com.girlkun.models.item;

import com.girlkun.models.Template;
import com.girlkun.models.Template.ItemTemplate;
import com.girlkun.services.ItemService;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Đại diện cho một vật phẩm trong game.
 * <p>
 * Vật phẩm có thể chứa các thuộc tính như số lượng, hạn sử dụng, các tùy chọn
 * đặc biệt {@link ItemOption}, cũng như nhiều phân loại khác nhau (Đá nâng cấp, 
 * Mảnh trang sức, Thức ăn, Công thức,...).
 * </p>
 *
 * @author Lucifer
 */
public class Item {

    /** ID định danh của item */
    public int id;

    /** Cho biết item có hạn sử dụng hay không */
    public boolean isExpires = false;

    /** Cho biết item có thể xếp chồng hay không */
    public boolean isUpToUp;

    /** Số lượng tạm (dùng khi stack hoặc giao dịch) */
    public int quantityTemp = 1;

    /** ID icon head tạm */
    public short headTemp;

    /** ID icon body tạm */
    public short bodyTemp;

    /** ID icon leg tạm */
    public short legTemp;

    /** ID template tạm */
    public int idTemp;

    /** Thông tin template gốc của item */
    public ItemTemplate template;

    /** Chuỗi thông tin mô tả item */
    public String info;

    /** Chuỗi nội dung bổ sung của item */
    public String content;

    /** Số lượng hiện tại */
    public int quantity;

    /** Số lượng trong giao dịch */
    public int quantityGD = 0;

    /** Danh sách các tùy chọn (option) của item */
    public List<ItemOption> itemOptions;

    /** Thời gian khởi tạo item (theo millis) */
    public long createTime;

    /**
     * Khởi tạo item từ một item khác (clone).
     * 
     * @param _item item gốc cần sao chép
     */
    public Item(Item _item) { 
        this.id = _item.id;
        this.template = _item.template;
        this.info = _item.info;
        this.content = _item.content;
        this.isExpires = _item.isExpires;
        this.isUpToUp = _item.isUpToUp;
        this.quantityTemp = _item.quantityTemp;
        this.headTemp = _item.headTemp;
        this.bodyTemp = _item.bodyTemp;
        this.legTemp = _item.legTemp;
        this.idTemp = _item.idTemp;
        this.itemOptions = new ArrayList<>();
        for (ItemOption _option : _item.itemOptions) {
            this.itemOptions.add(new ItemOption(_option.optionTemplate.id, _option.param));
        }
        this.quantity = 1;
    }

    /** @return tên item */
    public String Name() {
        return this.template.name;
    }

    /** @return true nếu item hợp lệ (khác null) */
    public boolean isNotNullItem() {
        return this.template != null;
    }

    /** Constructor mặc định */
    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    /**
     * Khởi tạo item theo id template
     * 
     * @param itemId id template
     */
    public Item(short itemId) {
        this.template = ItemService.gI().getTemplate(itemId);
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    /** @return chuỗi mô tả các tùy chọn (option) của item */
    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    /** @return chuỗi mô tả đầy đủ thông tin item */
    public String getInfoItem() {
        String strInfo = "|1|" + template.name + "\n|0|";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString() + "\n";
        }
        strInfo += "|2|" + template.description;
        return strInfo;
    }

    /** @return nội dung điều kiện sử dụng item */
    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    /** Giải phóng dữ liệu item */
    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    /**
     * Đại diện cho tùy chọn (option) của một item.
     * <p>
     * Mỗi option có một template gốc và một giá trị param.
     * </p>
     */
    public static class ItemOption {

        /** Bảng ánh xạ tùy chọn sang chuỗi hiển thị */
        private static Map<String, String> OPTION_STRING = new HashMap<>();

        /** Giá trị của option */
        public int param;

        /** Template gốc của option */
        public Template.ItemOptionTemplate optionTemplate;

        /** Danh sách tùy chọn con (nếu có) */
        public List<ItemOption> itemOptions;

        /** Constructor mặc định */
        public ItemOption() {
        }

        /**
         * Copy constructor.
         *
         * @param io option gốc
         */
        public ItemOption(ItemOption io) {
            this.param = io.param;
            this.optionTemplate = io.optionTemplate;
        }

        /**
         * Constructor với id template và param.
         *
         * @param tempId id template
         * @param param giá trị option
         */
        public ItemOption(int tempId, int param) {
            this.optionTemplate = ItemService.gI().getItemOptionTemplate(tempId);
            this.param = param;
        }

        /**
         * Constructor với template và param.
         *
         * @param temp template gốc
         * @param param giá trị option
         */
        public ItemOption(Template.ItemOptionTemplate temp, int param) {
            this.optionTemplate = temp;
            this.param = param;
        }

        /**
         * @return chuỗi mô tả option theo format template
         */
        public String getOptionString() {
            return Util.replace(this.optionTemplate.name, "#", String.valueOf(this.param));
        }

        /** Giải phóng dữ liệu option */
        public void dispose() {
            this.optionTemplate = null;
        }

        @Override
        public String toString() {
            final String n = "\"";
            return "{"
                    + n + "id" + n + ":" + n + optionTemplate.id + n + ","
                    + n + "param" + n + ":" + n + param + n
                    + "}";
        }
    }

    /** @return true nếu item là SKH (Set Kích Hoạt) */
    public boolean isSKH() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                return true;
            }
        }
        return false;
    }

    /** @return true nếu item là Đá Tăng Sao (DTS) */
    public boolean isDTS() {
        return this.template.id >= 1048 && this.template.id <= 1062;
    }

    /** @return true nếu item là Thức ăn */
    public boolean isThucAn() {
        return this.template.id >= 663 && this.template.id <= 667;
    }

    /** @return true nếu item là Đá Thần Linh (DTL) */
    public boolean isDTL() {
        return this.template.id >= 555 && this.template.id <= 567;
    }

    /** @return true nếu item là Đá Hủy Diệt (DHD) */
    public boolean isDHD() {
        return this.template.id >= 650 && this.template.id <= 662;
    }

    /** @return true nếu item là Mảnh Trang Sức */
    public boolean isManhTS() {
        return this.template.id >= 1066 && this.template.id <= 1070;
    }

    /** @return true nếu item là Công thức VIP */
    public boolean isCongThucVip() {
        return this.template.id >= 1084 && this.template.id <= 1086;
    }

    /** @return true nếu item là Đá nâng cấp */
    public boolean isDaNangCap() {
        return this.template.id >= 1074 && this.template.id <= 1078;
    }

    /** @return true nếu item là Đá may mắn */
    public boolean isDaMayMan() {
        return this.template.id >= 1079 && this.template.id <= 1083;
    }

    /** @return tên loại trang bị (Áo, Quần, Găng, Giày, Rada) */
    public String typeName() {
        switch (this.template.type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Rada";
            default:
                return "";
        }
    }

    /** @return hành tinh tương ứng với item */
    public String typeHanhTinh() {
        switch (this.template.id) {
            case 1071:
            case 1084:
                return "Trái đất";
            case 1072:
            case 1085:
                return "Namếc";
            case 1073:
            case 1086:
                return "Xayda";
            default:
                return "";
        }
    }

    /** @return id loại mảnh trang sức (0-4) */
    public byte typeIdManh() {
        if (!isManhTS()) {
            return -1;
        }
        switch (this.template.id) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1070:
                return 2;
            case 1068:
                return 3;
            case 1069:
                return 4;
            default:
                return -1;
        }
    }

    /** @return tên loại mảnh trang sức */
    public String typeNameManh() {
        switch (this.template.id) {
            case 1066:
                return "Áo";
            case 1067:
                return "Quần";
            case 1070:
                return "Găng";
            case 1068:
                return "Giày";
            case 1069:
                return "Nhẫn";
            default:
                return "";
        }
    }

    /** @return cấp độ của Đá nâng cấp */
    public String typeDanangcap() {
        switch (this.template.id) {
            case 1074:
                return "cấp 1";
            case 1075:
                return "cấp 2";
            case 1076:
                return "cấp 3";
            case 1077:
                return "cấp 4";
            case 1078:
                return "cấp 5";
            default:
                return "";
        }
    }

    /** @return cấp độ của Đá may mắn */
    public String typeDaMayman() {
        switch (this.template.id) {
            case 1079:
                return "cấp 1";
            case 1080:
                return "cấp 2";
            case 1081:
                return "cấp 3";
            case 1082:
                return "cấp 4";
            case 1083:
                return "cấp 5";
            default:
                return "";
        }
    }
}
