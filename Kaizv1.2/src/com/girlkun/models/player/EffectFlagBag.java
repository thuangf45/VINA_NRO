package com.girlkun.models.player;

/**
 * Lớp đại diện cho các cờ hiệu ứng (flag effects) của túi đồ người chơi, 
 * quản lý trạng thái sử dụng các vật phẩm hiệu ứng đặc biệt.
 * @author Lucifer
 */
public class EffectFlagBag {
    
    /** Trạng thái sử dụng vật phẩm Võ Ốc. */
    public boolean useVoOc;

    /** Trạng thái sử dụng vật phẩm Cây Kem. */
    public boolean useCayKem;

    /** Trạng thái sử dụng vật phẩm Cá Heo. */
    public boolean useCaHeo;

    /** Trạng thái sử dụng vật phẩm Con Diều. */
    public boolean useConDieu;

    /** Trạng thái sử dụng vật phẩm Diều Rồng. */
    public boolean useDieuRong;

    /** Trạng thái sử dụng vật phẩm Mèo Mùn. */
    public boolean useMeoMun;

    /** Trạng thái sử dụng vật phẩm Xiên Cá. */
    public boolean useXienCa;

    /** Trạng thái sử dụng vật phẩm Phong Heo. */
    public boolean usePhongHeo;

    /** Trạng thái sử dụng vật phẩm Hào Quang. */
    public boolean useHaoQuang;

    /**
     * Đặt lại tất cả trạng thái sử dụng vật phẩm hiệu ứng về false.
     */
    public void reset() {
        this.useVoOc = false;
        this.useCayKem = false;
        this.useCaHeo = false;
        this.useConDieu = false;
        this.useDieuRong = false;
        this.useMeoMun = false;
        this.useXienCa = false;
        this.usePhongHeo = false;
        this.useHaoQuang = false;
    }
    
    /**
     * Giải phóng tài nguyên hoặc thiết lập lại trạng thái của các cờ hiệu ứng.
     */
    public void dispose() {
    }
}