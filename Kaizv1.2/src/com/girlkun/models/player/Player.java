package com.girlkun.models.player;

import com.arriety.card.Card;
import com.arriety.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.skill.PlayerSkill;
import java.util.List;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.intrinsic.IntrinsicPlayer;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.ItemTime;
import com.girlkun.models.npc.specialnpc.MagicTree;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.mob.MobMe;
import com.girlkun.data.DataGame;
import com.girlkun.models.Effect.EffectChar;
import com.girlkun.models.Effect.EffectService;
import com.girlkun.models.Effect.HaoQuang;
import com.girlkun.models.Effect.VongChan;
import com.girlkun.models.Event.CauCa;
import com.girlkun.models.ThanhTich.ThanhTich;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.map.BDKB.BanDoKhoBauService;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.map.Zone;
//import com.girlkun.models.yadat.Yadat;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.matches.IPVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.npc.specialnpc.BillEgg;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.server.io.MySession;
import com.girlkun.models.task.TaskPlayer;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.FriendAndEnemyService;
import com.girlkun.services.MapService;
import com.girlkun.services.PetService;
import com.girlkun.services.TaskService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.ChonAiDay;
import com.girlkun.services.func.CombineNew;
import com.girlkun.services.func.TopService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import javafx.scene.effect.Effect;

/**
 * Lớp Player đại diện cho nhân vật người chơi trong trò chơi, quản lý các thuộc tính, trạng thái, và hành động.
 */
public class Player {

    /**
     * Kiểm tra trạng thái bắt đầu của người chơi.
     */
    public boolean SetStart;

    /**
     * Thời điểm bắt đầu lần cuối.
     */
    public long LastStart;

    /**
     * Danh sách các hiệu ứng đang áp dụng lên người chơi.
     */
    public List<EffectChar> ListEffect = new ArrayList<>();

    /**
     * Danh sách các danh hiệu của người chơi.
     */
    public List<Item> ListDanhHieu = new ArrayList<>();

    /**
     * Số vàng cược trong trò chơi Tài Xỉu.
     */
    public int goldTai;

    /**
     * Số vàng cược trong trò chơi Xỉu.
     */
    public int goldXiu;

    /**
     * Phiên kết nối của người chơi.
     */
    public MySession session;

    /**
     * Kiểm tra trạng thái trước khi giải phóng tài nguyên.
     */
    public boolean beforeDispose;

    /**
     * Danh sách thành tích của người chơi.
     */
    public List<ThanhTich> Archivement = new ArrayList<>();

    /**
     * Kiểm tra nhân vật có phải là thú cưng không.
     */
    public boolean isPet;

    /**
     * Kiểm tra nhân vật có phải là thú cưng mới không.
     */
    public boolean isNewPet;

    /**
     * Thời gian online của người chơi.
     */
    public int TimeOnline = 0;

    /**
     * Kiểm tra đã hoàn thành Doanh Trại Độc Nhãn hay chưa.
     */
    public boolean DoneDTDN = false;

    /**
     * Kiểm tra đã hoàn thành Bản Đồ Kho Báu hay chưa.
     */
    public boolean DoneDKB = false;

    /**
     * Kiểm tra đã tham gia Ngọc Rồng Sao Đen hay chưa.
     */
    public boolean JoinNRSD = false;

    /**
     * Kiểm tra đã hoàn thành Ngọc Rồng Sao Đen hay chưa.
     */
    public boolean DoneNRSD = false;

    /**
     * Số lần câu cá.
     */
    public int TickCauCa = 0;

    /**
     * Số lần nạp trong ngày.
     */
    public int NapNgay = 0;

    /**
     * Thời điểm online lần cuối.
     */
    public long LastTimeOnline = System.currentTimeMillis() + 30000;

    /**
     * Kiểm tra trạng thái tích xanh.
     */
    public boolean tickxanh = false;

    /**
     * Kiểm tra nhân vật có phải là Boss không.
     */
    public boolean isBoss;

    /**
     * Kiểm tra nhân vật có phải là Yadart không.
     */
    public boolean isYadat;

