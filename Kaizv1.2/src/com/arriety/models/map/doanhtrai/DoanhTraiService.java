package com.arriety.models.map.doanhtrai;

import com.girlkun.models.boss.list_boss.doanh_trai.NinjaTim;
import com.girlkun.models.boss.list_boss.doanh_trai.RobotVeSi;
import com.girlkun.models.boss.list_boss.doanh_trai.TrungUyThep;
import com.girlkun.models.boss.list_boss.doanh_trai.TrungUyTrang;
import com.girlkun.models.boss.list_boss.doanh_trai.TrungUyXanhLo;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp DoanhTraiService:
 * Lớp DoanhTraiService thuộc package com.arriety.models.map.doanhtrai, là một singleton quản lý các doanh trại (DoanhTrai) trong game. Lớp chịu trách nhiệm khởi tạo, thêm khu vực, cập nhật trạng thái, và xử lý logic tham gia doanh trại cho người chơi. Ngoài ra, lớp quản lý thời gian reset và thời gian chờ của doanh trại, cũng như khởi tạo các boss trong doanh trại dựa trên chỉ số của bang hội.
 */
public class DoanhTraiService {
    /**
     * I: DoanhTraiService - Biến tĩnh để lưu trữ instance duy nhất của lớp DoanhTraiService (singleton pattern).
     */
    private static DoanhTraiService I;

    /**
     * doanhTrais: List<DoanhTrai> - Danh sách các doanh trại, được khởi tạo với số lượng tối đa (DoanhTrai.AVAILABLE).
     */
    public List<DoanhTrai> doanhTrais;

    /**
     * TIME_RESET_DT: long - Thời gian reset doanh trại (tính bằng mili-giây), xác định khi nào doanh trại được làm mới.
     */
    public static long TIME_RESET_DT;

    /**
     * TIME_DELAY_DT: long - Thời gian chờ sau reset (tính bằng mili-giây), xác định khi nào doanh trại có thể mở lại.
     */
    public static long TIME_DELAY_DT;

    /**
     * TIME_RESET_DT_HOUR, TIME_RESET_DT_MIN, TIME_RESET_DT_SECOND: byte - Giờ, phút, giây mặc định để reset doanh trại (7:00:00).
     */
    public static final byte TIME_RESET_DT_HOUR = 7;
    public static final byte TIME_RESET_DT_MIN = 0;
    public static final byte TIME_RESET_DT_SECOND = 0;

    /**
     * TIME_DELAY_DT_HOUR, TIME_DELAY_DT_MIN, TIME_DELAY_DT_SECOND: byte - Giờ, phút, giây mặc định cho thời gian chờ (7:00:01).
     */
    public static final byte TIME_DELAY_DT_HOUR = 7;
    public static final byte TIME_DELAY_DT_MIN = 0;
    public static final byte TIME_DELAY_DT_SECOND = 1;

    /**
     * day: int - Ngày hiện tại (dựa trên TimeUtil.getCurrDay()), dùng để kiểm tra và cập nhật thời gian reset nếu ngày thay đổi.
     */
    private int day = -1;

    /**
     * gI():
     * - Mô tả: Trả về instance duy nhất của lớp DoanhTraiService (singleton pattern). Gọi setTimeJoinMapDT() để cập nhật thời gian reset nếu cần.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: DoanhTraiService - Instance duy nhất của lớp.
     */
    public static DoanhTraiService gI() {
        if (DoanhTraiService.I == null) {
            DoanhTraiService.I = new DoanhTraiService();
        }
        DoanhTraiService.I.setTimeJoinMapDT();
        return DoanhTraiService.I;
    }

