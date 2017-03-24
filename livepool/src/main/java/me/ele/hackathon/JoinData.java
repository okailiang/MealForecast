package me.ele.hackathon;

import com.google.common.base.Splitter;
import me.ele.hackathon.data.Constant;
import me.ele.hackathon.data.LibSvmDataBuilder;

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
public class JoinData {

    public static String PATH_PREIX =  "/Users/solomon/Desktop/hackathon/spark_data_20_2/";

    public static final String HIS_ORDER_INFO =PATH_PREIX+"his_order_info.txt";
    public static final String RST_INFO = PATH_PREIX+"rst_info.txt";

    //需设置的变量
    public static String ECO_INFO = PATH_PREIX;
    public static String ECO_ENV = PATH_PREIX;
    public static String OUTPATH = PATH_PREIX;

    public static final int CLICK = 1;
    public static final int BUY = 2;
    public static final int TRAIN = 1;
    public static final int TEST = 2;
    public static int LABEL_TYPE ;//1.CLICK 2.BUY
    public static int COLLECT_TYPE ;//1.TRAIN 2.TEST

    public static boolean LOCAL = true;//本地测试会将前20周数据拆分为训练集和测试集 向量文件生成会略有不同

    public static void main(String[] args) throws IOException{
        buildClickTrain();
//        buildBuyTrain();
//        buildClickTest();
//        buildBuyTest();
    }

    public static void buildClickTrain() throws IOException{
        System.out.println(" --------------- buildClickTrain ---------------");
        LABEL_TYPE = CLICK;
        COLLECT_TYPE = TRAIN;
        ECO_INFO = PATH_PREIX+"his_eco_info.txt";
        ECO_ENV = PATH_PREIX+"his_eco_env.txt";
        OUTPATH = PATH_PREIX+"click_train.txt";
        build();
    }

    public static void buildBuyTrain() throws IOException{
        System.out.println(" --------------- buildBuyTrain ---------------");
        LABEL_TYPE = BUY;
        COLLECT_TYPE = TRAIN;
        ECO_INFO = PATH_PREIX+"his_eco_info.txt";
        ECO_ENV = PATH_PREIX+"his_eco_env.txt";
        OUTPATH = PATH_PREIX+"buy_train.txt";
        build();
    }

    public static void buildClickTest() throws IOException{
        System.out.println(" --------------- buildClickTest ---------------");
        LABEL_TYPE = CLICK;
        COLLECT_TYPE = TEST;
        ECO_INFO = PATH_PREIX+"next_eco_info.txt";
        ECO_ENV = PATH_PREIX+"next_eco_env.txt";
        OUTPATH = PATH_PREIX+"click_test.txt";
        build();
    }

    public static void buildBuyTest() throws IOException{
        System.out.println(" --------------- buildBuyTest ---------------");
        LABEL_TYPE = BUY;
        COLLECT_TYPE = TEST;
        ECO_INFO = PATH_PREIX+"next_eco_info.txt";
        ECO_ENV = PATH_PREIX+"next_eco_env.txt";
        OUTPATH = PATH_PREIX+"buy_test.txt";
        build();
    }

    public static void build() throws IOException{
        Map<String,String> envMap = parseToMap(ECO_ENV,0);
        Map<String,String> rstMap = parseToMap(RST_INFO,0);
        List<String> dataList = joinData(envMap, rstMap);
        outputFile(OUTPATH,dataList);
        dataList = null;

    }


    /**
     * his_eco_env、rst_info 数据放入map
     * @param path
     * @param idIndex
     * @return
     * @throws IOException
     */
    public static Map<String,String> parseToMap(String path,int idIndex) throws IOException {
//        System.out.println("--------------- parseToMap -------------");
        Map<String,String> dataMap = new HashMap<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            String title = in.readLine();    // ignore first line
            dataMap.put(path,title);
            String row = null;
            while ((row = in.readLine()) != null) {
                List<String> ecoInfoList = Splitter.on(Constant.SEPERATOR).splitToList(row);
                String id = ecoInfoList.get(idIndex);
                dataMap.put(id, row);
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
    static public List<String> joinData(Map<String,String> envMap, Map<String,String> rstMap) throws IOException {
        List<String> rowList = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(ECO_INFO));
            String title1= in.readLine();    // first line is title
            String title2 = envMap.get(ECO_ENV);
            String title3 = rstMap.get(RST_INFO);


            String title = title1 + Constant.SEPERATOR + title2 + Constant.SEPERATOR + title3;
//            rowList.add(title);

            String infoRow = null;
            String listId = null;
            String rstId = null;
            String envRow = null;
            String rstRow = null;
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
                if(LOCAL){
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
                String row = buildRow(infoRow,envRow,rstRow);
                String libSvm = buildSvm(row);
                rowList.add(libSvm);
                //扩容数据
//                expandData(rowList,libSvm,row);

            }
            System.out.println("|--------------- uselessCount="+uselessCount);
            System.out.println("|--------------- totalCount="+rowList.size());
        }finally {
            if(null !=in){
                in.close();
            }
        }

        return rowList;
    }

