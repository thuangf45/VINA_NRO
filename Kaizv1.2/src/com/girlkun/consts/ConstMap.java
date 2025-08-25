package com.girlkun.consts;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ConstMap:
 * Lớp ConstMap thuộc package com.girlkun.consts, định nghĩa các hằng số tĩnh (static final) liên quan đến các loại bản đồ và các kiểu chuyển đổi bản đồ trong game. Các hằng số này được sử dụng để xác định đặc điểm của bản đồ (ví dụ: bản đồ thường, doanh trại, hay bản đồ kho báu) và các kiểu chuyển đổi bản đồ (như sử dụng viên capsule hoặc tham gia sự kiện). Ngoài ra, lớp cũng định nghĩa thời gian hỗ trợ cho một số hoạt động trong game.
 */
public class ConstMap {
    /**
     * TILE_TOP: int - Hằng số biểu thị thuộc tính của ô bản đồ, giá trị 2.
     * - Có thể được sử dụng để xác định các ô đặc biệt hoặc giới hạn trên bản đồ (ví dụ: ô không thể đi qua).
     */
    public static final int TILE_TOP = 2;

    // Type map
    /**
     * MAP_NORMAL: byte - Loại bản đồ thường, giá trị 0.
     * - Biểu thị các bản đồ thông thường trong game.
     */
    public static final byte MAP_NORMAL = 0;

    /**
     * MAP_OFFLINE: byte - Loại bản đồ offline, giá trị 1.
     * - Biểu thị các bản đồ dùng trong chế độ ngoại tuyến.
     */
    public static final byte MAP_OFFLINE = 1;

    /**
     * MAP_DOANH_TRAI: byte - Loại bản đồ doanh trại, giá trị 2.
     * - Biểu thị các bản đồ thuộc doanh trại, nơi bang hội tham gia chiến đấu.
     */
    public static final byte MAP_DOANH_TRAI = 2;

    /**
     * MAP_BLACK_BALL_WAR: byte - Loại bản đồ chiến tranh ngọc rồng đen, giá trị 3.
     * - Biểu thị các bản đồ dùng trong sự kiện chiến tranh ngọc rồng đen.
     */
    public static final byte MAP_BLACK_BALL_WAR = 3;

    /**
     * MAP_BAN_DO_KHO_BAU: byte - Loại bản đồ kho báu, giá trị 4.
     * - Biểu thị các bản đồ liên quan đến sự kiện kho báu.
     */
    public static final byte MAP_BAN_DO_KHO_BAU = 4;

    /**
     * MAP_MA_BU: byte - Loại bản đồ Ma Bư, giá trị 5.
     * - Biểu thị các bản đồ liên quan đến sự kiện hoặc khu vực Ma Bư.
     */
    public static final byte MAP_MA_BU = 5;

    /**
     * MAP_KHI_GAS: byte - Loại bản đồ khí gas, giá trị 6.
     * - Biểu thị các bản đồ liên quan đến sự kiện hoặc khu vực khí gas.
     */
    public static final byte MAP_KHI_GAS = 6;

    /**
     * MAP_DAO_KHO_BAU: byte - Loại bản đồ đảo kho báu, giá trị 7.
     * - Biểu thị các bản đồ liên quan đến sự kiện đảo kho báu.
     */
    public static final byte MAP_DAO_KHO_BAU = 7;

    // Type change map
    /**
     * CHANGE_CAPSULE: int - Loại chuyển đổi bản đồ bằng viên capsule, giá trị 500.
     * - Biểu thị hành động chuyển đổi bản đồ sử dụng viên capsule.
     */
    public static final int CHANGE_CAPSULE = 500;

    /**
     * CHANGE_BLACK_BALL: int - Loại chuyển đổi bản đồ liên quan đến ngọc rồng đen, giá trị 501.
     * - Biểu thị hành động chuyển đổi bản đồ trong sự kiện ngọc rồng đen.
     */
    public static final int CHANGE_BLACK_BALL = 501;

    /**
     * CHANGE_MAP_MA_BU: int - Loại chuyển đổi bản đồ liên quan đến Ma Bư, giá trị 502.
     * - Biểu thị hành động chuyển đổi bản đồ trong sự kiện Ma Bư.
     */
    public static final int CHANGE_MAP_MA_BU = 502;

    /**
     * TIME_START_SUPPORT: int - Thời gian bắt đầu hỗ trợ (giờ), giá trị 17.
     * - Biểu thị thời gian bắt đầu (17:00) cho một số hoạt động hoặc sự kiện trong game.
     */
    public static final int TIME_START_SUPPORT = 17;

    /**
     * TIME_END_SUPPORT: int - Thời gian kết thúc hỗ trợ (giờ), giá trị 21.
     * - Biểu thị thời gian kết thúc (21:00) cho một số hoạt động hoặc sự kiện trong game.
     */
    public static final int TIME_END_SUPPORT = 21;
}