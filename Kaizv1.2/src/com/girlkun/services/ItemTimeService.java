package com.girlkun.services;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.item.Item;
import static com.girlkun.models.item.ItemTime.*;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.arriety.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.gas.Gas;
import static com.girlkun.models.map.gas.Gas.KHI_GAS;
import com.girlkun.models.player.Fusion;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.utils.Logger;

/**
 * Service quản lý thời gian sử dụng các vật phẩm và sự kiện của người chơi.
 */
public class ItemTimeService {

    private static ItemTimeService i;

    /**
     * Lấy instance của ItemTimeService (Singleton pattern).
     * 
     * @return ItemTimeService instance.
     */
    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

    /**
     * Gửi toàn bộ thông tin thời gian vật phẩm và sự kiện cho client.
     * 
     * @param player Người chơi.
     */
    public void sendAllItemTime(Player player) {
        sendTextDoanhTrai(player);
        sendTextBanDoKhoBau(player);
        sendTextGas(player);
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.itemTime.isX2EXP) {
            sendItemTime(player, player.itemTime.IconX2EXP, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastX2EXP)) / 1000));
        }
        if (player.itemTime.isX3EXP) {
            sendItemTime(player, 21877, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastX3EXP)) / 1000));
        }
        if (player.itemTime.isX5EXP) {
            sendItemTime(player, 21878, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastX5EXP)) / 1000));
        }
        if (player.itemTime.isX7EXP) {
            sendItemTime(player, 21879, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastX7EXP)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isdaiviet) {
            sendItemTime(player, 5829, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastdaiviet)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet2) {
            sendItemTime(player, 10714, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) / 1000));
        }
        if (player.itemTime.isUseBoKhi2) {
            sendItemTime(player, 10715, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) / 1000));
        }
        if (player.itemTime.isUseGiapXen2) {
            sendItemTime(player, 10712, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) / 1000));
        }
        if (player.itemTime.isUseCuongNo2) {
            sendItemTime(player, 10716, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) / 1000));
        }
        if (player.itemTime.isUseAnDanh2) {
            sendItemTime(player, 10717, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh2)) / 1000));
        }
        if (player.itemTime.isOpenPower) {
            sendItemTime(player, 3783, (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
        if (player.itemTime.isUseMayDo2) {
            sendItemTime(player, 16004, (int) ((TIME_MAY_DO2 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) / 1000));
        }
        if (player.itemTime.isEatMeal) {
            sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
        }
        if (player.itemTime.isUseTDLT) {
            sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        }
    }

    /**
     * Bật tính năng tự động luyện tập (TDLT) cho người chơi.
     * 
     * @param player Người chơi.
     * @param item Vật phẩm TDLT.
     */
    public void turnOnTDLT(Player player, Item item) {
        if (player != null && item != null && item.itemOptions != null) {
            int min = 0;
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 1) {
                    min = io.param; // Lấy số phút từ tùy chọn
                    io.param = 0; // Đặt lại số phút về 0
                    break;
                }
            }
            player.itemTime.isUseTDLT = true;
            player.itemTime.timeTDLT = min * 60 * 1000; // Chuyển phút thành mili giây
            player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
            sendCanAutoPlay(player); // Gửi thông báo tự động luyện tập
            sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000); // Gửi thời gian TDLT
            InventoryServiceNew.gI().sendItemBags(player); // Cập nhật hành trang
        }
    }

    /**
     * Tắt tính năng tự động luyện tập (TDLT) cho người chơi.
     * 
     * @param player Người chơi.
     * @param item Vật phẩm TDLT.
     */
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param += (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000); // Cập nhật số phút còn lại
                break;
            }
        }
        sendCanAutoPlay(player); // Gửi thông báo tắt tự động luyện tập
        removeItemTime(player, 4387); // Xóa thời gian TDLT
        InventoryServiceNew.gI().sendItemBags(player); // Cập nhật hành trang
    }

    /**
     * Gửi thông báo trạng thái tự động luyện tập cho client.
     * 
     * @param player Người chơi.
     */
    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0); // 1: bật, 0: tắt
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(ItemTimeService.class, e);
        }
    }

    /**
     * Gửi văn bản thời gian của sự kiện Khí Gas Hủy Diệt.
     * 
     * @param player Người chơi.
     */
    public void sendTextGas(Player player) {
        if (player.clan != null && player.clan.timeOpenKhiGas != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenKhiGas) / 1000);
            int secondsLeft = (Gas.TIME_KHI_GAS / 1000) - secondPassed;
            sendTextTime(player, KHI_GASS, "Khí Gas Hủy Diệt: ", secondsLeft);
        }
    }

    /**
     * Gửi văn bản thời gian của sự kiện Doanh Trại Độc Nhãn.
     * 
     * @param player Người chơi.
     */
    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && player.clan.doanhTrai != null && player.clan.lastTimeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.lastTimeOpenDoanhTrai) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn: ", secondsLeft);
        }
    }

    /**
     * Gửi văn bản thời gian của sự kiện Bản Đồ Kho Báu.
     * 
     * @param player Người chơi.
     */
    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Bản đồ kho báu: ", secondsLeft);
        }
    }

    /**
     * Xóa văn bản thời gian của sự kiện Doanh Trại Độc Nhãn.
     * 
     * @param player Người chơi.
     */
    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    /**
     * Xóa văn bản thời gian của sự kiện Khí Gas Hủy Diệt.
     * 
     * @param player Người chơi.
     */
    public void removeTextKhiGas(Player player) {
        removeTextTime(player, KHI_GASS);
    }

    /**
     * Xóa văn bản thời gian của sự kiện.
     * 
     * @param player Người chơi.
     * @param id ID của sự kiện.
     */
    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

    /**
     * Gửi văn bản thời gian của sự kiện cho client.
     * 
     * @param player Người chơi.
     * @param id ID của sự kiện.
     * @param text Nội dung văn bản.
     * @param seconds Thời gian còn lại (giây).
     */
    public void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(ItemTimeService.class, e);
        }
    }

    /**
     * Gửi thời gian sử dụng vật phẩm cho client.
     * 
     * @param player Người chơi.
     * @param itemId ID của vật phẩm.
     * @param time Thời gian còn lại (giây).
     */
    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(ItemTimeService.class, e);
        }
    }

    /**
     * Xóa thời gian sử dụng vật phẩm.
     * 
     * @param player Người chơi.
     * @param itemId ID của vật phẩm.
     */
    public void removeItemTime(Player player, int itemId) {
        sendItemTime(player, itemId, 0);
    }
}