package me.ele.hackathon.data;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.feature.ChiSqSelector;
import org.apache.spark.mllib.feature.ChiSqSelectorModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;

import java.util.List;

/**
 * Created by solomon on 16/10/18.
 */
public class FeatureSelector {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaMultilayerPerceptronClassifierExample")
                .master("local[4]")
                .config("spark.driver.memory", "2G")
                .config("spark.executor.memory", "1G")
                .getOrCreate();

        selectFeatureByChiSq(spark.sparkContext());

        long end = System.currentTimeMillis();
        System.out.println("==========");
        System.out.println("duration: " + (end-start)/1000);
        System.out.println("==========");

    }

    public static void selectFeatureByChiSq(SparkContext sparkContext){
        String trainFilePath = "/Users/solomon/Desktop/hackathon/select/click_train.txt";
        JavaRDD<LabeledPoint> points = MLUtils.loadLibSVMFile(sparkContext, trainFilePath).toJavaRDD().cache();

        // Discretize data in 16 equal bins since ChiSqSelector requires categorical features
        // Although features are doubles, the ChiSqSelector treats each unique value as a category
        JavaRDD<LabeledPoint> discretizedData = points.map(
                new Function<LabeledPoint, LabeledPoint>() {
                    @Override
                    public LabeledPoint call(LabeledPoint lp) {
                        final double[] discretizedFeatures = new double[lp.features().size()];
                        for (int i = 0; i < lp.features().size(); ++i) {
//                            discretizedFeatures[i] = Math.floor(lp.features().apply(i)/16);
                            discretizedFeatures[i] = lp.features().apply(i);
                        }
                        return new LabeledPoint(lp.label(), Vectors.dense(discretizedFeatures));
                    }
                }
        );

        // Create ChiSqSelector that will select top n features
        ChiSqSelector selector = new ChiSqSelector(9);
        // Create ChiSqSelector model (selecting features)
        final ChiSqSelectorModel transformer = selector.fit(discretizedData.rdd());
            // Filter the top 50 features from each feature vector
        JavaRDD<LabeledPoint> filteredData = discretizedData.map(
                new Function<LabeledPoint, LabeledPoint>() {
                    @Override
                    public LabeledPoint call(LabeledPoint lp) {
                        return new LabeledPoint(lp.label(), transformer.transform(lp.features()));
                    }
                }
        );
        List<LabeledPoint> labeledPointList = filteredData.collect();
        System.out.println(" \n ************** " + labeledPointList.get(0).features().toString());
    }
}
