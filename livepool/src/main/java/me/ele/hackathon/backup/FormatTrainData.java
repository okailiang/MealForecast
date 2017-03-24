package me.ele.hackathon.backup;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.ele.hackathon.data.Parse;
import me.ele.hackathon.object.EcoEnv;
import me.ele.hackathon.object.EcoInfo;
import me.ele.hackathon.object.RstInfo;

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
public class FormatTrainData {



    private static final String SEPERATOR = " ";
    private static final String PATH_PREFIX = "/Users/solomon/Desktop/hackathon/spark_data_20_1/";

    public static void main(String[] args) {
        try {
            opreation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void opreation() throws IOException {
        //about tran
        String tranHisEcoEnvFilePath = PATH_PREFIX+"his_eco_env.txt";
        String tranHisEcoInfFilePath = PATH_PREFIX+"his_eco_info.txt";
        String tranRstInfFilePath = PATH_PREFIX+"rst_info.txt";
        String tranClickOutFile = PATH_PREFIX +"click_train.txt";
        String tranBuyOutFile = PATH_PREFIX +"buy_train.txt";
        buildData(tranHisEcoEnvFilePath, tranHisEcoInfFilePath, tranRstInfFilePath, tranClickOutFile, tranBuyOutFile, true);
    }

    private static void buildData(String envFile,String infoFile,String rstInfFile,String clickOutputFile,String buyOutputFile,Boolean ifhanldeData) throws IOException{
        //处理 env表
        Map<String, EcoEnv> ecoEnvMap = readAndHandleEcoEnv(envFile);
        readAndHandleRstInfo(rstInfFile);

        //处理 info表 将env和info 关联起来
        List<EcoInfo>  ecoInfoList = readAndHandleEcoInfoForTrain(ecoEnvMap, infoFile);
        //随机打乱顺序
//        Collections.shuffle(ecoInfoList);
        outputClickFileForTrain(ecoInfoList, clickOutputFile);
//        outputBuyFileForTrain(ecoInfoList, buyOutputFile);

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
     * 读取餐厅表
     * @return
     * @throws IOException
     */

    private static void readAndHandleRstInfo(String rstInfoFile) throws IOException {

        BufferedReader in =null;
        try {
            in = new BufferedReader(new FileReader(rstInfoFile));

            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String id = list.get(0);
                RstInfo rstInfo = new RstInfo();

                rstInfo.setPrimary_category(Parse.parseRrimaryCategory(list.get(1)));//餐厅分类
                rstInfo.setAgent_fee(Integer.parseInt(list.get(6)));//配送费
                rstInfo.setIs_premium(Integer.parseInt(list.get(7)));//是否品牌馆
                rstInfo.setGood_rating_rate((int) (Double.parseDouble(list.get(9)) * 100));//四星好评占比
                rstInfo.setMin_deliver_amount(Integer.parseInt(list.get(13)));//起送价
                rstInfo.setIs_time_ensure(Integer.parseInt(list.get(15)));//
                rstInfo.setIs_time_ensure_discount(Integer.parseInt(list.get(17)));//
                rstInfo.setIs_eleme_deliver(Integer.parseInt(list.get(18)));//
                rstInfo.setBu_flag(Parse.parseBuFlag(list.get(20)));//
                rstInfo.setService_rating((int) (Double.parseDouble(list.get(22)) * 100));//
                rstInfo.setIs_promotion_info(Integer.parseInt(list.get(28)));//

                Parse.rstInfoMap.put(id, rstInfo);
            }
        }finally {
            if(null != in){
                in.close();
            }
        }
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
                    String rstId = list.get(2);
                    ecoInfo.setRestaurantId(Parse.parseRstId(rstId));
                    if(null != Parse.rstInfoMap.get(rstId)){
                        ecoInfo.setRstInfo(Parse.rstInfoMap.get(rstId));
                    }
                    ecoInfoList.add(ecoInfo);
//                    --- only for click
                    if (isClick == 1) { // 对click扩容
//                        for (int i = 0; i < 20; i++) {
//                            ecoInfoList.add(ecoInfo);
//                            addCount ++;
//                        }
                        if(env.getIsNew()==1){
                            for (int i = 0; i < 49; i++) {
                                ecoInfoList.add(ecoInfo);
                                addCount ++;
                            }
                        }else{
                            for (int i = 0; i <18; i++) {
                                ecoInfoList.add(ecoInfo);
                                addCount ++;
                            }
                        }

                    }
//                    --- only for buy
//                    if (isClick == 1 && isBuy==0 ) { // 对click扩容
//                        for (int i = 0; i < 24; i++) {
//                            ecoInfoList.add(ecoInfo);
//                        }
//                    }
//                    if (isBuy == 1) {    //对 buy 扩容
//                        for (int i = 0; i < 200; i++) {
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
            if (null != in) {
                in.close();
            }
        }
        return ecoInfoList;
    }




    private static void outputClickFileForTrain(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {

//            int tagNum = 1;
//            pw.append("0").append(SEPERATOR);
//            for(int i=0;i<19;i++){
//                pw.append((tagNum) + ":").append(String.valueOf(tagNum++)).append(SEPERATOR);
//            }
//            pw.append("\n");

            ecoInfoList.stream().forEach(ecoInfo -> {
                int num = 1;
                pw.append(String.valueOf(ecoInfo.getIsClick())).append(SEPERATOR);
                Util.buildObject(pw, ecoInfo, num);
                pw.append("\n");

            });
        }
    }


    private static void outputBuyFileForTrain(List<EcoInfo> ecoInfoList,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            ecoInfoList.stream().forEach(ecoInfo -> {
                int num = 1;
                pw.append(String.valueOf(ecoInfo.getIsBuy())).append(SEPERATOR);
                num = Util.buildObject(pw, ecoInfo, num);
                pw.append((num++) + ":").append(String.valueOf(ecoInfo.getIsClick()));
                pw.append("\n");

            });
        }
    }


}
