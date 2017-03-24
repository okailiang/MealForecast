package me.ele.hackathon.solon.handler;

import com.google.common.base.Splitter;
import me.ele.hackathon.data.Constant;
import me.ele.hackathon.data.Parse;
import me.ele.hackathon.data.UserDataFilter;
import me.ele.hackathon.object.EcoEnvNew;
import me.ele.hackathon.object.EcoInfoNew;
import me.ele.hackathon.object.RstInfoNew;
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
public class KMeansDataHandler {

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

    public static List<String> logIdList = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        buildClickTrain();
        buildClickTest();
        buildBuyTrain();
        buildBuyTest();
        System.out.println("---clickSize="+clickCount);
        System.out.println("---buySize="+buyCount);
        System.out.println("---addCount="+addCount);
    }


    public static void startBuildData() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        buildClickTrain();
        buildClickTest();
        buildBuyTrain();
        buildBuyTest();
    }

    public static void buildClickTrain() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        System.out.println("+--------------- buildClickTrain ---------------+");
        LABEL_TYPE = CLICK;
        COLLECT_TYPE = TRAIN;
        ECO_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"k_click_train.txt";
        build();
        addCount = 0;
        clickCount = 0;
    }

    public static void buildBuyTrain() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        System.out.println("+--------------- buildBuyTrain ---------------+");
        LABEL_TYPE = BUY;
        COLLECT_TYPE = TRAIN;
        ECO_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"his_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"k_buy_train.txt";
        build();
        addCount = 0;
        buyCount = 0;
    }

    public static void buildClickTest() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        System.out.println("+--------------- buildClickTest ---------------+");
        LABEL_TYPE = CLICK;
        COLLECT_TYPE = TEST;
        ECO_INFO = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"k_click_test.txt";
        build();
    }

    public static void buildBuyTest() throws IOException{
        RST_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"rst_info.txt";
        System.out.println("+--------------- buildBuyTest ---------------+");
        LABEL_TYPE = BUY;
        COLLECT_TYPE = TEST;
        ECO_INFO = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_info.txt";
        ECO_ENV = Strategy.PATH_PREIX+ Strategy.BASE_DATA_SUB_DIR_PATH +"next_eco_env.txt";
        OUTPATH = Strategy.PATH_PREIX +"k_buy_test.txt";
        build();
    }

    public static void build() throws IOException{
        Map<String,EcoEnvNew> envMap = buildEnvMap(ECO_ENV,0);
        Map<String,RstInfoNew> rstMap = buildRstMap(RST_INFO, 0);
        List<String> dataList = joinData(envMap, rstMap);
        outputFile(OUTPATH, dataList);
//        outputLogIdFile();
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
     * 分批生成 聚合后的集合
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
            int number = 1;
            int uselessCount = 0;
            while ((infoRow = in.readLine()) != null) {
//                if(number>10){
//                    break;
//                }
//              System.out.println("***** row "+(number)+" *****");
                number++;
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
                //TODO 将数据过滤 做测试 过滤有问题
                if(Strategy.IF_FILTER_RST){
                    if(Strategy.rstSet.contains(rstId)){
                        uselessCount++;
                        continue;
                    }
                }
                rstRow = rstMap.get(rstId);
                EcoInfoNew info = Parse.buildInfo(ecoInfoList);
                info.setEcoEnvNew(envRow);
                info.setRstInfoNew(rstRow);
                String rowlibSvm = buildRowSvm(info, ecoInfoList);
                rowList.add(rowlibSvm);

                //扩容数据
//                if(Strategy.IF_EXPAND){
//                    expandData(rowList,rowlibSvm,info,ecoInfoList);
//                }

            }
            System.out.println("|--------------- uselessCount ="+uselessCount);
            System.out.println("|--------------- totalCount after add ="+rowList.size());
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

    public static void expandData(List<String> rowList ,String svmStr,EcoInfoNew info,List<String> row){
        if(COLLECT_TYPE == TRAIN){//训练集才扩容
            if(LABEL_TYPE == CLICK){//click 扩容20倍
                int click = Integer.parseInt(row.get(Constant.info_is_click_4));
                int is_new = info.getEcoEnvNew().getEnv_is_new_5();
                if(click==1){
                    clickCount++;
//                    if(is_new==1){
//                        for(int i=0;i<CLICK_EXPAIN_NUM_IS_NEW1;i++){
//                            addCount++;
//                            rowList.add(svmStr);
//                        }
//                    }else{
//                        for(int i=0;i<CLICK_EXPAIN_NUM_IS_NEW0;i++){
//                            addCount++;
//                            rowList.add(svmStr);
//                        }
//                    }
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
        svmSb.append(infoNew.getRstInfoNew().getRst_primary_category_1()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_restaurant_id_0()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_food_name_list_2()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_category_list_3()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_x_4()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_y_5()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_agent_fee_6()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_is_premium_7()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_address_type_8()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_good_rating_rate_9()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_open_month_num_10()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_has_image_11()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_has_food_img_12()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_min_deliver_amount_13()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_time_ensure_spent_14()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_is_time_ensure_15()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_is_ka_16()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_is_time_ensure_discount_17()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_is_eleme_deliver_18()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_radius_43_19()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_bu_flag_20()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_brand_name_21()).append(Constant.SPACE)
                .append(infoNew.getRstInfoNew().getRst_service_rating_22()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_invoice_23()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_online_payment_24()).append(Constant.SPACE)
//                .append(infoNew.getRstInfoNew().getRst_public_degree_25()).append(Constant.SPACE)
                .append(infoNew.getEcoEnvNew().getEnv_is_select_1()).append(Constant.SPACE)
                .append(infoNew.getEcoEnvNew().getEnv_day_no_2()).append(Constant.SPACE)
                .append(infoNew.getEcoEnvNew().getEnv_minutes_3()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_eleme_device_id_4()).append(Constant.SPACE)
                .append(infoNew.getEcoEnvNew().getEnv_is_new_5()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_info_x_6()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_info_y_7()).append(Constant.SPACE)
                .append(infoNew.getEcoEnvNew().getEnv_user_id_8()).append(Constant.SPACE)
                .append(infoNew.getEcoEnvNew().getEnv_network_type_9()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_platform_10()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_brand_11()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_model_12()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_network_operator_13()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_resolution_14()).append(Constant.SPACE)
//                .append(infoNew.getEcoEnvNew().getEnv_channel_15()).append(Constant.SPACE)
                .append(infoNew.getInfo_index_3()).append(Constant.SPACE)
//                .append(infoNew.getInfo_is_click_4()).append(Constant.SPACE)
//                .append(infoNew.getInfo_is_buy_5()).append(Constant.SPACE)
//                .append(infoNew.getInfo_is_raw_buy_6()).append(Constant.SPACE)
//                .append(infoNew.getInfo_order_id_7())
               ;




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

        System.out.println("|--------------- clickSize ="+clickCount);
        System.out.println("|--------------- buySize ="+buyCount);
        System.out.println("|--------------- addCount ="+addCount);
//        System.out.println("--- diff_count="+diff_count+"---");
        System.out.println("|--------------- output svm File ");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(outPath)))) {
            dataList.stream().forEach(data -> {
                pw.append(data).append(Constant.LINE_BREAK);
            });
//            System.out.println("|---------------outPath:"+outPath+"dataListSize=" + dataList.size());
        }
    }



}
