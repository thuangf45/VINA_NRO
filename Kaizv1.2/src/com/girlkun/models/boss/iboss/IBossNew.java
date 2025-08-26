package com.girlkun.models.boss.iboss;

import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.player.Player;

/**
 * Interface định nghĩa các hành vi và trạng thái của boss mới trong game.
 * @author Lucifer
 */
public interface IBossNew {

    /**
     * Cập nhật trạng thái hoặc hành vi của boss theo thời gian thực.
     */
    void update();

    /**
     * Khởi tạo các thuộc tính cơ bản của boss khi được tạo ra.
     */
    void initBase();

    /**
     * Thay đổi trạng thái của boss (ví dụ: từ nghỉ sang tấn công).
     * @param status Trạng thái mới của boss
     */
    void changeStatus(BossStatus status);

    /**
     * Lấy thông tin người chơi mà boss đang tấn công.
     * @return Người chơi mục tiêu
     */
    Player getPlayerAttack();

    /**
     * Chuyển boss sang trạng thái PK (Player Killer - có thể tấn công người chơi).
     */
    void changeToTypePK();

    /**
     * Chuyển boss sang trạng thái không PK (Non-Player Killer - không tấn công người chơi).
     */
    void changeToTypeNonPK();
    
    /**
     * Di chuyển boss đến vị trí của người chơi.
     * @param player Người chơi mà boss sẽ di chuyển đến
     */
    void moveToPlayer(Player player);
    
    /**
     * Di chuyển boss đến tọa độ cụ thể trên bản đồ.
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    void moveTo(int x, int y);
    
    /**
     * Kiểm tra xem người chơi có bị boss tiêu diệt hay không.
     * @param player Người chơi cần kiểm tra
     */
    void checkPlayerDie(Player player);
    
    /**
     * Kích hoạt các boss khác khi boss này xuất hiện.
     */
    void wakeupAnotherBossWhenAppear();
    
    /**
     * Kích hoạt các boss khác khi boss này biến mất.
     */
    void wakeupAnotherBossWhenDisappear();
    
    /**
     * Xử lý phần thưởng cho người chơi khi tiêu diệt boss.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    void reward(Player plKill);
    
    /**
     * Thực hiện hành động tấn công của boss.
     */
    void attack();

    /**
     * Đưa boss vào trạng thái nghỉ ngơi.
     */
    void rest();

    /**
     * Hồi sinh boss sau khi bị tiêu diệt.
     */
    void respawn();

    /**
     * Thêm boss vào bản đồ khi bắt đầu hoạt động.
     */
    void joinMap();

    /**
     * Kiểm tra xem boss có thể thực hiện đoạn hội thoại đặc biệt (chatS) hay không.
     * @return True nếu boss có thể chat, false nếu không
     */
    boolean chatS();
    
    /**
     * Hoàn thành đoạn hội thoại đặc biệt (chatS) của boss.
     */
    void doneChatS();

    /**
     * Kích hoạt hành vi của boss (ví dụ: bắt đầu tấn công hoặc di chuyển).
     */
    void active();
    
    /**
     * Thực hiện đoạn hội thoại chính (chatM) của boss.
     */
    void chatM();

    /**
     * Xử lý khi boss bị tiêu diệt.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    void die(Player plKill);

    /**
     * Kiểm tra xem boss có thể thực hiện đoạn hội thoại kết thúc (chatE) hay không.
     * @return True nếu boss có thể chat, false nếu không
     */
    boolean chatE();
    
    /**
     * Hoàn thành đoạn hội thoại kết thúc (chatE) của boss.
     */
    void doneChatE();

    /**
     * Xóa boss khỏi bản đồ khi chết hoặc kết thúc hoạt động.
     */
    void leaveMap();
}