package com.girlkun.services;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.io.MySession;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Lớp ChatGlobalService quản lý chức năng chat toàn server trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất,
 * chạy một luồng nền để xử lý và gửi các tin nhắn chat từ danh sách chờ.
 * 
 * @author Lucifer
 */
public class ChatGlobalService implements Runnable {

    /**
     * Số lượng tin nhắn tối đa trong danh sách chat đang hiển thị.
     */
    private static int COUNT_CHAT = 50;

    /**
     * Số lượng tin nhắn tối đa trong danh sách chờ.
     */
    private static int COUNT_WAIT = 50;

    /**
     * Thể hiện duy nhất của lớp ChatGlobalService (singleton pattern).
     */
    private static ChatGlobalService i;

    /**
     * Danh sách các tin nhắn chat đang hiển thị.
     */
    private List<ChatGlobal> listChatting;

    /**
     * Danh sách các tin nhắn chat đang chờ gửi.
     */
    private List<ChatGlobal> waitingChat;

    /**
     * Khởi tạo một đối tượng ChatGlobalService và bắt đầu luồng xử lý chat.
     */
    private ChatGlobalService() {
        this.listChatting = new ArrayList<>();
        this.waitingChat = new LinkedList<>();
        new Thread(this, "**Chat global").start();
    }

    /**
     * Lấy thể hiện duy nhất của lớp ChatGlobalService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp ChatGlobalService.
     */
    public static ChatGlobalService gI() {
        if (i == null) {
            i = new ChatGlobalService();
        }
        return i;
    }

    /**
     * Xử lý yêu cầu chat toàn server từ một người chơi.
     * Kiểm tra điều kiện sức mạnh, số ngọc, thời gian chờ, và nội dung trùng lặp.
     * 
     * @param player Người chơi gửi tin nhắn.
     * @param text Nội dung tin nhắn.
     */
    public void chat(Player player, String text) {
        if (player.getSession().player.nPoint.power >= 2000000000L) {
            Service.gI().sendThongBaoFromAdmin(player,
                    "Cần đạt 2 tỉ sức mạnh để sử dụng tính năng này!");
            return;
        }
        if (waitingChat.size() >= COUNT_WAIT) {
            Service.gI().sendThongBao(player, "Kênh thế giới hiện đang quá tải, không thể chat lúc này");
            return;
        }
        boolean haveInChatting = false;
        for (ChatGlobal chat : listChatting) {
            if (chat.text.equals(text)) {
                haveInChatting = true;
                break;
            }
        }
        if (haveInChatting) {
            return;
        }

        if (player.inventory.gem >= 1000) {
            if (player.isAdmin() || Util.canDoWithTime(player.iDMark.getLastTimeChatGlobal(), 360000)) {
                if (player.isAdmin() || player.nPoint.power > 1000000000) {
                    player.inventory.subGemAndRubyAndTien(1000);
                    Service.gI().sendMoney(player);
                    player.iDMark.setLastTimeChatGlobal(System.currentTimeMillis());
                    waitingChat.add(new ChatGlobal(player, text.length() > 100 ? text.substring(0, 100) : text));
                } else {
                    Service.gI().sendThongBao(player, "Sức mạnh phải ít nhất 10tỉ sm mới có thể chat thế giới");
                }
            } else {
                Service.gI().sendThongBao(player, "Không thể chat thế giới lúc này, vui lòng đợi "
                        + TimeUtil.getTimeLeft(player.iDMark.getLastTimeChatGlobal(), 240));
            }
        } else {
            Service.gI().sendThongBao(player, "Không đủ ngọc chat thế giới");
        }
    }

