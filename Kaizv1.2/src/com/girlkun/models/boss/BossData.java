package com.girlkun.models.boss;

import lombok.Builder;
import lombok.Data;

/**
 * Chứa dữ liệu định nghĩa 1 Boss trong game:
 * - Thông tin cơ bản (tên, giới tính, trang phục)
 * - Chỉ số chiến đấu (damage, HP, skill)
 * - Thông tin bản đồ xuất hiện, lời thoại
 * - Cấu hình cách xuất hiện (thời gian nghỉ, loại xuất hiện, boss đi cùng)
 *
 * Class này chủ yếu dùng để khởi tạo dữ liệu cho Boss,
 * không xử lý logic chiến đấu.
 * 
 * @author Lucifer
 */
@Data
public class BossData {

    // Các kiểu xuất hiện của Boss
    public static final int DEFAULT_APPEAR = 0;       // xuất hiện bình thường
    public static final int APPEAR_WITH_ANOTHER = 1;  // xuất hiện cùng boss khác
    public static final int ANOTHER_LEVEL = 2;        // xuất hiện dạng level khác

    // Thông tin cơ bản
    private String name;       // tên boss
    private byte gender;       // giới tính
    private short[] outfit;    // trang phục (id các item mặc)

    // Chỉ số chiến đấu
    private int dame;          // damage
    private int[] hp;          // máu (có thể nhiều mốc theo level)
    private int[][] skillTemp; // danh sách skill

    // Bản đồ xuất hiện
    private int[] mapJoin;

    // Lời thoại
    private String[] textS;    // khi spawn
    private String[] textM;    // khi đang chiến đấu
    private String[] textE;    // khi chết

    // Cấu hình xuất hiện
    private int secondsRest;           // thời gian nghỉ (hồi sinh)
    private TypeAppear typeAppear;     // kiểu xuất hiện
    private int[] bossesAppearTogether; // danh sách boss xuất hiện cùng

    /**
     * Constructor cơ bản, mặc định:
     * - secondsRest = 0
     * - typeAppear = DEFAULT_APPEAR
     */
    private BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
                     int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
                     String[] textE) {
        this.name = name;
        this.gender = gender;
        this.outfit = outfit;
        this.dame = dame;
        this.hp = hp;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.textS = textS;
        this.textM = textM;
        this.textE = textE;
        this.secondsRest = 0;
        this.typeAppear = TypeAppear.DEFAULT_APPEAR;
    }

    // Constructor có thêm thời gian nghỉ
    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
                    int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
                    String[] textE, int secondsRest) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.secondsRest = secondsRest;
    }

    // Constructor có thêm thời gian nghỉ và boss đi cùng
    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
                    int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
                    String[] textE, int secondsRest, int[] bossesAppearTogether) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest);
        this.bossesAppearTogether = bossesAppearTogether;
    }

    // Constructor có thêm loại xuất hiện
    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
                    int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
                    String[] textE, TypeAppear typeAppear) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.typeAppear = typeAppear;
    }

    // Constructor có thêm thời gian nghỉ và loại xuất hiện
    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
                    int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
                    String[] textE, int secondsRest, TypeAppear typeAppear) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest);
        this.typeAppear = typeAppear;
    }

    /**
     * Constructor dùng @Builder của Lombok.
     * Cho phép build BossData linh hoạt mà không cần truyền đủ tất cả tham số.
     * Một số field như textS, textM, textE mặc định rỗng.
     */
    @Builder
    public BossData(String name, byte gender, int dame, int[] hp,
                    short[] outfit, int[] mapJoin, int[][] skillTemp,
                    int secondsRest, String[] textS, String[] textM,
                    String[] textE) {
        this.name = name;
        this.gender = gender;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.textS = new String[]{};
        this.textM = new String[]{};
        this.textE = new String[]{};
    }
}
