package me.ele.hackathon.ks;

import me.ele.hackathon.object.Algorithm;
import me.ele.hackathon.object.Result;
import org.apache.commons.math3.util.Pair;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static me.ele.hackathon.Main.*;

/**
 * Created by: sheng.ke
 * Date: 2016/10/23
 * Time: 下午9:22
 */
public class RandomForestAlgorithm extends Algorithm {
    private static final Logger log = LoggerFactory.getLogger(RandomForestAlgorithm.class);
    private SparkSession spark;

    private int treeNum = 20;
    private int depth = 30;

    public RandomForestAlgorithm(int treeNum, int depth) {
        spark = buildSparkSession();
        this.treeNum = treeNum;
        this.depth = depth;
    }


    @Override
    public String getAlgorithmName() {
        return "RandomForest_tree_" + treeNum + "_depth_" + depth;
    }

    @Override
    public Result trainOnHistoricData() {
        long start = System.currentTimeMillis();

        TrainAndTestResult clickResult = trainAndTest(datasetFile.getClickExpand(), datasetFile.getClickOrigin(),
                "click");
        TrainAndTestResult buyResult = trainAndTest(datasetFile.getBuyExpand(), datasetFile.getBuyOrigin(), "buy");


        return new Result(datasetFile, this, clickResult.getModel(), buyResult.getModel(),
                clickResult.getAccuracy(), buyResult.getAccuracy(),
                clickResult.getScore(), buyResult.getScore(),
                System.currentTimeMillis() - start, "CLICK: " + clickResult.getExtra() + ", BUY: " + buyResult.getExtra());
    }

    private TrainAndTestResult trainAndTest(String trainFile, String testFile, String modelName) {

        Dataset<Row> train = spark.read().format("libsvm").load(trainFile);
        if (IS_LOCAL_DEBUG) {
            train = train.limit(SAMPLER_LIMIT);
        }
        StringIndexerModel clickLabelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(train);
        VectorIndexerModel clickFeatureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(30)
                .fit(train);

        RandomForestClassifier rf = new RandomForestClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures")
                .setNumTrees(treeNum)
                .setMaxDepth(depth)
                ;

        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(clickLabelIndexer.labels());

        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{clickLabelIndexer, clickFeatureIndexer, rf, labelConverter});

        PipelineModel clickPipelineModel = pipeline.fit(train);


        // for test:
        Dataset<Row> test = spark.read().format("libsvm").load(testFile);

        new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(test);
        new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(20).fit(test);

        Dataset<Row> clickPredictions = clickPipelineModel.transform(test);


        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double clickAccuracy = evaluator.evaluate(clickPredictions);
        Pair<String, Double> clickScore = calculateScore(clickPredictions, "predictedLabel");

        try {
            clickPipelineModel.save(FINAL_OUTPUT_FILE_PATH + getAlgorithmName() + "_" + modelName + "_s" + clickScore
                    .getValue() + "_" + clickScore.getFirst() + ".model");
        } catch (IOException e) {
            log.error("==trainAndTest== fail to save mode, as {}", e);
        }


        return new TrainAndTestResult(clickPipelineModel, clickAccuracy, clickScore.getValue(), clickScore.getKey());
    }

    @Override
    public void outputFinalResult(String filePath, Model clickModel, Model buyModel) throws IOException {
        Dataset<Row> testDataset = spark.read().format("libsvm").load(datasetFile.getTestFile());

        Dataset<Row> clickPredictResult = predict(testDataset, clickModel);


        Dataset<Row> buyPredictResult = predict(testDataset, buyModel);


        List<Row> clickList = clickPredictResult.collectAsList();
        List<Row> buyList = buyPredictResult.collectAsList();


        List<String> ids = readTestIds();

// Use relative path for Unix systems
        File f = new File(filePath);

        f.getParentFile().mkdirs();
        f.createNewFile();

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(filePath)))) {
            for (int i = 0; i < ids.size(); i++) {
                pw.append(ids.get(i)).append("\t");
                pw.append((clickList.get(i).getAs("predictedLabel")).equals("1.0") ? "1" : "0").append("\t");
                pw.append((buyList.get(i).getAs("predictedLabel")).equals("1.0") ? "1" : "0").append("\n");
            }
        }
    }

    private static Dataset<Row> predict(Dataset<Row> testDataset, Model model) {
        Dataset<Row> buyPredictions = model.transform(testDataset);
//        buyPredictions.select("predictedLabel", "label", "features").show(5);

        return buyPredictions.select("predictedLabel");
    }
}
