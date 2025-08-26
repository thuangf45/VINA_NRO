package com.girlkun.models.mob;

import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import com.girlkun.network.io.Message;

/**
 * Đại diện cho một quái vật được triệu hồi bởi người chơi, sử dụng kỹ năng cụ
 * thể.
 *
 * @author Lucifer
 */
public final class MobMe extends Mob {

    /**
     * Người chơi triệu hồi quái vật này.
     */
    private Player player;

    /**
     * Thời điểm quái vật được triệu hồi.
     */
    private final long lastTimeSpawn;

    /**
     * Thời gian tồn tại của quái vật (tính bằng mili giây).
     */
    private final int timeSurvive;

    /**
     * Khởi tạo một MobMe cho người chơi được chỉ định.
     *
     * @param player người chơi triệu hồi quái vật
     * @throws IllegalArgumentException nếu player là null
     */
    public MobMe(Player player) {
        super();
        if (player == null) {
            throw new IllegalArgumentException("Người chơi không được null");
        }
        this.player = player;
        this.id = (int) player.id;
        int level = player.playerSkill.getSkillbyId(12).point;
        this.tempId = SkillUtil.getTempMobMe(level);
        this.point.maxHp = SkillUtil.getHPMobMe(player.nPoint.hpMax, level);
        this.point.dame = SkillUtil.getHPMobMe(player.nPoint.getDameAttack(false), level);
        if (this.player.setClothes.pikkoroDaimao == 5) {
            this.point.dame *= 2; // Nhân đôi sát thương nếu người chơi mặc bộ Pikkoro Daimao cấp 5
        }
        this.point.hp = this.point.maxHp;
        this.zone = player.zone;
        this.lastTimeSpawn = System.currentTimeMillis();
        this.timeSurvive = SkillUtil.getTimeSurviveMobMe(level);
        spawn();
    }

    /**
     * Cập nhật trạng thái của MobMe, xóa nó nếu hết thời gian tồn tại và người
     * chơi không mặc bộ Pikkoro Daimao cấp 5.
     */
    @Override
    public void update() {
        if (player == null || zone == null) {
            dispose();
            return;
        }
        // MobMe chỉ biến mất nếu hết thời gian tồn tại và người chơi không mặc bộ Pikkoro Daimao cấp 5
        if (Util.canDoWithTime(lastTimeSpawn, timeSurvive) && player.setClothes.pikkoroDaimao != 5) {
            mobMeDie();
            dispose();
        }
    }

    /**
     * Thực hiện hành động tấn công người chơi hoặc quái vật khác.
     *
     * @param pl người chơi bị tấn công, hoặc null nếu không tấn công người chơi
     * @param mob quái vật bị tấn công, hoặc null nếu không tấn công quái vật
     */
    public void attack(Player pl, Mob mob) {
        if (zone == null || player == null) {
            Logger.logException(MobMe.class, new NullPointerException("zone hoặc player là null"),
                    "Không thể thực hiện tấn công cho MobMe ID: " + id);
            return;
        }
        Message msg = null;
        try {
            if (pl != null && !pl.isDie() && !pl.effectSkin.isVoHinh) {
                if (pl.nPoint.hp > this.point.dame && pl.nPoint.hp > pl.nPoint.hpMax * 0.05) {
                    int dameHit = pl.injured(null, this.point.dame, true, true);
                    msg = new Message(-95);
                    msg.writer().writeByte(2); // Loại: tấn công người chơi
                    msg.writer().writeInt(this.id);
                    msg.writer().writeInt((int) pl.id);
                    msg.writer().writeInt(dameHit);
                    msg.writer().writeInt(pl.nPoint.hp);
                    Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                    msg.cleanup();
                    msg = null;
                }
            }

            if (mob != null && !mob.isDie()) {
                if (mob.point.gethp() > this.point.dame) {
                    long tnsm = mob.getTiemNangForPlayer(player, this.point.dame);
                    msg = new Message(-95);
                    msg.writer().writeByte(3); // Loại: tấn công quái vật
                    msg.writer().writeInt(id);
                    msg.writer().writeInt(mob.id);
                    mob.point.sethp(mob.point.gethp() - this.point.dame);
                    msg.writer().writeInt(mob.point.gethp());
                    msg.writer().writeInt(this.point.dame);
                    Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                    msg.cleanup();
                    if (player.nPoint.power + tnsm < player.nPoint.getPowerLimit()) {
                        Service.getInstance().addSMTN(player, (byte) 2, tnsm, true);
                    } else {
                        Service.getInstance().sendThongBao(player, "Sức mạnh của bạn đã đạt giới hạn, không thể nhận thêm tiềm năng!");
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MobMe.class, e, "Lỗi khi xử lý tấn công cho MobMe ID: " + id);
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Triệu hồi MobMe vào khu vực hiện tại của người chơi và thông báo cho tất
     * cả người chơi trong bản đồ.
     */
    public void spawn() {
        if (zone == null || player == null) {
            Logger.logException(MobMe.class, new NullPointerException("zone hoặc player là null"),
                    "Không thể triệu hồi MobMe ID: " + id);
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-95);
            msg.writer().writeByte(0); // Loại: triệu hồi
            msg.writer().writeInt(id);
            msg.writer().writeShort(tempId);
            msg.writer().writeInt(point.hp);
            Service.getInstance().sendMessAllPlayerInMap(zone, msg);
        } catch (Exception e) {
            Logger.logException(MobMe.class, e, "Lỗi khi triệu hồi MobMe ID: " + id);
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Di chuyển MobMe đến một khu vực mới, xóa nó khỏi khu vực hiện tại nếu
     * cần.
     *
     * @param zone khu vực mới để di chuyển đến
     */
    public void goToMap(Zone zone) {
        if (zone != null) {
            removeMobInMap();
            this.zone = zone;
        }
    }

    /**
     * Xóa MobMe khỏi bản đồ hiện tại và thông báo cho tất cả người chơi trong
     * bản đồ.
     */
    private void removeMobInMap() {
        if (zone == null || player == null) {
            Logger.logException(MobMe.class, new NullPointerException("zone hoặc player là null"),
                    "Không thể xóa MobMe ID: " + id + " khỏi bản đồ");
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-95);
            msg.writer().writeByte(7); // Loại: xóa khỏi bản đồ
            msg.writer().writeInt(id);
            Service.getInstance().sendMessAllPlayerInMap(zone, msg);
        } catch (Exception e) {
            Logger.logException(MobMe.class, e, "Lỗi khi xóa MobMe ID: " + id + " khỏi bản đồ");
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Đánh dấu MobMe đã chết và thông báo cho tất cả người chơi trong bản đồ.
     */
    public void mobMeDie() {
        if (zone == null || player == null) {
            Logger.logException(MobMe.class, new NullPointerException("zone hoặc player là null"),
                    "Không thể xử lý cái chết của MobMe ID: " + id);
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-95);
            msg.writer().writeByte(6); // Loại: MobMe chết
            msg.writer().writeInt(id);
            Service.getInstance().sendMessAllPlayerInMap(zone, msg);
        } catch (Exception e) {
            Logger.logException(MobMe.class, e, "Lỗi khi xử lý cái chết của MobMe ID: " + id);
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Dọn dẹp MobMe, xóa các tham chiếu để tránh rò rỉ bộ nhớ.
     */
    public void dispose() {
        if (player != null) {
            player.mobMe = null;
        }
        this.player = null;
    }
}
