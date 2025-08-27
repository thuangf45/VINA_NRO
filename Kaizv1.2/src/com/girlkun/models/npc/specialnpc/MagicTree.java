package com.girlkun.models.npc.specialnpc;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.network.io.Message;
import com.girlkun.services.ItemService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.utils.Logger;

/**
 * MagicTree - Cây Đậu Thần
 *
 * <p>Quản lý trạng thái và hành vi của "Cây Đậu Thần" thuộc về một Player:
 * - Sinh hạt (pea) theo thời gian.
 * - Cho phép người chơi thu hoạch hạt.
 * - Hỗ trợ nâng cấp để tăng tốc/số lượng hạt sinh ra.
 * - Hỗ trợ nâng cấp nhanh, huỷ nâng cấp, và các menu tương tác gửi xuống client.</p>
 *
 * <p>Ghi chú về các cấu trúc dữ liệu:
 * - PEA_TEMP: template item id của hạt theo cấp cây (index = level - 1).
 * - PEA_PARAM: tham số dùng để tạo ItemOption cho hạt (index = level - 1).
 * - POS_PEAS: danh sách vị trí (x,y) hiển thị hạt cho từng cấp.
 * - PEA_UPGRADE: {days, hours, minutes, gold} - thông số thời gian & chi phí nâng cấp cho từng cấp.</p>
 *
 * @author Lucifer
 */
public class MagicTree {

    /** Cấp độ tối đa của cây */
    public static final byte MAX_LEVEL = 10;

    /** Template ID của hạt đậu theo cấp (index = level - 1) */
    public static final short[] PEA_TEMP = {13, 60, 61, 62, 63, 64, 65, 352, 523, 595};

    /** Tham số map cho item option của hạt tương ứng từng cấp */
    public static final int[] PEA_PARAM = {100, 500, 2, 4, 8, 16, 32, 64, 128, 256};

    /**
     * Vị trí hiển thị hạt trên cây theo từng cấp.
     * Mỗi phần tử là một mảng các cặp {x, y}.
     * POS_PEAS[level - 1] chứa các vị trí hiển thị hạt ở cấp tương ứng.
     */
    private static final int[][][] POS_PEAS = {
        {{19, 22}, {-1, 16}, {3, 10}, {19, 8}, {9, 0}},
        {{-1, 27}, {22, 35}, {15, 24}, {0, 17}, {-1, 7}, {26, 5}, {5, 0}},
        {{25, 41}, {-1, 40}, {25, 34}, {3, 32}, {25, 23}, {10, 19}, {2, 12}, {17, 10}, {4, 5}},
        {{3, 44}, {21, 49}, {25, 39}, {4, 30}, {29, 25}, {0, 18}, {21, 15}, {14, 39}, {18, 25}, {4, 7}, {15, 0}},
        {{21, 58}, {0, 56}, {18, 48}, {10, 0}, {25, 38}, {0, 26}, {14, 28}, {25, 16}, {1, 14}, {22, 7}, {10, 14}, {28, 23}, {15, 16}},
        {{25, 63}, {0, 66}, {21, 52}, {3, 55}, {14, 60}, {3, 45}, {22, 43}, {10, 35}, {22, 28}, {3, 28}, {18, 17}, {3, 14}, {17, 6}, {11, 22}, {6, 1}},
        {{32, 86}, {5, 77}, {25, 77}, {8, 89}, {29, 68}, {4, 63}, {18, 61}, {33, 53}, {8, 48}, {26, 39}, {11, 36}, {33, 23}, {18, 25}, {4, 20}, {26, 12}, {12, 7}, {19, 0}},
        {{32, 86}, {5, 77}, {25, 77}, {8, 89}, {29, 68}, {4, 63}, {18, 61}, {33, 53}, {8, 48}, {26, 39}, {11, 36}, {33, 23}, {18, 25}, {4, 20}, {26, 12}, {12, 7}, {19, 0}, {19, 0}, {19, 0}},
        {{32, 86}, {5, 77}, {25, 77}, {8, 89}, {29, 68}, {4, 63}, {18, 61}, {33, 53}, {8, 48}, {26, 39}, {11, 36}, {33, 23}, {18, 25}, {4, 20}, {26, 12}, {12, 7}, {19, 0}, {19, 0}, {19, 0}, {19, 0}, {19, 0}},
        {{32, 86}, {5, 77}, {25, 77}, {8, 89}, {29, 68}, {4, 63}, {18, 61}, {33, 53}, {8, 48}, {26, 39}, {11, 36}, {33, 23}, {18, 25}, {4, 20}, {26, 12}, {12, 7}, {19, 0}, {19, 0}, {19, 0}, {19, 0}, {19, 0}, {19, 0}, {19, 0}}
    };

