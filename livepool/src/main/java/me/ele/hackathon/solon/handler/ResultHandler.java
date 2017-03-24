package me.ele.hackathon.solon.handler;

import com.google.common.base.Splitter;
import me.ele.hackathon.data.Constant;
import me.ele.hackathon.data.ExtractData;
import me.ele.hackathon.object.Record;
import me.ele.hackathon.solon.constant.Strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by solomon on 16/10/21.
 */
public class ResultHandler {

    public static Record record = new Record();


    public static void main(String[] args) {
        try {
            List<String> logIdList = getData(Strategy.LOG_ID);
            List<String> clickList = getData(Strategy.CLICK_RESULT);
            List<String> buyList = getData(Strategy.BUY_RESULT);
            outputFile(logIdList,clickList,buyList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void outPutResult() {
        try {
            List<String> logIdList = getData(Strategy.LOG_ID);
            List<String> clickList = getData(Strategy.CLICK_RESULT);
            List<String> buyList = getData(Strategy.BUY_RESULT);
            outputFile(logIdList,clickList,buyList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<String> getData(String path) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            String row = null;
            while ((row = in.readLine()) != null) {
                List<String> data = Splitter.on(Constant.SEPERATOR).splitToList(row);
                list.add(data.get(0));
            }
        }finally {
            if(null !=in){
                in.close();
            }
        }
        return list;
    }

    public static void outputFile(List<String> logIdList,List<String> clickList,List<String> buyList) throws IOException {


        System.out.println("---------------- outputFile --------------");
        System.out.println("");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(Strategy.RESULT_OUT_PATH+"_"+ ExtractData.hour+".txt")))) {
            int size =logIdList.size();
            for(int i=0;i<size;i++){
                pw.append(logIdList.get(i)).append(Constant.SEPERATOR)
                        .append(clickList.get(i)).append(Constant.SEPERATOR)
                        .append(buyList.get(i))
                        .append(Constant.LINE_BREAK);

            }

        }
    }




}
