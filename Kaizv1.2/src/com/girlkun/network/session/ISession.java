package com.girlkun.network.session;

import com.girlkun.network.handler.IKeySessionHandler;
import com.girlkun.network.handler.IMessageHandler;
import com.girlkun.network.handler.IMessageSendCollect;
import com.girlkun.network.io.Message;

/**
 * Giao diện định nghĩa các phương thức để quản lý một phiên làm việc mạng.
 * Mở rộng IKey để hỗ trợ quản lý khóa mã hóa, cung cấp các phương thức để
 * xử lý thông điệp, kết nối, và trạng thái phiên làm việc.
 *
 * @author Lucifer
 */
public interface ISession extends IKey {

    /**
     * Lấy loại phiên làm việc hiện tại.
     *
     * @return loại phiên làm việc
     */
    TypeSession getTypeSession();

    /**
     * Thiết lập đối tượng xử lý việc gửi thông điệp.
     *
     * @param paramIMessageSendCollect đối tượng xử lý gửi thông điệp
     * @return đối tượng ISession
     */
    ISession setSendCollect(IMessageSendCollect paramIMessageSendCollect);

    /**
     * Thiết lập đối tượng xử lý thông điệp nhận được.
     *
     * @param paramIMessageHandler đối tượng xử lý thông điệp
     * @return đối tượng ISession
     */
    ISession setMessageHandler(IMessageHandler paramIMessageHandler);

    /**
     * Thiết lập đối tượng xử lý khóa mã hóa cho phiên làm việc.
     *
     * @param paramIKeySessionHandler đối tượng xử lý khóa phiên làm việc
     * @return đối tượng ISession
     */
    ISession setKeyHandler(IKeySessionHandler paramIKeySessionHandler);

    /**
     * Bắt đầu quá trình gửi thông điệp cho phiên làm việc.
     *
     * @return đối tượng ISession
     */
    ISession startSend();

    /**
     * Bắt đầu quá trình thu thập thông điệp từ phiên làm việc.
     *
     * @return đối tượng ISession
     */
    ISession startCollect();

    /**
     * Bắt đầu phiên làm việc, khởi tạo các tài nguyên cần thiết.
     *
     * @return đối tượng ISession
     */
    ISession start();

    /**
     * Thiết lập khả năng kết nối lại cho phiên làm việc.
     *
     * @param paramBoolean true nếu cho phép kết nối lại, false nếu không
     * @return đối tượng ISession
     */
    ISession setReconnect(boolean paramBoolean);

    /**
     * Khởi tạo luồng xử lý cho phiên làm việc.
     */
    void initThreadSession();

    /**
     * Thực hiện kết nối lại phiên làm việc nếu bị ngắt.
     */
    void reconnect();

    /**
     * Lấy địa chỉ IP của client kết nối với phiên làm việc.
     *
     * @return địa chỉ IP dưới dạng chuỗi
     */
    String getIP();

    /**
     * Kiểm tra xem phiên làm việc có đang kết nối hay không.
     *
     * @return true nếu phiên làm việc đang kết nối, false nếu không
     */
    boolean isConnected();

    /**
     * Lấy ID duy nhất của phiên làm việc.
     *
     * @return ID của phiên làm việc
     */
    long getID();

    /**
     * Gửi một thông điệp qua phiên làm việc.
     *
     * @param paramMessage thông điệp cần gửi
     */
    void sendMessage(Message paramMessage);

    /**
     * Thực hiện gửi một thông điệp qua phiên làm việc.
     *
     * @param paramMessage thông điệp cần gửi
     * @throws Exception nếu xảy ra lỗi trong quá trình gửi
     */
    void doSendMessage(Message paramMessage) throws Exception;

    /**
     * Ngắt kết nối phiên làm việc.
     */
    void disconnect();

    /**
     * Giải phóng tài nguyên của phiên làm việc.
     */
    void dispose();

    /**
     * Lấy số lượng thông điệp đang chờ xử lý trong phiên làm việc.
     *
     * @return số lượng thông điệp
     */
    int getNumMessages();
}