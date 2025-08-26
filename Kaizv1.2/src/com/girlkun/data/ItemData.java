package com.girlkun.data;

import com.girlkun.models.Template.ItemOptionTemplate;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.server.io.MySession;

/**
 * Quản lý cập nhật dữ liệu vật phẩm cho client, bao gồm các mẫu vật phẩm và tùy chọn vật phẩm.
 * @author Lucifer
 */
public class ItemData {

    /**
     * Cập nhật thông tin vật phẩm cho phiên client.
     * @param session Phiên làm việc của client
     */
    public static void updateItem(MySession session) {
        updateItemOptionItemplate(session);
        updateItemTemplate(session, 750);
        updateItemTemplate(session, 750, Manager.ITEM_TEMPLATES.size());
    }

    /**
     * Cập nhật mẫu tùy chọn vật phẩm cho phiên client.
     * @param session Phiên làm việc của client
     */
    private static void updateItemOptionItemplate(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); // Phiên bản vật phẩm
            msg.writer().writeByte(0); // Cập nhật tùy chọn
            msg.writer().writeByte(Manager.ITEM_OPTION_TEMPLATES.size());
            for (ItemOptionTemplate io : Manager.ITEM_OPTION_TEMPLATES) {
                msg.writer().writeUTF(io.name);
                msg.writer().writeByte(io.type);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Cập nhật mẫu vật phẩm cho phiên client với số lượng giới hạn.
     * @param session Phiên làm việc của client
     * @param count Số lượng mẫu vật phẩm cần gửi
     */
    private static void updateItemTemplate(MySession session, int count) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); // Phiên bản vật phẩm
            msg.writer().writeByte(1); // Tải lại mẫu vật phẩm
            msg.writer().writeShort(count);
            for (int i = 0; i < count; i++) {
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Cập nhật mẫu vật phẩm cho phiên client trong một khoảng xác định.
     * @param session Phiên làm việc của client
     * @param start Vị trí bắt đầu của danh sách mẫu vật phẩm
     * @param end Vị trí kết thúc của danh sách mẫu vật phẩm
     */
    private static void updateItemTemplate(MySession session, int start, int end) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); // Phiên bản vật phẩm
            msg.writer().writeByte(2); // Thêm mẫu vật phẩm
            msg.writer().writeShort(start);
            msg.writer().writeShort(end);
            for (int i = start; i < end; i++) {
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}