package com.girlkun.network.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Lớp thực hiện giao diện IAdvanceIOMessage, cung cấp các phương thức để quản lý thông điệp mạng,
 * bao gồm đọc/ghi dữ liệu, xử lý hình ảnh, và quản lý tài nguyên luồng dữ liệu.
 *
 * @author Lucifer
 */
public class Message implements IAdvanceIOMessage {

    /**
     * Mã lệnh của thông điệp, xác định loại thông điệp được gửi hoặc nhận.
     */
    public byte command;

    /**
     * Luồng đầu ra dạng byte để lưu trữ dữ liệu ghi.
     */
    private ByteArrayOutputStream os;

    /**
     * Luồng dữ liệu đầu ra để ghi các kiểu dữ liệu nguyên thủy và chuỗi.
     */
    private DataOutputStream dos;

    /**
     * Luồng đầu vào dạng byte để đọc dữ liệu.
     */
    private ByteArrayInputStream is;

    /**
     * Luồng dữ liệu đầu vào để đọc các kiểu dữ liệu nguyên thủy và chuỗi.
     */
    private DataInputStream dis;

    /**
     * Khởi tạo thông điệp với mã lệnh kiểu số nguyên.
     *
     * @param command mã lệnh của thông điệp
     */
    public Message(int command) {
        this((byte) command);
    }

