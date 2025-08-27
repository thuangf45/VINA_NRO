package com.girlkun.models.player;

/**
 * Lớp đại diện cho thông tin bạn bè của người chơi trong game.
 * @author Lucifer
 */
public class Friend {

    /** ID của người bạn. */
    public int id;

    /** Tên của người bạn. */
    public String name;

    /** ID của đầu (head) nhân vật bạn. */
    public short head;

    /** ID của thân (body) nhân vật bạn. */
    public short body;

    /** ID của chân (leg) nhân vật bạn. */
    public short leg;

    /** ID của túi (bag) nhân vật bạn. */
    public byte bag;

    /** Sức mạnh của người bạn. */
    public long power;

    /** Trạng thái trực tuyến của người bạn. */
    public boolean online;
}