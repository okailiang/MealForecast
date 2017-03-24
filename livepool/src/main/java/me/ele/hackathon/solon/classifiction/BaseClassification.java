package me.ele.hackathon.solon.classifiction;

import me.ele.hackathon.solon.constant.Strategy;
import org.apache.spark.sql.SparkSession;

/**
 * Created by solomon on 16/10/26.
 */
public abstract class BaseClassification {

    public static SparkSession spark ;

    static {
        spark = SparkSession
                .builder()
                .appName("JavaMultilayerPerceptronClassifierExample")
                .master("local[4]")
                .config("spark.driver.memory", "2G")
                .config("spark.executor.memory", "1G")
                .getOrCreate();
    }

    public static String CLICK_TRAINFILE_PATH = Strategy.PATH_PREIX  + "click_train.txt";
    public static String CLICK_TESTFILE_PATH = Strategy.PATH_PREIX  + "click_test.txt";
    public static String BUY_TRAINFILE_PATH = Strategy.PATH_PREIX  + "buy_train.txt";
    public static String BUY_TESTFILE_PATH = Strategy.PATH_PREIX + "buy_test.txt";

    public static String CLICK_RESULT_OUTPUT_FILE = Strategy.PATH_PREIX+"click_result.txt";
    public static String BUY_RESULT_OUTPUT_FILE = Strategy.PATH_PREIX+"buy_result.txt";




    public static String CLICK_RES;
    public static String BUY_RES;

    public static double CLICK_SCORE;
    public static double CLICK_POSITIVE_SCORE;
    public static double CLICK_NEGATIVE_SCORE;
    public static double BUY_SCORE;
    public static double BUY_POSITIVE_SCORE;
    public static double BUY_NEGATIVE_SCORE;

    public static int CLICK_TREE_NUM = 5;
    public static int CLICK_TREE_DEPTH = 8;
    public static int CLICK_TREE_SEED = 600;
    public static int BUY_TREE_NUM = 20;
    public static int BUY_TREE_DEPTH = 5;
    public static int BUY_TREE_SEED = 600;

    /**
     * 预测 包含click预测 和 buy的预测
     * @throws Exception
     */
    public abstract void startPerdict() throws Exception;

    /**
     * 单独click的预测
     * @throws Exception
     */
    public abstract void predictClick() throws Exception;

    /**
     * 单独buy的预测
     * @throws Exception
     */
    public abstract void predictBuy() throws Exception;


}
