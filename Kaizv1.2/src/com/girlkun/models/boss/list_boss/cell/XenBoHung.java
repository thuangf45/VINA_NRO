package com.girlkun.models.boss.list_boss.cell;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.player.Player;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Boss Xen Bọ Hung (Imperfect Cell).
 *
 * Đặc điểm:
 * - Có 3 giai đoạn (XEN_BO_HUNG_1, 2, 3).
 * - Kỹ năng đặc biệt: "Hấp Thu" (tăng HP khi nuốt người chơi).
 *
 * Cơ chế chiến đấu:
 * - Tự động chuyển sang chế độ PK khi hoạt động.
 * - Thường xuyên tấn công và sử dụng kỹ năng "Hấp Thu".
 * - Hấp Thu gây sát thương trí mạng cho nạn nhân (khiến chết ngay).
 *
 * @author Lucifer
 */
public class XenBoHung extends Boss {

    /** Thời gian lần cuối sử dụng chiêu Hấp Thu */
    private long lastTimeHapThu;
    /** Thời gian hồi chiêu Hấp Thu */
    private int timeHapThu;

    /**
     * Khởi tạo Xen Bọ Hung với 3 giai đoạn tiến hóa.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public XenBoHung() throws Exception {
        super(BossID.XEN_BO_HUNG,
                BossesData.XEN_BO_HUNG_1,
                BossesData.XEN_BO_HUNG_2,
                BossesData.XEN_BO_HUNG_3);
    }

    /**
     * Thưởng cho người chơi hạ boss.
     *
     * @param plKill người chơi kết liễu boss
     */
    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        rewardFutureBoss(plKill);
    }

    /**
     * Hành vi hoạt động của boss:
     * - Tự động bật chế độ PK nếu chưa có.
     * - Thường xuyên tấn công và kích hoạt skill "Hấp Thu".
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
     * Skill đặc trưng "Hấp Thu":
     * - Có tỉ lệ rất thấp để kích hoạt (1% mỗi lần kiểm tra).
     * - Nuốt 1 player ngẫu nhiên trên bản đồ.
     * - Tăng HP của boss = HP hiện tại + 20% HP gốc (hpg).
     * - Giới hạn HP tối đa = 2 tỉ.
     * - Tăng chỉ số chí mạng (crit).
     * - Hồi phục HP bằng lượng HP của nạn nhân.
     * - Gây sát thương chí mạng khiến nạn nhân chết ngay.
     * - Gửi thông báo + chat trêu chọc.
     */
    private void hapThu() {
        // Kiểm tra hồi chiêu và tỉ lệ xuất chiêu (1%)
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
            return;
        }

        // Lấy ngẫu nhiên 1 player trong map
        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }

        // Cộng thêm HP = 20% chỉ số gốc (hpg)
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
