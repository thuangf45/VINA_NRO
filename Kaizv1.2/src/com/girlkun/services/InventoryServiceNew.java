package com.girlkun.services;

import com.girlkun.models.item.Item;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp InventoryServiceNew quản lý các chức năng liên quan đến hành trang, rương đồ và trang bị của người chơi trong game.
 * Lớp này hỗ trợ tìm kiếm, thêm, xóa, sắp xếp vật phẩm, cũng như thực hiện các thao tác tháo/mặc trang bị.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class InventoryServiceNew {

    /** Thể hiện duy nhất của lớp InventoryServiceNew (singleton pattern) */
    private static InventoryServiceNew I;

    /**
     * Lấy thể hiện duy nhất của lớp InventoryServiceNew.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp InventoryServiceNew.
     */
    public static InventoryServiceNew gI() {
        if (InventoryServiceNew.I == null) {
            InventoryServiceNew.I = new InventoryServiceNew();
        }
        return InventoryServiceNew.I;
    }

    private void __________________Tìm_kiếm_item_____________________________() {
        //**********************************************************************
    }

    /**
     * Tìm vật phẩm trong danh sách theo ID mẫu.
     * 
     * @param list Danh sách vật phẩm cần tìm.
     * @param tempId ID mẫu của vật phẩm.
     * @return Vật phẩm tìm thấy, hoặc null nếu không tìm thấy.
     */
    public Item findItem(List<Item> list, int tempId) {
        try {
            for (Item item : list) {
                if (item.isNotNullItem() && item.template.id == tempId) {
                    return item;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Tìm vật phẩm trong trang bị trên người.
     * 
     * @param player Người chơi cần tìm vật phẩm.
     * @param tempId ID mẫu của vật phẩm.
     * @return Vật phẩm tìm thấy, hoặc null nếu không tìm thấy.
     */
    public Item findItemBody(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBody, tempId);
    }

    /**
     * Tìm vật phẩm trong hành trang.
     * 
     * @param player Người chơi cần tìm vật phẩm.
     * @param tempId ID mẫu của vật phẩm.
     * @return Vật phẩm tìm thấy, hoặc null nếu không tìm thấy.
     */
    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    /**
     * Tìm vật phẩm trong rương đồ.
     * 
     * @param player Người chơi cần tìm vật phẩm.
     * @param tempId ID mẫu của vật phẩm.
     * @return Vật phẩm tìm thấy, hoặc null nếu không tìm thấy.
     */
    public Item findItemBox(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBox, tempId);
    }

    /**
     * Kiểm tra sự tồn tại của vật phẩm trong danh sách theo ID mẫu.
     * 
     * @param list Danh sách vật phẩm cần kiểm tra.
     * @param tempId ID mẫu của vật phẩm.
     * @return true nếu vật phẩm tồn tại, false nếu không.
     */
    public boolean isExistItem(List<Item> list, int tempId) {
        try {
            this.findItem(list, tempId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Kiểm tra sự tồn tại của vật phẩm trong trang bị trên người.
     * 
     * @param player Người chơi cần kiểm tra.
     * @param tempId ID mẫu của vật phẩm.
     * @return true nếu vật phẩm tồn tại, false nếu không.
     */
    public boolean isExistItemBody(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBody, tempId);
    }

    /**
     * Kiểm tra sự tồn tại của vật phẩm trong hành trang.
     * 
     * @param player Người chơi cần kiểm tra.
     * @param tempId ID mẫu của vật phẩm.
     * @return true nếu vật phẩm tồn tại, false nếu không.
     */
    public boolean isExistItemBag(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBag, tempId);
    }

    /**
     * Kiểm tra sự tồn tại của vật phẩm trong rương đồ.
     * 
     * @param player Người chơi cần kiểm tra.
     * @param tempId ID mẫu của vật phẩm.
     * @return true nếu vật phẩm tồn tại, false nếu không.
     */
    public boolean isExistItemBox(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBox, tempId);
    }

    private void __________________Sao_chép_danh_sách_item__________________() {
        //**********************************************************************
    }

    /**
     * Sao chép danh sách vật phẩm.
     * 
     * @param items Danh sách vật phẩm cần sao chép.
     * @return Danh sách vật phẩm mới được sao chép.
     */
    public List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    /**
     * Sao chép danh sách vật phẩm trong trang bị trên người.
     * 
     * @param player Người chơi cần sao chép.
     * @return Danh sách vật phẩm mới được sao chép.
     */
    public List<Item> copyItemsBody(Player player) {
        return copyList(player.inventory.itemsBody);
    }

    /**
     * Sao chép danh sách vật phẩm trong hành trang.
     * 
     * @param player Người chơi cần sao chép.
     * @return Danh sách vật phẩm mới được sao chép.
     */
    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    /**
     * Sao chép danh sách vật phẩm trong rương đồ.
     * 
     * @param player Người chơi cần sao chép.
     * @return Danh sách vật phẩm mới được sao chép.
     */
    public List<Item> copyItemsBox(Player player) {
        return copyList(player.inventory.itemsBox);
    }

    private void __________________Vứt_bỏ_item______________________________() {
        //**********************************************************************
    }

    /**
     * Vứt bỏ vật phẩm từ hành trang hoặc trang bị trên người.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param where Vị trí vứt bỏ (0: trang bị trên người, 1: hành trang).
     * @param index Vị trí của vật phẩm trong danh sách.
     */
    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            itemThrow = player.inventory.itemsBody.get(index);
            removeItemBody(player, index);
            sendItemBody(player);
            Service.gI().Send_Caitrang(player);
        } else if (where == 1) {
            itemThrow = player.inventory.itemsBag.get(index);
            if (itemThrow.template.id != 1120) {
                removeItemBag(player, index);
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Sài Đi Đừng Vứt");
            }
        }
        Service.gI().sendFlagBag(player);
        if (itemThrow == null) {
            return;
        }
    }

    private void __________________Xoá_bỏ_item______________________________() {
        //**********************************************************************
    }

    /**
     * Xóa vật phẩm tại vị trí cụ thể trong danh sách.
     * 
     * @param items Danh sách vật phẩm.
     * @param index Vị trí của vật phẩm cần xóa.
     */
    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    /**
     * Xóa vật phẩm cụ thể trong danh sách.
     * 
     * @param items Danh sách vật phẩm.
     * @param item Vật phẩm cần xóa.
     */
    public void removeItem(List<Item> items, Item item) {
        if (item == null) {
            return;
        }
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                item.dispose();
                break;
            }
        }
    }

    /**
     * Xóa vật phẩm tại vị trí cụ thể trong hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí của vật phẩm cần xóa.
     */
    public void removeItemBag(Player player, int index) {
        this.removeItem(player.inventory.itemsBag, index);
    }

    /**
     * Xóa vật phẩm cụ thể trong hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần xóa.
     */
    public void removeItemBag(Player player, Item item) {
        this.removeItem(player.inventory.itemsBag, item);
    }

    /**
     * Xóa vật phẩm tại vị trí cụ thể trong trang bị trên người.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí của vật phẩm cần xóa.
     */
    public void removeItemBody(Player player, int index) {
        this.removeItem(player.inventory.itemsBody, index);
    }

    /**
     * Xóa vật phẩm tại vị trí cụ thể trong trang bị của đệ tử.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí của vật phẩm cần xóa.
     */
    public void removeItemPetBody(Player player, int index) {
        this.removeItemBody(player.pet, index);
    }

    /**
     * Xóa vật phẩm tại vị trí cụ thể trong rương đồ.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí của vật phẩm cần xóa.
     */
    public void removeItemBox(Player player, int index) {
        this.removeItem(player.inventory.itemsBox, index);
    }

    private void __________________Giảm_số_lượng_item_______________________() {
        //**********************************************************************
    }

    /**
     * Giảm số lượng vật phẩm trong hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần giảm số lượng.
     * @param quantity Số lượng cần giảm.
     */
    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    /**
     * Giảm số lượng vật phẩm trong trang bị trên người.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần giảm số lượng.
     * @param quantity Số lượng cần giảm.
     */
    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    /**
     * Giảm số lượng vật phẩm trong rương đồ.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần giảm số lượng.
     * @param quantity Số lượng cần giảm.
     */
    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    /**
     * Giảm số lượng vật phẩm trong danh sách.
     * 
     * @param items Danh sách vật phẩm.
     * @param item Vật phẩm cần giảm số lượng.
     * @param quantity Số lượng cần giảm.
     */
    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        this.removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    private void __________________Sắp_xếp_danh_sách_item___________________() {
        //**********************************************************************
    }

    /**
     * Sắp xếp danh sách vật phẩm, đưa vật phẩm không rỗng lên đầu.
     * 
     * @param list Danh sách vật phẩm cần sắp xếp.
     */
    public void sortItems(List<Item> list) {
        int first = -1;
        int last = -1;
        Item tempFirst = null;
        Item tempLast = null;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isNotNullItem()) {
                first = i;
                tempFirst = list.get(i);
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isNotNullItem()) {
                last = i;
                tempLast = list.get(i);
                break;
            }
        }
        if (first != -1 && last != -1 && first < last) {
            list.set(first, tempLast);
            list.set(last, tempFirst);
            sortItems(list);
        }
    }

    private void __________________Thao_tác_tháo_mặc_item___________________() {
        //**********************************************************************
    }

    /**
     * Đặt vật phẩm vào hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần đặt.
     * @return Vật phẩm rỗng nếu đặt thành công, hoặc vật phẩm ban đầu nếu không đủ chỗ.
     */
    private Item putItemBag(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            if (!player.inventory.itemsBag.get(i).isNotNullItem()) {
                player.inventory.itemsBag.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    /**
     * Đặt vật phẩm vào rương đồ.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần đặt.
     * @return Vật phẩm rỗng nếu đặt thành công, hoặc vật phẩm ban đầu nếu không đủ chỗ.
     */
    private Item putItemBox(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
            if (!player.inventory.itemsBox.get(i).isNotNullItem()) {
                player.inventory.itemsBox.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    /**
     * Đặt vật phẩm vào trang bị trên người.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần đặt.
     * @return Vật phẩm hiện tại ở vị trí trang bị nếu hoán đổi thành công, hoặc vật phẩm ban đầu nếu không hợp lệ.
     */
    private Item putItemBody(Player player, Item item) {
        Item sItem = item;
        if (!item.isNotNullItem()) {
            return sItem;
        }
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 32:
            case 23:
            case 24:
            case 11:
            case 72:
            case 27:
            case 21:
            case 74:
                break;
            default:
                Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
                return sItem;
        }
        if (item.template.gender < 3 && item.template.gender != player.gender) {
            Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
            return sItem;
        }
        long powerRequire = item.template.strRequire;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 21) {
                powerRequire = io.param * 1000000000L;
                break;
            }
        }
        if (player.nPoint.power < powerRequire) {
            Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Sức mạnh không đủ yêu cầu!");
            return sItem;
        }
        int index = -1;
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                index = item.template.type;
                break;
            case 32:
                index = 6;
                break;
            case 23:
            case 24:
                index = 9;
                break;
            case 11:
                index = 8;
                break;
            case 72:
                index = 10;
                break;
            case 27:
                index = 7;
                break;
            case 21:
                index = 7;
                break;
            case 74:
                index = 11;
                break;
        }
        sItem = player.inventory.itemsBody.get(index);
        player.inventory.itemsBody.set(index, item);
        return sItem;
    }

    /**
     * Chuyển vật phẩm từ hành trang sang trang bị trên người.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong hành trang.
     */
    public void itemBagToBody(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBag.set(index, putItemBody(player, item));
            if (item.template.id > 1350 && item.template.id < 1359) {
                Service.gI().removeTitle(player);
                Service.gI().sendFoot(player, item.template.id);
            }
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
        }
    }

    /**
     * Chuyển vật phẩm từ trang bị trên người sang hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong trang bị trên người.
     */
    public void itemBodyToBag(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (index == 10) {
                Service.getInstance().sendPetFollow(player, (short) 0);
            }
            if (index == 7) {
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                    player.newpet.dispose();
                    player.newpet = null;
                }
            }
            player.inventory.itemsBody.set(index, putItemBag(player, item));
            if (item.isNotNullItem()) {
                Service.gI().removeTitle(player);
                Service.gI().sendFoot(player, item.template.id);
            }
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
        }
    }

    /**
     * Chuyển vật phẩm từ hành trang sang trang bị của đệ tử.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong hành trang.
     */
    public void itemBagToPetBody(Player player, int index) {
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
            Item item = player.inventory.itemsBag.get(index);
            if (item.isNotNullItem()) {
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
                sendItemBags(player);
                sendItemBody(player);
                Service.getInstance().Send_Caitrang(player.pet);
                Service.getInstance().Send_Caitrang(player);
                if (!itemSwap.equals(item)) {
                    Service.getInstance().point(player);
                    Service.getInstance().showInfoPet(player);
                }
            }
        } else {
            Service.getInstance().sendThongBaoOK(player, "Đệ tử phải đạt 1tr5 sức mạnh mới có thể mặc");
        }
    }

    /**
     * Chuyển vật phẩm từ trang bị của đệ tử sang hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong trang bị của đệ tử.
     */
    public void itemPetBodyToBag(Player player, int index) {
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player.pet);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            Service.getInstance().showInfoPet(player);
        }
    }

    /**
     * Chuyển vật phẩm từ rương đồ sang hành trang hoặc trang bị trên người.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong rương đồ.
     */
    public void itemBoxToBodyOrBag(Player player, int index) {
        Item item = player.inventory.itemsBox.get(index);
        if (item.isNotNullItem()) {
            boolean done = false;
            if (item.template.type >= 0 && item.template.type <= 5 || item.template.type == 32) {
                Item itemBody = player.inventory.itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                if (!itemBody.isNotNullItem()) {
                    if (item.template.gender == player.gender || item.template.gender == 3) {
                        long powerRequire = item.template.strRequire;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 21) {
                                powerRequire = io.param * 1000000000L;
                                break;
                            }
                        }
                        if (powerRequire <= player.nPoint.power) {
                            player.inventory.itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                            player.inventory.itemsBox.set(index, itemBody);
                            done = true;

                            sendItemBody(player);
                            Service.gI().Send_Caitrang(player);
                            Service.gI().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox(player);
        }
    }

    /**
     * Chuyển vật phẩm từ hành trang sang rương đồ.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong hành trang.
     */
    public void itemBagToBox(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item != null) {
            if (item.template.id == 457 || item.template.id == 674) {
                Service.gI().sendThongBao(player, "Không thể cất vàng vào rương");
                return;
            }
            if (addItemBox(player, item)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox(player);
            }
        }
    }

    /**
     * Chuyển vật phẩm từ trang bị trên người sang rương đồ.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param index Vị trí vật phẩm trong trang bị trên người.
     */
    public void itemBodyToBox(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            sortItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox(player);
            Service.gI().Send_Caitrang(player);
            sendItemBody(player);
            Service.gI().point(player);
        }
    }

    private void __________________Gửi_danh_sách_item_cho_người_chơi________() {
        //**********************************************************************
    }

    /**
     * Gửi danh sách vật phẩm trong hành trang cho người chơi.
     * 
     * @param player Người chơi nhận danh sách.
     */
    public void sendItemBags(Player player) {
        sortItems(player.inventory.itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBag.size());
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); // options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi danh sách vật phẩm trong trang bị trên người cho người chơi.
     * 
     * @param player Người chơi nhận danh sách.
     */
    public void sendItemBody(Player player) {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeByte(player.inventory.itemsBody.size());
            for (Item item : player.inventory.itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<Item.ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (Item.ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        Service.gI().Send_Caitrang(player);
    }

    /**
     * Gửi danh sách vật phẩm trong rương đồ cho người chơi.
     * 
     * @param player Người chơi nhận danh sách.
     */
    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (Item.ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.openBox(player);
    }

    /**
     * Mở rương đồ cho người chơi.
     * 
     * @param player Người chơi thực hiện hành động.
     */
    public void openBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void __________________Thêm_vật_phẩm_vào_danh_sách______________() {
        //**********************************************************************
    }

    /**
     * Thêm vật phẩm đặc biệt (như bùa, tàu tennis, đùi gà nướng) vào hành trang hoặc xử lý đặc biệt.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần thêm.
     * @return true nếu thêm thành công, false nếu không.
     */
    private boolean addItemSpecial(Player player, Item item) {
        // bùa
        if (item.template.type == 13) {
            int min = 0;
            try {
                String tagShopBua = player.iDMark.getShopOpen().tagName;
                if (tagShopBua.equals("BUA_1H")) {
                    min = 60;
                } else if (tagShopBua.equals("BUA_8H")) {
                    min = 60 * 8;
                } else if (tagShopBua.equals("BUA_1M")) {
                    min = 60 * 24 * 30;
                }
            } catch (Exception e) {
            }
            player.charms.addTimeCharms(item.template.id, min);
            return true;
        }

        switch (item.template.id) {
            case 453: // tàu tennis
                player.haveTennisSpaceShip = true;
                return true;
            case 74: // đùi gà nướng
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return true;
        }
        return false;
    }

    /**
     * Thêm vật phẩm vào hành trang.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần thêm.
     * @return true nếu thêm thành công, false nếu không.
     */
    public boolean addItemBag(Player player, Item item) {
        // ngọc rồng đen
        if (ItemMapService.gI().isBlackBall(item.template.id)) {
            return BlackBallWar.gI().pickBlackBall(player, item);
        }
        if (addItemSpecial(player, item)) {
            return true;
        }

        // gold, gem, ruby
        switch (item.template.type) {
            case 9:
                if (player.inventory.gold + item.quantity <= Inventory.LIMIT_GOLD) {
                    player.inventory.gold += item.quantity;
                    Service.gI().sendMoney(player);
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Vàng sau khi nhặt quá giới hạn cho phép");
                    return false;
                }
            case 10:
                player.inventory.gem += item.quantity;
                Service.gI().sendMoney(player);
                return true;
            case 34:
                player.inventory.ruby += item.quantity;
                Service.gI().sendMoney(player);
                return true;
        }

        // mở rộng hành trang - rương đồ
        if (item.template.id == 517) {
            if (player.inventory.itemsBag.size() < Inventory.MAX_ITEMS_BAG) {
                player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                Service.gI().sendThongBaoOK(player, "Hành trang của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.gI().sendThongBaoOK(player, "Hành trang của bạn đã đạt tối đa");
                return false;
            }
        } else if (item.template.id == 518) {
            if (player.inventory.itemsBox.size() < Inventory.MAX_ITEMS_BOX) {
                player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                Service.gI().sendThongBaoOK(player, "Rương đồ của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.gI().sendThongBaoOK(player, "Rương đồ của bạn đã đạt tối đa");
                return false;
            }
        }
        return addItemList(player.inventory.itemsBag, item);
    }

    /**
     * Thêm vật phẩm vào rương đồ.
     * 
     * @param player Người chơi thực hiện hành động.
     * @param item Vật phẩm cần thêm.
     * @return true nếu thêm thành công, false nếu không.
     */
    public boolean addItemBox(Player player, Item item) {
        return addItemList(player.inventory.itemsBox, item);
    }

    /**
     * Thêm vật phẩm vào danh sách vật phẩm.
     * 
     * @param items Danh sách vật phẩm.
     * @param itemAdd Vật phẩm cần thêm.
     * @return true nếu thêm thành công, false nếu không.
     */
    public boolean addItemList(List<Item> items, Item itemAdd) {
        // nếu item ko có option, add option rỗng vào
        if (itemAdd.itemOptions.isEmpty()) {
            itemAdd.itemOptions.add(new Item.ItemOption(73, 0));
        }

        // item cộng thêm chỉ số param: tự động luyện tập
        int[] idParam = isItemIncrementalOption(itemAdd);
        if (idParam[0] != -1) {
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == itemAdd.template.id) {
                    for (Item.ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == idParam[0]) {
                            io.param += idParam[1];
                        }
                    }
                    return true;
                }
            }
        }

        // item tăng số lượng
        if (itemAdd.template.isUpToUp) {
            for (Item it : items) {
                if (!it.isNotNullItem() || it.template.id != itemAdd.template.id) {
                    continue;
                }
                // 457-thỏi vàng; 590-bí kiếp
                if (itemAdd.template.id == 457
                        || itemAdd.template.id == 590
                        || itemAdd.template.id == 610
                        || itemAdd.template.type == 14
                        || itemAdd.template.type == 933
                        || itemAdd.template.type == 934
                        || itemAdd.template.id == 537
                        || itemAdd.template.id == 538
                        || itemAdd.template.type == 539
                        || itemAdd.template.type == 541
                        || itemAdd.template.type == 542
                        || itemAdd.template.id == 2069
                        || itemAdd.template.type == 540
                        || itemAdd.template.id == 886
                        || itemAdd.template.id == 887
                        || itemAdd.template.id == 888
                        || itemAdd.template.id == 889) {
                    it.quantity += itemAdd.quantity;
                    itemAdd.quantity = 0;
                    return true;
                }

                if (it.quantity < 9999) {
                    int add = 9999 - it.quantity;
                    if (itemAdd.quantity <= add) {
                        it.quantity += itemAdd.quantity;
                        itemAdd.quantity = 0;
                        return true;
                    } else {
                        it.quantity = 9999;
                        itemAdd.quantity -= add;
                    }
                }
            }
        }

        // add item vào ô mới
        if (itemAdd.quantity > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isNotNullItem()) {
                    items.set(i, ItemService.gI().copyItem(itemAdd));
                    itemAdd.quantity = 0;
                    return true;
                }
            }
        }
        return false;
    }

    private void __________________Kiểm_tra_điều_kiện_vật_phẩm______________() {
        //**********************************************************************
    }

    /**
     * Kiểm tra vật phẩm có phải là vật phẩm tăng chỉ số option hay không.
     *
     * @param item Vật phẩm cần kiểm tra.
     * @return Mảng chứa ID option và param nếu là vật phẩm tăng chỉ số, ngược lại trả về {-1, -1}.
     */
    private int[] isItemIncrementalOption(Item item) {
        for (Item.ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
                case 1:
                    return new int[]{io.optionTemplate.id, io.param};
            }
        }
        return new int[]{-1, -1};
    }

    private void __________________Kiểm_tra_danh_sách_còn_chỗ_trống_________() {
        //**********************************************************************
    }

    /**
     * Đếm số ô trống trong hành trang.
     * 
     * @param player Người chơi cần kiểm tra.
     * @return Số ô trống trong hành trang.
     */
    public byte getCountEmptyBag(Player player) {
        return getCountEmptyListItem(player.inventory.itemsBag);
    }

    /**
     * Đếm số ô trống trong danh sách vật phẩm.
     * 
     * @param list Danh sách vật phẩm cần kiểm tra.
     * @return Số ô trống trong danh sách.
     */
    public byte getCountEmptyListItem(List<Item> list) {
        byte count = 0;
        for (Item item : list) {
            if (!item.isNotNullItem()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tìm vị trí của vật phẩm trong hành trang.
     * 
     * @param pl Người chơi cần kiểm tra.
     * @param it Vật phẩm cần tìm.
     * @return Vị trí của vật phẩm, hoặc -1 nếu không tìm thấy.
     */
    public byte getIndexBag(Player pl, Item it) {
        for (byte i = 0; i < pl.inventory.itemsBag.size(); ++i) {
            Item item = pl.inventory.itemsBag.get(i);
            if (item != null && it.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Kiểm tra xem người chơi có rương gỗ trong hành trang hoặc rương đồ hay không.
     * 
     * @param player Người chơi cần kiểm tra.
     * @return true nếu không có rương gỗ, false nếu có.
     */
    public boolean finditemWoodChest(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        return true;
    }
}