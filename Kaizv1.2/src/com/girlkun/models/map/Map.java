package com.girlkun.models.map;

import com.girlkun.consts.ConstMap;
import com.girlkun.models.Template;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.arriety.models.map.doanhtrai.DoanhTrai;
import com.arriety.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcFactory;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.services.func.TopService;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp đại diện cho bản đồ trong game. Quản lý zone, NPC, mob, trap, item và
 * boss trong bản đồ. Đồng thời điều khiển chu kỳ cập nhật thông qua Runnable.
 *
 * @author Lucifer
 */
public class Map implements Runnable {

    /**
     * Hằng số loại map trống
     */
    public static final byte T_EMPTY = 0;
    /**
     * Hằng số loại map top
     */
    public static final byte T_TOP = 2;
    /**
     * Kích thước mỗi ô tile (24px)
     */
    private static final int SIZE = 24;

    /**
     * ID bản đồ
     */
    public int mapId;
    /**
     * Tên bản đồ
     */
    public String mapName;
    /**
     * ID hành tinh chứa bản đồ
     */
    public byte planetId;
    /**
     * Tên hành tinh
     */
    public String planetName;
    /**
     * ID tile
     */
    public byte tileId;
    /**
     * ID background
     */
    public byte bgId;
    /**
     * Loại background
     */
    public byte bgType;
    /**
     * Loại bản đồ (offline, war, doanh trại...)
     */
    public byte type;
    /**
     * Mảng tile map 2D
     */
    private int[][] tileMap;
    /**
     * Danh sách tile có thể đứng
     */
    public int[] tileTop;
    /**
     * Chiều rộng bản đồ (px)
     */
    public int mapWidth;
    /**
     * Chiều cao bản đồ (px)
     */
    public int mapHeight;
    /**
     * Danh sách zone thuộc bản đồ
     */
    public List<Zone> zones;
    /**
     * Danh sách waypoint trên bản đồ
     */
    public List<WayPoint> wayPoints;
    /**
     * Danh sách NPC trong bản đồ
     */
    public List<Npc> npcs;

    /**
     * Khởi tạo một bản đồ mới.
     *
     * @param mapId ID bản đồ
     * @param mapName tên bản đồ
     * @param planetId ID hành tinh
     * @param tileId ID tile
     * @param bgId ID background
     * @param bgType loại background
     * @param type loại bản đồ
     * @param tileMap dữ liệu tile
     * @param tileTop tile đứng được
     * @param zones số zone
     * @param maxPlayer số người chơi tối đa trong zone
     * @param wayPoints danh sách waypoint
     */
    public Map(int mapId, String mapName, byte planetId,
            byte tileId, byte bgId, byte bgType, byte type, int[][] tileMap,
            int[] tileTop, int zones, int maxPlayer, List<WayPoint> wayPoints) {
        this.mapId = mapId;
        this.mapName = mapName;
        this.planetId = planetId;
        this.planetName = Service.getInstance().get_HanhTinh(planetId);
        this.tileId = tileId;
        this.bgId = bgId;
        this.bgType = bgType;
        this.type = type;
        this.tileMap = tileMap;
        this.tileTop = tileTop;
        this.zones = new ArrayList<>();
        this.wayPoints = wayPoints;
        try {
            this.mapHeight = tileMap.length * SIZE;
            this.mapWidth = tileMap[0].length * SIZE;
        } catch (Exception e) {
        }
        this.initZone(zones, maxPlayer);
        this.initItem();
        this.initTrapMap();
    }

    /**
     * Khởi tạo các zone cho bản đồ.
     *
     * @param nZone số zone
     * @param maxPlayer số lượng người chơi tối đa trong zone
     */
    private void initZone(int nZone, int maxPlayer) {
        switch (this.type) {
            case ConstMap.MAP_OFFLINE:
                nZone = 1;
                break;
            case ConstMap.MAP_BLACK_BALL_WAR:
                nZone = BlackBallWar.AVAILABLE;
                break;
            case ConstMap.MAP_MA_BU:
                nZone = MapMaBu.AVAILABLE;
                break;
            case ConstMap.MAP_DOANH_TRAI:
                nZone = DoanhTrai.AVAILABLE;
                break;
            case ConstMap.MAP_BAN_DO_KHO_BAU:
                nZone = BanDoKhoBau.MAX_AVAILABLE;
                break;
            case ConstMap.MAP_KHI_GAS:
                nZone = Gas.MAX_AVAILABLE;
                break;
        }

        for (int i = 0; i < nZone; i++) {
            Zone zone = new Zone(this, i, maxPlayer);
            this.zones.add(zone);
            switch (this.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    DoanhTraiService.gI().addMapDoanhTrai(i, zone);
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    BanDoKhoBau.addZone(i, zone);
                    break;
                case ConstMap.MAP_KHI_GAS:
                    Gas.addZone(i, zone);
                    break;
            }
        }
    }

