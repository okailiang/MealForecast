package me.ele.hackathon.data;

import me.ele.hackathon.object.EcoInfoNew;
import me.ele.hackathon.solon.constant.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by solomon on 16/10/20.
 */
public class LibSvmDataBuilder {

    public static List<String> featureCollect;

    public static List<String> featureList = Arrays.asList(
            "getRst_restaurant_id_0"
            , "getInfo_index_3"
            , "getRst_primary_category_1"
            , "getRst_good_rating_rate_9"
            , "getRst_open_month_num_10"
            , "getRst_service_rating_22"
            , "getRst_food_num_26"
            , "getRst_food_image_num_27"
            , "getEnv_day_no_2"
            , "getEnv_minutes_3" //10
//            ,"getEnv_is_select_1"
//            ,"getEnv_is_new_5"
////            ,"getEnv_user_id_8"
//            ,"getEnv_network_type_9" //13
//            ,"getEnv_platform_10"
//            ,"getEnv_brand_11"
//            ,"getEnv_model_12"
//            ,"getEnv_network_operator_13"
//            ,"getRst_is_promotion_info_28"
//            ,"getRst_is_bookable_29"
//            ,"getRst_invoice_23"
//            ,"getRst_online_payment_24"
//            ,"getRst_public_degree_25"
//            ,"getRst_has_image_11"
//            ,"getRst_has_food_img_12"
//            ,"getRst_min_deliver_amount_13"
//            ,"getRst_is_time_ensure_15"
//            ,"getRst_is_ka_16"
//            ,"getRst_is_time_ensure_discount_17"
//            ,"getRst_is_eleme_deliver_18"
//            ,"getRst_radius_43_19"
//            ,"getRst_bu_flag_20"
//            ,"getRst_agent_fee_6"
//            ,"getRst_is_premium_7"

    );
    static{
       featureCollect = arr1(LibSvmDataBuilder.featureList).stream().distinct().collect(Collectors.toList());
    }

    public static List<String> selected_feature_list;


    public static int buildSvmData(StringBuffer svmSb,EcoInfoNew info){
        int i=1;

        if(selected_feature_list.contains("getRst_restaurant_id_0")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_restaurant_id_0()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getInfo_index_3")){
            svmSb.append(i++).append(Constant.COLON).append(info.getInfo_index_3()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_primary_category_1")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_primary_category_1()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_good_rating_rate_9")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_good_rating_rate_9()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_open_month_num_10")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_open_month_num_10()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_service_rating_22")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_service_rating_22()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_food_num_26")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_food_num_26()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_food_image_num_27")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_food_image_num_27()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_day_no_2")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_day_no_2()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_minutes_3")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_minutes_3()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_is_select_1")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_is_select_1()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_is_new_5")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_is_new_5()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_user_id_8")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_user_id_8()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_network_type_9")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_network_type_9()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_platform_10")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_platform_10()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_brand_11")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_brand_11()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_model_12")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_model_12()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getEnv_network_operator_13")){
            svmSb.append(i++).append(Constant.COLON).append(info.getEcoEnvNew().getEnv_network_operator_13()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_promotion_info_28")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_promotion_info_28()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_bookable_29")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_bookable_29()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_invoice_23")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_invoice_23()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_online_payment_24")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_online_payment_24()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_public_degree_25")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_public_degree_25()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_has_image_11")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_has_image_11()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_has_food_img_12")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_has_food_img_12()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_min_deliver_amount_13")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_min_deliver_amount_13()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_time_ensure_15")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_time_ensure_15()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_ka_16")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_ka_16()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_time_ensure_discount_17")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_time_ensure_discount_17()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_eleme_deliver_18")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_eleme_deliver_18()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_radius_43_19")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_radius_43_19()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_bu_flag_20")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_bu_flag_20()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_agent_fee_6")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_agent_fee_6()).append(Constant.SVM_DATA_SEPERATOR);
        }
        if(selected_feature_list.contains("getRst_is_premium_7")){
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRst_is_premium_7()).append(Constant.SVM_DATA_SEPERATOR);
        }


        //ORDER
        if(Strategy.IF_ADD_ORDER_INFO) {
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_cut_money()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_deliver_fee()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_eleme_order_total()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_food_category()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_has_new_user_subsidy()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_hongbao_amount()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_is_coupon()).append(Constant.SVM_DATA_SEPERATOR);
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_order_process_minutes()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_receiver_deliver_fee()).append(Constant.SVM_DATA_SEPERATOR);
            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getAvg_total()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_cut_money()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_deliver_fee()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_eleme_order_total()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_food_category()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_has_new_user_subsidy()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_hongbao_amount()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_is_coupon()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_order_process_minutes()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_receiver_deliver_fee()).append(Constant.SVM_DATA_SEPERATOR);
