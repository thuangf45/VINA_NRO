package com.girlkun.models.task;

/**
 * Lớp đại diện cho mẫu nhiệm vụ phụ trong game, chứa thông tin về ID, tên và số lượng yêu cầu cho các cấp độ.
 * @author Lucifer
 */
public class SideTaskTemplate {

    /** ID của mẫu nhiệm vụ phụ. */
    public int id;

    /** Tên của nhiệm vụ phụ. */
    public String name;

    /** Mảng chứa số lượng yêu cầu cho từng cấp độ (5 cấp độ, mỗi cấp độ có [mức tối thiểu, mức tối đa]). */
    public int[][] count;

    /**
     * Khởi tạo đối tượng SideTaskTemplate với mảng số lượng yêu cầu mặc định.
     */
    public SideTaskTemplate() {
        this.count = new int[5][2];
    }
}