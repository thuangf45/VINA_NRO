package com.girlkun.network.server;

import com.girlkun.network.session.ISession;
import com.girlkun.network.session.Session;
import com.girlkun.network.session.SessionFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp quản lý máy chủ mạng, xử lý việc khởi tạo, chạy và đóng máy chủ.
 * Chấp nhận các kết nối từ client, quản lý phiên làm việc và giới hạn kết nối theo địa chỉ IP.
 *
 * @author Lucifer
 */
public class GirlkunServer implements IGirlkunServer {

    /**
     * Thể hiện duy nhất của lớp GirlkunServer (singleton pattern).
     */
    private static GirlkunServer I;

    /**
     * Cổng mạng mà máy chủ lắng nghe các kết nối.
     */
    private int port;

    /**
     * Socket máy chủ dùng để chấp nhận các kết nối từ client.
     */
    private ServerSocket serverListen;

    /**
     * Lớp dùng để tạo bản sao của các phiên làm việc (session).
     */
    private Class sessionClone;

    /**
     * Trạng thái chạy của máy chủ.
     */
    private boolean start;

    /**
     * Cờ xác định xem có sử dụng khóa ngẫu nhiên cho phiên làm việc hay không.
     */
    private boolean randomKey;

    /**
     * Trình xử lý sự kiện khi máy chủ đóng.
     */
    private IServerClose serverClose;

    /**
     * Trình xử lý chấp nhận phiên làm việc mới.
     */
    private ISessionAcceptHandler acceptHandler;

    /**
     * Luồng chạy vòng lặp chính của máy chủ để xử lý kết nối.
     */
    private Thread loopServer;

    /**
     * Bản đồ lưu trữ số lượng kết nối từ từng địa chỉ IP.
     */
    public static HashMap<String, Integer> connections = new HashMap<>();

    /**
     * Lấy thể hiện duy nhất của GirlkunServer (singleton pattern).
     *
     * @return thể hiện GirlkunServer
     */
    public static GirlkunServer gI() {
        if (I == null) {
            I = new GirlkunServer();
        }
        return I;
    }

    /**
     * Khởi tạo GirlkunServer với các giá trị mặc định.
     */
    private GirlkunServer() {
        this.port = -1;
        this.sessionClone = Session.class;
    }

    /**
     * Khởi tạo máy chủ với một luồng xử lý chính.
     *
     * @return đối tượng IGirlkunServer
     */
    public IGirlkunServer init() {
        this.loopServer = new Thread(this);
        return this;
    }

    /**
     * Khởi động máy chủ trên một cổng cụ thể.
     *
     * @param port cổng mạng để máy chủ lắng nghe
     * @return đối tượng IGirlkunServer
     * @throws Exception nếu cổng không hợp lệ, trình xử lý chấp nhận chưa được khởi tạo, hoặc loại session clone không hợp lệ
     */
    public IGirlkunServer start(int port) throws Exception {
        if (port < 0) {
            throw new Exception("Vui lòng khởi tạo port server!");
        }
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        }
        if (!ISession.class.isAssignableFrom(this.sessionClone)) {
            throw new Exception("Type session clone không hợp lệ!");
        }
        try {
            this.port = port;
            this.serverListen = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("ERROR PORT " + port);
            System.exit(0);
        }
        this.start = true;
        this.loopServer.start();
        System.out.println("RUN SERVER PORT: " + this.port);
        return this;
    }

    /**
     * Đóng máy chủ và giải phóng tài nguyên liên quan.
     *
     * @return đối tượng IGirlkunServer
     */
    public IGirlkunServer close() {
        this.start = false;
        if (this.serverListen != null) {
            try {
                this.serverListen.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (this.serverClose != null) {
            this.serverClose.serverClose();
        }
        System.out.println("Server Girlkun đã đóng!");
        return this;
    }

    /**
     * Giải phóng tài nguyên của máy chủ, đặt các đối tượng liên quan về null.
     *
     * @return đối tượng IGirlkunServer
     */
    public IGirlkunServer dispose() {
        this.acceptHandler = null;
        this.loopServer = null;
        this.serverListen = null;
        return this;
    }

    /**
     * Thiết lập trình xử lý chấp nhận phiên làm việc.
     *
     * @param handler trình xử lý chấp nhận phiên làm việc
     * @return đối tượng IGirlkunServer
     */
    public IGirlkunServer setAcceptHandler(ISessionAcceptHandler handler) {
        this.acceptHandler = handler;
        return this;
    }

    /**
     * Chạy luồng chính của máy chủ, chấp nhận và xử lý các kết nối từ client.
     */
    public void run() {
        while (this.start) {
            try {
                Socket socket = this.serverListen.accept();
                String ipConnect = socket.getInetAddress().getHostAddress();
                if (ipConnect.equals("127.0.0.1") || (connections.containsKey(ipConnect) && connections.get(ipConnect).intValue() > 10000)) {
                    System.out.println("Từ chối " + ipConnect + " do vượt quá giới hạn kết nối");
                    socket.close();
                } else {
                    ISession session = SessionFactory.gI().cloneSession(this.sessionClone, socket);
                    this.acceptHandler.sessionInit(session);
                    GirlkunSessionManager.gI().putSession(session);
                    if (connections.containsKey(ipConnect)) {
                        int value = connections.get(ipConnect).intValue();
                        value++;
                        connections.put(ipConnect, value);
                    } else {
                        connections.put(ipConnect, 1);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                Logger.getLogger(GirlkunServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Thiết lập trình xử lý sự kiện khi máy chủ đóng.
     *
     * @param serverClose trình xử lý sự kiện đóng máy chủ
     * @return đối tượng IGirlkunServer
     */
    public IGirlkunServer setDoSomeThingWhenClose(IServerClose serverClose) {
        this.serverClose = serverClose;
        return this;
    }

    /**
     * Thiết lập chế độ sử dụng khóa ngẫu nhiên cho phiên làm việc.
     *
     * @param isRandom cờ xác định có sử dụng khóa ngẫu nhiên hay không
     * @return đối tượng IGirlkunServer
     */
    public IGirlkunServer randomKey(boolean isRandom) {
        this.randomKey = isRandom;
        return this;
    }

    /**
     * Kiểm tra xem máy chủ có sử dụng khóa ngẫu nhiên hay không.
     *
     * @return true nếu sử dụng khóa ngẫu nhiên, false nếu không
     */
    public boolean isRandomKey() {
        return this.randomKey;
    }

    /**
     * Thiết lập lớp dùng để tạo bản sao phiên làm việc.
     *
     * @param clazz lớp của phiên làm việc
     * @return đối tượng IGirlkunServer
     * @throws Exception nếu lớp không hợp lệ
     */
    public IGirlkunServer setTypeSessioClone(Class clazz) throws Exception {
        this.sessionClone = clazz;
        return this;
    }

    /**
     * Lấy trình xử lý chấp nhận phiên làm việc.
     *
     * @return trình xử lý chấp nhận phiên làm việc
     * @throws Exception nếu trình xử lý chưa được khởi tạo
     */
    public ISessionAcceptHandler getAcceptHandler() throws Exception {
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        }
        return this.acceptHandler;
    }

    /**
     * Dừng chấp nhận các kết nối mới bằng cách đặt trạng thái start thành false.
     */
    public void stopConnect() {
        this.start = false;
    }
}