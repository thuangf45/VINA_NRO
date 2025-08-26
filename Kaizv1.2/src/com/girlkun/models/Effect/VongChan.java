package com.girlkun.models.Effect;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;

/**
 * Lớp xử lý các chức năng liên quan đến hiệu ứng vòng chân của nhân vật.
 * @author Lucifer
 */
public class VongChan {

    /**
     * Gán hiệu ứng vòng chân thứ nhất cho nhân vật dựa trên vật phẩm và gửi thông báo đến bản đồ.
     * @param pl Người chơi nhận hiệu ứng vòng chân
     * @param item Vật phẩm chứa thông tin vòng chân
     * @param z Tham số không sử dụng (dự phòng)
     */
    public static void CheckVongChan(Player pl, Item item, int z) {
        if (pl == null || item == null) {
            return; // Thoát nếu người chơi hoặc vật phẩm là null
        }
        /** Gán ID vòng chân thứ nhất dựa trên ID vật phẩm */
        switch (item.template.id) {
            case 1962:
            case 1963:
            case 1964:
            case 1242:
                pl.idVongChan = 21912;
                break;
            case 1243:
                pl.idVongChan = 21913;
                break;
            case 1244:
                pl.idVongChan = 21914;
                break;
            case 1245:
                pl.idVongChan = 21915;
                break;
            case 1246:
                pl.idVongChan = 21916;
                break;
            case 1247:
                pl.idVongChan = 21917;
                break;
            case 1248:
                pl.idVongChan = 21918;
                break;
        }
        /** Gửi hiệu ứng vòng chân đến tất cả người chơi trong bản đồ */
        EffectService.SendEffChartoMap(pl);
    }

    /**
     * Gán hiệu ứng vòng chân thứ hai cho nhân vật dựa trên vật phẩm và gửi thông báo đến bản đồ.
     * @param pl Người chơi nhận hiệu ứng vòng chân
     * @param item Vật phẩm chứa thông tin vòng chân
     * @param z Tham số không sử dụng (dự phòng)
     */
    public static void CheckVongChan2(Player pl, Item item, int z) {
        if (pl == null || item == null) {
            return; // Thoát nếu người chơi hoặc vật phẩm là null
        }
        /** Gán ID vòng chân thứ hai dựa trên ID vật phẩm */
        switch (item.template.id) {
            case 1249:
                pl.idVongChan2 = 21912;
                break;
            case 1250:
                pl.idVongChan2 = 21913;
                break;
            case 1251:
                pl.idVongChan2 = 21914;
                break;
            case 1252:
                pl.idVongChan2 = 21915;
                break;
            case 1253:
                pl.idVongChan2 = 21916;
                break;
            case 1254:
                pl.idVongChan2 = 21917;
                break;
            case 1255:
                pl.idVongChan2 = 21918;
                break;
        }
        /** Gửi hiệu ứng vòng chân đến tất cả người chơi trong bản đồ */
        EffectService.SendEffChartoMap(pl);
    }
}