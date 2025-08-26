package com.girlkun.models.map.challenge;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.dhvt.ChaPa;
import com.girlkun.models.boss.dhvt.ChanXu;
import com.girlkun.models.boss.dhvt.JackyChun;
import com.girlkun.models.boss.dhvt.LiuLiu;
import com.girlkun.models.boss.dhvt.ODo;
import com.girlkun.models.boss.dhvt.PonPut;
import com.girlkun.models.boss.dhvt.SoiHecQuyn;
import com.girlkun.models.boss.dhvt.TauPayPay;
import com.girlkun.models.boss.dhvt.ThienXinHang;
import com.girlkun.models.boss.dhvt.Xinbato;
import com.girlkun.models.boss.dhvt.Yamcha;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp MartialCongress quản lý giải đấu võ thuật trong trò chơi, bao gồm các vòng đấu và tương tác giữa người chơi và boss.
 * @author Lucifer
 */
@Getter
@Setter
public class MartialCongress {

    /**
     * Người chơi tham gia giải đấu.
     */
    private Player player;

    /**
     * Boss hiện tại trong vòng đấu.
     */
    private Boss boss;

    /**
     * NPC quản lý giải đấu.
     */
    private Player npc;

    /**
     * Thời gian còn lại của trận đấu (giây).
     */
    private int time;

    /**
     * Vòng đấu hiện tại.
     */
    private int round;

    /**
     * Thời gian chờ trước khi bắt đầu trận đấu (giây).
     */
    private int timeWait;

    /**
     * Cập nhật trạng thái của giải đấu, kiểm tra thời gian, kết quả và chuyển vòng.
     */
    public void update() {
        if (time > 0) {
            time--;
            if (player.isDie()) {
                die();
                return;
            }
            if (player.location != null && !player.isDie() && player != null && player.zone != null) {
                if (boss.isDie()) {
                    round++;
                    boss.leaveMap();
                    toTheNextRound();
                }
                if (player.location.y > 264) {
                    leave();
                }
            } else {
                if (boss != null) {
                    boss.leaveMap();
                }
                MartialCongressManager.gI().remove(this);
            }
        } else {
            timeOut();
        }
        if (timeWait > 0) {
            switch (timeWait) {
                case 10:
                    Service.getInstance().chat(npc, "Trận đấu giữa " + player.name + " VS " + boss.name + " sắp diễn ra");
                    ready();
                    break;
                case 8:
                    Service.getInstance().chat(npc, "Xin quý vị khán giả cho 1 tràng pháo tay để cổ vũ cho 2 đối thủ nào");
                    break;
                case 4:
                    Service.getInstance().chat(npc, "Mọi người ngồi sau hãy ổn định chỗ ngồi, trận đấu sẽ bắt đầu sau 3 giây nữa");
                    break;
                case 2:
                    Service.getInstance().chat(npc, "Trận đấu bắt đầu");
                    break;
                case 1:
                    Service.getInstance().chat(player, "Ok");
                    Service.getInstance().chat(boss, "Ok");
                    break;
            }
            timeWait--;
        }
    }

    /**
     * Chuẩn bị cho trận đấu, làm choáng người chơi và boss, gửi hiệu ứng thời gian.
     */
    public void ready() {
        EffectSkillService.gI().startStun(boss, System.currentTimeMillis(), 10000);
        EffectSkillService.gI().startStun(player, System.currentTimeMillis(), 10000);
        ItemTimeService.gI().sendItemTime(player, 3779, 10000 / 1000);
        MartialCongressService.setTimeout(() -> {
            if (boss.effectSkill != null) {
                EffectSkillService.gI().removeStun(boss);
            }
            MartialCongressService.gI().sendTypePK(player, boss);
            PlayerService.gI().changeAndSendTypePK(this.player, ConstPlayer.PK_PVP);
            boss.changeStatus(BossStatus.ACTIVE);
        }, 10000);
    }

    /**
     * Chuyển sang vòng đấu tiếp theo, khởi tạo boss mới dựa trên vòng hiện tại.
     */
    public void toTheNextRound() {
        try {
            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
            Boss bss = null;
            switch (round) {
                case 0:
                    bss = new SoiHecQuyn(player);
                    break;
                case 1:
                    bss = new ODo(player);
                    break;
                case 2:
                    bss = new Xinbato(player);
                    break;
                case 3:
                    bss = new ChaPa(player);
                    break;
                case 4:
                    bss = new PonPut(player);
                    break;
                case 5:
                    bss = new ChanXu(player);
                    break;
                case 6:
                    bss = new TauPayPay(player);
                    break;
                case 7:
                    bss = new Yamcha(player);
                    break;
                case 8:
                    bss = new JackyChun(player);
                    break;
                case 9:
                    bss = new ThienXinHang(player);
                    break;
                case 10:
                    bss = new LiuLiu(player);
                    break;
                default:
                    champion();
                    return;
            }
            MartialCongressService.gI().moveFast(player, 335, 264);
            setTimeWait(11);
            setBoss(bss);
            setTime(185);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thiết lập boss cho vòng đấu hiện tại.
     * @param boss Boss mới cho vòng đấu.
     */
    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    /**
     * Thiết lập thời gian còn lại cho trận đấu.
     * @param time Thời gian (giây).
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Thiết lập thời gian chờ trước khi bắt đầu trận đấu.
     * @param timeWait Thời gian chờ (giây).
     */
    public void setTimeWait(int timeWait) {
        this.timeWait = timeWait;
    }

    /**
     * Xử lý khi người chơi chết, kết thúc giải đấu.
     */
    private void die() {
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì chết queo");
        if (player.zone != null) {
            endChallenge();
        }
    }

    /**
     * Xử lý khi hết thời gian, kết thúc giải đấu.
     */
    private void timeOut() {
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì hết thời gian");
        endChallenge();
    }

    /**
     * Xử lý khi người chơi giành chức vô địch sau khi vượt qua tất cả các vòng.
     */
    private void champion() {
        Service.getInstance().sendThongBao(player, "Chúc mừng " + player.name + " vừa đoạt giải vô địch");
        endChallenge();
    }

    /**
     * Xử lý khi người chơi rời võ đài, kết thúc giải đấu.
     */
    public void leave() {
        setTime(0);
        EffectSkillService.gI().removeStun(player);
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì rời khỏi võ đài");
        endChallenge();
    }

    /**
     * Trao phần thưởng dựa trên số vòng người chơi đã vượt qua.
     */
    private void reward() {
        if (player.levelWoodChest < round) {
            player.levelWoodChest = round;
        }
    }

    /**
     * Kết thúc giải đấu, trao phần thưởng và đưa người chơi ra khỏi võ đài.
     */
    public void endChallenge() {
        reward();
        if (player.zone != null) {
            PlayerService.gI().hoiSinh(player);
        }
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        if (player != null && player.zone != null && player.zone.map.mapId == 129) {
            MartialCongressService.setTimeout(() -> {
                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
            }, 500);
        }
        if (boss != null) {
            boss.leaveMap();
        }
        MartialCongressManager.gI().remove(this);
    }
}