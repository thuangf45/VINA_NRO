package com.girlkun.models.player;

/**
 * Lớp đại diện cho các bùa (charms) của người chơi trong game, quản lý thời gian hiệu lực
 * của các loại bùa khác nhau.
 * @author Lucifer
 */
public class Charms {
    
    /** Thời gian hiệu lực của bùa Trí Tuệ. */
    public long tdTriTue;

    /** Thời gian hiệu lực của bùa Mạnh Mẽ. */
    public long tdManhMe;

    /** Thời gian hiệu lực của bùa Da Trâu. */
    public long tdDaTrau;

    /** Thời gian hiệu lực của bùa Oai Hùng. */
    public long tdOaiHung;

    /** Thời gian hiệu lực của bùa Bất Tử. */
    public long tdBatTu;

    /** Thời gian hiệu lực của bùa Dẻo Dai. */
    public long tdDeoDai;

    /** Thời gian hiệu lực của bùa Thu Hút. */
    public long tdThuHut;

    /** Thời gian hiệu lực của bùa Đệ Tử. */
    public long tdDeTu;

    /** Thời gian hiệu lực của bùa Trí Tuệ cấp 3. */
    public long tdTriTue3;

    /** Thời gian hiệu lực của bùa Trí Tuệ cấp 4. */
    public long tdTriTue4;
    
    /** Thời gian cuối cùng trừ thời gian bùa Trí Tuệ cấp 4. */
    public long lastTimeSubMinTriTueX4;

    /**
     * Thêm thời gian hiệu lực cho bùa dựa trên ID vật phẩm và số phút.
     *
     * @param itemId ID của vật phẩm bùa.
     * @param min Số phút thêm vào thời gian hiệu lực.
     */
    public void addTimeCharms(int itemId, int min) {
        switch (itemId) {
            case 213:
                if (tdTriTue < System.currentTimeMillis()) {
                    tdTriTue = System.currentTimeMillis();
                }
                tdTriTue += min * 60 * 1000L;
                break;
            case 214:
                if (tdManhMe < System.currentTimeMillis()) {
                    tdManhMe = System.currentTimeMillis();
                }
                tdManhMe += min * 60 * 1000L;
                break;
            case 215:
                if (tdDaTrau < System.currentTimeMillis()) {
                    tdDaTrau = System.currentTimeMillis();
                }
                tdDaTrau += min * 60 * 1000L;
                break;
            case 216:
                if (tdOaiHung < System.currentTimeMillis()) {
                    tdOaiHung = System.currentTimeMillis();
                }
                tdOaiHung += min * 60 * 1000L;
                break;
            case 217:
                if (tdBatTu < System.currentTimeMillis()) {
                    tdBatTu = System.currentTimeMillis();
                }
                tdBatTu += min * 60 * 1000L;
                break;
            case 218:
                if (tdDeoDai < System.currentTimeMillis()) {
                    tdDeoDai = System.currentTimeMillis();
                }
                tdDeoDai += min * 60 * 1000L;
                break;
            case 219:
                if (tdThuHut < System.currentTimeMillis()) {
                    tdThuHut = System.currentTimeMillis();
                }
                tdThuHut += min * 60 * 1000L;
                break;
            case 522:
                if (tdDeTu < System.currentTimeMillis()) {
                    tdDeTu = System.currentTimeMillis();
                }
                tdDeTu += min * 60 * 1000L;
                break;
            case 671:
                if (tdTriTue3 < System.currentTimeMillis()) {
                    tdTriTue3 = System.currentTimeMillis();
                }
                tdTriTue3 += min * 60 * 1000L;
                break;
            case 672:
                if (tdTriTue4 < System.currentTimeMillis()) {
                    tdTriTue4 = System.currentTimeMillis();
                }
                tdTriTue4 += min * 60 * 1000L;
                break;
        }
    }
    
    /**
     * Giải phóng tài nguyên hoặc thiết lập lại trạng thái của các bùa.
     */
    public void dispose() {
    }
}