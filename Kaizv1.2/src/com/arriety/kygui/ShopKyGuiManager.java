package com.arriety.kygui;

import com.girlkun.database.GirlkunDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.json.simple.JSONValue;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ShopKyGuiManager:
 * Lớp ShopKyGuiManager thuộc package com.arriety.kygui, quản lý danh sách vật phẩm ký gửi trong hệ thống cửa hàng ký gửi của game. Lớp này là một singleton, chịu trách nhiệm lưu trữ và quản lý danh sách vật phẩm ký gửi (ItemKyGui), tên các tab hiển thị trong cửa hàng, và thực hiện các thao tác lưu trữ dữ liệu vào cơ sở dữ liệu (database). Lớp cung cấp các phương thức để xóa và lưu danh sách vật phẩm vào database.
 */
public class ShopKyGuiManager {
    /**
     * instance: ShopKyGuiManager - Biến tĩnh để lưu trữ instance duy nhất của lớp ShopKyGuiManager, đảm bảo chỉ có một đối tượng được tạo.
     */
    private static ShopKyGuiManager instance;

    /**
     * tabName: String[] - Mảng chứa tên các tab hiển thị trong cửa hàng ký gửi: "Trang bị", "Bông tai", "Linh Thú", "Linh tinh", và một tab rỗng.
     */
    public String[] tabName = {"Trang bị", "Bông tai", "Linh Thú", "Linh tinh", ""};

    /**
     * listItem: List<ItemKyGui> - Danh sách các vật phẩm ký gửi, chứa thông tin các vật phẩm được người chơi đăng bán.
     */
    public List<ItemKyGui> listItem = new ArrayList<>();

    /**
     * gI():
     * - Mô tả: Trả về instance duy nhất của lớp ShopKyGuiManager (singleton pattern). Nếu instance chưa được tạo, sẽ khởi tạo mới.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Đối tượng ShopKyGuiManager.
     */
    public static ShopKyGuiManager gI() {
        if (instance == null) {
            instance = new ShopKyGuiManager();
        }
        return instance;
    }

    /**
     * clear():
     * - Mô tả: Xóa toàn bộ dữ liệu trong bảng shop_ky_gui trong cơ sở dữ liệu bằng cách thực hiện lệnh TRUNCATE.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Kết nối với cơ sở dữ liệu qua GirlkunDB.
     *   - Thực hiện lệnh SQL TRUNCATE để xóa toàn bộ dữ liệu trong bảng shop_ky_gui.
     *   - Xử lý ngoại lệ SQLException và ghi log lỗi nếu xảy ra.
     * - Trả về: Không trả về gì, chỉ thực hiện xóa dữ liệu trong database.
     * - Ngoại lệ: SQLException - Nếu có lỗi khi thực hiện truy vấn SQL.
     */
    public void clear() throws SQLException {
        try (Connection con = GirlkunDB.getConnection()) {
            String insertQuery = "TRUNCATE shop_ky_gui";
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.print("\nError at 4\n");
                e.printStackTrace();
            }
        }
    }

    /**
     * save():
     * - Mô tả: Lưu danh sách vật phẩm ký gửi (listItem) vào bảng shop_ky_gui trong cơ sở dữ liệu. Trước khi lưu, xóa toàn bộ dữ liệu cũ bằng phương thức clear(). Chỉ thực hiện khi không có thao tác lưu nào đang chạy (isSave = false).
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Kiểm tra trạng thái isSave để tránh lưu đồng thời.
     *   - Gọi phương thức clear() để xóa dữ liệu cũ trong database.
     *   - Kết nối với cơ sở dữ liệu qua GirlkunDB.
     *   - Với mỗi vật phẩm trong listItem, tạo truy vấn INSERT để lưu thông tin (id, player_id, tab, item_id, ruby, gem, quantity, itemOption, isUpTop, isBuy, createTime) vào bảng shop_ky_gui.
     *   - Chuyển danh sách options thành chuỗi JSON bằng JSONValue.toJSONString, nếu options là null thì lưu dưới dạng "[]".
     *   - Thực hiện truy vấn và xử lý ngoại lệ nếu có lỗi.
     *   - Đặt lại isSave = false sau khi lưu xong.
     * - Trả về: Không trả về gì, chỉ lưu dữ liệu vào database.
     * - Ngoại lệ: InterruptedException, SQLException - Nếu có lỗi khi kết nối hoặc thực hiện truy vấn SQL.
     */
    private boolean isSave;
    public void save() throws InterruptedException {
        if (isSave) {
            return;
        }
        isSave = true;
        try {
            clear();
        } catch (SQLException ex) {
            System.err.print("\nError at 5\n");
            java.util.logging.Logger.getLogger(ShopKyGuiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (Connection con = GirlkunDB.getConnection()) {
            String insertQuery = "INSERT INTO shop_ky_gui (id, player_id, tab, item_id, ruby, gem, quantity, itemOption, isUpTop, isBuy, createTime) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                List<ItemKyGui> newit = new ArrayList<>(listItem);
                for (ItemKyGui item : newit) {
                    ps.setInt(1, item.id);
                    ps.setInt(2, item.player_sell);
                    ps.setInt(3, item.tab);
                    ps.setInt(4, item.itemId);
                    ps.setInt(5, item.goldSell);
                    ps.setInt(6, item.gemSell);
                    ps.setInt(7, item.quantity);
                    ps.setString(8, JSONValue.toJSONString(item.options).equals("null") ? "[]" : JSONValue.toJSONString(item.options));
                    ps.setInt(9, item.isUpTop);
                    ps.setInt(10, item.isBuy);
                    ps.setLong(11, item.createTime);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.print("\nError at 6\n");
            e.printStackTrace();
        }
        isSave = false;
    }
}