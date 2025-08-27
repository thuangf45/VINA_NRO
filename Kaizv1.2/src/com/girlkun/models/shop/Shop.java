package com.girlkun.models.shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một cửa hàng (Shop) trong game.
 * <p>
 * Một Shop chứa nhiều TabShop, mỗi TabShop chứa danh sách ItemShop.
 * Shop cũng lưu thông tin NPC quản lý, loại shop và tagName hiển thị.
 * </p>
 * 
 * @author Lucifer
 */
public class Shop {

    /** Id của shop */
    public int id;

    /** Id của NPC quản lý shop */
    public byte npcId;

    /** Danh sách tab trong shop */
    public List<TabShop> tabShops;

    /** Tên hiển thị của shop */
    public String tagName;

    /** Loại shop */
    public byte typeShop;

    /**
     * Khởi tạo shop rỗng.
     */
    public Shop() {
        this.tabShops = new ArrayList<>();
    }

    /**
     * Sao chép shop dựa trên một shop khác và lọc theo giới tính người chơi.
     * 
     * @param shop Shop gốc để sao chép
     * @param gender Giới tính của người chơi (lọc item theo gender)
     */
    public Shop(Shop shop, int gender) {
        this.tabShops = new ArrayList<>();
        this.id = shop.id;
        this.npcId = shop.npcId;
        this.tagName = shop.tagName;
        this.typeShop = shop.typeShop;
        for (TabShop tabShop : shop.tabShops) {
            this.tabShops.add(new TabShop(tabShop, gender));
        }
    }

    /**
     * Sao chép shop dựa trên một shop khác (sao chép sâu).
     * 
     * @param shop Shop gốc để sao chép
     */
    public Shop(Shop shop) {
        this.tabShops = new ArrayList<>();
        this.id = shop.id;
        this.npcId = shop.npcId;
        this.tagName = shop.tagName;
        this.typeShop = shop.typeShop;
        for (TabShop tabShop : shop.tabShops) {
            this.tabShops.add(new TabShop(tabShop));
        }
    }
    
    /**
     * Lấy ItemShop theo template id.
     * 
     * @param temp Id của template item
     * @return ItemShop tương ứng hoặc null nếu không tìm thấy
     */
    public ItemShop getItemShop(int temp){
        for(TabShop tab : this.tabShops){
            for(ItemShop is : tab.itemShops){
                if(is.temp.id == temp){
                    return is;
                }
            }
        }
        return null;
    }

    /**
     * Giải phóng tài nguyên của shop.
     * <p>
     * Bao gồm dispose từng TabShop và xóa danh sách tabShops.
     * </p>
     */
    public void dispose() {
        if (this.tabShops != null) {
            for (TabShop ts : this.tabShops) {
                ts.dispose();
            }
            this.tabShops.clear();
        }
        this.tabShops = null;
    }

}
