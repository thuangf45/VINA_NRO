package com.girlkun.models.clan;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.arriety.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.services.ClanService;

import java.util.ArrayList;
import java.util.List;

import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.network.io.Message;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Đại diện cho một Clan (bang hội) trong game. Lưu trữ thông tin cơ bản, thành
 * viên, hoạt động và xử lý dữ liệu liên quan đến các hoạt động bang hội như Bản
 * Đồ Kho Báu, Doanh Trại, Khí Gas.
 *
 * Quản lý việc thêm/xóa thành viên, gửi thông điệp, cập nhật DB...
 *
 * @author Lucifer
 */
public class Clan {

    /**
     * ID tiếp theo của Clan (tăng dần).
     */
    public static int NEXT_ID = 0;

    /**
     * ID thông điệp clan hiện tại.
     */
    public int clanMessageId = 0;

    /**
     * Danh sách thông điệp clan.
     */
    private final List<ClanMessage> clanMessages;

    /**
     * Quyền bang chủ.
     */
    public static final byte LEADER = 0;
    /**
     * Quyền phó bang.
     */
    public static final byte DEPUTY = 1;
    /**
     * Quyền thành viên.
     */
    public static final byte MEMBER = 2;

    // Thông tin liên quan đến hoạt động Khí Gas
    public int levelKhiGas;
    public long TimeDoneKhiGas;
    public long SoLanDiKhiGas;

    // Thông tin cơ bản của Clan
    public int id;
    public int imgId;
    public String name;
    public String slogan;
    public long createTimeLong;
    public int createTime;
    public long powerPoint;
    public byte maxMember;
    public int level;
    public boolean active;
    public int capsuleClan;

    // Hoạt động Bản Đồ Kho Báu
    public long BanDoKhoBau_lastTimeOpen;
    public boolean BanDoKhoBau_haveGone;
    public String BanDoKhoBau_playerOpen;
    public long timeOpenBanDoKhoBau;
    public Player playerOpenBanDoKhoBau;
    public BanDoKhoBau BanDoKhoBau;

    // Hoạt động Khí Gas
    public long timeOpenKhiGas;
    public Player playerOpenKhiGas;
    public Gas khiGas;

    // Hoạt động Doanh Trại
    public long lastTimeOpenDoanhTrai;
    public boolean haveGoneDoanhTrai;
    public long timeOpenDoanhTrai;
    public String playerOpenDoanhTrai;
    public DoanhTrai doanhTrai;
    public long doanhTrai_lastTimeOpen;
    public boolean doanhTrai_haveGone;
    public String doanhTrai_playerOpen;

    /**
     * Danh sách thành viên clan.
     */
    public final List<ClanMember> members;
    /**
     * Danh sách thành viên đang online trong game.
     */
    public final List<Player> membersInGame;

    /**
     * Tạo mới một Clan mặc định. ID tự động tăng, tên rỗng, tối đa 15 thành
     * viên.
     */
    public Clan() {
        this.id = NEXT_ID++;
        this.name = "";
        this.slogan = "";
        this.maxMember = 15;
        this.createTime = (int) (System.currentTimeMillis() / 1000);
        this.members = new ArrayList<>();
        this.membersInGame = new ArrayList<>();
        this.clanMessages = new ArrayList<>();
    }

    /**
     * Lấy bang chủ của clan.
     *
     * @return {@link ClanMember} là bang chủ, nếu không tồn tại thì trả về
     * dummy.
     */
    public ClanMember getLeader() {
        for (ClanMember cm : members) {
            if (cm.role == LEADER) {
                return cm;
            }
        }
        ClanMember cm = new ClanMember();
        cm.name = "Bang chủ";
        return cm;
    }

