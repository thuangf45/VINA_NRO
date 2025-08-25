package com.arriety.models.map.doanhtrai;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author Lucifer
 * 
 * Tóm tắt lớp DoanhTrai:
 * Lớp DoanhTrai thuộc package com.arriety.models.map.doanhtrai, quản lý thông tin và logic của một doanh trại trong game, là nơi các thành viên bang hội tham gia để chiến đấu với quái. Lớp sử dụng annotation @Data từ Lombok để tự động tạo getter, setter, và các phương thức cơ bản. Doanh trại có các thuộc tính như ID, danh sách khu vực (zones), bang hội sở hữu, trạng thái mở, và thời gian mở. Lớp cung cấp các phương thức để mở doanh trại, thêm khu vực, khởi tạo quái, và gửi thông báo văn bản.
 */
@Data
public class DoanhTrai {
    /**
     * DOANH_TRAI: List<DoanhTrai> - Danh sách tĩnh chứa tất cả các doanh trại (150 doanh trại), được khởi tạo khi lớp được tải.
     */
    public static final List<DoanhTrai> DOANH_TRAI;

    /**
     * N_PLAYER_CLAN: int - Số lượng thành viên tối thiểu trong bang hội để mở doanh trại. Giá trị mặc định: 0.
     */
    public static final int N_PLAYER_CLAN = 0;

    /**
     * N_PLAYER_MAP: int - Số lượng người chơi cần đứng cùng khu vực để mở doanh trại. Giá trị mặc định: 0.
     */
    public static final int N_PLAYER_MAP = 0;

    /**
     * AVAILABLE: int - Số lượng doanh trại tối đa có thể tạo. Giá trị mặc định: 150.
     */
    public static final int AVAILABLE = 150;

    /**
     * TIME_DOANH_TRAI: int - Thời gian tồn tại của doanh trại (tính bằng mili-giây). Giá trị mặc định: 1.800.000 ms (30 phút).
     */
    public static final int TIME_DOANH_TRAI = 1800000;

    /**
     * Khối static:
     * - Mô tả: Khởi tạo danh sách DOANH_TRAI với 150 doanh trại khi lớp được tải, mỗi doanh trại có ID từ 0 đến 149.
     */
    static {
        DOANH_TRAI = new ArrayList<>();
        for (int i = 0; i < AVAILABLE; i++) {
            DOANH_TRAI.add(new DoanhTrai(i));
        }
    }

    /**
     * id: int - ID duy nhất của doanh trại, dùng để phân biệt các doanh trại.
     */
    private int id;

    /**
     * zones: List<Zone> - Danh sách các khu vực (Zone) thuộc doanh trại, chứa thông tin bản đồ và quái.
     */
    private List<Zone> zones;

    /**
     * clan: Clan - Bang hội sở hữu doanh trại, liên kết với doanh trại khi được mở.
     */
    private Clan clan;

    /**
     * isOpened: boolean - Trạng thái mở của doanh trại (true = đã mở, false = chưa mở).
     */
    private boolean isOpened;

    /**
     * lastTimeOpen: long - Thời gian gần nhất doanh trại được mở, dùng để theo dõi thời gian tồn tại.
     */
    private long lastTimeOpen;

