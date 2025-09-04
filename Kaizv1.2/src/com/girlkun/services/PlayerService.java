package com.girlkun.services;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

/**
 * Lớp PlayerService quản lý các chức năng liên quan đến người chơi trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để kiểm tra bộ trang bị, gửi thông tin, di chuyển, hồi sinh, và quản lý trạng thái PK.
 * 
 * @author Lucifer
 */
public class PlayerService {

    /**
     * Thể hiện duy nhất của lớp PlayerService (singleton pattern).
     */
    private static PlayerService i;

    /**
     * Constructor mặc định.
     */
    public PlayerService() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp PlayerService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp PlayerService.
     */
    public static PlayerService gI() {
        if (i == null) {
            i = new PlayerService();
        }
        return i;
    }

    /**
     * Kiểm tra xem người chơi có đang mặc đủ bộ trang bị Thần Linh không.
     * 
     * @param player Người chơi cần kiểm tra.
     * @return True nếu người chơi mặc đủ bộ Thần Linh, ngược lại trả về false.
     */
    public boolean isFullSetThanLinh(Player player) {
        if (player == null) {
            return false;
        }
        try {
            int genner = player.gender;
            int[] setThanlinhTraiDat = {555, 556, 561, 562, 563};
            int[] setThanlinhNamec = {557, 558, 561, 564, 565};
            int[] setThanlinhXayda = {559, 560, 561, 566, 567};
            int[] setThanlinh = new int[5];
            switch (genner) {
                case 0:
                    setThanlinh = setThanlinhTraiDat;
                    break;
                case 1:
                    setThanlinh = setThanlinhNamec;
                    break;
                default:
                    setThanlinh = setThanlinhXayda;
                    break;
            }
            for (int i = 0; i < 5; i++) {
                Item itemBody = InventoryServiceNew.gI().findItemBody(player, setThanlinh[i]);
                if (itemBody == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Logger.error("Lỗi kiểm tra set thần linh " + e);
            return false;
        }
    }

    /**
     * Kiểm tra xem người chơi có đang mặc đủ bộ trang bị Hủy Diệt không.
     * 
     * @param player Người chơi cần kiểm tra.
     * @return True nếu người chơi mặc đủ bộ Hủy Diệt, ngược lại trả về false.
     */
    public boolean isFullSetHuyDiet(Player player) {
        if (player == null) {
            return false;
        }
        try {
            int genner = player.gender;
            int[] setThanlinhTraiDat = {650, 651, 656, 657, 658};
            int[] setThanlinhNamec = {652, 653, 656, 659, 660};
            int[] setThanlinhXayda = {654, 655, 656, 661, 662};
            int[] setThanlinh = new int[5];
            switch (genner) {
                case 0:
                    setThanlinh = setThanlinhTraiDat;
                    break;
                case 1:
                    setThanlinh = setThanlinhNamec;
                    break;
                default:
                    setThanlinh = setThanlinhXayda;
                    break;
            }
            for (int i = 0; i < 5; i++) {
                Item itemBody = InventoryServiceNew.gI().findItemBody(player, setThanlinh[i]);
                if (itemBody == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Logger.error("Lỗi kiểm tra set hủy diệt " + e);
            return false;
        }
    }

    /**
     * Gửi thông báo sức mạnh hoặc tiềm năng cho người chơi.
     * 
     * @param player Người chơi nhận thông báo.
     * @param type Loại thông báo (0: sức mạnh, 1: tiềm năng, 2: cả hai).
     * @param param Giá trị cần gửi.
     */
    public void sendTNSM(Player player, byte type, long param) {
        if (param > 0) {
            Message msg;
            try {
                msg = new Message(-3);
                msg.writer().writeByte(type); // 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
                msg.writer().writeInt((int) param); // số tn cần cộng
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Gửi thông điệp tới tất cả người chơi trong server.
     * 
     * @param msg Thông điệp cần gửi.
     */
    public void sendMessageAllPlayer(Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    /**
     * Gửi thông điệp tới tất cả người chơi trừ một người chơi cụ thể.
     * 
     * @param plIgnore Người chơi bị bỏ qua.
     * @param msg Thông điệp cần gửi.
     */
    public void sendMessageIgnore(Player plIgnore, Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            try {
                if (pl != null && !pl.equals(plIgnore)) {
                    pl.sendMessage(msg);
                }
            } catch (Exception e) {
            }
        }
        msg.cleanup();
    }

    /**
     * Gửi thông tin HP của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin HP.
     */
    public void sendInfoHp(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 5);
            msg.writer().writeInt(player.nPoint.hp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    /**
     * Gửi thông tin MP của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin MP.
     */
    public void sendInfoMp(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 6);
            msg.writer().writeInt(player.nPoint.mp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    /**
     * Gửi thông tin HP và MP của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin.
     */
    public void sendInfoHpMp(Player player) {
        sendInfoHp(player);
        sendInfoMp(player);
    }

    /**
     * Hồi phục HP và MP cho người chơi nếu chưa chết.
     * 
     * @param player Người chơi cần hồi phục.
     * @param hp Lượng HP cần hồi.
     * @param mp Lượng MP cần hồi.
     */
    public void hoiPhuc(Player player, int hp, int mp) {
        if (!player.isDie()) {
            if (hp > 0) {
                player.nPoint.addHp(hp);
            }
            if (mp > 0) {
                player.nPoint.addMp(mp);
            }
            Service.gI().Send_Info_NV(player);
            if (!player.isPet && !player.isNewPet) {
                PlayerService.gI().sendInfoHpMp(player);
            }
        }
    }

    /**
     * Gửi thông tin về HP, MP, vàng, ngọc, và ruby của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin.
     */
    public void sendInfoHpMpMoney(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 4);
            try {
                if (player.getSession() != null && player.getSession().version >= 214) {
                    msg.writer().writeLong(player.inventory.gold);
                } else {
                    msg.writer().writeInt((int) player.inventory.gold);
                }
            } catch (Exception e) {
                msg.writer().writeInt((int) player.inventory.gold);
            }
            msg.writer().writeInt(player.inventory.gem); //luong
            msg.writer().writeInt(player.nPoint.hp); //chp
            msg.writer().writeInt(player.nPoint.mp); //cmp
            msg.writer().writeInt(player.inventory.ruby); //ruby
            player.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    /**
     * Di chuyển người chơi đến tọa độ mới và xử lý các hiệu ứng liên quan.
     * 
     * @param player Người chơi cần di chuyển.
     * @param x Tọa độ x đích.
     * @param y Tọa độ y đích.
     */
    public void playerMove(Player player, int x, int y) {
        if (player.zone == null) {
            return;
        }
        if (!player.isDie()) {
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            player.location.x = x;
            player.location.y = y;
            player.location.lastTimeplayerMove = System.currentTimeMillis();
            switch (player.zone.map.mapId) {
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                    if (!player.isBoss && !player.isPet) {
                        if (x < 24 || x > player.zone.map.mapWidth - 24 || y < 0 || y > player.zone.map.mapHeight - 24) {
                            if (MapService.gI().getWaypointPlayerIn(player) == null) {
                                ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                                return;
                            }
                        }
                        int yTop = player.zone.map.yPhysicInTop(player.location.x, player.location.y);
                        if (yTop >= player.zone.map.mapHeight - 24) {
                            ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                            return;
                        }
                    }
                    break;
            }
            if (player.pet != null) {
                player.pet.followMaster();
            }
            if (player.newpet != null) {
                player.newpet.followMaster();
            }
            MapService.gI().sendPlayerMove(player);
            TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
        }
    }

    /**
     * Gửi thông tin thể lực hiện tại của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin.
     */
    public void sendCurrentStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-68);
            msg.writer().writeShort(player.nPoint.stamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    /**
     * Gửi thông tin thể lực tối đa của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin.
     */
    public void sendMaxStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-69);
            msg.writer().writeShort(player.nPoint.maxStamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    /**
     * Thay đổi và gửi loại PK của người chơi.
     * 
     * @param player Người chơi cần thay đổi loại PK.
     * @param type Loại PK (0: NON_PK, 1: PK_PVP, 2: PK_ALL).
     */
    public void changeAndSendTypePK(Player player, int type) {
        changeTypePK(player, type);
        sendTypePk(player);
    }

    /**
     * Thay đổi loại PK của người chơi.
     * 
     * @param player Người chơi cần thay đổi loại PK.
     * @param type Loại PK (0: NON_PK, 1: PK_PVP, 2: PK_ALL).
     */
    public void changeTypePK(Player player, int type) {
        player.typePk = (byte) type;
    }

    /**
     * Gửi thông tin loại PK của người chơi đến tất cả người chơi trong khu vực.
     * 
     * @param player Người chơi cần gửi thông tin PK.
     */
    public void sendTypePk(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeByte(player.typePk);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Cấm tài khoản của người chơi.
     * 
     * @param playerBaned Người chơi bị cấm.
     */
    public void banPlayer(Player playerBaned) {
        if (playerBaned != null && playerBaned.iDMark != null) {
            try {
                GirlkunDB.executeUpdate("update account set ban = 1 where id = ? and username = ?",
                        playerBaned.getSession().userId, playerBaned.getSession().uu);
            } catch (Exception e) {
            }
            Service.gI().sendThongBao(playerBaned,
                    "Tài khoản của bạn đã bị khóa\nGame sẽ mất kết nối sau 5 giây...");
            playerBaned.iDMark.setLastTimeBan(System.currentTimeMillis());
            playerBaned.iDMark.setBan(true);
        }
    }

    /**
     * Hằng số chi phí vàng để hồi sinh.
     */
    private static final int COST_GOLD_HOI_SINH = 10000000;
    private static final int COST_GEM_HOI_SINH = 1;
    private static final int COST_GOLD_HOI_SINH_NRSD = 200000;
    private static final int COST_GOLD_HOI_SINH_PVP = 200000000;

    /**
     * Hồi sinh người chơi với chi phí vàng hoặc ngọc tùy thuộc vào bản đồ.
     * 
     * @param player Người chơi cần hồi sinh.
     */
    public void hoiSinh(Player player) {
        if (player.isDie()) {
            boolean canHs = false;
            if (MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_NRSD) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_NRSD;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_NRSD
                            - player.inventory.gold) + " vàng");
                    return;
                }
            }
            if (MapService.gI().isMapPVP(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_PVP) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_PVP;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_PVP
                            - player.inventory.gold) + " vàng");
                    return;
                }
            } else {
                if (player.inventory.gem >= COST_GEM_HOI_SINH) {
                    player.inventory.gem -= COST_GEM_HOI_SINH;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "?");
                    return;
                }
            }
            if (canHs) {
                Service.gI().sendMoney(player);
                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            }
        }
    }

    /**
     * Hồi sinh người chơi trong bản đồ Mabu với chi phí vàng.
     * 
     * @param player Người chơi cần hồi sinh.
     */
    public void hoiSinhMaBu(Player player) {
        if (player.isDie()) {
            boolean canHs = false;
            if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_NRSD) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_NRSD;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_NRSD
                            - player.inventory.gold) + " vàng");
                    return;
                }
            } else {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH
                            - player.inventory.gold) + " vàng");
                    return;
                }
            }
            if (canHs) {
                Service.gI().sendMoney(player);
                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            }
        }
    }
}