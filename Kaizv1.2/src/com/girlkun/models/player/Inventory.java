package com.girlkun.models.player;

import java.util.ArrayList;
import java.util.List;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.services.GiftService;

/**
 * Lớp quản lý kho đồ của người chơi, bao gồm các vật phẩm, vàng, ngọc, và các tài nguyên khác.
 * @author Lucifer
 */
public class Inventory {

    /** Giới hạn số vàng tối đa mà người chơi có thể sở hữu. */
    public static final long LIMIT_GOLD = 200000000000L;

    /** Số lượng vật phẩm tối đa trong túi đồ. */
    public static final int MAX_ITEMS_BAG = 80;

    /** Số lượng vật phẩm tối đa trong rương đồ. */
    public static final int MAX_ITEMS_BOX = 80;

    /** Vật phẩm giáp tập luyện của người chơi. */
    public Item trainArmor;

    /** Danh sách mã quà tặng mà người chơi đã sử dụng. */
    public List<String> giftCode;

    /** Danh sách vật phẩm trên cơ thể người chơi (trang bị). */
    public List<Item> itemsBody;

    /** Danh sách vật phẩm trong túi đồ. */
    public List<Item> itemsBag;

    /** Danh sách vật phẩm trong rương đồ. */
    public List<Item> itemsBox;

    /** Danh sách vật phẩm trong rương nứt bóng. */
    public List<Item> itemsBoxCrackBall;

    /** Số vàng hiện tại của người chơi. */
    public long gold;

    /** Số ngọc lục bảo hiện tại của người chơi. */
    public int gem;

    /** Số hồng ngọc hiện tại của người chơi. */
    public int ruby;

    /** Số tiền hiện tại của người chơi. */
    public int tien;

    /** Số phiếu giảm giá của người chơi. */
    public int coupon;

    /** Điểm sự kiện của người chơi. */
    public int event;

    /** Danh sách vật phẩm trong hộp thư. */
    public List<Item> itemsMailBox;

    /**
     * Khởi tạo đối tượng Inventory với các danh sách vật phẩm rỗng.
     */
    public Inventory() {
        itemsBody = new ArrayList<>();
        itemsBag = new ArrayList<>();
        itemsBox = new ArrayList<>();
        itemsBoxCrackBall = new ArrayList<>();
        itemsMailBox = new ArrayList<>();
        giftCode = new ArrayList<>();
    }

    /**
     * Tính tổng số ngọc lục bảo, hồng ngọc và tiền của người chơi.
     *
     * @return Tổng số ngọc lục bảo, hồng ngọc và tiền.
     */
    public int getGemAndRubyAndTien() {
        return this.gem + this.ruby + this.tien;
    }

    /**
     * Lấy giá trị tham số của một tùy chọn trên vật phẩm.
     *
     * @param it Vật phẩm cần kiểm tra.
     * @param id ID của tùy chọn cần lấy.
     * @return Giá trị tham số của tùy chọn, hoặc 0 nếu không tìm thấy.
     */
    public int getParam(Item it, int id) {
        for (ItemOption op : it.itemOptions) {
            if (op != null && op.optionTemplate.id == id) {
                return op.param;
            }
        }
        return 0;
    }

    /**
     * Kiểm tra xem một vật phẩm tại vị trí cụ thể có chứa tùy chọn với ID cho trước hay không.
     *
     * @param l Danh sách vật phẩm.
     * @param index Vị trí của vật phẩm trong danh sách.
     * @param id ID của tùy chọn cần kiểm tra.
     * @return true nếu vật phẩm có tùy chọn, false nếu không.
     */
    public boolean haveOption(List<Item> l, int index, int id) {
        Item it = l.get(index);
        if (it != null && it.isNotNullItem()) {
            return it.itemOptions.stream().anyMatch(op -> op != null && op.optionTemplate.id == id);
        }
        return false;
    }

    /**
     * Trừ số ngọc lục bảo, hồng ngọc và tiền của người chơi.
     *
     * @param num Số lượng cần trừ.
     */
    public void subGemAndRubyAndTien(int num) {
        this.ruby -= num;
        if (this.ruby < 0) {
            this.gem += this.ruby + this.tien;
            this.ruby = 0;
        }
    }

    /**
     * Thêm vàng vào kho của người chơi, đảm bảo không vượt quá giới hạn.
     *
     * @param gold Số vàng cần thêm.
     */
    public void addGold(int gold) {
        this.gold += gold;
        if (this.gold > LIMIT_GOLD) {
            this.gold = LIMIT_GOLD;
        }
    }

    /**
     * Giải phóng tài nguyên của đối tượng Inventory, bao gồm các vật phẩm và danh sách.
     */
    public void dispose() {
        if (this.trainArmor != null) {
            this.trainArmor.dispose();
        }
        this.trainArmor = null;
        if (this.itemsBody != null) {
            for (Item it : this.itemsBody) {
                it.dispose();
            }
            this.itemsBody.clear();
        }
        if (this.itemsBag != null) {
            for (Item it : this.itemsBag) {
                it.dispose();
            }
            this.itemsBag.clear();
        }
        if (this.itemsBox != null) {
            for (Item it : this.itemsBox) {
                it.dispose();
            }
            this.itemsBox.clear();
        }
        if (this.itemsBoxCrackBall != null) {
            for (Item it : this.itemsBoxCrackBall) {
                it.dispose();
            }
            this.itemsBoxCrackBall.clear();
        }
        this.itemsBody = null;
        this.itemsBag = null;
        this.itemsBox = null;
        this.itemsBoxCrackBall = null;
    }
}