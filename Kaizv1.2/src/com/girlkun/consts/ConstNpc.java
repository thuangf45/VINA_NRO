package com.girlkun.consts;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ConstNpc:
 * Lớp ConstNpc thuộc package com.girlkun.consts, định nghĩa các hằng số tĩnh (static final) liên quan đến NPC (Non-Player Character) trong game. Lớp bao gồm các chuỗi văn bản hướng dẫn cho người chơi (liên quan đến cốt truyện, doanh trại, sự kiện, v.v.), các ID của NPC, và các chỉ số menu (menu index) để xử lý các tương tác với NPC. Ngoài ra, lớp cũng định nghĩa các ID cửa hàng (shop) và các hành động liên quan.
 */
public class ConstNpc {
    /**
     * CALICK_KE_CHUYEN: String - Văn bản cốt truyện do NPC Calick kể, liên quan đến việc quay về quá khứ để cứu Goku và chiến đấu với Android sát thủ.
     */
    public static final String CALICK_KE_CHUYEN = "20 năm trước bọn Android sát thủ đã đánh bại nhóm bảo vệ trái đất của Sôngoku và Cađíc, Pôcôlô ...\n"
            + "Riêng Sôngoku vì bệnh tim nên đã chết trước đó nên không thể tham gia trận đánh...\n"
            + "Từ đó đến nay bọn chúng tàn phá Trái Đất không hề thương tiếc\n"
            + "Cháu và mẹ may mắn sống sót nhờ lẩn trốn tại tần hầm của công ty Capsule...\n"
            + "Cháu tuy cũng là siêu xayda nhưng cũng không thể làm gì được bọn Android sát thủ...\n"
            + "Chỉ có Sôngoku mới có thể đánh bại bọn chúng\n"
            + "mẹ cháu đã chế tạo thành công cỗ máy thời gian\n"
            + "và cháu quay về quá khứ để cứu Sôngoku...\n"
            + "Bệnh của Gôku ở quá khứ là nan y, nhưng với trình độ y học tương lai chỉ cần uống thuốc là khỏi...\n"
            + "Hãy đi theo cháu đến tương lai giúp nhóm của Gôku đánh bạn bọn Android sát thủ\n"
            + "Khi nào chú cần sự giúp đỡ của cháu hãy đến đây nhé";

    /**
     * HUONG_DAN_DOANH_TRAI: String - Văn bản hướng dẫn về doanh trại, giải thích cách tham gia, yêu cầu đồng đội, thời gian giới hạn, và phần thưởng.
     */
    public static final String HUONG_DAN_DOANH_TRAI = "1) Trại độc nhãn là nơi các ngươi không nên vào vì những tướng tá rất mạnh. Hahaha\n"
            + "2) Trong trại độc nhãn, mỗi vị tướng đều giữ ngọc rồng từ 4 sao đến 6 sao, tùy lúc\n"
            + "3) Nếu ngươi thích chết thì cứ việc vào. Nhưng ta chỉ cho vào mỗi ngày một lần thôi, để ngươi khỏi phải chết nhiều, hahaha.\n"
            + "4) Các vị tướng trong trại rất mạnh nhé, các ngươi không đơn giản có thể đánh bại họ bằng cách bình thường như đánh quái được đâu\n"
            + "5) Muốn vào, ngươi phải đi cùng một người đồng đội cùng bang (phải đứng gần ngươi). Nhưng ta khuyên là nên đi 3-4 người cùng.\n"
            + "6) Mỗi lần vào, ngươi chỉ có 30 phút để đánh. Sau 30 phút mà ngươi vẫn không thắng, ta sẽ cho máy bay chở ngươi về nhà.";

    /**
     * HUONG_DAN_BLACK_BALL_WAR: String - Văn bản hướng dẫn về sự kiện chiến tranh ngọc rồng sao đen, giải thích thời gian, cách thắng, và phần thưởng.
     */
    public static final String HUONG_DAN_BLACK_BALL_WAR = "Mỗi ngày từ 20h đến 21h các hành tinh có Ngọc Rồng Sao Đen sẽ xảy ra 1 cuộc đại chiến\n"
            + "Người nào tìm thấy và giữ được Ngọc Rồng Sao Đen sẽ mang phần thưởng về cho bang của mình trong vòng 1 ngày\n"
            + "Lưu ý mỗi bang có thể chiếm hữu nhiều viên khác nhau nhưng nếu cùng loại cũng chỉ nhận được 1 lần phần thưởng đó. Có 2 cách để thắng:\n"
            + "1) Giữ ngọc sao đen trên người hơn 5 phút liên tục\n"
            + "2)Sau 30 phút tham gia tàu sẽ đón về và đang giữ ngọc sao đen trên người";

    /**
     * HUONG_DAN_MAP_MA_BU: String - Văn bản hướng dẫn về sự kiện bản đồ Ma Bư, giải thích thời gian, cách chơi, và phần thưởng.
     */
    public static final String HUONG_DAN_MAP_MA_BU = "Mỗi ngày từ 12h đến 13h tại đại hội võ thuật sẽ xảy ra 1 cuộc đại chiến\n"
            + "Người nào đánh bại các boss ở map sẽ nhận được những món đồ giá trị\n"
            + "Lưu ý khi vào map sẽ chia làm 2 phe riêng biệt\n"
            + "Cố gắng tồn tại và chiến đấu đến Boss cuối cùng\n"
            + "Càng vào sâu trong map sẽ có những vật phẩm đặc biệt";

