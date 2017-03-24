package me.ele.hackathon.data;

import com.google.common.base.Splitter;
import me.ele.hackathon.solon.constant.Strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * solomon
 * 拆分原始文件
 * 将原始的训练集进行拆分
 */
public class ExtractData {

    public static void main(String[] args) throws IOException {
        extractData();
    }


    static public int weekDay = 1;
    static public int setDayNo = 0;
    static public int hour = 0;

    static public Integer[] minutesArray ={345,415,305,174,180,162,308,475,427,393,278,439,336,316,364,389,261,360,286,354,362,361,325,335,397,404,280,405,338,176,265,224,132,116,258,411,204,268,297,233};

    public static void extractData() throws IOException {
        String ecoEnvFile = Strategy.ORI_SRC_FILE_DIR + "his_eco_env.txt";

//        String trainEnvOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH + "train_his_eco_env_before" + before + ".txt";
//        String testEnvOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "test_his_eco_env_after" + after + ".txt";
        String trainEnvOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH + "his_eco_env.txt";
        String testEnvOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "next_eco_env.txt";

        BufferedReader in = null;
        PrintWriter pw = null;
        List<String> trainEnvList = new ArrayList<>();
        List<String> testEnvList = new ArrayList<>();
        Map<String, Boolean> trainListIdMap = new HashMap<>();
        Map<String, Boolean> testListIdMap = new HashMap<>();
        try {
            in = new BufferedReader(new FileReader(ecoEnvFile));
            String line = in.readLine();    // ignore first line
            List<Integer> mList = Arrays.asList(minutesArray);
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String id = list.get(0);
                int dayNo = Integer.parseInt(list.get(2));
                int hourData = Parse.parseMinutes(Integer.parseInt(list.get(3)));
                if (hourData == hour) {
                    trainEnvList.add(line);
                    trainListIdMap.put(id, true);
                }
                if (dayNo >112 && dayNo<=126 && hourData == hour) {
                    testEnvList.add(line);
                    testListIdMap.put(id, true);
                }
            }
        } finally {
            if (null != in) {
                in.close();
            }
        }
        outputFile(trainEnvList, trainEnvOutFile);
        outputFile(testEnvList, testEnvOutFile);
        extractEcoInfo(trainListIdMap, testListIdMap);
        trainListIdMap = null;
        testListIdMap = null;
        trainEnvList = null;
        testEnvList = null;
    }

    private static void extractEcoInfo(Map<String, Boolean> trainListIdMap, Map<String, Boolean> testListIdMap) throws IOException {
        String ecoInfoFile = Strategy.ORI_SRC_FILE_DIR + "his_eco_info.txt";
//        String trainInfoOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "train_his_eco_info_before"+before+".txt";
//        String testInfoOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "test_his_eco_info_after"+after+".txt";
        String trainInfoOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "his_eco_info.txt";
        String testInfoOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "next_eco_info.txt";

        BufferedReader in = null;
        PrintWriter pw = null;
        List<String> trainInfoList = new ArrayList<>();
        List<String> testInfoList = new ArrayList<>();
        try {
            in = new BufferedReader(new FileReader(ecoInfoFile));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {

                List<String> list = Splitter.on("\t").splitToList(line);
                String list_id = list.get(1);
                if (null != trainListIdMap.get(list_id)) {
                    trainInfoList.add(line);
                }
                if (null != testListIdMap.get(list_id)) {
                    testInfoList.add(line);
                }
            }
        } finally {
            if (null != in) {
                in.close();
            }
        }
        outputFile(trainInfoList, trainInfoOutFile);
        outputFile(testInfoList, testInfoOutFile);
    }


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
