package com.girlkun.models.boss;

import com.girlkun.consts.ConstPlayer;
import static com.girlkun.models.boss.BossStatus.*;
import com.girlkun.models.boss.iboss.IBossNew;
import com.girlkun.models.boss.iboss.IBossOutfit;
import com.girlkun.models.boss.list_boss.NRD.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.TaskService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * Lớp đại diện cho Boss, kế thừa từ Player và triển khai các interface IBossNew, IBossOutfit.
 * Quản lý các trạng thái và hành vi của Boss trong game.
 */
public class Boss extends Player implements IBossNew, IBossOutfit {

    /** Cấp độ hiện tại của Boss */
    public int currentLevel = -1;
    /** Dữ liệu cấu hình của Boss */
    protected final BossData[] data;

    /** Trạng thái hiện tại của Boss */
    public BossStatus bossStatus;

    /** Vùng bản đồ cuối cùng mà Boss xuất hiện */
    protected Zone lastZone;

    /** Thời gian nghỉ cuối cùng */
    protected long lastTimeRest;
    /** Thời gian nghỉ giữa các lần xuất hiện */
    protected int secondsRest;

    /** Thời gian chat bắt đầu cuối cùng */
    protected long lastTimeChatS;
    /** Thời gian chờ giữa các câu chat bắt đầu */
    protected int timeChatS;
    /** Chỉ số câu chat bắt đầu */
    protected byte indexChatS;

    /** Thời gian chat kết thúc cuối cùng */
    protected long lastTimeChatE;
    /** Thời gian chờ giữa các câu chat kết thúc */
    protected int timeChatE;
    /** Chỉ số câu chat kết thúc */
    protected byte indexChatE;

    /** Thời gian chat ngẫu nhiên cuối cùng */
    protected long lastTimeChatM;
    /** Thời gian chờ giữa các câu chat ngẫu nhiên */
    protected int timeChatM;

    /** Thời gian nhắm mục tiêu người chơi cuối cùng */
    protected long lastTimeTargetPlayer;
    /** Thời gian chờ để nhắm mục tiêu mới */
    protected int timeTargetPlayer;
    /** Người chơi đang bị nhắm làm mục tiêu */
    protected Player playerTarger;

    /** Boss cha (nếu có) */
    protected Boss parentBoss;
    /** Danh sách các Boss xuất hiện cùng */
    public Boss[][] bossAppearTogether;

    /** Vùng bản đồ cuối cùng của Boss */
    public Zone zoneFinal = null;

    /**
     * Khởi tạo một Boss mới với ID và dữ liệu cấu hình.
     * @param id Mã định danh của Boss
     * @param data Dữ liệu cấu hình của Boss
     * @throws Exception Nếu dữ liệu không hợp lệ
     */
    public Boss(int id, BossData... data) throws Exception {
        this.id = id;
        this.isBoss = true;
        if (data == null || data.length == 0) {
            throw new Exception("Dữ liệu boss không hợp lệ");
        }
        this.data = data;
        this.secondsRest = this.data[0].getSecondsRest();
        this.bossStatus = REST;
        BossManager.gI().addBoss(this);

        this.bossAppearTogether = new Boss[this.data.length][];
        for (int i = 0; i < this.bossAppearTogether.length; i++) {
            if (this.data[i].getBossesAppearTogether() != null) {
                this.bossAppearTogether[i] = new Boss[this.data[i].getBossesAppearTogether().length];
                for (int j = 0; j < this.data[i].getBossesAppearTogether().length; j++) {
                    Boss boss = BossManager.gI().createBoss(this.data[i].getBossesAppearTogether()[j]);
                    if (boss != null) {
                        boss.parentBoss = this;
                        this.bossAppearTogether[i][j] = boss;
                    }
                }
            }
        }
    }

