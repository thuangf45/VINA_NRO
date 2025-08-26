package com.girlkun.models.matches;

import lombok.Builder;
import lombok.Data;

/**
 * Lớp đại diện cho thông tin xếp hạng của người chơi trong bảng xếp hạng (TOP).
 * @author Lucifer
 */
@Data
@Builder
public class TOP {

    /**
     * ID của người chơi trong bảng xếp hạng.
     */
    private int id_player;

    /**
     * Sức mạnh (power) của người chơi.
     */
    private long power;

    /**
     * Lượng ki (năng lượng) của người chơi.
     */
    private long ki;

    /**
     * Lượng máu (HP) của người chơi.
     */
    private long hp;

    /**
     * Lượng sức mạnh (strength) của người chơi.
     */
    private long sd;

    /**
     * Nhân vật (loại nhân vật) của người chơi.
     */
    private byte nv;

    /**
     * Số sự kiện (event) mà người chơi đã tham gia.
     */
    private int sk;

    /**
     * Số trận PVP mà người chơi đã tham gia.
     */
    private int pvp;

    /**
     * Thông tin bổ sung 1 của người chơi.
     */
    private String info1;

    /**
     * Thông tin bổ sung 2 của người chơi.
     */
    private String info2;

    /**
     * Lấy tên của người chơi trong bảng xếp hạng.
     * @return Tên của người chơi
     * @throws UnsupportedOperationException Nếu phương thức chưa được triển khai
     */
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Lấy sức mạnh của người chơi dưới dạng chuỗi.
     * @return Sức mạnh của người chơi
     * @throws UnsupportedOperationException Nếu phương thức chưa được triển khai
     */
    public String getPower() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Lấy thông tin nhân vật của người chơi dưới dạng chuỗi.
     * @return Thông tin nhân vật
     * @throws UnsupportedOperationException Nếu phương thức chưa được triển khai
     */
    public String getNv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Lấy số trận PVP của người chơi dưới dạng chuỗi.
     * @return Số trận PVP
     * @throws UnsupportedOperationException Nếu phương thức chưa được triển khai
     */
    public String getPvp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Lấy số sự kiện của người chơi dưới dạng chuỗi.
     * @return Số sự kiện
     * @throws UnsupportedOperationException Nếu phương thức chưa được triển khai
     */
    public String getSk() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}