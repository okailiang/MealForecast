package me.ele.hackathon.ks;

import me.ele.hackathon.object.Algorithm;
import me.ele.hackathon.object.Result;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.Model;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.IOException;

import static me.ele.hackathon.Main.buildSparkSession;

/**
 * Created by: sheng.ke
 * Date: 2016/10/30
 * Time: 下午12:58
 */
public class SVMAlgorithm extends Algorithm {
    private SparkSession spark;


    public SVMAlgorithm(SparkSession spark) {
        this.spark = buildSparkSession();

    }

    @Override
    public String getAlgorithmName() {
        return "SVM";
    }

    @Override
    public Result trainOnHistoricData() {
        long start = System.currentTimeMillis();

        TrainAndTestResult clickResult = trainAndTest(datasetFile.getClickExpand(), datasetFile.getClickOrigin());
        TrainAndTestResult buyResult = trainAndTest(datasetFile.getBuyExpand(), datasetFile.getBuyOrigin());

        return new Result(datasetFile, this, clickResult.getModel(), buyResult.getModel(),
                clickResult.getAccuracy(), buyResult.getAccuracy(),
                clickResult.getScore(), buyResult.getScore(),
                System.currentTimeMillis() - start, "CLICK: " + clickResult.getExtra() + ", BUY: " + buyResult.getExtra());
    }

    private TrainAndTestResult trainAndTest(String expand, String origin) {
        JavaRDD<LabeledPoint> train = MLUtils.loadLibSVMFile(spark.sparkContext(), expand).toJavaRDD();
        train.cache();

// Run training algorithm to build the model.
        int numIterations = 100;
        final SVMModel model = SVMWithSGD.train(train.rdd(), numIterations);

// Clear the default threshold.
        model.clearThreshold();

        JavaRDD<LabeledPoint> test = MLUtils.loadLibSVMFile(spark.sparkContext(), origin).toJavaRDD();

// Compute raw scores on the test set.
        JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test.map(
                new Function<LabeledPoint, Tuple2<Object, Object>>() {
                    public Tuple2<Object, Object> call(LabeledPoint p) {
                        Double score = model.predict(p.features());
                        return new Tuple2<Object, Object>(score, p.label());
                    }
                }
        );

// Get evaluation metrics.
        BinaryClassificationMetrics metrics =
                new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));
        RDD<Tuple2<Object, Object>> xx = metrics.scoreAndLabels();
        double auROC = metrics.areaUnderROC();

        System.out.println("Area under ROC = " + auROC);
        return null;
    }

    @Override
    public void outputFinalResult(String filePath, Model clickModel, Model buyModel) throws IOException {

    }
}
