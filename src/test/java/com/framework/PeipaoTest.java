package com.framework;

import com.peipao.framework.util.SignFormat;
import com.peipao.qdl.statistics.service.UserStatisticCacheService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;


public class PeipaoTest {
    private static final Logger log = LoggerFactory.getLogger(PeipaoTest.class);
    @Autowired
    private UserStatisticCacheService userStatisticCacheService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void regexTest(){
        String content = "I am noob " +
                "from runoob.com.";
        String pattern = ".*runoob.*";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);
    }


    @Test
    public void regexTest2(){

        // 正则表达式中 \\符号 代表原样匹配 \\(  代表（
        String content = "22AB陪跑()-";
        String pattern =  "^[A-Za-z0-9\u4e00-\u9fa5\\(\\)\\-]+$";
       // return match(text, "^[A-Za-z0-9\u4e00-\u9fa5]+$");
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字母数字 " + isMatch);
    }

    @Test
    public void regexName(){
        String content = "鲁班.很强 ";
        String pattern =  "^[A-Za-z0-9\u4e00-\u9fa5\\.]+$";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字母数字 " + isMatch);
    }

    @Test
    public void regexClassName(){
        String content = "S软件(3)班-A";
        String pattern =  "^[A-Za-z0-9\u4e00-\u9fa5\\(\\)\\-]+$";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字母数字 " + isMatch);
    }


    @Test
    public void deleteWhitespace(){
        String spaceStr = " 22 B Cc ";
       String str =  StringUtils.deleteWhitespace(spaceStr);
        log.info("去掉空格后 ={}", str);
    }

//    @Test
//    public void redisTest(){
//        String key = "verifyCode:"+"1236666";
//        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(key);
//        ops.expire(60*30, TimeUnit.SECONDS);
//    }

    @Test
    public void redisOpt(){
        Jedis jedis = new Jedis("211.159.153.228",6379);
        jedis.auth("peipao");
        log.info("reids测试服务器连接...{}", jedis.ping());
        Boolean flag = jedis.exists("zhang*");
        log.info("exists is {}", flag);
        Set keys = jedis.keys("*runningRuleNode:runningRuleNodeList*");
        log.info(keys.toString());
    }


    /**
     * 原生操作reids
     * redis-cli -a 'peipao'
     * keys *   列出所有key
     * del  key  不能模糊匹配
     * 清空测试环境围缓存
     */
    @Test
    public void redisOptRunningRuleNode(){
        Jedis jedis = new Jedis("211.159.153.228",6379);
        jedis.auth("peipao");
        log.info("reids测试服务器连接...{}", jedis.ping());
        Set<String> keys = jedis.keys("*");
        for(String key : keys){
            Long info = jedis.del(key);
            log.info("删除 key ={} result ={}",key,info);
        }
        log.info(keys.toString());
    }

    @Test
    public void redisTrestmp(){
        log.info("测试服务器={}", "");
    }

    @Test
    public void urlEcode(){
        try {
            String encode = URLEncoder.encode("我们=", "UTF-8");
            String str = SignFormat.urlEncode("我们=");
            log.info(encode);
            log.info(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void robdty(){
        
    }
}
