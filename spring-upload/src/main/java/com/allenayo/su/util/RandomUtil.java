package com.allenayo.su.util;

import java.util.UUID;

public class RandomUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
