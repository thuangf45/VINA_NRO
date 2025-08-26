package com.girlkun.models.matches;

import com.girlkun.models.matches.pvp.TraThu;
import com.girlkun.models.matches.pvp.ThachDau;
import com.girlkun.consts.ConstNpc;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;
import java.io.IOException;

/**
 * Lớp quản lý các chức năng liên quan đến trận đấu PVP, bao gồm thách đấu và trả thù.
 * @author Lucifer
 */
public class PVPService {

    /**
     * Mảng chứa các mức cược vàng cho thách đấu.
     */
    private static final int[] GOLD_CHALLENGE = {1000000, 10000000, 100000000};

    /**
     * Mảng chứa các tùy chọn hiển thị mức cược vàng dưới dạng chuỗi.
     */
    private String[] optionsGoldChallenge;

    /**
     * Hằng số cho hành động mở giao diện chọn mức cược vàng.
     */
    private static final byte OPEN_GOLD_SELECT = 0;

    /**
     * Hằng số cho hành động chấp nhận thách đấu PVP.
     */
    private static final byte ACCEPT_PVP = 1;

    /**
     * Instance duy nhất của lớp PVPService (Singleton).
     */
    private static PVPService i;

    /**
     * Lấy instance của PVPService (Singleton pattern).
     * @return Instance của PVPService
     */
    public static PVPService gI() {
        if (i == null) {
            i = new PVPService();
        }
        return i;
    }

    /**
     * Constructor khởi tạo PVPService và tạo các tùy chọn mức cược vàng.
     */
    public PVPService() {
        this.optionsGoldChallenge = new String[GOLD_CHALLENGE.length];
        for (int i = 0; i < GOLD_CHALLENGE.length; i++) {
            this.optionsGoldChallenge[i] = Util.numberToMoney(GOLD_CHALLENGE[i]) + " vàng";
        }
    }

    /**
     * Xử lý các lệnh điều khiển thách đấu PVP từ người chơi.
     * @param player Người chơi thực hiện lệnh
     * @param message Tin nhắn chứa thông tin lệnh
     */
    public void controllerThachDau(Player player, Message message) {
        try {
            byte action = message.reader().readByte();
            byte type = message.reader().readByte();
            int playerId = message.reader().readInt();
            Player plMap = player.zone.getPlayerInMap(playerId);
            switch (action) {
                case OPEN_GOLD_SELECT:
                    openSelectGold(player, plMap);
                    break;
                case ACCEPT_PVP:
                    acceptPVP(player);
                    break;
            }
        } catch (IOException e) {
        }
    }

