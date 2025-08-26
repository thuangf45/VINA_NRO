package com.girlkun.models.matches;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.player.Player;
import com.girlkun.services.PlayerService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;

/**
 * Lớp trừu tượng quản lý trận đấu PVP giữa hai người chơi trong game.
 * @author Lucifer
 */
public abstract class PVP implements IPVP {

    /**
     * Loại trận đấu PVP (ví dụ: thách đấu, giải đấu).
     */
    public TYPE_PVP typePVP;

    /**
     * Người chơi thứ nhất tham gia trận đấu.
     */
    public Player p1;

    /**
     * Người chơi thứ hai tham gia trận đấu.
     */
    public Player p2;

    /**
     * Thời gian bắt đầu trận đấu.
     */
    public long lastTimeStart;

    /**
     * Trạng thái của trận đấu (đã bắt đầu hay chưa).
     */
    private boolean started;

    /**
     * Constructor khởi tạo trận đấu PVP giữa hai người chơi.
     * @param type Loại trận đấu PVP
     * @param p1 Người chơi thứ nhất
     * @param p2 Người chơi thứ hai
     */
    public PVP(TYPE_PVP type, Player p1, Player p2) {
        this.typePVP = type;
        this.p1 = p1;
        this.p2 = p2;
        p1.pvp = this;
        p2.pvp = this;
        this.lastTimeStart = System.currentTimeMillis();
        this.start();
        PVPManager.gI().addPVP(this);
    }

    /**
     * Bắt đầu trận đấu PVP và kích hoạt trạng thái PK cho cả hai người chơi.
     */
    @Override
    public void start() {
        this.started = true;
        this.changeToTypePK();
    }

    /**
     * Chuyển cả hai người chơi sang trạng thái PK (Player Killer) để tham gia trận đấu.
     */
    protected void changeToTypePK() {
        if (this.p1 != null && this.p2 != null) {
            PlayerService.gI().changeAndSendTypePK(this.p1, ConstPlayer.PK_PVP);
            PlayerService.gI().changeAndSendTypePK(this.p2, ConstPlayer.PK_PVP);
        }
    }

    /**
     * Chuyển cả hai người chơi về trạng thái không PK (Non-Player Killer) sau khi trận đấu kết thúc.
     */
    private void changeToTypeNonPK() {
        if (this.p1 != null && this.p2 != null) {
            PlayerService.gI().changeAndSendTypePK(this.p1, ConstPlayer.NON_PK);
            PlayerService.gI().changeAndSendTypePK(this.p2, ConstPlayer.NON_PK);
        }
    }

    /**
     * Kiểm tra xem một người chơi có đang tham gia trận đấu PVP này hay không.
     * @param pl Người chơi cần kiểm tra
     * @return True nếu người chơi đang trong trận đấu, false nếu không
     */
    @Override
    public boolean isInPVP(Player pl) {
        return this.p1.equals(pl) || this.p2.equals(pl);
    }

    /**
     * Xử lý khi một người chơi thua trận đấu và trao phần thưởng cho người thắng.
     * @param plLose Người chơi thua cuộc
     * @param typeLose Loại thua cuộc (ví dụ: chết, bỏ cuộc)
     */
    @Override
    public void lose(Player plLose, TYPE_LOSE_PVP typeLose) {
        if (started) {
            this.finish();
            if (plLose.equals(p1)) {
                this.reward(p2);
            } else {
                this.reward(p1);
            }
            this.sendResult(plLose, typeLose);
            this.dispose();
        }
    }

    /**
     * Dọn dẹp tài nguyên và kết thúc trận đấu PVP.
     */
    @Override
    public void dispose() {
        this.changeToTypeNonPK();
        if (this.p1 != null) {
            this.p1.pvp = null;
            this.p1 = null;
        }
        if (this.p2 != null) {
            this.p2.pvp = null;
            this.p2 = null;
        }
        PVPManager.gI().removePVP(this);
    }
}