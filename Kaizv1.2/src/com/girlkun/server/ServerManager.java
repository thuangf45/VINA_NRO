package com.girlkun.server;

import com.girlkun.database.GirlkunDB;
import java.net.ServerSocket;
import com.arriety.kygui.ShopKyGuiManager;
import com.arriety.kygui.ShopKyGuiService;
import com.girlkun.models.ThanhTich.CheckDataDay;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.girlkun.models.map.challenge.MartialCongressManager;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.player.Player;
import com.girlkun.models.player.Playerao;
import com.girlkun.network.session.ISession;
import com.girlkun.network.example.MessageSendCollect;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.server.IServerClose;
import com.girlkun.network.server.ISessionAcceptHandler;
import static com.girlkun.server.Maintenance.isBaoTri;
import com.girlkun.server.io.MyKeyHandler;
import com.girlkun.server.io.MySession;
import com.girlkun.services.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import com.girlkun.services.func.TaiXiu;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Lớp ServerManager quản lý toàn bộ hoạt động của server game, bao gồm khởi tạo, chạy server,
 * quản lý kết nối client, và xử lý các lệnh quản trị từ giao diện dòng lệnh.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class ServerManager {

    /**
     * Số lượng luồng bản đồ đang chạy.
     */
    public int threadMap;

    /**
     * Thời gian khởi động server, định dạng "dd/MM/yyyy HH:mm:ss".
     */
    public static String timeStart;

    /**
     * Danh sách các client đang kết nối, ánh xạ địa chỉ IP với số lượng kết nối.
     */
    public static final Map CLIENTS = new HashMap();

    /**
     * Tên của server.
     */
    public static String NAME = "Girlkun75";

    /**
     * Cổng kết nối của server.
     */
    public static int PORT = 14445;

    /**
     * Thể hiện duy nhất của lớp ServerManager (singleton pattern).
     */
    private static ServerManager instance;

    /**
     * Socket lắng nghe kết nối từ client.
     */
    public static ServerSocket listenSocket;

    /**
     * Trạng thái hoạt động của server (true nếu đang chạy, false nếu dừng).
     */
    public static boolean isRunning;

    /**
     * Khởi tạo server, tải dữ liệu từ Manager và đặt lại thời gian đăng nhập/đăng xuất
     * trong cơ sở dữ liệu nếu không chạy ở chế độ local.
     */
    public void init() {
        Manager.gI();
        try {
            if (Manager.LOCAL) {
                return;
            }
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
            System.err.print("\nError at 310\n");
            e.printStackTrace();
        }
    }

    /**
     * Lấy thể hiện duy nhất của lớp ServerManager.
     * Nếu chưa có, tạo mới một thể hiện và gọi hàm khởi tạo.
     * 
     * @return Thể hiện của lớp ServerManager.
     */
    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    /**
     * Phương thức chính để khởi động server.
     * Khởi tạo thời gian bắt đầu và chạy server.
     * 
     * @param args Tham số dòng lệnh (không sử dụng).
     */
    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager serverManager = ServerManager.gI();
        serverManager.run();
        // Tạo và chạy player ảo
        // Playerao.createVirtualPlayers(1); // Thay đổi số lượng player ảo tùy thích
    }

    /**
     * Chạy server, khởi tạo giao diện quản trị, kích hoạt dòng lệnh,
     * socket server, và các luồng cập nhật game.
     */
    public void run() {
        JFrame frame = new JFrame("Menu server manger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new Panel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        activeCommandLine();
        activeGame();
        activeServerSocket();
        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    long timeUpdate = System.currentTimeMillis() - start;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update dai hoi vo thuat").start();
        try {
            Thread.sleep(3000);
            BossManager.gI().loadBoss();
            Manager.MAPS.forEach(com.girlkun.models.map.Map::initBoss);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Kích hoạt socket server để chấp nhận kết nối từ client.
     * Cấu hình xử lý phiên kết nối và đóng server.
     * 
     * @throws Exception Nếu có lỗi khi khởi tạo socket server.
     */
    private void act() throws Exception {
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(ISession is) {
                if (!canConnectWithIp(is.getIP())) {
                    is.disconnect();
                    return;
                }
                is = is.setMessageHandler(Controller.getInstance())
                        .setSendCollect(new MessageSendCollect())
                        .setKeyHandler(new MyKeyHandler())
                        .startCollect();
            }

            @Override
            public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
            }
        }).setTypeSessioClone(MySession.class)
                .setDoSomeThingWhenClose(new IServerClose() {
                    @Override
                    public void serverClose() {
                        System.out.println("server close");
                        System.exit(0);
                    }
                })
                .start(PORT);
    }

    /**
     * Kích hoạt socket server để lắng nghe kết nối từ client.
     */
    private void activeServerSocket() {
        if (true) {
            try {
                this.act();
            } catch (Exception e) {
                // Bỏ qua lỗi để tiếp tục chạy server
            }
            return;
        }
    }

    /**
     * Kiểm tra xem một địa chỉ IP có thể kết nối tới server hay không.
     * Giới hạn số lượng kết nối tối đa từ một IP theo cấu hình.
     * 
     * @param ipAddress Địa chỉ IP của client.
     * @return true nếu IP được phép kết nối, false nếu vượt quá giới hạn.
     */
    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Ngắt kết nối của một phiên client và cập nhật số lượng kết nối từ IP tương ứng.
     * 
     * @param session Phiên kết nối của client.
     */
    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    /**
     * Kích hoạt giao diện dòng lệnh để xử lý các lệnh quản trị server.
     * Hỗ trợ các lệnh như bảo trì, lưu dữ liệu, gửi thông báo, và tặng quà cho người chơi.
     */
    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("savekigui")) {
                    // ShopKyGuiManager.gI().save();
                }
                if (line.equals("baotri")) {
                    Maintenance.gI().start(0);
                }
                if (line.equals("lavie")) {
                    ClanService.gI().saveclan();
                } else if (line.equals("online")) {
                    List<String> lines = new ArrayList<>();
                    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                    ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds());
                    System.out.println("Danh sách tên các luồng đang chạy:");
                    lines.add("Danh sách tên các luồng đang chạy:");
                    for (ThreadInfo threadInfo : threadInfos) {
                        lines.add(threadInfo.getThreadName());
                        System.out.println(threadInfo.getThreadName());
                    }
                    Path file = Paths.get("DataThread.txt");
                    try {
                        Files.write(file, lines, StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Logger.log(Logger.PURPLE, "Thread" + (Thread.activeCount() - threadMap) + "\nOnline:" + Client.gI().getPlayers().size());
                } else if (line.equals("nplayer")) {
                    Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }, "adminThread").start();
                } else if (line.startsWith("bang")) {
                    new Thread(() -> {
                        try {
                            ClanService.gI().close();
                            Logger.error("Save " + Manager.CLANS.size() + " bang");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
                        }
                    }, "bangThread").start();
                } else if (line.startsWith("a")) {
                    String a = line.replace("a ", "");
                    Service.gI().sendThongBaoAllPlayer(a);
                } else if (line.startsWith("tb")) {
                    String a = line.replace("tb ", "");
                    Service.gI().sendBangThongBaoAllPlayervip(a);
                } else if (line.startsWith("qua")) {
                    try {
                        List<Item.ItemOption> ios = new ArrayList<>();
                        String[] pagram1 = line.split("=")[1].split("-");
                        String[] pagram2 = line.split("=")[2].split("-");
                        if (pagram1.length == 4 && pagram2.length % 2 == 0) {
                            Player p = Client.gI().getPlayer(Integer.parseInt(pagram1[0]));
                            if (p != null) {
                                for (int i = 0; i < pagram2.length; i += 2) {
                                    ios.add(new Item.ItemOption(Integer.parseInt(pagram2[i]), Integer.parseInt(pagram2[i + 1])));
                                }
                                Item i = Util.sendDo(Integer.parseInt(pagram1[2]), Integer.parseInt(pagram1[3]), ios);
                                i.quantity = Integer.parseInt(pagram1[1]);
                                InventoryServiceNew.gI().addItemBag(p, i);
                                InventoryServiceNew.gI().sendItemBags(p);
                                Service.gI().sendThongBao(p, "Admin trả đồ. anh em thông cảm nhé...");
                            } else {
                                System.out.println("Người chơi không online");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Lỗi quà");
                    }
                }
            }
        }, "activeCommandLineThread").start();
    }

    /**
     * Kích hoạt luồng cập nhật game, bao gồm cập nhật bản đồ kho báu, đại hội võ thuật,
     * lưu dữ liệu bang hội, người chơi, và cửa hàng ký gửi.
     */
    private void activeGame() {
        final long delay = 8000; // Thời gian delay là 8000ms (8 giây)

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                while (!isBaoTri) {
                    for (BanDoKhoBau bando : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                        bando.update();
                    }
                    MartialCongressManager.gI().update();
                    ClanService.gI().saveclan();
                    Service.gI().AutoSavePlayerData();
                    ShopKyGuiManager.gI().save();
                    ShopKyGuiService.update();
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("\nThread interrupted\n");
            } catch (Exception e) {
                System.err.print("\nError at 314\n");
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }

    /**
     * Đóng server và lưu toàn bộ dữ liệu trước khi thoát.
     * 
     * @param delay Thời gian chờ (miligiây) trước khi đóng server.
     */
    public void close(long delay) {
        isRunning = false;
        try {
            GirlkunServer.gI().stopConnect();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: Lỗi Đóng kết nối tới server.\n");
        }
        Logger.log(Logger.BLACK, "\nĐóng kết nối tới server.\n");
        try {
            Client.gI().close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: Lỗi lưu dử liệu người chơi.\n");
        }
        try {
            ClanService.gI().close();
            Logger.log(Logger.BLACK, "Lưu dử liệu bang hội\n");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
        }
        try {
            ShopKyGuiManager.gI().save();
            Logger.log(Logger.BLACK, "Lưu dử liệu ký gửi\n");
        } catch (InterruptedException ex) {
            System.err.print("\nError at 315\n");
            ex.printStackTrace();
        }
        try {
            CheckDataDay.ResetDataDay();
            Logger.log(Logger.BLACK,
                    "Reset Dữ Liệu Hoạt Động Hằng Ngày - Quà Nạp Hằng Ngày.\n");
        } catch (SQLException ex) {
            System.err.print("\nError at 316\n");
            ex.printStackTrace();
        }
        Logger.log(Logger.BLACK, "Bảo trì đóng server thành công.\n");
        System.exit(0);
    }
}