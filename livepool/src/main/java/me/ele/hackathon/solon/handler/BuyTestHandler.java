package me.ele.hackathon.solon.handler;

import me.ele.hackathon.data.Constant;
import me.ele.hackathon.data.LibSvmDataBuilder;
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
public class BuyTestHandler {


    public static void main(String[] args) {
        combain( getBuyWithOutClick(),getClickResult());
    }

    public static void getBuyTestWithClick(){
        combain(getBuyWithOutClick(), getClickResult());
    }

    static public List<String> getBuyWithOutClick(){
        BufferedReader in =null;
        List<String> list = new ArrayList<>();
        try {
            in = new BufferedReader(new FileReader(Strategy.BUY_TEST_WITHOUT_CLICK));

            String line ;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    static public List<String> getClickResult(){
        BufferedReader in =null;
        List<String> list = new ArrayList<>();
        try {
            in = new BufferedReader(new FileReader(Strategy.CLICK_RESULT));

            String line ;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    static public void combain(List<String> list1,List<String> list2)  {
        try {
            try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(Strategy.BUY_TEST_WITH_CLICK)))) {
                int indexNum = LibSvmDataBuilder.selected_feature_list.size();
                for(int i=0;i<list1.size();i++){
                    pw.append(list1.get(i))
                            .append(String.valueOf(indexNum+1)).append(Constant.COLON).append(list2.get(i));
                    pw.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
