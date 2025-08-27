//package com.girlkun.models.pvp_old;
//
//import com.girlkun.consts.ConstPlayer;
//import com.girlkun.models.player.Player;
//import com.girlkun.services.PlayerService;
//import com.girlkun.utils.Util;
//
///**
// * Lớp trừu tượng PVP quản lý các trận đấu giữa hai người chơi trong game.
// * Lớp này xử lý việc bắt đầu, cập nhật, và kết thúc trận đấu PVP, bao gồm việc xác định người thắng,
// * trao phần thưởng, và gửi kết quả trận đấu. Các loại PVP bao gồm thách đấu, trả thù, và Đại Hội Võ Thuật.
// * 
// * @author Lucifer
// */
//public abstract class PVP {
//
//    /**
//     * Loại kết thúc trận đấu khi người chơi rời bản đồ.
//     */
//    public static final byte TYPE_LEAVE_MAP = 7;
//
//    /**
//     * Loại kết thúc trận đấu khi người chơi bị hạ gục.
//     */
//    public static final byte TYPE_DIE = 5;
//
//    /**
//     * Loại kết thúc trận đấu khi người chơi có lượng HP thấp hơn.
//     */
//    public static final byte TYPE_LOWER_HP = 2;
//
//    /**
//     * Loại PVP thách đấu.
//     */
//    protected static final byte TYPE_PVP_CHALLENGE = 7;
//
//    /**
//     * Loại PVP trả thù.
//     */
//    protected static final byte TYPE_PVP_REVENGE = 5;
//
//    /**
//     * Loại PVP Đại Hội Võ Thuật.
//     */
//    protected static final byte TYPE_PVP_MARTIAL_CONGRESS = 2;
//
//    /**
//     * Loại trận đấu PVP (thách đấu, trả thù, hoặc Đại Hội Võ Thuật).
//     */
//    public byte typePVP;
//
//    /**
//     * Người chơi thứ nhất tham gia trận đấu PVP.
//     */
//    public Player player1;
//
//    /**
//     * Người chơi thứ hai tham gia trận đấu PVP.
//     */
//    public Player player2;
//
//    /**
//     * Trạng thái của trận đấu PVP (true nếu trận đấu đang diễn ra).
//     */
//    public boolean start;
//
//    /**
//     * Thời điểm bắt đầu trận đấu PVP.
//     */
//    public long lastTimeStart;
//
//    /**
//     * Thời gian còn lại của trận đấu PVP (mặc định -1 nếu không giới hạn thời gian).
//     */
//    public int timeLeftPVP;
//
//    /**
//     * Bắt đầu trận đấu PVP, thiết lập thời điểm bắt đầu và trạng thái trận đấu.
//     */
//    public void start() {
//        this.lastTimeStart = System.currentTimeMillis();
//        this.timeLeftPVP = -1;
//        this.start = true;
//    }
//
//    /**
//     * Cập nhật trạng thái của trận đấu PVP.
//     * Nếu trận đấu có giới hạn thời gian và đã hết thời gian, xác định người thua dựa trên phần trăm HP còn lại.
//     */
//    public void update() {
//        if (this.timeLeftPVP != -1 && this.start && Util.canDoWithTime(lastTimeStart, timeLeftPVP)) {
//            if (player1.nPoint.getCurrPercentHP() < player2.nPoint.getCurrPercentHP()) {
//                finishPVP(player1, TYPE_LOWER_HP);
//            }
//        }
//    }
//
//    /**
//     * Kết thúc trận đấu PVP và xử lý kết quả.
//     * Trao phần thưởng cho người thắng, đặt lại trạng thái PK của cả hai người chơi,
//     * gửi kết quả trận đấu, và xóa trận đấu khỏi hệ thống.
//     * 
//     * @param plLose Người chơi thua trận.
//     * @param typeWin Loại chiến thắng (rời bản đồ, bị hạ gục, hoặc HP thấp hơn).
//     */
//    public void finishPVP(Player plLose, byte typeWin) {
//        if (plLose.typePk != ConstPlayer.NON_PK) {
//            Player plWin = player1.equals(plLose) ? player2 : player1;
//            reward(plWin);
//            PlayerService.gI().changeAndSendTypePK(player1, ConstPlayer.NON_PK);
//            PlayerService.gI().changeAndSendTypePK(player2, ConstPlayer.NON_PK);
//            sendResultMatch(plWin, plLose, typeWin);
//            PVPServcice.gI().removePVP(this);
//        }
//    }
//
//    /**
//     * Gửi kết quả trận đấu PVP đến người chơi, hiển thị thông tin về người thắng và người thua.
//     * Phương thức trừu tượng, cần được triển khai bởi các lớp con.
//     * 
//     * @param winer Người chơi thắng trận.
//     * @param loser Người chơi thua trận.
//     * @param typeWin Loại chiến thắng (rời bản đồ, bị hạ gục, hoặc HP thấp hơn).
//     */
//    public abstract void sendResultMatch(Player winer, Player loser, byte typeWin);
//
//    /**
//     * Trao phần thưởng cho người chơi thắng trận PVP.
//     * Phương thức trừu tượng, cần được triển khai bởi các lớp con.
//     * 
//     * @param plWin Người chơi thắng trận.
//     */
//    public abstract void reward(Player plWin);
//
//    /**
//     * Giải phóng tài nguyên của đối tượng PVP.
//     */
//    public void dispose() {
//    }
//}