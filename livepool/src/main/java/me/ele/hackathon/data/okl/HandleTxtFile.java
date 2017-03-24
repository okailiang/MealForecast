package me.ele.hackathon.data.okl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author oukailiang
 * @create 2016-10-28 上午11:06
 */

public class HandleTxtFile {
    private static final Logger LOG = LoggerFactory.getLogger(HandleTxtFile.class);
    //
    private static final String[] rstProperties = {"restaurant_id_0", "primary_category_1", "food_name_list_2", "category_list_3", "x_4", "y_5", "agent_fee_6", "is_premium_7", "address_type_8", "good_rating_rate_9", "open_month_num_10", "has_image_11", "has_food_img_12", "min_deliver_amount_13", "time_ensure_spent_14", "is_time_ensure_15", "is_ka_16", "is_time_ensure_discount_17", "is_eleme_deliver_18", "radius_19", "bu_flag_20", "brand_name_21", "service_rating_22", "invoice_23", "online_payment_24", "public_degree_25", "food_num_26", "food_image_num_27", "is_promotion_info_28", "is_bookable_29"};
    private static final String[] envProperties = {"list_id_0", "is_select_1", "day_no_2", "minutes_3", "eleme_device_id_4", "is_new_5", "x_6", "y_7", "user_id_8", "network_type_9", "platform_10", "brand_11", "model_12", "network_operator_13", "resolution_14", "channel_15"};
    //初始化呢数据
    private static Map<String, HisRstInfo> rstIdMap;
    private static Map<String, HisOrderInfo> orderIdMap;
    private static Map<String, HisEcoEnv> envIdMap;
    private static List<HisEcoInfo> nextInfoList;
    private static List<HisEcoInfo> infoList;
    private static Map<String, List<HisEcoInfo>> listIdInfoMap;
    private static Map<String, Map<Integer, List<HisEcoInfo>>> userIdDayNextInfoMap;
    private static Map<String, Map<Integer, List<HisEcoInfo>>> userIdDayInfoMap;

    //
    private static Map<String, Double> rstClickRate = new HashMap<>();
    private static Map<String, Double> rstBuyRate = new HashMap<>();


    public static void init() {
        System.out.println("开始初始化！");
        rstIdMap = setRstIdMap(getRstList());
        //
        List<HisEcoEnv> hisEcoEnvList = getEnvList();
        envIdMap = getEnvIdMap(hisEcoEnvList);
        //
        infoList = getInfoWithEnvRst(envIdMap, rstIdMap);
        //计算餐厅的点击和购买率
        calculateRstClickAndBuyRate(infoList);
        //将info按listId分类
        listIdInfoMap = setListIdInfoMap(infoList);
        //将info按用户id分类
        userIdDayInfoMap = setUserIdDayNextInfoMap(infoList);
        //
        nextInfoList = getNextInfoWithEnvRst(getEnvIdMap(getNextEnvList()), rstIdMap);
        //将next_info按用户分类
        userIdDayNextInfoMap = setUserIdDayNextInfoMap(nextInfoList);


        //orderIdMap = setOrderIdMap(getOrderList());
        System.out.println("初始化完成！");
    }

    public static List<HisEcoInfo> getInfoWithRst(Map<String, HisRstInfo> rstIdMap) {
        return getInfoWithEnvRstAndOrder(null, rstIdMap, null);
    }

    public static List<HisEcoInfo> getInfoWithEnv(Map<String, HisEcoEnv> envIdMap) {
        return getInfoWithEnvRstAndOrder(envIdMap, null, null);
    }

    public static List<HisEcoInfo> getInfoWithEnvRst(Map<String, HisEcoEnv> envIdMap, Map<String, HisRstInfo> rstIdMap) {
        return getInfoWithEnvRstAndOrder(envIdMap, rstIdMap, null);
    }

    public static List<HisEcoInfo> getNextInfoWithRst(Map<String, HisRstInfo> rstIdMap) {
        return getNextInfoWithEnvRstAndOrder(null, rstIdMap, null);
    }

    public static List<HisEcoInfo> getNextInfoWithEnv(Map<String, HisEcoEnv> nextEnvIdMap) {
        return getNextInfoWithEnvRstAndOrder(nextEnvIdMap, null, null);
    }

    public static List<HisEcoInfo> getNextInfoWithEnvRst(Map<String, HisEcoEnv> nextEnvIdMap, Map<String, HisRstInfo> rstIdMap) {
        return getNextInfoWithEnvRstAndOrder(nextEnvIdMap, rstIdMap, null);
    }

    public static List<HisEcoInfo> getNextInfoWithEnvRstAndOrder(Map<String, HisEcoEnv> envIdMap
            , Map<String, HisRstInfo> rstIdMap, Map<String, HisOrderInfo> orderIdMap) {
        System.out.println("开始读取next_his_eco_info.txt");
        List<HisEcoInfo> infoList = readInfoWithEnvRstAndOrder(Constant.NEXT_INFO_FILE, envIdMap, rstIdMap, orderIdMap);
        System.out.println("读取next_his_eco_info.txt结束");
        return infoList;
    }

    public static List<HisEcoInfo> getInfoWithEnvRstAndOrder(Map<String, HisEcoEnv> envIdMap
            , Map<String, HisRstInfo> rstIdMap, Map<String, HisOrderInfo> orderIdMap) {
        System.out.println("开始读取his_eco_info.txt");
        List<HisEcoInfo> infoList = readInfoWithEnvRstAndOrder(Constant.INFO_FILE, envIdMap, rstIdMap, orderIdMap);
        System.out.println("读取his_eco_info.txt结束");
        return infoList;
    }

