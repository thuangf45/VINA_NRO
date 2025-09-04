package com.girlkun.services;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;

/**
 * Lớp NapVangService xử lý chức năng đổi Xu thành HRZ (vàng) trong game.
 * Cung cấp phương thức để người chơi đổi các mức Xu khác nhau thành số lượng HRZ tương ứng.
 * 
 * @author Lucifer
 */
public class NapVangService {
    
    /**
     * Xử lý yêu cầu đổi Xu thành HRZ dựa trên lựa chọn của người chơi.
     * Kiểm tra số dư Xu, chỗ trống trong hành trang, và thực hiện đổi HRZ nếu hợp lệ.
     * 
     * @param chon Mức Xu muốn đổi (20, 50, 100, hoặc 500).
     * @param p Người chơi thực hiện đổi Xu.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình xử lý.
     */
    public static void ChonGiaTien(int chon, Player p) throws Exception {
        switch (chon) {
            case 20: { // Đổi 20 Xu thành 200 HRZ
                if (p.session.vnd < 20) {
                    Service.gI().sendThongBao(p, "Bạn phải có tối thiểu 20 Xu");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 1998, 200);
                if (thoivang != null) {
                    p.session.vnd -= 20;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 200 HRZ");
                }
                break;
            }
            case 50: { // Đổi 50 Xu thành 500 HRZ
                if (p.session.vnd < 50) {
                    Service.gI().sendThongBao(p, "Bạn phải có tối thiểu 50 Xu");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 1998, 500);
                if (thoivang != null) {
                    p.session.vnd -= 50;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 500 HRZ");
                }
                break;
            }
            case 100: { // Đổi 100 Xu thành 1000 HRZ
                if (p.session.vnd < 100) {
                    Service.gI().sendThongBao(p, "Bạn phải có tối thiểu 100 Xu");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 1998, 1000);
                if (thoivang != null) {
                    p.session.vnd -= 100;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 1000 HRZ");
                }
                break;
            }
            case 500: { // Đổi 500 Xu thành 6000 HRZ
                if (p.session.vnd < 500) {
                    Service.gI().sendThongBao(p, "Bạn phải có tối thiểu 500 Xu");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 1998, 6000);
                if (thoivang != null) {
                    p.session.vnd -= 500;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 6000 HRZ");
                }
                break;
            }
        }
    }
}