    /**
     * HUONG_DAN_DOI_SKH_VIP: String - Văn bản hướng dẫn cách tạo trang bị SKH VIP từ trang bị Thiên Sứ và SKH thường.
     */
    public static final String HUONG_DAN_DOI_SKH_VIP = "Nguyên liệu cần để làm SKH VIP là :\n"
            + "1 món Thiên Sứ và 2 món SKH thường\n"
            + "Lưu ý SKH VIP sẽ tạo ra dưa vào món Thiên Sứ\n"
            + "Ví dụ nguyên liệu gồm : Quần Xayda Thiên Tứ + 2 món SKH thường ngẫu nhiên\n"
            + "Bạn sẽ nhận lại được Quần Xayda với chỉ số SKH VIP";

    /**
     * HUONG_DAN_CAY: String - Văn bản hướng dẫn cách kiếm vàng trong game, bao gồm nhiệm vụ Bò Mộng và nạp thẻ.
     */
    public static final String HUONG_DAN_CAY = "Cách để bạn có 1 lượng vàng lớn :\n"
            + "Có thể cày ở nv bò mộng hàng ngày"
            + "Lưu ý mỗi ngày có 20nv Nhiệm vụ càng khó lượng vàng nhận được càng nhiều \n"
            + "Hoặc bạn có thể nạp lần đầu ở npc santa =)))\n"
            + "1k VND = 1k coin ; 1k coin = 3 thỏi vàng";

    /**
     * NPC_DHVT23: String - Văn bản mô tả về Đại hội võ thuật lần thứ 23, giải thích phần thưởng và cơ chế.
     */
    public static final String NPC_DHVT23 = "Đại hội quy tụ nhiều cao thủ như là Jacky CHun, Thiên Xin Hăng, Tàu Bảy Bảy... Phần thường là 1 rương gỗ chưa nhiều vật phẩm giá trị. Khi hạ được 1 đối thủ, phần thưởng sẽ nâng lên 1 cấp. Rương càng cao cấp, vật phaarm trong đó càng giá trị hơn.\n"
            + "Mỗi ngày bạn chỉ được nhận 1 phần thưởng. Bạn hãy cố gắng hết sức mình để nhận phần thưởng xứng đáng nhất nhé..\n";

    // NPC IDs
    /**
     * ONG_GOHAN: byte - ID của NPC Ông Gohan, giá trị 0.
     */
    public static final byte ONG_GOHAN = 0;

    /**
     * ONG_PARAGUS: byte - ID của NPC Ông Paragus, giá trị 1.
     */
    public static final byte ONG_PARAGUS = 1;

    /**
     * ONG_MOORI: byte - ID của NPC Ông Moori, giá trị 2.
     */
    public static final byte ONG_MOORI = 2;

    /**
     * RUONG_DO: byte - ID của NPC Rương Đồ, giá trị 3.
     */
    public static final byte RUONG_DO = 3;

    /**
     * DAU_THAN: byte - ID của NPC Đầu Thần, giá trị 4.
     */
    public static final byte DAU_THAN = 4;

    /**
     * CON_MEO: byte - ID của NPC Con Mèo, giá trị 5.
     */
    public static final byte CON_MEO = 5;

    /**
     * KHU_VUC: byte - ID của NPC Khu Vực, giá trị 6.
     */
    public static final byte KHU_VUC = 6;

    /**
     * BUNMA: byte - ID của NPC Bunma, giá trị 7 (ID trong game: 562).
     */
    public static final byte BUNMA = 7; //562

    /**
     * DENDE: byte - ID của NPC Dende, giá trị 8 (ID trong game: 350).
     */
    public static final byte DENDE = 8; //350

    /**
     * APPULE: byte - ID của NPC Appule, giá trị 9 (ID trong game: 565).
     */
    public static final byte APPULE = 9; //565

    /**
     * DR_DRIEF: byte - ID của NPC Dr. Drief, giá trị 10.
     */
    public static final byte DR_DRIEF = 10;

    /**
     * CARGO: byte - ID của NPC Cargo, giá trị 11.
     */
    public static final byte CARGO = 11;

    /**
     * CUI: byte - ID của NPC Cui, giá trị 12.
     */
    public static final byte CUI = 12;

    /**
     * QUY_LAO_KAME: byte - ID của NPC Quý Lão Kamê, giá trị 13.
     */
    public static final byte QUY_LAO_KAME = 13;

    /**
     * TRUONG_LAO_GURU: byte - ID của NPC Trưởng Lão Guru, giá trị 14.
     */
    public static final byte TRUONG_LAO_GURU = 14;

    /**
     * VUA_VEGETA: byte - ID của NPC Vua Vegeta, giá trị 15.
     */
    public static final byte VUA_VEGETA = 15;

    /**
     * URON: byte - ID của NPC Uron, giá trị 16.
     */
    public static final byte URON = 16;

    /**
     * BO_MONG: byte - ID của NPC Bò Mộng, giá trị 17.
     */
    public static final byte BO_MONG = 17;

    /**
     * THAN_MEO_KARIN: byte - ID của NPC Thần Mèo Karin, giá trị 18.
     */
    public static final byte THAN_MEO_KARIN = 18;

    /**
     * THUONG_DE: byte - ID của NPC Thượng Đế, giá trị 19.
     */
    public static final byte THUONG_DE = 19;

    /**
     * THAN_VU_TRU: byte - ID của NPC Thần Vũ Trụ, giá trị 20.
     */
    public static final byte THAN_VU_TRU = 20;

    /**
     * BA_HAT_MIT: byte - ID của NPC Bà Hạt Mít, giá trị 21 (ID trong game: 1410).
     */
    public static final byte BA_HAT_MIT = 21; //1410

    /**
     * TRONG_TAI: byte - ID của NPC Trọng Tài, giá trị 22.
     */
    public static final byte TRONG_TAI = 22;

    /**
     * GHI_DANH: byte - ID của NPC Ghi Danh, giá trị 23.
     */
    public static final byte GHI_DANH = 23;

