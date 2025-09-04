package com.girlkun.services;

import com.girlkun.models.Template.FlagBag;
import java.util.List;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import java.util.ArrayList;

/**
 * Lớp FlagBagService quản lý các chức năng liên quan đến cờ (flag) trong game, bao gồm gửi biểu tượng cờ,
 * hiệu ứng cờ và danh sách cờ cho bang hội.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * 
 * @author Lucifer
 */
public class FlagBagService {

    /** Danh sách các cờ dùng cho bang hội */
    private List<FlagBag> flagClan = new ArrayList<>();
    /** Thể hiện duy nhất của lớp FlagBagService (singleton pattern) */
    private static FlagBagService i;

    /**
     * Lấy thể hiện duy nhất của lớp FlagBagService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp FlagBagService.
     */
    public static FlagBagService gI() {
        if (i == null) {
            i = new FlagBagService();
        }
        return i;
    }

    /**
     * Gửi biểu tượng cờ được chọn cho người chơi.
     * 
     * @param player Người chơi nhận thông tin cờ.
     * @param id ID của cờ được chọn.
     */
    public void sendIconFlagChoose(Player player, int id) {
        FlagBag fb = getFlagBag(id);
        if (fb != null) {
            Message msg;
            try {
                msg = new Message(-62);
                msg.writer().writeByte(fb.id);
                msg.writer().writeByte(1);
                msg.writer().writeShort(fb.iconId);

                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                // Bỏ qua lỗi để tránh gián đoạn gửi thông tin
            }
        }
    }

    /**
     * Gửi hiệu ứng của cờ cho người chơi.
     * 
     * @param player Người chơi nhận thông tin hiệu ứng cờ.
     * @param id ID của cờ có hiệu ứng.
     */
    public void sendIconEffectFlag(Player player, int id) {
        FlagBag fb = getFlagBag(id);
        if (fb != null) {
            Message msg;
            try {
                msg = new Message(-63);
                msg.writer().writeByte(fb.id);
                msg.writer().writeByte(fb.iconEffect.length);
                for (Short iconId : fb.iconEffect) {
                    msg.writer().writeShort(iconId);
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                // Bỏ qua lỗi để tránh gián đoạn gửi thông tin
            }
        }
    }

    /**
     * Gửi danh sách cờ có thể chọn cho bang hội tới người chơi.
     * 
     * @param pl Người chơi nhận danh sách cờ.
     */
    public void sendListFlagClan(Player pl) {
        List<FlagBag> list = getFlagsForChooseClan();
        Message msg;
        try {
            msg = new Message(-46);
            msg.writer().writeByte(1); // type
            msg.writer().writeByte(list.size());
            for (FlagBag fb : list) {
                msg.writer().writeByte(fb.id);
                msg.writer().writeUTF(fb.name);
                msg.writer().writeInt(fb.gold);
                msg.writer().writeInt(fb.gem);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // Bỏ qua lỗi để tránh gián đoạn gửi thông tin
        }
    }

    /**
     * Lấy thông tin cờ dựa trên ID.
     * 
     * @param id ID của cờ cần tìm.
     * @return Đối tượng FlagBag nếu tìm thấy, ngược lại trả về null.
     */
    public FlagBag getFlagBag(int id) {
        for (FlagBag fb : Manager.FLAGS_BAGS) {
            if (fb.id == id) {
                return fb;
            }
        }
        return null;
    }

    /**
     * Lấy danh sách cờ dùng cho bang hội.
     * Nếu danh sách chưa được khởi tạo, tạo mới danh sách với các ID cờ cố định.
     * 
     * @return Danh sách các cờ dùng cho bang hội.
     */
    public List<FlagBag> getFlagsForChooseClan() {
        if (flagClan.isEmpty()) {
            int[] flagsId = {0, 8, 7, 6, 5, 4, 3, 2, 1, 18, 17, 16, 15, 14, 13,
                12, 11, 10, 9, 27, 26, 25, 24, 23, 19, 22, 21, 20, 29, 67, 72
            };
            for (int i = 0; i < flagsId.length; i++) {
                flagClan.add(getFlagBag(flagsId[i]));
            }
        }
        return flagClan;
    }
}