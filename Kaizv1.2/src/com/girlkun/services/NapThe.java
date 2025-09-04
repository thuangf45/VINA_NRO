package com.girlkun.services;

import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import java.security.MessageDigest;

/**
 * Lớp NapThe quản lý chức năng nạp thẻ trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để xử lý yêu cầu nạp thẻ và mã hóa MD5.
 * 
 * @author Lucifer
 */
public class NapThe {

    /**
     * Thể hiện duy nhất của lớp NapThe (singleton pattern).
     */
    private static NapThe I;

    /**
     * Lấy thể hiện duy nhất của lớp NapThe.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp NapThe.
     */
    public static NapThe gI() {
        if (NapThe.I == null) {
            NapThe.I = new NapThe();
        }
        return NapThe.I;
    }

    /**
     * Xử lý yêu cầu nạp thẻ của người chơi.
     * In ra thông tin mã thẻ và số seri để kiểm tra.
     * 
     * @param pl Người chơi thực hiện nạp thẻ.
     * @param maThe Mã thẻ nạp.
     * @param seri Số seri của thẻ.
     */
    public void napThe(Player pl, String maThe, String seri) {
        System.out.println(maThe);
        System.out.println(seri);
    }

    /**
     * Gửi yêu cầu nạp thẻ đến hệ thống thanh toán và xử lý phản hồi.
     * 
     * @param p Người chơi thực hiện nạp thẻ.
     * @param loaiThe Loại thẻ nạp (ví dụ: Viettel, Mobifone, ...).
     * @param menhGia Mệnh giá của thẻ.
     * @param soSeri Số seri của thẻ.
     * @param maPin Mã pin của thẻ.
     */
    public static final void SendCard(Player p, String loaiThe, String menhGia, String soSeri, String maPin) {
        String partnerId = "72461046463"; // ID đối tác.
        String partnerKey = "16502d49bf5e949c3f27238c2a762115"; // Khóa đối tác.
        String api = MD5Hash(partnerKey + maPin + soSeri); // Tạo chữ ký MD5.
        int requestID = Util.nextInt(100000000, 999999999); // ID yêu cầu ngẫu nhiên.
        String t = String.valueOf(requestID);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("telco", loaiThe)
                    .addFormDataPart("code", maPin)
                    .addFormDataPart("serial", soSeri)
                    .addFormDataPart("amount", menhGia)
                    .addFormDataPart("request_id", t)
                    .addFormDataPart("partner_id", partnerId)
                    .addFormDataPart("sign", api)
                    .addFormDataPart("command", "charging")
                    .build();

            Request request = new Request.Builder()
                    .url("https://thesieure.com/chargingws/v2")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            okhttp3.Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            Object obj = JSONValue.parse(jsonString);
            JSONObject jsonObject = (JSONObject) obj;
            long name = (long) jsonObject.get("status");

            if (name == 99 || name == 1) {
                PlayerDAO.LogNapTIen(p.getSession().uu, menhGia, soSeri, maPin, t);
                Service.gI().sendThongBaoOK(p, "Gửi thẻ thành công \n"
                        + "Seri :" + soSeri + "\n Mã thẻ :" + maPin + "\n Mệnh giá : " + menhGia + "\n"
                        + "Thời gian : " + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "\n"
                        + "Vui lòng thoát game để update lại số tiền");
            } else if (name == 2) {
                Service.gI().sendThongBao(p, "Nạp thành công nhưng sai mệnh giá. Con sẽ không được cộng tiền \n lần sau ông khóa mẹ acc con cho chừa nhé");
            } else if (name == 3) {
                Service.gI().sendThongBao(p, "Bạn đã nhập sai giá trị, hãy nhập đúng nhóe :3");
            } else if (name == 4) {
                Service.gI().sendThongBao(p, "Hệ thống nạp bảo trì rồi con");
            } else if (name == 100) {
                Service.gI().sendThongBao(p, "Sai seri và mã pin ồi con ơi");
            }

            System.out.println(name + "\n" + menhGia + soSeri + "\n" + maPin);

        } catch (Exception e) {
            // Logger.error("lỗi ở nạp thẻ mất ồi");
        }
    }

    /**
     * Mã hóa chuỗi đầu vào thành chuỗi MD5.
     * 
     * @param input Chuỗi cần mã hóa.
     * @return Chuỗi MD5 được mã hóa, hoặc null nếu có lỗi.
     */
    public static String MD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return null;
    }
}