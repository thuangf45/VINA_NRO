package com.girlkun.models.boss;

/**
 * Trạng thái (state) của Boss trong vòng đời xuất hiện.
 * Dùng để điều khiển hành vi Boss theo từng giai đoạn.
 *
 * @author Lucifer
 */
public enum BossStatus {

    /** Nghỉ ngơi (chưa spawn, hoặc đang chờ hồi sinh) */
    REST,

    /** Đang trong thời gian chuẩn bị hồi sinh */
    RESPAWN,

    /** Bắt đầu vào bản đồ (map) xuất hiện */
    JOIN_MAP,

    /** Chat câu thoại mở đầu (Start) */
    CHAT_S,

    /** Hoạt động trong map (di chuyển, tấn công, skill...) */
    ACTIVE,

    /** Boss đã bị tiêu diệt */
    DIE,

    /** Chat câu thoại kết thúc (End) */
    CHAT_E,

    /** Rời khỏi map (sau khi kết thúc event hoặc die) */
    LEAVE_MAP
}
