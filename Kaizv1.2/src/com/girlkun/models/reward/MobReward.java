package com.girlkun.models.reward;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Lớp MobReward quản lý các phần thưởng từ quái vật trong game.
 * Lớp này lưu trữ thông tin về ID của quái vật và các danh sách vật phẩm thưởng,
 * bao gồm vật phẩm thông thường và vàng.
 * 
 * @author Lucifer
 */
@Data
public class MobReward {

    /**
     * ID của quái vật liên quan đến phần thưởng.
     */
    private int mobId;

    /**
     * Danh sách các vật phẩm thưởng thông thường từ quái vật.
     */
    private List<ItemMobReward> itemReward;

    /**
     * Danh sách các vật phẩm thưởng là vàng từ quái vật.
     */
    private List<ItemMobReward> goldReward;

    /**
     * Khởi tạo một đối tượng MobReward cho một quái vật cụ thể.
     * Khởi tạo các danh sách phần thưởng (vật phẩm và vàng) rỗng.
     * 
     * @param mobId ID của quái vật.
     */
    public MobReward(int mobId) {
        this.mobId = mobId;
        this.itemReward = new ArrayList<>();
        this.goldReward = new ArrayList<>();
    }
}