    /**
     * Lấy quyền hạn của một người chơi trong clan.
     *
     * @param player Người chơi cần kiểm tra
     * @return role (LEADER/DEPUTY/MEMBER), hoặc -1 nếu không thuộc clan
     */
    public byte getRole(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id) {
                return cm.role;
            }
        }
        return -1;
    }

    /**
     * Kiểm tra một người chơi có phải bang chủ không.
     */
    public boolean isLeader(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id && cm.role == LEADER) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra một người chơi có phải phó bang không.
     */
    public boolean isDeputy(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id && cm.role == DEPUTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cộng SMTN cho toàn bộ thành viên cùng zone với người chơi chỉ định.
     */
    public void addSMTNClan(Player plOri, long param) {
        for (Player pl : this.membersInGame) {
            if (!plOri.equals(pl) && plOri.zone.equals(pl.zone)) {
                Service.gI().addSMTN(pl, (byte) 1, param, false);
            }
        }
    }

    /**
     * Gửi một thông điệp đến tất cả thành viên online trong clan.
     */
    public void sendMessageClan(ClanMessage cmg) {
        Message msg;
        try {
            msg = new Message(-51);
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
            for (Player pl : this.membersInGame) {
                pl.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Thêm một thông điệp vào danh sách, giữ tối đa 20 tin mới nhất.
     */
    public void addClanMessage(ClanMessage cmg) {
        this.clanMessages.add(0, cmg);
        if (clanMessages.size() > 20) {
            for (int i = clanMessages.size() - 1; i >= 20; i--) {
                clanMessages.remove(i).dispose();
            }
        }
    }

    /**
     * Lấy thông điệp clan theo ID.
     */
    public ClanMessage getClanMessage(int clanMessageId) {
        for (ClanMessage cmg : this.clanMessages) {
            if (cmg.id == clanMessageId) {
                return cmg;
            }
        }
        return null;
    }

    /**
     * Lấy danh sách thông điệp hiện tại (tối đa 20).
     */
    public List<ClanMessage> getCurrClanMessages() {
        List<ClanMessage> list = new ArrayList();
        if (this.clanMessages.size() <= 20) {
            list.addAll(this.clanMessages);
        } else {
            for (int i = 0; i < 20; i++) {
                list.add(this.clanMessages.get(i));
            }
        }
        return list;
    }

    /**
     * Gửi dữ liệu clan đến toàn bộ thành viên online.
     */
    public void sendMyClanForAllMember() {
        for (Player pl : this.membersInGame) {
            if (pl != null) {
                ClanService.gI().sendMyClan(pl);
            }
        }
    }

    /**
     * Gửi dữ liệu cờ túi (Flag Bag) đến toàn bộ thành viên online.
     */
    public void sendFlagBagForAllMember() {
        for (Player pl : this.membersInGame) {
            if (pl != null) {
                Service.gI().sendFlagBag(pl);
            }
        }
    }

    /**
     * Thêm một người chơi vào danh sách online của clan.
     */
    public void addMemberOnline(Player player) {
        this.membersInGame.add(player);
    }

    /**
     * Xóa một người chơi khỏi danh sách online của clan.
     */
    public void removeMemberOnline(ClanMember cm, Player player) {
        if (player != null) {
            this.membersInGame.remove(player);
        }
        if (cm != null) {
            for (int i = this.membersInGame.size() - 1; i >= 0; i--) {
                if (this.membersInGame.get(i).id == cm.id) {
                    this.membersInGame.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * Lấy đối tượng Player đang online theo ID.
     */
    public Player getPlayerOnline(int playerId) {
        for (Player player : this.membersInGame) {
            if (player.id == playerId) {
                return player;
            }
        }
        return null;
    }

    /**
     * Thêm thành viên vào danh sách clan từ DB.
     */
    public void addClanMember(ClanMember cm) {
        this.members.add(cm);
    }

    /**
     * Thêm thành viên mới khi tạo clan hoặc gia nhập.
     */
    public void addClanMember(Player player, byte role) {
        ClanMember cm = new ClanMember(player, this, role);
        this.members.add(cm);
        player.clanMember = cm;
    }

    /**
     * Xóa thành viên khỏi clan. xóa khi member rời clan or bị kích
     */
    public void removeClanMember(ClanMember cm) {
        this.members.remove(cm);
        cm.dispose();
    }

    /**
     * Lấy số thành viên hiện tại.
     */
    public byte getCurrMembers() {
        return (byte) this.members.size();
    }

    /**
     * Lấy danh sách thành viên clan.
     */
    public List<ClanMember> getMembers() {
        return this.members;
    }

    /**
     * Lấy thông tin thành viên theo ID.
     */
    public ClanMember getClanMember(int memberId) {
        for (ClanMember cm : members) {
            if (cm.id == memberId) {
                return cm;
            }
        }
        return null;
    }

    /**
     * Reload thông tin thành viên từ dữ liệu Player online.
     */
    public void reloadClanMember() {
        for (ClanMember cm : this.members) {
            Player pl = Client.gI().getPlayer(cm.id);
            if (pl != null) {
                cm.powerPoint = pl.nPoint.power;
            }
        }
    }

    /**
     * Thêm clan vào database.
     */
    public void insert() {
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject();
        JSONArray dataArray2 = new JSONArray();
        dataArray2.add(System.currentTimeMillis());
        dataArray2.add(System.currentTimeMillis());
        String TimeClan = dataArray2.toJSONString();
        for (ClanMember cm : this.members) {
            dataObject.put("id", cm.id);
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
            dataObject.put("power", cm.powerPoint);
            dataArray.add(dataObject.toJSONString());
            dataObject.clear();
        }
        String member = dataArray.toJSONString();
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into clan_sv" + Manager.SERVER
                    + "(id, name, slogan, img_id, power_point, max_member, clan_point, level, members,createtimeLong,DataGas) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, this.id);
            ps.setString(2, this.name);
            ps.setString(3, this.slogan);
            ps.setInt(4, this.imgId);
            ps.setLong(5, this.powerPoint);
            ps.setByte(6, this.maxMember);
            ps.setInt(7, this.capsuleClan);
            ps.setInt(8, this.level);
            ps.setString(9, member);
            ps.setString(10, TimeClan);
            ps.setString(11, "[0,0,0]");
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.err.print("\nError at 120\n");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                System.err.print("\nError at 121\n");
                e.printStackTrace();
            }
        }

    }

    /**
     * Cập nhật thời gian mở Doanh Trại.
     */
    public void updateTimeDoanhTrai() {
        JSONArray dataArray2 = new JSONArray();
        dataArray2.add(this.createTimeLong);
        dataArray2.add(this.lastTimeOpenDoanhTrai);
        String timeClan = dataArray2.toJSONString();
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("UPDATE clan_sv" + Manager.SERVER + " SET createtimeLong = ? WHERE id = ? LIMIT 1");
            ps.setString(1, timeClan);
            ps.setInt(2, this.id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.err.print("\nError at 122\n");
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin Khí Gas trong database.
     */
    public void updateTimeKhiGas() {
        JSONArray dataArray2 = new JSONArray();
        dataArray2.add(this.levelKhiGas);
        dataArray2.add(this.TimeDoneKhiGas);
        dataArray2.add(SoLanDiKhiGas);
        String DataGas = dataArray2.toJSONString();
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("UPDATE clan_sv" + Manager.SERVER + " SET DataGas = ? WHERE id = ? LIMIT 1");
            ps.setString(1, DataGas);
            ps.setInt(2, this.id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.err.print("\nError at 123\n");
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin clan vào database.
     */
    public void update() {
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject();
        for (ClanMember cm : this.members) {
            dataObject.put("id", cm.id);
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
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update clan_sv" + Manager.SERVER
                    + " set slogan = ?, img_id = ?, power_point = ?, max_member = ?, clan_point = ?, "
                    + "level = ?, members = ? where id = ? limit 1");
            ps.setString(1, this.slogan);
            ps.setInt(2, this.imgId);
            ps.setLong(3, this.powerPoint);
            ps.setByte(4, this.maxMember);
            ps.setInt(5, this.capsuleClan);
            ps.setInt(6, this.level);
            ps.setString(7, member);
            ps.setInt(8, this.id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi insert clan vào db");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Xóa clan khỏi database theo ID.
     */
    public void deleteDB(int id) {
        PreparedStatement ps;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("delete from clan_sv" + Manager.SERVER + " where id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.err.print("\nError at 124\n");
            e.printStackTrace();
        }
    }

}
