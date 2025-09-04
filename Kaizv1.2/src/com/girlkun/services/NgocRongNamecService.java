package com.girlkun.services;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lớp NgocRongNamecService quản lý các chức năng liên quan đến Ngọc Rồng Namec trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để khởi tạo, quản lý, và xử lý các viên Ngọc Rồng Namec.
 * 
 * @author Lucifer
 */
public class NgocRongNamecService {

    /**
     * Thể hiện duy nhất của lớp NgocRongNamecService (singleton pattern).
     */
    private static NgocRongNamecService instance;

    /**
     * Lấy thể hiện duy nhất của lớp NgocRongNamecService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp NgocRongNamecService.
     */
    public static NgocRongNamecService gI() {
        if (instance == null) {
            instance = new NgocRongNamecService();
        }
        return instance;
    }

    /**
     * Mảng lưu trữ ID bản đồ của các viên Ngọc Rồng Namec.
     */
    public int mapNrNamec[] = {-1, -1, -1, -1, -1, -1, -1};

    /**
     * Mảng lưu trữ tên bản đồ của các viên Ngọc Rồng Namec.
     */
    public String nameNrNamec[] = {"", "", "", "", "", "", ""};

    /**
     * Mảng lưu trữ ID khu vực của các viên Ngọc Rồng Namec.
     */
    public byte zoneNrNamec[] = {-1, -1, -1, -1, -1, -1, -1};

    /**
     * Mảng lưu trữ tên người chơi sở hữu các viên Ngọc Rồng Namec.
     */
    public String pNrNamec[] = {"", "", "", "", "", "", ""};

    /**
     * Mảng lưu trữ ID người chơi sở hữu các viên Ngọc Rồng Namec.
     */
    public int idpNrNamec[] = {-1, -1, -1, -1, -1, -1, -1};

    /**
     * Thời gian liên quan đến Ngọc Rồng Namec.
     */
    public long timeNrNamec = 0;

    /**
     * Biến kiểm tra xem đây có phải lần đầu khởi tạo Ngọc Rồng Namec không.
     */
    public boolean firstNrNamec = true;

    /**
     * Thời gian mở Ngọc Rồng Namec.
     */
    public long tOpenNrNamec = 0;

    /**
     * Thời gian cuối cùng khởi tạo lại Ngọc Rồng Namec.
     */
    public long lastTimeReinit;

    /**
     * Biến kiểm tra xem có đang trong trạng thái khởi tạo lại không.
     */
    public boolean isReinit;

    /**
     * Khởi tạo Ngọc Rồng Namec hoặc Hóa Thạch Ngọc Rồng trên các bản đồ ngẫu nhiên.
     * 
     * @param type Loại khởi tạo (0: Ngọc Rồng, 1: Hóa Thạch Ngọc Rồng).
     */
    public void initNgocRongNamec(byte type) {
        ArrayList<Integer> listMap = new ArrayList<>();
        listMap.add(8);
        listMap.add(9);
        listMap.add(10);
        listMap.add(11);
        listMap.add(12);
        listMap.add(13);
        listMap.add(31);
        listMap.add(32);
        listMap.add(33);
        listMap.add(34);
        for (byte i = 0; i < (byte) 7; i++) {
            int index = Util.nextInt(0, listMap.size() - 1);
            int idZone = Util.nextInt(0, Manager.MAPS.get(listMap.get(index)).zones.size() - 1);
            mapNrNamec[i] = listMap.get(index);
            nameNrNamec[i] = Manager.MAPS.get(listMap.get(index)).mapName;
            zoneNrNamec[i] = (byte) idZone;
            Zone zone = Manager.MAPS.get(listMap.get(index)).zones.get(idZone);
            int x = 0;
            int y = 0;
            if (null != listMap.get(index)) switch (listMap.get(index)) {
                case 8:
                    x = (short) 553;
                    y = (short) 288;
                    break;
                case 9:
                    x = (short) 634;
                    y = (short) 432;
                    break;
                case 10:
                    x = (short) 711;
                    y = (short) 288;
                    break;
                case 11:
                    x = (short) 1078;
                    y = (short) 336;
                    break;
                case 12:
                    x = (short) 1300;
                    y = (short) 288;
                    break;
                case 13:
                    x = (short) 323;
                    y = (short) 432;
                    break;
                case 31:
                    x = (short) 606;
                    y = (short) 312;
                    break;
                case 32:
                    x = (short) 650;
                    y = (short) 360;
                    break;
                case 33:
                    x = (short) 1325;
                    y = (short) 360;
                    break;
                case 34:
                    x = (short) 643;
                    y = (short) 432;
                    break;
                case 7:
                    x = (short) 643;
                    y = (short) 432;
                    break;
                default:
                    break;
            }
            if (type == (byte) 0) {
                ItemMap itemMap = new ItemMap(zone, i + 353, 1, x, y, -1);
                Service.gI().dropItemMap(zone, itemMap);
                System.out.println(itemMap.itemTemplate.name + "[" + zone.map.mapId + "-" + zone.zoneId + "]");
            } else {
                ItemMap itemMap = new ItemMap(zone, 362, 1, x, y, -1);
                Service.gI().dropItemMap(zone, itemMap);
            }
            listMap.remove(index);
        }
    }

