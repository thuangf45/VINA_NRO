package DataControlGame;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp DataGame:
 * Lớp DataGame thuộc package DataControlGame, chứa các hằng số tĩnh (static constants) dùng để cấu hình và kiểm soát dữ liệu trong game, bao gồm thông tin server, trạng thái bảo trì, tỷ lệ rơi đồ, và các key mã hóa cho login và reset. Lớp này không có phương thức, chỉ chứa các thuộc tính tĩnh để lưu trữ dữ liệu cấu hình.
 */
public class DataGame {
    /**
     * IPServerGame: String - Thông tin kết nối server, bao gồm tên server, địa chỉ IP, cổng (port) và các cờ (flags). Giá trị mặc định: "Nro Green 01:127.0.0.1:14445:0,0,0".
     */
    public static String IPServerGame = "Nro Green 01:127.0.0.1:14445:0,0,0";

    /**
     * IsBaoTri: boolean - Trạng thái bảo trì của server. Giá trị false biểu thị server không trong trạng thái bảo trì.
     */
    public static boolean IsBaoTri = false;

    /**
     * TiLeRoiDoThanBoss: int - Tỷ lệ rơi đồ thần từ boss trong game. Giá trị mặc định: 2.
     */
    public final static int TiLeRoiDoThanBoss = 2;

    /**
     * TiLeNap: int - Tỷ lệ nạp, có thể liên quan đến nạp tiền hoặc item trong game. Giá trị mặc định: 1.
     */
    public final static int TiLeNap = 1;

    /**
     * TiLeRoiDoCold: int - Tỷ lệ rơi đồ cold (một loại vật phẩm) trong game. Giá trị mặc định: 30000.
     */
    public final static int TiLeRoiDoCold = 30000;

    /**
     * TiLeRoiThucAnCold: int - Tỷ lệ rơi thức ăn cold trong game. Giá trị mặc định: 2.
     */
    public final static int TiLeRoiThucAnCold = 2;

    /**
     * Ngoc: long - Giá trị mã hóa (key) dùng để tạo mảng KeyLogin. Giá trị mặc định: 7092435447648304445L.
     */
    private final static long Ngoc = 7092435447648304445L;

    /**
     * Rong: long - Giá trị mã hóa (key) dùng để tạo mảng KeyLogin. Giá trị mặc định: 7164445758408047933L.
     */
    private final static long Rong = 7164445758408047933L;

    /**
     * G8: long - Giá trị mã hóa (key) dùng để tạo mảng KeyLogin. Giá trị mặc định: 7003779621799279933L.
     */
    private final static long G8 = 7003779621799279933L;

    /**
     * Sieu: long - Giá trị mã hóa (key) dùng để tạo mảng KeyLogin và KeyRes. Giá trị mặc định: 7147894771385711933L.
     */
    private final static long Sieu = 7147894771385711933L;

    /**
     * Cap: long - Giá trị mã hóa (key) dùng để tạo mảng KeyLogin và KeyRes. Giá trị mặc định: 1496467063L.
     */
    private final static long Cap = 1496467063L;

    /**
     * VIPPRO: long - Giá trị mã hóa (key) dùng để tạo mảng KeyLogin và KeyRes. Giá trị mặc định: 7236559436276845174L.
     */
    private final static long VIPPRO = 7236559436276845174L;

    /**
     * NQMP: long - Giá trị mã hóa (key) dùng để tạo mảng KeyRes. Giá trị mặc định: 7092683928850480445L.
     */
    private final static long NQMP = 7092683928850480445L;

    /**
     * Anti: long - Giá trị mã hóa (key) dùng để tạo mảng KeyRes. Giá trị mặc định: 5861211921953406269L.
     */
    private final static long Anti = 5861211921953406269L;

    /**
     * Res: long - Giá trị mã hóa (key) dùng để tạo mảng KeyRes. Giá trị mặc định: 1433228922L.
     */
    private final static long Res = 1433228922L;

    /**
     * KeyIcon: byte[] - Mảng byte chứa các giá trị mã hóa, có thể dùng cho icon hoặc mục đích mã hóa khác. Giá trị mặc định: {87, 87, 48, 49, 82, 50, 82, 72, 84, 107, 104, 78, 86, 50, 104, 111, 86, 106, 70, 75, 100, 49, 108, 116, 77, 87, 53, 81, 85, 84, 48, 57}.
     */
    public final static byte[] KeyIcon = new byte[]{
        87, 87, 48, 49, 82, 50, 82, 72, 84, 107,
        104, 78, 86, 50, 104, 111, 86, 106, 70, 75,
        100, 49, 108, 116, 77, 87, 53, 81, 85, 84,
        48, 57
    };

    /**
     * KeyLogin: long[] - Mảng chứa các key mã hóa dùng cho quá trình đăng nhập. Được khởi tạo từ các giá trị Ngoc, Rong, G8, Sieu, Cap, VIPPRO.
     */
    public final static long[] KeyLogin = new long[]{
        Ngoc, Rong, G8, Sieu, Cap, VIPPRO
    };

    /**
     * KeyRes: long[] - Mảng chứa các key mã hóa dùng cho quá trình reset. Được khởi tạo từ các giá trị NQMP, Anti, Res, Sieu, Cap, VIPPRO.
     */
    public final static long[] KeyRes = new long[]{
        NQMP, Anti, Res, Sieu, Cap, VIPPRO
    };
}