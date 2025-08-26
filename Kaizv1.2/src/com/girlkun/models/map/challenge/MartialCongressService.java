package com.girlkun.models.map.challenge;

import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

/**
 * Lớp MartialCongressService quản lý các dịch vụ liên quan đến giải đấu võ thuật, bao gồm khởi tạo và di chuyển người chơi.
 * @author Lucifer
 */
public class MartialCongressService {

    /**
     * Thể hiện duy nhất của lớp MartialCongressService (mô hình Singleton).
     */
    private static MartialCongressService i;

    /**
     * Lấy thể hiện duy nhất của lớp MartialCongressService.
     * @return Thể hiện của MartialCongressService.
     */
    public static MartialCongressService gI() {
        if (i == null) {
            i = new MartialCongressService();
        }
        return i;
    }

    /**
     * Bắt đầu giải đấu võ thuật cho người chơi, chuyển họ đến khu vực thi đấu và khởi tạo vòng đấu đầu tiên.
     * @param player Người chơi tham gia giải đấu.
     */
    public void startChallenge(Player player) {
        Zone zone = getMapChalllenge(129);
        if (zone != null) {
            ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
            setTimeout(() -> {
                MartialCongress mc = new MartialCongress();
                mc.setPlayer(player);
                mc.setNpc(zone.getReferee());
                mc.toTheNextRound();
                MartialCongressManager.gI().add(mc);
                Service.getInstance().sendThongBao(player, "Số thứ tự của ngươi là 1\n chuẩn bị thi đấu nhé");
            }, 500);
        } else {
        }
    }

    /**
     * Thiết lập một tác vụ chạy sau một khoảng thời gian chờ.
     * @param runnable Tác vụ cần thực hiện.
     * @param delay Thời gian chờ (mili giây).
     */
    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    /**
     * Di chuyển nhanh người chơi đến tọa độ cụ thể trong bản đồ.
     * @param pl Người chơi cần di chuyển.
     * @param x Tọa độ x đích.
     * @param y Tọa độ y đích.
     */
    public void moveFast(Player pl, int x, int y) {
        Message msg;
        try {
            msg = new Message(58);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeInt((int) pl.id);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông tin loại PK (PVP) giữa người chơi và boss.
     * @param player Người chơi nhận thông tin.
     * @param boss Boss đối thủ.
     */
    public void sendTypePK(Player player, Player boss) {
        if (player != null) {
            Message msg;
            try {
                msg = Service.getInstance().messageSubCommand((byte) 35);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeByte(3);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lấy một khu vực ngẫu nhiên cho bản đồ giải đấu với ID cụ thể, đảm bảo không có boss.
     * @param mapId ID của bản đồ.
     * @return Khu vực hợp lệ hoặc null nếu không tìm thấy.
     */
    public Zone getMapChalllenge(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.getNumOfBosses() < 1) {
            return map;
        }
        return null;
    }
}