package com.girlkun.services;

import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.EffectSkill;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.utils.SkillUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp EffectSkillService quản lý các hiệu ứng kỹ năng trong game, bao gồm các trạng thái như trói, thôi miên, choáng, biến hình, v.v.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class EffectSkillService {

    // Các hằng số đại diện cho trạng thái bật/tắt hiệu ứng
    /** Hằng số biểu thị bật hiệu ứng */
    public static final byte TURN_ON_EFFECT = 1;
    /** Hằng số biểu thị tắt hiệu ứng */
    public static final byte TURN_OFF_EFFECT = 0;
    /** Hằng số biểu thị tắt tất cả hiệu ứng */
    public static final byte TURN_OFF_ALL_EFFECT = 2;

    // Các hằng số đại diện cho loại hiệu ứng
    /** Hằng số biểu thị hiệu ứng trói */
    public static final byte HOLD_EFFECT = 32;
    /** Hằng số biểu thị hiệu ứng khiên năng lượng */
    public static final byte SHIELD_EFFECT = 33;
    /** Hằng số biểu thị hiệu ứng huýt sáo */
    public static final byte HUYT_SAO_EFFECT = 39;
    /** Hằng số biểu thị hiệu ứng choáng (thái dương hạ san) */
    public static final byte BLIND_EFFECT = 40;
    /** Hằng số biểu thị hiệu ứng thôi miên */
    public static final byte SLEEP_EFFECT = 41;
    /** Hằng số biểu thị hiệu ứng hóa đá */
    public static final byte STONE_EFFECT = 42;
    /** Hằng số biểu thị hiệu ứng hóa băng */
    public static final int ICE_EFFECT = 202;

    /** Thể hiện duy nhất của lớp EffectSkillService (singleton pattern) */
    private static EffectSkillService i;

    /**
     * Khởi tạo một đối tượng EffectSkillService.
     * Constructor được đặt ở chế độ private để đảm bảo tính singleton.
     */
    private EffectSkillService() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp EffectSkillService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp EffectSkillService.
     */
    public static EffectSkillService gI() {
        if (i == null) {
            i = new EffectSkillService();
        }
        return i;
    }

    /**
     * Gửi hiệu ứng khi người chơi sử dụng kỹ năng.
     * 
     * @param player Người chơi sử dụng kỹ năng.
     * @param skillId ID của kỹ năng được sử dụng.
     */
    public void sendEffectUseSkill(Player player, byte skillId) {
        Skill skill = SkillUtil.getSkillbyId(player, skillId);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(8);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng kỹ năng từ một người chơi lên một người chơi khác.
     * 
     * @param plUseSkill Người chơi sử dụng kỹ năng.
     * @param plTarget Người chơi mục tiêu chịu hiệu ứng.
     * @param toggle Trạng thái hiệu ứng (bật/tắt).
     * @param effect Loại hiệu ứng được áp dụng.
     */
    public void sendEffectPlayer(Player plUseSkill, Player plTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle);
            msg.writer().writeByte(0);
            if (toggle == TURN_OFF_ALL_EFFECT) {
                msg.writer().writeInt((int) plTarget.id);
            } else {
                msg.writer().writeByte(effect);
                msg.writer().writeInt((int) plTarget.id);
                msg.writer().writeInt((int) plUseSkill.id);
            }
            Service.gI().sendMessAllPlayerInMap(plUseSkill, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng kỹ năng từ một người chơi lên một quái vật.
     * 
     * @param plUseSkill Người chơi sử dụng kỹ năng.
     * @param mobTarget Quái vật mục tiêu chịu hiệu ứng.
     * @param toggle Trạng thái hiệu ứng (bật/tắt).
     * @param effect Loại hiệu ứng được áp dụng.
     */
    public void sendEffectMob(Player plUseSkill, Mob mobTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle);
            msg.writer().writeByte(1);
            msg.writer().writeByte(effect);
            msg.writer().writeByte(mobTarget.id);
            msg.writer().writeInt((int) plUseSkill.id);
            Service.gI().sendMessAllPlayerInMap(mobTarget.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Hủy trạng thái sử dụng kỹ năng trói của người chơi.
     * 
     * @param player Người chơi đang sử dụng kỹ năng trói.
     */
    public void removeUseTroi(Player player) {
        if (player.effectSkill.mobAnTroi != null) {
            player.effectSkill.mobAnTroi.effectSkill.removeAnTroi();
        }
        if (player.effectSkill.plAnTroi != null) {
            removeAnTroi(player.effectSkill.plAnTroi);
        }
        player.effectSkill.useTroi = false;
        player.effectSkill.mobAnTroi = null;
        player.effectSkill.plAnTroi = null;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
    }

    /**
     * Hủy trạng thái bị trói của người chơi.
     * 
     * @param player Người chơi đang bị trói.
     */
    public void removeAnTroi(Player player) {
        if (player != null && player.effectSkill != null) {
            player.effectSkill.anTroi = false;
            player.effectSkill.plTroi = null;
            sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
        }
    }

    /**
     * Thiết lập trạng thái bị trói cho người chơi.
     * 
     * @param player Người chơi bị trói.
     * @param plTroi Người chơi sử dụng kỹ năng trói.
     * @param lastTimeAnTroi Thời gian bắt đầu bị trói.
     * @param timeAnTroi Thời gian hiệu ứng trói kéo dài.
     */
    public void setAnTroi(Player player, Player plTroi, long lastTimeAnTroi, int timeAnTroi) {
        player.effectSkill.anTroi = true;
        player.effectSkill.lastTimeAnTroi = lastTimeAnTroi;
        player.effectSkill.timeAnTroi = timeAnTroi;
        player.effectSkill.plTroi = plTroi;
    }

    /**
     * Thiết lập trạng thái sử dụng kỹ năng trói cho người chơi.
     * 
     * @param player Người chơi sử dụng kỹ năng trói.
     * @param lastTimeTroi Thời gian bắt đầu sử dụng kỹ năng.
     * @param timeTroi Thời gian hiệu ứng trói kéo dài.
     */
    public void setUseTroi(Player player, long lastTimeTroi, int timeTroi) {
        player.effectSkill.useTroi = true;
        player.effectSkill.lastTimeTroi = lastTimeTroi;
        player.effectSkill.timeTroi = timeTroi;
    }

    /**
     * Thiết lập trạng thái bị thôi miên cho người chơi.
     * 
     * @param player Người chơi bị thôi miên.
     * @param lastTimeThoiMien Thời gian bắt đầu bị thôi miên.
     * @param timeThoiMien Thời gian hiệu ứng thôi miên kéo dài.
     */
    public void setThoiMien(Player player, long lastTimeThoiMien, int timeThoiMien) {
        player.effectSkill.isThoiMien = true;
        player.effectSkill.lastTimeThoiMien = lastTimeThoiMien;
        player.effectSkill.timeThoiMien = timeThoiMien;
    }

    /**
     * Hủy trạng thái thôi miên của người chơi.
     * 
     * @param player Người chơi đang bị thôi miên.
     */
    public void removeThoiMien(Player player) {
        player.effectSkill.isThoiMien = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SLEEP_EFFECT);
    }

    /**
     * Bắt đầu trạng thái choáng (thái dương hạ san) cho người chơi.
     * 
     * @param player Người chơi bị choáng.
     * @param lastTimeStartBlind Thời gian bắt đầu bị choáng.
     * @param timeBlind Thời gian hiệu ứng choáng kéo dài.
     */
    public void startStun(Player player, long lastTimeStartBlind, int timeBlind) {
        player.effectSkill.lastTimeStartStun = lastTimeStartBlind;
        player.effectSkill.timeStun = timeBlind;
        player.effectSkill.isStun = true;
        sendEffectPlayer(player, player, TURN_ON_EFFECT, BLIND_EFFECT);
    }

    /**
     * Hủy trạng thái choáng của người chơi.
     * 
     * @param player Người chơi đang bị choáng.
     */
    public void removeStun(Player player) {
        player.effectSkill.isStun = false;
        Service.gI().chat(player, "Đau mắt quá !");
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }

    
        //**************************************************************************
    //Cải trang Drabura Frost
//    public void SetHoaBang(Player player, long lastTimeHoaBang, int timeHoaBang){
//        player.effectSkill.lastTimeHoaBang = lastTimeHoaBang;
//        player.effectSkill.timeBang = timeHoaBang;
//        player.effectSkill.isBang = true;
//        sendEffectPlayer(player, player, TURN_ON_EFFECT, (byte) ICE_EFFECT);
//        
//    }
//    public void removeBang(Player player){
//        player.effectSkill.isBang = false;
//        Service.gI().Send_Caitrang(player);
//        sendEffectPlayer(player, player, TURN_ON_EFFECT, (byte) ICE_EFFECT);
//    }
//    //**************************************************************************
//    //Cải trang Drabura Hóa Đá
//    public void SetHoaDa(Player player, long lastTimeHoaDa, int timeHoaDa){
//        player.effectSkill.lastTimeHoaDa = lastTimeHoaDa;
//        player.effectSkill.timeDa = timeHoaDa;
//        player.effectSkill.isDa = true;
//        
//    }
//    public void removeDa(Player player){
//        player.effectSkill.isDa = false;
//        Service.gI().Send_Caitrang(player);
//    }
    //**************************************************************************
    //Cải trang Thỏ Đại Ca
//    public void SetHoaCarot(Player player, long lastTimeHoaCarot, int timeHoaCarot){
//        player.effectSkill.lastTimeHoaCarot = lastTimeHoaCarot;
//        player.effectSkill.timeCarot = timeHoaCarot;
//        player.effectSkill.isCarot = true;
//        
//    }
//    public void removeCarot(Player player){
//        player.effectSkill.isCarot = false;
//        Service.gI().Send_Caitrang(player);
//    }
    
    /**
     * Thiết lập trạng thái biến thành socola cho người chơi.
     * 
     * @param player Người chơi bị biến thành socola.
     * @param lastTimeSocola Thời gian bắt đầu hiệu ứng.
     * @param timeSocola Thời gian hiệu ứng kéo dài.
     */
    public void setSocola(Player player, long lastTimeSocola, int timeSocola) {
        player.effectSkill.lastTimeSocola = lastTimeSocola;
        player.effectSkill.timeSocola = timeSocola;
        player.effectSkill.isSocola = true;
        player.effectSkill.countPem1hp = 0;
    }

    /**
     * Hủy trạng thái socola của người chơi.
     * 
     * @param player Người chơi đang ở trạng thái socola.
     */
    public void removeSocola(Player player) {
        player.effectSkill.isSocola = false;
        Service.gI().Send_Caitrang(player);
    }

    /**
     * Gửi hiệu ứng biến quái thành socola.
     * 
     * @param player Người chơi sử dụng kỹ năng.
     * @param mob Quái vật bị biến thành socola.
     * @param timeSocola Thời gian hiệu ứng kéo dài.
     */
    public void sendMobToSocola(Player player, Mob mob, int timeSocola) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id);
            msg.writer().writeShort(4133);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setSocola(System.currentTimeMillis(), timeSocola);
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Thiết lập trạng thái bị choáng bởi kỹ năng dịch chuyển tức thời.
     * 
     * @param player Người chơi bị choáng.
     * @param lastTimeDCTT Thời gian bắt đầu hiệu ứng.
     * @param timeBlindDCTT Thời gian hiệu ứng kéo dài.
     */
    public void setBlindDCTT(Player player, long lastTimeDCTT, int timeBlindDCTT) {
        if (player == null) {
            return;
        }
        player.effectSkill.isBlindDCTT = true;
        player.effectSkill.lastTimeBlindDCTT = lastTimeDCTT;
        player.effectSkill.timeBlindDCTT = timeBlindDCTT;
    }

    /**
     * Hủy trạng thái choáng bởi kỹ năng dịch chuyển tức thời.
     * 
     * @param player Người chơi đang bị choáng.
     */
    public void removeBlindDCTT(Player player) {
        if (player == null) {
            return;
        }
        player.effectSkill.isBlindDCTT = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }

    /**
     * Thiết lập trạng thái huýt sáo cho người chơi.
     * 
     * @param player Người chơi bị huýt sáo.
     * @param tiLeHP Tỷ lệ HP bị ảnh hưởng bởi huýt sáo.
     */
    public void setStartHuytSao(Player player, int tiLeHP) {
        try {
            if (player == null) {
                return;
            }
            int tiLeHPNonNegative = Math.max(0, tiLeHP);
            player.effectSkill.tiLeHPHuytSao = tiLeHPNonNegative;
            player.effectSkill.lastTimeHuytSao = System.currentTimeMillis();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Hủy trạng thái huýt sáo của người chơi.
     * 
     * @param player Người chơi đang bị huýt sáo.
     */
    public void removeHuytSao(Player player) {
        if (player == null) {
            return;
        }
        player.effectSkill.tiLeHPHuytSao = 0;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HUYT_SAO_EFFECT);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    /**
     * Thiết lập trạng thái biến khỉ cho người chơi.
     * 
     * @param player Người chơi sử dụng kỹ năng biến khỉ.
     */
    public void setIsMonkey(Player player) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int timeMonkey = SkillUtil.getTimeMonkey(player.playerSkill.skillSelect.point);
        if (player.setClothes.cadic == 5) {
            timeMonkey *= 5;
        }
        player.effectSkill.isMonkey = true;
        player.effectSkill.timeMonkey = timeMonkey;
        player.effectSkill.lastTimeUpMonkey = System.currentTimeMillis();
        player.effectSkill.levelMonkey = (byte) player.playerSkill.skillSelect.point;
        player.nPoint.setHp(player.nPoint.hp * 2);
    }

    /**
     * Hủy trạng thái biến khỉ của người chơi.
     * 
     * @param player Người chơi đang ở trạng thái biến khỉ.
     */
    public void monkeyDown(Player player) {
        player.effectSkill.isMonkey = false;
        player.effectSkill.levelMonkey = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }
        sendEffectEndCharge(player);
        sendEffectMonkey(player);
        Service.gI().setNotMonkey(player);
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendInfoPlayerEatPea(player);
    }

    /**
     * Bắt đầu trạng thái tái tạo năng lượng cho người chơi.
     * 
     * @param player Người chơi sử dụng kỹ năng tái tạo năng lượng.
     */
    public void startCharge(Player player) {
        if (!player.effectSkill.isCharging) {
            player.effectSkill.isCharging = true;
            sendEffectCharge(player);
        }
    }

    /**
     * Dừng trạng thái tái tạo năng lượng của người chơi.
     * 
     * @param player Người chơi đang tái tạo năng lượng.
     */
    public void stopCharge(Player player) {
        player.effectSkill.countCharging = 0;
        player.effectSkill.isCharging = false;
        sendEffectStopCharge(player);
    }

    /**
     * Thiết lập trạng thái khiên năng lượng cho người chơi.
     * 
     * @param player Người chơi sử dụng kỹ năng khiên năng lượng.
     */
    public void setStartShield(Player player) {
        player.effectSkill.isShielding = true;
        player.effectSkill.lastTimeShieldUp = System.currentTimeMillis();
        player.effectSkill.timeShield = SkillUtil.getTimeShield(player.playerSkill.skillSelect.point);
    }

    /**
     * Hủy trạng thái khiên năng lượng của người chơi.
     * 
     * @param player Người chơi đang có khiên năng lượng.
     */
    public void removeShield(Player player) {
        player.effectSkill.isShielding = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SHIELD_EFFECT);
    }

    /**
     * Phá khiên năng lượng của người chơi và thông báo.
     * 
     * @param player Người chơi có khiên năng lượng bị phá.
     */
    public void breakShield(Player player) {
        removeShield(player);
        Service.gI().sendThongBao(player, "Khiên năng lượng đã bị vỡ!");
        ItemTimeService.gI().removeItemTime(player, 3784);
    }

    /**
     * Gửi hiệu ứng choáng thái dương hạ san cho nhiều người chơi và quái vật.
     * 
     * @param plUseSkill Người chơi sử dụng kỹ năng.
     * @param players Danh sách người chơi bị ảnh hưởng.
     * @param mobs Danh sách quái vật bị ảnh hưởng.
     * @param timeStun Thời gian hiệu ứng choáng kéo dài.
     */
    public void sendEffectBlindThaiDuongHaSan(Player plUseSkill, List<Player> players, List<Mob> mobs, int timeStun) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plUseSkill.id);
            msg.writer().writeShort(plUseSkill.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeByte(mob.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            msg.writer().writeByte(players.size());
            for (Player pl : players) {
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            Service.gI().sendMessAllPlayerInMap(plUseSkill, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng bắt đầu gồng (tái tạo năng lượng) của người chơi.
     * 
     * @param player Người chơi bắt đầu gồng.
     */
    public void sendEffectStartCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng đang gồng (tái tạo năng lượng) của người chơi.
     * 
     * @param player Người chơi đang gồng.
     */
    public void sendEffectCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng dừng gồng (tái tạo năng lượng) của người chơi.
     * 
     * @param player Người chơi dừng gồng.
     */
    public void sendEffectStopCharge(Player player) {
        try {
            Message msg = new Message(-45);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(-1);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng nổ khi kết thúc gồng (tái tạo năng lượng) của người chơi.
     * 
     * @param player Người chơi kết thúc gồng.
     */
    public void sendEffectEndCharge(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(5);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng biến khỉ của người chơi.
     * 
     * @param player Người chơi sử dụng kỹ năng biến khỉ.
     */
    public void sendEffectMonkey(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.BIEN_KHI);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(5);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    /**
     * Gửi hiệu ứng biến quái thành cái bình chứa.
     * 
     * @param player Người chơi sử dụng kỹ năng.
     * @param mob Quái vật bị biến thành cái bình chứa.
     * @param timeSocola Thời gian hiệu ứng kéo dài.
     * @param iconID ID của biểu tượng hiển thị.
     */
    public void sendMobToCaiBinh(Player player, Mob mob, int timeSocola, short iconID) {
        Message message = null;
        try {
            message = new Message(-112);
            message.writer().writeByte(1);
            message.writer().writeByte(mob.id);
            message.writer().writeShort(iconID);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
            mob.effectSkill.setCaiBinhChua(System.currentTimeMillis(), timeSocola);
        } catch (Exception e) {
            com.girlkun.utils.Logger.logException(EffectSkillService.class, e);
        } finally {
            if (message != null) {
                message.cleanup();
            }
        }
    }

    /**
     * Thiết lập trạng thái biến thành cái bình chứa cho người chơi.
     * 
     * @param player Người chơi bị biến thành cái bình chứa.
     * @param time Thời gian hiệu ứng kéo dài.
     */
    public void sendPlayerToCaiBinh(Player player, int time) {
        if (player.effectSkill != null) {
            player.effectSkill.isCaiBinhChua = true;
            player.effectSkill.timeCaiBinhChua = time;
            player.effectSkill.lastTimeCaiBinhChua = System.currentTimeMillis();
            Service.getInstance().Send_Caitrang(player);
        }
    }
}