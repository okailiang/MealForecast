package me.ele.hackathon.solon;

import me.ele.hackathon.data.ExtractData;
import me.ele.hackathon.data.LibSvmDataBuilder;
import me.ele.hackathon.solon.classifiction.MultiplerRFClassification;
import me.ele.hackathon.solon.classifiction.RandomForestClassification;
import me.ele.hackathon.solon.constant.Strategy;
import me.ele.hackathon.solon.handler.DataHandler;
import me.ele.hackathon.solon.handler.ResultHandler;
import me.ele.hackathon.solon.prediction.Prediction;
import me.ele.hackathon.utils.AggregateFile;
import me.ele.hackathon.utils.CalculateScore;

import java.io.IOException;
import java.util.Arrays;


/**
 * Created by solomon on 16/10/22.
 * 两层随机森林树算法
 */
public class MultipleRfPredictor {

    public static void main(String[] args) {
        //生成基础文件  1-18周为训练集  19-20周为测试集
        try {
            ExtractData.extractData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LibSvmDataBuilder.selected_feature_list = Arrays.asList(
                "getRst_restaurant_id_0"
                , "getInfo_index_3"
                , "getRst_primary_category_1"
                , "getRst_good_rating_rate_9"
                , "getRst_open_month_num_10"
                , "getRst_service_rating_22"
                , "getRst_food_num_26"
                , "getRst_food_image_num_27"
//                , "getEnv_day_no_2"
                , "getEnv_minutes_3"
        );
        //多层RF算法
        MultiplerRFClassification.paramList =Arrays.asList("10,5,32","6,5,32");//"treeNum","treeDepth"","treeMaxbin"
        try {
            //[1]通过多次RF 得出svm文件
            // 生成第一层向量文件
            Strategy.WITH_CLICK = false;
            DataHandler.startBuildData();
            //根据第一层向量文案 和 paramList里的算法参数 计算出多个结果
            MultiplerRFClassification classification =  new MultiplerRFClassification();
            classification.startPerdict();
            //多个结果合并成第二层向量文件
            classification.resultToSvm();
            //[2]再通过RF,计算最终结果
            new RandomForestClassification().startPerdict();
            ResultHandler.outPutResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
