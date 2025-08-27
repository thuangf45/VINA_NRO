package com.girlkun.models.player;

import com.girlkun.models.item.Item;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.ItemService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp quản lý các hiệu ứng trang bị (skin) của người chơi, bao gồm các hiệu ứng như vô hình, 
 * giáp tập luyện, hút HP/KI xung quanh, và các hiệu ứng đặc biệt khác.
 * @author Lucifer
 */
public class EffectSkin {

    /** Danh sách các câu thoại khi sử dụng hiệu ứng Ô Đô. */
    private static final String[] textOdo = new String[]{
        "Hôi quá", "Tránh ra đi thằng ở dơ", "Mùi gì kinh quá vậy?",
        "Kinh tởm quá", "Biến đi thằng ở dơ", "Kính ngài ở dơ"
    };

    /** Danh sách các câu thoại khi sử dụng hiệu ứng Test. */
    private static final String[] test = new String[]{
        "Người gì mà đẹp zai zậy", "Ui anh Béo :3", "Sao anh đẹp zoai zị?"
    };

    /** Người chơi sở hữu các hiệu ứng trang bị. */
    private Player player;

    /** Thời điểm cuối cùng tấn công. */
    public long lastTimeAttack;

    /** Thời điểm cuối cùng kích hoạt hiệu ứng Ô Đô. */
    private long lastTimeOdo;

    /** Thời điểm cuối cùng kích hoạt hiệu ứng Test. */
    private long lastTimeTest;

    /** Thời điểm cuối cùng kích hoạt hiệu ứng hút HP/KI xung quanh. */
    private long lastTimeXenHutHpKi;

    /** Thời điểm cuối cùng tăng thời gian giáp tập luyện. */
    public long lastTimeAddTimeTrainArmor;

    /** Thời điểm cuối cùng giảm thời gian giáp tập luyện. */
    public long lastTimeSubTimeTrainArmor;

    /** Trạng thái vô hình của người chơi. */
    public boolean isVoHinh;

    /** Thời điểm cuối cùng cập nhật hiệu ứng tăng HP/KI. */
    public long lastTimeXHPKI;

    /** Hệ số tăng HP/KI của người chơi. */
    public int xHPKI;

    /** Thời điểm cuối cùng cập nhật hiệu ứng cải trang Hải Tặc. */
    public long lastTimeUpdateCTHT;

    /**
     * Khởi tạo đối tượng EffectSkin cho một người chơi.
     *
     * @param player Người chơi sở hữu các hiệu ứng trang bị.
     */
    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }

    /**
     * Cập nhật trạng thái của các hiệu ứng trang bị, bao gồm vô hình, Ô Đô, hút HP/KI, giáp tập luyện và cải trang Hải Tặc.
     */
    public void update() {
        updateVoHinh();
        if (this.player.zone != null && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateXenHutXungQuanh();
        }
        if (!this.player.isBoss && !this.player.isPet && !player.isNewPet) {
            updateTrainArmor();
        }
        if (xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.gI().point(player);
        }
        updateCTHaiTac();
    }

    /**
     * Cập nhật hiệu ứng cải trang Hải Tặc, thay đổi thông số tùy thuộc vào số lượng người chơi gần đó.
     */
    private void updateCTHaiTac() {
        if (this.player.setClothes.ctHaiTac != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctHaiTac - 618] = this.player.setClothes.ctHaiTac;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes.ctHaiTac != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctHaiTac - 618] = pl.setClothes.ctHaiTac;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
                Logger.logException(EffectSkin.class, e, "Lỗi khi cập nhật cải trang Hải Tặc");
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 618 && ct.template.id <= 626) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 103) {
                            io.param = count * 3;
                        }
                    }
                }
                if (!pl.isPet && !pl.isNewPet && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryServiceNew.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }

    /**
     * Cập nhật hiệu ứng hút HP và KI từ các người chơi hoặc quái xung quanh.
     */
    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                int subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    for (Player pl : players) {
                        int subHp = pl.nPoint.hpMax * param / 100;
                        int subMp = pl.nPoint.mpMax * param / 100;
                        if (subHp >= pl.nPoint.hp) {
                            subHp = pl.nPoint.hp - 1;
                        }
                        if (subMp >= pl.nPoint.mp) {
                            subMp = pl.nPoint.mp - 1;
                        }
                        hpHut += subHp;
                        mpHut += subMp;
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.logException(EffectSkin.class, e, "Lỗi khi cập nhật hiệu ứng hút HP/KI");
        }
    }

    /**
     * Cập nhật hiệu ứng Ô Đô, gây giảm HP cho các người chơi xung quanh và hiển thị câu thoại.
     */
    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 10000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        int subHp = pl.nPoint.hpMax * param / 100;
                        if (subHp >= pl.nPoint.hp) {
                            subHp = pl.nPoint.hp - 1;
                        }
                        Service.gI().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.logException(EffectSkin.class, e, "Lỗi khi cập nhật hiệu ứng Ô Đô");
        }
    }

    /**
     * Cập nhật hiệu ứng Test, gây giảm HP cho các người chơi xung quanh và hiển thị câu thoại.
     */
    private void Test() {
        try {
            int param = this.player.nPoint.test;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeTest, 10000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        int subHp = pl.nPoint.hpMax * param / 100;
                        if (subHp >= pl.nPoint.hp) {
                            subHp = pl.nPoint.hp - 1;
                        }
                        Service.gI().chat(pl, test[Util.nextInt(0, test.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeTest = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.logException(EffectSkin.class, e, "Lỗi khi cập nhật hiệu ứng Test");
        }
    }

    /**
     * Cập nhật hiệu ứng giáp tập luyện, tăng hoặc giảm thông số tùy thuộc vào thời gian.
     */
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < 1000) {
                            io.param++;
                            InventoryServiceNew.gI().sendItemBody(player);
                        }
                        break;
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().point(this.player);
        }
    }

    /**
     * Cập nhật trạng thái vô hình của người chơi dựa trên thời gian tấn công.
     */
    private void updateVoHinh() {
        if (this.player.nPoint.wearingVoHinh) {
            if (Util.canDoWithTime(lastTimeAttack, 5000)) {
                isVoHinh = true;
            } else {
                isVoHinh = false;
            }
        }
    }

    /**
     * Giải phóng tài nguyên của đối tượng EffectSkin.
     */
    public void dispose() {
        this.player = null;
    }
}