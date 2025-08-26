package com.girlkun.models.ThanhTich;

import com.girlkun.models.player.Player;

/**
 * Lớp kiểm tra các thành tích dài hạn (thời trang, boss, nhiệm vụ, nạp thẻ, sức mạnh...).
 * Dùng để xác định người chơi đã đạt mốc nào trong hệ thống thành tích.
 * 
 * @author Lucifer
 */
public class CheckTaskThanhTich {

    /**
     * Kiểm tra set quần áo (Set Ki Huyết, God Clothes, Hủy Diệt).
     * @param pl Người chơi
     * @param type Loại kiểm tra (1: SKH thường, 2: +God, 3: +Hủy Diệt)
     */
    public static boolean CheckSKH(Player pl,int type) {
        boolean flag1 = pl.setClothes.nappa == 5 || pl.setClothes.kakarot == 5 || pl.setClothes.cadic == 5;
        boolean flag2 = pl.setClothes.kirin == 5 || pl.setClothes.songoku == 5 || pl.setClothes.thienXinHang == 5;
        boolean flag3 = pl.setClothes.picolo == 5 || pl.setClothes.ocTieu == 5 || pl.setClothes.pikkoroDaimao == 5;
        if(type == 1) {
            return flag1 || flag2 || flag3;
        }
        if(type == 2) {
            return (flag1 || flag2 || flag3) && pl.setClothes.godClothes;
        }
        if(type == 3) {
            return (flag1 || flag2 || flag3) && pl.setClothes.IsSetHuyDiet();
        }
        return false;
    }

    /**
     * Kiểm tra số boss đã tiêu diệt.
     * @param type 1: 50 boss, 2: 500 boss, 3: 5000 boss
     */
    public static boolean CheckKillBoss(Player pl,int type) {
        int param = Integer.MAX_VALUE;
        switch(type) {
            case 1: param = 50; break;
            case 2: param = 500; break;
            case 3: param = 5000; break;
        }
        return pl.PointBoss >= param;
    }

    /** Chuỗi hiển thị số boss đã giết / mốc yêu cầu */
    public static String SCheckKillBoss(Player pl,int type) {
        int param = 0;
        switch(type) {
            case 1: param = 50; break;
            case 2: param = 500; break;
            case 3: param = 5000; break;
        }
        return " ("+pl.PointBoss + "/" + param + ")";
    }

    /**
     * Kiểm tra tiến độ nhiệm vụ chính.
     * @param type 1: đạt nhiệm vụ 21, 2: đạt nhiệm vụ 24
     */
    public static boolean CheckTask(Player pl,int type) {
        int param = Integer.MAX_VALUE;
        switch(type) {
            case 1: param = 21; break;
            case 2: param = 24; break;
        }
        return pl.playerTask.taskMain.id >= param;
    }

    /** Chuỗi hiển thị tiến độ nhiệm vụ chính */
    public static String SCheckTask(Player pl,int type) {
        int param = 0;
        switch(type) {
            case 1: param = 21; break;
            case 2: param = 24; break;
        }
        return " ("+pl.playerTask.taskMain.id + "/" + param + ")";
    }

    /**
     * Kiểm tra tổng số tiền nạp.
     * @param type 1: 50k, 2: 100k, 3: 500k
     */
    public static boolean CheckTongNap(Player pl,int type) {
        int param = Integer.MAX_VALUE;
        switch(type) {
            case 1: param = 50000; break;
            case 2: param = 100000; break;
            case 3: param = 500000; break;
        }
        return pl.getSession().TongNap >= param;
    }

    /** Chuỗi hiển thị tổng nạp / mốc yêu cầu */
    public static String SCheckTongNap(Player pl,int type) {
        int param = 0;
        switch(type) {
            case 1: param = 50000; break;
            case 2: param = 100000; break;
            case 3: param = 500000; break;
        }
        return " ("+pl.getSession().TongNap + "/" + param + ")";
    }

    /**
     * Kiểm tra sức mạnh tối thiểu (SMTN) của cả player và pet.
     * @param type 1: 120T, 2: 150T, 3: 180T
     */
    public static boolean CheckSMTN(Player pl,int type) {
        long param = Long.MAX_VALUE;
        switch(type) {
            case 1: param = 120_000_000_000L; break;
            case 2: param = 150_000_000_000L; break;
            case 3: param = 180_000_000_000L; break;
        }
        return pl.nPoint.power >= param && pl.pet.nPoint.power >= param;
    }
}
