package com.girlkun.network.server;

import com.girlkun.network.session.ISession;

/**
 * Giao diện định nghĩa các phương thức để xử lý sự kiện liên quan đến phiên làm việc,
 * bao gồm khởi tạo và ngắt kết nối phiên làm việc.
 *
 * @author Lucifer
 */
public interface ISessionAcceptHandler {

    /**
     * Khởi tạo một phiên làm việc mới khi kết nối được thiết lập.
     *
     * @param paramISession phiên làm việc cần khởi tạo
     */
    void sessionInit(ISession paramISession);

    /**
     * Xử lý sự kiện khi một phiên làm việc bị ngắt kết nối.
     *
     * @param paramISession phiên làm việc bị ngắt kết nối
     */
    void sessionDisconnect(ISession paramISession);
}
