package me.ele.hackathon.ks;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.ele.hackathon.object.DatasetFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static me.ele.hackathon.Main.FEATURE_DATA_DIR;
import static me.ele.hackathon.Main.SOURCE_FILE_DIR;

/**
 * Created by: sheng.ke
 * Date: 2016/10/9
 * Time: 上午11:47
 */

/**
 * 可变扩容比例的dataset
 */
@Deprecated
public class SimpleFeatureSelectorV21 {
    private static final Logger log = LoggerFactory.getLogger(SimpleFeatureSelectorV21.class);

    public String VERSION = "simple_feature_selector_v21";

    public static final String INFO_SOURCE_FILE = SOURCE_FILE_DIR + "his_eco_info.txt";
    public static final String ENV_SOURCE_FILE = SOURCE_FILE_DIR + "his_eco_env.txt";
    public static final String RST_SOURCE_FILE = SOURCE_FILE_DIR + "rst_info.txt";
    public static final String TEST_INFO_SOURCE_FILE = SOURCE_FILE_DIR + "next_eco_info.txt";
    public static final String TEST_ENV_SOURCE_FILE = SOURCE_FILE_DIR + "next_eco_env.txt";


    public String CLICK_OUTPUT_File = FEATURE_DATA_DIR + VERSION + "/click_train.txt";
    public String BUY_OUTPUT_File = FEATURE_DATA_DIR + VERSION + "/buy_train.txt";
    public String TEST_OUTPUT_File = FEATURE_DATA_DIR + VERSION + "/test.txt";

    private static final String SEPARATOR = " ";

    int clickTimes = 1;
    int buyTimes = 1;

    public SimpleFeatureSelectorV21(int clickTimes, int buyTimes) {
        this.clickTimes = clickTimes;
        this.buyTimes = buyTimes;
        this.VERSION = this.VERSION + "_" + this.clickTimes + "_" + this.buyTimes;

        CLICK_OUTPUT_File = FEATURE_DATA_DIR + VERSION + "/click_train.txt";
        BUY_OUTPUT_File = FEATURE_DATA_DIR + VERSION + "/buy_train.txt";
        TEST_OUTPUT_File = FEATURE_DATA_DIR + VERSION + "/test.txt";
    }

    public static DatasetFile buildDataset(int clickTimes, int buyTimes, boolean recreateFile) {

        SimpleFeatureSelectorV21 v21 = null;
        try {
            v21 = new SimpleFeatureSelectorV21(clickTimes, buyTimes);

            if (recreateFile) {

                log.warn("==buildDataset== Going to write dataset to disk.");
                restaurantMap = readRestaurant();

                v21.formatTrainDataset(INFO_SOURCE_FILE, ENV_SOURCE_FILE,
                        v21.CLICK_OUTPUT_File,
                        v21.BUY_OUTPUT_File);
                v21.formatTestDataset(TEST_INFO_SOURCE_FILE,
                        TEST_ENV_SOURCE_FILE, v21.TEST_OUTPUT_File);
            }
        } catch (IOException e) {
            log.error("==buildDataset== {}", e);
        }

        log.warn("==构建SimpleFeatureSelector2选择的数据== 需确认已执行过SimpleFeatureSelector的main()将数据文件生成。");

        return new DatasetFile(v21.VERSION, v21.CLICK_OUTPUT_File, v21.BUY_OUTPUT_File, v21.TEST_OUTPUT_File, 11);
    }

    private static Map<String/* Restaurant id*/, Restaurant> restaurantMap;


    public static void main(String[] args) throws IOException {
        buildDataset(20, 100, true);
    }

    private static Map<String, Restaurant> readRestaurant() throws IOException {
        Map<String/* ad_list_id*/, Restaurant> map = Maps.newHashMap();

        BufferedReader in = new BufferedReader(new FileReader(RST_SOURCE_FILE));

        String line = in.readLine();    // ignore first line
        while ((line = in.readLine()) != null) {

            List<String> list = Splitter.on("\t").splitToList(line);
            String rstId = list.get(0);

            double x = Double.parseDouble(list.get(4));
            double y = Double.parseDouble(list.get(5));
            int isPremium = Integer.parseInt(list.get(7));
            int hasImage = Integer.parseInt(list.get(11));
            int isTimeEnsure = Integer.parseInt(list.get(15));
            int isKa = Integer.parseInt(list.get(16));
            int isTimeEnsureDisCount = Integer.parseInt(list.get(17));
            int isElemeDeliver = Integer.parseInt(list.get(18));
            int isPromotionInfo = Integer.parseInt(list.get(28));


            Restaurant restaurant = new Restaurant(x, y, isPremium, hasImage, isTimeEnsure, isKa,
                    isTimeEnsureDisCount, isElemeDeliver, isPromotionInfo);
            map.put(rstId, restaurant);
        }
        return map;
    }

