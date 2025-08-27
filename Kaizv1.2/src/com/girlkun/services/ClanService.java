package com.girlkun.services;

import com.girlkun.database.GirlkunDB;
import java.util.List;
import static com.girlkun.models.clan.Clan.DEPUTY;
import static com.girlkun.models.clan.Clan.LEADER;
import static com.girlkun.models.clan.Clan.MEMBER;
import com.girlkun.models.item.Item;
import com.girlkun.consts.ConstNpc;
import com.girlkun.models.Template.FlagBag;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.clan.ClanMessage;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Lớp ClanService quản lý các chức năng liên quan đến bang hội trong game, bao
 * gồm tạo bang, quản lý thành viên, tin nhắn bang, và lưu trữ dữ liệu bang hội.
 * Sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 *
 * @author Lucifer
 */
public class ClanService {

    /**
     * Hằng số yêu cầu danh sách cờ để tạo bang hội.
     */
    private static final byte REQUEST_FLAGS_CHOOSE_CREATE_CLAN = 1;

    /**
     * Hằng số xác nhận tạo bang hội.
     */
    private static final byte ACCEPT_CREATE_CLAN = 2;

    /**
     * Hằng số yêu cầu danh sách cờ để thay đổi thông tin bang hội.
     */
    private static final byte REQUEST_FLAGS_CHOOSE_CHANGE_CLAN = 3;

    /**
     * Hằng số xác nhận thay đổi thông tin bang hội.
     */
    private static final byte ACCEPT_CHANGE_INFO_CLAN = 4;

    /**
     * Hằng số cho tin nhắn chat trong bang hội.
     */
    private static final byte CHAT = 0;

    /**
     * Hằng số cho yêu cầu xin đậu trong bang hội.
     */
    private static final byte ASK_FOR_PEA = 1;

    /**
     * Hằng số cho yêu cầu xin vào bang hội.
     */
    private static final byte ASK_FOR_JOIN_CLAN = 2;

    /**
     * Hằng số chấp nhận yêu cầu xin vào bang hội.
     */
    private static final byte ACCEPT_ASK_JOIN_CLAN = 0;

    /**
     * Hằng số hủy yêu cầu xin vào bang hội.
     */
    private static final byte CANCEL_ASK_JOIN_CLAN = 1;

    /**
     * Hằng số đuổi thành viên khỏi bang hội.
     */
    private static final byte KICK_OUT = -1;

    /**
     * Hằng số cắt chức phó bang.
     */
    private static final byte CAT_CHUC = 2;

    /**
     * Hằng số phong phó bang.
     */
    private static final byte PHONG_PHO = 1;

    //clan invite
    private static final byte SEND_INVITE_CLAN = 0;
    private static final byte ACCEPT_JOIN_CLAN = 1;

    /**
     * Hằng số nhường chức bang chủ.
     */
    private static final byte PHONG_PC = 0;

    /**
     * Thể hiện duy nhất của lớp ClanService (singleton pattern).
     */
    private static ClanService i;

    /**
     * Trạng thái đang lưu dữ liệu bang hội.
     */
    public boolean isSave;

    /**
     * Khởi tạo một đối tượng ClanService.
     */
    private ClanService() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp ClanService. Nếu chưa có, tạo mới một thể
     * hiện.
     *
     * @return Thể hiện của lớp ClanService.
     */
    public static ClanService gI() {
        if (i == null) {
            i = new ClanService();
        }
        return i;
    }

    /**
     * Lấy bang hội theo ID bằng thuật toán tìm kiếm nhị phân.
     *
     * @param id ID của bang hội cần tìm.
     * @return Bang hội tương ứng hoặc null nếu không tìm thấy.
     * @throws Exception Nếu không tìm thấy bang hội với ID được cung cấp.
     */
    public Clan getClanById(int id) throws Exception {
        return getClanById(0, Manager.getNumClan(), id);
    }

    /**
     * Tìm kiếm bang hội theo ID bằng thuật toán tìm kiếm nhị phân (đệ quy).
     *
     * @param l Chỉ số bắt đầu của danh sách bang hội.
     * @param r Chỉ số kết thúc của danh sách bang hội.
     * @param id ID của bang hội cần tìm.
     * @return Bang hội tương ứng hoặc null nếu không tìm thấy.
     * @throws Exception Nếu không tìm thấy bang hội với ID được cung cấp.
     */
    private Clan getClanById(int l, int r, int id) throws Exception {
        if (l <= r) {
            int m = (l + r) / 2;
            Clan clan = null;
            try {
                clan = Manager.CLANS.get(m);
            } catch (Exception e) {
                throw new Exception("Không tìm thấy clan id: " + id);
            }
            if (clan.id == id) {
                return clan;
            } else if (clan.id > id) {
                r = m - 1;
            } else {
                l = m + 1;
            }
            return getClanById(l, r, id);
        } else {
            GirlkunDB.executeUpdate("update player set clan_id_sv1 = ? where clan_id_sv1 = ?", -1, id);
        }
        return null;
    }

