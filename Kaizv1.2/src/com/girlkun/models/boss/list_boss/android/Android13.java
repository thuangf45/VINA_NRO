package com.girlkun.models.boss.list_boss.android;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Boss Android 13.
 *
 * Đặc điểm:
 * - Khởi tạo với {@link BossID#ANDROID_13} và dữ liệu từ {@link BossesData#ANDROID_13}.
 * - Có khả năng phối hợp cùng Android 15 (chưa chết thì Android 13 sẽ không chết được).
 *
 * Cơ chế phần thưởng:
 * - Xác suất 15% rơi item có ID trong mảng itemRan (hiện đang fix cứng {@code itemRan[2]}).
 * - Gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ khi người chơi giết boss.
 *
 * Cơ chế chiến đấu:
 * - Nếu Android 15 còn sống → Android 13 không thể bị giết (damage trả về 0).
 * - Nếu parentBoss còn sống thì Android 13 cũng không chết cho đến khi parentBoss ngã xuống.
 * - Khi điều kiện thỏa mãn, xử lý sát thương bằng logic mặc định của {@link Boss#injured}.
 *
 * Cơ chế phối hợp:
 * - Khi Android 13 chat xong (doneChatS), nếu Android 15 còn sống thì sẽ đổi sang trạng thái PK
 *   để cùng tham chiến.
 * - Đồng thời, parentBoss cũng được đổi sang trạng thái PK.
 *
 * @author Lucifer
 */
public class Android13 extends Boss {

    /**
     * Khởi tạo boss Android 13.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public Android13() throws Exception {
        super(BossID.ANDROID_13, BossesData.ANDROID_13);
    }

    /**
     * Phần thưởng khi tiêu diệt Android 13.
     *
     * @param plKill người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{1142, 382, 383, 384, 1142};
        int itemId = itemRan[2]; // hiện tại luôn chọn item index 2 (ID = 383)
        if (Util.isTrue(15, 100)) { // 15% rơi đồ
            ItemMap it = new ItemMap(this.zone, itemId, 17,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        // Kiểm tra nhiệm vụ giết boss
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Khi boss hoàn tất câu thoại S (script chat).
     * Xử lý logic liên kết với Android 15 và parentBoss.
     */
    @Override
    public void doneChatS() {
        if (this.parentBoss == null) {
            return;
        }
        if (this.parentBoss.bossAppearTogether == null
                || this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.ANDROID_15 && !boss.isDie()) {
                boss.changeToTypePK(); // Android 15 vào trạng thái PK
                break;
            }
        }
        this.parentBoss.changeToTypePK(); // Parent boss cũng vào PK
    }

    /**
     * Xử lý khi Android 13 nhận sát thương.
     *
     * Điều kiện đặc biệt:
     * - Nếu Android 15 còn sống, Android 13 không thể chết (damage trả về 0).
     * - Nếu parentBoss chưa chết, Android 13 cũng không thể chết.
     *
     * @param plAtt       người chơi tấn công
     * @param damage      sát thương gây ra
     * @param piercing    có xuyên giáp không
     * @param isMobAttack có phải đòn của quái không
     * @return lượng sát thương thực tế Android 13 nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (damage >= this.nPoint.hp) {
            boolean flag = true;
            if (this.parentBoss != null) {
                if (this.parentBoss.bossAppearTogether != null
                        && this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] != null) {
                    for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
                        if (boss.id == BossID.ANDROID_15 && !boss.isDie()) {
                            flag = false; // Android 15 còn sống → không cho chết
                            break;
                        }
                    }
                }
                if (flag && !this.parentBoss.isDie()) {
                    flag = false; // parentBoss còn sống → không cho chết
                }
            }
            if (!flag) {
                return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }
}
