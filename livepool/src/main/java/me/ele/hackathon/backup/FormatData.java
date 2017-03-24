package me.ele.hackathon.backup;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.ele.hackathon.data.Parse;
import me.ele.hackathon.object.EcoEnv;
import me.ele.hackathon.object.EcoInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * solomon
 * 变量选取
 * 生成libsvm格式文件
 */
public class FormatData {

    /*算法用*/
    public static final String CLICK_TRAN_FIlE = "/Users/solomon/Desktop/hackathon/spark_libsvm/click_train.txt";
    public static final String BUY_TRAN_FILE = "/Users/solomon/Desktop/hackathon/spark_libsvm/buy_train.txt";
    public static final String CLICK_TEST_FIlE = "/Users/solomon/Desktop/hackathon/spark_libsvm/click_test.txt";
    public static final String BUY_TEST_FILE = "/Users/solomon/Desktop/hackathon/spark_libsvm/buy_test.txt";


    private static final String SEPERATOR = " ";

    public static void main(String[] args) throws IOException {
        //about tran
        String tranHisEcoEnvFilePath = "/Users/solomon/Desktop/hackathon/spark_data/train_his_eco_env.txt";
        String tranHisEcoInfFilePath = "/Users/solomon/Desktop/hackathon/spark_data/train_his_eco_info.txt";
        String tranClickOutFile = CLICK_TRAN_FIlE;
        String tranBuyOutFile = BUY_TRAN_FILE;
        buildData(tranHisEcoEnvFilePath,tranHisEcoInfFilePath,tranClickOutFile,tranBuyOutFile,true,"train");
        //about test
        String testHisEcoEnvFilePath = "/Users/solomon/Desktop/hackathon/spark_data/test_his_eco_env.txt";
        String testHisEcoInfFilePath = "/Users/solomon/Desktop/hackathon/spark_data/test_his_eco_info.txt";
        String testClickOutFile = CLICK_TEST_FIlE;
        String testBuyOutFile = BUY_TEST_FILE;
        buildData(testHisEcoEnvFilePath,testHisEcoInfFilePath,testClickOutFile,testBuyOutFile,false,"test");

    }

    private static void buildData(String envFile,String infoFile,String clickOutputFile,String buyOutputFile,Boolean ifhanldeData,String tag) throws IOException{
        //处理 env表
        Map<String, EcoEnv> ecoEnvMap = readAndHandleEcoEnv(envFile);

        //输出click模型libsvm文件
        if("train".equals(tag)){
            //处理 info表 将env和info 关联起来
            List<EcoInfo>  ecoInfoList = readAndHandleEcoInfoForTrain(ecoEnvMap, infoFile);
            //随机打乱顺序
            Collections.shuffle(ecoInfoList);
            outputClickFileForTrain(ecoInfoList,clickOutputFile);
            outputBuyFileForTrain(ecoInfoList, buyOutputFile);
        } else {
            List<EcoInfo> ecoInfoList = readAndHandleEcoInfoForTest(ecoEnvMap,infoFile);
            outputClickFileForTest(ecoInfoList,clickOutputFile);
            outputBuyFileForTest(ecoInfoList, buyOutputFile);
        }
        //输出buy模型libsvm文件
        System.out.println("output to files done.");
    }

    /**
     * 读取env表中变量--曝光表
     * @return
     * @throws IOException
     */
    private static Map<String, EcoEnv> readAndHandleEcoEnv(String ecoEnvFile) throws IOException {
        Map<String/* env:list_id*/, EcoEnv> ecoListMap = Maps.newHashMap();
        BufferedReader in =null;
        try {
            in = new BufferedReader(new FileReader(ecoEnvFile));

            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String id = list.get(0);
                int isSelect = Integer.parseInt(list.get(1));
                int isNew = Integer.parseInt(list.get(5));
                int userId = Parse.parseUserId(list.get(8));
                int networkType = Parse.parseNetworkType(list.get(9));
                int platyform = Parse.parsePlatform(list.get(10));
                int netOp = Parse.parseNetworkOperator(list.get(13));
                int hour = Parse.parseMinutes(Integer.parseInt(list.get(3)));

                EcoEnv ecoEnv = new EcoEnv(id, isSelect, isNew, networkType, platyform, netOp,hour);
                ecoEnv.setUser_id(userId);

                EcoEnv previous = ecoListMap.put(id, ecoEnv);
                if (previous != null) {
                    System.out.println("Found Duplicate, id: " + id);
                }
            }
            //使用完了将userMap数据清除 fortest 还要使用
        }finally {
            if(null != in){
                in.close();
            }
        }
        return ecoListMap;
    }

    /**
     * 读取并处理（扩容）HIS_ECO_INFO 数据 ，并得到最后结果
     * @param ecoEnvMap
     */
    private static List<EcoInfo> readAndHandleEcoInfoForTrain( Map<String, EcoEnv> ecoEnvMap,String ecoInfoFile)throws IOException{
        List<EcoInfo>  ecoInfoList = Lists.newArrayList();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(ecoInfoFile));
            int not_found_list_id_count = 0;
            int clickCount = 0;
            int buyCount = 0;
            int addCount = 0;
            int n= 1;
            String line = in.readLine();    // ignore first line
            //针对训练集 需扩容
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String listId = list.get(1);

                EcoEnv env = ecoEnvMap.get(listId);
                if (env == null) {
                    System.out.println("Oops,env.userId="+env.getUser_id());
//                        System.out.println("Oops, list_id: [" + listId + "] not found! "+(not_found_list_id_count++));
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
                    EcoInfo ecoInfo = new EcoInfo(index, isClick, isBuy, env);
//                    int rstId = parseRstId(list.get(2));
//                    System.out.println("rstId="+rstId);
//                    ecoInfo.setRestaurantId(rstId);

                    //--- only for click
                    if (isClick == 1) { // 对click扩容
                        for (int i = 0; i < 20; i++) {
                            ecoInfoList.add(ecoInfo);
                            addCount ++;
                        }
//                        if(env.getIsNew()==1){
//                            for (int i = 0; i < 1161; i++) {
//                                ecoInfoList.add(ecoInfo);
//                                addCount ++;
//                            }
//                        }else{
//                            for (int i = 0; i <1 && (n++)%3==0; i++) {
//                                ecoInfoList.add(ecoInfo);
//                                addCount ++;
//                            }
//                        }

                    } else {
                        ecoInfoList.add(ecoInfo);
                    }
                    //--- only for buy
//                    if (isClick == 1 && isBuy==0 ) { // 对click扩容
//                        for (int i = 0; i < 24; i++) {
//                            ecoInfoList.add(ecoInfo);
//                        }
//                    }
//                    if (isBuy == 1) {    //对 buy 扩容
//                        for (int i = 0; i < 124; i++) {
//                            ecoInfoList.add(ecoInfo);
//                        }
//                    }
//                    else {
//                        ecoInfoList.add(ecoInfo);
//                    }
                }
            }
            System.out.println("addCount = "+addCount);
            System.out.println("done train.  size: " + ecoInfoList.size() + " click, buy: " + clickCount + "," + buyCount );
