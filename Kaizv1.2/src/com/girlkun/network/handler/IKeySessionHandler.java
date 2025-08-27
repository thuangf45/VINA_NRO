package com.girlkun.network.handler;

import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;

/**
 * Interface định nghĩa các phương thức xử lý khóa mã hóa cho phiên kết nối mạng trong game.
 * @author Lucifer
 */
public interface IKeySessionHandler {

    /**
     * Gửi khóa mã hóa đến phiên kết nối.
     *
     * @param paramISession Phiên kết nối mạng.
     */
    void sendKey(ISession paramISession);

    /**
     * Thiết lập khóa mã hóa cho phiên kết nối từ tin nhắn nhận được.
     *
     * @param paramISession Phiên kết nối mạng.
     * @param paramMessage Tin nhắn chứa dữ liệu khóa.
     * @throws Exception Nếu xảy ra lỗi trong quá trình thiết lập khóa.
     */
    void setKey(ISession paramISession, Message paramMessage) throws Exception;
}