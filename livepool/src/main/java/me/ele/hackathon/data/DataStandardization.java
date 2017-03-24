package me.ele.hackathon.data;

import java.io.*;
import java.util.*;

/**
 * 将libsvm或者txt文件的数据转为0或1
 *
 * @author oukailiang
 * @create 2016-10-20 下午7:45
 */

public class DataStandardization {

    ///Users/oukailiang/Downloads/hackathon/E_Data/
    private static final String LIBSVM_FILE_DIR = "/Users/oukailiang/Downloads/";
    private static final String TXT_FILE_DIR = "/Users/oukailiang/Downloads/hackathon/E_Data/data/";
    private static final String NUM_TXT_FILE_DIR = "/Users/oukailiang/Downloads/hackathon/E_Data_handled/";
    //转libsvm文件
    private static final String LIBSVM_FILE = LIBSVM_FILE_DIR + "click_train.txt";
    private static final String STANDARD_FILE = LIBSVM_FILE_DIR + "click_train_standard.txt";
    //转txt文件
    private static final String TXT_FILE = LIBSVM_FILE_DIR + "rst_info.txt";
    private static final String STANDARD_TXT_FILE = LIBSVM_FILE_DIR + "standard_rst_info.txt";
    //文件中每个数据间按该间隔符分割
    private static final String SEPARATOR = " ";
    //一列值得种类大于这个数时不进行纬度扩展
    private static final Integer MAX_COL_VALUE_NUM = 200;
    //文件中每个数据间按该间隔符分割
    private static final String SEPARATOR_TAB = "\t";
    private static int[] needConvertArr = {};

