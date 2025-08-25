package com.arriety.card;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp OptionCard:
 * Lớp OptionCard thuộc package com.arriety.card, biểu diễn một tùy chọn (option) của thẻ trong game, chứa các thuộc tính như ID, tham số (param) và trạng thái kích hoạt (active). Lớp này chủ yếu dùng để lưu trữ thông tin tùy chọn của thẻ, không chứa logic xử lý phức tạp, chỉ cung cấp constructor để khởi tạo và phương thức toString để biểu diễn dữ liệu dưới dạng chuỗi JSON.
 */
public class OptionCard {
    /**
     * id: int - ID duy nhất của tùy chọn, xác định loại tùy chọn hoặc đặc tính cụ thể của thẻ.
     */
    public int id;

    /**
     * param: int - Tham số của tùy chọn, biểu thị giá trị hoặc mức độ ảnh hưởng của tùy chọn (ví dụ: sức mạnh, chỉ số).
     */
    public int param;

    /**
     * active: byte - Trạng thái kích hoạt của tùy chọn (ví dụ: 0 = chưa kích hoạt, 1 = đã kích hoạt).
     */
    public byte active;

    /**
     * Constructor:
     * - Mô tả: Khởi tạo đối tượng OptionCard với ID, tham số và trạng thái kích hoạt.
     * - Thuộc tính:
     *   - i: int - ID của tùy chọn, xác định loại tùy chọn.
     *   - p: int - Tham số của tùy chọn, biểu thị giá trị hoặc mức độ của tùy chọn.
     *   - a: byte - Trạng thái kích hoạt của tùy chọn.
     * - Trả về: Không trả về gì, tạo mới đối tượng OptionCard với id = i, param = p, active = a.
     */
    public OptionCard(int i, int p, byte a) {
        id = i;
        param = p;
        active = a;
    }

    /**
     * toString:
     * - Mô tả: Ghi đè phương thức toString để trả về biểu diễn chuỗi JSON của đối tượng OptionCard, chứa thông tin tất cả thuộc tính.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Chuỗi (String) dạng JSON chứa các thuộc tính id, active, param.
     */
    @Override
    public String toString() {
        final String n = "\"";
        return "{"
                + n + "id" + n + ":" + n + id + n + ","
                + n + "active" + n + ":" + n + active + n + ","
                + n + "param" + n + ":" + n + param + n
                + "}";
    }
}