package me.ele.hackathon;

import com.google.common.collect.Lists;
import me.ele.hackathon.ks.MultilayerPerceptronAlgorithm;
import me.ele.hackathon.ks.RandomForestAlgorithm;
import me.ele.hackathon.ks.SimpleFeatureSelectorV51;
import me.ele.hackathon.object.Algorithm;
import me.ele.hackathon.object.DatasetFile;
import me.ele.hackathon.object.Result;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by: sheng.ke
 * Date: 2016/10/19
 * Time: 下午10:46
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

//    // local dev
//    public static  String FINAL_OUTPUT_FILE_PATH = "/Users/jacob/Desktop/output/";//结果算分文件
//    public static  String SOURCE_FILE_DIR = "/Users/jacob/Desktop/E_data/"; // 放原始数据的文件夹
//    public static  String FEATURE_DATA_DIR = "/Users/jacob/Desktop/datasetFile/";
//    public static final boolean IS_LOCAL_DEBUG = false; // 本地测试开发则置为true


    // on prod
    public static String FINAL_OUTPUT_FILE_PATH = "/home/sheng.ke/output/";//结果算分文件
    public static final String SOURCE_FILE_DIR = "/home/sheng.ke/E_data/"; // 放原始数据的文件夹
    public static final String FEATURE_DATA_DIR = "/home/sheng.ke/datasetFile/";
    public static final boolean IS_LOCAL_DEBUG = false; // 本地测试开发则置为true

    public static final int SAMPLER_LIMIT = 100; //当本地测试开发时，限制样本数量

    public static SparkSession buildSparkSession() {
        return SparkSession.builder().appName("ETrace-Alert")
                .master("local[4]")
                .config("spark.driver.memory", "3G")
                .config("spark.executor.memory", "2G")
                .getOrCreate();
    }


    public static void main(String[] args) throws IOException {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        FINAL_OUTPUT_FILE_PATH = FINAL_OUTPUT_FILE_PATH + day + "_" + hour + "." + minute + "/";
        //just log
        outputLocalDebug();

        List<DatasetFile> datasetFiles = selectDataset();
        List<Algorithm> algorithms = selectAlgorithm();

        int i = 1;
        for (DatasetFile datasetFile : datasetFiles) {
            for (Algorithm algorithm : algorithms) {
                log.warn("== [{}/{}] Going to Run: algorithm [{}] on dataset [{}]",
                        i++, datasetFiles.size() * algorithms.size(),
                        algorithm.getAlgorithmName(), datasetFile.getDatasetName());
            }
        }

        List<Result> results = Lists.newArrayList();
        for (DatasetFile datasetFile : datasetFiles) {
            for (Algorithm algorithm : algorithms) {
                algorithm.setDatasetFile(datasetFile);
                if (algorithm instanceof MultilayerPerceptronAlgorithm) {
                    ((MultilayerPerceptronAlgorithm) algorithm).setFeatureNum(datasetFile.getFeatureNum());
                }
                log.warn("==Running Dataset: {}, Algorithm: {}", datasetFile.getDatasetName(), algorithm.getAlgorithmName());
                Result result = null;
                try {
                    result = algorithm.trainOnHistoricData();

                    String outputFile = FINAL_OUTPUT_FILE_PATH + algorithm.getAlgorithmName() + "_c" + result.getClickScore()
                            + "_b" + result.getBuyScore() + "_result.txt";
                    algorithm.outputFinalResult(outputFile, result.getClickModel(), result.getBuyModel());
                } catch (Exception e) {
                    log.error("==Running Failed, as {}", e);
                }

                log.warn("==One Turn Done: Dataset: {}, Algorithm: {}, \n\tResult: {}==",
                        datasetFile.getDatasetName(), algorithm.getAlgorithmName(), result);
                results.add(result);
            }
        }

        Result bestResult = outputResultsAndSelectBest(results);
        if (bestResult != null) {
            String bestOutputFile = FINAL_OUTPUT_FILE_PATH + "BEST_" + bestResult.getAlgorithm().getAlgorithmName() +
                    "_result.txt";
            bestResult.getAlgorithm().outputFinalResult(bestOutputFile, bestResult.getClickModel(), bestResult.getBuyModel());
            log.warn("==Output Best: `result.txt` to {}", bestOutputFile);
        } else {
            log.error("==No BestResult!! maybe no datasetfile or algorithm");
        }
        outputLocalDebug();
    }

    private static void outputLocalDebug() {
        log.warn("==输出结果文件： {}", FINAL_OUTPUT_FILE_PATH);

        if (IS_LOCAL_DEBUG) {
            log.warn("==处于本地开发模式，对数据集有删减，仅取{}个样本，勿用于最终结果==", SAMPLER_LIMIT);
        }
    }

    private static List<DatasetFile> selectDataset() {
//        DatasetFile v32_10_60 = SimpleFeatureSelectorV32.buildDataset(10, 60, false);
//        DatasetFile v22_10_60 = SimpleFeatureSelectorV22.buildDataset(10, 60, false);
//        DatasetFile v32_15_50 = SimpleFeatureSelectorV32.buildDataset(15, 50, false);

//        DatasetFile v32_15_120 = SimpleFeatureSelectorV32.buildDataset(15, 120, true);

//        DatasetFile v41 = SimpleFeatureSelectorV41.buildDataset(10, 60, true);
//        DatasetFile v42 = SimpleFeatureSelectorV42.buildDataset(false);
//        DatasetFile v43 = SimpleFeatureSelectorV43.buildDataset(false);
        DatasetFile v51_20_120 = SimpleFeatureSelectorV51.buildDataset(true);
        DatasetFile v51_25_140 = SimpleFeatureSelectorV51.buildDataset(true, 25, 140);
        DatasetFile v51_15_100 = SimpleFeatureSelectorV51.buildDataset(true, 15, 100);

        return Lists.newArrayList(v51_20_120, v51_15_100, v51_25_140);
    }

    private static List<Algorithm> selectAlgorithm() {
//        return Lists.newArrayList(new RandomForestAlgorithm(15, 20));
        List<Algorithm>  list = Lists.newArrayList(
                new RandomForestAlgorithm(10, 15)
                , new RandomForestAlgorithm(10, 20)
                , new RandomForestAlgorithm(10, 25)
                , new RandomForestAlgorithm(10, 30)
                , new RandomForestAlgorithm(15, 15)
                , new RandomForestAlgorithm(15, 20)
                , new RandomForestAlgorithm(15, 25)
                , new RandomForestAlgorithm(15, 30)
                , new RandomForestAlgorithm(20, 15)
                , new RandomForestAlgorithm(20, 20)
                , new RandomForestAlgorithm(20, 25)
                , new RandomForestAlgorithm(20, 30)
                , new RandomForestAlgorithm(30, 15)
                , new RandomForestAlgorithm(30, 20)
                , new RandomForestAlgorithm(30, 25)
                , new RandomForestAlgorithm(30, 30)
        );
//
//        List<Algorithm> list = Lists.newArrayList(
//                new MultilayerPerceptronAlgorithm(Lists.newArrayList(4))
//                , new MultilayerPerceptronAlgorithm(Lists.newArrayList(8))
//                , new MultilayerPerceptronAlgorithm(Lists.newArrayList(2, 2))
//                , new MultilayerPerceptronAlgorithm(Lists.newArrayList(4, 2))
//                , new MultilayerPerceptronAlgorithm(Lists.newArrayList(2, 4))
//
//        );
        Collections.reverse(list);
        return list;
    }

    private static Result outputResultsAndSelectBest(List<Result> results) {

        log.warn("==outputResultsAndSelectBest== FINALLY DONE......");
        log.warn("==outputResultsAndSelectBest== FINALLY DONE......");

        Result best = null;
        double bestScore = 0;
        for (Result result : results) {
            double score = calculateFinalScore(result.getClickScore(), result.getBuyScore());
            if (best == null) {
                best = result;
                bestScore = score;
            } else {
                if (score > bestScore) {
                    best = result;
                    bestScore = score;
                }
            }
            log.warn("==Result== Score: [{}]\n" +
                            "\tAlgorithm: {}\tdsName: {}\tts: {}(second)\n" +
                            "\tclickScore: {}\tClickAccu: {}\n" +
                            "\tbuyScore: {}\t buyAccu: {}\n" +
                            "\textra: {}.",
                    score, result.getAlgorithm().getAlgorithmName(), result.getDatasetFile().getDatasetName(), result.getRunningTime() / 1000,
                    result.getClickScore(), result.getClickAccuracy(),
                    result.getBuyScore(), result.getBuyAccuracy(),
                    result.getExtraInfo());
        }
        log.warn("==outputResultsAndSelectBest== FINALLY DONE......");
        log.warn("==outputResultsAndSelectBest== FINALLY DONE......");
        log.warn("==Best Result==  Score: [{}] " +
                        "\tAlgorithm:{}\tdsName: {}\tts: {}(second)\n" +
                        "\tclickScore: {}\tClickAccu: {}\n" +
                        "\tbuyScore: {}\t buyAccu: {}\n" +
                        "\textra: {}.",
                bestScore, best.getAlgorithm().getAlgorithmName(), best.getDatasetFile().getDatasetName(), best.getRunningTime() / 1000,
                best.getClickScore(), best.getClickAccuracy(),
                best.getBuyScore(), best.getBuyAccuracy(),
                best.getExtraInfo());
        return best;
    }

    private static double calculateFinalScore(double click, double buy) {
        return 0.66 * click + 0.34 * buy;
    }
}
