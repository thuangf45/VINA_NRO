package com.girlkun.models.npc;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

/**
 * Lớp trừu tượng quản lý các NPC trong game, triển khai giao diện IAtionNpc
 * @author Lucifer
 */
public abstract class Npc implements IAtionNpc {

    /** ID của bản đồ chứa NPC */
    public int mapId;
    /** Đối tượng bản đồ chứa NPC */
    public Map map;
    /** Trạng thái của NPC */
    public int status;
    /** Tọa độ X của NPC */
    public int cx;
    /** Tọa độ Y của NPC */
    public int cy;
    /** ID mẫu của NPC */
    public int tempId;
    /** Hình ảnh đại diện của NPC */
    public int avartar;
    /** Menu cơ bản của NPC */
    public BaseMenu baseMenu;

    /** Constructor khởi tạo NPC với các thông tin cơ bản */
    protected Npc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        /** Gán bản đồ dựa trên ID */
        this.map = MapService.gI().getMapById(mapId);
        /** Gán ID bản đồ */
        this.mapId = mapId;
        /** Gán trạng thái */
        this.status = status;
        /** Gán tọa độ X */
        this.cx = cx;
        /** Gán tọa độ Y */
        this.cy = cy;
        /** Gán ID mẫu */
        this.tempId = tempId;
        /** Gán hình ảnh đại diện */
        this.avartar = avartar;
        /** Thêm NPC vào danh sách quản lý */
        Manager.NPCS.add(this);
    }

    /** Khởi tạo menu cơ bản từ chuỗi văn bản */
    public void initBaseMenu(String text) {
        /** Loại bỏ ký tự đầu tiên của chuỗi */
        text = text.substring(1);
        /** Tách chuỗi thành mảng dữ liệu */
        String[] data = text.split("\\|");
        /** Tạo đối tượng menu cơ bản */
        baseMenu = new BaseMenu();
        /** Gán ID của NPC cho menu */
        baseMenu.npcId = tempId;
        /** Gán lời thoại của NPC, thay thế <> bằng dấu xuống dòng */
        baseMenu.npcSay = data[0].replaceAll("<>", "\n");
        /** Khởi tạo mảng các lựa chọn menu */
        baseMenu.menuSelect = new String[data.length - 1];
        for (int i = 0; i < baseMenu.menuSelect.length; i++) {
            /** Gán từng lựa chọn menu, thay thế <> bằng dấu xuống dòng */
            baseMenu.menuSelect[i] = data[i + 1].replaceAll("<>", "\n");
        }
    }

    /** Tạo menu khác cho người chơi với chỉ số menu và các lựa chọn */
    public void createOtherMenu(Player player, int indexMenu, String npcSay, String... menuSelect) {
        Message msg;
        try {
            /** Gán chỉ số menu cho người chơi */
            player.iDMark.setIndexMenu(indexMenu);
            /** Tạo tin nhắn mới với mã lệnh 32 */
            msg = new Message(32);
            /** Ghi ID của NPC */
            msg.writer().writeShort(tempId);
            /** Ghi lời thoại của NPC */
            msg.writer().writeUTF(npcSay);
            /** Ghi số lượng lựa chọn menu */
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                /** Ghi từng lựa chọn menu */
                msg.writer().writeUTF(menu);
            }
            /** Gửi tin nhắn đến người chơi */
            player.sendMessage(msg);
            /** Giải phóng tài nguyên */
            msg.cleanup();
        } catch (Exception e) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }

    /** Tạo menu khác cho người chơi với chỉ số menu, các lựa chọn và đối tượng bổ sung */
    public void createOtherMenu(Player player, int indexMenu, String npcSay, String[] menuSelect, Object object) {
        /** Lưu đối tượng bổ sung liên quan đến người chơi */
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        Message msg;
        try {
            /** Gán chỉ số menu cho người chơi */
            player.iDMark.setIndexMenu(indexMenu);
            /** Tạo tin nhắn mới với mã lệnh 32 */
            msg = new Message(32);
            /** Ghi ID của NPC */
            msg.writer().writeShort(tempId);
            /** Ghi lời thoại của NPC */
            msg.writer().writeUTF(npcSay);
            /** Ghi số lượng lựa chọn menu */
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                /** Ghi từng lựa chọn menu */
                msg.writer().writeUTF(menu);
            }
            /** Gửi tin nhắn đến người chơi */
            player.sendMessage(msg);
            /** Giải phóng tài nguyên */
            msg.cleanup();
        } catch (Exception e) {
            /** Xử lý ngoại lệ (để trống trong mã gốc) */
        }
    }

    /** Mở menu cơ bản cho người chơi nếu thỏa mãn điều kiện */
    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            /** Gán chỉ số menu cơ bản */
            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
            try {
                if (baseMenu != null) {
                    /** Mở menu cơ bản nếu đã được khởi tạo */
                    baseMenu.openMenu(player);
                } else {
                    /** Tạo menu mặc định nếu không có menu cơ bản */
                    Message msg;
                    msg = new Message(32);
                    msg.writer().writeShort(tempId);
                    msg.writer().writeUTF("Cậu muốn gì ở tôi?");
                    msg.writer().writeByte(1);
                    msg.writer().writeUTF("Không");
                    player.sendMessage(msg);
                    msg.cleanup();
                }
            } catch (Exception e) {
                Logger.logException(Npc.class, e);
                /** Xử lý ngoại lệ */
            }
        }
    }

    /** Gửi lời thoại của NPC đến một người chơi cụ thể */
    public void npcChat(Player player, String text) {
        Message msg;
        try {
            /** Tạo tin nhắn mới với mã lệnh 124 */
            msg = new Message(124);
            /** Ghi ID của NPC */
            msg.writer().writeShort(tempId);
            /** Ghi nội dung lời thoại */
            msg.writer().writeUTF(text);
            /** Gửi tin nhắn đến người chơi */
            player.sendMessage(msg);
            /** Giải phóng tài nguyên */
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
            /** Xử lý ngoại lệ */
        }
    }

    /** Gửi lời thoại của NPC đến tất cả người chơi trong bản đồ */
    public void npcChat(String text) {
        Message msg;
        try {
            /** Tạo tin nhắn mới với mã lệnh 124 */
            msg = new Message(124);
            /** Ghi ID của NPC */
            msg.writer().writeShort(tempId);
            /** Ghi nội dung lời thoại */
            msg.writer().writeUTF(text);
            /** Gửi tin nhắn đến tất cả người chơi trong các vùng của bản đồ */
            for (Zone zone : map.zones) {
                Service.gI().sendMessAllPlayerInMap(zone, msg);
            }
            /** Giải phóng tài nguyên */
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
            /** Xử lý ngoại lệ */
        }
    }

    /** Kiểm tra xem người chơi có thể mở NPC hay không */
    public boolean canOpenNpc(Player player) {
        if (this.tempId == ConstNpc.DAU_THAN) {
            /** Kiểm tra điều kiện đặc biệt cho NPC Đầu Thần */
            if (player.zone.map.mapId == 21
                    || player.zone.map.mapId == 22
                    || player.zone.map.mapId == 23) {
                return true;
            } else {
                /** Ẩn hộp thoại chờ và thông báo lỗi */
                Service.gI().hideWaitDialog(player);
                Service.gI().sendThongBao(player, "Không thể thực hiện");
                return false;
            }
        }
        /** Kiểm tra người chơi ở cùng bản đồ và khoảng cách đủ gần */
        if (player.zone.map.mapId == this.mapId
                && Util.getDistance(this.cx, this.cy, player.location.x, player.location.y) <= 60) {
            /** Gán NPC được chọn cho người chơi */
            player.iDMark.setNpcChose(this);
            return true;
        } else {
            /** Ẩn hộp thoại chờ và thông báo lỗi */
            Service.gI().hideWaitDialog(player);
            Service.gI().sendThongBao(player, "Không thể thực hiện khi đứng quá xa");
            return false;
        }
    }
}