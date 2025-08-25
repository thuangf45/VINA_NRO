package com.arriety.kygui;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.npc.NpcFactory;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ShopKyGuiService:
 * Lớp ShopKyGuiService thuộc package com.arriety.kygui, là một lớp dịch vụ quản lý hệ thống cửa hàng ký gửi trong game. Đây là một singleton, chịu trách nhiệm xử lý các chức năng như đăng bán, mua, hủy, hoặc nhận tiền từ vật phẩm ký gửi, cũng như hiển thị danh sách vật phẩm theo tab và trang. Lớp sử dụng các lớp khác như Player, Item, ItemKyGui, InventoryServiceNew, ItemService, NpcService, và Service để tương tác với hệ thống game.
 */
public class ShopKyGuiService {
    /**
     * instance: ShopKyGuiService - Biến tĩnh để lưu trữ instance duy nhất của lớp ShopKyGuiService, đảm bảo chỉ có một đối tượng được tạo.
     */
    private static ShopKyGuiService instance;

    /**
     * LastTimeUpdate: long - Thời gian cập nhật cuối cùng, dùng để kiểm tra thời gian hết hạn của vật phẩm ký gửi.
     */
    private static long LastTimeUpdate;

    /**
     * gI():
     * - Mô tả: Trả về instance duy nhất của lớp ShopKyGuiService (singleton pattern). Nếu instance chưa được tạo, sẽ khởi tạo mới.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Đối tượng ShopKyGuiService.
     */
    public static ShopKyGuiService gI() {
        if (instance == null) {
            instance = new ShopKyGuiService();
        }
        return instance;
    }

    /**
     * update():
     * - Mô tả: Cập nhật trạng thái hết hạn của các vật phẩm ký gửi trong danh sách ShopKyGuiManager.gI().listItem. Chạy định kỳ mỗi 60 giây, kiểm tra và đánh dấu vật phẩm hết hạn (isBuy = 2) nếu thời gian tạo vượt quá 2 ngày và chưa được mua.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Kiểm tra thời gian hiện tại so với LastTimeUpdate, chỉ thực hiện nếu đã qua 60 giây.
     *   - Duyệt qua danh sách vật phẩm, nếu thời gian tạo vượt quá 2 ngày (172800000 ms) và vật phẩm chưa được mua (isBuy = 0), đặt isBuy = 2.
     *   - Cập nhật LastTimeUpdate bằng thời gian hiện tại.
     * - Trả về: Không trả về gì, chỉ cập nhật trạng thái vật phẩm.
     */
    public static void update() {
        if (System.currentTimeMillis() - LastTimeUpdate >= 60000) {
            List<ItemKyGui> list = new ArrayList<>(ShopKyGuiManager.gI().listItem);
            for (ItemKyGui item : list) {
                if (System.currentTimeMillis() - item.createTime >= 60000 * 60 * 24 * 2 && item.isBuy == 0) {
                    item.isBuy = 2;
                }
            }
            LastTimeUpdate = System.currentTimeMillis();
        }
    }

    /**
     * getItemKyGui2(Player pl, byte tab, int max):
     * - Mô tả: Lấy một phần danh sách vật phẩm ký gửi trong tab cụ thể, phân trang với số lượng tối đa 20 vật phẩm mỗi trang.
     * - Thuộc tính:
     *   - pl: Player - Người chơi yêu cầu danh sách vật phẩm.
     *   - tab: byte - Tab hiển thị trong cửa hàng ký gửi.
     *   - max: int - Số trang yêu cầu (số lượng vật phẩm = max * 20).
     * - Xử lý logic:
     *   - Gọi getItemKyGui2(pl, tab) để lấy danh sách vật phẩm đã được lọc và sắp xếp.
     *   - Tính toán chỉ số bắt đầu và kết thúc của phân trang, trả về danh sách con (subList) với tối đa 20 vật phẩm.
     * - Trả về: List<ItemKyGui> - Danh sách vật phẩm ký gửi trong tab và trang yêu cầu.
     */
    private List<ItemKyGui> getItemKyGui2(Player pl, byte tab, int max) {
        List<ItemKyGui> z = new ArrayList<>(getItemKyGui2(pl, tab));
        int x = max * 20;
        x = z.size() < x ? z.size() : x;
        return z.subList(x - 20 < 0 ? 0 : x - 20, x);
    }

