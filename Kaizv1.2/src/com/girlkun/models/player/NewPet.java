package com.girlkun.models.player;

import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

/**
 * Lớp NewPet đại diện cho thú cưng của người chơi trong trò chơi, kế thừa từ lớp Player.
 * @author Lucifer
 */
public class NewPet extends Player {
    
    /**
     * Người chơi chủ nhân của thú cưng.
     */
    public Player master;
    
    /**
     * ID của phần thân thú cưng.
     */
    public short body;
    
    /**
     * ID của phần chân thú cưng.
     */
    public short leg;
    
    /**
     * ID cơ sở để gán cho thú cưng, giảm dần sau mỗi lần tạo.
     */
    public static int idb = -400000;
    
    /**
     * Khởi tạo thú cưng mới với thông tin chủ nhân và ngoại hình.
     * @param master Người chơi chủ nhân.
     * @param h ID của đầu thú cưng.
     * @param b ID của thân thú cưng.
     * @param l ID của chân thú cưng.
     * @param name Tên của thú cưng.
     */
    public NewPet(Player master, short h, short b, short l, String name) {
        this.name = name;
        this.master = master;
        this.isNewPet = true;
        // this.isNewPet1 = true;
        this.id = idb;
        // this.name = "";
        idb--;
        this.head = h;
        this.body = b;
        this.leg = l;
    }
    
    /**
     * Lấy ID của đầu thú cưng.
     * @return ID của đầu.
     */
    @Override
    public short getHead() {
        return head;
    }

    /**
     * Lấy ID của thân thú cưng.
     * @return ID của thân.
     */
    @Override
    public short getBody() {
        return body;
    }
    
    /**
     * Lấy ID của chân thú cưng.
     * @return ID của chân.
     */
    @Override
    public short getLeg() {
        return leg;
    }
    
    /**
     * Chuyển thú cưng đến bản đồ của chủ nhân.
     */
    public void joinMapMaster() {
        if (master == null) {
            return;
        }
        this.location.x = master.location.x + Util.nextInt(-10, 10);
        this.location.y = master.location.y;
        ChangeMapService.gI().goToMap(this, master.zone);
        this.zone.load_Me_To_Another(this);
    }
    
    /**
     * Thời điểm lần cuối di chuyển khi ở trạng thái rảnh rỗi.
     */
    private long lastTimeMoveIdle;
    
    /**
     * Thời gian chờ trước khi di chuyển ngẫu nhiên.
     */
    private int timeMoveIdle;
    
    /**
     * Kiểm tra trạng thái rảnh rỗi của thú cưng.
     */
    public boolean idle;

    /**
     * Di chuyển ngẫu nhiên khi ở trạng thái rảnh rỗi.
     */
    private void moveIdle() {
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }
    
    /**
     * Cập nhật trạng thái của thú cưng.
     */
    @Override
    public void update() {
        super.update();
        if (this.isDie()) {
            Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
        }
        if (master != null && (this.zone == null || this.zone != master.zone)) {
            joinMapMaster();
        }
        if (master != null && master.isDie()) {
            return;
        }
        moveIdle();
    }
    
    /**
     * Thú cưng di chuyển theo chủ nhân với khoảng cách mặc định.
     */
    public void followMaster() {
        followMaster(50);
    }

    /**
     * Thú cưng di chuyển theo chủ nhân với khoảng cách chỉ định.
     * @param dis Khoảng cách tối đa so với chủ nhân.
     */
    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }
    
    /**
     * Giải phóng tài nguyên của thú cưng.
     */
    @Override
    public void dispose() {
        this.master = null;
        super.dispose();
    }
}