/*
 * Tóm tắt lớp DataGame:
 * 
 * Lớp DataGame là một lớp chứa các hằng số tĩnh (constants) dùng để cấu hình và kiểm soát dữ liệu trong game, bao gồm thông tin server, trạng thái bảo trì, tỷ lệ rơi đồ, và các key mã hóa cho login và reset.
 * Lớp không có phương thức nào, chỉ toàn thuộc tính tĩnh (static fields).
 * 
 * Các thuộc tính chính:
 * - IPServerGame: String - Đại diện cho thông tin kết nối server (tên server:IP:port:flags), giá trị "Nro Green 01:127.0.0.1:14445:0,0,0". Không có xử lý logic, chỉ lưu giá trị.
 * - IsBaoTri: boolean - Trạng thái bảo trì server, giá trị false (không bảo trì). Không có xử lý logic.
 * - TiLeRoiDoThanBoss: int - Tỷ lệ rơi đồ thần từ boss, giá trị 2. Không có xử lý logic.
 * - TiLeNap: int - Tỷ lệ nạp (có thể là nạp tiền hoặc item), giá trị 1. Không có xử lý logic.
 * - TiLeRoiDoCold: int - Tỷ lệ rơi đồ cold, giá trị 30000. Không có xử lý logic.
 * - TiLeRoiThucAnCold: int - Tỷ lệ rơi thức ăn cold, giá trị 2. Không có xử lý logic.
 * - Ngoc, Rong, G8, Sieu, Cap, VIPPRO, NQMP, Anti, Res: long (private final static) - Các giá trị long mã hóa riêng lẻ, dùng để tạo mảng key. Không có xử lý logic, chỉ lưu giá trị (ví dụ: Ngoc = 7092435447648304445L).
 * - KeyIcon: byte[] - Mảng byte chứa các giá trị mã hóa (có thể là key icon hoặc mã hóa), giá trị {87, 87, 48, ... ,57}. Không có xử lý logic.
 * - KeyLogin: long[] - Mảng long chứa các key login từ các biến Ngoc, Rong, G8, Sieu, Cap, VIPPRO. Xử lý logic: Khởi tạo mảng từ các biến private. Trả về mảng key để sử dụng trong login.
 * - KeyRes: long[] - Mảng long chứa các key reset từ các biến NQMP, Anti, Res, Sieu, Cap, VIPPRO. Xử lý logic: Khởi tạo mảng từ các biến private. Trả về mảng key để sử dụng trong reset.
 * 
 * Lớp này chủ yếu dùng để lưu trữ dữ liệu cấu hình tĩnh, không có phương thức xử lý hoặc trả về giá trị động.
 */

package DataControlGame;

/**
 *
 * @author Lucifer
 */
public class DataGame {
    public static String IPServerGame = "Nro Green 01:127.0.0.1:14445:0,0,0";
    public static boolean IsBaoTri = false;
    public final static int TiLeRoiDoThanBoss = 2;
    public final static int TiLeNap = 1;
    public final static int TiLeRoiDoCold = 30000;
    public final static int TiLeRoiThucAnCold = 2;
    
    // NQMPVIPPRO
    // Key Login
    
    private final static long Ngoc = 7092435447648304445L;
    private final static long Rong = 7164445758408047933L;
    private final static long G8 = 7003779621799279933L;
    private final static long Sieu = 7147894771385711933L;
    private final static long Cap = 1496467063L;
    private final static long VIPPRO = 7236559436276845174L;
    private final static long NQMP = 7092683928850480445L;
    private final static long Anti = 5861211921953406269L;
    private final static long Res = 1433228922L;
    
    public final static byte[] KeyIcon = new byte[]{
        87, 87, 48, 49, 82, 50, 82, 72, 84, 107,
        104, 78, 86, 50, 104, 111, 86, 106, 70, 75,
        100, 49, 108, 116, 77, 87, 53, 81, 85, 84,
        48, 57
    };
    
    public final static long[] KeyLogin = new long[]{
        Ngoc, Rong, G8, Sieu, Cap, VIPPRO
    };
    
    public final static long[] KeyRes = new long[]{
        NQMP, Anti, Res, Sieu, Cap, VIPPRO
    };
}