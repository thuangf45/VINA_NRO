package com.girlkun.models.player;

import com.girlkun.models.mob.Mob;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.SkillService;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Lớp SkillSpecial quản lý kỹ năng đặc biệt của người chơi trong game.
 * Lớp này xử lý việc kích hoạt, cập nhật, và đóng kỹ năng đặc biệt, bao gồm việc theo dõi các mục tiêu (người chơi và quái vật)
 * cũng như quản lý thời gian và trạng thái của kỹ năng.
 * 
 * @author Lucifer
 */
public class SkillSpecial {

    /**
     * Thời gian thực hiện động tác "gồng" của kỹ năng đặc biệt, mặc định là 2000 milliseconds (2 giây).
     */
    public static final int TIME_GONG = 2000;

    /**
     * Thời gian kết thúc kỹ năng đặc biệt, mặc định là 3000 milliseconds (3 giây).
     */
    public static final int TIME_END = 3000;

    /**
     * Người chơi sử dụng kỹ năng đặc biệt.
     */
    private Player player;

    /**
     * Kỹ năng đặc biệt được chọn để sử dụng.
     */
    public Skill skillSpecial;

    /**
     * Hướng của kỹ năng đặc biệt (ví dụ: trái, phải, lên, xuống).
     */
    public byte dir;

    /**
     * Tọa độ x của người chơi khi sử dụng kỹ năng đặc biệt.
     */
    public short _xPlayer;

    /**
     * Tọa độ y của người chơi khi sử dụng kỹ năng đặc biệt.
     */
    public short _yPlayer;

    /**
     * Tọa độ x của mục tiêu mà kỹ năng đặc biệt nhắm đến.
     */
    public short _xObjTaget;

    /**
     * Tọa độ y của mục tiêu mà kỹ năng đặc biệt nhắm đến.
     */
    public short _yObjTaget;

    /**
     * Danh sách các người chơi là mục tiêu của kỹ năng đặc biệt.
     */
    public List<Player> playersTaget;

    /**
     * Danh sách các quái vật (mob) là mục tiêu của kỹ năng đặc biệt.
     */
    public List<Mob> mobsTaget;

    /**
     * Trạng thái kích hoạt kỹ năng đặc biệt (true nếu kỹ năng đang được sử dụng).
     */
    public boolean isStartSkillSpecial;

    /**
     * Giai đoạn hiện tại của kỹ năng đặc biệt (bước thực hiện).
     */
    public byte stepSkillSpecial;

    /**
     * Thời điểm lần cuối kỹ năng đặc biệt được kích hoạt.
     */
    public long lastTimeSkillSpecial;

    /**
     * Bộ đếm thời gian (timer) để quản lý việc cập nhật kỹ năng đặc biệt.
     */
    private Timer timer;

    /**
     * Nhiệm vụ (task) của bộ đếm thời gian để thực hiện cập nhật kỹ năng.
     */
    private TimerTask timerTask;

    /**
     * Trạng thái hoạt động của bộ đếm thời gian (true nếu timer đang chạy).
     */
    private boolean isActive = false;

    /**
     * Khởi tạo một đối tượng SkillSpecial cho một người chơi cụ thể.
     * Khởi tạo các danh sách mục tiêu (người chơi và quái vật).
     * 
     * @param player Người chơi sử dụng kỹ năng đặc biệt.
     */
    public SkillSpecial(Player player) {
        this.player = player;
        this.playersTaget = new ArrayList<>();
        this.mobsTaget = new ArrayList<>();
    }

    /**
     * Cập nhật trạng thái của kỹ năng đặc biệt.
     * Nếu kỹ năng đang được kích hoạt, gọi dịch vụ SkillService để xử lý cập nhật.
     */
    private void update() {
        if (this.isStartSkillSpecial) {
            SkillService.gI().updateSkillSpecial(player);
        }
    }

    /**
     * Thiết lập thông tin cho kỹ năng đặc biệt, bao gồm hướng, tọa độ người chơi, và tọa độ mục tiêu.
     * Kích hoạt kỹ năng, tăng cấp độ kỹ năng nếu cần, và bắt đầu bộ đếm thời gian để cập nhật.
     * 
     * @param dir Hướng của kỹ năng đặc biệt.
     * @param _xPlayer Tọa độ x của người chơi.
     * @param _yPlayer Tọa độ y của người chơi.
     * @param _xObjTaget Tọa độ x của mục tiêu.
     * @param _yObjTaget Tọa độ y của mục tiêu.
     */
    public void setSkillSpecial(byte dir, short _xPlayer, short _yPlayer, short _xObjTaget, short _yObjTaget) {
        this.isStartSkillSpecial = true;
        this.skillSpecial = this.player.playerSkill.skillSelect;
        if (skillSpecial.currLevel < 1000) {
            skillSpecial.currLevel++;
            SkillService.gI().sendCurrLevelSpecial(player, skillSpecial);
        }
        this.dir = dir;
        this._xPlayer = _xPlayer;
        this._yPlayer = _yPlayer;
        this._xObjTaget = (short) (skillSpecial.dx + skillSpecial.point * 50); // Độ dài kỹ năng
        this._yObjTaget = (short) skillSpecial.dy;
        this.stepSkillSpecial = 0;
        this.lastTimeSkillSpecial = System.currentTimeMillis();
        this.start(250); // Delay để cập nhật và gây sát thương
    }

    /**
     * Đóng kỹ năng đặc biệt, đặt lại trạng thái và xóa danh sách mục tiêu.
     * Hủy bộ đếm thời gian nếu đang hoạt động.
     */
    public void closeSkillSpecial() {
        this.isStartSkillSpecial = false;
        this.stepSkillSpecial = 0;
        this.playersTaget.clear();
        this.mobsTaget.clear();
        this.close();
    }

    /**
     * Đóng bộ đếm thời gian và hủy nhiệm vụ liên quan để giải phóng tài nguyên.
     */
    private void close() {
        try {
            this.isActive = false;
            this.timer.cancel();
            this.timerTask.cancel();
            this.timer = null;
            this.timerTask = null;
        } catch (Exception e) {
            this.timer = null;
            this.timerTask = null;
        }
    }

    /**
     * Bắt đầu bộ đếm thời gian để cập nhật kỹ năng đặc biệt với khoảng thời gian lặp lại được chỉ định.
     * 
     * @param leep Khoảng thời gian lặp lại (milliseconds) để cập nhật kỹ năng.
     */
    public void start(int leep) {
        if (!this.isActive) {
            this.isActive = true;
            this.timer = new Timer();
            this.timerTask = new TimerTask() {
                @Override
                public void run() {
                    SkillSpecial.this.update();
                }
            };
            this.timer.schedule(timerTask, leep, leep);
        }
    }

    /**
     * Giải phóng tài nguyên bằng cách đặt tham chiếu đến người chơi và kỹ năng đặc biệt về null.
     */
    public void dispose() {
        this.player = null;
        this.skillSpecial = null;
    }
}