package com.girlkun.network.server;

import com.girlkun.network.session.ISession;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp quản lý danh sách các phiên làm việc (session) trong hệ thống mạng.
 * Cung cấp các phương thức để thêm, xóa, tìm kiếm và lấy thông tin về các phiên làm việc.
 *
 * @author Lucifer
 */
public class GirlkunSessionManager {

    /**
     * Thể hiện duy nhất của lớp GirlkunSessionManager (singleton pattern).
     */
    private static GirlkunSessionManager i;

    /**
     * Danh sách chứa các phiên làm việc hiện tại.
     */
    private final List<ISession> sessions;

    /**
     * Lấy thể hiện duy nhất của GirlkunSessionManager (singleton pattern).
     *
     * @return thể hiện GirlkunSessionManager
     */
    public static GirlkunSessionManager gI() {
        if (i == null) {
            i = new GirlkunSessionManager();
        }
        return i;
    }

    /**
     * Khởi tạo GirlkunSessionManager với danh sách phiên làm việc rỗng.
     */
    public GirlkunSessionManager() {
        this.sessions = new ArrayList<>();
    }

    /**
     * Thêm một phiên làm việc vào danh sách.
     *
     * @param session phiên làm việc cần thêm
     */
    public void putSession(ISession session) {
        this.sessions.add(session);
    }

    /**
     * Xóa một phiên làm việc khỏi danh sách.
     *
     * @param session phiên làm việc cần xóa
     */
    public void removeSession(ISession session) {
        this.sessions.remove(session);
    }

    /**
     * Lấy danh sách tất cả các phiên làm việc hiện tại.
     *
     * @return danh sách các phiên làm việc
     */
    public List<ISession> getSessions() {
        return this.sessions;
    }

    /**
     * Tìm kiếm một phiên làm việc theo ID.
     *
     * @param id ID của phiên làm việc cần tìm
     * @return phiên làm việc tương ứng
     * @throws Exception nếu phiên làm việc không tồn tại
     */
    public ISession findByID(long id) throws Exception {
        if (this.sessions.isEmpty()) {
            throw new Exception("Session " + id + " không tồn tại");
        }
        for (ISession session : this.sessions) {
            if (session.getID() > id) {
                throw new Exception("Session " + id + " không tồn tại");
            }
            if (session.getID() == id) {
                return session;
            }
        }
        throw new Exception("Session " + id + " không tồn tại");
    }

    /**
     * Lấy số lượng phiên làm việc hiện tại.
     *
     * @return số lượng phiên làm việc
     */
    public int getNumSession() {
        return this.sessions.size();
    }
}