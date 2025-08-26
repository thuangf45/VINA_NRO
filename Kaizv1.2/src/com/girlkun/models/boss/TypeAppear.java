package com.girlkun.models.boss;

/**
 * Kiểu xuất hiện (cách spawn) của Boss.
 * Dùng để xác định Boss xuất hiện độc lập, đi kèm hay do Boss khác gọi ra.
 *
 * @author Lucifer
 */
public enum TypeAppear {

    /** Boss xuất hiện mặc định (bình thường, theo lịch hoặc điều kiện game) */
    DEFAULT_APPEAR,

    /** Xuất hiện cùng lúc với một Boss khác */
    APPEAR_WITH_ANOTHER,

    /** Phiên bản cấp độ khác (Boss mạnh hơn hoặc yếu hơn) */
    ANOTHER_LEVEL,

    /** Được gọi ra bởi một Boss khác trong trận chiến */
    CALL_BY_ANOTHER
}
