package com.arriety.card;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp Card:
 * Lớp Card thuộc package com.arriety.card, biểu diễn một thẻ (card) trong game, chứa các thuộc tính như ID, số lượng hiện tại, số lượng tối đa, cấp độ, trạng thái sử dụng và danh sách tùy chọn (OptionCard). Lớp này chủ yếu dùng để lưu trữ thông tin thẻ, không chứa logic xử lý phức tạp, chỉ cung cấp các constructor để khởi tạo và phương thức toString để biểu diễn dữ liệu dưới dạng chuỗi JSON.
 */
public class Card {
    /**
     * Id: short - ID duy nhất của thẻ, dùng để xác định thẻ cụ thể trong game. Mặc định -1.
     */
    public short Id;

    /**
     * Amount: byte - Số lượng thẻ hiện tại mà người chơi sở hữu. Mặc định 0.
     */
    public byte Amount;

    /**
     * MaxAmount: byte - Số lượng tối đa của thẻ, xác định giới hạn số lượng thẻ có thể sở hữu. Mặc định 0.
     */
    public byte MaxAmount;

    /**
     * Level: byte - Cấp độ của thẻ, biểu thị mức độ nâng cấp hoặc sức mạnh của thẻ. Mặc định 0.
     */
    public byte Level;

    /**
     * Used: byte - Trạng thái sử dụng của thẻ (0 = chưa sử dụng), dùng để kiểm tra thẻ đã được sử dụng hay chưa. Mặc định 0.
     */
    public byte Used;

    /**
     * Options: List<OptionCard> - Danh sách các tùy chọn (option) của thẻ, chứa các thuộc tính bổ sung hoặc đặc tính đặc biệt của thẻ. Mặc định là danh sách rỗng (ArrayList).
     */
    public List<OptionCard> Options;

    /**
     * Constructor mặc định:
     * - Mô tả: Khởi tạo thẻ với tất cả thuộc tính mang giá trị mặc định.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Không trả về gì, tạo mới đối tượng Card với Id = -1, Amount = 0, MaxAmount = 0, Level = 0, Used = 0, Options = new ArrayList<>.
     */
    public Card() {
        Id = -1;
        Amount = 0;
        MaxAmount = 0;
        Level = 0;
        Used = 0;
        Options = new ArrayList<>();
    }
    
    /**
     * Constructor với số lượng tối đa và danh sách tùy chọn:
     * - Mô tả: Khởi tạo thẻ với số lượng tối đa và danh sách tùy chọn. Các thuộc tính khác được gán giá trị mặc định.
     * - Thuộc tính:
     *   - m: byte - Số lượng tối đa của thẻ (MaxAmount), xác định giới hạn số lượng thẻ có thể sở hữu.
     *   - o: List<OptionCard> - Danh sách các tùy chọn (option) của thẻ, chứa các thuộc tính bổ sung cho thẻ.
     * - Trả về: Không trả về gì, tạo mới đối tượng Card với MaxAmount = m, Options = o, các thuộc tính khác mặc định.
     */
    public Card(byte m, List<OptionCard> o) {
        MaxAmount = m;
        Options = o;
    }
    
    /**
     * Constructor với ID, số lượng, số lượng tối đa, cấp độ và danh sách tùy chọn:
     * - Mô tả: Khởi tạo thẻ với ID, số lượng hiện tại, số lượng tối đa, cấp độ và danh sách tùy chọn. Thuộc tính Used mặc định là 0.
     * - Thuộc tính:
     *   - i: short - ID duy nhất của thẻ.
     *   - a: byte - Số lượng hiện tại của thẻ (Amount).
     *   - ma: byte - Số lượng tối đa của thẻ (MaxAmount).
     *   - le: byte - Cấp độ của thẻ (Level).
     *   - o: List<OptionCard> - Danh sách các tùy chọn (option) của thẻ.
     * - Trả về: Không trả về gì, tạo mới đối tượng Card với Id = i, Amount = a, MaxAmount = ma, Level = le, Options = o, Used = 0.
     */
    public Card(short i, byte a, byte ma, byte le, List<OptionCard> o) {
        Id = i;
        Amount = a;
        MaxAmount = ma;
        Level = le;
        Options = o;
    }
    
    /**
     * Constructor đầy đủ:
     * - Mô tả: Khởi tạo thẻ với tất cả thuộc tính được chỉ định.
     * - Thuộc tính:
     *   - i: short - ID duy nhất của thẻ.
     *   - a: byte - Số lượng hiện tại của thẻ (Amount).
     *   - ma: byte - Số lượng tối đa của thẻ (MaxAmount).
     *   - le: byte - Cấp độ của thẻ (Level).
     *   - o: List<OptionCard> - Danh sách các tùy chọn (option) của thẻ.
     *   - u: byte - Trạng thái sử dụng của thẻ (Used).
     * - Trả về: Không trả về gì, tạo mới đối tượng Card với Id = i, Amount = a, MaxAmount = ma, Level = le, Options = o, Used = u.
     */
    public Card(short i, byte a, byte ma, byte le, List<OptionCard> o, byte u) {
        Id = i;
        Amount = a;
        MaxAmount = ma;
        Level = le;
        Options = o;
        Used = u;
    }
    
    /**
     * toString:
     * - Mô tả: Ghi đè phương thức toString để trả về biểu diễn chuỗi JSON của đối tượng Card, chứa thông tin tất cả thuộc tính.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Chuỗi (String) dạng JSON chứa các thuộc tính Id, Amount, MaxAmount, Options, Level, Used.
     */
    @Override
    public String toString() {
        final String n = "\"";
        return "{"
                + n + "id" + n + ":" + n + Id + n + ","
                + n + "amount" + n + ":" + n + Amount + n + ","
                + n + "max" + n + ":" + n + MaxAmount + n + ","
                + n + "option" + n + ":" + Options + ","
                + n + "level" + n + ":" + n + Level + n + ","
                + n + "used" + n + ":" + n + Used + n
                + "}";
    }
}