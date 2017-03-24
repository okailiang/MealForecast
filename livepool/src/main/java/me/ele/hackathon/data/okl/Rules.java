package me.ele.hackathon.data.okl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 规则
 *
 * @author oukailiang
 * @create 2016-10-30 下午4:44
 */

public class Rules {
    private static List<HisEcoInfo> resultInfoList = new ArrayList<>();

    public static void main(String[] args) {
        HandleTxtFile.init();
        List<HisEcoInfo> nextInfoList = HandleTxtFile.getNextInfos();
        //将is_click、 is_buy、is_ray_buy职位0
        setNextInfoClickBuyIsZero(nextInfoList);
        //rule(nextInfoList);

    }

    public static void rule(HisEcoInfo info) {
        List<HisEcoInfo> testInfoList = HandleTxtFile.getNextInfos();
        info = HandleTxtFile.getNextInfos().get(0);
        String userId = info.getHisEcoEnv().getUser_id();
        String listId = info.getList_id();
        //1.获得该用户的


        //
        envUserIdIsNull(testInfoList);
        //testInfoList.removeAll(resultInfoList);
        resultInfoList = new ArrayList<>();
        envIsNewOrSelect(testInfoList);
        //testInfoList.removeAll(resultInfoList);
        //resultInfoList = new ArrayList<>();
    }

    public static void envUserIdIsNull(List<HisEcoInfo> infoList) {
        for (HisEcoInfo info : infoList) {
            if ("NULL".equals(info.getHisEcoEnv().getUser_id())) {
                info.setIs_click(Constant.S_ZERO);
                info.setIs_buy(Constant.S_ZERO);
            }
        }
    }

    /**
     * 移除点击或购买为0的餐厅
     *
     * @param infoList
     */

    public static void removeClickBuyIsZero(List<HisEcoInfo> infoList) {
        Map<String, Double> rstClickRateMap = HandleTxtFile.getRstClickRateMap();
        Map<String, Double> rstBuyRateMap = HandleTxtFile.getRstBuyRateMap();

        for (HisEcoInfo info : infoList) {
            Double buyRate = rstBuyRateMap.get(info.getRestaurant_id());
            Double clickRate = rstClickRateMap.get(info.getRestaurant_id());
            if (buyRate == null) {
                buyRate = 0.0;
                clickRate = 0.0;
            }
            if (clickRate == 0.00) {
                info.setIs_click(Constant.S_ZERO);
            }
            if (buyRate == 0.00) {
                info.setIs_buy(Constant.S_ZERO);
            }
        }
    }

    public static void envIsNewOrSelect(List<HisEcoInfo> infoList) {
        Map<String, Double> rstClickRateMap = HandleTxtFile.getRstClickRateMap();
        Map<String, Double> rstBuyRateMap = HandleTxtFile.getRstBuyRateMap();

        for (HisEcoInfo info : infoList) {
            Double buyRate = rstBuyRateMap.get(info.getRestaurant_id());
            Double clickRate = rstClickRateMap.get(info.getRestaurant_id());
            if (buyRate == null) {
                buyRate = 0.0;
                clickRate = 0.0;
            }
            if ((Constant.S_ONE.equals(info.getHisEcoEnv().getIs_new())
                    || Constant.S_ONE.equals(info.getHisEcoEnv().getIs_select())
                    && !(Constant.S_ONE.equals(info.getIs_click()) && Constant.S_ONE.equals(info.getIs_buy())))) {
                if (clickRate < Constant.rst_click_rate_avg) {
                    info.setIs_click(Constant.S_ZERO);
                }
            }
        }
    }

    private static String appendResult(String logId, String isclick, String isBuy) {
        StringBuilder sb = new StringBuilder();
        sb.append(logId).append(Constant.SEPARATOR_TAB).append(isclick)
                .append(Constant.SEPARATOR_TAB).append(isBuy);
        return sb.toString();
    }

