package com.arriety.card;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp RadarService:
 * Lớp RadarService thuộc package com.arriety.card, quản lý các chức năng liên quan đến hệ thống thẻ radar trong game. Lớp này là một singleton, cung cấp các phương thức để gửi thông tin thẻ radar, cập nhật trạng thái sử dụng, cấp độ và số lượng thẻ cho người chơi thông qua các thông điệp (Message). Lớp sử dụng danh sách RADAR_TEMPLATE để lưu trữ các mẫu thẻ radar và tương tác với người chơi thông qua mạng (network).
 */
public class RadarService {
    /**
     * RADAR_TEMPLATE: List<RadarCard> - Danh sách các mẫu thẻ radar (RadarCard), lưu trữ thông tin các thẻ radar có sẵn trong game.
     */
    public List<RadarCard> RADAR_TEMPLATE = new ArrayList<>();

    /**
     * instance: RadarService - Biến tĩnh để lưu trữ instance duy nhất của lớp RadarService, đảm bảo chỉ có một đối tượng được tạo.
     */
    private static RadarService instance;

    /**
     * gI():
     * - Mô tả: Trả về instance duy nhất của lớp RadarService (singleton pattern). Nếu instance chưa được tạo, sẽ khởi tạo mới.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Đối tượng RadarService.
     */
    public static RadarService gI() {
        if (instance == null) {
            instance = new RadarService();
        }
        return instance;
    }

