package com.shanyangcode.infinitechat.authenticationservice.utils;

import java.util.Random;

public class NicknameGeneratorUtil {

    static String [] adj = {"烦人的", "可爱的", "迷人的", "炽热的", "肥胖的", "扯淡的", "绿色的", "大眼睛的"};
    static String [] animal = {"哈吉米", "狗支", "火狐", "东北虎", "大熊猫", "末影龙"};

    public static String generateNickname() {
        Random random = new Random();
        String adjj = adj[random.nextInt(adj.length)];
        String animall = animal[random.nextInt(animal.length)];
        return adjj + animall;

    }
}
