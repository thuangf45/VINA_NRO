package com.girlkun.models.player;

/**
 * Lớp đại diện cho các thuộc tính chỉ số của người chơi trong game, bao gồm sức mạnh, tiềm năng, 
 * máu, năng lượng, sát thương, phòng thủ và tỷ lệ chí mạng.
 * @author Lucifer
 */
public class CPoint {
    
    /** Giới hạn sức mạnh của người chơi. */
    public byte limitPower;
    
    /** Tốc độ di chuyển hoặc hành động của người chơi. */
    public byte speed;

    /** Sức mạnh hiện tại của người chơi. */
    public long power;

    /** Tiềm năng hiện tại của người chơi. */
    public long potential;
    
    /** Thể lực hiện tại của người chơi. */
    public int stamina;

    /** Thể lực tối đa của người chơi. */
    public int maxStamina;
    
    /** Máu hiện tại của người chơi. */
    public long hp;

    /** Máu tối đa của người chơi. */
    public long maxHp;

    /** Máu gốc (ban đầu) của người chơi. */
    public long oriHp;
    
    /** Năng lượng (ki) hiện tại của người chơi. */
    public long ki;

    /** Năng lượng tối đa của người chơi. */
    public long maxKi;

    /** Năng lượng gốc (ban đầu) của người chơi. */
    public long oriKi;
    
    /** Sát thương hiện tại của người chơi. */
    public int dame;

    /** Sát thương gốc (ban đầu) của người chơi. */
    public int oriDame;
    
    /** Phòng thủ hiện tại của người chơi. */
    public int def;

    /** Phòng thủ gốc (ban đầu) của người chơi. */
    public int oriDef;
    
    /** Tỷ lệ chí mạng hiện tại của người chơi. */
    public int crit;

    /** Tỷ lệ chí mạng gốc (ban đầu) của người chơi. */
    public int oriCrit;
}