    /**
     * Xóa các viên Ngọc Rồng Namec khỏi bản đồ.
     */
    public void removeStoneNrNamec() {
        for (byte i = 0; i < (byte) 7; i++) {
            Zone zone = Manager.MAPS.get(mapNrNamec[i]).zones.get(zoneNrNamec[i]);
            int idItem = (int) (i + 353);
            for (byte j = 0; j < zone.items.size(); j++) {
                if (zone.items.get(j).itemTemplate.id == idItem) {
                    ItemMapService.gI().removeItemMap(zone.items.remove(j));
                }
            }
        }
    }

    /**
     * Hoàn tất quá trình thu thập Ngọc Rồng Namec, xóa trạng thái sở hữu và cập nhật trạng thái người chơi.
     */
    public void doneDragonNamec() {
        for (int i = 0; i < 7; i++) {
            Player p = Client.gI().getPlayer(idpNrNamec[i]);
            if (p != null) {
                p.idNRNM = -1;
                pNrNamec[i] = "";
                idpNrNamec[i] = -1;
                Service.gI().sendFlagBag(p);
                PlayerService.gI().changeAndSendTypePK(p, ConstPlayer.NON_PK);
            }
        }
    }

    /**
     * Đặt lại thời gian khởi tạo lại Ngọc Rồng Namec.
     * 
     * @param time Thời gian chờ trước khi khởi tạo lại (tính bằng mili giây).
     */
    public void reInitNrNamec(long time) {
        lastTimeReinit = System.currentTimeMillis() + time;
        isReinit = true;
    }

    /**
     * Kiểm tra xem tất cả các viên Ngọc Rồng Namec có nằm trên cùng một bản đồ không.
     * 
     * @return True nếu tất cả Ngọc Rồng Namec nằm trên bản đồ có ID 7, ngược lại trả về false.
     */
    public boolean isSameMapNrNamec() {
        return (mapNrNamec[0] == 7) && (mapNrNamec[1] == 7) && (mapNrNamec[2] == 7) && (mapNrNamec[3] == 7) && (mapNrNamec[4] == 7) && (mapNrNamec[5] == 7) && (mapNrNamec[6] == 7);
    }

    /**
     * Kiểm tra xem tất cả các viên Ngọc Rồng Namec có nằm trong cùng một khu vực không.
     * 
     * @return True nếu tất cả Ngọc Rồng Namec nằm trong cùng một khu vực, ngược lại trả về false.
     */
    public boolean isSameZoneNrNamec() {
        return (zoneNrNamec[0] == zoneNrNamec[1]) && (zoneNrNamec[2] == zoneNrNamec[0]) && (zoneNrNamec[3] == zoneNrNamec[0]) && (zoneNrNamec[4] == zoneNrNamec[0]) && (zoneNrNamec[5] == zoneNrNamec[0]) && (zoneNrNamec[6] == zoneNrNamec[0]);
    }

