package com.girlkun.network.session;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp quản lý danh sách các phiên làm việc mạng (Session).
 * Cung cấp chức năng khởi tạo và lấy thể hiện duy nhất của lớp theo mẫu Singleton.
 *
 * @author Lucifer
 */
public class SessionManager {

    /**
     * Thể hiện duy nhất của lớp SessionManager (singleton pattern).
     */
    private static SessionManager i;

    /**
     * Danh sách chứa các phiên làm việc hiện tại.
     */
    private List<Session> sessions;

    /**
     * Lấy thể hiện duy nhất của SessionManager (singleton pattern).
     *
     * @return thể hiện SessionManager
     */
    public static SessionManager gI() {
        if (i == null) {
            i = new SessionManager();
        }
        return i;
    }

    /**
     * Khởi tạo SessionManager với danh sách phiên làm việc rỗng.
     */
    public SessionManager() {
        this.sessions = new ArrayList<>();
    }
}