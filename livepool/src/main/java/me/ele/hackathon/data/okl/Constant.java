package me.ele.hackathon.data.okl;

/**
 * @author oukailiang
 * @create 2016-10-30 下午4:40
 */

public class Constant {

    ///Users/oukailiang/Downloads/hackathon/E_Data/
    public static final String LIBSVM_FILE_DIR = "/Users/oukailiang/Downloads/hackathon/";
    public static final String TXT_FILE_DIR = "/Users/oukailiang/Downloads/hackathon/E_Data/new_data/";
    public static final String NEW_TXT_DIR = "/Users/oukailiang/Downloads/hackathon/combine/";

    //需要修改的数据
    public static final int TEST_BEGAIN_DAY = 21 * 7; //测试集开始的天
    public static final int BEFORE_DAY = TEST_BEGAIN_DAY - 7 * 6; //向前搜索4周
    public static final int MIN_WEEK = 14; //与INFO_FILE和ENV_FILE最小周对应
    //
    public static final String INFO_FILE = TXT_FILE_DIR + "1420_his_eco_info.txt";
    public static final String ENV_FILE = TXT_FILE_DIR + "1420_his_eco_env.txt";
    public static final String RST_FILE = TXT_FILE_DIR + "rst_info.txt";
    public static final String ORDER_FILE = TXT_FILE_DIR + "his_order_info.txt";
    public static final String NEXT_INFO_FILE = TXT_FILE_DIR + "2122_next_eco_info.txt";
    public static final String NEXT_ENV_FILE = TXT_FILE_DIR + "2122_next_eco_env.txt";
    //
    public static final String RESULT_FILE = TXT_FILE_DIR + "result.txt";
    public static final String RULE_RESULT_FILE = TXT_FILE_DIR + "rule_result.txt";
    //
    public static final String HIS_INFO_ENV_RST = NEW_TXT_DIR + "combine_his_info_env_rst.txt";
    //文件中每个数据间按该间隔符分割
    public static final String SEPARATOR = " ";
    //文件中每个数据间按该间隔符分割
    public static final String SEPARATOR_TAB = "\t";
    //全部的20周的
    public static final double click_rate_avg = 0.0470;
    public static final double buy_rate_avg = 0.0072;
    //餐厅的
    public static double rst_click_rate_avg = 0.045;
    public static double rst_buy_rate_avg = 0.0060;
    //
    public static final int AFTER_WEEK = 0;
    public static final int ONE = 1;
    public static final int ZERO = 0;
    public static final String S_ONE = "1";
    public static final String S_ZERO = "0";

    public static final String WEEK_USER_GROWTH = "week" + SEPARATOR_TAB + "total_num" + SEPARATOR_TAB
            + "old_user" + SEPARATOR_TAB + "new_user" + SEPARATOR_TAB + "new_rate";
    public static final String RST_CLICK_BUY_RATE = "rst_id" + SEPARATOR_TAB + "total_log_num" + SEPARATOR_TAB
            + "click_num" + SEPARATOR_TAB + "buy_num" + SEPARATOR_TAB + "click_rate" + SEPARATOR_TAB + "buy_rate";
}