    /**
     * RONG_THIENG: byte - ID của NPC Rồng Thiêng, giá trị 24.
     */
    public static final byte RONG_THIENG = 24;

    /**
     * LINH_CANH: byte - ID của NPC Lính Canh, giá trị 25.
     */
    public static final byte LINH_CANH = 25;

    /**
     * DOC_NHAN: byte - ID của NPC Độc Nhãn, giá trị 26.
     */
    public static final byte DOC_NHAN = 26;

    /**
     * RONG_THIENG_NAMEC: byte - ID của NPC Rồng Thiêng Namec, giá trị 27.
     */
    public static final byte RONG_THIENG_NAMEC = 27;

    /**
     * CUA_HANG_KY_GUI: byte - ID của NPC Cửa Hàng Ký Gửi, giá trị 28.
     */
    public static final byte CUA_HANG_KY_GUI = 28;

    /**
     * RONG_OMEGA: byte - ID của NPC Rồng Omega, giá trị 29.
     */
    public static final byte RONG_OMEGA = 29;

    /**
     * RONG_2S: byte - ID của NPC Rồng 2 Sao, giá trị 30.
     */
    public static final byte RONG_2S = 30;

    /**
     * RONG_3S: byte - ID của NPC Rồng 3 Sao, giá trị 31.
     */
    public static final byte RONG_3S = 31;

    /**
     * RONG_4S: byte - ID của NPC Rồng 4 Sao, giá trị 32.
     */
    public static final byte RONG_4S = 32;

    /**
     * RONG_5S: byte - ID của NPC Rồng 5 Sao, giá trị 33.
     */
    public static final byte RONG_5S = 33;

    /**
     * RONG_6S: byte - ID của NPC Rồng 6 Sao, giá trị 34.
     */
    public static final byte RONG_6S = 34;

    /**
     * RONG_7S: byte - ID của NPC Rồng 7 Sao, giá trị 35.
     */
    public static final byte RONG_7S = 35;

    /**
     * RONG_1S: byte - ID của NPC Rồng 1 Sao, giá trị 36.
     */
    public static final byte RONG_1S = 36;

    /**
     * BUNMA_TL: byte - ID của NPC Bunma Tương Lai, giá trị 37.
     */
    public static final byte BUNMA_TL = 37;

    /**
     * CALICK: byte - ID của NPC Calick, giá trị 38.
     */
    public static final byte CALICK = 38;

    /**
     * SANTA: byte - ID của NPC Santa, giá trị 39.
     */
    public static final byte SANTA = 39;

    /**
     * MABU_MAP: byte - ID của NPC Mabu Map, giá trị 40.
     */
    public static final byte MABU_MAP = 40;

    /**
     * TRUNG_THU: byte - ID của NPC Trung Thu, giá trị 41.
     */
    public static final byte TRUNG_THU = 41;

    /**
     * QUOC_VUONG: byte - ID của NPC Quốc Vương, giá trị 42.
     */
    public static final byte QUOC_VUONG = 42;

    /**
     * TO_SU_KAIO: byte - ID của NPC Tố Sư Kaio, giá trị 43.
     */
    public static final byte TO_SU_KAIO = 43;

    /**
     * OSIN: byte - ID của NPC Osin, giá trị 44.
     */
    public static final byte OSIN = 44;

    /**
     * KIBIT: byte - ID của NPC Kibit, giá trị 45.
     */
    public static final byte KIBIT = 45;

    /**
     * BABIDAY: byte - ID của NPC Babiday, giá trị 46.
     */
    public static final byte BABIDAY = 46;

    /**
     * GIUMA_DAU_BO: byte - ID của NPC Giuma Đầu Bò, giá trị 47.
     */
    public static final byte GIUMA_DAU_BO = 47;

    /**
     * NGO_KHONG: byte - ID của NPC Ngộ Không, giá trị 48.
     */
    public static final byte NGO_KHONG = 48;

    /**
     * DUONG_TANG: byte - ID của NPC Đường Tăng, giá trị 49.
     */
    public static final byte DUONG_TANG = 49;

    /**
     * QUA_TRUNG: byte - ID của NPC Quả Trứng, giá trị 50.
     */
    public static final byte QUA_TRUNG = 50;

    /**
     * TRUNGBILL: byte - ID của NPC Trứng Bill, giá trị 50.
     */
    public static final byte TRUNGBILL = 50;

    /**
     * DUA_HAU: byte - ID của NPC Dưa Hấu, giá trị 51.
     */
    public static final byte DUA_HAU = 51;

    /**
     * HUNG_VUONG: byte - ID của NPC Hùng Vương, giá trị 52.
     */
    public static final byte HUNG_VUONG = 52;

    /**
     * TAPION: byte - ID của NPC Tapion, giá trị 53.
     */
    public static final byte TAPION = 53;

    /**
     * LY_TIEU_NUONG: byte - ID của NPC Lý Tiểu Nương, giá trị 54.
     */
    public static final byte LY_TIEU_NUONG = 54;

    /**
     * BILL: byte - ID của NPC Bill, giá trị 55.
     */
    public static final byte BILL = 55;

    /**
     * WHIS: byte - ID của NPC Whis, giá trị 56.
     */
    public static final byte WHIS = 56;

    /**
     * CHAMPA: byte - ID của NPC Champa, giá trị 57.
     */
    public static final byte CHAMPA = 57;

    /**
     * VADOS: byte - ID của NPC Vados, giá trị 58.
     */
    public static final byte VADOS = 58;

    /**
     * TRONG_TAI_: byte - ID của NPC Trọng Tài (thêm), giá trị 59.
     */
    public static final byte TRONG_TAI_ = 59;

