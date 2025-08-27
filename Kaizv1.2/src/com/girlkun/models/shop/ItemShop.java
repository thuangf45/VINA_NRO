package com.girlkun.models.shop;

import com.girlkun.models.Template;
import com.girlkun.models.item.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một mặt hàng trong cửa hàng (Shop).
 * <p>
 * Mỗi ItemShop liên kết với một TabShop cụ thể, chứa thông tin về template,
 * giá bán, icon, các tùy chọn (ItemOption) và trạng thái mới.
 * </p>
 * 
 * @author Lucifer
 */
public class ItemShop {
    
    /** Tab chứa mặt hàng này */
    public TabShop tabShop;

    /** Id của mặt hàng trong tab */
    public int id;
    
    /** Template gốc của mặt hàng */
    public Template.ItemTemplate temp;
    
    /** Trạng thái mới của mặt hàng */
    public boolean isNew;
    
    /** Danh sách các tùy chọn của mặt hàng */
    public List<Item.ItemOption> options;
    
    /** Loại bán của mặt hàng */
    public byte typeSell;
    
    /** Icon đặc biệt hiển thị */
    public int iconSpec;
    
    /** Giá bán của mặt hàng */
    public int cost;

    /**
     * Khởi tạo ItemShop rỗng.
     */
    public ItemShop() {
        this.options = new ArrayList<>();
    }
    
    /**
     * Khởi tạo ItemShop mới dựa trên một ItemShop khác (sao chép sâu).
     * 
     * @param itemShop ItemShop gốc để sao chép
     */
    public ItemShop(ItemShop itemShop){
        this.options = new ArrayList<>();
        this.tabShop = itemShop.tabShop;
        this.id = itemShop.id;
        this.temp = itemShop.temp;
        this.isNew = itemShop.isNew;
        this.typeSell = itemShop.typeSell;
        this.iconSpec = itemShop.iconSpec;
        this.cost = itemShop.cost;
        for(Item.ItemOption io : itemShop.options){
            this.options.add(new Item.ItemOption(io));
        }
    }
    
    /**
     * Giải phóng tài nguyên của ItemShop.
     * <p>
     * Bao gồm giải phóng tham chiếu tabShop, template và danh sách options.
     * Gọi dispose() của từng ItemOption trước khi xóa danh sách.
     * </p>
     */
    public void dispose(){
        this.tabShop = null;
        this.temp = null;
        if(this.options != null){
            for(Item.ItemOption io : this.options){
                io.dispose();
            }
            this.options.clear();
        }
        this.options = null;
    }
    
}
