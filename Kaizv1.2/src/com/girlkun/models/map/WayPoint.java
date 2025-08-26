package com.girlkun.models.map;

/**
 * Đại diện cho điểm dịch chuyển (WayPoint) trên bản đồ, cho phép người chơi di chuyển 
 * từ khu vực này sang khu vực khác hoặc sang bản đồ khác.
 * 
 * @author Lucifer
 */
public class WayPoint {

    /** Tọa độ X nhỏ nhất (biên trái) của khu vực waypoint */
    public short minX;

    /** Tọa độ Y nhỏ nhất (biên trên) của khu vực waypoint */
    public short minY;

    /** Tọa độ X lớn nhất (biên phải) của khu vực waypoint */
    public short maxX;

    /** Tọa độ Y lớn nhất (biên dưới) của khu vực waypoint */
    public short maxY;

    /** Cờ cho biết waypoint có phải là điểm vào bản đồ hay không */
    public boolean isEnter;

    /** Cờ cho biết waypoint có phải là bản đồ ngoại tuyến (offline map) hay không */
    public boolean isOffline;

    /** Tên waypoint (nếu có) */
    public String name;

    /** ID của bản đồ mà waypoint dẫn đến */
    public int goMap;

    /** Tọa độ X trong bản đồ đích sau khi dịch chuyển */
    public short goX;

    /** Tọa độ Y trong bản đồ đích sau khi dịch chuyển */
    public short goY;
}