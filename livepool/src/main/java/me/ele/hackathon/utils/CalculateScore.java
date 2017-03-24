package me.ele.hackathon.utils;


import me.ele.hackathon.data.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据click或者buy的真实结果和预测结果计算分数
 *
 * @author oukailiang
 * @create 2016-10-26 下午4:23
 */

public class CalculateScore {
    private static final String SEPARATOR = " ";
    private static final String SEPARATOR_TAB = "\t";
    private static final String ONE = "1";
    private static final String ZERO = "0";

    /**
     * @param realFilePath
     * @param predFilePath
     * @return
     */
    public static double getScore(String realFilePath, String predFilePath) {
        BufferedReader realBr = null;
        BufferedReader predBr = null;
        String realline;
        String predline;
        //真实和预测相等且为1
        int postiveRightCount = 0;
        int postiveTotalCount = 0;
        //真实和预测相等且为0
        int negativeRightCount = 0;
        int negativeTotalCount = 0;
        try {

            File realFile = new File(realFilePath);
            File predFile = new File(predFilePath);
            realBr = new BufferedReader(new FileReader(realFile));
            predBr = new BufferedReader(new FileReader(predFile));

            while ((realline = realBr.readLine()) != null && (predline = predBr.readLine()) != null) {
                String real = realline.split(SEPARATOR)[0];
                String pred = predline.split(SEPARATOR)[0];
                if (real.equals(pred) && ONE.equals(real)) {
                    postiveRightCount++;
                }
                if (real.equals(pred) && ZERO.equals(real)) {
                    negativeRightCount++;
                }
                if (ONE.equals(real)) {
                    postiveTotalCount++;
                }
                if (ZERO.equals(real)) {
                    negativeTotalCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
                predBr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ((double) postiveRightCount / postiveTotalCount) * ((double) negativeRightCount / negativeTotalCount) * 100;
    }

    /**
     * 将一个文件夹下所有的result汇总为一个结果
     *
     * @param resultFileDir
     * @param infoFilePath
     */
    public static void getScoreByManyResult(String resultFileDir, String infoFilePath) {
        BufferedReader realBr = null;
        BufferedReader predBr = null;
        String realline;
        String predline;
        //真实和预测相等且为1
        int click_postiveRightCount = 0;
        int click_postiveTotalCount = 0;
        //真实和预测相等且为0
        int click_negativeRightCount = 0;
        int click_negativeTotalCount = 0;
        //真实和预测相等且为1
        int buy_postiveRightCount = 0;
        int buy_postiveTotalCount = 0;
        //真实和预测相等且为0
        int buy_negativeRightCount = 0;
        int buy_negativeTotalCount = 0;
        Map<String, List<String>> logIdClickBuyMap = new HashMap<>();
        List<String> clickBuyList;
        int count = 0;
        try {
            File resultFile = new File(resultFileDir);
            //获得目录下的多个文件
            File[] fileList = resultFile.listFiles();
            fileList = filterNoTxt(fileList);
            int fileLen = fileList.length;
            //读取result目录下的所有result.txt放到map中
            for (int i = 0; i < fileLen; i++) {
                predBr = new BufferedReader(new FileReader(fileList[i]));
                while ((predline = predBr.readLine()) != null) {
                    clickBuyList = new ArrayList<>();//list中包含两个元素第一个是click第二个是buy
                    String[] predRowArr = predline.split(SEPARATOR_TAB);

                    clickBuyList.add(predRowArr[1]);
                    clickBuyList.add(predRowArr[2]);
                    logIdClickBuyMap.put(predRowArr[0], clickBuyList);
                }
            }
            //读取真实数据
            realBr = new BufferedReader(new FileReader(infoFilePath));
            realBr.readLine();//跳过第一行
            int resultCount = 0;
            while ((realline = realBr.readLine()) != null) {
                String[] realRowArr = realline.split(SEPARATOR_TAB);
                String logId = realRowArr[0];
                String realClick = realRowArr[Constant.info_is_click_4];
                String realBuy = realRowArr[Constant.info_is_buy_5];
                if ((clickBuyList = logIdClickBuyMap.get(logId)) != null) {
                    String predClick = clickBuyList.get(0);
                    String predBuy = clickBuyList.get(1);
                    //点击
                    if (realClick.equals(predClick) && ONE.equals(realClick)) {
                        click_postiveRightCount++;
                    }
                    if (realClick.equals(predClick) && ZERO.equals(realClick)) {
                        click_negativeRightCount++;
                    }
                    if (ONE.equals(realClick)) {
                        click_postiveTotalCount++;
                    }
                    if (ZERO.equals(realClick)) {
                        click_negativeTotalCount++;
                    }
                    //购买
                    if (realBuy.equals(predBuy) && ONE.equals(realBuy)) {
                        buy_postiveRightCount++;
                    }
                    if (realBuy.equals(predBuy) && ZERO.equals(realBuy)) {
                        buy_negativeRightCount++;
                    }
                    if (ONE.equals(realBuy)) {
                        buy_postiveTotalCount++;
                    }
                    if (ZERO.equals(realBuy)) {
                        buy_negativeTotalCount++;
                    }
                    resultCount++;
                }else{
//                    System.out.println("<*************** logId = "+logId);
                }
            }
            System.out.println("<********** resultCount="+resultCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
                predBr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("click score=" + (((double) click_postiveRightCount / click_postiveTotalCount)
                * ((double) click_negativeRightCount / click_negativeTotalCount) * 100));
        System.out.println("buy score=" + (((double) buy_postiveRightCount / buy_postiveTotalCount)
                * ((double) buy_negativeRightCount / buy_negativeTotalCount) * 100));

    }

    private static File[] filterNoTxt(File[] fileList) {
        List<File> newFileList = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].getName().lastIndexOf(".txt") > -1) {
                newFileList.add(fileList[i]);
            }
        }
        return newFileList.toArray(new File[newFileList.size()]);
    }

    /**
     * 结果文件中包含log_id,is_click,is_buy计算click和buy分数
     *
     * @param realFilePath
     * @param predFilePath
     * @return
     */
    private static void getResultScore(String realFilePath, String predFilePath) {
        BufferedReader realBr = null;
        BufferedReader predBr = null;
        String realline;
        String predline;
        //真实和预测相等且为1
        int postiveRightCount = 0;
        int postiveTotalCount = 0;
        //真实和预测相等且为0
        int negativeRightCount = 0;
        int negativeTotalCount = 0;
        //真实和预测相等且为1
        int buy_postiveRightCount = 0;
        int buy_postiveTotalCount = 0;
        //真实和预测相等且为0
        int buy_negativeRightCount = 0;
        int buy_negativeTotalCount = 0;
        int count = 0;
        try {

            File realFile = new File(realFilePath);
            File predFile = new File(predFilePath);
            realBr = new BufferedReader(new FileReader(realFile));
            predBr = new BufferedReader(new FileReader(predFile));

            while ((realline = realBr.readLine()) != null && (predline = predBr.readLine()) != null) {
                String real = realline.split(SEPARATOR_TAB)[1];
                String pred = predline.split(SEPARATOR_TAB)[1];
                String buy_real = realline.split(SEPARATOR_TAB)[2];
                String buy_pred = predline.split(SEPARATOR_TAB)[2];
//                if (buy_real.equals("1") && pred.equals("0") && buy_pred.equals("1")) {
//                    count++;
//                }
                //点击
                if (real.equals(pred) && ONE.equals(real)) {
                    postiveRightCount++;
                }
                if (real.equals(pred) && ZERO.equals(real)) {
                    negativeRightCount++;
                }
                if (ONE.equals(real)) {
                    postiveTotalCount++;
                }
                if (ZERO.equals(real)) {
                    negativeTotalCount++;
                }
                //购买
                if (buy_real.equals(buy_pred) && ONE.equals(buy_real)) {
                    buy_postiveRightCount++;
                }
                if (buy_real.equals(buy_pred) && ZERO.equals(buy_real)) {
                    buy_negativeRightCount++;
                }
                if (ONE.equals(buy_real)) {
                    buy_postiveTotalCount++;
                }
                if (ZERO.equals(buy_real)) {
                    buy_negativeTotalCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
                predBr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("click score=" + (((double) postiveRightCount / postiveTotalCount)
                * ((double) negativeRightCount / negativeTotalCount) * 100));
        System.out.println("buy score=" + (((double) buy_postiveRightCount / buy_postiveTotalCount)
                * ((double) buy_negativeRightCount / buy_negativeTotalCount) * 100));

    }

    /**
     * 结果的点记录和购买率
     *
     * @param resultFilePath
     * @return
     */
    private static void getResultClickBuyRate(String resultFilePath) {
        BufferedReader br = null;
        String line;
        int total = 0;
        int is_click = 0;
        int is_buy = 0;
        int is_click0_buy1 = 0;
        try {
            //读
            br = new BufferedReader(new FileReader(new File(resultFilePath)));
            br.readLine();
            while ((line = br.readLine()) != null) {
                total++;
                String[] rowArr = line.split("\t");
                if (rowArr[1].equals("1")) {
                    is_click++;
                }
                if (rowArr[2].equals("1")) {
                    is_buy++;
                }
                if (rowArr[1].equals("0") && rowArr[2].equals("1")) {
                    is_click0_buy1++;
                }
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
        System.out.println("总记录数：" + total);
        System.out.println("点击数：" + is_click);
        System.out.println("购买数：" + is_buy);
        System.out.println("点击率=" + (double) is_click / total);
        System.out.println("购买率=" + (double) is_buy / total);
        System.out.println("is_click0_buy1=" + is_click0_buy1);
    }


    public static void main(String[] args) {
        // double score = getScore("/Users/oukailiang/Desktop/score/real_click.txt", "/Users/oukailiang/Desktop/score/pred_click.txt");
        // System.out.println("score=" + score);
//        getResultClickBuyRate("/Users/oukaliang/Desktop/result/result_10.21.txt");
        getResultClickBuyRate("/Users/oukailiang/Desktop/result/rule_result.txt");
        getResultScore("/Users/oukailiang/Desktop/result/2122_real_result.txt"
                , "/Users/oukailiang/Desktop/result/rule_result_replaced.txt");

//        getScoreByManyResult("/Users/solomon/Desktop/hackathon/spark_data_local/result_caculate/",
//                "/Users/solomon/Desktop/hackathon/spark_data_local/data/next_eco_info.txt");
    }
}
