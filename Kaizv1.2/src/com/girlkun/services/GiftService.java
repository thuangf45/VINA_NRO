package com.girlkun.services;

import com.girlkun.models.player.Player;
import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.GiftDAO;
import com.girlkun.models.item.Item;
import com.girlkun.result.GirlkunResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 * Lớp GiftService quản lý chức năng nhập mã gift code trong game, cho phép người chơi nhận vật phẩm, vàng, ngọc xanh hoặc ngọc hồng.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class GiftService {

    /** Thể hiện duy nhất của lớp GiftService (singleton pattern) */
    private static GiftService i;

    /**
     * Khởi tạo một đối tượng GiftService.
     * Constructor được đặt ở chế độ private để đảm bảo tính singleton.
     */
    private GiftService() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp GiftService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp GiftService.
     */
    public static GiftService gI() {
        if (i == null) {
            i = new GiftService();
        }
        return i;
    }

    /**
     * Xử lý việc nhập mã gift code của người chơi, kiểm tra tính hợp lệ và cấp phần thưởng tương ứng.
     * 
     * @param player Người chơi nhập mã gift code.
     * @param code Mã gift code được nhập.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình xử lý (lỗi cơ sở dữ liệu, định dạng JSON, v.v.).
     */
    public void giftCode(Player player, String code) throws Exception {
        try {
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM `gift` WHERE name = ?", code);
            GirlkunResultSet check = GirlkunDB.executeQuery("SELECT * FROM `history_gift` WHERE `player_id` = ? AND `name_gift` = ?", player.id, code);
            if (check.first()) {
                Service.getInstance().sendThongBaoOK(player, "Bạn đã nhập gift code này");
            } else {
                if (rs.first()) {
                    JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("item_id"));
                    int j;
                    int[] itemId = new int[jar.size()];
                    for (j = 0; j < jar.size(); j++) {
                        itemId[j] = Integer.parseInt(jar.get(j).toString());
                    }
                    jar = (JSONArray) JSONValue.parse(rs.getString("item_quantity"));
                    long[] itemQuantity = new long[jar.size()];
                    for (j = 0; j < jar.size(); j++) {
                        itemQuantity[j] = Long.parseLong(jar.get(j).toString());
                    }
                    int power = rs.getInt("power");
                    if (player.nPoint.power >= power) {
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                            if (itemId.length == itemQuantity.length) {
                                int i;
                                for (i = 0; i < itemId.length; i++) {
                                    if (itemId[i] == -3) {
                                        player.inventory.gold += (itemQuantity[i]);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " vàng ");
                                    } else if (itemId[i] == -2) {
                                        player.inventory.gem += (itemQuantity[i]);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " ngọc xanh ");
                                    } 
                                    else if (itemId[i] == 861) {
                                        player.inventory.ruby += (itemQuantity[i]);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " ngọc hồng ");
                                    } else {
                                        Item itemup = ItemService.gI().createNewItem((short) itemId[i], (int) itemQuantity[i]);
                                        itemup.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, itemup);
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " " + itemup.template.name);
                                    }
                                }
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }

                        // GirlkunDB.executeUpdate("insert into history_gift (player_id, name_gift) values()", player.id, code); //code cũ
                        GiftDAO.insertHistoryGift((int) player.id, code); // insert history gift
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Yêu cầu sức mạnh lớn hơn " + power + " mới có thể nhập code này");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Gift code không chính xác vui lòng thử lại");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}