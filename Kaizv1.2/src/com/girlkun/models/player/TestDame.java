package com.girlkun.models.player;

import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.List;

/**
 * Lớp TestDame đại diện cho một nhân vật đặc biệt trong game, kế thừa từ lớp Player.
 * Lớp này được sử dụng để kiểm tra sát thương và thực hiện các hành vi như gửi thông báo định kỳ,
 * tham gia bản đồ, và xử lý các nhiệm vụ liên quan đến người chơi hoặc quái vật.
 * Nhân vật này có các đặc điểm cố định như tên, sức mạnh, và vị trí trên bản đồ.
 * 
 * @author Lucifer
 */
public class TestDame extends Player {

    /**
     * Thời điểm lần cuối nhân vật gửi thông báo trong game.
     */
    private long lastTimeChat;

    /**
     * Người chơi mục tiêu mà nhân vật đang tương tác hoặc theo dõi.
     */
    private Player playerTarget;

    /**
     * Thời điểm lần cuối nhân vật chọn người chơi mục tiêu.
     */
    private long lastTimeTargetPlayer;

    /**
     * Khoảng thời gian chờ để chọn người chơi mục tiêu mới, mặc định là 5000 milliseconds (5 giây).
     */
    private long timeTargetPlayer = 5000;

    /**
     * Thời điểm lần cuối nhân vật chuyển đổi giữa các khu vực (zone).
     */
    private long lastZoneSwitchTime;

    /**
     * Khoảng thời gian chờ giữa các lần chuyển đổi khu vực.
     */
    private long zoneSwitchInterval;

    /**
     * Danh sách các khu vực (zone) mà nhân vật có thể tham gia.
     */
    private List<Zone> availableZones;

    /**
     * Khởi tạo một đối tượng TestDame và gọi phương thức init() để thiết lập các thuộc tính ban đầu.
     */
    public void initTraidat() {
        init();
    }

    /**
     * Lấy ID của đầu (head) cho nhân vật.
     * 
     * @return ID của đầu, cố định là 83.
     */
    @Override
    public short getHead() {
        return 83;
    }

    /**
     * Lấy ID của thân (body) cho nhân vật.
     * 
     * @return ID của thân, cố định là 84.
     */
    @Override
    public short getBody() {
        return 84;
    }

    /**
     * Lấy ID của chân (leg) cho nhân vật.
     * 
     * @return ID của chân, cố định là 85.
     */
    @Override
    public short getLeg() {
        return 85;
    }

    /**
     * Xử lý sát thương mà nhân vật nhận được từ người chơi hoặc quái vật.
     * Nếu nhân vật bị tấn công bởi người chơi đang thực hiện nhiệm vụ cụ thể,
     * tiến hành nhiệm vụ tiếp theo. Nếu HP của nhân vật xuống dưới ngưỡng, phục hồi HP.
     * 
     * @param plAtt Người chơi hoặc pet tấn công nhân vật.
     * @param damage Lượng sát thương gây ra.
     * @param piercing Có bỏ qua phòng thủ hay không.
     * @param isMobAttack Có phải tấn công từ quái vật hay không.
     * @return Lượng sát thương thực tế áp dụng.
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null && (plAtt.isPl() || plAtt.isPet)) {
        }
        if (plAtt != null && plAtt.isPl() && (plAtt.playerTask.taskMain.id == 11 || plAtt.playerTask.taskMain.id == 27)) {
            TaskService.gI().sendNextTaskMain(plAtt);
            this.chat("Á đù người hướng nội !");
        }
        if (this.nPoint.hp <= 500000000) {
            this.nPoint.hp = this.nPoint.hpMax;
            Service.gI().point(this);
        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            this.chat("Sát thương gây ra: " + Util.numberToMoney(damage));
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Cho phép nhân vật tham gia một khu vực (zone) cụ thể và di chuyển đến khu vực đó.
     * 
     * @param z Khu vực (zone) mà nhân vật sẽ tham gia.
     * @param player Nhân vật (TestDame) sẽ được di chuyển đến khu vực.
     */
    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    /**
     * Cập nhật trạng thái của nhân vật.
     * Gửi thông báo định kỳ về sự kiện "NGỌC RỒNG GREEN" mỗi 5 giây nếu thời gian chờ đã đủ,
     * đồng thời phục hồi HP và MP của nhân vật.
     */
    @Override
    public void update() {
        String[] textBaoCat = {"|1|NGỌC RỒNG GREEN\nCome Back", "|1|NGỌC RỒNG GREEN\nCome Back", "|1|NGỌC RỒNG GREEN\nCome Back"};
        if (Util.canDoWithTime(lastTimeChat, 5000)) {
            Service.getInstance().chat(this, textBaoCat[Util.nextInt(textBaoCat.length - 1)]);
            lastTimeChat = System.currentTimeMillis();
            this.nPoint.setFullHpMp();
            Service.gI().point(this);
        }
    }

    /**
     * Khởi tạo các đối tượng TestDame cho các khu vực trong bản đồ có ID 164, 165, hoặc 166.
     * Thiết lập các thuộc tính như tên, giới tính, ID, HP, sức mạnh, và vị trí cho nhân vật.
     * Mỗi nhân vật được gán vào một khu vực cụ thể và tham gia vào khu vực đó.
     */
    private void init() {
        int id = Util.randomBossId();
        for (Map m : Manager.MAPS) {
            if (m.mapId == 164 || m.mapId == 165 || m.mapId == 166) {
                for (Zone z : m.zones) {
                    TestDame pl = new TestDame();
                    pl.name = "mr pôro";
                    pl.gender = 0;
                    pl.id = id;
                    pl.isBuNhin = true;
                    pl.nPoint.hpMax = (int) 2000000000;
                    pl.nPoint.hpg = (int) 2000000000;
                    pl.nPoint.hp = (int) 2000000000;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 236;
                    pl.location.y = 336;
                    pl.nPoint.power = 99999999999L;
                    pl.cFlag = 8;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }
        }
    }
}