    /**
     * sendRadar(Player pl, List<Card> cards):
     * - Mô tả: Gửi thông tin tất cả thẻ radar trong RADAR_TEMPLATE tới người chơi, kèm theo thông tin thẻ cụ thể của người chơi (từ danh sách cards). Tạo thông điệp (Message) với mã 127, type 0, chứa chi tiết về các thẻ radar (ID, biểu tượng, cấp bậc, số lượng, v.v.) và gửi qua mạng.
     * - Thuộc tính:
     *   - pl: Player - Người chơi nhận thông điệp.
     *   - cards: List<Card> - Danh sách thẻ của người chơi, dùng để kiểm tra số lượng và trạng thái của từng thẻ.
     * - Xử lý logic:
     *   - Tạo thông điệp với mã 127, type 0.
     *   - Ghi số lượng thẻ radar trong RADAR_TEMPLATE.
     *   - Với mỗi thẻ radar, kiểm tra thẻ tương ứng trong danh sách cards của người chơi. Nếu không tìm thấy, tạo thẻ mới với số lượng tối đa và tùy chọn từ RADAR_TEMPLATE.
     *   - Ghi các thông tin của thẻ radar (ID, IconId, Rank, Amount, MaxAmount, Type, Template/Head/Body/Leg/Bag, Name, Info, Level, Used, Options) vào thông điệp.
     *   - Gửi thông điệp tới người chơi và dọn dẹp tài nguyên.
     * - Trả về: Không trả về gì, chỉ gửi thông điệp tới người chơi.
     */
    public void sendRadar(Player pl, List<Card> cards) {
        try {
            Message m = new Message(127);
            m.writer().writeByte(0);
            m.writer().writeShort(RadarService.gI().RADAR_TEMPLATE.size());
            for (RadarCard radar : RadarService.gI().RADAR_TEMPLATE) {
                Card card = cards.stream().filter(c -> c.Id == radar.Id).findFirst().orElse(null);
                if (card == null) {
                    card = new Card(radar.Max, radar.Options);
                }
                m.writer().writeShort(radar.Id);
                m.writer().writeShort(radar.IconId);
                m.writer().writeByte(radar.Rank);
                m.writer().writeByte(card.Amount);
                m.writer().writeByte(card.MaxAmount);
                m.writer().writeByte(radar.Type);
                switch (radar.Type) {
                    case 0:
                        m.writer().writeShort(radar.Template);
                        break;
                    case 1:
                        m.writer().writeShort(radar.Head);
                        m.writer().writeShort(radar.Body);
                        m.writer().writeShort(radar.Leg);
                        m.writer().writeShort(radar.Bag);
                        break;
                }
                m.writer().writeUTF(radar.Name);
                m.writer().writeUTF(radar.Info);
                m.writer().writeByte(card.Level);
                m.writer().writeByte(card.Used);
                m.writer().writeByte(radar.Options.size());
                for (OptionCard option : radar.Options) {
                    m.writer().writeByte(option.id);
                    m.writer().writeShort(option.param);
                    m.writer().writeByte(option.active);
                }
            }
            m.writer().flush();
            pl.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Radar1(Player pl, short id, int use):
     * - Mô tả: Gửi thông điệp cập nhật trạng thái sử dụng của một thẻ radar cụ thể tới người chơi. Thông điệp có mã 127, type 1.
     * - Thuộc tính:
     *   - pl: Player - Người chơi nhận thông điệp.
     *   - id: short - ID của thẻ radar cần cập nhật.
     *   - use: int - Trạng thái sử dụng mới của thẻ (ví dụ: 0 = chưa sử dụng, 1 = đã sử dụng).
     * - Xử lý logic:
     *   - Tạo thông điệp với mã 127, type 1.
     *   - Ghi ID thẻ và trạng thái sử dụng vào thông điệp.
     *   - Gửi thông điệp tới người chơi và dọn dẹp tài nguyên.
     * - Trả về: Không trả về gì, chỉ gửi thông điệp tới người chơi.
     */
    public void Radar1(Player pl, short id, int use) {
        try {
            Message message = new Message(127);
            message.writer().writeByte(1);
            message.writer().writeShort(id);
            message.writer().writeByte(use);
            message.writer().flush();
            pl.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * RadarSetLevel(Player pl, int id, int level):
     * - Mô tả: Gửi thông điệp cập nhật cấp độ của một thẻ radar cụ thể tới người chơi. Thông điệp có mã 127, type 2.
     * - Thuộc tính:
     *   - pl: Player - Người chơi nhận thông điệp.
     *   - id: int - ID của thẻ radar cần cập nhật.
     *   - level: int - Cấp độ mới của thẻ radar.
     * - Xử lý logic:
     *   - Tạo thông điệp với mã 127, type 2.
     *   - Ghi ID thẻ và cấp độ mới vào thông điệp.
     *   - Gửi thông điệp tới người chơi và dọn dẹp tài nguyên.
     * - Trả về: Không trả về gì, chỉ gửi thông điệp tới người chơi.
     */
    public void RadarSetLevel(Player pl, int id, int level) {
        try {
            Message message = new Message(127);
            message.writer().writeByte(2);
            message.writer().writeShort(id);
            message.writer().writeByte(level);
            message.writer().flush();
            pl.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * RadarSetAmount(Player pl, int id, int amount, int max_amount):
     * - Mô tả: Gửi thông điệp cập nhật số lượng hiện tại và số lượng tối đa của một thẻ radar cụ thể tới người chơi. Thông điệp có mã 127, type 3.
     * - Thuộc tính:
     *   - pl: Player - Người chơi nhận thông điệp.
     *   - id: int - ID của thẻ radar cần cập nhật.
     *   - amount: int - Số lượng hiện tại mới của thẻ.
     *   - max_amount: int - Số lượng tối đa mới của thẻ.
     * - Xử lý logic:
     *   - Tạo thông điệp với mã 127, type 3.
     *   - Ghi ID thẻ, số lượng hiện tại và số lượng tối đa vào thông điệp.
     *   - Gửi thông điệp tới người chơi và dọn dẹp tài nguyên.
     * - Trả về: Không trả về gì, chỉ gửi thông điệp tới người chơi.
     */
    public void RadarSetAmount(Player pl, int id, int amount, int max_amount) {
        try {
            Message message = new Message(127);
            message.writer().writeByte(3);
            message.writer().writeShort(id);
            message.writer().writeByte(amount);
            message.writer().writeByte(max_amount);
            message.writer().flush();
            pl.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
        }
    }
}