package me.ele.hackathon.logistic;

import me.ele.hackathon.solon.classifiction.BaseClassification;
import me.ele.hackathon.solon.constant.Strategy;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

/**
 * 逻辑回归
 *
 * @author oukailiang
 * @create 2016-10-22 下午12:50
 */

public class LogicRegression {
    public static final String PATH_PREIX = "/Users/oukailiang/Downloads/hackathon/spark_data_local/";
    public static final String CLICK_PREDICT_FILE = "/Users/oukailiang/Downloads/hackathon/spark_prediction/click_predict.csv";
    public static final String BUY_PREDICT_FILE = "/Users/oukailiang/Downloads/hackathon/spark_prediction/buy_predict.csv";

    public static String CLICK_TRAINFILE_PATH = PATH_PREIX + "click_train.txt";
    public static String CLICK_TESTFILE_PATH = PATH_PREIX + "click_test.txt";
    public static String BUY_TRAINFILE_PATH = PATH_PREIX + "buy_train.txt";
    public static String BUY_TESTFILE_PATH = PATH_PREIX + "buy_test.txt";
    //
    public static final int SAMPLER_LIMIT = 10000; //当本地测试开发时，限制样本数量
    public static SparkSession spark;

    static {
        spark = SparkSession
                .builder()
                .appName("JavaMultilayerPerceptronClassifierExample")
                .master("local[4]")
                .config("spark.driver.memory", "2G")
                .config("spark.executor.memory", "1G")
                .getOrCreate();
    }

    public static void main(String[] args) throws Exception {
       // predictClick(spark);
        predictBuy(spark);

    }

    public static void predictClick(SparkSession spark) throws Exception {
        Dataset<Row> train = spark.read().format("libsvm").load(CLICK_TRAINFILE_PATH);
        Dataset<Row> test = spark.read().format("libsvm").load(CLICK_TESTFILE_PATH);

        //train.limit(SAMPLER_LIMIT);
        Dataset<Row>[] clickSplits = train.randomSplit(new double[]{0.7, 0.3}, 1234L);
        Dataset<Row> clickTrain = clickSplits[0];
        Dataset<Row> clickTest = clickSplits[1];

        //逻辑回归
        LogisticRegression logisticRegression = new LogisticRegression().setMaxIter(20).setElasticNetParam(0.8);
        LogisticRegressionModel model = logisticRegression.fit(train);
        //线性回归
        //LinearRegression linearRegression = new LinearRegression();
        //LinearRegressionModel model = linearRegression.fit(buyTest);
        // compute accuracy on the test set
        Dataset<Row> result = model.transform(test);

        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        predictionAndLabels.show(30);
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        predictionAndLabels.write().mode(SaveMode.Overwrite).format("com.databricks.spark.csv")
                .save(CLICK_PREDICT_FILE);

        double accu = evaluator.evaluate(predictionAndLabels);
        long totalPositiveCount = predictionAndLabels.filter(col("label").equalTo("1.0")).count();
        long totalNegtiveCount = predictionAndLabels.filter(col("label").equalTo("0.0")).count();

        long positiveRight = predictionAndLabels.filter(col("prediction").equalTo("1.0")).filter(col("label").equalTo
                ("1.0")).count();
        long negativeRight = predictionAndLabels.filter(col("prediction").equalTo("0.0")).filter(col("label").equalTo
                ("0.0")).count();

        double score = ((double) positiveRight / totalPositiveCount) * ((double) negativeRight / totalNegtiveCount) * 100;
        System.out.println("==========");
        System.out.println("click Accuracy = " + accu);
        System.out.println("click Score = " + score);
        System.out.println("==========");
    }


    public static void predictBuy(SparkSession spark) throws Exception {
        Dataset<Row> train = spark.read().format("libsvm").load(BUY_TRAINFILE_PATH);
        Dataset<Row> test = spark.read().format("libsvm").load(BUY_TESTFILE_PATH);

        //train.limit(SAMPLER_LIMIT);
        Dataset<Row>[] buySplits = train.randomSplit(new double[]{0.7, 0.3}, 1234L);
        Dataset<Row> buyTrain = buySplits[0];
        Dataset<Row> buyTest = buySplits[1];

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
        Dataset<Row> result = model.transform(test);

        Dataset<Row> predictionAndLabels = result.select("predictedLabel", "prediction", "label");
        predictionAndLabels.show(10);
//        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
//                .setMetricName("accuracy");
//
//        predictionAndLabels.write().mode(SaveMode.Overwrite).format("com.databricks.spark.csv")
//                .save(BUY_PREDICT_FILE);

//        double accu = evaluator.evaluate(predictionAndLabels);
        long totalPositiveCount = predictionAndLabels.filter(col("label").equalTo("1.0")).collectAsList().size();
        long totalNegtiveCount = predictionAndLabels.filter(col("label").equalTo("0.0")).collectAsList().size();

        long positiveRight = predictionAndLabels.filter(col("prediction").equalTo("1.0")).filter(col("label").equalTo
                ("1.0")).collectAsList().size();
        long negativeRight = predictionAndLabels.filter(col("prediction").equalTo("0.0")).filter(col("label").equalTo
                ("0.0")).collectAsList().size();

        double score = ((double) positiveRight / totalPositiveCount) * ((double) negativeRight / totalNegtiveCount) * 100;
        System.out.println("==========");
//        System.out.println("buy Accuracy = " + accu);
        System.out.println("buy Score = " + score);
        System.out.println("==========");
    }
}
