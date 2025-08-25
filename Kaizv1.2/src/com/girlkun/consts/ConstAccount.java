package com.girlkun.consts;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ConstAccount:
 * Lớp ConstAccount thuộc package com.girlkun.consts, định nghĩa các hằng số tĩnh (static final) liên quan đến trạng thái tài khoản trong game. Các hằng số này được sử dụng để biểu thị các trạng thái khác nhau của tài khoản, như trạng thái mặc định, đang trong game, có thể đăng nhập, tài khoản sai, hoặc bị cấm.
 */
public class ConstAccount {
    /**
     * DEFAUT: byte - Trạng thái mặc định của tài khoản, giá trị -1.
     * - Được sử dụng khi tài khoản chưa được xác định trạng thái cụ thể.
     */
    public static final byte DEFAUT = -1;

    /**
     * IS_INGAME: byte - Trạng thái tài khoản đang trong game, giá trị 0.
     * - Biểu thị rằng tài khoản hiện đang được sử dụng trong trò chơi.
     */
    public static final byte IS_INGAME = 0;

    /**
     * CAN_LOGIN: byte - Trạng thái tài khoản có thể đăng nhập, giá trị 1.
     * - Biểu thị rằng tài khoản hợp lệ và sẵn sàng để đăng nhập.
     */
    public static final byte CAN_LOGIN = 1;

    /**
     * WRONG_ACCOUNT: byte - Trạng thái tài khoản sai (ví dụ: sai tên đăng nhập hoặc mật khẩu), giá trị 2.
     * - Được sử dụng khi thông tin đăng nhập không chính xác.
     */
    public static final byte WRONG_ACCOUNT = 2;

    /**
     * IS_BAN: byte - Trạng thái tài khoản bị cấm, giá trị 3.
     * - Biểu thị rằng tài khoản đã bị khóa và không thể đăng nhập.
     */
    public static final byte IS_BAN = 3;
}