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
@Deprecated
public class SimpleFeatureSelectorV1 {
    private static final Logger log = LoggerFactory.getLogger(SimpleFeatureSelectorV1.class);

    public static final String INFO_SOURCE_FILE = SOURCE_FILE_DIR + "his_eco_info.txt";
    public static final String ENV_SOURCE_FILE = SOURCE_FILE_DIR + "his_eco_env.txt";
    public static final String TEST_INFO_SOURCE_FILE = SOURCE_FILE_DIR + "next_eco_info.txt";
    public static final String TEST_ENV_SOURCE_FILE = SOURCE_FILE_DIR + "next_eco_env.txt";


    public static final String CLICK_OUTPUT_File = FEATURE_DATA_DIR + "simple_feature_selector_v1/click_train.txt";
    public static final String BUY_OUTPUT_File = FEATURE_DATA_DIR + "simple_feature_selector_v1/buy_train.txt";
    public static final String TEST_OUTPUT_File = FEATURE_DATA_DIR + "simple_feature_selector_v1/test.txt";

    private static final String SEPARATOR = " ";

    public static DatasetFile buildDataset() {
        log.warn("==构建SimpleFeatureSelectorV1选择的数据== 需确认已执行过SimpleFeatureSelector的main()将数据文件生成。");

        return new DatasetFile("simple_feature_selector_v1", CLICK_OUTPUT_File, BUY_OUTPUT_File, TEST_OUTPUT_File, 6);
    }

    public static void main(String[] args) throws IOException {
        formatTrainDataset(INFO_SOURCE_FILE, ENV_SOURCE_FILE, CLICK_OUTPUT_File, BUY_OUTPUT_File);
        formatTestDataset(TEST_INFO_SOURCE_FILE, TEST_ENV_SOURCE_FILE, TEST_OUTPUT_File);
    }

    private static void formatTrainDataset(String info, String env, String clickOutput, String buyOutput) throws IOException {
        Map<String, AdList> adListMap = readAdList(env);

        List<Ad> ads = Lists.newArrayList();

        BufferedReader in = new BufferedReader(new FileReader(info));

        int clickCount = 0, buyCount = 0;
        String line = in.readLine();    // ignore first line
        while ((line = in.readLine()) != null) {

            List<String> list = Splitter.on("\t").splitToList(line);
            String listId = list.get(1);

            AdList adList = adListMap.get(listId);
            if (adList == null) {
                System.out.println("Oops, list_id: [" + listId + "] not found!");
            } else {
                int index = Integer.parseInt(list.get(3));
                int isClick = Integer.parseInt(list.get(4));
                if (isClick == 1) {
                    clickCount++;
                }

                int isBuy = Integer.parseInt(list.get(5));
                if (isBuy == 1) {
                    buyCount++;
                }
                Ad ad = new Ad(index, isClick, isBuy, adList);

                if (isBuy == 1) {    //对 buy 扩容50倍
                    for (int i = 0; i < 50; i++) {
                        ads.add(ad);
                    }
                } else if (isClick == 1) { // 对click扩容10倍
                    for (int i = 0; i < 10; i++) {
                        ads.add(ad);
                    }
                } else {
                    ads.add(ad);
                }
            }
        }

        Collections.shuffle(ads);

        System.out.println("done. 扩容后size: " + ads.size() + " click, buy: " + clickCount + "," + buyCount + ", " +
                "click_rate: " + clickCount / ads.size() + ", buy_rate: " + buyCount / ads.size());

        outputClickFile(ads, clickOutput);
        outputBuyFile(ads, buyOutput);
        System.out.println("Format Train DatasetFile ok, clickOutput: " + clickOutput + ", buyOutput: " + buyOutput);

    }

    private static void formatTestDataset(String info, String env, String testOutput) throws IOException {
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

                Ad ad = new Ad(index, adList);
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
                pw.append("4:").append(String.valueOf(ad.getNetworkType())).append(SEPARATOR);
                pw.append("5:").append(String.valueOf(ad.getPlatform())).append(SEPARATOR);
                pw.append("6:").append(String.valueOf(ad.getNetrowkOperator()));

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
                pw.append("4:").append(String.valueOf(ad.getNetworkType())).append(SEPARATOR);
                pw.append("5:").append(String.valueOf(ad.getPlatform())).append(SEPARATOR);
                pw.append("6:").append(String.valueOf(ad.getNetrowkOperator())).append(SEPARATOR);
//                pw.append("7:").append(String.valueOf(ad.getIsClick()));

                pw.append("\n");
            });
        }
    }

    private static void outputTestFile(List<Ad> ads, String trainOutput) throws IOException {

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(trainOutput)))) {
            ads.stream().forEach(ad -> {
                pw.append(String.valueOf(ad.getIsClick())).append(SEPARATOR);

                pw.append("1:").append(String.valueOf(ad.getIndex())).append(SEPARATOR);
                pw.append("2:").append(String.valueOf(ad.getIsSelect())).append(SEPARATOR);
                pw.append("3:").append(String.valueOf(ad.getIsNew())).append(SEPARATOR);
                pw.append("4:").append(String.valueOf(ad.getNetworkType())).append(SEPARATOR);
                pw.append("5:").append(String.valueOf(ad.getPlatform())).append(SEPARATOR);
                pw.append("6:").append(String.valueOf(ad.getNetrowkOperator()));

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
            int networkType = parseNetworkType(list.get(9));
            int platyform = parsePlatform(list.get(10));
            int netOp = parseNetworkOperator(list.get(13));

            AdList adList = new AdList(id, isSelect, isNew, networkType, platyform, netOp);

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


        // from AdList
        int isSelect;
        int isNew;

        int networkType;
        int platform;
        int netrowkOperator;

        public Ad(int index, int isClick, int isBuy, AdList adList) {
            this.index = index;
            this.isClick = isClick;
            this.isBuy = isBuy;

            this.isSelect = adList.getIsSelect();
            this.isNew = adList.getIsNew();
            this.networkType = adList.getNetworkType();
            this.platform = adList.getPlatform();
            this.netrowkOperator = adList.getNetrowkOperator();
        }

        public Ad(int index, AdList adList) {
            this.index = index;
            this.isClick = -1; // not set
            this.isBuy = -1;    // not set

            this.isSelect = adList.getIsSelect();
            this.isNew = adList.getIsNew();
            this.networkType = adList.getNetworkType();
            this.platform = adList.getPlatform();
            this.netrowkOperator = adList.getNetrowkOperator();
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

    public static class AdList {
        String id;
        int isSelect;
        int isNew;

        int networkType;
        int platform;
        int netrowkOperator;

        public AdList(String id, int isSelect, int isNew, int networkType, int platform, int netrowkOperator) {
            this.id = id;
            this.isSelect = isSelect;
            this.isNew = isNew;
            this.networkType = networkType;
            this.platform = platform;
            this.netrowkOperator = netrowkOperator;
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
}
