package com.allenayo.sj.util;

import java.util.UUID;

public class RandomUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
