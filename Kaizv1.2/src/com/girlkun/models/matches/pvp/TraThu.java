package com.girlkun.models.matches.pvp;

import com.girlkun.models.matches.PVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho trận đấu trả thù (PVP) giữa hai người chơi trong game.
 * @author Lucifer
 */
public class TraThu extends PVP {

    /**
     * Constructor khởi tạo trận đấu trả thù giữa hai người chơi.
     * @param p1 Người chơi khởi xướng trả thù
     * @param p2 Người chơi bị trả thù
     */
    public TraThu(Player p1, Player p2) {
        super(TYPE_PVP.TRA_THU, p1, p2);
    }

    /**
     * Bắt đầu trận đấu trả thù, di chuyển người chơi khởi xướng đến vị trí đối thủ và gửi thông báo.
     */
    @Override
    public void start() {
        ChangeMapService.gI().changeMap(p1, p2.zone, p2.location.x + Util.nextInt(-5, 5), p2.location.y);
        Service.gI().sendThongBao(p2, "Có người tìm tới bạn để trả thù");
        Service.gI().chat(p1, "Mày Tới Số Rồi Con Ạ!");
        super.start();
    }

    /**
     * Kết thúc trận đấu trả thù (hiện không thực hiện hành động cụ thể).
     */
    @Override
    public void finish() {
    }

    /**
     * Cập nhật trạng thái trận đấu trả thù (hiện không thực hiện hành động cụ thể).
     */
    @Override
    public void update() {
    }

    /**
     * Trao phần thưởng cho người chơi thắng trận đấu trả thù (hiện không thực hiện hành động cụ thể).
     * @param plWin Người chơi thắng
     */
    @Override
    public void reward(Player plWin) {
    }

    /**
     * Gửi kết quả trận đấu trả thù và xử lý thông báo cho người thua (hiện không thực hiện hành động cụ thể).
     * @param plLose Người chơi thua
     * @param typeLose Loại thua cuộc (bỏ chạy hoặc chết)
     */
    @Override
    public void sendResult(Player plLose, TYPE_LOSE_PVP typeLose) {
    }
}