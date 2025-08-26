package com.girlkun.models.npc;

import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 * Lớp quản lý bảng xếp hạng (BXH) sức mạnh của người chơi trong game
 * @author Lucifer
 */
public class BXH {

    /** Mảng chứa các danh sách bảng xếp hạng theo loại */
    public static ArrayList<Entry>[] bangXH = new ArrayList[11];
    /** Danh sách bảng xếp hạng sức mạnh (Top SM) */
    public static ArrayList<Entry2> bangXHTopSM = new ArrayList<>();
    /** Timer để quản lý cập nhật bảng xếp hạng */
    public static Timer t = new Timer(true);

    /** Khởi tạo các bảng xếp hạng */
    public static void init() {
        /** Cập nhật bảng xếp hạng sức mạnh */
        BXH.updateTopSM();
        /** Khởi tạo danh sách trống cho từng loại bảng xếp hạng */
        for (int i = 0; i < BXH.bangXH.length; ++i) {
            BXH.bangXH[i] = new ArrayList<Entry>();
        }
        /** In thông báo khi load bảng xếp hạng */
        System.out.println("load BXH");
        /** Khởi tạo dữ liệu cho từng loại bảng xếp hạng */
        for (int i = 0; i < BXH.bangXH.length; ++i) {
            initBXH(i);
        }
    }

    /** Lấy chuỗi mô tả bảng xếp hạng theo loại */
    public static String getStringBXH(int type) {
        String str = "";
        switch (type) {
            case 0: {
                /** Nếu bảng xếp hạng trống, trả về thông báo */
                if (BXH.bangXH[type].isEmpty()) {
                    str = "Chưa có thông tin";
                    break;
                }
                /** Nếu bảng xếp hạng sức mạnh trống, hiển thị từ bangXH */
                if (BXH.bangXHTopSM.size() < 1) {
                    for (Entry bxh : BXH.bangXH[type]) {
                        str = str + bxh.index + ". " + bxh.name + ": " + ")\n";
                    }
                } else {
                    /** Hiển thị thông tin bảng xếp hạng sức mạnh */
                    for (Entry2 bxh : BXH.bangXHTopSM) {
                        str = str + bxh.index + ". " + bxh.name + " (" + Service.getInstance().get_HanhTinh(bxh.gender) + ") đã đạt sức mạnh " + bxh.power + ".\n";
                    }
                }
                break;
            }
        }
        return str;
    }

    /** Cập nhật bảng xếp hạng sức mạnh (Top SM) từ cơ sở dữ liệu */
    public static void updateTopSM() {
        /** Xóa danh sách bảng xếp hạng sức mạnh hiện tại */
        BXH.bangXHTopSM.clear();
        GirlkunResultSet rs = null;
        try {
            int i = 1;
            /** Truy vấn lấy top 10 người chơi có sức mạnh cao nhất */
            rs = GirlkunDB.executeQuery("select name, power, gender from player where power > 0 ORDER BY power DESC LIMIT 10;");
            Entry2 bXHTopSM;
            if (rs != null) {
                while (rs.next()) {
                    /** Tạo entry mới cho bảng xếp hạng */
                    bXHTopSM = new Entry2();
                    bXHTopSM.name = rs.getString("name");
                    bXHTopSM.power = rs.getLong("power");
                    bXHTopSM.gender = rs.getInt("gender");
                    bXHTopSM.index = i;
                    bangXHTopSM.add(bXHTopSM);
                    i++;
                }
            }
        } catch (Exception e) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }

    /** Khởi tạo bảng xếp hạng theo loại từ cơ sở dữ liệu */
    public static void initBXH(int type) {
        /** Xóa danh sách bảng xếp hạng hiện tại */
        BXH.bangXH[type].clear();
        ArrayList<Entry> bxh = BXH.bangXH[type];
        switch (type) {
            case 0: {
                GirlkunResultSet rs = null;
                try {
                    int i = 1;
                    /** Truy vấn lấy top 10 người chơi có sức mạnh cao nhất */
                    rs = GirlkunDB.executeQuery("select name, power, gender from player where power > 0 ORDER BY power DESC LIMIT 10;");
                    Entry bXHE0;
                    String name;
                    long power;
                    int gender;
                    if (rs != null) {
                        while (rs.next()) {
                            name = rs.getString("name");
                            power = rs.getLong("power");
                            gender = rs.getInt("gender");
                            /** Tạo entry mới cho bảng xếp hạng */
                            bXHE0 = new Entry();
                            bXHE0.nXH = new long[3];
                            bXHE0.name = name;
                            bXHE0.index = i;
                            bXHE0.nXH[0] = power;
                            bXHE0.nXH[2] = gender;
                            bxh.add(bXHE0);
                            i++;
                        }
                    }
                } catch (Exception e) {
                    /** Xử lý ngoại lệ (để trống trong mã gốc) */
                }
                break;
            }
        }
    }

    /** Lớp đại diện cho một mục trong bảng xếp hạng */
    public static class Entry {
        /** Thứ hạng */
        int index;
        /** Tên người chơi */
        String name;
        /** Mảng chứa thông tin bổ sung (sức mạnh, giới tính, v.v.) */
        long[] nXH;
    }

    /** Lớp đại diện cho một mục trong bảng xếp hạng sức mạnh */
    public static class Entry2 {
        /** Thứ hạng */
        int index;
        /** Tên người chơi */
        String name;
        /** Sức mạnh của người chơi */
        long power;
        /** Giới tính của người chơi */
        int gender;
    }
}