    /**
     * setTimeJoinMapDT():
     * - Mô tả: Cập nhật thời gian reset (TIME_RESET_DT) và thời gian chờ (TIME_DELAY_DT) nếu ngày hiện tại thay đổi.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Kiểm tra nếu day chưa được gán hoặc khác ngày hiện tại (TimeUtil.getCurrDay()).
     *   - Cập nhật day bằng ngày hiện tại.
     *   - Tính TIME_RESET_DT và TIME_DELAY_DT dựa trên thời gian hiện tại và các giá trị TIME_RESET_DT_HOUR, TIME_DELAY_DT_HOUR, v.v.
     * - Trả về: Không trả về gì, chỉ cập nhật thời gian.
     */
    public void setTimeJoinMapDT() {
        if (DoanhTraiService.I.day == -1 || DoanhTraiService.I.day != TimeUtil.getCurrDay()) {
            DoanhTraiService.I.day = TimeUtil.getCurrDay();
            try {
                TIME_RESET_DT = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + TIME_RESET_DT_HOUR + ":" + TIME_RESET_DT_MIN + ":" + TIME_RESET_DT_SECOND, "dd/MM/yyyy HH:mm:ss");
                TIME_DELAY_DT = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + TIME_DELAY_DT_HOUR + ":" + TIME_DELAY_DT_MIN + ":" + TIME_DELAY_DT_SECOND, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Constructor:
     * - Mô tả: Khởi tạo đối tượng DoanhTraiService với danh sách doanh trại (doanhTrais) chứa số lượng DoanhTrai.AVAILABLE doanh trại.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Trả về: Không trả về gì, tạo mới đối tượng với danh sách doanh trại.
     */
    private DoanhTraiService() {
        this.doanhTrais = new ArrayList<>();
        for (int i = 0; i < DoanhTrai.AVAILABLE; i++) {
            this.doanhTrais.add(new DoanhTrai(i));
        }
    }

    /**
     * addMapDoanhTrai(int id, Zone zone):
     * - Mô tả: Thêm một khu vực (Zone) vào doanh trại với ID tương ứng.
     * - Thuộc tính:
     *   - id: int - ID của doanh trại.
     *   - zone: Zone - Khu vực cần thêm.
     * - Xử lý logic: Gọi phương thức addZone của doanh trại tương ứng để thêm zone vào danh sách zones.
     * - Trả về: Không trả về gì, chỉ thêm zone vào doanh trại.
     */
    public void addMapDoanhTrai(int id, Zone zone) {
        this.doanhTrais.get(id).getZones().add(zone);
    }

    /**
     * update(Player player):
     * - Mô tả: Cập nhật trạng thái doanh trại của người chơi, kiểm tra thời gian tồn tại và reset doanh trại nếu hết thời gian hoặc trong khoảng thời gian reset.
     * - Thuộc tính:
     *   - player: Player - Người chơi cần kiểm tra.
     * - Xử lý logic:
     *   - Nếu người chơi là nhân vật chính (isPl()), có doanh trại (clan.doanhTrai != null) và thời gian mở doanh trại (timeOpenDoanhTrai) không bằng 0:
     *     - Kiểm tra nếu thời gian doanh trại (TIME_DOANH_TRAI = 30 phút) đã hết, gọi ketthucDT để kết thúc doanh trại.
     *     - Kiểm tra nếu thời gian hiện tại nằm trong khoảng TIME_RESET_DT và TIME_DELAY_DT, đặt clan.doanhTrai = null để reset.
     * - Trả về: Không trả về gì, chỉ cập nhật trạng thái doanh trại.
     */
    public void update(Player player) {
        if (player.isPl() && player.clan.doanhTrai != null && player.clan.timeOpenDoanhTrai != 0) {
            if (Util.canDoWithTime(player.clan.timeOpenDoanhTrai, TIME_DOANH_TRAI)) {
                ketthucDT(player);
            }
            try {
                long now = System.currentTimeMillis();
                if (now > TIME_RESET_DT && now < TIME_DELAY_DT) {
                    player.clan.doanhTrai = null;
                }
            } catch (Exception ignored) {
            }
        } else {
            try {
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * kickOutOfDT(Player player):
     * - Mô tả: Đưa người chơi ra khỏi doanh trại nếu họ đang ở bản đồ doanh trại, sử dụng tàu vận chuyển để chuyển về bản đồ chính.
     * - Thuộc tính:
     *   - player: Player - Người chơi cần đưa ra.
     * - Xử lý logic:
     *   - Kiểm tra nếu người chơi đang ở bản đồ doanh trại (MapService.gI().isMapDoanhTrai).
     *   - Gửi thông báo "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà".
     *   - Chuyển người chơi về bản đồ chính (ID bản đồ = gender + 21) bằng ChangeMapService.
     * - Trả về: Không trả về gì, chỉ thực hiện hành động đưa người chơi ra khỏi doanh trại.
     */
    private void kickOutOfDT(Player player) {
        if (MapService.gI().isMapDoanhTrai(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    /**
     * ketthucDT(Player player):
     * - Mô tả: Kết thúc doanh trại, đưa tất cả người chơi trong bản đồ doanh trại ra ngoài.
     * - Thuộc tính:
     *   - player: Player - Người chơi khởi tạo kết thúc doanh trại.
     * - Xử lý logic:
     *   - Lấy danh sách người chơi trong khu vực hiện tại (player.zone.getPlayers()).
     *   - Duyệt ngược danh sách và gọi kickOutOfDT cho từng người chơi.
     * - Trả về: Không trả về gì, chỉ đưa người chơi ra khỏi doanh trại.
     */
    private void ketthucDT(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfDT(pl);
        }
    }

    /**
     * joinDoanhTrai(Player player):
     * - Mô tả: Cho phép người chơi tham gia hoặc mở doanh trại. Nếu bang hội đã có doanh trại, chuyển người chơi vào; nếu không, tìm doanh trại trống để mở và khởi tạo boss.
     * - Thuộc tính:
     *   - player: Player - Người chơi muốn tham gia.
     * - Xử lý logic:
     *   - Nếu người chơi không có bang hội (clan = null), thông báo lỗi và thoát.
     *   - Nếu bang hội đã có doanh trại, chuyển người chơi vào bản đồ doanh trại (map ID 53).
     *   - Nếu không, tìm doanh trại trống (clan = null) trong danh sách doanhTrais.
     *   - Nếu không tìm thấy doanh trại trống, thông báo "Doanh trại đã đầy".
     *   - Nếu tìm thấy, mở doanh trại (openDoanhTrai) và khởi tạo các boss (TrungUyTrang, TrungUyXanhLo, TrungUyThep, NinjaTim, RobotVeSi) với chỉ số dựa trên tổng dame và hp của thành viên bang hội.
     *   - Giới hạn dame và hp của boss ở mức tối đa 2 tỷ.
     * - Trả về: Không trả về gì, thực hiện hành động tham gia hoặc mở doanh trại.
     */
    public void joinDoanhTrai(Player player) {
        if (player.clan == null) {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (player.clan.doanhTrai != null) {
            ChangeMapService.gI().changeMapInYard(player, 53, -1, 60);
            return;
        }
        DoanhTrai doanhTrai = null;
        for (DoanhTrai dt : this.doanhTrais) {
            if (dt.getClan() == null) {
                doanhTrai = dt;
                break;
            }
        }
        if (doanhTrai == null) {
            Service.getInstance().sendThongBao(player, "Doanh trại đã đầy, hãy quay lại vào lúc khác!");
            return;
        }
        if (doanhTrai != null) {
            doanhTrai.openDoanhTrai(player);
            try {
                long totalDame = 0;
                long totalHp = 0;
                for (Player play : player.clan.membersInGame) {
                    totalDame += play.nPoint.dame;
                    totalHp += play.nPoint.hpMax;
                }
                long dame = (totalHp / 20) * 5;
                long hp = (totalDame * 4) * 5;
                if (dame >= 2000000000L) {
                    dame = 2000000000L;
                }
                if (hp >= 2000000000L) {
                    hp = 2000000000L;
                }
                new TrungUyTrang(player.clan.doanhTrai.getMapById(59), (int) totalDame, (int) totalHp);
                new TrungUyXanhLo(player.clan.doanhTrai.getMapById(62), (int) totalDame, (int) totalHp);
                new TrungUyThep(player.clan.doanhTrai.getMapById(55), (int) totalDame, (int) totalHp);
                new NinjaTim(player.clan.doanhTrai.getMapById(54), (int) totalDame, (int) totalHp);
                new RobotVeSi(player.clan.doanhTrai.getMapById(57), (int) totalDame, (int) totalHp);
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBao(player, "Doanh Trại đã đầy, vui lòng quay lại sau");
        }
    }
}