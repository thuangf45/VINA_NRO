package com.girlkun.models.task;

import com.girlkun.consts.ConstTask;

/**
 * Lớp quản lý các nhiệm vụ phụ của người chơi trong game.
 * @author Lucifer
 */
public class SideTask {

    /** Mẫu nhiệm vụ phụ của người chơi. */
    public SideTaskTemplate template;

    /** Số lượng đã hoàn thành của nhiệm vụ. */
    public int count;

    /** Số lượng tối đa cần đạt để hoàn thành nhiệm vụ. */
    public int maxCount;

    /** Cấp độ khó của nhiệm vụ. */
    public int level;

    /** Số lượng nhiệm vụ phụ còn lại có thể nhận. */
    public int leftTask;

    /** Thời điểm nhận nhiệm vụ. */
    public long receivedTime;

    /** Trạng thái thông báo tiến độ 0%. */
    public boolean notify0;

    /** Trạng thái thông báo tiến độ 10%. */
    public boolean notify10;

    /** Trạng thái thông báo tiến độ 20%. */
    public boolean notify20;

    /** Trạng thái thông báo tiến độ 30%. */
    public boolean notify30;

    /** Trạng thái thông báo tiến độ 40%. */
    public boolean notify40;

    /** Trạng thái thông báo tiến độ 50%. */
    public boolean notify50;

    /** Trạng thái thông báo tiến độ 60%. */
    public boolean notify60;

    /** Trạng thái thông báo tiến độ 70%. */
    public boolean notify70;

    /** Trạng thái thông báo tiến độ 80%. */
    public boolean notify80;

    /** Trạng thái thông báo tiến độ 90%. */
    public boolean notify90;

    /**
     * Khởi tạo đối tượng SideTask với số lượng nhiệm vụ phụ tối đa.
     */
    public SideTask() {
        this.leftTask = ConstTask.MAX_SIDE_TASK;
    }

    /**
     * Đặt lại toàn bộ thông tin nhiệm vụ phụ về trạng thái ban đầu.
     */
    public void reset() {
        this.template = null;
        this.count = 0;
        this.level = 0;
        this.notify0 = false;
        this.notify10 = false;
        this.notify20 = false;
        this.notify30 = false;
        this.notify40 = false;
        this.notify50 = false;
        this.notify60 = false;
        this.notify70 = false;
        this.notify80 = false;
        this.notify90 = false;
    }

    /**
     * Kiểm tra xem nhiệm vụ phụ đã hoàn thành hay chưa.
     *
     * @return true nếu nhiệm vụ đã hoàn thành, false nếu chưa.
     */
    public boolean isDone() {
        return this.count >= this.maxCount;
    }

    /**
     * Lấy tên của nhiệm vụ phụ, thay thế số lượng tối đa vào tên nhiệm vụ.
     *
     * @return Tên nhiệm vụ hoặc thông báo không có nhiệm vụ nếu template là null.
     */
    public String getName() {
        if (this.template != null) {
            return this.template.name.replaceAll("%1", String.valueOf(maxCount));
        }
        return "Hiện tại không có nhiệm vụ nào";
    }

    /**
     * Lấy tên cấp độ khó của nhiệm vụ phụ.
     *
     * @return Tên cấp độ khó hoặc thông báo mặc định nếu cấp độ không xác định.
     */
    public String getLevel() {
        switch (this.level) {
            case ConstTask.EASY:
                return "dễ";
            case ConstTask.NORMAL:
                return "bình thường";
            case ConstTask.HARD:
                return "khó";
            case ConstTask.VERY_HARD:
                return "rất khó";
            case ConstTask.HELL:
                return "địa ngục";
            default:
                return "Arriety.org";
        }
    }

    /**
     * Tính phần trăm tiến độ hoàn thành nhiệm vụ phụ.
     *
     * @return Phần trăm tiến độ hoàn thành (0-100).
     */
    public int getPercentProcess() {
        if (this.count >= this.maxCount) {
            return 100;
        }
        return (int) ((long) count * 100 / maxCount);
    }
}