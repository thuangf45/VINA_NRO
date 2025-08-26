package com.girlkun.models.ThanhTich;

import com.girlkun.models.player.Player;

/**
 * Lớp kiểm tra các nhiệm vụ/ngày và điều kiện thành tích của người chơi.
 * Dùng để xác định xem player đã hoàn thành yêu cầu nào trong ngày.
 * 
 * @author Lucifer
 */
public class CheckTaskDays {
    
    /**
     * Kiểm tra tất cả các nhiệm vụ/ngày của người chơi.
     * @param pl Người chơi
     * @return Mảng boolean biểu thị trạng thái từng nhiệm vụ
     */
    public static boolean[] CheckTaskDay(Player pl) {
        return new boolean[] {
            CheckTimeOnline(pl,0),
            CheckTimeOnline(pl,1),
            CheckTimeOnline(pl,2),
            CheckTimeOnline(pl,3),
            CheckTimeOnline(pl,4),
            CheckTimeOnline(pl,5),
            CheckDoneDTDN(pl),
            CheckDoneDKB(pl),
            CheckJoinNRSD(pl),
            CheckDoneNRSD(pl),
            CheckCauCa(pl)
        };
    }

    /**
     * Kiểm tra tất cả các mốc nạp thẻ trong ngày.
     * @param pl Người chơi
     * @return Mảng boolean trạng thái các mốc nạp
     */
    public static boolean[] CheckNapTheDay(Player pl) {
        return new boolean[] {
            CheckNapThe(pl,0),
            CheckNapThe(pl,1),
            CheckNapThe(pl,2),
            CheckNapThe(pl,3),
            CheckNapThe(pl,4),
            CheckNapThe(pl,5),
            CheckNapThe(pl,6)
        };
    }

    /**
     * Kiểm tra một mốc nạp thẻ cụ thể trong ngày.
     * @param pl Người chơi
     * @param type Loại mốc (0-6)
     */
    public static boolean CheckNapThe(Player pl,int type) {
        int napngay = pl.NapNgay;
        switch(type) {
            case 0 : return napngay >= 10000;
            case 1 : return napngay >= 20000;
            case 2 : return napngay >= 50000;
            case 3 : return napngay >= 100000;
            case 4 : return napngay >= 200000;
            case 5 : return napngay >= 500000;
            case 6 : return napngay >= 1000000;
        }
        return false;
    }

    /**
     * Kiểm tra thời gian online theo mốc.
     * @param pl Người chơi
     * @param type Loại mốc (0-5)
     */
    public static boolean CheckTimeOnline(Player pl,int type) {
        int time = pl.TimeOnline;
        switch(type) {
            case 0 : return time >= 10;
            case 1 : return time >= 30;
            case 2 : return time >= 60;
            case 3 : return time >= 120;
            case 4 : return time >= 180;
            case 5 : return time >= 300;
        }
        return false;
    }

    /**
     * Lấy chuỗi mô tả cho thời gian online (chưa cài đặt).
     */
    public static String GetSTRTimeOnline(Player pl,int type) {
        return "";
    }

    /** Kiểm tra hoàn thành nhiệm vụ DTDN */
    public static boolean CheckDoneDTDN(Player pl) {
        return pl.DoneDTDN;
    }

    /** Kiểm tra hoàn thành nhiệm vụ DKB */
    public static boolean CheckDoneDKB(Player pl) {
        return pl.DoneDKB;
    }

    /** Kiểm tra đã tham gia NRSD */
    public static boolean CheckJoinNRSD(Player pl) {
        return pl.JoinNRSD;
    }

    /** Kiểm tra hoàn thành NRSD */
    public static boolean CheckDoneNRSD(Player pl) {
        return pl.DoneNRSD;
    }

    /** Kiểm tra số lần câu cá (đủ >= 10 mới tính) */
    public static boolean CheckCauCa(Player pl) {
        return pl.TickCauCa >= 10;
    }

    /** Lấy chuỗi mô tả trạng thái câu cá (chưa cài đặt) */
    public static String CheckSTRCauCa(Player pl) {
        return "";
    }
}
