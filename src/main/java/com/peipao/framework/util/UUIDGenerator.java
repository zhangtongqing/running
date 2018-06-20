package com.peipao.framework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Random;

/**
 * 方法名称：
 * 功能描述：
 * 参数：
 * 返回值：
 * 作者：Administrator
 * 版本：
 * 创建日期：2017/8/12 13:15
 * 修订记录：
 */
public class UUIDGenerator {

    public static String localSequence;

    @Autowired(required = true)
    public static void setLocalSequence(@Value("${local.sequence.number}")String localSequence) {
        UUIDGenerator.localSequence = localSequence;
    }

    public static Long getCurrTime() {
        return System.currentTimeMillis();
    }

    public static String getRandomNumber(int length){
        Random rad = new Random();
        String result = rad.nextInt(1000000) +"";
        if(result.length() != length){
            return getRandomNumber(length);
        }
        return result;
    }


    public static Serializable generate() {
        StringBuffer sf = new StringBuffer();
        sf.append(getCurrTime());
        sf.append(getRandomNumber(6));
        return sf;
    }

    public static void main(String[] args) {
        new UUIDGenerator().generate();
    }
}
