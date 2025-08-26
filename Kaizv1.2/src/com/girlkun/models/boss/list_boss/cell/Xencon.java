package com.girlkun.models.boss.list_boss.cell;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Boss Xen Con (Cell Jr - đám con do Perfect Cell sinh ra).
 *
 * Đặc điểm:
 * - Có thể xuất hiện nhiều Xen Con cùng lúc.
 * - Có khả năng "Hấp Thu" giống Cell, nhưng yếu hơn.
 *
 * Cơ chế chiến đấu:
 * - Tự động chuyển sang chế độ PK khi hoạt động.
 * - Tấn công người chơi liên tục.
 * - Đôi khi sử dụng kỹ năng "Hấp Thu":
 *   + Tăng HP dựa trên % HP gốc (20% hpg).
 *   + Tăng chỉ số chí mạng (crit).
 *   + Gây sát thương chết ngay cho người chơi bị nuốt.
 *
 * @author Lucifer
 */
public class Xencon extends Boss {

    /** Thời gian lần cuối sử dụng chiêu Hấp Thu */
    private long lastTimeHapThu;
    /** Thời gian hồi chiêu Hấp Thu */
    private int timeHapThu;

    /**
     * Khởi tạo 1 Xen Con.
     *
     * @throws Exception nếu không thể load dữ liệu boss
     */
    public Xencon() throws Exception {
        super(BossID.XEN_CON_1, BossesData.XEN_CON);
    }

    /**
     * Hành vi hoạt động của Xen Con:
     * - Luôn bật chế độ PK nếu chưa có.
     * - Tấn công player xung quanh.
     * - Có cơ hội sử dụng skill "Hấp Thu".
     */
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.hapThu();
        this.attack();
    }

    /**
     * Xử lý khi boss bị tiêu diệt.
     *
     * @param plKill người chơi kết liễu boss
     */
    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        rewardFutureBoss(plKill);
    }

    /**
     * Skill đặc trưng "Hấp Thu":
     * - Tỉ lệ xuất chiêu 1% mỗi lần kiểm tra.
     * - Lựa chọn ngẫu nhiên 1 player còn sống trong bản đồ.
     * - Nuốt nạn nhân → cộng thêm HP dựa trên 20% hpg.
     * - Giới hạn HP tối đa 2 tỉ.
     * - Tăng chỉ số chí mạng (crit).
     * - Hồi máu theo lượng HP của nạn nhân.
     * - Giết ngay lập tức nạn nhân bị hấp thu.
     * - Thông báo và chat "trêu chọc".
     */
    private void hapThu() {
        // Kiểm tra hồi chiêu + xác suất 1%
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
            return;
        }

        // Lấy random 1 player trong map
        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }

        // Tăng HP dựa trên 20% chỉ số gốc
        long HP = this.nPoint.hp + (long) (this.nPoint.hpg * 0.2);
        if (HP > 2000000000) {
            HP = 2000000000;
        }

        if (this.nPoint.hpg < HP) {
            this.nPoint.hpg = (int) HP;
        }

        this.nPoint.hp = (int) HP;
        this.nPoint.critg++;

        // Hồi máu dựa trên HP của nạn nhân
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);

        // Gây chết ngay cho player bị hấp thu
        pl.injured(null, pl.nPoint.hpMax, true, false);

        // Thông báo + chat
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");

        // Reset hồi chiêu
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(15000, 20000); // 15s - 20s
    }
}
