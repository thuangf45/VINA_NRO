package com.girlkun.server.io;

import java.net.Socket;
import com.girlkun.models.player.Player;
import com.girlkun.server.Controller;
import com.girlkun.network.io.Message;
import com.girlkun.data.DataGame;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.models.item.Item;
import com.girlkun.network.session.Session;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.model.AntiLogin;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp MySession đại diện cho một phiên kết nối của client với server.
 * Kế thừa từ lớp Session, quản lý thông tin người chơi, xử lý đăng nhập,
 * và khởi tạo các phần thưởng từ dữ liệu.
 * 
 * @author Lucifer
 */
public class MySession extends Session {

    /**
     * Danh sách ánh xạ các địa chỉ IP với thông tin chống đăng nhập (AntiLogin).
     */
    private static final Map<String, AntiLogin> ANTILOGIN = new HashMap<>();

    /**
     * Đối tượng người chơi liên kết với phiên kết nối này.
     */
    public Player player;

    /**
     * Thời gian chờ xử lý (tính bằng giây).
     */
    public byte timeWait = 1;

    /**
     * Trạng thái kết nối của phiên (true nếu đã kết nối).
     */
    public boolean connected;

    /**
     * Trạng thái đã gửi khóa cho client (true nếu đã gửi).
     */
    public boolean sentKey;

    /**
     * Mảng khóa dùng để mã hóa thông điệp.
     */
    public static final byte[] KEYS = {0};

    /**
     * Chỉ số đọc hiện tại trong mảng khóa.
     */
    public byte curR;

    /**
     * Chỉ số ghi hiện tại trong mảng khóa.
     */
    public byte curW;

    /**
     * Địa chỉ IP của client.
     */
    public String ipAddress;

    /**
     * Trạng thái quản trị viên (true nếu là admin).
     */
    public boolean isAdmin;

    /**
     * ID người dùng trong cơ sở dữ liệu.
     */
    public int userId;

    /**
     * Tên người dùng dùng để đăng nhập.
     */
    public String uu;

    /**
     * Mật khẩu dùng để đăng nhập.
     */
    public String pp;

    /**
     * Tổng số tiền nạp của người chơi.
     */
    public int TongNap;

    /**
     * Loại client (phiên bản hoặc thiết bị).
     */
    public int typeClient;

    /**
     * Mức độ thu phóng giao diện của client.
     */
    public byte zoomLevel;

    /**
     * Thời gian đăng xuất lần cuối.
     */
    public long lastTimeLogout;

    /**
     * Trạng thái đã tham gia game (true nếu đã vào game).
     */
    public boolean joinedGame;

    /**
     * Thời gian lần cuối đọc tin nhắn.
     */
    public long lastTimeReadMessage;

    /**
     * Trạng thái kích hoạt tài khoản.
     */
    public boolean actived;

    /**
     * Trạng thái mở tính năng mtvgtd.
     */
    public boolean mtvgtd;

    /**
     * Trạng thái VIP 1 ngày.
     */
    public boolean vip1d;

    /**
     * Trạng thái VIP 2 ngày.
     */
    public boolean vip2d;

    /**
     * Trạng thái VIP 3 ngày.
     */
    public boolean vip3d;

    /**
     * Trạng thái VIP 4 ngày.
     */
    public boolean vip4d;

    /**
     * Trạng thái VIP 5 ngày.
     */
    public boolean vip5d;

    /**
     * Trạng thái VIP 6 ngày.
     */
    public boolean vip6d;

    /**
     * Số lượng vàng (gold bar) của người chơi.
     */
    public int goldBar;

    /**
     * Số lượng vàng (vang) của người chơi.
     */
    public int vang;

    /**
     * Điểm VIP cấp 1.
     */
    public int vip1;

    /**
     * Điểm VIP cấp 2.
     */
    public int vip2;

    /**
     * Điểm VIP cấp 3.
     */
    public int vip3;

    /**
     * Điểm VIP cấp 4.
     */
    public int vip4;

    /**
     * Điểm VIP cấp 5.
     */
    public int vip5;

    /**
     * Điểm VIP cấp 6.
     */
    public int vip6;

    /**
     * Số lượng coin bar của người chơi.
     */
    public int coinBar;

    /**
     * Danh sách các vật phẩm phần thưởng.
     */
    public List<Item> itemsReward;

    /**
     * Dữ liệu phần thưởng dạng chuỗi.
     */
    public String dataReward;

    /**
     * Trạng thái hộp quà (true nếu có hộp quà).
     */
    public boolean is_gift_box;

    /**
     * Tỷ lệ sức mạnh của người chơi.
     */
    public double bdPlayer;

    /**
     * Phiên bản client.
     */
    public int version;

    /**
     * Số lượng coin của người chơi.
     */
    public int coin;

    /**
     * Số lượng tiền VND của người chơi.
     */
    public int vnd;

    /**
     * Điểm giới thiệu của người chơi.
     */
    public int gioithieu;

    /**
     * Số lượng bar của người chơi.
     */
    public int Bar;

    /**
     * Trạng thái hiển thị biểu tượng R (true nếu hiển thị).
     */
    public boolean isRIcon;

