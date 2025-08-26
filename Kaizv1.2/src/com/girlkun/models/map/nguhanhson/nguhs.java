package com.girlkun.models.map.nguhanhson;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Lớp nguhs quản lý bản đồ Ngũ Hành Sơn trong trò chơi, bao gồm thời gian mở/đóng và các hoạt động liên quan.
 * @author Lucifer
 */
@Data
public class nguhs {

    /**
     * Giờ mở bản đồ Ngũ Hành Sơn.
     */
    public static final byte HOUR_OPEN_MAP_NHS = 0;

    /**
     * Phút mở bản đồ Ngũ Hành Sơn.
     */
    public static final byte MIN_OPEN_MAP_NHS = 0;

    /**
     * Giây mở bản đồ Ngũ Hành Sơn.
     */
    public static final byte SECOND_OPEN_MAP_NHS = 0;

    /**
     * Giờ đóng bản đồ Ngũ Hành Sơn.
     */
    public static final byte HOUR_CLOSE_MAP_NHS = 12;

    /**
     * Phút đóng bản đồ Ngũ Hành Sơn.
     */
    public static final byte MIN_CLOSE_MAP_NHS = 0;

    /**
     * Giây đóng bản đồ Ngũ Hành Sơn.
     */
    public static final byte SECOND_CLOSE_MAP_NHS = 0;

    /**
     * Số lượng bản đồ Ngũ Hành Sơn tối đa.
     */
    public static final int AVAILABLE = 7;

    /**
     * Thể hiện duy nhất của lớp nguhs (mô hình Singleton).
     */
    private static nguhs i;

    /**
     * Thời điểm mở bản đồ Ngũ Hành Sơn (mili giây).
     */
    public static long TIME_OPEN_NHS;

    /**
     * Thời điểm đóng bản đồ Ngũ Hành Sơn (mili giây).
     */
    public static long TIME_CLOSE_NHS;

    /**
     * Ngày hiện tại để kiểm tra cập nhật thời gian mở/đóng.
     */
    private int day = -1;

    /**
     * Lấy thể hiện duy nhất của lớp nguhs và cập nhật thời gian mở/đóng.
     * @return Thể hiện của nguhs.
     */
    public static nguhs gI() {
        if (i == null) {
            i = new nguhs();
        }
        i.setTimeJoinnguhs();
        return i;
    }

    /**
     * Cập nhật thời gian mở và đóng bản đồ Ngũ Hành Sơn dựa trên ngày hiện tại.
     */
    public void setTimeJoinnguhs() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                TIME_OPEN_NHS = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_NHS + ":" + MIN_OPEN_MAP_NHS + ":" + SECOND_OPEN_MAP_NHS, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_NHS = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_NHS + ":" + MIN_CLOSE_MAP_NHS + ":" + SECOND_CLOSE_MAP_NHS, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Đưa người chơi ra khỏi bản đồ Ngũ Hành Sơn khi sự kiện kết thúc.
     * @param player Người chơi cần đưa ra ngoài.
     */
    private void kickOutOfnguhs(Player player) {
        if (MapService.gI().isnguhs(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Hết thời gian rồi, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    /**
     * Kết thúc bản đồ Ngũ Hành Sơn và đưa tất cả người chơi trong khu vực ra ngoài.
     * @param player Người chơi khởi tạo kết thúc.
     */
    private void ketthucnguhs(Player player) {
        player.zone.finishnguhs = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfnguhs(pl);
        }
    }

    /**
     * Thêm người chơi vào bản đồ Ngũ Hành Sơn và thay đổi cờ bang hội nếu cần.
     * @param player Người chơi tham gia bản đồ.
     */
    public void joinnguhs(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.getInstance().changeFlag(player, 8);
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.getInstance().changeFlag(player, 8);
        }
    }

    /**
     * Cập nhật trạng thái bản đồ Ngũ Hành Sơn, kiểm tra thời gian và kết thúc nếu cần.
     * @param player Người chơi cần kiểm tra.
     */
    public void update(Player player) {
        try {
            long now = System.currentTimeMillis();
            if (!(now > TIME_OPEN_NHS && now < TIME_CLOSE_NHS) && MapService.gI().isnguhs(player.zone.map.mapId)) {
                ketthucnguhs(player);
            }
        } catch (Exception ex) {
        }
    }
}