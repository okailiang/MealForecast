package me.ele.hackathon.object;

import org.apache.spark.ml.Model;

/**
 * Created by: sheng.ke
 * Date: 2016/10/19
 * Time: 下午10:53
 */
public class Result {

    DatasetFile datasetFile;

    Algorithm algorithm;

    Model clickModel;
    Model buyModel;

    double clickAccuracy;
    double buyAccuracy;
    double clickScore;
    double buyScore;

    long runningTime;

    String extraInfo;

    public Result(DatasetFile datasetFile, Algorithm algorithm, Model clickModel, Model buyModel,
                  double clickAccuracy, double buyAccuracy, double clickScore, double buyScore, long runningTime,
                  String extraInfo) {
        this.datasetFile = datasetFile;
        this.algorithm = algorithm;
        this.clickModel = clickModel;
        this.buyModel = buyModel;
        this.clickAccuracy = clickAccuracy;
        this.buyAccuracy = buyAccuracy;
        this.clickScore = clickScore;
        this.buyScore = buyScore;
        this.runningTime = runningTime;
        this.extraInfo = extraInfo;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public DatasetFile getDatasetFile() {
        return datasetFile;
    }

    public void setDatasetFile(DatasetFile datasetFile) {
        this.datasetFile = datasetFile;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Model getClickModel() {
        return clickModel;
    }

    public void setClickModel(Model clickModel) {
        this.clickModel = clickModel;
    }

    public Model getBuyModel() {
        return buyModel;
    }

    public void setBuyModel(Model buyModel) {
        this.buyModel = buyModel;
    }

    public double getClickAccuracy() {
        return clickAccuracy;
    }

    public void setClickAccuracy(double clickAccuracy) {
        this.clickAccuracy = clickAccuracy;
    }

    public double getBuyAccuracy() {
        return buyAccuracy;
    }

    public void setBuyAccuracy(double buyAccuracy) {
        this.buyAccuracy = buyAccuracy;
    }

    public double getClickScore() {
        return clickScore;
    }

    public void setClickScore(double clickScore) {
        this.clickScore = clickScore;
    }

    public double getBuyScore() {
        return buyScore;
    }

    public void setBuyScore(double buyScore) {
        this.buyScore = buyScore;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    @Override
    public String toString() {
        return "Result{" +
                "datasetFile=" + datasetFile +
                ", algorithm=" + algorithm +
                ", clickModel=" + clickModel +
                ", buyModel=" + buyModel +
                ", clickAccuracy=" + clickAccuracy +
                ", buyAccuracy=" + buyAccuracy +
                ", clickScore=" + clickScore +
                ", buyScore=" + buyScore +
                ", runningTime=" + runningTime +
                ", extraInfo='" + extraInfo + '\'' +
                '}';
    }
}

