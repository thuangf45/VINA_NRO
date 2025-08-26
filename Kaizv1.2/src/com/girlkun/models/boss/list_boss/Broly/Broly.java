package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.PetService;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * Lớp đại diện cho boss Broly trong game.
 * Xử lý hành vi, cơ chế chiến đấu và phần thưởng khi bị hạ gục.
 * 
 * @author Lucifer
 */
public class Broly extends Boss {

    /**
     * Trạng thái Super của Broly (chưa sử dụng).
     */
    private boolean supper;

    /**
     * Constructor khởi tạo boss Broly với ID và dữ liệu từ BossesData.
     * 
     * @throws Exception Nếu có lỗi trong quá trình khởi tạo
     */
    public Broly() throws Exception {
        super(BossID.BROLY, BossesData.BROLY_1);
    }

    /**
     * Xử lý phần thưởng khi người chơi tiêu diệt Broly.
     * @param plKill Người chơi đã tiêu diệt boss
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(5, 10)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 568, 1, this.location.x, this.location.y, plKill.id));
            return;
        }
    }

    /**
     * Kích hoạt hành vi của Broly, bao gồm tự động rời bản đồ sau 15 phút.
     */
    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    /**
     * Thêm Broly vào bản đồ và ghi lại thời gian bắt đầu.
     */
    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    /**
     * Thời gian bắt đầu khi Broly tham gia bản đồ.
     */
    private long st;

    /**
     * Xử lý sát thương mà Broly nhận từ người chơi hoặc quái.
     * Giảm sát thương theo các cơ chế phòng thủ và hiệu ứng kỹ năng.
     * 
     * @param plAtt Người chơi tấn công boss
     * @param damage Lượng sát thương gây ra
     * @param piercing True nếu sát thương bỏ qua phòng thủ
     * @param isMobAttack True nếu sát thương đến từ quái
     * @return Lượng sát thương thực tế được áp dụng
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 4);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 4;
            }
            damage = damage * 4 / 5;
            this.nPoint.subHP(damage);
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
