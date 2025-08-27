package com.girlkun.models.shop;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Manager;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.services.func.TransactionService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.List;

/**
 * Lớp quản lý các hoạt động liên quan đến cửa hàng trong game, bao gồm mở cửa hàng, mua và bán vật phẩm.
 * @author Lucifer
 */
public class ShopServiceNew {

    /** Hằng số đại diện cho loại thanh toán bằng vàng. */
    private static final byte COST_GOLD = 0;

    /** Hằng số đại diện cho loại thanh toán bằng ngọc lục bảo. */
    private static final byte COST_GEM = 1;

    /** Hằng số đại diện cho loại thanh toán bằng vật phẩm đặc biệt. */
    private static final byte COST_ITEM_SPEC = 2;

    /** Hằng số đại diện cho loại thanh toán bằng hồng ngọc. */
    private static final byte COST_RUBY = 3;

    /** Hằng số đại diện cho loại thanh toán bằng phiếu giảm giá. */
    private static final byte COST_COUPON = 4;

    /** Hằng số đại diện cho cửa hàng thông thường. */
    private static final byte NORMAL_SHOP = 0;

    /** Hằng số đại diện cho cửa hàng đặc biệt. */
    private static final byte SPEC_SHOP = 3;

    /** Hằng số đại diện cho rương phần thưởng. */
    private static final byte BOX = 4;

    /** Thể hiện singleton của lớp ShopServiceNew. */
    private static ShopServiceNew I;

    /**
     * Lấy thể hiện singleton của lớp ShopServiceNew.
     *
     * @return Thể hiện singleton của ShopServiceNew.
     */
    public static ShopServiceNew gI() {
        if (ShopServiceNew.I == null) {
            ShopServiceNew.I = new ShopServiceNew();
        }
        return ShopServiceNew.I;
    }

