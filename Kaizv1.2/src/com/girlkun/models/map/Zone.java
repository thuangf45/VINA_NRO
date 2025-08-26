package com.girlkun.models.map;

import com.girlkun.consts.ConstTask;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.dhvt.BossDHVT;
import com.girlkun.models.item.Item;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.player.Referee;
import com.girlkun.models.player.TestDame;
import com.girlkun.network.io.Message;
import com.girlkun.services.ItemMapService;
import com.girlkun.services.ItemService;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.services.InventoryServiceNew;
//import com.girlkun.services.NgocRongNamecService;
import com.girlkun.utils.FileIO;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Zone đại diện cho một khu vực cụ thể trong bản đồ của trò chơi, quản lý người chơi, quái vật, vật phẩm và các thực thể khác.
 * @author Lucifer
 */
public class Zone {

    /**
     * Hằng số quy định số lượng người chơi tiêu chuẩn trong một khu vực.
     */
    public static final byte PLAYERS_TIEU_CHUAN_TRONG_MAP = 12;

    /**
     * Đếm số lượng vật phẩm đã xuất hiện trong khu vực.
     */
    public int countItemAppeaerd = 0;

    /**
     * Bản đồ chứa khu vực này.
     */
    public Map map;

    /**
     * ID của khu vực.
     */
    public int zoneId;

    /**
     * Số lượng người chơi tối đa trong khu vực.
     */
    public int maxPlayer;

    /**
     * Thời gian (giờ) của khu vực.
     */
    public int hours;

    /**
     * Danh sách các thực thể con người (bao gồm người chơi, boss, pet).
     */
    private final List<Player> humanoids;

    /**
     * Danh sách các thực thể không phải boss (người chơi, pet).
     */
    private final List<Player> notBosses;

    /**
     * Danh sách người chơi trong khu vực.
     */
    private final List<Player> players;

    /**
     * Danh sách các boss trong khu vực.
     */
    public final List<Player> bosses;

    /**
     * Danh sách các pet trong khu vực.
     */
    private final List<Player> pets;

    /**
     * Danh sách các quái vật trong khu vực.
     */
    public final List<Mob> mobs;

    /**
     * Danh sách các vật phẩm trên bản đồ.
     */
    public final List<ItemMap> items;

    /**
     * Thời điểm lần cuối thả ngọc rồng đen.
     */
    public long lastTimeDropBlackBall;

    /**
     * Trạng thái hoàn thành sự kiện ngọc rồng đen.
     */
    public boolean finishBlackBallWar;

    /**
     * Trạng thái hoàn thành bản đồ Ma Bư.
     */
    public boolean finishMapMaBu;

    /**
     * Danh sách các bẫy trong bản đồ.
     */
    public List<TrapMap> trapMaps;

    /**
     * Trạng thái hoàn thành nhiệm vụ ngũ hành sơn.
     */
    public boolean finishnguhs;

    /**
     * Người chơi trọng tài trong khu vực.
     */
    @Setter
    @Getter
    private Player referee;

    /**
     * Kiểm tra xem khu vực đã đầy người chơi chưa.
     * @return true nếu số lượng người chơi đạt tối đa.
     */
    public boolean isFullPlayer() {
        return this.players.size() >= this.maxPlayer;
    }

    /**
     * Trạng thái sống của Trung Úy Trắng.
     */
    public boolean isTrungUyTrangAlive;

    /**
     * Trạng thái sống của Bulon 13.
     */
    public boolean isbulon13Alive;

    /**
     * Trạng thái sống của Bulon 14.
     */
    public boolean isbulon14Alive;

    /**
     * Cập nhật trạng thái của các quái vật trong khu vực.
     */
    private void udMob() {
        for (Mob mob : this.mobs) {
            mob.update();
        }
    }

    /**
     * Cập nhật trạng thái của các vật phẩm trong khu vực.
     */
    private void udItem() {
        for (int i = this.items.size() - 1; i >= 0; i--) {
            this.items.get(i).update();
        }
    }

