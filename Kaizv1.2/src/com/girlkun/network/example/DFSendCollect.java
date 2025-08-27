package com.girlkun.network.example;

import com.girlkun.network.handler.IMessageSendCollect;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Lớp thực hiện việc thu thập và gửi tin nhắn qua mạng trong game.
 * @author Lucifer
 */
public class DFSendCollect implements IMessageSendCollect {

    /**
     * Đọc dữ liệu tin nhắn từ luồng đầu vào và tạo đối tượng Message.
     *
     * @param session Phiên kết nối mạng.
     * @param dis Luồng dữ liệu đầu vào.
     * @return Đối tượng Message chứa dữ liệu đã đọc.
     * @throws Exception Nếu xảy ra lỗi trong quá trình đọc dữ liệu.
     */
    @Override
    public Message readMessage(ISession session, DataInputStream dis) throws Exception {
        byte cmd = dis.readByte();
        int size = dis.readInt();
        byte[] data = new byte[size];
        int len = 0;
        int byteRead = 0;
        while (len != -1 && byteRead < size) {
            len = dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
                byteRead += len;
            }
        }
        return new Message(cmd, data);
    }

    /**
     * Đọc khóa mã hóa từ dữ liệu đầu vào.
     *
     * @param session Phiên kết nối mạng.
     * @param b Byte cần đọc khóa.
     * @return Giá trị khóa, mặc định trả về -1.
     */
    @Override
    public byte readKey(ISession session, byte b) {
        return -1;
    }

    /**
     * Gửi tin nhắn qua luồng đầu ra.
     *
     * @param session Phiên kết nối mạng.
     * @param dos Luồng dữ liệu đầu ra.
     * @param msg Tin nhắn cần gửi.
     * @throws Exception Nếu xảy ra lỗi trong quá trình gửi dữ liệu.
     */
    @Override
    public void doSendMessage(ISession session, DataOutputStream dos, Message msg) throws Exception {
        try {
            byte[] data = msg.getData();
            dos.writeByte(msg.command);
            if (data != null) {
                dos.writeInt(data.length);
                dos.write(data);
            } else {
                dos.writeInt(0);
            }
            dos.flush();
        } catch (Exception exception) {

        }
    }

    /**
     * Ghi khóa mã hóa cho dữ liệu đầu ra.
     *
     * @param session Phiên kết nối mạng.
     * @param b Byte cần ghi khóa.
     * @return Giá trị khóa, mặc định trả về -1.
     */
    @Override
    public byte writeKey(ISession session, byte b) {
        return -1;
    }
}