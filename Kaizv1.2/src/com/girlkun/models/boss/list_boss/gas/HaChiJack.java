package com.girlkun.models.boss.list_boss.gas;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Đại diện cho Boss HaChiJack trong sự kiện Khí Gas Hủy Diệt.
 * Boss này có hệ thống skill đa dạng, rơi đồ và thông báo cho bang hội khi bị hạ gục.
 *
 * @author Lucifer
 */
public class HaChiJack extends Boss {

    /** Cấp độ của Boss */
    private int levell;

    /** Danh sách kỹ năng Demon đầy đủ các cấp */
    private static final int[][] FULL_DEMON = new int[][]{
        {Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3},
        {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}
    };

    /** Thời gian lần cuối thực hiện chiêu Hấp Thu */
    private long lastTimeHapThu;

    /** Thời gian hồi chiêu Hấp Thu */
    private int timeHapThu;

    /** Lưu thời gian cập nhật gần nhất */
    private long lastUpdate = System.currentTimeMillis();

    /** Thời điểm Boss tham gia bản đồ */
    private long timeJoinMap;

    /** Biến khởi tạo trạng thái đặc biệt */
    private int initSuper = 0;

    /** Người chơi đã spawn ra Boss */
    private Player plSpawn;

    /** Người chơi hiện tại mà Boss đang tấn công */
    protected Player playerAtt;

    /** Thời gian tồn tại tối đa của Boss */
    private int timeLive = 200000000;

    /**
     * Khởi tạo Boss HaChiJack.
     *
     * @param pl    Người chơi tạo ra Boss (thường là người mở sự kiện)
     * @param level Cấp độ Boss
     * @param dame  Sát thương cơ bản
     * @param hp    Máu cơ bản
     * @throws Exception Nếu dữ liệu không hợp lệ
     */
    public HaChiJack(Player pl, int level, int dame, int hp) throws Exception {
        super(BossID.HACHIYACK, new BossData(
                "HaChiYack", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{639, 640, 641, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((30000 * level)), //dame
                new int[]{(((int)200000000L * (int)level))}, //hp
                new int[]{148}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        this.zone = pl.zone;
        this.levell = level;
        plSpawn = pl;
    }

    /**
     * Xử lý phần thưởng khi Boss bị tiêu diệt.
     *
     * @param plKill Người chơi hạ gục Boss
     */
    @Override
    public void reward(Player plKill) {
        int paramhp = 0;
        int parammp = 0;
        int paramsd = 0;
        int paramgiap = 0;
        for (int i = 0; i <= 4; i++) {
            ItemMap it = new ItemMap(zone, 729, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
            it.x = Util.nextInt(100, 300);
            if (levell < 10) {
                paramhp = Util.nextInt(3, 5);
                parammp = Util.nextInt(3, 5);
                paramsd = Util.nextInt(3, 5);
                paramgiap = Util.nextInt(2);
            } else if (levell < 20) {
                paramhp = Util.nextInt(5, 7);
                parammp = Util.nextInt(5, 7);
                paramsd = Util.nextInt(5, 7);
                paramgiap = Util.nextInt(3, 5);
            } else if (levell < 40) {
                paramhp = Util.nextInt(7, 10);
                parammp = Util.nextInt(7, 10);
                paramsd = Util.nextInt(7, 10);
                paramgiap = Util.nextInt(6, 8);
            } else if (levell < 60) {
                paramhp = Util.nextInt(10, 15);
                parammp = Util.nextInt(10, 15);
                paramsd = Util.nextInt(10, 15);
                paramgiap = Util.nextInt(9, 12);
            } else if (levell < 80) {
                paramhp = Util.nextInt(15, 20);
                paramgiap = Util.nextInt(13, 18);
                parammp = Util.nextInt(15, 20);
                paramsd = Util.nextInt(15, 20);
            } else if (levell < 99) {
                paramhp = Util.nextInt(24, 28);
                paramgiap = Util.nextInt(18, 24);
                parammp = Util.nextInt(24, 28);
                paramsd = Util.nextInt(24, 28);
            } else {
                paramhp = Util.nextInt(31, 35);
                parammp = Util.nextInt(31, 35);
                paramsd = Util.nextInt(31, 35);
                paramgiap = Util.nextInt(24, 28);
            }
            it.options.add(new Item.ItemOption(77, paramhp));
            it.options.add(new Item.ItemOption(103, parammp));
            it.options.add(new Item.ItemOption(50, paramsd));
            it.options.add(new Item.ItemOption(93, 7));
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    /**
     * Kích hoạt hành vi Boss (AI, di chuyển, tấn công, ...).
     */
    @Override
    public void active() {
        super.active();
    }

    /**
     * Xử lý sát thương khi Boss bị tấn công.
     *
     * @param plAtt        Người chơi tấn công
     * @param damage       Sát thương gây ra
     * @param piercing     Có bỏ qua giáp hay không
     * @param isMobAttack  Có phải từ quái thường không
     * @return Sát thương thực tế Boss nhận vào
     */
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(50, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
                if (plAtt.clan != null) {
                    Service.gI().sendThongBao(plAtt, "Đã hoàn thành Khí Gas Hủy Diệt cấp độ " + levell + " với tổng thời gian :" + (System.currentTimeMillis() - plAtt.clan.timeOpenKhiGas) + "s");
                    if (plAtt.clan.levelKhiGas < levell) {
                        plAtt.clan.levelKhiGas = this.levell;
                        plAtt.clan.TimeDoneKhiGas = System.currentTimeMillis() - plAtt.clan.timeOpenKhiGas;
                    } else if (plAtt.clan.levelKhiGas == levell && plAtt.clan.TimeDoneKhiGas > System.currentTimeMillis() - plAtt.clan.timeOpenKhiGas) {
                        plAtt.clan.TimeDoneKhiGas = System.currentTimeMillis() - plAtt.clan.timeOpenKhiGas;
                    }
                    plAtt.clan.updateTimeKhiGas();
                }
            }
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Xử lý khi Boss rời bản đồ (xóa khỏi BossManager).
     */
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
