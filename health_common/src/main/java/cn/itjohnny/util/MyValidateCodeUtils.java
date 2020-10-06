package cn.itjohnny.util;

import org.junit.Test;

import java.util.Random;

public class MyValidateCodeUtils {


    /**
     * 生成1-6位指定位数的随机数字串,可以0开头
     * @param length
     * @return
     */
    public static String getRandomCode(Integer length){
        // 长度必须在1和6之间
        if (length > 6 || length <= 0){
            throw new IllegalArgumentException("length must between 1 and 6");
        }
        // 生成随机数字
        Random r = new Random();
        StringBuffer random = new StringBuffer();
        for (int i = 0; i < length; i++) {
            random.append(r.nextInt(10));
        }

        return random.toString();
    }


}
