package com.girlkun.models.skill;

import com.girlkun.models.Template.SkillTemplate;

/**
 * Lớp đại diện cho một kỹ năng của nhân vật trong game, bao gồm các thuộc tính như sát thương, thời gian hồi chiêu, và thông tin mẫu kỹ năng.
 * @author Lucifer
 */
public class Skill {

    /** Khoảng cách tấn công của chiêu đánh (Chiều Đâm). */
    public static final int RANGE_ATTACK_CHIEU_DAM = 100;

    /** Khoảng cách tấn công của chiêu chương (Chiều Chương). */
    public static final int RANGE_ATTACK_CHIEU_CHUONG = 300;

    /** ID kỹ năng Demon. */
    public static final byte DEMON = 2;

    /** ID kỹ năng Masenko. */
    public static final byte MASENKO = 3;

    /** ID kỹ năng Trị Thương. */
    public static final byte TRI_THUONG = 7;

    /** ID kỹ năng Makankosappo. */
    public static final byte MAKANKOSAPPO = 11;

    /** ID kỹ năng Đệ Trùng. */
    public static final byte DE_TRUNG = 12;

    /** ID kỹ năng Liên Hoàn. */
    public static final byte LIEN_HOAN = 17;

    /** ID kỹ năng Socola. */
    public static final byte SOCOLA = 18;

    /** ID kỹ năng Galick. */
    public static final byte GALICK = 4;

    /** ID kỹ năng Antomic. */
    public static final byte ANTOMIC = 5;

    /** ID kỹ năng Tái Tạo Năng Lượng. */
    public static final byte TAI_TAO_NANG_LUONG = 8;

    /** ID kỹ năng Biến Khỉ. */
    public static final byte BIEN_KHI = 13;

    /** ID kỹ năng Tự Sát. */
    public static final byte TU_SAT = 14;

    /** ID kỹ năng Huýt Sáo. */
    public static final byte HUYT_SAO = 21;

    /** ID kỹ năng Trói. */
    public static final byte TROI = 23;

    /** ID kỹ năng Rồng. */
    public static final byte DRAGON = 0;

    /** ID kỹ năng Kamejoko. */
    public static final byte KAMEJOKO = 1;

    /** ID kỹ năng Thái Dương Hạ San. */
    public static final byte THAI_DUONG_HA_SAN = 6;

    /** ID kỹ năng Kaioken. */
    public static final byte KAIOKEN = 9;

    /** ID kỹ năng Quả Cầu Kênh Khí. */
    public static final byte QUA_CAU_KENH_KHI = 10;

    /** ID kỹ năng Dịch Chuyển Tức Thời. */
    public static final byte DICH_CHUYEN_TUC_THOI = 20;

    /** ID kỹ năng Thôi Miên. */
    public static final byte THOI_MIEN = 22;

    /** ID kỹ năng Khiên Năng Lượng. */
    public static final byte KHIEN_NANG_LUONG = 19;

    /** ID kỹ năng Super Kame. */
    public static final byte SUPER_KAME = 24;

    /** ID kỹ năng Liên Hoàn Chương. */
    public static final byte LIEN_HOAN_CHUONG = 25;

    /** ID kỹ năng Ma Phong Ba. */
    public static final byte MA_PHONG_BA = 26;

    /** Mẫu kỹ năng của kỹ năng này. */
    public SkillTemplate template;

    /** ID của kỹ năng. */
    public short skillId;

    /** Điểm kỹ năng (mức độ mạnh của kỹ năng). */
    public int point;

    /** Sức mạnh yêu cầu để sử dụng kỹ năng. */
    public long powRequire;

    /** Thời gian hồi chiêu của kỹ năng (ms). */
    public int coolDown;

    /** Thời điểm cuối cùng sử dụng kỹ năng. */
    public long lastTimeUseThisSkill;

    /** Phạm vi tấn công theo trục X. */
    public int dx;

    /** Phạm vi tấn công theo trục Y. */
    public int dy;

    /** Số lượng mục tiêu tối đa có thể tấn công. */
    public int maxFight;

    /** Lượng mana tiêu hao khi sử dụng kỹ năng. */
    public int manaUse;

    /** Sát thương của kỹ năng. */
    public short damage;

    /** Cấp độ hiện tại của kỹ năng. */
    public short currLevel;

    /** Thông tin bổ sung về kỹ năng. */
    public String moreInfo;

    /** Giá để học hoặc nâng cấp kỹ năng. */
    public short price;

    /**
     * Khởi tạo một kỹ năng mặc định.
     */
    public Skill() {
    }

    /**
     * Khởi tạo một kỹ năng dựa trên một kỹ năng khác (sao chép).
     *
     * @param skill Kỹ năng gốc để sao chép.
     */
    public Skill(Skill skill) {
        this.skillId = skill.skillId;
        this.point = skill.point;
        this.powRequire = skill.powRequire;
        this.coolDown = skill.coolDown;
        this.lastTimeUseThisSkill = skill.lastTimeUseThisSkill;
        this.dx = skill.dx;
        this.dy = skill.dy;
        this.maxFight = skill.maxFight;
        this.manaUse = skill.manaUse;
        this.damage = skill.damage;
        this.moreInfo = skill.moreInfo;
        this.price = skill.price;
        this.template = skill.template;
    }

    /**
     * Giải phóng tài nguyên của đối tượng Skill.
     */
    public void dispose() {
        this.template = null;
    }
}