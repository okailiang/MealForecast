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
import java.util.List;
import java.util.Map;

/**
 * solomon
 * 变量选取
 * 生成libsvm格式文件
 */
public class FormatTestData {



    private static final String PATH_PREFIX = "/Users/solomon/Desktop/hackathon/spark_data_20_1/";

    private static final String SEPERATOR = " ";

    public static void operation() throws IOException {
        //about test
        String testHisEcoEnvFilePath = PATH_PREFIX+"next_eco_env.txt";
        String testHisEcoInfFilePath = PATH_PREFIX+"next_eco_info.txt";
        String tranRstInfFilePath = PATH_PREFIX+"rst_info.txt";
        String testClickOutFile = PATH_PREFIX +"click_test.txt";
        String testBuyOutFile = PATH_PREFIX +"buy_test.txt";
        buildData(testHisEcoEnvFilePath, testHisEcoInfFilePath, testClickOutFile, testBuyOutFile);

    }

    private static void buildData(String envFile,String infoFile,String clickOutputFile,String buyOutputFile) throws IOException{
        //处理 env表
        Map<String, EcoEnv> ecoEnvMap = readAndHandleEcoEnv(envFile);
        //输出click模型libsvm文件
        List<EcoInfo> ecoInfoList = readAndHandleEcoInfoForTest(ecoEnvMap, infoFile);
        outputClickFileForTest(ecoInfoList, clickOutputFile);
//        outputBuyFileForTest(ecoInfoList, buyOutputFile);
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
            int n=0;
            in = new BufferedReader(new FileReader(ecoEnvFile));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String id = list.get(0);
                int isSelect = Integer.parseInt(list.get(1));
                int isNew = Integer.parseInt(list.get(5));
                int userId = Parse.userIdMap.get(list.get(8))==null?(100000000+n++):Parse.userIdMap.get(list.get(8));
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


    private static List<EcoInfo> readAndHandleEcoInfoForTest( Map<String, EcoEnv> ecoEnvMap,String ecoInfoFile)throws IOException{
        List<EcoInfo>  ecoInfoList = Lists.newArrayList();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(ecoInfoFile));
            int not_found_list_id_count = 0;
            int clickCount = 0;
            int buyCount = 0;
            String line = in.readLine();    // ignore first line
            int n=0;
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
                    ecoInfo.setRestaurantId( Parse.rstIdMap.get(list.get(2))==null?10000+n++:Parse.rstIdMap.get(list.get(2)));
                    String rstId = list.get(2);
                    ecoInfo.setRestaurantId(Parse.parseRstId(rstId));
                    if(null!=Parse.rstInfoMap.get(rstId)){
                        ecoInfo.setRstInfo(Parse.rstInfoMap.get(rstId));
                    }
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


    private static void outputClickFileForTest(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            final int[] testCollectionNum = {0};
            ecoInfoList.stream().forEach(ecoInfo -> {
                int num = 1;
//                if(ecoInfo.getEcoEnv().getUser_id()>=100000000) {
                    pw.append(String.valueOf(ecoInfo.getIsClick())).append(SEPERATOR);
                    Util.buildObject(pw, ecoInfo, num);
                    pw.append("\n");
                    testCollectionNum[0]++;
//                }else{
//                    for(int i=0;i<2;i++){
//                        pw.append(String.valueOf(ecoInfo.getIsClick())).append(SEPERATOR);
//                        buildObject(pw,ecoInfo,num);
//                        pw.append("\n");
//                        testCollectionNum[0]++;
//                    }
//                }
            });
            System.out.println("testCollectionNum=" + testCollectionNum[0]);
        }
    }

    private static void outputBuyFileForTest(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            ecoInfoList.stream().forEach(ecoInfo -> {
                int num = 1;
                pw.append(String.valueOf(ecoInfo.getIsBuy())).append(SEPERATOR);
                Util.buildObject(pw, ecoInfo, num);
//                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getIsClick()));
                pw.append("\n");
            });
        }
    }






}
