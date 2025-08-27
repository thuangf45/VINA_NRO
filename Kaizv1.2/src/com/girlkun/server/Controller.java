package com.girlkun.server;

import com.arriety.card.Card;
import com.arriety.card.RadarCard;
import com.arriety.card.RadarService;
import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.consts.ConstIgnoreName;
import com.girlkun.consts.ConstMap;
import com.girlkun.services.*;
import com.girlkun.utils.Util;
import com.girlkun.data.DataGame;
import com.girlkun.server.io.MySession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.UseItem;
import com.girlkun.services.func.Input;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstTask;
import com.girlkun.data.ItemData;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.arriety.kygui.ShopKyGuiService;
import com.girlkun.models.Effect.HaoQuang;
import com.girlkun.models.Effect.VongChan;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Player;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.player.Archivement;
import com.girlkun.models.player.Referee;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.network.handler.IMessageHandler;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.services.func.CombineServiceNew;
import static com.girlkun.services.func.Input.CHOOSE_LEVEL_BDKB;
import static com.girlkun.services.func.Input.NUMERIC;
import com.girlkun.services.func.LuckyRound;
import com.girlkun.services.func.SummonDragon;
import com.girlkun.services.func.TransactionService;
import com.girlkun.utils.Logger;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Lớp Controller xử lý các thông điệp (message) từ client gửi đến server trong game.
 * Lớp này thực hiện vai trò trung tâm điều khiển, quản lý các hành động của người chơi như mua bán vật phẩm,
 * chuyển bản đồ, sử dụng kỹ năng, giao tiếp với NPC, và nhiều tính năng khác.
 * 
 * @author Lucifer
 */
public class Controller implements IMessageHandler {

    /**
     * Thể hiện duy nhất của lớp Controller (singleton pattern).
     */
    private static Controller instance;

