package com.girlkun.data;

import com.girlkun.models.Template.*;
import com.girlkun.models.skill.NClass;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.server.Manager;
import com.girlkun.server.io.MySession;
import com.girlkun.services.Service;
import com.girlkun.utils.FileIO;
import com.girlkun.utils.Logger;

import java.io.*;
import java.util.*;

/**
 * Quản lý các hoạt động liên quan đến dữ liệu game như gửi thông tin phiên bản, cập nhật dữ liệu, bản đồ, kỹ năng và tài nguyên.
 * @author Lucifer
 */
public class DataGame {

    /**
     * Phiên bản của dữ liệu game.
     */
    public static byte vsData = 80;

    /**
     * Phiên bản của bản đồ game.
     */
    public static byte vsMap = 80;

    /**
     * Phiên bản của kỹ năng game.
     */
    public static byte vsSkill = 6;

    /**
     * Phiên bản của vật phẩm game.
     */
    public static byte vsItem = 80;

    /**
     * Phiên bản của tài nguyên game.
     */
    public static int vsRes = 752011;

    /**
     * Chuỗi kết nối cho IP và cổng.
     */
    public static String LINK_IP_PORT = "Nro Green 01:127.0.0.1:14445:0,0,0";

    /**
     * Chuỗi chứa các số hiệu của thú cưỡi và ánh xạ của chúng.
     */
    private static final String MOUNT_NUM = "733:1,734:2,735:3,743:4,744:5,746:6,795:7,849:8,897:9,920:10,1092:11,1141:15";

    /**
     * Map để lưu trữ ánh xạ số hiệu của thú cưỡi.
     */
    public static final Map MAP_MOUNT_NUM = new HashMap();

    /**
     * Khởi tạo ánh xạ số hiệu thú cưỡi.
     */
    static {
        String[] array = MOUNT_NUM.split(",");
        for (String str : array) {
            String[] data = str.split(":");
            short num = (short) (Short.parseShort(data[1]) + 30000);
            MAP_MOUNT_NUM.put(data[0], num);
        }
    }

    /**
     * Hàm tạo riêng tư để ngăn khởi tạo đối tượng.
     */
    private DataGame() {
    }

