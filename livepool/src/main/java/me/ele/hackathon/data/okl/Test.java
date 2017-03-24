package me.ele.hackathon.data.okl;

import java.util.HashMap;
import java.util.List;

/**
 * 测试
 *
 * @author oukailiang
 * @create 2016-10-31 上午11:30
 */

public class Test {
    public static void main(String[] args) {
        HandleTxtFile.init();
        List<HisEcoInfo> nextInfoList = HandleTxtFile.getNextInfos();
        System.out.println("ok");

    }
}
