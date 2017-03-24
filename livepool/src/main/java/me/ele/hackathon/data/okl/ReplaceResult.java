package me.ele.hackathon.data.okl;

import java.io.*;
import java.util.*;

/**
 * 用一个结果替换另外一个结果
 *
 * @author oukailiang
 * @create 2016-10-31 下午12:21
 */

public class ReplaceResult {

    /**
     * 用目的文件替换源文件中的部分值
     *
     * @param srcFilePath
     * @param outReplacedFilePath
     * @param ruleResultMap
     */
    public static void replace(String srcFilePath, String outReplacedFilePath
            , Map<String, ResultInfo> ruleResultMap) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(srcFilePath));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            ResultInfo row;
            StringBuilder sb;
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                if ((row = ruleResultMap.get(rowArr[0])) == null) {
                    bw.write(line + "\n");
                } else if ("1".equals(rowArr[1]) && "1".equals(rowArr[2])) {
                    bw.write(line + "\n");
                } else if ("0".equals(rowArr[1]) && "1".equals(rowArr[2])) {
                    sb.append(row.getLog_id()).append(Constant.SEPARATOR_TAB).append("0")
                            .append(Constant.SEPARATOR_TAB).append("0");
                    bw.write(sb.toString() + "\n");
                } else {
                    sb.append(row.getLog_id()).append(Constant.SEPARATOR_TAB).append(row.getIs_click())
                            .append(Constant.SEPARATOR_TAB).append(row.getIs_buy());
                    bw.write(sb.toString() + "\n");
                }
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将结果中click=0&&buy=1替换为click=1&&buy=1
     *
     * @param srcFilePath
     * @param outReplacedFilePath
     */
    public static void replaceClick0Buy1(String srcFilePath, String outReplacedFilePath) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(srcFilePath));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            StringBuilder sb;
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                if ("0".equals(rowArr[1]) && "1".equals(rowArr[2])) {
                    rowArr[1] = "1";
                }
                sb.append(rowArr[0]).append(Constant.SEPARATOR_TAB).append(rowArr[1])
                        .append(Constant.SEPARATOR_TAB).append(rowArr[2]);
                bw.write(sb.toString() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将rule_result的文件log_id顺序调整为源文件的顺序
     *
     * @param srcFilePath
     * @param outReplacedFilePath
     * @param ruleResultMap
     */
    public static void ruleResultFollow(String srcFilePath, String outReplacedFilePath
            , Map<String, ResultInfo> ruleResultMap) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(srcFilePath));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            ResultInfo row;
            StringBuilder sb;
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                if ((row = ruleResultMap.get(rowArr[0])) != null) {
                    sb.append(row.getLog_id()).append(Constant.SEPARATOR_TAB).append(row.getIs_click())
                            .append(Constant.SEPARATOR_TAB).append(row.getIs_buy());
                    bw.write(sb.toString() + "\n");
                }
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给result添加冒号
     *
     * @param srcFilePath
     */
    public static void addResultSemicolon(String srcFilePath, String outReplacedFilePath) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(srcFilePath));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            StringBuilder sb;
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                sb.append("\"" + rowArr[0] + "\"").append(Constant.SEPARATOR_TAB).append(rowArr[1])
                        .append(Constant.SEPARATOR_TAB).append(rowArr[2]);
                bw.write(sb.toString() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给next_info添加冒号
     *
     * @param srcFilePath
     */
    public static void addNextInfoSemicolon(String srcFilePath, String outReplacedFilePath) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(srcFilePath));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            StringBuilder sb;
            bw.write(br.readLine() + "\n");
            bw.flush();
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                sb.append("\"" + rowArr[0] + "\"").append(Constant.SEPARATOR_TAB).append("\"" + rowArr[1] + "\"")
                        .append(Constant.SEPARATOR_TAB).append("\"" + rowArr[2] + "\"")
                        .append(Constant.SEPARATOR_TAB).append(rowArr[3]).append(Constant.SEPARATOR_TAB)
                        .append(0).append(Constant.SEPARATOR_TAB).append(0).append(Constant.SEPARATOR_TAB).append(0)
                        .append(Constant.SEPARATOR_TAB).append("NULL");
                bw.write(sb.toString() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用预测的结果文件中的click buy 替换next_info中的click和buy
     *
     * @param nextInfoFile
     * @param ruleResultMap
     */
    public static void replacePredClickBuyToNextInfo(String nextInfoFile
            , Map<String, ResultInfo> ruleResultMap, String outReplacedFilePath) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(nextInfoFile));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            ResultInfo resultInfo;
            StringBuilder sb;
            bw.write(br.readLine() + "\n");
            bw.flush();
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                String log_id = rowArr[0];
                resultInfo = ruleResultMap.get(log_id);
                if (resultInfo == null) {
                    System.out.println("数据出错！");
                }
                sb.append(log_id).append(Constant.SEPARATOR_TAB).append(rowArr[1]).append(Constant.SEPARATOR_TAB).
                        append(rowArr[2]).append(Constant.SEPARATOR_TAB).append(rowArr[3]).append(Constant.SEPARATOR_TAB)
                        .append(resultInfo.getIs_click()).append(Constant.SEPARATOR_TAB).append(resultInfo.getIs_buy())
                        .append(Constant.SEPARATOR_TAB).append(rowArr[6]).append(Constant.SEPARATOR_TAB).append(rowArr[7]);
                bw.write(sb.toString() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将真实的info文件中click buy转为result（log_id is_ckick is_buy）
     *
     * @param srcFilePath
     * @param outReplacedFilePath
     */
    public static void getRealResultFromInfo(String srcFilePath, String outReplacedFilePath) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(new File(srcFilePath));
            br = new BufferedReader(fr);
            fw = new FileWriter(new File(outReplacedFilePath));
            bw = new BufferedWriter(fw);
            String line;
            br.readLine();
            StringBuilder sb;
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                sb.append(rowArr[0]).append(Constant.SEPARATOR_TAB).append(rowArr[4])
                        .append(Constant.SEPARATOR_TAB).append(rowArr[5]);
                bw.write(sb.toString() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取人工规则产生的结果
     *
     * @param resultFilePath
     * @return
     */
    public static Map<String, ResultInfo> readRuleResult(String resultFilePath) {
        FileReader fr = null;
        BufferedReader br = null;
        Map<String, ResultInfo> resultMap = new HashMap<>();
        ResultInfo resultInfo;
        try {
            fr = new FileReader(new File(resultFilePath));
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                resultInfo = new ResultInfo();
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                resultInfo.setLog_id(rowArr[0]);
                resultInfo.setIs_click(rowArr[1]);
                resultInfo.setIs_buy(rowArr[2]);
                resultMap.put(rowArr[0], resultInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    private static void rule(Map<String, ResultInfo> ruleResultMap) {
        Map<String, ResultInfo> newResultMap = new HashMap<>();
        int size = ruleResultMap.size();
        List<HisEcoInfo> infoList;
        //
        Map<String, Map<Integer, List<HisEcoInfo>>> userIdMap = HandleTxtFile.getUserIdDayNextInfoMap();
        int count = 0;
        int two_buy_count = 0;
        for (Map.Entry<String, Map<Integer, List<HisEcoInfo>>> entry : userIdMap.entrySet()) {
            String userId = entry.getKey();
            Map<Integer, List<HisEcoInfo>> daynoMap = entry.getValue();
            //
            Set daySet = daynoMap.keySet();
            List<Integer> daylist = getDayList(daySet);
            int daySize = daylist.size();
            //
            for (int i = 0; i < daySize; i++) {
                //当天的
                infoList = daynoMap.get(daylist.get(i));
                int isbuy = 0;
                for (HisEcoInfo info : infoList) {
                    count++;
                    ResultInfo resultInfo = ruleResultMap.get(info.getLog_id());
                    if (Constant.S_ONE.equals(resultInfo.getIs_buy())) {
                        isbuy++;
                        //if (isbuy > 2) {
                        System.out.println(info.getLog_id() + " listid=" + info.getList_id()
                                        + " click =" + resultInfo.getIs_click() + " buy="
                                        + resultInfo.getIs_buy() + " dayNo:" + info.getHisEcoEnv().getDay_no()
                                        + " minute=" + info.getHisEcoEnv().getMinutes()
                        );
                        if (isbuy > 2) {
                            two_buy_count++;
                        }
                        // }
                    }
                }

            }
            System.out.println(userId);
        }
        System.out.println("count=" + count);
        System.out.println("before_two_buy_count=" + two_buy_count);
//        while (size > 0) {
//
//            size = ruleResultMap.size();
//        }
    }

    private static boolean isOverTwoBuy(List<HisEcoInfo> infoList, Map<String, ResultInfo> ruleResultMap) {

        return true;
    }

    private static List<Integer> getDayList(Set daySet) {
        List<Integer> dayList = new ArrayList<>();
        Iterator<Integer> it = daySet.iterator();
        while (it.hasNext()) {
            dayList.add(it.next());
        }
        Collections.sort(dayList);
        return dayList;
    }


    public static void main(String[] args) {
        //  HandleTxtFile.init();
        String FILEDIR = "/Users/oukailiang/Desktop/result/";
        String replacedFile = "/Users/oukailiang/Desktop/result/result_replaced.txt";
        String replacedToNextInfoFile = "/Users/oukailiang/Desktop/result/next_info_replaced.txt";
        String realResultFile = "/Users/oukailiang/Desktop/result/2122_real_result.txt";

        String realInfo = "/Users/oukailiang/Downloads/hackathon/E_Data/new_data/2122_next_eco_info_old_real.txt";
        //Map<String, ResultInfo> ruleResultMap = readRuleResult(realFile);
        // rule(ruleResultMap);

        //1.将真实的info文件中click buy转为result（log_id is_ckick is_buy）
//            getRealResultFromInfo(realInfo, realResultFile);

        //2.替换result时用
//        Map<String, ResultInfo> ruleResultMap = readRuleResult("/Users/oukailiang/Downloads/hackathon/E_Data/new_data/result.txt");
//        replace("/Users/oukailiang/Desktop/result/1920_pred_result.txt", replacedFile, ruleResultMap);

        //3. 用预测的结果文件中的click buy 替换next_info中的click和buy
//        Map<String, ResultInfo> ruleResultMap = readRuleResult(FILEDIR + "result_10.25_semicolon.txt");
//        replacePredClickBuyToNextInfo(realInfo, ruleResultMap, replacedToNextInfoFile);

        //4.将rule_result的文件log_id顺序调整为源文件的顺序
        Map<String, ResultInfo> ruleResultMap = readRuleResult("/Users/oukailiang/Desktop/result/rule_result.txt");
        ruleResultFollow(realResultFile, "/Users/oukailiang/Desktop/result/rule_result_replaced.txt", ruleResultMap);

//        //5.将结果中click=0&&buy=1替换为click=1&&buy=1
//        replaceClick0Buy1("/Users/oukailiang/Desktop/result/rule_result_replaced.txt"
//                , "/Users/oukailiang/Desktop/result/rule_result.txt");

        //6.给result中logId添加分号
        //  addResultSemicolon(FILEDIR + "result_10.25.txt", FILEDIR + "result_10.25_semicolon.txt");

        //7.
//        addNextInfoSemicolon("/Users/oukailiang/Downloads/hackathon/E_Data/next_eco_info.txt", "/Users/oukailiang/Downloads/hackathon/E_Data/next_eco_info_semicolon.txt");
    }
}
