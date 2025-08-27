package com.girlkun.models.shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một tab trong cửa hàng (Shop).
 * <p>
 * Mỗi tab chứa danh sách các ItemShop (mặt hàng) có thể được hiển thị trong tab đó.
 * Class hỗ trợ sao chép tab với tùy chọn lọc theo giới tính của người chơi.
 * </p>
 * 
 * @author Lucifer
 */
public class TabShop {
    
    /** Cửa hàng chứa tab này */
    public Shop shop;

    /** Id của tab */
    public int id;
    
    /** Tên hiển thị của tab */
    public String name;
    
    /** Danh sách mặt hàng trong tab */
    public List<ItemShop> itemShops;

    /**
     * Khởi tạo tab rỗng.
     */
    public TabShop() {
        this.itemShops = new ArrayList<>();
    }
    
    /**
     * Khởi tạo tab mới dựa trên một tab khác, lọc các mặt hàng theo giới tính.
     * 
     * @param tabShop tab gốc để sao chép
     * @param gender giới tính của người chơi (0: nam, 1: nữ, 2: khác)
     */
    public TabShop(TabShop tabShop, int gender){
        this.itemShops = new ArrayList();
        this.shop = tabShop.shop;
        this.id = tabShop.id;
        this.name = tabShop.name;
        
        for(ItemShop itemShop : tabShop.itemShops){
            if(itemShop.temp.gender == gender || itemShop.temp.gender > 2){
                this.itemShops.add(new ItemShop(itemShop));
            }
        }
    }
    
    /**
     * Khởi tạo tab mới dựa trên một tab khác mà không lọc.
     * 
     * @param tabShop tab gốc để sao chép
     */
    public TabShop(TabShop tabShop){
        this.itemShops = new ArrayList<>();
        this.shop = tabShop.shop;
        this.id = tabShop.id;
        this.name = tabShop.name;
        for(ItemShop itemShop : tabShop.itemShops){
            this.itemShops.add(new ItemShop(itemShop));
        }
    }
    
    /**
     * Giải phóng tài nguyên của tab.
     * <p>
     * Bao gồm giải phóng tham chiếu shop, name, và danh sách itemShops.
     * Gọi phương thức dispose() của từng ItemShop trước khi xóa danh sách.
     * </p>
     */
    public void dispose(){
        this.shop = null;
        this.name = null;
        if(this.itemShops != null){
            for(ItemShop is : this.itemShops){
                is.dispose();
            }
            this.itemShops.clear();
        }
        this.itemShops = null;
    }
    
}
