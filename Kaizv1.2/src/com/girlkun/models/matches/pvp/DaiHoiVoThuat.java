package com.girlkun.models.matches.pvp;

import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Lớp đại diện cho sự kiện Đại Hội Võ Thuật trong game.
 * @author Lucifer
 */
public class DaiHoiVoThuat {

    /**
     * Danh sách người chơi đã đăng ký tham gia Đại Hội Võ Thuật.
     */
    public ArrayList<Player> listReg = new ArrayList<>();

    /**
     * Danh sách người chơi đang chờ tham gia trận đấu.
     */
    public ArrayList<Player> listPlayerWait = new ArrayList<>();

    /**
     * Tên giải đấu (ví dụ: Nhi đồng, Siêu cấp 1, Ngoại hạng).
     */
    public String NameCup;

    /**
     * Mảng chứa các khung giờ diễn ra giải đấu.
     */
    public String[] Time;

    /**
     * Lệ phí ngọc để tham gia giải đấu.
     */
    public int gem;

    /**
     * Lệ phí vàng để tham gia giải đấu.
     */
    public int gold;

    /**
     * Số phút tối thiểu để bắt đầu giải đấu.
     */
    public int min_start;

    /**
     * Số phút tối thiểu tạm thời để bắt đầu giải đấu.
     */
    public int min_start_temp;

    /**
     * Giới hạn phút cho giải đấu.
     */
    public int min_limit;

    /**
     * Vòng đấu hiện tại của giải đấu.
     */
    public int round = 1;

    /**
     * Giờ hiện tại của hệ thống (theo múi giờ Asia/Ho_Chi_Minh).
     */
    public int Hour;

    /**
     * Phút hiện tại của hệ thống.
     */
    public int Minutes;

    /**
     * Giây hiện tại của hệ thống.
     */
    public int Second;

    /**
     * Instance duy nhất của lớp DaiHoiVoThuat (Singleton).
     */
    private static DaiHoiVoThuat instance;

    /**
     * Lấy instance của DaiHoiVoThuat (Singleton pattern).
     * @return Instance của DaiHoiVoThuat
     */
    public static DaiHoiVoThuat gI() {
        if (instance == null) {
            instance = new DaiHoiVoThuat();
        }
        return instance;
    }

    /**
     * Lấy thông tin về giải đấu hiện tại dựa trên giờ hiện tại.
     * @return Giải đấu hiện tại nếu tìm thấy, hoặc null nếu không có
     */
    public DaiHoiVoThuat getDaiHoiNow() {
        for (DaiHoiVoThuat dh : Manager.LIST_DHVT) {
            if (dh != null && Util.contains(dh.Time, String.valueOf(Hour))) {
                return dh;
            }
        }
        return null;
    }

    /**
     * Lấy thông tin chi tiết về lịch thi đấu, giải thưởng và lệ phí của các giải đấu.
     * @return Chuỗi thông tin về giải đấu
     */
    public String Info() {
        for (DaiHoiVoThuat daihoi : Manager.LIST_DHVT) {
            if (daihoi.gold > 0) {
                return "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,14,18h\bGiải Siêu cấp 1: 9,13,19h\bGiải Siêu Cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\n"
                        + "Giải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng\n"
                        + "Lệ phí đăng ký các giải đấu\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng";
            } else if (daihoi.gem > 0) {
                return "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,14,18h\bGiải Siêu cấp 1: 9,13,19h\bGiải Siêu Cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\n"
                        + "Giải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng\n"
                        + "Lệ phí đăng ký các giải đấu\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng";
            }
        }
        return "Đã hết thời gian đăng ký vui lòng đợi đến giải đấu sau\b";
    }

    /**
     * Cập nhật trạng thái của giải đấu Đại Hội Võ Thuật dựa trên thời gian hiện tại.
     */
    public void update() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        try {
            Second = calendar.get(Calendar.SECOND);
            Minutes = calendar.get(Calendar.MINUTE);
            Hour = calendar.get(Calendar.HOUR_OF_DAY);
            DaiHoiVoThuatService.gI(getDaiHoiNow()).Update();
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }
}