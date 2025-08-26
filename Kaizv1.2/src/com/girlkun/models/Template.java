package com.girlkun.models;

import com.girlkun.consts.ConstMap;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.WayPoint;
import com.girlkun.models.skill.Skill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lớp chứa các lớp mẫu (template) cho các đối tượng trong game như vật phẩm, kỹ năng, NPC, quái vật, bản đồ, v.v.
 * @author Lucifer
 */
public class Template {

    /**
     * Lớp mẫu cho tùy chọn vật phẩm.
     */
    public static class ItemOptionTemplate {

        public int id;

        public String name;

        public int type;

        public ItemOptionTemplate() {
        }

        /**
         * Khởi tạo mẫu tùy chọn vật phẩm.
         * @param id ID của tùy chọn
         * @param name Tên của tùy chọn
         * @param type Loại tùy chọn
         */
        public ItemOptionTemplate(int id, String name, int type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }

    /**
     * Lớp mẫu cho thành tích.
     */
    public static class ArchivementTemplate {
        public int id;

        public String info1;

        public String info2;

        public int money;

        /**
         * Khởi tạo mẫu thành tích.
         */
        public ArchivementTemplate() {
        }
    }

    /**
     * Lớp mẫu cho vật phẩm.
     */
    public static class ItemTemplate {

        public short id;

        public byte type;

        public byte gender;

        public String name;

        public String description;

        public byte level;

        public short iconID;

        public short part;

        public boolean isUpToUp;

        public int strRequire;
        
        public int gold;

        public int gem;

        public int ruby;

        public int head;
        
        public int body;
        
        public int leg;

        public ItemTemplate() {
        }

        /**
         * Khởi tạo mẫu vật phẩm.
         * @param id ID của vật phẩm
         * @param type Loại vật phẩm
         * @param gender Giới tính yêu cầu (0: Trái Đất, 1: Namek, 2: Saiyan)
         * @param name Tên vật phẩm
         * @param description Mô tả vật phẩm
         * @param iconID ID biểu tượng
         * @param part Vị trí trang bị (nếu có)
         * @param isUpToUp Có thể nâng cấp hay không
         * @param strRequire Sức mạnh yêu cầu
         */
        public ItemTemplate(short id, byte type, byte gender, String name, String description, short iconID, short part, boolean isUpToUp, int strRequire) {
            this.id = id;
            this.type = type;
            this.gender = gender;
            this.name = name;
            this.description = description;
            this.iconID = iconID;
            this.part = part;
            this.isUpToUp = isUpToUp;
            this.strRequire = strRequire;
        }
    }

    /**
     * Lớp mẫu cho quái vật.
     */
    public static class MobTemplate {

        public int id;

        public byte type;

        public String name;

        public int hp;

        public byte rangeMove;

        public byte speed;

        public byte dartType;

        public byte percentDame;

        public byte percentTiemNang;

        /**
         * Khởi tạo mẫu quái vật.
         */
        public MobTemplate() {
        }
    }

    /**
     * Lớp mẫu cho NPC.
     */
    public static class NpcTemplate {

        public int id;

        public String name;

        public int head;

        public int body;

        public int leg;

        public int avatar;

        /**
         * Khởi tạo mẫu NPC.
         */
        public NpcTemplate() {
        }
    }

    /**
     * Lớp mẫu cho bản đồ.
     */
    public static class MapTemplate {

        public int id;

        public String name;

        public byte type;

        public byte planetId;

        public byte bgType;

        public byte tileId;

        public byte bgId;

        public byte zones;

        public byte maxPlayerPerZone;

        public List<WayPoint> wayPoints;

        public byte[] mobTemp;

        public byte[] mobLevel;

        public int[] mobHp;

        public short[] mobX;

        public short[] mobY;

        public byte[] npcId;

        public short[] npcX;

        public short[] npcY;

        /**
         * Khởi tạo mẫu bản đồ, khởi tạo danh sách WayPoint rỗng.
         */
        public MapTemplate() {
            this.wayPoints = new ArrayList<>();
        }
    }

    /**
     * Lớp mẫu cho kỹ năng.
     */
    public static class SkillTemplate {

        public byte id;

        public int classId;

        public String name;

        public int maxPoint;

        public int manaUseType;

        public int type;

        public int iconId;

        public String[] description;

        public Skill[] skills;

        public List<Skill> skillss = new ArrayList<>();

        public String damInfo;

        /**
         * Khởi tạo mẫu kỹ năng.
         */
        public SkillTemplate() {
        }
    }

    /**
     * Lớp mẫu cho bộ phận (part) của nhân vật hoặc trang bị.
     */
    public static class Part {

        public int id;

        public int type;

        public List<PartDetail> partDetails;

        /**
         * Khởi tạo mẫu bộ phận, khởi tạo danh sách PartDetail rỗng.
         */
        public Part() {
            this.partDetails = new ArrayList<>();
        }
    }

    /**
     * Lớp mẫu cho chi tiết bộ phận.
     */
    public static class PartDetail {

        public short iconId;

        public byte dx;

        public byte dy;

        /**
         * Khởi tạo chi tiết bộ phận.
         * @param iconId ID biểu tượng
         * @param dx Độ lệch ngang
         * @param dy Độ lệch dọc
         */
        public PartDetail(short iconId, byte dx, byte dy) {
            this.iconId = iconId;
            this.dx = dx;
            this.dy = dy;
        }
    }

    /**
     * Lớp mẫu cho avatar của đầu nhân vật.
     */
    public static class HeadAvatar {

        public int headId;

        public int avatarId;

        /**
         * Khởi tạo mẫu avatar đầu.
         * @param headId ID của đầu
         * @param avatarId ID của avatar
         */
        public HeadAvatar(int headId, int avatarId) {
            this.headId = headId;
            this.avatarId = avatarId;
        }
    }

    /**
     * Lớp mẫu cho túi cờ (FlagBag).
     */
    public static class FlagBag {

        public int id;

        public short iconId;

        public short[] iconEffect;

        public String name;

        public int gold;

        public int gem;

        public int ruby;

        /**
         * Khởi tạo mẫu túi cờ.
         */
        public FlagBag() {
        }
    }

    /**
     * Lớp mẫu cho vật phẩm trong vòng quay may mắn.
     */
    public static class ItemLuckyRound {

        public Template.ItemTemplate temp;

        public int ratio;

        public int typeRatio;

        public List<ItemOptionLuckyRound> itemOptions;

        /**
         * Khởi tạo mẫu vật phẩm vòng quay may mắn, khởi tạo danh sách tùy chọn rỗng.
         */
        public ItemLuckyRound() {
            this.itemOptions = new ArrayList<>();
        }
    }

    /**
     * Lớp mẫu cho tùy chọn vật phẩm trong vòng quay may mắn.
     */
    public static class ItemOptionLuckyRound {

        public Item.ItemOption itemOption;

        public int param1;

        public int param2;

        /**
         * Khởi tạo mẫu tùy chọn vật phẩm vòng quay may mắn, đặt param2 mặc định là -1.
         */
        public ItemOptionLuckyRound() {
            this.param2 = -1;
        }
    }
}