package com.girlkun.services;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.player.NewPet;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

/**
 * Lớp PetService quản lý các chức năng liên quan đến việc tạo và thay đổi đệ tử (pet) trong game.
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 * Cung cấp các phương thức để tạo các loại đệ tử khác nhau và đổi tên đệ tử.
 * 
 * @author Lucifer
 */
public class PetService {

    /**
     * Thể hiện duy nhất của lớp PetService (singleton pattern).
     */
    private static PetService i;

    /**
     * Lấy thể hiện duy nhất của lớp PetService.
     * Nếu chưa có, tạo mới một thể hiện.
     * 
     * @return Thể hiện của lớp PetService.
     */
    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    /**
     * Tạo đệ tử Kaido cho người chơi.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử (0, 1, hoặc 2).
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createKaidoPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet1(player, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử bình thường cho người chơi với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử (0, 1, hoặc 2).
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử bình thường cho người chơi với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Kaido.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử Kaido (0, 1, hoặc 2).
     */
    public void changeKaidoPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createKaidoPet(player, gender, limitPower);
    }

    /**
     * Tạo đệ tử Mabu cho người chơi với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử (0, 1, hoặc 2).
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createMabuPet1(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử Berus cho người chơi với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createBerusPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử Berus cho người chơi với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử (0, 1, hoặc 2).
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createBerusPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử Pic cho người chơi với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createPicPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, true, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Sư Phụ Broly hiện thân tụi mày quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử Pic cho người chơi với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử (0, 1, hoặc 2).
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createPicPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Sư Phụ Broly hiện thân tụi mày quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử Xên Con cho người chơi với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createXencon(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử là Xên Con...");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Tạo đệ tử Xên Con cho người chơi với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử (0, 1, hoặc 2).
     * @param limitPower Giới hạn sức mạnh của đệ tử (tùy chọn).
     */
    public void createXencon(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Chào sư phụ, Đệ tử đang tu luyện tuyệt kỹ masenco !");
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử bình thường với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử bình thường (0, 1, hoặc 2).
     */
    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử bình thường với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     */
    public void changeNormalPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Mabu với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử Mabu (0, 1, hoặc 2).
     */
    public void changeMabuPet1(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet1(player, gender, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Mabu với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     */
    public void changeMabuPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet1(player, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Berus với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     */
    public void changeBerusPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Berus với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử Berus (0, 1, hoặc 2).
     */
    public void changeBerusPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, gender, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Pic với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     */
    public void changePicPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createPicPet(player, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Pic với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử Pic (0, 1, hoặc 2).
     */
    public void changePicPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createPicPet(player, gender, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Xên Con với giới tính ngẫu nhiên.
     * 
     * @param player Người chơi sở hữu đệ tử.
     */
    public void changeXencon(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createXencon(player, limitPower);
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Xên Con với giới tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param gender Giới tính của đệ tử Xên Con (0, 1, hoặc 2).
     */
    public void changeXencon(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createXencon(player, gender, limitPower);
    }

    /**
     * Đổi tên đệ tử của người chơi, yêu cầu thẻ đặt tên và kiểm tra tên hợp lệ.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param name Tên mới cho đệ tử.
     */
    public void changeNamePet(Player player, String name) {
        try {
            if (!InventoryServiceNew.gI().isExistItemBag(player, 400)) {
                Service.getInstance().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
                return;
            } else if (Util.haveSpecialCharacter(name)) {
                Service.getInstance().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
                return;
            } else if (name.length() > 10) {
                Service.getInstance().sendThongBao(player, "Tên quá dài");
                return;
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.name = "$" + name.toLowerCase().trim();
            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 400), 1);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
                } catch (Exception e) {
                }
            }).start();
        } catch (Exception ex) {
        }
    }

    /**
     * Lấy dữ liệu chỉ số cơ bản cho đệ tử bình thường.
     * 
     * @return Mảng chứa các chỉ số: HP, MP, sát thương, phòng thủ, chí mạng.
     */
    private int[] getDataPetNormal() {
        long[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    /**
     * Lấy dữ liệu chỉ số cơ bản cho đệ tử Mabu.
     * 
     * @return Mảng chứa các chỉ số: HP, MP, sát thương, phòng thủ, chí mạng.
     */
    private int[] getDataPetMabu() {
        long[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    /**
     * Lấy dữ liệu chỉ số cơ bản cho đệ tử Berus.
     * 
     * @return Mảng chứa các chỉ số: HP, MP, sát thương, phòng thủ, chí mạng.
     */
    private int[] getDataPetBerus() {
        long[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 110) * 20; //hp
        petData[1] = Util.nextInt(40, 110) * 20; //mp
        petData[2] = Util.nextInt(50, 130); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    /**
     * Lấy dữ liệu chỉ số cơ bản cho đệ tử Pic.
     * 
     * @return Mảng chứa các chỉ số: HP, MP, sát thương, phòng thủ, chí mạng.
     */
    private int[] getDataPetPic() {
        long[] hpmp = {2000, 2100, 2200, 2300, 2400, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 125) * 20; //hp
        petData[1] = Util.nextInt(40, 125) * 20; //mp
        petData[2] = Util.nextInt(80, 160); //dame
        petData[3] = Util.nextInt(10, 60); //def
        petData[4] = Util.nextInt(2, 5); //crit
        return petData;
    }

    /**
     * Lấy dữ liệu chỉ số cơ bản cho đệ tử Xên Con.
     * 
     * @return Mảng chứa các chỉ số: HP, MP, sát thương, phòng thủ, chí mạng.
     */
    private int[] getDataXencon() {
        long[] hpmp = {2000, 2100, 2200, 2300, 2400, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 125) * 20; //hp
        petData[1] = Util.nextInt(40, 125) * 20; //mp
        petData[2] = Util.nextInt(80, 160); //dame
        petData[3] = Util.nextInt(10, 60); //def
        petData[4] = Util.nextInt(2, 5); //crit
        return petData;
    }

    /**
     * Lấy dữ liệu chỉ số cơ bản cho đệ tử Kaido.
     * 
     * @return Mảng chứa các chỉ số: HP, MP, sát thương, phòng thủ, chí mạng.
     */
    private int[] getDataPetKaido() {
        int[] hpmp = {2000, 2100, 2200, 2300, 2400, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    /**
     * Tạo một đệ tử mới cho người chơi với các thuộc tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param isMabu Có phải là đệ tử Mabu không.
     * @param isBerus Có phải là đệ tử Berus không.
     * @param isPic Có phải là đệ tử Pic không.
     * @param isXencon Có phải là đệ tử Xên Con không.
     * @param gender Giới tính của đệ tử (tùy chọn).
     */
    private void createNewPet(Player player, boolean isMabu, boolean isBerus, boolean isPic, boolean isXencon, byte... gender) {
        int[] data = isXencon ? getDataXencon() : isMabu ? getDataPetMabu() : isPic ? getDataPetPic() : isBerus ? getDataPetBerus() : getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isBerus ? "Goku Rose" : isPic ? "Super Broly" : isXencon ? "Xên Con" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = isMabu || isBerus || isXencon || isPic ? 1500000 : 2000;
        pet.typePet = (byte) (isXencon ? 4 : isMabu ? 1 : isBerus ? 2 : isPic ? 3 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }

    /**
     * Tạo một đệ tử mới (Kaido, Berus, hoặc Pic) cho người chơi với các thuộc tính cụ thể.
     * 
     * @param player Người chơi sở hữu đệ tử.
     * @param isKaido Có phải là đệ tử Kaido không.
     * @param isBerus Có phải là đệ tử Berus không.
     * @param isPic Có phải là đệ tử Pic không.
     * @param gender Giới tính của đệ tử (tùy chọn).
     */
    private void createNewPet1(Player player, boolean isKaido, boolean isBerus, boolean isPic, byte... gender) {
        int[] data = isKaido ? getDataPetKaido() : isPic ? getDataPetPic() : isBerus ? getDataPetBerus() : getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isKaido ? "Tứ Hoàng Kaido" : isBerus ? "Berus" : isPic ? "Pic" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = isKaido || isBerus || isPic ? 1500000 : 2000;
        pet.typePet = (byte) (isKaido ? 34 : isBerus ? 2 : isPic ? 3 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }

    /**
     * Tạo một đệ tử mới (NewPet) với các thuộc tính cơ bản dựa trên người chơi.
     * 
     * @param pl Người chơi sở hữu đệ tử.
     * @param h ID đầu của đệ tử.
     * @param b ID thân của đệ tử.
     * @param l ID chân của đệ tử.
     * @param name Tên của đệ tử.
     */
    public static void Pet2(Player pl, int h, int b, int l, String name) {
        if (pl.newpet != null) {
            pl.newpet.dispose();
        }
        pl.newpet = new NewPet(pl, (short) h, (short) b, (short) l, name);
        pl.newpet.name = name;
        pl.newpet.gender = pl.gender;
        pl.newpet.nPoint.tiemNang = 1;
        pl.newpet.nPoint.power = 1;
        pl.newpet.nPoint.limitPower = 1;
        pl.newpet.nPoint.hpg = 500000;
        pl.newpet.nPoint.mpg = 500000;
        pl.newpet.nPoint.hp = 500000;
        pl.newpet.nPoint.mp = 500000;
        pl.newpet.nPoint.dameg = pl.nPoint.dameg;
        pl.newpet.nPoint.defg = 1;
        pl.newpet.nPoint.critg = 1;
        pl.newpet.nPoint.stamina = 1;
        pl.newpet.nPoint.setBasePoint();
        pl.newpet.nPoint.setFullHpMp();
    }

    /**
     * Tạo đệ tử Mabu cho người chơi (chưa được triển khai).
     * 
     * @param plKill Người chơi sở hữu đệ tử.
     * @throws UnsupportedOperationException Phương thức chưa được triển khai.
     */
    public void createMabuPet1(Player plKill) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Thay đổi đệ tử hiện tại của người chơi thành đệ tử Mabu (chưa được triển khai).
     * 
     * @param plKill Người chơi sở hữu đệ tử.
     * @throws UnsupportedOperationException Phương thức chưa được triển khai.
     */
    public void changeMabuPet1(Player plKill) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}