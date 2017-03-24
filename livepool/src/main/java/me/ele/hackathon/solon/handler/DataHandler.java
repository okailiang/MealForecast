package me.ele.hackathon.solon.handler;

import com.google.common.base.Splitter;
import me.ele.hackathon.clustering.KMeansCluster;
import me.ele.hackathon.data.Constant;
import me.ele.hackathon.data.LibSvmDataBuilder;
import me.ele.hackathon.data.Parse;
import me.ele.hackathon.data.UserDataFilter;
import me.ele.hackathon.object.*;
import me.ele.hackathon.solon.constant.Strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by solomon on 16/10/20.
 */
public class DataHandler {

    private static String RST_INFO = "";
    private static String LOGIDLIST_PATH = "";

    private static String ECO_INFO = "";
    private static String ECO_ENV = "";
    private static String OUTPATH = "";

    private static final int CLICK = 1;
    private static final int BUY = 2;
    private static final int TRAIN = 1;
    private static final int TEST = 2;
    private static int LABEL_TYPE ;//1.CLICK 2.BUY
    private static int COLLECT_TYPE ;//1.TRAIN 2.TEST

    private static String NULL = "NULL";

    public static List<String> logIdList = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        LOGIDLIST_PATH = Strategy.PATH_PREIX+"log_id.txt";
        buildClickTrain();
        buildClickTest();
        buildBuyTrain();
        buildBuyTest();
//        System.out.println("---clickSize="+clickCount);
//        System.out.println("---buySize="+buyCount);
//        System.out.println("---addCount="+addCount);
    }


    public static void startBuildData() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        LOGIDLIST_PATH = Strategy.PATH_PREIX+"log_id.txt";
        buildClickTrain();
        buildClickTest();
        buildBuyTrain();
        buildBuyTest();
    }

    public static void buildClickTrain() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        LOGIDLIST_PATH = Strategy.PATH_PREIX+"log_id.txt";
        System.out.println("+--------------- buildClickTrain ---------------+");
        LABEL_TYPE = CLICK;
        COLLECT_TYPE = TRAIN;
        ECO_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"click_train.txt";
        build();
        addCount = 0;
        clickCount = 0;
    }

    public static void buildBuyTrain() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        LOGIDLIST_PATH = Strategy.PATH_PREIX+"log_id.txt";
        System.out.println("+--------------- buildBuyTrain ---------------+");
        LABEL_TYPE = BUY;
        COLLECT_TYPE = TRAIN;
        ECO_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"buy_train.txt";
        build();
        addCount = 0;
        buyCount = 0;
    }

    public static void buildClickTest() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        LOGIDLIST_PATH = Strategy.PATH_PREIX+"log_id.txt";
        System.out.println("+--------------- buildClickTest ---------------+");
        LABEL_TYPE = CLICK;
        COLLECT_TYPE = TEST;
        ECO_INFO = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"click_test.txt";
        build();
    }

    public static void buildBuyTest() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        LOGIDLIST_PATH = Strategy.PATH_PREIX+"log_id.txt";
        System.out.println("+--------------- buildBuyTest ---------------+");
        LABEL_TYPE = BUY;
        COLLECT_TYPE = TEST;
        ECO_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"buy_test.txt";
        build();
    }

    public static void build() throws IOException{
        Map<String,EcoEnvNew> envMap = buildEnvMap(ECO_ENV,0);
        Map<String,RstInfoNew> rstMap = buildRstMap(RST_INFO, 0);
        countExpandNum(envMap, rstMap);
        List<String> dataList = joinData(envMap, rstMap);
        outputFile(OUTPATH,dataList);
        outputLogIdFile();
        dataList = null;
    }


    /**
     * 构造ENV数据放入map
     * @param path
     * @param idIndex
     * @return
     * @throws IOException
     */
    public static Map<String,EcoEnvNew> buildEnvMap(String path,int idIndex) throws IOException {
        Map<String,EcoEnvNew> dataMap = new HashMap<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            String title = in.readLine();    // ignore first line
            String row = null;
            while ((row = in.readLine()) != null) {
                List<String> envRow = Splitter.on(Constant.SEPERATOR).splitToList(row);
                String id = envRow.get(idIndex);
                String userId = envRow.get(8);
                //过滤训练集中userId是NULL的数据
                if(Strategy.IF_FILTE_NULL_USER) {
                    if (NULL.equals(userId)) {
                        continue;
                    }
                }
                if(Strategy.IF_FILTE_USER) {
                    //在测试集 但不在 训练集中的用户进行过滤
                    if (UserDataFilter.hisUserSet.contains(userId)
                            && UserDataFilter.nextUserSet.contains(userId)) {
                        dataMap.put(id, Parse.buildEnv(envRow));
                    }
                }else{
                    dataMap.put(id, Parse.buildEnv(envRow));
                }
            }
        }finally {
            if(null !=in){
                in.close();
            }
        }
        return dataMap;
    }

    /**
     * 构造餐厅数据放入map
     * @param path
     * @param idIndex
     * @return
     * @throws IOException
     */
    public static Map<String,RstInfoNew> buildRstMap(String path,int idIndex) throws IOException {
        Map<String,RstInfoNew> dataMap = new HashMap<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            String title = in.readLine();    // ignore first line
            String row = null;
            while ((row = in.readLine()) != null) {
                List<String> rstRow = Splitter.on(Constant.SEPERATOR).splitToList(row);
                String id = rstRow.get(idIndex);
                //选择特定的餐厅
                if(Strategy.IF_FILTER_RST){
                    if(Strategy.rstSet.contains(id)){
                        continue;
                    }
                }
                dataMap.put(id, Parse.buildRst(rstRow));
            }
        }finally {
            if(null !=in){
                in.close();
            }
        }
        return dataMap;
    }

    /**
     * 组合数据
     * @param envMap
     * @param rstMap
     * @return
     * @throws IOException
     */
    static public List<String> countExpandNum(Map<String,EcoEnvNew> envMap, Map<String,RstInfoNew> rstMap) throws IOException {
        List<String> rowList = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(ECO_INFO));
            String title_info = in.readLine();    // first line is title
            String infoRow = null;
            String listId = null;
            String rstId = null;
            EcoEnvNew envRow = null;
            RstInfoNew rstRow = null;
            int number = 0;
            int uselessCount = 0;
            int clickNumber = 0;
            int buyNumber = 0;
            while ((infoRow = in.readLine()) != null) {
                List<String> ecoInfoList = Splitter.on(Constant.SEPERATOR).splitToList(infoRow);
                //本地测试的时候是拿1-18周作为训练集 19-20周作为测试集 两个集合 字段数量和格式一致
                if(Strategy.LOCAL){
                    listId = ecoInfoList.get(Constant.info_list_id_1);
                    rstId = ecoInfoList.get(Constant.info_restaurant_id_2);
                }else{
                    //线上his为训练集 next作为测试集 两个集合 字段数量和格式【不】一致
                    if(COLLECT_TYPE==TRAIN){
                        listId = ecoInfoList.get(Constant.info_list_id_1);
                        rstId = ecoInfoList.get(Constant.info_restaurant_id_2);
                    }
                    if(COLLECT_TYPE==TEST){//next_eco_info格式不一样 不带引号
                        listId = Constant.DOUBLE_QUOTATION_MARKS + ecoInfoList.get(Constant.info_list_id_1)+Constant.DOUBLE_QUOTATION_MARKS;
                        rstId = Constant.DOUBLE_QUOTATION_MARKS + ecoInfoList.get(Constant.info_restaurant_id_2)+Constant.DOUBLE_QUOTATION_MARKS;
                    }
                }
                envRow = envMap.get(listId);
                if(envRow == null){
                    uselessCount++;
                    continue;
                }
                EcoInfoNew info = Parse.buildInfo(ecoInfoList);
                //过滤index大于50的数据
                if(Strategy.IF_FILTE_INDEX) {
                    if (info.getInfo_index_3() > 50) {
                        uselessCount++;
                        continue;
                    }
                }
                if(COLLECT_TYPE==TRAIN){
                    int click = Integer.parseInt(ecoInfoList.get(Constant.info_is_click_4));
                    int buy = Integer.parseInt(ecoInfoList.get(Constant.info_is_buy_5));
                    if(click == 1){
                        clickNumber++;
                    }
                    if(buy == 1){
                        buyNumber ++;
                    }
                }
                number++;
            }
            if(COLLECT_TYPE==TRAIN) {
//                System.out.println("|--------------- click before expand  ="+ clickNumber);
//                System.out.println("|--------------- b u y before expand  ="+ buyNumber);
                Strategy.CLICK_EXPAND_NUM = (int) Math.rint((number - clickNumber) / clickNumber) - 1;
                Strategy.BUY_EXPAND_NUM = (int) Math.rint((number - buyNumber) / buyNumber) - 1;
//                System.out.println("|--------------- CLICK EXPAND          ="+ Strategy.CLICK_EXPAND_NUM);
//                System.out.println("|--------------- BUY   EXPAND          ="+ Strategy.BUY_EXPAND_NUM);
            }

        }finally {
            if(null !=in){
                in.close();
            }
        }

        return rowList;
    }

    /**
     * 组合数据
     * @param envMap
     * @param rstMap
     * @return
     * @throws IOException
     */
    static public List<String> joinData(Map<String,EcoEnvNew> envMap, Map<String,RstInfoNew> rstMap) throws IOException {
        List<String> rowList = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(ECO_INFO));
            String title_info = in.readLine();    // first line is title

            String infoRow = null;
            String listId = null;
            String rstId = null;
            EcoEnvNew envRow = null;
            RstInfoNew rstRow = null;
            int number = 0;
            int uselessCount = 0;
            while ((infoRow = in.readLine()) != null) {
                List<String> ecoInfoList = Splitter.on(Constant.SEPERATOR).splitToList(infoRow);
                //本地测试的时候是拿1-18周作为训练集 19-20周作为测试集 两个集合 字段数量和格式一致
                if(Strategy.LOCAL){
                    listId = ecoInfoList.get(Constant.info_list_id_1);
                    rstId = ecoInfoList.get(Constant.info_restaurant_id_2);
                }else{
                    //线上his为训练集 next作为测试集 两个集合 字段数量和格式【不】一致
                    if(COLLECT_TYPE==TRAIN){
                        listId = ecoInfoList.get(Constant.info_list_id_1);
                        rstId = ecoInfoList.get(Constant.info_restaurant_id_2);
                    }
                    if(COLLECT_TYPE==TEST){//next_eco_info格式不一样 不带引号
                        listId = Constant.DOUBLE_QUOTATION_MARKS + ecoInfoList.get(Constant.info_list_id_1)+Constant.DOUBLE_QUOTATION_MARKS;
                        rstId = Constant.DOUBLE_QUOTATION_MARKS + ecoInfoList.get(Constant.info_restaurant_id_2)+Constant.DOUBLE_QUOTATION_MARKS;
                    }
                }
                envRow = envMap.get(listId);
                if(envRow == null){
                    uselessCount++;
                    continue;
                }
                rstRow = rstMap.get(rstId);
                EcoInfoNew info = Parse.buildInfo(ecoInfoList);
                //过滤index大于50的数据
                if(Strategy.IF_FILTE_INDEX) {
                    if (info.getInfo_index_3() > 50) {
                        uselessCount++;
                        continue;
                    }
                }
                number++;
                info.setEcoEnvNew(envRow);
                info.setRstInfoNew(rstRow);
                String rowlibSvm = buildRowSvm(info, ecoInfoList);
                rowList.add(rowlibSvm);
                //扩容数据
                if(Strategy.IF_EXPAND){
                    expandData(rowList,rowlibSvm,info,ecoInfoList);
                }
            }

            //强制1:1
            if(COLLECT_TYPE==TRAIN) {
                int svmSize = rowList.size();
                if (LABEL_TYPE == CLICK) {
                    int toAdd = (svmSize - clickCount)-clickCount;
                    int size = clickSVMList.size();
                    if(toAdd < size){
                        rowList.addAll(clickSVMList.subList(0,toAdd));
                    }else{
                        int c = (int)Math.floor(toAdd/size);
                        for(int i=0;i<c;i++){
                            rowList.addAll(clickSVMList);
                        }
                        rowList.addAll(clickSVMList.subList(0,toAdd-(c*size)));
                    }
                    clickCount +=toAdd;
                }
                if (LABEL_TYPE == BUY) {
                    int toAdd = (svmSize - buyCount)-buyCount;
                    int size = buySVMList.size();
                    if(toAdd < size){
                        rowList.addAll(buySVMList.subList(0,toAdd));
                    }else{
                        int c = (int)Math.floor(toAdd/size);
                        for(int i=0;i<c;i++){
                            rowList.addAll(buySVMList);
                            buyCount +=size;
                        }
                        rowList.addAll(clickSVMList.subList(0,toAdd-(c*size)));
                    }
                    buyCount +=toAdd;
                }
            }
            if(!Strategy.LOCAL && COLLECT_TYPE==TRAIN){
                rowList.add("0 1:61 2:18 3:21 4:0 5:2 6:500 7:33 8:34 9:0 10:0 11:0 12:0 13:1 14:2 15:2 16:20 17:3 18:0 19:0 20:0 21:0 22:0 23:0 24:0 25:0 26:0 27:0 28:0 29:0 30:2 31:1 32:0 33:0 \n");
                rowList.add("0 1:61 2:18 3:21 4:0 5:2 6:500 7:33 8:34 9:4 10:1 11:0 12:0 13:1 14:1 15:19 16:8 17:4 18:0 19:0 20:0 21:0 22:0 23:0 24:0 25:0 26:0 27:0 28:0 29:0 30:2 31:1 32:0 33:0 \n");
            }