    /**
     * Lấy thể hiện duy nhất của lớp Controller.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp Controller.
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Xử lý các thông điệp từ client gửi đến server.
     * Phân tích lệnh (command) từ thông điệp và thực hiện hành động tương ứng.
     * 
     * @param s Phiên (session) của client.
     * @param _msg Thông điệp (message) nhận được từ client.
     */
    @Override
    public void onMessage(ISession s, Message _msg) {
        MySession _session = (MySession) s;
        long st = System.currentTimeMillis();
        Player player = null;
        try {
            player = _session.player;
            byte cmd = _msg.command;
            switch (cmd) {
                case -100: // Xử lý ký gửi (ShopKyGui)
                    byte action = _msg.reader().readByte();
                    switch (action) {
                        case 0: // Ký gửi vật phẩm
                            short idItem = _msg.reader().readShort();
                            byte rubyType = _msg.reader().readByte();
                            int ruby = _msg.reader().readInt();
                            int quantity;
                            if (player.getSession().version >= 214) {
                                quantity = _msg.reader().readInt();
                            } else {
                                quantity = _msg.reader().readByte();
                            }
                            if (quantity > 0) {
                                ShopKyGuiService.gI().KiGui(player, idItem, ruby, rubyType, quantity);
                            }
                            break;
                        case 1: // Hủy ký gửi
                        case 2: // Nhận lại vật phẩm ký gửi
                            idItem = _msg.reader().readShort();
                            ShopKyGuiService.gI().claimOrDel(player, action, idItem);
                            break;
                        case 3: // Mua vật phẩm ký gửi
                            idItem = _msg.reader().readShort();
                            _msg.reader().readByte();
                            _msg.reader().readInt();
                            ShopKyGuiService.gI().buyItem(player, idItem);
                            break;
                        case 4: // Mở cửa hàng ký gửi
                            rubyType = _msg.reader().readByte();
                            ruby = _msg.reader().readByte();
                            ShopKyGuiService.gI().openShopKyGui(player, rubyType, ruby);
                            break;
                        case 5: // Đưa vật phẩm lên đầu danh sách
                            idItem = _msg.reader().readShort();
                            ShopKyGuiService.gI().upItemToTop(player, idItem);
                            break;
                        default:
                            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                            break;
                    }
                    break;
                case 127: // Xử lý thẻ radar
                    if (player != null) {
                        byte actionRadar = _msg.reader().readByte();
                        switch (actionRadar) {
                            case 0: // Hiển thị danh sách thẻ radar
                                RadarService.gI().sendRadar(player, player.Cards);
                                break;
                            case 1: // Sử dụng hoặc hủy sử dụng thẻ radar
                                short idC = _msg.reader().readShort();
                                Card card = player.Cards.stream().filter(r -> r != null && r.Id == idC).findFirst().orElse(null);
                                if (card != null) {
                                    if (card.Level == 0) {
                                        return;
                                    }
                                    if (card.Used == 0) {
                                        if (player.Cards.stream().anyMatch(c -> c != null && c.Used == 1)) {
                                            Service.gI().sendThongBao(player, "Số thẻ sử dụng đã đạt tối đa");
                                            return;
                                        }
                                        card.Used = 1;
                                        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == idC).findFirst().orElse(null);
                                        if (radarTemplate != null && card.Level >= 2) {
                                            player.idAura = radarTemplate.AuraId;
                                        }
                                    } else {
                                        card.Used = 0;
                                        player.idAura = -1;
                                    }
                                    RadarService.gI().Radar1(player, idC, card.Used);
                                    Service.gI().point(player);
                                }
                                break;
                        }
                    }
                    break;
                case -105: // Nhẫn thời không
                    if (player != null) {
                        if (player.type == 0 && player.maxTime == 30) {
                            ChangeMapService.gI().changeMap(player, 102, 0, 100, 336);
                        } else if (player.type == 1 && player.maxTime == 5) {
                            ChangeMapService.gI().changeMap(player, 160, 0, -1, 5);
                        } else if (player.type == 2 && player.maxTime == 5) {
                            ChangeMapService.gI().changeMap(player, 170, 0, 1560, 336);
                        }
                    }
                    break;
                case 42: // Đăng ký tài khoản
                    Service.gI().regisAccount(_session, _msg);
                    break;
                case -127: // Vòng quay may mắn
                    if (player != null) {
                        LuckyRound.gI().readOpenBall(player, _msg);
                    }
                    break;
                case -125: // Nhập dữ liệu
                    if (player != null) {
                        Input.gI().doInput(player, _msg);
                    }
                    break;
                case 112: // Mở menu nội tại
                    if (player != null) {
                        IntrinsicService.gI().showMenu(player);
                    }
                    break;
                case -34: // Xử lý cây đậu thần
                    if (player != null) {
                        switch (_msg.reader().readByte()) {
                            case 1: // Mở menu cây đậu
                                player.magicTree.openMenuTree();
                                break;
                            case 2: // Tải thông tin cây đậu
                                player.magicTree.loadMagicTree();
                                break;
                        }
                    }
                    break;
                case -99: // Kẻ thù
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerEnemy(player, _msg);
                    }
                    break;
                case 18: // Đi đến người chơi bằng kỹ năng dịch chuyển
                    if (player != null) {
                        FriendAndEnemyService.gI().goToPlayerWithYardrat(player, _msg);
                    }
                    break;
                case -72: // Chat riêng
                    if (player != null) {
                        FriendAndEnemyService.gI().chatPrivate(player, _msg);
                    }
                    break;
                case -80: // Quản lý bạn bè
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerFriend(player, _msg);
                    }
                    break;
                case -59: // Thách đấu
                    if (player != null) {
                        PVPService.gI().controllerThachDau(player, _msg);
                    }
                    break;
                case -86: // Giao dịch
                    if (player != null) {
                        TransactionService.gI().controller(player, _msg);
                    }
                    break;
                case -107: // Hiển thị thông tin pet
                    if (player != null) {
                        Service.gI().showInfoPet(player);
                    }
                    break;
                case -108: // Thay đổi trạng thái pet
                    if (player != null && player.pet != null) {
                        player.pet.changeStatus(_msg.reader().readByte());
                    }
                    break;
                case 6: // Mua vật phẩm
                    if (player != null && !Maintenance.isRuning) {
                        try {
                            InputStream inputStream = _msg.reader();
                            byte typeBuy = _msg.reader().readByte();
                            int tempId = -1;
                            int quantity = -1;
                            byte[] buffer = new byte[4];
                            int bytesRead = 0;
                            while (bytesRead < 4) {
                                int count = inputStream.read(buffer, bytesRead, 4 - bytesRead);
                                if (count == -1) {
                                    break;
                                }
                                bytesRead += count;
                            }
                            tempId = new BigInteger(new byte[]{buffer[0], buffer[1]}).intValue();
                            quantity = new BigInteger(new byte[]{buffer[2], buffer[3]}).intValue();
                            ShopServiceNew.gI().takeItem(player, typeBuy, tempId, quantity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 7: // Bán vật phẩm
                    if (player != null && !Maintenance.isRuning) {
                        action = _msg.reader().readByte();
                        if (action == 0) {
                            ShopServiceNew.gI().showConfirmSellItem(player, _msg.reader().readByte(),
                                    _msg.reader().readShort());
                        } else {
                            // ShopServiceNew.gI().sellItem(player, _msg.reader().readByte(),
                            //         _msg.reader().readShort());
                        }
                    }
                    break;
                case 29: // Mở giao diện chọn khu vực
                    if (player != null) {
                        ChangeMapService.gI().openZoneUI(player);
                    }
                    break;
                case 21: // Chuyển khu vực
                    if (player != null) {
                        int zoneId = _msg.reader().readByte();
                        ChangeMapService.gI().changeZone(player, zoneId);
                    }
                    break;
                case -71: // Chat toàn cầu
                    if (player != null) {
                        ChatGlobalService.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case -79: // Mở menu người chơi
                    if (player != null) {
                        Service.gI().getPlayerMenu(player, _msg.reader().readInt());
                    }
                    break;
                case -113: // Cập nhật phím tắt kỹ năng
                    if (player != null) {
                        for (int i = 0; i < 5; i++) {
                            player.playerSkill.skillShortCut[i] = _msg.reader().readByte();
                        }
                        player.playerSkill.sendSkillShortCut();
                    }
                    break;
                case -101: // Chuyển sang màn hình đăng ký
                    login2(_session, _msg);
                    break;
                case -103: // Xử lý cờ
                    if (player != null) {
                        byte act = _msg.reader().readByte();
                        if (act == 0) {
                            Service.gI().openFlagUI(player);
                        } else if (act == 1) {
                            Service.gI().chooseFlag(player, _msg.reader().readByte());
                        }
                    }
                    break;
                case -7: // Di chuyển người chơi
                    if (player != null && player.location != null) {
                        int toX = player.location.x;
                        int toY = player.location.y;
                        try {
                            byte b = _msg.reader().readByte();
                            toX = _msg.reader().readShort();
                            toY = _msg.reader().readShort();
                        } catch (Exception e) {
                        }
                        PlayerService.gI().playerMove(player, toX, toY);
                    }
                    break;
                case -74: // Gửi tài nguyên
                    byte type = _msg.reader().readByte();
                    if (type == 1) {
                        DataGame.sendSizeRes(_session);
                    } else if (type == 2) {
                        DataGame.sendRes(_session);
                    }
                    break;
                case -81: // Hiển thị thông tin ghép vật phẩm
                    if (player != null) {
                        _msg.reader().readByte();
                        int[] indexItem = new int[_msg.reader().readByte()];
                        for (int i = 0; i < indexItem.length; i++) {
                            indexItem[i] = _msg.reader().readByte();
                        }
                        CombineServiceNew.gI().showInfoCombine(player, indexItem);
                    }
                    break;
                case -87: // Cập nhật dữ liệu game
                    DataGame.updateData(_session);
                    break;
                case -67: // Gửi icon
                    if (!_session.isRIcon) {
                        int id = _msg.reader().readInt();
                        DataGame.sendIcon(_session, id);
                    }
                    break;
                case 66: // Gửi hình ảnh theo tên
                    DataGame.sendImageByName(_session, _msg.reader().readUTF());
                    break;
                case -66: // Gửi dữ liệu hiệu ứng
                    int effId = _msg.reader().readShort();
                    int idT = effId;
                    if (effId == 25 && SummonDragon.gI().playerSummonShenron.zone.map.mapId == 7) {
                        idT = 50; // Hiệu ứng rồng trên bản đồ ID 7
                    }
                    if (effId == 25 && SummonDragon.gI().playerSummonShenron.zone.map.mapId == 5) {
                        idT = 51; // Hiệu ứng rồng trên bản đồ ID 5
                    }
                    if (effId == 25 && SummonDragon.gI().playerSummonShenron.zone.map.mapId == 185) {
                        idT = 51; // Hiệu ứng rồng trên bản đồ ID 185
                    }
                    DataGame.effData(_session, effId, idT);
                    break;
                case -62: // Chọn icon cờ
                    if (player != null) {
                        FlagBagService.gI().sendIconFlagChoose(player, _msg.reader().readByte());
                    }
                    break;
                case -63: // Gửi hiệu ứng cờ
                    if (player != null) {
                        byte fbid = _msg.reader().readByte();
                        int fbidz = fbid & 0xFF;
                        FlagBagService.gI().sendIconEffectFlag(player, fbidz);
                    }
                    break;
                case -76: // Nhận ngọc từ thành tích
                    if (player != null && _msg.reader().available() >= 1) {
                        Archivement.gI().receiveGem(_msg.reader().readByte(), player);
                    }
                    break;
                case -32: // Gửi mẫu nền
                    int bgId = _msg.reader().readShort();
                    DataGame.sendItemBGTemplate(_session, bgId);
                    break;
                case 22: // Xác nhận menu NPC
                    if (player != null) {
                        _msg.reader().readByte();
                        NpcManager.getNpc(ConstNpc.DAU_THAN).confirmMenu(player, _msg.reader().readByte());
                    }
                    break;
                case -33: // Chuyển bản đồ bằng điểm dịch chuyển
                case -23:
                    if (player != null) {
                        ChangeMapService.gI().changeMapWaypoint(player);
                        Service.gI().hideWaitDialog(player);
                    }
                    break;
                case -45: // Sử dụng kỹ năng
                    if (player != null) {
                        SkillService.gI().useSkill(player, null, null, _msg);
                    }
                    break;
                case -46: // Lấy thông tin bang hội
                    if (player != null) {
                        ClanService.gI().getClan(player, _msg);
                    }
                    break;
                case -51: // Gửi tin nhắn bang hội
                    if (player != null) {
                        ClanService.gI().clanMessage(player, _msg);
                    }
                    break;
                case -54: // Quyên góp cho bang hội
                    if (player != null) {
                        ClanService.gI().clanDonate(player, _msg);
                        Service.gI().sendThongBao(player, "Can not invoke clan donate");
                    }
                    break;
                case -49: // Tham gia bang hội
                    if (player != null) {
                        ClanService.gI().joinClan(player, _msg);
                    }
                    break;
                case -50: // Gửi danh sách thành viên bang hội
                    if (player != null) {
                        ClanService.gI().sendListMemberClan(player, _msg.reader().readInt());
                    }
                    break;
                case -56: // Quản lý bang hội từ xa
                    if (player != null) {
                        ClanService.gI().clanRemote(player, _msg);
                    }
                    break;
                case -47: // Gửi danh sách bang hội
                    if (player != null) {
                        ClanService.gI().sendListClan(player, _msg.reader().readUTF());
                    }
                    break;
                case -55: // Rời bang hội
                    if (player != null) {
                        if (player.clan != null && player.clan.doanhTrai != null) {
                            Service.gI().sendThongBao(player, "Không thể thực hiện lúc này");
                            return;
                        }
                        ClanService.gI().showMenuLeaveClan(player);
                    }
                    break;
                case -57: // Mời vào bang hội
                    if (player != null) {
                        ClanService.gI().clanInvite(player, _msg);
                    }
                    break;
                case -40: // Lấy vật phẩm
                    if (_session != null) {
                        UseItem.gI().getItem(_session, _msg);
                    }
                    break;
                case -41: // Gửi caption
                    Service.gI().sendCaption(_session, _msg.reader().readByte());
                    break;
                case -43: // Sử dụng vật phẩm
                    if (player != null) {
                        UseItem.gI().doItem(player, _msg);
                    }
                    break;
                case -91: // Chọn bản đồ từ capsule
                    if (player != null && player.iDMark != null) {
                        switch (player.iDMark.getTypeChangeMap()) {
                            case ConstMap.CHANGE_CAPSULE:
                                UseItem.gI().choseMapCapsule(player, _msg.reader().readByte());
                                break;
                            case ConstMap.CHANGE_BLACK_BALL:
                                BlackBallWar.gI().changeMap(player, _msg.reader().readByte());
                                break;
                        }
                    }
                    break;
                case -39: // Hoàn tất tải bản đồ
                    if (player != null) {
                        ChangeMapService.gI().finishLoadMap(player);
                        if (player.zone.map.mapId == (21 + player.gender)) {
                            if (player.mabuEgg != null) {
                                player.mabuEgg.sendMabuEgg();
                            }
                        }
                        if (player.zone.map.mapId == 154) {
                            if (player.billEgg != null) {
                                player.billEgg.sendBillEgg();
                            }
                        }
                    }
                    break;
                case 11: // Yêu cầu mẫu quái vật
                    byte modId = _msg.reader().readByte();
                    DataGame.requestMobTemplate(_session, modId);
                    break;
                case 44: // Chat trong khu vực
                    if (player != null) {
                        Service.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case 32: // Chọn menu NPC
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        int select = _msg.reader().readByte();
                        MenuController.getInstance().doSelectMenu(player, npcId, select);
                    }
                    break;
                case 33: // Mở menu NPC
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        MenuController.getInstance().openMenuNPC(_session, npcId, player);
                    }
                    break;
                case 34: // Chọn kỹ năng
                    if (player != null) {
                        int selectSkill = _msg.reader().readShort();
                        SkillService.gI().selectSkill(player, selectSkill);
                    }
                    break;
                case 54: // Tấn công quái vật
                    if (player != null) {
                        Service.gI().attackMob(player, (int) (_msg.reader().readByte()));
                    }
                    break;
                case -60: // Tấn công người chơi
                    if (player != null) {
                        int playerId = _msg.reader().readInt();
                        Service.gI().attackPlayer(player, playerId);
                    }
                    break;
                case -27: // Gửi key và phiên bản tài nguyên
                    _session.sendKey();
                    DataGame.sendVersionRes(_session);
                    break;
                case -111: // Gửi phiên bản hình ảnh
                    DataGame.sendDataImageVersion(_session);
                    break;
                case -20: // Nhặt vật phẩm trên bản đồ
                    if (player != null && !player.isDie()) {
                        int itemMapId = _msg.reader().readShort();
                        ItemMapService.gI().pickItem(player, itemMapId, false);
                    }
                    break;
                case -28: // Xử lý thông điệp không liên quan đến bản đồ
                    messageNotMap(_session, _msg);
                    break;
                case -29: // Xử lý thông điệp không đăng nhập
                    messageNotLogin(_session, _msg);
                    break;
                case -30: // Xử lý thông điệp phụ
                    messageSubCommand(_session, _msg);
                    break;
                case -15: // Về nhà
                    if (player != null) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                    }
                    break;
                case -16: // Hồi sinh
                    if (player != null) {
                        PlayerService.gI().hoiSinh(player);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        } finally {
            _msg.cleanup();
            _msg.dispose();
        }
    }

    /**
     * Xử lý các thông điệp không liên quan đến trạng thái đăng nhập (trước khi đăng nhập).
     * 
     * @param session Phiên (session) của client.
     * @param msg Thông điệp nhận được từ client.
     */
    public void messageNotLogin(MySession session, Message msg) {
        if (msg != null) {
            try {
                byte cmd = msg.reader().readByte();
                switch (cmd) {
                    case 0: // Đăng nhập
                        session.login(msg.reader().readUTF(), msg.reader().readUTF());
                        if (Manager.LOCAL) {
                            break;
                        }
                        break;
                    case 2: // Thiết lập loại client
                        Service.gI().setClientType(session, msg);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    /**
     * Xử lý các thông điệp không liên quan đến bản đồ (trước khi tải bản đồ).
     * 
     * @param _session Phiên (session) của client.
     * @param _msg Thông điệp nhận được từ client.
     */
    public void messageNotMap(MySession _session, Message _msg) {
        if (_msg != null) {
            Player player = null;
            try {
                player = _session.player;
                byte cmd = _msg.reader().readByte();
                switch (cmd) {
                    case 2: // Tạo nhân vật
                        createChar(_session, _msg);
                        break;
                    case 6: // Cập nhật dữ liệu bản đồ
                        DataGame.updateMap(_session);
                        break;
                    case 7: // Cập nhật dữ liệu kỹ năng
                        DataGame.updateSkill(_session);
                        break;
                    case 8: // Cập nhật dữ liệu vật phẩm
                        ItemData.updateItem(_session);
                        break;
                    case 10: // Gửi mẫu bản đồ
                        DataGame.sendMapTemp(_session, _msg.reader().readUnsignedByte());
                        break;
                    case 13: // Client xác nhận OK
                        if (player != null) {
                            Service.gI().player(player);
                            Service.gI().Send_Caitrang(player);
                            player.zone.load_Another_To_Me(player);
                            Service.gI().sendFlagBag(player);
                            player.playerSkill.sendSkillShortCut();
                            ItemTimeService.gI().sendAllItemTime(player);
                            TaskService.gI().sendInfoCurrentTask(player);
                        }
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    /**
     * Xử lý các thông điệp phụ (sub-command).
     * 
     * @param _session Phiên (session) của client.
     * @param _msg Thông điệp nhận được từ client.
     */
    public void messageSubCommand(MySession _session, Message _msg) {
        if (_msg != null) {
            Player player = null;
            try {
                player = _session.player;
                byte command = _msg.reader().readByte();
                switch (command) {
                    case 16: // Tăng điểm chỉ số
                        byte type = _msg.reader().readByte();
                        short point = _msg.reader().readShort();
                        if (player != null && player.nPoint != null) {
                            player.nPoint.increasePoint(type, point);
                        }
                        break;
                    case 64: // Xử lý menu phụ
                        int playerId = _msg.reader().readInt();
                        int menuId = _msg.reader().readShort();
                        SubMenuService.gI().controller(player, playerId, menuId);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    /**
     * Tạo nhân vật mới cho người chơi dựa trên thông tin từ client.
     * Kiểm tra tên nhân vật, giới tính, kiểu tóc và thực hiện tạo nhân vật nếu hợp lệ.
     * 
     * @param session Phiên (session) của client.
     * @param msg Thông điệp chứa thông tin tạo nhân vật.
     */
    public void createChar(MySession session, Message msg) {
        if (!Maintenance.isRuning) {
            GirlkunResultSet rs = null;
            boolean created = false;
            try {
                String name = msg.reader().readUTF();
                int gender = msg.reader().readByte();
                int hair = msg.reader().readByte();
                if (name.length() <= 10) {
                    rs = GirlkunDB.executeQuery("select * from player where name = ?", name);
                    if (rs.first()) {
                        Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                    } else {
                        if (Util.haveSpecialCharacter(name)) {
                            Service.gI().sendThongBaoOK(session, "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else {
                            boolean isNotIgnoreName = true;
                            for (String n : ConstIgnoreName.IGNORE_NAME) {
                                if (name.equals(n)) {
                                    Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                                    isNotIgnoreName = false;
                                    break;
                                }
                            }
                            if (isNotIgnoreName) {
                                created = PlayerDAO.createNewPlayer(session.userId, name.toLowerCase(), (byte) gender, hair);
                            }
                        }
                    }
                } else {
                    Service.gI().sendThongBaoOK(session, "Tên nhân vật tối đa 10 ký tự");
                }
            } catch (Exception e) {
                Logger.logException(Controller.class, e);
            } finally {
                if (rs != null) {
                    rs.dispose();
                }
            }
            if (created) {
                session.login(session.uu, session.pp);
            }
        }
    }

    /**
     * Chuyển client sang màn hình đăng ký nếu cần.
     * 
     * @param session Phiên (session) của client.
     * @param msg Thông điệp nhận được từ client.
     */
    public void login2(MySession session, Message msg) {
        Service.gI().switchToRegisterScr(session);
        Service.gI().sendThongBaoOK(session, "Vui lòng đăng ký tài khoản tại trang chủ");
    }

    /**
     * Gửi thông tin khởi tạo ban đầu cho người chơi khi đăng nhập.
     * Bao gồm thông tin bản đồ, nội tại, nhiệm vụ, vật phẩm, bang hội, và các hiệu ứng.
     * 
     * @param session Phiên (session) của client.
     */
    public void sendInfo(MySession session) {
        Player player = session.player;
        // Gửi thông tin tileset
        DataGame.sendTileSetInfo(session);
        // Gửi thông tin nội tại
        IntrinsicService.gI().sendInfoIntrinsic(player);
        // Gửi điểm số
        Service.getInstance().point(player);
        // Gửi nhiệm vụ chính
        TaskService.gI().sendTaskMain(player);
        // Xóa bản đồ
        Service.getInstance().clearMap(player);
        // Gửi thông tin bang hội
        ClanService.gI().sendMyClan(player);
        // Gửi stamina tối đa
        PlayerService.gI().sendMaxStamina(player);
        // Gửi stamina hiện tại
        PlayerService.gI().sendCurrentStamina(player);
        // Gửi trạng thái pet
        Service.getInstance().sendHavePet(player);
        // Gửi bảng xếp hạng
        Service.getInstance().sendMessage(session, -119, "1630679754740_-119_r");
        // Gửi thông báo bảng thông báo
        ServerNotify.gI().sendNotifyTab(player);
        // Gửi thông tin bản đồ và khu vực
        player.zone.load_Me_To_Another(player);
        player.zone.mapInfo(player);
        // Gửi thông báo server
        sendThongBaoServer(player);
        // Thiết lập bộ trang phục
        player.setClothes.setup();
        if (player.pet != null) {
            player.pet.setClothes.setup();
        }
        // Gửi thời gian sử dụng kỹ năng
        Service.getInstance().sendTimeSkill(player);
        // Xóa vật phẩm sự kiện
        clearVTSK(player);
        // Hiển thị hướng dẫn cho nhiệm vụ đầu tiên
        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
            NpcService.gI().createTutorial(player, -1,
                    "Chào mừng " + player.name + " đến với Máy Chủ Ngọc rồng Green\n"
                    + "Nhiệm vụ đầu tiên của bạn là di chuyển\n"
                    + "Bạn hãy di chuyển nhân vật theo mũi tên chỉ hướng");
        }
        // Gửi pet theo sau
        if (player.inventory.itemsBody.get(10).isNotNullItem()) {
            Service.getInstance().sendPetFollow(player, (short) (player.inventory.itemsBody.get(10).template.iconID - 1));
        }
        // Gửi hiệu ứng chân
        if (player.inventory.itemsBody.get(12).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(12).template.id);
                } catch (Exception e) {
                }
            }).start();
        }
        // Gửi danh hiệu
        if (player.partDanhHieu >= 60) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.getInstance().sendTitle(player, player.partDanhHieu);
                } catch (Exception e) {
                }
            }).start();
        }
        player.SetStart = true;
        player.LastStart = System.currentTimeMillis();
    }

    /**
     * Gửi thông báo server cho người chơi khi đăng nhập.
     * 
     * @param player Người chơi nhận thông báo.
     */
    private void sendThongBaoServer(Player player) {
        Service.gI().sendThongBaoFromAdmin(player, "|1| Máy Chủ:Ngọc Rồng Green 01\n|0|Lưu ý:Khi anh em mặc pet,hiệu ứng danh hiệu,item\nKhi anh em out game ra vào lại sẽ tự động tắt hiệu ứng\n vì vậy anh em lên thoá ra mặc lại\nAdmin chúc anh em chơi game vui vẻ");
    }

    /**
     * Xóa vật phẩm sự kiện (ID 610) khỏi túi và rương của người chơi.
     * 
     * @param player Người chơi cần xóa vật phẩm.
     */
    private void clearVTSK(Player player) {
        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 610).forEach(item -> {
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, item.quantity);
        });
        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 610).forEach(item -> {
            InventoryServiceNew.gI().subQuantityItemsBox(player, item, item.quantity);
        });
        InventoryServiceNew.gI().sendItemBags(player);
    }
}