    /**
     * Chạy luồng nền để quản lý và gửi tin nhắn chat toàn server.
     * Xóa tin nhắn đã hiển thị quá 1 giây và gửi tin nhắn từ danh sách chờ.
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (!listChatting.isEmpty()) {
                    ChatGlobal chat = listChatting.get(0);
                    if (Util.canDoWithTime(chat.timeSendToPlayer, 1000)) {
                        listChatting.remove(0).dispose();
                    }
                }

                if (!waitingChat.isEmpty()) {
                    ChatGlobal chat = waitingChat.get(0);
                    if (listChatting.size() < COUNT_CHAT) {
                        waitingChat.remove(0);
                        chat.timeSendToPlayer = System.currentTimeMillis();
                        listChatting.add(chat);
                        chatGlobal(chat);
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                Logger.logException(ChatGlobalService.class, e);
            }
        }
    }

    /**
     * Gửi tin nhắn chat toàn server tới tất cả người chơi sử dụng tin nhắn loại 92.
     * 
     * @param chat Đối tượng ChatGlobal chứa thông tin tin nhắn và người gửi.
     */
    private void chatGlobal(ChatGlobal chat) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(chat.playerName);
            msg.writer().writeUTF("|5|" + chat.text);
            msg.writer().writeInt((int) chat.playerId);
            msg.writer().writeShort(chat.head);
            msg.writer().writeShort(-1);
            msg.writer().writeShort(chat.body);
            msg.writer().writeShort(chat.bag); //bag
            msg.writer().writeShort(chat.leg);
            msg.writer().writeByte(0);
            Service.gI().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi tin nhắn
        }
    }

    /**
     * Biến đổi nội dung tin nhắn, thay thế các từ nhạy cảm bằng ký tự "***".
     * 
     * @param chat Đối tượng ChatGlobal chứa tin nhắn cần biến đổi.
     */
    private void transformText(ChatGlobal chat) {
        String text = chat.text;
        text = text.replaceAll("admin", "***")
                .replaceAll("địt", "***")
                .replaceAll("lồn", "***")
                .replaceAll("buồi", "***")
                .replaceAll("cc", "***")
                .replaceAll(".mobi", "***")
                .replaceAll(".online", "***")
                .replaceAll(".info", "***")
                .replaceAll(".tk", "***")
                .replaceAll(".ml", "***")
                .replaceAll(".ga", "***")
                .replaceAll(".gq", "***")
                .replaceAll(".io", "***")
                .replaceAll(".club", "***")
                .replaceAll("cltx", "***")
                .replaceAll("ôm cl", "***")
                .replaceAll("địt mẹ", "***")
                .replaceAll("như lồn", "***")
                .replaceAll("như cặc", "***")
                .replaceAll("sập", "***")
                .replaceAll("sv", "***");

        chat.text = text;
    }

    /**
     * Lớp nội tại ChatGlobal lưu trữ thông tin của một tin nhắn chat toàn server.
     */
    private class ChatGlobal {

        /**
         * Tên của người chơi gửi tin nhắn.
         */
        public String playerName;

        /**
         * ID của người chơi gửi tin nhắn.
         */
        public int playerId;

        /**
         * ID của đầu nhân vật.
         */
        public short head;

        /**
         * ID của thân nhân vật.
         */
        public short body;

        /**
         * ID của chân nhân vật.
         */
        public short leg;

        /**
         * ID của túi nhân vật.
         */
        public short bag;

        /**
         * Nội dung tin nhắn.
         */
        public String text;

        /**
         * Thời gian gửi tin nhắn tới người chơi.
         */
        public long timeSendToPlayer;

        /**
         * Khởi tạo một đối tượng ChatGlobal từ thông tin người chơi và nội dung tin nhắn.
         * 
         * @param player Người chơi gửi tin nhắn.
         * @param text Nội dung tin nhắn.
         */
        public ChatGlobal(Player player, String text) {
            this.playerName = player.name;
            this.playerId = (int) player.id;
            this.head = player.getHead();
            this.body = player.getBody();
            this.leg = player.getLeg();
            this.bag = player.getFlagBag();
            this.text = text;
            transformText(this);
        }

        /**
         * Giải phóng tài nguyên của đối tượng ChatGlobal.
         */
        private void dispose() {
            this.playerName = null;
            this.text = null;
        }
    }
}