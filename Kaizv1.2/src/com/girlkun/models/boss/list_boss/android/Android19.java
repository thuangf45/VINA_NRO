package com.girlkun.models.boss.list_boss.android;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Boss Android 19.
 *
 * Đặc điểm:
 * - Khi nhận sát thương từ các skill đặc biệt (Kamejoko, Masenko, Antomic), Android 19 sẽ hấp thụ 80% sát thương đó thành HP hồi phục.
 * - Có khả năng chat ngẫu nhiên khi hấp thụ chiêu (20% tỉ lệ).
 *
 * Cơ chế phần thưởng:
 * - Xác suất 15% rơi item (hiện tại luôn chọn {@code itemRan[2]} là itemID = 383).
 * - Gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ khi người chơi giết boss.
 *
 * Cơ chế hoạt động:
 * - Ghi nhận thời điểm tham gia map (st = System.currentTimeMillis()).
 * - (Có sẵn code để rời map sau 15 phút nhưng hiện tại bị comment lại).
 *
 * Cơ chế chiến đấu:
 * - Nếu người chơi tấn công bằng Kamejoko / Masenko / Antomic → hấp thụ (không nhận sát thương) + hồi HP theo % damage.
 * - Nếu không → nhận sát thương bình thường theo logic cha.
 *
 * Cơ chế liên kết boss:
 * - Khi biến mất, nếu có parentBoss thì sẽ kích hoạt {@link Boss#changeToTypePK()} của parentBoss.
 *
 * @author Lucifer
 */
public class Android19 extends Boss {

    /** Thời điểm tham gia map (ms). */
    private long st;

    /**
     * Khởi tạo boss Android 19.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public Android19() throws Exception {
        super(BossID.ANDROID_19, BossesData.ANDROID_19);
    }

    /**
     * Phần thưởng khi tiêu diệt Android 19.
     *
     * @param plKill người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{381, 382, 383, 384, 385};
        int itemId = itemRan[2]; // luôn chọn item 383
        if (Util.isTrue(15, 100)) { // 15% tỉ lệ rơi đồ
            ItemMap it = new ItemMap(this.zone, itemId, 17,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Xử lý khi boss tham gia map.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /*
    @Override
    public void active() {
        super.active();
        // Nếu muốn: tự rời map sau 15 phút
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    */

    /**
     * Xử lý khi Android 19 nhận sát thương.
     * - Nếu bị trúng chiêu Kamejoko / Masenko / Antomic:
     *   + Không nhận damage.
     *   + Hồi phục 80% damage thành HP.
     *   + Có 20% tỉ lệ chat "Hấp thụ.. các ngươi nghĩ sao vậy?".
     * - Ngược lại → nhận sát thương bình thường.
     *
     * @return lượng sát thương thực nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                    int hpHoi = (int) ((long) damage * 80 / 100);
                    PlayerService.gI().hoiPhuc(this, hpHoi, 0);
                    if (Util.isTrue(1, 5)) {
                        this.chat("Hấp thụ.. các ngươi nghĩ sao vậy?");
                    }
                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    /**
     * Khi boss biến mất sẽ kích hoạt parentBoss chuyển sang trạng thái PK (nếu có).
     */
    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss != null) {
            this.parentBoss.changeToTypePK();
        }
    }
}