    /**
     * Lấy danh sách bang hội dựa trên tên (tìm kiếm gần đúng).
     *
     * @param name Tên hoặc một phần tên của bang hội.
     * @return Danh sách bang hội phù hợp, tối đa 20 bang hội.
     */
    public List<Clan> getClans(String name) {
        List<Clan> listClan = new ArrayList();
        if (Manager.CLANS.size() <= 20) {
            for (Clan clan : Manager.CLANS) {
                if (clan.name.contains(name)) {
                    listClan.add(clan);
                }
            }
        } else {
            int n = Util.nextInt(0, Manager.CLANS.size() - 20);
            for (int i = n; i < Manager.CLANS.size(); i++) {
                Clan clan = Manager.CLANS.get(i);
                if (clan.name.contains(name)) {
                    listClan.add(clan);
                }
                if (listClan.size() >= 20) {
                    break;
                }
            }
        }
        return listClan;
    }

    /**
     * Xử lý các yêu cầu liên quan đến bang hội (tạo, thay đổi thông tin).
     *
     * @param player Người chơi gửi yêu cầu.
     * @param msg Tin nhắn chứa dữ liệu yêu cầu.
     */
    public void getClan(Player player, Message msg) {
        try {
            byte action = msg.reader().readByte();
            switch (action) {
                case REQUEST_FLAGS_CHOOSE_CREATE_CLAN:
                    FlagBagService.gI().sendListFlagClan(player);
                    break;
                case ACCEPT_CREATE_CLAN:
                    byte imgId = msg.reader().readByte();
                    String name = msg.reader().readUTF();
                    createClan(player, imgId, name);
                    break;
                case REQUEST_FLAGS_CHOOSE_CHANGE_CLAN:
                    FlagBagService.gI().sendListFlagClan(player);
                    break;
                case ACCEPT_CHANGE_INFO_CLAN:
                    imgId = msg.reader().readByte();
                    String slogan = msg.reader().readUTF();
                    changeInfoClan(player, imgId, slogan);
                    break;
            }
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Xử lý tin nhắn trong bang hội (chat, xin đậu, xin vào bang).
     *
     * @param player Người chơi gửi tin nhắn.
     * @param msg Tin nhắn chứa dữ liệu yêu cầu.
     */
    public void clanMessage(Player player, Message msg) {
        try {
            byte type = msg.reader().readByte();
            switch (type) {
                case CHAT:
                    chat(player, msg.reader().readUTF());
                    break;
                case ASK_FOR_PEA:
                    // askForPea(player);
                    break;
                case ASK_FOR_JOIN_CLAN:
                    askForJoinClan(player, msg.reader().readInt());
                    break;
            }
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Xử lý việc tặng đậu cho thành viên trong bang hội.
     *
     * @param plGive Người chơi tặng đậu.
     * @param msg Tin nhắn chứa ID tin nhắn bang hội.
     */
    public void clanDonate(Player plGive, Message msg) {
        Clan clan = plGive.clan;
        if (clan != null) {
            try {
                ClanMessage cmg = clan.getClanMessage(msg.reader().readInt());
                if (cmg != null) {
                    if (cmg.receiveDonate < cmg.maxDonate) {
                        Player plReceive = clan.getPlayerOnline(cmg.playerId);
                        if (plReceive != null) {
                            Item pea = null;
                            for (Item item : plGive.inventory.itemsBox) {
                                if (item.isNotNullItem() && item.template.type == 6) {
                                    pea = item;
                                    break;
                                }
                            }
                            if (pea != null) {
                                InventoryServiceNew.gI().subQuantityItem(plGive.inventory.itemsBox, pea, 1);
                                Item peaCopy = ItemService.gI().createNewItem(pea.template.id);
                                peaCopy.itemOptions = pea.itemOptions;
                                InventoryServiceNew.gI().addItemBag(plReceive, peaCopy);
                                InventoryServiceNew.gI().sendItemBags(plReceive);
                                Service.gI().sendThongBao(plReceive, plGive.name + " đã cho bạn " + peaCopy.template.name);
                                cmg.receiveDonate++;
                                clan.sendMessageClan(cmg);
                            } else {
                                Service.gI().sendThongBao(plGive, "Không tìm thấy đậu trong rương");
                            }
                        } else {
                            Service.gI().sendThongBao(plGive, "Người chơi hiện không online");
                        }
                    }
                }
            } catch (Exception e) {
                // Bỏ qua lỗi để tránh gián đoạn xử lý
            }
        }
    }

    /**
     * Xử lý yêu cầu tham gia bang hội (chấp nhận hoặc hủy).
     *
     * @param player Người chơi xử lý yêu cầu.
     * @param msg Tin nhắn chứa ID tin nhắn bang hội và hành động.
     */
    public void joinClan(Player player, Message msg) {
        try {
            int clanMessageId = msg.reader().readInt();
            byte action = msg.reader().readByte();
            switch (action) {
                case ACCEPT_ASK_JOIN_CLAN:
                    acceptAskJoinClan(player, clanMessageId);
                    break;
                case CANCEL_ASK_JOIN_CLAN:
                    cancelAskJoinClan(player, clanMessageId);
                    break;
            }
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Xử lý các hành động quản lý bang hội từ xa (cắt chức, đuổi, phong phó,
     * nhường bang chủ).
     *
     * @param player Người chơi thực hiện hành động.
     * @param msg Tin nhắn chứa ID thành viên và vai trò hành động.
     */
    public void clanRemote(Player player, Message msg) {
        try {
            int playerId = msg.reader().readInt();
            byte role = msg.reader().readByte();
            switch (role) {
                case CAT_CHUC:
                    catChuc(player, playerId);
                    break;
                case KICK_OUT:
                    kickOut(player, playerId);
                    break;
                case PHONG_PHO:
                    phongPho(player, playerId);
                    break;
                case PHONG_PC:
                    showMenuNhuongPc(player, playerId);
                    break;
            }
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Xử lý lời mời tham gia bang hội và chấp nhận tham gia.
     *
     * @param player Người chơi xử lý lời mời.
     * @param msg Tin nhắn chứa hành động và ID bang hội.
     */
    public void clanInvite(Player player, Message msg) {
        try {
            byte action = msg.reader().readByte();
            switch (action) {
                case SEND_INVITE_CLAN:
                    sendInviteClan(player, msg.reader().readInt());
                    break;
                case ACCEPT_JOIN_CLAN:
                    acceptJoinClan(player, msg.reader().readInt());
                    break;
            }
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Gửi lời mời tham gia bang hội tới một người chơi.
     *
     * @param player Người chơi gửi lời mời.
     * @param playerId ID của người chơi nhận lời mời.
     */
    private void sendInviteClan(Player player, int playerId) {
        Player pl = Client.gI().getPlayer(playerId);
        if (pl != null && player.clan != null) {
            Message msg;
            try {
                msg = new Message(-57);
                msg.writer().writeUTF(player.name + " mời bạn vào bang " + player.clan.name);
                msg.writer().writeInt(player.clan.id);
                msg.writer().writeInt(758435); // code
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                // Bỏ qua lỗi để tránh gián đoạn gửi lời mời
            }
        }
    }

    /**
     * Chấp nhận lời mời tham gia bang hội.
     *
     * @param player Người chơi chấp nhận lời mời.
     * @param clanId ID của bang hội.
     */
    private void acceptJoinClan(Player player, int clanId) {
        try {
            if (player.clan == null) {
                Clan clan = getClanById(clanId);
                if (clan != null && clan.getCurrMembers() < clan.maxMember) {
                    clan.addClanMember(player, Clan.MEMBER);
                    clan.addMemberOnline(player);
                    player.clan = clan;
                    clan.sendMyClanForAllMember();
                    this.sendClanId(player);
                    Service.gI().sendFlagBag(player);
                    ItemTimeService.gI().sendTextDoanhTrai(player);
                    checkDoneTaskJoinClan(clan);
                } else {
                    Service.gI().sendThongBao(player, "Bang hội đã đủ người");
                }
            } else {
                Service.gI().sendThongBao(player, "Không thể thực hiện");
            }
        } catch (Exception ex) {
            Service.gI().sendThongBao(player, ex.getMessage());
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Chấp nhận yêu cầu xin vào bang hội từ một người chơi.
     *
     * @param player Bang chủ xử lý yêu cầu.
     * @param clanMessageId ID của tin nhắn yêu cầu.
     */
    private void acceptAskJoinClan(Player player, int clanMessageId) {
        Clan clan = player.clan;
        if (clan != null && clan.isLeader(player)) {
            ClanMessage cmg = clan.getClanMessage(clanMessageId);
            boolean existInClan = false;
            for (ClanMember cm : clan.members) {
                if (cm.id == cmg.playerId) {
                    existInClan = true;
                    break;
                }
            }
            if (cmg != null && !existInClan) {
                Player pl = Client.gI().getPlayer(cmg.playerId);
                cmg.type = 0;
                cmg.role = Clan.LEADER;
                cmg.playerId = (int) player.id;
                cmg.playerName = player.name;
                cmg.isNewMessage = 0;
                cmg.color = ClanMessage.RED;
                if (pl != null) {
                    if (pl.clan == null) {
                        if (clan.getCurrMembers() < clan.maxMember) {
                            clan.addClanMember(pl, Clan.MEMBER);
                            clan.addMemberOnline(pl);
                            pl.clan = player.clan;
                            cmg.text = "Chấp nhận " + pl.name + " vào bang.";
                            this.sendClanId(pl);
                            Service.gI().sendFlagBag(pl);
                            ItemTimeService.gI().sendTextDoanhTrai(pl);
                            Service.gI().sendThongBao(pl, "Bạn vừa được nhận vào bang " + clan.name);
                            checkDoneTaskJoinClan(clan);
                        } else {
                            cmg.text = "Bang hội đã đủ người";
                        }
                    } else {
                        cmg.text = "Người chơi đã vào bang khác";
                    }
                } else {
                    cmg.text = "Người chơi đang offline";
                }
                clan.sendMyClanForAllMember();
            } else {
                Service.gI().sendThongBao(player, "Không thể thực hiện");
            }
        }
    }

    /**
     * Hủy yêu cầu xin vào bang hội.
     *
     * @param player Bang chủ xử lý yêu cầu.
     * @param clanMessageId ID của tin nhắn yêu cầu.
     */
    private void cancelAskJoinClan(Player player, int clanMessageId) {
        Clan clan = player.clan;
        if (clan != null && clan.isLeader(player)) {
            ClanMessage cmg = clan.getClanMessage(clanMessageId);
            if (cmg != null) {
                Player newMember = Client.gI().getPlayer(cmg.playerId);
                cmg.type = 0;
                cmg.role = Clan.LEADER;
                cmg.playerId = (int) player.id;
                cmg.playerName = player.name;
                cmg.isNewMessage = 0;
                cmg.color = ClanMessage.RED;
                cmg.text = "Từ chối " + cmg.playerName + " vào bang";
                if (newMember != null) {
                    Service.gI().sendThongBao(newMember, "Bang hội " + clan.name + " đã từ chối bạn vào bang");
                }
                clan.sendMyClanForAllMember();
            }
        }
    }

    /**
     * Yêu cầu xin đậu từ thành viên bang hội.
     *
     * @param player Người chơi xin đậu.
     */
    private void askForPea(Player player) {
        Clan clan = player.clan;
        if (clan != null) {
            ClanMember cm = clan.getClanMember((int) player.id);
            if (cm != null) {
                if ((cm.timeAskPea + 1000 * 60 * 5) < System.currentTimeMillis()) {
                    cm.timeAskPea = System.currentTimeMillis();
                    ClanMessage cmg = new ClanMessage(clan);
                    cmg.type = 1;
                    cmg.playerId = cm.id;
                    cmg.playerName = cm.name;
                    cmg.role = cm.role;
                    cmg.receiveDonate = 0;
                    cmg.maxDonate = 5;
                    clan.addClanMessage(cmg);
                    clan.sendMessageClan(cmg);
                } else {
                    Service.gI().sendThongBao(player, "Bạn chỉ có thể xin đậu 5 phút 1 lần.");
                }
            }
        }
    }

    /**
     * Yêu cầu xin vào bang hội.
     *
     * @param player Người chơi xin vào bang.
     * @param clanId ID của bang hội.
     */
    private void askForJoinClan(Player player, int clanId) {
        try {
            if (player.clan == null) {
                Clan clan = getClanById(clanId);
                if (clan != null) {
                    boolean isMeInClan = false;
                    for (ClanMember cm : clan.members) {
                        if (cm.id == player.id) {
                            isMeInClan = true;
                            break;
                        }
                    }
                    if (!isMeInClan) {
                        if (clan.getCurrMembers() < clan.maxMember) {
                            boolean asked = false;
                            for (ClanMessage c : clan.getCurrClanMessages()) {
                                if (c.type == 2 && c.playerId == (int) player.id
                                        && c.role == -1) {
                                    asked = true;
                                    break;
                                }
                            }
                            if (!asked) {
                                ClanMessage cmg = new ClanMessage(clan);
                                cmg.type = 2;
                                cmg.playerId = (int) player.id;
                                cmg.playerName = player.name;
                                cmg.playerPower = player.nPoint.power;
                                cmg.role = -1;
                                clan.addClanMessage(cmg);
                                clan.sendMessageClan(cmg);
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bang hội đã đủ người");
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            } else {
                Service.gI().sendThongBao(player, "Không thể thực hiện");
            }
        } catch (Exception ex) {
            Service.gI().sendThongBao(player, ex.getMessage());
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Thay đổi thông tin bang hội (cờ hoặc khẩu hiệu).
     *
     * @param player Người chơi thực hiện thay đổi.
     * @param imgId ID của cờ bang hội.
     * @param slogan Khẩu hiệu của bang hội.
     */
    private void changeInfoClan(Player player, byte imgId, String slogan) {
        if (!slogan.equals("")) {
            changeSlogan(player, slogan);
        } else {
            changeFlag(player, imgId);
        }
    }

    /**
     * Tạo bang hội mới.
     *
     * @param player Người chơi tạo bang hội.
     * @param imgId ID của cờ bang hội.
     * @param name Tên bang hội.
     */
    private void createClan(Player player, byte imgId, String name) {
        if (player.clan == null) {
            if (name.length() > 30) {
                Service.gI().sendThongBao(player, "Tên bang hội không được quá 30 ký tự");
                return;
            }
            FlagBag flagBag = FlagBagService.gI().getFlagBag(imgId);
            if (flagBag != null) {
                if (flagBag.gold > 0) {
                    if (player.inventory.gold >= flagBag.gold) {
                        player.inventory.gold -= flagBag.gold;
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu "
                                + Util.numberToMoney(flagBag.gold - player.inventory.gold) + " vàng");
                        return;
                    }
                }
                if (flagBag.gem > 0) {
                    if (player.inventory.gem >= flagBag.gem) {
                        player.inventory.gem -= flagBag.gem;
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ ngọc, còn thiếu "
                                + (flagBag.gem - player.inventory.gem) + " ngọc");
                        return;
                    }
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                Clan clan = new Clan();
                clan.imgId = imgId;
                clan.name = name;
                Manager.addClan(clan);
                player.clan = clan;
                clan.addClanMember(player, Clan.LEADER);
                clan.addMemberOnline(player);
                clan.insert();
                Service.gI().sendFlagBag(player);
                sendMyClan(player);
            }
        }
    }

    /**
     * Gửi danh sách bang hội tới người chơi.
     *
     * @param player Người chơi nhận danh sách.
     * @param name Tên hoặc một phần tên bang hội để tìm kiếm.
     */
    public void sendListClan(Player player, String name) {
        Message msg;
        try {
            List<Clan> clans = getClans(name);
            msg = new Message(-47);
            msg.writer().writeByte(clans.size());
            for (Clan clan : clans) {
                msg.writer().writeInt(clan.id);
                msg.writer().writeUTF(clan.name);
                msg.writer().writeUTF(clan.slogan);
                msg.writer().writeByte(clan.imgId);
                msg.writer().writeUTF(String.valueOf(clan.powerPoint));
                msg.writer().writeUTF(clan.getLeader().name);
                msg.writer().writeByte(clan.getCurrMembers());
                msg.writer().writeByte(clan.maxMember);
                msg.writer().writeInt(clan.createTime);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi danh sách
        }
    }

    /**
     * Gửi danh sách thành viên bang hội tới người chơi.
     *
     * @param player Người chơi nhận danh sách.
     * @param clanId ID của bang hội.
     */
    public void sendListMemberClan(Player player, int clanId) {
        try {
            Clan clan = getClanById(clanId);
            if (clan != null) {
                clan.reloadClanMember();
                Message msg;
                try {
                    msg = new Message(-50);
                    msg.writer().writeByte(clan.getCurrMembers());
                    for (ClanMember cm : clan.getMembers()) {
                        msg.writer().writeInt((int) cm.id);
                        msg.writer().writeShort(cm.head);
                        msg.writer().writeShort(-1);
                        msg.writer().writeShort(cm.leg);
                        msg.writer().writeShort(cm.body);
                        msg.writer().writeUTF(cm.name);
                        msg.writer().writeByte(cm.role);
                        msg.writer().writeUTF(Util.numberToMoney(cm.powerPoint));
                        msg.writer().writeInt(cm.donate);
                        msg.writer().writeInt(cm.receiveDonate);
                        msg.writer().writeInt(cm.clanPoint);
                        msg.writer().writeInt(cm.joinTime);
                    }
                    player.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                    Service.gI().sendThongBao(player, e.getMessage());
                    // Bỏ qua lỗi để tránh gián đoạn gửi danh sách
                }
            }
        } catch (Exception ex) {
            Service.gI().sendThongBao(player, ex.getMessage());
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Gửi thông tin bang hội của người chơi (bao gồm thành viên và tin nhắn).
     *
     * @param player Người chơi nhận thông tin.
     */
    public void sendMyClan(Player player) {
        Message msg;
        try {
            msg = new Message(-53);
            if (player.clan == null) {
                msg.writer().writeInt(-1);
            } else {
                msg.writer().writeInt(player.clan.id);
                msg.writer().writeUTF(player.clan.name);
                msg.writer().writeUTF(player.clan.slogan);
                msg.writer().writeByte(player.clan.imgId);
                msg.writer().writeUTF(String.valueOf(player.clan.powerPoint));
                msg.writer().writeUTF(player.clan.getLeader().name);
                msg.writer().writeByte(player.clan.getCurrMembers());
                msg.writer().writeByte(player.clan.maxMember);
                msg.writer().writeByte(player.clan.getRole(player));
                msg.writer().writeInt((int) player.clan.capsuleClan);
                msg.writer().writeByte(player.clan.level);
                for (ClanMember cm : player.clan.getMembers()) {
                    msg.writer().writeInt(cm.id);
                    msg.writer().writeShort(cm.head);
                    msg.writer().writeShort(-1);
                    msg.writer().writeShort(cm.leg);
                    msg.writer().writeShort(cm.body);
                    msg.writer().writeUTF(cm.name);
                    msg.writer().writeByte(cm.role);
                    msg.writer().writeUTF(Util.numberToMoney(cm.powerPoint));
                    msg.writer().writeInt(cm.donate);
                    msg.writer().writeInt(cm.receiveDonate);
                    msg.writer().writeInt(cm.clanPoint);
                    msg.writer().writeInt(cm.memberPoint);
                    msg.writer().writeInt(cm.joinTime);
                }
                List<ClanMessage> clanMessages = player.clan.getCurrClanMessages();
                msg.writer().writeByte(clanMessages.size());
                for (ClanMessage cmg : clanMessages) {
                    msg.writer().writeByte(cmg.type);
                    msg.writer().writeInt(cmg.id);
                    msg.writer().writeInt(cmg.playerId);
                    if (cmg.type == 2) {
                        msg.writer().writeUTF(cmg.playerName + " (" + Util.numberToMoney(cmg.playerPower) + ")");
                    } else {
                        msg.writer().writeUTF(cmg.playerName);
                    }
                    msg.writer().writeByte(cmg.role);
                    msg.writer().writeInt(cmg.time);
                    if (cmg.type == 0) {
                        msg.writer().writeUTF(cmg.text);
                        msg.writer().writeByte(cmg.color);
                    } else if (cmg.type == 1) {
                        msg.writer().writeByte(cmg.receiveDonate);
                        msg.writer().writeByte(cmg.maxDonate);
                        msg.writer().writeByte(cmg.isNewMessage);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(ClanService.class, e, "Lỗi send my clan " + player.clan.name + " - " + player.clan.id);
            // Bỏ qua lỗi để tránh gián đoạn gửi thông tin
        }
    }

    /**
     * Gửi ID bang hội của người chơi tới tất cả người chơi trong cùng bản đồ.
     *
     * @param player Người chơi cần gửi ID bang hội.
     */
    public void sendClanId(Player player) {
        Message msg;
        try {
            msg = new Message(-61);
            msg.writer().writeInt((int) player.id);
            if (player.clan == null) {
                msg.writer().writeInt(-1);
            } else {
                msg.writer().writeInt(player.clan.id);
            }
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi ID
        }
    }

    /**
     * Hiển thị menu xác nhận rời bang hội.
     *
     * @param player Người chơi muốn rời bang hội.
     */
    public void showMenuLeaveClan(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_LEAVE_CLAN,
                -1, "Bạn có chắc chắn rời bang hội không?", "Đồng ý", "Từ chối");
    }

    /**
     * Hiển thị menu xác nhận nhường chức bang chủ.
     *
     * @param player Bang chủ hiện tại.
     * @param playerId ID của thành viên được nhường chức.
     */
    public void showMenuNhuongPc(Player player, int playerId) {
        if (player.clan.isLeader(player)) {
            ClanMember cm = player.clan.getClanMember(playerId);
            if (cm != null) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_NHUONG_PC, -1,
                        "Bạn có đồng ý nhường chức bang chủ cho " + cm.name + "?", new String[]{"Đồng ý", "Từ chối"}, playerId);
            }
        }
    }

    /**
     * Thay đổi khẩu hiệu của bang hội.
     *
     * @param player Bang chủ thay đổi khẩu hiệu.
     * @param slogan Khẩu hiệu mới.
     */
    public void changeSlogan(Player player, String slogan) {
        if (slogan.length() > 250) {
            slogan = slogan.substring(0, 250);
        }
        Clan clan = player.clan;
        if (clan != null && clan.isLeader(player)) {
            clan.slogan = slogan;
            clan.sendMyClanForAllMember();
        }
    }

    /**
     * Thay đổi cờ của bang hội.
     *
     * @param player Bang chủ thay đổi cờ.
     * @param imgId ID của cờ mới.
     */
    public void changeFlag(Player player, int imgId) {
        Clan clan = player.clan;
        if (clan != null && clan.isLeader(player) && imgId != clan.imgId) {
            FlagBag flagBag = FlagBagService.gI().getFlagBag(imgId);
            if (flagBag != null) {
                if (flagBag.gold > 0) {
                    if (player.inventory.gold >= flagBag.gold) {
                        player.inventory.gold -= flagBag.gold;
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu "
                                + Util.numberToMoney(flagBag.gold - player.inventory.gold) + " vàng");
                        return;
                    }
                }
                if (flagBag.gem > 0) {
                    if (player.inventory.gem >= flagBag.gem) {
                        player.inventory.gem -= flagBag.gem;
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ ngọc, còn thiếu "
                                + (flagBag.gem - player.inventory.gem) + " ngọc");
                        return;
                    }
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                player.clan.imgId = imgId;
                clan.sendFlagBagForAllMember();
            }
        }
    }

    /**
     * Rời khỏi bang hội.
     *
     * @param player Người chơi muốn rời bang hội.
     */
    public void leaveClan(Player player) {
        Clan clan = player.clan;
        if (clan != null) {
            ClanMember cm = clan.getClanMember((int) player.id);
            if (cm != null) {
                if (clan.isLeader(player)) {
                    Service.gI().sendThongBao(player, "Phải nhường chức bang chủ trước khi rời");
                    return;
                }
                ClanMessage cmg = new ClanMessage(clan);
                cmg.type = 0;
                cmg.role = clan.getRole(player);
                cmg.color = ClanMessage.BLACK;
                cmg.playerId = (int) player.id;
                cmg.playerName = player.name;
                cmg.text = player.name + " đã rời khỏi bang.";
                cmg.color = ClanMessage.RED;
                clan.removeClanMember(cm);
                clan.removeMemberOnline(cm, player);
                cm.clan = null;
                cm = null;
                player.clan = null;
                player.clanMember = null;
                ClanService.gI().sendMyClan(player);
                ClanService.gI().sendClanId(player);
                Service.gI().sendFlagBag(player);
                Service.gI().sendThongBao(player, "Bạn đã rời khỏi bang");
                ItemTimeService.gI().removeTextDoanhTrai(player);
                clan.sendMyClanForAllMember();
                clan.addClanMessage(cmg);
                clan.sendMessageClan(cmg);
            }
        }
    }

    /**
     * Cắt chức phó bang của một thành viên.
     *
     * @param player Bang chủ thực hiện hành động.
     * @param memberId ID của thành viên bị cắt chức.
     */
    public void catChuc(Player player, int memberId) {
        Clan clan = player.clan;
        if (clan != null) {
            if (player.clan.isLeader(player)) {
                ClanMember cm = clan.getClanMember(memberId);
                if (cm != null) {
                    ClanMember leader = clan.getLeader();
                    ClanMessage cmg = new ClanMessage(clan);
                    cmg.type = 0;
                    cmg.role = leader.role;
                    cmg.playerId = leader.id;
                    cmg.playerName = leader.name;
                    cmg.text = "Cắt chức phó bang của " + cm.name;
                    cmg.color = ClanMessage.RED;
                    cm.role = MEMBER;
                    clan.sendMyClanForAllMember();
                    clan.addClanMessage(cmg);
                    clan.sendMessageClan(cmg);
                }
            }
        }
    }

    /**
     * Đuổi một thành viên khỏi bang hội.
     *
     * @param player Bang chủ hoặc phó bang thực hiện hành động.
     * @param memberId ID của thành viên bị đuổi.
     */
    public void kickOut(Player player, int memberId) {
        Clan clan = player.clan;
        ClanMember cm = clan.getClanMember(memberId);
        if (clan != null && cm != null
                && (clan.isLeader(player) || clan.isDeputy(player) && cm.role == MEMBER)) {
            Player plKicked = clan.getPlayerOnline(memberId);
            ClanMember cmKick = clan.getClanMember((int) player.id);
            ClanMessage cmg = new ClanMessage(clan);
            cmg.type = 0;
            cmg.role = cmKick.role;
            cmg.playerId = cmKick.id;
            cmg.playerName = cmKick.name;
            cmg.text = "Đuổi " + cm.name + " ra khỏi bang.";
            cmg.color = ClanMessage.RED;
            clan.removeClanMember(cm);
            clan.removeMemberOnline(cm, plKicked);
            cm.clan = null;
            cm = null;
            if (plKicked != null) {
                plKicked.clan = null;
                plKicked.clanMember = null;
                ClanService.gI().sendMyClan(plKicked);
                ClanService.gI().sendClanId(plKicked);
                Service.gI().sendFlagBag(plKicked);
                Service.gI().sendThongBao(plKicked, "Bạn đã bị đuổi khỏi bang");
                ItemTimeService.gI().removeTextDoanhTrai(plKicked);
            } else {
                removeClanPlayer(memberId);
            }
            clan.sendMyClanForAllMember();
            clan.addClanMessage(cmg);
            clan.sendMessageClan(cmg);
        }
    }

    /**
     * Xóa thông tin bang hội của một người chơi khỏi cơ sở dữ liệu.
     *
     * @param plId ID của người chơi.
     */
    private void removeClanPlayer(int plId) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update player set clan_id_sv"
                    + Manager.SERVER + " = -1 where id = " + plId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            removeClanPlayer(plId);
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                // Bỏ qua lỗi đóng PreparedStatement
            }
        }
    }

    /**
     * Phong một thành viên làm phó bang.
     *
     * @param player Bang chủ hoặc phó bang thực hiện hành động.
     * @param memberId ID của thành viên được phong phó.
     */
    public void phongPho(Player player, int memberId) {
        Clan clan = player.clan;
        if (clan != null && (clan.isLeader(player) || clan.isDeputy(player))) {
            ClanMember cm1 = clan.getClanMember(memberId);
            if (cm1 != null && cm1.role == MEMBER) {
                ClanMember cm2 = clan.getClanMember((int) player.id);
                ClanMessage cmg = new ClanMessage(clan);
                cmg.type = 0;
                cmg.role = cm2.role;
                cmg.playerId = cm2.id;
                cmg.playerName = cm2.name;
                cmg.text = "Phong phó bang cho " + cm1.name;
                cmg.color = ClanMessage.RED;
                cm1.role = DEPUTY;
                clan.sendMyClanForAllMember();
                clan.addClanMessage(cmg);
                clan.sendMessageClan(cmg);
            } else {
                Service.gI().sendThongBao(player, "Không thể thực hiện");
            }
        }
    }

    /**
     * Nhường chức bang chủ cho một thành viên.
     *
     * @param player Bang chủ hiện tại.
     * @param memberId ID của thành viên được nhường chức.
     */
    public void phongPc(Player player, int memberId) {
        Clan clan = player.clan;
        if (clan != null && clan.isLeader(player)) {
            ClanMember leader = clan.getLeader();
            ClanMember cm = clan.getClanMember(memberId);
            if (cm != null) {
                ClanMessage cmg = new ClanMessage(clan);
                cmg.type = 0;
                cmg.role = leader.role;
                cmg.playerId = leader.id;
                cmg.playerName = leader.name;
                cmg.text = "Nhường chức bang chủ cho " + cm.name;
                cmg.color = ClanMessage.RED;
                leader.role = MEMBER;
                cm.role = LEADER;
                clan.sendMyClanForAllMember();
                clan.addClanMessage(cmg);
                clan.sendMessageClan(cmg);
            }
        }
    }

    /**
     * Gửi tin nhắn chat trong bang hội.
     *
     * @param player Người chơi gửi tin nhắn.
     * @param text Nội dung tin nhắn.
     */
    public void chat(Player player, String text) {
        Clan clan = player.clan;
        if (clan != null) {
            ClanMember cm = clan.getClanMember((int) player.id);
            if (cm != null) {
                ClanMessage cmg = new ClanMessage(clan);
                cmg.type = 0;
                cmg.playerId = cm.id;
                cmg.playerName = cm.name;
                cmg.role = cm.role;
                cmg.text = text;
                cmg.color = 0;
                clan.addClanMessage(cmg);
                clan.sendMessageClan(cmg);
            }
        }
    }

    /**
     * Kiểm tra và hoàn thành nhiệm vụ tham gia bang hội cho các thành viên.
     *
     * @param clan Bang hội cần kiểm tra.
     */
    private void checkDoneTaskJoinClan(Clan clan) {
        if (clan.getMembers().size() >= 2) {
            for (Player player : clan.membersInGame) {
                TaskService.gI().checkDoneTaskJoinClan(player);
            }
        }
    }

    /**
     * Lưu toàn bộ dữ liệu bang hội vào cơ sở dữ liệu khi đóng server.
     */
    public void close() {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update clan_sv" + Manager.SERVER
                    + " set slogan = ?, img_id = ?, power_point = ?, max_member = ?, clan_point = ?, "
                    + "level = ?, members = ? where id = ? limit 1");
            for (Clan clan : Manager.CLANS) {
                JSONArray dataArray = new JSONArray();
                JSONObject dataObject = new JSONObject();
                for (ClanMember cm : clan.members) {
                    dataObject.put("id", cm.id);
                    dataObject.put("power", cm.powerPoint);
                    dataObject.put("name", cm.name);
                    dataObject.put("head", cm.head);
                    dataObject.put("body", cm.body);
                    dataObject.put("leg", cm.leg);
                    dataObject.put("role", cm.role);
                    dataObject.put("donate", cm.donate);
                    dataObject.put("receive_donate", cm.receiveDonate);
                    dataObject.put("member_point", cm.memberPoint);
                    dataObject.put("clan_point", cm.clanPoint);
                    dataObject.put("join_time", cm.joinTime);
                    dataObject.put("ask_pea_time", cm.timeAskPea);
                    dataArray.add(dataObject.toJSONString());
                    dataObject.clear();
                }
                String member = dataArray.toJSONString();
                ps.setString(1, clan.slogan);
                ps.setInt(2, clan.imgId);
                ps.setLong(3, clan.powerPoint);
                ps.setByte(4, clan.maxMember);
                ps.setInt(5, clan.capsuleClan);
                ps.setInt(6, clan.level);
                ps.setString(7, member);
                ps.setInt(8, clan.id);
                ps.addBatch();
                Logger.error("SAVE CLAN: " + clan.name + " (" + clan.id + ")\n");
            }
            ps.executeBatch();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi update clan vào db");
            // Bỏ qua lỗi để tránh gián đoạn đóng server
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                // Bỏ qua lỗi đóng PreparedStatement
            }
        }
    }

    /**
     * Lưu toàn bộ dữ liệu bang hội vào cơ sở dữ liệu với cơ chế chống trùng
     * lặp.
     */
    public void saveclan() {
        if (isSave) {
            return;
        }
        isSave = true;
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update clan_sv" + Manager.SERVER
                    + " set slogan = ?, img_id = ?, power_point = ?, max_member = ?, clan_point = ?, "
                    + "level = ?, members = ? where id = ? limit 1");
            for (Clan clan : Manager.CLANS) {
                JSONArray dataArray = new JSONArray();
                JSONObject dataObject = new JSONObject();
                for (ClanMember cm : clan.members) {
                    dataObject.put("id", cm.id);
                    dataObject.put("power", cm.powerPoint);
                    dataObject.put("name", cm.name);
                    dataObject.put("head", cm.head);
                    dataObject.put("body", cm.body);
                    dataObject.put("leg", cm.leg);
                    dataObject.put("role", cm.role);
                    dataObject.put("donate", cm.donate);
                    dataObject.put("receive_donate", cm.receiveDonate);
                    dataObject.put("member_point", cm.memberPoint);
                    dataObject.put("clan_point", cm.clanPoint);
                    dataObject.put("join_time", cm.joinTime);
                    dataObject.put("ask_pea_time", cm.timeAskPea);
                    dataArray.add(dataObject.toJSONString());
                    dataObject.clear();
                }
                String member = dataArray.toJSONString();
                ps.setString(1, clan.slogan);
                ps.setInt(2, clan.imgId);
                ps.setLong(3, clan.powerPoint);
                ps.setByte(4, clan.maxMember);
                ps.setInt(5, clan.capsuleClan);
                ps.setInt(6, clan.level);
                ps.setString(7, member);
                ps.setInt(8, clan.id);
                ps.addBatch();
            }
            ps.executeBatch();
            Thread.sleep(30000);
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi update clan vào db");
            // Bỏ qua lỗi để tránh gián đoạn lưu dữ liệu
        } finally {
            isSave = false;
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                // Bỏ qua lỗi đóng PreparedStatement
            }
        }
    }
}
