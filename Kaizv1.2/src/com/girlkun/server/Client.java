package com.girlkun.server;

import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.girlkun.server.io.MySession;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.SummonDragon;
import com.girlkun.services.func.TransactionService;
import com.girlkun.services.InventoryServiceNew;
//import com.girlkun.services.NgocRongNamecService;
import com.girlkun.utils.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;

/**
 * Lớp Client quản lý toàn bộ người chơi đang kết nối vào server.
 * - Chứa danh sách Player theo ID, userId, name
 * - Xử lý đăng nhập, đăng xuất, kick session, lưu dữ liệu khi thoát
 * - Vòng lặp cập nhật session client
 *
 * Singleton pattern (dùng Client.gI() để lấy instance).
 *
 * @author Lucifer
 */
public class Client implements Runnable {

    /** Singleton instance */
    private static Client i;

    /** Map quản lý Player theo id */
    private final Map<Long, Player> players_id = new HashMap<Long, Player>();
    /** Map quản lý Player theo userId */
    private final Map<Integer, Player> players_userId = new HashMap<Integer, Player>();
    /** Map quản lý Player theo tên */
    private final Map<String, Player> players_name = new HashMap<String, Player>();
    /** Danh sách toàn bộ Player online */
    private final List<Player> players = new ArrayList<>();

    /**
     * @return danh sách toàn bộ Player đang online
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Lấy instance duy nhất (Singleton)
     * @return instance Client
     */
    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    /**
     * Đăng ký player vào hệ thống quản lý
     * @param player người chơi
     */
    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }
    }

    /**
     * Xóa session ra khỏi hệ thống
     * @param session phiên kết nối
     */
    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }
        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                GirlkunDB.executeUpdate("update account set last_time_logout = ? where id = ?", 
                        new Timestamp(System.currentTimeMillis()), session.userId);
            } catch (Exception e) {
                // bỏ qua lỗi DB
            }
        }
        ServerManager.gI().disconnect(session);
    }

    /**
     * Xóa player ra khỏi hệ thống và xử lý lưu dữ liệu
     * @param player người chơi
     */
    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;

            // Thoát map, hủy giao dịch, xóa buff, xử lý clan, mob, pet, Shenron
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
            if (player.itemTime != null && player.itemTime.isUseTDLT) {
                Item tdlt = null;
                try {
                    tdlt = InventoryServiceNew.gI().findItemBag(player, 521);
                } catch (Exception e) {
                }
                if (tdlt != null) {
                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
                }
            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    /**
     * Kick session ra khỏi server
     * @param session phiên kết nối
     */
    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    /**
     * Lấy Player theo id
     * @param playerId id người chơi
     * @return Player
     */
    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    /**
     * Lấy Player theo userId
     * @param userId id tài khoản
     * @return Player
     */
    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    /**
     * Lấy Player theo tên
     * @param name tên người chơi
     * @return Player
     */
    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    /**
     * Đóng toàn bộ kết nối, lưu dữ liệu người chơi trước khi server tắt
     */
    public void close() {
        Logger.log(Logger.BLACK, "Hệ thống tiến hành lưu dữ liệu người chơi và đăng xuất người chơi khỏi server." 
                + players.size() + "\n");
        while (!players.isEmpty()) {
            this.kickSession((MySession) players.remove(0).getSession());
        }
        Logger.error("Hệ thống lỗi đăng xuất người chơi\n");
    }

    /**
     * Kick ra các session không có player (session ma)
     */
    public void cloneMySessionNotConnect() {
        Logger.error("BEGIN KICK OUT MySession Not Connect...............................\n");
        Logger.error("COUNT: " + GirlkunSessionManager.gI().getSessions().size());
        if (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
            for (int j = 0; j < GirlkunSessionManager.gI().getSessions().size(); j++) {
                MySession m = (MySession) GirlkunSessionManager.gI().getSessions().get(j);
                if (m.player == null) {
                    this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(j));
                }
            }
        }
        Logger.error("..........................................................SUCCESSFUL\n");
    }

    /**
     * Vòng lặp chính để cập nhật session client
     */
    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
                // bỏ qua lỗi vòng lặp
            }
        }
    }

    /**
     * Cập nhật session: xử lý timeWait, auto kick khi quá hạn
     */
    private void update() {
        if (GirlkunSessionManager.gI().getSessions() != null) {
            for (ISession s : GirlkunSessionManager.gI().getSessions()) {
                MySession session = (MySession) s;
                if (session.timeWait > 0) {
                    session.timeWait--;
                    if (session.timeWait == 0) {
                        kickSession(session);
                    }
                }
            }
        }
    }

    /**
     * Hiển thị thông tin thống kê số lượng session, player
     * @param player người chơi gọi lệnh
     */
    public void show(Player player) {
        String txt = "";
        txt += "sessions: " + GirlkunSessionManager.gI().getSessions().size() + "\n";
        txt += "players_id: " + players_id.size() + "\n";
        txt += "players_userId: " + players_userId.size() + "\n";
        txt += "players_name: " + players_name.size() + "\n";
        txt += "players: " + players.size() + "\n";
        Service.gI().sendThongBao(player, txt);
    }
}
