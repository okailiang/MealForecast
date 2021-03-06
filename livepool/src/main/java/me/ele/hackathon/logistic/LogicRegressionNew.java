package me.ele.hackathon.logistic;

import me.ele.hackathon.solon.classifiction.BaseClassification;
import me.ele.hackathon.solon.constant.Strategy;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.spark.sql.functions.col;

/**
 * @author oukailiang
 * @create 2016-10-26 下午8:12
 */

public class LogicRegressionNew extends BaseClassification {
    public void startPerdict() throws Exception {
        long start = System.currentTimeMillis();
        predictClick();
        predictBuy();
        long end = System.currentTimeMillis();
        System.out.println("==========");
        System.out.println("Score Click: " + CLICK_RES);
        System.out.println("Score B u y: " + BUY_RES);
        System.out.println("duration: " + (end - start) / 1000 / 60);
        System.out.println("==========");
    }


    public void predictClick() throws Exception {
        Dataset<Row> train = spark.read().format("libsvm").load(CLICK_TRAINFILE_PATH);
        Dataset<Row> test = spark.read().format("libsvm").load(CLICK_TESTFILE_PATH);
        
        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(train);
        // Automatically identify categorical features, and index them.
        // Set maxCategories so features with > 4 distinct values are treated as continuous.
        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(15)
                .fit(train);

        //逻辑回归
//        LogisticRegression logisticRegression = new LogisticRegression().setMaxIter(20).setElasticNetParam(0.8);
        LogisticRegression logisticRegression = new LogisticRegression()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures")
                .setMaxIter(20).setElasticNetParam(0.8);

        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{labelIndexer, featureIndexer, logisticRegression, labelConverter});

//        LogisticRegressionModel model = logisticRegression.fit(train);
        // Train model. This also runs the indexers.
        PipelineModel model = pipeline.fit(train);

        //线性回归
        //LinearRegression linearRegression = new LinearRegression();
        //LinearRegressionModel model = linearRegression.fit(train);
        // compute accuracy on the test set
        Dataset<Row> predictions = model.transform(test);

        predictions.select("predictedLabel", "prediction", "label").show(10);

        outputClickPredictLabel(predictions);
        if (Strategy.LOCAL) {
            MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                    .setLabelCol("indexedLabel")
                    .setPredictionCol("prediction")
                    .setMetricName("accuracy");

//            predictions.write().mode(SaveMode.Overwrite).format("com.databricks.spark.csv")
//                    .save(CLICK_PREDICT_FILE);

//            double accu = evaluator.evaluate(predictions);
            long totalPositiveCount = predictions.filter(col("label").equalTo("1.0")).count();
            long totalNegtiveCount = predictions.filter(col("label").equalTo("0.0")).count();

            long positiveRight = predictions.filter(col("predictedLabel").equalTo("1.0")).filter(col("label").equalTo
                    ("1.0")).count();
            long negativeRight = predictions.filter(col("predictedLabel").equalTo("0.0")).filter(col("label").equalTo
                    ("0.0")).count();

            CLICK_SCORE = ((double) positiveRight / totalPositiveCount) * ((double) negativeRight / totalNegtiveCount) * 100;
            CLICK_POSITIVE_SCORE = ((double) positiveRight / (double) totalPositiveCount) * 100;
            CLICK_NEGATIVE_SCORE = ((double) negativeRight / (double) totalNegtiveCount) * 100;

            System.out.println("==========");
//            System.out.println("click Accuracy = " + accu);
            System.out.println("Score = " + CLICK_SCORE + " positiveRate = " + CLICK_POSITIVE_SCORE + " negativeRate = " + CLICK_NEGATIVE_SCORE);
            System.out.println("==========");

            CLICK_RES = "Score = " + CLICK_SCORE + " positiveRate = " + CLICK_POSITIVE_SCORE + " negativeRate = " + CLICK_NEGATIVE_SCORE;
        }
    }

    public void predictBuy( ) throws Exception {
        // Load training data
        Dataset<Row> train = spark.read().format("libsvm").load(BUY_TRAINFILE_PATH);
        Dataset<Row> test = spark.read().format("libsvm").load(BUY_TESTFILE_PATH);

        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(train);
        // Automatically identify categorical features, and index them.
        // Set maxCategories so features with > 4 distinct values are treated as continuous.
        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(15)
                .fit(train);

        //逻辑回归
//        LogisticRegression logisticRegression = new LogisticRegression().setMaxIter(20).setElasticNetParam(0.8);
        LogisticRegression logisticRegression = new LogisticRegression()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures")
                .setMaxIter(20).setElasticNetParam(0.8);

        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{labelIndexer, featureIndexer, logisticRegression, labelConverter});