//            System.out.println("|--------------- uselessCount         ="+uselessCount);
            System.out.println("|--------------- total after expand ="+rowList.size());
        }finally {
            if(null !=in){
                in.close();
            }
        }

        return rowList;
    }


    public static int addCount=0;
    public static int clickCount=0;
    public static int buyCount=0;
    public static List<String> clickSVMList = new ArrayList<>() ;
    public static List<String> buySVMList = new ArrayList<>() ;

    public static void expandData(List<String> rowList ,String svmStr,EcoInfoNew info,List<String> row){
        if(COLLECT_TYPE == TRAIN){//训练集才扩容
            if(LABEL_TYPE == CLICK){//click 扩容20倍
                int click = Integer.parseInt(row.get(Constant.info_is_click_4));
                int is_new = info.getEcoEnvNew().getEnv_is_new_5();
                if(click==1){
                    clickSVMList.add(svmStr);
                    for(int i=0;i<Strategy.CLICK_EXPAND_NUM;i++){
                        addCount++;
                        clickCount++;
                        rowList.add(svmStr);
                    }

                }
            }
            if(LABEL_TYPE == BUY){//buy 扩容20倍
                int buy = Integer.parseInt(row.get(Constant.info_is_buy_5));
                int click = Integer.parseInt(row.get(Constant.info_is_click_4));
                if(buy==1){
                    buySVMList.add(svmStr);
                    clickCount ++;
                    buyCount++;
                    for(int i=0;i<Strategy.BUY_EXPAND_NUM;i++){
                        addCount++;
                        buyCount++;
                        rowList.add(svmStr);
                    }
                }

            }
        }
    }

    public static int diff_count =1;
    /**
     * 原始数据转换成向量格式文件
     * @return
     */
    static public String buildRowSvm(EcoInfoNew infoNew,List<String> infoList){
        StringBuffer svmSb = new StringBuffer();
        if(LABEL_TYPE == CLICK){
            if(COLLECT_TYPE== TRAIN){
                //click_train
                svmSb.append(infoList.get(Constant.info_is_click_4)).append(Constant.SVM_DATA_SEPERATOR);
            }else{
                //click_test
                logIdList.add(infoList.get(Constant.info_log_id_0));
                if(Strategy.LOCAL){
                    svmSb.append(infoList.get(Constant.info_is_click_4)).append(Constant.SVM_DATA_SEPERATOR);
                }else{
                    svmSb.append(0).append(Constant.SVM_DATA_SEPERATOR);
                }
            }
        }else{//buy
            if(COLLECT_TYPE==TRAIN){
                //buy_train
                svmSb.append(infoList.get(Constant.info_is_buy_5)).append(Constant.SVM_DATA_SEPERATOR);
//                int click = Integer.parseInt(infoList.get(Constant.info_is_click_4));
//                int buy = Integer.parseInt(infoList.get(Constant.info_is_buy_5));
//                if( buy ==0 && click==1){
//                    System.out.println("*****************click != buy "+diff_count+++" *****************");
//                }
            }else{
                //buy_test
                if(Strategy.LOCAL){
                    svmSb.append(infoList.get(Constant.info_is_buy_5)).append(Constant.SVM_DATA_SEPERATOR);
                }else{
                    svmSb.append(1).append(Constant.SVM_DATA_SEPERATOR);
                }
            }
        }
        int num = LibSvmDataBuilder.buildSvmData(svmSb, infoNew);

        //buy_train 需要带上click作为特征时
        if(Strategy.WITH_CLICK && LABEL_TYPE==BUY && COLLECT_TYPE==TRAIN ){
            svmSb.append(num).append(Constant.COLON).append(infoList.get(Constant.info_is_click_4)).append(Constant.SVM_DATA_SEPERATOR);
        }

        return svmSb.toString();
    }



    /**
     * 集合生成文件
     * @param outPath
     * @param dataList
     * @throws IOException
     */
    public static void outputFile(String outPath,List<String> dataList) throws IOException {
        //打乱训练集的次序
        if(COLLECT_TYPE==TRAIN) {
            if(LABEL_TYPE == CLICK){
                Collections.shuffle(dataList,new Random(Strategy.CLICK_SHUFFLE_SEED));
            }
            if(LABEL_TYPE == BUY){
                Collections.shuffle(dataList,new Random(Strategy.BUY_SHUFFLE_SEED));
            }
//            dataList.add(0,LibSvmDataBuilder.buildTitle());//选取特征头部增加一行作为标记
        }

        System.out.println("|--------------- click after expand ="+clickCount);
        System.out.println("|--------------- b u y after expand ="+buyCount);
//        System.out.println("|--------------- addCount ="+addCount);
//        System.out.println("--- diff_count="+diff_count+"---");
        System.out.println("|--------------- output svm File ");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(outPath)))) {
            dataList.stream().forEach(data -> {
                pw.append(data).append(Constant.LINE_BREAK);
            });
//            System.out.println("|---------------outPath:"+outPath+"dataListSize=" + dataList.size());
        }
    }

    public static void outputLogIdFile() throws IOException {
        System.out.println("|--------------- output log_id File ");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(LOGIDLIST_PATH)))) {
            logIdList.stream().forEach(logId -> {
                pw.append(logId).append(Constant.LINE_BREAK);
            });
        }
        System.out.println("+--------------- ------------ ---------------+");
        System.out.println("");
    }




}