    private void formatTrainDataset(String info, String env, String clickOutput, String buyOutput) throws IOException {
        Map<String, AdList> adListMap = readAdList(env);

        List<Ad> ads = Lists.newArrayList();

        BufferedReader in = new BufferedReader(new FileReader(info));

        int originClickCount = 0, originBuyCount = 0, expandClickCount = 0, expandBuyCount = 0;
        String line = in.readLine();    // ignore first line
        while ((line = in.readLine()) != null) {

            List<String> list = Splitter.on("\t").splitToList(line);
            String listId = list.get(1);
            String rstId = list.get(2);

            AdList adList = adListMap.get(listId);
            Restaurant rst = restaurantMap.get(rstId);
            if (adList == null) {
                System.out.println("Oops, list_id: [" + listId + "] not found!");
            } else if (rst == null) {
                System.out.println("Oops, rst_id: [" + listId + "] not found!");
            } else {
                int index = Integer.parseInt(list.get(3));
                int isClick = Integer.parseInt(list.get(4));
                if (isClick == 1) {
                    originClickCount++;
                }

                int isBuy = Integer.parseInt(list.get(5));
                if (isBuy == 1) {
                    originBuyCount++;
                }
                Ad ad = new Ad(index, isClick, isBuy, adList, rst);

                if (isBuy == 1) {    //对 buy 扩容50倍
                    for (int i = 0; i < this.buyTimes; i++) {
                        expandBuyCount++;
                        ads.add(ad);
                    }
                } else if (isClick == 1) { // 对click扩容10倍
                    for (int i = 0; i < this.clickTimes; i++) {
                        expandClickCount++;
                        ads.add(ad);
                    }
                } else {
                    ads.add(ad);
                }
            }
        }

        Collections.shuffle(ads);

        log.warn("done. 扩容后size: {},  click: {}/(origin){}, buy:: {}/(origin){}, click_rate: {}, buy_rate: {}. ",
                ads.size(), expandClickCount, originClickCount, expandBuyCount, originBuyCount,
                (double) expandClickCount / ads.size(), (double) expandBuyCount / ads.size());
//        System.out.println("done. 扩容后size: " + ads.size() + " click, buy: " + originClickCount + "," + originBuyCount + ", " +
//                "click_rate: " + originClickCount / ads.size() + ", buy_rate: " + originBuyCount / ads.size());

        outputClickFile(ads, clickOutput);
        outputBuyFile(ads, buyOutput);
        log.warn("Format Train Dataset File ok, clickOutput: {}, buyOutput: {}", clickOutput, buyOutput);
    }

    private void formatTestDataset(String info, String env, String testOutput) throws IOException {
        Map<String, AdList> adListMap = readAdList(env);

        List<Ad> ads = Lists.newArrayList();

        BufferedReader in = new BufferedReader(new FileReader(info));

        String line = in.readLine();    // ignore first line
        while ((line = in.readLine()) != null) {

            List<String> list = Splitter.on("\t").splitToList(line);
            String listId = list.get(1);

            AdList adList = adListMap.get("\"" + listId + "\""); // pity
            if (adList == null) {
                System.out.println("Oops, list_id: [" + listId + "] not found!");
            } else {
                String restaurantId = list.get(2);
                int index = Integer.parseInt(list.get(3));

                Restaurant restaurant = restaurantMap.get("\"" + restaurantId + "\"");
                Ad ad;
                if (restaurant == null) {
                    System.out.println("Oops, rst_id: [" + restaurantId + "] not found!");
                    ad = new Ad(index, adList);
                } else {

                    ad = new Ad(index, adList, restaurant);
                }
                ads.add(ad);
            }
        }

        System.out.println("done. 待测试的size: " + ads.size());

        outputTestFile(ads, testOutput);
        System.out.println("output to TEST files done.");
    }