    /**
     * getItemKyGui2(Player pl, byte tab):
     * - Mô tả: Lấy danh sách vật phẩm ký gửi trong tab cụ thể, sắp xếp theo trạng thái ưu tiên (isUpTop) và thời gian tạo (createTime).
     * - Thuộc tính:
     *   - pl: Player - Người chơi yêu cầu danh sách vật phẩm.
     *   - tab: byte - Tab hiển thị trong cửa hàng ký gửi.
     * - Xử lý logic:
     *   - Lọc danh sách vật phẩm từ ShopKyGuiManager.gI().listItem theo tab và trạng thái chưa bán (isBuy = 0).
     *   - Sắp xếp theo thời gian tạo (mới nhất trước), sau đó ưu tiên các vật phẩm có isUpTop = 1 (tối đa 9 vật phẩm).
     *   - Kết hợp danh sách ưu tiên và danh sách còn lại, loại bỏ trùng lặp.
     * - Trả về: List<ItemKyGui> - Danh sách vật phẩm đã được sắp xếp.
     */
    private List<ItemKyGui> getItemKyGui2(Player pl, byte tab) {
        List<ItemKyGui> its = new ArrayList<>();
        List<ItemKyGui> listSort = new ArrayList<>();
        List<ItemKyGui> listSort2 = new ArrayList<>();
        List<ItemKyGui> listSort3 = new ArrayList<>();
        List<ItemKyGui> listSort4 = new ArrayList<>();
        List<ItemKyGui> listItem = new ArrayList<>(ShopKyGuiManager.gI().listItem);
        listItem.stream().filter((it) -> (it != null && it.tab == tab && it.isBuy == 0)).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.createTime, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        listSort2 = new ArrayList<>(listSort);
        listSort.clear();
        listSort2.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).limit(9).forEach(i -> listSort.add(i));
        listSort3 = new ArrayList<>(listSort);
        listSort.clear();
        listSort.addAll(listSort3);
        listSort2.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.createTime, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        for (int i = 0; i < listSort.size(); i++) {
            if (!listSort4.contains(listSort.get(i))) {
                listSort4.add(listSort.get(i));
            }
        }
        return listSort4;
    }

    /**
     * getItemKyGui(Player pl, byte tab, byte... max):
     * - Mô tả: Lấy danh sách vật phẩm ký gửi trong tab cụ thể, không bao gồm vật phẩm của chính người chơi, với tùy chọn giới hạn số lượng.
     * - Thuộc tính:
     *   - pl: Player - Người chơi yêu cầu danh sách vật phẩm.
     *   - tab: byte - Tab hiển thị trong cửa hàng ký gửi.
     *   - max: byte... - Mảng tùy chọn để giới hạn số lượng vật phẩm trả về (có thể là một hoặc hai giá trị).
     * - Xử lý logic:
     *   - Lọc danh sách vật phẩm từ ShopKyGuiManager.gI().listItem theo tab, trạng thái chưa bán (isBuy = 0), và không thuộc người chơi hiện tại.
     *   - Sắp xếp theo trạng thái ưu tiên (isUpTop).
     *   - Nếu max có 2 giá trị, trả về danh sách con từ max[0] đến max[1]. Nếu max có 1 giá trị, trả về tối đa max[0] vật phẩm. Nếu không, trả về toàn bộ danh sách.
     * - Trả về: List<ItemKyGui> - Danh sách vật phẩm ký gửi đã được lọc và sắp xếp.
     */
    private List<ItemKyGui> getItemKyGui(Player pl, byte tab, byte... max) {
        List<ItemKyGui> its = new ArrayList<>();
        List<ItemKyGui> listSort = new ArrayList<>();
        List<ItemKyGui> listSort2 = new ArrayList<>();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.tab == tab && it.isBuy == 0 && it.player_sell != pl.id)).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        if (max.length == 2) {
            if (listSort.size() > max[1]) {
                for (int i = max[0]; i < max[1]; i++) {
                    if (listSort.get(i) != null) {
                        listSort2.add(listSort.get(i));
                    }
                }
            } else {
                for (int i = max[0]; i <= max[0]; i++) {
                    if (listSort.get(i) != null) {
                        listSort2.add(listSort.get(i));
                    }
                }
            }
            return listSort2;
        }
        if (max.length == 1 && listSort.size() > max[0]) {
            for (int i = 0; i < max[0]; i++) {
                if (listSort.get(i) != null) {
                    listSort2.add(listSort.get(i));
                }
            }
            return listSort2;
        }
        return listSort;
    }

    /**
     * getItemKyGui():
     * - Mô tả: Lấy toàn bộ danh sách vật phẩm ký gửi chưa được mua (isBuy = 0), sắp xếp theo trạng thái ưu tiên (isUpTop).
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Lọc danh sách vật phẩm từ ShopKyGuiManager.gI().listItem với trạng thái chưa bán (isBuy = 0).
     *   - Sắp xếp theo trạng thái ưu tiên (isUpTop).
     * - Trả về: List<ItemKyGui> - Danh sách vật phẩm ký gửi đã được sắp xếp.
     */
    private List<ItemKyGui> getItemKyGui() {
        List<ItemKyGui> its = new ArrayList<>();
        List<ItemKyGui> listSort = new ArrayList<>();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.isBuy == 0)).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        return listSort;
    }

    /**
     * isKyGui(Item item):
     * - Mô tả: Kiểm tra xem một vật phẩm có thể được ký gửi hay không dựa trên các điều kiện về loại vật phẩm, ID, và tùy chọn.
     * - Thuộc tính:
     *   - item: Item - Vật phẩm cần kiểm tra.
     * - Xử lý logic:
     *   - Kiểm tra nếu vật phẩm có tùy chọn với ID 86 hoặc ID vật phẩm là 1280, trả về true.
     *   - Với các loại vật phẩm (type) từ 0-4, kiểm tra nếu tên chứa "Thần".
     *   - Với type 27, kiểm tra ID cụ thể (921, 1155, 1156, 568 với tùy chọn > 1, hoặc 1066-1070, 1280).
     *   - Với type 21 hoặc 72, trả về true.
     *   - Các trường hợp khác trả về false.
     * - Trả về: boolean - True nếu vật phẩm có thể ký gửi, false nếu không.
     */
    private boolean isKyGui(Item item) {
        for (ItemOption option : item.itemOptions) {
            if (option.optionTemplate.id == 86) {
                return true;
            }
        }
        if (item.template.id == 1280) {
            return true;
        }
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return item.template.name.contains("Thần");
            case 27:
                switch (item.template.id) {
                    case 921:
                    case 1155:
                    case 1156:
                    case 568:
                        return item.itemOptions.size() > 1;
                    case 1066:
                    case 1067:
                    case 1068:
                    case 1069:
                    case 1070:
                    case 1280:
                        return true;
                }
                return false;
            case 21:
            case 72:
                return true;
        }
        return false;
    }

    /**
     * SubThoiVang(Player pl, int quantity):
     * - Mô tả: Trừ số lượng thỏi vàng (item ID 457) từ túi đồ của người chơi nếu có đủ và không có tùy chọn không thể giao dịch.
     * - Thuộc tính:
     *   - pl: Player - Người chơi cần trừ thỏi vàng.
     *   - quantity: int - Số lượng thỏi vàng cần trừ.
     * - Xử lý logic:
     *   - Duyệt qua túi đồ, tìm vật phẩm có ID 457, đủ số lượng và không có tùy chọn ID 30.
     *   - Nếu tìm thấy, gọi InventoryServiceNew.gI().subQuantityItemsBag để trừ số lượng và trả về true.
     *   - Nếu không đủ, trả về false.
     * - Trả về: boolean - True nếu trừ thành công, false nếu không đủ hoặc không tìm thấy.
     */
    private boolean SubThoiVang(Player pl, int quantity) {
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 457 && item.quantity >= quantity && !HaveOPNotTrade(item)) {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quantity);
                return true;
            }
        }
        return false;
    }

    /**
     * HaveOPNotTrade(Item item):
     * - Mô tả: Kiểm tra xem vật phẩm có tùy chọn không thể giao dịch (ID 30) hay không.
     * - Thuộc tính:
     *   - item: Item - Vật phẩm cần kiểm tra.
     * - Xử lý logic:
     *   - Duyệt qua danh sách tùy chọn, nếu tìm thấy tùy chọn với ID 30, trả về true.
     *   - Nếu không tìm thấy, trả về false.
     * - Trả về: boolean - True nếu vật phẩm có tùy chọn không thể giao dịch, false nếu không.
     */
    private boolean HaveOPNotTrade(Item item) {
        for (Item.ItemOption op : item.itemOptions) {
            if (op != null && op.optionTemplate.id == 30) {
                return true;
            }
        }
        return false;
    }

    /**
     * buyItem(Player pl, int id):
     * - Mô tả: Xử lý việc người chơi mua một vật phẩm ký gửi dựa trên ID. Kiểm tra điều kiện sức mạnh, quyền sở hữu, và tài nguyên (vàng hoặc hồng ngọc) trước khi mua.
     * - Thuộc tính:
     *   - pl: Player - Người chơi thực hiện mua.
     *   - id: int - ID của vật phẩm ký gửi.
     * - Xử lý logic:
     *   - Kiểm tra sức mạnh người chơi (>= 40 tỷ), nếu không đủ, thông báo và mở lại cửa hàng.
     *   - Lấy vật phẩm ký gửi từ getItemBuy(id), nếu không tồn tại hoặc đã bán (isBuy = 1), thông báo lỗi.
     *   - Nếu người chơi là người bán, thông báo lỗi và mở lại cửa hàng.
     *   - Kiểm tra và trừ vàng (SubThoiVang) hoặc hồng ngọc (ruby) dựa trên giá bán (goldSell, gemSell).
     *   - Nếu mua thành công, tạo vật phẩm mới, thêm vào túi đồ, cập nhật isBuy = 1, thông báo và mở lại cửa hàng.
     * - Trả về: Không trả về gì, thực hiện hành động mua và gửi thông báo.
     */
    public void buyItem(Player pl, int id) {
        if (pl.nPoint.power < 40000000000L) {
            Service.gI().sendThongBao(pl, "Yêu cầu sức mạnh lớn hơn 40 tỷ");
            openShopKyGui(pl);
            return;
        }
        ItemKyGui it = getItemBuy(id);
        if (it == null || it.isBuy == 1) {
            Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
            return;
        }
        if (it.player_sell == pl.id) {
            Service.gI().sendThongBao(pl, "Không thể mua vật phẩm bản thân đăng bán");
            openShopKyGui(pl);
            return;
        }
        boolean isBuy = false;
        if (it.goldSell > 0) {
            if (SubThoiVang(pl, it.goldSell)) {
                isBuy = true;
            } else {
                Service.gI().sendThongBao(pl, "Bạn cần đủ thỏi vàng có thể giao dịch mới có thể mua vật phẩm");
                isBuy = false;
            }
        } else if (it.gemSell > 0) {
            if (pl.inventory.ruby >= it.gemSell) {
                pl.inventory.ruby -= it.gemSell;
                isBuy = true;
            } else {
                Service.gI().sendThongBao(pl, "Bạn không đủ hồng ngọc để mua vật phẩm này!");
                isBuy = false;
            }
        }
        Service.gI().sendMoney(pl);
        if (isBuy) {
            Item item = ItemService.gI().createNewItem(it.itemId);
            item.quantity = it.quantity;
            item.itemOptions.addAll(it.options);
            it.isBuy = 1;
            if (it.isBuy == 1) {
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + item.template.name);
                openShopKyGui(pl);
            }
        }
    }

    /**
     * getItemBuy(int id):
     * - Mô tả: Lấy vật phẩm ký gửi theo ID từ danh sách vật phẩm chưa bán.
     * - Thuộc tính:
     *   - id: int - ID của vật phẩm ký gửi.
     * - Xử lý logic:
     *   - Duyệt qua danh sách từ getItemKyGui(), trả về vật phẩm nếu ID khớp.
     *   - Nếu không tìm thấy, trả về null.
     * - Trả về: ItemKyGui - Vật phẩm ký gửi hoặc null nếu không tìm thấy.
     */
    public ItemKyGui getItemBuy(int id) {
        for (ItemKyGui it : getItemKyGui()) {
            if (it != null && it.id == id) {
                return it;
            }
        }
        return null;
    }

    /**
     * getItemBuy(Player pl, int id):
     * - Mô tả: Lấy vật phẩm ký gửi theo ID và thuộc sở hữu của người chơi.
     * - Thuộc tính:
     *   - pl: Player - Người chơi cần kiểm tra.
     *   - id: int - ID của vật phẩm ký gửi.
     * - Xử lý logic:
     *   - Duyệt qua danh sách từ ShopKyGuiManager.gI().listItem, trả về vật phẩm nếu ID và player_sell khớp.
     *   - Nếu không tìm thấy, trả về null.
     * - Trả về: ItemKyGui - Vật phẩm ký gửi hoặc null nếu không tìm thấy.
     */
    public ItemKyGui getItemBuy(Player pl, int id) {
        for (ItemKyGui it : ShopKyGuiManager.gI().listItem) {
            if (it != null && it.id == id && it.player_sell == pl.id) {
                return it;
            }
        }
        return null;
    }

    /**
     * openShopKyGui(Player pl, byte index, int page):
     * - Mô tả: Hiển thị giao diện cửa hàng ký gửi cho người chơi, gửi thông tin vật phẩm trong tab và trang yêu cầu qua thông điệp (Message) với mã -100.
     * - Thuộc tính:
     *   - pl: Player - Người chơi mở cửa hàng.
     *   - index: byte - Tab hiển thị (0-4).
     *   - page: int - Trang hiện tại của tab.
     * - Xử lý logic:
     *   - Kiểm tra trang hợp lệ, nếu không, thoát.
     *   - Tạo thông điệp với mã -100, ghi tab, số trang tối đa, trang hiện tại, và danh sách vật phẩm.
     *   - Với mỗi vật phẩm, tạo đối tượng Item mới, thêm tùy chọn (nếu không có thì thêm tùy chọn mặc định ID 73), ghi thông tin (ID, giá, số lượng, sở hữu, tùy chọn) vào thông điệp.
     *   - Gửi thông điệp và dọn dẹp tài nguyên.
     * - Trả về: Không trả về gì, chỉ gửi thông điệp hiển thị cửa hàng.
     */
    public void openShopKyGui(Player pl, byte index, int page) {
        if (page > getItemKyGui(pl, index).size()) {
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-100);
            msg.writer().writeByte(index);
            List<ItemKyGui> items = getItemKyGui2(pl, index);
            byte tab = (byte) (items.size() / 20 > 0 ? items.size() / 20 : 1);
            msg.writer().writeByte(tab);
            msg.writer().writeByte(page);
            List<ItemKyGui> itemsSend = getItemKyGui2(pl, index, page + 1);
            msg.writer().writeByte(itemsSend.size());
            for (int j = 0; j < itemsSend.size(); j++) {
                ItemKyGui itk = itemsSend.get(j);
                Item it = ItemService.gI().createNewItem(itk.itemId);
                it.itemOptions.clear();
                if (itk.options.isEmpty()) {
                    it.itemOptions.add(new ItemOption(73, 0));
                } else {
                    it.itemOptions.addAll(itk.options);
                }
                msg.writer().writeShort(it.template.id);
                msg.writer().writeShort(itk.id);
                msg.writer().writeInt(itk.goldSell);
                msg.writer().writeInt(itk.gemSell);
                msg.writer().writeByte(0);
                if (pl.getSession().version >= 222) {
                    msg.writer().writeInt(itk.quantity);
                } else {
                    msg.writer().writeByte(itk.quantity);
                }
                msg.writer().writeByte(itk.player_sell == pl.id ? 1 : 0);
                msg.writer().writeByte(it.itemOptions.size());
                for (int a = 0; a < it.itemOptions.size(); a++) {
                    msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
                    msg.writer().writeShort(it.itemOptions.get(a).param);
                }
                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            System.err.print("\nError at 7\n");
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    /**
     * upItemToTop(Player pl, int id):
     * - Mô tả: Đặt vật phẩm ký gửi lên đầu danh sách hiển thị (isUpTop = 1) sau khi kiểm tra quyền sở hữu.
     * - Thuộc tính:
     *   - pl: Player - Người chơi yêu cầu đưa vật phẩm lên đầu.
     *   - id: int - ID của vật phẩm ký gửi.
     * - Xử lý logic:
     *   - Lấy vật phẩm từ getItemBuy(id), nếu không tồn tại hoặc đã bán, thông báo lỗi.
     *   - Kiểm tra quyền sở hữu, nếu không phải của người chơi, thông báo lỗi và mở lại cửa hàng.
     *   - Lưu ID vật phẩm vào iDMark và hiển thị menu xác nhận với phí 2 thỏi vàng.
     * - Trả về: Không trả về gì, hiển thị menu xác nhận.
     */
    public void upItemToTop(Player pl, int id) {
        ItemKyGui it = getItemBuy(id);
        if (it == null || it.isBuy == 1) {
            Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
            return;
        }
        if (it.player_sell != pl.id) {
            Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
            openShopKyGui(pl);
            return;
        }
        pl.iDMark.setIdItemUpTop(id);
        NpcService.gI().createMenuConMeo(pl, ConstNpc.UP_TOP_ITEM, -1, "Bạn có muốn đưa vật phẩm ['" + ItemService.gI().createNewItem(it.itemId).template.name + "'] của bản thân lên trang đầu?\nYêu cầu 2 thỏi vàng.", "Đồng ý", "Từ Chối");
    }

    /**
     * StartupItemToTop(Player pl):
     * - Mô tả: Thực hiện hành động đưa vật phẩm lên đầu danh sách sau khi xác nhận, trừ 2 thỏi vàng và cập nhật trạng thái isUpTop.
     * - Thuộc tính:
     *   - pl: Player - Người chơi thực hiện hành động.
     * - Xử lý logic:
     *   - Kiểm tra và trừ 2 thỏi vàng bằng SubThoiVang, nếu không đủ, thông báo lỗi.
     *   - Tìm vật phẩm trong danh sách với ID từ iDMark, cập nhật isUpTop = 1 và thời gian tạo mới.
     *   - Thông báo thành công và mở lại cửa hàng.
     * - Trả về: Không trả về gì, thực hiện hành động và gửi thông báo.
     */
    public void StartupItemToTop(Player pl) {
        if (!SubThoiVang(pl, 2)) {
            Service.gI().sendThongBao(pl, "Bạn cần có ít nhất 2 thỏi vàng đưa vật phẩm lên trang đầu");
            return;
        }
        for (ItemKyGui its : ShopKyGuiManager.gI().listItem) {
            if (its.id == pl.iDMark.getIdItemUpTop()) {
                its.isUpTop = 1;
                its.createTime = System.currentTimeMillis();
                Service.gI().sendThongBao(pl, "Đưa vật phẩm lên trang đầu thành công");
                break;
            }
        }
        openShopKyGui(pl);
    }

    /**
     * claimOrDel(Player pl, byte action, int id):
     * - Mô tả: Xử lý hành động hủy vật phẩm hoặc nhận tiền sau khi bán vật phẩm ký gửi.
     * - Thuộc tính:
     *   - pl: Player - Người chơi thực hiện hành động.
     *   - action: byte - Hành động (1 = hủy, 2 = nhận tiền).
     *   - id: int - ID của vật phẩm ký gửi.
     * - Xử lý logic:
     *   - Lấy vật phẩm từ getItemBuy(pl, id), kiểm tra quyền sở hữu và trạng thái.
     *   - Nếu action = 1 (hủy): Trả vật phẩm về túi đồ, xóa khỏi danh sách, thông báo và mở lại cửa hàng.
     *   - Nếu action = 2 (nhận tiền): Trừ 10% giá trị, thêm vàng hoặc hồng ngọc vào túi đồ, xóa vật phẩm, thông báo và mở lại cửa hàng.
     * - Trả về: Không trả về gì, thực hiện hành động và gửi thông báo.
     */
    public void claimOrDel(Player pl, byte action, int id) {
        ItemKyGui it = getItemBuy(pl, id);
        switch (action) {
            case 1:
                if (it == null || it.isBuy == 1) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
                    return;
                }
                if (it.player_sell != pl.id) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                    openShopKyGui(pl);
                    return;
                }
                Item item = ItemService.gI().createNewItem(it.itemId);
                item.quantity = it.quantity;
                item.itemOptions.addAll(it.options);
                if (ShopKyGuiManager.gI().listItem.remove(it)) {
                    InventoryServiceNew.gI().addItemBag(pl, item);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    Service.gI().sendMoney(pl);
                    Service.gI().sendThongBao(pl, "Hủy bán vật phẩm thành công");
                    openShopKyGui(pl);
                }
                break;
            case 2:
                if (it == null || it.isBuy == 0) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc chưa được bán");
                    return;
                }
                if (it.player_sell != pl.id) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                    openShopKyGui(pl);
                    return;
                }
                if (it.goldSell > 0) {
                    Item tvAdd = ItemService.gI().createNewItem((short) 457);
                    tvAdd.quantity = (int) (it.goldSell * 0.9);
                    InventoryServiceNew.gI().addItemBag(pl, tvAdd);
                } else if (it.gemSell > 0) {
                    pl.inventory.ruby += (int) (it.gemSell * 0.9);
                }
                if (ShopKyGuiManager.gI().listItem.remove(it)) {
                    Service.gI().sendMoney(pl);
                    Service.gI().sendThongBao(pl, "Bạn đã bán vật phẩm thành công");
                    openShopKyGui(pl);
                }
                break;
        }
    }

    /**
     * getItemCanKiGui(Player pl):
     * - Mô tả: Lấy danh sách vật phẩm có thể ký gửi của người chơi, bao gồm vật phẩm đang ký gửi và vật phẩm trong túi đồ hợp lệ.
     * - Thuộc tính:
     *   - pl: Player - Người chơi cần kiểm tra.
     * - Xử lý logic:
     *   - Lọc danh sách vật phẩm ký gửi thuộc người chơi từ ShopKyGuiManager.gI().listItem.
     *   - Lọc vật phẩm trong túi đồ hợp lệ (isKyGui = true), tạo đối tượng ItemKyGui mới với tab mặc định là 4.
     * - Trả về: List<ItemKyGui> - Danh sách vật phẩm có thể ký gửi.
     */
    public List<ItemKyGui> getItemCanKiGui(Player pl) {
        List<ItemKyGui> its = new ArrayList<>();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.player_sell == pl.id)).forEachOrdered((it) -> {
            its.add(it);
        });
        pl.inventory.itemsBag.stream().filter((it) -> (it.isNotNullItem() && (isKyGui(it)))).forEachOrdered((it) -> {
            its.add(new ItemKyGui(InventoryServiceNew.gI().getIndexBag(pl, it), it.template.id, (int) pl.id, (byte) 4, -1, -1, it.quantity, (byte) -1, it.itemOptions, (byte) 0, System.currentTimeMillis()));
        });
        return its;
    }

    /**
     * getMaxId():
     * - Mô tả: Lấy ID lớn nhất của vật phẩm ký gửi trong danh sách.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Thu thập danh sách ID từ ShopKyGuiManager.gI().listItem.
     *   - Nếu danh sách rỗng, trả về 0; ngược lại, trả về ID lớn nhất.
     *   - Xử lý ngoại lệ và ghi log lỗi nếu xảy ra.
     * - Trả về: int - ID lớn nhất hoặc 0 nếu danh sách rỗng.
     */
    public int getMaxId() {
        try {
            List<Integer> id = new ArrayList<>();
            ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null)).forEachOrdered((it) -> {
                id.add(it.id);
            });
            if (id.isEmpty()) {
                return 0;
            }
            return Collections.max(id);
        } catch (Exception e) {
            System.err.print("\nError at 8\n");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * getTabKiGui(Item it):
     * - Mô tả: Xác định tab hiển thị của vật phẩm ký gửi dựa trên loại và ID vật phẩm.
     * - Thuộc tính:
     *   - it: Item - Vật phẩm cần xác định tab.
     * - Xử lý logic:
     *   - Nếu type từ 0-4, trả về tab 0 (Trang bị).
     *   - Nếu type là 27, trả về tab 1 (Bông tai) cho ID 921, 1155, 1156, 568; tab 3 (Linh tinh) cho ID 1066-1070; mặc định tab 3.
     *   - Nếu type là 72, trả về tab 2 (Linh Thú).
     *   - Các trường hợp khác trả về tab 3 (Linh tinh).
     * - Trả về: byte - Chỉ số tab (0, 1, 2, hoặc 3).
     */
    public byte getTabKiGui(Item it) {
        if (it.template.type >= 0 && it.template.type <= 4) {
            return 0;
        } else if ((it.template.type == 27)) {
            switch (it.template.id) {
                case 921:
                case 1155:
                case 1156:
                case 568:
                    return 1;
                case 1066:
                case 1067:
                case 1068:
                case 1069:
                case 1070:
                    return 3;
            }
            return 3;
        } else if (it.template.type == 72) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * ManhThienSu(int id):
     * - Mô tả: Kiểm tra xem ID vật phẩm có phải là Mảnh Thiên Sứ (ID từ 1066 đến 1070).
     * - Thuộc tính:
     *   - id: int - ID của vật phẩm.
     * - Xử lý logic: Kiểm tra ID có nằm trong danh sách 1066, 1067, 1068, 1069, 1070.
     * - Trả về: boolean - True nếu là Mảnh Thiên Sứ, false nếu không.
     */
    private boolean ManhThienSu(int id) {
        return id == 1066 || id == 1067 || id == 1068 || id == 1069 || id == 1070;
    }

    /**
     * KiGui(Player pl, int id, int money, byte moneyType, int quantity):
     * - Mô tả: Xử lý việc người chơi đăng bán vật phẩm ký gửi với giá và loại tiền (vàng hoặc hồng ngọc), kiểm tra các điều kiện hợp lệ.
     * - Thuộc tính:
     *   - pl: Player - Người chơi đăng bán.
     *   - id: int - Chỉ số vật phẩm trong túi đồ.
     *   - money: int - Giá bán.
     *   - moneyType: byte - Loại tiền (0 = vàng, 1 = hồng ngọc).
     *   - quantity: int - Số lượng vật phẩm đăng bán.
     * - Xử lý logic:
     *   - Kiểm tra và trừ 1 thỏi vàng làm phí đăng bán.
     *   - Kiểm tra vật phẩm không có tùy chọn không thể giao dịch (ID 30).
     *   - Kiểm tra giá tiền hợp lệ (> 0), số lượng hợp lệ (≤ số lượng trong túi, ≤ 9999, ≥ 50 cho Ma Thạch và Mảnh Thiên Sứ).
     *   - Trừ 5 hồng ngọc, tạo và thêm vật phẩm ký gửi vào danh sách, cập nhật túi đồ, thông báo và mở lại cửa hàng.
     * - Trả về: Không trả về gì, thực hiện hành động và gửi thông báo.
     */
    public void KiGui(Player pl, int id, int money, byte moneyType, int quantity) {
        try {
            if (!SubThoiVang(pl, 1)) {
                Service.gI().sendThongBao(pl, "Bạn cần có ít nhất 1 thỏi vàng để làm phí đăng bán");
                return;
            }
            Item it = ItemService.gI().copyItem(pl.inventory.itemsBag.get(id));
            for (Item.ItemOption daubuoi : it.itemOptions) {
                if (daubuoi.optionTemplate.id == 30) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thể kí gửi");
                    openShopKyGui(pl);
                    return;
                }
            }
            if (money <= 0 || quantity > it.quantity) {
                openShopKyGui(pl);
                return;
            }
            if (quantity > 9999) {
                Service.gI().sendThongBao(pl, "Ký gửi tối đa x9999");
                openShopKyGui(pl);
                return;
            }
            if (quantity < 50 && it.template.id == 1280) {
                Service.gI().sendThongBao(pl, "Ma thạch cần ký gửi x50 trở lên");
                openShopKyGui(pl);
                return;
            }
            if (quantity < 50 && ManhThienSu(it.template.id)) {
                Service.gI().sendThongBao(pl, "Mảnh thiên sứ cần ký gửi x50 trở lên");
                openShopKyGui(pl);
                return;
            }
            pl.inventory.gem -= 5;
            switch (moneyType) {
                case 0:
                    if (money > 100000) {
                        Service.gI().sendThongBao(pl, "không thể ký gửi quá 100000 thỏi vàng");
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, pl.inventory.itemsBag.get(id), quantity);
                        ShopKyGuiManager.gI().listItem.add(new ItemKyGui(getMaxId() + 1, it.template.id, (int) pl.id, getTabKiGui(it), money, -1, quantity, (byte) 0, it.itemOptions, (byte) 0, System.currentTimeMillis()));
                        InventoryServiceNew.gI().sendItemBags(pl);
                        openShopKyGui(pl);
                        Service.gI().sendMoney(pl);
                        Service.gI().sendThongBao(pl, "Đăng bán thành công");
                    }
                    break;
                case 1:
                    if (money > 100000) {
                        Service.gI().sendThongBao(pl, "không thể ký gửi quá 100000 ngọc");
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, pl.inventory.itemsBag.get(id), quantity);
                        ShopKyGuiManager.gI().listItem.add(new ItemKyGui(getMaxId() + 1, it.template.id, (int) pl.id, getTabKiGui(it), -1, money, quantity, (byte) 0, it.itemOptions, (byte) 0, System.currentTimeMillis()));
                        InventoryServiceNew.gI().sendItemBags(pl);
                        openShopKyGui(pl);
                        Service.gI().sendMoney(pl);
                        Service.gI().sendThongBao(pl, "Đăng bán thành công");
                    }
                    break;
                default:
                    Service.gI().sendThongBao(pl, "Có lỗi xảy ra");
                    openShopKyGui(pl);
                    break;
            }
        } catch (Exception e) {
            System.err.print("\nError at 9\n");
            e.printStackTrace();
        }
    }

    /**
     * openShopKyGui(Player pl):
     * - Mô tả: Hiển thị giao diện cửa hàng ký gửi đầy đủ, bao gồm tất cả tab và vật phẩm có thể ký gửi của người chơi, thông qua thông điệp (Message) mã -44.
     * - Thuộc tính:
     *   - pl: Player - Người chơi mở cửa hàng.
     * - Xử lý logic:
     *   - Tạo thông điệp với mã -44, ghi số lượng tab (5).
     *   - Với mỗi tab (0-4), ghi tên tab, số trang tối đa, và danh sách vật phẩm:
     *     - Tab 4: Hiển thị vật phẩm có thể ký gửi của người chơi (getItemCanKiGui).
     *     - Tab 0-3: Hiển thị vật phẩm trong tab, với thông tin tương tự openShopKyGui(pl, index, page).
     *   - Gửi thông điệp và dọn dẹp tài nguyên.
     * - Trả về: Không trả về gì, chỉ gửi thông điệp hiển thị cửa hàng.
     */
    public void openShopKyGui(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(2);
            msg.writer().writeByte(5);
            for (byte i = 0; i < 5; i++) {
                if (i == 4) {
                    msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
                    msg.writer().writeByte(0);
                    msg.writer().writeByte(getItemCanKiGui(pl).size());
                    for (int j = 0; j < getItemCanKiGui(pl).size(); j++) {
                        ItemKyGui itk = getItemCanKiGui(pl).get(j);
                        if (itk == null) {
                            continue;
                        }
                        Item it = ItemService.gI().createNewItem(itk.itemId);
                        it.itemOptions.clear();
                        if (itk.options.isEmpty()) {
                            it.itemOptions.add(new ItemOption(73, 0));
                        } else {
                            it.itemOptions.addAll(itk.options);
                        }
                        msg.writer().writeShort(it.template.id);
                        msg.writer().writeShort(itk.id);
                        msg.writer().writeInt(itk.goldSell);
                        msg.writer().writeInt(itk.gemSell);
                        if (getItemBuy(pl, itk.id) == null) {
                            msg.writer().writeByte(0);
                        } else if (itk.isBuy == 1) {
                            msg.writer().writeByte(2);
                        } else if (itk.isBuy == 2) {
                            msg.writer().writeByte(3);
                        } else {
                            msg.writer().writeByte(1);
                        }
                        if (pl.getSession().version >= 222) {
                            msg.writer().writeInt(itk.quantity);
                        } else {
                            msg.writer().writeByte(itk.quantity);
                        }
                        msg.writer().writeByte(1);
                        msg.writer().writeByte(it.itemOptions.size());
                        for (int a = 0; a < it.itemOptions.size(); a++) {
                            msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
                            msg.writer().writeShort(it.itemOptions.get(a).param);
                        }
                        msg.writer().writeByte(0);
                        msg.writer().writeByte(0);
                    }
                } else {
                    List<ItemKyGui> items = getItemKyGui2(pl, i);
                    msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
                    byte tab = (byte) (items.size() / 20 > 0 ? items.size() / 20 : 1);
                    List<ItemKyGui> itemsSend = getItemKyGui2(pl, i, 1);
                    msg.writer().writeByte(tab);
                    msg.writer().writeByte(itemsSend.size());
                    for (int j = 0; j < itemsSend.size(); j++) {
                        ItemKyGui itk = itemsSend.get(j);
                        Item it = ItemService.gI().createNewItem(itk.itemId);
                        it.itemOptions.clear();
                        if (itk.options.isEmpty()) {
                            it.itemOptions.add(new ItemOption(73, 0));
                        } else {
                            it.itemOptions.addAll(itk.options);
                        }
                        msg.writer().writeShort(it.template.id);
                        msg.writer().writeShort(itk.id);
                        msg.writer().writeInt(itk.goldSell);
                        msg.writer().writeInt(itk.gemSell);
                        msg.writer().writeByte(0);
                        if (pl.getSession().version >= 222) {
                            msg.writer().writeInt(itk.quantity);
                        } else {
                            msg.writer().writeByte(itk.quantity);
                        }
                        msg.writer().writeByte(itk.player_sell == pl.id ? 1 : 0);
                        msg.writer().writeByte(it.itemOptions.size());
                        for (int a = 0; a < it.itemOptions.size(); a++) {
                            msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
                            msg.writer().writeShort(it.itemOptions.get(a).param);
                        }
                        msg.writer().writeByte(0);
                        msg.writer().writeByte(0);
                    }
                }
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            System.err.print("\nError at 10\n");
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }
}