    /**
     * Khởi tạo một phiên kết nối mới với socket của client.
     * 
     * @param socket Socket kết nối tới client.
     */
    public MySession(Socket socket) {
        super(socket);
        ipAddress = socket.getInetAddress().getHostAddress();
        this.isRIcon = false;
    }

    /**
     * Khởi tạo danh sách phần thưởng từ dữ liệu chuỗi phần thưởng.
     * Phân tích dữ liệu để tạo các vật phẩm với tùy chọn tương ứng.
     */
    public void initItemsReward() {
        try {
            this.itemsReward = new ArrayList<>();
            String[] itemsReward = dataReward.split(";");
            for (String itemInfo : itemsReward) {
                if (itemInfo == null || itemInfo.equals("")) {
                    continue;
                }
                String[] subItemInfo = itemInfo.replaceAll("[{}\\[\\]]", "").split("\\|");
                String[] baseInfo = subItemInfo[0].split(":");
                int itemId = Integer.parseInt(baseInfo[0]);
                int quantity = Integer.parseInt(baseInfo[1]);
                Item item = ItemService.gI().createNewItem((short) itemId, quantity);
                if (subItemInfo.length == 2) {
                    String[] options = subItemInfo[1].split(",");
                    for (String opt : options) {
                        if (opt == null || opt.equals("")) {
                            continue;
                        }
                        String[] optInfo = opt.split(":");
                        int tempIdOption = Integer.parseInt(optInfo[0]);
                        int param = Integer.parseInt(optInfo[1]);
                        item.itemOptions.add(new Item.ItemOption(tempIdOption, param));
                    }
                }
                this.itemsReward.add(item);
            }
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn khởi tạo phần thưởng
        }
    }

    /**
     * Gửi khóa phiên và bắt đầu quá trình gửi dữ liệu tới client.
     * 
     * @throws Exception Nếu có lỗi khi gửi khóa.
     */
    @Override
    public void sendKey() throws Exception {
        super.sendKey();
        this.startSend();
    }

    /**
     * Gửi khóa phiên tới client sử dụng tin nhắn loại -27.
     */
    public void sendSessionKey() {
        Message msg = new Message(-27);
        try {
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; i++) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            this.sendMessage(msg);
            msg.cleanup();
            sentKey = true;
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi khóa
        }
    }

    /**
     * Xử lý đăng nhập của người chơi với tên người dùng và mật khẩu.
     * Kiểm tra giới hạn đăng nhập, trạng thái bảo trì, và số lượng người chơi tối đa.
     * 
     * @param username Tên người dùng.
     * @param password Mật khẩu.
     */
    public void login(String username, String password) {
        AntiLogin al = ANTILOGIN.get(this.ipAddress);
        if (al == null) {
            al = new AntiLogin();
            ANTILOGIN.put(this.ipAddress, al);
        }
        if (!al.canLogin()) {
            Service.gI().sendThongBaoOK(this, al.getNotifyCannotLogin());
            return;
        }
        if (Manager.LOCAL) {
            Service.gI().sendThongBaoOK(this, "Server này chỉ để lưu dữ liệu\nVui lòng qua server khác");
            return;
        }
        if (Maintenance.isRuning) {
            Service.gI().sendThongBaoOK(this, "Server đang trong thời gian bảo trì, vui lòng quay lại sau");
            return;
        }
        if (!this.isAdmin && Client.gI().getPlayers().size() >= Manager.MAX_PLAYER) {
            Service.gI().sendThongBaoOK(this, "Máy chủ hiện đang quá tải, "
                    + "cư dân vui lòng di chuyển sang máy chủ khác.");
            return;
        }
        if (this.player != null) {
            return;
        } else {
            Player player = null;
            try {
                long st = System.currentTimeMillis();
                this.uu = username;
                this.pp = password;

                player = GodGK.login(this, al);
                if (player != null) {
                    // -77 max small
                    DataGame.sendSmallVersion(this);
                    // -93 bgitem version
                    Service.gI().sendMessage(this, -93, "1630679752231_-93_r");

                    this.timeWait = 1;
                    this.joinedGame = true;
                    player.nPoint.calPoint();
                    player.nPoint.setHp(player.nPoint.hp);
                    player.nPoint.setMp(player.nPoint.mp);
                    player.zone.addPlayer(player);
                    if (player.pet != null) {
                        player.pet.nPoint.calPoint();
                        player.pet.nPoint.setHp(player.pet.nPoint.hp);
                        player.pet.nPoint.setMp(player.pet.nPoint.mp);
                    }

                    player.setSession(this);
                    Client.gI().put(player);
                    this.player = player;
                    //-28 -4 version data game
                    DataGame.sendVersionGame(this);
                    //-31 data item background
                    DataGame.sendDataItemBG(this);
                    Controller.getInstance().sendInfo(this);
                    Logger.log(Logger.BLACK, "Login Player: " + this.player.name + " Go Server\n");

                    Service.gI().sendThongBao(player, "Login server thành công ");
                }
            } catch (Exception e) {
                if (player != null) {
                    player.dispose();
                }
                // Bỏ qua lỗi để tránh gián đoạn quá trình đăng nhập
            }
        }
    }
}