    /**
     * Khởi tạo các thông số cơ bản của Boss dựa trên cấp độ hiện tại.
     */
    @Override
    public void initBase() {
        BossData data = this.data[this.currentLevel];
        this.name = String.format(data.getName(), Util.nextInt(0, 100));
        this.gender = data.getGender();
        this.nPoint.mpg = 7_5_2002;
        this.nPoint.dameg = data.getDame();
        this.nPoint.hpg = data.getHp()[Util.nextInt(0, data.getHp().length - 1)];
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    /**
     * Khởi tạo kỹ năng của Boss dựa trên dữ liệu cấu hình.
     */
    protected void initSkill() {
        for (Skill skill : this.playerSkill.skills) {
            skill.dispose();
        }
        this.playerSkill.skills.clear();
        this.playerSkill.skillSelect = null;
        int[][] skillTemp = data[this.currentLevel].getSkillTemp();
        for (int i = 0; i < skillTemp.length; i++) {
            Skill skill = SkillUtil.createSkill(skillTemp[i][0], skillTemp[i][1]);
            if (skillTemp[i].length == 3) {
                skill.coolDown = skillTemp[i][2];
            }
            this.playerSkill.skills.add(skill);
        }
    }

    /**
     * Đặt lại các thông số cơ bản của Boss.
     */
    protected void resetBase() {
        this.lastTimeChatS = 0;
        this.lastTimeChatE = 0;
        this.timeChatS = 0;
        this.timeChatE = 0;
        this.indexChatS = 0;
        this.indexChatE = 0;
    }

    /**
     * Lấy ID phần đầu của Boss (head).
     * @return ID phần đầu
     */
    @Override
    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        }
        return this.data[this.currentLevel].getOutfit()[0];
    }

    /**
     * Lấy ID phần thân của Boss (body).
     * @return ID phần thân
     */
    @Override
    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        return this.data[this.currentLevel].getOutfit()[1];
    }

    /**
     * Lấy ID phần chân của Boss (leg).
     * @return ID phần chân
     */
    @Override
    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        }
        return this.data[this.currentLevel].getOutfit()[2];
    }

    /**
     * Lấy ID cờ túi của Boss.
     * @return ID cờ túi
     */
    @Override
    public short getFlagBag() {
        return this.data[this.currentLevel].getOutfit()[3];
    }

    /**
     * Lấy ID hào quang của Boss.
     * @return ID hào quang
     */
    @Override
    public byte getAura() {
        return (byte) this.data[this.currentLevel].getOutfit()[4];
    }

    /**
     * Lấy ID hiệu ứng phía trước của Boss.
     * @return ID hiệu ứng phía trước
     */
    @Override
    public byte getEffFront() {
        return (byte) this.data[this.currentLevel].getOutfit()[5];
    }

    /**
     * Lấy bản đồ mà Boss sẽ tham gia.
     * @return Vùng bản đồ
     */
    public Zone getMapJoin() {
        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        return map;
    }

    /**
     * Thay đổi trạng thái của Boss.
     * @param status Trạng thái mới
     */
    @Override
    public void changeStatus(BossStatus status) {
        this.bossStatus = status;
    }

    /**
     * Lấy người chơi để tấn công.
     * @return Người chơi mục tiêu
     */
    @Override
    public Player getPlayerAttack() {
        if (this.playerTarger != null && (this.playerTarger.isDie() && !this.isNewPet || !this.name.equals("binn") || !this.zone.equals(this.playerTarger.zone))) {
            this.playerTarger = null;
        }
        if (this.playerTarger == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarger = this.zone.getRandomPlayerInMap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        return this.playerTarger;
    }

    /**
     * Chuyển Boss sang trạng thái PK (tấn công tất cả).
     */
    @Override
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }

    /**
     * Chuyển Boss sang trạng thái không PK.
     */
    @Override
    public void changeToTypeNonPK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
    }

    /**
     * Cập nhật trạng thái và hành vi của Boss.
     */
    @Override
    public void update() {
        super.update();
        this.nPoint.mp = this.nPoint.mpg;
        if (this.effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.bossStatus) {
            case REST:
                this.rest();
                break;
            case RESPAWN:
                this.respawn();
                this.changeStatus(JOIN_MAP);
                break;
            case JOIN_MAP:
                this.joinMap();
                this.changeStatus(CHAT_S);
                break;
            case CHAT_S:
                if (chatS()) {
                    this.doneChatS();
                    this.lastTimeChatM = System.currentTimeMillis();
                    this.timeChatM = 5000;
                    this.changeStatus(ACTIVE);
                }
                break;
            case ACTIVE:
                this.chatM();
                if (this.effectSkill.isCharging && !Util.isTrue(1, 20) || this.effectSkill.useTroi) {
                    return;
                }
                this.active();
                break;
            case DIE:
                this.changeStatus(CHAT_E);
                break;
            case CHAT_E:
                if (chatE()) {
                    this.doneChatE();
                    this.changeStatus(LEAVE_MAP);
                }
                break;
            case LEAVE_MAP:
                this.leaveMap();
                break;
        }
    }

    /**
     * Xử lý trạng thái nghỉ của Boss.
     */
    @Override
    public void rest() {
        int nextLevel = this.currentLevel + 1;
        if (nextLevel >= this.data.length) {
            nextLevel = 0;
        }
        if (this.data[nextLevel].getTypeAppear() == TypeAppear.DEFAULT_APPEAR
                && Util.canDoWithTime(lastTimeRest, secondsRest * 1000)) {
            this.changeStatus(RESPAWN);
        }
    }

    /**
     * Tái sinh Boss với cấp độ mới.
     */
    @Override
    public void respawn() {
        this.currentLevel++;
        if (this.currentLevel >= this.data.length) {
            this.currentLevel = 0;
        }
        this.initBase();
        this.changeToTypeNonPK();
    }

    /**
     * Tham gia bản đồ của Boss.
     */
    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                if (this.parentBoss == null) {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
                } else {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone,
                            this.parentBoss.location.x + Util.nextInt(-100, 100));
                }
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.gI().sendFlagBag(this);
            this.notifyJoinMap();
        }
    }

    /**
     * Tham gia bản đồ dựa trên vùng của người chơi.
     * @param player Người chơi
     */
    public void joinMapByZone(Player player) {
        if (player.zone != null) {
            this.zone = player.zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }
    }

    /**
     * Tham gia bản đồ dựa trên vùng chỉ định.
     * @param zone Vùng bản đồ
     */
    public void joinMapByZone(Zone zone) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }
    }

    /**
     * Thông báo khi Boss tham gia bản đồ.
     */
    protected void notifyJoinMap() {
        if ((this.id >= -22 && this.id <= -20) || (this.id >= -9 && this.id <= -12)) {
            return;
        }
        if (this.zone.map.mapId == 140 || this.zone.map.mapId == 143 || this.zone.map.mapId == 131 || this.zone.map.mapId == 132 || this.zone.map.mapId == 133 || MapService.gI().isMapMaBu(this.zone.map.mapId) || MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
            return;
        }
        ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
    }

    /**
     * Xử lý đoạn hội thoại bắt đầu của Boss.
     * @return True nếu hoàn thành đoạn hội thoại
     */
    @Override
    public boolean chatS() {
        if (Util.canDoWithTime(lastTimeChatS, timeChatS)) {
            if (this.indexChatS == this.data[this.currentLevel].getTextS().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextS()[this.indexChatS];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatS = System.currentTimeMillis();
            this.timeChatS = textChat.length() * 100;
            if (this.timeChatS > 2000) {
                this.timeChatS = 2000;
            }
            this.indexChatS++;
        }
        return false;
    }

    /**
     * Hoàn thành đoạn hội thoại bắt đầu.
     */
    @Override
    public void doneChatS() {
    }

    /**
     * Xử lý đoạn hội thoại ngẫu nhiên trong trận chiến.
     */
    @Override
    public void chatM() {
        if (this.typePk == ConstPlayer.NON_PK) {
            return;
        }
        if (this.data[this.currentLevel].getTextM().length == 0) {
            return;
        }
        if (!Util.canDoWithTime(this.lastTimeChatM, this.timeChatM)) {
            return;
        }
        String textChat = this.data[this.currentLevel].getTextM()[Util.nextInt(0, this.data[this.currentLevel].getTextM().length - 1)];
        int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
        textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
        this.chat(prefix, textChat);
        this.lastTimeChatM = System.currentTimeMillis();
        this.timeChatM = Util.nextInt(3000, 20000);
    }

    /**
     * Kích hoạt trạng thái tấn công của Boss.
     */
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
    }

    /** Thời gian tấn công cuối cùng */
    protected long lastTimeAttack;

    /**
     * Xử lý hành vi tấn công của Boss.
     */
    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                if (pl != null) {
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                            } else {
                                this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                            }
                        }
                        SkillService.gI().useSkill(this, pl, null, null);
                        checkPlayerDie(pl);
                    } else {
                        if (Util.isTrue(1, 2)) {
                            this.moveToPlayer(pl);
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Kiểm tra xem người chơi có bị chết sau khi bị tấn công không.
     * @param player Người chơi bị tấn công
     */
    @Override
    public void checkPlayerDie(Player player) {
        if (player.isDie()) {
        }
    }

    /**
     * Lấy phạm vi tấn công của kỹ năng được chọn.
     * @return Khoảng cách tấn công
     */
    protected int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 752002;
    }

    /**
     * Xử lý khi Boss bị tiêu diệt.
     * @param plKill Người chơi tiêu diệt Boss
     */
    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
            ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name + ", ghê chưa ghê chưa..");
            if (this.nPoint.hpMax >= 1000000000) {
                plKill.PointBoss += 1;
            }
        }
        this.changeStatus(DIE);
    }

    /**
     * Thưởng cho người chơi khi tiêu diệt Boss.
     * @param plKill Người chơi tiêu diệt Boss
     */
    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    /**
     * Thưởng vật phẩm cho Boss tương lai.
     * @param plKill Người chơi tiêu diệt Boss
     */
    public void rewardFutureBoss(Player plKill) {
        int[] itemDos = new int[]{16, 17};
        int randomnro = new Random().nextInt(itemDos.length);
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        if (Util.isTrue(70, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemDos[randomnro], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    /**
     * Thưởng vật phẩm cho Boss rừng.
     * @param plKill Người chơi tiêu diệt Boss
     */
    public void rewardBossForest(Player plKill) {
        int random = Util.nextInt(100);
        if (random <= 40) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 674, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }

    /**
     * Xử lý đoạn hội thoại kết thúc của Boss.
     * @return True nếu hoàn thành đoạn hội thoại
     */
    @Override
    public boolean chatE() {
        if (Util.canDoWithTime(lastTimeChatE, timeChatE)) {
            if (this.indexChatE == this.data[this.currentLevel].getTextE().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextE()[this.indexChatE];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatE = System.currentTimeMillis();
            this.timeChatE = textChat.length() * 100;
            if (this.timeChatE > 2000) {
                this.timeChatE = 2000;
            }
            this.indexChatE++;
        }
        return false;
    }

    /**
     * Hoàn thành đoạn hội thoại kết thúc.
     */
    @Override
    public void doneChatE() {
    }

    /**
     * Rời khỏi bản đồ.
     */
    @Override
    public void leaveMap() {
        if (this.currentLevel < this.data.length - 1) {
            this.lastZone = this.zone;
            this.changeStatus(RESPAWN);
        } else {
            ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
            ChangeMapService.gI().exitMap(this);
            this.lastZone = null;
            this.lastTimeRest = System.currentTimeMillis();
            this.changeStatus(REST);
        }
        this.wakeupAnotherBossWhenDisappear();
    }

    /**
     * Xử lý sát thương mà Boss nhận vào.
     * @param plAtt Người chơi tấn công
     * @param damage Sát thương gây ra
     * @param piercing Có xuyên giáp hay không
     * @param isMobAttack Có phải do quái tấn công không
     * @return Sát thương thực tế
     */
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Di chuyển Boss đến vị trí của người chơi.
     * @param player Người chơi mục tiêu
     */
    @Override
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
    }

    /**
     * Tham gia bản đồ tại vị trí cụ thể.
     * @param zone Vùng bản đồ
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    public void joinMapByZone(Zone zone, int x, int y) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    /**
     * Di chuyển Boss đến vị trí cụ thể.
     * @param x Tọa độ X
     * @param y Tọa độ Y
     */
    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    /**
     * Gửi đoạn chat của Boss.
     * @param text Nội dung chat
     */
    public void chat(String text) {
        Service.gI().chat(this, text);
    }

    /**
     * Xử lý đoạn chat với tiền tố và nội dung.
     * @param prefix Tiền tố chat
     * @param textChat Nội dung chat
     * @return True nếu chat thành công
     */
    protected boolean chat(int prefix, String textChat) {
        if (prefix == -1) {
            this.chat(textChat);
        } else if (prefix == -2) {
            Player plMap = this.zone.getRandomPlayerInMap();
            if (plMap != null && !plMap.isDie() && Util.getDistance(this, plMap) <= 600) {
                Service.gI().chat(plMap, textChat);
            } else {
                return false;
            }
        } else if (prefix == -3) {
            if (this.parentBoss != null && !this.parentBoss.isDie()) {
                this.parentBoss.chat(textChat);
            }
        } else if (prefix >= 0) {
            if (this.bossAppearTogether != null && this.bossAppearTogether[this.currentLevel] != null) {
                Boss boss = this.bossAppearTogether[this.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            } else if (this.parentBoss != null && this.parentBoss.bossAppearTogether != null
                    && this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] != null) {
                Boss boss = this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            }
        }
        return true;
    }

    /**
     * Kích hoạt các Boss khác khi Boss này xuất hiện.
     */
    @Override
    public void wakeupAnotherBossWhenAppear() {
        if (!MapService.gI().isMapMaBu(this.zone.map.mapId) && MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
        }
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            int nextLevelBoss = boss.currentLevel + 1;
            if (nextLevelBoss >= boss.data.length) {
                nextLevelBoss = 0;
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.CALL_BY_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.APPEAR_WITH_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
                boss.changeStatus(RESPAWN);
            }
        }
    }

    /**
     * Kích hoạt các Boss khác khi Boss này biến mất.
     */
    @Override
    public void wakeupAnotherBossWhenDisappear() {
    }
}