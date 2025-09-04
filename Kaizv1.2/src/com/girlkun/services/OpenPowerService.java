package com.girlkun.services;

import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.Service;

/**
 * Lớp OpenPowerService quản lý chức năng mở giới hạn sức mạnh cho người chơi hoặc đệ tử.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để mở giới hạn sức mạnh cơ bản và nhanh chóng.
 * 
 * @author Lucifer
 */
public class OpenPowerService {

    /**
     * Hằng số chi phí để mở giới hạn sức mạnh nhanh chóng.
     */
    public static final int COST_SPEED_OPEN_LIMIT_POWER = 500000000;

    /**
     * Thể hiện duy nhất của lớp OpenPowerService (singleton pattern).
     */
    private static OpenPowerService i;

    /**
     * Constructor riêng để ngăn khởi tạo trực tiếp từ bên ngoài.
     */
    private OpenPowerService() {
    }

    /**
     * Lấy thể hiện duy nhất của lớp OpenPowerService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp OpenPowerService.
     */
    public static OpenPowerService gI() {
        if (i == null) {
            i = new OpenPowerService();
        }
        return i;
    }

    /**
     * Mở giới hạn sức mạnh cơ bản cho người chơi.
     * Kiểm tra điều kiện sức mạnh và trạng thái mở giới hạn.
     * 
     * @param player Người chơi thực hiện mở giới hạn sức mạnh.
     * @return True nếu mở thành công, ngược lại trả về false.
     */
    public boolean openPowerBasic(Player player) {
        byte curLimit = player.nPoint.limitPower;
        if (curLimit < NPoint.MAX_LIMIT) {
            if (!player.itemTime.isOpenPower && player.nPoint.canOpenPower()) {
                player.itemTime.isOpenPower = true;
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis();
                ItemTimeService.gI().sendAllItemTime(player);
                return true;
            } else {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
                return false;
            }
        } else {
            Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            return false;
        }
    }

    /**
     * Mở giới hạn sức mạnh nhanh chóng cho người chơi hoặc đệ tử.
     * Yêu cầu sức mạnh tối thiểu và trừ chi phí nếu thành công.
     * 
     * @param player Người chơi hoặc đệ tử thực hiện mở giới hạn sức mạnh.
     * @return True nếu mở thành công, ngược lại trả về false.
     */
    public boolean openPowerSpeed(Player player) {
        if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
            if (player.nPoint.power >= 17900000000L) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                if (!player.isPet) {
                    Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                } else {
                    Service.gI().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
                }
                return true;
            } else {
                if (!player.isPet) {
                    Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
                } else {
                    Service.gI().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử không đủ để thực hiện");
                }
                return false;
            }
        } else {
            if (!player.isPet) {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            } else {
                Service.gI().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử đã đạt tới mức tối đa");
            }
            return false;
        }
    }
}