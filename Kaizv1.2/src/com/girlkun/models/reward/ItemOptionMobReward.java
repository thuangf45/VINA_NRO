package com.girlkun.models.reward;

import com.girlkun.models.Template;
import com.girlkun.server.Manager;
import lombok.Data;

/**
 * Lớp ItemOptionMobReward quản lý các tùy chọn (option) của vật phẩm thưởng từ quái vật trong game.
 * Lớp này xác định thông tin về mẫu tùy chọn vật phẩm, phạm vi tham số, và tỷ lệ xuất hiện của tùy chọn.
 * 
 * @author Lucifer
 */
@Data
public class ItemOptionMobReward {

    /**
     * Mẫu tùy chọn vật phẩm (template), lấy từ Manager.ITEM_OPTION_TEMPLATES.
     */
    private Template.ItemOptionTemplate temp;

    /**
     * Phạm vi tham số của tùy chọn (giá trị tối thiểu và tối đa).
     */
    private int[] param;

    /**
     * Tỷ lệ xuất hiện của tùy chọn (phần tử đầu tiên là xác suất, phần tử thứ hai là tổng số).
     */
    private int[] ratio;

    /**
     * Khởi tạo một đối tượng ItemOptionMobReward với thông tin về tùy chọn vật phẩm.
     * Đảm bảo các tham số tối thiểu và tối đa hợp lệ bằng cách xử lý giá trị âm và hoán đổi nếu cần.
     * 
     * @param tempId ID của mẫu tùy chọn vật phẩm.
     * @param param Phạm vi tham số của tùy chọn (tối thiểu và tối đa).
     * @param ratio Tỷ lệ xuất hiện của tùy chọn (xác suất và tổng số).
     */
    public ItemOptionMobReward(int tempId, int[] param, int[] ratio) {
        this.temp = Manager.ITEM_OPTION_TEMPLATES.get(tempId);
        this.param = param;
        if (this.param[0] < 0) {
            this.param[0] = -this.param[0];
        } else if (this.param[0] == 0) {
            this.param[0] = 1;
        }
        if (this.param[1] < 0) {
            this.param[1] = -this.param[1];
        } else if (this.param[1] == 0) {
            this.param[1] = 1;
        }
        if (this.param[0] > this.param[1]) {
            int tempSwap = this.param[0];
            this.param[0] = this.param[1];
            this.param[1] = tempSwap;
        }
        this.ratio = ratio;
    }
}