    public static String buildRow(String infoRow,String envRow,String rstRow){
        String row = null;
        if(LOCAL){
            row= infoRow + Constant.SEPERATOR + envRow + Constant.SEPERATOR + rstRow;
        }else{
            if(COLLECT_TYPE==TRAIN) {//训练集 有 isclick isbuy israwbuy orderid 四个字段
                row= infoRow + Constant.SEPERATOR + envRow + Constant.SEPERATOR + rstRow;
            }
            if(COLLECT_TYPE==TEST) {//测试集 补充
                row = infoRow + Constant.SEPERATOR +( 0+ Constant.SEPERATOR+0+ Constant.SEPERATOR+0+ Constant.SEPERATOR+0+ Constant.SEPERATOR )+ envRow + Constant.SEPERATOR + rstRow;
            }
        }
        return row;
    }


    public static void expandData(List<String> rowList ,String svmStr,String row){
        List<String> list = Splitter.on("\t").splitToList(row);
        if(COLLECT_TYPE == TRAIN){//训练集才扩容
            if(LABEL_TYPE == CLICK){//click 扩容20倍
                int click = Integer.parseInt(list.get(Constant.info_is_click_4));
                int is_new = Integer.parseInt(list.get(Constant.env_is_new_13));
                if(click==1){
                    if(is_new==1){
                        for(int i=0;i<50;i++){
                            rowList.add(svmStr);
                        }
                    }else{
                        for(int i=0;i<19;i++){
                            rowList.add(svmStr);
                        }
                    }

                }
            }
            if(LABEL_TYPE == BUY){//buy 扩容20倍
                int buy = Integer.parseInt(list.get(Constant.info_is_buy_5));
                int click = Integer.parseInt(list.get(Constant.info_is_click_4));
                if(click==1 && buy==0){
                    for(int i=0;i<24;i++){
                        rowList.add(svmStr);
                    }
                }
                if(buy==1){
                    for(int i=0;i<124;i++){
                        rowList.add(svmStr);
                    }
                }

            }
        }
    }

    /**
     * 原始数据转换成向量格式文件
     * @param originalData
     * @return
     */
    static public String buildSvm(String originalData){
        StringBuffer svmStr = new StringBuffer();

        List<String> list = Splitter.on(Constant.SEPERATOR).splitToList(originalData);
        if(LABEL_TYPE == CLICK){
            if(COLLECT_TYPE== TRAIN){
                //click_train
                svmStr.append(list.get(Constant.info_is_click_4)).append(Constant.SVM_DATA_SEPERATOR);
            }else{
                //click_test
                if(LOCAL){
                    svmStr.append(list.get(Constant.info_is_click_4)).append(Constant.SVM_DATA_SEPERATOR);
                }else{
                    svmStr.append(0).append(Constant.SVM_DATA_SEPERATOR);
                }
            }
        }else{//buy
            if(COLLECT_TYPE==TRAIN){
                //buy_train
                svmStr.append(list.get(Constant.info_is_buy_5)).append(Constant.SVM_DATA_SEPERATOR);
            }else{
                //buy_test
                if(LOCAL){
                    svmStr.append(list.get(Constant.info_is_buy_5)).append(Constant.SVM_DATA_SEPERATOR);
                }else{
                    svmStr.append(1).append(Constant.SVM_DATA_SEPERATOR);
                }
            }
        }
        LibSvmDataBuilder.buildSvmData(svmStr, list);
        return svmStr.toString();
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
            Collections.shuffle(dataList);
//            dataList.add(0,LibSvmDataBuilder.buildTitle());//选取特征头部增加一行作为标记
        }

        System.out.println("---------------- outputFile --------------");
        System.out.println("");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(outPath)))) {
            dataList.stream().forEach(data -> {
                pw.append(data).append(Constant.LINE_BREAK);
            });
        }
    }




}
