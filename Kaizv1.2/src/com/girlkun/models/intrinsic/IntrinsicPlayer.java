package com.girlkun.models.intrinsic;

import com.girlkun.services.IntrinsicService;

/**
 * Đại diện cho nội tại hiện tại của một người chơi.
 * <p>
 * Mỗi người chơi có thể sở hữu và mở nhiều lần nội tại. 
 * Nội tại được quản lý thông qua {@link IntrinsicService}.
 * </p>
 * 
 * @author Lucifer
 */
public class IntrinsicPlayer {

    /** 
     * Số lần người chơi đã mở nội tại (dùng để giới hạn/tính xác suất). 
     */
    public byte countOpen;

    /** 
     * Nội tại hiện tại của người chơi. 
     */
    public Intrinsic intrinsic;

    /**
     * Constructor mặc định. 
     * <p>
     * Khi khởi tạo, nội tại mặc định sẽ là loại có ID = 0, 
     * được lấy từ {@link IntrinsicService}.
     * </p>
     */
    public IntrinsicPlayer() {
        this.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
    }

    /**
     * Giải phóng dữ liệu, tránh rò rỉ bộ nhớ.
     * Sau khi gọi, {@link #intrinsic} sẽ bị gán về {@code null}.
     */
    public void dispose() {
        this.intrinsic = null;
    }
}