    /**
     * Khởi tạo NPC trong bản đồ.
     *
     * @param npcId danh sách id NPC
     * @param npcX vị trí X của NPC
     * @param npcY vị trí Y của NPC
     */
    public void initNpc(byte[] npcId, short[] npcX, short[] npcY) {
        this.npcs = new ArrayList<>();
        for (int i = 0; i < npcId.length; i++) {
            this.npcs.add(NpcFactory.createNPC(this.mapId, 1, npcX[i], npcY[i], npcId[i]));
        }
    }

    /**
     * Chu kỳ cập nhật map chạy trong thread
     */
    @Override
    public void run() {
        while (true) {
            try {
                long startTime = System.currentTimeMillis();
                // Thực hiện cập nhật cho từng Zone
                for (Zone zone : this.zones) {
                    zone.update();
                }
                long elapsedTime = System.currentTimeMillis() - startTime;
                // Tính thời gian cần chờ để đảm bảo chu kỳ chạy là 1 giây
                long waitTime = Math.max(0, 1000 - elapsedTime);
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // Xử lý ngoại lệ khi thread bị gián đoạn
                // ...
                break; // Thoát khỏi vòng lặp nếu bị gián đoạn
            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý ngoại lệ (exception) khác
                // ...
            }
        }
    }

    /**
     * Khởi tạo mob cho bản đồ.
     *
     * @param mobTemp id mob
     * @param mobLevel level mob
     * @param mobHp máu mob
     * @param mobX tọa độ X
     * @param mobY tọa độ Y
     */
    public void initMob(byte[] mobTemp, byte[] mobLevel, int[] mobHp, short[] mobX, short[] mobY) {
        for (int i = 0; i < mobTemp.length; i++) {
            int mobTempId = mobTemp[i];
            Template.MobTemplate temp = Manager.getMobTemplateByTemp(mobTempId);
            if (temp != null) {
                Mob mob = new Mob();
                mob.id = i;
                mob.tempId = mobTemp[i];
                mob.level = mobLevel[i];
                mob.point.setHpFull(mobHp[i]);
                mob.location.x = mobX[i];
                mob.location.y = mobY[i];
                mob.point.sethp(mob.point.getHpFull());
                mob.pDame = temp.percentDame;
                mob.pTiemNang = temp.percentTiemNang;
                mob.setTiemNang();
                for (Zone zone : this.zones) {
                    Mob mobZone = new Mob(mob);
                    mobZone.zone = zone;
                    zone.mobs.add(mobZone);
                }
            }
        }
    }

    /**
     * Khởi tạo mob từ danh sách có sẵn.
     *
     * @param mobs danh sách mob
     */
    public void initMob(List<Mob> mobs) {
        for (Zone zone : zones) {
            for (Mob m : mobs) {
                Mob mob = new Mob(m);
                mob.zone = zone;
                zone.mobs.add(mob);
            }
        }
    }

    /**
     * Khởi tạo trap trong bản đồ
     */
    private void initTrapMap() {
        for (Zone zone : zones) {
            TrapMap trap = null;
            switch (this.mapId) {
                case 135:
                    trap = new TrapMap();
                    trap.x = 260;
                    trap.y = 960;
                    trap.w = 740;
                    trap.h = 72;
                    trap.effectId = 49;
                    zone.trapMaps.add(trap);
                    break;
            }
        }
    }

