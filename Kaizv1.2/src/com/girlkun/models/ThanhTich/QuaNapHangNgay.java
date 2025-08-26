package com.girlkun.models.ThanhTich;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import org.json.simple.JSONArray;

/**
 * Quản lý phần thưởng nạp hằng ngày (quy đổi tiền sang quà).
 * - Người chơi đạt các mốc nạp trong ngày sẽ được nhận quà tương ứng.
 * - Quản lý tình trạng hoàn thành, đã nhận quà hay chưa.
 * 
 * @author Lucifer
 */
public class QuaNapHangNgay {

    // Loại thành tích
    public static int Type = 3;
    public static String Name = "Quy Đổi Hằng Ngày";

    // Thông tin mô tả các mốc
    public static String Info1 = "Quy Đổi Hằng Ngày";
    public static String[] Info2 = new String[]{
        "Quy Đổi ngày đạt 10.000Đ",
        "Quy Đổi ngày đạt 20.000Đ",
        "Quy Đổi ngày đạt 50.000Đ",
        "Quy Đổi ngày đạt 100.000Đ",
        "Quy Đổi ngày đạt 200.000Đ",
        "Quy Đổi ngày đạt 500.000Đ",
        "Quy Đổi ngày đạt 1.000.000Đ",
    };

    // Số tiền quy đổi tương ứng với từng mốc
    public static int[] Money = new int[]{
        10, 20, 5, 10, 10, 20, 50
    };

    // Trạng thái: đã hoàn thành chưa
    public static boolean[] isFinish = new boolean[]{
        false, false, false, false, false, false, false
    };

    // Trạng thái: đã nhận thưởng chưa
    public static boolean[] isRecieve = new boolean[]{
        false, false, false, false, false, false, false
    };

    // Tên phần thưởng
    public static String[] Reward = new String[]{
        "Đá bảo vệ",
        "Đá bảo vệ",
        "Item\nCấp 2",
        "Item\nCấp 2",
        "Đá ngũ sắc",
        "Đá ngũ sắc",
        "Đá ngũ sắc",
    };

    /**
     * Cập nhật trạng thái nhận thưởng từ dữ liệu JSON.
     */
    public static void GetRecieve(Player pl, JSONArray dataArray) {
        for (int i = 0; i < QuaNapHangNgay.isRecieve.length; i++) {
            QuaNapHangNgay.isRecieve[i] = Integer.parseInt(String.valueOf(dataArray.get(i))) == 1;
        }
    }

    /**
     * Người chơi nhận thưởng ở một mốc cụ thể.
     * @param index chỉ số mốc (0-6)
     */
    public static void GetArchivemnt(Player pl, int index) {
        QuaNapHangNgay.isFinish = CheckTaskDays.CheckNapTheDay(pl);

        // Nếu đã nhận trước đó
        if (QuaNapHangNgay.isRecieve[index]) {
            Service.gI().sendThongBao(pl, "Bạn đã nhận từ trước rồi");
            return;
        }

        // Nếu chưa hoàn thành mốc
        if (!QuaNapHangNgay.isFinish[index]) {
            Service.gI().sendThongBao(pl, "Bạn chưa hoàn thành nhiệm vụ này");
            return;
        }

        // Đủ điều kiện và chưa nhận thưởng
        if (QuaNapHangNgay.isFinish[index] && !QuaNapHangNgay.isRecieve[index]) {
            short tempID = 0;
            int soluong = 0;
            switch (index) {
                case 0: tempID = 987; soluong = 10; break;
                case 1: tempID = 987; soluong = 20; break;
                case 2: tempID = 1278; soluong = 10; break;
                case 3: tempID = 1278; soluong = 20; break;
                case 4: tempID = 674; soluong = 10; break;
                case 5: tempID = 674; soluong = 20; break;
                case 6: tempID = 674; soluong = 50; break;
            }

            // Kiểm tra túi đồ
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
            } else {
                // Cập nhật trạng thái đã nhận
                QuaNapHangNgay.isRecieve[index] = true;

                // Tạo item và thêm vào túi
                Item item = ItemService.gI().createNewItem(tempID, soluong);
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);

                // Thông báo
                Service.gI().sendThongBao(pl, "Bạn nhận được x" + soluong + " " + item.template.name);

                // Gửi cập nhật cho client
                QuaNapHangNgay.SendGetArchivemnt(pl, index);
            }
        }
    }

    /**
     * Gửi thông tin đã nhận quà của 1 mốc về client.
     */
    public static void SendGetArchivemnt(Player pl, int index) {
        Message msg = null;
        try {
            msg = new Message(-76);
            msg.writer().writeByte(1);
            msg.writer().writeByte(index);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at SendGetArchivemnt\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi toàn bộ thông tin thành tích (Quà nạp hằng ngày) về client.
     */
    public static void SendThanhTich(Player pl) {
        if (pl == null) return;

        Message msg = null;
        try {
            msg = new Message(-76);
            msg.writer().writeByte(0);
            msg.writer().writeUTF(QuaNapHangNgay.Name);
            msg.writer().writeByte(QuaNapHangNgay.Type);
            msg.writer().writeByte(QuaNapHangNgay.Info2.length);

            for (int i = 0; i < QuaNapHangNgay.Info2.length; i++) {
                msg.writer().writeUTF(QuaNapHangNgay.Info1);
                msg.writer().writeUTF(QuaNapHangNgay.Info2[i]);
                msg.writer().writeShort(QuaNapHangNgay.Money[i]);
                msg.writer().writeBoolean(CheckTaskDays.CheckNapTheDay(pl)[i]);
                msg.writer().writeBoolean(QuaNapHangNgay.isRecieve[i]);
                msg.writer().writeUTF(QuaNapHangNgay.Reward[i]);
            }

            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at SendThanhTich\n");
            e.printStackTrace();
        }
    }
}