    /**
     * Điểm Ngọc Hành Sơn.
     */
    public int NguHanhSonPoint = 0;

    /**
     * Đối tượng PvP của người chơi.
     */
    public IPVP pvp;

    /**
     * Điểm PvP của người chơi.
     */
    public int pointPvp;

    /**
     * Hiệu ứng hào quang siêu cấp.
     */
    public int SuperAura;

    /**
     * Điểm Boss của người chơi.
     */
    public int PointBoss;

    /**
     * Thời gian tối đa (thường dùng cho hiệu ứng hoặc trạng thái).
     */
    public byte maxTime = 30;

    /**
     * Loại nhân vật (0: người chơi, 1: thú cưng, ...).
     */
    public byte type = 0;

    /**
     * Số lần reset kỹ năng.
     */
    public int ResetSkill = 0;

    /**
     * ID bản đồ trước khi đăng xuất.
     */
    public int mapIdBeforeLogout;

    /**
     * Danh sách các bản đồ Ngọc Rồng Sao Đen.
     */
    public List<Zone> mapBlackBall;

    /**
     * Danh sách các bản đồ Ma Bư.
     */
    public List<Zone> mapMaBu;

    /**
     * Giới hạn vàng của người chơi.
     */
    public long limitgold = 0;

    /**
     * Thời điểm tham gia Doanh Trại lần cuối.
     */
    public long LastDoanhTrai = 0;

    /**
     * Khu vực hiện tại của người chơi.
     */
    public Zone zone;

    /**
     * Bản đồ trước khi vào capsule.
     */
    public Zone mapBeforeCapsule;

    /**
     * Danh sách các bản đồ capsule.
     */
    public List<Zone> mapCapsule;

    /**
     * Thú cưng của người chơi.
     */
    public Pet pet;

    /**
     * Thú cưng mới của người chơi.
     */
    public NewPet newpet;

    /**
     * Quái vật đi theo người chơi.
     */
    public MobMe mobMe;

    /**
     * Vị trí của người chơi.
     */
    public Location location;

    /**
     * Bộ trang bị của người chơi.
     */
    public SetClothes setClothes;

    /**
     * Hiệu ứng kỹ năng của người chơi.
     */
    public EffectSkill effectSkill;

    /**
     * Trứng Mabu của người chơi.
     */
    public MabuEgg mabuEgg;

    /**
     * Trứng Bill của người chơi.
     */
    public BillEgg billEgg;

    /**
     * Nhiệm vụ của người chơi.
     */
    public TaskPlayer playerTask;

    /**
     * Thời gian sử dụng vật phẩm.
     */
    public ItemTime itemTime;

    /**
     * Quản lý hợp thể của người chơi.
     */
    public Fusion fusion;

    /**
     * Cây đậu thần của người chơi.
     */
    public MagicTree magicTree;

    /**
     * Nội tại của người chơi.
     */
    public IntrinsicPlayer playerIntrinsic;

    /**
     * Kho đồ của người chơi.
     */
    public Inventory inventory;

    /**
     * Kỹ năng của người chơi.
     */
    public PlayerSkill playerSkill;

    /**
     * Quản lý ghép đồ mới.
     */
    public CombineNew combineNew;

    /**
     * Đánh dấu ID của người chơi.
     */
    public IDMark iDMark;

    /**
     * Bùa của người chơi.
     */
    public Charms charms;

    /**
     * Hiệu ứng da của người chơi.
     */
    public EffectSkin effectSkin;

    /**
     * Điểm sức mạnh, máu, mana của người chơi.
     */
    public NPoint nPoint;

    /**
     * Phần thưởng Ngọc Rồng Sao Đen.
     */
    public RewardBlackBall rewardBlackBall;

    /**
     * Hiệu ứng cờ của người chơi.
     */
    public EffectFlagBag effectFlagBag;

    /**
     * Quản lý chiến đấu với Mabu.
     */
    public FightMabu fightMabu;

    /**
     * Kỹ năng đặc biệt của người chơi.
     */
    public SkillSpecial skillSpecial;

    /**
     * Bang hội của người chơi.
     */
    public Clan clan;

