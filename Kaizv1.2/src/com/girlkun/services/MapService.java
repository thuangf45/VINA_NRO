package com.girlkun.services;

import com.girlkun.consts.ConstMap;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.WayPoint;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.arriety.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Lớp MapService quản lý các chức năng liên quan đến bản đồ trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để xử lý bản đồ, khu vực, và di chuyển của người chơi.
 * 
 * @author Lucifer
 */
public class MapService {

    /**
     * Thể hiện duy nhất của lớp MapService (singleton pattern).
     */
    private static MapService i;

    /**
     * Lấy thể hiện duy nhất của lớp MapService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp MapService.
     */
    public static MapService gI() {
        if (i == null) {
            i = new MapService();
        }
        return i;
    }

    /**
     * Tìm và trả về WayPoint mà người chơi đang đứng trong khu vực.
     * 
     * @param player Người chơi cần kiểm tra vị trí.
     * @return WayPoint mà người chơi đang đứng, hoặc null nếu không tìm thấy.
     */
    public WayPoint getWaypointPlayerIn(Player player) {
        for (WayPoint wp : player.zone.map.wayPoints) {
            if (player.location.x >= wp.minX && player.location.x <= wp.maxX && player.location.y >= wp.minY && player.location.y <= wp.maxY) {
                return wp;
            }
        }
        return null;
    }