    /**
     * GOKU_SSJ: byte - ID của NPC Goku SSJ, giá trị 60.
     */
    public static final byte GOKU_SSJ = 60;

    /**
     * MEO_THAN_TAI: byte - ID của NPC Mèo Thần Tài, giá trị 80.
     */
    public static final byte MEO_THAN_TAI = 80;

    /**
     * GOKU_SSJ_: byte - ID của NPC Goku SSJ (thêm), giá trị 61.
     */
    public static final byte GOKU_SSJ_ = 61;

    /**
     * POTAGE: byte - ID của NPC Potage, giá trị 62.
     */
    public static final byte POTAGE = 62;

    /**
     * JACO: byte - ID của NPC Jaco, giá trị 63.
     */
    public static final byte JACO = 63;

    /**
     * NPC_64: byte - ID của NPC không xác định, giá trị -64.
     */
    public static final byte NPC_64 = -64;

    /**
     * YARIROBE: byte - ID của NPC Yarirobe, giá trị 65.
     */
    public static final byte YARIROBE = 65;

    /**
     * NOI_BANH: byte - ID của NPC Nồi Bánh, giá trị 66.
     */
    public static final byte NOI_BANH = 66;

    /**
     * MR_POPO: byte - ID của NPC Mr. Popo, giá trị 67.
     */
    public static final byte MR_POPO = 67;

    /**
     * PANCHY: byte - ID của NPC Panchy, giá trị 68.
     */
    public static final byte PANCHY = 68;

    /**
     * THO_DAI_CA: byte - ID của NPC Thỏ Đại Ca, giá trị 69.
     */
    public static final byte THO_DAI_CA = 69;

    /**
     * UNKOWN: byte - ID của NPC Unknown, giá trị 72.
     */
    public static final byte UNKOWN = 72;

    /**
     * THAN_GOD: byte - ID của NPC Thần God, giá trị 75.
     */
    public static final byte THAN_GOD = 75;

    /**
     * KHI_DAU_MOI: byte - ID của NPC Khỉ Đầu Mới, giá trị 95.
     */
    public static final byte KHI_DAU_MOI = 95;

    /**
     * CHO_MEO_AN: byte - ID của NPC Chó Mèo Ăn, giá trị 103.
     */
    public static final byte CHO_MEO_AN = 103;

    /**
     * TRUNG_LINH_THU: byte - ID của NPC Trứng Linh Thú, giá trị 97.
     */
    public static final byte TRUNG_LINH_THU = 97;

    /**
     * Monaito: byte - ID của NPC Monaito, giá trị 102.
     */
    public static final byte Monaito = 102;

    /**
     * Granola: byte - ID của NPC Granola, giá trị 96.
     */
    public static final byte Granola = 96;

    /**
     * ADMIN: byte - ID của NPC Admin, giá trị 75.
     */
    public static final byte ADMIN = 75;

    /**
     * NGUDAN: byte - ID của NPC Ngư Dân, giá trị 98.
     */
    public static final byte NGUDAN = 98;

    /**
     * THOREN: byte - ID của NPC Thoren, giá trị 99.
     */
    public static final byte THOREN = 99;

    /**
     * HOANGUC: byte - ID của NPC Hoanguc, giá trị 100.
     */
    public static final byte HOANGUC = 100;

    /**
     * GOHAN_NHAT_NGUYET: byte - ID của NPC Gohan Nhật Nguyệt, giá trị 76.
     */
    public static final byte GOHAN_NHAT_NGUYET = 76;

    /**
     * KAIDO: byte - ID của NPC Kaido, giá trị 78.
     */
    public static final byte KAIDO = 78;

    /**
     * BUNMA1: byte - ID của NPC Bunma 1, giá trị 81.
     */
    public static final byte BUNMA1 = 81;

    /**
     * ADMIN1: byte - ID của NPC Admin 1, giá trị 82.
     */
    public static final byte ADMIN1 = 82;

    // Index menu
    /**
     * GO_UPSTAIRS_MENU: int - ID menu để lên tầng trong bản đồ Mabu, giá trị 10000.
     */
    public static final int GO_UPSTAIRS_MENU = 10000;

    /**
     * IGNORE_MENU: int - ID menu bỏ qua, giá trị 712002.
     */
    public static final int IGNORE_MENU = 712002;

    /**
     * BASE_MENU: int - ID menu cơ bản, giá trị 752002.
     */
    public static final int BASE_MENU = 752002;

    // Index menu Quý Lão Kamê
    /**
     * MENU_OPEN_DBKB: int - ID menu mở Bản Đồ Kho Báu, giá trị 500.
     */
    public static final int MENU_OPEN_DBKB = 500;

    /**
     * MENU_OPENED_DBKB: int - ID menu khi Bản Đồ Kho Báu đã mở, giá trị 501.
     */
    public static final int MENU_OPENED_DBKB = 501;

    /**
     * MENU_ACCEPT_GO_TO_BDKB: int - ID menu xác nhận đi đến Bản Đồ Kho Báu, giá trị 502.
     */
    public static final int MENU_ACCEPT_GO_TO_BDKB = 502;

    /**
     * MENU_EVENT: int - ID menu sự kiện, giá trị 523.
     */
    public static final int MENU_EVENT = 523;

    /**
     * MENU_GIAO_BONG: int - ID menu giao bóng, giá trị 524.
     */
    public static final int MENU_GIAO_BONG = 524;

    /**
     * CONFIRM_DOI_THUONG_SU_KIEN: int - ID menu xác nhận đổi thưởng sự kiện, giá trị 525.
     */
    public static final int CONFIRM_DOI_THUONG_SU_KIEN = 525;

