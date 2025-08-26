package com.girlkun.jdbc.daos;

import com.girlkun.models.item.Item;
import com.girlkun.models.shop.ItemShop;
import com.girlkun.models.shop.Shop;
import com.girlkun.models.shop.TabShop;
import com.girlkun.services.ItemService;
import com.girlkun.utils.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp xử lý các thao tác liên quan đến dữ liệu cửa hàng trong cơ sở dữ liệu.
 * @author Lucifer
 */
public class ShopDAO {

    /**
     * Lấy danh sách tất cả các cửa hàng từ cơ sở dữ liệu.
     * @param con Kết nối cơ sở dữ liệu
     * @return Danh sách các đối tượng Shop
     */
    public static List<Shop> getShops(Connection con) {
        List<Shop> list = new ArrayList<>();
        try {
            /** Truy vấn tất cả cửa hàng, sắp xếp theo npc_id tăng dần */
            PreparedStatement ps = con.prepareStatement("select * from shop order by npc_id asc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Shop shop = new Shop();
                shop.id = rs.getInt("id");
                shop.npcId = rs.getByte("npc_id");
                shop.tagName = rs.getString("tag_name");
                shop.typeShop = rs.getByte("type_shop");
                /** Tải danh sách tab cho cửa hàng */
                loadShopTab(con, shop);
                list.add(shop);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                // Bỏ qua lỗi khi đóng tài nguyên
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
        return list;
    }

    /**
     * Tải danh sách tab của một cửa hàng từ cơ sở dữ liệu.
     * @param con Kết nối cơ sở dữ liệu
     * @param shop Đối tượng cửa hàng cần tải tab
     */
    private static void loadShopTab(Connection con, Shop shop) {
        try {
            /** Truy vấn tất cả tab của cửa hàng theo shop_id, sắp xếp theo id */
            PreparedStatement ps = con.prepareStatement("select * from tab_shop where shop_id = ? order by id");
            ps.setInt(1, shop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TabShop tab = new TabShop();
                tab.shop = shop;
                tab.id = rs.getInt("id");
                tab.name = rs.getString("name").replaceAll("<>", "\n");
                /** Tải danh sách vật phẩm cho tab */
                loadItemShop(con, tab);
                shop.tabShops.add(tab);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                // Bỏ qua lỗi khi đóng tài nguyên
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
    }

    /**
     * Tải danh sách vật phẩm của một tab cửa hàng từ cơ sở dữ liệu.
     * @param con Kết nối cơ sở dữ liệu
     * @param tabShop Đối tượng tab cửa hàng cần tải vật phẩm
     */
    private static void loadItemShop(Connection con, TabShop tabShop) {
        try {
            /** Truy vấn tất cả vật phẩm đang bán trong tab, sắp xếp theo thời gian tạo giảm dần */
            PreparedStatement ps = con.prepareStatement("select * from item_shop where is_sell = 1 and tab_id = ? "
                    + "order by create_time desc");
            ps.setInt(1, tabShop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ItemShop itemShop = new ItemShop();
                itemShop.tabShop = tabShop;
                itemShop.id = rs.getInt("id");
                itemShop.temp = ItemService.gI().getTemplate(rs.getShort("temp_id"));
                itemShop.isNew = rs.getBoolean("is_new");
                itemShop.cost = rs.getInt("cost");
                itemShop.iconSpec = rs.getInt("icon_spec");
                itemShop.typeSell = rs.getByte("type_sell");
                /** Tải danh sách tùy chọn cho vật phẩm */
                loadItemShopOption(con, itemShop);
                tabShop.itemShops.add(itemShop);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                // Bỏ qua lỗi khi đóng tài nguyên
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
    }

    /**
     * Tải danh sách tùy chọn của một vật phẩm cửa hàng từ cơ sở dữ liệu.
     * @param con Kết nối cơ sở dữ liệu
     * @param itemShop Đối tượng vật phẩm cửa hàng cần tải tùy chọn
     */
    private static void loadItemShopOption(Connection con, ItemShop itemShop) {
        try {
            /** Truy vấn tất cả tùy chọn của vật phẩm theo item_shop_id */
            PreparedStatement ps = con.prepareStatement("select * from item_shop_option where item_shop_id = ?");
            ps.setInt(1, itemShop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itemShop.options.add(new Item.ItemOption(rs.getInt("option_id"), rs.getInt("param")));
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                // Bỏ qua lỗi khi đóng tài nguyên
            }
        } catch (Exception e) {
            Logger.logException(ShopDAO.class, e);
        }
    }
}