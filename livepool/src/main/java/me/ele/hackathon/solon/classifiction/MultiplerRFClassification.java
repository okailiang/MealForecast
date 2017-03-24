package me.ele.hackathon.solon.classifiction;

import me.ele.hackathon.data.Constant;
import me.ele.hackathon.solon.constant.Strategy;
import me.ele.hackathon.utils.AggregateFile;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.spark.sql.functions.col;


public class MultiplerRFClassification extends BaseClassification{

    public static List<String> paramList = Arrays.asList("10,5,32");

    public void startPerdict() throws Exception {
        for(String p: paramList){
            String[] param =  p.split(",");
            TREE_NUM = Integer.parseInt(param[0]);
            TREE_DEPTH = Integer.parseInt(param[1]);
            TREE_BINS = Integer.parseInt(param[2]);
            predictClick();
            predictBuy();
        }
    }

    /**
     * 结果文件生成向量文件
     */
    public void resultToSvm(){
        String predctionResultsDir = Strategy.PATH_PREIX  + "result/click_train/";
        String click_train = Strategy.PATH_PREIX  + "click_train.txt";
        AggregateFile.manyLableFeatureToOne(predctionResultsDir, click_train);
        predctionResultsDir = Strategy.PATH_PREIX  + "result/buy_train/";
        String buy_train = Strategy.PATH_PREIX  + "buy_train.txt";
        AggregateFile.manyLableFeatureToOne(predctionResultsDir,buy_train);
        predctionResultsDir = Strategy.PATH_PREIX  + "result/click_test/";
        String click_test = Strategy.PATH_PREIX  + "click_test.txt";
        AggregateFile.manyLableFeatureToOne(predctionResultsDir,click_test);
        predctionResultsDir = Strategy.PATH_PREIX  + "result/buy_test/";
        String buy_test = Strategy.PATH_PREIX  + "buy_test.txt";
        AggregateFile.manyLableFeatureToOne(predctionResultsDir,buy_test);
    }

    private static String SVM_FILE_PATH = "";
    private static String PREDICTION_OUTPUT_PATH = "";
    private static int TREE_NUM = 10;
    private static int TREE_DEPTH = 5;
    private static int TREE_BINS = 32;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void predictClick() throws Exception {
        System.out.println("<--------------- MultiplerRFClassification predictClick ");
        //click train
        SVM_FILE_PATH = CLICK_TRAINFILE_PATH;
        PREDICTION_OUTPUT_PATH = Strategy.PATH_PREIX  + "result/click_train/click_train_result_"+sdf.format(new Date())+".txt";
        this.predict();
        //click test
        SVM_FILE_PATH = CLICK_TESTFILE_PATH;
        PREDICTION_OUTPUT_PATH = Strategy.PATH_PREIX  + "result/click_test/click_train_result_"+sdf.format(new Date())+".txt";
        this.predict();
    }

    public void predictBuy() throws Exception {
        System.out.println("<--------------- MultiplerRFClassification predictBuy ");
        //buy train
        SVM_FILE_PATH = BUY_TRAINFILE_PATH;
        PREDICTION_OUTPUT_PATH = Strategy.PATH_PREIX  + "result/buy_train/buy_train_result_"+sdf.format(new Date())+".txt";
        this.predict();
        //buy test
        SVM_FILE_PATH = BUY_TESTFILE_PATH;
        PREDICTION_OUTPUT_PATH = Strategy.PATH_PREIX  + "result/buy_test/buy_train_result_"+sdf.format(new Date())+".txt";
        this.predict();
    }

    private void predict() throws Exception{
        Dataset<Row> dataSet = spark.read().format("libsvm").load(SVM_FILE_PATH).limit(20000);

        // Index labels, adding metadata to the label column.
        // Fit on whole dataset to include all labels in index.
        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(dataSet);

        // Automatically identify categorical features, and index them.
        // Set maxCategories so features with > 4 distinct values are treated as continuous.
        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(15)
                .fit(dataSet);

        RandomForestClassifier rf = new RandomForestClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures");

        rf.setNumTrees(TREE_NUM);
        rf.setMaxDepth(TREE_DEPTH);
        rf.setMaxBins(TREE_BINS);
        System.out.println("**** seed="+rf.getSeed()+"/n");
//        rf.setMaxBins(24);

        // Convert indexed labels back to original labels.
        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        // Chain indexers and forest in a Pipeline
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{labelIndexer, featureIndexer, rf, labelConverter});

        // Train model. This also runs the indexers.
        PipelineModel model = pipeline.fit(dataSet);

        // Make predictions.
        Dataset<Row> predictions = model.transform(dataSet);

        // Select example rows to display.
        predictions.select("predictedLabel","prediction", "label", "features").show(5);

        outputPredictLabel(predictions);

        dataSet = null;
        predictions = null;
    }

    public static List<Row> rowList = null;

    /**
     * 输出结果文件 不同于其他算法 输出文件里包含predictedLabel和label，其他算法 只需 predictedLabel
     * @param predictions
     * @throws Exception
     */
    public static void outputPredictLabel(Dataset<Row> predictions) throws Exception{
        rowList = predictions.select("label","predictedLabel").collectAsList();
        System.out.println("<--------------- MultiplerRFClassification file:"+PREDICTION_OUTPUT_PATH+" rowList"+rowList.size());
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(PREDICTION_OUTPUT_PATH)))) {
            rowList.stream().forEach(row -> {
                String label = String.valueOf(row.apply(0));
                String predictedLabel = String.valueOf(row.apply(1));
                pw.append((int) Double.parseDouble(label)+ Constant.SVM_DATA_SEPERATOR + (int) Double.parseDouble(predictedLabel));
                pw.append("\n");
            });
        }
    }


}
