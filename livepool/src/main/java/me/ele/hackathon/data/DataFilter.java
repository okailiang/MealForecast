package me.ele.hackathon.data;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by solomon on 16/10/22.
 */
public class DataFilter {

    private static Multimap<String,String> his18UserIdMap = ArrayListMultimap.create();
    private static Multimap<String,String> his1920UserIdMap = ArrayListMultimap.create();
    private static Multimap<String,String> nextUserIdMap = ArrayListMultimap.create();
    private static Set<String> filtedListIdSet = new HashSet<>();
    private static Set<String> filtedLogIdSet = new HashSet<>();

    private static String his_eco_info_1920 = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/his_eco_info_1920.txt";
    private static String his_eco_env_1920 = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/his_eco_env_1920.txt";
    private static String next_eco_info_ori = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/next_eco_info_ori.txt";
    private static String next_eco_env_ori = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/next_eco_env_ori.txt";
    private static String rst_handled = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/rst_handled.txt";
    private static String next_eco_info = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/next_eco_info.txt";
    private static String next_eco_env = "/Users/solomon/Desktop/hackathon/E_Data_handled/filter/next_eco_env.txt";


    private static Set<Integer>  indexSet = new HashSet<>();
    private static Set<Integer>  minutesSet = new HashSet<>();
    private static Set<Integer>  dayNOSet = new HashSet<>();
    private static Map<String,Set<Integer>>  rstMap = new HashMap<>();


    public static void main(String[] args) {
        doFilt();
    }


    public static void doFilt( ) {
        System.out.println("start");
        getFiltInfo();
        try {
            System.out.println("outputInfoFile");
            outputInfoFile(his_eco_info_1920, next_eco_info);
            System.out.println("outputEnvFile");
            outputEnvFile(his_eco_env_1920,next_eco_env);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getFiltInfo( ) {
        List<String> rstIdList = getRstId(rst_handled);
        for(String rstId:rstIdList) {
            putNextSet(rstId, next_eco_info_ori, next_eco_env_ori);
            filterHis(rstId, his_eco_info_1920, his_eco_env_1920);
            indexSet.clear();
            minutesSet.clear();
            dayNOSet.clear();
            rstMap.clear();
        }
    }

    private static List<String>  getRstId(String rstIdFile){
        List<String> rstIdList = new ArrayList<>();
        BufferedReader in =null;
        try {
            in = new BufferedReader(new FileReader(rstIdFile));
            String line = "";
            while ((line = in.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                String rstId = Constant.DOUBLE_QUOTATION_MARKS+list.get(0)+Constant.DOUBLE_QUOTATION_MARKS;
                rstIdList.add(rstId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rstIdList;
    }

    private static void putNextSet(String baseRstId,String nextInfoFile,String nextEnvFile){
        Set<String> innerListIdSet = new HashSet<>();
        BufferedReader infoIn =null;
        BufferedReader envIn =null;
        try {
            infoIn = new BufferedReader(new FileReader(nextInfoFile));
            String line = infoIn.readLine();    // ignore first line
            while ((line = infoIn.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                String listId = Constant.DOUBLE_QUOTATION_MARKS+list.get(1)+Constant.DOUBLE_QUOTATION_MARKS;
                String rstId = Constant.DOUBLE_QUOTATION_MARKS+list.get(2)+Constant.DOUBLE_QUOTATION_MARKS;
                if(!baseRstId.equals(rstId)){
                    continue;
                }
                Integer index = Integer.parseInt(list.get(3));
                indexSet.add(index);
                innerListIdSet.add(listId);
            }
            envIn = new BufferedReader(new FileReader(nextEnvFile));
            line = envIn.readLine();    // ignore first line
            while ((line = envIn.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                String listId = list.get(0);
                if(!innerListIdSet.contains(listId)){
                    continue;
                }
                Integer dayNo = Parse.parseDayNo(Integer.parseInt(list.get(2)));
                Integer minutes = Parse.parseMinutes(Integer.parseInt(list.get(3)));
                dayNOSet.add(dayNo);
                minutesSet.add(minutes);
            }

            rstMap.put(baseRstId+"_index",indexSet);
            rstMap.put(baseRstId+"_dayNo",dayNOSet);
            rstMap.put(baseRstId+"_minutes",minutesSet);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                infoIn.close();
                envIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> filtedInfoList = new ArrayList<>();
    private List<String> filtedENVList = new ArrayList<>();

    private static void filterHis(String baseRstId,String hisInfoFile,String hisEnvFile){

        BufferedReader infoIn =null;
        BufferedReader envIn =null;
        Set<String> innerListIdSet = new HashSet<>();
        try {
            infoIn = new BufferedReader(new FileReader(hisInfoFile));
            String Infoline = infoIn.readLine();    // ignore first line
            while ((Infoline = infoIn.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(Infoline);
                String logId = list.get(0);
                String listId = list.get(1);
                String rstId = list.get(2);
                if(!baseRstId.equals(rstId)){
                    continue;
                }
                Integer index = Integer.parseInt(list.get(3));
                if(!rstMap.get(baseRstId+"_index").contains(index)){
                    continue;
                }
                innerListIdSet.add(listId);
                filtedLogIdSet.add(logId);
            }
            envIn = new BufferedReader(new FileReader(hisEnvFile));
            String envLine = envIn.readLine();    // ignore first line
            while ((envLine = envIn.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(envLine);
                String listId = list.get(0);
                if(!innerListIdSet.contains(listId)){
                    continue;
                }
                Integer dayNo = Parse.parseDayNo(Integer.parseInt(list.get(2)));
                if(!rstMap.get(baseRstId+"_dayNo").contains(dayNo)){
                    continue;
                }
                Integer minutes = Parse.parseMinutes(Integer.parseInt(list.get(3)));
                if(!rstMap.get(baseRstId+"_minutes").contains(minutes)){
                    continue;
                }
                filtedListIdSet.add(listId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                infoIn.close();
                envIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getOutInfoList(String inputFile,String outputFile) throws IOException {

    }

    private static void outputInfoFile(String inputFile,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(outputFile)))) {
            pw.append("title").append("\n");
            BufferedReader in =null;
            try {
                in = new BufferedReader(new FileReader(inputFile));
                String line = "";
                while ((line = in.readLine()) != null) {
                    List<String> list = Splitter.on("\t").splitToList(line);
                    String logId = list.get(0);
                    if(filtedLogIdSet.contains(logId)){
                        pw.append(line).append(Constant.LINE_BREAK);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally{
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void outputEnvFile(String inputFile,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(outputFile)))) {
            pw.append("title").append("\n");
            BufferedReader in =null;
            try {
                in = new BufferedReader(new FileReader(inputFile));
                String line = "";
                while ((line = in.readLine()) != null) {
                    List<String> list = Splitter.on("\t").splitToList(line);
                    String logId = list.get(0);
                    if(filtedListIdSet.contains(logId)){
                        pw.append(line).append(Constant.LINE_BREAK);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally{
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
