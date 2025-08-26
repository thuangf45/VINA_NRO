package com.girlkun.models.Event;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Lớp xử lý các chức năng liên quan đến sự kiện câu cá của người chơi.
 * @author Lucifer
 */
public class CauCa {

    public Player player;
    public int PointCauCa = 0;
    public boolean IsCauCa;
    public long LastCauCa;
    public long LastSendTime;

    /**
     * Khởi tạo đối tượng sự kiện câu cá cho người chơi.
     * @param pl Người chơi tham gia sự kiện
     */
    public CauCa(Player pl) {
        this.player = pl;
    }

    /**
     * Bắt đầu quá trình câu cá.
     */
    public void StartCauCa() {
        if (player.zone != null && player.zone.map != null && player.zone.map.mapId == 178) {
            if (!IsCauCa) {
                if (player.inventory.gold >= 199000000) {
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                        /** Gửi thông báo thời gian câu cá */
                        SendTime();
                        IsCauCa = true;
                        LastCauCa = System.currentTimeMillis();
                        player.inventory.gold -= 199000000; // Trừ vàng
                        Service.gI().sendMoney(player); // Cập nhật tiền
                    } else {
                        Service.gI().sendThongBao(player, "Cần 1 ô trống trong hành trang");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Cần có 199tr vàng để có thể sử dụng cần câu");
                }
            } else {
                Service.gI().sendThongBao(player, "Hãy chờ cá cắn câu rồi hãy tiếp tục");
            }
        } else {
            Service.gI().sendThongBao(player, "Chỉ có thể sử dụng ở map câu cá");
        }
    }

    /**
     * Xử lý khi câu cá thành công, thưởng vật phẩm hoặc vàng ngẫu nhiên.
     */
    public void Done() {
        PointCauCa++; // Tăng điểm câu cá
        int random = Util.nextInt(100); // Tạo số ngẫu nhiên từ 0 đến 99
        if (random <= 20) {
            random = Util.nextInt(100);
            if (random <= 35) {
                Item item = ItemService.gI().createNewItem((short) 1103, 1); // Cá thường
                InventoryServiceNew.gI().addItemBag(player, item);
                Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
            } else if (random <= 70) {
                Item item = ItemService.gI().createNewItem((short) 1102, 1); // Cá hiếm
                InventoryServiceNew.gI().addItemBag(player, item);
                Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
            } else {
                int[] itc2 = new int[]{1099, 1100, 1101}; // Các vật phẩm cao cấp
                Item item = ItemService.gI().createNewItem((short) itc2[Util.nextInt(0, 2)], 1);
                InventoryServiceNew.gI().addItemBag(player, item);
                Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
            }
        } else if (random <= 30) {
            Item item = ItemService.gI().createNewItem((short) 16, 1); // Đậu thần cấp thấp
            InventoryServiceNew.gI().addItemBag(player, item);
            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
        } else if (random <= 40) {
            Item item = ItemService.gI().createNewItem((short) 17, 1); // Đậu thần cấp cao
            InventoryServiceNew.gI().addItemBag(player, item);
            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
        } else if (random <= 70) {
            int gold = Util.nextInt(50, 199); // Thưởng vàng ngẫu nhiên
            player.inventory.gold += gold * 1000000;
            Service.gI().sendThongBao(player, "Bạn nhận được " + gold + "tr vàng");
        } else if (random <= 96) {
            Item item = ItemService.gI().createNewItem((short) 1978, 1); // Vật phẩm đặc biệt
            item.itemOptions.add(new Item.ItemOption(30, 1)); // Tùy chọn cấm giao dịch
            InventoryServiceNew.gI().addItemBag(player, item);
            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
        } else if (random <= 97) {
            Item item = ItemService.gI().createNewItem((short) 1999, 1); // Vật phẩm hiếm
            item.itemOptions.add(new Item.ItemOption(30, 1)); // Tùy chọn cấm giao dịch
            InventoryServiceNew.gI().addItemBag(player, item);
            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
        } else {
            Item item = ItemService.gI().createNewItem((short) 1998, 1); // Vật phẩm đặc biệt khác
            item.itemOptions.add(new Item.ItemOption(30, 1)); // Tùy chọn cấm giao dịch
            InventoryServiceNew.gI().addItemBag(player, item);
            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
        }
        Service.gI().sendMoney(player); // Cập nhật tiền
        InventoryServiceNew.gI().sendItemBags(player); // Cập nhật hành trang
    }

    /**
     * Gửi thông báo thời gian chờ câu cá cho người chơi.
     */
    public void SendTime() {
        ItemTimeService.gI().sendTextTime(player, (byte) 0, "Câu cá : ", 10); // Gửi thông báo thời gian 10 giây
    }

    /**
     * Cập nhật trạng thái câu cá, kiểm tra thời gian để hoàn thành.
     */
    public void Update() {
        if (IsCauCa) {
            if (System.currentTimeMillis() - LastCauCa >= 10000) { // Sau 10 giây
                Done(); // Hoàn thành câu cá
                IsCauCa = false; // Đặt lại trạng thái
            }
        }
    }
}