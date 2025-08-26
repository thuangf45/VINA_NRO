package com.girlkun.models.map.MapMaBu;

import com.girlkun.models.player.Player;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.util.List;

/**
 * Lớp MapMaBu quản lý bản đồ Ma Bư trong trò chơi, bao gồm thời gian mở/đóng và các hoạt động liên quan.
 * @author Lucifer
 */
public class MapMaBu {

    /**
     * Giờ mở bản đồ Ma Bư.
     */
    public static final byte HOUR_OPEN_MAP_MABU = 12;

    /**
     * Phút mở bản đồ Ma Bư.
     */
    public static final byte MIN_OPEN_MAP_MABU = 0;

    /**
     * Giây mở bản đồ Ma Bư.
     */
    public static final byte SECOND_OPEN_MAP_MABU = 0;

    /**
     * Giờ đóng bản đồ Ma Bư.
     */
    public static final byte HOUR_CLOSE_MAP_MABU = 13;

    /**
     * Phút đóng bản đồ Ma Bư.
     */
    public static final byte MIN_CLOSE_MAP_MABU = 0;

    /**
     * Giây đóng bản đồ Ma Bư.
     */
    public static final byte SECOND_CLOSE_MAP_MABU = 0;

    /**
     * Số lượng bản đồ Ma Bư tối đa.
     */
    public static final int AVAILABLE = 7;

    /**
     * Thể hiện duy nhất của lớp MapMaBu (mô hình Singleton).
     */
    private static MapMaBu i;

    /**
     * Thời điểm mở bản đồ Ma Bư (mili giây).
     */
    public static long TIME_OPEN_MABU;

    /**
     * Thời điểm đóng bản đồ Ma Bư (mili giây).
     */
    public static long TIME_CLOSE_MABU;

    /**
     * Ngày hiện tại để kiểm tra cập nhật thời gian mở/đóng.
     */
    private int day = -1;

    /**
     * Lấy thể hiện duy nhất của lớp MapMaBu và cập nhật thời gian mở/đóng.
     * @return Thể hiện của MapMaBu.
     */
    public static MapMaBu gI() {
        if (i == null) {
            i = new MapMaBu();
        }
        i.setTimeJoinMapMaBu();
        return i;
    }

    /**
     * Cập nhật thời gian mở và đóng bản đồ Ma Bư dựa trên ngày hiện tại.
     */
    public void setTimeJoinMapMaBu() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                TIME_OPEN_MABU = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_MABU + ":" + MIN_OPEN_MAP_MABU + ":" + SECOND_OPEN_MAP_MABU, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_MABU = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_MABU + ":" + MIN_CLOSE_MAP_MABU + ":" + SECOND_CLOSE_MAP_MABU, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception e) {
            }
        }
    }

    /**
     * Đưa người chơi ra khỏi bản đồ Ma Bư khi sự kiện kết thúc.
     * @param player Người chơi cần đưa ra ngoài.
     */
    private void kickOutOfMapMabu(Player player) {
        if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    /**
     * Kết thúc bản đồ Ma Bư và đưa tất cả người chơi trong khu vực ra ngoài.
     * @param player Người chơi khởi tạo kết thúc (có thể null).
     */
    private void ketthucmabu(Player player) {
        if (player != null && player.zone != null) {
            player.zone.finishMapMaBu = true;
            List<Player> playersMap = player.zone.getPlayers();
            for (int i = playersMap.size() - 1; i >= 0; i--) {
                Player pl = playersMap.get(i);
                kickOutOfMapMabu(pl);
            }
        }
    }

    /**
     * Thêm người chơi vào bản đồ Ma Bư và thay đổi cờ bang hội nếu cần.
     * @param player Người chơi tham gia bản đồ.
     */
    public void joinMapMabu(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, Util.nextInt(9, 10));
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, Util.nextInt(9, 10));
        }
    }

    /**
     * Cập nhật trạng thái bản đồ Ma Bư, kiểm tra thời gian và kết thúc nếu cần.
     * @param player Người chơi cần kiểm tra.
     */
    public void update(Player player) {
        if (player.zone == null || !MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
            try {
                long now = System.currentTimeMillis();
                if (now < TIME_OPEN_MABU || now > TIME_CLOSE_MABU) {
                    ketthucmabu(player);
                }
            } catch (Exception e) {
            }
        }
    }
}