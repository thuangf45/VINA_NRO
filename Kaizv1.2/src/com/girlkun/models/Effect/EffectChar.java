package com.girlkun.models.Effect;

/**
 * Lớp đại diện cho hiệu ứng của nhân vật trong game.
 * @author Lucifer
 */
public class EffectChar {
    
    public int id;
    
    public int layer;
    
    public int loop;
    
    public int loopCount;
    
    public int isStand;

    /**
     * Khởi tạo một hiệu ứng nhân vật với các thuộc tính được chỉ định.
     * @param id ID của hiệu ứng
     * @param layer Lớp hiển thị của hiệu ứng (độ ưu tiên hiển thị)
     * @param loop Số lần lặp lại của hiệu ứng
     * @param loopCount Số lần đã lặp lại của hiệu ứng
     * @param isStand Trạng thái đứng yên (0: không đứng yên, 1: đứng yên)
     */
    public EffectChar(int id, int layer, int loop, int loopCount, int isStand) {
        this.id = id;
        this.layer = layer;
        this.loop = loop;
        this.loopCount = loopCount;
        this.isStand = isStand;
    }
}