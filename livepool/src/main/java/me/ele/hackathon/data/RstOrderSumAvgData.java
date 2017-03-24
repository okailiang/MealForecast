package me.ele.hackathon.data;

import me.ele.hackathon.object.RstOrderSumAvg;
import me.ele.hackathon.solon.constant.Strategy;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author oukailiang
 * @create 2016-10-25 下午9:47
 */

public class RstOrderSumAvgData {
    private static final String FILEDIR = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH;
    private static final String ORDER_SUM_AVG_FILE = FILEDIR + "rst_order_sum_avg.txt";
    private static final String SEPARATOR = " ";
    public static Map<String, RstOrderSumAvg> orderSumAvgMap;

    static {
        orderSumAvgMap = getOrderSumAvgMap();
    }

    public static Map<String, RstOrderSumAvg> getOrderSumAvgMap() {
        BufferedReader br = null;
        String line;
        Map<String, RstOrderSumAvg> orderSumAvgMap = new HashMap<>();
        try {
            //读
            br = new BufferedReader(new FileReader(new File(ORDER_SUM_AVG_FILE)));

            br.readLine();
            while ((line = br.readLine()) != null) {

                String[] cols = line.split(SEPARATOR);
                orderSumAvgMap.put(Constant.DOUBLE_QUOTATION_MARKS+cols[0]+Constant.DOUBLE_QUOTATION_MARKS, getRstOrderSumAvg(cols));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return orderSumAvgMap;
    }

    private static RstOrderSumAvg getRstOrderSumAvg(String[] cols) {
        RstOrderSumAvg rstOrderSumAvg = new RstOrderSumAvg();
        int len = cols.length;
        rstOrderSumAvg.setRestaurant_id(cols[0]);
        rstOrderSumAvg.setSum_deliver_fee((int) Double.parseDouble(cols[1]));
        rstOrderSumAvg.setAvg_deliver_fee((int) Double.parseDouble(cols[2]));
        rstOrderSumAvg.setSum_order_process_minutes((int) Double.parseDouble(cols[3]));
        rstOrderSumAvg.setAvg_order_process_minutes((int) Double.parseDouble(cols[4]));
        rstOrderSumAvg.setSum_is_coupon((int) Double.parseDouble(cols[5]));
        rstOrderSumAvg.setAvg_is_coupon((int) Double.parseDouble(cols[6]));
        rstOrderSumAvg.setSum_eleme_order_total((int) Double.parseDouble(cols[7]));
        rstOrderSumAvg.setAvg_eleme_order_total((int) Double.parseDouble(cols[8]));
        rstOrderSumAvg.setSum_total((int) Double.parseDouble(cols[9]));
        rstOrderSumAvg.setAvg_total((int) Double.parseDouble(cols[10]));
        rstOrderSumAvg.setSum_cut_money((int) Double.parseDouble(cols[11]));
        rstOrderSumAvg.setAvg_cut_money((int) Double.parseDouble(cols[12]));
        rstOrderSumAvg.setSum_has_new_user_subsidy((int) Double.parseDouble(cols[13]));
        rstOrderSumAvg.setAvg_has_new_user_subsidy((int) Double.parseDouble(cols[14]));
        rstOrderSumAvg.setSum_hongbao_amount((int) Double.parseDouble(cols[15]));
        rstOrderSumAvg.setAvg_hongbao_amount((int) Double.parseDouble(cols[16]));
        rstOrderSumAvg.setSum_receiver_deliver_fee((int) Double.parseDouble(cols[17]));
        rstOrderSumAvg.setAvg_receiver_deliver_fee((int) Double.parseDouble(cols[18]));
        rstOrderSumAvg.setSum_food_category((int) Double.parseDouble(cols[19]));
        rstOrderSumAvg.setAvg_food_category((int)Double.parseDouble(cols[20]));

        return rstOrderSumAvg;
    }

    public static void main(String[] args) {
        Map<String, RstOrderSumAvg> o = RstOrderSumAvgData.orderSumAvgMap;
    }
}
