package me.ele.hackathon.ks;

import com.google.common.collect.Lists;
import me.ele.hackathon.object.Algorithm;
import me.ele.hackathon.object.Result;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark_project.guava.base.Joiner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static me.ele.hackathon.Main.*;

/**
 * Created by: sheng.ke
 * Date: 2016/10/19
 * Time: 下午11:20
 */
public class MultilayerPerceptronAlgorithm extends Algorithm {

    private static final Logger log = LoggerFactory.getLogger(MultilayerPerceptronAlgorithm.class);
    private SparkSession spark;

    private List<Integer> layers;


    private int featureNum = -1;

    public MultilayerPerceptronAlgorithm(List<Integer> layers) {
        this.layers = layers;
        spark = buildSparkSession();
    }

    public MultilayerPerceptronAlgorithm(int featureNum) {
        this.featureNum = featureNum;
        spark = buildSparkSession();
    }

    @Override
    public String getAlgorithmName() {
        return "MultilayerPerceptron_F_" + featureNum + "_L_" + Joiner.on("_").join(layers);
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
        // train CLICK
        Dataset<Row> train = spark.read().format("libsvm").load(trainFile);
        if (IS_LOCAL_DEBUG) {
            train = train.limit(SAMPLER_LIMIT);
        }

        int[] clickLayers = buildLayer();

        // create the trainer and set its parameters
        MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
                .setLayers(clickLayers)
                .setBlockSize(1000)
                .setSeed(1234L)
                .setMaxIter(150);
        // train the model
        MultilayerPerceptronClassificationModel clickModel = trainer.fit(train);

        Dataset<Row> test = spark.read().format("libsvm").load(testFile);
        Dataset<Row> clickResult = clickModel.transform(test);

        Dataset<Row> predictionAndLabels = clickResult.select("prediction", "label");
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy");

        double clickAccuracy = evaluator.evaluate(predictionAndLabels);
        Pair<String, Double> clickScore = calculateScore(predictionAndLabels);

        try {
            clickModel.save(FINAL_OUTPUT_FILE_PATH + getAlgorithmName() + "_" + modelName + "_s" + clickScore.getValue() + "_" + clickScore.getFirst()+ ".model");
        } catch (IOException e) {
            log.error("==trainAndTest== fail to save mode, as {}", e);
        }

        return new TrainAndTestResult(clickModel, clickAccuracy, clickScore.getValue(), clickScore.getKey());
    }


    private int[] buildLayer() {
        List<Integer> list = Lists.newArrayList();
        list.add(featureNum);
        list.addAll(layers);
        list.add(2);

        return ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
    }

    @Override
    public void outputFinalResult(String outputFilePath, Model clickModel, Model buyModel) throws IOException {
        Dataset<Row> testDataset = spark.read().format("libsvm").load(datasetFile.getTestFile());

        Dataset<Row> clickPredictResult = predict(testDataset, clickModel);
        Dataset<Row> buyPredictResult = predict(testDataset, buyModel);


        List<Row> clickList = clickPredictResult.collectAsList();
        List<Row> buyList = buyPredictResult.collectAsList();


        List<String> ids = readTestIds();

        File f = new File(outputFilePath);

        f.getParentFile().mkdirs();
        f.createNewFile();

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFilePath)))) {
            for (int i = 0; i < ids.size(); i++) {

                pw.append(ids.get(i)).append("\t");
                pw.append((clickList.get(i).getAs("prediction")).equals(1D) ? "1" : "0").append("\t");
                pw.append((buyList.get(i).getAs("prediction")).equals(1D) ? "1" : "0").append("\n");
            }
        }
    }

    private static Dataset<Row> predict(Dataset<Row> testDataset, Model model) {
        Dataset<Row> result = model.transform(testDataset);
        Dataset<Row> predictionAndLabels = result.select("prediction");
        return predictionAndLabels;
    }

    public void setFeatureNum(int featureNum) {
        this.featureNum = featureNum;
    }
}