//          + "click_rate: " + clickCount / ecoInfoList.size() + ", buy_rate: " + buyCount / ecoInfoList.size());
        }finally {
            if(null != in){
                in.close();
            }
        }
        return ecoInfoList;
    }

    private static List<EcoInfo> readAndHandleEcoInfoForTest( Map<String, EcoEnv> ecoEnvMap,String ecoInfoFile)throws IOException{
        List<EcoInfo>  ecoInfoList = Lists.newArrayList();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(ecoInfoFile));
            int not_found_list_id_count = 0;
            int clickCount = 0;
            int buyCount = 0;
            String line = in.readLine();    // ignore first line

                //针对测试集
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String listId = list.get(1);

                EcoEnv env = ecoEnvMap.get(listId);
                if (env == null) {
                    System.out.println("Oops,env.userId="+env.getUser_id());
//                        System.out.println("Oops, list_id: [" + listId + "] not found! "+(not_found_list_id_count++));
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
                    EcoInfo ecoInfo = new EcoInfo(index, isClick, isBuy, env);
//                    int rstId = parseRstId(list.get(2));
//                    System.out.println("rstId="+rstId);
//                    ecoInfo.setRestaurantId(rstId);
                    ecoInfoList.add(ecoInfo);
                }
            }
            System.out.println("done test. size: " + ecoInfoList.size() + " click, buy: " + clickCount + "," + buyCount );
//          + "click_rate: " + clickCount / ecoInfoList.size() + ", buy_rate: " + buyCount / ecoInfoList.size());
        }finally {
            if(null != in){
                in.close();
            }
        }
        return ecoInfoList;
    }


    private static void outputClickFileForTrain(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            ecoInfoList.stream().forEach(ecoInfo -> {
                int num = 1;
                pw.append(String.valueOf(ecoInfo.getIsClick())).append(SEPERATOR);

                //                pw.append((num++)+":").append(String.valueOf(ecoInfo.getIndex())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsSelect())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsNew())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getHour())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getUser_id())).append(SEPERATOR);
//                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getRestaurantId())).append(SEPERATOR);
                //                pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetworkType())).append(SEPERATOR);
//                pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getPlatform())).append(SEPERATOR);
                //                pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetrowkOperator()));
                pw.append("\n");

            });
        }
    }

    private static void outputClickFileForTest(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            ecoInfoList.stream().forEach(ecoInfo -> {
//                if(1 == ecoInfo.getIsClick()) {
                    int num = 1;
                    pw.append(String.valueOf(ecoInfo.getIsClick())).append(SEPERATOR);

//                                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getIndex())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsSelect())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsNew())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getHour())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getUser_id())).append(SEPERATOR);
//                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getRestaurantId())).append(SEPERATOR);
//                                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetworkType())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getPlatform())).append(SEPERATOR);
                    //                pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetrowkOperator()));
                    pw.append("\n");
//                }
            });
        }
    }

    private static void outputBuyFileForTrain(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            ecoInfoList.stream().forEach(ecoInfo -> {
                int num = 1;
                pw.append(String.valueOf(ecoInfo.getIsBuy())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getIndex())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsSelect())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsNew())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getHour())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getUser_id())).append(SEPERATOR);
//                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getRestaurantId())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetworkType())).append(SEPERATOR);
//                pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getPlatform())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetrowkOperator())).append(SEPERATOR);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getIsClick()));
                pw.append("\n");

            });
        }
    }

    private static void outputBuyFileForTest(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            ecoInfoList.stream().forEach(ecoInfo -> {
//                if(1 == ecoInfo.getIsBuy()) {
                    int num = 1;
                    pw.append(String.valueOf(ecoInfo.getIsBuy())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getIndex())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsSelect())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsNew())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getHour())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getUser_id())).append(SEPERATOR);
//                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getRestaurantId())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetworkType())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getPlatform())).append(SEPERATOR);
//                    pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetrowkOperator())).append(SEPERATOR);
                    pw.append((num++) + ":").append(String.valueOf(ecoInfo.getIsClick()));
                    pw.append("\n");
//                }
            });
        }
    }


}
