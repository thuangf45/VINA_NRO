package com.girlkun.jdbc.daos;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Lớp xử lý các thao tác liên quan đến việc ghi và xóa lịch sử giao dịch giữa các người chơi.
 * @author Lucifer
 */
public class HistoryTransactionDAO {

    /**
     * Ghi lại lịch sử giao dịch giữa hai người chơi vào cơ sở dữ liệu.
     * @param pl1 Người chơi thứ nhất
     * @param pl2 Người chơi thứ hai
     * @param goldP1 Số vàng giao dịch của người chơi thứ nhất
     * @param goldP2 Số vàng giao dịch của người chơi thứ hai
     * @param itemP1 Danh sách vật phẩm giao dịch của người chơi thứ nhất
     * @param itemP2 Danh sách vật phẩm giao dịch của người chơi thứ hai
     * @param bag1Before Túi đồ của người chơi thứ nhất trước giao dịch
     * @param bag2Before Túi đồ của người chơi thứ hai trước giao dịch
     * @param bag1After Túi đồ của người chơi thứ nhất sau giao dịch
     * @param bag2After Túi đồ của người chơi thứ hai sau giao dịch
     * @param gold1Before Số vàng của người chơi thứ nhất trước giao dịch
     * @param gold2Before Số vàng của người chơi thứ hai trước giao dịch
     * @param gold1After Số vàng của người chơi thứ nhất sau giao dịch
     * @param gold2After Số vàng của người chơi thứ hai sau giao dịch
     */
    public static void insert(Player pl1, Player pl2,
                              int goldP1, int goldP2, List<Item> itemP1, List<Item> itemP2,
                              List<Item> bag1Before, List<Item> bag2Before,
                              List<Item> bag1After,
                              List<Item> bag2After,
                              long gold1Before, long gold2Before, long gold1After, long gold2After) {

        /** Chuẩn bị thông tin người chơi */
        String player1 = pl1.name + " (" + pl1.id + ")";
        String player2 = pl2.name + " (" + pl2.id + ")";
        String itemPlayer1 = "Gold: " + goldP1 + ", ";
        String itemPlayer2 = "Gold: " + goldP2 + ", ";

        /** Danh sách vật phẩm giao dịch đã được gộp số lượng */
        List<Item> doGD1 = new ArrayList<>();
        List<Item> doGD2 = new ArrayList<>();

        /** Gộp số lượng vật phẩm giao dịch của người chơi thứ nhất */
        for (Item item : itemP1) {
            if (item.isNotNullItem() && doGD1.stream().noneMatch(item1 -> item1.template.id == item.template.id)) {
                doGD1.add(item);
            } else if (item.isNotNullItem()) {
                doGD1.stream().filter(item1 -> item1.template.id == item.template.id).findFirst().get().quantityGD += item.quantityGD;
            }
        }

        /** Gộp số lượng vật phẩm giao dịch của người chơi thứ hai */
        for (Item item : itemP2) {
            if (item.isNotNullItem() && doGD2.stream().noneMatch(item1 -> item1.template.id == item.template.id)) {
                doGD2.add(item);
            } else if (item.isNotNullItem()) {
                doGD2.stream().filter(item1 -> item1.template.id == item.template.id).findFirst().get().quantityGD += item.quantityGD;
            }
        }

        /** Tạo chuỗi mô tả vật phẩm giao dịch của người chơi thứ nhất */
        for (Item item : doGD1) {
            if (item.isNotNullItem()) {
                itemPlayer1 += item.template.name + " (x" + item.quantityGD + "),";
            }
        }

        /** Tạo chuỗi mô tả vật phẩm giao dịch của người chơi thứ hai */
        for (Item item : doGD2) {
            if (item.isNotNullItem()) {
                itemPlayer2 += item.template.name + " (x" + item.quantityGD + "),";
            }
        }

        /** Tạo chuỗi mô tả túi đồ trước giao dịch của người chơi thứ nhất */
        String beforeTran1 = "";
        for (Item item : bag1Before) {
            if (item.isNotNullItem()) {
                beforeTran1 += item.template.name + " (x" + item.quantity + "),";
            }
        }

        /** Tạo chuỗi mô tả túi đồ trước giao dịch của người chơi thứ hai */
        String beforeTran2 = "";
        for (Item item : bag2Before) {
            if (item.isNotNullItem()) {
                beforeTran2 += item.template.name + " (x" + item.quantity + "),";
            }
        }

        /** Tạo chuỗi mô tả túi đồ sau giao dịch của người chơi thứ nhất */
        String afterTran1 = "";
        for (Item item : bag1After) {
            if (item.isNotNullItem()) {
                afterTran1 += item.template.name + " (x" + item.quantity + "),";
            }
        }

        /** Tạo chuỗi mô tả túi đồ sau giao dịch của người chơi thứ hai */
        String afterTran2 = "";
        for (Item item : bag2After) {
            if (item.isNotNullItem()) {
                afterTran2 += item.template.name + " (x" + item.quantity + "),";
            }
        }

        try {
            /** Ghi lịch sử giao dịch vào cơ sở dữ liệu */
            GirlkunDB.executeUpdate("insert into history_transaction values()", player1, player2,
                    itemPlayer1, itemPlayer2, beforeTran1, beforeTran2, afterTran1, afterTran2, new Timestamp(System.currentTimeMillis()));
        } catch (Exception ex) {
            // Bỏ qua lỗi
        }
    }

    /**
     * Xóa các bản ghi lịch sử giao dịch cũ hơn 3 ngày khỏi cơ sở dữ liệu.
     */
    public static void deleteHistory() {
        /*
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("delete from history_transaction where time_tran < '"
                    + TimeUtil.getTimeBeforeCurrent(3 * 24 * 60 * 60 * 1000, "yyyy-MM-dd") + "'");
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            // Bỏ qua lỗi
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HistoryTransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
    }
}