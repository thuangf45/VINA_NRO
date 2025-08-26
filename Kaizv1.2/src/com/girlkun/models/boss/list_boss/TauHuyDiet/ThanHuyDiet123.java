package com.girlkun.models.boss.list_boss.TauHuyDiet;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Boss Thần Hủy Diệt (bản đặc biệt trong Tàu Hủy Diệt).
 *
 * Đặc điểm:
 * - Sinh ra với dữ liệu trong {@link BossesData#THAN_HUY_DIET1}.
 * - Có cơ chế đặc biệt "Hakai" (hủy diệt ngẫu nhiên người chơi trong map).
 * - Khi Hakai kích hoạt, boss tăng mạnh chỉ số và hồi phục máu.
 *
 * Cơ chế chiến đấu:
 * - Có tỉ lệ né đòn dựa trên {@code tlNeDon}.
 * - Nếu đang có khiên chắn: sát thương nhận vào giảm một nửa, khi sát thương vượt quá HP tối đa thì khiên sẽ vỡ.
 * - Boss chủ động chuyển sang trạng thái PK khi xuất hiện.
 *
 * Cơ chế Hakai:
 * - Kích hoạt ngẫu nhiên sau một khoảng thời gian 20–30 giây.
 * - Khi thực hiện:
 *   + Boss hút máu người chơi trong map, hồi phục cho bản thân.
 *   + Boss tăng sát thương (+5% theo dame của nạn nhân), máu (+2% theo hp của nạn nhân), và chí mạng (+1).
 *   + Người chơi bị trúng Hakai sẽ chết ngay lập tức (mất toàn bộ HP).
 *   + Boss phát ra thông báo & chat trêu người chơi.
 *
 * Cơ chế phần thưởng:
 * - Dùng phương thức {@link #rewardFutureBoss(Player)} để rơi đồ (định nghĩa ở Boss cha).
 *
 * @author 
 *  - Code gốc: team dev
 *  - JavaDoc & chú thích: Lucifer
 */
public class ThanHuyDiet123 extends Boss {

    /** Thời gian cuối cùng sử dụng Hakai (ms) */
    private long lasttimehakai;

    /** Thời gian hồi lại Hakai (ngẫu nhiên 20–30 giây) */
    private int timehakai;

    /** Thời điểm boss xuất hiện trong map */
    private long st;

    /**
     * Khởi tạo Thần Hủy Diệt với dữ liệu định nghĩa sẵn trong BossesData.
     *
     * @throws Exception nếu có lỗi khởi tạo
     */
    public ThanHuyDiet123() throws Exception {
        super(Util.randomBossId(), BossesData.THAN_HUY_DIET1);
    }

    /**
     * Xử lý phần thưởng khi boss bị tiêu diệt.
     *
     * @param plKill Người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        rewardFutureBoss(plKill);
    }

    /**
     * Xử lý sát thương khi boss bị tấn công.
     *
     * @param plAtt       Người chơi tấn công
     * @param damage      Sát thương gây ra
     * @param piercing    Đòn có xuyên giáp hay không
     * @param isMobAttack Đòn từ quái hay từ người chơi
     * @return sát thương thực tế boss nhận vào
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

            // Giảm sát thương theo chỉ số phòng thủ
            damage = this.nPoint.subDameInjureWithDeff(damage);

            // Khiên chắn: giảm 1/2 sát thương
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }

            // Trừ máu
            this.nPoint.subHP(damage);

            // Nếu chết thì xử lý
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Vòng lặp hoạt động của boss:
     * - Luôn bật chế độ PK.
     * - Chủ động tấn công.
     * - Thỉnh thoảng kích hoạt Hakai.
     */
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.huydiet();
        this.attack();
    }

    /**
     * Cơ chế Hakai: Hủy diệt một người chơi ngẫu nhiên trong map và tăng sức mạnh cho boss.
     */
    private void huydiet() {
        // Điều kiện: phải đủ thời gian hồi và có tỉ lệ random
        if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(1, 100)) {
            return;
        }

        // Chọn player ngẫu nhiên trong map
        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }

        // Tăng chỉ số boss dựa trên chỉ số của nạn nhân
        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
        this.nPoint.critg++;
        this.nPoint.calPoint();

        // Hút máu từ nạn nhân
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);

        // Giết ngay lập tức nạn nhân
        pl.injured(null, pl.nPoint.hpMax, true, false);

        // Gửi thông báo và chat
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " cho bay màu");
        this.chat(2, "Hắn ta mạnh quá, coi chừng " + pl.name + ", tên " + this.name + " hắn không giống như những kẻ thù trước đây");
        this.chat("Thật là yếu ớt " + pl.name);

        // Cập nhật lại thời gian hồi Hakai
        this.lasttimehakai = System.currentTimeMillis();
        this.timehakai = Util.nextInt(20000, 30000); // 20-30 giây
    }
}
