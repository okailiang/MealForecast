package me.ele.hackathon.data.okl;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 分割文件
 *
 * @author oukailiang
 * @create 2016-11-03 下午12:22
 */

public class SplitFile {

    private static final String FILE_DIR = "/Users/oukailiang/Downloads/hackathon/E_Data/";
    private static final String NEW_FILE_DIR = "/Users/oukailiang/Downloads/hackathon/E_Data/new_data/";
    private static final String ENV_FILE = FILE_DIR + "his_eco_env.txt";
    private static final String INFO_FILE = FILE_DIR + "his_eco_info.txt";
    //训练周数
    private static int TRAIN_START = 14;
    private static int TRAIN_END = 20;
    //测试周数
    private static int TEST_START = 21;
    private static int TEST_END = 22;

    private static final String TRAIN_ENV_FILE = NEW_FILE_DIR + TRAIN_START + TRAIN_END + "_his_eco_env.txt";
    private static final String TRAIN_INFO_FILE = NEW_FILE_DIR + TRAIN_START + TRAIN_END + "_his_eco_info.txt";
    private static final String TEST_ENV_FILE = NEW_FILE_DIR + TEST_START + TEST_END + "_his_eco_env.txt";
    private static final String TEST_INFO_FILE = NEW_FILE_DIR + TEST_START + TEST_END + "_his_eco_info.txt";
    private static String ENV_TITLE;
    private static String INFO_TITLE;


    public static void splitEnvInfo() {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        FileWriter fwTest = null;
        BufferedWriter bwTest = null;
        Map<String, String> listIdEnvMap = new HashMap<>();
        Map<String, String> listIdTestEnvMap = new HashMap<>();
        try {
            fr = new FileReader(new File(ENV_FILE));
            br = new BufferedReader(fr);
            //训练集输出
            fw = new FileWriter(new File(TRAIN_ENV_FILE));
            bw = new BufferedWriter(fw);
            //测试集输出
            fwTest = new FileWriter(new File(TEST_ENV_FILE));
            bwTest = new BufferedWriter(fwTest);
            String line;
            ENV_TITLE = br.readLine();
            bw.write(ENV_TITLE + "\n");
            bwTest.write(ENV_TITLE + "\n");
            bw.flush();
            bwTest.flush();
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                String listId = rowArr[0];
                int dayNo = Integer.parseInt(rowArr[2]);
                //训练集
                if (dayNo > (TRAIN_START - 1) * 7 && dayNo <= TRAIN_END * 7) {
                    listIdEnvMap.put(listId, line);
                    bw.write(line + "\n");
                }
                //测试集
                if (dayNo > (TEST_START - 1) * 7 && dayNo <= TEST_END * 7) {
                    listIdTestEnvMap.put(listId, line);
                    bwTest.write(line + "\n");
                }
                bw.flush();
                bwTest.flush();
            }
            //
            outInfoFile(listIdEnvMap, listIdTestEnvMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
                fwTest.close();
                bwTest.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void splitEnvInfobyFiler() {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        FileWriter fwTest = null;
        BufferedWriter bwTest = null;
        Map<String, String> listIdEnvMap = new HashMap<>();
        Map<String, String> listIdTestEnvMap = new HashMap<>();
        try {
            fr = new FileReader(new File(ENV_FILE));
            br = new BufferedReader(fr);
            //训练集输出
            fw = new FileWriter(new File(TRAIN_ENV_FILE));
            bw = new BufferedWriter(fw);
            //测试集输出
            fwTest = new FileWriter(new File(TEST_ENV_FILE));
            bwTest = new BufferedWriter(fwTest);
            String line;
            ENV_TITLE = br.readLine();
            bw.write(ENV_TITLE + "\n");
            bwTest.write(ENV_TITLE + "\n");
            bw.flush();
            bwTest.flush();
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                String listId = rowArr[0];
                int dayNo = Integer.parseInt(rowArr[2]);
                //训练集
                if (dayNo > (TRAIN_START - 1) * 7 && dayNo <= TRAIN_END * 7) {
                    listIdEnvMap.put(listId, line);
                    bw.write(line + "\n");
                }
                //测试集
                if (dayNo > (TEST_START - 1) * 7 && dayNo <= TEST_END * 7) {
                    listIdTestEnvMap.put(listId, line);
                    bwTest.write(line + "\n");
                }
                bw.flush();
                bwTest.flush();
            }
            //
            outInfoFile(listIdEnvMap, listIdTestEnvMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
                fwTest.close();
                bwTest.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void outInfoFile(Map<String, String> listIdEnvMap, Map<String, String> listIdTestEnvMap) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        FileWriter fwTest = null;
        BufferedWriter bwTest = null;
        try {
            fr = new FileReader(new File(INFO_FILE));
            br = new BufferedReader(fr);
            //训练集输出
            fw = new FileWriter(new File(TRAIN_INFO_FILE));
            bw = new BufferedWriter(fw);
            //测试集输出
            fwTest = new FileWriter(new File(TEST_INFO_FILE));
            bwTest = new BufferedWriter(fwTest);
            String line;
            INFO_TITLE = br.readLine();
            bw.write(INFO_TITLE + "\n");
            bwTest.write(INFO_TITLE + "\n");
            bw.flush();
            bwTest.flush();
            while ((line = br.readLine()) != null) {
                String[] rowArr = line.split(Constant.SEPARATOR_TAB);
                String listId = rowArr[1];
                //训练集info
                if (listIdEnvMap.get(listId) != null) {
                    bw.write(line + "\n");
                }
                //测试集info
                if (listIdTestEnvMap.get(listId) != null) {
                    bwTest.write(line + "\n");
                }
                bw.flush();
                bwTest.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
                fw.close();
                bw.close();
                fwTest.close();
                bwTest.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        splitEnvInfo();
    }
}
