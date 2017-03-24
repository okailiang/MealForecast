package me.ele.hackathon.solon.prediction;

import me.ele.hackathon.data.ExtractData;
import me.ele.hackathon.data.LibSvmDataBuilder;
import me.ele.hackathon.object.Record;
import me.ele.hackathon.solon.classifiction.BaseClassification;
import me.ele.hackathon.solon.constant.Strategy;
import me.ele.hackathon.solon.handler.BuyTestHandler;
import me.ele.hackathon.solon.handler.DataHandler;
import me.ele.hackathon.solon.handler.ResultHandler;
import me.ele.hackathon.utils.ResultToExcel;

import java.io.IOException;


/**
 * Created by solomon on 16/10/22.
 */
public class Prediction {

    public static Record record = new Record();

    public static void predictWithoutClick(BaseClassification classification){
        try {
            Strategy.WITH_CLICK = false;
            DataHandler.startBuildData();
            classification.startPerdict();
            ResultHandler.outPutResult();
            //结果记录
            outputRecordToExl(classification);
            DataHandler.logIdList.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void predictWithClick(BaseClassification classification) {
        try {
            Strategy.WITH_CLICK = true;
            //[1]先预测click
            DataHandler.buildClickTrain();
            DataHandler.buildClickTest();
            classification.predictClick();
            //[2]再预测buy
            DataHandler.buildBuyTrain();
            DataHandler.buildBuyTest();
            //将click预测完的结果放入buy_test中
            BuyTestHandler.getBuyTestWithClick();
            classification.predictBuy();
            ResultHandler.outPutResult();
            //结果记录
            outputRecordToExl(classification);
            DataHandler.logIdList.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void outputRecordToExl(BaseClassification classification)  {
        String feature = "";
        int size = LibSvmDataBuilder.selected_feature_list.size();
        for(int i=0;i<size;i++){
            feature += LibSvmDataBuilder.selected_feature_list.get(i)+";";
        }

        String strategy =
                "classification:" + classification.getClass().getSimpleName()
                        + "\n CLICK_SHUFFLE_SEED:"+ Strategy.CLICK_SHUFFLE_SEED
                        + "\n BUY_SHUFFLE_SEED:"+ Strategy.BUY_SHUFFLE_SEED
                        + "\n click_expain:" + Strategy.CLICK_EXPAND_NUM
                        + "\n buy_expain:" + Strategy.BUY_EXPAND_NUM
                        + "\n click_tree_num:" + classification.CLICK_TREE_NUM
                        + "\n click_tree_depth:" + classification.CLICK_TREE_DEPTH
                        + "\n click_tree_seed:" + classification.CLICK_TREE_SEED
                        + "\n buy_tree_num:" + classification.BUY_TREE_NUM
                        + "\n buy_tree_depth:" + classification.BUY_TREE_DEPTH
                        + "\n buy_tree_seed:" + classification.BUY_TREE_SEED
                        + "\n his_collect:"+ Strategy.TEST_COLLECTION_TYPE
                        + "\n day_no:"+ ExtractData.setDayNo
                ;

        String[] resultArray = {
                feature
                , String.valueOf(classification.CLICK_SCORE)
                , String.valueOf(classification.BUY_SCORE)
                , String.valueOf(classification.CLICK_POSITIVE_SCORE)
                , String.valueOf(classification.CLICK_NEGATIVE_SCORE)
                , String.valueOf(classification.BUY_POSITIVE_SCORE)
                , String.valueOf(classification.BUY_NEGATIVE_SCORE)
                , strategy
                , String.valueOf("hour:"+ExtractData.hour)
        };
        ResultToExcel.arrayToExcel(resultArray,Strategy.RECORD_FILE);
    }
}
