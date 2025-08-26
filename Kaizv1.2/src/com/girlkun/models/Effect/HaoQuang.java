package com.girlkun.models.Effect;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;

/**
 * Lớp xử lý các chức năng liên quan đến hiệu ứng hào quang của nhân vật.
 * @author Lucifer
 */
public class HaoQuang {

    /**
     * Gán hiệu ứng hào quang cho nhân vật dựa trên vật phẩm và gửi thông báo đến bản đồ.
     * @param pl Người chơi nhận hào quang
     * @param item Vật phẩm chứa thông tin hào quang
     */
    public static void SendHaoQuang(Player pl, Item item) {
        if (pl == null || item == null) {
            return; // Thoát nếu người chơi hoặc vật phẩm là null
        }
        /** Gán ID hào quang dựa trên ID vật phẩm */
        switch (item.template.id) {
            case 1236:
                pl.idAura = 24;
                break;
            case 1237:
                pl.idAura = 22;
                break;
            case 1238:
                pl.idAura = 21;
                break;
            case 1239:
                pl.idAura = 23;
                break;
            case 1240:
                pl.idAura = 20;
                break;
            case 1256:
                pl.idAura = 13;
                break;
            case 1257:
                pl.idAura = 7;
                break;
            case 1258:
                pl.idAura = 6;
                break;
            case 1259:
                pl.idAura = 8;
                break;
            case 1260:
                pl.idAura = 3;
                break;
            case 1261:
                pl.idAura = 17;
                break;
            case 1262:
                pl.idAura = 4;
                break;
            case 1263:
                pl.idAura = 5;
                break;
            case 1264:
                pl.idAura = 27;
                break;
            case 1265:
                pl.idAura = 26;
                break;
        }
        /** Gửi hiệu ứng hào quang đến tất cả người chơi trong bản đồ */
        EffectService.SendEffChartoMap(pl);
    }
}