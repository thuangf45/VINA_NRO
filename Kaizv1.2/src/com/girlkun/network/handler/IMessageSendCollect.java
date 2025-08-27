package com.girlkun.network.handler;

import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Interface định nghĩa các phương thức xử lý việc thu thập và gửi tin nhắn qua mạng, bao gồm mã hóa và giải mã dữ liệu.
 * @author Lucifer
 */
public interface IMessageSendCollect {

    /**
     * Đọc tin nhắn từ luồng dữ liệu đầu vào và tạo đối tượng Message.
     *
     * @param paramISession Phiên kết nối mạng.
     * @param paramDataInputStream Luồng dữ liệu đầu vào.
     * @return Đối tượng Message chứa dữ liệu đã đọc.
     * @throws Exception Nếu xảy ra lỗi trong quá trình đọc dữ liệu.
     */
    Message readMessage(ISession paramISession, DataInputStream paramDataInputStream) throws Exception;

    /**
     * Giải mã một byte dữ liệu bằng khóa của phiên kết nối.
     *
     * @param paramISession Phiên kết nối mạng.
     * @param paramByte Byte cần giải mã.
     * @return Byte đã giải mã.
     */
    byte readKey(ISession paramISession, byte paramByte);

    /**
     * Gửi tin nhắn qua luồng dữ liệu đầu ra, hỗ trợ mã hóa nếu cần.
     *
     * @param paramISession Phiên kết nối mạng.
     * @param paramDataOutputStream Luồng dữ liệu đầu ra.
     * @param paramMessage Tin nhắn cần gửi.
     * @throws Exception Nếu xảy ra lỗi trong quá trình gửi dữ liệu.
     */
    void doSendMessage(ISession paramISession, DataOutputStream paramDataOutputStream, Message paramMessage) throws Exception;

    /**
     * Mã hóa một byte dữ liệu bằng khóa của phiên kết nối.
     *
     * @param paramISession Phiên kết nối mạng.
     * @param paramByte Byte cần mã hóa.
     * @return Byte đã mã hóa.
     */
    byte writeKey(ISession paramISession, byte paramByte);
}