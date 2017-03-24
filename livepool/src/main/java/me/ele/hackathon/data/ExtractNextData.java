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
public class ExtractNextData {

    public static void main(String[] args) throws IOException {
        extractData();
    }


    public static void extractData() throws IOException {
        extractHisEcoEnvTrain();
        extractNextEcoEnvTest();
    }


    static public int hour = 0;


    public static void extractHisEcoEnvTrain() throws IOException {
        String file = Strategy.ORI_SRC_FILE_DIR + "his_eco_env.txt";

        String trainInfoOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "his_eco_env.txt";
        List<String> trainInfoList = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                int hourData = Parse.parseMinutes(Integer.parseInt(list.get(3)));
//                System.out.println("hourData="+hourData);
                if (hourData == hour) {
                    trainInfoList.add(line);
                }
            }
        } finally {
            if (null != in) {
                in.close();
            }
        }
        outputFile(trainInfoList, trainInfoOutFile);
        trainInfoList = null;
    }

    public static void extractNextEcoEnvTest() throws IOException {
        String file = Strategy.ORI_SRC_FILE_DIR + "next_eco_env.txt";

        String testInfoOutFile = Strategy.PATH_PREIX+Strategy.BASE_DATA_SUB_DIR_PATH  + "next_eco_env.txt";
        List<String> testInfoList = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                String id = list.get(0);
                int hourData = Parse.parseMinutes(Integer.parseInt(list.get(3)));
                if (hourData == hour) {
                    testInfoList.add(line);
                }
            }
        } finally {
            if (null != in) {
                in.close();
            }
        }
        outputFile(testInfoList, testInfoOutFile);
        testInfoList = null;
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