    /**
     * MENU_OPEN_KG: int - ID menu mở Khí Gas, giá trị 526.
     */
    public static final int MENU_OPEN_KG = 526;

    /**
     * MENU_OPENED_KG: int - ID menu khi Khí Gas đã mở, giá trị 527.
     */
    public static final int MENU_OPENED_KG = 527;

    /**
     * MENU_ACCEPT_GO_TO_KG: int - ID menu xác nhận đi đến Khí Gas, giá trị 528.
     */
    public static final int MENU_ACCEPT_GO_TO_KG = 528;

    // Index menu Thần Vũ Trụ
    /**
     * MENU_DI_CHUYEN: int - ID menu di chuyển, giá trị 500.
     */
    public static final int MENU_DI_CHUYEN = 500;

    /**
     * MENU_THIEN_TU: int - ID menu Thiên Tử, giá trị 5134.
     */
    public static final int MENU_THIEN_TU = 5134;

    // Index menu Bà Hạt Mít
    /**
     * MENU_PHA_LE_HOA_TRANG_BI: int - ID menu Pha Lê Hóa Trang Bị, giá trị 500.
     */
    public static final int MENU_PHA_LE_HOA_TRANG_BI = 500;

    /**
     * MENU_CHUYEN_HOA_TRANG_BI: int - ID menu Chuyển Hóa Trang Bị, giá trị 501.
     */
    public static final int MENU_CHUYEN_HOA_TRANG_BI = 501;

    /**
     * MENU_OPTION_SHOP_BUA: int - ID menu tùy chọn cửa hàng bùa, giá trị 502.
     */
    public static final int MENU_OPTION_SHOP_BUA = 502;

    /**
     * MENU_START_COMBINE: int - ID menu bắt đầu kết hợp, giá trị 503.
     */
    public static final int MENU_START_COMBINE = 503;

    /**
     * MENU_PHAN_RA_DO_THAN_LINH: int - ID menu phân rã đồ thần linh, giá trị 504.
     */
    public static final int MENU_PHAN_RA_DO_THAN_LINH = 504;

    /**
     * MENU_NANG_CAP_DO_TS: int - ID menu nâng cấp đồ Thiên Sứ, giá trị 505.
     */
    public static final int MENU_NANG_CAP_DO_TS = 505;

    /**
     * MENU_NANG_DOI_SKH_VIP: int - ID menu nâng cấp đồ SKH VIP, giá trị 506.
     */
    public static final int MENU_NANG_DOI_SKH_VIP = 506;

    /**
     * MENU_NANG_KHI: int - ID menu nâng cấp khí, giá trị 507.
     */
    public static final int MENU_NANG_KHI = 507;

    /**
     * MENU_DAP_DO: int - ID menu đập đồ, giá trị 508.
     */
    public static final int MENU_DAP_DO = 508;

    /**
     * MENU_NANG_LUFFY: int - ID menu nâng cấp Luffy, giá trị 5071.
     */
    public static final int MENU_NANG_LUFFY = 5071;

    /**
     * MENU_NANG_MEO: int - ID menu nâng cấp Mèo, giá trị 2507.
     */
    public static final int MENU_NANG_MEO = 2507;

    // Index menu Khí Gas
    /**
     * MENU_ACCPET_GO_TO_GAS: int - ID menu xác nhận đi đến Khí Gas, giá trị 538.
     */
    public static final int MENU_ACCPET_GO_TO_GAS = 538;

    /**
     * MENU_OPEN_GAS: int - ID menu mở Khí Gas, giá trị 539.
     */
    public static final int MENU_OPEN_GAS = 539;

    /**
     * MENU_OPENED_GAS: int - ID menu khi Khí Gas đã mở, giá trị 540.
     */
    public static final int MENU_OPENED_GAS = 540;

    // Index menu Lính Canh
    /**
     * MENU_JOIN_DOANH_TRAI: int - ID menu tham gia doanh trại, giá trị 502.
     */
    public static final int MENU_JOIN_DOANH_TRAI = 502;

    /**
     * MENU_OPENED_DOANH_TRAI: int - ID menu khi doanh trại đã mở, giá trị 544.
     */
    public static final int MENU_OPENED_DOANH_TRAI = 544;

    // Index menu Con Mèo
    /**
     * MAKE_MATCH_PVP: int - ID menu tạo trận PVP, giá trị 502.
     */
    public static final int MAKE_MATCH_PVP = 502;

    /**
     * MAKE_FRIEND: int - ID menu kết bạn, giá trị 503.
     */
    public static final int MAKE_FRIEND = 503;

    /**
     * REVENGE: int - ID menu báo thù, giá trị 504.
     */
    public static final int REVENGE = 504;

    /**
     * TUTORIAL_SUMMON_DRAGON: int - ID menu hướng dẫn triệu hồi rồng, giá trị 505.
     */
    public static final int TUTORIAL_SUMMON_DRAGON = 505;

    /**
     * SUMMON_SHENRON: int - ID menu triệu hồi Rồng Thần, giá trị 506.
     */
    public static final int SUMMON_SHENRON = 506;

    /**
     * INTRINSIC: int - ID menu nội tại, giá trị 507.
     */
    public static final int INTRINSIC = 507;

    /**
     * CONFIRM_OPEN_INTRINSIC: int - ID menu xác nhận mở nội tại, giá trị 508.
     */
    public static final int CONFIRM_OPEN_INTRINSIC = 508;

    /**
     * CONFIRM_OPEN_INTRINSIC_VIP: int - ID menu xác nhận mở nội tại VIP, giá trị 509.
     */
    public static final int CONFIRM_OPEN_INTRINSIC_VIP = 509;

    /**
     * CONFIRM_LEAVE_CLAN: int - ID menu xác nhận rời bang, giá trị 510.
     */
    public static final int CONFIRM_LEAVE_CLAN = 510;

