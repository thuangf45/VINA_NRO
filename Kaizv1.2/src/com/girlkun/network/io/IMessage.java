package com.girlkun.network.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Giao diện mở rộng từ IIOMessage, cung cấp các phương thức để quản lý luồng dữ liệu đầu vào và đầu ra,
 * lấy dữ liệu dạng mảng byte, và dọn dẹp tài nguyên sau khi sử dụng.
 *
 * @author Lucifer
 */
public interface IMessage extends IIOMessage {

    /**
     * Trả về đối tượng DataOutputStream để ghi dữ liệu vào luồng đầu ra.
     *
     * @return đối tượng DataOutputStream
     */
    DataOutputStream writer();

    /**
     * Trả về đối tượng DataInputStream để đọc dữ liệu từ luồng đầu vào.
     *
     * @return đối tượng DataInputStream
     */
    DataInputStream reader();

    /**
     * Lấy dữ liệu dưới dạng mảng byte từ thông điệp.
     *
     * @return mảng byte chứa dữ liệu của thông điệp
     */
    byte[] getData();

    /**
     * Dọn dẹp các tài nguyên liên quan đến thông điệp, nhưng không giải phóng hoàn toàn.
     */
    void cleanup();

    /**
     * Giải phóng hoàn toàn các tài nguyên liên quan đến thông điệp.
     */
    void dispose();
}