    private static void outputFile(List<String> list) {
        String outputFile = Constant.RESULT_FILE;
        File file = new File(outputFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            for (String str : list) {
                bw.write(str + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void outputFileByLogId(Map<String, HisEcoInfo> nextInfoMap) {
        String outputFile = Constant.RULE_RESULT_FILE;
        File file = new File(outputFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        BufferedWriter bw = null;
        FileWriter fw = null;
        StringBuilder sb;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (Map.Entry<String, HisEcoInfo> entry : nextInfoMap.entrySet()) {
                sb = new StringBuilder();
                HisEcoInfo info = entry.getValue();
                sb.append(info.getLog_id()).append(Constant.SEPARATOR_TAB).append(info.getIs_click()).
                        append(Constant.SEPARATOR_TAB).append(info.getIs_buy());
                bw.write(sb.toString() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void setNextInfoClickBuyIsZero(List<HisEcoInfo> nextInfoList) {
        for (HisEcoInfo info : nextInfoList) {
            info.setIs_click(Constant.S_ZERO);
            info.setIs_buy(Constant.S_ZERO);
            info.setIs_raw_buy(Constant.S_ZERO);
        }
    }

    public static List<Integer> getDayList(Map<Integer, List<HisEcoInfo>> daynoInfoMap) {
        if (daynoInfoMap == null) {
            return null;
        }
        Set daySet = daynoInfoMap.keySet();
        List<Integer> dayList = new ArrayList<>();
        Iterator<Integer> it = daySet.iterator();
        while (it.hasNext()) {
            dayList.add(it.next());
        }
        Collections.sort(dayList);
        return dayList;
    }

    public static List<HisRstInfo> findRecentBuyTopRst(List<Integer> beforeBuyDays, Map<Integer, List<HisEcoInfo>> buyDayMap) {
        if (beforeBuyDays == null || beforeBuyDays.size() == 0) {
            return null;
        }
        List<HisRstInfo> rstInfoList = new ArrayList<>();
        Map<String, Integer> rstTimeMap = new HashMap<>();
        List<String> rstIdList = new ArrayList<>();
        int size = beforeBuyDays.size();
        HisRstInfo hisRstInfo = buyDayMap.get(beforeBuyDays.get(size - 1)).get(0).getHisRstInfo();
        if (size == 1) {
            rstInfoList.add(hisRstInfo);
            return rstInfoList;
        }
        int max = 0;
        //按rstId计数
        for (int i = size - 1; i >= 0; i--) {
            List<HisEcoInfo> hisEcoInfos = buyDayMap.get(beforeBuyDays.get(i));
            for (HisEcoInfo info : hisEcoInfos) {
                String rstId = info.getRestaurant_id();
                if (rstTimeMap.get(rstId) == null) {
                    rstTimeMap.put(rstId, 1);
                } else {
                    int count = rstTimeMap.get(rstId) + 1;
                    if (count > max) {
                        max = count;
                        hisRstInfo = info.getHisRstInfo();
                    }
                    rstTimeMap.put(rstId, count);
                }
            }
        }
        rstInfoList.add(hisRstInfo);
        return rstInfoList;
    }

    /**
     * 在next_info中找到该用户当天访问最多的餐厅
     *
     * @param infoList
     * @return
     */
    public static List<String> getNextDayRstTop(List<HisEcoInfo> infoList) {
        if (infoList == null || infoList.size() == 0) {
            return null;
        }
        List<String> dayTopRsts = new ArrayList<>();
        String rstId = infoList.get(0).getRestaurant_id();
        int size = infoList.size();
        if (size == 0) {
            dayTopRsts.add(rstId);
            return dayTopRsts;
        }
        Map<String, Integer> rstTimeMap = new HashMap<>();
        int max = 0;
        for (int i = 0; i < size; i++) {
            String rstIdTemp = infoList.get(i).getRestaurant_id();
            if (rstTimeMap.get(rstIdTemp) == null) {
                rstTimeMap.put(rstIdTemp, 1);
            } else {
                int count = rstTimeMap.get(rstIdTemp) + 1;
                if (count > max) {
                    max = count;
                    rstId = rstIdTemp;
                }
                rstTimeMap.put(rstIdTemp, count);
            }
        }
        dayTopRsts.add(rstId);
        return dayTopRsts;
    }

    public static int getDayBuyNum(List<HisEcoInfo> infoList) {
        int num = 0;
        for (HisEcoInfo info : infoList) {
            if (Constant.S_ONE.equals(info.getIs_buy())) {
                num++;
            }
        }
        return num;
    }

    public static int getDayOnlyBuyNum(List<HisEcoInfo> infoList) {
        int num = 0;
        for (HisEcoInfo info : infoList) {
            if (Constant.S_ZERO.equals(info.getIs_click()) && Constant.S_ONE.equals(info.getIs_buy())) {
                num++;
            }
        }
        return num;
    }

    /**
     * 移除当天只有购买没有点击的数据
     *
     * @param infoList
     */
    public static void removeDayOnlyBuyInfo(List<HisEcoInfo> infoList) {

        for (HisEcoInfo info : infoList) {
            if (Constant.S_ZERO.equals(info.getIs_click()) && Constant.S_ONE.equals(info.getIs_buy())) {
                info.setIs_buy(Constant.S_ZERO);
            }
        }
    }

    /**
     * 移除当天只有购买没有点击且在最近没有购买餐厅的数据
     *
     * @param infoList
     */
    public static void removeDayOnlyBuyInfo(List<HisEcoInfo> infoList, List<String> recentBuyRstList) {

        for (HisEcoInfo info : infoList) {
            if (Constant.S_ZERO.equals(info.getIs_click()) && Constant.S_ONE.equals(info.getIs_buy())
                    && !recentBuyRstList.contains(info.getRestaurant_id())) {
                info.setIs_buy(Constant.S_ZERO);
            }
        }
    }

    /**
     * 移除没有过点击的
     *
     * @param infoList
     */
    public static void removeNoClickInfo(List<HisEcoInfo> infoList, List<String> clickRstList) {
        Map<String, Double> rstClickRateMap = HandleTxtFile.getRstClickRateMap();
        for (HisEcoInfo info : infoList) {
            Double clickRate = rstClickRateMap.get(info.getRestaurant_id());
            if (clickRate == null) {
                clickRate = 0.00;
            }
            String catery = info.getHisRstInfo().getPrimary_category();
            boolean flag = ("\"麻辣烫\"".equals(catery) || "\"盖浇饭\"".equals(catery));
            if (!clickRstList.contains(info.getRestaurant_id()) && clickRate < (Constant.rst_click_rate_avg + 0.01)) {
                info.setIs_click(Constant.S_ZERO);
            }
        }
    }

    /**
     * 添加有点击过和后面有出现的
     *
     * @param infoList
     */
    public static void addClickRstInfo(List<HisEcoInfo> infoList, List<String> clickRstList) {
        Map<String, Double> rstClickRateMap = HandleTxtFile.getRstClickRateMap();
        for (HisEcoInfo info : infoList) {
            Double clickRate = rstClickRateMap.get(info.getRestaurant_id());
            if (clickRate == null) {
                clickRate = 0.00;
            }
            int index = Integer.parseInt(info.getSort_index());
            String catery = info.getHisRstInfo().getPrimary_category();
            boolean flag = ("\"麻辣烫\"".equals(catery) || "\"盖浇饭\"".equals(catery));
            if (flag && (clickRstList.contains(info.getRestaurant_id()) || (clickRate > Constant.rst_click_rate_avg + 0.020))) {
                info.setIs_click(Constant.S_ONE);
            }
        }
    }

    /**
     * 移除最近没有购买的数据
     *
     * @param infoList
     */
    public static void removeRecentNoBuyInfo(List<HisEcoInfo> infoList, List<String> recentBuyRstList) {

        for (HisEcoInfo info : infoList) {
            if (!recentBuyRstList.contains(info.getRestaurant_id())) {
                info.setIs_buy(Constant.S_ZERO);
            }
        }
    }

    /**
     * 随机给购买最高的餐厅添加buy
     *
     * @param infoList
     */
    public static void addBuyTopRstInfo(List<HisEcoInfo> infoList, List<HisRstInfo> buyTopRstList) {
        if (buyTopRstList == null || buyTopRstList.size() == 0) {
            return;
        }
        Map<String, Double> rstBuyRateMap = HandleTxtFile.getRstBuyRateMap();
        for (HisRstInfo buyTopRst : buyTopRstList) {
            for (HisEcoInfo info : infoList) {
                Double buyRate = rstBuyRateMap.get(info.getRestaurant_id());
                if (buyRate == null) {
                    buyRate = 0.0;
                }

                if (buyTopRst.getRestaurant_id().equals(info.getRestaurant_id())
                        || (info.getHisRstInfo().equals(buyTopRst.getPrimary_category()) && buyRate > Constant.rst_buy_rate_avg)) {
                    info.setIs_buy(Constant.S_ONE);
                    info.setIs_click(Constant.S_ONE);
                }
            }
        }
    }

    /**
     * 随机给点击最高的餐厅添加buy
     *
     * @param infoList
     */
    public static void addClickTopRstInfo(List<HisEcoInfo> infoList, List<HisRstInfo> buyTopRstList) {
        if (buyTopRstList == null || buyTopRstList.size() == 0) {
            return;
        }
        for (HisRstInfo buyTopRst : buyTopRstList) {
            for (HisEcoInfo info : infoList) {
                if (buyTopRst.getRestaurant_id().equals(info.getRestaurant_id())) {
                    info.setIs_click(Constant.S_ONE);
                }
            }
        }
    }

    /**
     * 随机给当天点击最高的餐厅添加buy
     *
     * @param infoList
     */
    public static void addBuyDayTopRstInfo(List<HisEcoInfo> infoList, List<String> dayaTopRstList) {
        if (dayaTopRstList == null || dayaTopRstList.size() == 0) {
            return;
        }
        for (String buyTopRst : dayaTopRstList) {
            for (HisEcoInfo info : infoList) {
                if (buyTopRst.equals(info.getRestaurant_id())) {
                    info.setIs_buy(Constant.S_ONE);
                    //info.setIs_click(Constant.S_ONE);
                }
            }
        }
    }

    /**
     * 随机移除当天购买一个餐厅重复的
     *
     * @param infoList
     */
    public static void removeBuyRstRepeatInfo(List<HisEcoInfo> infoList) {
        List<HisEcoInfo> buyRstRepeatInfos = findBuyRstRepeatInfos(infoList);
        if (buyRstRepeatInfos.size() == 0) {
            return;
        }
        for (HisEcoInfo repeatInfo : buyRstRepeatInfos) {
            for (HisEcoInfo info : infoList) {
                if (repeatInfo.getLog_id().equals(info.getLog_id())) {
                    info.setIs_buy(Constant.S_ZERO);
                    info.setIs_click(Constant.S_ZERO);
                    continue;
                }
            }
        }
    }

    /**
     * 移除sort_index之后的
     *
     * @param infoList
     */
    public static void removeAfterIndexInfo(List<HisEcoInfo> infoList, int index) {
        if (index < 0) {
            return;
        }
        Map<String, Double> rstClickRateMap = HandleTxtFile.getRstClickRateMap();
        Map<String, Double> rstBuyRateMap = HandleTxtFile.getRstBuyRateMap();
        for (HisEcoInfo info : infoList) {
            int sortIndex = Integer.parseInt(info.getSort_index());
            if (sortIndex > index) {
                Double buyRate = rstBuyRateMap.get(info.getRestaurant_id());
                Double clickRate = rstClickRateMap.get(info.getRestaurant_id());
                if (buyRate == null) {
                    buyRate = 0.0;
                    clickRate = 0.0;
                }
                if (buyRate < Constant.rst_buy_rate_avg || clickRate < Constant.rst_click_rate_avg) {
                    info.setIs_buy(Constant.S_ZERO);
                    info.setIs_click(Constant.S_ZERO);
                }
            }
        }

    }

    /**
     * 移除不是最近购买的餐厅
     *
     * @param infoList
     */
    public static void removeNoRecentBuyInfo(List<HisEcoInfo> infoList, List<HisRstInfo> rencentBuyInfoList) {
        if (rencentBuyInfoList == null || rencentBuyInfoList.size() == 0) {
            return;
        }
        Map<String, Double> rstClickRateMap = HandleTxtFile.getRstClickRateMap();
        Map<String, Double> rstBuyRateMap = HandleTxtFile.getRstBuyRateMap();
        for (HisEcoInfo info : infoList) {
            Double buyRate = rstBuyRateMap.get(info.getRestaurant_id());
            Double clickRate = rstClickRateMap.get(info.getRestaurant_id());
            if (buyRate == null) {
                buyRate = 0.0;
                clickRate = 0.0;
            }
            boolean flag = false;
            for (HisRstInfo recentBuyInfo : rencentBuyInfoList) {
                if (recentBuyInfo.getRestaurant_id().equals(info.getRestaurant_id())) {
                    flag = true;
                }
            }
            if (!flag) {
                if (buyRate < Constant.rst_buy_rate_avg) {
                    info.setIs_buy(Constant.S_ZERO);
                }
                if (clickRate < Constant.rst_click_rate_avg + 0.002) {
                    info.setIs_click(Constant.S_ZERO);
                }
            }
//            if (buyRate < Constant.rst_buy_rate_avg || clickRate < Constant.rst_click_rate_avg) {
//                info.setIs_buy(Constant.S_ZERO);
//                info.setIs_click(Constant.S_ZERO);
//            }
        }
    }

    private static List<HisEcoInfo> findBuyRstRepeatInfos(List<HisEcoInfo> infoList) {
        List<HisEcoInfo> buyRstRepeatInfo = new ArrayList<>();
        Map<String, HisEcoInfo> buyInfoMap = new HashMap<>();
        int index = 0;
        int min = 0;
        HisEcoInfo repeatInfo;
        for (HisEcoInfo info : infoList) {
            String rstId = info.getRestaurant_id();
            if ((repeatInfo = buyInfoMap.get(rstId)) == null) {
                buyInfoMap.put(rstId, info);
            } else {
                //找到index最小的
                index = Integer.parseInt(info.getSort_index());
                min = Integer.parseInt(repeatInfo.getSort_index());
                if (index < min) {
                    buyRstRepeatInfo.add(repeatInfo);
                    buyInfoMap.put(rstId, info);
                    min = index;
                } else {
                    buyRstRepeatInfo.add(info);
                }
            }
        }

        return buyRstRepeatInfo;
    }

    /**
     * 获得产生购买的餐厅
     *
     * @param infoList
     */
    public static List<HisEcoInfo> getBuyNextInfo(List<HisEcoInfo> infoList) {
        List<HisEcoInfo> nextInfoList = new ArrayList<>();
        for (HisEcoInfo info : infoList) {
            if (Constant.S_ONE.equals(info.getIs_buy())) {
                nextInfoList.add(info);
            }
        }
        return nextInfoList;
    }

    public static void outDayMuniteInfoList(List<HisEcoInfo> infoList) {

        for (HisEcoInfo info : infoList) {
            System.out.println("listId:" + info.getList_id() + " rstId:" + info.getRestaurant_id() +
                    " index:" + info.getSort_index() + " isclick:" + info.getIs_click()
                    + " isBuy:" + info.getIs_buy() + " day:" + info.getHisEcoEnv().getDay_no() + " munites:" + info.getHisEcoEnv().getMinutes());
        }
        System.out.println("=======================================" + infoList.get(0).getHisEcoEnv().getUser_id());
    }

    public static boolean isBuyRst(List<Integer> beforeBuyDays, Map<Integer, List<HisEcoInfo>> buyDayMap) {
        return true;
    }

    public static List<HisEcoInfo> getResultInfoList() {
        return resultInfoList;
    }
}
