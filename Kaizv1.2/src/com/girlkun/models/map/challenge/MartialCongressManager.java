package com.girlkun.models.map.challenge;

import com.girlkun.models.map.challenge.MartialCongress;
import com.girlkun.server.Manager;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp MartialCongressManager quản lý danh sách các giải đấu võ thuật trong trò chơi.
 * @author Lucifer
 */
public class MartialCongressManager {

    /**
     * Thể hiện duy nhất của lớp MartialCongressManager (mô hình Singleton).
     */
    private static MartialCongressManager i;

    /**
     * Thời điểm cập nhật lần cuối (mili giây).
     */
    private long lastUpdate;

    /**
     * Danh sách các giải đấu võ thuật đang diễn ra.
     */
    private static List<MartialCongress> list = new ArrayList<>();

    /**
     * Danh sách các giải đấu võ thuật cần xóa.
     */
    private static List<MartialCongress> toRemove = new ArrayList<>();

    /**
     * Lấy thể hiện duy nhất của lớp MartialCongressManager.
     * @return Thể hiện của MartialCongressManager.
     */
    public static MartialCongressManager gI() {
        if (i == null) {
            i = new MartialCongressManager();
        }
        return i;
    }

    /**
     * Cập nhật trạng thái của tất cả các giải đấu võ thuật, xóa các giải đấu đã hoàn thành.
     */
    public void update() {
        if (Util.canDoWithTime(lastUpdate, 1000)) {
            lastUpdate = System.currentTimeMillis();
            synchronized (list) {
                for (MartialCongress mc : list) {
                    try {
                        mc.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                list.removeAll(toRemove);
            }
        }
    }

    /**
     * Thêm một giải đấu võ thuật vào danh sách quản lý.
     * @param mc Giải đấu võ thuật cần thêm.
     */
    public void add(MartialCongress mc) {
        synchronized (list) {
            list.add(mc);
        }
    }

    /**
     * Đánh dấu một giải đấu võ thuật để xóa khỏi danh sách.
     * @param mc Giải đấu võ thuật cần xóa.
     */
    public void remove(MartialCongress mc) {
        synchronized (toRemove) {
            toRemove.add(mc);
        }
    }
}