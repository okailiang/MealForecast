package me.ele.hackathon.solon;

import me.ele.hackathon.clustering.KMeansCluster;
import me.ele.hackathon.data.ExtractData;
import me.ele.hackathon.data.ExtractNextData;
import me.ele.hackathon.data.LibSvmDataBuilder;
import me.ele.hackathon.solon.classifiction.GBTreeClassification;
import me.ele.hackathon.solon.classifiction.RandomForestClassification;
import me.ele.hackathon.solon.constant.Strategy;
import me.ele.hackathon.solon.handler.DataHandler;
import me.ele.hackathon.solon.prediction.Prediction;

import java.io.IOException;
import java.util.Arrays;


/**
 * Created by solomon on 16/10/22.
 * 单次算法或特征
 */
public class SinglePredictor {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        LibSvmDataBuilder.selected_feature_list = Arrays.asList(
                "getRst_restaurant_id_0"
              //  , "getInfo_index_3"
                , "getRst_primary_category_1"
                , "getRst_good_rating_rate_9"
                , "getRst_open_month_num_10"
                , "getRst_service_rating_22"
                , "getRst_food_num_26"
                , "getRst_food_image_num_27"
               // , "getEnv_day_no_2"
                , "getEnv_minutes_3"
//                , "getEnv_brand_11"//15
//                , "getEnv_model_12"//16
//                , "getRst_min_deliver_amount_13"// add
//                ,"getRst_public_degree_25"//22
                //  ,"getRst_agent_fee_6"//32
        );
//        LibSvmDataBuilder.selected_feature_list = Arrays.asList(
//                "getRst_restaurant_id_0","getInfo_index_3","getRst_primary_category_1","getRst_good_rating_rate_9","getRst_open_month_num_10","getRst_service_rating_22","getRst_food_num_26","getRst_food_image_num_27","getEnv_day_no_2","getEnv_minutes_3","getEnv_is_select_1","getEnv_is_new_5","getEnv_user_id_8","getEnv_network_type_9","getEnv_platform_10","getEnv_brand_11","getEnv_model_12","getEnv_network_operator_13","getRst_is_promotion_info_28"
//        );
        //Feature Select result
        //click [1.0,2.0,3.0,4.0,5.0,7.0,8.0,22.0,32.0]X`
        //b u y [1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,10.0]

       // LibSvmDataBuilder.selected_feature_list = LibSvmDataBuilder.featureList;

        /** 试试 buy_tree =20时 线上的预测情况 */

        RandomForestClassification classification = new RandomForestClassification();

//        ExtractData.hour = 0;
        // ExtractNextData.hour = 24;

        //for(int i=0;i<1;i++){
//            ExtractData.extractData();
        //  ExtractNextData.extractData();
//            DataHandler.clickSVMList.clear();
//            DataHandler.buySVMList.clear();
        Prediction.predictWithoutClick(classification);
//            ExtractData.hour ++;
        // ExtractNextData.hour ++;
        // }

        long end = System.currentTimeMillis();
        System.out.println(" time cost:" + (end - start) / 1000);

    }

}