    /**
     * Khởi tạo thông điệp với mã lệnh kiểu byte và khởi tạo luồng đầu ra.
     *
     * @param command mã lệnh của thông điệp
     */
    public Message(byte command) {
        this.command = command;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.os);
    }

    /**
     * Khởi tạo thông điệp với mã lệnh và dữ liệu đầu vào.
     *
     * @param command mã lệnh của thông điệp
     * @param data    mảng byte chứa dữ liệu đầu vào
     */
    public Message(byte command, byte[] data) {
        this.command = command;
        this.is = new ByteArrayInputStream(data);
        this.dis = new DataInputStream(this.is);
    }

    /**
     * Trả về luồng dữ liệu đầu ra để ghi dữ liệu.
     *
     * @return đối tượng DataOutputStream
     */
    public DataOutputStream writer() {
        return this.dos;
    }

    /**
     * Trả về luồng dữ liệu đầu vào để đọc dữ liệu.
     *
     * @return đối tượng DataInputStream
     */
    public DataInputStream reader() {
        return this.dis;
    }

    /**
     * Lấy dữ liệu của thông điệp dưới dạng mảng byte.
     *
     * @return mảng byte chứa dữ liệu của thông điệp
     */
    public byte[] getData() {
        return this.os.toByteArray();
    }

    /**
     * Chuyển đổi dữ liệu của thông điệp (hiện không thực hiện gì).
     */
    public void transformData() {
    }

    /**
     * Dọn dẹp các tài nguyên luồng, đóng các luồng đầu vào và đầu ra nếu có.
     */
    public void cleanup() {
        try {
            if (this.is != null) {
                this.is.close();
            }
            if (this.os != null) {
                this.os.close();
            }
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.dos != null) {
                this.dos.close();
            }
        } catch (Exception exception) {
        }
    }

    /**
     * Giải phóng hoàn toàn các tài nguyên của thông điệp, bao gồm gọi cleanup và đặt các luồng về null.
     */
    public void dispose() {
        cleanup();
        this.dis = null;
        this.is = null;
        this.dos = null;
        this.os = null;
    }

    /**
     * Đọc một byte từ luồng đầu vào.
     *
     * @return giá trị byte được đọc dưới dạng số nguyên
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public int read() throws IOException {
        return reader().read();
    }

    /**
     * Đọc các byte vào mảng byte được chỉ định.
     *
     * @param b mảng byte để lưu dữ liệu được đọc
     * @return số byte được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public int read(byte[] b) throws IOException {
        return reader().read(b);
    }

    /**
     * Đọc một số lượng byte xác định vào mảng byte được chỉ định, bắt đầu từ vị trí offset.
     *
     * @param b   mảng byte để lưu dữ liệu được đọc
     * @param off vị trí bắt đầu trong mảng để đọc dữ liệu
     * @param len số byte cần đọc
     * @return số byte được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public int read(byte[] b, int off, int len) throws IOException {
        return reader().read(b, off, len);
    }

    /**
     * Đọc một giá trị boolean từ luồng đầu vào.
     *
     * @return giá trị boolean được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public boolean readBoolean() throws IOException {
        return reader().readBoolean();
    }

    /**
     * Đọc một byte có dấu từ luồng đầu vào.
     *
     * @return giá trị byte được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public byte readByte() throws IOException {
        return reader().readByte();
    }

    /**
     * Đọc một số ngắn có dấu (16-bit) từ luồng đầu vào.
     *
     * @return giá trị short được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public short readShort() throws IOException {
        return reader().readShort();
    }

    /**
     * Đọc một số nguyên có dấu (32-bit) từ luồng đầu vào.
     *
     * @return giá trị số nguyên được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public int readInt() throws IOException {
        return reader().readInt();
    }

    /**
     * Đọc một số dài có dấu (64-bit) từ luồng đầu vào.
     *
     * @return giá trị long được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public long readLong() throws IOException {
        return reader().readLong();
    }

    /**
     * Đọc một số thực kiểu float (32-bit) từ luồng đầu vào.
     *
     * @return giá trị float được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public float readFloat() throws IOException {
        return reader().readFloat();
    }

    /**
     * Đọc một số thực kiểu double (64-bit) từ luồng đầu vào.
     *
     * @return giá trị double được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public double readDouble() throws IOException {
        return reader().readDouble();
    }

    /**
     * Đọc một ký tự từ luồng đầu vào.
     *
     * @return ký tự được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public char readChar() throws IOException {
        return reader().readChar();
    }

    /**
     * Đọc một chuỗi mã hóa UTF-8 từ luồng đầu vào.
     *
     * @return chuỗi được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public String readUTF() throws IOException {
        return reader().readUTF();
    }

    /**
     * Đọc đầy đủ các byte để điền vào mảng byte được chỉ định.
     *
     * @param b mảng byte cần điền đầy
     * @throws IOException nếu xảy ra lỗi I/O hoặc gặp cuối luồng
     */
    public void readFully(byte[] b) throws IOException {
        reader().readFully(b);
    }

    /**
     * Đọc một số lượng byte xác định vào mảng byte được chỉ định, bắt đầu từ vị trí offset.
     *
     * @param b   mảng byte cần điền đầy
     * @param off vị trí bắt đầu trong mảng để đọc dữ liệu
     * @param len số byte cần đọc
     * @throws IOException nếu xảy ra lỗi I/O hoặc gặp cuối luồng
     */
    public void readFully(byte[] b, int off, int len) throws IOException {
        reader().readFully(b, off, len);
    }

    /**
     * Đọc một byte không dấu (8-bit) từ luồng đầu vào.
     *
     * @return giá trị byte không dấu được đọc dưới dạng số nguyên (0 đến 255)
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public int readUnsignedByte() throws IOException {
        return reader().readUnsignedByte();
    }

    /**
     * Đọc một số ngắn không dấu (16-bit) từ luồng đầu vào.
     *
     * @return giá trị short không dấu được đọc dưới dạng số nguyên (0 đến 65535)
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public int readUnsignedShort() throws IOException {
        return reader().readUnsignedShort();
    }

    /**
     * Ghi mảng byte được chỉ định vào luồng đầu ra.
     *
     * @param b mảng byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void write(byte[] b) throws IOException {
        writer().write(b);
    }

    /**
     * Ghi một byte vào luồng đầu ra.
     *
     * @param b giá trị byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void write(int b) throws IOException {
        writer().write(b);
    }

    /**
     * Ghi một phần của mảng byte được chỉ định vào luồng đầu ra.
     *
     * @param b   mảng byte chứa dữ liệu cần ghi
     * @param off vị trí bắt đầu trong mảng để ghi dữ liệu
     * @param len số byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void write(byte[] b, int off, int len) throws IOException {
        writer().write(b, off, len);
    }

    /**
     * Ghi một giá trị boolean vào luồng đầu ra.
     *
     * @param v giá trị boolean cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeBoolean(boolean v) throws IOException {
        writer().writeBoolean(v);
    }

    /**
     * Ghi một giá trị byte vào luồng đầu ra.
     *
     * @param v giá trị byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeByte(int v) throws IOException {
        writer().writeByte(v);
    }

    /**
     * Ghi các byte của chuỗi được chỉ định vào luồng đầu ra.
     *
     * @param s chuỗi có các byte cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeBytes(String s) throws IOException {
        writer().writeBytes(s);
    }

    /**
     * Ghi một ký tự vào luồng đầu ra.
     *
     * @param v giá trị ký tự cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeChar(int v) throws IOException {
        writer().writeChar(v);
    }

    /**
     * Ghi từng ký tự của chuỗi được chỉ định vào luồng đầu ra dưới dạng chuỗi byte.
     *
     * @param s chuỗi có các ký tự cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeChars(String s) throws IOException {
        writer().writeChars(s);
    }

    /**
     * Ghi một giá trị double (64-bit) vào luồng đầu ra.
     *
     * @param v giá trị double cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeDouble(double v) throws IOException {
        writer().writeDouble(v);
    }

    /**
     * Ghi một giá trị float (32-bit) vào luồng đầu ra.
     *
     * @param v giá trị float cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeFloat(float v) throws IOException {
        writer().writeFloat(v);
    }

    /**
     * Ghi một giá trị số nguyên (32-bit) vào luồng đầu ra.
     *
     * @param v giá trị số nguyên cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeInt(int v) throws IOException {
        writer().writeInt(v);
    }

    /**
     * Ghi một giá trị số dài (64-bit) vào luồng đầu ra.
     *
     * @param v giá trị long cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeLong(long v) throws IOException {
        writer().writeLong(v);
    }

    /**
     * Ghi một giá trị số ngắn (16-bit) vào luồng đầu ra.
     *
     * @param v giá trị short cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeShort(int v) throws IOException {
        writer().writeShort(v);
    }

    /**
     * Ghi một chuỗi mã hóa UTF-8 vào luồng đầu ra.
     *
     * @param str chuỗi cần ghi
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeUTF(String str) throws IOException {
        writer().writeUTF(str);
    }

    /**
     * Đọc một hình ảnh từ luồng đầu vào dưới dạng BufferedImage.
     *
     * @return đối tượng BufferedImage được đọc
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public BufferedImage readImage() throws IOException {
        int size = readInt();
        byte[] dataImage = new byte[size];
        read(dataImage);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(dataImage));
        return image;
    }

    /**
     * Ghi một hình ảnh vào luồng đầu ra dưới định dạng được chỉ định.
     *
     * @param image  hình ảnh BufferedImage cần ghi
     * @param format định dạng hình ảnh (ví dụ: "png", "jpg")
     * @throws IOException nếu xảy ra lỗi I/O
     */
    public void writeImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] dataImage = baos.toByteArray();
        writeInt(dataImage.length);
        write(dataImage);
    }
}