    /**
     * Thông số nâng cấp theo cấp hiện tại:
     * Mỗi dòng = {days, hours, minutes, gold}
     * Lưu ý: giá gold trong mảng có thể được hiểu theo scale (ví dụ "k" hoặc "Tr") tùy cách tính trong code.
     */
    private static final short[][] PEA_UPGRADE = {
        {0, 0, 10, 5}, {0, 1, 40, 10}, {0, 16, 40, 100}, {6, 22, 0, 1},
        {13, 21, 0, 10}, {27, 18, 0, 20}, {55, 13, 0, 50}, {69, 10, 0, 100},
        {104, 4, 0, 300}, {0, 0, 0, 0}
    };

    /** Icon ID hiển thị cây theo [gender][levelIndex] */
    private static final short[][] ID_MAGIC_TREE = {
        {84, 85, 86, 87, 88, 89, 90, 90, 90, 90},
        {371, 372, 373, 374, 375, 376, 377, 377, 377, 377},
        {378, 379, 380, 381, 382, 383, 384, 384, 384, 384}
    };

    /** Vị trí icon cây theo gender: {x, y} */
    private static final short[][] POS_MAGIC_TREE = {{348, 336}, {372, 336}, {348, 336}};

    /** Cờ cho biết cây đã gửi dữ liệu lần đầu cho player chưa (để tránh gửi lặp) */
    private boolean loadedMagicTreeToPlayer;

    /** Player sở hữu cây này */
    private Player player;

    /** (không dùng phổ biến trong code gốc) state để bật/tắt chức năng - giữ nguyên vì logic gốc có field này */
    private boolean actived;

    /** Cấp hiện tại của cây (1..MAX_LEVEL) */
    public byte level;

    /** Số hạt hiện có (capped bởi getMaxPea()) */
    public int currPeas;

    /** Cờ đang trong trạng thái nâng cấp hay không */
    public boolean isUpgrade;

    /** Timestamp lần thu hoạch cuối (millis) */
    public long lastTimeHarvest;

    /** Timestamp lần bắt đầu nâng cấp cuối (millis) */
    public long lastTimeUpgrade;

    /**
     * Constructor
     *
     * @param player           người chơi sở hữu cây
     * @param level            cấp cây (byte)
     * @param currPeas         số hạt hiện có (byte)
     * @param lastTimeHarvest  thời gian thu hoạch cuối cùng (ms)
     * @param isUpgrade        đang nâng cấp hay không
     * @param lastTimeUpgrade  thời gian bắt đầu nâng cấp cuối (ms)
     */
    public MagicTree(Player player, byte level, byte currPeas, long lastTimeHarvest, boolean isUpgrade, long lastTimeUpgrade) {
        this.player = player;
        this.level = level;
        this.currPeas = currPeas;
        if (this.currPeas > this.getMaxPea()) {
            this.currPeas = this.getMaxPea();
        }
        this.isUpgrade = isUpgrade;
        this.lastTimeHarvest = lastTimeHarvest;
        this.lastTimeUpgrade = lastTimeUpgrade;
    }

    /**
     * Cập nhật trạng thái cây theo thời gian:
     * - Nếu không đang nâng cấp: tính số hạt sinh ra kể từ lastTimeHarvest.
     *   + numPeaRelease = elapsedSeconds / getSecondPerPea()
     *   + cập nhật currPeas và lastTimeHarvest tương ứng.
     * - Nếu đang nâng cấp: kiểm tra thời gian nâng cấp đã đủ (Util.canDoWithTime)
     *   + nếu đủ: tăng level (nếu < MAX_LEVEL) và set isUpgrade = false.
     */
    public void update() {
        if (!isUpgrade) {
            if (this.currPeas < this.getMaxPea()) {
                int timeThrow = (int) ((System.currentTimeMillis() - lastTimeHarvest) / 1000);
                int numPeaRelease = timeThrow / getSecondPerPea();
                if (numPeaRelease > 0) {
                    this.currPeas += numPeaRelease;
                    if (this.currPeas >= this.getMaxPea()) {
                        this.currPeas = this.getMaxPea();
                        this.lastTimeHarvest = System.currentTimeMillis();
                    } else {
                        this.lastTimeHarvest += (numPeaRelease * getSecondPerPea()) * 1000;
                    }
                }
            }
        } else {
            if (Util.canDoWithTime(lastTimeUpgrade, getTimeUpgrade())) {
                if (this.level < MAX_LEVEL) {
                    this.level++;
                }
                this.isUpgrade = false;
            }
        }
    }

