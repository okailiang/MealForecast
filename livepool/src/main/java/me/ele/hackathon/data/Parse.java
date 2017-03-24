package me.ele.hackathon.data;

import com.google.common.collect.Maps;
import me.ele.hackathon.object.*;
import me.ele.hackathon.solon.constant.Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by solomon on 16/10/19.
 */
public class Parse {

    /**
     * 将字符串 转换成libSvm 格式
     * @param network
     * @return
     */
    public static int parseNetworkType(String network) {
        switch (network) {
            case "\"WIFI\"":
                return 1;
            case "\"4G\"":
                return 2;
            case "\"3G\"":
                return 3;
            case "\"2G\"":
                return 4;
            case "\"UNKNOWN\"":
                return 5;
            default:
//                System.out.println("Unknown Network Type: " + network);
                return 6;
        }
    }

    public static int parsePlatform(String plartform) {
        switch (plartform) {
            case "\"Android\"":
                return 1;
            case "\"iOS\"":
                return 2;
            default:
//                System.out.println("Unknown Platform: " + plartform);
                return 3;
        }
    }

    public static int parseNetworkOperator(String op) {
        switch (op) {
            case "\"yd\"":
                return 1;
            case "\"lt\"":
                return 2;
            case "\"dx\"":
                return 3;
            case "\"OTHER\"":
                return 4;
            default:
//                System.out.println("Unknown Network Operator: " + op);
                return 5;
        }
    }


    public static int parseBuFlag(String op) {
        switch (op) {
            case "\"BL\"":
                return 1;
            case "\"GKA\"":
                return 2;
            case "\"SIG\"":
                return 3;
            default:
//                System.out.println("Unknown Network Operator: " + op);
                return 5;
        }
    }

    public static int parseMinutes(int minutes) {
        return (int) (minutes/60);
    }
    public static int parseDayNo(int dayNo) {
        return (dayNo%7);
    }

    public static Map<String,Integer> userIdMap = new HashMap<>();
    public static Integer userCount =1;
    public static int parseUserId(String userId){
        if(null == userIdMap.get(userId)){
            userCount++;
            userIdMap.put(userId, userCount);

        }
        return userIdMap.get(userId);
    }

    public static Map<String,Integer> rstIdMap = new HashMap<>();
    public static Integer rstCount =1;
    public static int parseRstId(String rstId){
        if(null == rstIdMap.get(rstId)){
            rstCount++;
            rstIdMap.put(rstId, rstCount);

        }
        return rstIdMap.get(rstId);
    }

    public static Map<String,Integer> primaryCategoryMap = new HashMap<>();
    public static Integer primaryCategoryCount =1;
    public static int parseRrimaryCategory(String str){
        if(null == primaryCategoryMap.get(str)){
            primaryCategoryCount++;
            primaryCategoryMap.put(str,primaryCategoryCount);

        }
        return primaryCategoryMap.get(str);
    }


    public static Map<String,Integer> brandMap = new HashMap<>();
    public static Integer brandCount =1;
    public static int parseBrand(String str){
        if(null == brandMap.get(str)){
            brandCount++;
            brandMap.put(str,brandCount);

        }
        return brandMap.get(str);
    }

    static public  Map<String,Integer> modelMap = new HashMap<>();
    public static Integer modelCount =1;
    public static int parseModel(String str){
        if(null == modelMap.get(str)){
            modelCount++;
            modelMap.put(str,modelCount);

        }
        return modelMap.get(str);
    }


    public static Map<String, RstInfo> rstInfoMap = Maps.newHashMap();



