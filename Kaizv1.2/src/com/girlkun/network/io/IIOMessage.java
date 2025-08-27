package com.girlkun.network.io;

import java.io.IOException;

/**
 * Giao diện định nghĩa các phương thức để đọc và ghi các kiểu dữ liệu khác nhau qua kết nối mạng.
 * Cung cấp chức năng xử lý các thao tác nhập/xuất, bao gồm đọc và ghi mảng byte,
 * các kiểu dữ liệu nguyên thủy và chuỗi, với hỗ trợ xử lý ngoại lệ IO.
 *
 * @author Lucifer
 */
public interface IIOMessage {

    /**
     * Đọc một byte từ luồng đầu vào.
     *
     * @return giá trị byte được đọc dưới dạng số nguyên
     * @throws IOException nếu xảy ra lỗi I/O
     */
    int read() throws IOException;

    /**
     * Đọc các byte vào mảng byte được chỉ định.
     *
     * @param paramArrayOfbyte mảng byte để lưu dữ liệu được đọc
     * @return số byte được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    int read(byte[] paramArrayOfbyte) throws IOException;

    /**
     * Đọc một số lượng byte xác định vào mảng byte được chỉ định, bắt đầu từ vị trí offset.
     *
     * @param paramArrayOfbyte mảng byte để lưu dữ liệu được đọc
     * @param paramInt1        vị trí bắt đầu trong mảng để đọc dữ liệu
     * @param paramInt2        số byte cần đọc
     * @return số byte được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;

    /**
     * Đọc một giá trị boolean từ luồng đầu vào.
     *
     * @return giá trị boolean được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    boolean readBoolean() throws IOException;

    /**
     * Đọc một byte có dấu từ luồng đầu vào.
     *
     * @return giá trị byte được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    byte readByte() throws IOException;

    /**
     * Đọc một số ngắn có dấu (16-bit) từ luồng đầu vào.
     *
     * @return giá trị short được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    short readShort() throws IOException;

    /**
     * Đọc một số nguyên có dấu (32-bit) từ luồng đầu vào.
     *
     * @return giá trị số nguyên được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    int readInt() throws IOException;

    /**
     * Đọc một số dài có dấu (64-bit) từ luồng đầu vào.
     *
     * @return giá trị long được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    long readLong() throws IOException;

    /**
     * Đọc một số thực kiểu float (32-bit) từ luồng đầu vào.
     *
     * @return giá trị float được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    float readFloat() throws IOException;

    /**
     * Đọc một số thực kiểu double (64-bit) từ luồng đầu vào.
     *
     * @return giá trị double được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    double readDouble() throws IOException;

    /**
     * Đọc một ký tự từ luồng đầu vào.
     *
     * @return ký tự được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    char readChar() throws IOException;

    /**
     * Đọc một chuỗi mã hóa UTF-8 từ luồng đầu vào.
     *
     * @return chuỗi được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    String readUTF() throws IOException;

    /**
     * Đọc đầy đủ các byte để điền vào mảng byte được chỉ định.
     *
     * @param paramArrayOfbyte mảng byte cần điền đầy
     * @throws IOException nếu xảy ra lỗi I/O hoặc gặp cuối luồng
     */
    void readFully(byte[] paramArrayOfbyte) throws IOException;

    /**
     * Đọc một số lượng byte xác định vào mảng byte được chỉ định, bắt đầu từ vị trí offset,
     * cho đến khi đọc đủ số byte yêu cầu hoặc gặp cuối luồng.
     *
     * @param paramArrayOfbyte mảng byte cần điền đầy
     * @param paramInt1        vị trí bắt đầu trong mảng để đọc dữ liệu
     * @param paramInt2        số byte cần đọc
     * @throws IOException nếu xảy ra lỗi I/O hoặc gặp cuối luồng
     */
    void readFully(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;

    /**
     * Đọc một byte không dấu (8-bit) từ luồng đầu vào.
     *
     * @return giá trị byte không dấu được đọc dưới dạng số nguyên (0 đến 255)
     * @throws IOException nếu xảy ra lỗi I/O
     */
    int readUnsignedByte() throws IOException;

    /**
     * Đọc một số ngắn không dấu (16-bit) từ luồng đầu vào.
     *
     * @return giá trị short không dấu được đọc dưới dạng số nguyên (0 đến 65535)
     * @throws IOException nếu xảy ra lỗi I/O
     */
    int readUnsignedShort() throws IOException;

    /**
     * Ghi mảng byte được chỉ định vào luồng đầu ra.
     *
     * @param paramArrayOfbyte mảng byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void write(byte[] paramArrayOfbyte) throws IOException;

    /**
     * Ghi một byte vào luồng đầu ra.
     *
     * @param paramInt giá trị byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void write(int paramInt) throws IOException;

    /**
     * Ghi một phần của mảng byte được chỉ định vào luồng đầu ra.
     *
     * @param paramArrayOfbyte mảng byte chứa dữ liệu cần ghi
     * @param paramInt1        vị trí bắt đầu trong mảng để ghi dữ liệu
     * @param paramInt2        số byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;

    /**
     * Ghi một giá trị boolean vào luồng đầu ra.
     *
     * @param paramBoolean giá trị boolean cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeBoolean(boolean paramBoolean) throws IOException;

    /**
     * Ghi một giá trị byte vào luồng đầu ra.
     *
     * @param paramInt giá trị byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeByte(int paramInt) throws IOException;

    /**
     * Ghi các byte của chuỗi được chỉ định vào luồng đầu ra.
     *
     * @param paramString chuỗi có các byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeBytes(String paramString) throws IOException;

    /**
     * Ghi một ký tự vào luồng đầu ra.
     *
     * @param paramInt giá trị ký tự cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeChar(int paramInt) throws IOException;

    /**
     * Ghi từng ký tự của chuỗi được chỉ định vào luồng đầu ra dưới dạng chuỗi byte.
     *
     * @param paramString chuỗi có các ký tự cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeChars(String paramString) throws IOException;

    /**
     * Ghi một giá trị double (64-bit) vào luồng đầu ra.
     *
     * @param paramDouble giá trị double cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeDouble(double paramDouble) throws IOException;

    /**
     * Ghi một giá trị float (32-bit) vào luồng đầu ra.
     *
     * @param paramFloat giá trị float cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeFloat(float paramFloat) throws IOException;

    /**
     * Ghi một giá trị số nguyên (32-bit) vào luồng đầu ra.
     *
     * @param paramInt giá trị số nguyên cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeInt(int paramInt) throws IOException;

    /**
     * Ghi một giá trị số dài (64-bit) vào luồng đầu ra.
     *
     * @param paramLong giá trị long cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeLong(long paramLong) throws IOException;

    /**
     * Ghi một giá trị số ngắn (16-bit) vào luồng đầu ra.
     *
     * @param paramInt giá trị short cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeShort(int paramInt) throws IOException;

    /**
     * Ghi một chuỗi mã hóa UTF-8 vào luồng đầu ra.
     *
     * @param paramString chuỗi cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    void writeUTF(String paramString) throws IOException;
}