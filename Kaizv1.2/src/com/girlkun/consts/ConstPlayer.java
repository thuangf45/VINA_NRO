package com.girlkun.consts;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ConstPlayer:
 * Lớp ConstPlayer thuộc package com.girlkun.consts, định nghĩa các hằng số tĩnh (static final) liên quan đến người chơi trong game. Các hằng số này bao gồm mảng ID của đầu khỉ (dùng cho biến hình), các loại hành tinh của nhân vật, các loại PK (Player Killing), và các loại hợp thể (fusion). Lớp cung cấp các giá trị cố định để xử lý logic liên quan đến người chơi trong game.
 */
public class ConstPlayer {
    /**
     * HEADMONKEY: int[] - Mảng chứa các ID của đầu khỉ, dùng cho biến hình khỉ trong game.
     * - Giá trị: {192, 195, 196, 199, 197, 200, 198}.
     */
    public static final int[] HEADMONKEY = {192, 195, 196, 199, 197, 200, 198};

    /**
     * TRAI_DAT: byte - ID của hành tinh Trái Đất, giá trị 0.
     * - Biểu thị nhân vật thuộc hành tinh Trái Đất.
     */
    public static final byte TRAI_DAT = 0;

    /**
     * NAMEC: byte - ID của hành tinh Namec, giá trị 1.
     * - Biểu thị nhân vật thuộc hành tinh Namec.
     */
    public static final byte NAMEC = 1;

    /**
     * XAYDA: byte - ID của hành tinh Xayda, giá trị 2.
     * - Biểu thị nhân vật thuộc hành tinh Xayda.
     */
    public static final byte XAYDA = 2;

    // Type PK
    /**
     * NON_PK: byte - Loại không PK, giá trị 0.
     * - Biểu thị người chơi không tham gia chế độ PK (không tấn công người chơi khác).
     */
    public static final byte NON_PK = 0;

    /**
     * PK_PVP: byte - Loại PK trong chế độ PVP, giá trị 3.
     * - Biểu thị người chơi tham gia chế độ PK chỉ trong các trận đấu PVP.
     */
    public static final byte PK_PVP = 3;

    /**
     * PK_ALL: byte - Loại PK toàn bộ, giá trị 5.
     * - Biểu thị người chơi có thể tấn công bất kỳ người chơi nào trong game.
     */
    public static final byte PK_ALL = 5;

    // Type Fusion
    /**
     * NON_FUSION: byte - Loại không hợp thể, giá trị 0.
     * - Biểu thị nhân vật không sử dụng trạng thái hợp thể.
     */
    public static final byte NON_FUSION = 0;

    /**
     * LUONG_LONG_NHAT_THE: byte - Loại hợp thể Lưỡng Long Nhất Thể, giá trị 4.
     * - Biểu thị trạng thái hợp thể đặc biệt của nhân vật.
     */
    public static final byte LUONG_LONG_NHAT_THE = 4;

    /**
     * HOP_THE_PORATA: byte - Loại hợp thể Porata, giá trị 6.
     * - Biểu thị trạng thái hợp thể sử dụng Porata cấp 1.
     */
    public static final byte HOP_THE_PORATA = 6;

    /**
     * HOP_THE_PORATA2: byte - Loại hợp thể Porata cấp 2, giá trị 8.
     * - Biểu thị trạng thái hợp thể sử dụng Porata cấp 2.
     */
    public static final byte HOP_THE_PORATA2 = 8;

    /**
     * HOP_THE_PORATA3: byte - Loại hợp thể Porata cấp 3, giá trị 10.
     * - Biểu thị trạng thái hợp thể sử dụng Porata cấp 3.
     */
    public static final byte HOP_THE_PORATA3 = 10;

    /**
     * HOP_THE_PORATA4: byte - Loại hợp thể Porata cấp 4, giá trị 12.
     * - Biểu thị trạng thái hợp thể sử dụng Porata cấp 4.
     */
    public static final byte HOP_THE_PORATA4 = 12;

    /**
     * NAMEC1: byte - Biến tĩnh không được khởi tạo giá trị cụ thể.
     * - Có thể là ID bổ sung cho hành tinh Namec hoặc một trạng thái liên quan, cần kiểm tra thêm trong mã nguồn.
     */
    public static byte NAMEC1;
}