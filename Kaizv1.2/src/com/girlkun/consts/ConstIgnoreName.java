package com.girlkun.consts;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ConstIgnoreName:
 * Lớp ConstIgnoreName thuộc package com.girlkun.consts, định nghĩa một mảng hằng số tĩnh (static final) chứa các tên bị cấm sử dụng trong game. Mảng này được sử dụng để kiểm tra và ngăn chặn việc sử dụng các tên cụ thể (ví dụ: tên liên quan đến admin) khi người chơi tạo tài khoản hoặc nhân vật.
 */
public class ConstIgnoreName {
    /**
     * IGNORE_NAME: String[] - Mảng tĩnh chứa danh sách các tên bị cấm sử dụng.
     * - Bao gồm các chuỗi như "admin", "admjn", và một chuỗi "admin" trùng lặp.
     * - Được sử dụng để kiểm tra và chặn các tên không hợp lệ.
     */
    public static final String[] IGNORE_NAME = {
        "admin",
        "admjn",
        "admin"
    };
}