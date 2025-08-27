package com.girlkun.network.handler;

import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;

/**
 * Interface định nghĩa phương thức xử lý tin nhắn mạng trong game.
 * @author Lucifer
 */
public interface IMessageHandler {

    /**
     * Xử lý tin nhắn nhận được từ phiên kết nối mạng.
     *
     * @param paramISession Phiên kết nối mạng.
     * @param paramMessage Tin nhắn cần xử lý.
     * @throws Exception Nếu xảy ra lỗi trong quá trình xử lý tin nhắn.
     */
    void onMessage(ISession paramISession, Message paramMessage) throws Exception;
}