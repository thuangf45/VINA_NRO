package com.girlkun.server.io;

import com.girlkun.data.DataGame;
import com.girlkun.network.session.ISession;
import com.girlkun.network.example.KeyHandler;

/**
 * Lớp MyKeyHandler xử lý việc gửi khóa (key) cho phiên kết nối của client.
 * Kế thừa từ lớp KeyHandler và mở rộng chức năng để gửi thông tin phiên bản tài nguyên.
 * 
 * @author Lucifer
 */
public class MyKeyHandler extends KeyHandler {

    /**
     * Gửi khóa cho phiên kết nối và gửi thông tin phiên bản tài nguyên tới client.
     * 
     * @param session Phiên kết nối của client.
     */
    @Override
    public void sendKey(ISession session) {
        super.sendKey(session);
        DataGame.sendVersionRes((MySession) session);
    }
}