    /**
     * Thành viên bang hội của người chơi.
     */
    public ClanMember clanMember;

    /**
     * Danh sách bạn bè.
     */
    public List<Friend> friends;

    /**
     * Danh sách kẻ thù.
     */
    public List<Enemy> enemies;

    /**
     * ID duy nhất của người chơi.
     */
    public long id;

    /**
     * Tên của người chơi.
     */
    public String name;

    /**
     * Giới tính của người chơi (0: Trái Đất, 1: Namếc, 2: Xayda).
     */
    public byte gender;

    /**
     * Kiểm tra người chơi có phải là thành viên mới không.
     */
    public boolean isNewMember;

    /**
     * ID đầu của người chơi.
     */
    public short head;

    /**
     * ID vòng chân thứ nhất.
     */
    public int idVongChan = -1;

    /**
     * ID vòng chân thứ hai.
     */
    public int idVongChan2 = -1;

    /**
     * Loại PK của người chơi.
     */
    public byte typePk;

    /**
     * Cờ của người chơi.
     */
    public byte cFlag;

    /**
     * Kiểm tra có tàu vũ trụ tennis không.
     */
    public boolean haveTennisSpaceShip;

    /**
     * Kiểm tra vừa được hồi sinh.
     */
    public boolean justRevived;

    /**
     * Thời điểm hồi sinh lần cuối.
     */
    public long lastTimeRevived;

    /**
     * Kiểm tra bị ban vàng.
     */
    public boolean banv = false;

    /**
     * Kiểm tra đã mua vàng.
     */
    public boolean muav = false;

    /**
     * Thời gian sử dụng dịch vụ vàng.
     */
    public long timeudbv = 0;

    /**
     * Thời gian sử dụng dịch vụ mua vàng.
     */
    public long timeudmv = 0;

    /**
     * Thời điểm bị ban vàng lần cuối.
     */
    public long lasttimebanv;

    /**
     * Thời điểm mua vàng lần cuối.
     */
    public long lasttimemuav;

    /**
     * Số lần vi phạm của người chơi.
     */
    public int violate;

    /**
     * Tổng số người chơi vi phạm.
     */
    public byte totalPlayerViolate;

    /**
     * Thời điểm chuyển khu vực lần cuối.
     */
    public long timeChangeZone;

    /**
     * Thời điểm sử dụng tùy chọn lần cuối.
     */
    public long lastTimeUseOption;

    /**
     * ID của Ngọc Rồng Namek đang cầm.
     */
    public short idNRNM = -1;

    /**
     * ID của viên đá đi.
     */
    public short idGo = -1;

    /**
     * Thời điểm nhặt Ngọc Rồng Namek lần cuối.
     */
    public long lastTimePickNRNM;

    /**
     * Số vàng thường.
     */
    public int goldNormar;

    /**
     * Số vàng VIP.
     */
    public int goldVIP;

    /**
     * Thời điểm chiến thắng lần cuối.
     */
    public long lastTimeWin;

    /**
     * Kiểm tra đã chiến thắng hay chưa.
     */
    public boolean isWin;

    /**
     * Danh sách thẻ bài của người chơi.
     */
    public List<Card> Cards = new ArrayList<>();

    /**
     * ID hiệu ứng hào quang.
     */
    public short idAura = -1;

    /**
     * Số tiền VND.
     */
    public int vnd;

    /**
     * Thời điểm điểm danh.
     */
    public long diemdanh;

    /**
     * Số lần giới thiệu.
     */
    public int gioithieu;

    /**
     * Tổng số tiền VND.
     */
    public int VND;

    /**
     * Tổng số tiền nạp.
     */
    public int tongnap;

    /**
     * Tổng số tiền nạp (bản sao).
     */
    public int TONGNAP;

    /**
     * Cấp độ rương gỗ.
     */
    public int levelWoodChest;

    /**
     * Kiểm tra đã nhận rương gỗ hay chưa.
     */
    public boolean receivedWoodChest;

    /**
     * Số vàng trong thử thách.
     */
    public int goldChallenge;

