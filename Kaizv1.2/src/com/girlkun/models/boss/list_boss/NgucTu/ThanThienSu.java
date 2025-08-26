package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.Random;

/**
 * Boss Thần Thiên Sứ trong Ngục Tù.
 *
 * Đặc điểm:
 * - Dữ liệu khởi tạo dựa trên {@link BossesData#THAN_HUY_DIET}.
 * - Tồn tại trong map tối đa 15 phút (900.000 ms), sau đó tự rời đi.
 * - Có khả năng né đòn và giảm sát thương nhờ khiên (shield).
 *
 * Cơ chế phần thưởng:
 * - 1/30 tỉ lệ rơi một item trong danh sách itemDos1 (đồ hiếm).
 * - 30% tỉ lệ rơi item trong danh sách itemDos2 (Ngọc Rồng 4,5,6,7 sao).
 * - (Danh sách itemDos3 được khai báo nhưng hiện không dùng đến).
 *
 * Cơ chế chiến đấu:
 * - Có xác suất né đòn dựa trên tlNeDon.
 * - Nếu có khiên chắn, sát thương nhận giảm một nửa, khi sát thương vượt quá HP tối đa thì khiên sẽ vỡ.
 *
 * @author Lucifer
 */
public class ThanThienSu extends Boss {

    /** Thời điểm boss tham gia map (ms) */
    private long st;

    /**
     * Khởi tạo boss Thần Thiên Sứ với dữ liệu định nghĩa trong BossesData.
     *
     * @throws Exception nếu có lỗi trong quá trình khởi tạo
     */
    public ThanThienSu() throws Exception {
        super(BossID.THAN_HUY_DIET, BossesData.THAN_HUY_DIET);
    }

    /**
     * Xử lý phần thưởng khi boss bị tiêu diệt.
     *
     * @param plKill Người chơi hạ gục boss
     */
    public void reward(Player plKill) {
        int[] itemDos1 = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567}; // Đồ hiếm
        int[] itemDos2 = new int[]{14, 15, 16, 17}; // Ngọc Rồng 4-7 sao
        int[] itemDos3 = new int[]{650, 652, 654, 651, 653, 655, 657, 659, 661, 658, 660, 662}; // (khai báo nhưng không dùng)

        int randomDo = new Random().nextInt(itemDos1.length);
        int randomDo1 = new Random().nextInt(itemDos2.length);
        int randomDo2 = new Random().nextInt(itemDos3.length);

        // 1/30 cơ hội rơi item trong danh sách itemDos1
        if (Util.isTrue(1, 30)) {
            Service.getInstance().dropItemMap(this.zone,
                    Util.ratiItem(zone, itemDos1[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }

        // 30% cơ hội rơi Ngọc Rồng (14-17)
        if (Util.isTrue(30, 100)) {
            Service.getInstance().dropItemMap(this.zone,
                    Util.ratiItem(zone, itemDos2[randomDo1], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    /**
     * Khi boss tham gia map, lưu thời gian xuất hiện để tính thời gian tồn tại.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /**
     * Vòng lặp hoạt động: nếu quá 15 phút thì boss tự rời map.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) { // 900.000 ms = 15 phút
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Xử lý sát thương khi boss bị tấn công.
     *
     * @param plAtt      Người chơi tấn công
     * @param damage     Lượng sát thương gây ra
     * @param piercing   Có phải đòn xuyên giáp không
     * @param isMobAttack Đòn đánh từ quái hay từ người chơi
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

            // Nếu có khiên chắn thì giảm nửa sát thương
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }

            // Trừ máu boss
            this.nPoint.subHP(damage);

            // Kiểm tra chết
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}