    /**
     * Khởi tạo item có sẵn trên bản đồ
     */
    private void initItem() {
        for (Zone zone : zones) {
            ItemMap itemMap = null;
            switch (this.mapId) {
                case 21:
                    itemMap = new ItemMap(zone, 74, 1, 633, 315, -1);
                    break;
                case 22:
                    itemMap = new ItemMap(zone, 74, 1, 56, 315, -1);
                    break;
                case 23:
                    itemMap = new ItemMap(zone, 74, 1, 633, 320, -1);
                    break;
                case 42:
                    itemMap = new ItemMap(zone, 78, 1, 70, 288, -1);
                    break;
                case 43:
                    itemMap = new ItemMap(zone, 78, 1, 70, 264, -1);
                    break;
                case 44:
                    itemMap = new ItemMap(zone, 78, 1, 70, 288, -1);
                    break;
                case 85: //1 sao đen
                    itemMap = new ItemMap(zone, 372, 1, 0, 0, -1);
                    break;
                case 86: //2 sao đen
                    itemMap = new ItemMap(zone, 373, 1, 0, 0, -1);
                    break;
                case 87: //3 sao đen
                    itemMap = new ItemMap(zone, 374, 1, 0, 0, -1);
                    break;
                case 88: //4 sao đen
                    itemMap = new ItemMap(zone, 375, 1, 0, 0, -1);
                    break;
                case 89: //5 sao đen
                    itemMap = new ItemMap(zone, 376, 1, 0, 0, -1);
                    break;
                case 90: //6 sao đen
                    itemMap = new ItemMap(zone, 377, 1, 0, 0, -1);
                    break;
                case 91: //7 sao đen
                    itemMap = new ItemMap(zone, 378, 1, 0, 0, -1);
                    break;
            }
        }
    }

    /**
     * Khởi tạo boss trong bản đồ
     */
    public void initBoss() {
        for (Zone zone : zones) {
            short bossId = -1;
            switch (this.mapId) {
                case 114:
                    bossId = BossID.DRABURA;
                    break;
                case 115:
                    bossId = BossID.DRABURA_2;
                    break;
                case 117:
                    bossId = BossID.BUI_BUI;
                    break;
                case 118:
                    bossId = BossID.BUI_BUI_2;
                    break;
                case 119:
                    bossId = BossID.YA_CON;
                    break;
                case 120:
                    bossId = BossID.MABU_12H;
            }
            if (bossId != -1) {
                Boss boss = BossManager.gI().createBoss(bossId);
                boss.zoneFinal = zone;
                boss.joinMapByZone(zone);
            }
        }
    }

    /**
     * Lấy ID map tiếp theo trong chuỗi Mabu.
     *
     * @param mapId id hiện tại
     * @return id map kế tiếp
     */
    public short mapIdNextMabu(short mapId) {
        switch (mapId) {
            case 114:
                return 115;
            case 115:
                return 117;
            case 117:
                return 118;
            case 118:
                return 119;
            case 119:
                return 120;
            default:
                return -1;
        }
    }

    /**
     * Tìm NPC trong map gần người chơi.
     *
     * @param player người chơi
     * @param tempId id npc
     * @return npc hoặc null nếu không tìm thấy
     */
    public Npc getNpc(Player player, int tempId) {
        for (Npc npc : npcs) {
            if (npc.tempId == tempId && Util.getDistance(player, npc) <= 60) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Tính vị trí Y trên tile top gần nhất từ tọa độ (x, y).
     *
     * @param x tọa độ X
     * @param y tọa độ Y
     * @return giá trị Y nằm trên tile top
     */
    public int yPhysicInTop(int x, int y) {
        try {
            int rX = (int) x / SIZE;
            int rY = 0;
            if (isTileTop(tileMap[y / SIZE][rX])) {
                return y;
            }
            for (int i = y / SIZE; i < tileMap.length; i++) {
                if (isTileTop(tileMap[i][rX])) {
                    rY = i * SIZE;
                    break;
                }
            }
            return rY;
        } catch (Exception e) {
            return y;
        }
    }

    /**
     * Kiểm tra tile có phải là tile top không.
     *
     * @param tileMap id tile
     * @return true nếu là tile top
     */
    private boolean isTileTop(int tileMap) {
        for (int i = 0; i < tileTop.length; i++) {
            if (tileTop[i] == tileMap) {
                return true;
            }
        }
        return false;
    }
}
