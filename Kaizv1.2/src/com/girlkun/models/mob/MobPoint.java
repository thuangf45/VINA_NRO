package com.girlkun.models.mob;

import com.girlkun.utils.Util;

/**
 * Lớp quản lý các thuộc tính sức mạnh của quái vật (mob) trong game
 * @author Lucifer
 */
public class MobPoint {

    /** Đối tượng quái vật liên quan */
    public final Mob mob;
    /** Máu hiện tại của quái vật */
    public int hp;
    /** Máu tối đa của quái vật */
    public int maxHp;
    /** Sát thương cơ bản của quái vật */
    public int dame;

    /** Constructor khởi tạo MobPoint với đối tượng quái vật */
    public MobPoint(Mob mob) {
        this.mob = mob;
    }

    /** Lấy giá trị máu tối đa của quái vật */
    public int getHpFull() {
        return maxHp;
    }

    /** Thiết lập máu tối đa cho quái vật */
    public void setHpFull(int hp) {
        maxHp = hp;
    }

    /** Lấy giá trị máu hiện tại của quái vật */
    public int gethp() {
        return hp;
    }

    /** Thiết lập máu hiện tại cho quái vật, đảm bảo không âm */
    public void sethp(int hp) {
        if (this.hp < 0) {
            /** Đặt máu về 0 nếu giá trị âm */
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    /** Tính toán sát thương tấn công của quái vật */
    public int getDameAttack() {
        return this.dame != 0 
            ? this.dame + Util.nextInt(-(this.dame / 100), (this.dame / 100))
            : this.getHpFull() * Util.nextInt(mob.pDame - 1, mob.pDame + 1) / 100
                + Util.nextInt(-(mob.level * 10), mob.level * 10);
    }
}