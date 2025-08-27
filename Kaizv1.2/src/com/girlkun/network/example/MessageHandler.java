package com.girlkun.network.example;

import com.girlkun.network.handler.IMessageHandler;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;

/**
 * Lớp xử lý các tin nhắn mạng nhận được trong game.
 * @author Lucifer
 */
public class MessageHandler implements IMessageHandler {

    /**
     * Xử lý tin nhắn nhận được từ phiên kết nối mạng.
     *
     * @param session Phiên kết nối mạng.
     * @param msg Tin nhắn nhận được.
     * @throws Exception Nếu xảy ra lỗi trong quá trình xử lý tin nhắn.
     */
    @Override
    public void onMessage(ISession session, Message msg) throws Exception {
        try {
            System.out.println(msg.reader().readUTF());
            msg.cleanup();
        } catch (Exception e) {

            throw e;
        }
    }
}