package com.girlkun.network.session;

import java.net.Socket;

/**
 * Lớp thực hiện mẫu thiết kế Factory để tạo các phiên làm việc mạng.
 * Hỗ trợ tạo bản sao của các phiên làm việc dựa trên lớp được chỉ định.
 *
 * @author Lucifer
 */
public class SessionFactory {

    /**
     * Thể hiện duy nhất của lớp SessionFactory (singleton pattern).
     */
    private static SessionFactory I;

    /**
     * Lấy thể hiện duy nhất của SessionFactory (singleton pattern).
     *
     * @return thể hiện SessionFactory
     */
    public static SessionFactory gI() {
        if (I == null) {
            I = new SessionFactory();
        }
        return I;
    }

    /**
     * Tạo một bản sao phiên làm việc từ lớp được chỉ định và socket.
     *
     * @param clazz  lớp của phiên làm việc (phải triển khai ISession)
     * @param socket socket kết nối với client
     * @return phiên làm việc được tạo
     * @throws Exception nếu lớp không hợp lệ hoặc không thể tạo instance
     */
    public ISession cloneSession(Class<ISession> clazz, Socket socket) throws Exception {
        return clazz.getConstructor(new Class[] { Socket.class }).newInstance(new Object[] { socket });
    }
}