package com.girlkun.models.player;

/**
 * Lớp Location lưu trữ thông tin vị trí và thời gian di chuyển của người chơi trong trò chơi.
 * @author Lucifer
 */
public class Location {

    /**
     * Tọa độ x của người chơi trên bản đồ.
     */
    public int x;

    /**
     * Tọa độ y của người chơi trên bản đồ.
     */
    public int y;

    /**
     * Thời điểm lần cuối người chơi di chuyển (mili giây).
     */
    public long lastTimeplayerMove;
}