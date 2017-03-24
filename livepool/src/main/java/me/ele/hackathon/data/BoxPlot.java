package me.ele.hackathon.data;

import com.google.common.base.Splitter;
import me.ele.hackathon.solon.constant.Strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by solomon on 16/10/31.
 */
public class BoxPlot {

    public static int FEATURE_TAG = Constant.info_index_3  ;//2:rstid
    public static Set<Integer> tagSet = new HashSet<>();

    public static void main(String[] args) {
        try {
            //过滤训练集
            String inputPath = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH+"his_eco_info.txt";
            String outputPath = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH+"his_eco_info_filt.txt";
            //获取某一特征向量值的list
            List<Integer> tagList = getData(inputPath);
            //通过五位数法过滤原始数据文件，放入一个list
            List<String> filtedTrainList = filtTrainData(tagList, inputPath);
            //输出过滤后的训练数据 可直接覆盖原始数据文件
            outputFile(filtedTrainList,outputPath);

            /*inputPath = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH+"next_eco_info.txt";
            outputPath = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH+"next_eco_info_filt.txt";
            //根据训练集中的特征值 筛选 测试集合中的特征值
            List<String> filtedTestList = filtTestData(inputPath);
            //输出过滤后的测试数据 可直接覆盖原始数据文件
            outputFile(filtedTestList,outputPath);
            tagList = null;
            filtedTrainList = null;
            filtedTestList = null*/;

        } catch (IOException e) {
            e.printStackTrace();
        }
        //需要过滤测试集合，不然执行算法的时候，会因为训练集中不存在某些key而报错


    }

    /**
     * 获取特征值list
     * @param filePath
     * @return
     */
    public static List<Integer> getData(String filePath) {
        List<Integer> tagData = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String line = in.readLine(); //title
            while ((line = in.readLine()) != null) {
                List<String> row = Splitter.on(Constant.SEPERATOR).splitToList(line);
                int tagValue = Parse.parseRstId(row.get(FEATURE_TAG));//rstId
                tagData.add(tagValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tagData;
    }

    /**
     * 箱线法过滤数据
     * @param tagData
     * @param filePath
     * @return
     */
    public static List<String> filtTrainData(List<Integer> tagData,String filePath) {
        List<Integer> sortList = tagData.stream().sorted().collect(Collectors.toList());
        int q1Index = (sortList.size()+1)/4;
        int q3Index = (sortList.size()+1)*3/4;
        int Q1 = sortList.get(q1Index);
        int Q3 = sortList.get(q3Index);
        int IQR = Q3-Q1;
        int minLimit = (int) (Q1 - 1.5*IQR);
        int maxLimit = (int) (Q3 + 1.5*IQR);
        List<String> filtedList = new ArrayList<>();
        BufferedReader in = null;
        int delCount = 0;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String line = in.readLine(); //title
            while ((line = in.readLine()) != null) {
                List<String> row = Splitter.on(Constant.SEPERATOR).splitToList(line);
                int tagValue = Parse.parseRstId(row.get(FEATURE_TAG));//rstId
                if(tagValue>= minLimit && tagValue<=maxLimit){
                    filtedList.add(line);
                }else{
                    tagSet.add(tagValue);
                    delCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("<-------- del train size="+delCount);
        return filtedList;
    }

    public static List<String> filtTestData(String filePath) {
        List<String> filtedList = new ArrayList<>();
        BufferedReader in = null;
        int delCount = 0;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String line = in.readLine(); //title
            while ((line = in.readLine()) != null) {
                List<String> row = Splitter.on(Constant.SEPERATOR).splitToList(line);
                int featureValue = Parse.parseRstId(row.get(FEATURE_TAG));
                if(!tagSet.contains(featureValue)){
                    filtedList.add(line);
                }else{
                    delCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("<-------- del test size="+delCount);
        return filtedList;
    }

    /**
     * 输出文件
     * @param list
     * @param outputFile
     * @throws IOException
     */
    private static void outputFile(List<String> list, String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(outputFile)))) {
            pw.append("title").append("\n");
            list.stream().forEach(line -> {
                pw.append(line).append("\n");
            });
        }
    }
}
