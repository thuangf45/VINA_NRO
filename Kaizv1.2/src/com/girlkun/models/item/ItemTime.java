package com.girlkun.models.item;

import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.services.ItemTimeService;

/**
 * Quản lý thời gian hiệu lực của các item đặc biệt (buff, vật phẩm sự kiện, EXP, v.v).
 * @author Lucifer
 */
public class ItemTime {

    /** ID item doanh trại */
    public static final byte DOANH_TRAI = 0;
    /** ID item bản đồ kho báu */
    public static final byte BAN_DO_KHO_BAU = 1;
    /** Thời gian hiệu lực mặc định của item (10 phút) */
    public static final int TIME_ITEM = 600000;
    /** Thời gian mở giới hạn sức mạnh (1 ngày) */
    public static final int TIME_OPEN_POWER = 86400000;
    /** ID khí gas */
    public static final byte KHI_GASS = 2;
    /** Thời gian hiệu lực máy dò (30 phút) */
    public static final int TIME_MAY_DO = 1800000;
    /** Thời gian hiệu lực máy dò 2 (30 phút) */
    public static final int TIME_MAY_DO2 = 1800000;
    /** Thời gian hiệu lực ăn bữa ăn (10 phút) */
    public static final int TIME_EAT_MEAL = 600000;
    /** Thời gian đuôi khỉ (5 phút) */
    public static final int TIME_DUOI_KHI = 300000;

    /** Người chơi sở hữu ItemTime */
    private Player player;

    /** Cờ trạng thái buff Bổ Huyết */
    public boolean isUseBoHuyet;
    /** Cờ trạng thái buff Bổ Khí */
    public boolean isUseBoKhi;
    /** Cờ trạng thái buff Giáp Xên */
    public boolean isUseGiapXen;
    /** Cờ trạng thái buff Cuồng Nộ */
    public boolean isUseCuongNo;
    /** Cờ trạng thái buff Ẩn Danh */
    public boolean isUseAnDanh;
    /** Cờ trạng thái buff Bổ Huyết 2 */
    public boolean isUseBoHuyet2;
    /** Cờ trạng thái buff Bổ Khí 2 */
    public boolean isUseBoKhi2;
    /** Cờ trạng thái buff Giáp Xên 2 */
    public boolean isUseGiapXen2;
    /** Cờ trạng thái buff Cuồng Nộ 2 */
    public boolean isUseCuongNo2;
    /** Cờ trạng thái buff Ẩn Danh 2 */
    public boolean isUseAnDanh2;
    /** Cờ trạng thái buff Đại Việt */
    public boolean isdaiviet;

    /** Thời gian lần cuối sử dụng Bổ Huyết */
    public long lastTimeBoHuyet;
    /** Thời gian lần cuối sử dụng Bổ Khí */
    public long lastTimeBoKhi;
    /** Thời gian lần cuối sử dụng Giáp Xên */
    public long lastTimeGiapXen;
    /** Thời gian lần cuối sử dụng Cuồng Nộ */
    public long lastTimeCuongNo;
    /** Thời gian lần cuối sử dụng Ẩn Danh */
    public long lastTimeAnDanh;
    /** Thời gian lần cuối sử dụng Đại Việt */
    public long lastdaiviet1;
    /** Thời gian lần cuối sử dụng Bổ Huyết 2 */
    public long lastTimeBoHuyet2;
    /** Thời gian lần cuối sử dụng Bổ Khí 2 */
    public long lastTimeBoKhi2;
    /** Thời gian lần cuối sử dụng Giáp Xên 2 */
    public long lastTimeGiapXen2;
    /** Thời gian lần cuối sử dụng Cuồng Nộ 2 */
    public long lastTimeCuongNo2;
    /** Thời gian lần cuối sử dụng Ẩn Danh 2 */
    public long lastTimeAnDanh2;

    /** Cờ trạng thái dùng máy dò */
    public boolean isUseMayDo;
    /** Thời gian lần cuối dùng máy dò */
    public long lastTimeUseMayDo;
    /** Cờ trạng thái dùng máy dò 2 */
    public boolean isUseMayDo2;
    /** Thời gian lần cuối dùng máy dò 2 */
    public long lastTimeUseMayDo2;

    /** Cờ trạng thái mở giới hạn sức mạnh */
    public boolean isOpenPower;
    /** Thời gian lần cuối mở giới hạn sức mạnh */
    public long lastTimeOpenPower;