    /**
     * Gửi (load) dữ liệu cây đậu xuống client để hiển thị UI cây đậu.
     * Message nội dung theo giao thức:
     * - writer().writeByte(0) : code load tree
     * - writeShort(ID_MAGIC_TREE[player.gender][level - 1]) : icon
     * - writeUTF("Đậu thần cấp " + level) : title
     * - writeShort(POS_MAGIC_TREE[player.gender][0/1]) : vị trí icon
     * - writeByte(level) : level
     * - writeShort(this.currPeas) : số hạt hiện có
     * - writeShort(getMaxPea()) : số hạt tối đa
     * - writeUTF(...) : mô tả
     * - writeInt(seconds) : seconds đến event tiếp theo (upgrade hoặc pea)
     * - writeByte(pos pea length) + list pos pea : vị trí hiển thị các hạt
     * - writeBoolean(this.isUpgrade) : cờ nâng cấp
     */
    public void loadMagicTree() {
        Message msg;
        try {
            msg = new Message(-34);
            msg.writer().writeByte(0);

            msg.writer().writeShort(ID_MAGIC_TREE[player.gender][level - 1]);

            msg.writer().writeUTF("Đậu thần cấp " + level);
            msg.writer().writeShort(POS_MAGIC_TREE[player.gender][0]);
            msg.writer().writeShort(POS_MAGIC_TREE[player.gender][1]);
            msg.writer().writeByte(level);
            msg.writer().writeShort(this.currPeas);
            msg.writer().writeShort(getMaxPea());
            msg.writer().writeUTF("Đang kết hạt\nCây lớn sinh nhiều hạt hơn");
            msg.writer().writeInt(this.isUpgrade ? getSecondUpgrade() : getSecondPea()); //seconds
            msg.writer().writeByte(POS_PEAS[this.level - 1].length); //pos pea
            for (int i = 0; i < POS_PEAS[this.level - 1].length; i++) {
                msg.writer().writeByte(POS_PEAS[this.level - 1][i][0]);
                msg.writer().writeByte(POS_PEAS[this.level - 1][i][1]);
            }
            msg.writer().writeBoolean(this.isUpgrade);
            player.sendMessage(msg);
            msg.cleanup();
            if (!loadedMagicTreeToPlayer) {
                loadedMagicTreeToPlayer = true;
            }
        } catch (Exception e) {
            Logger.logException(MagicTree.class, e);
        }
    }

    /**
     * Tạo menu tương tác cây đậu gửi xuống client.
     * - Nếu không đang nâng cấp: hiển thị "Thu hoạch", (nếu chưa max) "Kết hạt nhanh 4 ngọc" và text nâng cấp nếu level < MAX_LEVEL.
     * - Nếu đang nâng cấp: hiển thị "Nâng cấp nhanh 9 ngọc" và "Hủy nâng cấp hồi X vàng".
     * - Thiết lập player.iDMark index menu tương ứng để xử lý lựa chọn sau này.
     */
    public void openMenuTree() {
        Message msg;
        try {
            msg = new Message(-34);
            msg.writer().writeByte(1);
            if (!isUpgrade) {
                msg.writer().writeUTF("Thu\nhoạch");
                if (this.level < MAX_LEVEL) {
                    msg.writer().writeUTF(getTextMenuUpgrade());
                }
                if (this.currPeas < this.getMaxPea()) {
                    msg.writer().writeUTF("Kết hạt\nnhanh\n4 ngọc");
                    this.player.iDMark.setIndexMenu(ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA);
                } else {
                    this.player.iDMark.setIndexMenu(ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA);
                }
            } else {
                msg.writer().writeUTF("Nâng cấp\nnhanh\n9\nngọc");
                msg.writer().writeUTF("Hủy\nnâng cấp\nhồi " + (PEA_UPGRADE[this.level - 1][3] / 2 + (this.level <= 3 ? " k" : " Tr")) + "\nvàng");
                this.player.iDMark.setIndexMenu(ConstNpc.MAGIC_TREE_UPGRADE);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MagicTree.class, e);
        }
    }

