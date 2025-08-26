package com.girlkun.models.map.BDKB;

import com.girlkun.models.boss.list_boss.BDKB.TrungUyXanhLo;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp BanDoKhoBau đại diện cho bản đồ kho báu trong trò chơi, quản lý các khu vực, bang hội và hoạt động liên quan.
 * @author Lucifer
 */
public class BanDoKhoBau {

    /**
     * Sức mạnh tối thiểu để tham gia bản đồ kho báu.
     */
    public static final long POWER_CAN_GO_TO_DBKB = 1500000000L;

    /**
     * Danh sách các bản đồ kho báu được tạo sẵn.
     */
    public static final List<BanDoKhoBau> BAN_DO_KHO_BAUS;

    /**
     * Số lượng bản đồ kho báu tối đa có thể tạo.
     */
    public static final int MAX_AVAILABLE = 50;

    /**
     * Thời gian tồn tại của bản đồ kho báu (mili giây).
     */
    public static final int TIME_BAN_DO_KHO_BAU = 1800000;

    /**
     * Người chơi liên quan đến bản đồ kho báu.
     */
    private Player player;

    /**
     * Khởi tạo danh sách bản đồ kho báu với số lượng tối đa.
     */
    static {
        BAN_DO_KHO_BAUS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            BAN_DO_KHO_BAUS.add(new BanDoKhoBau(i));
        }
    }

    /**
     * Boss Trung Úy Xanh Lơ trong bản đồ kho báu.
     */
    TrungUyXanhLo boss;

    /**
     * ID của bản đồ kho báu.
     */
    public int id;

    /**
     * Cấp độ của bản đồ kho báu.
     */
    public byte level;

    /**
     * Danh sách các khu vực trong bản đồ kho báu.
     */
    public final List<Zone> zones;

    /**
     * Bang hội sở hữu bản đồ kho báu.
     */
    public Clan clan;

    /**
     * Trạng thái mở của bản đồ kho báu.
     */
    public boolean isOpened;

    /**
     * Thời điểm lần cuối mở bản đồ kho báu.
     */
    private long lastTimeOpen;

    /**
     * Khởi tạo một bản đồ kho báu với ID cụ thể.
     * @param id ID của bản đồ kho báu.
     */
    public BanDoKhoBau(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    /**
     * Cập nhật trạng thái của bản đồ kho báu, kiểm tra thời gian tồn tại.
     */
    public void update() {
        if (this.isOpened) {
            if (Util.canDoWithTime(lastTimeOpen, TIME_BAN_DO_KHO_BAU)) {
                this.finish();
            }
        }
    }

    /**
     * Mở bản đồ kho báu cho một bang hội với cấp độ cụ thể.
     * @param plOpen Người chơi mở bản đồ.
     * @param clan Bang hội sở hữu bản đồ.
     * @param level Cấp độ của bản đồ.
     */
    public void openBanDoKhoBau(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenBanDoKhoBau = this.lastTimeOpen;
        this.clan.playerOpenBanDoKhoBau = plOpen;
        this.clan.BanDoKhoBau = this;

        resetBanDo();
        ChangeMapService.gI().goToDBKB(plOpen);
        sendTextBanDoKhoBau();
    }

    /**
     * Đặt lại trạng thái của bản đồ kho báu, bao gồm quái vật và bẫy.
     */
    private void resetBanDo() {
        for (Zone zone : zones) {
            for (TrapMap trap : zone.trapMaps) {
                trap.dame = this.level * 10000;
            }
        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                m.initMobBanDoKhoBau(m, this.level);
                m.hoiSinhMob(m);
                m.hoiSinh();
                m.sendMobHoiSinh();
            }
        }
    }

    /**
     * Kết thúc bản đồ kho báu, đưa tất cả người chơi ra ngoài.
     */
    public void finish() {
        List<Player> plOutBD = new ArrayList<>();
        for (int i = 0; i < zones.size(); i++) {
            Zone zone = zones.get(i);
            List<Player> players = zone.getPlayers();
            for (int j = 0; j < players.size(); j++) {
                Player pl = players.get(j);
                plOutBD.add(pl);
                kickOutOfBDKB(pl);
            }
        }

        for (Player pl : plOutBD) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 5, -1, 64);
        }

        this.clan.BanDoKhoBau = null;
        this.clan = null;
        this.isOpened = false;
    }

    /**
     * Đưa người chơi ra khỏi bản đồ kho báu.
     * @param player Người chơi cần đưa ra ngoài.
     */
    private void kickOutOfBDKB(Player player) {
        if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Hang Kho Báu Đã Sập Bạn Đang Được Đưa Ra Ngoài");
            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1038);
            this.clan.BanDoKhoBau = null;
        }
    }

    /**
     * Lấy khu vực trong bản đồ kho báu theo ID bản đồ.
     * @param mapId ID của bản đồ.
     * @return Khu vực tương ứng hoặc null nếu không tìm thấy.
     */
    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Thêm một khu vực vào bản đồ kho báu.
     * @param idBanDo ID của bản đồ kho báu.
     * @param zone Khu vực cần thêm.
     */
    public static void addZone(int idBanDo, Zone zone) {
        BAN_DO_KHO_BAUS.get(idBanDo).zones.add(zone);
    }

    /**
     * Gửi thông báo văn bản về bản đồ kho báu cho các thành viên bang hội.
     */
    private void sendTextBanDoKhoBau() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }
    }
}