package com.girlkun.models.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Lớp Playerao đại diện cho một nhân vật người chơi trong game.
 * Lớp này quản lý các thông tin cơ bản của người chơi như tên, sức mạnh, HP, vị trí, giới tính, và bản đồ hiện tại.
 * Cung cấp các phương thức để tạo người chơi ảo, thực hiện kỹ năng ngẫu nhiên, di chuyển trong bản đồ, và chuyển đổi giữa các bản đồ.
 * 
 * @author Lucifer
 */
public class Playerao {

    /**
     * Tên của người chơi, được sinh ngẫu nhiên từ danh sách tên hoặc mặc định với hậu tố số.
     */
    private String playerName;

    /**
     * Sức mạnh của người chơi, được sinh ngẫu nhiên trong khoảng từ 200,000,000 đến 89,090,900,000.
     */
    private long power;

    /**
     * ID diện mạo của người chơi, được chọn ngẫu nhiên từ danh sách các ID diện mạo.
     */
    private int appearanceId;

    /**
     * Máu (HP) của người chơi, được sinh ngẫu nhiên trong khoảng từ 293,084 đến 1,289,974.
     */
    private long hp;

    /**
     * Vị trí hiện tại của người chơi trên bản đồ, bao gồm tọa độ x và y.
     */
    private Location location;

    /**
     * Giới tính của người chơi, được sinh ngẫu nhiên (Trái đất, Namếc, hoặc Xayda).
     */
    private String gender;

    /**
     * ID của bản đồ hiện tại mà người chơi đang ở.
     */
    private int currentMapId;

    /**
     * Danh sách các tên có thể sử dụng để sinh ngẫu nhiên tên người chơi.
     */
    private static List<String> possibleNames = new ArrayList<>(Arrays.asList("gojo"));

    /**
     * Danh sách các tên đã được sử dụng để tránh trùng lặp.
     */
    private static List<String> usedNames = new ArrayList<>();

    /**
     * Danh sách các kỹ năng mà người chơi có thể sử dụng.
     */
    private static final List<String> skills = Arrays.asList("DRAGON", "KHIEU_NANG_LUONG");

    /**
     * Danh sách các ID bản đồ mà người chơi có thể di chuyển đến.
     */
    private static final List<Integer> mapIds = Arrays.asList(14);

    /**
     * Khởi tạo một đối tượng Playerao với các thuộc tính được sinh ngẫu nhiên.
     * Các thuộc tính bao gồm tên, sức mạnh, HP, giới tính, vị trí, và diện mạo.
     */
    public Playerao() {
        this.power = generateRandomPower();
        this.playerName = generateRandomName();
        this.appearanceId = selectRandomAppearance();
        this.hp = generateRandomHP();
        this.gender = generateRandomGender();
        this.location = generateRandomLocation();
    }

    /**
     * Sinh ngẫu nhiên sức mạnh cho người chơi trong khoảng từ 200,000,000 đến 89,090,900,000.
     * 
     * @return Giá trị sức mạnh ngẫu nhiên kiểu long.
     */
    private long generateRandomPower() {
        Random random = new Random();
        long min = 200_000_000L;
        long max = 89_090_900_000L;
        return random.nextLong() % (max - min + 1) + min;
    }

    /**
     * Sinh tên ngẫu nhiên cho người chơi từ danh sách tên có sẵn hoặc tạo tên mặc định với hậu tố ngẫu nhiên.
     * Đảm bảo tên không trùng lặp bằng cách theo dõi danh sách tên đã sử dụng.
     * 
     * @return Tên ngẫu nhiên của người chơi.
     */
    private String generateRandomName() {
        Random random = new Random();
        String randomName;

        if (possibleNames.isEmpty()) {
            randomName = "halica" + random.nextInt(100);
        } else {
            int randomIndex = random.nextInt(possibleNames.size());
            String selectedName = possibleNames.get(randomIndex);

            String randomSuffix = String.format("%04d", random.nextInt(100));
            randomName = selectedName + randomSuffix;

            usedNames.add(randomName);
            possibleNames.remove(selectedName);
        }

        return randomName;
    }

    /**
     * Chọn ngẫu nhiên ID diện mạo từ danh sách các ID diện mạo có sẵn.
     * 
     * @return ID diện mạo ngẫu nhiên.
     */
    private int selectRandomAppearance() {
        Random random = new Random();
        List<Integer> appearanceIds = Arrays.asList(405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417,
                418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430,
                431, 432, 433, 451);

        int index = random.nextInt(appearanceIds.size());
        return appearanceIds.get(index);
    }

    /**
     * Sinh ngẫu nhiên máu (HP) cho người chơi trong khoảng từ 293,084 đến 1,289,974.
     * 
     * @return Giá trị HP ngẫu nhiên kiểu long.
     */
    private long generateRandomHP() {
        Random random = new Random();
        long min = 293_084L;
        long max = 1_289_974L;
        return random.nextLong() % (max - min + 1) + min;
    }