    /**
     * Cập nhật trạng thái của khu vực, bao gồm quái vật, người chơi và vật phẩm.
     */
    public void update() {
        udMob();
        udPlayer();
        udItem();
    }

    /**
     * Khởi tạo một khu vực mới với bản đồ, ID khu vực và số lượng người chơi tối đa.
     * @param map Bản đồ chứa khu vực.
     * @param zoneId ID của khu vực.
     * @param maxPlayer Số lượng người chơi tối đa.
     */
    public Zone(Map map, int zoneId, int maxPlayer) {
        this.map = map;
        this.zoneId = zoneId;
        this.maxPlayer = maxPlayer;
        this.humanoids = new ArrayList<>();
        this.notBosses = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bosses = new ArrayList<>();
        this.pets = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.items = new ArrayList<>();
        this.trapMaps = new ArrayList<>();
    }

    /**
     * Lấy số lượng người chơi hiện tại trong khu vực.
     * @return Số lượng người chơi.
     */
    public int getNumOfPlayers() {
        return this.players.size();
    }

    /**
     * Kiểm tra xem một boss có thể tham gia khu vực hay không.
     * @param boss Boss cần kiểm tra.
     * @return true nếu boss có thể tham gia.
     */
    public boolean isBossCanJoin(Boss boss) {
        for (Player b : this.bosses) {
            if (b.id == boss.id) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cập nhật trạng thái của các người chơi không phải boss trong khu vực.
     */
    private void udPlayer() {
        for (int i = this.notBosses.size() - 1; i >= 0; i--) {
            if (i >= 0 && i < this.notBosses.size()) { // Check if the index is valid
                Player pl = this.notBosses.get(i);
                if (!pl.isBoss && !pl.isNewPet) {
                    pl.update();
                }
            }
        }
    }

    /**
     * Kiểm tra xem có trọng tài trong khu vực hay không.
     * @return true nếu không có trọng tài.
     */
    public boolean IsTrongTaiDeoCoTrongKhu() {
        for (Player b : this.players) {
            if (b.id == -1000000) {
                return false;
            }
        }
        return true;
    }

    /**
     * Lấy danh sách các thực thể không phải boss.
     * @return Danh sách người chơi và pet.
     */
    public List<Player> getNotBosses() {
        return this.notBosses;
    }

    /**
     * Lấy danh sách người chơi trong khu vực.
     * @return Danh sách người chơi.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Lấy danh sách các thực thể con người (người chơi, boss, pet).
     * @return Danh sách các thực thể con người.
     */
    public List<Player> getHumanoids() {
        return this.humanoids;
    }

    /**
     * Lấy danh sách các boss trong khu vực.
     * @return Danh sách boss.
     */
    public List<Player> getBosses() {
        return this.bosses;
    }

    /**
     * Thêm một người chơi vào khu vực và phân loại vào các danh sách phù hợp.
     * @param player Người chơi cần thêm.
     */
    public void addPlayer(Player player) {
        if (player != null) {
            if (!this.humanoids.contains(player)) {
                this.humanoids.add(player);
            }
            if (!player.isBoss && !this.notBosses.contains(player)) {
                this.notBosses.add(player);
            }
            if (!player.isBoss && !player.isNewPet && !player.isPet && !this.players.contains(player)) {
                this.players.add(player);
            }
            if (player.isBoss) {
                this.bosses.add(player);
            }
            if (player.isPet || player.isNewPet) {
                this.pets.add(player);
            }
        }
    }

    /**
     * Kiểm tra xem có trọng tài trong khu vực hay không dựa trên tên hoặc ID bản đồ.
     * @return true nếu không có trọng tài.
     */
    public boolean isKhongCoTrongTaiTrongKhu() {
        boolean cccccc = true;
        for (Player pl : players) {
            if (pl.name.compareTo("Trọng Tài") == 0) {
                cccccc = false;
                break;
            }
            if (pl.zone.map.mapId >= 21 && pl.zone.map.mapId <= 23) {
                cccccc = false;
            }
        }
        return cccccc;
    }

    /**
     * Xóa người chơi khỏi khu vực và các danh sách liên quan.
     * @param player Người chơi cần xóa.
     */
    public void removePlayer(Player player) {
        this.humanoids.remove(player);
        this.notBosses.remove(player);
        this.players.remove(player);
        this.bosses.remove(player);
        this.pets.remove(player);
    }

    /**
     * Lấy vật phẩm trên bản đồ theo ID vật phẩm.
     * @param itemId ID của vật phẩm trên bản đồ.
     * @return Vật phẩm tìm thấy hoặc null.
     */
    public ItemMap getItemMapByItemMapId(int itemId) {
        for (ItemMap item : this.items) {
            if (item.itemMapId == itemId) {
                return item;
            }
        }
        return null;
    }

    /**
     * Lấy vật phẩm trên bản đồ theo ID mẫu vật phẩm.
     * @param tempId ID mẫu của vật phẩm.
     * @return Vật phẩm tìm thấy hoặc null.
     */
    public ItemMap getItemMapByTempId(int tempId) {
        for (ItemMap item : this.items) {
            if (item.itemTemplate.id == tempId) {
                return item;
            }
        }
        return null;
    }

    /**
     * Lấy danh sách vật phẩm trên bản đồ phù hợp với nhiệm vụ của người chơi.
     * @param player Người chơi cần kiểm tra.
     * @return Danh sách vật phẩm phù hợp.
     */
    public List<ItemMap> getItemMapsForPlayer(Player player) {
        List<ItemMap> list = new ArrayList<>();
        for (ItemMap item : items) {
            if (item.itemTemplate.id == 78) {
                if (TaskService.gI().getIdTask(player) != ConstTask.TASK_3_1) {
                    continue;
                }
            }
            if (item.itemTemplate.id == 74) {
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_3_0) {
                    continue;
                }
            }
            list.add(item);
        }
        return list;
    }

    /**
     * Lấy người chơi trong khu vực theo ID.
     * @param idPlayer ID của người chơi.
     * @return Người chơi tìm thấy hoặc null.
     */
    public Player getPlayerInMap(long idPlayer) {
        for (Player pl : humanoids) {
            if (pl.id == idPlayer) {
                return pl;
            }
        }
        return null;
    }

    /**
     * Xử lý việc nhặt vật phẩm trên bản đồ cho người chơi.
     * @param player Người chơi nhặt vật phẩm.
     * @param itemMapId ID của vật phẩm trên bản đồ.
     */
    public void pickItem(Player player, int itemMapId) {
        ItemMap itemMap = getItemMapByItemMapId(itemMapId);
        if (itemMap != null) {
            if (itemMap.playerId == player.id || itemMap.playerId == -1) {
                Item item = ItemService.gI().createItemFromItemMap(itemMap);
                boolean picked = true;
                if (!ItemMapService.gI().isNamecBall(item.template.id)) {
                    picked = InventoryServiceNew.gI().addItemBag(player, item);
                }
                if (picked) {
                    int itemType = item.template.type;
                    Message msg;
                    try {
                        msg = new Message(-20);
                        msg.writer().writeShort(itemMapId);
                        switch (itemType) {
                            case 9:
                            case 10:
                            case 34:
                                msg.writer().writeUTF("");
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                break;
                            default:
                                switch (item.template.id) {
                                    case 362:
                                        Service.gI().sendThongBao(player, "Chỉ là cục đá thôi, nhặt làm gì?");
                                        break;
                                    case 353:
                                    case 354:
                                    case 355:
                                    case 356:
                                    case 357:
                                    case 358:
                                        msg.writer().writeUTF("Bạn đã nhặt được " + item.template.name);
                                        msg.writer().writeShort(item.quantity);
                                        player.sendMessage(msg);
                                        msg.cleanup();
                                        break;
                                    case 73:
                                        msg.writer().writeUTF("");
                                        msg.writer().writeShort(item.quantity);
                                        player.sendMessage(msg);
                                        msg.cleanup();
                                        break;
                                    case 74:
                                        msg.writer().writeUTF("Bạn mới vừa ăn " + item.template.name);
                                        break;
                                    case 78:
                                        msg.writer().writeUTF("Wow, một cậu bé dễ thương!");
                                        msg.writer().writeShort(item.quantity);
                                        player.sendMessage(msg);
                                        msg.cleanup();
                                        break;
                                    default:
                                        if (item.template.type >= 0 && item.template.type < 5) {
                                            msg.writer().writeUTF(item.template.name + " ngon ngon...");
                                        } else {
                                            msg.writer().writeUTF("Bạn mới nhặt được " + item.template.name);
                                        }
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        break;
                                }
                        }
                        msg.writer().writeShort(item.quantity);
                        player.sendMessage(msg);
                        msg.cleanup();
                        Service.gI().sendToAntherMePickItem(player, itemMapId);
                        if (!(this.map.mapId >= 21 && this.map.mapId <= 23
                                && itemMap.itemTemplate.id == 74
                                || this.map.mapId >= 42 && this.map.mapId <= 44
                                && itemMap.itemTemplate.id == 78)) {
                            removeItemMap(itemMap);
                        }
                    } catch (Exception e) {
                        Logger.logException(Zone.class, e);
                    }
                } else {
                    if (!ItemMapService.gI().isBlackBall(item.template.id)) {
                        String text = "Hành trang không còn chỗ trống";
                        Service.gI().sendThongBao(player, text);
                    }
                }
            } else {
                Service.gI().sendThongBao(player, "Không thể nhặt vật phẩm của người khác");
            }
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
        TaskService.gI().checkDoneTaskPickItem(player, itemMap);
        TaskService.gI().checkDoneSideTaskPickItem(player, itemMap);
    }

    /**
     * Thêm vật phẩm vào danh sách vật phẩm của khu vực.
     * @param itemMap Vật phẩm trên bản đồ cần thêm.
     */
    public void addItem(ItemMap itemMap) {
        if (itemMap != null && !items.contains(itemMap)) {
            items.add(0, itemMap);
        }
    }

    /**
     * Xóa vật phẩm khỏi danh sách vật phẩm của khu vực.
     * @param itemMap Vật phẩm trên bản đồ cần xóa.
     */
    public void removeItemMap(ItemMap itemMap) {
        this.items.remove(itemMap);
    }

    /**
     * Lấy ngẫu nhiên một người chơi không phải boss trong khu vực.
     * @return Người chơi ngẫu nhiên hoặc null nếu không có.
     */
    public Player getRandomPlayerInMap() {
        if (!this.notBosses.isEmpty()) {
            return this.notBosses.get(Util.nextInt(0, this.notBosses.size() - 1));
        } else {
            return null;
        }
    }

    /**
     * Gửi thông tin của người chơi cho những người chơi khác trong khu vực.
     * @param player Người chơi cần gửi thông tin.
     */
    public void load_Me_To_Another(Player player) {
        try {
            if (player.zone != null) {
                if (MapService.gI().isMapOffline(this.map.mapId)) {
                    if (player.isPet && this.equals(((Pet) player).master.zone)) {
                        infoPlayer(((Pet) player).master, player);
                    }
                } else {
                    for (int i = 0; i < players.size(); i++) {
                        Player pl = players.get(i);
                        if (!player.equals(pl)) {
                            infoPlayer(pl, player);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    /**
     * Gửi thông tin của những người chơi khác trong khu vực cho người chơi mới vào.
     * @param player Người chơi cần nhận thông tin.
     */
    public void load_Another_To_Me(Player player) {
        try {
            if (MapService.gI().isMapOffline(this.map.mapId)) {
                for (Player pl : this.humanoids) {
                    if (pl.id == -player.id) {
                        infoPlayer(player, pl);
                        break;
                    }
                }
            } else {
                for (Player pl : this.humanoids) {
                    if (pl != null && !player.equals(pl)) {
                        infoPlayer(player, pl);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    /**
     * Gửi thông tin của boss và các thực thể khác trong khu vực.
     * @param boss Boss cần gửi thông tin.
     */
    public void loadBoss(Boss boss) {
        try {
            if (MapService.gI().isMapOffline(this.map.mapId)) {
                for (Player pl : this.humanoids) {
                    if (pl.id == -boss.id) {
                        infoPlayer(boss, pl);
                        break;
                    }
                }
            } else {
                for (Player pl : this.bosses) {
                    if (!boss.equals(pl)) {
                        infoPlayer(boss, pl);
                        infoPlayer(pl, boss);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    /**
     * Gửi thông tin chi tiết của một người chơi cho người chơi khác.
     * @param plReceive Người chơi nhận thông tin.
     * @param plInfo Người chơi cần gửi thông tin.
     */
    private void infoPlayer(Player plReceive, Player plInfo) {
        Message msg;
        try {
            String name;
            msg = new Message(-5);
            msg.writer().writeInt((int) plInfo.id);
            if (plInfo.clan != null) {
                msg.writer().writeInt(plInfo.clan.id);
                name = "[" + plInfo.clan.name + "]" + plInfo.name;
            } else {
                msg.writer().writeInt(-1);
                name = plInfo.name;
            }
            msg.writer().writeByte(Service.gI().getCurrLevel(plInfo));
            msg.writer().writeBoolean(false);
            msg.writer().writeByte(plInfo.typePk);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeShort(plInfo.getHead());
            msg.writer().writeUTF(name);
            msg.writer().writeInt(plInfo.nPoint.hp);
            msg.writer().writeInt(plInfo.nPoint.hpMax);
            msg.writer().writeShort(plInfo.getBody());
            msg.writer().writeShort(plInfo.getLeg());
            msg.writer().writeByte(plInfo.getFlagBag()); // bag
            msg.writer().writeByte(-1);
            msg.writer().writeShort(plInfo.location.x);
            msg.writer().writeShort(plInfo.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0); //

            msg.writer().writeByte(0);

            msg.writer().writeByte(plInfo.iDMark.getIdSpaceShip());

            msg.writer().writeByte(plInfo.effectSkill.isMonkey ? 1 : 0);
            msg.writer().writeShort(plInfo.getMount());
            msg.writer().writeByte(plInfo.cFlag);
            msg.writer().writeByte(0);

            msg.writer().writeShort(plInfo.isPl() ? plInfo.idAura : -1); // idauraeff
            msg.writer().writeByte(-1); // idauraeff
            msg.writer().writeShort(-1); // seteff

            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Logger.logException(MapService.class, e);
        }
        Service.gI().sendFlagPlayerToMe(plReceive, plInfo);
        if (!plInfo.isBoss && !plInfo.isPet && !plInfo.isNewPet && !(plInfo instanceof BossDHVT) && !(plInfo instanceof Referee && ! (plInfo instanceof TestDame))) {
            Service.gI().sendPetFollowToMe(plReceive, plInfo);
            if (plInfo.inventory.itemsBody.get(12).isNotNullItem()) {
                Service.getInstance().sendFootRv(plInfo, plReceive, (short) plInfo.inventory.itemsBody.get(12).template.id);
            }
            if (plInfo.inventory.itemsBody.get(11).isNotNullItem()) {
                Service.gI().sendTitleRv1(plInfo, plReceive, (short) (plInfo.inventory.itemsBody.get(11).template.id));
            }
        }

        try {
            if (plInfo.isDie()) {
                msg = new Message(-8);
                msg.writer().writeInt((int) plInfo.id);
                msg.writer().writeByte(0);
                msg.writer().writeShort(plInfo.location.x);
                msg.writer().writeShort(plInfo.location.y);
                plReceive.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Gửi thông tin bản đồ và các thực thể trong khu vực cho người chơi.
     * @param pl Người chơi nhận thông tin.
     */
    public void mapInfo(Player pl) {
        Message msg;
        try {
            msg = new Message(-24);
            msg.writer().writeByte(this.map.mapId);
            msg.writer().writeByte(this.map.planetId);
            msg.writer().writeByte(this.map.tileId);
            msg.writer().writeByte(this.map.bgId);
            msg.writer().writeByte(this.map.type);
            msg.writer().writeUTF(this.map.mapName);
            msg.writer().writeByte(this.zoneId);

            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);

            // waypoint
            List<WayPoint> wayPoints = this.map.wayPoints;
            msg.writer().writeByte(wayPoints.size());
            for (WayPoint wp : wayPoints) {
                msg.writer().writeShort(wp.minX);
                msg.writer().writeShort(wp.minY);
                msg.writer().writeShort(wp.maxX);
                msg.writer().writeShort(wp.maxY);
                msg.writer().writeBoolean(wp.isEnter);
                msg.writer().writeBoolean(wp.isOffline);
                msg.writer().writeUTF(wp.name);
            }
            // mob
            List<Mob> mobs = this.mobs;
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeBoolean(false); // is disable
                msg.writer().writeBoolean(false); // is dont move
                msg.writer().writeBoolean(false); // is fire
                msg.writer().writeBoolean(false); // is ice
                msg.writer().writeBoolean(false); // is wind
                msg.writer().writeByte(mob.tempId);
                msg.writer().writeByte(0);
                msg.writer().writeInt(mob.point.gethp());
                msg.writer().writeByte(mob.level);
                msg.writer().writeInt((mob.point.getHpFull()));
                msg.writer().writeShort(mob.location.x);
                msg.writer().writeShort(mob.location.y);
                msg.writer().writeByte(mob.status);
                msg.writer().writeByte(mob.lvMob);
                msg.writer().writeBoolean(false);
            }

            msg.writer().writeByte(0);

            // npc
            List<Npc> npcs = NpcManager.getNpcsByMapPlayer(pl);
            msg.writer().writeByte(npcs.size());
            for (Npc npc : npcs) {
                msg.writer().writeByte(npc.status);
                msg.writer().writeShort(npc.cx);
                msg.writer().writeShort(npc.cy);
                msg.writer().writeByte(npc.tempId);
                msg.writer().writeShort(npc.avartar);
            }

            // item
            List<ItemMap> itemsMap = this.getItemMapsForPlayer(pl);
            msg.writer().writeByte(itemsMap.size());
            for (ItemMap it : itemsMap) {
                msg.writer().writeShort(it.itemMapId);
                msg.writer().writeShort(it.itemTemplate.id);
                msg.writer().writeShort(it.x);
                msg.writer().writeShort(it.y);
                msg.writer().writeInt((int) it.playerId);
            }

            // bg item
            try {
                byte[] bgItem = FileIO.readFile("data/girlkun/map/item_bg_map_data/" + this.map.mapId);
                msg.writer().write(bgItem);
            } catch (Exception e) {
                msg.writer().writeShort(0);
            }

            // eff item
            try {
                byte[] effItem = FileIO.readFile("data/girlkun/map/eff_map/" + this.map.mapId);
                msg.writer().write(effItem);
            } catch (Exception e) {
                msg.writer().writeShort(0);
            }

            msg.writer().writeByte(this.map.bgType);
            msg.writer().writeByte(pl.iDMark.getIdSpaceShip());
            msg.writer().writeByte(this.map.mapId == 149 ? 1 : 0);
            pl.sendMessage(msg);

            msg.cleanup();

        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    /**
     * Kiểm tra xem người chơi có đang ở trong vùng bẫy hay không.
     * @param player Người chơi cần kiểm tra.
     * @return Bẫy mà người chơi đang ở trong hoặc null nếu không có.
     */
    public TrapMap isInTrap(Player player) {
        for (TrapMap trap : this.trapMaps) {
            if (player.location.x >= trap.x && player.location.x <= trap.x + trap.w
                    && player.location.y >= trap.y && player.location.y <= trap.y + trap.h) {
                return trap;
            }
        }
        return null;
    }

    /**
     * Lấy số lượng boss hiện tại trong khu vực.
     * @return Số lượng boss.
     */
    public int getNumOfBosses() {
        return this.bosses.size();
    }
}