//            svmSb.append(i++).append(Constant.COLON).append(info.getRstInfoNew().getRstOrderSumAvg().getSum_total()).append(Constant.SVM_DATA_SEPERATOR);
        }
        return i;

    }


    /**
     * 获取所有排列组合的方法
     * @param innerList
     * @return
     */
    public static List<String> arr1(List<String> innerList){
        int n  = innerList.size();
        List<String> resultList = new ArrayList<>();
        for(int x=0;x<n;x++){
            List<String> a = innerList.subList(x,x+1);
            List<String> b = innerList.subList(x+1,n);
            resultList.addAll(arr2(a,b));
        }
        return resultList;
    }

    public static List<String> arr2(List<String> innerAList,List<String> innerBList){
        int n  = innerBList.size();
        List<String> resultList = new ArrayList<>();
        final StringBuffer result = new StringBuffer();
        for(int x=0;x<n;x++){
            for(int y=0;y<=x;y++){
                List<String> splitBList = innerBList.subList(y,x+1);
                innerAList.stream().forEach(a->{
                    result.append(a).append(",");
                });
                splitBList.stream().forEach(b -> {
                    result.append(b).append(",");
                });
                resultList.add(result.toString());
                result.setLength(0) ;
            }
        }
        return resultList;
    }

    public static String buildTitle(){
        StringBuffer svmStr = new StringBuffer();
        svmStr.append(0).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.info_restaurant_id_2).append(Constant.COLON).append(Constant.info_restaurant_id_2).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.info_index_3).append(Constant.COLON).append(Constant.info_index_3).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_is_select_9).append(Constant.COLON).append(Constant.env_is_select_9).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_minutes_11).append(Constant.COLON).append(Constant.env_minutes_11).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_is_new_13).append(Constant.COLON).append( Constant.env_is_new_13).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_network_type_17).append(Constant.COLON).append(Constant.env_network_type_17).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_platform_18).append(Constant.COLON).append( Constant.env_platform_18).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_brand_19).append(Constant.COLON).append( Constant.env_brand_19).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_model_20).append(Constant.COLON).append( Constant.env_model_20).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.env_network_operator_21).append(Constant.COLON).append(Constant.env_network_operator_21).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_primary_category_25).append(Constant.COLON).append( Constant.rst_primary_category_25).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_agent_fee_30).append(Constant.COLON).append( Constant.rst_agent_fee_30).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_premium_31).append(Constant.COLON).append( Constant.rst_is_premium_31).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_good_rating_rate_33).append(Constant.COLON).append(Constant.rst_good_rating_rate_33).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_open_month_num_34).append(Constant.COLON).append(Constant.rst_open_month_num_34).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_has_image_35).append(Constant.COLON).append( Constant.rst_has_image_35).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_has_food_img_36).append(Constant.COLON).append( Constant.rst_has_food_img_36).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_min_deliver_amount_37).append(Constant.COLON).append( Constant.rst_min_deliver_amount_37).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_time_ensure_39).append(Constant.COLON).append( Constant.rst_is_time_ensure_39).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_ka_40).append(Constant.COLON).append( Constant.rst_is_ka_40).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_time_ensure_discount_41).append(Constant.COLON).append( Constant.rst_is_time_ensure_discount_41).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_eleme_deliver_42).append(Constant.COLON).append( Constant.rst_is_eleme_deliver_42).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_radius_43).append(Constant.COLON).append(Constant.rst_radius_43).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_bu_flag_44).append(Constant.COLON).append(Constant.rst_bu_flag_44).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_service_rating_46).append(Constant.COLON).append(Constant.rst_service_rating_46).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_public_degree_49).append(Constant.COLON).append( Constant.rst_public_degree_49).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_food_num_50).append(Constant.COLON).append( Constant.rst_food_num_50).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_food_image_num_51).append(Constant.COLON).append( Constant.rst_food_image_num_51).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_promotion_info_52).append(Constant.COLON).append( Constant.rst_is_promotion_info_52).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_bookable_53).append(Constant.COLON).append( Constant.rst_is_bookable_53).append(Constant.SVM_DATA_SEPERATOR);

        return svmStr.toString();

    }

    @Deprecated
    public static StringBuffer buildSvmData(StringBuffer svmStr,List<String> list){

        svmStr.append(Constant.info_restaurant_id_2).append(Constant.COLON).append(Parse.parseRstId(list.get(Constant.info_restaurant_id_2))).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.info_index_3).append(Constant.COLON).append(list.get(Constant.info_index_3)).append(Constant.SVM_DATA_SEPERATOR);

