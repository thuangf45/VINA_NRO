package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho boss ThienXinHang trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class ThienXinHang extends BossDHVT {

    /** Thời gian lần cuối sử dụng kỹ năng phân thân */
    private long lastTimePhanThan = System.currentTimeMillis();

    /** Constructor khởi tạo boss ThienXinHang với ID và dữ liệu từ BossesData */
    public ThienXinHang(Player player) throws Exception {
        super(BossID.THIEN_XIN_HANG, BossesData.THIEN_XIN_HANG);
        /** Gán người chơi mục tiêu cho boss */
        this.playerAtt = player;
    }

    /** Tấn công người chơi, bao gồm xóa hiệu ứng choáng và sử dụng kỹ năng phân thân */
    @Override
    public void attack() {
        super.attack();
        try {
            /** Xóa hiệu ứng choáng (stun) khỏi boss */
            EffectSkillService.gI().removeStun(this);
            /** Kiểm tra xem đã đủ 30 giây kể từ lần cuối sử dụng phân thân chưa */
            if (Util.canDoWithTime(lastTimePhanThan, 30000)) {
                lastTimePhanThan = System.currentTimeMillis();
                /** Gọi phương thức phân thân để tạo các bản sao */
                phanThan();
            }
        } catch (Exception ex) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }

    /** Tạo các bản sao của boss ThienXinHang */
    private void phanThan() {
        try {
            /** Tạo bốn bản sao của boss với các ID khác nhau */
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE1, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE2, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE3, playerAtt);
        } catch (Exception e) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }
}