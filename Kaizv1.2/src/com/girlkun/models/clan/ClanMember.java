package com.girlkun.models.clan;

import com.girlkun.models.player.Player;
import com.girlkun.utils.TimeUtil;
import java.util.Date;

/**
 * Đại diện cho một thành viên trong Clan (Bang hội).
 * Chứa thông tin cá nhân, vai trò, đóng góp và dữ liệu hoạt động trong Clan.
 * 
 * @author Lucifer
 */
public class ClanMember {

    /** Tham chiếu đến Clan mà thành viên này thuộc về */
    public Clan clan;

    /** ID của thành viên (theo ID người chơi) */
    public int id;

    /** ID hình đầu */
    public short head;

    /** ID hình chân */
    public short leg;

    /** ID hình thân */
    public short body;

    /** Tên của thành viên */
    public String name;

    /** Vai trò trong Clan (Leader/Deputy/Member) */
    public byte role;

    /** Chỉ số sức mạnh hiện tại của thành viên */
    public long powerPoint;

    /** Số lần đã hiến tặng (donate) cho Clan */
    public int donate;

    /** Số lần nhận donate từ Clan */
    public int receiveDonate;

    /** Capsule cá nhân mà thành viên này sở hữu */
    public int memberPoint;

    /** Capsule đã đóng góp cho bang */
    public int clanPoint;

    /** Lần cuối thành viên gửi request (thời gian dạng epoch second) */
    public int lastRequest;

    /** Thời gian tham gia Clan (epoch second) */
    public int joinTime;

    /** Thời gian gần nhất xin đậu thần (epoch millis) */
    public long timeAskPea;

    /**
     * Khởi tạo mặc định.
     */
    public ClanMember() {
    }

    /**
     * Khởi tạo ClanMember từ một đối tượng Player khi tham gia Clan.
     *
     * @param player Người chơi tham gia Clan
     * @param clan Clan mà người chơi tham gia
     * @param role Vai trò trong Clan (Leader/Deputy/Member)
     */
    public ClanMember(Player player, Clan clan, byte role) {
        this.clan = clan;
        this.id = (int) player.id;
        this.head = player.getHead();
        this.body = player.getBody();
        this.leg = player.getLeg();
        this.name = player.name;
        this.role = role;
        this.powerPoint = player.nPoint.power;
        this.donate = 0;
        this.receiveDonate = 0;
        this.memberPoint = 0;
        this.clanPoint = 0;
        this.lastRequest = 0;
        this.joinTime = (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Lấy số ngày kể từ khi tham gia Clan đến ngày hiện tại.
     *
     * @return số ngày đã trôi qua từ lúc join đến hiện tại
     */
    public int getNumDateFromJoinTimeToToday() {
        return (int) TimeUtil.diffDate(new Date(), new Date(this.joinTime * 1000L), TimeUtil.DAY);
    }
    
    /**
     * Giải phóng tài nguyên khi thành viên rời Clan.
     */
    public void dispose(){
        this.clan = null;
        this.name = null;
    }

}