    /**
     * Gửi thông tin phiên bản game đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendVersionGame(MySession session) {
        Message msg;
        try {
            msg = Service.gI().messageNotMap((byte) 4);
            msg.writer().writeByte(vsData);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(vsItem);
            msg.writer().writeByte(0);
            long[] smtieuchuan = {1000L, 3000L, 15000L, 40000L, 90000L, 170000L, 340000L, 700000L,
                1500000L, 15000000L, 150000000L, 1500000000L, 5000000000L, 10000000000L, 40000000000L,
                50010000000L, 60010000000L, 70010000000L, 80010000000L, 100010000000L};
            msg.writer().writeByte(smtieuchuan.length);
            for (long l : smtieuchuan) {
                msg.writer().writeLong(l);
            }
            session.sendMessage(msg);
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 14\n");
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật dữ liệu game cho phiên client.
     * @param session Phiên làm việc của client
     */
    public static void updateData(MySession session) {
        Message msg;
        try {
            File dartFile = new File("data/girlkun/update_data/dart");
            File arrowFile = new File("data/girlkun/update_data/arrow");
            File effectFile = new File("data/girlkun/update_data/effect");
            File imageFile = new File("data/girlkun/update_data/image");
            File partFile = new File("data/girlkun/update_data/part");
            File skillFile = new File("data/girlkun/update_data/skill");

            if (dartFile.exists() && arrowFile.exists() && effectFile.exists() && imageFile.exists() && partFile.exists() && skillFile.exists()) {
                byte[] dart = FileIO.readFile(dartFile.getPath());
                byte[] arrow = FileIO.readFile(arrowFile.getPath());
                byte[] effect = FileIO.readFile(effectFile.getPath());
                byte[] image = FileIO.readFile(imageFile.getPath());
                byte[] part = FileIO.readFile(partFile.getPath());
                byte[] skill = FileIO.readFile(skillFile.getPath());

                msg = new Message(-87);
                msg.writer().writeByte(vsData);
                msg.writer().writeInt(dart.length);
                msg.writer().write(dart);
                msg.writer().writeInt(arrow.length);
                msg.writer().write(arrow);
                msg.writer().writeInt(effect.length);
                msg.writer().write(effect);
                msg.writer().writeInt(image.length);
                msg.writer().write(image);
                msg.writer().writeInt(part.length);
                msg.writer().write(part);
                msg.writer().writeInt(skill.length);
                msg.writer().write(skill);

                session.doSendMessage(msg);
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 15\n");
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin bản đồ cho phiên client.
     * @param session Phiên làm việc của client
     */
    public static void updateMap(MySession session) {
        Message msg;
        try {
            msg = Service.gI().messageNotMap((byte) 6);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(Manager.MAP_TEMPLATES.length);
            for (MapTemplate temp : Manager.MAP_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
            }
            msg.writer().writeByte(Manager.NPC_TEMPLATES.size());
            for (NpcTemplate temp : Manager.NPC_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
                msg.writer().writeShort(temp.head);
                msg.writer().writeShort(temp.body);
                msg.writer().writeShort(temp.leg);
                msg.writer().writeByte(0);
            }
            msg.writer().writeByte(Manager.MOB_TEMPLATES.size());
            for (MobTemplate temp : Manager.MOB_TEMPLATES) {
                msg.writer().writeByte(temp.type);
                msg.writer().writeUTF(temp.name);
                msg.writer().writeInt(temp.hp);
                msg.writer().writeByte(temp.rangeMove);
                msg.writer().writeByte(temp.speed);
                msg.writer().writeByte(temp.dartType);
            }
            session.sendMessage(msg);
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 16\n");
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin kỹ năng cho phiên client.
     * @param session Phiên làm việc của client
     */
    public static void updateSkill(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(7);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(0); //count skill option
            msg.writer().writeByte(Manager.NCLASS.size());
            for (NClass nClass : Manager.NCLASS) {
                msg.writer().writeUTF(nClass.name);
                msg.writer().writeByte(nClass.skillTemplatess.size());
                for (SkillTemplate skillTemp : nClass.skillTemplatess) {
                    msg.writer().writeByte(skillTemp.id);
                    msg.writer().writeUTF(skillTemp.name);
                    msg.writer().writeByte(skillTemp.maxPoint);
                    msg.writer().writeByte(skillTemp.manaUseType);
                    msg.writer().writeByte(skillTemp.type);
                    msg.writer().writeShort(skillTemp.iconId);
                    msg.writer().writeUTF(skillTemp.damInfo);
                    msg.writer().writeUTF("Arriety");
                    if (skillTemp.id != 0) {
                        msg.writer().writeByte(skillTemp.skillss.size());
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            msg.writer().writeInt(skill.coolDown);
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                    } else {
                        //Thêm 2 skill trống 105, 106
                        msg.writer().writeByte(skillTemp.skillss.size() + 2);
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            msg.writer().writeInt(skill.coolDown);
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                        for (int i = 105; i <= 106; i++) {
                            msg.writer().writeShort(i);
                            msg.writer().writeByte(0);
                            msg.writer().writeLong(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeInt(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeByte(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeUTF("");
                        }
                    }
                }
            }
            session.doSendMessage(msg);
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 17\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông tin phiên bản hình ảnh đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendDataImageVersion(MySession session) {
        Message msg = null;
        try {
            File imgVersionFile = new File("data/girlkun/data_img_version/x" + session.zoomLevel + "/img_version");
            if (imgVersionFile.exists()) {
                msg = new Message(-111);
                msg.writer().write(FileIO.readFile(imgVersionFile.getPath()));
                session.doSendMessage(msg);
                if (msg != null) {
                    msg.cleanup();
                }
            } else {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 18\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi mẫu hiệu ứng đến phiên client.
     * @param session Phiên làm việc của client
     * @param id ID của hiệu ứng
     */
    public static void sendEffectTemplate(MySession session, int id) {
        Message msg = null;
        try {
            File effFile = new File("data/girlkun/effdata/x" + session.zoomLevel + "/" + id);
            if (effFile.exists()) {
                byte[] eff_data = FileIO.readFile(effFile.getPath());
                msg = new Message(-66);
                msg.writer().write(eff_data);
                session.sendMessage(msg);
                if (msg != null) {
                    msg.cleanup();
                }
            } else {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 18\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi dữ liệu hiệu ứng đến phiên client.
     * @param session Phiên làm việc của client
     * @param id ID của hiệu ứng
     * @param idtemp ID bổ sung (nếu có)
     */
    public static void effData(MySession session, int id, int... idtemp) {
        if (id >= 208) {
            sendEffectTemplate(session, id);
            return;
        }
        int idT = id;
        if (idtemp.length > 0 && idtemp[0] != 0) {
            idT = idtemp[0];
        }
        Message msg = null;
        try {
            File effDataFile = new File("data/girlkun/effect/x" + session.zoomLevel + "/data/DataEffect_" + idT);
            File effImgFile = new File("data/girlkun/effect/x" + session.zoomLevel + "/img/ImgEffect_" + idT + ".png");
            if (effDataFile.exists() && effImgFile.exists()) {
                byte[] effData = FileIO.readFile(effDataFile.getPath());
                byte[] effImg = FileIO.readFile(effImgFile.getPath());
                msg = new Message(-66);
                msg.writer().writeShort(id);
                msg.writer().writeInt(effData.length);
                msg.writer().write(effData);
                msg.writer().writeByte(0);
                msg.writer().writeInt(effImg.length);
                msg.writer().write(effImg);
                session.sendMessage(msg);
                if (msg != null) {
                    msg.cleanup();
                }
            } else {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 19\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi mẫu nền vật phẩm đến phiên client.
     * @param session Phiên làm việc của client
     * @param id ID của mẫu nền
     */
    public static void sendItemBGTemplate(MySession session, int id) {
        Message msg = null;
        try {
            File bgTempFile = new File("data/girlkun/item_bg_temp/x" + session.zoomLevel + "/" + id + ".png");
            if (bgTempFile.exists()) {
                byte[] bg_temp = FileIO.readFile(bgTempFile.getPath());
                if (bg_temp != null) {
                    msg = new Message(-32);
                    msg.writer().writeShort(id);
                    msg.writer().writeInt(bg_temp.length);
                    msg.writer().write(bg_temp);
                    session.sendMessage(msg);
                    if (msg != null) {
                        msg.cleanup();
                    }
                }
            } else {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 20\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi dữ liệu nền vật phẩm đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendDataItemBG(MySession session) {
        Message msg = null;
        try {
            File itemBgFile = new File("data/girlkun/item_bg_temp/item_bg_data");
            if (itemBgFile.exists()) {
                byte[] item_bg = FileIO.readFile(itemBgFile.getPath());
                if (item_bg != null) {
                    msg = new Message(-31);
                    msg.writer().write(item_bg);
                    session.sendMessage(msg);
                    if (msg != null) {
                        msg.cleanup();
                    }
                }
            } else {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 21\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi biểu tượng đến phiên client.
     * @param session Phiên làm việc của client
     * @param id ID của biểu tượng
     */
    public static void sendIcon(MySession session, int id) {
        Message msg = null;
        try {
            File iconFile = new File("data/girlkun/icon/x" + session.zoomLevel + "/" + id + ".png");
            if (iconFile.exists()) {
                byte[] icon = FileIO.readFile(iconFile.getPath());
                if (icon != null) {
                    msg = new Message(-67);
                    msg.writer().writeInt(id);
                    msg.writer().writeInt(icon.length);
                    msg.writer().write(icon);
                    session.sendMessage(msg);
                    if (msg != null) {
                        msg.cleanup();
                    }
                }
            } else {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 22\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi phiên bản nhỏ của dữ liệu đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendSmallVersion(MySession session) {
        Message msg = null;
        try {
            File imgVersionFile = new File("data/girlkun/data_img_version/x" + session.zoomLevel + "/img_version");
            if (imgVersionFile.exists()) {
                byte[] data = FileIO.readFile(imgVersionFile.getPath());
                if (data != null) {
                    msg = new Message(-77);
                    msg.writer().write(data);
                }
                session.sendMessage(msg);
            }
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 22\n");
            e.printStackTrace();
        }
    }

    /**
     * Danh sách lưu trữ các ID.
     */
    private static List<Integer> list = new ArrayList<>();

    /**
     * Yêu cầu mẫu quái vật từ phiên client.
     * @param session Phiên làm việc của client
     * @param id ID của quái vật
     */
    public static void requestMobTemplate(MySession session, int id) {
        Message msg = null;
        try {
            File mobFile = new File("data/girlkun/mob/x" + session.zoomLevel + "/" + id);
            if (mobFile.exists()) {
                byte[] mob = FileIO.readFile(mobFile.getPath());
                if (mob != null) {
                    msg = new Message(11);
                    msg.writer().writeByte(id);
                    msg.writer().write(mob);
                    session.sendMessage(msg);
                }
            }
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 23\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông tin tập hợp tile đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendTileSetInfo(MySession session) {
        Message msg;
        try {
            msg = new Message(-82);
            msg.writer().write(FileIO.readFile("data/girlkun/map/tile_set_info"));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi dữ liệu bản đồ tạm thời đến phiên client.
     * @param session Phiên làm việc của client
     * @param id ID của bản đồ
     */
    public static void sendMapTemp(MySession session, int id) {
        try {
            byte[] tempMapData = FileIO.readFile("data/girlkun/map/tile_map_data/" + id);
            if (tempMapData == null) {
                return;
            }
            Message msg = new Message(-28);
            msg.writer().writeByte(10);
            msg.writer().write(tempMapData);
            session.sendMessage(msg);
        } catch (Exception e) {
        }
    }

    /**
     * Gửi thông tin avatar đầu đến client.
     * @param msg Tin nhắn chứa dữ liệu
     */
    public static void sendHeadAvatar(Message msg) {
        try {
            msg.writer().writeShort(Manager.HEAD_AVATARS.size());
            for (HeadAvatar ha : Manager.HEAD_AVATARS) {
                msg.writer().writeShort(ha.headId);
                msg.writer().writeShort(ha.avatarId);
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 26\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi hình ảnh theo tên đến phiên client.
     * @param session Phiên làm việc của client
     * @param imgName Tên của hình ảnh
     */
    public static void sendImageByName(MySession session, String imgName) {
        Message msg;
        try {
            File file = new File("data/girlkun/img_by_name/x" + session.zoomLevel + "/" + imgName + ".png");
            if (file.exists()) {
                msg = new Message(66);
                msg.writer().writeUTF(imgName);
                msg.writer().writeByte(Manager.getNFrameImageByName(imgName));
                byte[] data = FileIO.readFile(file.getPath());
                assert data != null;
                msg.writer().writeInt(data.length);
                msg.writer().write(data);
                session.sendMessage(msg);
                if (msg != null) {
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 27\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi phiên bản tài nguyên đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendVersionRes(ISession session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(0);
            msg.writer().writeInt(vsRes);
            session.sendMessage(msg);
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 28\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi kích thước tài nguyên đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendSizeRes(MySession session) {
        Message msg = null;
        try {
            File resFolder = new File("data/girlkun/res/x" + session.zoomLevel);
            if (resFolder.exists() && resFolder.isDirectory()) {
                File[] files = resFolder.listFiles();
                if (files != null) {
                    msg = new Message(-74);
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(files.length);
                    session.sendMessage(msg);
                }
            }
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 29\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi tài nguyên đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendRes(MySession session) {
        Message msg;
        try {
            for (final File fileEntry : Objects.requireNonNull(new File("data/girlkun/res/x" + session.zoomLevel).listFiles())) {
                String original = fileEntry.getName();
                byte[] res = FileIO.readFile(fileEntry.getAbsolutePath());
                msg = new Message(-74);
                msg.writer().writeByte(2);
                msg.writer().writeUTF(original);
                assert res != null;
                msg.writer().writeInt(res.length);
                msg.writer().write(res);
                session.sendMessage(msg);
                if (msg != null) {
                    msg.cleanup();
                }
                Thread.sleep(10);
            }
            msg = new Message(-74);
            msg.writer().writeByte(3);
            msg.writer().writeInt(vsRes);
            session.sendMessage(msg);
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 30\n");
            e.printStackTrace();
        }
    }

    /**
     * Gửi liên kết IP đến phiên client.
     * @param session Phiên làm việc của client
     */
    public static void sendLinkIP(MySession session) {
        Message msg;
        try {
            msg = new Message(-29);
            msg.writer().writeByte(2);
            msg.writer().writeUTF(DataControlGame.DataGame.IPServerGame);
            msg.writer().writeInt(DataControlGame.DataGame.KeyLogin.length);
            for (int i = 0; i < DataControlGame.DataGame.KeyLogin.length; i++) {
                msg.writer().writeLong(DataControlGame.DataGame.KeyLogin[i]);
            }
            msg.writer().writeInt(DataControlGame.DataGame.KeyRes.length);
            for (int i = 0; i < DataControlGame.DataGame.KeyRes.length; i++) {
                msg.writer().writeLong(DataControlGame.DataGame.KeyRes[i]);
            }
            msg.writer().writeByte(1);
            session.sendMessage(msg);
            if (msg != null) {
                msg.cleanup();
            }
        } catch (Exception e) {
            System.err.print("\nLỗi tại 31\n");
            e.printStackTrace();
        }
    }
}