//        svmStr.append(Constant.env_is_select_9).append(Constant.COLON).append(list.get(Constant.env_is_select_9)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_minutes_11).append(Constant.COLON).append(Parse.parseMinutes(Integer.parseInt(list.get(Constant.env_minutes_11)))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_is_new_13).append(Constant.COLON).append(list.get(Constant.env_is_new_13)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_network_type_17).append(Constant.COLON).append(Parse.parseNetworkType(list.get(Constant.env_network_type_17))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_platform_18).append(Constant.COLON).append(Parse.parsePlatform(list.get(Constant.env_platform_18))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_brand_19).append(Constant.COLON).append(Parse.parseBrand(list.get(Constant.env_brand_19))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_model_20).append(Constant.COLON).append(Parse.parseModel(list.get(Constant.env_model_20))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.env_network_operator_21).append(Constant.COLON).append(Parse.parseNetworkOperator(list.get(Constant.env_network_operator_21))).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_primary_category_25).append(Constant.COLON).append(Parse.parseRrimaryCategory(list.get(Constant.rst_primary_category_25))).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_agent_fee_30).append(Constant.COLON).append(list.get(Constant.rst_agent_fee_30)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_is_premium_31).append(Constant.COLON).append(list.get(Constant.rst_is_premium_31)).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_good_rating_rate_33).append(Constant.COLON).append( (int)Double.parseDouble(list.get(Constant.rst_good_rating_rate_33))).append(Constant.SVM_DATA_SEPERATOR);
//        int rst_open_month_num_34 = Integer.parseInt(list.get(Constant.rst_open_month_num_34))+7;
//        svmStr.append(Constant.rst_open_month_num_34).append(Constant.COLON).append(rst_open_month_num_34).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_has_image_35).append(Constant.COLON).append(list.get(Constant.rst_has_image_35)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_has_food_img_36).append(Constant.COLON).append(list.get(Constant.rst_has_food_img_36)).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_min_deliver_amount_37).append(Constant.COLON).append(list.get(Constant.rst_min_deliver_amount_37)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_is_time_ensure_39).append(Constant.COLON).append(list.get(Constant.rst_is_time_ensure_39)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_is_ka_40).append(Constant.COLON).append(list.get(Constant.rst_is_ka_40)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_is_time_ensure_discount_41).append(Constant.COLON).append(list.get(Constant.rst_is_time_ensure_discount_41)).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_is_eleme_deliver_42).append(Constant.COLON).append(list.get(Constant.rst_is_eleme_deliver_42)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_radius_43).append(Constant.COLON).append((int)Double.parseDouble(list.get(Constant.rst_radius_43))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_bu_flag_44).append(Constant.COLON).append(Parse.parseBuFlag(list.get(Constant.rst_bu_flag_44))).append(Constant.SVM_DATA_SEPERATOR);
        svmStr.append(Constant.rst_service_rating_46).append(Constant.COLON).append((int)Double.parseDouble(list.get(Constant.rst_service_rating_46))).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_public_degree_49).append(Constant.COLON).append(list.get(Constant.rst_public_degree_49)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_food_num_50).append(Constant.COLON).append(list.get(Constant.rst_food_num_50)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_food_image_num_51).append(Constant.COLON).append(list.get(Constant.rst_food_image_num_51)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_is_promotion_info_52).append(Constant.COLON).append(list.get(Constant.rst_is_promotion_info_52)).append(Constant.SVM_DATA_SEPERATOR);
//        svmStr.append(Constant.rst_is_bookable_53).append(Constant.COLON).append(list.get(Constant.rst_is_bookable_53)).append(Constant.SVM_DATA_SEPERATOR);

        return svmStr;

    }

}
