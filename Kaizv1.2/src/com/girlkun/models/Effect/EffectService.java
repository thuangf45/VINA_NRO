package com.girlkun.models.Effect;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.Service;
import java.io.IOException;

/**
 * Lớp xử lý các chức năng liên quan đến hiệu ứng của nhân vật trong game.
 * @author Lucifer
 */
public class EffectService {

    /**
     * Thêm hiệu ứng cho nhân vật và gửi thông báo đến tất cả người chơi trong bản đồ.
     * @param pl Người chơi nhận hiệu ứng
     * @param idEff ID của hiệu ứng
     * @param layer Lớp hiển thị của hiệu ứng (0: sau nhân vật, 1: trước nhân vật)
     * @param loop Số lần lặp hiệu ứng (-1: lặp vô hạn)
     * @param loopCount Thời gian lặp của hiệu ứng
     * @param isStand Trạng thái đứng yên (0: không đứng yên, 1: đứng yên)
     */
    public static void AddEffecttoChar(Player pl, int idEff, int layer, int loop, int loopCount, int isStand) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0); // 0: thêm hiệu ứng
            msg.writer().writeInt((int) pl.id); // ID nhân vật
            msg.writer().writeShort((short) idEff); // ID hiệu ứng
            msg.writer().writeByte((byte) layer); // Lớp hiển thị
            msg.writer().writeByte((byte) loop); // Số lần lặp
            msg.writer().writeShort((short) loopCount); // Thời gian lặp
            msg.writer().writeByte((byte) isStand); // Trạng thái đứng yên
            Service.gI().sendMessAllPlayerInMap(pl, msg); // Gửi thông báo đến tất cả người chơi trong bản đồ
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 80\n");
            e.printStackTrace();
        }
    }

    /**
     * Xóa hiệu ứng của nhân vật và gửi thông báo đến tất cả người chơi trong bản đồ.
     * @param pl Người chơi cần xóa hiệu ứng
     * @param id ID của hiệu ứng cần xóa
     */
    public static void RemoveEffecttoChar(Player pl, short id) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(1); // 1: xóa hiệu ứng
            msg.writer().writeInt((int) pl.id); // ID nhân vật
            msg.writer().writeShort(id); // ID hiệu ứng
            Service.gI().sendMessAllPlayerInMap(pl, msg); // Gửi thông báo đến tất cả người chơi trong bản đồ
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 81\n");
            e.printStackTrace();
        }
    }

    /**
     * Xóa tất cả hiệu ứng của nhân vật và gửi thông báo đến tất cả người chơi trong bản đồ.
     * @param pl Người chơi cần xóa toàn bộ hiệu ứng
     */
    public static void RemoveAllEff(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(2); // 2: xóa tất cả hiệu ứng
            msg.writer().writeInt((int) pl.id); // ID nhân vật
            Service.gI().sendMessAllPlayerInMap(pl, msg); // Gửi thông báo đến tất cả người chơi trong bản đồ
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 82\n");
            e.printStackTrace();
        }
    }

    /**
     * Xóa một hiệu ứng cụ thể khỏi danh sách hiệu ứng của nhân vật.
     * @param pl Người chơi cần xóa hiệu ứng
     * @param ideff ID của hiệu ứng cần xóa
     */
    public static void RemoveEff(Player pl, int ideff) {
        for (EffectChar ef : pl.ListEffect) {
            if (ef.id == ideff) {
                pl.ListEffect.remove(ef); // Xóa hiệu ứng khỏi danh sách
                break;
            }
        }
    }

    /**
     * Gửi hiệu ứng của thú cưng đến người chơi trong bản đồ.
     * @param pl Người chơi sở hữu thú cưng
     */
    public static void SendEffPettoMap(Player pl) {
        if (pl != null && pl.pet != null) {
            try {
                Message msg = null;
                msg = new Message(-128);
                msg.writer().writeByte(4); // 4: gửi hiệu ứng thú cưng
                msg.writer().writeInt(pl.pet.idAura); // ID aura của thú cưng
                msg.writer().writeInt(pl.pet.idVongChan); // ID vòng chân 1
                msg.writer().writeInt(pl.pet.idVongChan2); // ID vòng chân 2
                pl.sendMessage(msg); // Gửi thông báo đến người chơi
                msg.cleanup();
            } catch (IOException e) {
                System.err.print("\nError at 83\n");
                e.printStackTrace();
            }
        }
    }

    /**
     * Gửi danh sách hiệu ứng và danh hiệu của nhân vật đến một người chơi cụ thể trong bản đồ.
     * @param pl Người chơi sở hữu hiệu ứng và danh hiệu
     * @param plrecieve Người chơi nhận thông tin hiệu ứng
     */
    public static void SendEffChartoMap(Player pl, Player plrecieve) {
        try {
            Message msg = null;
            msg = new Message(-128);
            msg.writer().writeByte(3); // 3: gửi danh sách hiệu ứng và danh hiệu
            msg.writer().writeInt((int) pl.id); // ID nhân vật
            msg.writer().writeInt((int) pl.ListEffect.size()); // Số lượng hiệu ứng
            for (int i = 0; i < pl.ListEffect.size(); i++) {
                msg.writer().writeShort((short) pl.ListEffect.get(i).id); // ID hiệu ứng
                msg.writer().writeByte((byte) pl.ListEffect.get(i).layer); // Lớp hiển thị
                msg.writer().writeByte((byte) pl.ListEffect.get(i).loop); // Số lần lặp
                msg.writer().writeShort((short) pl.ListEffect.get(i).loopCount); // Thời gian lặp
                msg.writer().writeByte((byte) pl.ListEffect.get(i).isStand); // Trạng thái đứng yên
            }
            msg.writer().writeInt(pl.ListDanhHieu.size()); // Số lượng danh hiệu
            for (int i = 0; i < pl.ListDanhHieu.size(); i++) {
                msg.writer().writeInt(DanhHieu.GetImgDanhHieu(pl.ListDanhHieu.get(i))); // ID hình ảnh danh hiệu
            }
            msg.writer().writeInt(pl.idAura); // ID aura
            msg.writer().writeInt(pl.idVongChan); // ID vòng chân 1
            msg.writer().writeInt(pl.idVongChan2); // ID vòng chân 2
            plrecieve.sendMessage(msg); // Gửi thông báo đến người chơi nhận
            msg.cleanup();
        } catch (IOException e) {
            System.err.print("\nError at 84\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi danh sách hiệu ứng và danh hiệu của nhân vật đến tất cả người chơi trong bản đồ.
     * @param pl Người chơi sở hữu hiệu ứng và danh hiệu
     */
    public static void SendEffChartoMap(Player pl) {
        try {
            Message msg = null;
            msg = new Message(-128);
            msg.writer().writeByte(3); // 3: gửi danh sách hiệu ứng và danh hiệu
            msg.writer().writeInt((int) pl.id); // ID nhân vật
            msg.writer().writeInt((int) pl.ListEffect.size()); // Số lượng hiệu ứng
            for (int i = 0; i < pl.ListEffect.size(); i++) {
                msg.writer().writeShort((short) pl.ListEffect.get(i).id); // ID hiệu ứng
                msg.writer().writeByte((byte) pl.ListEffect.get(i).layer); // Lớp hiển thị
                msg.writer().writeByte((byte) pl.ListEffect.get(i).loop); // Số lần lặp
                msg.writer().writeShort((short) pl.ListEffect.get(i).loopCount); // Thời gian lặp
                msg.writer().writeByte((byte) pl.ListEffect.get(i).isStand); // Trạng thái đứng yên
            }
            msg.writer().writeInt(pl.ListDanhHieu.size()); // Số lượng danh hiệu
            for (int i = 0; i < pl.ListDanhHieu.size(); i++) {
                msg.writer().writeInt(DanhHieu.GetImgDanhHieu(pl.ListDanhHieu.get(i))); // ID hình ảnh danh hiệu
            }
            msg.writer().writeInt(pl.idAura); // ID aura
            msg.writer().writeInt(pl.idVongChan); // ID vòng chân 1
            msg.writer().writeInt(pl.idVongChan2); // ID vòng chân 2
            Service.gI().sendMessAllPlayerInMap(pl, msg); // Gửi thông báo đến tất cả người chơi trong bản đồ
            msg.cleanup();
        } catch (IOException e) {
            System.err.print("\nError at 85\n");
            e.printStackTrace();
        }
    }
}