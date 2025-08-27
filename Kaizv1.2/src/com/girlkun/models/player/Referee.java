package com.girlkun.models.player;

import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.server.Manager;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Referee đại diện cho một nhân vật trọng tài trong game, kế thừa từ lớp Player.
 * Lớp này quản lý các hành vi của trọng tài, bao gồm việc tham gia bản đồ, khởi tạo thuộc tính,
 * và thông báo các thông điệp định kỳ về sự kiện trong game.
 * 
 * @author Lucifer
 */
public class Referee extends Player {

    /**
     * Thời điểm lần cuối trọng tài gửi thông báo trong game.
     */
    private long lastTimeChat;

    /**
     * Người chơi mục tiêu mà trọng tài đang tương tác hoặc theo dõi.
     */
    private Player playerTarget;

    /**
     * Thời điểm lần cuối trọng tài chọn người chơi mục tiêu.
     */
    private long lastTimeTargetPlayer;

    /**
     * Khoảng thời gian chờ để chọn người chơi mục tiêu mới, mặc định là 10 giây.
     */
    private long timeTargetPlayer = 10000;

    /**
     * Thời điểm lần cuối trọng tài chuyển đổi giữa các khu vực (zone).
     */
    private long lastZoneSwitchTime;

    /**
     * Khoảng thời gian chờ giữa các lần chuyển đổi khu vực.
     */
    private long zoneSwitchInterval;

    /**
     * Danh sách các khu vực (zone) mà trọng tài có thể tham gia.
     */
    private List<Zone> availableZones;

    /**
     * Khởi tạo một đối tượng trọng tài và gọi phương thức init() để thiết lập các thuộc tính ban đầu.
     */
    public void initReferee() {
        init();
    }

    /**
     * Lấy ID của đầu (head) cho nhân vật trọng tài.
     * 
     * @return ID của đầu, cố định là 114.
     */
    @Override
    public short getHead() {
        return 114;
    }

    /**
     * Lấy ID của thân (body) cho nhân vật trọng tài.
     * 
     * @return ID của thân, cố định là 115.
     */
    @Override
    public short getBody() {
        return 115;
    }

    /**
     * Lấy ID của chân (leg) cho nhân vật trọng tài.
     * 
     * @return ID của chân, cố định là 116.
     */
    @Override
    public short getLeg() {
        return 116;
    }

    /**
     * Cho phép trọng tài tham gia một khu vực (zone) cụ thể và di chuyển người chơi đến khu vực đó.
     * 
     * @param z Khu vực (zone) mà trọng tài sẽ tham gia.
     * @param player Người chơi (trọng tài) sẽ được di chuyển đến khu vực.
     */
    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    /**
     * Cập nhật trạng thái của trọng tài.
     * Gửi thông báo định kỳ về sự kiện Đại Hội Võ Thuật mỗi 5 giây nếu thời gian chờ đã đủ.
     */
    @Override
    public void update() {
        if (Util.canDoWithTime(lastTimeChat, 5000)) {
            Service.getInstance().chat(this, "Đại Hội Võ Thuật lần thứ 23 đã chính thức khai mạc");
            Service.getInstance().chat(this, "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn");
            lastTimeChat = System.currentTimeMillis();
        }
    }

    /**
     * Khởi tạo các đối tượng trọng tài cho các khu vực trong bản đồ có ID 52 và 129.
     * Thiết lập các thuộc tính như tên, giới tính, ID, HP, và vị trí cho trọng tài.
     * Mỗi trọng tài được gán vào một khu vực cụ thể và tham gia vào khu vực đó.
     */
    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 52) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMpDame();
                    pl.location.x = 387;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            } else if (m.mapId == 129) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMpDame();
                    pl.location.x = 385;
                    pl.location.y = 264;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }
        }
    }
}