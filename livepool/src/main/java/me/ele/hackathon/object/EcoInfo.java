package me.ele.hackathon.object;

/**
 * Created by solomon on 16/10/15.
 */
public class EcoInfo {
    String rstId;
    int index;
    int isClick;
    int isBuy;
    int restaurantId;


    EcoEnv ecoEnv;
    RstInfo rstInfo;

    public EcoInfo(int index, int isClick, int isBuy, EcoEnv env) {
        this.index = index;
        this.isClick = isClick;
        this.isBuy = isBuy;
        this.ecoEnv = env;
    }

    public int getIndex() {
        return index;
    }

    public int getIsClick() {
        return isClick;
    }

    public int getIsBuy() {
        return isBuy;
    }

    public EcoEnv getEcoEnv() {
        return ecoEnv;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
    }

    public void setIsBuy(int isBuy) {
        this.isBuy = isBuy;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setEcoEnv(EcoEnv ecoEnv) {
        this.ecoEnv = ecoEnv;
    }

    public RstInfo getRstInfo() {
        return rstInfo;
    }

    public void setRstInfo(RstInfo rstInfo) {
        this.rstInfo = rstInfo;
    }

    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
    }
}
