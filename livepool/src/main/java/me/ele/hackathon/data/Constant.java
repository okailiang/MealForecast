package me.ele.hackathon.data;

import scala.Int;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by solomon on 16/10/19.
 */
public class Constant {

    /*算法用*/
    public static final String CLICK_TEST_FIlE = "/Users/solomon/Desktop/hackathon/spark_libsvm/click_test.txt";
    public static final String BUY_TEST_FILE = "/Users/solomon/Desktop/hackathon/spark_libsvm/buy_test.txt";

    /*算法用*/
    public static final String CLICK_TRAN_FIlE = "/Users/solomon/Desktop/hackathon/spark_libsvm/click_train.txt";
    public static final String BUY_TRAN_FILE = "/Users/solomon/Desktop/hackathon/spark_libsvm/buy_train.txt";

    static public final String SEPERATOR = "\t";
    static public final String LINE_BREAK = "\n";
    static public final String COLON = ":";
    static public final String SVM_DATA_SEPERATOR = " ";
    static public final String DOUBLE_QUOTATION_MARKS = "\"";
    static public final String NUMBER_SIGN = "#";
    static public final String SPACE = " ";


    //eco_info
    public static final int info_log_id_0 = 0;
    public static final int info_list_id_1 = 1;
    public static final int info_restaurant_id_2 = 2;
    public static final int info_index_3 = 3;
    public static final int info_is_click_4 = 4;
    public static final int info_is_buy_5 = 5;
    public static final int info_is_raw_buy_6 = 6;
    public static final int info_order_id_7 = 7;

    //eco_env
    public static final int env_list_id_8 = 8;
    public static final int env_is_select_9 = 9;
    public static final int env_day_no_10 = 10;
    public static final int env_minutes_11 = 11;
    public static final int env_eleme_device_id_12 = 12;
    public static final int env_is_new_13 = 13;
    public static final int env_info_x = 14;
    public static final int env_info_y = 15;
    public static final int env_user_id = 16;
    public static final int env_network_type_17 = 17;
    public static final int env_platform_18 = 18;
    public static final int env_brand_19 = 19;
    public static final int env_model_20 = 20;
    public static final int env_network_operator_21 = 21;
    public static final int env_resolution_22 = 22;
    public static final int env_channel_23 = 23;


    //rst_info
    public static final int rst_restaurant_id = 24;
    public static final int rst_primary_category_25 = 25;
    public static final int rst_food_name_list_26 = 26;
    public static final int rst_category_list_27 = 27;
    public static final int rst_x = 28;
    public static final int rst_y = 29;
    public static final int rst_agent_fee_30 = 30;
    public static final int rst_is_premium_31 = 31;
    public static final int rst_address_type_32 = 32;
    public static final int rst_good_rating_rate_33 = 33;
    public static final int rst_open_month_num_34 = 34;
    public static final int rst_has_image_35 = 35;
    public static final int rst_has_food_img_36 = 36;
    public static final int rst_min_deliver_amount_37 = 37;
    public static final int rst_time_ensure_spent_38 = 38;
    public static final int rst_is_time_ensure_39 = 39;
    public static final int rst_is_ka_40= 40;

    public static final int rst_is_time_ensure_discount_41 = 41;
    public static final int rst_is_eleme_deliver_42 = 42;
    public static final int rst_radius_43 = 43;
    public static final int rst_bu_flag_44 = 44;
    public static final int rst_brand_name_45 = 45;
    public static final int rst_service_rating_46 = 46;
    public static final int rst_invoice_47 = 47;
    public static final int rst_online_payment_48 = 48;
    public static final int rst_public_degree_49 = 49;
    public static final int rst_food_num_50 = 50;
    public static final int rst_food_image_num_51 = 51;
    public static final int rst_is_promotion_info_52 = 52;
    public static final int rst_is_bookable_53 = 53;

    private static Set<Integer> excludeSet ;

    static{
        excludeSet = new HashSet<>();
        excludeSet.add(info_log_id_0);
        excludeSet.add(info_list_id_1);
        excludeSet.add(info_is_click_4);
        excludeSet.add(info_is_buy_5);
        excludeSet.add(info_order_id_7);
        excludeSet.add(env_list_id_8);
        excludeSet.add(env_day_no_10);
        excludeSet.add(env_info_x);
        excludeSet.add(env_info_y);
        excludeSet.add(env_user_id);
        excludeSet.add(env_resolution_22);
        excludeSet.add(rst_restaurant_id);
        excludeSet.add(rst_x);
        excludeSet.add(rst_y);
    }

    public static Set<Integer> getExcludeSet(){
        return excludeSet;
    }

}
