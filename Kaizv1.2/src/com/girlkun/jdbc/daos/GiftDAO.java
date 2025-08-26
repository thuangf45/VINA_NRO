package com.girlkun.jdbc.daos;

import com.girlkun.utils.Logger;
import com.girlkun.database.GirlkunDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Lớp này xử lý các thao tác liên quan đến việc lưu trữ lịch sử quà tặng trong cơ sở dữ liệu.
 * Nó cung cấp phương thức để thêm thông tin về quà tặng mà người chơi nhận được vào bảng history_gift.
 * @author Lucifer
 */
public class GiftDAO {

    /**
     * Thêm một bản ghi lịch sử quà tặng vào cơ sở dữ liệu.
     * Phương thức này lưu ID của người chơi và mã quà tặng vào bảng history_gift.
     * Nếu có lỗi xảy ra, lỗi sẽ được ghi lại bằng Logger.
     * @param playerId ID của người chơi nhận quà tặng
     * @param code Mã của quà tặng
     */
    public static void insertHistoryGift(int playerId, String code) {
        try (Connection con = GirlkunDB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("insert into history_gift (player_id, name_gift) values (?,?)");
            ps.setInt(1, playerId);
            ps.setString(2, code);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(GiftDAO.class, e);
        }
    }
}