    /**
     * 将libsvm文件标准化
     */
    public static void libsvmToStandard() {
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String line;
        try {
            //读
            br = new BufferedReader(new FileReader(LIBSVM_FILE));
            //写
            fw = new FileWriter(STANDARD_FILE);
            bw = new BufferedWriter(fw);
            //计算一列值有多少维度
            Map<Integer, List<String>> colDimensionMap = getColDimension(br);
            br = new BufferedReader(new FileReader(LIBSVM_FILE));
            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                String[] cols = line.split(SEPARATOR);
                int colNum = cols.length;
                //第一列保留
                sb.append(cols[0]).append(SEPARATOR);
                //先将一行分割
                for (int i = 1; i < colNum; i++) {
                    //顺序对应
                    List<String> colValueList = colDimensionMap.get(i);
                    int colSize = colValueList.size();
                    String first = colValueList.get(0);
                    //已经标准化的不需要多为扩展
                    if (colSize == 2 || "-1".equals(first)) {
                        sb.append(cols[i]).append(SEPARATOR);
                        continue;
                    }
                    //对该列值进行多维度扩展
                    for (int j = 0; j < colSize; j++) {
                        String[] indexValue = colValueList.get(j).split(":");
                        sb.append(indexValue[0]).append(indexValue[1]);
                        //对该列值赋1
                        if (colValueList.get(j).equals(cols[i])) {
                            sb.append(":1").append(SEPARATOR);
                        } else {
                            sb.append(":0").append(SEPARATOR);
                        }
                    }
                }
                String writeLine = sb.toString();
                //去除最后的空格
                writeLine = writeLine.substring(0, writeLine.length() - 1);
                bw.write(writeLine + "\n");
                bw.flush();
            }
            bw.flush();
            //获得文件中一行数据对应的列
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
     * 计算一列值有多少维度
     *
     * @param bufferedReader
     * @return
     * @throws IOException
     */
    public static Map<Integer, List<String>> getColDimension(BufferedReader bufferedReader) throws IOException {
        Map<Integer, HashSet<String>> colDimensionMap = new HashMap<>();
        HashSet<String> colValueSet;
        Map<Integer, List<String>> colIndexMap = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {

            String[] cols = line.split(SEPARATOR);
            int colNum = cols.length;
            //下标0列保留原来的数值
            for (int i = 1; i < colNum; i++) {
                if (colDimensionMap.get(i) == null) {
                    colValueSet = new HashSet<>();
                } else {
                    colValueSet = colDimensionMap.get(i);
                }
                colValueSet.add(cols[i]);
                colDimensionMap.put(i, colValueSet);
            }
        }

        //将每列的所有值排序
        for (Map.Entry<Integer, HashSet<String>> entry : colDimensionMap.entrySet()) {
            HashSet<String> colSet = entry.getValue();
            List<String> colKeyList = new ArrayList<>();
            //超过允许最大值不进行处理
            if (colSet.size() > MAX_COL_VALUE_NUM) {
                colKeyList.add("-1");
                colIndexMap.put(entry.getKey(), colKeyList);
                continue;
            }
            Iterator<String> it = colSet.iterator();
            while (it.hasNext()) {
                colKeyList.add(it.next());
            }
            Collections.sort(colKeyList);
            colIndexMap.put(entry.getKey(), colKeyList);
        }
        return colIndexMap;
    }

    /**
     * 将txt文件标准化
     */
    public static void txtToStandard() {
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String line;
        try {
            //读
            br = new BufferedReader(new FileReader(TXT_FILE));
            //写
            fw = new FileWriter(STANDARD_TXT_FILE);
            bw = new BufferedWriter(fw);
            //计算一列值有多少维度
            Map<Integer, List<String>> colDimensionMap = getTxtColDimension(br);
            br = new BufferedReader(new FileReader(TXT_FILE));
            String[] oneRowArr = br.readLine().split(SEPARATOR_TAB);
            //处理文档开头
            writeToTxt(bw, oneRowArr, colDimensionMap, false);
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(SEPARATOR_TAB);
                writeToTxt(bw, cols, colDimensionMap, true);
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
     * 构造文件头行
     *
     * @param bw
     * @param rowArr
     * @param colDimensionMap
     * @throws IOException
     */
    public static void writeToTxt(BufferedWriter bw, String[] rowArr, Map<Integer
            , List<String>> colDimensionMap, Boolean flag)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        int colNum = rowArr.length;
        //写第一列表头
        for (int row = 0; row < colNum; row++) {
            //顺序对应
            List<String> colValueList = colDimensionMap.get(row);
            String first = colValueList.get(0);
            int colSize = colValueList.size();
            //已经标准化或者大于允许扩展的不进行扩展
            if ((colSize == 2 && ("0".equals(first) || "1".equals(first)))
                    || "-1".equals(first)) {
                sb.append(removeColon(rowArr[row])).append(SEPARATOR_TAB);
                continue;
            }
            //对该列值进行多维度扩展
            for (int j = 0; j < colSize; j++) {
                if (flag) {
                    //对该列值赋1
                    if (colValueList.get(j).equals(removeColon(rowArr[row]))) {
                        sb.append("0").append(SEPARATOR_TAB);
                    } else {
                        sb.append("0").append(SEPARATOR_TAB);
                    }
                    //处理文档头
                } else {
                    String indexValue = colValueList.get(j);
                    sb.append(removeColon(rowArr[row])).append("_")
                            .append(indexValue.replace(" ", "")).append(SEPARATOR_TAB);
                }

            }
        }
        if (!flag) {
            System.out.println("对txt标准化后的列数：" + sb.toString().split(SEPARATOR_TAB).length);
            System.out.println("对txt标准化后的列：" + sb.toString());
        }
        sb.append("\n");
        bw.write(sb.toString());
        bw.flush();
    }

    /**
     * 将txt文件标准化数字
     */
    public static void txtToStandardNum(String srcFilePath, String destFilePath, int[] convertCol) {
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String line;
        try {
            needConvertArr = convertCol;
            //读
            br = new BufferedReader(new FileReader(srcFilePath));
            //写
            fw = new FileWriter(destFilePath);
            bw = new BufferedWriter(fw);
            //计算一列值有多少维度
            Map<Integer, List<String>> colDimensionMap = getTxtColDimension(br);
            br = new BufferedReader(new FileReader(srcFilePath));
            //文档第一行baoliu
            bw.write(br.readLine() + "\n");
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(SEPARATOR_TAB);
                writeNumToTxt(bw, cols, colDimensionMap);
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
     * 构造文件行
     *
     * @param bw
     * @param rowArr
     * @param colDimensionMap
     * @throws IOException
     */
    public static void writeNumToTxt(BufferedWriter bw, String[] rowArr, Map<Integer
            , List<String>> colDimensionMap)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        int colNum = rowArr.length;
        //写第一列表头
        for (int row = 0; row < colNum; row++) {
            //顺序对应
            List<String> colValueList = colDimensionMap.get(row);
            String first = colValueList.get(0);
            int colSize = colValueList.size();
            //已经标准化或者大于允许扩展的不进行扩展
            if ((colSize == 2 && ("0".equals(first) || "1".equals(first)))
                    || !containCol(needConvertArr, row)) {
                //sb = isOrder(sb, rowArr, row);
                //sb = isRst(sb, rowArr, row);
                sb = isEnv(sb, rowArr, row);

                continue;
            }
            //对该列值进行多维度扩展
            for (int j = 0; j < colSize; j++) {
                //对该列值赋1
                if (colValueList.get(j).equals(removeColon(rowArr[row]))) {
                    sb.append(j).append(SEPARATOR_TAB);
                }
            }

        }
        sb.append("\n");
        bw.write(sb.toString());
        bw.flush();
    }

    private static StringBuilder isOrder(StringBuilder sb, String[] rowArr, int row) {
        if (row == 0) {
            sb.append(handleDayNo(removeColon(rowArr[row]))).append(SEPARATOR_TAB);
        } else if (row == 1 || row == 6) {
            sb.append(handleMinutes(removeColon(rowArr[row]))).append(SEPARATOR_TAB);
        } else if (row == 4) {
            sb.append(handleOrderDeliverFee(removeColon(rowArr[row]))).append(SEPARATOR_TAB);
        } else {
            sb.append(removeColon(rowArr[row])).append(SEPARATOR_TAB);
        }
        return sb;
    }

    private static StringBuilder isInfo(StringBuilder sb, String[] rowArr, int row) {
        sb.append(removeColon(rowArr[row])).append(SEPARATOR_TAB);
        return sb;
    }

    private static StringBuilder isEnv(StringBuilder sb, String[] rowArr, int row) {
        if (row == 3) {
            sb.append(handleMinutes(removeColon(rowArr[row]))).append(SEPARATOR_TAB);
        } else {
            sb.append(removeColon(rowArr[row])).append(SEPARATOR_TAB);
        }
        return sb;
    }

    private static StringBuilder isRst(StringBuilder sb, String[] rowArr, int row) {
        if (row == 9 || row == 19 || row == 22) {
            sb.append(String.valueOf(Math.round(100 * Double.parseDouble(removeColon(rowArr[row]))))).append(SEPARATOR_TAB);
        } else if (row == 10) {
            sb.append(Integer.parseInt(removeColon(rowArr[row])) + 7).append(SEPARATOR_TAB);
        } else {
            sb.append(removeColon(rowArr[row])).append(SEPARATOR_TAB);
        }
        return sb;
    }

    private static boolean containCol(int[] noConvertCol, int index) {
        boolean flag = false;
        for (int i = 0; i < noConvertCol.length; i++) {
            if (noConvertCol[i] == index) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static String handleOrderDeliverFee(String DeliverFree) {
        return String.valueOf(Math.round(Double.parseDouble(DeliverFree)));
    }

    public static String handleMinutes(String minute) {
        return String.valueOf(Integer.parseInt(("NULL".equals(minute) ? "0" : minute)) / 60);
    }

    public static String handleDayNo(String dayNo) {
        int day = Integer.parseInt("NULL".equals(dayNo) ? "0" : dayNo);
        if (day >= 0) {
            return dayNo;
        }
        return String.valueOf(Math.abs(day));
    }

    /**
     * 计算一列值有多少维度
     *
     * @param bufferedReader
     * @return
     * @throws IOException
     */
    public static Map<Integer, List<String>> getTxtColDimension(BufferedReader bufferedReader) throws IOException {
        Map<Integer, HashSet<String>> colDimensionMap = new HashMap<>();
        HashSet<String> colValueSet;
        Map<Integer, List<String>> colIndexMap = new HashMap<>();
        String line;
        bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {

            String[] cols = line.split(SEPARATOR_TAB);
            int colNum = cols.length;
            for (int i = 0; i < colNum; i++) {
                if (colDimensionMap.get(i) == null) {
                    colValueSet = new HashSet<>();
                } else {
                    colValueSet = colDimensionMap.get(i);
                }
                colValueSet.add(removeColon(cols[i]));
                colDimensionMap.put(i, colValueSet);
            }
        }

        //将每列的所有值排序
        for (Map.Entry<Integer, HashSet<String>> entry : colDimensionMap.entrySet()) {
            HashSet<String> colSet = entry.getValue();
            List<String> colKeyList = new ArrayList<>();
            if (colSet.size() > MAX_COL_VALUE_NUM) {
                colKeyList.add("-1");
                colIndexMap.put(entry.getKey(), colKeyList);
                continue;
            }
            Iterator<String> it = colSet.iterator();
            while (it.hasNext()) {
                colKeyList.add(it.next());
            }
            Collections.sort(colKeyList);
            int index = entry.getKey();
            if (containCol(needConvertArr, index)) {
                for (int i = 0; i < colKeyList.size(); i++) {
                    System.out.println(index + "=" + colKeyList.get(i) + ":" + i);
                }

            }
            colIndexMap.put(index, colKeyList);
        }
        return colIndexMap;
    }

    /**
     * 去除列值两边的分号
     *
     * @param colValue
     * @return
     */
    public static String removeColon(String colValue) {
        if (colValue.indexOf("\"") == -1) {
            return colValue;
        }
        int len = colValue.length();
        return colValue.substring(1, len - 1);
    }

    public static int getDistance(String envX, String envY, String rstX, String rstY) {

        Double x = Double.parseDouble(envX);
        Double y = Double.parseDouble(envY);
        Double x1 = Double.parseDouble(rstX);
        Double y1 = Double.parseDouble(rstY);
        double result = Math.sqrt(Math.pow(Math.abs(x - x1), 2) + Math.pow(Math.abs(y - y1), 2));
        System.out.println(Math.round(result * 1000 / 10));
        return 0;
    }

    public static double getOpenMonthNum(String openMonth) {
        return Integer.parseInt(openMonth) + Math.ceil(22 * 7 + 50) / 30;

    }

    public static void rstTxtToStandardNum() {
        int[] convertCol = {1, 2, 3, 8, 20, 21};
        txtToStandardNum(TXT_FILE_DIR + "rst_info.txt", NUM_TXT_FILE_DIR + "standard_rst_info.txt", convertCol);
    }

    public static void infoTxtToStandardNum() {
        int[] convertCol = {};
        txtToStandardNum(TXT_FILE_DIR + "next_eco_info.txt", NUM_TXT_FILE_DIR + "standard_next_eco_info.txt", convertCol);
    }

    public static void envTxtToStandardNum() {
        int[] noConvertCol = {9, 10, 11, 12, 13, 14, 15};
        txtToStandardNum(TXT_FILE_DIR + "next_eco_env.txt", NUM_TXT_FILE_DIR + "standard_next_eco_env.txt", noConvertCol);
    }

    public static void orderTxtToStandardNum() {
        int[] convertCol = {8, 16, 25, 26};
        txtToStandardNum(TXT_FILE_DIR + "his_order_info.txt", NUM_TXT_FILE_DIR + "standard_his_order_info.txt", convertCol);
    }

    public static void main(String[] args) {

        libsvmToStandard();
        //txtToStandard();
        //getDistance("9.12654313689991", "7.65977874245384", "9.6716", "9.7284");
        //System.out.println(getOpenMonthNum("-7"));
        //rstTxtToStandardNum();
        //infoTxtToStandardNum();
        //envTxtToStandardNum();
        //orderTxtToStandardNum();
    }
}
