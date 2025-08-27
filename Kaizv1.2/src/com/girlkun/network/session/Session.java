package com.girlkun.network.session;

import com.girlkun.network.handler.IKeySessionHandler;
import com.girlkun.network.handler.IMessageHandler;
import com.girlkun.network.handler.IMessageSendCollect;
import com.girlkun.network.io.Collector;
import com.girlkun.network.io.Message;
import com.girlkun.network.io.Sender;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.utils.StringUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.girlkun.models.player.Player;
import java.net.InetAddress;

/**
 * Lớp thực hiện giao diện ISession, quản lý phiên làm việc mạng, bao gồm gửi/nhận thông điệp,
 * xử lý khóa mã hóa, và quản lý kết nối với client hoặc server.
 *
 * @author Lucifer
 */
public class Session implements ISession {

    /**
     * Thể hiện duy nhất của lớp Session (singleton pattern).
     */
    private static ISession I;

    /**
     * Biến đếm để tạo ID duy nhất cho các phiên làm việc.
     */
    private static int ID_INIT;

    /**
     * Loại phiên làm việc (client hoặc server).
     */
    public TypeSession typeSession;

    /**
     * Thời gian chờ tối đa cho các thao tác mạng (đơn vị không xác định).
     */
    public byte timeWait = 50;

    /**
     * Khóa mã hóa dùng cho phiên làm việc.
     */
    private byte[] KEYS = "Girlkun75".getBytes();

    /**
     * Trạng thái xác định xem khóa đã được gửi hay chưa.
     */
    private boolean sentKey;

    /**
     * ID duy nhất của phiên làm việc.
     */
    public int id;

    /**
     * Socket kết nối với client hoặc server.
     */
    private Socket socket;

    /**
     * Trạng thái kết nối của phiên làm việc.
     */
    private boolean connected;

    /**
     * Cờ xác định xem phiên làm việc có được phép kết nối lại hay không.
     */
    private boolean reconnect;

    /**
     * Đối tượng xử lý gửi thông điệp.
     */
    private Sender sender;

    /**
     * Đối tượng xử lý thu thập thông điệp.
     */
    private Collector collector;

    /**
     * Luồng xử lý gửi thông điệp.
     */
    private Thread tSender;

    /**
     * Luồng xử lý thu thập thông điệp.
     */
    private Thread tCollector;

    /**
     * Đối tượng xử lý khóa mã hóa cho phiên làm việc.
     */
    private IKeySessionHandler keyHandler;

    /**
     * Địa chỉ IP của client kết nối.
     */
    private String ip;

    /**
     * Địa chỉ host của server (dùng cho client).
     */
    private String host;

    /**
     * Cổng mạng của server (dùng cho client).
     */
    private int port;

    /**
     * Lấy thể hiện duy nhất của Session (singleton pattern).
     *
     * @return thể hiện ISession
     * @throws Exception nếu instance chưa được khởi tạo
     */
    public static ISession gI() throws Exception {
        if (I == null) {
            throw new Exception("Instance chưa được khởi tạo!");
        }
        return I;
    }

    /**
     * Khởi tạo thể hiện Session với host và cổng mạng.
     *
     * @param host địa chỉ server
     * @param port cổng mạng
     * @return thể hiện ISession
     * @throws Exception nếu instance đã được khởi tạo
     */
    public static ISession initInstance(String host, int port) throws Exception {
        if (I != null) {
            throw new Exception("Instance đã được khởi tạo!");
        }
        I = new Session(host, port);
        return I;
    }

    /**
     * Khởi tạo Session với host và cổng mạng cho client.
     *
     * @param host địa chỉ server
     * @param port cổng mạng
     * @throws IOException nếu xảy ra lỗi khi tạo socket
     */
    public Session(String host, int port) throws IOException {
        this.id = 752002;
        this.socket = new Socket(host, port);
        this.socket.setSendBufferSize(1048576);
        this.socket.setReceiveBufferSize(1048576);
        this.typeSession = TypeSession.CLIENT;
        this.connected = true;
        this.host = host;
        this.port = port;
        initThreadSession();
    }

