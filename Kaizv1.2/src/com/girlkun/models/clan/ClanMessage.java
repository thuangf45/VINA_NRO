package com.girlkun.models.clan;

/**
 * Đại diện cho một tin nhắn trong Clan (Bang hội).
 * 
 * Tin nhắn có thể bao gồm thông tin người gửi, loại tin nhắn, màu sắc hiển thị,
 * cũng như thông tin donate (hiến tặng/nhận) trong Clan.
 * 
 * @author Lucifer
 */
public class ClanMessage {

    /** Màu đen (mặc định) cho tin nhắn */
    public static final byte BLACK = 0;

    /** Màu đỏ cho tin nhắn (nổi bật, thông báo quan trọng) */
    public static final byte RED = 1;

    /** Clan mà tin nhắn này thuộc về */
    private Clan clan;

    /** ID của tin nhắn (tự tăng theo clanMessageId trong Clan) */
    public int id;

    /** Loại tin nhắn (ví dụ: donate, thông báo, chat, ...) */
    public byte type;

    /** ID người chơi gửi tin nhắn */
    public int playerId;

    /** Tên người chơi gửi tin nhắn */
    public String playerName;

    /** Sức mạnh của người gửi tại thời điểm nhắn */
    public long playerPower;

    /** Vai trò của người gửi trong Clan (Leader/Deputy/Member) */
    public byte role;

    /** Thời gian gửi tin nhắn (epoch second) */
    public int time;

    /** Nội dung tin nhắn */
    public String text;

    /** Số lần đã nhận donate từ tin nhắn này */
    public byte receiveDonate;

    /** Số lần donate tối đa có thể từ tin nhắn này */
    public byte maxDonate;

    /** Đánh dấu tin nhắn mới (1 = mới, 0 = đã xem) */
    public byte isNewMessage;

    /** Màu hiển thị của tin nhắn (BLACK hoặc RED) */
    public byte color;

    /**
     * Khởi tạo một tin nhắn Clan mới.
     * 
     * @param clan Clan mà tin nhắn này thuộc về
     */
    public ClanMessage(Clan clan) {
        this.clan = clan;
        this.id = clan.clanMessageId++;
        this.isNewMessage = 1;
        this.time = (int) (System.currentTimeMillis() / 1000);
    }
    
    /**
     * Giải phóng tài nguyên, xóa liên kết đến Clan và text để tránh rò rỉ bộ nhớ.
     */
    public void dispose(){
        this.clan = null;
        this.text = null;
    }
}
