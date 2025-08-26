package com.girlkun.models.map.blackball;

import com.girlkun.models.player.Player;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.util.Date;
import java.util.List;

/**
 * Lớp BlackBallWar quản lý sự kiện chiến tranh ngọc rồng sao đen, bao gồm thời gian, nhặt ngọc và phần thưởng.
 * @author Lucifer
 */
public class BlackBallWar {

    /**
     * Thời gian tối thiểu để nhặt ngọc rồng sao đen sau khi rơi (mili giây).
     */
    private static final int TIME_CAN_PICK_BLACK_BALL_AFTER_DROP = 0;

    /**
     * Hệ số nhân HP/KI cho ngọc rồng 3 sao.
     */
    public static final byte X3 = 3;

    /**
     * Hệ số nhân HP/KI cho ngọc rồng 5 sao.
     */
    public static final byte X5 = 5;

    /**
     * Hệ số nhân HP/KI cho ngọc rồng 7 sao.
     */
    public static final byte X7 = 7;

    /**
     * Chi phí vàng để kích hoạt nhân HP/KI 3 lần.
     */
    public static final int COST_X3 = 100000000;

    /**
     * Chi phí vàng để kích hoạt nhân HP/KI 5 lần.
     */
    public static final int COST_X5 = 300000000;

    /**
     * Chi phí vàng để kích hoạt nhân HP/KI 7 lần.
     */
    public static final int COST_X7 = 500000000;

    /**
     * Giờ mở sự kiện ngọc rồng sao đen.
     */
    public static final byte HOUR_OPEN = 20;

    /**
     * Phút mở sự kiện ngọc rồng sao đen.
     */
    public static final byte MIN_OPEN = 0;

    /**
     * Giây mở sự kiện ngọc rồng sao đen.
     */
    public static final byte SECOND_OPEN = 0;

    /**
     * Giờ cho phép nhặt ngọc rồng sao đen.
     */
    public static final byte HOUR_CAN_PICK_DB = 20;

    /**
     * Phút cho phép nhặt ngọc rồng sao đen.
     */
    public static final byte MIN_CAN_PICK_DB = 25;

    /**
     * Giây cho phép nhặt ngọc rồng sao đen.
     */
    public static final byte SECOND_CAN_PICK_DB = 0;

    /**
     * Giờ đóng sự kiện ngọc rồng sao đen.
     */
    public static final byte HOUR_CLOSE = 21;

    /**
     * Phút đóng sự kiện ngọc rồng sao đen.
     */
    public static final byte MIN_CLOSE = 0;

    /**
     * Giây đóng sự kiện ngọc rồng sao đen.
     */
    public static final byte SECOND_CLOSE = 0;

    /**
     * Số lượng bản đồ ngọc rồng sao đen tối đa.
     */
    public static final int AVAILABLE = 7;

    /**
     * Thời gian tối thiểu để giữ ngọc và giành chiến thắng (mili giây).
     */
    private static final int TIME_WIN = 300000;

    /**
     * Thể hiện duy nhất của lớp BlackBallWar (mô hình Singleton).
     */
    private static BlackBallWar i;

    /**
     * Thời điểm mở sự kiện ngọc rồng sao đen (mili giây).
     */
    public static long TIME_OPEN;

    /**
     * Thời điểm cho phép nhặt ngọc rồng sao đen (mili giây).
     */
    private static long TIME_CAN_PICK_DB;

    /**
     * Thời điểm đóng sự kiện ngọc rồng sao đen (mili giây).
     */
    public static long TIME_CLOSE;

    /**
     * Ngày hiện tại để kiểm tra cập nhật thời gian sự kiện.
     */
    private int day = -1;

    /**
     * Lấy thể hiện duy nhất của lớp BlackBallWar và cập nhật thời gian sự kiện.
     * @return Thể hiện của BlackBallWar.
     */
    public static BlackBallWar gI() {
        if (i == null) {
            i = new BlackBallWar();
        }
        i.setTime();
        return i;
    }

