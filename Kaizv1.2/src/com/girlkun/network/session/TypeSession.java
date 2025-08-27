package com.girlkun.network.session;

/**
 * Enum định nghĩa các loại phiên làm việc mạng.
 * Dùng để phân biệt giữa phiên làm việc phía server và phía client.
 *
 * @author Lucifer
 */
public enum TypeSession {

    /**
     * Đại diện cho phiên làm việc phía server.
     */
    SERVER,

    /**
     * Đại diện cho phiên làm việc phía client.
     */
    CLIENT;
}