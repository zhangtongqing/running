package com.peipao.framework.util;

import net.sf.json.JSONObject;

import java.io.*;
import java.util.Calendar;
import java.util.Iterator;

/**
 * 方法名称：FileUtil
 * 功能描述：FileUtil
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/18 13:23
 * 修订记录：
 */
public class FileUtil {

//    public static boolean deleteFile(String path) {
//        File file = new File(path);
//        return file.delete();
//    }

    public static String createFilePath(String tempPath, String fileName) {
        File one = new File(tempPath);
        Calendar cal = Calendar.getInstance();
        Integer year = Integer.valueOf(cal.get(1));
        Integer month = Integer.valueOf(cal.get(2) + 1);
        one = new File(tempPath + "/" + year + "/" + month);
        if (!one.exists()) {
            one.mkdirs();
        }
        return one.getPath() + File.separator + fileName;
    }

    /**
     * 根据本地模板生成静态页面
     * @param tempPath 模板文件路经
     * @param HtmlFilePath 生成的html路经
     * @param jsonObj 内容等信息
     * @return boolean 保存成功=true
     */
    public static boolean createHtmlFile(String tempPath, String HtmlFilePath, JSONObject jsonObj) {
        String str = "";
		//long beginDate = (new Date()).getTime();
        try {
            String tempStr = "";
            FileInputStream is = new FileInputStream(tempPath);// 读取模块文件
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((tempStr = br.readLine()) != null) {
                str = str + tempStr;
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            for (Iterator iterator = jsonObj.keys(); iterator.hasNext();) {
                String jsonKey = (String) iterator.next();
                str = str.replace("{#" + jsonKey + "#}", jsonObj.getString(jsonKey));// 替换掉模块中相应的地方
                //System.out.println(jsonKey);
                //System.out.println(jsonObj.getString(jsonKey));
            }
            File f = new File(HtmlFilePath);
            BufferedWriter o = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HtmlFilePath), "UTF-8"));
            o.write(str);
            o.close();
            //System.out.println("生成HTML页面成功，共用时：" + ((new Date()).getTime() - beginDate) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
