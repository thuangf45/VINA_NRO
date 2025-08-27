package com.girlkun.models.player;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.shop.Shop;
import lombok.Data;

/**
 * Lớp quản lý các thông tin trạng thái và đánh dấu (ID mark) của người chơi trong game, 
 * bao gồm các trạng thái giao dịch, thách đấu, giữ ngọc rồng, và các hoạt động khác.
 * @author Lucifer
 */
@Data
public class IDMark {

    /** ID của vật phẩm đang nâng cấp lên cấp độ cao nhất. */
    private int idItemUpTop;

    /** Loại phương thức thay đổi bản đồ (capsule, ngọc rồng đen, ...). */
    private int typeChangeMap;

    /** Chỉ số menu NPC đang tương tác. */
    private int indexMenu;

    /** Loại dữ liệu đầu vào (input). */
    private int typeInput;

    /** Loại vòng quay may mắn. */
    private byte typeLuckyRound;

    /** ID của người chơi được mời thách đấu. */
    private long idPlayThachDau;

    /** Số vàng dùng để thách đấu. */
    private int goldThachDau;

    /** ID của kẻ thù để trả thù. */
    private long idEnemy;

    /** Cửa hàng mà người chơi đang mở. */
    private Shop shopOpen;

    /** Thẻ tên của cửa hàng đang mở. */
    private String tagNameShop;

    /** Trạng thái đi đến hành tinh Gas. */
    private boolean goToGas;

    /** Thời điểm cuối cùng đi đến hành tinh Gas. */
    private long lastTimeGotoGas;

    /** Loại tàu vận chuyển (0: không dùng, 1: tàu vũ trụ, 2: dịch chuyển tức thời, 3: tàu Tennis). */
    private byte idSpaceShip;

    /** Thời điểm cuối cùng bị cấm. */
    private long lastTimeBan;

    /** Trạng thái bị cấm của người chơi. */
    private boolean isBan;

    /** ID của người chơi đang giao dịch. */
    private int playerTradeId = -1;

    /** Đối tượng người chơi đang giao dịch. */
    private Player playerTrade;

    /** Thời điểm cuối cùng thực hiện giao dịch. */
    private long lastTimeTrade;

    /** Thời điểm cuối cùng thông báo thời gian giữ ngọc rồng đen. */
    private long lastTimeNotifyTimeHoldBlackBall;

    /** Thời điểm cuối cùng giữ ngọc rồng đen. */
    private long lastTimeHoldBlackBall;

    /** ID tạm thời của ngọc rồng đen đang giữ. */
    private int tempIdBlackBallHold = -1;

    /** Trạng thái giữ ngọc rồng đen. */
    private boolean holdBlackBall;

    /** ID tạm thời của ngọc rồng Namek đang giữ. */
    private int tempIdNamecBallHold = -1;

    /** Trạng thái giữ ngọc rồng Namek. */
    private boolean holdNamecBall;

    /** Trạng thái đã tải toàn bộ dữ liệu người chơi từ cơ sở dữ liệu. */
    private boolean loadedAllDataPlayer;

    /** Thời điểm cuối cùng thay đổi cờ (flag). */
    private long lastTimeChangeFlag;

    /** Trạng thái đi tới tương lai. */
    private boolean gotoFuture;

    /** Thời điểm cuối cùng đi tới tương lai. */
    private long lastTimeGoToFuture;

    /** Thời điểm cuối cùng thay đổi khu vực (zone). */
    private long lastTimeChangeZone;

    /** Thời điểm cuối cùng chat toàn server. */
    private long lastTimeChatGlobal;

    /** Thời điểm cuối cùng chat riêng tư. */
    private long lastTimeChatPrivate;

    /** Thời điểm cuối cùng nhặt vật phẩm. */
    private long lastTimePickItem;

    /** Trạng thái đi đến bản đồ Băng Đảo Kame. */
    private boolean goToBDKB;

    /** Thời điểm cuối cùng đi đến bản đồ Băng Đảo Kame. */
    private long lastTimeGoToBDKB;

    /** Thời điểm cuối cùng bị ăn xiên bẫy ở bản đồ Băng Đảo Kame. */
    private long lastTimeAnXienTrapBDKB;

    /** Trạng thái đi đến bản đồ Kho Báu. */
    private boolean goToKG;

    /** Thời điểm cuối cùng đi đến bản đồ Kho Báu. */
    private long lastTimeGoToKG;

    /** Thời điểm cuối cùng bị ăn xiên bẫy ở bản đồ Kho Báu. */
    private long lastTimeAnXienTrapKG;

    /** NPC mà người chơi đang tương tác. */
    private Npc npcChose;

    /** Loại thẻ nạp của người chơi. */
    private byte loaiThe;

    /** Thời điểm cuối cùng sử dụng kỹ năng đặc biệt. */
    private long lastTimeSkillSpecial;

    /**
     * Kiểm tra xem người chơi có đang ở menu chính của NPC hay không.
     *
     * @return true nếu đang ở menu chính, false nếu không.
     */
    public boolean isBaseMenu() {
        return this.indexMenu == ConstNpc.BASE_MENU;
    }

    /**
     * Giải phóng tài nguyên của đối tượng IDMark, bao gồm cửa hàng và các tham chiếu khác.
     */
    public void dispose() {
        if (this.shopOpen != null) {
            this.shopOpen.dispose();
            this.shopOpen = null;
        }
        this.npcChose = null;
        this.tagNameShop = null;
        this.playerTrade = null;
    }
}