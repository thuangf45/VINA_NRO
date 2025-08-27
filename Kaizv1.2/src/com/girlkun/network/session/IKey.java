package com.girlkun.network.session;

import com.girlkun.network.io.Message;

/**
 * Giao diện định nghĩa các phương thức để quản lý khóa mã hóa trong phiên làm việc mạng.
 * Hỗ trợ gửi, thiết lập, lấy khóa và kiểm tra trạng thái gửi khóa.
 *
 * @author Lucifer
 */
public interface IKey {

    /**
     * Gửi khóa mã hóa đến phiên làm việc.
     *
     * @throws Exception nếu xảy ra lỗi trong quá trình gửi
     */
    void sendKey() throws Exception;

    /**
     * Thiết lập khóa mã hóa từ một thông điệp.
     *
     * @param paramMessage thông điệp chứa dữ liệu khóa
     * @throws Exception nếu xảy ra lỗi trong quá trình thiết lập
     */
    void setKey(Message paramMessage) throws Exception;

    /**
     * Thiết lập khóa mã hóa từ một mảng byte.
     *
     * @param paramArrayOfbyte mảng byte chứa dữ liệu khóa
     */
    void setKey(byte[] paramArrayOfbyte);

    /**
     * Lấy khóa mã hóa hiện tại.
     *
     * @return mảng byte chứa dữ liệu khóa
     */
    byte[] getKey();

    /**
     * Kiểm tra xem khóa đã được gửi hay chưa.
     *
     * @return true nếu khóa đã được gửi, false nếu chưa
     */
    boolean sentKey();

    /**
     * Thiết lập trạng thái gửi khóa.
     *
     * @param paramBoolean true nếu khóa đã được gửi, false nếu chưa
     */
    void setSentKey(boolean paramBoolean);
}