    /**
     * Sinh ngẫu nhiên giới tính cho người chơi (Trái đất, Namếc, hoặc Xayda).
     * In ra console giá trị giới tính được chọn và trả về giá trị tương ứng.
     * 
     * @return Giới tính ngẫu nhiên của người chơi.
     */
    private String generateRandomGender() {
        Random random = new Random();
        int randomValue = random.nextInt(3); // Sinh một số nguyên từ 0 đến 2

        switch (randomValue) {
            case 0:
                System.out.println("Trái đất");
                return "Trái đất";
            case 1:
                System.out.println("Namếc");
                return "Namếc";
            case 2:
                System.out.println("Xayda");
                return "Xayda";
        }
        return "1"; // Giá trị mặc định nếu có lỗi
    }

    /**
     * Cho phép người chơi tham gia một bản đồ với ID được chỉ định.
     * Cập nhật ID bản đồ hiện tại và in thông báo ra console.
     * 
     * @param mapId ID của bản đồ mà người chơi tham gia.
     */
    public void joinMap(int mapId) {
        currentMapId = mapId;
        System.out.println(playerName + " joined map " + mapId);
    }

    /**
     * Sinh ngẫu nhiên vị trí (tọa độ x, y) cho người chơi trên bản đồ.
     * Tọa độ x nằm trong khoảng [0, 1400] và y nằm trong khoảng [0, 400].
     * 
     * @return Đối tượng Location chứa tọa độ ngẫu nhiên.
     */
    private Location generateRandomLocation() {
        Random random = new Random();
        Location newLocation = new Location();
        newLocation.x = random.nextInt(1401);
        newLocation.y = random.nextInt(401);
        return newLocation;
    }

    /**
     * Di chuyển người chơi trong bản đồ hiện tại bằng cách thay đổi tọa độ x, y ngẫu nhiên.
     * Tọa độ mới được giới hạn trong phạm vi bản đồ (x: [0, 1400], y: [0, 400]).
     */
    public void moveWithinMap() {
        Random random = new Random();
        int newX = location.x + random.nextInt(21) - 10; // Di chuyển từ -10 đến 10 đơn vị
        int newY = location.y + random.nextInt(21) - 10;

        // Cập nhật tọa độ mới, đảm bảo không vượt ra ngoài bản đồ
        location.x = Math.max(0, Math.min(1400, newX));
        location.y = Math.max(0, Math.min(400, newY));
    }

    /**
     * Di chuyển người chơi đến một bản đồ ngẫu nhiên từ danh sách các bản đồ có sẵn.
     * In thông báo ra console về bản đồ mới mà người chơi di chuyển đến.
     */
    public void moveToRandomMap() {
        int newMapId = selectRandomMap();
        System.out.println(playerName + " moved to map " + newMapId);
    }

    /**
     * Chọn ngẫu nhiên ID bản đồ từ danh sách các ID bản đồ có sẵn.
     * 
     * @return ID bản đồ ngẫu nhiên.
     */
    private int selectRandomMap() {
        Random random = new Random();
        int index = random.nextInt(mapIds.size());
        return mapIds.get(index);
    }

    /**
     * Tạo một số lượng người chơi ảo được chỉ định, mỗi người chơi chạy trên một luồng riêng.
     * Mỗi người chơi sẽ thực hiện các hành động như sử dụng kỹ năng, di chuyển trong bản đồ, và chuyển bản đồ ngẫu nhiên.
     * 
     * @param totalPlayers Số lượng người chơi ảo cần tạo.
     */
    public static void createVirtualPlayers(int totalPlayers) {
        List<Playerao> players = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < totalPlayers; i++) {
            Playerao player = new Playerao();
            players.add(player);

            Thread playerThread = new Thread(() -> {
                while (true) {
                    player.performRandomSkill();
                    player.moveToRandomMap();
                    player.moveWithinMap();

                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            playerThread.start();
        }
    }

    /**
     * Thực hiện một kỹ năng ngẫu nhiên từ danh sách kỹ năng có sẵn.
     * 
     * @return Tên kỹ năng được chọn ngẫu nhiên.
     */
    public String performRandomSkill() {
        Random random = new Random();
        int index = random.nextInt(skills.size());
        return skills.get(index);
    }

    /**
     * Lớp Location đại diện cho vị trí của người chơi trên bản đồ với tọa độ x và y.
     */
    class Location {
        /**
         * Tọa độ x của người chơi trên bản đồ.
         */
        private int x;

        /**
         * Tọa độ y của người chơi trên bản đồ.
         */
        private int y;

        /**
         * Lấy giá trị tọa độ x.
         * 
         * @return Giá trị tọa độ x.
         */
        public int getX() {
            return x;
        }

        /**
         * Cập nhật giá trị tọa độ x.
         * 
         * @param x Giá trị tọa độ x mới.
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * Lấy giá trị tọa độ y.
         * 
         * @return Giá trị tọa độ y.
         */
        public int getY() {
            return y;
        }

        /**
         * Cập nhật giá trị tọa độ y.
         * 
         * @param y Giá trị tọa độ y mới.
         */
        public void setY(int y) {
            this.y = y;
        }
    }
}