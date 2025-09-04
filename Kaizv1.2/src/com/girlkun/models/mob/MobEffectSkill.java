package com.girlkun.models.mob;

import com.girlkun.network.io.Message;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

/**
 * Manages skill effects applied to a mob, such as stun, hypnosis, blindness, transformation, and binding.
 *
 * @author Lucifer
 */
public class MobEffectSkill {

    private final Mob mob;

    public MobEffectSkill(Mob mob) {
        this.mob = mob;
    }

    public boolean isStun;
    public long lastTimeStun;
    public int timeStun;

    public boolean isThoiMien;
    public long lastTimeThoiMien;
    public int timeThoiMien;

    public boolean isBlindDCTT;
    public long lastTimeBlindDCTT;
    public int timeBlindDCTT;

    public boolean isAnTroi;
    public long lastTimeAnTroi;
    public int timeAnTroi;

    public boolean isSocola;
    private long lastTimeSocola;
    private int timeSocola;

    public boolean isCaiBinhChua;
    private long lastTimeCaiBinhChua;
    private int timeCaiBinhChua;

    /**
     * Updates the status of all skill effects, removing them if they expire or if the mob is dead.
     */
    public void update() {
        if (mob.isDie()) {
            removeAllEffects();
            return;
        }

        if (isStun && Util.canDoWithTime(lastTimeStun, timeStun)) {
            removeStun();
        }
        if (isThoiMien && Util.canDoWithTime(lastTimeThoiMien, timeThoiMien)) {
            removeThoiMien();
        }
        if (isBlindDCTT && Util.canDoWithTime(lastTimeBlindDCTT, timeBlindDCTT)) {
            removeBlindDCTT();
        }
        if (isSocola && Util.canDoWithTime(lastTimeSocola, timeSocola)) {
            removeSocola();
        }
        if (isAnTroi && Util.canDoWithTime(lastTimeAnTroi, timeAnTroi)) {
            removeAnTroi();
        }
        if (isCaiBinhChua && Util.canDoWithTime(lastTimeCaiBinhChua, timeCaiBinhChua)) {
            removeCaiBinhChua();
        }
    }

    /**
     * Checks if the mob is affected by any skill effect.
     *
     * @return true if the mob has any active effect, false otherwise
     */
    public boolean isHaveEffectSkill() {
        return isAnTroi || isBlindDCTT || isStun || isThoiMien || isSocola || isCaiBinhChua;
    }

    /**
     * Applies the stun effect to the mob.
     *
     * @param lastTimeStartStun when the stun effect started
     * @param timeStun duration of the stun effect in milliseconds
     */
    public void startStun(long lastTimeStartStun, int timeStun) {
        this.lastTimeStun = lastTimeStartStun;
        this.timeStun = timeStun;
        this.isStun = true;
    }

    /**
     * Removes the stun effect from the mob and notifies all players in the map.
     */
    private void removeStun() {
        isStun = false;
        sendEffectMessage(-124, 0, 1, 40);
    }

    /**
     * Applies the hypnosis (Thoi Mien) effect to the mob.
     *
     * @param lastTimeThoiMien when the hypnosis effect started
     * @param timeThoiMien duration of the hypnosis effect in milliseconds
     */
    public void setThoiMien(long lastTimeThoiMien, int timeThoiMien) {
        this.isThoiMien = true;
        this.lastTimeThoiMien = lastTimeThoiMien;
        this.timeThoiMien = timeThoiMien;
    }

    /**
     * Removes the hypnosis effect from the mob and notifies all players in the map.
     */
    public void removeThoiMien() {
        this.isThoiMien = false;
        sendEffectMessage(-124, 0, 1, 41);
    }

    /**
     * Applies the blindness (DCTT) effect to the mob.
     *
     * @param lastTimeBlindDCTT when the blindness effect started
     * @param timeBlindDCTT duration of the blindness effect in milliseconds
     */
    public void setStartBlindDCTT(long lastTimeBlindDCTT, int timeBlindDCTT) {
        this.isBlindDCTT = true;
        this.lastTimeBlindDCTT = lastTimeBlindDCTT;
        this.timeBlindDCTT = timeBlindDCTT;
    }

