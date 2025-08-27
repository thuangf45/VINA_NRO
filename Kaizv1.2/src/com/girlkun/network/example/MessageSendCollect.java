package com.girlkun.network.example;

import com.girlkun.network.handler.IMessageSendCollect;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Lớp xử lý việc thu thập và gửi tin nhắn qua mạng với hỗ trợ mã hóa khóa trong game.
 * @author Lucifer
 */
public class MessageSendCollect implements IMessageSendCollect {

    /** Chỉ số hiện tại của khóa khi đọc dữ liệu. */
    private int curR = 0;

    /** Chỉ số hiện tại của khóa khi ghi dữ liệu. */
    private int curW = 0;

    /**
     * Đọc dữ liệu tin nhắn từ luồng đầu vào, hỗ trợ giải mã nếu đã gửi khóa.
     *
     * @param session Phiên kết nối mạng.
     * @param dis Luồng dữ liệu đầu vào.
     * @return Đối tượng Message chứa dữ liệu đã đọc.
     * @throws Exception Nếu xảy ra lỗi trong quá trình đọc dữ liệu.
     */
    @Override
    public Message readMessage(ISession session, DataInputStream dis) throws Exception {
        byte cmd = dis.readByte();
        if (session.sentKey()) {
            cmd = readKey(session, cmd);
        }

        int size;
        if (session.sentKey()) {
            byte b1 = dis.readByte();
            byte b2 = dis.readByte();
            size = (readKey(session, b1) & 0xFF) << 8 | readKey(session, b2) & 0xFF;
        } else {
            size = dis.readUnsignedShort();
        }

        byte[] data = new byte[size];
        int len = 0;
        int byteRead = 0;
        while (len != -1 && byteRead < size) {
            len = dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
                byteRead += len;
            }
        }

        if (session.sentKey()) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readKey(session, data[i]);
            }
        }

        return new Message(cmd, data);
    }

    /**
     * Giải mã một byte dữ liệu bằng khóa của phiên kết nối.
     *
     * @param session Phiên kết nối mạng.
     * @param b Byte cần giải mã.
     * @return Byte đã giải mã.
     */
    @Override
    public byte readKey(ISession session, byte b) {
        byte i = (byte) (session.getKey()[this.curR++] & 0xFF ^ b & 0xFF);
        if (this.curR >= session.getKey().length) {
            this.curR %= session.getKey().length;
        }
        return i;
    }

    /**
     * Gửi tin nhắn qua luồng đầu ra, hỗ trợ mã hóa nếu đã gửi khóa.
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
            if (session.sentKey()) {
                byte b = writeKey(session, msg.command);
                dos.writeByte(b);
            } else {
                dos.writeByte(msg.command);
            }

            if (data != null) {
                int size = data.length;
                if (msg.command == -32 || msg.command == -66 || msg.command == -74 || msg.command == 11
                        || msg.command == -67 || msg.command == -87 || msg.command == 66) {
                    byte b = writeKey(session, (byte) size);
                    dos.writeByte(b - 128);
                    byte b2 = writeKey(session, (byte) (size >> 8));
                    dos.writeByte(b2 - 128);
                    byte b3 = writeKey(session, (byte) (size >> 16));
                    dos.writeByte(b3 - 128);
                } else if (session.sentKey()) {
                    int byte1 = writeKey(session, (byte) (size >> 8));
                    dos.writeByte(byte1);
                    int byte2 = writeKey(session, (byte) (size & 0xFF));
                    dos.writeByte(byte2);
                } else {
                    dos.writeShort(size);
                }

                if (session.sentKey()) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = writeKey(session, data[i]);
                    }
                }
                dos.write(data);
            } else {
                dos.writeShort(0);
            }

            dos.flush();
            msg.cleanup();
        } catch (Exception exception) {
            
            throw exception;
        }
    }

    /**
     * Mã hóa một byte dữ liệu bằng khóa của phiên kết nối.
     *
     * @param session Phiên kết nối mạng.
     * @param b Byte cần mã hóa.
     * @return Byte đã mã hóa.
     */
    @Override
    public byte writeKey(ISession session, byte b) {
        byte i = (byte) (session.getKey()[this.curW++] & 0xFF ^ b & 0xFF);
        if (this.curW >= session.getKey().length) {
            this.curW %= session.getKey().length;
        }
        return i;
    }
}