    /**
     * Cập nhật thời gian mở, đóng và thời điểm cho phép nhặt ngọc rồng sao đen.
     */
    public void setTime() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                this.TIME_OPEN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN + ":" + MIN_OPEN + ":" + SECOND_OPEN, "dd/MM/yyyy HH:mm:ss");
                this.TIME_CAN_PICK_DB = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CAN_PICK_DB + ":" + MIN_CAN_PICK_DB + ":" + SECOND_CAN_PICK_DB, "dd/MM/yyyy HH:mm:ss");
                this.TIME_CLOSE = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE + ":" + MIN_CLOSE + ":" + SECOND_CLOSE, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception e) {
            }
        }
    }

    /**
     * Thả ngọc rồng sao đen xuống bản đồ khi người chơi đang giữ.
     * @param player Người chơi thả ngọc.
     */
    public synchronized void dropBlackBall(Player player) {
        if (player.iDMark.isHoldBlackBall()) {
            player.iDMark.setHoldBlackBall(false);
            ItemMap itemMap = new ItemMap(player.zone,
                    player.iDMark.getTempIdBlackBallHold(), 1, player.location.x,
                    player.zone.map.yPhysicInTop(player.location.x, player.location.y - 24),
                    -1);
            Service.gI().dropItemMap(itemMap.zone, itemMap);
            player.iDMark.setTempIdBlackBallHold(-1);
            player.zone.lastTimeDropBlackBall = System.currentTimeMillis();
            Service.gI().sendFlagBag(player);

            if (player.clan != null) {
                List<Player> players = player.zone.getPlayers();
                for (Player pl : players) {
                    if (pl.clan != null && player.clan.equals(pl.clan)) {
                        Service.gI().changeFlag(pl, Util.nextInt(1, 7));
                    }
                }
            } else {
                Service.gI().changeFlag(player, Util.nextInt(1, 7));
            }
        }
    }

    /**
     * Cập nhật trạng thái sự kiện ngọc rồng sao đen, kiểm tra chiến thắng hoặc kết thúc.
     * @param player Người chơi cần kiểm tra.
     */
    public void update(Player player) {
        if (player.zone == null || !MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
            return;
        }
        if (player.iDMark.isHoldBlackBall()) {
            if (Util.canDoWithTime(player.iDMark.getLastTimeHoldBlackBall(), TIME_WIN)) {
                win(player);
                return;
            } else {
                if (Util.canDoWithTime(player.iDMark.getLastTimeNotifyTimeHoldBlackBall(), 10000)) {
                    Service.gI().sendThongBao(player, "Cố gắng giữ ngọc rồng trong "
                            + TimeUtil.getSecondLeft(player.iDMark.getLastTimeHoldBlackBall(), TIME_WIN / 1000)
                            + " giây nữa, đem chiến thắng về cho bang hội!");
                    player.iDMark.setLastTimeNotifyTimeHoldBlackBall(System.currentTimeMillis());
                }
            }
        }
        try {
            long now = System.currentTimeMillis();
            if (!(now > TIME_OPEN && now < TIME_CLOSE)) {
                if (player.iDMark.isHoldBlackBall()) {
                    win(player);
                } else {
                    kickOutOfMap(player);
                }
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Xử lý chiến thắng khi người chơi giữ ngọc đủ thời gian và trao phần thưởng.
     * @param player Người chơi giành chiến thắng.
     */
    private void win(Player player) {
        player.zone.finishBlackBallWar = true;
        int star = player.iDMark.getTempIdBlackBallHold() - 371;
        if (player.clan != null) {
            try {
                List<Player> players = player.clan.membersInGame;
                for (Player pl : players) {
                    if (pl != null) {
                        pl.rewardBlackBall.reward((byte) star);
                        Service.gI().sendThongBao(pl, "Chúc mừng bang hội của bạn đã "
                                + "dành chiến thắng ngọc rồng sao đen " + star + " sao");
                    }
                }
            } catch (Exception e) {
                Logger.logException(BlackBallWar.class, e,
                        "Lỗi ban thưởng ngọc rồng đen "
                                + star + " sao cho clan " + player.clan.id);
            }
        } else {
            player.rewardBlackBall.reward((byte) star);
            Service.gI().sendThongBao(player, "Chúc mừng bang hội của bạn đã "
                    + "dành chiến thắng ngọc rồng sao đen " + star + " sao");
        }

        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMap(pl);
        }
    }

    /**
     * Đưa người chơi ra khỏi bản đồ ngọc rồng sao đen khi sự kiện kết thúc.
     * @param player Người chơi cần đưa ra ngoài.
     */
    private void kickOutOfMap(Player player) {
        if (player.cFlag == 8) {
            Service.gI().changeFlag(player, Util.nextInt(1, 7));
        }
        Service.gI().sendThongBao(player, "Trận đại chiến ngọc rồng sao đen đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
    }

    /**
     * Chuyển người chơi đến bản đồ ngọc rồng sao đen theo chỉ số.
     * @param player Người chơi cần chuyển.
     * @param index Chỉ số bản đồ.
     */
    public void changeMap(Player player, byte index) {
        try {
            long now = System.currentTimeMillis();
            if (now > TIME_OPEN && now < TIME_CLOSE) {
                ChangeMapService.gI().changeMap(player,
                        player.mapBlackBall.get(index).map.mapId, -1, 50, 50);
            } else {
                Service.gI().sendThongBao(player, "Đại chiến ngọc rồng sao đen chưa mở");
                Service.gI().hideWaitDialog(player);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Thêm người chơi vào bản đồ ngọc rồng sao đen và thay đổi cờ bang hội.
     * @param player Người chơi tham gia.
     */
    public void joinMapBlackBallWar(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, pl.cFlag);
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, Util.nextInt(1, 7));
        }
    }

    /**
     * Xử lý việc nhặt ngọc rồng sao đen của người chơi.
     * @param player Người chơi nhặt ngọc.
     * @param item Vật phẩm ngọc rồng sao đen.
     * @return true nếu nhặt thành công, false nếu không.
     */
    public boolean pickBlackBall(Player player, Item item) {
        try {
            if (System.currentTimeMillis() < this.TIME_CAN_PICK_DB) {
                Service.gI().sendThongBao(player, "Chưa thể nhặt ngọc rồng sao đen lúc này, vui lòng đợi "
                        + TimeUtil.diffDate(new Date(this.TIME_CAN_PICK_DB),
                        new Date(System.currentTimeMillis()), TimeUtil.SECOND) + " giây nữa");
                return false;
            } else if (player.zone.finishBlackBallWar) {
                Service.gI().sendThongBao(player, "Đại chiến ngọc rồng sao đen "
                        + "đã kết thúc, hãy đợi đến ngày mai");
                return false;
            } else {
                if (Util.canDoWithTime(player.zone.lastTimeDropBlackBall, TIME_CAN_PICK_BLACK_BALL_AFTER_DROP)) {
                    player.iDMark.setHoldBlackBall(true);
                    player.iDMark.setTempIdBlackBallHold(item.template.id);
                    player.iDMark.setLastTimeHoldBlackBall(System.currentTimeMillis());
                    Service.gI().sendFlagBag(player);
                    if (player.clan != null) {
                        List<Player> players = player.zone.getPlayers();
                        for (Player pl : players) {
                            if (pl.clan != null && player.clan.equals(pl.clan)) {
                                Service.gI().changeFlag(pl, 8);
                            }
                        }
                    } else {
                        Service.gI().changeFlag(player, 8);
                    }
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Không thể nhặt ngọc rồng đen ngay lúc này");
                    return false;
                }
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Kích hoạt hiệu ứng nhân HP/KI cho người chơi dựa trên số vàng.
     * @param player Người chơi kích hoạt.
     * @param x Hệ số nhân HP/KI (X3, X5, X7).
     */
    public void xHPKI(Player player, byte x) {
        int cost = 0;
        switch (x) {
            case X3:
                cost = COST_X3;
                break;
            case X5:
                cost = COST_X5;
                break;
            case X7:
                cost = COST_X7;
                break;
        }
        if (player.inventory.gold >= cost) {
            player.inventory.gold -= cost;
            Service.gI().sendMoney(player);
            player.effectSkin.lastTimeXHPKI = System.currentTimeMillis();
            player.effectSkin.xHPKI = x;
            player.nPoint.calPoint();
            player.nPoint.setHp((long) player.nPoint.hp * x);
            player.nPoint.setMp((long) player.nPoint.mp * x);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().point(player);
        } else {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu "
                    + Util.numberToMoney(cost - player.inventory.gold) + " vàng");
        }
    }
}