package com.girlkun.network.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Interface định nghĩa các phương thức mở rộng để xử lý đọc và ghi hình ảnh trong giao tiếp mạng.
 * @author Lucifer
 */
public interface IAdvanceIOMessage extends IMessage {

    /**
     * Đọc dữ liệu hình ảnh từ luồng đầu vào.
     *
     * @return Đối tượng BufferedImage chứa dữ liệu hình ảnh.
     * @throws IOException Nếu xảy ra lỗi trong quá trình đọc hình ảnh.
     */
    BufferedImage readImage() throws IOException;

    /**
     * Ghi dữ liệu hình ảnh vào luồng đầu ra.
     *
     * @param paramBufferedImage Hình ảnh cần ghi.
     * @param paramString Định dạng hoặc thông tin bổ sung về hình ảnh.
     * @throws IOException Nếu xảy ra lỗi trong quá trình ghi hình ảnh.
     */
    void writeImage(BufferedImage paramBufferedImage, String paramString) throws IOException;
}