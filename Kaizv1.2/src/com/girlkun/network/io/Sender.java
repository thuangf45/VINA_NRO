package com.girlkun.network.io;

import com.girlkun.network.handler.IMessageSendCollect;
import com.girlkun.network.session.ISession;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Lớp xử lý việc gửi thông điệp qua mạng, chạy trong một luồng riêng biệt.
 * Quản lý danh sách các thông điệp cần gửi và thực hiện gửi thông điệp qua luồng đầu ra.
 *
 * @author Lucifer
 */
public class Sender implements Runnable {

    /**
     * Phiên làm việc mạng, đại diện cho kết nối với client.
     */
    private ISession session;

    /**
     * Danh sách các thông điệp chờ được gửi.
     */
    private ArrayList<Message> messages;

    /**
     * Luồng dữ liệu đầu ra để gửi thông điệp.
     */
    private DataOutputStream dos;

    /**
     * Đối tượng xử lý việc gửi thông điệp, được sử dụng để tùy chỉnh hành vi gửi.
     */
    private IMessageSendCollect sendCollect;

    /**
     * Khởi tạo Sender với phiên làm việc và socket.
     *
     * @param session phiên làm việc mạng
     * @param socket  socket kết nối với client
     */
    public Sender(ISession session, Socket socket) {
        try {
            this.session = session;
            this.messages = new ArrayList<>();
            setSocket(socket);
        } catch (Exception exception) {}
    }

    /**
     * Thiết lập socket và khởi tạo luồng dữ liệu đầu ra.
     *
     * @param socket socket kết nối với client
     * @return đối tượng Sender hiện tại
     */
    public Sender setSocket(Socket socket) {
        try {
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception exception) {}
        return this;
    }

    /**
     * Chạy luồng Sender, liên tục kiểm tra và gửi các thông điệp trong danh sách.
     */
    public void run() {
        while (this.session != null && this.session.isConnected()) {
            try {
                while (this.session != null && this.session.isConnected() && this.messages.size() > 0) {
                    Message message = this.messages.remove(0);
                    if (message != null) {
                        doSendMessage(message);
                    }
                    message = null;
                }
                Thread.sleep(1L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gửi một thông điệp qua luồng đầu ra, sử dụng đối tượng sendCollect.
     *
     * @param message thông điệp cần gửi
     * @throws Exception nếu xảy ra lỗi khi gửi
     */
    public synchronized void doSendMessage(Message message) throws Exception {
        this.sendCollect.doSendMessage(this.session, this.dos, message);
    }

    /**
     * Thêm một thông điệp vào danh sách chờ gửi nếu phiên làm việc đang kết nối.
     *
     * @param msg thông điệp cần gửi
     */
    public synchronized void sendMessage(Message msg) {
        if (this.session != null && this.session.isConnected()) {
            this.messages.add(msg);
        }
    }

    /**
     * Thiết lập đối tượng xử lý gửi thông điệp.
     *
     * @param sendCollect đối tượng IMessageSendCollect để xử lý gửi thông điệp
     */
    public void setSend(IMessageSendCollect sendCollect) {
        this.sendCollect = sendCollect;
    }

    /**
     * Lấy số lượng thông điệp đang chờ trong danh sách.
     *
     * @return số lượng thông điệp, hoặc -1 nếu danh sách không tồn tại
     */
    public int getNumMessages() {
        if (this.messages != null) {
            return this.messages.size();
        }
        return -1;
    }

    /**
     * Đóng Sender, xóa danh sách thông điệp và đóng luồng đầu ra.
     */
    public void close() {
        if (this.messages != null) {
            this.messages.clear();
        }
        if (this.dos != null) {
            try {
                this.dos.close();
            } catch (Exception exception) {}
        }
    }

    /**
     * Giải phóng hoàn toàn tài nguyên của Sender, đặt các đối tượng về null.
     */
    public void dispose() {
        this.session = null;
        this.messages = null;
        this.sendCollect = null;
        this.dos = null;
    }
}