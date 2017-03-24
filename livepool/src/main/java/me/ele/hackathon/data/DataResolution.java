package me.ele.hackathon.data;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by solomon on 16/10/22.
 */
public class DataResolution {

    private static Multimap<String,String> his18UserIdMap = ArrayListMultimap.create();
    private static Multimap<String,String> his1920UserIdMap = ArrayListMultimap.create();
    private static Multimap<String,String> nextUserIdMap = ArrayListMultimap.create();
    private static List<String> hisEnvList = new ArrayList<>();
    private static List<String> nextEnvList = new ArrayList<>();

    private static String inputHis18EnvFilePath = "/Users/solomon/Desktop/hackathon/E_Data_handled/reso_user_id/input/his_eco_env_18.txt";
    private static String inputHis1920EnvFilePath = "/Users/solomon/Desktop/hackathon/E_Data_handled/reso_user_id/input/his_eco_env_1920.txt";
    private static String inputNextEnvFilePath = "/Users/solomon/Desktop/hackathon/E_Data_handled/reso_user_id/input/next_eco_env.txt";

    private static String outHisEnvFilePath = "/Users/solomon/Desktop/hackathon/E_Data_handled/reso_user_id/his_eco_env.txt";
    private static String outNextEnvFilePath = "/Users/solomon/Desktop/hackathon/E_Data_handled/reso_user_id/next_eco_env.txt";

    public static void main(String[] args) {
        getHis18Map(inputHis18EnvFilePath);
        getHis1920Map(inputHis1920EnvFilePath);
        getNextMap(inputNextEnvFilePath);
        union();
        try {
            outputFile(hisEnvList,outHisEnvFilePath);
            outputFile(nextEnvList,outNextEnvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String,String> union(){
        for (String userId: his18UserIdMap.keySet()) {
            if(null != his1920UserIdMap.get(userId) && null!=nextUserIdMap.get(userId)){
                his18UserIdMap.get(userId).stream().forEach(env -> {
                    hisEnvList.add(env);
                });
            }
        }

        for (String userId: his1920UserIdMap.keySet()) {
            if(null != his18UserIdMap.get(userId) && null!=nextUserIdMap.get(userId)){
                his1920UserIdMap.get(userId).stream().forEach(env -> {
                    nextEnvList.add(env);
                });
            }
        }
        return null;
    }

    private static void outputFile(List<String> list,String outputFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(outputFile)))) {
            pw.append("title").append("\n");
            list.stream().forEach(line-> pw.append(line));
        }
    }

    private static Map<String,String> getHis18Map(String his18File){

        BufferedReader in =null;
        try {
            in = new BufferedReader(new FileReader(his18File));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                his18UserIdMap.put(list.get(8), line);
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
        return null;
    }

    private static Map<String,String> getHis1920Map(String his1920File){
        BufferedReader in =null;
        try {
            in = new BufferedReader(new FileReader(his1920File));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                his1920UserIdMap.put(list.get(8), line);
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
        return null;
    }

    private static Map<String,String> getNextMap(String nextFile){
        BufferedReader in =null;
        try {
            in = new BufferedReader(new FileReader(nextFile));
            String line = in.readLine();    // ignore first line
            while ((line = in.readLine()) != null) {
                List<String> list = Splitter.on("\t").splitToList(line);
                nextUserIdMap.put(list.get(8), line);
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
        return null;
    }
}
