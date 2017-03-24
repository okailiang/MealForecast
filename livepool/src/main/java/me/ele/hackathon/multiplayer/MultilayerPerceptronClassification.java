package me.ele.hackathon.multiplayer;

import me.ele.hackathon.ks.SimpleFeatureSelectorV1;
import me.ele.hackathon.data.Constant;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;


public class MultilayerPerceptronClassification {

    public static final String CLICK_PREDICT_FILE = "/Users/solomon/Desktop/hackathon/spark_prediction/click_predict.csv";
    public static final String BUY_PREDICT_FILE = "/Users/solomon/Desktop/hackathon/spark_prediction/buy_predict.csv";

    private static int SAMPLER_LIMIT = 1 * 10000;

    private static int FIRST_LAYER = 20;
    private static int SECOND_LAYER = 10;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaMultilayerPerceptronClassifierExample")
                .master("local[4]")
                .config("spark.driver.memory", "2G")
                .config("spark.executor.memory", "1G")
                .getOrCreate();

        double clickAccu = trainClick(spark);
        double buyAccu = trainBuy(spark);

//        double bayesClick = nativeBayesClick(spark);
//        double bayesBuy = nativeBayesBuy(spark);
        long end = System.currentTimeMillis();
        System.out.println("==========");
        System.out.println("MP Click: " + clickAccu);
        System.out.println("MP Buy: " + buyAccu);
//        System.out.println("Naive Bayes Click: " + bayesClick);
//        System.out.println("Naive Bayes Buy: " + bayesBuy);
        System.out.println("duration: " + (end-start)/1000/60);
        System.out.println("==========");


    }

    private static double trainClick(SparkSession spark) {
        String trainFilePath = Constant.CLICK_TRAN_FIlE;
        String testFilePath = Constant.CLICK_TEST_FIlE;
        Dataset<Row> train =  spark.read().format("libsvm").load(trainFilePath);
        Dataset<Row> test =  spark.read().format("libsvm").load(testFilePath);

        // Load training data
//        String path = “”；
//        DatasetFile<Row> dataFrame = spark.read().format("libsvm").load(path);
//        dataFrame = dataFrame.limit(SAMPLER_LIMIT);
        // Split the data into train and test
//        DatasetFile<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
//        DatasetFile<Row> train = splits[0];
//        DatasetFile<Row> test = splits[1];
        // specify layers for the neural network:
// input layer of size 4 (features), two intermediate of size 5 and 4
// and output of size 3 (classes)
        int[] layers = new int[]{6, FIRST_LAYER, 2};
        // create the trainer and set its parameters
        MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
                .setLayers(layers)
                .setBlockSize(1000)
                .setSeed(1234L)
                .setMaxIter(1000);
        // train the model
        MultilayerPerceptronClassificationModel model = trainer.fit(train);
        // compute accuracy on the test set
        Dataset<Row> result = model.transform(test);

        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
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

        double score = ((double) positiveRight/totalPositiveCount) * ((double)negativeRight / totalNegtiveCount) * 100;
        System.out.println("==========");
        System.out.println("click Accuracy = " + accu);
        System.out.println("click Score = " + score);
        System.out.println("==========");

        return accu;
    }

    private static double trainBuy(SparkSession spark) {
        // Load training data
        String trainFilePath = Constant.BUY_TRAN_FILE;
        String testFilePath = Constant.BUY_TEST_FILE;
        Dataset<Row> train =  spark.read().format("libsvm").load(trainFilePath);
        Dataset<Row> test =  spark.read().format("libsvm").load(testFilePath);

//        String path ="";
//        DatasetFile<Row> dataFrame = spark.read().format("libsvm").load(path);
//        dataFrame = dataFrame.limit(SAMPLER_LIMIT);
//        // Split the data into train and test
//        DatasetFile<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
//        DatasetFile<Row> train = splits[0];
//        DatasetFile<Row> test = splits[1];
        // specify layers for the neural network:
// input layer of size 4 (features), two intermediate of size 5 and 4
// and output of size 3 (classes)
        int[] layers = new int[]{6, FIRST_LAYER, 2};
        // create the trainer and set its parameters
        MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
                .setLayers(layers)
                .setBlockSize(128)
                .setSeed(1234L)
                .setMaxIter(100);
        // train the model
        MultilayerPerceptronClassificationModel model = trainer.fit(train);
        // compute accuracy on the test set
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        predictionAndLabels.write().mode(SaveMode.Overwrite).format("com.databricks.spark.csv").save
                (BUY_PREDICT_FILE);

        double accu = evaluator.evaluate(predictionAndLabels);
        long totalPositiveCount = predictionAndLabels.filter(col("label").equalTo("1.0")).count();
        long totalNegtiveCount = predictionAndLabels.filter(col("label").equalTo("0.0")).count();

        long positiveRight = predictionAndLabels.filter(col("prediction").equalTo("1.0")).filter(col("label").equalTo
                ("1.0")).count();
        long negativeRight = predictionAndLabels.filter(col("prediction").equalTo("0.0")).filter(col("label").equalTo
                ("0.0")).count();

        double score = ((double) positiveRight/totalPositiveCount) * ((double)negativeRight / totalNegtiveCount) * 100;
        System.out.println("==========");
        System.out.println("buy Accuracy = " + accu);
        System.out.println("buy Score = " + score);
        System.out.println("==========");
        return accu;
    }


    public static double nativeBayesClick(SparkSession spark) {
        // Load training data
        String path = SimpleFeatureSelectorV1.CLICK_OUTPUT_File;
        Dataset<Row> dataFrame = spark.read().format("libsvm").load(path);

        dataFrame = dataFrame.limit(SAMPLER_LIMIT);

// Split the data into train and test
        Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

// create the trainer and set its parameters
        NaiveBayes nb = new NaiveBayes();
// train the model
        NaiveBayesModel model = nb.fit(train);
// compute accuracy on the test set
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");
        System.out.println("nativeBayes Accuracy = " + evaluator.evaluate(predictionAndLabels));

        return evaluator.evaluate(predictionAndLabels);
    }


    public static double nativeBayesBuy(SparkSession spark) {
        // Load training data
        String path = SimpleFeatureSelectorV1.BUY_OUTPUT_File;
        Dataset<Row> dataFrame = spark.read().format("libsvm").load(path);

        dataFrame = dataFrame.limit(SAMPLER_LIMIT);

// Split the data into train and test
        Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

// create the trainer and set its parameters
        NaiveBayes nb = new NaiveBayes();
// train the model
        NaiveBayesModel model = nb.fit(train);
// compute accuracy on the test set
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");
        System.out.println("nativeBayes Accuracy = " + evaluator.evaluate(predictionAndLabels));

        return evaluator.evaluate(predictionAndLabels);
    }
}
