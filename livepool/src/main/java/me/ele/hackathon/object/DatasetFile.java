package me.ele.hackathon.object;

/**
 * Created by: sheng.ke
 * Date: 2016/10/19
 * Time: 下午10:47
 */
public class DatasetFile {

    String datasetName; // or called datasetFile type

    String clickOrigin;
    String clickExpand;

    String buyOrigin;
    String buyExpand;

    String testFile;

    int featureNum;

    @Deprecated
    public DatasetFile(String datasetName, String clickExpand, String buytTrainFile, String testFile, int featureNum) {
        this.datasetName = datasetName;
        this.clickExpand = clickExpand;
        this.buyExpand = buytTrainFile;
        this.testFile = testFile;
        this.featureNum = featureNum;
    }

    public DatasetFile(String datasetName, String clickExpand, String clickOrigin,
                       String buyExpand, String buyOrigin, String testFile, int featureNum) {
        this.datasetName = datasetName;
        this.clickOrigin = clickOrigin;
        this.clickExpand = clickExpand;
        this.buyOrigin = buyOrigin;
        this.buyExpand = buyExpand;
        this.testFile = testFile;
        this.featureNum = featureNum;
    }

    public String getClickOrigin() {
        return clickOrigin;
    }

    public String getBuyOrigin() {
        return buyOrigin;
    }

    public int getFeatureNum() {
        return featureNum;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getClickExpand() {
        return clickExpand;
    }

    public void setClickExpand(String clickExpand) {
        this.clickExpand = clickExpand;
    }

    public String getBuyExpand() {
        return buyExpand;
    }

    public void setBuyExpand(String buyExpand) {
        this.buyExpand = buyExpand;
    }

    public String getTestFile() {
        return testFile;
    }

    public void setTestFile(String testFile) {
        this.testFile = testFile;
    }

    @Override
    public String toString() {
        return "DatasetFile{" +
                "datasetName='" + datasetName + '\'' +
                ", clickOrigin='" + clickOrigin + '\'' +
                ", clickExpand='" + clickExpand + '\'' +
                ", buyOrigin='" + buyOrigin + '\'' +
                ", buyExpand='" + buyExpand + '\'' +
                ", testFile='" + testFile + '\'' +
                ", featureNum=" + featureNum +
                '}';
    }
}
