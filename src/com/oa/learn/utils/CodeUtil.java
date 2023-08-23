package com.oa.learn.utils;

import java.util.Random;

public class CodeUtil {
    public static String generateRandomNumber() {
        StringBuilder strB = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < 6; i++) {
            int digit = rand.nextInt(10); // 生成0到9之间的随机数字
            strB.append(digit);
        }
        return strB.toString();
    }
}
