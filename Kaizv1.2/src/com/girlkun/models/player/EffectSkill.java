package com.girlkun.models.player;

import com.girlkun.models.mob.Mob;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * Lớp quản lý các hiệu ứng kỹ năng của người chơi trong game, bao gồm các trạng thái như làm choáng, khiên năng lượng, 
 * biến khỉ, thôi miên, trói, và các hiệu ứng khác.
 * @author Lucifer
 */
public class EffectSkill {

    /** Người chơi sở hữu các hiệu ứng kỹ năng. */
    public Player player;

    // Thái dương hạ san
    /** Trạng thái làm choáng của người chơi. */
    public boolean isStun;

    /** Thời điểm bắt đầu hiệu ứng làm choáng. */
    public long lastTimeStartStun;

    /** Thời gian hiệu lực của hiệu ứng làm choáng. */
    public int timeStun;

    // Khiên năng lượng
    /** Trạng thái sử dụng khiên năng lượng. */
    public boolean isShielding;

    /** Thời điểm kích hoạt khiên năng lượng. */
    public long lastTimeShieldUp;

    /** Thời gian hiệu lực của khiên năng lượng. */
    public int timeShield;

    // Biến khỉ
    /** Trạng thái biến thành khỉ. */
    public boolean isMonkey;

    /** Cấp độ của trạng thái khỉ. */
    public byte levelMonkey;

    /** Thời điểm bắt đầu biến thành khỉ. */
    public long lastTimeUpMonkey;

    /** Thời gian hiệu lực của trạng thái khỉ. */
    public int timeMonkey;

    // Tái tạo năng lượng
    /** Trạng thái đang tái tạo năng lượng. */
    public boolean isCharging;

    /** Số lần tái tạo năng lượng. */
    public int countCharging;

    // Huýt sáo
    /** Tỷ lệ HP ảnh hưởng bởi kỹ năng huýt sáo. */
    public int tiLeHPHuytSao;

    /** Thời điểm cuối cùng sử dụng huýt sáo. */
    public long lastTimeHuytSao;

    // Thôi miên
    /** Trạng thái bị thôi miên. */
    public boolean isThoiMien;

    /** Thời điểm bắt đầu bị thôi miên. */
    public long lastTimeThoiMien;

    /** Thời gian hiệu lực của trạng thái thôi miên. */
    public int timeThoiMien;

    // Trói
    /** Trạng thái sử dụng kỹ năng trói. */
    public boolean useTroi;

    /** Trạng thái bị trói. */
    public boolean anTroi;

    /** Thời điểm bắt đầu sử dụng kỹ năng trói. */
    public long lastTimeTroi;

    /** Thời điểm bắt đầu bị trói. */
    public long lastTimeAnTroi;

    /** Thời gian hiệu lực của kỹ năng trói. */
    public int timeTroi;

    /** Thời gian hiệu lực của trạng thái bị trói. */
    public int timeAnTroi;

    /** Người chơi sử dụng kỹ năng trói. */
    public Player plTroi;

    /** Người chơi bị trói. */
    public Player plAnTroi;

    /** Quái bị trói. */
    public Mob mobAnTroi;

    // Dịch chuyển tức thời
    /** Trạng thái bị làm mù bởi dịch chuyển tức thời. */
    public boolean isBlindDCTT;

    /** Thời điểm bắt đầu hiệu ứng làm mù DCTT. */
    public long lastTimeBlindDCTT;

    /** Thời gian hiệu lực của hiệu ứng làm mù DCTT. */
    public int timeBlindDCTT;

    // Socola
    /** Trạng thái bị biến thành socola. */
    public boolean isSocola;

    /** Thời điểm bắt đầu hiệu ứng socola. */
    public long lastTimeSocola;

    /** Thời gian hiệu lực của hiệu ứng socola. */
    public int timeSocola;

    /** Số lần giảm HP khi ở trạng thái socola. */
    public int countPem1hp;

    // Biến thành cái bình
    /** Trạng thái bị biến thành cái bình chứa. */
    public boolean isCaiBinhChua;

    /** Thời điểm bắt đầu hiệu ứng biến thành cái bình. */
    public long lastTimeCaiBinhChua;

    /** Thời gian hiệu lực của hiệu ứng biến thành cái bình. */
    public int timeCaiBinhChua;

    /**
     * Khởi tạo đối tượng EffectSkill cho một người chơi.
     *
     * @param player Người chơi sở hữu các hiệu ứng kỹ năng.
     */
    public EffectSkill(Player player) {
        this.player = player;
    }

    /**
     * Xóa các hiệu ứng kỹ năng khi người chơi chết.
     */
    public void removeSkillEffectWhenDie() {
        if (isMonkey) {
            EffectSkillService.gI().monkeyDown(player);
        }
        if (isShielding) {
            EffectSkillService.gI().removeShield(player);
            ItemTimeService.gI().removeItemTime(player, 3784);
        }
        if (useTroi) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
        if (isStun) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
    }

    /**
     * Cập nhật trạng thái của các hiệu ứng kỹ năng, kiểm tra thời gian hiệu lực và xóa các hiệu ứng khi hết thời gian.
     */
    public void update() {
        if (isMonkey && (Util.canDoWithTime(lastTimeUpMonkey, timeMonkey))) {
            EffectSkillService.gI().monkeyDown(player);
        }
        if (isShielding && (Util.canDoWithTime(lastTimeShieldUp, timeShield))) {
            EffectSkillService.gI().removeShield(player);
        }
        if (useTroi && Util.canDoWithTime(lastTimeTroi, timeTroi)
                || plAnTroi != null && plAnTroi.isDie()
                || useTroi && isHaveEffectSkill()) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
        if (anTroi && (Util.canDoWithTime(lastTimeAnTroi, timeAnTroi) || player.isDie())) {
            EffectSkillService.gI().removeAnTroi(this.player);
        }
        if (isStun && Util.canDoWithTime(lastTimeStartStun, timeStun)) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien && (Util.canDoWithTime(lastTimeThoiMien, timeThoiMien))) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT && (Util.canDoWithTime(lastTimeBlindDCTT, timeBlindDCTT))) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
        if (isSocola && (Util.canDoWithTime(lastTimeSocola, timeSocola))) {
            EffectSkillService.gI().removeSocola(this.player);
        }
        if (tiLeHPHuytSao != 0 && Util.canDoWithTime(lastTimeHuytSao, 30000)) {
            EffectSkillService.gI().removeHuytSao(this.player);
        }
        if (isCaiBinhChua && (Util.canDoWithTime(this.lastTimeCaiBinhChua, this.timeCaiBinhChua) || this.player.isDie())) {
            isCaiBinhChua = false;
            Service.getInstance().Send_Caitrang(this.player);
        }
    }

    /**
     * Kiểm tra xem người chơi có đang chịu một trong các hiệu ứng kỹ năng (làm choáng, làm mù, bị trói, thôi miên) hay không.
     *
     * @return true nếu người chơi đang chịu một hiệu ứng kỹ năng, false nếu không.
     */
    public boolean isHaveEffectSkill() {
        return isStun || isBlindDCTT || anTroi || isThoiMien;
    }

    /**
     * Giải phóng tài nguyên của đối tượng EffectSkill.
     */
    public void dispose() {
        this.player = null;
    }
}