    /**
     * Đọc thông tin tile index theo loại tile từ file dữ liệu.
     * 
     * @param tileTypeFocus Loại tile cần tập trung (top, bot, left, right...).
     * @return Mảng hai chiều chứa thông tin tile index theo tile type.
     */
    public int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return tileIndexTileType;
    }

    /**
     * Đọc dữ liệu tile map từ file để sử dụng cho việc vẽ bản đồ.
     * 
     * @param mapId ID của bản đồ cần đọc.
     * @return Mảng hai chiều chứa thông tin tile map.
     */
    public int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_map_data/" + mapId));
            dis.readByte();
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
        }
        return tileMap;
    }

    /**
     * Lấy khu vực mà người chơi có thể tham gia dựa trên ID bản đồ và ID khu vực.
     * 
     * @param player Người chơi cần kiểm tra.
     * @param mapId ID của bản đồ.
     * @param zoneId ID của khu vực (-1 để chọn ngẫu nhiên).
     * @return Khu vực (Zone) mà người chơi có thể tham gia, hoặc null nếu không hợp lệ.
     */
    public Zone getMapCanJoin(Player player, int mapId, int zoneId) {
        if (isMapOffline(mapId)) {
            return getMapById(mapId).zones.get(0);
        }   
        if (this.isMapKhiGas(mapId)) {
            if (player.clan == null || player.clan.khiGas == null) {
                return null;
            }
            if (this.isMapKhiGas(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
            }
            return player.clan.khiGas.getMapById(mapId);
        }
        if (this.isMapDoanhTrai(mapId)) {
            if (player.clan == null || player.clan.doanhTrai == null) {
                return null;
            }
            if (this.isMapDoanhTrai(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }
            if (this.isMapDoanhTrai(mapId)) {
                if (player.clan == null || player.clan.doanhTrai == null) {
                    return null;
                }
                if (this.isMapDoanhTrai(player.zone.map.mapId)) {
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            return null;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            return null;
                        }
                    }
                }
                return player.clan.doanhTrai.getMapById(mapId);
            }
        }
        if (this.isMapBanDoKhoBau(mapId)) {
            if (player.clan == null || player.clan.BanDoKhoBau == null) {
                return null;
            }
            if (this.isMapBanDoKhoBau(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }
            return player.clan.BanDoKhoBau.getMapById(mapId);
        }
        if (zoneId == -1) {
            return getZone(mapId);
        } else {
            return getZoneByMapIDAndZoneID(mapId, zoneId);
        }
    }

    /**
     * Lấy khu vực ngẫu nhiên trong bản đồ dựa trên ID bản đồ.
     * 
     * @param mapId ID của bản đồ.
     * @return Khu vực (Zone) ngẫu nhiên trong bản đồ, hoặc null nếu không tìm thấy.
     */
    public Zone getZone(int mapId) {
        Map map = getMapById(mapId);
        if (map == null) {
            return null;
        }
        int z = 0;
        while (map.zones.get(z).getNumOfPlayers() >= map.zones.get(z).maxPlayer) {
            z++;
        }
        return map.zones.get(z);
    }

    /**
     * Lấy khu vực cụ thể dựa trên ID bản đồ và ID khu vực.
     * 
     * @param mapId ID của bản đồ.
     * @param zoneId ID của khu vực.
     * @return Khu vực (Zone) tương ứng, hoặc null nếu không tìm thấy.
     */
    private Zone getZoneByMapIDAndZoneID(int mapId, int zoneId) {
        Zone zoneJoin = null;
        try {
            Map map = getMapById(mapId);
            if (map != null) {
                zoneJoin = map.zones.get(zoneId);
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return zoneJoin;
    }

    /**
     * Lấy bản đồ dựa trên ID bản đồ.
     * 
     * @param mapId ID của bản đồ.
     * @return Bản đồ (Map) tương ứng, hoặc null nếu không tìm thấy.
     */
    public Map getMapById(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map;
            }
        }
        return null;
    }

    /**
     * Lấy một bản đồ ngẫu nhiên trong khoảng ID từ 27 đến 29 cho Calich.
     * 
     * @return Bản đồ (Map) ngẫu nhiên.
     */
    public Map getMapForCalich() {
        int mapId = Util.nextInt(27, 29);
        return MapService.gI().getMapById(mapId);
    }

    /**
     * Lấy một khu vực ngẫu nhiên trong bản đồ dựa trên ID bản đồ.
     * 
     * @param mapId ID của bản đồ.
     * @return Khu vực (Zone) ngẫu nhiên, hoặc null nếu không tìm thấy.
     */
    public Zone getMapWithRandZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        Zone zone = null;
        try {
            if (map != null) {
                zone = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
            }
        } catch (Exception e) {
        }
        return zone;
    }

    /**
     * Lấy tên hành tinh dựa trên ID hành tinh.
     * 
     * @param planetId ID của hành tinh.
     * @return Tên hành tinh, hoặc chuỗi rỗng nếu không tìm thấy.
     */
    public String getPlanetName(byte planetId) {
        switch (planetId) {
            case 0:
                return "Trái đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    /**
     * Lấy danh sách các khu vực mà người chơi có thể di chuyển bằng capsule.
     * 
     * @param pl Người chơi cần lấy danh sách capsule.
     * @return Danh sách các khu vực (Zone) có thể di chuyển.
     */
    public List<Zone> getMapCapsule(Player pl) {
        List<Zone> list = new ArrayList<>();
        if (pl.mapBeforeCapsule != null
                && pl.mapBeforeCapsule.map.mapId != 21
                && pl.mapBeforeCapsule.map.mapId != 22
                && pl.mapBeforeCapsule.map.mapId != 23
                && !isMapTuongLai(pl.mapBeforeCapsule.map.mapId)) {
            addListMapCapsule(pl, list, pl.mapBeforeCapsule);
        }
        addListMapCapsule(pl, list, getMapCanJoin(pl, 21 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 47, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 154, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 0, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 7, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 14, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 84, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 5, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 20, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 13, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 24 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 27, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 19, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 79, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 252, 0));
        return list;
    }

    /**
     * Lấy danh sách các khu vực thuộc bản đồ Black Ball War.
     * 
     * @return Danh sách các khu vực (Zone) của Black Ball War.
     */
    public List<Zone> getMapBlackBall() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(85 + i).zones.get(0));
        }
        return list;
    }

    /**
     * Lấy danh sách các khu vực thuộc bản đồ Ma Bư.
     * 
     * @return Danh sách các khu vực (Zone) của Ma Bư.
     */
    public List<Zone> getMapMaBu() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(114 + i).zones.get(0));
        }
        return list;
    }

    /**
     * Thêm khu vực vào danh sách capsule nếu khu vực hợp lệ và không trùng lặp.
     * 
     * @param pl Người chơi.
     * @param list Danh sách các khu vực (Zone).
     * @param zone Khu vực cần thêm.
     */
    private void addListMapCapsule(Player pl, List<Zone> list, Zone zone) {
        for (Zone z : list) {
            if (z != null && zone != null && z.map.mapId == zone.map.mapId) {
                return;
            }
        }
        if (zone != null && pl.zone.map.mapId != zone.map.mapId) {
            list.add(zone);
        }
    }

    /**
     * Gửi thông tin di chuyển của người chơi tới tất cả người chơi trong bản đồ.
     * 
     * @param player Người chơi thực hiện di chuyển.
     */
    public void sendPlayerMove(Player player) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ offline không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ offline, ngược lại trả về false.
     */
    public boolean isMapOffline(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map.type == ConstMap.MAP_OFFLINE;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Black Ball War không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Black Ball War, ngược lại trả về false.
     */
    public boolean isMapBlackBallWar(int mapId) {
        return mapId >= 85 && mapId <= 91;
    }
    
    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Khí Gas không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Khí Gas, ngược lại trả về false.
     */
    public boolean isMapKhiGas(int mapId) {
        return mapId == 149 || mapId == 148 || mapId == 147 || mapId == 151 || mapId == 152;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Ma Bư không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Ma Bư, ngược lại trả về false.
     */
    public boolean isMapMaBu(int mapId) {
        return mapId >= 114 && mapId <= 120;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ PVP không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ PVP, ngược lại trả về false.
     */
    public boolean isMapPVP(int mapId) {
        return mapId == 112;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Cold không.
     * 
     * @param map Bản đồ cần kiểm tra.
     * @return True nếu là bản đồ Cold, ngược lại trả về false.
     */
    public boolean isMapCold(Map map) {
        int mapId = map.mapId;
        return mapId >= 105 && mapId <= 110;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Nhà không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Nhà, ngược lại trả về false.
     */
    public boolean isMapNha(int mapId) {
        return mapId >= 21 && mapId <= 23;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Doanh Trại không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Doanh Trại, ngược lại trả về false.
     */
    public boolean isMapDoanhTrai(int mapId) {
        return mapId >= 53 && mapId <= 62;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Hủy Diệt không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Hủy Diệt, ngược lại trả về false.
     */
    public boolean isMapHuyDiet(int mapId) {
        return mapId >= 146 && mapId <= 148;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Bản Đồ Kho Báu không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Bản Đồ Kho Báu, ngược lại trả về false.
     */
    public boolean isMapBanDoKhoBau(int mapId) {
        return mapId >= 135 && mapId <= 138;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Ngục Tù không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Ngục Tù, ngược lại trả về false.
     */
    public boolean isMapNgucTu(int mapId) {
        return mapId == 155;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Ngu Hành Sơn không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Ngu Hành Sơn, ngược lại trả về false.
     */
    public boolean isnguhs(int mapId) {
        return mapId >= 122 && mapId <= 124;
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ Tương Lai không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu là bản đồ Tương Lai, ngược lại trả về false.
     */
    public boolean isMapTuongLai(int mapId) {
        return (mapId >= 92 && mapId <= 94)
                || (mapId >= 96 && mapId <= 100)
                || mapId == 102 || mapId == 103;
    }

    /**
     * Di chuyển người chơi đến một khu vực mới.
     * 
     * @param player Người chơi cần di chuyển.
     * @param zoneJoin Khu vực đích đến.
     */
    public void goToMap(Player player, Zone zoneJoin) {
        Zone oldZone = player.zone;
        if (oldZone != null) {
            ChangeMapService.gI().exitMap(player);
            if (player.mobMe != null) {
                player.mobMe.goToMap(zoneJoin);
            }
        }
        player.zone = zoneJoin;
        player.zone.addPlayer(player);
    }

    /**
     * Kiểm tra xem bản đồ có thuộc tập hợp kích hoạt không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu bản đồ thuộc tập hợp kích hoạt, ngược lại trả về false.
     */
    public boolean isMapSetKichHoat(int mapId) {
        return (mapId >= 1 && mapId <= 3)
                || (mapId == 8 || mapId == 9 || mapId == 11)
                || (mapId >= 15 && mapId <= 17);
    }

    /**
     * Kiểm tra xem bản đồ có phải là bản đồ không có siêu quái không.
     * 
     * @param mapId ID của bản đồ.
     * @return True nếu bản đồ không có siêu quái, ngược lại trả về false.
     */
    public boolean isMapKhongCoSieuQuai(int mapId) {
        return !isMapSetKichHoat(mapId)
                && mapId != 4 && mapId != 27 && mapId != 28
                && mapId != 12 && mapId != 31 && mapId != 32
                && mapId != 18 && mapId != 35 && mapId != 36;
    }
}