    /**
     * Kiểm tra đã tham gia Bản Đồ Kho Báu hay chưa.
     */
    public boolean bdkb_isJoinBdkb;

    /**
     * Quản lý câu cá của người chơi.
     */
    public CauCa cauca;

    /**
     * Dữ liệu nhiệm vụ.
     */
    public int data_task;

    /**
     * Kiểm tra có sử dụng danh hiệu vật phẩm hay không.
     */
    public boolean titleitem;

    /**
     * Phần danh hiệu của người chơi.
     */
    public int partDanhHieu;

    /**
     * Kiểm tra trạng thái bị bù nhìn.
     */
    public boolean isBuNhin = false;

    /**
     * Danh sách thành tích (bản sao của Archivement).
     */
    public List<Archivement> archivementList = new ArrayList<>();

    /**
     * Khởi tạo người chơi với các thuộc tính mặc định.
     */
    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        cauca = new CauCa(this);
    }

    /**
     * Kiểm tra người chơi có chết hay không.
     * @return true nếu HP bằng 0, false nếu không.
     */
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    /**
     * Thiết lập phiên kết nối cho người chơi.
     * @param session Phiên kết nối.
     */
    public void setSession(MySession session) {
        this.session = session;
    }

    /**
     * Gửi thông điệp đến người chơi.
     * @param msg Thông điệp cần gửi.
     */
    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    /**
     * Lấy phiên kết nối của người chơi.
     * @return Phiên kết nối hiện tại.
     */
    public MySession getSession() {
        return this.session;
    }

    /**
     * Kiểm tra nhân vật có phải là người chơi hay không (không phải thú cưng hoặc boss).
     * @return true nếu là người chơi, false nếu không.
     */
    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet;
    }

    /**
     * Gửi đoạn chat của người chơi.
     * @param text Nội dung chat.
     */
    public void chat(String text) {
        Service.gI().chat(this, text);
    }

    /**
     * Cập nhật trạng thái của người chơi và các đối tượng liên quan.
     */
    public void update() {
        if (this != null && this.name != null && !this.beforeDispose) {
            try {
                if (!iDMark.isBan()) {
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkill != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    MapMaBu.gI().update(this);
                    BlackBallWar.gI().update(this);
                    if (this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    }
                    if (this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        this.iDMark.setGoToBDKB(false);
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if ((this.isPl() || this.isPet) && this.inventory.itemsBody.size() == 11
                            && this.inventory.itemsBody.get(7) != null) {
                        Item it = this.inventory.itemsBody.get(7);
                        if (it != null && it.isNotNullItem() && this.newpet == null) {
                            PetService.Pet2(this, it.template.head, it.template.body, it.template.leg,
                                    it.template.name);
                            Service.getInstance().point(this);
                        }
                    } else if ((this.isPl() || this.isPet) && this.inventory.itemsBody.size() == 11 && newpet != null
                            && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                        newpet.dispose();
                        newpet = null;
                    }
                    if (this.isPl() && isWin && this.zone.map.mapId == 51 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
                    if (location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(getSession());
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    /**
     * Mảng chứa ID trang bị hợp thể theo giới tính và loại hợp thể.
     */
    private static final short[][] idOutfitFusion = {
        {380, 381, 382}, // Lưỡng Long Nhất Thể (Trái Đất/Xayda)
        {383, 384, 385}, // Porata (Trái Đất/Xayda)
        {391, 392, 393}, // Hợp thể chung (Namếc)
        {870, 871, 872}, // Trái Đất cấp 2
        {873, 874, 875}, // Namếc cấp 2
        {867, 868, 869}, // Xayda cấp 2
        {2057, 2058, 2059}, // Trái Đất cấp 3
        {2060, 2061, 2062}, // Namếc cấp 3
        {2063, 2064, 2065}, // Xayda cấp 3
        {2048, 2049, 2050}, // Trái Đất cấp 4
        {2051, 2052, 2053}, // Namếc cấp 4
        {2054, 2055, 2056}, // Xayda cấp 4
    };

    /**
     * Lấy cấp độ hiệu ứng trước dựa trên trang bị.
     * @return Cấp độ hiệu ứng (4-8) hoặc -1 nếu không đủ điều kiện.
     */
    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        int levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        int levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        int levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        int levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        int levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    /**
     * Lấy ID đầu của người chơi.
     * @return ID đầu dựa trên trạng thái và trang bị.
     */
    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1988;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][0];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][0];
                }
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[6 + this.gender][0];
                }
                return idOutfitFusion[6 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[9 + this.gender][0];
                }
                return idOutfitFusion[9 + this.gender][0];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int head = inventory.itemsBody.get(5).template.head;
            if (head != -1) {
                return (short) head;
            }
        }
        return this.head;
    }

    /**
     * Lấy ID thân của người chơi.
     * @return ID thân dựa trên trạng thái và trang bị.
     */
    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1989;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][1];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][1];
                }
                return idOutfitFusion[3 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[6 + this.gender][1];
                }
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[9 + this.gender][1];
                }
                return idOutfitFusion[9 + this.gender][1];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    /**
     * Lấy ID chân của người chơi.
     * @return ID chân dựa trên trạng thái và trang bị.
     */
    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1990;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][2];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][2];
                }
                return idOutfitFusion[3 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[6 + this.gender][2];
                }
                return idOutfitFusion[6 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[9 + this.gender][2];
                }
                return idOutfitFusion[9 + this.gender][2];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    /**
     * Lấy ID hào quang của người chơi.
     * @return ID hào quang hoặc -1 nếu không có.
     */
    public byte getAura() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5); // Gốc là 8
        if (!item.isNotNullItem()) {
            return 0;
        }
        if (item.template.id == 1430) {
            return 29;
        }
        if (item.template.id == 1431) {
            return 29;
        }
        if (item.template.id == 1432) {
            return 29;
        } else {
            return -1;
        }
    }

    /**
     * Lấy ID cờ của người chơi.
     * @return ID cờ hoặc -1 nếu không có.
     */
    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (this.inventory.itemsBody.size() > 10) {
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    /**
     * Lấy ID phương tiện di chuyển (tàu, thú cưỡi) của người chơi.
     * @return ID phương tiện hoặc -1 nếu không có.
     */
    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }
    }

    /**
     * Xử lý sát thương nhận vào từ người tấn công hoặc quái.
     * @param plAtt Người chơi tấn công.
     * @param damage Sát thương nhận vào.
     * @param piercing Có xuyên giáp hay không.
     * @param isMobAttack Có phải tấn công từ quái hay không.
     * @return Sát thương thực tế nhận vào.
     */
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            com.girlkun.services.PlayerService.gI().hoiPhuc(this, 0, damage * this.nPoint.voHieuChuong / 100);
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                if (this != null && this.zone != null && this.zone.map != null) {
                    if (this.zone.map.mapId == 112 && plAtt != null) {
                        plAtt.pointPvp++;
                    }
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Xử lý khi người chơi chết.
     * @param plAtt Người chơi gây ra cái chết.
     */
    protected void setDie(Player plAtt) {
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        this.effectSkill.removeSkillEffectWhenDie();
        nPoint.setHp(0);
        nPoint.setMp(0);
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        if (!this.isPet && !this.isNewPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
        BlackBallWar.gI().dropBlackBall(this);
    }

    /**
     * Cập nhật thông tin thành viên bang hội.
     */
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    /**
     * Kiểm tra người chơi có phải là admin hay không.
     * @return true nếu là admin, false nếu không.
     */
    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    /**
     * Đánh dấu người chơi vừa được hồi sinh.
     */
    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    /**
     * Chuẩn bị giải phóng tài nguyên của người chơi.
     */
    public void preparedToDispose() {
    }

    /**
     * Giải phóng tài nguyên của người chơi.
     */
    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    /**
     * Tính phần trăm vàng cược (thường hoặc VIP).
     * @param type Loại vàng (0: thường, 1: VIP).
     * @return Phần trăm vàng dưới dạng chuỗi.
     */
    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

    /**
     * Danh sách văn bản liên quan đến rương gỗ.
     */
    public List<String> textRuongGo = new ArrayList<>();
}