    /**
     * Mở giao diện chọn mức cược vàng cho người chơi thách đấu.
     * @param pl Người chơi khởi tạo thách đấu
     * @param plMap Người chơi được thách đấu
     */
    private void openSelectGold(Player pl, Player plMap) {
        if (pl == null || plMap == null) {
            return;
        }
        if (pl.pvp != null || plMap.pvp != null) {
            Service.gI().hideWaitDialog(pl);
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        pl.iDMark.setIdPlayThachDau(plMap.id);
        NpcService.gI().createMenuConMeo(pl, ConstNpc.MAKE_MATCH_PVP,
                -1, plMap.name + " (sức mạnh " + Util.numberToMoney(plMap.nPoint.power)
                        + ")\nBạn muốn cược bao nhiêu vàng?",
                this.optionsGoldChallenge);
    }

    /**
     * Gửi lời mời thách đấu PVP tới người chơi được chọn.
     * @param pl Người chơi gửi lời mời
     * @param selectGold Mức cược vàng được chọn
     */
    public void sendInvitePVP(Player pl, byte selectGold) {
        if (pl == null) {
            return;
        }
        Player plMap = pl.zone.getPlayerInMap(pl.iDMark.getIdPlayThachDau());
        if (plMap == null) {
            Service.gI().sendThongBao(pl, "Đối thủ đã rời khỏi map");
            return;
        }
        if (pl.nPoint.power < 40000000000L || plMap.nPoint.power < 40000000000L) {
            Service.gI().sendThongBao(pl, "40 Tỷ mới thách đấu được nhé, Mr Ping bảo thế !");
            return;
        }
        int goldThachDau = GOLD_CHALLENGE[selectGold];
        if (pl.inventory.gold < goldThachDau) {
            Service.gI().sendThongBao(pl, "Bạn chỉ có " + pl.inventory.gold + " vàng, không đủ tiền cược");
            return;
        }
        if (plMap.inventory.gold < goldThachDau) {
            Service.gI().sendThongBao(pl, "Đối thủ chỉ có " + plMap.inventory.gold + " vàng, không đủ tiền cược");
            return;
        }

        plMap.iDMark.setIdPlayThachDau(pl.id);
        plMap.iDMark.setGoldThachDau(goldThachDau);

        // Gửi message
        Message msg = null;
        try {
            msg = new Message(-59);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(goldThachDau);
            msg.writer().writeUTF(pl.name + " (sức mạnh " + Util.numberToMoney(pl.nPoint.power) + ") muốn thách đấu bạn với mức cược " + goldThachDau);
            plMap.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Xử lý khi người chơi chấp nhận thách đấu PVP.
     * @param pl Người chơi chấp nhận thách đấu
     */
    private void acceptPVP(Player pl) {
        if (pl == null) {
            return;
        }
        Player plMap = pl.zone.getPlayerInMap(pl.iDMark.getIdPlayThachDau());
        if (plMap == null) {
            Service.gI().sendThongBao(pl, "Đối thủ đã rời khỏi map");
            return;
        }
        if (pl.nPoint.power < 40000000000L || plMap.nPoint.power < 40000000000L) {
            Service.gI().sendThongBao(pl, "Cả hai bên cần đạt trên 40 tỉ sức mạnh để thực hiện thách đấu.");
            Service.gI().sendThongBao(plMap, "Cả hai bên cần đạt trên 40 tỉ sức mạnh để thực hiện thách đấu.");
            return;
        }
        if (pl.pvp != null || plMap.pvp != null) {
            Service.gI().hideWaitDialog(pl);
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        int goldThachDau = pl.iDMark.getGoldThachDau();
        if (pl.inventory.gold < goldThachDau) {
            Service.gI().sendThongBao(pl, "Không đủ vàng để thực hiện");
            return;
        }
        if (plMap.inventory.gold < goldThachDau) {
            Service.gI().sendThongBao(pl, "Đối thủ không đủ vàng để thực hiện");
            return;
        }
        ThachDau thachDau = new ThachDau(pl, plMap, goldThachDau);
    }

    /**
     * Mở giao diện chọn trả thù cho người chơi.
     * @param pl Người chơi muốn trả thù
     * @param idEnemy ID của kẻ thù
     */
    public void openSelectRevenge(Player pl, long idEnemy) {
        Player enemy = Client.gI().getPlayer(idEnemy);
        if (enemy == null) {
            Service.gI().sendThongBao(pl, "Kẻ thù hiện đang offline");
            return;
        }

        pl.iDMark.setIdEnemy(idEnemy);
        NpcService.gI().createMenuConMeo(pl, ConstNpc.REVENGE,
                -1, "Bạn muốn đến ngay chỗ hắn?", "Ok", "Từ chối");
    }

    /**
     * Xử lý khi người chơi chấp nhận trả thù.
     * @param pl Người chơi chấp nhận trả thù
     */
    public void acceptRevenge(Player pl) {
        Player enemy = Client.gI().getPlayer(pl.iDMark.getIdEnemy());
        if (enemy == null) {
            Service.gI().sendThongBao(pl, "Kẻ thù hiện đang offline");
            return;
        }
        if (pl.pvp != null || enemy.pvp != null) {
            Service.gI().hideWaitDialog(pl);
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        Zone mapGo = enemy.zone;
        if ((mapGo = ChangeMapService.gI().checkMapCanJoin(pl, mapGo)) == null || mapGo.isFullPlayer()) {
            Service.gI().sendThongBao(pl, "Không thể tới ngay lúc này, vui lòng đợi sau ít phút");
            return;
        }
        TraThu traThu = new TraThu(pl, enemy);
    }
}