package me.ele.hackathon.utils;

import java.io.*;
import java.util.*;

/**
 * 将多个相同的文件聚合到一个文件
 *
 * @author oukailiang
 * @create 2016-10-26 上午10:30
 */

public class AggregateFile {

    private static final String SEPARATOR = " ";
    private static final String SEPARATOR_TAB = "\t";

    /**
     * 将一个文件夹下所有的result.txt合并一个文件且和原来文件的l顺序一致
     *
     * @param resultFileDir
     * @param realInfoFilePath
     */
    public static void mergeResult(String resultFileDir, String realInfoFilePath, String outMergeFilePath) {
        BufferedReader realBr = null;
        BufferedReader predBr = null;
        BufferedWriter bw = null;
        String realline;
        String predline;

        Map<String, String> logIdClickBuyMap = new HashMap<>();
        try {
            File resultFile = new File(resultFileDir);
            //获得目录下的多个文件
            File[] fileList = resultFile.listFiles();
            fileList = filterNoTxt(fileList);
            int fileLen = fileList.length;
            //读取result目录下的所有result.txt放到map中
            for (int i = 0; i < fileLen; i++) {
                predBr = new BufferedReader(new FileReader(fileList[i]));
                while ((predline = predBr.readLine()) != null) {
                    String[] predRowArr = predline.split(SEPARATOR_TAB);
                    logIdClickBuyMap.put(predRowArr[0], predline);
                }
            }
            //读取真实info数据
            realBr = new BufferedReader(new FileReader(realInfoFilePath));
            bw = new BufferedWriter(new FileWriter(outMergeFilePath));
            realBr.readLine();//跳过第一行
            while ((realline = realBr.readLine()) != null) {
                String[] realRowArr = realline.split(SEPARATOR_TAB);
                String logId = realRowArr[0];
                if (logIdClickBuyMap.get(logId) != null) {
                    bw.write(logIdClickBuyMap.get(logId) + "\n");
                }
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
                predBr.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将一个目录下行数相同的多个文件合并为一个文件
     *
     * @param lableFeatureFileDir 存要合的多个文件的目录 其中文件格式为1 0 1 ...
     * @param destFilePath        合并后的文件输出路径
     */
    public static void manyLableFeatureToOne(String lableFeatureFileDir, String destFilePath) {

        BufferedReader br = null;
        String line;
        Map<Integer, String> rowMap = new LinkedHashMap<>();
        StringBuilder sb;
        try {

            File srcFile = new File(lableFeatureFileDir);
            //获得目录下的多个文件
            File[] fileList = srcFile.listFiles();
            fileList = filterNoTxt(fileList);

            int fileLen = fileList.length;
            for (int i = 0; i < fileLen; i++) {
                br = new BufferedReader(new FileReader(fileList[i]));
                //已经合并的特征数,读取第一个文件时rowMap中没有数据
                int curFeatureNum = (i == 0 ? 0 : (rowMap.get(0).split(SEPARATOR).length - 1));
                //每个文件中的行索引
                int rowIndex = 0;

                while ((line = br.readLine()) != null) {
                    sb = new StringBuilder();
                    String[] rowArr = line.split(SEPARATOR);
                    //第一次读取时保留标签
                    if (i == 0) {
                        sb.append(rowArr[0]).append(SEPARATOR);
                    }
                    //拼接多个特征
                    int rowLen = rowArr.length;
                    for (int col = 1; col < rowLen; col++) {
                        sb.append(curFeatureNum + col).append(":").append(rowArr[col]).append(SEPARATOR);
                    }

                    String rowLine = rowMap.get(rowIndex);
                    if (rowLine != null) {
                        sb.insert(0, rowLine);
                    }
                    rowMap.put(rowIndex, sb.toString());
                    rowIndex++;
                }
            }

            //将结果输入到一个文件中
            outDestFile(rowMap, destFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将一个目录下行数相同的多个文件合并为一个文件
     *
     * @param resutlFileDir 存要合的多个文件的目录,其中文件的内容只有一行值为1或0
     * @param destFilePath  合并后的文件输出路径
     */
    public static void voteResultsToOne(String resutlFileDir, String destFilePath) {

        BufferedReader br = null;
        String line;
        Map<Integer, String> rowMap = new LinkedHashMap<>();
        StringBuilder sb;
        try {

            File srcFile = new File(resutlFileDir);
            //获得目录下的多个文件
            File[] fileList = srcFile.listFiles();
            fileList = filterNoTxt(fileList);

            int fileLen = fileList.length;
            for (int i = 0; i < fileLen; i++) {
                br = new BufferedReader(new FileReader(fileList[i]));
                //每个文件中的行索引
                int rowIndex = 0;
                while ((line = br.readLine()) != null) {
                    sb = new StringBuilder();
                    sb.append(line).append(SEPARATOR);
                    String rowLine = rowMap.get(rowIndex);
                    if (rowLine != null) {
                        sb.insert(0, rowLine);
                    }
                    rowMap.put(rowIndex, sb.toString());
                    rowIndex++;
                }
            }

            //将结果输入到一个文件中
            outVoteDestFile(rowMap, destFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File[] filterNoTxt(File[] fileList) {
        List<File> newFileList = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].getName().lastIndexOf(".txt") > -1) {
                newFileList.add(fileList[i]);
            }
        }
        return newFileList.toArray(new File[newFileList.size()]);
    }

    private static void outDestFile(Map<Integer, String> rowMap, String destFilePath) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            fw = new FileWriter(new File(destFilePath));
            bw = new BufferedWriter(fw);
            for (Map.Entry<Integer, String> entry : rowMap.entrySet()) {
//                System.out.println(entry.getKey() + ":" + entry.getValue());

                bw.write(entry.getValue() + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void outVoteDestFile(Map<Integer, String> rowMap, String destFilePath) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            fw = new FileWriter(new File(destFilePath));
            bw = new BufferedWriter(fw);
            for (Map.Entry<Integer, String> entry : rowMap.entrySet()) {
//                System.out.println((entry.getKey() + 1) + ":" + entry.getValue());
                bw.write(voteResult(entry.getValue()) + "\n");
                bw.flush();
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String voteResult(String rowsValue) {
        String[] valueArr = rowsValue.split(SEPARATOR);
        int oneCount = 0;
        int zeroCount = 0;
        int len = valueArr.length;
        for (int i = 0; i < len; i++) {
            if ("1".equals(valueArr[i])) {
                oneCount++;
            } else {
                zeroCount++;
            }
        }
        if (oneCount > zeroCount) {
            return "1";
        }
        return "0";
    }

    public static void main(String[] args) {
//        manyLableFeatureToOne("/Users/oukailiang/Desktop/many_result/", "/Users/oukailiang/Desktop/label_feature_result.txt");
//        voteResultsToOne("/Users/oukailiang/Desktop/result/", "/Users/oukailiang/Desktop/result.txt");
        mergeResult("/Users/oukailiang/Desktop/many_result/", "/Users/oukailiang/Desktop/1920_his_eco_info_old_real.txt",
                "/Users/oukailiang/Desktop/merge_result.txt");

    }
}
