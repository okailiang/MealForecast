package me.ele.hackathon.backup;

import java.io.IOException;

/**
 * solomon
 * 变量选取
 * 生成libsvm格式文件
 */
public class FormatDataStart {

    /*算法用*/
    public static final String CLICK_TRAN_FIlE = "/Users/solomon/Desktop/hackathon/spark_libsvm/click_tran.txt";
    public static final String BUY_TRAN_FILE = "/Users/solomon/Desktop/hackathon/spark_libsvm/buy_tran.txt";
    public static final String CLICK_TEST_FIlE = "/Users/solomon/Desktop/hackathon/spark_libsvm/click_test.txt";
    public static final String BUY_TEST_FILE = "/Users/solomon/Desktop/hackathon/spark_libsvm/buy_test.txt";


    private static final String SEPERATOR = " ";

    public static void main(String[] args) throws IOException {
        FormatTrainData.opreation();
        FormatTestData.operation();
    }


}
