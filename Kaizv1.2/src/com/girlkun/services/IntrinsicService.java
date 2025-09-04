package com.girlkun.services;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.intrinsic.Intrinsic;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.utils.Util;
import java.util.List;

/**
 * Lớp IntrinsicService quản lý các chức năng liên quan đến nội tại (intrinsic) trong game, bao gồm hiển thị thông tin nội tại,
 * mở hoặc thay đổi nội tại, và cấp vật phẩm set hủy diệt. Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class IntrinsicService {

    /** Thể hiện duy nhất của lớp IntrinsicService (singleton pattern) */
    private static IntrinsicService I;
    /** Mảng chi phí vàng để mở nội tại theo số lần mở */
    private static final int[] COST_OPEN = {10, 20, 40, 80, 160, 320, 640, 1280};

    /**
     * Lấy thể hiện duy nhất của lớp IntrinsicService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp IntrinsicService.
     */
    public static IntrinsicService gI() {
        if (IntrinsicService.I == null) {
            IntrinsicService.I = new IntrinsicService();
        }
        return IntrinsicService.I;
    }

    /**
     * Lấy danh sách nội tại dựa trên giới tính của nhân vật.
     * 
     * @param playerGender Giới tính của nhân vật (0: Trái Đất, 1: Namek, khác: Xayda).
     * @return Danh sách các nội tại tương ứng với giới tính.
     */
    public List<Intrinsic> getIntrinsics(byte playerGender) {
        switch (playerGender) {
            case 0:
                return Manager.INTRINSIC_TD;
            case 1:
                return Manager.INTRINSIC_NM;
            default:
                return Manager.INTRINSIC_XD;
        }
    }

    /**
     * Lấy thông tin nội tại dựa trên ID.
     * 
     * @param id ID của nội tại cần tìm.
     * @return Đối tượng Intrinsic nếu tìm thấy, ngược lại trả về null.
     */
    public Intrinsic getIntrinsicById(int id) {
        for (Intrinsic intrinsic : Manager.INTRINSICS) {
            if (intrinsic.id == id) {
                return new Intrinsic(intrinsic);
            }
        }
        return null;
    }

    /**
     * Gửi thông tin nội tại hiện tại của người chơi.
     * 
     * @param player Người chơi cần gửi thông tin nội tại.
     */
    public void sendInfoIntrinsic(Player player) {
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.playerIntrinsic.intrinsic.icon);
            msg.writer().writeUTF(player.playerIntrinsic.intrinsic.getName());
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi thông tin
        }
    }

    /**
     * Hiển thị danh sách tất cả nội tại có thể chọn cho người chơi.
     * 
     * @param player Người chơi cần xem danh sách nội tại.
     */
    public void showAllIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(1); // count tab
            msg.writer().writeUTF("Nội tại");
            msg.writer().writeByte(listIntrinsic.size() - 1);
            for (int i = 1; i < listIntrinsic.size(); i++) {
                msg.writer().writeShort(listIntrinsic.get(i).icon);
                msg.writer().writeUTF(listIntrinsic.get(i).getDescription());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi thông tin
        }
    }

    /**
     * Hiển thị menu nội tại cho người chơi.
     * 
     * @param player Người chơi cần mở menu nội tại.
     */
    public void showMenu(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.INTRINSIC, -1,
                "Nội tại là một kỹ năng bị động hỗ trợ đặc biệt\nBạn có muốn mở hoặc thay đổi nội tại không?",
                "Xem\ntất cả\nNội Tại", "Mở\nNội Tại", "Mở VIP", "Từ chối");
    }

    /**
     * Xử lý lựa chọn nội tại cho nhân vật Trái Đất.
     * 
     * @param player Người chơi thực hiện lựa chọn.
     * @param id ID của menu hoặc hành động.
     */
    public void sattd(Player player, int id) {
        if (id == 1105) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.menutd, -1,
                    "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nkamejoko", "Từ chối");
        }
        if (id == 1985 || id == 1986 || id == 1987) {
            NpcService.gI().createMenuConMeo(player, id * 10, -1,
                    "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nkamejoko", "Từ chối");
        }
        if (id == 19800) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1980);
            Item ao = ItemService.gI().otphd((short)650);
            Item quan = ItemService.gI().otphd((short)651);
            Item gang = ItemService.gI().otphd((short)657);
            Item giay = ItemService.gI().otphd((short)658);
            Item nhan = ItemService.gI().otphd((short)656);
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
        if (id == 19801) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1980);
            Item ao = ItemService.gI().otphd((short)652);
            Item quan = ItemService.gI().otphd((short)653);
            Item gang = ItemService.gI().otphd((short)659);
            Item giay = ItemService.gI().otphd((short)660);
            Item nhan = ItemService.gI().otphd((short)656);
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
        if (id == 19802) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1980);
            Item ao = ItemService.gI().otphd((short)654);
            Item quan = ItemService.gI().otphd((short)655);
            Item gang = ItemService.gI().otphd((short)661);
            Item giay = ItemService.gI().otphd((short)662);
            Item nhan = ItemService.gI().otphd((short)656);
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    /**
     * Xử lý lựa chọn nội tại cho nhân vật Namek.
     * 
     * @param player Người chơi thực hiện lựa chọn.
     * @param id ID của menu hoặc hành động.
     */
    public void satnm(Player player, int id) {
        if (id == 1105) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.menunm, -1,
                    "chọn lẹ đi để tau đi chơi với ny", "Set\ngod ki", "Set\ngod dame", "Set\nsummon", "Từ chối");
        }
        if (id == 1985 || id == 1986 || id == 1987) {
            NpcService.gI().createMenuConMeo(player, (id * 10) + 1, -1,
                    "chọn lẹ đi để tau đi chơi với ny", "Set\ngod ki", "Set\ngod dame", "Set\nsummon", "Từ chối");
        }
    }

    /**
     * Xử lý lựa chọn nội tại cho nhân vật Xayda.
     * 
     * @param player Người chơi thực hiện lựa chọn.
     * @param id ID của menu hoặc hành động.
     */
    public void setxd(Player player, int id) {
        if (id == 1105) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.menuxd, -1,
                    "chọn lẹ đi để tau đi chơi với ny", "Set\ngod galick", "Set\nmonkey", "Set\ngod hp", "Từ chối");
        }
        if (id == 1985 || id == 1986 || id == 1987) {
            NpcService.gI().createMenuConMeo(player, (id * 10) + 2, -1,
                    "chọn lẹ đi để tau đi chơi với ny", "Set\ngod galick", "Set\nmonkey", "Set\ngod hp", "Từ chối");
        }
    }

    /**
     * Hiển thị menu xác nhận mở nội tại với chi phí vàng.
     * 
     * @param player Người chơi cần xác nhận mở nội tại.
     */
    public void showConfirmOpen(Player player) {
        try {
            NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC, -1, "Bạn muốn đổi Nội Tại khác\nvới giá là "
                    + COST_OPEN[player.playerIntrinsic.countOpen] + " Tr vàng ?", "Mở\nNội Tại", "Từ chối");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Bỏ qua lỗi để tránh gián đoạn xử lý
        }
    }

    /**
     * Hiển thị menu xác nhận mở nội tại VIP với chi phí ngọc.
     * 
     * @param player Người chơi cần xác nhận mở nội tại VIP.
     */
    public void showConfirmOpenVip(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP, -1,
                "Bạn có muốn mở Nội Tại\nvới giá là 100 ngọc và\ntái lập giá vàng quay lại ban đầu không?", "Mở\nNội VIP", "Từ chối");
    }

    /**
     * Thay đổi nội tại ngẫu nhiên cho người chơi.
     * 
     * @param player Người chơi cần thay đổi nội tại.
     */
    private void changeIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        player.playerIntrinsic.intrinsic = new Intrinsic(listIntrinsic.get(Util.nextInt(1, listIntrinsic.size() - 1)));
        player.playerIntrinsic.intrinsic.param1 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom1, player.playerIntrinsic.intrinsic.paramTo1);
        player.playerIntrinsic.intrinsic.param2 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom2, player.playerIntrinsic.intrinsic.paramTo2);
        Service.gI().sendThongBao(player, "Bạn nhận được Nội tại:\n" + player.playerIntrinsic.intrinsic.getName().substring(0, player.playerIntrinsic.intrinsic.getName().indexOf(" [")));
        sendInfoIntrinsic(player);
    }

    /**
     * Mở nội tại mới với chi phí vàng.
     * 
     * @param player Người chơi thực hiện mở nội tại.
     */
    public void open(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            int goldRequire = COST_OPEN[player.playerIntrinsic.countOpen] * 1000000;
            if (player.inventory.gold >= goldRequire) {
                player.inventory.gold -= goldRequire;
                PlayerService.gI().sendInfoHpMpMoney(player);
                changeIntrinsic(player);
                player.playerIntrinsic.countOpen++;
            } else {
                Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu "
                        + Util.numberToMoney(goldRequire - player.inventory.gold) + " vàng nữa");
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

    /**
     * Mở nội tại VIP với chi phí ngọc và tái lập số lần mở.
     * 
     * @param player Người chơi thực hiện mở nội tại VIP.
     */
    public void openVip(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            int gemRequire = 100;
            if (player.inventory.gem >= 100) {
                player.inventory.gem -= gemRequire;
                PlayerService.gI().sendInfoHpMpMoney(player);
                changeIntrinsic(player);
                player.playerIntrinsic.countOpen = 0;
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ ngọc, còn thiếu "
                        + (gemRequire - player.inventory.gem) + " ngọc nữa");
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }
}