    /**
     * Kiểm tra xem người chơi có thể triệu hồi Rồng Namec không.
     * Yêu cầu tất cả Ngọc Rồng phải ở cùng bản đồ, cùng khu vực và do các thành viên trong cùng bang sở hữu.
     * 
     * @param p Người chơi cần kiểm tra.
     * @return True nếu người chơi có thể triệu hồi Rồng Namec, ngược lại trả về false.
     */
    public boolean canCallDragonNamec(Player p) {
        byte count = (byte) 0;
        if (isSameMapNrNamec() && isSameZoneNrNamec()) {
            if (p.clan != null) {
                for (int i = 0; i < idpNrNamec.length; i++) {
                    for (int j = 0; j < p.clan.members.size(); j++) {
                        if (idpNrNamec[i] == p.clan.members.get(j).id) {
                            count++;
                        }
                    }
                }
                if (count == (byte) 7) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Dịch chuyển người chơi đến vị trí của viên Ngọc Rồng Namec được chỉ định.
     * 
     * @param p Người chơi cần dịch chuyển.
     */
    public void teleportToNrNamec(Player p) {
        int idMAP = mapNrNamec[p.idGo];
        int idZone = zoneNrNamec[p.idGo];
        Zone z = Manager.MAPS.get(idMAP).zones.get(idZone);
        if (z != null && !z.items.isEmpty()) {
            for (int i = 0; i < z.items.size(); i++) {
                ItemMap it = z.items.get(i);
                if (it != null && it.isNamecBall) {
                    ChangeMapService.gI().changeMap(p, z, Util.nextInt(100, z.map.mapWidth), 5);
                }
            }
        }
    }

    /**
     * Tính khoảng cách từ người chơi đến một viên Ngọc Rồng Namec cụ thể.
     * 
     * @param pl Người chơi.
     * @param id ID của viên Ngọc Rồng Namec (0-6).
     * @param temp ID mẫu của viên Ngọc Rồng.
     * @return Khoảng cách dưới dạng chuỗi, hoặc "?" nếu không xác định được.
     */
    public String getDis(Player pl, int id, short temp) {
        try {
            int idMAP = mapNrNamec[id];
            int idZone = zoneNrNamec[id];
            Integer[] sttMap = {8, 9, 11, 12, 13, 31, 32, 33, 34};
            Zone z = Manager.MAPS.get(idMAP).zones.get(idZone);
            if (z != null && !z.items.isEmpty()) {
                ItemMap it = z.getItemMapByTempId(temp);
                if (it != null) {
                    if (pl.zone.map.mapId == it.zone.map.mapId) {
                        return String.valueOf(Math.abs((pl.location.x - it.x) / 10));
                    } else {
                        List<Integer> check = Arrays.asList(sttMap);
                        if (check.contains(pl.zone.map.mapId)) {
                            int index = findIndex(pl.zone.map.mapId);
                            int indexMap = findIndex(idMAP);
                            int w = 0;
                            for (int i = 0; i < findIndex(index, indexMap).size(); i++) {
                                int map = findIndex(index, indexMap).get(i);
                                w += Manager.MAPS.get(map).mapWidth;
                            }
                            return String.valueOf(Math.abs((pl.location.x - it.x - w) / 10));
                        } else {
                            return "?";
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "?";
    }

    /**
     * Tìm chỉ số của bản đồ trong danh sách các bản đồ được xác định.
     * 
     * @param id ID của bản đồ.
     * @return Chỉ số của bản đồ, hoặc -1 nếu không tìm thấy.
     */
    public byte findIndex(int id) {
        Integer[] sttMap = {8, 9, 11, 12, 13, 31, 32, 33, 34};
        for (byte i = 0; i < sttMap.length; i++) {
            if (sttMap[i] == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Tìm danh sách các bản đồ nằm giữa hai chỉ số bản đồ.
     * 
     * @param start Chỉ số bắt đầu.
     * @param stop Chỉ số kết thúc.
     * @return Danh sách ID các bản đồ nằm giữa start và stop.
     */
    public List<Integer> findIndex(int start, int stop) {
        List<Integer> a = new ArrayList<>();
        Integer[] sttMap = {8, 9, 11, 12, 13, 31, 32, 33, 34};
        if (start < stop) {
            for (int i = start; i < stop; i++) {
                a.add(sttMap[i]);
            }
        } else {
            for (int i = stop; i < start; i++) {
                a.add(sttMap[i]);
            }
        }
        return a;
    }

    /**
     * Cập nhật trạng thái Ngọc Rồng Namec, khởi tạo lại nếu cần.
     */
    public void update() {
        if (this.isReinit && this.lastTimeReinit - System.currentTimeMillis() <= 0) {
            removeStoneNrNamec();
            initNgocRongNamec((byte) 0);
        }
    }
}