package me.ele.hackathon.object;

/**
 * Created by solomon on 16/10/15.
 */
public class EcoEnv {
    String id;
    int isSelect;
    int isNew;
    int dayNo;
    int hour;//minutes/60
    int user_id;

    int networkType;
    int platform;
    int netrowkOperator;
    int channel;
    int model;
    int brand;
    int resolution;

    public EcoEnv(){
    }


    public EcoEnv(String id, int isSelect, int isNew,
                  int networkType, int platform, int netrowkOperator, int hour) {
        this.id = id;
        this.isSelect = isSelect;
        this.isNew = isNew;
        this.networkType = networkType;
        this.platform = platform;
        this.netrowkOperator = netrowkOperator;
        this.hour = hour;
    }


    public EcoEnv(String id, int isSelect, int isNew,
                  int networkType, int platform, int netrowkOperator) {
        this.id = id;
        this.isSelect = isSelect;
        this.isNew = isNew;
        this.networkType = networkType;
        this.platform = platform;
        this.netrowkOperator = netrowkOperator;
    }


    public String getId() {
        return id;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public int getIsNew() {
        return isNew;
    }

    public int getNetworkType() {
        return networkType;
    }

    public int getPlatform() {
        return platform;
    }

    public int getNetrowkOperator() {
        return netrowkOperator;
    }

    public int getHour() {
        return hour;
    }

    public int getDayNo() {
        return dayNo;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getChannel() {
        return channel;
    }

    public int getModel() {
        return model;
    }

    public int getBrand() {
        return brand;
    }

    public int getResolution() {
        return resolution;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public void setDayNo(int dayNo) {
        this.dayNo = dayNo;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setNetrowkOperator(int netrowkOperator) {
        this.netrowkOperator = netrowkOperator;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }


}