    /**
     * Thu hoạch hạt:
     * - Kiểm tra currPeas > 0.
     * - Gọi addPeaHarvest(level, currPeas) để tạo item và trả số lượng còn lại.
     * - Nếu số lượng còn lại thay đổi, cập nhật lastTimeHarvest và gửi thông tin item & thời gian về client.
     */
    public void harvestPea() {
        if (this.currPeas > 0) {
            byte currPeasTemp = (byte) this.currPeas;
            this.currPeas = (byte) this.addPeaHarvest(this.level, this.currPeas);
            if (this.currPeas == currPeasTemp) {
                return;
            }
            this.lastTimeHarvest = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Message msg;
            try {
                msg = new Message(-34);
                msg.writer().writeByte(2);
                msg.writer().writeShort(this.currPeas);
                msg.writer().writeInt(getSecondPea());
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(MagicTree.class, e);
            }
        }
    }

    /**
     * Hiển thị menu xác nhận nâng cấp (gửi menu từ NPC ĐẬU_THẦN trên map của player).
     */
    public void showConfirmUpgradeMagicTree() {
        NpcManager.getByIdAndMap(ConstNpc.DAU_THAN, this.player.zone.map.mapId).
                createOtherMenu(player, ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE, "Bạn có chắc chắn nâng cấp cây đậu?", "OK", "Từ chối");
    }

    /**
     * Hiển thị menu xác nhận hủy nâng cấp (gửi menu từ NPC ĐẬU_THẦN trên map của player).
     */
    public void showConfirmUnuppgradeMagicTree() {
        NpcManager.getByIdAndMap(ConstNpc.DAU_THAN, this.player.zone.map.mapId).
                createOtherMenu(player, ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE, "Bạn có chắc chắn hủy nâng cấp cây đậu?", "OK", "Từ chối");
    }

    /**
     * Bắt đầu nâng cấp cây:
     * - Tính goldRequired dựa trên PEA_UPGRADE[currentLevel - 1][3] và scale (level <= 3 ? * 1000 : * 1000000).
     * - Nếu người chơi đủ vàng: trừ vàng, set isUpgrade = true, lastTimeUpgrade = now, và gửi loadMagicTree().
     * - Nếu không đủ vàng: gửi thông báo còn thiếu.
     */
    public void upgradeMagicTree() {
        short gold = PEA_UPGRADE[this.level - 1][3];
        int goldRequire = gold * (this.level <= 3 ? 1000 : 1000000);
        if (this.player.inventory.gold < goldRequire) {
            Service.gI().sendThongBao(player, "Bạn không đủ vàng để nâng cấp, còn thiếu "
                    + (goldRequire - this.player.inventory.gold) + " vàng nữa");
        } else {
            this.player.inventory.gold -= goldRequire;
            PlayerService.gI().sendInfoHpMpMoney(this.player);
            this.isUpgrade = true;
            this.lastTimeUpgrade = System.currentTimeMillis();
            this.loadMagicTree();
        }
    }

    /**
     * Hủy nâng cấp:
     * - Trả lại 50% của chi phí nâng cấp (đã scale) vào inventory.gold.
     * - Set isUpgrade = false và reload UI bằng loadMagicTree().
     */
    public void unupgradeMagicTree() {
        short gold = PEA_UPGRADE[this.level - 1][3];
        int goldReturn = (gold * (this.level <= 3 ? 1000 : 1000000)) / 2;
        this.player.inventory.gold += goldReturn;
        PlayerService.gI().sendInfoHpMpMoney(this.player);
        this.isUpgrade = false;
        this.loadMagicTree();
    }

    /**
     * Kết hạt nhanh: đặt currPeas = getMaxPea() và reload UI.
     * (Giá quy đổi / kiểm tra vật phẩm thực hiện ở chỗ gọi method này trong menu)
     */
    public void fastRespawnPea() {
        //giá kết hạt nhanh
        this.currPeas = this.getMaxPea();
        this.loadMagicTree();
    }

    /**
     * Nâng cấp nhanh: tăng level ngay nếu level < MAX_LEVEL, huỷ cờ nâng cấp và reload UI.
     * (Giá quy đổi / kiểm tra vật phẩm thực hiện ở chỗ gọi method này trong menu)
     */
    public void fastUpgradeMagicTree() {
        //giá nâng cấp nhanh
        if (this.level < MAX_LEVEL) {
            this.level++;
        }
        this.isUpgrade = false;
        this.loadMagicTree();
    }

