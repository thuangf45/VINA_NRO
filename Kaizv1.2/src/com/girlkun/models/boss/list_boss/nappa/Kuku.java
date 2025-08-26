package com.girlkun.models.boss.list_boss.nappa;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 * Đại diện cho Boss Kuku trong game.
 * Kuku thuộc nhóm boss đi cùng Nappa, có hành vi cơ bản khi tham gia bản đồ
 * và hoạt động tương tự các boss khác nhưng dữ liệu được định nghĩa riêng trong {@link BossesData}.
 *
 * @author Lucifer
 */
public class Kuku extends Boss {

    /**
     * Khởi tạo Boss Kuku với dữ liệu từ {@link BossesData#KUKU}.
     *
     * @throws Exception nếu dữ liệu khởi tạo không hợp lệ
     */
    public Kuku() throws Exception {
        super(BossID.KUKU, BossesData.KUKU);
    }

    /**
     * Hành vi của Boss trong khi hoạt động.
     * Ở đây chỉ gọi lại logic mặc định từ {@link Boss#active()}.
     */
    @Override
    public void active() {
        super.active(); 
    }

    /**
     * Khi Boss tham gia bản đồ.
     * Gọi logic mặc định từ {@link Boss#joinMap()}.
     */
    @Override
    public void joinMap() {
        super.joinMap(); 
    }

}
