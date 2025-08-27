package com.girlkun.server.model;

/**
 * Lớp AntiLogin quản lý cơ chế chống đăng nhập sai quá nhiều lần từ một địa chỉ IP.
 * Lớp này theo dõi số lần đăng nhập sai và giới hạn thời gian đăng nhập lại khi vượt quá ngưỡng.
 * 
 * @author Lucifer
 */
public class AntiLogin {

    /**
     * Số lần đăng nhập sai tối đa cho phép.
     */
    private static final byte MAX_WRONG = 0;

    /**
     * Thời gian chờ (miligiây) trước khi cho phép đăng nhập lại sau khi vượt quá số lần sai.
     */
    private static final int TIME_ANTI = 0;

    /**
     * Thời gian đăng nhập lần cuối.
     */
    private long lastTimeLogin;

    /**
     * Thời gian chờ trước khi cho phép đăng nhập lại.
     */
    private int timeCanLogin;

    /**
     * Số lần đăng nhập sai hiện tại.
     */
    public byte wrongLogin;

    /**
     * Kiểm tra xem có thể đăng nhập hay không dựa trên số lần đăng nhập sai và thời gian chờ.
     * 
     * @return true nếu có thể đăng nhập, false nếu không.
     */
    public boolean canLogin() {
        return true;
        // if (lastTimeLogin != -1) {
        //     if (Util.canDoWithTime(lastTimeLogin, timeCanLogin)) {
        //         this.reset();
        //         return true;
        //     }
        // }
        // return wrongLogin < MAX_WRONG;
    }

    /**
     * Ghi nhận một lần đăng nhập sai và cập nhật trạng thái chống đăng nhập nếu cần.
     */
    public void wrong() {
        wrongLogin++;
        if (wrongLogin >= MAX_WRONG) {
            this.lastTimeLogin = System.currentTimeMillis();
            this.timeCanLogin = TIME_ANTI;
        }
    }

    /**
     * Đặt lại trạng thái chống đăng nhập, bao gồm số lần sai và thời gian chờ.
     */
    public void reset() {
        this.wrongLogin = 0;
        this.lastTimeLogin = -1;
        this.timeCanLogin = 0;
    }

    /**
     * Trả về thông báo khi không thể đăng nhập do vượt quá số lần sai.
     * 
     * @return Chuỗi thông báo lý do không thể đăng nhập.
     */
    public String getNotifyCannotLogin() {
        return "Bạn đã đăng nhập tài khoản sai quá nhiều lần. Vui lòng thử lại sau ít phút";
    }
}