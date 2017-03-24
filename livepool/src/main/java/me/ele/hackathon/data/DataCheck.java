package me.ele.hackathon.data;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by solomon on 16/10/22.
 */
public class DataCheck {

    public static final String DATA_PATH   = "/Users/solomon/Desktop/hackathon/E_data/his_eco_info.txt";

    public static void main(String[] args) {
        try {
            checkData(DATA_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int click1buy0 = 1;
    private static int click1buy1 = 1;
    private static int click0buy0 = 1;

    public static List<String> checkData(String path) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            in.readLine();//ignore title
            String row = null;
            while ((row = in.readLine()) != null) {
                List<String> data = Splitter.on(Constant.SEPERATOR).splitToList(row);
                int click = Integer.parseInt(data.get(4));
                int buy = Integer.parseInt(data.get(5));
                if(click ==1 && buy==0){
                    click1buy0++;
                }
                if(click ==1 && buy==1){
                    click1buy1++;
                }
                if(click ==0 && buy==0){
                    click0buy0++;
                }
            }
            System.out.println("click 0 buy 0=="+click0buy0);
            System.out.println("click 1 buy 1=="+click1buy1);
            System.out.println("click 1 buy 0=="+click1buy0);
        }finally {
            if(null !=in){
                in.close();
            }
        }
        return list;
    }
}
