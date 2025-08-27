package com.girlkun.models.player;

import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.services.Service;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.util.Date;

/**
 * Lớp RewardBlackBall quản lý phần thưởng liên quan đến sự kiện Ngọc Rồng Đen (Black Ball) trong game.
 * Lớp này xử lý việc nhận và quản lý phần thưởng dựa trên số sao (star) của ngọc, thời gian hết hạn phần thưởng,
 * và thời gian chờ để nhận phần thưởng.
 * 
 * @author Lucifer
 */
public class RewardBlackBall {

    /**
     * Thời gian hiệu lực của phần thưởng, mặc định là 79,200,000 milliseconds (22 giờ).
     */
    private static final int TIME_REWARD = 79200000;

    /**
     * Phần thưởng cho ngọc 1 sao (lần 1): 20 đơn vị.
     */
    public static final int R1S_1 = 20;

    /**
     * Phần thưởng cho ngọc 1 sao (lần 2): 15 đơn vị.
     */
    public static final int R1S_2 = 15;

    /**
     * Phần thưởng cho ngọc 2 sao (lần 1): 15 đơn vị.
     */
    public static final int R2S_1 = 15;

    /**
     * Phần thưởng cho ngọc 2 sao (lần 2): 20 đơn vị.
     */
    public static final int R2S_2 = 20;

    /**
     * Phần thưởng cho ngọc 3 sao (lần 1): 20 đơn vị.
     */
    public static final int R3S_1 = 20;

    /**
     * Phần thưởng cho ngọc 3 sao (lần 2): 10 đơn vị.
     */
    public static final int R3S_2 = 10;

    /**
     * Phần thưởng cho ngọc 4 sao (lần 1): 10 đơn vị.
     */
    public static final int R4S_1 = 10;

    /**
     * Phần thưởng cho ngọc 4 sao (lần 2): 20 đơn vị.
     */
    public static final int R4S_2 = 20;

    /**
     * Phần thưởng cho ngọc 5 sao (lần 1): 20 đơn vị.
     */
    public static final int R5S_1 = 20;

    /**
     * Phần thưởng cho ngọc 5 sao (lần 2): 20 đơn vị.
     */
    public static final int R5S_2 = 20;

    /**
     * Phần thưởng cho ngọc 5 sao (lần 3): 20 đơn vị.
     */
    public static final int R5S_3 = 20;

    /**
     * Phần thưởng cho ngọc 6 sao (lần 1): 50 đơn vị.
     */
    public static final int R6S_1 = 50;

    /**
     * Phần thưởng cho ngọc 6 sao (lần 2): 20 đơn vị.
     */
    public static final int R6S_2 = 20;

    /**
     * Phần thưởng cho ngọc 7 sao (lần 1): 10 đơn vị.
     */
    public static final int R7S_1 = 10;

    /**
     * Phần thưởng cho ngọc 7 sao (lần 2): 15 đơn vị.
     */
    public static final int R7S_2 = 15;

    /**
     * Thời gian chờ để nhận phần thưởng tiếp theo, mặc định là 3,600,000 milliseconds (1 giờ).
     */
    public static final int TIME_WAIT = 3600000;

    /**
     * Thời gian bắt đầu sự kiện Ngọc Rồng Đen (8 giờ).
     */
    public static long time8h;

    /**
     * Người chơi được gán với hệ thống phần thưởng Ngọc Rồng Đen.
     */
    private Player player;

    /**
     * Mảng lưu thời gian hết hạn của phần thưởng cho từng loại ngọc (1 đến 7 sao).
     */
    public long[] timeOutOfDateReward;

    /**
     * Mảng lưu số lượng ngọc đã thu thập cho từng loại ngọc (1 đến 7 sao).
     */
    public int[] quantilyBlackBall;

    /**
     * Mảng lưu thời điểm lần cuối nhận phần thưởng cho từng loại ngọc (1 đến 7 sao).
     */
    public long[] lastTimeGetReward;

    /**
     * Khởi tạo một đối tượng RewardBlackBall cho một người chơi cụ thể.
     * Khởi tạo các mảng để quản lý thời gian hết hạn, số lượng ngọc, và thời điểm nhận thưởng.
     * 
     * @param player Người chơi được gán với hệ thống phần thưởng.
     */
    public RewardBlackBall(Player player) {
        this.player = player;
        this.timeOutOfDateReward = new long[7];
        this.lastTimeGetReward = new long[7];
        this.quantilyBlackBall = new int[7];
        time8h = BlackBallWar.TIME_OPEN;
    }

    /**
     * Cập nhật phần thưởng cho người chơi khi thu thập được ngọc với số sao xác định.
     * Nếu phần thưởng chưa hết hạn, tăng số lượng ngọc và cập nhật thời gian hết hạn.
     * 
     * @param star Số sao của ngọc (từ 1 đến 7).
     */
    public void reward(byte star) {
        if (this.timeOutOfDateReward[star - 1] > time8h) {
            quantilyBlackBall[star - 1]++;
        }
        this.timeOutOfDateReward[star - 1] = System.currentTimeMillis() + TIME_REWARD;
        Service.gI().point(player);
    }

    /**
     * Cho phép người chơi nhận phần thưởng dựa trên lựa chọn thứ tự (select).
     * Tìm phần thưởng còn hiệu lực theo thứ tự và gọi phương thức nhận thưởng.
     * 
     * @param select Thứ tự của phần thưởng được chọn (dựa trên các phần thưởng còn hiệu lực).
     */
    public void getRewardSelect(byte select) {
        int index = 0;
        for (int i = 0; i < timeOutOfDateReward.length; i++) {
            if (timeOutOfDateReward[i] > System.currentTimeMillis()) {
                index++;
                if (index == select + 1) {
                    getReward(i + 1);
                    break;
                }
            }
        }
    }

    /**
     * Nhận phần thưởng cho ngọc với số sao xác định nếu phần thưởng còn hiệu lực và đã đủ thời gian chờ.
     * Gửi thông báo cho người chơi về kết quả nhận thưởng.
     * 
     * @param star Số sao của ngọc (từ 1 đến 7).
     */
    private void getReward(int star) {
        if (timeOutOfDateReward[star - 1] > System.currentTimeMillis()
                && Util.canDoWithTime(lastTimeGetReward[star - 1], TIME_WAIT)) {
            switch (star) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    Service.gI().sendThongBao(player, "Phần thưởng chỉ số tự động nhận");
                    break;
            }
        } else {
            Service.gI().sendThongBao(player, "Chưa thể nhận phần quà ngay lúc này, vui lòng đợi "
                    + TimeUtil.diffDate(new Date(lastTimeGetReward[star - 1]), new Date(lastTimeGetReward[star - 1] + TIME_WAIT),
                    TimeUtil.MINUTE) + " phút nữa");
        }
    }

    /**
     * Giải phóng tài nguyên bằng cách đặt tham chiếu đến người chơi về null.
     */
    public void dispose() {
        this.player = null;
    }
}