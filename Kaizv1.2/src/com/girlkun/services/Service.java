package com.girlkun.services;

import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.database.GirlkunDB;
import com.girlkun.data.DataGame;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.Effect.EffectService;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.map.Zone;
import com.girlkun.models.matches.TOP;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ItemShop;
import com.girlkun.models.shop.Shop;
import com.girlkun.server.io.MySession;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.network.session.Session;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.Input;
import com.girlkun.utils.FileIO;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Lớp cung cấp các dịch vụ chính để xử lý logic game, bao gồm gửi tin nhắn mạng, quản lý vật phẩm, và xử lý tương tác người chơi.
 * @author Lucifer
 */
public class Service {

    private static Service instance;
    public long lasttimechatbanv = 0;
    public long lasttimechatmuav = 0;

    /**
     * Lấy instance của Service theo mẫu Singleton.
     *
     * @return Đối tượng Service.
     */
    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    /**
     * Lấy instance của Service theo mẫu Singleton.
     *
     * @return Đối tượng Service.
     */
    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    /**
     * Gửi danh hiệu cho người chơi và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần gửi danh hiệu.
     * @param part ID của danh hiệu.
     */
    public void sendTitle1(Player player, int part) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (player.titleitem) {
                me.writer().writeShort(part);
            }
            me.writer().writeShort(part);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi danh hiệu");
        }
    }

    /**
     * Gửi danh hiệu cho một người chơi cụ thể.
     *
     * @param player Người chơi cần gửi danh hiệu.
     * @param p2 Người chơi nhận thông báo danh hiệu.
     * @param part ID của danh hiệu.
     */
    public void sendTitleRv1(Player player, Player p2, int part) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (player.partDanhHieu >= 60) {
                me.writer().writeShort(part);
            }
            me.writer().writeShort(part);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi danh hiệu riêng");
        }
    }

    /**
     * Gửi danh hiệu dựa trên ID vật phẩm cho người chơi.
     *
     * @param player Người chơi cần gửi danh hiệu.
     * @param id ID của vật phẩm tương ứng với danh hiệu.
     */
    public void sendTitle(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1271:
                    me.writer().writeShort(60);
                    break;
                case 1272:
                    me.writer().writeShort(61);
                    break;
                case 1273:
                    me.writer().writeShort(62);
                    break;
                case 1266:
                    me.writer().writeShort(66);
                    break;
                case 1270:
                    me.writer().writeShort(73);
                    break;
                case 1274:
                    me.writer().writeShort(63);
                    break;
                case 1275:
                    me.writer().writeShort(64);
                    break;
                case 1276:
                    me.writer().writeShort(67);
                    break;
                case 1277:
                    me.writer().writeShort(65);
                    break;
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi danh hiệu theo ID vật phẩm");
        }
    }

    /**
     * Xóa dấu chân của người chơi và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần xóa dấu chân.
     */
    public void removeFoot(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(1);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi xóa dấu chân");
        }
    }

    /**
     * Xóa dấu chân của người chơi (phiên bản thứ hai) và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần xóa dấu chân.
     */
    public void removeFoot1(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(1);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi xóa dấu chân (phiên bản 2)");
        }
    }

    /**
     * Xóa danh hiệu của người chơi và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần xóa danh hiệu.
     */
    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi xóa danh hiệu");
        }
    }

    /**
     * Xóa danh hiệu của người chơi (phiên bản thứ hai) và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần xóa danh hiệu.
     */
    public void removeTitle1(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi xóa danh hiệu (phiên bản 2)");
        }
    }

    /**
     * Gửi dấu chân cho người chơi và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần gửi dấu chân.
     * @param id ID của vật phẩm tương ứng với dấu chân.
     */
    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1300:
                    me.writer().writeShort(74);
                    break;
                case 1301:
                    me.writer().writeShort(75);
                    break;
                case 1302:
                    me.writer().writeShort(76);
                    break;
                case 1303:
                    me.writer().writeShort(77);
                    break;
                case 1304:
                    me.writer().writeShort(78);
                    break;
                case 1305:
                    me.writer().writeShort(79);
                    break;
                case 1306:
                    me.writer().writeShort(80);
                    break;
                case 1307:
                    me.writer().writeShort(81);
                    break;
                case 1308:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi dấu chân");
        }
    }

    /**
     * Gửi dấu chân của người chơi đến một người chơi cụ thể.
     *
     * @param player Người chơi cần gửi dấu chân.
     * @param p2 Người chơi nhận thông báo dấu chân.
     * @param id ID của vật phẩm tương ứng với dấu chân.
     */
    public void sendFootRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1300:
                    me.writer().writeShort(74);
                    break;
                case 1301:
                    me.writer().writeShort(75);
                    break;
                case 1302:
                    me.writer().writeShort(76);
                    break;
                case 1303:
                    me.writer().writeShort(77);
                    break;
                case 1304:
                    me.writer().writeShort(78);
                    break;
                case 1305:
                    me.writer().writeShort(79);
                    break;
                case 1306:
                    me.writer().writeShort(80);
                    break;
                case 1307:
                    me.writer().writeShort(81);
                    break;
                case 1308:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi dấu chân riêng");
        }
    }

    /**
     * Gửi danh hiệu của người chơi đến một người chơi cụ thể.
     *
     * @param player Người chơi cần gửi danh hiệu.
     * @param p2 Người chơi nhận thông báo danh hiệu.
     * @param id ID của vật phẩm tương ứng với danh hiệu.
     */
    public void sendTitleRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1271:
                    me.writer().writeShort(60);
                    break;
                case 1272:
                    me.writer().writeShort(61);
                    break;
                case 1273:
                    me.writer().writeShort(62);
                    break;
                case 1266:
                    me.writer().writeShort(66);
                    break;
                case 1270:
                    me.writer().writeShort(73);
                    break;
                case 1274:
                    me.writer().writeShort(63);
                    break;
                case 1275:
                    me.writer().writeShort(64);
                    break;
                case 1276:
                    me.writer().writeShort(67);
                    break;
                case 1277:
                    me.writer().writeShort(65);
                    break;
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi danh hiệu riêng");
        }
    }

    /**
     * Cập nhật hiệu ứng hóa đá cho người chơi và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi cần cập nhật hiệu ứng.
     * @param typead Loại hiệu ứng thêm.
     * @param typeTar Loại mục tiêu.
     * @param type Loại hiệu ứng.
     */
    public void SendMsgUpdateHoaDa(Player player, byte typead, byte typeTar, byte type) {
        Message message;
        try {
            message = new Message(-124);
            message.writer().writeByte(typead);
            message.writer().writeByte(typeTar);
            message.writer().writeByte(type);
            message.writer().writeInt((int) player.id);
            sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi cập nhật hiệu ứng hóa đá");
        }
    }

    /**
     * Hiển thị danh sách top người chơi cho người chơi.
     *
     * @param player Người chơi nhận danh sách top.
     * @param tops Danh sách top người chơi.
     * @param isPVP Chế độ PVP (1: PVP, 0: khác).
     */
    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            if (tops != null) {
                msg.writer().writeByte(tops.size());
            }
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dame + "\n" + "HP: " + pl.nPoint.hpMax + "\n" + "KI: " + pl.nPoint.mpMax + "\n") : top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi hiển thị danh sách top");
        }
    }

    /**
     * Gửi tin nhắn đến tất cả người chơi trong bản đồ, ngoại trừ người chơi hiện tại.
     *
     * @param player Người chơi hiện tại.
     * @param msg Tin nhắn cần gửi.
     */
    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = new ArrayList<>(player.zone.getPlayers());
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        players.stream().filter((pl) -> (pl != null && !pl.equals(player))).forEachOrdered((pl) -> {
            pl.sendMessage(msg);
        });
        msg.cleanup();
    }

    /**
     * Gửi thông báo dạng pop-up nhiều dòng cho người chơi.
     *
     * @param pl Người chơi nhận thông báo.
     * @param tempID ID mẫu thông báo.
     * @param avt ID avatar.
     * @param text Nội dung thông báo.
     */
    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi pop-up nhiều dòng");
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Gửi thông tin về thú cưng đi theo người chơi và thông báo đến tất cả người chơi trong bản đồ.
     *
     * @param player Người chơi có thú cưng.
     * @param smallId ID của thú cưng.
     */
    public void sendPetFollow(Player player, short smallId) {
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) player.id);
            if (smallId == 0) {
                msg.writer().writeByte(0);
            } else {
                msg.writer().writeByte(1);
                msg.writer().writeShort(smallId);
                msg.writer().writeByte(1);
                int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
                int[] fr2 = new int[]{0, 1, 2, 3, 4, 5};
                if (smallId == 14420) {
                    msg.writer().writeByte(fr2.length);
                    for (int i = 0; i < fr2.length; i++) {
                        msg.writer().writeByte(fr2[i]);
                    }
                } else {
                    msg.writer().writeByte(fr.length);
                    for (int i = 0; i < fr.length; i++) {
                        msg.writer().writeByte(fr[i]);
                    }
                }
                if (smallId == 20210 || smallId == 20212 || smallId == 30674 || smallId == 30676 || smallId == 30678 || smallId == 25246 || smallId == 25248 || smallId == 28890) {
                    msg.writer().writeShort(32);
                    msg.writer().writeShort(32);
                } else if (smallId == 25246 || smallId == 25248) {
                    msg.writer().writeShort(40);
                    msg.writer().writeShort(45);
                } else if (smallId == 20214) {
                    msg.writer().writeShort(34);
                    msg.writer().writeShort(36);
                } else {
                    msg.writer().writeShort(smallId == 20210 ? 65 : 75);
                    msg.writer().writeShort(smallId == 20210 ? 65 : 75);
                }
            }
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi thông tin thú cưng đi theo");
        }
    }

    /**
     * Gửi thông tin thú cưng của người chơi khác đến người chơi hiện tại.
     *
     * @param me Người chơi nhận thông tin.
     * @param pl Người chơi có thú cưng.
     */
    public void sendPetFollowToMe(Player me, Player pl) {
        Item linhThu = pl.inventory.itemsBody.get(10);
        if (!linhThu.isNotNullItem()) {
            return;
        }
        short smallId = (short) (linhThu.template.iconID - 1);
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);
            int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }
            msg.writer().writeShort(smallId == 15067 ? 32 : 32);
            msg.writer().writeShort(smallId == 20212 ? 32 : 32);
            msg.writer().writeShort(smallId == 25246 ? 40 : 45);
            msg.writer().writeShort(smallId == 25248 ? 40 : 45);
            msg.writer().writeShort(smallId == 20214 ? 34 : 36);
            msg.writer().writeShort(smallId == 30674 ? 32 : 32);
            msg.writer().writeShort(smallId == 30678 ? 32 : 32);
            msg.writer().writeShort(smallId == 30678 ? 32 : 32);
            msg.writer().writeShort(smallId == 25246 ? 32 : 32);
            msg.writer().writeShort(smallId == 25248 ? 32 : 32);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "Lỗi khi gửi thông tin thú cưng đến người chơi khác");
        }
    }

    /**
     * Gửi tin nhắn đến tất cả người chơi trong server.
     *
     * @param msg Tin nhắn cần gửi.
     */
    public void sendMessAllPlayer(Message msg) {
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    /**
     * Gửi tin nhắn đến tất cả người chơi trong server, ngoại trừ người chơi hiện tại.
     *
     * @param player Người chơi hiện tại.
     * @param msg Tin nhắn cần gửi.
     */
    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    /**
     * Gửi tin nhắn đến tất cả người chơi trong bản đồ.
     *
     * @param zone Bản đồ cần gửi tin nhắn.
     * @param msg Tin nhắn cần gửi.
     */
    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        if (zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    /**
     * Gửi thông tin về số lượng ruby và ngọc cho người chơi.
     *
     * @param pl Người chơi nhận thông tin.
     */
    public void sendRuby(Player pl) {
        Message msg;
        try {
            msg = new Message(65);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.ruby);
            } else {
                msg.writer().writeInt((int) pl.inventory.ruby);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
     * Gửi tin nhắn đến tất cả người chơi trong bản đồ của người chơi hiện tại.
     * Nếu bản đồ là bản đồ offline, chỉ gửi tin nhắn đến người chơi hoặc chủ nhân của pet.
     *
     * @param player Người chơi hiện tại, xác định bản đồ để gửi tin nhắn.
     * @param msg Tin nhắn cần gửi.
     */
    public void sendMessAllPlayerInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
            if (player.isPet) {
                ((Pet) player).master.sendMessage(msg);
            } else {
                player.sendMessage(msg);
            }
        } else {
            List<Player> players = player.zone.getPlayers();
            if (players.isEmpty()) {
                msg.dispose();
                return;
            }
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                if (pl != null) {
                    pl.sendMessage(msg);
                }
            }
        }
        msg.cleanup();
    }
    
    /**
     * Xử lý đăng ký tài khoản người dùng từ thông tin nhận được qua tin nhắn.
     *
     * @param session Phiên kết nối của người dùng.
     * @param _msg Tin nhắn chứa thông tin đăng ký (tên tài khoản và mật khẩu).
     */
    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            String user = _msg.readUTF();
            String pass = _msg.readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where username = ?", user);
            if (rs.first()) {
                sendThongBaoOK((MySession) session, "Tài khoản đã tồn tại");
            } else {
                                pass = pass;              
                                GirlkunDB.executeUpdate("insert into account (username, password) values ()", user, pass);
                sendThongBaoOK((MySession) session, "Đăng ký tài khoản thành công !!");
            }
            rs.dispose();
        } catch (Exception e) {

        }
    }

