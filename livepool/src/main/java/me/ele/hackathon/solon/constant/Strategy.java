package me.ele.hackathon.solon.constant;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by solomon on 16/10/26.
 */
public class Strategy {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * for DataHandler
     */
    public static String PATH_PREIX =  "";
    public static String RECORD_FILE = "";
    public static String ORI_SRC_FILE_DIR = "";

    public static int CLICK_SHUFFLE_SEED = 9600;
    public static int BUY_SHUFFLE_SEED = 10200;

    //    public static String PATH_PREIX =  "/home/sheng.ke/solon";//服务器上的路径
    public static String BASE_DATA_SUB_DIR_PATH =  "data/";

    public static boolean LOCAL = true;//本地测试会将前20周数据拆分为训练集和测试集 向量文件生成会略有不同
    public static boolean IF_EXPAND = true;//是否扩容数据

    public static boolean IF_FILTER_RST = false;//是否过滤餐厅
    public static boolean IF_ADD_ORDER_INFO = false;//是否添加订单信息
    public static boolean IF_FILTE_USER = false;//是否过滤训练集中的用户数据
    public static boolean IF_FILTE_NULL_USER = false;//是否过滤训练集中的用户数据
    public static boolean IF_FILTE_INDEX = false;//是否过滤训练集中的用户数据
    public static boolean IF_CLUSTER = false;//是否按照聚类分组进行预测

    public static int CLICK_EXPAND_NUM = 19;//19
    public static int BUY_EXPAND_NUM = 123;//123
    public static int CLUSTER_GROUP_NUM = 0;

    public static String TEST_COLLECTION_TYPE = "Weed_day";



    static{
        Properties prop = new Properties();
        try {
            prop = PropertiesLoaderUtils.loadAllProperties("application.properties");
            PATH_PREIX = prop.getProperty("PATH_PREIX");
            RECORD_FILE = PATH_PREIX + "/record/feature_result_"+sdf.format(new Date()) +".csv";
            BASE_DATA_SUB_DIR_PATH = prop.getProperty("BASE_DATA_SUB_DIR_PATH");
            LOCAL = Boolean.parseBoolean(prop.getProperty("LOCAL"));
            IF_EXPAND = Boolean.parseBoolean(prop.getProperty("IF_EXPAND"));
            IF_FILTER_RST = Boolean.parseBoolean(prop.getProperty("IF_FILTER_RST"));
            IF_ADD_ORDER_INFO = Boolean.parseBoolean(prop.getProperty("IF_ADD_ORDER_INFO"));
            ORI_SRC_FILE_DIR = prop.getProperty("ORI_SRC_FILE_DIR");
            IF_FILTE_USER = Boolean.parseBoolean(prop.getProperty("IF_FILTE_USER"));
            IF_FILTE_INDEX = Boolean.parseBoolean(prop.getProperty("IF_FILTE_INDEX"));
            IF_FILTE_NULL_USER = Boolean.parseBoolean(prop.getProperty("IF_FILTE_NULL_USER"));
//            CLICK_EXPAND_NUM = Integer.parseInt(prop.getProperty("CLICK_EXPAND_NUM"));
//            BUY_EXPAND_NUM = Integer.parseInt(prop.getProperty("BUY_EXPAND_NUM"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean WITH_CLICK = false;//是否考虑click当做bug的特征

    public static Set<String> rstSet = new HashSet<>();
    static{
        rstSet.add("\"56b048c6b7d333abd4fe1e46f2b2b398\"");//19290:14
        rstSet.add("\"49a39c898743acac7e0b22d77ea97fe0\"");//1628:1
    }


    /**
     * for rfClassification
     */
    public static final String CLICK_PREDICT_FILE = "/Users/solomon/Desktop/hackathon/spark_prediction/click_predict.csv";
    public static final String BUY_PREDICT_FILE = "/Users/solomon/Desktop/hackathon/spark_prediction/buy_predict.csv";


    /**
     * for ResultHandler
     */
    public static final String LOG_ID = PATH_PREIX +"log_id.txt";
    public static final String CLICK_RESULT = PATH_PREIX+"click_result.txt";
    public static final String BUY_RESULT   = PATH_PREIX+"buy_result.txt";
    public static final String RESULT_OUT_PATH   = PATH_PREIX+"result.txt";

    /**
     * for BuyTestHandler
     */
    public static String BUY_TEST_WITHOUT_CLICK = PATH_PREIX + "buy_test.txt";
    public static String BUY_TEST_WITH_CLICK = PATH_PREIX+"buy_test.txt";

    public static void main(String[] args) {

    }
}
