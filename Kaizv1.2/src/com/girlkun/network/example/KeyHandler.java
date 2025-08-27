package com.girlkun.network.example;

import com.girlkun.network.handler.IKeySessionHandler;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.network.CommandMessage;

/**
 * Lớp xử lý các thao tác liên quan đến khóa mã hóa trong giao tiếp mạng của game.
 * @author Lucifer
 */
public class KeyHandler implements IKeySessionHandler {

    /**
     * Gửi khóa mã hóa đến phiên kết nối.
     *
     * @param session Phiên kết nối mạng.
     */
    @Override
    public void sendKey(ISession session) {
        Message msg = new Message(CommandMessage.REQUEST_KEY);
        try {
            byte[] KEYS = session.getKey();
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; i++) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            session.doSendMessage(msg);
            msg.cleanup();
            session.setSentKey(true);
        } catch (Exception exception) {

        }
    }

    /**
     * Thiết lập khóa mã hóa cho phiên kết nối từ tin nhắn nhận được.
     *
     * @param session Phiên kết nối mạng.
     * @param message Tin nhắn chứa dữ liệu khóa.
     * @throws Exception Nếu xảy ra lỗi trong quá trình đọc dữ liệu.
     */
    @Override
    public void setKey(ISession session, Message message) throws Exception {
        try {
            byte b = message.reader().readByte();
            byte[] KEYS = new byte[b];
            for (int i = 0; i < b; i++) {
                KEYS[i] = message.reader().readByte();
            }
            for (int j = 0; j < KEYS.length - 1; j++) {
                KEYS[j + 1] = (byte)(KEYS[j + 1] ^ KEYS[j]);
            }
            session.setKey(KEYS);
            session.setSentKey(true);
        } catch (Exception exception) {

            throw exception;
        }
    }
}