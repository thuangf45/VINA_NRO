package com.girlkun.models.task;

/**
 * Lớp đại diện cho nhiệm vụ phụ chính trong game, chứa thông tin về nhiệm vụ, NPC và bản đồ liên quan.
 * @author Lucifer
 */
public class SubTaskMain {

    /** Số lượng đã hoàn thành của nhiệm vụ. */
    public short count;

    /** Tên của nhiệm vụ phụ chính. */
    public String name;

    /** Số lượng tối đa cần đạt để hoàn thành nhiệm vụ. */
    public short maxCount;

    /** Thông báo hiển thị khi hoàn thành nhiệm vụ. */
    public String notify;

    /** ID của NPC liên quan đến nhiệm vụ. */
    public byte npcId;

    /** ID của bản đồ liên quan đến nhiệm vụ. */
    public short mapId;

    /**
     * Khởi tạo đối tượng SubTaskMain mặc định.
     */
    public SubTaskMain() {
    }

    /**
     * Khởi tạo đối tượng SubTaskMain bằng cách sao chép từ một nhiệm vụ khác.
     *
     * @param stm Nhiệm vụ phụ chính gốc để sao chép.
     */
    public SubTaskMain(SubTaskMain stm) {
        this.count = 0;
        this.name = stm.name;
        this.maxCount = stm.maxCount;
        this.npcId = stm.npcId;
        this.mapId = stm.mapId;
        this.notify = stm.notify;
    }
}