package com.girlkun.models.map.gas;
//import com.girlkun.models.boss.bdkb.TrungUyXanhLo;

import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.list_boss.gas.DrLyChee;
import com.girlkun.models.boss.list_boss.gas.HaChiJack;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Gas quản lý bản đồ Khí Gas trong trò chơi, bao gồm các khu vực, bang hội và hoạt động liên quan.
 * @author Lucifer
 */
public class Gas {

    /**
     * Sức mạnh tối thiểu để tham gia bản đồ Khí Gas.
     */
    public static final long POWER_CAN_GO_TO_GAS = 40000000000L;

    /**
     * Danh sách các bản đồ Khí Gas được tạo sẵn.
     */
    public static final List<Gas> KHI_GAS;

    /**
     * Số lượng bản đồ Khí Gas tối đa có thể tạo.
     */
    public static final int MAX_AVAILABLE = 500;

    /**
     * Thời gian tồn tại của bản đồ Khí Gas (mili giây).
     */
    public static final int TIME_KHI_GAS = 1800000;

    /**
     * Người chơi liên quan đến bản đồ Khí Gas.
     */
    private Player player;

    /**
     * Khởi tạo danh sách bản đồ Khí Gas với số lượng tối đa.
     */
    static {
        KHI_GAS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            KHI_GAS.add(new Gas(i));
        }
    }

    /**
     * ID của bản đồ Khí Gas.
     */
    public int id;

    /**
     * Cấp độ của bản đồ Khí Gas.
     */
    public int level;

    /**
     * Danh sách các khu vực trong bản đồ Khí Gas.
     */
    public final List<Zone> zones;

    /**
     * Bang hội sở hữu bản đồ Khí Gas.
     */
    public Clan clan;

    /**
     * Trạng thái mở của bản đồ Khí Gas.
     */
    public boolean isOpened;

    /**
     * Thời điểm lần cuối mở bản đồ Khí Gas.
     */
    private long lastTimeOpen;

    /**
     * Trạng thái khởi tạo boss trong bản đồ Khí Gas.
     */
    public boolean isInitBoss;

    /**
     * Khởi tạo một bản đồ Khí Gas với ID cụ thể.
     * @param id ID của bản đồ Khí Gas.
     */
    public Gas(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    /**
     * Cập nhật trạng thái của bản đồ Khí Gas, kiểm tra thời gian tồn tại.
     */
    public void update() {
        if (this.isOpened) {
            if (Util.canDoWithTime(lastTimeOpen, TIME_KHI_GAS)) {
                this.finish();
            }
        }
    }

    /**
     * Mở bản đồ Khí Gas cho một bang hội với cấp độ cụ thể.
     * @param plOpen Người chơi mở bản đồ.
     * @param clan Bang hội sở hữu bản đồ.
     * @param level Cấp độ của bản đồ.
     */
    public void openGas(Player plOpen, Clan clan, int level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenKhiGas = System.currentTimeMillis();
        this.clan.playerOpenKhiGas = plOpen;
        this.clan.khiGas = this;
        resetGas();
        ChangeMapService.gI().goToGas(plOpen);
        sendTextGas();
    }

    /**
     * Khởi tạo boss Dr. Lychee trong bản đồ Khí Gas.
     * @param pl Người chơi liên quan đến việc khởi tạo boss.
     */
    public void InitBoss(Player pl) {
        try {
            new DrLyChee(pl, level, (int) 1, (int) 1, BossID.DR_LYCHEE);
            isInitBoss = true;
        } catch (Exception e) {
            System.err.print("\nError at 171\n");
            e.printStackTrace();
        }
    }

    /**
     * Đặt lại trạng thái của bản đồ Khí Gas, khởi tạo lại quái vật.
     */
    private void resetGas() {
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                Mob.initMopbKhiGas(m, this.level);
                Mob.hoiSinhMob(m);
            }
        }
    }

    /**
     * Kết thúc bản đồ Khí Gas, đưa tất cả người chơi ra ngoài.
     */
    public void finish() {
        List<Player> plOutDT = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutDT.add(pl);
            }
        }
        for (Player pl : plOutDT) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 0, -1, 384);
        }
        this.clan.khiGas = null;
        this.clan = null;
        this.isOpened = false;
    }

    /**
     * Lấy khu vực trong bản đồ Khí Gas theo ID bản đồ.
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
     * Thêm một khu vực vào bản đồ Khí Gas.
     * @param idGas ID của bản đồ Khí Gas.
     * @param zone Khu vực cần thêm.
     */
    public static void addZone(int idGas, Zone zone) {
        KHI_GAS.get(idGas).zones.add(zone);
    }

    /**
     * Gửi thông báo văn bản về bản đồ Khí Gas cho các thành viên bang hội.
     */
    public void sendTextGas() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextGas(pl);
        }
    }
}