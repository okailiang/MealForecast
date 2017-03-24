package me.ele.hackathon.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by solomon on 16/10/24.
 */
public class Test {

//    public static List<String> featureList = Arrays.asList("getRst_restaurant_id_0", "getInfo_index_3", "getRst_primary_category_1",
//            "getRst_good_rating_rate_9", "getRst_open_month_num_10", "getRst_service_rating_22", "getRst_food_num_26",
//            "getRst_food_image_num_27", "getEnv_day_no_2","getEnv_minutes_3");

    public static List<String> featureList = Arrays.asList("1","2","3","4","5","6","7","8","9","10");


    public static void main(String[] args) {
        List<String> res = arr1(featureList).stream().distinct().collect(Collectors.toList());
        System.out.println("size:"+res.size());

        res.stream().forEach(re -> {
            System.out.println(re);
        });

    }

    public static List<String> arr1(List<String> innerList){
        int n  = innerList.size();
        List<String> resultList = new ArrayList<>();
        for(int x=0;x<n;x++){
            List<String> a = innerList.subList(x,x+1);
            List<String> b = innerList.subList(x+1,n);
            resultList.addAll(arr2(a,b));
        }
        return resultList;
    }

    public static List<String> arr2(List<String> innerAList,List<String> innerBList){
        int n  = innerBList.size();
        List<String> resultList = new ArrayList<>();
        final StringBuffer result = new StringBuffer();
        for(int x=0;x<n;x++){
            for(int y=0;y<=x;y++){
                List<String> splitBList = innerBList.subList(y,x+1);
                innerAList.stream().forEach(a->{
                    result.append(a).append(",");
                });
                splitBList.stream().forEach(b -> {
                    result.append(b).append(",");
                });
                resultList.add(result.toString());
                result.setLength(0) ;
            }
        }
        return resultList;
    }


}