//    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
//        if (player == null || player.zone == null) {
//            msg.dispose();
//            return;
//        }
//        List<Player> players = player.zone.getPlayers();
//        if (players.isEmpty()) {
//            msg.dispose();
//            return;
//        }
//        for (Player pl : players) {
//            if (pl != null && !pl.equals(player)) {
//                pl.sendMessage(msg);
//            }
//        }
//        msg.cleanup();
//
//    }
    
    /**
     * Gửi thông tin nhân vật (máu, ID, hiệu ứng) đến tất cả người chơi trong bản đồ, ngoại trừ người chơi hiện tại.
     *
     * @param pl Người chơi cần gửi thông tin.
     */
    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);//Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(0);//Hiệu ứng Ăn Đậu
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
     * Gửi thông tin về người chơi khi sử dụng đậu thần (máu, ID, hiệu ứng ăn đậu) đến tất cả người chơi trong bản đồ, ngoại trừ người chơi hiện tại.
     *
     * @param pl Người chơi sử dụng đậu thần.
     */
    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
     * Gửi thông báo thời gian chờ đăng nhập đến phiên kết nối của người dùng.
     *
     * @param session Phiên kết nối của người dùng.
     * @param second Thời gian chờ (tính bằng giây).
     */
    public void loginDe(MySession session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
     * Đặt lại vị trí của người chơi tại tọa độ (x, y) và gửi thông báo cập nhật vị trí.
     *
     * @param player Người chơi cần đặt lại vị trí.
     * @param x Tọa độ x mới.
     * @param y Tọa độ y mới.
     */
    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    /**
     * Xóa thông tin bản đồ hiện tại của người chơi bằng cách gửi tin nhắn xóa bản đồ.
     *
     * @param player Người chơi cần xóa thông tin bản đồ.
     */
    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
     * Chuyển giao diện client sang màn hình đăng ký tài khoản.
     *
     * @param session Phiên kết nối của người dùng.
     */    
    public void switchToRegisterScr(ISession session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }
    
    /**
     * Biến lưu trữ dữ liệu phần thưởng của quái vật.
     */
    public String DataMobReward = "";

    /**
    * Xử lý lệnh chat của người chơi, bao gồm các lệnh admin và lệnh điều khiển pet, đồng thời gửi tin nhắn chat đến tất cả người chơi trong bản đồ.
    *
    * @param player Người chơi gửi lệnh hoặc tin nhắn chat.
    * @param text Nội dung tin nhắn hoặc lệnh được gửi.
    */
    public void chat(Player player, String text) {
//        if (text.equals("a")) {
//            for (int i = 0; i < 5000; i++) {
//                new Thread(() -> {
//                    while (true) {
//                        try {
//                            Thread.sleep(1000);
//                            this.sendThongBao(player, "Time " + System.currentTimeMillis());
//                            System.out.println(player.getSession().getNumMessages());
//                        } catch (Exception e) {
//                        }
//                    }
//                }).start();
//            }
//            return;
//        }
//        if (text.equals("a")) {
//            BossManager.gI().loadBoss();
//            return;
//        }
//        if (text.startsWith("drop")) {
//            try {
//                Boss trung = new TrungUyTrang(1000, 60000, player.zone);
//                System.err.println("name: " + trung.name);
//                System.err.println("mapid: " + trung.zoneFinal.map.mapId);
//                
//            } catch (Exception ex) {
//                java.util.logging.Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
//            }
//  
//        }
        if (player.getSession() != null && player.isAdmin()) {
            if (text.contains("effect0")) {
                EffectService.AddEffecttoChar(player, 83, 0, -1, 10, 1);
                return;
            }
            if (text.contains("effect1")) {
                EffectService.AddEffecttoChar(player, 83, 1, -1, 10, 1);
                return;
            }
            if (text.contains("enddt")) {
                player.clan.doanhTrai = null;
                return;
            }
            if (text.equals("load")) {
                Manager.loadPart();
                DataGame.updateData(player.getSession());
                return;
            }
            if (text.equals("g")) {
                Service.getInstance().releaseCooldownSkill(player);
                return;
            }
            if (text.equals("hskill")) { // hồi all skill, Ki
                Service.getInstance().releaseCooldownSkill(player);
                return;
            }
            if (text.equals("mob")) {
                System.err.print(DataMobReward);
                return;

            }
            if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                return;
            }
            if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return;
            }
            if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                return;
            }
            if (text.equals("client")) {
                Client.gI().show(player);
            } else if (text.equals("map")) {
                sendThongBao(player, "Thông tin map: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                return;
            } else if (text.equals("vt")) {
                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
            } else if (text.equals("hs")) {
                player.nPoint.setFullHpMpDame();
                PlayerService.gI().sendInfoHpMp(player);
                sendThongBao(player, "Quyền năng trị liệu\n");
                return;
            } else if (text.equals("m")) {
                sendThongBao(player, "Map " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                return;
            } else if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
            } else if (text.equals("vt")) {
                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
            } else if (text.startsWith("ss")) {

//                Message msg;
//                try {
//                    msg = new Message(48);
//                    msg.writer().writeByte(Byte.parseByte(text.replaceAll("ss", "")));
//                    player.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//                try {
//                    msg = new Message(113);
//                    msg.writer().writeByte(111);
//                    msg.writer().writeByte(3);
//                    msg.writer().writeByte(Byte.parseByte(text.replaceAll("ss", "")));//id
//                    msg.writer().writeShort(player.location.x);
//                    msg.writer().writeShort(player.location.y);
//                    msg.writer().writeShort(1);
//                    player.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
            } else if (text.equals("a")) {

//                BossManager.gI().createBoss(BossID.ANDROID_13);
//                BossManager.gI().loadBoss();
//                Message msg;
//                try {
//                    msg = new Message(31);
//                    msg.writer().writeInt((int) player.id);
//                    msg.writer().writeByte(1);
//                    msg.writer().writeShort(7094);
//
////                    msg.writer().writeByte(4);
////                    int n = 3;
////                    msg.writer().writeByte(n);
////                    for (int i = 0; i < n; i++) {
////                        msg.writer().writeByte(i);
////                    }
////                    msg.writer().writeShort(70);
////                    msg.writer().writeShort(80);
//                    player.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//                try {
//                    msg = new Message(52);
//                    msg.writer().writeByte(1);
//                    msg.writer().writeInt((int) player.id);
//                    msg.writer().writeShort(player.location.x);
//                    msg.writer().writeShort(player.location.y-16);
//                    sendMessAllPlayerInMap(player, msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//                Message msg;
//                try {
//                    msg = new Message(50);
//                    msg.writer().writeByte(10);
//                    for (int i = 0; i < 10; i++) {
//                        System.out.println("ok");
//                        msg.writer().writeShort(i);
//                        msg.writer().writeUTF("main " + i);
//                        msg.writer().writeUTF("content " + i);
//                    }
//                    player.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//                Message msg;
//                try {
//                    msg = new Message(-96);
//                    msg.writer().writeByte(0);
//                    msg.writer().writeUTF("Girlkun test");
//                    msg.writer().writeByte(100);
//                    for(int i = 0; i < 100; i++){
//                        msg.writer().writeInt(i);
//                        msg.writer().writeInt(i);
//                        msg.writer().writeShort(player.getHead());
//                        msg.writer().writeShort(player.getBody());
//                        msg.writer().writeShort(player.getLeg());
//                        msg.writer().writeUTF("Test name " + i);
//                        msg.writer().writeUTF("Test info");
//                        msg.writer().writeUTF("info 2");
//                    }
//                    player.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
            } else if (text.equals("b")) {
                Message msg;
                try {
                    msg = new Message(52);
                    msg.writer().writeByte(0);
                    msg.writer().writeInt((int) player.id);
                    sendMessAllPlayerInMap(player, msg);
                    msg.cleanup();
                } catch (Exception e) {

                }
            } else if (text.equals("c")) {
                Message msg;
                try {
                    msg = new Message(52);
                    msg.writer().writeByte(2);
                    msg.writer().writeInt((int) player.id);
                    msg.writer().writeInt((int) player.zone.getHumanoids().get(1).id);
                    sendMessAllPlayerInMap(player, msg);
                    msg.cleanup();
                } catch (Exception e) {

                }
            }
//              else if (text.startsWith("sm_")) {
//                pl.point.power = Long.valueOf(text.replace("sm_", ""));
//                Service.gI().sendInfo;
//                return;
//            }
//            if (text.equals("nrnm")) {
//                Service.gI().activeNamecShenron(player);
//            }
            if (text.equals("ts")) {
                sendThongBaoFromAdmin(player, "Time start server: " + ServerManager.timeStart + "\n");
                return;
            }
            if (text.startsWith("it ")) {
                String[] itemRange = text.replace("it ", "").split(" ");

                if (itemRange.length == 2) {
                    int startItemId = Integer.parseInt(itemRange[0]);
                    int endItemId = Integer.parseInt(itemRange[1]);

                    for (int itemId = startItemId; itemId <= endItemId; itemId++) {
                        Item item = ItemService.gI().createNewItem((short) itemId);
                        ItemShop it = new Shop().getItemShop(itemId);

                        if (it != null && !it.options.isEmpty()) {
                            item.itemOptions.addAll(it.options);
                        }

                        InventoryServiceNew.gI().addItemBag(player, item);
                    }

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Đã lấy các món đồ từ kho đồ!");
                } else {
                    // Xử lý khi đầu vào không hợp lệ, ví dụ: "i 1112" hoặc "i 1112 1130 1150"
                }
            }
            if (text.startsWith("tb")) {
                String a = text.replace("tb ", "");
                Service.gI().sendThongBaoAllPlayer(a);
            }
            if (text.equals("admin")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1, "Menu Máy chủ:Nro Green 01" + "\n" + "Số người online: " + Client.gI().getPlayers().size() + "\n" + "Thread gốc: " + (Thread.activeCount() - ServerManager.gI().threadMap) + "\nDev src by:Hoàng Phúc",
                        "Ngọc rồng", "Đệ tử", "Bảo trì", "Tìm kiếm\nngười chơi", "xem\nBoss", "Send item", "Send item\noption", "Send item\nSKH", "GiffCode", "Đóng");
                return;

            }
            if (text.equals("dtu")) {
                PetService.gI().createNormalPet(player, (byte) 2);
                return;
            }
            if (text.equals("buffv2")) {
                Input.gI().createFormSenditem1(player);
                return;
            } else if (text.startsWith("upp")) {
                try {
                    long power = Long.parseLong(text.replaceAll("upp", ""));
                    addSMTN(player.pet, (byte) 2, power, false);
                    return;
                } catch (Exception e) {

                }

            } else if (text.startsWith("up")) {
                try {
                    long power = Long.parseLong(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                } catch (Exception e) {

                }

            } else if (text.startsWith("m")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m", ""));
                    ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
                    return;
                } catch (Exception e) {

                }
            } else if (text.startsWith("i")) {
                int itemId = Integer.parseInt(text.replace("i", ""));
                Item item = ItemService.gI().createNewItem(((short) itemId));
                ItemShop it = new Shop().getItemShop(itemId);
                if (it != null && !it.options.isEmpty()) {
                    item.itemOptions.addAll(it.options);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "GET " + item.template.name + " [" + item.template.id + "] SUCCESS !");

            } else if (text.startsWith("get ")) {
                int itemId = Integer.parseInt(text.replace("get ", ""));
                Item item = ItemService.gI().createNewItem(((short) itemId));
                ItemShop it = new Shop().getItemShop(itemId);
                if (it != null && !it.options.isEmpty()) {
                    item.itemOptions.addAll(it.options);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Đã lấy " + item.template.name + " [" + item.template.id + "]  ra từ kho đồ!");
            }
            if (text.startsWith("it ")) {
                String[] itemRange = text.replace("it ", "").split(" ");

                if (itemRange.length == 2) {
                    int startItemId = Integer.parseInt(itemRange[0]);
                    int endItemId = Integer.parseInt(itemRange[1]);

                    for (int itemId = startItemId; itemId <= endItemId; itemId++) {
                        Item item = ItemService.gI().createNewItem((short) itemId);
                        ItemShop it = new Shop().getItemShop(itemId);

                        if (it != null && !it.options.isEmpty()) {
                            item.itemOptions.addAll(it.options);
                        }

                        InventoryServiceNew.gI().addItemBag(player, item);
                    }

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Đã lấy các món đồ từ kho đồ!");
                } else {
                    // Xử lý khi đầu vào không hợp lệ, ví dụ: "i 1112" hoặc "i 1112 1130 1150"
                }
            }
            if (text.startsWith("getsl")) {
                String[] parts = text.split("");
                if (parts.length < 3) {
                    Service.gI().sendThongBao(player, "Vui lòng nhập số lượng!");
                    return;
                }
                int itemId = Integer.parseInt(parts[1]);
                long quantity = Long.parseLong(parts[2]);
                if (quantity > 2000000000) {
                    Service.gI().sendThongBao(player, "Không thể lấy số lượng vượt quá 2 tỷ!");
                    return;
                }
                Item item = ItemService.gI().createNewItem((short) itemId, (int) quantity);
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Đã lấy vật phẩm: " + item.template.name + " Số lượng: " + quantity + " ra từ kho đồ!");
            } else if (text.equals("buffv1")) {//???
                Input.gI().createFormGiveItem(player);
            } else if (text.equals("key")) {
                Input.gI().createFormSenditem1(player);
            } else if (text.equals("thread")) {
                sendThongBao(player, "Current thread: " + (Thread.activeCount() - ServerManager.gI().threadMap));
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                return;
            } else if (text.startsWith("s")) {
                try {
                    player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
                    point(player);
                    return;
                } catch (Exception e) {

                }

            } else if (text.startsWith("go ")) {
                try {
                    int mapId = Integer.parseInt(text.replace("go ", ""));
                    ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
                    sendThongBao(player, "" + player.name + " đã dịch chuyển tức thời đến: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                    return;
                } catch (Exception e) {

                }
            } else if (text.startsWith("get ")) {
                int itemId = Integer.parseInt(text.replace("get ", ""));
                Item item = ItemService.gI().createNewItem(((short) itemId));
                ItemShop it = new Shop().getItemShop(itemId);
                if (it != null && !it.options.isEmpty()) {
                    item.itemOptions.addAll(it.options);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Đã lấy " + item.template.name + " [" + item.template.id + "]  ra từ kho đồ!");
            } else if (text.startsWith("it ")) {
                String[] itemRange = text.replace("it ", "").split(" ");

                if (itemRange.length == 2) {
                    int startItemId = Integer.parseInt(itemRange[0]);
                    int endItemId = Integer.parseInt(itemRange[1]);

                    for (int itemId = startItemId; itemId <= endItemId; itemId++) {
                        Item item = ItemService.gI().createNewItem((short) itemId);
                        ItemShop it = new Shop().getItemShop(itemId);

                        if (it != null && !it.options.isEmpty()) {
                            item.itemOptions.addAll(it.options);
                        }

                        InventoryServiceNew.gI().addItemBag(player, item);
                    }

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Đã lấy các món đồ từ kho đồ!");
                } else {
                    // Xử lý khi đầu vào không hợp lệ, ví dụ: "i 1112" hoặc "i 1112 1130 1150"
                }
            } else if (text.startsWith("getsl ")) {
                String[] parts = text.split(" ");
                if (parts.length < 3) {
                    Service.getInstance().sendThongBao(player, "Vui lòng nhập số lượng!");
                    return;
                }
                int itemId = Integer.parseInt(parts[1]);
                long quantity = Long.parseLong(parts[2]);
                if (quantity > 2000000000) {
                    Service.getInstance().sendThongBao(player, "Không thể lấy số lượng vượt quá 2 tỷ!");
                    return;
                }
                Item item = ItemService.gI().createNewItem((short) itemId);
                item.quantity = (int) quantity;
                ItemShop itemShop = new Shop().getItemShop(itemId);
                if (itemShop != null && !itemShop.options.isEmpty()) {
                    item.itemOptions.addAll(itemShop.options);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Đã lấy vật phẩm: " + item.template.name + " Số lượng: " + quantity + " ra từ kho đồ!");
            } else if (text.equals("keyz")) {//???
                Input.gI().createFormGiveItem(player);
            } else if (text.equals("key")) {
                Input.gI().createFormSenditem1(player);
            } else if (text.equals("thread")) {
                sendThongBao(player, "Current thread: " + (Thread.activeCount() - ServerManager.gI().threadMap));
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                return;
            } else if (text.startsWith("s")) {
                try {
                    player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
                    point(player);
                    return;
                } catch (Exception e) {

                }
            }
        }

        if (text.equals("banv")) {

            long now = System.currentTimeMillis();
            if (now >= lasttimechatbanv + 10000) {
                if (player.muav == false) {
                    if (player.banv == false) {
                        player.banv = true;
                        Service.getInstance().sendThongBao(player, "Đã bật tự động bán vàng khi vàng dưới 1 tỷ !");
                        lasttimechatbanv = System.currentTimeMillis();
                        Logger.success("Thằng " + player.name + " chat banv\n");
                        return;
                    } else if (player.banv == true) {
                        player.banv = false;
                        Service.getInstance().sendThongBao(player, "Đã tắt tự động bán vàng khi vàng dưới 1 tỷ !");
                        lasttimechatbanv = System.currentTimeMillis();
                        Logger.success("Thằng " + player.name + " chat banv\n");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Vui lòng tắt mua vàng !");
                    lasttimechatbanv = System.currentTimeMillis();
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Spam chat con mọe m !");
                return;
            }
        }

//        if (text.equals("muav")) {
//
//            long now = System.currentTimeMillis();
//            if (now >= lasttimechatmuav + 10000) {
//                if (player.banv == false) {
//                    if (player.muav == false) {
//                        player.muav = true;
//                        Service.getInstance().sendThongBao(player, "Đã bật tự động mua vàng khi vàng trên 500tr !");
//                        lasttimechatmuav = System.currentTimeMillis();
//                        Logger.success("Thằng " + player.name + " chat muav\n");
//                        return;
//                    } else if (player.muav == true) {
//                        player.muav = false;
//                        Service.getInstance().sendThongBao(player, "Đã tắt tự động mua vàng khi vàng trên 500tr !");
//                        lasttimechatmuav = System.currentTimeMillis();
//                        Logger.success("Thằng " + player.name + " chat muav\n");
//                        return;
//                    }
//                } else {
//                    Service.getInstance().sendThongBao(player, "Vui lòng tắt ban vàng !");
//                    lasttimechatmuav = System.currentTimeMillis();
//                    return;
//                }
//            } else {
//                Service.getInstance().sendThongBao(player, "Spam chat con mọe m !");
//                return;
//            }
//        }
        if (text.equals("boss")) {
            BossManager.gI().showListBoss(player);
            return;

        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
//        } else if (text.equals("mabu")) {
//            sendThongBao(player, "Khởi Tạo Mabu Thành Công: " + (player.mabuEgg != null));
//            MabuEgg.createMabuEgg(player);
//        } else if (text.equals("freakyex")) {
//            System.exit(0);
//        } else if (text.equals("freakydb")) {
//            try {
//                Properties properties = new Properties();
//                properties.load(new FileInputStream("data/girlkun/girlkun.properties"));
//                String str = "";
//                Object value = null;
//                if ((value = properties.get("server.girlkun.db.ip")) != null) {
//                    str += String.valueOf(value) + "\n";
//                }
//                if ((value = properties.get("server.girlkun.db.port")) != null) {
//                    str += Integer.parseInt(String.valueOf(value)) + "\n";
//                }
//                if ((value = properties.get("server.girlkun.db.name")) != null) {
//                    str += String.valueOf(value) + "\n";
//                }
//                if ((value = properties.get("server.girlkun.db.us")) != null) {
//                    str += String.valueOf(value) + "\n";
//                }
//                if ((value = properties.get("server.girlkun.db.pw")) != null) {
//                    str += String.valueOf(value);
//                }
//                Service.gI().sendThongBao(player, str);
//                return;
//            } catch (Exception e) {
//            }
//        }
//        if (text.equals("fixapk")) {
//            Service.gI().player(player);
//            Service.gI().Send_Caitrang(player);
        }

        if (player.pet != null) {
            if (text.equals("di theo") || text.equals("follow")) {
                player.pet.changeStatus(Pet.FOLLOW);
            } else if (text.equals("bao ve") || text.equals("protect")) {
                player.pet.changeStatus(Pet.PROTECT);
            } else if (text.equals("tan cong") || text.equals("attack")) {
                player.pet.changeStatus(Pet.ATTACK);
            } else if (text.equals("ve nha") || text.equals("go home")) {
                player.pet.changeStatus(Pet.GOHOME);
            } else if (text.equals("bien hinh")) {
                player.pet.transform();
            }
            //  }
            //   if (text.equals("bill")) {
            //      sendThongBao(player, "Khởi Tạo Bill Thành Công: " + (player.billEgg != null));
            //      BillEgg.createBillEgg(player);
        }
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(this.getClass(), e);
        }
    }

    
    /**
    * Gửi một tin nhắn riêng tư từ một người chơi đến một người chơi khác, sử dụng mã lệnh 44.
    * Phương thức này được sử dụng để hỗ trợ giao tiếp riêng tư giữa các người chơi trong game,
    * có thể được kích hoạt bởi một sự kiện với ID 1111.
    *
    * @param me     người chơi nhận tin nhắn
    * @param plChat người chơi gửi tin nhắn
    * @param text   nội dung của tin nhắn
    * @throws IllegalArgumentException nếu bất kỳ tham số nào (me, plChat, hoặc text) là null
    * @throws IOException             nếu xảy ra lỗi I/O khi ghi dữ liệu tin nhắn
    */
    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private boolean isSave;

    /**
    * Tự động lưu dữ liệu của tất cả người chơi trong hệ thống game.
    * Phương thức này được sử dụng để lưu thông tin người chơi vào cơ sở dữ liệu,
    * có thể được kích hoạt như một phần của sự kiện với ID 1111. 
    * Quá trình lưu được thực hiện định kỳ và chỉ chạy khi không có tiến trình lưu nào khác đang hoạt động.
    *
    * @throws RuntimeException nếu có lỗi xảy ra trong quá trình lưu hoặc khi bị gián đoạn
    */
    public void AutoSavePlayerData() {
        if (isSave) {
            return;
        }
        isSave = true;
        try {
            System.gc();
            Runtime.getRuntime().freeMemory();
            Player player = null;
            for (int i = 0; i < Client.gI().getPlayers().size(); ++i) {
                try {
                    if (Client.gI().getPlayers().get(i) != null) {
                        player = (Client.gI().getPlayers().get(i));
                        PlayerDAO.updatePlayer(player);
                    }
                } catch (Exception e) {
                }
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        isSave = false;
    }

    /**
     * Gửi thông tin dịch chuyển cho người chơi, sử dụng mã lệnh -105.
     * Phương thức này truyền dữ liệu về thời gian tối đa và loại dịch chuyển của người chơi,
     * có thể được kích hoạt trong ngữ cảnh sự kiện với ID 1111.
     *
     * @param pl người chơi nhận thông tin dịch chuyển
     * @throws IllegalArgumentException nếu tham số pl là null
     * @throws IOException             nếu xảy ra lỗi I/O khi ghi dữ liệu tin nhắn
     */
    public void Transport(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    /**
    * Xác định mức kinh nghiệm cần thiết để đạt cấp độ tiếp theo dựa trên sức mạnh của người chơi.
    * Phương thức trả về giá trị kinh nghiệm cố định tương ứng với khoảng sức mạnh,
    * có thể liên quan đến sự kiện hoặc cơ chế với ID 1111.
    *
    * @param sucmanh sức mạnh hiện tại của người chơi
    * @return mức kinh nghiệm cần thiết để lên cấp tiếp theo
    */
    public long exp_level1(long sucmanh) {
        if (sucmanh < 3000) {
            return 3000;
        } else if (sucmanh < 15000) {
            return 15000;
        } else if (sucmanh < 40000) {
            return 40000;
        } else if (sucmanh < 90000) {
            return 90000;
        } else if (sucmanh < 170000) {
            return 170000;
        } else if (sucmanh < 340000) {
            return 340000;
        } else if (sucmanh < 700000) {
            return 700000;
        } else if (sucmanh < 1500000) {
            return 1500000;
        } else if (sucmanh < 15000000) {
            return 15000000;
        } else if (sucmanh < 150000000) {
            return 150000000;
        } else if (sucmanh < 1500000000) {
            return 1500000000;
        } else if (sucmanh < 5000000000L) {
            return 5000000000L;
        } else if (sucmanh < 10000000000L) {
            return 10000000000L;
        } else if (sucmanh < 40000000000L) {
            return 40000000000L;
        } else if (sucmanh < 50010000000L) {
            return 50010000000L;
        } else if (sucmanh < 60010000000L) {
            return 60010000000L;
        } else if (sucmanh < 70010000000L) {
            return 70010000000L;
        } else if (sucmanh < 80010000000L) {
            return 80010000000L;
        } else if (sucmanh < 200010000000L) {
            return 200010000000L;
        }
        return 1000;
    }

    /**
    * Cập nhật và gửi thông tin chỉ số của người chơi qua tin nhắn với mã lệnh -42.
    * Phương thức này tính toán các chỉ số của người chơi (như HP, MP, sát thương, phòng thủ, v.v.)
    * và gửi chúng đến client, có thể liên quan đến sự kiện hoặc cơ chế với ID 1111.
    * Chỉ áp dụng cho người chơi chính, không áp dụng cho thú cưng, boss hoặc thú cưng mới.
    *
    * @param player người chơi cần cập nhật và gửi thông tin chỉ số
    * @throws IOException nếu xảy ra lỗi I/O khi ghi dữ liệu tin nhắn
    */
    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(player.nPoint.hpg);
                msg.writer().writeInt(player.nPoint.mpg);
                msg.writer().writeInt(player.nPoint.dameg);
                msg.writer().writeInt(player.nPoint.hpMax);// hp full
                msg.writer().writeInt(player.nPoint.mpMax);// mp full
                msg.writer().writeInt(player.nPoint.hp);// hp
                msg.writer().writeInt(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(player.nPoint.dame);// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    /**
    * Gửi thông điệp xuất hiện Shenron tại bản đồ nơi người chơi đang đứng.
    * Thông điệp chứa thông tin bản đồ, zone, ID người chơi và tọa độ,
    * sau đó được gửi đến tất cả người chơi trong cùng bản đồ.
    *
    * @param pl Người chơi kích hoạt Shenron, dùng để lấy thông tin map, zone và vị trí.
    */
    private void activeNamecShenron(Player pl) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(0);

            msg.writer().writeShort(pl.zone.map.mapId);
            msg.writer().writeShort(pl.zone.map.bgId);
            msg.writer().writeByte(pl.zone.zoneId);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeUTF("");
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            msg.writer().writeByte(1);
            //   lastTimeShenronWait = System.currentTimeMillis();
            //   isShenronAppear = true;

            Service.gI().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {

        }
    }

    /**
    * Gửi toàn bộ thông tin của người chơi (Player) về client.
    * 
    * Thông điệp bao gồm:
    * - ID, giới tính, tên, nhiệm vụ chính, sức mạnh
    * - Kỹ năng hiện có
    * - Vàng, ngọc, ngọc khóa
    * - Danh sách item trong body, bag, box
    * - Avatar, hiệu ứng, trạng thái nhập thể, cờ thành viên mới
    *
    * @param pl Người chơi cần gửi thông tin. Nếu null thì không làm gì.
    */
    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); //cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            //---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            //--------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            //-----------------
            DataGame.sendHeadAvatar(msg);
            //-----------------
            msg.writer().writeShort(514); //char info id - con chim thông báo
            msg.writer().writeShort(515); //char info id
            msg.writer().writeShort(537); //char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); //nhập thể
//            msg.writer().writeInt(1632811835); //deltatime
            msg.writer().writeInt(333); //deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); //is new member

            msg.writer().writeShort(pl.getAura()); //idauraeff
         //   msg.writer().writeByte(pl.getEffFront());

            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Tạo {@link Message} dành cho client chưa đăng nhập.
    * Thông điệp có mã -29 và ghi thêm lệnh con được truyền vào.
    *
    * @param command mã lệnh con cần gửi
    * @return đối tượng {@link Message} đã khởi tạo
    * @throws IOException nếu có lỗi khi ghi dữ liệu vào message
    */
    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    /**
    * Tạo {@link Message} dành cho client chưa vào bản đồ.
    * Thông điệp có mã -28 và ghi thêm lệnh con được truyền vào.
    *
    * @param command mã lệnh con cần gửi
    * @return đối tượng {@link Message} đã khởi tạo
    * @throws IOException nếu có lỗi khi ghi dữ liệu vào message
    */
    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    /**
     * Tạo {@link Message} dạng sub-command.
     * Thông điệp có mã -30 và ghi thêm lệnh con được truyền vào.
     *
     * @param command mã lệnh con cần gửi
     * @return đối tượng {@link Message} đã khởi tạo
     * @throws IOException nếu có lỗi khi ghi dữ liệu vào message
     */
    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    /**
     * Cộng thêm sức mạnh hoặc tiềm năng cho người chơi hoặc thú cưng. 
     * Nếu là pet thì chỉ số cũng được chuyển một phần cho chủ nhân. 
     * Nếu là người chơi thì cách cộng phụ thuộc vào type, đồng thời có thể cộng cho clan nếu isOri = true.
     *
     * @param player người chơi hoặc pet được cộng chỉ số
     * @param type loại cộng chỉ số (1 = TN, 2 = SM + TN, khác = SM)
     * @param param giá trị chỉ số cần cộng
     * @param isOri true nếu là giá trị gốc, dùng để cộng thêm cho clan
     */
    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;
            param = master.nPoint.calSubTNSM(param);
            master.nPoint.powerUp(param);
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    //    public void congTiemNang(Player pl, byte type, int tiemnang) {
//        Message msg;
//        try {
//            msg = new Message(-3);
//            msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
//            msg.writer().writeInt(tiemnang);// số tn cần cộng
//            if (!pl.isPet) {
//                pl.sendMessage(msg);
//            } else {
//                ((Pet) pl).master.nPoint.powerUp(tiemnang);
//                ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
//                ((Pet) pl).master.sendMessage(msg);
//            }
//            msg.cleanup();
//            switch (type) {
//                case 1:
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                case 2:
//                    pl.nPoint.powerUp(tiemnang);
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                default:
//                    pl.nPoint.powerUp(tiemnang);
//                    break;
//            }
//        } catch (Exception e) {
//
//        }
//    }
    
    /**
     * Trả về tên hành tinh dựa theo mã số.
     * 0 = Trái Đất, 1 = Namếc, 2 = Xayda, mặc định trả về chuỗi rỗng.
     *
     * @param hanhtinh mã số hành tinh
     * @return tên hành tinh tương ứng hoặc chuỗi rỗng nếu không hợp lệ
     */
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    /**
     * Xác định và trả về cấp độ hiện tại của người chơi dựa trên sức mạnh.
     * Mỗi khoảng sức mạnh sẽ ứng với một danh hiệu hoặc cấp bậc khác nhau.
     *
     * @param pl người chơi cần kiểm tra cấp độ
     * @return tên cấp độ tương ứng với sức mạnh hiện tại của người chơi
     */
    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 150010000000L) {
            return "Vương Diệt Thần";
        } else if (sucmanh < 21100010000000L) {
            return "Hạn Diệt Thần";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    /**
    * Xác định và trả về cấp số hiện tại của người chơi dựa trên sức mạnh.
    * Mỗi khoảng sức mạnh ứng với một cấp độ khác nhau, biểu diễn bằng số nguyên.
    *
    * @param pl người chơi cần kiểm tra cấp độ
    * @return số cấp độ tương ứng với sức mạnh của người chơi, mặc định trả về 21 nếu vượt ngưỡng
    */
    public int getCurrLevel(Player pl) {
        if (pl != null && pl.nPoint != null) {
            long sucmanh = pl.nPoint.power;
            if (sucmanh < 3000) {
                return 1;
            } else if (sucmanh < 15000) {
                return 2;
            } else if (sucmanh < 40000) {
                return 3;
            } else if (sucmanh < 90000) {
                return 4;
            } else if (sucmanh < 170000) {
                return 5;
            } else if (sucmanh < 340000) {
                return 6;
            } else if (sucmanh < 700000) {
                return 7;
            } else if (sucmanh < 1500000) {
                return 8;
            } else if (sucmanh < 15000000) {
                return 9;
            } else if (sucmanh < 150000000) {
                return 10;
            } else if (sucmanh < 1500000000) {
                return 11;
            } else if (sucmanh < 5000000000L) {
                return 12;
            } else if (sucmanh < 10000000000L) {
                return 13;
            } else if (sucmanh < 40000000000L) {
                return 14;
            } else if (sucmanh < 50010000000L) {
                return 15;
            } else if (sucmanh < 60010000000L) {
                return 16;
            } else if (sucmanh < 70010000000L) {
                return 17;
            } else if (sucmanh < 80010000000L) {
                return 18;
            } else if (sucmanh < 100010000000L) {
                return 19;
            } else if (sucmanh < 150010000000L) {
                return 20;
            } else if (sucmanh < 2100010000000L) {
                return 21;
            }
        }
        return 21;
    }

    /**
    * Hồi sinh hoặc khôi phục nhân vật với lượng HP và MP được chỉ định,
    * đồng thời gửi thông báo cập nhật đến chính người chơi và toàn bộ người chơi trong bản đồ.
    *
    * @param pl nhân vật cần hồi sinh hoặc khôi phục
    * @param hp lượng máu cần đặt cho nhân vật
    * @param mp lượng mana cần đặt cho nhân vật
    */
    public void hsChar(Player pl, int hp, int mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt((hp));
            msg.writer().writeInt((mp));
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Xử lý sự kiện khi nhân vật chết. 
    * Gửi thông báo chết đến client và các người chơi khác trong bản đồ, 
    * đồng thời thực hiện một số hành động đặc biệt như ghi lại thời gian chết của pet 
    * hoặc dịch chuyển nhân vật khỏi bản đồ đặc thù.
    *
    * @param pl nhân vật vừa chết
    */
    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else if (pl.isPet) {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }
//            if (!pl.isPet && !pl.isBoss && pl.idNRNM != -1) {
//                ItemMap itemMap = new ItemMap(pl.zone, pl.idNRNM, 1, pl.location.x, pl.location.y, -1);
//                Service.gI().dropItemMap(pl.zone, itemMap);
//                NgocRongNamecService.gI().pNrNamec[pl.idNRNM - 353] = "";
//                NgocRongNamecService.gI().idpNrNamec[pl.idNRNM - 353] = -1;
//                pl.idNRNM = -1;
//                PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.NON_PK);
//                Service.gI().sendFlagBag(pl);
//            }
            if (pl.zone.map.mapId == 51) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 21 + pl.gender, 0, -1);
            }
            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); //cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();

//            Send_Info_NV(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Thực hiện hành động tấn công quái (Mob) trong cùng bản đồ theo ID quái. 
    * Nếu tìm thấy quái có ID trùng khớp, người chơi sẽ dùng kỹ năng hiện tại để tấn công nó.
    *
    * @param pl người chơi thực hiện tấn công
    * @param mobId ID của quái cần tấn công
    */
    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    /**
    * Gửi thông tin cài trang (ngoại hình) của người chơi đến tất cả người chơi trong bản đồ. 
    * Thông điệp bao gồm ID nhân vật, head, body, leg và trạng thái biến khỉ.
    *
    * @param player người chơi cần gửi thông tin cài trang
    */
    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); //id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);//set head
                msg.writer().writeShort(body);//setbody
                msg.writer().writeShort(leg);//set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);//set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    /**
    * Gửi thông báo hủy trạng thái biến khỉ của người chơi 
    * đến tất cả người chơi khác trong cùng bản đồ.
    *
    * @param player người chơi cần xóa trạng thái biến khỉ
    */
    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Gửi thông tin cờ trên túi của người chơi đến tất cả người chơi trong cùng bản đồ.
    *
    * @param pl người chơi cần gửi trạng thái cờ túi
    */
    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông báo dạng hộp thoại OK đến người chơi. 
    * Nếu đối tượng là thú cưng thì không gửi thông báo.
    *
    * @param pl   người chơi nhận thông báo
    * @param text nội dung thông báo
    */
    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isNewPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Gửi thông báo dạng hộp thoại OK trực tiếp đến một phiên làm việc (session).
    *
    * @param session phiên làm việc của người chơi nhận thông báo
    * @param text    nội dung thông báo
    */
    public void sendThongBaoOK(MySession session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông báo chung đến tất cả người chơi trong game.
    *
    * @param thongBao nội dung thông báo cần gửi
    */
    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi bảng thông báo đặc biệt (VIP) đến tất cả người chơi trong game.
    *
    * @param thongBao nội dung thông báo cần gửi
    */
    public void sendBangThongBaoAllPlayervip(String thongBao) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông báo dạng "Big Message" đến một người chơi, 
    * kèm theo biểu tượng minh họa và nội dung văn bản.
    *
    * @param player người chơi nhận thông báo
    * @param iconId ID biểu tượng hiển thị trong thông báo
    * @param text   nội dung thông báo
    */
    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

        }
    }

    /**
    * Gửi thông báo từ Admin đến một người chơi dưới dạng Big Message,
    * sử dụng biểu tượng mặc định của Admin.
    *
    * @param player người chơi nhận thông báo
    * @param text   nội dung thông báo từ Admin
    */
    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 11061, text);
    }

    /**
    * Gửi thông báo dạng hộp thoại thường đến một người chơi.
    *
    * @param pl       người chơi nhận thông báo
    * @param thongBao nội dung thông báo cần gửi
    */
    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông báo hiển thị bên dưới màn hình đến tất cả người chơi trong game.
    *
    * @param text nội dung thông báo cần gửi
    */
    public void sendThongBaoBenDuoi(String text) {
        Message msg = null;
        try {
            msg = new Message(93);
            msg.writer().writeUTF(text);
            sendMessAllPlayer(msg);
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    /**
    * Gửi thông báo đến một danh sách người chơi.
    *
    * @param pl       danh sách người chơi nhận thông báo
    * @param thongBao nội dung thông báo cần gửi
    */
    public void sendThongBao(List<Player> pl, String thongBao) {
        for (int i = 0; i < pl.size(); i++) {
            Player ply = pl.get(i);
            if (ply != null) {
                this.sendThongBao(ply, thongBao);
            }
        }
    }

    /**
    * Gửi thông tin tiền tệ của người chơi (vàng, ngọc, hồng ngọc) về client. 
    * Dữ liệu vàng được ghi theo kiểu long nếu phiên bản client >= 214, ngược lại dùng int.
    *
    * @param pl người chơi cần gửi thông tin tiền tệ
    */
    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông báo đến tất cả người chơi khác trong map (ngoại trừ chính người chơi)
    * rằng người chơi này đã nhặt một vật phẩm trên bản đồ.
    *
    * @param player   người chơi đã nhặt vật phẩm
    * @param itemMapId id của vật phẩm trên bản đồ bị nhặt
    */
    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerIgnoreMe(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }
    /** 
     * Danh sách tempId của các loại cờ (flag). 
     * Mỗi giá trị đại diện cho một loại cờ trong game. 
     */
    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    
    /** 
     * Danh sách iconId tương ứng với {@link #flagTempId}. 
     * Chỉ số trong mảng khớp với vị trí trong flagTempId. 
     */
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    
    /**
     * Mở giao diện chọn cờ cho người chơi.
     * Gửi gói tin (-103) chứa danh sách các loại cờ có thể chọn.
     * 
     * Với flagTempId = 363: yêu cầu level 73, giá 0.
     * Với flagTempId = 371: yêu cầu level 88, giá 10.
     * Các loại cờ khác: yêu cầu level 88, giá 5.
     *
     * @param pl Người chơi cần mở giao diện cờ
     */
    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Thay đổi cờ của người chơi và gửi thông báo đến tất cả người chơi trong bản đồ.
    * Cập nhật cả cờ cho thú cưng nếu có.
    *
    * Gửi gói tin (-103) với loại 1: thông báo người chơi hoặc thú cưng thay đổi cờ.
    * Gửi gói tin (-103) với loại 2: gửi icon cờ tương ứng cho tất cả người chơi trong map.
    * Sau khi đổi cờ, lưu lại thời điểm thay đổi gần nhất.
    *
    * @param pl    người chơi thay đổi cờ
    * @param index chỉ số cờ trong mảng flagIconId và flagTempId
    */
    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.iDMark.setLastTimeChangeFlag(System.currentTimeMillis());
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Gửi thông tin cờ của một người chơi khác đến chính người chơi hiện tại.
    * Dùng để hiển thị icon cờ của đối tượng trong bản đồ.
    *
    * Gói tin (-103) với loại 2 được gửi, chứa chỉ số cờ và icon tương ứng.
    *
    * @param me người chơi nhận thông tin cờ
    * @param pl người chơi có cờ cần gửi
    */
    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Xử lý yêu cầu chọn cờ của người chơi.
    * Người chơi không thể đổi cờ nếu đang ở map Black Ball War, Ma Bư hoặc PVP.
    * Kiểm tra thời gian kể từ lần đổi cờ gần nhất, yêu cầu tối thiểu 60 giây.
    * Nếu thỏa điều kiện, tiến hành đổi cờ; ngược lại thông báo thời gian chờ còn lại.
    *
    * @param pl    người chơi muốn chọn cờ
    * @param index chỉ số cờ cần chọn
    */
    public void chooseFlag(Player pl, int index) {
        if (MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) || MapService.gI().isMapMaBu(pl.zone.map.mapId) || MapService.gI().isMapPVP(pl.zone.map.mapId)) {
            sendThongBao(pl, "Không thể đổi cờ lúc này!");
            return;
        }
        if (Util.canDoWithTime(pl.iDMark.getLastTimeChangeFlag(), 60000)) {
            changeFlag(pl, index);
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.iDMark.getLastTimeChangeFlag(), 60) + " nữa!");
        }
    }

    /**
    * Thực hiện hành động tấn công một người chơi khác trong cùng bản đồ.
    *
    * @param pl         người chơi thực hiện tấn công
    * @param idPlAnPem  ID của người chơi mục tiêu trong bản đồ
    */
    public void attackPlayer(Player pl, int idPlAnPem) {
        if (pl.zone != null) {
            SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
        }
    }

    /**
    * Xóa toàn bộ thời gian hồi chiêu của kỹ năng cho người chơi,
    * đặt lại MP về tối đa và gửi thông tin HP/MP/Vàng cho client.
    *
    * @param pl người chơi được xóa hồi chiêu kỹ năng
    */
    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông tin thời gian hồi chiêu còn lại của tất cả kỹ năng
    * của người chơi về client.
    *
    * @param pl người chơi cần gửi thông tin kỹ năng
    */
    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }
    
    /**
     * Thả vật phẩm xuống bản đồ và thông báo cho tất cả người chơi trong khu vực.
     *
     * @param zone khu vực (map) nơi vật phẩm được thả
     * @param item đối tượng ItemMap đại diện cho vật phẩm trên bản đồ
     */
    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông tin thả vật phẩm trên bản đồ chỉ đến một người chơi cụ thể.
    *
    * @param player người chơi nhận thông báo
    * @param item   đối tượng ItemMap đại diện cho vật phẩm trên bản đồ
    */
    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Hiển thị đầy đủ thông tin thú cưng (Pet) của người chơi cho chính người đó.
    * Thông tin bao gồm trang bị, chỉ số sức mạnh, tiềm năng, kỹ năng và trạng thái.
    *
    * @param pl người chơi sở hữu thú cưng cần hiển thị thông tin
    */
    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                msg.writer().writeInt(pl.pet.nPoint.hp); //hp
                msg.writer().writeInt(pl.pet.nPoint.hpMax); //hpfull
                msg.writer().writeInt(pl.pet.nPoint.mp); //mp
                msg.writer().writeInt(pl.pet.nPoint.mpMax); //mpfull
                msg.writer().writeInt(pl.pet.nPoint.dame); //damefull
                msg.writer().writeUTF(pl.pet.name); //name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); //curr level
                msg.writer().writeLong(pl.pet.nPoint.power); //power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); //tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); //status
                msg.writer().writeShort(pl.pet.nPoint.stamina); //stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); //stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); //crit
                msg.writer().writeShort(pl.pet.nPoint.def); //def
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(4); //counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 1tỷ5 để mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ\nđể mở");
                                break;