    private static void outputClickFile(List<Ad> ads, String clickOutput) throws IOException {
        File f = new File(clickOutput);
        f.getParentFile().mkdirs();
        f.createNewFile();


        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(clickOutput)))) {
            ads.stream().forEach(ad -> {
                pw.append(String.valueOf(ad.getIsClick())).append(SEPARATOR);

                pw.append("1:").append(String.valueOf(ad.getIndex())).append(SEPARATOR);
                pw.append("2:").append(String.valueOf(ad.getIsSelect())).append(SEPARATOR);
                pw.append("3:").append(String.valueOf(ad.getIsNew())).append(SEPARATOR);
                pw.append("4:").append(String.valueOf(ad.getDistance())).append(SEPARATOR);
                pw.append("5:").append(String.valueOf(ad.getIs_premium())).append(SEPARATOR);
                pw.append("6:").append(String.valueOf(ad.getHas_image())).append(SEPARATOR);
                pw.append("7:").append(String.valueOf(ad.getIs_time_ensure())).append(SEPARATOR);
                pw.append("8:").append(String.valueOf(ad.getIs_ka())).append(SEPARATOR);
                pw.append("9:").append(String.valueOf(ad.getIs_time_ensure_discount())).append(SEPARATOR);
                pw.append("10:").append(String.valueOf(ad.getIs_eleme_deliver())).append(SEPARATOR);
                pw.append("11:").append(String.valueOf(ad.getIs_promotion_info())).append(SEPARATOR);

                pw.append("\n");
            });
        }
    }

    private static void outputBuyFile(List<Ad> ads, String buyOutput) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(buyOutput)))) {
            ads.stream().forEach(ad -> {
                pw.append(String.valueOf(ad.getIsBuy())).append(SEPARATOR);

                pw.append("1:").append(String.valueOf(ad.getIndex())).append(SEPARATOR);
                pw.append("2:").append(String.valueOf(ad.getIsSelect())).append(SEPARATOR);
                pw.append("3:").append(String.valueOf(ad.getIsNew())).append(SEPARATOR);
                pw.append("4:").append(String.valueOf(ad.getDistance())).append(SEPARATOR);
                pw.append("5:").append(String.valueOf(ad.getIs_premium())).append(SEPARATOR);
                pw.append("6:").append(String.valueOf(ad.getHas_image())).append(SEPARATOR);
                pw.append("7:").append(String.valueOf(ad.getIs_time_ensure())).append(SEPARATOR);
                pw.append("8:").append(String.valueOf(ad.getIs_ka())).append(SEPARATOR);
                pw.append("9:").append(String.valueOf(ad.getIs_time_ensure_discount())).append(SEPARATOR);
                pw.append("10:").append(String.valueOf(ad.getIs_eleme_deliver())).append(SEPARATOR);
                pw.append("11:").append(String.valueOf(ad.getIs_promotion_info())).append(SEPARATOR);

                pw.append("\n");
            });
        }
    }

    private static void outputTestFile(List<Ad> ads, String trainOutput) throws IOException {

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(trainOutput)))) {
            ads.stream().forEach(ad -> {
                // todo: here label is alayws 0.
                pw.append("0").append(SEPARATOR);

                pw.append("1:").append(String.valueOf(ad.getIndex())).append(SEPARATOR);
                pw.append("2:").append(String.valueOf(ad.getIsSelect())).append(SEPARATOR);
                pw.append("3:").append(String.valueOf(ad.getIsNew())).append(SEPARATOR);
                pw.append("4:").append(String.valueOf(ad.getDistance())).append(SEPARATOR);
                pw.append("5:").append(String.valueOf(ad.getIs_premium())).append(SEPARATOR);
                pw.append("6:").append(String.valueOf(ad.getHas_image())).append(SEPARATOR);
                pw.append("7:").append(String.valueOf(ad.getIs_time_ensure())).append(SEPARATOR);
                pw.append("8:").append(String.valueOf(ad.getIs_ka())).append(SEPARATOR);
                pw.append("9:").append(String.valueOf(ad.getIs_time_ensure_discount())).append(SEPARATOR);
                pw.append("10:").append(String.valueOf(ad.getIs_eleme_deliver())).append(SEPARATOR);
                pw.append("11:").append(String.valueOf(ad.getIs_promotion_info())).append(SEPARATOR);


                pw.append("\n");
            });
        }
    }


    private static Map<String, AdList> readAdList(String envFile) throws IOException {
        Map<String/* ad_list_id*/, AdList> adListMap = Maps.newHashMap();

        BufferedReader in = new BufferedReader(new FileReader(envFile));

        String line = in.readLine();    // ignore first line
        while ((line = in.readLine()) != null) {

            List<String> list = Splitter.on("\t").splitToList(line);
            String id = list.get(0);
            int isSelect = Integer.parseInt(list.get(1));
            int isNew = Integer.parseInt(list.get(5));
            double x = Double.parseDouble(list.get(6));
            double y = Double.parseDouble(list.get(7));

            int networkType = parseNetworkType(list.get(9));
            int platyform = parsePlatform(list.get(10));
            int netOp = parseNetworkOperator(list.get(13));

            AdList adList = new AdList(id, isSelect, isNew, x, y, networkType, platyform, netOp);

            AdList previous = adListMap.put(id, adList);
            if (previous != null) {
                System.out.println("Found Duplicate, id: " + id);
            }
        }
        return adListMap;
    }


    public static class Ad {
        int index;
        int isClick;
        int isBuy;

        double distance;


        // from AdList
        int isSelect;
        int isNew;


        // from restaurant

        int is_premium;
        int has_image;

        int is_time_ensure;
        int is_ka;
        int is_time_ensure_discount;
        int is_eleme_deliver;
        int is_promotion_info;

        public Ad(int index, int isClick, int isBuy, AdList adList, Restaurant rst) {
            this.index = index;
            this.isClick = isClick;
            this.isBuy = isBuy;
            this.isSelect = adList.getIsSelect();
            this.isNew = adList.getIsNew();
            this.is_premium = rst.getIs_premium();
            this.has_image = rst.getHas_image();
            this.is_time_ensure = rst.getIs_time_ensure();
            this.is_ka = rst.getIs_ka();
            this.is_time_ensure_discount = rst.getIs_time_ensure_discount();
            this.is_eleme_deliver = rst.getIs_eleme_deliver();
            this.is_promotion_info = rst.getIs_promotion_info();

            this.distance = Math.sqrt((rst.getX() - adList.getX()) * (rst.getX() - adList.getX()) +
                    (rst.getY() - adList.getY()) * (rst.getY() - adList.getY()));
        }

        public Ad(int index, AdList adList, Restaurant rst) {
            this.index = index;
            this.isClick = -1;
            this.isBuy = -1;
            this.isSelect = adList.getIsSelect();
            this.isNew = adList.getIsNew();
            this.is_premium = rst.getIs_premium();
            this.has_image = rst.getHas_image();
            this.is_time_ensure = rst.getIs_time_ensure();
            this.is_ka = rst.getIs_ka();
            this.is_time_ensure_discount = rst.getIs_time_ensure_discount();
            this.is_eleme_deliver = rst.getIs_eleme_deliver();
            this.is_promotion_info = rst.getIs_promotion_info();

            this.distance = Math.sqrt((rst.getX() - adList.getX()) * (rst.getX() - adList.getX()) +
                    (rst.getY() - adList.getY()) * (rst.getY() - adList.getY()));

        }

        public Ad(int index, AdList adList) {
            this.index = index;
            this.isClick = -1;
            this.isBuy = -1;
            this.isSelect = adList.getIsSelect();
            this.isNew = adList.getIsNew();
            this.is_premium = 0;
            this.has_image = 0;
            this.is_time_ensure = 0;
            this.is_ka = 0;
            this.is_time_ensure_discount = 0;
            this.is_eleme_deliver = 0;
            this.is_promotion_info = 0;

            this.distance = 9999;
        }

        public int getIndex() {
            return index;
        }

        public int getIsClick() {
            return isClick;
        }

        public int getIsBuy() {
            return isBuy;
        }

        public double getDistance() {
            return distance;
        }

        public int getIsSelect() {
            return isSelect;
        }

        public int getIsNew() {
            return isNew;
        }

        public int getIs_premium() {
            return is_premium;
        }

        public int getHas_image() {
            return has_image;
        }

        public int getIs_time_ensure() {
            return is_time_ensure;
        }

        public int getIs_ka() {
            return is_ka;
        }

        public int getIs_time_ensure_discount() {
            return is_time_ensure_discount;
        }

        public int getIs_eleme_deliver() {
            return is_eleme_deliver;
        }

        public int getIs_promotion_info() {
            return is_promotion_info;
        }
    }

    public static class AdList {
        String id;
        int isSelect;
        int isNew;

        double x;
        double y;

        int networkType;
        int platform;
        int netrowkOperator;

        public AdList(String id, int isSelect, int isNew, double x, double y, int networkType, int platform, int
                netrowkOperator) {
            this.id = id;
            this.isSelect = isSelect;
            this.isNew = isNew;
            this.x = x;
            this.y = y;
            this.networkType = networkType;
            this.platform = platform;
            this.netrowkOperator = netrowkOperator;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getId() {
            return id;
        }

        public int getIsSelect() {
            return isSelect;
        }

        public int getIsNew() {
            return isNew;
        }

        public int getNetworkType() {
            return networkType;
        }

        public int getPlatform() {
            return platform;
        }

        public int getNetrowkOperator() {
            return netrowkOperator;
        }
    }


    public static int parseNetworkType(String network) {
        switch (network) {
            case "\"WIFI\"":
                return 1;
            case "\"4G\"":
                return 2;
            case "\"3G\"":
                return 3;
            case "\"2G\"":
                return 4;
            case "\"UNKNOWN\"":
                return 5;
            default:
                System.out.println("Unknown Network Type: " + network);
                return 6;
        }
    }

    public static int parsePlatform(String plartform) {
        switch (plartform) {
            case "\"Android\"":
                return 1;
            case "\"iOS\"":
                return 2;
            default:
                System.out.println("Unknown Platform: " + plartform);
                return 3;
        }
    }

    public static int parseNetworkOperator(String op) {
        switch (op) {
            case "\"yd\"":
                return 1;
            case "\"lt\"":
                return 2;
            case "\"dx\"":
                return 3;
            case "\"OTHER\"":
                return 4;
            default:
                System.out.println("Unknown Network Operator: " + op);
                return 5;
        }
    }

    private static class Restaurant {
        double x;
        double y;
        int is_premium;
        int has_image;

        int is_time_ensure;
        int is_ka;
        int is_time_ensure_discount;
        int is_eleme_deliver;
        int is_promotion_info;

        public Restaurant(double x, double y, int is_premium, int has_image, int is_time_ensure, int is_ka,
                          int is_time_ensure_discount, int is_eleme_deliver, int is_promotion_info) {
            this.x = x;
            this.y = y;
            this.is_premium = is_premium;
            this.has_image = has_image;
            this.is_time_ensure = is_time_ensure;
            this.is_ka = is_ka;
            this.is_time_ensure_discount = is_time_ensure_discount;
            this.is_eleme_deliver = is_eleme_deliver;
            this.is_promotion_info = is_promotion_info;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }


        public int getIs_premium() {
            return is_premium;
        }

        public void setIs_premium(int is_premium) {
            this.is_premium = is_premium;
        }

        public int getHas_image() {
            return has_image;
        }

        public void setHas_image(int has_image) {
            this.has_image = has_image;
        }

        public int getIs_time_ensure() {
            return is_time_ensure;
        }

        public void setIs_time_ensure(int is_time_ensure) {
            this.is_time_ensure = is_time_ensure;
        }

        public int getIs_ka() {
            return is_ka;
        }

        public void setIs_ka(int is_ka) {
            this.is_ka = is_ka;
        }

        public int getIs_time_ensure_discount() {
            return is_time_ensure_discount;
        }

        public void setIs_time_ensure_discount(int is_time_ensure_discount) {
            this.is_time_ensure_discount = is_time_ensure_discount;
        }

        public int getIs_eleme_deliver() {
            return is_eleme_deliver;
        }

        public void setIs_eleme_deliver(int is_eleme_deliver) {
            this.is_eleme_deliver = is_eleme_deliver;
        }

        public int getIs_promotion_info() {
            return is_promotion_info;
        }

        public void setIs_promotion_info(int is_promotion_info) {
            this.is_promotion_info = is_promotion_info;
        }
    }
}