//        LogisticRegressionModel model = logisticRegression.fit(train);
        // Train model. This also runs the indexers.
        PipelineModel model = pipeline.fit(train);

        //线性回归
        //LinearRegression linearRegression = new LinearRegression();
        //LinearRegressionModel model = linearRegression.fit(train);
        // compute accuracy on the test set
        Dataset<Row> predictions = model.transform(test);

        predictions.select("predictedLabel", "prediction", "label").show(10);


        outputBuyPredictLabel(predictions);

        if (Strategy.LOCAL) {
            MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                    .setLabelCol("indexedLabel")
                    .setPredictionCol("prediction")
                    .setMetricName("accuracy");

//            predictions.write().mode(SaveMode.Overwrite)
//                    .format("com.databricks.spark.csv").save(BUY_PREDICT_FILE);

//            double accu = evaluator.evaluate(predictions);
            long totalPositiveCount = predictions.filter(col("label").equalTo("1.0")).count();
            long totalNegtiveCount = predictions.filter(col("label").equalTo("0.0")).count();

            long positiveRight = predictions.filter(col("predictedLabel").equalTo("1.0")).filter(col("label").equalTo
                    ("1.0")).count();
            long negativeRight = predictions.filter(col("predictedLabel").equalTo("0.0")).filter(col("label").equalTo
                    ("0.0")).count();

            BUY_SCORE = ((double) positiveRight / totalPositiveCount) * ((double) negativeRight / totalNegtiveCount) * 100;
            BUY_POSITIVE_SCORE = ((double) positiveRight / (double) totalPositiveCount) * 100;
            BUY_NEGATIVE_SCORE = ((double) negativeRight / (double) totalNegtiveCount) * 100;


            System.out.println("==========");
//            System.out.println("buy Accuracy = " + accu);
            System.out.println("Score = " + BUY_SCORE + " positiveRate = " + BUY_POSITIVE_SCORE + " negativeRate = " + BUY_NEGATIVE_SCORE);
            System.out.println("==========");
            BUY_RES = "Score = " + BUY_SCORE + " positiveRate = " + BUY_POSITIVE_SCORE + " negativeRate = " + BUY_NEGATIVE_SCORE;
        }
    }


    public static void outputClickPredictLabel(Dataset<Row> predictions) throws Exception {
        List<Row> rowList = predictions.select("predictedLabel").collectAsList();
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(CLICK_RESULT_OUTPUT_FILE)))) {
            rowList.stream().forEach(row -> {
                String s = String.valueOf(row.apply(0));
                pw.append("" + (int) Double.parseDouble(s));
                pw.append("\n");
            });
        }
    }

    public static void outputBuyPredictLabel(Dataset<Row> predictions) throws Exception {
        List<Row> rowList = predictions.select("predictedLabel").collectAsList();
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(BUY_RESULT_OUTPUT_FILE)))) {

            rowList.stream().forEach(row -> {
                String s = String.valueOf(row.apply(0));
                pw.append("" + (int) Double.parseDouble(s));
                pw.append("\n");
            });
        }
    }


    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();
        // RandomForestClassification classification = new RandomForestClassification();
        //classification.predictClick(spark);
        // classification.predictBuy(spark);
        long end = System.currentTimeMillis();
        System.out.println("==========");
        System.out.println("Score Click: " + CLICK_RES);
        System.out.println("Score B u y: " + BUY_RES);
        System.out.println("duration: " + (end - start) / 1000 / 60);
        System.out.println("==========");


    }
}
