package com.girlkun.network.server;

/**
 * Giao diện định nghĩa các phương thức để quản lý máy chủ mạng, bao gồm khởi tạo,
 * chạy, đóng máy chủ, và xử lý các phiên làm việc. Giao diện này mở rộng Runnable
 * để hỗ trợ chạy máy chủ trong một luồng riêng.
 *
 * @author Lucifer
 */
public interface IGirlkunServer extends Runnable {

    /**
     * Khởi tạo máy chủ với các cấu hình ban đầu.
     *
     * @return đối tượng IGirlkunServer
     */
    IGirlkunServer init();

    /**
     * Khởi động máy chủ trên một cổng mạng cụ thể.
     *
     * @param paramInt cổng mạng để máy chủ lắng nghe
     * @return đối tượng IGirlkunServer
     * @throws Exception nếu cổng không hợp lệ hoặc có lỗi khi khởi động
     */
    IGirlkunServer start(int paramInt) throws Exception;

    /**
     * Thiết lập trình xử lý chấp nhận phiên làm việc mới.
     *
     * @param paramISessionAcceptHandler trình xử lý chấp nhận phiên làm việc
     * @return đối tượng IGirlkunServer
     */
    IGirlkunServer setAcceptHandler(ISessionAcceptHandler paramISessionAcceptHandler);

    /**
     * Đóng máy chủ và giải phóng tài nguyên liên quan.
     *
     * @return đối tượng IGirlkunServer
     */
    IGirlkunServer close();

    /**
     * Giải phóng hoàn toàn tài nguyên của máy chủ.
     *
     * @return đối tượng IGirlkunServer
     */
    IGirlkunServer dispose();

    /**
     * Thiết lập chế độ sử dụng khóa ngẫu nhiên cho phiên làm việc.
     *
     * @param paramBoolean cờ xác định có sử dụng khóa ngẫu nhiên hay không
     * @return đối tượng IGirlkunServer
     */
    IGirlkunServer randomKey(boolean paramBoolean);

    /**
     * Thiết lập trình xử lý sự kiện khi máy chủ đóng.
     *
     * @param paramIServerClose trình xử lý sự kiện đóng máy chủ
     * @return đối tượng IGirlkunServer
     */
    IGirlkunServer setDoSomeThingWhenClose(IServerClose paramIServerClose);

    /**
     * Thiết lập lớp dùng để tạo bản sao phiên làm việc.
     *
     * @param paramClass lớp của phiên làm việc
     * @return đối tượng IGirlkunServer
     * @throws Exception nếu lớp không hợp lệ
     */
    IGirlkunServer setTypeSessioClone(Class paramClass) throws Exception;

    /**
     * Lấy trình xử lý chấp nhận phiên làm việc.
     *
     * @return trình xử lý chấp nhận phiên làm việc
     * @throws Exception nếu trình xử lý chưa được khởi tạo
     */
    ISessionAcceptHandler getAcceptHandler() throws Exception;

    /**
     * Kiểm tra xem máy chủ có sử dụng khóa ngẫu nhiên hay không.
     *
     * @return true nếu sử dụng khóa ngẫu nhiên, false nếu không
     */
    boolean isRandomKey();

    /**
     * Dừng chấp nhận các kết nối mới từ client.
     */
    void stopConnect();
}
