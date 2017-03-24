package me.ele.hackathon.object;

/**
 * Created by solomon on 16/10/24.
 */
public class Record {

    private String feature;

    private int count = 1;

    private double clickScore;
    private double clickPositiveRate;
    private double clickNegativeRate;

    private double buyScore;
    private double buyPositiveRate;
    private double buyNegativeRate;

    private String strategy;

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public double getClickScore() {
        return clickScore;
    }

    public void setClickScore(double clickScore) {
        this.clickScore = clickScore;
    }

    public double getClickPositiveRate() {
        return clickPositiveRate;
    }

    public void setClickPositiveRate(double clickPositiveRate) {
        this.clickPositiveRate = clickPositiveRate;
    }

    public double getClickNegativeRate() {
        return clickNegativeRate;
    }

    public void setClickNegativeRate(double clickNegativeRate) {
        this.clickNegativeRate = clickNegativeRate;
    }

    public double getBuyScore() {
        return buyScore;
    }

    public void setBuyScore(double buyScore) {
        this.buyScore = buyScore;
    }

    public double getBuyPositiveRate() {
        return buyPositiveRate;
    }

    public void setBuyPositiveRate(double buyPositiveRate) {
        this.buyPositiveRate = buyPositiveRate;
    }

    public double getBuyNegativeRate() {
        return buyNegativeRate;
    }

    public void setBuyNegativeRate(double buyNegativeRate) {
        this.buyNegativeRate = buyNegativeRate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
