package com.girlkun.models.boss.dhvt;

import com.girlkun.consts.ConstRatio;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import static com.girlkun.models.boss.BossStatus.ACTIVE;
import static com.girlkun.models.boss.BossStatus.JOIN_MAP;
import static com.girlkun.models.boss.BossStatus.RESPAWN;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.PlayerService;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

/**
 * Lớp trừu tượng định nghĩa hành vi của các boss trong sự kiện Đại Hội Võ Thuật (DHVT)
 *  * @author Lucifer
 */
public abstract class BossDHVT extends Boss {

    /** Người chơi đang được boss tấn công */
    protected Player playerAtt;
    /** Thời gian boss tham gia vào bản đồ */
    protected long timeJoinMap;

    /** Constructor khởi tạo boss với ID và dữ liệu boss */
    public BossDHVT(byte id, BossData data) throws Exception {
        super(id, data);
        this.bossStatus = BossStatus.RESPAWN;
    }

    /** Kiểm tra người chơi bị tiêu diệt (chưa được triển khai) */
    @Override
    public void checkPlayerDie(Player pl) {
    }

    /** Di chuyển boss đến tọa độ x, y */
    protected void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            /** Tính hướng di chuyển (trái hoặc phải) */
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            /** Khoảng cách di chuyển ngẫu nhiên từ 50 đến 100 */
            byte move = (byte) Util.nextInt(50, 100);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
        } else {
            /** Dịch chuyển tức thời đến tọa độ x, y */
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    /** Tấn công người chơi */
    @Override
    public void attack() {
        try {
            /** Kiểm tra xem đã đủ thời gian (10 giây) kể từ khi boss tham gia bản đồ */
            if (Util.canDoWithTime(timeJoinMap, 10000)) {
                /** Kiểm tra người chơi mục tiêu còn tồn tại và ở cùng khu vực */
                if (playerAtt.location != null && playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                    if (this.isDie()) {
                        return; /** Không tấn công nếu boss đã chết */
                    }
                    /** Chọn ngẫu nhiên một kỹ năng để sử dụng */
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    /** Kiểm tra khoảng cách có thể tấn công bằng kỹ năng đã chọn */
                    if (Util.getDistance(this, playerAtt) <= this.getRangeCanAttackWithSkillSelect()) {
                        /** 15% xác suất sử dụng kỹ năng đặc biệt (Chuong) */
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            /** Di chuyển đến gần người chơi với tọa độ ngẫu nhiên */
                            goToXY(playerAtt.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)), 
                                   Util.nextInt(10) % 2 == 0 ? playerAtt.location.y : playerAtt.location.y - Util.nextInt(0, 50), false);
                        }
                        /** Sử dụng kỹ năng tấn công người chơi */
                        SkillService.gI().useSkill(this, playerAtt, null, null);
                        checkPlayerDie(playerAtt);
                    } else {
                        /** Di chuyển đến gần người chơi nếu khoảng cách quá xa */
                        goToPlayer(playerAtt, false);
                    }
                } else {
                    /** Rời bản đồ nếu người chơi mục tiêu không còn ở khu vực */
                    this.leaveMap();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Di chuyển đến vị trí của người chơi */
    protected void goToPlayer(Player pl, boolean isTeleport) {
        goToXY(pl.location.x, pl.location.y, isTeleport);
    }

    /** Tham gia bản đồ */
    @Override
    public void joinMap() {
        if (playerAtt.zone != null) {
            this.zone = playerAtt.zone; /** Đặt khu vực của boss là khu vực của người chơi */
            /** Dịch chuyển boss đến tọa độ cố định (435, 264) */
            ChangeMapService.gI().changeMap(this, this.zone, 435, 264);
        }
    }

    /** Phục hồi mana (mp) của boss về mức tối đa */
    protected void immortalMp() {
        this.nPoint.mp = this.nPoint.mpg;
    }

    /** Cập nhật trạng thái của boss */
    @Override
    public void update() {
        try {
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn(); /** Hồi sinh boss */
                    this.changeStatus(BossStatus.JOIN_MAP); /** Chuyển sang trạng thái tham gia bản đồ */
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE); /** Chuyển sang trạng thái hoạt động */
                        timeJoinMap = System.currentTimeMillis(); /** Ghi lại thời gian tham gia */
                        this.immortalMp(); /** Phục hồi mana */
                        this.typePk = 3; /** Đặt trạng thái PK (chế độ chiến đấu) */
                    }
                    break;
                case ACTIVE:
                    /** Nếu boss đang chuẩn bị kỹ năng đặc biệt (TuSat, Laze, QCKK), tạm dừng */
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack(); /** Thực hiện tấn công */
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi cập nhật boss DHVT");
        }
    }

    /** Thông báo khi người chơi tiêu diệt boss (chưa triển khai) */
    protected void notifyPlayeKill(Player player) {
    }

    /** Xử lý khi boss chết */
    @Override
    public void die(Player plKill) {
        /** Phần thưởng cho người chơi và thông báo (đã bị comment trong bản gốc) */
        /*
        if (plKill != null) {
            reward(plKill);
            ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name + ", ghê chưa ghê chưa..");
        }
        */
        plKill.nPoint.hp = plKill.nPoint.hpMax; /** Phục hồi HP của người chơi giết boss */
        this.changeStatus(BossStatus.DIE); /** Chuyển trạng thái boss sang đã chết */
    }

    /** Rời khỏi bản đồ */
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this); /** Xóa boss khỏi danh sách quản lý */
    }
}