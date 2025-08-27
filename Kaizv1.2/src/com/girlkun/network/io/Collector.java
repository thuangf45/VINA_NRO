package com.girlkun.network.io;

import com.girlkun.network.CommandMessage;
import com.girlkun.network.handler.IMessageHandler;
import com.girlkun.network.handler.IMessageSendCollect;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.session.ISession;
import com.girlkun.network.session.TypeSession;
import java.io.DataInputStream;
import java.net.Socket;

/**
 * Lớp xử lý việc thu thập và xử lý tin nhắn mạng trong game, chạy trong một luồng riêng.
 * @author Lucifer
 */
public class Collector implements Runnable {

    /** Phiên kết nối mạng. */
    private ISession session;

    /** Luồng dữ liệu đầu vào từ socket. */
    private DataInputStream dis;

    /** Đối tượng xử lý việc thu thập và gửi tin nhắn. */
    private IMessageSendCollect collect;

    /** Đối tượng xử lý tin nhắn nhận được. */
    private IMessageHandler messageHandler;

    /**
     * Khởi tạo đối tượng Collector với phiên kết nối và socket.
     *
     * @param session Phiên kết nối mạng.
     * @param socket Socket dùng để giao tiếp mạng.
     */
    public Collector(ISession session, Socket socket) {
        this.session = session;
        setSocket(socket);
    }

    /**
     * Thiết lập socket và khởi tạo luồng dữ liệu đầu vào.
     *
     * @param socket Socket dùng để giao tiếp mạng.
     * @return Đối tượng Collector hiện tại.
     */
    public Collector setSocket(Socket socket) {
        try {
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (Exception exception) {

        }
        return this;
    }

    /**
     * Chạy luồng xử lý tin nhắn mạng, liên tục đọc và xử lý các tin nhắn từ phiên kết nối.
     */
    @Override
    public void run() {
        try {
            while (true) {
                if (this.session.isConnected()) {
                    Message msg = this.collect.readMessage(this.session, this.dis);
                    if (msg.command == CommandMessage.REQUEST_KEY) {
                        if (this.session.getTypeSession() == TypeSession.SERVER) {
                            this.session.sendKey();
                        } else {
                            this.session.setKey(msg);
                        }
                    } else {
                        this.messageHandler.onMessage(this.session, msg);
                    }
                    msg.cleanup();
                }
                Thread.sleep(1L);
            }
        } catch (Exception ex) {
            try {
                GirlkunServer.gI().getAcceptHandler().sessionDisconnect(this.session);
            } catch (Exception exception) {

            }
            if (this.session != null) {
                System.out.println("Mất kết nối với session " + this.session.getIP() + "...");
                this.session.disconnect();
            }
        }
    }

    /**
     * Thiết lập đối tượng xử lý việc thu thập và gửi tin nhắn.
     *
     * @param collect Đối tượng IMessageSendCollect.
     */
    public void setCollect(IMessageSendCollect collect) {
        this.collect = collect;
    }

    /**
     * Thiết lập đối tượng xử lý tin nhắn.
     *
     * @param handler Đối tượng IMessageHandler.
     */
    public void setMessageHandler(IMessageHandler handler) {
        this.messageHandler = handler;
    }

    /**
     * Đóng luồng dữ liệu đầu vào.
     */
    public void close() {
        if (this.dis != null) {
            try {
                this.dis.close();
            } catch (Exception exception) {

            }
        }
    }

    /**
     * Giải phóng tài nguyên của đối tượng Collector.
     */
    public void dispose() {
        this.session = null;
        this.dis = null;
        this.collect = null;
    }
}