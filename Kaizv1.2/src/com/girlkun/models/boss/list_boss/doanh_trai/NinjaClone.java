package com.girlkun.models.boss.list_boss.doanh_trai;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import static com.girlkun.models.boss.BossStatus.ACTIVE;
import static com.girlkun.models.boss.BossStatus.JOIN_MAP;
import static com.girlkun.models.boss.BossStatus.RESPAWN;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Boss Ninja Áo Tím (Doanh Trại).
 *
 * Đặc điểm:
 * - Là một boss "clone" (bản sao ninja).
 * - Xuất hiện trong map Doanh Trại.
 * - Tồn tại trong thời gian hữu hạn (timeLive).
 *
 * Cơ chế:
 * - Spawn với skill set đa dạng (Kame, Masenko, Antomic, Demon, Dragon, Galick).
 * - Khi hoạt động sẽ tự động bật chế độ PK.
 * - Có hành vi respawn → joinMap → active.
 * - Khi bị tấn công, có khả năng né đòn hoặc chống bằng khiên.
 * - Khi chết sẽ rơi item.
 * 
 * @author Lucifer
 */
public class NinjaClone extends Boss {

    /** Lần cập nhật hành vi gần nhất */
    private long lastUpdate = System.currentTimeMillis();

    /** Thời điểm boss join vào map */
    private long timeJoinMap;

    /** Người chơi mục tiêu */
    protected Player playerAtt;

    /** Thời gian tồn tại (sau đó sẽ biến mất) */
    private int timeLive = 200000000;

    /**
     * Khởi tạo NinjaClone.
     *
     * @param zone bản đồ xuất hiện
     * @param dame sát thương cơ bản
     * @param hp máu cơ bản
     * @param id id boss
     * @throws Exception nếu không load được dữ liệu
     */
    public NinjaClone(Zone zone, int dame, int hp, int id) throws Exception {
        super(id, new BossData(
                "Ninja Áo Tím", // Tên boss
                ConstPlayer.TRAI_DAT, // Giới tính (Trái Đất)
                new short[]{123, 124, 125, -1, -1, -1}, // Trang phục {head, body, leg, bag, aura, eff}
                (1000), // Damage
                new int[]{(10000)}, // HP
                new int[]{49}, // Map spawn
                new int[][]{ // Skill set
                        {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                        {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9},
                        {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                        {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16},
                        {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                        {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                },
                new String[]{}, // text chat 1
                new String[]{"|-1|Nhóc con"}, // text chat 2
                new String[]{}, // text chat 3
                60 // tỉ lệ né đòn
        ));
        this.zone = zone;
    }

    /**
     * Xử lý khi boss chết → rơi item.
     *
     * @param plKill người chơi kết liễu
     */
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }

    /**
     * Hành vi hoạt động của NinjaClone:
     * - Respawn → Join map → Active.
     * - Khi Active, sẽ tấn công player.
     * - Sau một thời gian nhất định sẽ tự rời map.
     */
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn();
                    this.changeStatus(BossStatus.JOIN_MAP);
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE);
                        timeJoinMap = System.currentTimeMillis();
                        this.typePk = 3;
                        PlayerService.gI().changeAndSendTypePK(playerAtt, ConstPlayer.PK_PVP);
                        this.changeStatus(BossStatus.ACTIVE);
                    }
                    break;
                case ACTIVE:
                    // Nếu đang charge skill đặc biệt thì không tấn công
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack();
                    }
                    break;
            }
            // Kiểm tra thời gian tồn tại
            if (Util.canDoWithTime(lastUpdate, 1000)) {
                lastUpdate = System.currentTimeMillis();
                if (timeLive > 0) {
                    timeLive--;
                } else {
                    super.leaveMap();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý khi bị tấn công.
     *
     * @param plAtt người chơi tấn công
     * @param damage sát thương
     * @param piercing có bỏ qua thủ không
     * @param isMobAttack có phải mob tấn công không
     * @return lượng sát thương nhận
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            // Né đòn
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 1000;
            }
            // Tính toán sát thương với thủ
            damage = this.nPoint.subDameInjureWithDeff(1000);
            // Nếu có khiên
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1000;
            }
            // Trừ HP
            this.nPoint.subHP(damage);
            // Nếu chết
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return 1000;
        } else {
            return 0;
        }
    }
}