    /**
     * Mở cửa hàng cho người chơi dựa trên tên thẻ và tùy chọn giới tính.
     *
     * @param player Người chơi mở cửa hàng.
     * @param tagName Tên thẻ của cửa hàng.
     * @param allGender Cho phép hiển thị tất cả vật phẩm bất kể giới tính.
     */
    public void opendShop(Player player, String tagName, boolean allGender) {
        if (tagName.equals("ITEMS_MAIL_BOX")) {
            openShopType4(player, tagName, player.inventory.itemsMailBox);
            return;
        }
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {
            openShopType4(player, tagName, player.inventory.itemsBoxCrackBall);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {
            player.getSession().initItemsReward();
            return;
        }
        try {
            Shop shop = this.getShop(tagName);
            shop = this.resolveShop(player, shop, allGender);
            switch (shop.typeShop) {
                case NORMAL_SHOP:
                    openShopType0(player, shop);
                    break;
                case SPEC_SHOP:
                    openShopType3(player, shop);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Service.gI().sendThongBao(player, ex.getMessage());
        }
    }

    /**
     * Lấy thông tin cửa hàng dựa trên tên thẻ.
     *
     * @param tagName Tên thẻ của cửa hàng.
     * @return Đối tượng Shop tương ứng.
     * @throws Exception Nếu cửa hàng không tồn tại.
     */
    private Shop getShop(String tagName) throws Exception {
        for (Shop s : Manager.SHOPS) {
            if (s.tagName != null && s.tagName.equals(tagName)) {
                return s;
            }
        }
        throw new Exception("Shop " + tagName + " không tồn tại!");
    }

    /**
     * Xử lý cửa hàng trước khi gửi thông tin, điều chỉnh dữ liệu theo giới tính hoặc loại bùa.
     *
     * @param player Người chơi mở cửa hàng.
     * @param shop Cửa hàng cần xử lý.
     * @param allGender Cho phép hiển thị tất cả vật phẩm bất kể giới tính.
     * @return Cửa hàng đã được xử lý.
     */
    private Shop resolveShop(Player player, Shop shop, boolean allGender) {
        if (shop.tagName != null && (shop.tagName.equals("BUA_1H")
                || shop.tagName.equals("BUA_8H") || shop.tagName.equals("BUA_1M"))) {
            return this.resolveShopBua(player, new Shop(shop));
        }
        return allGender ? new Shop(shop) : new Shop(shop, player.gender);
    }

    /**
     * Xử lý cửa hàng bùa, cập nhật thời gian hiệu lực của các bùa.
     *
     * @param player Người chơi mở cửa hàng.
     * @param s Cửa hàng bùa cần xử lý.
     * @return Cửa hàng bùa đã được cập nhật.
     */
    private Shop resolveShopBua(Player player, Shop s) {
        for (TabShop tabShop : s.tabShops) {
            for (ItemShop item : tabShop.itemShops) {
                long min = 0;
                switch (item.temp.id) {
                    case 213:
                        long timeTriTue = player.charms.tdTriTue;
                        long current = System.currentTimeMillis();
                        min = (timeTriTue - current) / 60000;
                        break;
                    case 214:
                        min = (player.charms.tdManhMe - System.currentTimeMillis()) / 60000;
                        break;
                    case 215:
                        min = (player.charms.tdDaTrau - System.currentTimeMillis()) / 60000;
                        break;
                    case 216:
                        min = (player.charms.tdOaiHung - System.currentTimeMillis()) / 60000;
                        break;
                    case 217:
                        min = (player.charms.tdBatTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 218:
                        min = (player.charms.tdDeoDai - System.currentTimeMillis()) / 60000;
                        break;
                    case 219:
                        min = (player.charms.tdThuHut - System.currentTimeMillis()) / 60000;
                        break;
                    case 522:
                        min = (player.charms.tdDeTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 671:
                        min = (player.charms.tdTriTue3 - System.currentTimeMillis()) / 60000;
                        break;
                    case 672:
                        min = (player.charms.tdTriTue4 - System.currentTimeMillis()) / 60000;
                        break;
                }
                if (min > 0) {
                    item.options.clear();
                    if (min >= 1440) {
                        item.options.add(new Item.ItemOption(63, (int) min / 1440));
                    } else if (min >= 60) {
                        item.options.add(new Item.ItemOption(64, (int) min / 60));
                    } else {
                        item.options.add(new Item.ItemOption(65, (int) min));
                    }
                }
            }
        }
        return s;
    }

    /**
     * Mở cửa hàng thông thường (type 0) và gửi thông tin cửa hàng đến người chơi.
     *
     * @param player Người chơi mở cửa hàng.
     * @param shop Cửa hàng cần mở.
     */
    private void openShopType0(Player player, Shop shop) {
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {
            Message msg;
            try {
                msg = new Message(-44);
                msg.writer().writeByte(NORMAL_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {
                        msg.writer().writeShort(itemShop.temp.id);
                        if (itemShop.typeSell == COST_GOLD) {
                            msg.writer().writeInt(itemShop.cost);
                            msg.writer().writeInt(0);
                        } else if (itemShop.typeSell == COST_GEM) {
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_RUBY) {
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_COUPON) {
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        }
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e, "Lỗi khi mở cửa hàng loại 0");
            }
        }
    }

    /**
     * Mở cửa hàng đặc biệt (type 3) và gửi thông tin cửa hàng đến người chơi.
     *
     * @param player Người chơi mở cửa hàng.
     * @param shop Cửa hàng cần mở.
     */
    private void openShopType3(Player player, Shop shop) {
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {
            Message msg;
            try {
                msg = new Message(-44);
                msg.writer().writeByte(SPEC_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeShort(itemShop.iconSpec);
                        msg.writer().writeInt(itemShop.cost);
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e, "Lỗi khi mở cửa hàng loại 3");
            }
        }
    }

    /**
     * Mở rương phần thưởng (type 4) và gửi thông tin vật phẩm đến người chơi.
     *
     * @param player Người chơi mở rương.
     * @param tagName Tên thẻ của rương.
     * @param items Danh sách vật phẩm trong rương.
     */
    private void openShopType4(Player player, String tagName, List<Item> items) {
        if (items == null) {
            return;
        }
        player.iDMark.setTagNameShop(tagName);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(BOX);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(items.size());
            for (Item item : items) {
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|Ngọc Rồng Green");
                msg.writer().writeByte(item.itemOptions.size() + 1);
                for (Item.ItemOption io : item.itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                msg.writer().writeByte(1);
                if (item.template.type == 5) {
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(item.template.head);
                    msg.writer().writeShort(item.template.body);
                    msg.writer().writeShort(item.template.leg);
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(ShopServiceNew.class, e, "Lỗi khi mở rương phần thưởng");
        }
    }

    /**
     * Xử lý việc người chơi lấy vật phẩm từ cửa hàng hoặc rương.
     *
     * @param player Người chơi thực hiện hành động.
     * @param type Loại hành động (0: nhận, 1: xóa, 2: nhận hết).
     * @param tempId ID template của vật phẩm.
     * @param quantity Số lượng vật phẩm.
     */
    public void takeItem(Player player, byte type, int tempId, int quantity) {
        String tagName = player.iDMark.getTagNameShop();
        if (tagName == null || tagName.length() <= 0) {
            return;
        }
        if (tagName.equals("ITEMS_MAIL_BOX")) {
            getItemSideBoxLuckyRound(player, player.inventory.itemsMailBox, type, tempId);
            return;
        }
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {
            getItemSideBoxLuckyRound(player, player.inventory.itemsBoxCrackBall, type, tempId);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {
            return;
        }
        if (player.iDMark.getShopOpen() == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (tagName.equals("BUA_1H") || tagName.equals("BUA_8H") || tagName.equals("BUA_1M")) {
            buyItemBua(player, tempId);
        } else {
            buyItem(player, tempId);
        }
        Service.gI().sendMoney(player);
    }

    /**
     * Trừ tiền hoặc tài nguyên của người chơi dựa trên loại thanh toán của vật phẩm.
     *
     * @param player Người chơi thực hiện mua.
     * @param is Vật phẩm trong cửa hàng.
     * @return true nếu trừ thành công, false nếu không đủ tài nguyên.
     */
    private boolean subMoneyByItemShop(Player player, ItemShop is) {
        int gold = 0;
        int gem = 0;
        int ruby = 0;
        int coupon = 0;
        switch (is.typeSell) {
            case COST_GOLD:
                gold = is.cost;
                break;
            case COST_GEM:
                gem = is.cost;
                break;
            case COST_RUBY:
                ruby = is.cost;
                break;
            case COST_COUPON:
                coupon = is.cost;
                break;
        }
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Bạn không có đủ vàng");
            return false;
        } else if (player.inventory.gem < gem) {
            Service.gI().sendThongBao(player, "Bạn không có đủ ngọc");
            return false;
        } else if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc");
            return false;
        } else if (player.inventory.coupon < coupon) {
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm");
            return false;
        }
        player.inventory.gold -= is.temp.gold;
        player.inventory.gem -= is.temp.gem;
        player.inventory.ruby -= ruby;
        player.inventory.coupon -= coupon;
        return true;
    }

    /**
     * Xử lý việc mua bùa từ cửa hàng.
     *
     * @param player Người chơi thực hiện mua.
     * @param itemTempId ID template của bùa.
     */
    private void buyItemBua(Player player, int itemTempId) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (!subMoneyByItemShop(player, is)) {
            return;
        }
        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
        InventoryServiceNew.gI().sendItemBags(player);
        opendShop(player, shop.tagName, true);
    }

    /**
     * Xử lý việc mua vật phẩm từ cửa hàng.
     *
     * @param player Người chơi thực hiện mua.
     * @param itemTempId ID template của vật phẩm.
     */
    public void buyItem(Player player, int itemTempId) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }
        if (isHuyDietItem(is) && !hasEnoughThucAn(player)) {
            Service.gI().sendThongBao(player, "Không đủ thức ăn để mua đồ!");
            return;
        }
        if (shop.typeShop == NORMAL_SHOP) {
            if (!subMoneyByItemShop(player, is)) {
                return;
            }
        } else if (shop.typeShop == SPEC_SHOP) {
            if (!this.subIemByItemShop(player, is)) {
                return;
            }
        }
        Item item = ItemService.gI().createItemFromItemShop(is);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name);
    }

    /**
     * Kiểm tra xem vật phẩm có phải là vật phẩm hủy diệt hay không.
     *
     * @param itemShop Vật phẩm trong cửa hàng.
     * @return true nếu là vật phẩm hủy diệt, false nếu không.
     */
    private boolean isHuyDietItem(ItemShop itemShop) {
        int itemId = itemShop.temp.id;
        return itemId >= 650 && itemId <= 662;
    }

    /**
     * Kiểm tra xem người chơi có đủ thức ăn để mua vật phẩm hủy diệt hay không.
     *
     * @param player Người chơi cần kiểm tra.
     * @return true nếu có đủ thức ăn, false nếu không.
     */
    private boolean hasEnoughThucAn(Player player) {
        return player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.isThucAn() && item.quantity >= 99)
                .findFirst().isPresent();
    }

    /**
     * Trừ vật phẩm đặc biệt từ túi đồ của người chơi khi mua vật phẩm từ cửa hàng đặc biệt.
     *
     * @param pl Người chơi thực hiện mua.
     * @param itemShop Vật phẩm trong cửa hàng đặc biệt.
     * @return true nếu trừ thành công, false nếu không đủ vật phẩm.
     */
    private boolean subIemByItemShop(Player pl, ItemShop itemShop) {
        boolean isBuy = false;
        short itSpec = ItemService.gI().getItemIdByIcon((short) itemShop.iconSpec);
        int buySpec = itemShop.cost;
        Item itS = ItemService.gI().createNewItem(itSpec);
        switch (itS.template.id) {
            case 861:
            case 188:
            case 189:
            case 190:
                if (pl.inventory.ruby >= buySpec) {
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Vàng Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;
            case 76:
                if (pl.inventory.ruby >= buySpec) {
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Hồng Ngọc Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;
            default:
                if (InventoryServiceNew.gI().findItemBag(pl, itSpec) == null || !InventoryServiceNew.gI().findItemBag(pl, itSpec).isNotNullItem()) {
                    Service.gI().sendThongBao(pl, "Không tìm thấy " + itS.template.name);
                    isBuy = false;
                } else if (InventoryServiceNew.gI().findItemBag(pl, itSpec).quantity < buySpec) {
                    Service.gI().sendThongBao(pl, "Bạn không có đủ " + buySpec + " " + itS.template.name);
                    isBuy = false;
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, itSpec), buySpec);
                    isBuy = true;
                }
                break;
        }
        return isBuy;
    }

    /**
     * Hiển thị xác nhận bán vật phẩm cho người chơi.
     *
     * @param pl Người chơi thực hiện bán.
     * @param where Vị trí vật phẩm (0: trên người, khác: trong túi).
     * @param index Vị trí của vật phẩm trong danh sách.
     */
    public void showConfirmSellItem(Player pl, int where, int index) {
        TransactionService.gI().cancelTrade(pl);
        if (index < 0) {
            return;
        }
        Item item = null;
        if (where == 0) {
            item = pl.inventory.itemsBody.get(index);
        } else {
            if (pl.getSession().version < 220) {
                index -= (pl.inventory.itemsBody.size() - 7);
            }
            if (index >= 0) {
                item = pl.inventory.itemsBag.get(index);
            } else {
                item = pl.inventory.itemsBag.get(0);
            }
        }
        if (item != null && item.isNotNullItem()) {
            int quantity = item.quantity;
            int cost = item.template.id == 457 ? item.template.gold : 1;
            if (item.template.id == 457) {
                quantity = 1;
            } else {
                cost /= 4;
            }
            if (cost == 0) {
                cost = 1;
            }
            cost *= quantity;

            String text = "Bạn có muốn bán\nx" + quantity
                    + " " + item.template.name + "\nvới giá là " + Util.numberToMoney(cost) + " vàng?";
            Message msg = new Message(7);
            try {
                msg.writer().writeByte(where);
                msg.writer().writeShort(index);
                msg.writer().writeUTF(text);
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(ShopServiceNew.class, e, "Lỗi khi hiển thị xác nhận bán vật phẩm");
            }
        }
    }

    /**
     * Xử lý việc bán vật phẩm của người chơi.
     *
     * @param pl Người chơi thực hiện bán.
     * @param where Vị trí vật phẩm (0: trên người, khác: trong túi).
     * @param index Vị trí của vật phẩm trong danh sách.
     */
    public void sellItem(Player pl, int where, int index) {
        Item item = null;
        if (index < 0) {
            return;
        }
        if (where == 0) {
            item = pl.inventory.itemsBody.get(index);
        } else {
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.template.id != 921 && item.template.id != 454 && item.template.id != 194) {
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457) {
                quantity = 1;
            } else {
                cost /= 4;
            }
            if (cost == 0) {
                cost = 1;
            }
            cost *= quantity;

            if (pl.inventory.gold + cost > Inventory.LIMIT_GOLD) {
                Service.gI().sendThongBao(pl, "Vàng sau khi bán vượt quá giới hạn");
                return;
            }
            pl.inventory.gold += cost;
            Service.gI().sendMoney(pl);
            Service.gI().sendThongBao(pl, "Đã bán " + item.template.name
                    + " thu được " + Util.numberToMoney(cost) + " vàng");
            if (where == 0) {
                InventoryServiceNew.gI().subQuantityItemsBody(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBody(pl);
                Service.gI().Send_Caitrang(pl);
            } else {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            Service.gI().sendThongBao(pl, "Không thể bán " + item.template.name + " này được");
        }
    }

    /**
     * Xử lý việc nhận hoặc xóa vật phẩm từ rương đặc biệt hoặc hộp thư.
     *
     * @param player Người chơi thực hiện hành động.
     * @param items Danh sách vật phẩm trong rương hoặc hộp thư.
     * @param type Loại hành động (0: nhận, 1: xóa, 2: nhận hết).
     * @param index Vị trí của vật phẩm trong danh sách.
     */
    private void getItemSideBoxLuckyRound(Player player, List<Item> items, byte type, int index) {
        if (items == null || items.isEmpty() || index >= items.size()) {
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        Item item = items.get(index);
        switch (type) {
            case 0: // nhận
                if (item.isNotNullItem()) {
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        InventoryServiceNew.gI().sendItemBags(player);
                        items.remove(index);
                    } else {
                        Service.gI().sendThongBao(player, "Hành trang đã đầy");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                }
                break;
            case 1: // xóa
                items.remove(index);
                Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                break;
            case 2: // nhận hết
                for (int i = items.size() - 1; i >= 0; i--) {
                    item = items.get(i);
                    if (InventoryServiceNew.gI().addItemBag(player, item)) {
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        items.remove(i);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                break;
        }
        openShopType4(player, player.iDMark.getTagNameShop(), items);
    }
}