    /**
     * Khởi tạo Session với socket cho server.
     *
     * @param socket socket kết nối với client
     */
    public Session(Socket socket) {
        this.id = ID_INIT++;
        this.typeSession = TypeSession.SERVER;
        this.socket = socket;
        try {
            this.socket.setSendBufferSize(1048576);
            this.socket.setReceiveBufferSize(1048576);
        } catch (Exception exception) {}
        this.connected = true;
        this.ip = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        initThreadSession();
    }

    /**
     * Gửi một thông điệp qua phiên làm việc nếu đang kết nối và số lượng thông điệp chờ không vượt quá giới hạn.
     *
     * @param msg thông điệp cần gửi
     */
    public void sendMessage(Message msg) {
        if (this.sender != null && isConnected() && this.sender.getNumMessages() < 200) {
            this.sender.sendMessage(msg);
        }
    }

    /**
     * Thiết lập đối tượng xử lý gửi và thu thập thông điệp.
     *
     * @param collect đối tượng xử lý gửi và thu thập thông điệp
     * @return đối tượng ISession
     */
    public ISession setSendCollect(IMessageSendCollect collect) {
        this.sender.setSend(collect);
        this.collector.setCollect(collect);
        return this;
    }

    /**
     * Thiết lập đối tượng xử lý thông điệp nhận được.
     *
     * @param handler đối tượng xử lý thông điệp
     * @return đối tượng ISession
     */
    public ISession setMessageHandler(IMessageHandler handler) {
        this.collector.setMessageHandler(handler);
        return this;
    }

    /**
     * Thiết lập đối tượng xử lý khóa mã hóa cho phiên làm việc.
     *
     * @param handler đối tượng xử lý khóa phiên làm việc
     * @return đối tượng ISession
     */
    public ISession setKeyHandler(IKeySessionHandler handler) {
        this.keyHandler = handler;
        return this;
    }

    /**
     * Bắt đầu luồng gửi thông điệp.
     *
     * @return đối tượng ISession
     */
    public ISession startSend() {
        this.tSender.start();
        return this;
    }

    /**
     * Bắt đầu luồng thu thập thông điệp.
     *
     * @return đối tượng ISession
     */
    public ISession startCollect() {
        this.tCollector.start();
        return this;
    }

    /**
     * Lấy địa chỉ IP của client kết nối.
     *
     * @return địa chỉ IP dưới dạng chuỗi
     */
    public String getIP() {
        return this.ip;
    }

    /**
     * Lấy ID duy nhất của phiên làm việc.
     *
     * @return ID của phiên làm việc
     */
    public long getID() {
        return this.id;
    }

    /**
     * Ngắt kết nối phiên làm việc và xử lý kết nối lại nếu được phép.
     */
    public void disconnect() {
        this.connected = false;
        this.sentKey = false;
        if (this.sender != null) {
            this.sender.close();
        }
        if (this.collector != null) {
            this.collector.close();
        }
        if (socket != null) {
            try {
                socket.close();
                GirlkunServer.connections.remove(socket.getInetAddress().getHostAddress());
            } catch (IOException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (this.reconnect) {
            reconnect();
            return;
        }
        dispose();
    }

    /**
     * Giải phóng tài nguyên của phiên làm việc.
     */
    public void dispose() {
        if (this.sender != null) {
            this.sender.dispose();
        }
        if (this.collector != null) {
            this.collector.dispose();
        }
        this.socket = null;
        this.sender = null;
        this.collector = null;
        this.tSender = null;
        this.tCollector = null;
        this.ip = null;
        GirlkunSessionManager.gI().removeSession(this);
    }

    /**
     * Gửi khóa mã hóa cho phiên làm việc.
     *
     * @throws Exception nếu keyHandler chưa được khởi tạo hoặc xảy ra lỗi khi gửi
     */
    public void sendKey() throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler chưa được khởi tạo!");
        }
        if (GirlkunServer.gI().isRandomKey()) {
            this.KEYS = StringUtil.randomText(7).getBytes();
        }
        this.keyHandler.sendKey(this);
    }

