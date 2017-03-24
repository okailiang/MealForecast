package me.ele.hackathon.data;

import com.google.common.base.Splitter;
import me.ele.hackathon.solon.constant.Strategy;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by solomon on 16/10/22.
 */
public class UserDataFilter {


    public static Set<String> hisUserSet = new HashSet<>();
    public static Set<String> nextUserSet = new HashSet<>();

    static{
        Properties prop = new Properties();
        try {
            prop = PropertiesLoaderUtils.loadAllProperties("application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean ifUserFilt = Boolean.parseBoolean(prop.getProperty("IF_FILTE_USER"));
        if(ifUserFilt) {
            String hisEnvFile = Strategy.PATH_PREIX + Strategy.BASE_DATA_SUB_DIR_PATH + "his_eco_env.txt";
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(hisEnvFile));
                String line = in.readLine();//ignore title
                while ((line = in.readLine()) != null) {
                    List<String> list = Splitter.on("\t").splitToList(line);
                    String userId = list.get(8);
                    hisUserSet.add(userId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String nextEnvFile = Strategy.PATH_PREIX + Strategy.BASE_DATA_SUB_DIR_PATH + "next_eco_env.txt";
            in = null;
            try {
                in = new BufferedReader(new FileReader(nextEnvFile));
                String line = in.readLine();//ignore title
                while ((line = in.readLine()) != null) {
                    List<String> list = Splitter.on("\t").splitToList(line);
                    String userId = list.get(8);
                    nextUserSet.add(userId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
        }
    }



}
