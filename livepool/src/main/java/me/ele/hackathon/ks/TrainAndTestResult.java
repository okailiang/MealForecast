package me.ele.hackathon.ks;

import org.apache.spark.ml.Model;

/**
 * Created by: sheng.ke
 * Date: 2016/10/28
 * Time: 上午10:35
 */
public class TrainAndTestResult {
    Model model;
    double accuracy;
    double score ;

    String extra;

    public TrainAndTestResult(Model model, double accuracy, double score, String extra) {
        this.model = model;
        this.accuracy = accuracy;
        this.score = score;
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    public Model getModel() {
        return model;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getScore() {
        return score;
    }
}