    /**
     * Tính số hạt tối đa theo cấp:
     * formula: (level - 1) * 2 + 5
     *
     * @return max peas for current level
     */
    private byte getMaxPea() {
        return (byte) ((this.level - 1) * 2 + 5);
    }

    /**
     * Thời gian (giây) để ra 1 hạt, phụ thuộc vào level:
     * formula: level * 60 (giây)
     *
     * @return seconds per pea
     */
    private short getSecondPerPea() {
        return (short) (this.level * 60);
    }

    /**
     * Tính số giây còn lại để ra hạt tiếp theo (dựa trên lastTimeHarvest và getSecondPerPea).
     *
     * @return seconds left (>= 0)
     */
    private int getSecondPea() {
        short secondPerPea = (short) getSecondPerPea();
        long timePeaRelease = lastTimeHarvest + secondPerPea * 1000;
        int secondLeft = (int) ((timePeaRelease - System.currentTimeMillis()) / 1000);
        return secondLeft < 0 ? 0 : secondLeft;
    }

    /**
     * Tính số giây còn lại để nâng cấp hoàn thành:
     *
     * @return seconds left for upgrade (>= 0)
     */
    private int getSecondUpgrade() {
        return (int) ((lastTimeUpgrade + getTimeUpgrade() - System.currentTimeMillis()) / 1000);
    }

    /**
     * Xây dựng chuỗi mô tả Menu Nâng cấp (hiển thị thời gian + giá).
     *
     * @return text to display in menu
     */
    private String getTextMenuUpgrade() {
        String text = "Nâng cấp\n";
        short d = PEA_UPGRADE[this.level - 1][0];
        short h = PEA_UPGRADE[this.level - 1][1];
        short m = PEA_UPGRADE[this.level - 1][2];
        short gold = PEA_UPGRADE[this.level - 1][3];
        if (d != 0) {
            text += d + "d";
        }
        if (h != 0) {
            text += h + "h";
        }
        if (m != 0) {
            text += m + "'";
        }
        text += "\n" + gold + (this.level <= 3 ? " k" : " Tr") + "\nvàng";
        return text;
    }

    /**
     * Quy đổi thông số PEA_UPGRADE[level - 1] thành thời gian (ms).
     *
     * @return upgrade time in milliseconds
     */
    private long getTimeUpgrade() {
        short d = PEA_UPGRADE[this.level - 1][0];
        short h = PEA_UPGRADE[this.level - 1][1];
        short m = PEA_UPGRADE[this.level - 1][2];
        return d * 24 * 60 * 60 * 1000L + h * 60 * 60 * 1000L + m * 60 * 1000L;
    }
    
    /**
     * Giải phóng tham chiếu tới player (khi dispose/clear object).
     */
    public void dispose(){
        this.player = null;
    }
   
    /**
     * Thêm item hạt (pea) vào túi người chơi khi thu hoạch.
     * - Tạo item dựa theo PEA_TEMP[level - 1] với quantity tương ứng.
     * - Thêm ItemOption tùy cấp: nếu level - 1 > 1 dùng option type 2, else dùng 48.
     * - Gọi InventoryServiceNew.gI().addItemBag để thêm vào túi.
     * - Nếu pea.quantity > 0 thì addItemBox (logic gốc giữ nguyên).
     * - Nếu pea.quantity < quantity => gửi thông báo cho player về số lượng thu được.
     *
     * @param level    cấp cây (byte)
     * @param quantity số lượng hạt muốn thu (int)
     * @return số hạt thực tế đã thêm (pea.quantity)
     */
    public int addPeaHarvest(byte level, int quantity) {
        Item pea = ItemService.gI().createNewItem(MagicTree.PEA_TEMP[level - 1], quantity);
        pea.itemOptions.add(new Item.ItemOption(level - 1 > 1 ? 2 : 48, MagicTree.PEA_PARAM[level - 1]));
        InventoryServiceNew.gI().addItemBag(player, pea);
        if (pea.quantity > 0) {
            InventoryServiceNew.gI().addItemBox(player, pea);
        }
        if (pea.quantity < quantity) {
            Service.gI().sendThongBao(player, "Bạn vừa thu hoạch được " + (quantity - pea.quantity) + " hạt " + pea.template.name);
        }
        return pea.quantity;
    }

}
