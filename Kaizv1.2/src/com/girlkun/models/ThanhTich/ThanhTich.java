package com.girlkun.models.ThanhTich;

import com.girlkun.models.Template;

/**
 * Đại diện cho một thành tích (achievement) của người chơi.
 * - Lưu template định nghĩa (nội dung, phần thưởng, điều kiện...).
 * - Quản lý trạng thái đã hoàn thành hay chưa (isFinish).
 * - Quản lý trạng thái đã nhận thưởng hay chưa (isRecieve).
 * 
 * @author Lucifer
 */
public class ThanhTich {
    
    /** Thông tin template gốc của thành tích */
    public Template.ArchivementTemplate Template;

    /** Đã hoàn thành thành tích chưa */
    public boolean isFinish;

    /** Đã nhận thưởng thành tích chưa */
    public boolean isRecieve;

    /** Constructor mặc định */
    public ThanhTich() { }

    /** Constructor sao chép từ 1 thành tích khác */
    public ThanhTich(ThanhTich a) {
        this.Template = a.Template;
        this.isFinish = a.isFinish;
        this.isRecieve = a.isRecieve;
    }
}
