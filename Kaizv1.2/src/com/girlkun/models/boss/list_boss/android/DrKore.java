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
 * Boss Dr. Kore (tương ứng Dr. Gero/Android 20).
 *
 * Đặc điểm:
 * - Có khả năng hấp thụ toàn bộ sát thương từ chiêu năng lượng (Kamejoko, Masenko, Antomic) → chuyển thành HP hồi phục 100% damage.
 * - Có khả năng ra lệnh cho Android 19 chat phụ họa.
 *
 * Cơ chế phần thưởng:
 * - Xác suất 15% rơi item (hiện tại luôn chọn {@code itemRan[2]} là itemID = 383).
 * - Sau khi bị hạ → gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ của người chơi.
 *
 * Cơ chế hội thoại (chat):
 * - {@link #chatM()} có thể:
 *   + Ngẫu nhiên gọi chat mặc định của boss.
 *   + Nếu Android 19 còn sống: DrKore ra lệnh hút năng lượng, Android 19 sẽ chat đáp lại.
 * - {@link #doneChatS()} → khi chat xong, sẽ chuyển Android 19 sang trạng thái PK.
 *
 * Cơ chế chiến đấu:
 * - Nhận damage từ skill đặc biệt (Kamejoko, Masenko, Antomic):
 *   + Không nhận sát thương.
 *   + Hồi lại HP bằng đúng lượng damage.
 *   + Có 20% tỉ lệ chat "Hấp thụ.. các ngươi nghĩ sao vậy?".
 * - Nếu không → nhận damage theo logic cha.
 *
 * Cơ chế chuyển trạng thái:
 * - Khi chuyển sang PK → chat "Mau đền mạng cho thằng em trai ta".
 *
 * @author Lucifer
 */
public class DrKore extends Boss {

    /**
     * Khởi tạo boss DrKore.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public DrKore() throws Exception {
        super(BossID.DR_KORE, BossesData.DR_KORE);
    }

    /**
     * Phần thưởng khi tiêu diệt DrKore.
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
     * Chat hành động (chatM).
     * - Có tỉ lệ ngẫu nhiên gọi {@link Boss#chatM()}.
     * - Nếu Android 19 còn sống → ra lệnh hút năng lượng và Android 19 chat lại.
     */
    @Override
    public void chatM() {
        if (Util.isTrue(60, 61)) { // random gọi chat mặc định
            super.chatM();
            return;
        }
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_19 && !boss.isDie()) {
                this.chat("Hút năng lượng của nó, mau lên");
                boss.chat("Tuân lệnh đại ca, hê hê hê");
                break;
            }
        }
    }

    /**
     * Xử lý khi boss hoạt động trong map.
     */
    @Override
    public void active() {
        super.active();
    }

    /**
     * Xử lý khi DrKore nhận sát thương.
     * - Nếu bị trúng chiêu năng lượng (Kamejoko, Masenko, Antomic):
     *   + Không nhận damage.
     *   + Hồi HP đúng bằng damage nhận vào.
     *   + Có 20% tỉ lệ chat "Hấp thụ.. các ngươi nghĩ sao vậy?".
     * - Ngược lại → nhận damage theo mặc định.
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
                    PlayerService.gI().hoiPhuc(this, damage, 0);
                    if (Util.isTrue(1, 5)) {
                        this.chat("Hấp thụ.. các ngươi nghĩ sao vậy?");
                    }
                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    /**
     * Khi DrKore chat xong → ép Android 19 chuyển sang trạng thái PK.
     */
    @Override
    public void doneChatS() {
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_19) {
                boss.changeToTypePK();
                break;
            }
        }
    }

    /**
     * Khi chuyển sang trạng thái PK sẽ kèm chat thách thức.
     */
    @Override
    public void changeToTypePK() {
        super.changeToTypePK();
        this.chat("Mau đền mạng cho thằng em trai ta");
    }
}
