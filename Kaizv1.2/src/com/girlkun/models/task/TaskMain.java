package com.girlkun.models.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp đại diện cho nhiệm vụ chính của người chơi trong game, chứa thông tin và danh sách nhiệm vụ phụ.
 * @author Lucifer
 */
public class TaskMain {

    /** ID của nhiệm vụ chính. */
    public int id;

    /** Chỉ số (vị trí) của nhiệm vụ trong danh sách nhiệm vụ. */
    public int index;

    /** Tên của nhiệm vụ chính. */
    public String name;

    /** Mô tả chi tiết của nhiệm vụ chính. */
    public String detail;

    /** Danh sách các nhiệm vụ phụ thuộc nhiệm vụ chính. */
    public List<SubTaskMain> subTasks;

    /**
     * Khởi tạo đối tượng TaskMain với danh sách nhiệm vụ phụ rỗng.
     */
    public TaskMain() {
        this.subTasks = new ArrayList<>();
    }

    /**
     * Khởi tạo đối tượng TaskMain bằng cách sao chép từ một nhiệm vụ chính khác.
     *
     * @param taskMain Nhiệm vụ chính gốc để sao chép.
     */
    public TaskMain(TaskMain taskMain) {
        this.id = taskMain.id;
        this.index = taskMain.index;
        this.name = taskMain.name;
        this.detail = taskMain.detail;
        this.subTasks = new ArrayList<>();
        for (SubTaskMain stm : taskMain.subTasks) {
            this.subTasks.add(new SubTaskMain(stm));
        }
    }
}