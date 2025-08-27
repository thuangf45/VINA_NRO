package com.girlkun.models.skill;

import java.util.ArrayList;
import java.util.List;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.network.io.Message;

/**
 * Lớp quản lý các kỹ năng của người chơi trong game.
 * @author Lucifer
 */
public class PlayerSkill {

    /** Người chơi sở hữu các kỹ năng. */
    private Player player;

    /** Danh sách các kỹ năng của người chơi. */
    public List<Skill> skills;

    /** Kỹ năng đang được chọn. */
    public Skill skillSelect;

    /** Mảng chứa các phím tắt kỹ năng. */
    public byte[] skillShortCut = new byte[5];

    /** Trạng thái chuẩn bị sử dụng kỹ năng Quả Cầu Kame. */
    public boolean prepareQCKK;

    /** Trạng thái chuẩn bị sử dụng kỹ năng Tự Sát. */
    public boolean prepareTuSat;

    /** Trạng thái chuẩn bị sử dụng kỹ năng Laze. */
    public boolean prepareLaze;

    /** Thời điểm cuối cùng chuẩn bị kỹ năng Quả Cầu Kame. */
    public long lastTimePrepareQCKK;

    /** Thời điểm cuối cùng chuẩn bị kỹ năng Tự Sát. */
    public long lastTimePrepareTuSat;

    /** Thời điểm cuối cùng chuẩn bị kỹ năng Laze. */
    public long lastTimePrepareLaze;

    /**
     * Khởi tạo đối tượng PlayerSkill cho một người chơi.
     *
     * @param player Người chơi sở hữu các kỹ năng.
     */
    public PlayerSkill(Player player) {
        this.player = player;
        skills = new ArrayList<>();
    }

    /**
     * Lấy kỹ năng dựa trên ID kỹ năng.
     *
     * @param id ID của kỹ năng cần tìm.
     * @return Kỹ năng tương ứng, hoặc null nếu không tìm thấy.
     */
    public Skill getSkillbyId(int id) {
        for (Skill skill : skills) {
            if (skill.template.id == id) {
                return skill;
            }
        }
        return null;
    }

    /**
     * Gửi thông tin phím tắt kỹ năng đến người chơi.
     */
    public void sendSkillShortCut() {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 61);
            msg.writer().writeUTF("KSkill");
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
            msg = Service.gI().messageSubCommand((byte) 61);
            msg.writer().writeUTF("OSkill");
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) 
        {
//            Logger.logException(PlayerSkill.class, e, "Lỗi khi gửi phím tắt kỹ năng");
        }
    }

    /**
     * Lấy chỉ số của kỹ năng đang chọn (dùng để xác định vị trí kỹ năng trong giao diện).
     *
     * @return Chỉ số của kỹ năng đang chọn (1, 2, hoặc 3).
     */
    public byte getIndexSkillSelect() {
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.KAIOKEN:
            case Skill.LIEN_HOAN:
                return 1;
            case Skill.KAMEJOKO:
            case Skill.ANTOMIC:
            case Skill.MASENKO:
                return 2;
            default:
                return 3;
        }
    }

    /**
     * Lấy số lượng kỹ năng có sẵn của người chơi.
     *
     * @return Số lượng kỹ năng hợp lệ.
     */
    public byte getSizeSkill() {
        byte size = 0;
        for (Skill skill : skills) {
            if (skill.skillId != -1) {
                size++;
            }
        }
        return size;
    }

    /**
     * Giải phóng tài nguyên của đối tượng PlayerSkill, bao gồm các kỹ năng và tham chiếu người chơi.
     */
    public void dispose() {
        if (this.skillSelect != null) {
            this.skillSelect.dispose();
        }
        if (this.skills != null) {
            for (Skill skill : this.skills) {
                skill.dispose();
            }
            this.skills.clear();
        }
        this.player = null;
        this.skillSelect = null;
        this.skills = null;
    }
}