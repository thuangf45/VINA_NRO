package com.girlkun.models.intrinsic;

/**
 * Đại diện cho một Nội Tại (Intrinsic) của nhân vật.
 * <p>
 * Nội tại cung cấp các chỉ số đặc biệt như tăng sức mạnh, phòng thủ, chí mạng, 
 * và có thể có nhiều mức độ (paramFrom → paramTo). 
 * Mỗi giới tính và nhân vật có thể có các nội tại khác nhau.
 * </p>
 * 
 * @author Lucifer
 */
public class Intrinsic {

    /** ID của nội tại */
    public int id;

    /** Tên của nội tại (có thể chứa placeholder như p0, p1, ...) */
    public String name;

    /** Giá trị tham số 1 bắt đầu (min) */
    public short paramFrom1;

    /** Giá trị tham số 1 kết thúc (max) */
    public short paramTo1;

    /** Giá trị tham số 2 bắt đầu (min) */
    public short paramFrom2;

    /** Giá trị tham số 2 kết thúc (max) */
    public short paramTo2;

    /** Icon đại diện cho nội tại */
    public short icon;

    /** Giới tính áp dụng (0 = Trái đất, 1 = Namec, 2 = Xayda) */
    public byte gender;

    /** Giá trị thực tế của tham số 1 (nằm trong khoảng [paramFrom1, paramTo1]) */
    public short param1;

    /** Giá trị thực tế của tham số 2 (nằm trong khoảng [paramFrom2, paramTo2]) */
    public short param2;

    /**
     * Constructor mặc định (khởi tạo rỗng).
     */
    public Intrinsic() {
    }

    /**
     * Constructor sao chép từ một {@link Intrinsic} khác.
     * 
     * @param intrinsic Nội tại nguồn để sao chép
     */
    public Intrinsic(Intrinsic intrinsic) {
        this.id = intrinsic.id;
        this.name = intrinsic.name;
        this.paramFrom1 = intrinsic.paramFrom1;
        this.paramTo1 = intrinsic.paramTo1;
        this.paramFrom2 = intrinsic.paramFrom2;
        this.paramTo2 = intrinsic.paramTo2;
        this.icon = intrinsic.icon;
        this.gender = intrinsic.gender;
    }

    /**
     * Lấy mô tả chi tiết của nội tại, thay thế các placeholder (p0, p1, p2, p3)
     * bằng giá trị min/max tương ứng.
     * 
     * @return Mô tả nội tại (ví dụ: "Tăng 5% - 10% sát thương")
     */
    public String getDescription() {
        return this.name.replaceAll("p0", String.valueOf(paramFrom1))
                .replaceAll("p1", String.valueOf(paramTo1))
                .replaceAll("p2", String.valueOf(paramFrom2))
                .replaceAll("p3", String.valueOf(paramTo2));
    }

    /**
     * Giới hạn giá trị {@code param1} không vượt quá {@code paramTo1}.
     */
    public void SetMaxValue() {
        if (param1 > paramTo1) {
            param1 = paramTo1;
        }
    }

    /**
     * Lấy tên hiển thị của nội tại, thay thế placeholder bằng giá trị thực tế 
     * ({@link #param1}, {@link #param2}) và bổ sung khoảng giá trị nếu có.
     * 
     * @return Tên nội tại đã được xử lý
     */
    public String getName() {
        return this.name.replaceAll("p0% đến p1", "p0")
                .replaceAll("p2% đến p3", "p1")
                .replaceAll("p0", String.valueOf(this.param1))
                .replaceAll("p1", String.valueOf(this.param2))
                + (this.id != 0 ? " [" + this.paramFrom1 + " đến " + this.paramTo1 + "]" : "");
    }
}