    /** Cờ trạng thái dùng TDLT */
    public boolean isUseTDLT;
    /** Thời gian lần cuối dùng TDLT */
    public long lastTimeUseTDLT;
    /** Thời gian hiệu lực TDLT */
    public int timeTDLT;

    /** Cờ trạng thái đang ăn bữa ăn */
    public boolean isEatMeal;
    /** Thời gian lần cuối ăn bữa ăn */
    public long lastTimeEatMeal;
    /** Icon của bữa ăn */
    public int iconMeal;

    /** Thời gian lần cuối x2 EXP */
    public long lastX2EXP;
    /** Cờ trạng thái x2 EXP */
    public boolean isX2EXP;
    /** Thời gian lần cuối x3 EXP */
    public long lastX3EXP;
    /** Cờ trạng thái x3 EXP */
    public boolean isX3EXP;
    /** Thời gian lần cuối x5 EXP */
    public long lastX5EXP;
    /** Cờ trạng thái x5 EXP */
    public boolean isX5EXP;
    /** Thời gian lần cuối x7 EXP */
    public long lastX7EXP;
    /** Cờ trạng thái x7 EXP */
    public boolean isX7EXP;

    /** Thời gian lần cuối sử dụng Đại Việt */
    public long lastdaiviet;
    /** Icon mặc định cho buff X2EXP */
    public int IconX2EXP = 21881;

    /**
     * Khởi tạo ItemTime cho một người chơi.
     *
     * @param player Người chơi
     */
    public ItemTime(Player player) {
        this.player = player;
    }

    /**
     * Cập nhật trạng thái item buff theo thời gian.
     * Kiểm tra xem buff đã hết hạn chưa và tắt nếu cần.
     */
    public void update() {
        if (isX3EXP && Util.canDoWithTime(lastX3EXP, TIME_MAY_DO)) {
            isX3EXP = false;
        }
        if (isX5EXP && Util.canDoWithTime(lastX5EXP, TIME_MAY_DO)) {
            isX5EXP = false;
        }
        if (isX7EXP && Util.canDoWithTime(lastX7EXP, TIME_MAY_DO)) {
            isX7EXP = false;
        }
        if (isX2EXP && Util.canDoWithTime(lastX2EXP, TIME_MAY_DO)) {
            isX2EXP = false;
        }
        if (isEatMeal && Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
            isEatMeal = false;
            Service.gI().point(player);
        }
        if (isUseBoHuyet && Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
            isUseBoHuyet = false;
            Service.gI().point(player);
        }
        if (isUseBoKhi && Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
            isUseBoKhi = false;
            Service.gI().point(player);
        }
        if (isUseGiapXen && Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
            isUseGiapXen = false;
        }
        if (isdaiviet && Util.canDoWithTime(lastdaiviet1, TIME_MAY_DO)) {
            isdaiviet = false;
        }
        if (isUseCuongNo && Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
            isUseCuongNo = false;
            Service.gI().point(player);
        }
        if (isUseAnDanh && Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
            isUseAnDanh = false;
        }
        if (isUseBoHuyet2 && Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
            isUseBoHuyet2 = false;
            Service.gI().point(player);
        }
        if (isUseBoKhi2 && Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
            isUseBoKhi2 = false;
            Service.gI().point(player);
        }
        if (isUseGiapXen2 && Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
            isUseGiapXen2 = false;
        }
        if (isUseCuongNo2 && Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
            isUseCuongNo2 = false;
            Service.gI().point(player);
        }
        if (isUseAnDanh2 && Util.canDoWithTime(lastTimeAnDanh2, TIME_ITEM)) {
            isUseAnDanh2 = false;
        }
        if (isOpenPower && Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
            player.nPoint.limitPower++;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            isOpenPower = false;
        }
        if (isUseMayDo && Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
            isUseMayDo = false;
        }
        if (isUseMayDo2 && Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
            isUseMayDo2 = false;
        }
        if (isUseTDLT && Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
            this.isUseTDLT = false;
            ItemTimeService.gI().sendCanAutoPlay(this.player);
        }
    }

    /**
     * Giải phóng bộ nhớ khi không còn sử dụng ItemTime.
     */
    public void dispose() {
        this.player = null;
    }
}
