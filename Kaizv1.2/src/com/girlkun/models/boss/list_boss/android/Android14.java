package com.girlkun.models.boss.list_boss.android;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Boss Android 14.
 *
 * Đặc điểm:
 * - Khi sắp chết lần đầu, Android 14 sẽ gọi **Android 13** xuất hiện trở lại (RESPAWN).
 * - Đồng thời, sẽ hồi phục bản thân và phối hợp với Android 15 (cho Android 15 hồi máu & vào trạng thái non-PK).
 *
 * Cơ chế phần thưởng:
 * - Xác suất 15% rơi item (hiện tại luôn chọn {@code itemRan[2]} là itemID = 383).
 * - Gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ khi người chơi giết boss.
 *
 * Cơ chế chiến đấu:
 * - Khi còn sống và chưa gọi Android 13 → tự động chuyển sang trạng thái PK (PvP).
 * - Nếu sát thương chí mạng đủ để giết chết → thay vì chết sẽ gọi hàm {@link #callApk13()} rồi hồi máu trở lại.
 *
 * Cơ chế phối hợp:
 * - Khi gọi {@link #callApk13()}:
 *   + Android 13 được respawn.
 *   + Android 15 hồi máu, chuyển sang non-PK và bật flag callApk13.
 *   + Android 14 tự hồi máu, chuyển sang non-PK và bật flag callApk13.
 * - Khi kết thúc câu thoại (doneChatS), Android 15 sẽ được đưa vào trạng thái PK.
 *
 * @author  
 *  - Code gốc: team dev
 *  - JavaDoc & chú thích: Lucifer
 */
public class Android14 extends Boss {

    /** Đánh dấu đã gọi Android 13 hay chưa. */
    public boolean callApk13;

    /**
     * Khởi tạo boss Android 14.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public Android14() throws Exception {
        super(BossID.ANDROID_14, BossesData.ANDROID_14);
    }

    /**
     * Phần thưởng khi tiêu diệt Android 14.
     *
     * @param plKill người chơi hạ gục boss
     */
    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{1142, 382, 383, 384, 1142};
        int itemId = itemRan[2]; // luôn chọn item 383
        if (Util.isTrue(15, 100)) { // 15% rơi đồ
            ItemMap it = new ItemMap(this.zone, itemId, 17,
                    this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Reset lại trạng thái cơ bản khi boss respawn.
     */
    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
    }

    /**
     * Hành vi chủ động của boss trong mỗi tick.
     * - Nếu chưa gọi Android 13 và đang ở trạng thái NON_PK thì chuyển sang PK.
     * - Gọi phương thức attack() để tấn công.
     */
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK && !this.callApk13) {
            this.changeToTypePK();
        }
        this.attack();
    }

    /**
     * Xử lý khi Android 14 nhận sát thương.
     * - Nếu sát thương chí mạng đủ để giết và chưa gọi Android 13 → gọi {@link #callApk13()} thay vì chết.
     *
     * @return lượng sát thương thực nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && damage >= this.nPoint.hp) {
            this.callApk13();
            return 0; // không chết ngay, chỉ trigger sự kiện
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    /**
     * Gọi Android 13 quay lại và đồng bộ với Android 15.
     * - Android 13 được respawn.
     * - Android 15 hồi phục HP và vào trạng thái non-PK.
     * - Android 14 cũng hồi máu và vào trạng thái non-PK.
     */
    public void callApk13() {
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_13) {
                boss.changeStatus(BossStatus.RESPAWN);
            } else if (boss.id == BossID.ANDROID_15) {
                boss.changeToTypeNonPK();
                ((Android15) boss).callApk13 = true;
                ((Android15) boss).recoverHP();
            }
        }
        this.changeToTypeNonPK();
        this.recoverHP();
        this.callApk13 = true;
    }

    /**
     * Hồi phục toàn bộ HP cho Android 14.
     */
    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    /**
     * Khi boss chat xong câu thoại script → Android 15 sẽ vào trạng thái PK.
     */
    @Override
    public void doneChatS() {
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_15) {
                boss.changeToTypePK();
                break;
            }
        }
    }

}
