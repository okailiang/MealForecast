package me.ele.hackathon.object;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.ele.hackathon.ks.SimpleFeatureSelectorV1;
import org.apache.commons.math3.util.Pair;
import org.apache.spark.ml.Model;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.apache.spark.sql.functions.col;

/**
 * Created by: sheng.ke
 * Date: 2016/10/19
 * Time: 下午10:52
 */
public abstract class Algorithm {

    protected DatasetFile datasetFile;

    public void setDatasetFile(DatasetFile datasetFile) {
        this.datasetFile = datasetFile;
    }

    /**
     * predictionAndLabels 必须包含"prediction", "label"的row
     */
    public Pair<String, Double> calculateScore(Dataset<Row> predictionAndLabels) {
        return calculateScore(predictionAndLabels, "prediction");
    }

    public Pair<String, Double> calculateScore(Dataset<Row> predictionAndLabels, String predictionColumnName) {
        long totalPositiveCount = predictionAndLabels.filter(col("label").equalTo("1.0")).count();
        long totalNegtiveCount = predictionAndLabels.filter(col("label").equalTo("0.0")).count();

        long positiveRight = predictionAndLabels.filter(col(predictionColumnName).equalTo("1.0")).filter(col("label").equalTo
                ("1.0")).count();
        long negativeRight = predictionAndLabels.filter(col(predictionColumnName).equalTo("0.0")).filter(col("label").equalTo
                ("0.0")).count();

        String s = "PR: " + (double) positiveRight / totalPositiveCount + ", NR: " + (double) negativeRight /
                totalNegtiveCount;


        double score = ((double) positiveRight / totalPositiveCount) * ((double) negativeRight / totalNegtiveCount) * 100;
        return new Pair<String, Double>(s, score);
    }

    public abstract String getAlgorithmName();

    public abstract Result trainOnHistoricData();

    public abstract void outputFinalResult(String filePath, Model clickModel, Model buyModel) throws IOException;

    public List<String> readTestIds() throws IOException {
        String testSourceFile = SimpleFeatureSelectorV1.TEST_INFO_SOURCE_FILE;
        List<String> ids = Lists.newArrayList();

        BufferedReader in = new BufferedReader(new FileReader(testSourceFile));

        String line = in.readLine();    // ignore first line
        while ((line = in.readLine()) != null) {

            List<String> list = Splitter.on("\t").splitToList(line);
            String logId = list.get(0);
            ids.add(logId);
        }
        return ids;
    }
}
