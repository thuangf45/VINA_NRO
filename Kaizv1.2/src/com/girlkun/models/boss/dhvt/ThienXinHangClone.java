package com.girlkun.models.boss.dhvt;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstRatio;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.SkillService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho bản sao của boss ThienXinHang trong sự kiện Đại Hội Võ Thuật (DHVT)
 * @author Lucifer
 */
public class ThienXinHangClone extends BossDHVT {

    /** Thời gian tồn tại của bản sao (tính bằng giây) */
    private int timeLive;
    /** Thời gian lần cuối cập nhật trạng thái */
    private long lastUpdate = System.currentTimeMillis();

    /** Constructor khởi tạo bản sao ThienXinHang với ID và dữ liệu từ BossesData */
    public ThienXinHangClone(byte id, Player player) throws Exception {
        super(id, BossesData.THIEN_XIN_HANG_CLONE);
        /** Gán người chơi mục tiêu cho bản sao */
        this.playerAtt = player;
        /** Đặt thời gian tồn tại mặc định là 10 giây */
        timeLive = 10;
        /*
        this.bossStatus = BossStatus.JOIN_MAP;
        this.bossStatus = BossStatus.ACTIVE;
        this.typePk = 3;
        this.nPoint.khangTDHS = true;
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_PVP);
        MartialCongressService.gI().sendTypePK(playerAtt, this);
        */
        /** Đặt thời gian tham gia bản đồ (trì hoãn 10 giây) */
        this.timeJoinMap = System.currentTimeMillis() + 10000;
    }

    /** Tấn công người chơi */
    @Override
    public void attack() {
        try {
            /** Kiểm tra người chơi mục tiêu còn tồn tại và ở cùng khu vực */
            if (playerAtt.location != null && playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                if (this.isDie()) {
                    return; /** Không tấn công nếu bản sao đã chết */
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
        } catch (Exception ex) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }

    /** Cập nhật trạng thái của bản sao */
    @Override
    public void update() {
        try {
            /** Xóa hiệu ứng choáng (stun) khỏi bản sao */
            EffectSkillService.gI().removeStun(this);
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn(); /** Hồi sinh bản sao */
                    this.changeStatus(BossStatus.JOIN_MAP); /** Chuyển sang trạng thái tham gia bản đồ */
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE); /** Chuyển sang trạng thái hoạt động */
                        timeJoinMap = System.currentTimeMillis(); /** Ghi lại thời gian tham gia */
                        this.immortalMp(); /** Phục hồi mana */
                        this.typePk = 3; /** Đặt trạng thái PK (chế độ chiến đấu) */
                        /** Đặt trạng thái PK cho người chơi và bản sao (đã bị comment trong mã gốc) */
                        /*
                        MartialCongressService.gI().sendTypePK(playerAtt, this);
                        */
                        PlayerService.gI().changeAndSendTypePK(playerAtt, ConstPlayer.PK_PVP);
                        this.changeStatus(BossStatus.ACTIVE); /** Chuyển lại trạng thái hoạt động */
                    }
                    break;
                case ACTIVE:
                    /** Nếu bản sao đang chuẩn bị kỹ năng đặc biệt (TuSat, Laze, QCKK), tạm dừng */
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack(); /** Thực hiện tấn công */
                    }
                    break;
            }
            /** Cập nhật thời gian tồn tại mỗi giây */
            if (Util.canDoWithTime(lastUpdate, 1000)) {
                lastUpdate = System.currentTimeMillis();
                if (timeLive > 0) {
                    timeLive--; /** Giảm thời gian tồn tại */
                } else {
                    super.leaveMap(); /** Rời bản đồ khi hết thời gian tồn tại */
                }
            }
        } catch (Exception e) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }
}