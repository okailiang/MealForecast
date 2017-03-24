package me.ele.hackathon.data;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 给定的libsvm文件找到对应的数据表中的列
 *
 * @author oukailiang
 * @create 2016-10-21 下午1:05
 */

public class FindLibSvmColumn {
    private static final String LIBSVM_FILE_DIR = "/Users/oukailiang/Downloads/hackathon/combine/";
    //转libsvm文件
    private static final String LIBSVM_FILE = LIBSVM_FILE_DIR + "click_train.txt";//没有标准化的libsvm文件
    private static final String STANDARD_FILE = LIBSVM_FILE_DIR + "click_train_standard_libsvm_column.txt";
    //文件中每个数据间按该间隔符分割
    private static final String SEPARATOR = " ";
    //文件中每个数据间按该间隔符分割
    private static final String SEPARATOR_TAB = "\t";
    //
    private static final String[] colValueArr = new String[]{
            "info_log_id", "info_list_id", "info_restaurant_id", "info_index", "info_is_click",
            "info_is_buy", "info_is_raw_buy", "info_order_id", "env_list_id", "env_is_select",
            "env_day_no", "env_minutes", "env_eleme_device_id", "env_is_new", "env_info_x",
            "env_info_y", "env_user_id", "env_network_type", "env_platform", "env_brand",
            "env_model", "env_network_operator", "env_resolution", "env_channel", "rst_restaurant_id",
            "rst_primary_category", "rst_food_name_list", "rst_category_list", "rst_x", "rst_y",
            "rst_agent_fee", "rst_is_premium", "rst_address_type", "rst_good_rating_rate", "rst_open_month_num",
            "rst_has_image", "rst_has_food_img", "rst_min_deliver_amount", "rst_time_ensure_spent", "rst_is_time_ensure",
            "rst_is_ka", "rst_is_time_ensure_discount", "rst_is_eleme_deliver", "rst_radius", "rst_bu_flag",
            "rst_brand_name", "rst_service_rating", "rst_invoice", "rst_online_payment", "rst_public_degree",
            "rst_food_num", "rst_food_image_num", "rst_is_promotion_info", "rst_is_bookable"
    };

