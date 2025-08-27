package com.girlkun.models.task;

/**
 * Lớp quản lý nhiệm vụ của người chơi, bao gồm nhiệm vụ chính và nhiệm vụ phụ.
 * @author Lucifer
 */
public class TaskPlayer {

    /** Nhiệm vụ chính của người chơi. */
    public TaskMain taskMain;

    /** Nhiệm vụ phụ của người chơi. */
    public SideTask sideTask;

    /**
     * Khởi tạo đối tượng TaskPlayer với nhiệm vụ phụ mặc định.
     */
    public TaskPlayer() {
        this.sideTask = new SideTask();
    }

    /**
     * Giải phóng tài nguyên của đối tượng TaskPlayer.
     */
    public void dispose() {
        this.taskMain = null;
        this.sideTask = null;
    }
}