    /**
     * CONFIRM_NHUONG_PC: int - ID menu xác nhận nhường PC, giá trị 511.
     */
    public static final int CONFIRM_NHUONG_PC = 511;

    /**
     * MENU_ADMIN: int - ID menu admin, giá trị 512.
     */
    public static final int MENU_ADMIN = 512;

    /**
     * BAN_PLAYER: int - ID menu cấm người chơi, giá trị 513.
     */
    public static final int BAN_PLAYER = 513;

    /**
     * BUFF_PET: int - ID menu buff pet, giá trị 514.
     */
    public static final int BUFF_PET = 514;

    /**
     * CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND: int - ID menu xác nhận xóa toàn bộ vật phẩm vòng quay may mắn, giá trị 515.
     */
    public static final int CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND = 515;

    /**
     * MENU_FIND_PLAYER: int - ID menu tìm người chơi, giá trị 516.
     */
    public static final int MENU_FIND_PLAYER = 516;

    /**
     * CONFIRM_DISSOLUTION_CLAN: int - ID menu xác nhận giải tán bang, giá trị 517.
     */
    public static final int CONFIRM_DISSOLUTION_CLAN = 517;

    /**
     * CONFIRM_ACTIVE: int - ID menu xác nhận kích hoạt, giá trị 518.
     */
    public static final int CONFIRM_ACTIVE = 518;

    /**
     * menutd: int - ID menu Trái Đất, giá trị 519.
     */
    public static final int menutd = 519;

    /**
     * menunm: int - ID menu Namec, giá trị 520.
     */
    public static final int menunm = 520;

    /**
     * menuxd: int - ID menu Xayda, giá trị 521.
     */
    public static final int menuxd = 521;

    /**
     * CONFIRM_TELE_NAMEC: int - ID menu xác nhận dịch chuyển đến Namec, giá trị 522.
     */
    public static final int CONFIRM_TELE_NAMEC = 522;

    /**
     * RUT_VANG: int - ID menu rút vàng, giá trị 526.
     */
    public static final int RUT_VANG = 526;

    /**
     * UP_TOP_ITEM: int - ID menu đưa vật phẩm lên top, giá trị 527.
     */
    public static final int UP_TOP_ITEM = 527;

    /**
     * MENU_OPTION_USE_ITEM2000: int - ID menu sử dụng vật phẩm 2000, giá trị 2000.
     */
    public static final int MENU_OPTION_USE_ITEM2000 = 2000;

    /**
     * MENU_OPTION_USE_ITEM2001: int - ID menu sử dụng vật phẩm 2001, giá trị 2001.
     */
    public static final int MENU_OPTION_USE_ITEM2001 = 2001;

    /**
     * MENU_OPTION_USE_ITEM2002: int - ID menu sử dụng vật phẩm 2002, giá trị 2002.
     */
    public static final int MENU_OPTION_USE_ITEM2002 = 2002;

    /**
     * MENU_OPTION_USE_ITEM2003: int - ID menu sử dụng vật phẩm 2003, giá trị 2003.
     */
    public static final int MENU_OPTION_USE_ITEM2003 = 2003;

    /**
     * MENU_OPTION_USE_ITEM2004: int - ID menu sử dụng vật phẩm 2004, giá trị 2004.
     */
    public static final int MENU_OPTION_USE_ITEM2004 = 2004;

    /**
     * MENU_OPTION_USE_ITEM2005: int - ID menu sử dụng vật phẩm 2005, giá trị 2005.
     */
    public static final int MENU_OPTION_USE_ITEM2005 = 2005;

    /**
     * MENU_OPTION_USE_ITEM736: int - ID menu sử dụng vật phẩm 736, giá trị 736.
     */
    public static final int MENU_OPTION_USE_ITEM736 = 736;

    /**
     * MENU_OPTION_USE_ITEM1105: int - ID menu sử dụng vật phẩm 1105, giá trị 1105.
     */
    public static final int MENU_OPTION_USE_ITEM1105 = 1105;

    // Index menu Rồng Thiêng
    /**
     * SHENRON_CONFIRM: int - ID menu xác nhận với Rồng Thần, giá trị 501.
     */
    public static final int SHENRON_CONFIRM = 501;

    /**
     * SHENRON_1_1: int - ID menu lựa chọn 1-1 của Rồng Thần, giá trị 502.
     */
    public static final int SHENRON_1_1 = 502;

    /**
     * SHENRON_1_2: int - ID menu lựa chọn 1-2 của Rồng Thần, giá trị 503.
     */
    public static final int SHENRON_1_2 = 503;

    /**
     * SHENRON_2: int - ID menu lựa chọn 2 của Rồng Thần, giá trị 504.
     */
    public static final int SHENRON_2 = 504;

    /**
     * SHENRON_3: int - ID menu lựa chọn 3 của Rồng Thần, giá trị 505.
     */
    public static final int SHENRON_3 = 505;

    /**
     * NAMEC_1: int - ID menu liên quan đến Namec, giá trị 506.
     */
    public static final int NAMEC_1 = 506;

    /**
     * XU_HRZ: int - ID menu liên quan đến Xu HRZ, giá trị 5099.
     */
    public static final int XU_HRZ = 5099;

    // Index menu Magic Tree
    /**
     * MAGIC_TREE_NON_UPGRADE_LEFT_PEA: int - ID menu cây đậu chưa nâng cấp (còn đậu), giá trị 501.
     */
    public static final int MAGIC_TREE_NON_UPGRADE_LEFT_PEA = 501;

