package com.girlkun.models.map.gas;

import com.girlkun.models.boss.BossID;
//import com.girlkun.models.boss.bdkb.TrungUyXanhLo;
import com.girlkun.models.boss.list_boss.gas.DrLyChee;
import com.girlkun.models.boss.list_boss.gas.HaChiJack;
import com.girlkun.models.item.Item;
//import static com.girlkun.models.map.bando.BanDoKhoBau.TIME_BAN_DO_KHO_BAU;
import static com.girlkun.models.map.gas.Gas.TIME_KHI_GAS;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.List;

/**
 * Lớp GasService quản lý việc mở và xử lý các hoạt động liên quan đến bản đồ Khí Gas trong trò chơi.
 * @author Lucifer
 */
public class GasService {

    /**
     * Thể hiện duy nhất của lớp GasService (mô hình Singleton).
     */
    public static GasService i;

    /**
     * Khởi tạo đối tượng GasService (private để hỗ trợ Singleton).
     */
    public GasService() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp GasService.
     * @return Thể hiện của GasService.
     */
    public static GasService gI() {
        if (i == null) {
            i = new GasService();
        }
        return i;
    }

    /**
     * Cập nhật trạng thái bản đồ Khí Gas, kiểm tra thời gian và kết thúc nếu cần.
     * @param player Người chơi cần kiểm tra.
     */
    public void update(Player player) {
        if (player.isPl() == true && player.clan.khiGas != null
                && player.clan.timeOpenKhiGas != 0) {
            if (Util.canDoWithTime(player.clan.timeOpenKhiGas, TIME_KHI_GAS)) {
                ketthucGas(player);
                player.clan.khiGas = null;
            }
        }
    }

    /**
     * Đưa người chơi ra khỏi bản đồ Khí Gas khi sự kiện kết thúc.
     * @param player Người chơi cần đưa ra ngoài.
     */
    private void kickOutOfGas(Player player) {
        if (MapService.gI().isMapKhiGas(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    /**
     * Kết thúc bản đồ Khí Gas, đưa tất cả người chơi trong khu vực ra ngoài.
     * @param player Người chơi khởi tạo kết thúc.
     */
    private void ketthucGas(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfGas(pl);
        }
    }

    /**
     * Mở bản đồ Khí Gas cho người chơi với cấp độ cụ thể.
     * @param player Người chơi muốn mở bản đồ.
     * @param level Cấp độ của bản đồ Khí Gas.
     */
    public void openBanDoKhoBau1(Player player, int level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.khiGas == null) {
                Item item = InventoryServiceNew.gI().findItemBag(player, 1375);
                if (item != null && item.quantity > 0) {
                    Gas gas = null;
                    for (Gas bdkb : Gas.KHI_GAS) {
                        if (!bdkb.isOpened) {
                            gas = bdkb;
                            break;
                        }
                    }
                    if (gas != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
//                        gas.openBanDoKhoBau1(player, player.clan, level);
                        try {
                            long totalDame = 0;
                            long totalHp = 0;
                            for (Player play : player.clan.membersInGame) {
                                totalDame += play.nPoint.dame;
                                totalHp += play.nPoint.hpMax;
                            }
                            long dame = (totalHp / 20) * (level);
                            long hp = (totalDame * 10) * (level);
                            if (dame >= 200000000L) {
                                dame = 20000L;
                            }
                            if (hp >= 200000000L) {
                                hp = 20000L;
                            }
//                            new DrLyChee(player.clan.khiGas.getMapById(148), player.clan.khiGas.level, (int) dame, (int) hp);
//                            new HaChiJack(player.clan.khiGas.getMapById(148), player.clan.khiGas.level, (int) dame, (int) hp);
//                            new TrungUyXanhLo(player.clan.banDoKhoBau.getMapById(137), player.clan.banDoKhoBau.level, (int) dame, (int) hp);
                        } catch (Exception e) {
                            Logger.logException(GasService.class, e, "Lỗi init boss");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bản đồ kho báu Khí Gas, vui lòng quay lại sau");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Yêu cầu có bản đồ khí Gas");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
}