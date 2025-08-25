package com.arriety.card;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp RadarCard:
 * Lớp RadarCard thuộc package com.arriety.card, biểu diễn một thẻ radar (RadarCard) trong game, chứa thông tin về thẻ như ID, biểu tượng, cấp bậc, số lượng tối đa, loại, mẫu, các bộ phận liên quan (đầu, thân, chân, túi), tên, mô tả, danh sách tùy chọn, yêu cầu sử dụng và hào quang. Lớp này chỉ có một constructor mặc định để khởi tạo với các giá trị mặc định, không chứa logic xử lý phức tạp.
 */
public class RadarCard {
    /**
     * Id: short - ID duy nhất của thẻ radar, dùng để xác định thẻ cụ thể. Mặc định -1.
     */
    public short Id;

    /**
     * IconId: short - ID của biểu tượng (icon) hiển thị cho thẻ radar. Mặc định -1.
     */
    public short IconId;

    /**
     * Rank: byte - Cấp bậc của thẻ radar, biểu thị mức độ hiếm hoặc sức mạnh của thẻ. Mặc định 0.
     */
    public byte Rank;

    /**
     * Max: byte - Số lượng tối đa của thẻ radar có thể sở hữu. Mặc định 0.
     */
    public byte Max;

    /**
     * Type: byte - Loại thẻ radar, xác định danh mục hoặc chức năng của thẻ. Mặc định 0.
     */
    public byte Type;

    /**
     * Template: short - ID mẫu của thẻ, có thể liên quan đến kiểu thiết kế hoặc hiển thị. Mặc định 1.
     */
    public short Template;

    /**
     * Head: short - ID của bộ phận đầu (head) liên quan đến thẻ radar. Mặc định không gán giá trị cụ thể.
     */
    public short Head;

    /**
     * Body: short - ID của bộ phận thân (body) liên quan đến thẻ radar. Mặc định không gán giá trị cụ thể.
     */
    public short Body;

    /**
     * Leg: short - ID của bộ phận chân (leg) liên quan đến thẻ radar. Mặc định không gán giá trị cụ thể.
     */
    public short Leg;

    /**
     * Bag: short - ID của túi (bag) liên quan đến thẻ radar. Mặc định không gán giá trị cụ thể.
     */
    public short Bag;

    /**
     * Name: String - Tên của thẻ radar, dùng để hiển thị trong game. Mặc định là chuỗi rỗng.
     */
    public String Name;

    /**
     * Info: String - Mô tả hoặc thông tin chi tiết về thẻ radar. Mặc định là chuỗi rỗng.
     */
    public String Info;

    /**
     * Options: List<OptionCard> - Danh sách các tùy chọn (option) của thẻ radar, chứa các thuộc tính bổ sung hoặc đặc tính đặc biệt. Mặc định là danh sách rỗng (ArrayList).
     */
    public List<OptionCard> Options;

    /**
     * Require: short - Yêu cầu cần thiết để sử dụng thẻ radar (ví dụ: ID vật phẩm hoặc điều kiện). Mặc định -1.
     */
    public short Require;

    /**
     * RequireLevel: short - Cấp độ tối thiểu cần thiết để sử dụng thẻ radar. Mặc định 0.
     */
    public short RequireLevel;

    /**
     * AuraId: short - ID của hào quang (aura) liên quan đến thẻ radar, có thể dùng để hiển thị hiệu ứng. Mặc định -1.
     */
    public short AuraId;

    /**
     * Constructor mặc định:
     * - Mô tả: Khởi tạo đối tượng RadarCard với tất cả thuộc tính mang giá trị mặc định.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Không trả về gì, tạo mới đối tượng RadarCard với Id = -1, IconId = -1, Rank = 0, Max = 0, Type = 0, Template = 1, Head = 0, Body = 0, Leg = 0, Bag = 0, Name = "", Info = "", Options = new ArrayList<>(), Require = -1, RequireLevel = 0, AuraId = -1.
     */
    public RadarCard() {
        Id = -1;
        IconId = -1;
        Rank = 0;
        Max = 0;
        Type = 0;
        Template = 1;
        Name = "";
        Info = "";
        Options = new ArrayList<>();
        Require = -1;
        RequireLevel = 0;
        AuraId = -1;
    }
}