    /**
     * Removes the blindness effect from the mob and notifies all players in the map.
     */
    public void removeBlindDCTT() {
        this.isBlindDCTT = false;
        sendEffectMessage(-124, 0, 1, 40);
    }

    /**
     * Applies the binding (An Troi) effect to the mob.
     *
     * @param lastTimeAnTroi when the binding effect started
     * @param timeAnTroi duration of the binding effect in milliseconds
     */
    public void setTroi(long lastTimeAnTroi, int timeAnTroi) {
        this.lastTimeAnTroi = lastTimeAnTroi;
        this.timeAnTroi = timeAnTroi;
        this.isAnTroi = true;
    }

    /**
     * Removes the binding effect from the mob and notifies all players in the map.
     */
    public void removeAnTroi() {
        isAnTroi = false;
        sendEffectMessage(-124, 0, 1, 32);
    }

    /**
     * Applies the chocolate transformation (Socola) effect to the mob.
     *
     * @param lastTimeSocola when the transformation effect started
     * @param timeSocola duration of the transformation effect in milliseconds
     */
    public void setSocola(long lastTimeSocola, int timeSocola) {
        this.lastTimeSocola = lastTimeSocola;
        this.timeSocola = timeSocola;
        this.isSocola = true;
    }

    /**
     * Removes the chocolate transformation effect from the mob and notifies all players in the map.
     */
    public void removeSocola() {
        this.isSocola = false;
        sendEffectMessage(-112, 0, -1, -1);
    }

    /**
     * Applies the Cai Binh Chua effect to the mob.
     *
     * @param lastTimeCaiBinhChua when the effect started
     * @param timeCaiBinhChua duration of the effect in milliseconds
     */
    public void setCaiBinhChua(long lastTimeCaiBinhChua, int timeCaiBinhChua) {
        this.lastTimeCaiBinhChua = lastTimeCaiBinhChua;
        this.timeCaiBinhChua = timeCaiBinhChua;
        this.isCaiBinhChua = true;
    }

    /**
     * Removes the Cai Binh Chua effect from the mob and notifies all players in the map.
     */
    public void removeCaiBinhChua() {
        this.isCaiBinhChua = false;
        sendEffectMessage(-112, 0, -1, -1);
    }

    /**
     * Removes all active effects from the mob when it dies.
     */
    private void removeAllEffects() {
        if (isStun) {
            removeStun();
        }
        if (isThoiMien) {
            removeThoiMien();
        }
        if (isBlindDCTT) {
            removeBlindDCTT();
        }
        if (isSocola) {
            removeSocola();
        }
        if (isAnTroi) {
            removeAnTroi();
        }
        if (isCaiBinhChua) {
            removeCaiBinhChua();
        }
    }

    /**
     * Sends a message to all players in the mob's zone to update the effect status.
     *
     * @param messageId the message ID (e.g., -124 or -112)
     * @param b1 first byte value
     * @param b2 second byte value
     * @param effectId effect ID or -1 if not applicable
     */
    private void sendEffectMessage(int messageId, int b1, int b2, int effectId) {
        if (mob.zone == null) {
            Logger.logException(MobEffectSkill.class, new NullPointerException("mob.zone is null"), "Failed to send effect message for mob ID: " + mob.id);
            return;
        }
        Message msg = null;
        try {
            msg = new Message(messageId);
            msg.writer().writeByte(b1);
            msg.writer().writeByte(mob.id);
            if (b2 >= 0) {
                msg.writer().writeByte(b2);
            }
            if (effectId >= 0) {
                msg.writer().writeByte(effectId);
            }
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
        } catch (Exception e) {
            Logger.logException(MobEffectSkill.class, e, "Error sending effect message for mob ID: " + mob.id);
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}