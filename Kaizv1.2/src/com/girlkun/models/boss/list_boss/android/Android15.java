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
 * Boss Android 15.
 *
 * Đặc điểm:
 * - Khi bị giết lần đầu (nếu chưa từng gọi Android 13), Android 15 sẽ "kích hoạt" hành vi gọi
 *   {@link Android14#callApk13()} để hồi sinh Android 13 và phối hợp chiến đấu.
 *
 * Cơ chế phần thưởng:
 * - Xác suất 15% rơi item (hiện tại luôn chọn {@code itemRan[2]} là itemID = 383).
 * - Gọi {@link TaskService#checkDoneTaskKillBoss(Player, Boss)} để kiểm tra nhiệm vụ khi người chơi giết boss.
 *
 * Cơ chế chiến đấu:
 * - Khi bị tấn công:
 *   + Nếu sát thương chí mạng đủ để giết và chưa từng gọi Android 13 → trigger sự kiện {@link Android14#callApk13()} qua parentBoss.
 *   + Nếu đã gọi hoặc không thuộc trường hợp trên → nhận sát thương bình thường.
 * - Trong trạng thái active: chỉ gọi attack().
 *
 * Cơ chế phối hợp:
 * - Android 15 sẽ được hồi phục HP và chuyển sang Non-PK khi Android 14 gọi {@link Android14#callApk13()}.
 *
 * @author  
 *  - Code gốc: team dev
 *  - JavaDoc & chú thích: Lucifer
 */
public class Android15 extends Boss {

    /** Đánh dấu đã từng tham gia sự kiện gọi Android 13 hay chưa. */
    public boolean callApk13;

    /**
     * Khởi tạo boss Android 15.
     *
     * @throws Exception nếu có lỗi trong quá trình load dữ liệu boss
     */
    public Android15() throws Exception {
        super(BossID.ANDROID_15, BossesData.ANDROID_15);
    }

    /**
     * Phần thưởng khi tiêu diệt Android 15.
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
     * Hành vi chủ động trong mỗi tick của boss.
     * - Android 15 chỉ tấn công, không tự thay đổi trạng thái PK như Android 14.
     */
    @Override
    public void active() {
        this.attack();
    }

    /**
     * Xử lý khi Android 15 nhận sát thương.
     * - Nếu sát thương đủ để giết và chưa gọi Android 13 → gọi {@link Android14#callApk13()} thông qua parentBoss.
     * - Ngược lại → xử lý sát thương bình thường theo logic cha.
     *
     * @return lượng sát thương thực nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && damage >= this.nPoint.hp) {
            if (this.parentBoss != null) {
                ((Android14) this.parentBoss).callApk13();
            }
            return 0; // không chết ngay, chỉ trigger sự kiện
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    /**
     * Hồi phục toàn bộ HP cho Android 15.
     */
    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }
}