    public static EcoEnvNew buildEnv( List<String> envRow){
        EcoEnvNew env = new EcoEnvNew();
        env.setEnv_list_id_0(envRow.get(0));
        env.setEnv_is_select_1(Integer.parseInt(envRow.get(1)));
        env.setEnv_day_no_2(Parse.parseDayNo(Integer.parseInt(envRow.get(2))));
        env.setEnv_minutes_3(Parse.parseMinutes(Integer.parseInt(envRow.get(3))));
//        env.setEnv_eleme_device_id_4(Integer.parseInt());
        env.setEnv_is_new_5(Integer.parseInt(envRow.get(5)));
//        env.setEnv_info_x_6(Integer.parseInt());
//        env.setEnv_info_y_7(Integer.parseInt());
        env.setEnv_user_id_8(Parse.parseUserId(envRow.get(8)));
        env.setEnv_network_type_9(Parse.parseNetworkType(envRow.get(9)));
        env.setEnv_platform_10(Parse.parsePlatform(envRow.get(10)));
        env.setEnv_brand_11(Parse.parseBrand(envRow.get(11)));
        env.setEnv_model_12(Parse.parseModel(envRow.get(12)));
        env.setEnv_network_operator_13(Parse.parseNetworkOperator(envRow.get(13)));
//        env.setEnv_resolution_14();
//        env.setEnv_channel_15();
        return env;
    }

    public static RstInfoNew buildRst( List<String> rstRow){
        RstInfoNew rst = new RstInfoNew();
        rst.setRestaurant_id(rstRow.get(0));
        rst.setRst_restaurant_id_0(Parse.parseRstId(rstRow.get(0)));
        rst.setRst_primary_category_1(Parse.parseRrimaryCategory(rstRow.get(1)));
//        rst.setRst_x_4();
//        rst.setRst_y_5();
        rst.setRst_agent_fee_6(Integer.parseInt(rstRow.get(6)));
        rst.setRst_is_premium_7(Integer.parseInt(rstRow.get(7)));
//        rst.setRst_address_type_8();
        rst.setRst_good_rating_rate_9((int) (Double.parseDouble(rstRow.get(9)) * 100));
        rst.setRst_open_month_num_10(Integer.parseInt(rstRow.get(10)) + 7);
        rst.setRst_has_image_11(Integer.parseInt(rstRow.get(11)));
        rst.setRst_has_food_img_12(Integer.parseInt(rstRow.get(12)));
        rst.setRst_min_deliver_amount_13(Integer.parseInt(rstRow.get(13)));
//        rst.setRst_time_ensure_spent_14();
        rst.setRst_is_time_ensure_15(Integer.parseInt(rstRow.get(15)));
        rst.setRst_is_ka_16(Integer.parseInt(rstRow.get(16)));
        rst.setRst_is_time_ensure_discount_17(Integer.parseInt(rstRow.get(17)));
        rst.setRst_is_eleme_deliver_18(Integer.parseInt(rstRow.get(18)));
        rst.setRst_radius_43_19((int) Double.parseDouble(rstRow.get(19)));
        rst.setRst_bu_flag_20(Parse.parseBuFlag(rstRow.get(20)));
//        rst.setRst_brand_name_21();
        rst.setRst_service_rating_22((int) (Double.parseDouble(rstRow.get(22))*100));
        rst.setRst_invoice_23(Integer.parseInt(rstRow.get(23)));
//        rst.setRst_online_payment_24();
        rst.setRst_public_degree_25(Integer.parseInt(rstRow.get(25)));
        rst.setRst_food_num_26(Integer.parseInt(rstRow.get(26)));
        rst.setRst_food_image_num_27(Integer.parseInt(rstRow.get(27)));
        rst.setRst_is_promotion_info_28(Integer.parseInt(rstRow.get(28)));
        rst.setRst_is_bookable_29(Integer.parseInt(rstRow.get(29)));
        String idStr = rstRow.get(0);
        if(Strategy.IF_ADD_ORDER_INFO) {
            if (null != RstOrderSumAvgData.orderSumAvgMap.get(idStr)) {
                rst.setRstOrderSumAvg(RstOrderSumAvgData.orderSumAvgMap.get(idStr));
            } else {
                rst.setRstOrderSumAvg(new RstOrderSumAvg());
            }
        }
        return rst;
    }

    public static EcoInfoNew buildInfo( List<String> infoRow){
        EcoInfoNew ecoInfoNew = new EcoInfoNew();
        ecoInfoNew.setInfo_log_id_0(infoRow.get(0));
        ecoInfoNew.setInfo_list_id_1(infoRow.get(1));
        ecoInfoNew.setInfo_restaurant_id_2(infoRow.get(2));
        ecoInfoNew.setInfo_index_3(Integer.parseInt(infoRow.get(3)));
        return ecoInfoNew;
    }
}