    public static List<HisRstInfo> getRstList() {
        BufferedReader br = null;
        String line;
        List<HisRstInfo> rstList = new ArrayList<>();
        try {
            //读
            br = new BufferedReader(new FileReader(new File(Constant.RST_FILE)));
            br.readLine();
            System.out.println("开始读取rst_info.txt");
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                HisRstInfo rstInfo = getHisRst(rowArr);
                rstList.add(rstInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("读取rst_info.txt结束");
        return rstList;
    }

    public static List<HisOrderInfo> getOrderList() {
        BufferedReader br = null;
        String line;
        List<HisOrderInfo> orderList = new ArrayList<>();
        try {
            //读
            br = new BufferedReader(new FileReader(new File(Constant.ORDER_FILE)));
            br.readLine();
            System.out.println("开始读取his_order_info.txt");
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                HisOrderInfo orderInfo = getOrderInfo(rowArr);
                orderList.add(orderInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("读取his_order_info.txt结束");
        return orderList;
    }

    public static List<HisEcoEnv> getEnvList() {
        System.out.println("开始读取his_eco_env.txt");
        List<HisEcoEnv> envList = readEnv(Constant.ENV_FILE);
        System.out.println("读取his_eco_env.txt结束");
        return envList;
    }

    private static List<HisEcoEnv> getNextEnvList() {
        System.out.println("开始读取next_eco_env.txt");
        List<HisEcoEnv> envList = readEnv(Constant.NEXT_ENV_FILE);
        System.out.println("读取next_eco_env.txt结束");
        return envList;
    }

    public static List<HisEcoInfo> getInfoList() {
        System.out.println("开始读取his_eco_info.txt");
        List<HisEcoInfo> infoList = readInfo(Constant.INFO_FILE);
        System.out.println("读取his_eco_info.txt结束");
        return infoList;
    }

    private static List<HisEcoInfo> getNextInfoList() {
        System.out.println("开始读取next_eco_info.txt");
        List<HisEcoInfo> infoList = readInfo(Constant.NEXT_INFO_FILE);
        System.out.println("读取next_eco_info.txt结束");
        return infoList;
    }

    public static Map<String, HisRstInfo> getRstIdMap() {
        return rstIdMap;
    }

    public static List<HisEcoInfo> getInfos() {
        return infoList;
    }

    public static List<HisEcoInfo> getNextInfos() {
        return nextInfoList;
    }

    public static Map<String, Map<Integer, List<HisEcoInfo>>> getUserIdDayInfoMap() {
        return userIdDayInfoMap;
    }

    public static Map<String, Map<Integer, List<HisEcoInfo>>> getUserIdDayNextInfoMap() {
        return userIdDayNextInfoMap;
    }


    public static Map<String, HisOrderInfo> getOrderIdMap() {
        return orderIdMap;
    }

    public static Map<String, Double> getRstClickRateMap() {
        return rstClickRate;
    }

    public static Map<String, Double> getRstBuyRateMap() {
        return rstBuyRate;
    }

    /**
     * 计算每周的用户增长
     */
    public static void calculateUserGrowth(List<HisEcoEnv> envList) {
        System.out.println("计算每周用户情况开始！");
        String userGrowthFile = Constant.TXT_FILE_DIR + "result_temp/user_week_growth.txt";
        Map<Integer, Map<String, Integer>> weekUserMap = new HashMap<>();
        Map<String, Integer> newOldUserMap;
        int week;
        //1.先将每周用户分开，都先认为是新用户
        for (HisEcoEnv env : envList) {
            System.out.println(env.getDay_no());
            String userId = env.getUser_id();
            if ("NULL".equals(userId)) {
                continue;
            }
            week = getWeek(env.getDay_no());
            //每周的新老用户
            newOldUserMap = weekUserMap.get(week);
            if (newOldUserMap != null) {
                newOldUserMap.put(userId, Constant.ZERO);
            } else {
                newOldUserMap = new HashMap<>();
                newOldUserMap.put(userId, Constant.ZERO);
            }
            weekUserMap.put(week, newOldUserMap);
        }
        //2.区分每周的新老用户,并写到文件里
        File file = new File(userGrowthFile);
        if (file.exists()) {
            file.delete();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Constant.WEEK_USER_GROWTH);
        outOriginFile(userGrowthFile, sb.toString());
        int weekNum = Constant.MIN_WEEK + weekUserMap.size();

        for (week = Constant.MIN_WEEK; week < weekNum; week++) {
            sb = new StringBuilder();
            int weekOldUser = 0;
            int weekNewUser = 0;
            newOldUserMap = weekUserMap.get(week);
            for (Map.Entry<String, Integer> entry : newOldUserMap.entrySet()) {
                String userId = entry.getKey();
                if ("NULL".equals(userId)) {
                    System.out.println("用户id为空！");
                    continue;
                }
                if (isOldUser(weekUserMap, week, userId)) {
                    newOldUserMap.put(userId, Constant.ONE);
                    weekOldUser++;
                } else {
                    weekNewUser++;
                }
            }
            int weekTotalNum = weekOldUser + weekNewUser;
            System.out.println("第" + week + "周用户总数：" + weekTotalNum + "\n老用户数："
                    + weekOldUser + "\n新用户数：" + weekNewUser + "\n用用户比率："
                    + ((double) weekNewUser / weekTotalNum));
            sb.append(week).append(Constant.SEPARATOR_TAB).append(weekTotalNum).append(Constant.SEPARATOR_TAB).append(weekOldUser)
                    .append(Constant.SEPARATOR_TAB).append(weekNewUser).append(Constant.SEPARATOR_TAB).append(((double) weekNewUser / weekTotalNum));
            outOriginFile(userGrowthFile, sb.toString());
        }
        System.out.println("计算每周用户情况结束！");
    }

    public static Map<String, Map<Integer, List<HisEcoInfo>>> setUserIdDayNextInfoMap(List<HisEcoInfo> infoList) {
        System.out.println("按userId和day_no分类info开始！");
        Map<Integer, List<HisEcoInfo>> daynoMap;
        List<HisEcoInfo> hisEcoInfoList;
        //Map<String, List<HisEcoInfo>>
        Map<String, Map<Integer, List<HisEcoInfo>>> userIdDayNextInfoMap = new HashMap<>();
        for (HisEcoInfo info : infoList) {
            String userId = info.getHisEcoEnv().getUser_id();
            int dayNo = Integer.parseInt(info.getHisEcoEnv().getDay_no());
            if ((daynoMap = userIdDayNextInfoMap.get(userId)) != null) {
                if ((hisEcoInfoList = daynoMap.get(dayNo)) == null) {
                    hisEcoInfoList = new ArrayList<>();
                }
            } else {
                daynoMap = new HashMap<>();
                hisEcoInfoList = new ArrayList<>();
            }
            hisEcoInfoList.add(info);
            sortDaynoMinute(hisEcoInfoList);
            daynoMap.put(dayNo, hisEcoInfoList);
            userIdDayNextInfoMap.put(userId, daynoMap);
        }
        System.out.println("按userId和day_no分类info开始！");
        return userIdDayNextInfoMap;
    }

    public static Map<String, HisEcoInfo> getLogIdInfoMap(List<HisEcoInfo> infoList) {
        System.out.println("按logId分类info开始！");
        Map<String, HisEcoInfo> logIdInfoMap = new HashMap<>();
        for (HisEcoInfo info : infoList) {
            String logId = info.getLog_id();
            logIdInfoMap.put(logId, info);
        }
        System.out.println("按logId分类info开始！");
        return logIdInfoMap;
    }

    public static Map<String, List<HisEcoInfo>> setListIdInfoMap(List<HisEcoInfo> infoList) {
        System.out.println("按listId分类info开始！");
        //Map<String, List<HisEcoInfo>>
        Map<String, List<HisEcoInfo>> listIdInfoMap = new HashMap<>();
        List<HisEcoInfo> hisEcoInfoList;
        for (HisEcoInfo info : infoList) {
            String listId = info.getList_id();
            if ((hisEcoInfoList = listIdInfoMap.get(listId)) == null) {
                hisEcoInfoList = new ArrayList<>();
            }
            hisEcoInfoList.add(info);
            sortDaynoMinute(hisEcoInfoList);
            listIdInfoMap.put(listId, hisEcoInfoList);
        }
        System.out.println("按listId分类info结束！");
        return listIdInfoMap;
    }

    private static void sortDaynoMinute(List<HisEcoInfo> infoList) {
        infoList.sort((x, y) -> {
            int value = x.getHisEcoEnv().getDay_no().compareTo(y.getHisEcoEnv().getDay_no());
            if (value == 0) {
                value = x.getHisEcoEnv().getMinutes().compareTo(y.getHisEcoEnv().getMinutes());
            }
            return value;
        });
    }

    public static void calculateRstClickAndBuyRate(List<HisEcoInfo> infoList) {
        System.out.println("计算餐厅点击率和购买率开始！");
        Map<String, List<Integer>> rstMap = new HashMap<>();
        for (HisEcoInfo info : infoList) {
            addRstClickBuy(rstMap, info);
        }
//        //计算点击率和购买率
        getRstClickBuyRate(rstMap);
        System.out.println("餐厅总数：" + rstMap.size());
        System.out.println("计算餐厅点击率和购买率技术！");
    }

    /**
     * 将info env rst 合并为一个文件
     *
     * @param envIdMap
     * @param rstIdMap
     */
    public static void combineInfoEnvRst(Map<String, HisEcoEnv> envIdMap, Map<String, HisRstInfo> rstIdMap) {
        BufferedReader br = null;
        FileReader fr = null;
        BufferedWriter bw = null;
        FileWriter fw = null;
        String line;
        List<HisEcoInfo> infoList = new ArrayList<>();
        try {
            //读
            fr = new FileReader(new File(Constant.INFO_FILE));
            fw = new FileWriter(new File(Constant.HIS_INFO_ENV_RST));
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            br.readLine();
            System.out.println("开始读合并info_env_rst.txt");
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                HisEcoInfo info = getHisInfo(rowArr);
                //eco_env
                if (envIdMap != null) {
                    String listId = info.getList_id();
                    if (envIdMap.get(listId) == null) {
                        System.out.println("listId=" + listId + "不存在eco_env.txt中");
                    } else {
                        info.setHisEcoEnv(envIdMap.get(listId));
                    }
                }
                //rst_info
                if (rstIdMap != null) {
                    String rstId = info.getRestaurant_id();
                    if (rstIdMap.get(rstId) == null) {
                        System.out.println("rstId=" + rstId + "不存在rst_info.txt中");
                    } else {
                        info.setHisRstInfo(rstIdMap.get(rstId));
                    }
                }

                bw.write(getInfoEnvRst(info) + "\n");
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                br.close();
                fr.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("合并info_env_rst.txt结束");

    }

    private static void addRstClickBuy(Map<String, List<Integer>> rstMap, HisEcoInfo info) {
        List<Integer> clickBuyNumList;
        HisRstInfo rstInfo = info.getHisRstInfo();
        if (rstInfo == null) {
            System.out.println("该HisEcoInfo还没有添加餐厅信息");
            return;
        }
        String rstId = rstInfo.getRestaurant_id();
        if ((clickBuyNumList = rstMap.get(rstId)) == null) {
            clickBuyNumList = new ArrayList<>();
            //list索引0、1、2分别为总数、点击数和购买数
            clickBuyNumList.add(0);
            clickBuyNumList.add(0);
            clickBuyNumList.add(0);
        }

        int total = clickBuyNumList.get(0) + 1;
        clickBuyNumList.remove(0);
        clickBuyNumList.add(0, total);
        if (Constant.S_ONE.equals(info.getIs_click())) {
            int clickNum = clickBuyNumList.get(1) + 1;
            clickBuyNumList.remove(1);
            clickBuyNumList.add(1, clickNum);
        }
        if (Constant.S_ONE.equals(info.getIs_buy())) {
            int buyNum = clickBuyNumList.get(2) + 1;
            clickBuyNumList.remove(2);
            clickBuyNumList.add(2, buyNum);
        }
        rstMap.put(rstId, clickBuyNumList);
    }

    private static void getRstClickBuyRate(Map<String, List<Integer>> rstMap) {
        String outFile = Constant.TXT_FILE_DIR + "result_temp/rst_click_buy_rate.txt";
        File file = new File(outFile);
        if (file.exists()) {
            file.delete();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.RST_CLICK_BUY_RATE);
        outOriginFile(outFile, sb.toString());
        double rstClickRateAvg;
        double rstBuyRateavg;
        int clickTotal = 0;
        int buyTotal = 0;
        int total = 0;
        //
        for (Map.Entry<String, List<Integer>> entry : rstMap.entrySet()) {
            sb = new StringBuilder();
            String key = entry.getKey();
            int totalNum = entry.getValue().get(0);
            int clickNum = entry.getValue().get(1);
            int buyNum = entry.getValue().get(2);
            clickTotal = clickTotal + clickNum;
            buyTotal = buyTotal + buyNum;
            total = total + totalNum;
            rstClickRate.put(key, formatRate((double) clickNum / totalNum));
            rstBuyRate.put(key, formatRate((double) buyNum / totalNum));
            System.out.println("餐厅：" + key + " 点击率：" + rstClickRate.get(key) + " 购买率：" + rstBuyRate.get(key));
            sb.append(key).append(Constant.SEPARATOR_TAB).append(totalNum).append(Constant.SEPARATOR_TAB)
                    .append(clickNum).append(Constant.SEPARATOR_TAB).append(buyNum).append(Constant.SEPARATOR_TAB)
                    .append(rstClickRate.get(key)).append(Constant.SEPARATOR_TAB).append(rstBuyRate.get(key));
            outOriginFile(outFile, sb.toString());
        }
        rstClickRateAvg = formatRate((double) clickTotal / total);
        rstBuyRateavg = formatRate((double) buyTotal / total);
        // Constant.rst_click_rate_avg = rstClickRateAvg;
        // Constant.rst_buy_rate_avg = rstBuyRateavg;
        System.out.println("餐厅：" + total + " 点击率avg：" + rstClickRateAvg
                + " 购买率avg：" + rstBuyRateavg);
    }

    private static double formatRate(double rate) {
//        DecimalFormat df = new DecimalFormat("0.0000");
//        return Double.parseDouble(df.format(rate));
        BigDecimal b = new BigDecimal(rate);
        return b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static List<HisEcoInfo> readInfoWithEnvRstAndOrder(String infoFilePath, Map<String
            , HisEcoEnv> envIdMap, Map<String, HisRstInfo> rstIdMap
            , Map<String, HisOrderInfo> orderIdMap) {
        BufferedReader br = null;
        String line;
        List<HisEcoInfo> infoList = new ArrayList<>();
        try {
            //读
            br = new BufferedReader(new FileReader(new File(infoFilePath)));
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                HisEcoInfo info = getHisInfo(rowArr);
                //eco_env
                if (envIdMap != null) {
                    String listId = info.getList_id();
                    if (envIdMap.get(listId) == null) {
                        System.out.println("listId=" + listId + "不存在eco_env.txt中");
                        continue;
                    } else {
                        info.setHisEcoEnv(envIdMap.get(listId));
                    }
                    if (isFilterDayNo(info.getHisEcoEnv().getDay_no())) {
                        continue;
                    }
                }

                //rst_info
                if (rstIdMap != null) {
                    String rstId = info.getRestaurant_id();
                    if (rstIdMap.get(rstId) == null) {
                        System.out.println("rstId=" + rstId + "不存在rst_info.txt中");
                    } else {
                        info.setHisRstInfo(rstIdMap.get(rstId));
                    }
                }
                //order_info
                if (orderIdMap != null) {
                    String orderId = info.getOrder_id();
                    if (orderIdMap.get(orderId) == null) {
                        System.out.println("orderId=" + orderId + "不存在order_info.txt中");
                    } else {
                        info.setHisOrderInfo(orderIdMap.get(orderId));
                    }
                }
                infoList.add(info);
                // System.out.println("infoList.size=" + infoList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return infoList;
    }

    private static List<HisEcoEnv> readEnv(String filePath) {
        BufferedReader br = null;
        String line;
        List<HisEcoEnv> envList = new ArrayList<>();
        try {
            //读
            br = new BufferedReader(new FileReader(new File(filePath)));
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                if (isFilterDayNo(rowArr[2])) {
                    continue;
                }
                HisEcoEnv env = getHisEnv(rowArr);
                envList.add(env);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return envList;
    }

    private static List<HisEcoInfo> readInfo(String filePath) {
        BufferedReader br = null;
        String line;
        List<HisEcoInfo> infoList = new ArrayList<>();
        try {
            //读
            br = new BufferedReader(new FileReader(new File(filePath)));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                HisEcoInfo info = getHisInfo(rowArr);
                infoList.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return infoList;
    }


    public static Map<String, HisEcoEnv> getEnvIdMap(List<HisEcoEnv> envList) {
        System.out.println("开始构建EnvIdMap！");
        Map<String, HisEcoEnv> hisEcoEnvMap = new HashMap<>();
        for (HisEcoEnv hisEcoEnv : envList) {
            hisEcoEnvMap.put(hisEcoEnv.getList_id(), hisEcoEnv);
        }
        System.out.println("构建EnvIdMap结束！");
        return hisEcoEnvMap;
    }

    private static Map<String, HisRstInfo> setRstIdMap(List<HisRstInfo> rstInfoList) {
        System.out.println("开始构建RstIdMap！");
        Map<String, HisRstInfo> rstMap = new HashMap<>();
        for (HisRstInfo hisRstInfo : rstInfoList) {
            rstMap.put(hisRstInfo.getRestaurant_id(), hisRstInfo);
        }
        System.out.println("构建RstIdMap结束！");
        return rstMap;
    }

    private static Map<String, HisOrderInfo> setOrderIdMap(List<HisOrderInfo> OrderInfoList) {
        System.out.println("开始构建OrderIdMap！");
        Map<String, HisOrderInfo> orderMap = new HashMap<>();
        for (HisOrderInfo order : OrderInfoList) {
            orderMap.put(order.getOrder_id(), order);
        }
        System.out.println("构建OrderIdMap结束！");
        return orderMap;
    }


    private static boolean isFilterDayNo(String dayNo) {
        if (dayNo != null && Integer.parseInt(dayNo) < Constant.AFTER_WEEK) {
            return true;
        }
        return false;
    }

    private static boolean containProperties(String[] propertiesArr, int index) {
        boolean flag = false;
        int len = propertiesArr.length;
        for (int i = 0; i < len; i++) {
            if (Integer.parseInt(propertiesArr[i].substring(len - 1)) == index) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private static void outOriginFile(String outFilePath, String rowValue) {

        BufferedWriter bw = null;
        File file = new File(outFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(rowValue + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw == null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从当前周向前搜索是否有该用户
     *
     * @param weekUserMap
     * @param currentWeek
     * @param userId
     * @return
     */
    private static boolean isOldUser(Map<Integer, Map<String, Integer>> weekUserMap
            , int currentWeek, String userId) {
        Map<String, Integer> newOldUserMap;
        while (currentWeek > Constant.MIN_WEEK) {
            newOldUserMap = weekUserMap.get(currentWeek - 1);
            if (newOldUserMap.get(userId) != null) {
                return true;
            }
            currentWeek--;
        }
        return false;
    }

    private static void outputFile(List<String> list, String outputFile) throws IOException {
        File file = new File(outputFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            list.stream().forEach(line -> {
                pw.append(line).append("\n");
            });
        }
    }

    private static int getWeek(String dayNo) {
        if ("NULL".equals(dayNo)) {
            return -1;
        }
        int day = Integer.parseInt(dayNo);
        int week = day / 7;
        return day % 7 == 0 ? week : (week + 1);
    }

    private static HisRstInfo getHisRst(String[] rowArr) {
        HisRstInfo rst = new HisRstInfo();
        rst.setRestaurant_id(rowArr[0]);
        rst.setPrimary_category(rowArr[1]);
        rst.setFood_name_list(rowArr[2]);
        rst.setCategory_list(rowArr[3]);
        rst.setX(rowArr[4]);
        rst.setY(rowArr[5]);
        rst.setAgent_fee(rowArr[6]);
        rst.setIs_premium(rowArr[7]);
        rst.setAddress_type(rowArr[8]);
        rst.setGood_rating_rate(rowArr[9]);
        rst.setOpen_month_num(rowArr[10]);
        rst.setHas_image(rowArr[11]);
        rst.setHas_food_img(rowArr[12]);
        rst.setMin_deliver_amount(rowArr[13]);
        rst.setTime_ensure_spent(rowArr[14]);
        rst.setIs_time_ensure(rowArr[15]);
        rst.setIs_ka(rowArr[16]);
        rst.setIs_time_ensure_discount(rowArr[17]);
        rst.setIs_eleme_deliver(rowArr[18]);
        rst.setRadius(rowArr[19]);
        rst.setBu_flag(rowArr[20]);
        rst.setBrand_name(rowArr[21]);
        rst.setService_rating(rowArr[22]);
        rst.setInvoice(rowArr[23]);
        rst.setOnline_payment(rowArr[24]);
        rst.setPublic_degree(rowArr[25]);
        rst.setFood_num(rowArr[26]);
        rst.setFood_image_num(rowArr[27]);
        rst.setIs_promotion_info(rowArr[28]);
        rst.setIs_bookable(rowArr[29]);
        return rst;
    }

    private static HisEcoInfo getHisInfo(String[] rowArr) {
        HisEcoInfo info = new HisEcoInfo();
        info.setLog_id(rowArr[0]);
        info.setList_id(rowArr[1]);
        info.setRestaurant_id(rowArr[2]);
        info.setSort_index(rowArr[3]);
        info.setIs_click(rowArr[4]);
        info.setIs_buy(rowArr[5]);
        info.setIs_raw_buy(rowArr[6]);
        info.setOrder_id(rowArr[7]);
        return info;
    }

    private static HisEcoEnv getHisEnv(String[] rowArr) {
        HisEcoEnv env = new HisEcoEnv();
        env.setList_id(rowArr[0]);
        env.setIs_select(rowArr[1]);
        env.setDay_no(rowArr[2]);
        env.setMinutes(rowArr[3]);
        env.setEleme_device_id(rowArr[4]);
        env.setIs_new(rowArr[5]);
        env.setX(rowArr[6]);
        env.setY(rowArr[7]);
        env.setUser_id(rowArr[8]);
        env.setNetwork_type(rowArr[9]);
        env.setPlatform(rowArr[10]);
        env.setBrand(rowArr[11]);
        env.setModel(rowArr[12]);
        env.setNetwork_operator(rowArr[13]);
        env.setResolution(rowArr[14]);
        env.setChannel(rowArr[15]);
        return env;
    }

    private static HisOrderInfo getOrderInfo(String[] rowArr) {
        HisOrderInfo order = new HisOrderInfo();
        order.setDay_no(rowArr[0]);
        order.setMinutes(rowArr[1]);
        order.setOrder_id(rowArr[2]);
        order.setRestaurant_id(rowArr[3]);
        order.setDeliver_fee(rowArr[4]);
        order.setIs_online_paid(rowArr[5]);
        order.setOrder_process_minutes(rowArr[6]);
        order.setRestaurant_num(rowArr[7]);
        order.setAddress_type(rowArr[8]);
        order.setIs_valid(rowArr[9]);
        order.setIs_book(rowArr[10]);
        order.setIs_coupon(rowArr[11]);
        order.setIs_invoice(rowArr[12]);
        order.setPindan_flag(rowArr[13]);
        order.setX(rowArr[14]);
        order.setY(rowArr[15]);
        order.setBu_flag(rowArr[16]);
        order.setEleme_order_total(rowArr[17]);
        order.setTotal(rowArr[18]);
        order.setCut_money(rowArr[19]);
        order.setIs_activity(rowArr[20]);
        order.setHas_new_user_subsidy(rowArr[21]);
        order.setHongbao_amount(rowArr[22]);
        order.setReceiver_deliver_fee(rowArr[23]);
        order.setUser_id(rowArr[24]);
        order.setFood_name(rowArr[25]);
        order.setFood_category(rowArr[26]);
        return order;
    }

    private static String getInfoEnvRst(HisEcoInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append(info.getLog_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getList_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getRestaurant_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getSort_index()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getIs_click()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getIs_buy()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getIs_raw_buy()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getOrder_id()).append(Constant.SEPARATOR_TAB);
        //env
        sb.append(info.getHisEcoEnv().getList_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getIs_select()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getDay_no()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getMinutes()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getEleme_device_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getIs_new()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getX()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getY()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getUser_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getNetwork_type()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getPlatform()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getBrand()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getModel()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getNetwork_operator()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getResolution()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisEcoEnv().getChannel()).append(Constant.SEPARATOR_TAB);
        //rst
        sb.append(info.getHisRstInfo().getRestaurant_id()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getPrimary_category()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getFood_name_list()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getCategory_list()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getX()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getY()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getAgent_fee()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_premium()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getAddress_type()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getGood_rating_rate()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getOpen_month_num()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getHas_image()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getHas_food_img()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getMin_deliver_amount()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getTime_ensure_spent()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_time_ensure()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_ka()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_time_ensure_discount()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_eleme_deliver()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getRadius()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getBu_flag()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getBrand_name()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getService_rating()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getInvoice()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getOnline_payment()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getPublic_degree()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getFood_num()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getFood_image_num()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_promotion_info()).append(Constant.SEPARATOR_TAB);
        sb.append(info.getHisRstInfo().getIs_bookable()).append(Constant.SEPARATOR_TAB);
        //
        return sb.toString();
    }

    private static void runUserIdRule1() {
        //将is_click、 is_buy、is_ray_buy职位0
        //Rules.setNextInfoClickBuyIsZero(nextInfoList);
        //生成logId对应的info map
        Map<String, HisEcoInfo> logIdNextInfoMap = getLogIdInfoMap(nextInfoList);

        //Map<String, ResultInfo> newResultMap = new HashMap<>();
        List<HisEcoInfo> nextInfoList;
        Map<String, HisRstInfo> noBeforeRstMap = new HashMap<>();
        int count = 0;
        //0.按userId处理-->dayNo->minutes
        Map<String, Map<Integer, List<HisEcoInfo>>> userIdNextMap = getUserIdDayNextInfoMap();
        for (Map.Entry<String, Map<Integer, List<HisEcoInfo>>> entry : userIdNextMap.entrySet()) {
            String userId = entry.getKey();
            //1.获取next_info和his_info中该用户对应的所有天的info
            Map<Integer, List<HisEcoInfo>> nextDaynoInfoMap = entry.getValue();
            Map<Integer, List<HisEcoInfo>> daynoInfoMap = userIdDayInfoMap.get(userId);
            //按天排序,
            List<Integer> nextDaylist = Rules.getDayList(nextDaynoInfoMap);//预测集中用户出现的天
            List<Integer> hisDaylist = Rules.getDayList(daynoInfoMap);//his集中用户出现的天
            //2.向前查找该用户的行为
            //******************************************************************
            int beforeDay = Constant.TEST_BEGAIN_DAY;//
            List<Integer> recordBeforeDays = new ArrayList<>();//向前出现的天
            List<Integer> beforeBuyDays = new ArrayList<>();//向前出现购买天
            List<Integer> beforeClickDays = new ArrayList<>();//向前出现点击的天
            Map<Integer, List<HisEcoInfo>> recordBeforeDayMap = new HashMap<>();//向前出现天对应的info
            Map<Integer, List<HisEcoInfo>> beforeDayClickMap = new HashMap<>();//向前出现天对应产生点击的info
            Map<Integer, List<HisEcoInfo>> beforeDayBuyMap = new HashMap<>();//向前出现天对应产生购买的info
            List<String> buyRstIdList = new ArrayList<>();
            List<String> clickRstIdList = new ArrayList<>();

            double userClickRate = 0.0;
            double userBuyRate = 0.0;
            int hisTotal = 0;
            int hisClickNum = 0;
            int hisBuyNum = 0;
            List<HisEcoInfo> beforeOneDayList;
            if (daynoInfoMap != null) {
                for (int i = 0; i < hisDaylist.size(); i++) {
                    beforeDay = hisDaylist.get(i);
                    if (beforeDay < Constant.BEFORE_DAY) {
                        continue;
                    }
                    beforeOneDayList = daynoInfoMap.get(beforeDay);
                    if (beforeOneDayList == null) {
                        continue;
                    }
                    //
                    List<HisEcoInfo> beforeBuyInfos = new ArrayList<>();
                    List<HisEcoInfo> beforeClickInfos = new ArrayList<>();
                    for (HisEcoInfo info : beforeOneDayList) {
                        //click
                        if (Constant.S_ONE.equals(info.getIs_click())) {
                            beforeClickInfos.add(info);
                            clickRstIdList.add(info.getRestaurant_id());
                        }
                        //buy
                        if (Constant.S_ONE.equals(info.getIs_buy())) {
                            beforeBuyInfos.add(info);
                            buyRstIdList.add(info.getRestaurant_id());
                        }
                    }
                    if (beforeClickInfos.size() > 0) {
                        beforeDayClickMap.put(beforeDay, beforeClickInfos);
                        beforeClickDays.add(beforeDay);
                        hisClickNum = hisClickNum + beforeClickInfos.size();
                    }
                    if (beforeBuyInfos.size() > 0) {
                        beforeDayBuyMap.put(beforeDay, beforeBuyInfos);
                        beforeBuyDays.add(beforeDay);
                        hisBuyNum = hisBuyNum + beforeBuyInfos.size();
                    }
                    //
                    recordBeforeDays.add(beforeDay);
                    recordBeforeDayMap.put(beforeDay, beforeOneDayList);
                    hisTotal = hisTotal + beforeOneDayList.size();
                    //System.out.println(beforeOneDayList.get(0).getHisEcoEnv().getDay_no());
                }
                if (hisTotal != 0) {
                    userClickRate = formatRate((double) hisClickNum / hisTotal);
                    userBuyRate = formatRate((double) hisBuyNum / hisTotal);
                    //  System.out.println("user click rate" + userClickRate);
                    //  System.out.println("user buy rate" + userBuyRate);
                }
            }
            //向前搜索用户行为结束
            //******************************************************************

            //3.向后查找该用户的行为开始，以下均是按用户每天来处理
            int nextDaySize = nextDaylist.size();//用户在next_info中出现的天数
            List<HisEcoInfo> predBuyList = new ArrayList<>();
            List<HisEcoInfo> predClickList = new ArrayList<>();
            double predClickNum;
            double predBuyNum;
            for (int i = 0; i < nextDaySize; i++) {
                int currentDay = nextDaylist.get(i);
                //3.1.获得当天对应的next_info
                nextInfoList = nextDaynoInfoMap.get(currentDay);
                int nextInfoSize = nextInfoList.size();
                String rstId;
                String logId;
                String primaryCategory;
                int isbuyFlag = 0;

                for (HisEcoInfo in : nextInfoList) {
                    rstId = in.getRestaurant_id();
                    if (rstClickRate.get(rstId) == null) {
                        noBeforeRstMap.put(rstId, in.getHisRstInfo());
                        System.out.println("rstId is null:" + rstId);
                        continue;
                    }
                }
                //预测该用户当天的点击数和购买数
                predClickNum = Math.ceil(nextInfoSize * userClickRate);
                predBuyNum = Math.ceil(nextInfoSize * userBuyRate);
                //找到最近购买最多的餐厅
                List<HisRstInfo> buyRstTop = Rules.findRecentBuyTopRst(beforeBuyDays, beforeDayBuyMap);
                List<HisRstInfo> clickRstTop = Rules.findRecentBuyTopRst(beforeClickDays, beforeDayClickMap);
                List<HisRstInfo> nextRstTop = Rules.findRecentBuyTopRst(nextDaylist, nextDaynoInfoMap);
                //当天点击出现最高的餐厅
                List<String> curDayRstTop = Rules.getNextDayRstTop(nextInfoList);

                int dayBuyNum = Rules.getDayBuyNum(nextInfoList);
                int dayOnlyBuyNum = Rules.getDayOnlyBuyNum(nextInfoList);
                //进入规则区
                Rules.envUserIdIsNull(nextInfoList);
                // Rules.envIsNewOrSelect(nextInfoList);
                Rules.removeAfterIndexInfo(nextInfoList, 50);

                //(1)当天没有购买或者只有一次购买，则不进行干预
                if (dayBuyNum == 0) {
                    continue;
                }
                //(2)当天两次以上购买且有一次以上是is_click=0，则去除is_click=0&&is_buy=0
                if ((dayBuyNum - dayOnlyBuyNum) >= 1) {
                    Rules.removeDayOnlyBuyInfo(nextInfoList, buyRstIdList);
                    dayBuyNum = Rules.getDayBuyNum(nextInfoList);
                    //在（2）中去除dayOnlyBuyNum后dayBuyNum
                    if (dayBuyNum == 1) {
                        continue;
                    }
                }
                //(3)全是buy或者全是onlybuy
                List<HisEcoInfo> buyNextInfoList = Rules.getBuyNextInfo(nextInfoList);

                //移除没有最近出现的餐厅
                List<HisRstInfo> tmpList = new ArrayList<>();//最近购买和向后最近出现的餐厅
                if (buyRstTop != null) {
                    tmpList.add(buyRstTop.get(0));
                }
                if (nextRstTop != null) {
                    tmpList.add(nextRstTop.get(0));
                }
                Rules.removeNoRecentBuyInfo(nextInfoList, tmpList);

                //增加历史购买最多的餐厅的
                // Rules.addBuyTopRstInfo(nextInfoList, buyRstTop);
                //增加next_info中出现最多的餐厅的
                // Rules.addBuyTopRstInfo(nextInfoList, nextRstTop);

                dayBuyNum = Rules.getDayBuyNum(nextInfoList);

                //添加当天点击出现最高的餐厅
                //  Rules.addBuyDayTopRstInfo(nextInfoList, curDayRstTop);

                //增加点击最多的
                if (nextRstTop != null && clickRstTop != null) {
                    clickRstTop.add(nextRstTop.get(0));
                }
                // Rules.addClickTopRstInfo(nextInfoList, clickRstTop);

                //删除没有出现且点击率较低的
                if (curDayRstTop != null) {
                    clickRstIdList.add(curDayRstTop.get(0));
                }
                //添加有出现的点击和后面出现的
                // Rules.addClickRstInfo(nextInfoList, clickRstIdList);
                Rules.removeNoClickInfo(nextInfoList, clickRstIdList);


                //随机移除当天购买一个餐厅重复的
                Rules.removeBuyRstRepeatInfo(nextInfoList);

                //移除餐厅在index之后
                Rules.removeAfterIndexInfo(nextInfoList, 50);
                Rules.envUserIdIsNull(nextInfoList);
                Rules.envIsNewOrSelect(nextInfoList);
                Rules.removeClickBuyIsZero(nextInfoList);

                //输出当天的info信息***************
                Rules.outDayMuniteInfoList(nextInfoList);

//                for (HisEcoInfo info : nextInfoList) {
//
//                    count++;
//                    rstId = info.getRestaurant_id();
//                    logId = info.getLog_id();
//                    primaryCategory = info.getHisRstInfo().getPrimary_category();
//                    //3.1.1.如果该用户在next中只出现一次,向前没有历史记录,则认为是新用户
//                    if (nextDaySize == 1 && recordBeforeDays.size() == 0) {
//                        logIdNextInfoMap.get(logId).setIs_click(Constant.S_ZERO);
//                        logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ZERO);
//                        //需细化
//                        if (rstClickRate.get(rstId) == null) {
//                            System.out.println("rstId is null:" + rstId);
//                            continue;
//                        }
//                        if (rstClickRate.get(rstId) > Constant.click_rate_avg) {
//                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                        }
//                        if (rstBuyRate.get(rstId) > Constant.buy_rate_avg && isbuyFlag == 0) {
//                            logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
//                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                            isbuyFlag++;
//                        }
//                        continue;
//                    }
//                    //3.1.2.如果该用户在next中只出现多次,向前没有历史记录
//                    if (nextDaySize > 1 && recordBeforeDays.size() == 0) {
//                        logIdNextInfoMap.get(logId).setIs_click(Constant.S_ZERO);
//                        logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ZERO);
//                        //需细化
//                        if (rstClickRate.get(rstId) == null) {
//                            System.out.println("rstId is null:" + rstId);
//                            continue;
//                        }
//                        boolean nextTopRstFlag = (nextRstTop != null & nextRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id()));
//                        boolean nextDayTopRstFlag = (curDayRstTop != null && curDayRstTop.get(0).equals(info.getRestaurant_id()));
//                        if (rstClickRate.get(rstId) > Constant.rst_click_rate_avg && nextTopRstFlag) {
//                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                        }
//                        //出现比较多的餐厅
//                        if (rstBuyRate.get(rstId) > Constant.rst_buy_rate_avg
//                                && isbuyFlag == 0 && nextTopRstFlag && nextDayTopRstFlag) {
//                            logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
//                            isbuyFlag++;
//                        }
//                        continue;
//                    }
//                    //3.1.3.如果该用户在next中只出现多次,向前也有记录
//                    //找到最近购买最多的餐厅
//                    if (predBuyNum > 0 && isbuyFlag == 0) {
//                        if (clickRstTop != null && buyRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id())) {
//                            logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
//                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                            isbuyFlag++;
//                            predClickNum--;
//                            predBuyNum--;
//                        }
//                    }
//                    if (predClickNum > 0) {
//                        if (clickRstTop != null && clickRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id())) {
//                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                            predClickNum--;
//                        }
//                    }
//
//                    //ResultInfo resultInfo = ruleResultMap.get(info.getLog_id());
////                    if (Constant.S_ONE.equals(resultInfo.getIs_buy())) {
////                        isbuy++;
////                        //if (isbuy > 2) {
////                        System.out.println(info.getLog_id() + " listid=" + info.getList_id()
////                                        + " click =" + resultInfo.getIs_click() + " buy="
////                                        + resultInfo.getIs_buy() + " dayNo:" + info.getHisEcoEnv().getDay_no()
////                                        + " minute=" + info.getHisEcoEnv().getMinutes()
////                        );
////                        // }
////                    }
//                }

                //再对当天进行一次循环,找之前点击和购买的
//                for (HisEcoInfo info : nextInfoList) {
//                    rstId = info.getRestaurant_id();
//                    logId = info.getLog_id();
//                    if (nextDaySize > 1 && recordBeforeDays.size() > 0) {
//                        if (predBuyNum > 0 && isbuyFlag == 0) {
//                            if (buyRstIdList.contains(info.getRestaurant_id())) {
//                                logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
//                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                                isbuyFlag++;
//                                predClickNum--;
//                                predBuyNum--;
//                            }
//                        }
//                        if (predClickNum > 0) {
//                            if (clickRstIdList.contains(info.getRestaurant_id())) {
//                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                                predClickNum--;
//                            }
//                        }
//                    }
//                }
                //向后查找
//                for (HisEcoInfo info : nextInfoList) {
//                    rstId = info.getRestaurant_id();
//                    logId = info.getLog_id();
//                    if (nextDaySize > 1 && recordBeforeDays.size() > 0) {
//                        if (rstClickRate.get(rstId) == null) {
//                            System.out.println("rstId is null:" + rstId);
//                            continue;
//                        }
//                        boolean nextTopRstFlag = (nextRstTop != null & nextRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id()));
//                        boolean nextDayTopRstFlag = (curDayRstTop != null && curDayRstTop.get(0).equals(info.getRestaurant_id()));
//                        //出现比较多的餐厅
//                        if (predBuyNum > 0 && isbuyFlag == 0 && rstBuyRate.get(rstId) > Constant.rst_buy_rate_avg
//                                && nextTopRstFlag && nextDayTopRstFlag) {
//                            if (buyRstIdList.contains(info.getRestaurant_id())) {
//                                logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
//                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                                isbuyFlag++;
//                                predClickNum--;
//                                predBuyNum--;
//                            }
//                        }
//                        if (predClickNum > 0 && rstClickRate.get(rstId) > Constant.rst_click_rate_avg && nextTopRstFlag) {
//                            if (clickRstIdList.contains(info.getRestaurant_id())) {
//                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                                predClickNum--;
//                            }
//                        }
//                    }
//                }

            }
            //   System.out.println(userId);
        }
        System.out.println("count=" + count);
        Rules.outputFileByLogId(logIdNextInfoMap);
    }


    private static void runUserIdRule() {
        //将is_click、 is_buy、is_ray_buy职位0
        Rules.setNextInfoClickBuyIsZero(nextInfoList);
        //生成logId对应的info map
        Map<String, HisEcoInfo> logIdNextInfoMap = getLogIdInfoMap(nextInfoList);

        //Map<String, ResultInfo> newResultMap = new HashMap<>();
        List<HisEcoInfo> nextInfoList;
        int count = 0;
        //0.按userId处理-->dayNo->minutes
        Map<String, Map<Integer, List<HisEcoInfo>>> userIdNextMap = getUserIdDayNextInfoMap();
        for (Map.Entry<String, Map<Integer, List<HisEcoInfo>>> entry : userIdNextMap.entrySet()) {
            String userId = entry.getKey();
            //1.获取next_info和his_info中该用户对应的所有天的info
            Map<Integer, List<HisEcoInfo>> nextDaynoInfoMap = entry.getValue();
            Map<Integer, List<HisEcoInfo>> daynoInfoMap = userIdDayInfoMap.get(userId);
            //按天排序,
            List<Integer> nextDaylist = Rules.getDayList(nextDaynoInfoMap);//预测集中用户出现的天
            List<Integer> hisDaylist = Rules.getDayList(daynoInfoMap);//his集中用户出现的天
            //2.向前查找该用户的行为
            //******************************************************************
            int beforeDay = Constant.TEST_BEGAIN_DAY;//
            List<Integer> recordBeforeDays = new ArrayList<>();//向前出现的天
            List<Integer> beforeBuyDays = new ArrayList<>();//向前出现购买天
            List<Integer> beforeClickDays = new ArrayList<>();//向前出现点击的天
            Map<Integer, List<HisEcoInfo>> recordBeforeDayMap = new HashMap<>();//向前出现天对应的info
            Map<Integer, List<HisEcoInfo>> beforeDayClickMap = new HashMap<>();//向前出现天对应产生点击的info
            Map<Integer, List<HisEcoInfo>> beforeDayBuyMap = new HashMap<>();//向前出现天对应产生购买的info
            List<String> buyRstIdList = new ArrayList<>();
            List<String> clickRstIdList = new ArrayList<>();

            double userClickRate = 0.0;
            double userBuyRate = 0.0;
            int hisTotal = 0;
            int hisClickNum = 0;
            int hisBuyNum = 0;
            List<HisEcoInfo> beforeOneDayList;
            if (daynoInfoMap != null) {
                for (int i = 0; i < hisDaylist.size(); i++) {
                    beforeDay = hisDaylist.get(i);
                    if (beforeDay < Constant.BEFORE_DAY) {
                        continue;
                    }
                    beforeOneDayList = daynoInfoMap.get(beforeDay);
                    if (beforeOneDayList == null) {
                        continue;
                    }
                    //
                    List<HisEcoInfo> beforeBuyInfos = new ArrayList<>();
                    List<HisEcoInfo> beforeClickInfos = new ArrayList<>();
                    for (HisEcoInfo info : beforeOneDayList) {
                        //click
                        if (Constant.S_ONE.equals(info.getIs_click())) {
                            beforeClickInfos.add(info);
                            clickRstIdList.add(info.getRestaurant_id());
                        }
                        //buy
                        if (Constant.S_ONE.equals(info.getIs_buy())) {
                            beforeBuyInfos.add(info);
                            buyRstIdList.add(info.getRestaurant_id());
                        }
                    }
                    if (beforeClickInfos.size() > 0) {
                        beforeDayClickMap.put(beforeDay, beforeClickInfos);
                        beforeClickDays.add(beforeDay);
                        hisClickNum = hisClickNum + beforeClickInfos.size();
                    }
                    if (beforeBuyInfos.size() > 0) {
                        beforeDayBuyMap.put(beforeDay, beforeBuyInfos);
                        beforeBuyDays.add(beforeDay);
                        hisBuyNum = hisBuyNum + beforeBuyInfos.size();
                    }
                    //
                    recordBeforeDays.add(beforeDay);
                    recordBeforeDayMap.put(beforeDay, beforeOneDayList);
                    hisTotal = hisTotal + beforeOneDayList.size();
                    //System.out.println(beforeOneDayList.get(0).getHisEcoEnv().getDay_no());
                }
                if (hisTotal != 0) {
                    userClickRate = formatRate((double) hisClickNum / hisTotal);
                    userBuyRate = formatRate((double) hisBuyNum / hisTotal);
                    //  System.out.println("user click rate" + userClickRate);
                    //  System.out.println("user buy rate" + userBuyRate);
                }
            }
            //向前搜索用户行为结束
            //******************************************************************

            //3.向后查找该用户的行为开始，以下均是按用户每天来处理
            int nextDaySize = nextDaylist.size();//用户在next_info中出现的天数
            List<HisEcoInfo> predBuyList = new ArrayList<>();
            List<HisEcoInfo> predClickList = new ArrayList<>();
            double predClickNum;
            double predBuyNum;
            for (int i = 0; i < nextDaySize; i++) {
                int currentDay = nextDaylist.get(i);
                //3.1.获得当天对应的next_info
                nextInfoList = nextDaynoInfoMap.get(currentDay);
                int nextInfoSize = nextInfoList.size();
                //预测该用户当天的点击数和购买数
                predClickNum = Math.ceil(nextInfoSize * userClickRate);
                predBuyNum = Math.ceil(nextInfoSize * userBuyRate);
                //找到最近购买最多的餐厅
                List<HisRstInfo> buyRstTop = Rules.findRecentBuyTopRst(beforeBuyDays, beforeDayBuyMap);
                List<HisRstInfo> clickRstTop = Rules.findRecentBuyTopRst(beforeBuyDays, beforeDayClickMap);
                List<HisRstInfo> nextRstTop = Rules.findRecentBuyTopRst(nextDaylist, nextDaynoInfoMap);
                String rstId;
                String logId;
                String primaryCategory;
                int isbuyFlag = 0;

                //当天点击出现最高的餐厅
                List<String> curDayRstTop = Rules.getNextDayRstTop(nextInfoList);
                for (HisEcoInfo info : nextInfoList) {

                    count++;
                    rstId = info.getRestaurant_id();
                    logId = info.getLog_id();
                    primaryCategory = info.getHisRstInfo().getPrimary_category();
                    //3.1.1.如果该用户在next中只出现一次,向前没有历史记录,则认为是新用户
                    if (nextDaySize == 1 && recordBeforeDays.size() == 0) {
                        logIdNextInfoMap.get(logId).setIs_click(Constant.S_ZERO);
                        logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ZERO);
                        //需细化
                        if (rstClickRate.get(rstId) == null) {
                            System.out.println("rstId is null:" + rstId);
                            continue;
                        }
                        if (rstClickRate.get(rstId) > Constant.click_rate_avg) {
                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                        }
                        if (rstBuyRate.get(rstId) > Constant.buy_rate_avg && isbuyFlag == 0) {
                            logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                            isbuyFlag++;
                        }
                        continue;
                    }
                    //3.1.2.如果该用户在next中只出现多次,向前没有历史记录
                    if (nextDaySize > 1 && recordBeforeDays.size() == 0) {
                        logIdNextInfoMap.get(logId).setIs_click(Constant.S_ZERO);
                        logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ZERO);
                        //需细化
                        if (rstClickRate.get(rstId) == null) {
                            System.out.println("rstId is null:" + rstId);
                            continue;
                        }
                        boolean nextTopRstFlag = (nextRstTop != null & nextRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id()));
                        boolean nextDayTopRstFlag = (curDayRstTop != null && curDayRstTop.get(0).equals(info.getRestaurant_id()));
                        if (rstClickRate.get(rstId) > Constant.rst_click_rate_avg && nextTopRstFlag) {
                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                        }
                        //出现比较多的餐厅
                        if (rstBuyRate.get(rstId) > Constant.rst_buy_rate_avg
                                && isbuyFlag == 0 && nextTopRstFlag && nextDayTopRstFlag) {
                            logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
                            isbuyFlag++;
                        }
                        continue;
                    }
                    //3.1.3.如果该用户在next中只出现多次,向前也有记录
                    //找到最近购买最多的餐厅
                    if (predBuyNum > 0 && isbuyFlag == 0) {
                        if (clickRstTop != null && buyRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id())) {
                            logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                            isbuyFlag++;
                            predClickNum--;
                            predBuyNum--;
                        }
                    }
                    if (predClickNum > 0) {
                        if (clickRstTop != null && clickRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id())) {
                            logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                            predClickNum--;
                        }
                    }

                    //ResultInfo resultInfo = ruleResultMap.get(info.getLog_id());
//                    if (Constant.S_ONE.equals(resultInfo.getIs_buy())) {
//                        isbuy++;
//                        //if (isbuy > 2) {
//                        System.out.println(info.getLog_id() + " listid=" + info.getList_id()
//                                        + " click =" + resultInfo.getIs_click() + " buy="
//                                        + resultInfo.getIs_buy() + " dayNo:" + info.getHisEcoEnv().getDay_no()
//                                        + " minute=" + info.getHisEcoEnv().getMinutes()
//                        );
//                        // }
//                    }
                }

                //再对当天进行一次循环,找之前点击和购买的
//                for (HisEcoInfo info : nextInfoList) {
//                    rstId = info.getRestaurant_id();
//                    logId = info.getLog_id();
//                    if (nextDaySize > 1 && recordBeforeDays.size() > 0) {
//                        if (predBuyNum > 0 && isbuyFlag == 0) {
//                            if (buyRstIdList.contains(info.getRestaurant_id())) {
//                                logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
//                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                                isbuyFlag++;
//                                predClickNum--;
//                                predBuyNum--;
//                            }
//                        }
//                        if (predClickNum > 0) {
//                            if (clickRstIdList.contains(info.getRestaurant_id())) {
//                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
//                                predClickNum--;
//                            }
//                        }
//                    }
//                }
                //向后查找
                for (HisEcoInfo info : nextInfoList) {
                    rstId = info.getRestaurant_id();
                    logId = info.getLog_id();
                    if (nextDaySize > 1 && recordBeforeDays.size() > 0) {
                        if (rstClickRate.get(rstId) == null) {
                            System.out.println("rstId is null:" + rstId);
                            continue;
                        }
                        boolean nextTopRstFlag = (nextRstTop != null & nextRstTop.get(0).getRestaurant_id().equals(info.getRestaurant_id()));
                        boolean nextDayTopRstFlag = (curDayRstTop != null && curDayRstTop.get(0).equals(info.getRestaurant_id()));
                        //出现比较多的餐厅
                        if (predBuyNum > 0 && isbuyFlag == 0 && rstBuyRate.get(rstId) > Constant.rst_buy_rate_avg
                                && nextTopRstFlag && nextDayTopRstFlag) {
                            if (buyRstIdList.contains(info.getRestaurant_id())) {
                                logIdNextInfoMap.get(logId).setIs_buy(Constant.S_ONE);
                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                                isbuyFlag++;
                                predClickNum--;
                                predBuyNum--;
                            }
                        }
                        if (predClickNum > 0 && rstClickRate.get(rstId) > Constant.rst_click_rate_avg && nextTopRstFlag) {
                            if (clickRstIdList.contains(info.getRestaurant_id())) {
                                logIdNextInfoMap.get(logId).setIs_click(Constant.S_ONE);
                                predClickNum--;
                            }
                        }
                    }
                }

            }
            //   System.out.println(userId);
        }
        System.out.println("count=" + count);
        Rules.outputFileByLogId(logIdNextInfoMap);
        //此处需要循环计算
//        int size = nextInfoList.size();
//        while (size > 0) {
//
//            size = nextInfoList.size();
//        }
        //
    }

    private static List<List<HisEcoInfo>> categoryByMinute(List<HisEcoInfo> nextInfoList) {
        List<List<HisEcoInfo>> minuteInfolist = new ArrayList<>();
        for (HisEcoInfo info : nextInfoList) {

        }
        return minuteInfolist;
    }

    public static void rule(HisEcoInfo info) {
        String userId = info.getHisEcoEnv().getUser_id();
        String listId = info.getList_id();
        //1.获得该用户的当前list列表
        List<HisEcoInfo> lisIdInfoList = listIdInfoMap.get(listId);
        if (lisIdInfoList == null) {
            System.out.println("程序出现异常");
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        init();
        System.out.println("初始化完成！");
        //List<HisEcoInfo> hisEcoInfos = getInfoWithEnvRst(getEnvIdMap(getEnvList()), rstIdMap);
        System.out.println("getInfoWithEnvRst success！");
        //combineInfoEnvRst(getEnvIdMap(getEnvList()), rstIdMap);

        //计算每周用户增长情况
        //calculateUserGrowth(getEnvList());
        //计算餐厅的点击率和购买率
        //calculateRstClickAndBuyRate(getInfoWithRst(rstIdMap));
        //执行规则
        runUserIdRule1();
        long end = System.currentTimeMillis();
        System.out.println("The program running time is " + (end - start));
    }

}
