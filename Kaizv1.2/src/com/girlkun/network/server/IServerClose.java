package com.girlkun.network.server;

/**
 * Giao diện định nghĩa phương thức để xử lý sự kiện khi máy chủ đóng.
 *
 * @author Lucifer
 */
public interface IServerClose {

    /**
     * Thực hiện các hành động cần thiết khi máy chủ được đóng.
     */
    void serverClose();
}