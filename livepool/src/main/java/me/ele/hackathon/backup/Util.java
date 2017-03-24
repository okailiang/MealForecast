package me.ele.hackathon.backup;

import me.ele.hackathon.object.EcoInfo;

import java.io.PrintWriter;

/**
 * Created by solomon on 16/10/19.
 */
public class Util {

    private static final String SEPERATOR = " ";


    public static int buildObject(PrintWriter pw,EcoInfo ecoInfo,int num){
        pw.append((num++) + ":").append(String.valueOf(ecoInfo.getRestaurantId())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getIndex())).append(SEPERATOR);
//        pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsSelect())).append(SEPERATOR);
//        pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getIsNew())).append(SEPERATOR);
//        pw.append((num++) + ":").append(String.valueOf(ecoInfo.getEcoEnv().getHour())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetworkType())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getPlatform())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getEcoEnv().getNetrowkOperator())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getPrimary_category())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getAgent_fee())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getIs_premium())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getGood_rating_rate())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getMin_deliver_amount())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getIs_time_ensure())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getIs_time_ensure_discount())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getIs_eleme_deliver())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getBu_flag())).append(SEPERATOR);
        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getService_rating())).append(SEPERATOR);
//        pw.append((num++)+":").append(String.valueOf(ecoInfo.getRstInfo().getIs_promotion_info())).append(SEPERATOR);

        return num;
    }
}