    /**
     * MAGIC_TREE_NON_UPGRADE_FULL_PEA: int - ID menu cây đậu chưa nâng cấp (đầy đậu), giá trị 502.
     */
    public static final int MAGIC_TREE_NON_UPGRADE_FULL_PEA = 502;

    /**
     * MAGIC_TREE_CONFIRM_UPGRADE: int - ID menu xác nhận nâng cấp cây đậu, giá trị 503.
     */
    public static final int MAGIC_TREE_CONFIRM_UPGRADE = 503;

    /**
     * MAGIC_TREE_UPGRADE: int - ID menu nâng cấp cây đậu, giá trị 504.
     */
    public static final int MAGIC_TREE_UPGRADE = 504;

    /**
     * MAGIC_TREE_CONFIRM_UNUPGRADE: int - ID menu xác nhận hủy nâng cấp cây đậu, giá trị 505.
     */
    public static final int MAGIC_TREE_CONFIRM_UNUPGRADE = 505;

    // Index menu Mabu Egg
    /**
     * CAN_NOT_OPEN_EGG: int - ID menu không thể mở trứng Mabu, giá trị 500.
     */
    public static final int CAN_NOT_OPEN_EGG = 500;

    /**
     * CAN_OPEN_EGG: int - ID menu có thể mở trứng Mabu, giá trị 501.
     */
    public static final int CAN_OPEN_EGG = 501;

    /**
     * CONFIRM_OPEN_EGG: int - ID menu xác nhận mở trứng Mabu, giá trị 502.
     */
    public static final int CONFIRM_OPEN_EGG = 502;

    /**
     * CONFIRM_DESTROY_EGG: int - ID menu xác nhận hủy trứng Mabu, giá trị 503.
     */
    public static final int CONFIRM_DESTROY_EGG = 503;

    // Index menu Trứng Bill
    /**
     * CAN_NOT_OPEN_BILL: int - ID menu không thể mở trứng Bill, giá trị 500.
     */
    public static final int CAN_NOT_OPEN_BILL = 500;

    /**
     * CAN_OPEN_BILL: int - ID menu có thể mở trứng Bill, giá trị 501.
     */
    public static final int CAN_OPEN_BILL = 501;

    /**
     * CONFIRM_OPEN_BILL: int - ID menu xác nhận mở trứng Bill, giá trị 502.
     */
    public static final int CONFIRM_OPEN_BILL = 502;

    /**
     * CONFIRM_DESTROY_BILL: int - ID menu xác nhận hủy trứng Bill, giá trị 503.
     */
    public static final int CONFIRM_DESTROY_BILL = 503;

    // Index menu NPC Nhà
    /**
     * QUA_TAN_THU: int - ID menu quà tân thủ, giá trị 500.
     */
    public static final int QUA_TAN_THU = 500;

    /**
     * MENU_PHAN_THUONG: int - ID menu phần thưởng, giá trị 501.
     */
    public static final int MENU_PHAN_THUONG = 501;

    /**
     * NAP_THE: int - ID menu nạp thẻ, giá trị 5022.
     */
    public static final int NAP_THE = 5022;

    /**
     * VIETTEL: int - ID menu Viettel, giá trị 5033.
     */
    public static final int VIETTEL = 5033;

    /**
     * MOBIFONE: int - ID menu Mobifone, giá trị 5044.
     */
    public static final int MOBIFONE = 5044;

    /**
     * VINAPHONE: int - ID menu Vinaphone, giá trị 5055.
     */
    public static final int VINAPHONE = 5055;

    /**
     * QUY_DOI_THOI_VANG: int - ID menu quy đổi thỏi vàng, giá trị 5066.
     */
    public static final int QUY_DOI_THOI_VANG = 5066;

    /**
     * QUY_DOI_HONG_NGOC: int - ID menu quy đổi hồng ngọc, giá trị 5077.
     */
    public static final int QUY_DOI_HONG_NGOC = 5077;

    /**
     * QUY_DOI: int - ID menu quy đổi, giá trị 5088.
     */
    public static final int QUY_DOI = 5088;

    /**
     * QUY_DOI_HN: int - ID menu quy đổi hồng ngọc (thêm), giá trị 50887.
     */
    public static final int QUY_DOI_HN = 50887;

    // Index menu Quốc Vương
    /**
     * OPEN_POWER_MYSEFT: int - ID menu mở sức mạnh bản thân, giá trị 500.
     */
    public static final int OPEN_POWER_MYSEFT = 500;

    /**
     * OPEN_POWER_PET: int - ID menu mở sức mạnh pet, giá trị 501.
     */
    public static final int OPEN_POWER_PET = 501;

    // Index menu Thượng Đế
    /**
     * MENU_CHOOSE_LUCKY_ROUND: int - ID menu chọn vòng quay may mắn, giá trị 500.
     */
    public static final int MENU_CHOOSE_LUCKY_ROUND = 500;

    // Index menu Cui
    /**
     * MENU_FIND_KUKU: int - ID menu tìm Kuku, giá trị 501.
     */
    public static final int MENU_FIND_KUKU = 501;

    /**
     * MENU_FIND_MAP_DAU_DINH: int - ID menu tìm Map Đầu Đinh, giá trị 502.
     */
    public static final int MENU_FIND_MAP_DAU_DINH = 502;

    /**
     * MENU_FIND_RAMBO: int - ID menu tìm Rambo, giá trị 503.
     */
    public static final int MENU_FIND_RAMBO = 503;

    // Index menu Rồng Omega
    /**
     * MENU_NOT_OPEN_BDW: int - ID menu khi chiến tranh ngọc rồng đen chưa mở, giá trị 500.
     */
    public static final int MENU_NOT_OPEN_BDW = 500;

    /**
     * MENU_OPEN_BDW: int - ID menu mở chiến tranh ngọc rồng đen, giá trị 501.
     */
    public static final int MENU_OPEN_BDW = 501;