    /**
     * Constructor:
     * - Mô tả: Khởi tạo đối tượng DoanhTrai với ID và danh sách khu vực rỗng.
     * - Thuộc tính:
     *   - id: int - ID của doanh trại.
     * - Trả về: Không trả về gì, tạo mới đối tượng DoanhTrai với id được gán và zones là danh sách rỗng.
     */
    public DoanhTrai(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    /**
     * addZone(Zone zone):
     * - Mô tả: Thêm một khu vực (Zone) vào danh sách zones của doanh trại.
     * - Thuộc tính:
     *   - zone: Zone - Khu vực cần thêm vào doanh trại.
     * - Trả về: Không trả về gì, chỉ thêm zone vào danh sách zones.
     */
    public void addZone(Zone zone) {
        this.zones.add(zone);
    }

    /**
     * getMapById(int mapId):
     * - Mô tả: Lấy khu vực (Zone) trong doanh trại dựa trên ID bản đồ.
     * - Thuộc tính:
     *   - mapId: int - ID của bản đồ cần tìm.
     * - Xử lý logic:
     *   - Duyệt qua danh sách zones, trả về Zone có mapId khớp.
     *   - Nếu không tìm thấy, trả về null.
     * - Trả về: Zone - Khu vực khớp với mapId, hoặc null nếu không tìm thấy.
     */
    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    /**
     * openDoanhTrai(Player player):
     * - Mô tả: Mở doanh trại cho bang hội của người chơi, khởi tạo thông tin và đưa thành viên bang hội vào bản đồ doanh trại.
     * - Thuộc tính:
     *   - player: Player - Người chơi mở doanh trại.
     * - Xử lý logic:
     *   - Ghi nhận thời gian mở (lastTimeOpen) và gán bang hội (clan) của người chơi.
     *   - Cập nhật thông tin doanh trại cho bang hội (doanhTrai, playerOpenDoanhTrai, lastTimeOpenDoanhTrai, timeOpenDoanhTrai).
     *   - Khởi tạo quái trong doanh trại bằng phương thức init().
     *   - Đưa các thành viên bang hội đang ở cùng khu vực với người chơi vào bản đồ doanh trại (map ID 53) và gửi văn bản thông báo.
     * - Trả về: Không trả về gì, thực hiện mở doanh trại và di chuyển người chơi.
     */
    public void openDoanhTrai(Player player) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        player.clan.doanhTrai = this;
        player.clan.playerOpenDoanhTrai = player.name;
        player.clan.lastTimeOpenDoanhTrai = this.lastTimeOpen;
        player.clan.timeOpenDoanhTrai = this.lastTimeOpen;
        this.init();
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null || !player.zone.equals(pl.zone)) {
                continue;
            }
            ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
            ItemTimeService.gI().sendTextDoanhTrai(pl);
        }
    }

    /**
     * init():
     * - Mô tả: Khởi tạo quái trong doanh trại, tính toán sức mạnh quái dựa trên tổng chỉ số của các thành viên bang hội.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Tính tổng sát thương (totalDame) và máu tối đa (totalHp) của các thành viên bang hội.
     *   - Với mỗi khu vực (Zone) trong doanh trại, cập nhật chỉ số quái:
     *     - Sát thương (dame) = totalHp / 20 (sử dụng Util.trum để làm tròn).
     *     - Máu tối đa (maxHp) = totalDame * 20 (sử dụng Util.trum để làm tròn).
     *     - Hồi sinh quái bằng phương thức hoiSinh().
     * - Trả về: Không trả về gì, chỉ khởi tạo quái trong các khu vực.
     */
    private void init() {
        long totalDame = 0;
        long totalHp = 0;
        for (Player pl : this.clan.membersInGame) {
            totalDame += pl.nPoint.dame;
            totalHp += pl.nPoint.hpMax;
        }
        for (Zone zone : this.zones) {
            for (Mob mob : zone.mobs) {
                mob.point.dame = Util.trum(totalHp / 20);
                mob.point.maxHp = Util.trum(totalDame * 20);
                mob.hoiSinh();
            }
        }
    }

    /**
     * sendTextDoanhTrai():
     * - Mô tả: Gửi văn bản thông báo về doanh trại tới tất cả thành viên bang hội trong game.
     * - Thuộc tính: Không có tham số đầu vào.
     * - Xử lý logic:
     *   - Duyệt qua danh sách thành viên bang hội (membersInGame), gọi ItemTimeService.gI().sendTextDoanhTrai() để gửi thông báo cho từng người chơi.
     * - Trả về: Không trả về gì, chỉ gửi thông báo văn bản tới người chơi.
     */
    private void sendTextDoanhTrai() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextDoanhTrai(pl);
        }
    }
}
