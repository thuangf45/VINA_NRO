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
 * Đại diện cho Boss Rambo trong game.
 * Đây là một boss phụ trong nhóm Nappa,
 * dữ liệu hành vi và chỉ số được định nghĩa trong {@link BossesData#RAMBO}.
 *
 * @author Lucifer
 */
public class Rambo extends Boss {

    /**
     * Khởi tạo Boss Rambo với dữ liệu từ {@link BossesData#RAMBO}.
     *
     * @throws Exception nếu dữ liệu khởi tạo không hợp lệ
     */
    public Rambo() throws Exception {
        super(BossID.RAMBO, BossesData.RAMBO);
    }

    /**
     * Hành vi khi Boss hoạt động trong game.
     * Ở đây chỉ kế thừa logic mặc định từ {@link Boss#active()}.
     */
    @Override
    public void active() {
        super.active();
    }

    /**
     * Hành vi khi Boss tham gia bản đồ.
     * Gọi lại logic mặc định từ {@link Boss#joinMap()}.
     */
    @Override
    public void joinMap() {
        super.joinMap();
    }
}