    /**
     * 获得libsvm文件中一行的各列值得含义
     */
    public static void getLibsvmColumn() {
        Map<Integer, String> colValueMap = getColValueMap();
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            //读
            br = new BufferedReader(new FileReader(LIBSVM_FILE));
            //写
            fw = new FileWriter(STANDARD_FILE);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            //只取第一行
            String line = br.readLine();
            String[] oneRowArr = line.split(SEPARATOR);
            sb.append(SEPARATOR).append(SEPARATOR);
            for (int i = 1; i < oneRowArr.length; i++) {
                sb.append(colValueMap.get(Integer.parseInt(oneRowArr[i].split(":")[0]))).append(SEPARATOR);
            }
            sb.append("\n");
            System.out.print(sb.toString());
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得标准化libsvm文件中一行的各列值得含义
     *
     * @param filePath 没有标准化的libsvm文件路径
     */
    public static void getStandardLibsvmColumn(String filePath) {
        Map<Integer, String> colValueMap = getColValueMap();
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            //读
            br = new BufferedReader(new FileReader(filePath));
            //写
            String dest = filePath.substring(0, filePath.lastIndexOf(".")) + "_standard_column.txt";
            fw = new FileWriter(dest);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            //计算一列值有多少维度
            Map<Integer, List<String>> colDimensionMap = DataStandardization.getColDimension(br);
            //读
            br = new BufferedReader(new FileReader(filePath));
            //只取第一行
            String line = br.readLine();
            String[] oneRowArr = line.split(SEPARATOR);
            int len = oneRowArr.length;

            //sb.append(SEPARATOR).append(SEPARATOR);
            for (int i = 1; i < len; i++) {
                String tmpColName = colValueMap.get(Integer.parseInt(oneRowArr[i].split(":")[0]));
                //顺序对应
                List<String> colValueList = colDimensionMap.get(i);
                int colSize = colValueList.size();
                String first = colValueList.get(0);
                //已经标准化的不需要多为扩展
                if (colSize == 2 || "-1".equals(first)) {
                    sb.append(tmpColName).append(SEPARATOR);
                    continue;
                }
                //对该列值进行多维度扩展
                for (int j = 0; j < colSize; j++) {
                    String[] indexValue = colValueList.get(j).split(":");
                    sb.append(tmpColName).append("_").append(indexValue[1]).append(SEPARATOR);
                }
            }
            System.out.println("StandardLibsvm文件中不含第一列的列的个数：" + sb.toString().split(SEPARATOR).length);
            sb.append("\n");
            System.out.print(sb.toString());
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<Integer, String> getColValueMap() {
        Map<Integer, String> map = new HashMap<>();
        int len = colValueArr.length;
        System.out.println("colValueArr len = " + len);
        for (int i = 0; i < len; i++) {
            map.put(i, colValueArr[i]);
        }
        return map;
    }

    /**
     * 从静态类中生成静态变量
     *
     * @return
     */
    public static Map<Integer, String> parseStaticVar() {
        Map<Integer, String> colValueMap = new HashMap<>();
        List<String> colValueList = new ArrayList<>();
        try {
            Object obj = Class.forName("me.ele.hackathon.data.Constant").newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            boolean startFlag = false;

            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    String var = field.getName();
                    if ("info_log_id_0".equals(var)) {
                        startFlag = true;
                    }
                    if (startFlag) {
                        colValueList.add(var);
                    }
                    if ("rst_is_bookable_53".equals(var)) {
                        break;
                    }
                    System.out.println(field.getName());
                }
            }
            //
            for (int i = 0; i < colValueList.size(); i++) {
                String colValue = colValueList.get(i);
                String tmp = colValue.substring(colValue.lastIndexOf("_") + 1);
                //_最后一位是数字则取前半部分
                if (Pattern.compile("[0-9]*").matcher(tmp).matches()) {
                    colValue = colValue.substring(0, colValue.lastIndexOf("_"));
                }
                colValueMap.put(i, colValue);
                System.out.print("\"" + colValue + "\",");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return colValueMap;
    }

    /**
     * 获得一个大文件中的前三行
     */
    public static void getFileTopThreeRows(String filePath) {
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            //读
            br = new BufferedReader(new FileReader(filePath));
            //写
            String dest = filePath.substring(0, filePath.lastIndexOf(".")) + "_top3_rows.txt";
            fw = new FileWriter(dest);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            int count = 1;
            String line = br.readLine();
            System.out.println("文件中列的个数：" + line.split(SEPARATOR).length);
//            while ((line = br.readLine()) != null) {
//                count++;
//            }
//            System.out.println("row count:" + count);
            //只取前三行
            sb.append(line).append("\n");
            for (int i = 1; i < 10; i++) {
                sb.append(br.readLine()).append("\n");
            }
            System.out.print(sb.toString());
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将一个特征相同的文件添加到一个文件的后面
     */
    public static void appendToSrcFile(String srcFilePath, String destFilePath) {
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String line;
        try {
            //读
            br = new BufferedReader(new FileReader(destFilePath));
            //写
            fw = new FileWriter(srcFilePath, true);
            bw = new BufferedWriter(fw);
            //StringBuilder sb = new StringBuilder();
            //第一行不复制
            line = br.readLine();
            System.out.println("文件中列的个数：" + line.split(SEPARATOR_TAB).length);
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从如19、20周数据中忽的真实结果
     * @param srcFilePath
     * @param outFilePath
     */
    public static void getRealResult(String srcFilePath,String outFilePath) {
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            //读
            br = new BufferedReader(new FileReader(srcFilePath));
            //写
            fw = new FileWriter(outFilePath);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            System.out.println("文件中列的个数：" + line.split(SEPARATOR).length);
            while ((line = br.readLine()) != null) {
                sb = new StringBuilder();
                String[] rowArr = line.split(SEPARATOR_TAB);
                //0,5,6分别为log_id,is_click,is_buy
                sb.append(rowArr[0]).append(SEPARATOR_TAB).append(rowArr[4]).append(SEPARATOR_TAB).append(rowArr[5]);
                bw.write(sb.toString()+"\n");
                bw.flush();
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String txt_dir= "/Users/oukailiang/Downloads/hackathon/";
        //
        //parseStaticVar();
        //getLibsvmColumn();
        //getStandardLibsvmColumn(LIBSVM_FILE);
        //获得一个大文件中的前三行
       // getFileTopThreeRows(LIBSVM_FILE_DIR + "combine_his_info_env_rst.txt");
        //appendToSrcFile(LIBSVM_FILE_DIR + "his_eco_info.txt", LIBSVM_FILE_DIR + "next_eco_info.txt");
        //获得两周的真实结果
        getRealResult("/Users/oukailiang/Downloads/hackathon/spark_data_local/data/next_eco_info.txt"
                ,"/Users/oukailiang/Desktop/result/1920_real_result.txt");
    }
}
