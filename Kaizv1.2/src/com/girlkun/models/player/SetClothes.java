package com.girlkun.models.player;

import com.girlkun.models.item.Item;

/**
 * Lớp SetClothes quản lý các bộ trang phục và chỉ số liên quan của người chơi trong game.
 * Lớp này kiểm tra và thiết lập các bộ trang phục đặc biệt (như Hủy Diệt, Thần Linh, Thiên Sứ)
 * dựa trên các vật phẩm mà người chơi đang mặc, đồng thời theo dõi các chỉ số của các bộ trang phục.
 * 
 * @author Lucifer
 */
public class SetClothes {

    /**
     * Người chơi được gán với hệ thống quản lý trang phục.
     */
    private Player player;

    /**
     * Số lượng vật phẩm thuộc bộ Songoku.
     */
    public byte songoku;

    /**
     * Số lượng vật phẩm thuộc bộ Thiên Xin Hăng.
     */
    public byte thienXinHang;

    /**
     * Số lượng vật phẩm thuộc bộ Kirin.
     */
    public byte kirin;

    /**
     * Trạng thái kích hoạt bộ trang phục Hủy Diệt.
     */
    public boolean huydietClothers;

    /**
     * Trạng thái kích hoạt bộ trang phục Thần Linh.
     */
    public boolean thanlinhClothers;

    /**
     * Số lượng vật phẩm thuộc bộ Ốc Tiêu.
     */
    public byte ocTieu;

    /**
     * Số lượng vật phẩm thuộc bộ Pikkoro Daimao.
     */
    public byte pikkoroDaimao;

    /**
     * Số lượng vật phẩm thuộc bộ Picolo.
     */
    public byte picolo;

    /**
     * Số lượng vật phẩm thuộc bộ Kakarot.
     */
    public byte kakarot;

    /**
     * Số lượng vật phẩm thuộc bộ Cadic.
     */
    public byte cadic;

    /**
     * Số lượng vật phẩm thuộc bộ Nappa.
     */
    public byte nappa;

    /**
     * Số lượng vật phẩm thuộc bộ World Cup.
     */
    public byte worldcup;

    /**
     * Số lượng vật phẩm thuộc bộ Đại Hội Võ Thuật (DHD).
     */
    public byte setDHD;

    /**
     * Số lượng vật phẩm thuộc bộ Thiên Sứ.
     */
    public byte thienSuClothes = 0;

    /**
     * Trạng thái kích hoạt bộ trang phục Thần (God Clothes).
     */
    public boolean godClothes;

    /**
     * ID của cải trang Hải Tặc, mặc định là -1 nếu không có cải trang.
     */
    public int ctHaiTac = -1;

    /**
     * Khởi tạo một đối tượng SetClothes cho một người chơi cụ thể.
     * 
     * @param player Người chơi được gán với hệ thống quản lý trang phục.
     */
    public SetClothes(Player player) {
        this.player = player;
    }

    /**
     * Thiết lập các trạng thái và chỉ số của bộ trang phục dựa trên vật phẩm mà người chơi đang mặc.
     * Kiểm tra và kích hoạt các bộ trang phục như Hủy Diệt, Thần Linh, và Thần.
     */
    public void setup() {
        setDefault();
        setupSKT();
        this.huydietClothers = true;
        this.thanlinhClothers = true;
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 662 || item.template.id < 650) {
                    this.huydietClothers = false;
                    break;
                }
            } else {
                this.huydietClothers = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;
            }
        }
    }

    /**
     * Kiểm tra và kích hoạt bộ trang phục Hủy Diệt dựa trên các vật phẩm người chơi đang mặc.
     * 
     * @return true nếu bộ Hủy Diệt được kích hoạt, false nếu không.
     */
    public boolean setHuyDiet() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 650 && item.template.id <= 662) {
                    i++;
                } else if (i == 5) {
                    this.huydietClothers = true;
                    break;
                }
            } else {
                this.huydietClothers = false;
                break;
            }
        }
        return this.huydietClothers ? true : false;
    }

    /**
     * Kiểm tra và kích hoạt bộ trang phục Thần Linh dựa trên các vật phẩm người chơi đang mặc.
     * 
     * @return true nếu bộ Thần Linh được kích hoạt, false nếu không.
     */
    public boolean setThanLinh() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 555 && item.template.id <= 567) {
                    i++;
                } else if (i == 5) {
                    this.thanlinhClothers = true;
                    break;
                }
            } else {
                this.thanlinhClothers = false;
                break;
            }
        }
        return this.thanlinhClothers ? true : false;
    }

    /**
     * Kiểm tra xem người chơi có đang mặc đủ bộ trang phục Thiên Sứ hay không, dựa trên giới tính.
     * 
     * @return true nếu bộ Thiên Sứ được kích hoạt, false nếu không.
     */
    public boolean IsSetThienSu() {
        int[][] DoThienSu = new int[][]{
            {1048, 1051, 1054, 1057, 1060}, // Trái Đất
            {1049, 1052, 1055, 1058, 1060}, // Namec
            {1050, 1053, 1056, 1059, 1060}  // Xayda
        };
        int z = 0;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id == DoThienSu[this.player.gender][i]) {
                    z++;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return z == 5;
    }

    /**
     * Kiểm tra xem người chơi có đang mặc đủ bộ trang phục Hủy Diệt hay không, dựa trên giới tính.
     * 
     * @return true nếu bộ Hủy Diệt được kích hoạt, false nếu không.
     */
    public boolean IsSetHuyDiet() {
        int[][] DoHuyDiet = new int[][]{
            {650, 651, 657, 658, 656}, // Trái Đất
            {652, 653, 659, 660, 656}, // Namec
            {654, 655, 661, 662, 656}  // Xayda
        };
        int z = 0;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id == DoHuyDiet[this.player.gender][i]) {
                    z++;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return z == 5;
    }

    /**
     * Kiểm tra xem người chơi có đang mặc đủ bộ trang phục Thần Linh hay không, dựa trên giới tính.
     * 
     * @return true nếu bộ Thần Linh được kích hoạt, false nếu không.
     */
    public boolean IsSetThanLinh() {
        int[][] DoHuyDiet = new int[][]{
            {555, 556, 562, 563, 561}, // Trái Đất
            {557, 558, 564, 565, 561}, // Namec
            {559, 569, 566, 567, 561}  // Xayda
        };
        int z = 0;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id == DoHuyDiet[this.player.gender][i]) {
                    z++;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return z == 5;
    }

    /**
     * Thiết lập các chỉ số của các bộ trang phục đặc biệt (như Songoku, Thiên Xin Hăng, Kirin, v.v.)
     * dựa trên các tùy chọn (item options) của vật phẩm mà người chơi đang mặc.
     */
    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    /**
     * Đặt lại tất cả các chỉ số và trạng thái của bộ trang phục về giá trị mặc định.
     */
    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.setDHD = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    /**
     * Giải phóng tài nguyên bằng cách đặt tham chiếu đến người chơi về null.
     */
    public void dispose() {
        this.player = null;
    }
}