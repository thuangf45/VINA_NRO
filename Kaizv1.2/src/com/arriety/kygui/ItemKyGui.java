package com.arriety.kygui;

import com.girlkun.models.item.Item.ItemOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp ItemKyGui:
 * Lớp ItemKyGui thuộc package com.arriety.kygui, biểu diễn một vật phẩm được ký gửi trong hệ thống cửa hàng ký gửi của game. Lớp này lưu trữ thông tin về vật phẩm như ID, người bán, giá bán, số lượng, trạng thái hiển thị, và các tùy chọn bổ sung. Lớp chỉ cung cấp các constructor để khởi tạo, không chứa logic xử lý phức tạp.
 */
public class ItemKyGui {
    /**
     * id: int - ID duy nhất của vật phẩm ký gửi, dùng để phân biệt các vật phẩm trong hệ thống ký gửi.
     */
    public int id;

    /**
     * itemId: short - ID của vật phẩm, xác định loại vật phẩm cụ thể trong game.
     */
    public short itemId;

    /**
     * player_sell: int - ID của người chơi đăng bán vật phẩm ký gửi.
     */
    public int player_sell;

    /**
     * tab: byte - Tab (danh mục) trong cửa hàng ký gửi nơi vật phẩm được hiển thị.
     */
    public byte tab;

    /**
     * goldSell: int - Giá bán vật phẩm bằng vàng (thỏi vàng). Giá trị âm hoặc 0 nếu không bán bằng vàng.
     */
    public int goldSell;

    /**
     * gemSell: int - Giá bán vật phẩm bằng hồng ngọc. Giá trị âm hoặc 0 nếu không bán bằng hồng ngọc.
     */
    public int gemSell;

    /**
     * quantity: int - Số lượng vật phẩm ký gửi.
     */
    public int quantity;

    /**
     * isUpTop: byte - Trạng thái ưu tiên hiển thị của vật phẩm (1 = hiển thị ở đầu danh sách, 0 = không ưu tiên).
     */
    public byte isUpTop;

    /**
     * options: List<ItemOption> - Danh sách các tùy chọn (option) của vật phẩm, chứa các thuộc tính bổ sung như chỉ số hoặc hiệu ứng. Mặc định là danh sách rỗng (ArrayList).
     */
    public List<ItemOption> options = new ArrayList<>();

    /**
     * isBuy: int - Trạng thái mua của vật phẩm (0 = đang bán, 1 = đã bán, 2 = hết hạn).
     */
    public int isBuy;

    /**
     * createTime: long - Thời gian tạo vật phẩm ký gửi, dùng để kiểm tra thời gian hết hạn.
     */
    public long createTime;

    /**
     * Constructor mặc định:
     * - Mô tả: Khởi tạo đối tượng ItemKyGui với các thuộc tính mặc định (không gán giá trị cụ thể).
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Không trả về gì, tạo mới đối tượng ItemKyGui với các thuộc tính mặc định.
     */
    public ItemKyGui() {
    }

    /**
     * Constructor đầy đủ:
     * - Mô tả: Khởi tạo đối tượng ItemKyGui với tất cả thuộc tính được chỉ định.
     * - Thuộc tính:
     *   - i: int - ID của vật phẩm ký gửi.
     *   - id: short - ID của vật phẩm trong game.
     *   - plId: int - ID của người chơi đăng bán vật phẩm.
     *   - t: byte - Tab hiển thị vật phẩm trong cửa hàng ký gửi.
     *   - gold: int - Giá bán bằng vàng.
     *   - gem: int - Giá bán bằng hồng ngọc.
     *   - q: int - Số lượng vật phẩm ký gửi.
     *   - isUp: byte - Trạng thái ưu tiên hiển thị (1 = lên top, 0 = không).
     *   - op: List<ItemOption> - Danh sách tùy chọn của vật phẩm.
     *   - b: byte - Trạng thái mua (0 = đang bán, 1 = đã bán, 2 = hết hạn).
     *   - createTime: long - Thời gian tạo vật phẩm ký gửi.
     * - Trả về: Không trả về gì, tạo mới đối tượng ItemKyGui với các thuộc tính được gán giá trị từ tham số.
     */
    public ItemKyGui(int i, short id, int plId, byte t, int gold, int gem, int q, byte isUp, List<ItemOption> op, byte b, long createTime) {
        this.id = i;
        itemId = id;
        player_sell = plId;
        tab = t;
        goldSell = gold;
        gemSell = gem;
        quantity = q;
        isUpTop = isUp;
        options = op;
        isBuy = b;
        this.createTime = createTime;
    }
}