//                            default:
//                                msg.writer().writeShort(-1);
//                                msg.writer().writeUTF("Cần đạt sức mạnh 60tỷ\nđể mở");
//                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    /**
    * Gửi thông tin tốc độ di chuyển hiện tại của người chơi về client.
    *
    * @param pl     người chơi cần gửi thông tin tốc độ
    * @param speed  giá trị tốc độ tùy chỉnh, nếu là -1 thì lấy tốc độ gốc từ nPoint của người chơi
    */
    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Đặt lại vị trí của người chơi trong bản đồ và gửi thông báo đến tất cả người chơi khác trong cùng bản đồ.
    *
    * @param player người chơi cần thay đổi vị trí
    * @param x      tọa độ X mới
    * @param y      tọa độ Y mới
    */
    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Hiển thị menu thông tin cơ bản của một người chơi trong bản đồ
    * (gồm ID, sức mạnh, cấp độ) cho người chơi đang thao tác.
    * Nếu người chơi là Admin thì hiển thị thêm menu quản trị.
    *
    * @param player   người chơi đang mở menu
    * @param playerId ID của người chơi mục tiêu trong bản đồ
    */
    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.gI().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Ẩn hộp thoại chờ (waiting dialog) trên giao diện của người chơi.
    *
    * @param pl người chơi cần ẩn hộp thoại chờ
    */
    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi tin nhắn chat riêng giữa hai người chơi.
    * Tin nhắn sẽ được hiển thị cho cả người gửi và người nhận.
    *
    * @param plChat    người chơi gửi tin nhắn
    * @param plReceive người chơi nhận tin nhắn
    * @param text      nội dung tin nhắn
    */
    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|7|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(-1);
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); //bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Thay đổi mật khẩu tài khoản của người chơi.
    *
    * Quy trình thực hiện:
    * - Kiểm tra mật khẩu cũ có đúng không.
    * - Kiểm tra mật khẩu mới có độ dài tối thiểu 5 ký tự.
    * - So sánh mật khẩu mới với mật khẩu nhập lại để xác nhận.
    * - Nếu hợp lệ thì cập nhật mật khẩu vào cơ sở dữ liệu và session.
    *
    * @param player   người chơi yêu cầu đổi mật khẩu
    * @param oldPass  mật khẩu cũ
    * @param newPass  mật khẩu mới
    * @param rePass   mật khẩu nhập lại để xác nhận
    */
    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 5) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    try {
                        GirlkunDB.executeUpdate("update account set password = ? where id = ? and username = ?",
                                rePass, player.getSession().userId, player.getSession().uu);
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thành công!");
                    } catch (Exception ex) {
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thất bại!");
                        Logger.logException(Service.class, ex);
                    }
                } else {
                    Service.gI().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.gI().sendThongBao(player, "Mật khẩu ít nhất 5 ký tự!");
            }
        } else {
            Service.gI().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    /**
    * Chuyển session hiện tại sang giao diện tạo nhân vật mới.
    *
    * Thao tác này gửi một gói tin có mã lệnh 2 đến client để yêu cầu hiển thị màn hình tạo nhân vật.
    *
    * @param session phiên kết nối của người chơi
    */
    public void switchToCreateChar(MySession session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi danh sách các caption (chú thích) đến client của người chơi.
    * Các caption sẽ được thay thế ký tự %1 thành tên hành tinh dựa trên giới tính của nhân vật.
    *
    * @param session phiên kết nối của người chơi
    * @param gender  giới tính/hành tinh của nhân vật, dùng để tùy chỉnh caption
    */
    public void sendCaption(MySession session, byte gender) {
        Message msg;
        try {
            msg = new Message(-41);
            msg.writer().writeByte(Manager.CAPTIONS.size());
            for (String caption : Manager.CAPTIONS) {
                msg.writer().writeUTF(caption.replaceAll("%1", gender == ConstPlayer.TRAI_DAT ? "Trái đất"
                        : (gender == ConstPlayer.NAMEC ? "Namếc" : "Xayda")));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông tin cho client biết người chơi có thú cưng hay không.
    *
    * @param player người chơi cần kiểm tra và gửi trạng thái thú cưng
    */
    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Gửi thông báo cho client biết thời gian chờ trước khi có thể đăng nhập.
    *
    * @param session     phiên kết nối của người chơi
    * @param secondsWait số giây phải chờ trước khi đăng nhập
    */
    public void sendWaitToLogin(MySession session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
    * Gửi một thông điệp đến client dựa trên dữ liệu từ file.
    *
    * @param session phiên kết nối của người chơi
    * @param cmd     mã lệnh của gói tin
    * @param path    đường dẫn đến file chứa dữ liệu cần gửi
    */
    public void sendMessage(MySession session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Tạo một vật phẩm trên bản đồ tại vị trí hiện tại của người chơi và thả nó xuống bản đồ.
    *
    * @param player người chơi nơi vật phẩm được tạo
    * @param tempId ID mẫu của vật phẩm
    */
    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    /**
    * Gửi thông tin năng động (một giá trị cố định 100) đến client của người chơi.
    *
    * @param player người chơi nhận thông tin năng động
    */
    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    /**
    * Thiết lập loại client và các thông số màn hình của phiên kết nối dựa trên dữ liệu nhận được từ client.
    * Phương thức cũng xác định phiên bản client và gửi liên kết IP về client.
    *
    * @param session phiên kết nối của người chơi
    * @param msg     gói tin chứa thông tin client
    */
    public void setClientType(MySession session, Message msg) {
        try {
            session.typeClient = (msg.reader().readByte());// client_type
            session.zoomLevel = msg.reader().readByte();// zoom_level
            msg.reader().readBoolean();// is_gprs
            msg.reader().readInt();// width
            msg.reader().readInt();// height
            msg.reader().readBoolean();// is_qwerty
            msg.reader().readBoolean();// is_touch
            String platform = msg.reader().readUTF();
            String[] arrPlatform = platform.split("\\|");
            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));

            // System.out.println(platform);
        } catch (Exception e) {
        } finally {
            msg.cleanup();
        }
        DataGame.sendLinkIP(session);
    }

    /**
    * Thả vật phẩm xuống bản đồ tại tọa độ xác định và gửi thông báo cho tất cả người chơi trong khu vực.
    * Phương thức cũng sao chép các tùy chọn của vật phẩm gốc sang vật phẩm trên bản đồ.
    *
    * @param pl   người chơi thực hiện việc thả vật phẩm
    * @param item vật phẩm cần thả
    * @param map  bản đồ nơi vật phẩm được thả
    * @param x    tọa độ X trên bản đồ
    * @param y    tọa độ Y trên bản đồ
    */
    public void DropVeTinh(Player pl, Item item, Zone map, int x, int y) {
        ItemMap itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id);
        itemMap.options = item.itemOptions;
        map.addItem(itemMap);
        Message msg = null;
        try {
            msg = new Message(68);
            msg.writer().writeShort(itemMap.itemMapId);
            msg.writer().writeShort(itemMap.itemTemplate.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            msg.writer().writeInt(-2);
            msg.writer().writeShort(200);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    /**
    * Gửi thông tin số tiền bị trộm cho người chơi.
    * Thường dùng trong các tình huống boss ăn trộm tiền.
    *
    * @param pl         người chơi bị mất tiền
    * @param stealMoney số tiền bị trộm
    */
    public void stealMoney(Player pl, int stealMoney) {//danh cho boss an trom
        Message msg;
        try {
            msg = new Message(95);
            msg.writer().writeInt(stealMoney);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }
}