    /**
     * Thiết lập khóa mã hóa từ một thông điệp.
     *
     * @param message thông điệp chứa dữ liệu khóa
     * @throws Exception nếu keyHandler chưa được khởi tạo hoặc xảy ra lỗi
     */
    public void setKey(Message message) throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler chưa được khởi tạo!");
        }
        this.keyHandler.setKey(this, message);
    }

    /**
     * Thiết lập khóa mã hóa từ một mảng byte.
     *
     * @param key mảng byte chứa dữ liệu khóa
     */
    public void setKey(byte[] key) {
        this.KEYS = key;
    }

    /**
     * Kiểm tra xem khóa đã được gửi hay chưa.
     *
     * @return true nếu khóa đã được gửi, false nếu chưa
     */
    public boolean sentKey() {
        return this.sentKey;
    }

    /**
     * Thiết lập trạng thái gửi khóa.
     *
     * @param sent true nếu khóa đã được gửi, false nếu chưa
     */
    public void setSentKey(boolean sent) {
        this.sentKey = sent;
    }

    /**
     * Thực hiện gửi một thông điệp qua phiên làm việc.
     *
     * @param msg thông điệp cần gửi
     * @throws Exception nếu xảy ra lỗi trong quá trình gửi
     */
    public void doSendMessage(Message msg) throws Exception {
        this.sender.doSendMessage(msg);
    }

    /**
     * Bắt đầu cả hai luồng gửi và thu thập thông điệp.
     *
     * @return đối tượng ISession
     */
    public ISession start() {
        this.tSender.start();
        this.tCollector.start();
        return this;
    }

    /**
     * Kiểm tra xem phiên làm việc có đang kết nối hay không.
     *
     * @return true nếu phiên làm việc đang kết nối, false nếu không
     */
    public boolean isConnected() {
        return this != null && this.connected;
    }

    /**
     * Lấy khóa mã hóa hiện tại.
     *
     * @return mảng byte chứa dữ liệu khóa
     */
    public byte[] getKey() {
        return this.KEYS;
    }

    /**
     * Lấy loại phiên làm việc hiện tại.
     *
     * @return loại phiên làm việc
     */
    public TypeSession getTypeSession() {
        return this.typeSession;
    }

    /**
     * Thiết lập khả năng kết nối lại cho phiên làm việc.
     *
     * @param b true nếu cho phép kết nối lại, false nếu không
     * @return đối tượng ISession
     */
    public ISession setReconnect(boolean b) {
        this.reconnect = b;
        return this;
    }

    /**
     * Lấy số lượng thông điệp đang chờ xử lý trong phiên làm việc.
     *
     * @return số lượng thông điệp, hoặc -1 nếu không kết nối
     */
    public int getNumMessages() {
        if (isConnected()) {
            return this.sender.getNumMessages();
        }
        return -1;
    }

    /**
     * Thực hiện kết nối lại nếu phiên làm việc là client và đang không kết nối.
     */
    public void reconnect() {
        if (this.typeSession == TypeSession.CLIENT && !isConnected()) {
            try {
                this.socket = new Socket(this.host, this.port);
                this.connected = true;
                initThreadSession();
                start();
            } catch (Exception e) {
                try {
                    Thread.sleep(1000L);
                    reconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Khởi tạo các luồng gửi và thu thập thông điệp cho phiên làm việc.
     */
    public void initThreadSession() {
        this.tSender = new Thread((this.sender != null) ? (Runnable) this.sender.setSocket(this.socket) : (Runnable) (this.sender = new Sender(this, this.socket)), "Thread tsender");
        this.tCollector = new Thread((this.collector != null) ? (Runnable) this.collector.setSocket(this.socket) : (Runnable) (this.collector = new Collector(this, this.socket)), "Thread collecter");
        // Lấy địa chỉ IP của socket
        InetAddress ipAddress = this.socket.getInetAddress();
        String ip = ipAddress.getHostAddress();
        System.out.println("Player : " + ip);
    }
}