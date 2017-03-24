package me.ele.hackathon.solon;

import me.ele.hackathon.data.ExtractData;
import me.ele.hackathon.data.LibSvmDataBuilder;
import me.ele.hackathon.solon.classifiction.RandomForestClassification;
import me.ele.hackathon.solon.handler.DataHandler;
import me.ele.hackathon.solon.prediction.Prediction;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by solomon on 16/10/25.
 * 根据feature进行排列组合跑分数
 */
public class FeatureSelectPredictor {

    public static void main(String[] args) throws IOException {

        ExtractData.main(args);

        /* 请先设置DataHandler 里面的两个路径*/
        long start = System.currentTimeMillis();
        int count = LibSvmDataBuilder.featureCollect.size();
        for(int i=0;i<count;i++){
            String[] strArray = LibSvmDataBuilder.featureCollect.get(i).split(",");
            LibSvmDataBuilder.selected_feature_list = Arrays.asList(strArray);
            Prediction.predictWithoutClick(new RandomForestClassification());
            Prediction.record.setCount(i);
        }
        long end = System.currentTimeMillis();
        System.out.println(" time cost:"+(end-start)/1000);

    }
}
