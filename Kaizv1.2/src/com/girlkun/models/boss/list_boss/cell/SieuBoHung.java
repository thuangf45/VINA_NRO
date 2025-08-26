package com.girlkun.models.boss.list_boss.cell;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import com.girlkun.models.boss.list_boss.BDKB.TrungUyXanhLo;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Boss Siêu Bọ Hung (Perfect Cell).
 *
 * Đặc điểm:
 * - Có 3 giai đoạn (SIEU_BO_HUNG_1, 2, 3).
 * - Kỹ năng đặc biệt: "Hấp Thu" (tăng HP và chỉ số khi nuốt người chơi).
 * - Thời gian tồn tại trên bản đồ tối đa: 15 phút.
 *
 * Cơ chế chiến đấu:
 * - Tự động chuyển sang chế độ PK khi vào map.
 * - Thường xuyên tấn công và kích hoạt kỹ năng "Hấp Thu".
 * - Có khả năng né đòn và sử dụng khiên chắn giống các boss khác.
 *
 * @author Lucifer
 */
public class SieuBoHung extends Boss {

    /** Thời gian lần cuối sử dụng chiêu Hấp Thu */
    private long lastTimeHapThu;
    /** Thời gian hồi chiêu Hấp Thu */
    private int timeHapThu;
    /** Đánh dấu trạng thái Super (không sử dụng nhiều trong logic hiện tại) */
    private int initSuper = 0;
    /** Thời điểm boss xuất hiện trên bản đồ */
    private long st;

    /**
     * Khởi tạo Siêu Bọ Hung với 3 giai đoạn tiến hóa.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public SieuBoHung() throws Exception {
        super(BossID.SIEU_BO_HUNG,
                BossesData.SIEU_BO_HUNG_1,
                BossesData.SIEU_BO_HUNG_2,
                BossesData.SIEU_BO_HUNG_3);
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
     * Khi boss tham gia bản đồ → lưu lại thời gian xuất hiện.
     */
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    /**
     * Hành vi hoạt động của boss:
     * - Tự động bật chế độ PK nếu chưa có.
     * - Liên tục dùng skill Hấp Thu theo thời gian hồi chiêu.
     * - Tấn công người chơi.
     * - Rời map nếu tồn tại quá 15 phút.
     */
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
            this.hapThu();
        } catch (Exception ex) {
            Logger.getLogger(SieuBoHung.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.attack();
        super.active();

        if (Util.canDoWithTime(st, 900000)) { // 15 phút
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý khi boss nhận sát thương.
     *
     * @param plAtt       người chơi tấn công
     * @param damage      sát thương nhận vào
     * @param piercing    có xuyên giáp không
     * @param isMobAttack có phải mob tấn công không
     * @return lượng sát thương thực tế nhận vào
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

            // Giảm sát thương theo def
            damage = this.nPoint.subDameInjureWithDeff(damage);

            // Khiên chắn
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }

            // Trừ máu
            this.nPoint.subHP(damage);

            // Nếu chết thì xử lý die
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    // Hàm triệu hồi Trung Úy Xanh Lơ (chưa dùng, để comment lại)
    // public void summonTrungUyXanhLo() {
    //     for (int i = 0; i < 5; i++) {
    //         try {
    //             new TrungUyXanhLo(this.zone, 2,
    //                     Util.nextInt(1000, 16000),
    //                     Util.nextInt(16000000));
    //         } catch (Exception ex) {
    //             //
    //         }
    //     }
    // }

    /**
     * Skill đặc trưng "Hấp Thu":
     * - Nuốt 1 player ngẫu nhiên trên bản đồ.
     * - Tăng HP của boss bằng tổng HP hiện tại + HP của player đó.
     * - Giới hạn HP tối đa = 2 tỉ.
     * - Tăng chỉ số chí mạng (crit).
     * - Hồi phục HP bằng lượng HP của nạn nhân.
     * - Gửi thông báo + chat trêu chọc.
     *
     * @throws Exception nếu lỗi trong quá trình xử lý
     */
    private void hapThu() throws Exception {
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }

        // Cộng thêm HP bằng với HP của player bị hấp thu
        long HP = this.nPoint.hp + pl.nPoint.hp;
        if (HP > 2000000000) {
            HP = 2000000000;
        }

        if (this.nPoint.hpg < HP) {
            this.nPoint.hpg = (int) HP;
        }

        this.nPoint.hp = (int) HP;
        this.nPoint.critg++;

        // Hồi phục máu
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);

        // Thông báo cho người chơi bị hấp thu
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");

        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(15000, 20000); // 15s - 20s hồi chiêu
    }
}
