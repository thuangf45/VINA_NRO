package com.girlkun.models.boss.iboss;

/**
 * Interface định nghĩa các thuộc tính liên quan đến ngoại hình của boss trong game.
 * @author Lucifer
 */
public interface IBossOutfit {

    /**
     * Lấy ID của phần đầu (head) trong bộ ngoại hình của boss.
     * @return ID của phần đầu
     */
    short getHead();

    /**
     * Lấy ID của phần thân (body) trong bộ ngoại hình của boss.
     * @return ID của phần thân
     */
    short getBody();

    /**
     * Lấy ID của phần chân (leg) trong bộ ngoại hình của boss.
     * @return ID của phần chân
     */
    short getLeg();

    /**
     * Lấy ID của túi cờ (flag bag) trong bộ ngoại hình của boss.
     * @return ID của túi cờ
     */
    short getFlagBag();

    /**
     * Lấy ID của hiệu ứng hào quang (aura) xung quanh boss.
     * @return ID của hiệu ứng hào quang
     */
    byte getAura();

    /**
     * Lấy ID của hiệu ứng phía trước (effect front) của boss.
     * @return ID của hiệu ứng phía trước
     */
    byte getEffFront();
}