    /**
     * MENU_REWARD_BDW: int - ID menu phần thưởng chiến tranh ngọc rồng đen, giá trị 502.
     */
    public static final int MENU_REWARD_BDW = 502;

    // Index menu Osin
    /**
     * MENU_NOT_OPEN_MMB: int - ID menu khi bản đồ Ma Bư chưa mở, giá trị 500.
     */
    public static final int MENU_NOT_OPEN_MMB = 500;

    /**
     * MENU_OPEN_MMB: int - ID menu mở bản đồ Ma Bư, giá trị 501.
     */
    public static final int MENU_OPEN_MMB = 501;

    /**
     * MENU_REWARD_MMB: int - ID menu phần thưởng bản đồ Ma Bư, giá trị 502.
     */
    public static final int MENU_REWARD_MMB = 502;

    // Index menu Rồng Sao Đen
    /**
     * MENU_PHU_HP: int - ID menu phục hồi HP, giá trị 500.
     */
    public static final int MENU_PHU_HP = 500;

    /**
     * MENU_OPTION_PHU_HP: int - ID menu tùy chọn phục hồi HP, giá trị 501.
     */
    public static final int MENU_OPTION_PHU_HP = 501;

    /**
     * MENU_OPTION_GO_HOME: int - ID menu tùy chọn về nhà, giá trị 502.
     */
    public static final int MENU_OPTION_GO_HOME = 502;

    // Index menu Bò Mộng
    /**
     * MENU_OPTION_LEVEL_SIDE_TASK: int - ID menu cấp độ nhiệm vụ phụ, giá trị 500.
     */
    public static final int MENU_OPTION_LEVEL_SIDE_TASK = 500;

    /**
     * MENU_OPTION_PAY_SIDE_TASK: int - ID menu thanh toán nhiệm vụ phụ, giá trị 501.
     */
    public static final int MENU_OPTION_PAY_SIDE_TASK = 501;

    /**
     * SHOW_LIST_TOP: int - ID menu hiển thị danh sách top, giá trị 502.
     */
    public static final int SHOW_LIST_TOP = 502;

    // Shop IDs
    /**
     * SHOP_BUNMA_QK_0: int - ID cửa hàng Bunma (Quá Khứ) 0, giá trị 500.
     */
    public static final int SHOP_BUNMA_QK_0 = 500;

    /**
     * SHOP_DENDE_0: int - ID cửa hàng Dende 0, giá trị 501.
     */
    public static final int SHOP_DENDE_0 = 501;

    /**
     * SHOP_APPULE_0: int - ID cửa hàng Appule 0, giá trị 502.
     */
    public static final int SHOP_APPULE_0 = 502;

    /**
     * SHOP_SANTA_0: int - ID cửa hàng Santa 0, giá trị 503.
     */
    public static final int SHOP_SANTA_0 = 503;

    /**
     * SHOP_SANTA_1: int - ID cửa hàng Santa 1, giá trị 504.
     */
    public static final int SHOP_SANTA_1 = 504;

    /**
     * SHOP_URON_0: int - ID cửa hàng Uron 0, giá trị 505.
     */
    public static final int SHOP_URON_0 = 505;

    /**
     * SHOP_BA_HAT_MIT_0: int - ID cửa hàng Bà Hạt Mít 0, giá trị 506.
     */
    public static final int SHOP_BA_HAT_MIT_0 = 506;

    /**
     * SHOP_BA_HAT_MIT_1: int - ID cửa hàng Bà Hạt Mít 1, giá trị 507.
     */
    public static final int SHOP_BA_HAT_MIT_1 = 507;

    /**
     * SHOP_BA_HAT_MIT_2: int - ID cửa hàng Bà Hạt Mít 2, giá trị 508.
     */
    public static final int SHOP_BA_HAT_MIT_2 = 508;

    /**
     * SIDE_BOX_LUCKY_ROUND: int - ID hộp vòng quay may mắn, giá trị 509.
     */
    public static final int SIDE_BOX_LUCKY_ROUND = 509;

    /**
     * SHOP_BUNMA_TL_0: int - ID cửa hàng Bunma Tương Lai 0, giá trị 510.
     */
    public static final int SHOP_BUNMA_TL_0 = 510;

    /**
     * SIDE_BOX_ITEM_REWARD: int - ID hộp phần thưởng vật phẩm, giá trị 511.
     */
    public static final int SIDE_BOX_ITEM_REWARD = 511;

    /**
     * CALL_BOSS: int - ID gọi boss, giá trị 599.
     */
    public static final int CALL_BOSS = 599;

    /**
     * RUONG_GO: int - ID rương gỗ, giá trị 512.
     */
    public static final int RUONG_GO = 512;

    /**
     * MENU_DAP_DO_KICH_HOAT: int - ID menu đập đồ kích hoạt, giá trị 550.
     */
    public static final int MENU_DAP_DO_KICH_HOAT = 550;

    /**
     * MENU_DAP_DO_KICH_HOAT_THUONG: int - ID menu đập đồ kích hoạt thường, giá trị 800.
     */
    public static final int MENU_DAP_DO_KICH_HOAT_THUONG = 800;

    /**
     * MENU_DHVT: int - ID menu Đại Hội Võ Thuật, giá trị 560.
     */
    public static final int MENU_DHVT = 560;

    /**
     * MENU_MUA_CAPSULE: int - ID menu mua capsule, giá trị 2010.
     */
    public static final int MENU_MUA_CAPSULE = 2010;

    /**
     * MENU_MUA_CAPSULE1: int - ID menu mua capsule 1, giá trị 2002.
     */
    public static final int MENU_MUA_CAPSULE1 = 2002;
}