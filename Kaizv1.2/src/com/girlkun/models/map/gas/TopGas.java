package com.girlkun.models.map.gas;

import com.girlkun.models.clan.Clan;

/**
 * Lớp TopGas lưu trữ thông tin về bang hội trong bảng xếp hạng bản đồ Khí Gas.
 * @author Lucifer
 */
public class TopGas {

    /**
     * Bang hội liên quan đến bản đồ Khí Gas.
     */
    public Clan clan;

    /**
     * Tên của bang hội.
     */
    public String Name;

    /**
     * Cấp độ của bản đồ Khí Gas mà bang hội đã tham gia.
     */
    public int Level;

    /**
     * Thời gian hoàn thành bản đồ Khí Gas (mili giây).
     */
    public long TimeDone;

    /**
     * Khởi tạo một đối tượng TopGas dựa trên thông tin bang hội.
     * @param clan Bang hội cần lưu trữ thông tin.
     */
    public TopGas(Clan clan) {
        Name = clan.name;
        Level = clan.levelKhiGas;
        TimeDone = clan.TimeDoneKhiGas;
        this.clan = clan;
    }
}