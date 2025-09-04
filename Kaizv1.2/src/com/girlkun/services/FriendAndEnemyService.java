package com.girlkun.services;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.player.Enemy;
import com.girlkun.models.player.Friend;
import com.girlkun.models.player.Player;
import com.girlkun.models.matches.PVPService;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.io.IOException;

/**
 * Lớp FriendAndEnemyService quản lý các chức năng liên quan đến bạn bè và kẻ thù trong game, bao gồm thêm bạn, xóa bạn,
 * thêm kẻ thù, báo thù, và dịch chuyển tới người chơi khác. Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class FriendAndEnemyService {
    
    // Các hằng số đại diện cho hành động với danh sách bạn bè
    /** Hằng số biểu thị mở danh sách bạn bè */
    private static final byte OPEN_LIST = 0;
    /** Hằng số biểu thị thêm bạn */
    private static final byte MAKE_FRIEND = 1;
    /** Hằng số biểu thị xóa bạn */
    private static final byte REMOVE_FRIEND = 2;
    
    // Các hằng số đại diện cho hành động với danh sách kẻ thù
    /** Hằng số biểu thị báo thù */
    private static final byte REVENGE = 1;
    /** Hằng số biểu thị xóa kẻ thù */
    private static final byte REMOVE_ENEMY = 2;
    
    /** Thể hiện duy nhất của lớp FriendAndEnemyService (singleton pattern) */
    private static FriendAndEnemyService i;
    
    /**
     * Lấy thể hiện duy nhất của lớp FriendAndEnemyService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp FriendAndEnemyService.
     */
    public static FriendAndEnemyService gI() {
        if (i == null) {
            i = new FriendAndEnemyService();
        }
        return i;
    }
    
    /**
     * Xử lý các hành động liên quan đến danh sách bạn bè của người chơi.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param msg Thông điệp chứa thông tin hành động.
     */
    public void controllerFriend(Player player, Message msg) {
        try {
            byte action = msg.reader().readByte();
            switch (action) {
                case OPEN_LIST:
                    openListFriend(player);
                    break;
                case MAKE_FRIEND:
                    makeFriend(player, msg.reader().readInt());
                    break;
                case REMOVE_FRIEND:
                    removeFriend(player, msg.reader().readInt());
                    break;
            }
        } catch (IOException ex) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }
    
    /**
     * Xử lý các hành động liên quan đến danh sách kẻ thù của người chơi.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param msg Thông điệp chứa thông tin hành động.
     */
    public void controllerEnemy(Player player, Message msg) {
        try {
            byte action = msg.reader().readByte();
            switch (action) {
                case OPEN_LIST:
                    openListEnemy(player);
                    break;
                case REVENGE:
                    if (true) {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                        break;
                    }
                    int id = msg.reader().readInt();
                    boolean flag = false;
                    for (Enemy e : player.enemies) {
                        if (e.id == id) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        PVPService.gI().openSelectRevenge(player, id);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case REMOVE_ENEMY:
                    removeEnemy(player, msg.reader().readInt());
                    break;
            }
        } catch (IOException ex) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }
    
    /**
     * Cập nhật thông tin bạn bè của người chơi (trạng thái online, sức mạnh, trang bị).
     * 
     * @param player Người chơi cần cập nhật danh sách bạn bè.
     */
    private void reloadFriend(Player player) {
        for (Friend f : player.friends) {
            Player pl = null;
            if ((pl = Client.gI().getPlayerByUser(f.id)) != null || (pl = Client.gI().getPlayer(f.name)) != null) {
                try {
                    f.power = pl.nPoint.power;
                    f.head = pl.getHead();
                    f.body = pl.getBody();
                    f.leg = pl.getLeg();
                    f.bag = (byte) pl.getFlagBag();
                } catch (Exception e) {
                    // Bỏ qua lỗi để tránh gián đoạn cập nhật
                }
                f.online = true;
            } else {
                f.online = false;
            }
        }
    }
    
    /**
     * Cập nhật thông tin kẻ thù của người chơi (trạng thái online, sức mạnh, trang bị).
     * 
     * @param player Người chơi cần cập nhật danh sách kẻ thù.
     */
    private void reloadEnemy(Player player) {
        for (Enemy e : player.enemies) {
            Player pl = null;
            if ((pl = Client.gI().getPlayerByUser(e.id)) != null || (pl = Client.gI().getPlayer(e.name)) != null) {
                try {
                    e.power = pl.nPoint.power;
                    e.head = pl.getHead();
                    e.body = pl.getBody();
                    e.leg = pl.getLeg();
                    e.bag = (byte) pl.getFlagBag();
                } catch (Exception ex) {
                    // Bỏ qua lỗi để tránh gián đoạn cập nhật
                }
                e.online = true;
            } else {
                e.online = false;
            }
        }
    }
    
    /**
     * Mở danh sách bạn bè và gửi thông tin tới người chơi.
     * 
     * @param player Người chơi cần xem danh sách bạn bè.
     */
    private void openListFriend(Player player) {
        reloadFriend(player);
        Message msg;
        try {
            msg = new Message(-80);
            msg.writer().writeByte(OPEN_LIST);
            msg.writer().writeByte(player.friends.size());
            for (Friend f : player.friends) {
                msg.writer().writeInt(f.id);
                msg.writer().writeShort(f.head);
                msg.writer().writeShort(-1);
                msg.writer().writeShort(f.body);
                msg.writer().writeShort(f.leg);
                msg.writer().writeByte(f.bag);
                msg.writer().writeUTF(f.name);
                msg.writer().writeBoolean(Client.gI().getPlayer((int) f.id) != null);
                msg.writer().writeUTF(Util.numberToMoney(f.power));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(FriendAndEnemyService.class, e);
        }
    }
    
    /**
     * Mở danh sách kẻ thù và gửi thông tin tới người chơi.
     * 
     * @param player Người chơi cần xem danh sách kẻ thù.
     */
    private void openListEnemy(Player player) {
        reloadEnemy(player);
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(OPEN_LIST);
            msg.writer().writeByte(player.enemies.size());
            for (Enemy e : player.enemies) {
                msg.writer().writeInt(e.id);
                msg.writer().writeShort(e.head);
                msg.writer().writeShort(-1);
                msg.writer().writeShort(e.body);
                msg.writer().writeShort(e.leg);
                msg.writer().writeShort(e.bag);
                msg.writer().writeUTF(e.name);
                msg.writer().writeUTF(Util.numberToMoney(e.power));
                msg.writer().writeBoolean(Client.gI().getPlayer((int) e.id) != null);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(FriendAndEnemyService.class, e);
        }
    }
    
    /**
     * Thêm người chơi khác vào danh sách bạn bè.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param playerId ID của người chơi được thêm làm bạn.
     */
    private void makeFriend(Player player, int playerId) {
        boolean madeFriend = false;
        for (Friend friend : player.friends) {
            if (friend.id == playerId) {
                Service.gI().sendThongBao(player, "Đã có trong danh sách bạn bè");
                madeFriend = true;
                break;
            }
        }
        if (!madeFriend) {
            Player pl = Client.gI().getPlayer(playerId);
            if (pl != null) {
                String npcSay;
                if (player.friends.size() >= 5) {
                    npcSay = "Bạn có muốn kết bạn với " + pl.name + " với phí là 5 ngọc ?";
                } else {
                    npcSay = "Bạn có muốn kết bạn với " + pl.name + " ?";
                }
                NpcService.gI().createMenuConMeo(player, ConstNpc.MAKE_FRIEND, -1, npcSay, new String[]{"Đồng ý", "Từ chối"}, playerId);
            }
        }
    }
    
    /**
     * Xóa người chơi khỏi danh sách bạn bè.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param playerId ID của người chơi bị xóa khỏi danh sách bạn.
     */
    private void removeFriend(Player player, int playerId) {
        for (int i = 0; i < player.friends.size(); i++) {
            if (player.friends.get(i).id == playerId) {
                Service.gI().sendThongBao(player, "Đã xóa thành công "
                        + player.friends.get(i).name + " khỏi danh sách bạn");
                Message msg;
                try {
                    msg = new Message(-80);
                    msg.writer().writeByte(REMOVE_FRIEND);
                    msg.writer().writeInt((int) player.friends.get(i).id);
                    player.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                    // Bỏ qua lỗi để tránh gián đoạn xử lý
                }
                player.friends.remove(i);
                break;
            }
        }
    }
    
    /**
     * Xóa người chơi khỏi danh sách kẻ thù.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param playerId ID của người chơi bị xóa khỏi danh sách kẻ thù.
     */
    private void removeEnemy(Player player, int playerId) {
        for (int i = 0; i < player.enemies.size(); i++) {
            if (player.enemies.get(i).id == playerId) {
                player.enemies.remove(i);
                break;
            }
        }
        openListEnemy(player);
    }
    
    /**
     * Gửi tin nhắn riêng tư từ một người chơi tới người chơi khác.
     * 
     * @param player Người chơi gửi tin nhắn.
     * @param msg Thông điệp chứa thông tin người nhận và nội dung tin nhắn.
     */
    public void chatPrivate(Player player, Message msg) {
        if (Util.canDoWithTime(player.iDMark.getLastTimeChatPrivate(), 5000)) {
            player.iDMark.setLastTimeChatPrivate(System.currentTimeMillis());
            try {
                int playerId = msg.reader().readInt();
                String text = msg.reader().readUTF();
                Player pl = Client.gI().getPlayer(playerId);
                if (pl != null) {
                    Service.gI().chatPrivate(player, pl, text);
                }
            } catch (Exception e) {
                // Bỏ qua lỗi để tránh gián đoạn xử lý
            }
        }
    }
    
    /**
     * Chấp nhận kết bạn với một người chơi khác.
     * 
     * @param player Người chơi chấp nhận kết bạn.
     * @param playerId ID của người chơi được thêm vào danh sách bạn.
     */
    public void acceptMakeFriend(Player player, int playerId) {
        Player pl = Client.gI().getPlayer(playerId);
        if (pl != null) {
            Friend friend = new Friend();
            friend.id = (int) pl.id;
            friend.name = pl.name;
            friend.power = pl.nPoint.power;
            friend.head = pl.getHead();
            friend.body = pl.getBody();
            friend.leg = pl.getLeg();
            friend.bag = (byte) pl.getFlagBag();
            player.friends.add(friend);
            Service.gI().sendThongBao(player, "Kết bạn thành công");
            Service.gI().chatPrivate(player, pl, player.name + " vừa mới kết bạn với " + pl.name);
            TaskService.gI().checkDoneTaskMakeFriend(player, pl);
        } else {
            Service.gI().sendThongBao(player, "Không tìm thấy hoặc đang Offline, vui lòng thử lại sau");
        }
    }
    
    /**
     * Dịch chuyển tới vị trí của người chơi khác bằng kỹ năng Yardrat.
     * 
     * @param player Người chơi thực hiện dịch chuyển.
     * @param msg Thông điệp chứa ID của người chơi đích.
     */
    public void goToPlayerWithYardrat(Player player, Message msg) {
        try {
            Player pl = Client.gI().getPlayer(msg.reader().readInt());
            if (pl != null) {
                if (player.isAdmin() || player.nPoint.teleport) {
                    if (!pl.itemTime.isUseAnDanh || player.isAdmin()) {
                        if ((player.isAdmin() || !pl.zone.isFullPlayer()) && !MapService.gI().isMapDoanhTrai(pl.zone.map.mapId) && !MapService.gI().isMapMaBu(pl.zone.map.mapId)) {
                            ChangeMapService.gI().changeMapYardrat(player, pl.zone, pl.location.x + Util.nextInt(-5, 5), pl.location.y);
                        } else {
                            Service.gI().sendThongBao(player, "Người chơi ở nơi không thể dịch chuyển đến !");
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi ở nơi không thể dịch chuyển đến !");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Yêu cầu trang bị có khả năng dịch chuyển tức thời");
                }
            }
        } catch (IOException ex) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }
    
    /**
     * Thêm người chơi khác vào danh sách kẻ thù.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param enemy Người chơi được thêm vào danh sách kẻ thù.
     */
    public void addEnemy(Player player, Player enemy) {
        boolean hadEnemy = false;
        for (Enemy ene : player.enemies) {
            if (ene.id == ene.id) {
                hadEnemy = true;
            }
        }
        if (!hadEnemy) {
            Enemy e = new Enemy();
            e.id = (int) enemy.id;
            e.name = enemy.name;
            e.power = enemy.nPoint.power;
            e.head = enemy.getHead();
            e.body = enemy.getBody();
            e.leg = enemy.getLeg();
            e.bag = (byte) enemy.getFlagBag();
            player.enemies.add(e);
        }
    }
}