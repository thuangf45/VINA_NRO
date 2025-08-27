package com.girlkun.models.npc.specialnpc;

import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.PetService;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Util;
import com.girlkun.network.io.Message;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;

/**
 * Class BillEgg - Quản lý cơ chế Trứng Bill
 * - Mỗi Player có thể sở hữu một BillEgg.
 * - Sau khi đủ thời gian, trứng sẽ nở ra BerusPet.
 * @author Lucifer
 */
public class BillEgg {

    //    private static final long DEFAULT_TIME_DONE = 7776000000L;
    /** Thời gian mặc định để trứng nở (1 ngày = 86400000 ms) */
    private static final long DEFAULT_TIME_DONE = 86400000L;

    /** Người chơi sở hữu trứng */
    private Player player;

    /** Thời điểm bắt đầu tạo trứng (timestamp) */
    public long lastTimeCreate;

    /** Tổng thời gian cần để trứng nở */
    public long timeDone;

    /** ID định danh trứng Bill */
    private final short id = 50;

    /**
     * Constructor BillEgg
     * @param player Người chơi sở hữu
     * @param lastTimeCreate Thời điểm tạo trứng
     * @param timeDone Thời gian nở
     */
    public BillEgg(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    /**
     * Tạo trứng Bill mới cho player
     */
    public static void createBillEgg(Player player) {
        player.billEgg = new BillEgg(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }

    /**
     * Gửi thông tin trứng Bill xuống client
     * - Hiển thị đồng hồ đếm ngược
     */
    public void sendBillEgg() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(15073);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(BillEgg.class, e);
        }
    }

    /**
     * Tính số giây còn lại để trứng nở
     * @return số giây > 0, nếu hết hạn thì trả về 0
     */
    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    /**
     * Mở trứng Bill
     * - Yêu cầu: người chơi phải có đệ tử
     * - Nếu thành công:
     *   + Hủy trứng
     *   + Tạo mới hoặc đổi pet sang BerusPet
     *   + Đưa người chơi ra map sân (theo gender)
     */
    public void openEgg(int gender) {
        if (this.player.pet != null) {
            try {
                destroyEgg();
                Thread.sleep(4000);
                if (this.player.pet == null) {
                    PetService.gI().createBerusPet(this.player, gender);
                } else {
                    PetService.gI().changeBerusPet(this.player, gender);
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
                player.billEgg = null;
            } catch (Exception e) {

            }

        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử");
        }
    }

    /**
     * Hủy trứng Bill hiện tại
     * - Gửi hiệu ứng hủy trứng xuống client
     * - Xóa billEgg khỏi player
     */
    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
        this.player.billEgg = null;
    }

    /**
     * Giảm thời gian nở trứng
     * @param d ngày
     * @param h giờ
     * @param m phút
     * @param s giây
     */
    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        this.sendBillEgg();
    }

    /**
     * Giải phóng tham chiếu player khi không dùng nữa
     */
    public void dispose(){
        this.player = null;
    }
}
