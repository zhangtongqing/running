package com.peipao.qdl.statistics.service;


import com.peipao.framework.util.ExcelExport;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.course.service.CourseService;
import com.peipao.qdl.user.service.UserService;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：UserStatisticUtilService
 * 功能描述：UserStatisticUtilService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/27 15:19
 * 修订记录：
 */
@Service
public class UserStatisticUtilService {
    @Autowired
    private CourseService courseService;

    public String createStudentScoreInfoListExport(HttpServletResponse response, HttpServletRequest request, List<Map<String, Object>> list, String fileName, boolean morningTrainFlag) throws Exception {
        //有效晨练总次数 有效晨练总得分
        int colNum = 25;
        if(!morningTrainFlag) {//如果学校关闭了学期的晨练功能
            colNum = colNum - 2;
        }
        //TODO 根据 morningTrainFlag 调整col数量
        HSSFWorkbook wbarray = new HSSFWorkbook();
        HSSFSheet sheet = wbarray.createSheet( "第1页" );
        HSSFRow row = null;
        HSSFCell cell = null;
        DecimalFormat df = new DecimalFormat("######0.00");
        HSSFCellStyle contextstyle = wbarray.createCellStyle();
        int totalnum = list.size();// 总共多少条数据list
        for ( int r = 0; r < totalnum + 1; r++ ) {
            row = sheet.createRow( r );
            row.setHeight( ( short ) 350 );
            for ( int col = 0; col < colNum; col++ ) {
                cell = row.createCell( col );
            }
        }
        for ( int j = 0; j <= totalnum; j++ ) {
            int indexNum = 0;
            row = sheet.getRow( j );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "序号" );
            else{
                cell.setCellValue(j);
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "学号" );
            else{
                if (list.get(j-1).get("studentNo") != null) {
                    cell.setCellValue(list.get(j - 1).get("studentNo").toString());
                }
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "姓名" );
            else
                cell.setCellValue( list.get(j-1).get("username").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "手机账号" );
            else
                cell.setCellValue( list.get(j-1).get("mobile").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "专业班级" );
            else
                cell.setCellValue( list.get(j-1).get("classname").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "所属课程" );
            else
                cell.setCellValue( list.get(j-1).get("courseName").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "晨跑有效总里程(公里)" );
            else
                cell.setCellValue( list.get(j-1).get("morningRunningLengthAll").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "晨跑有效次数" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("morningRunningCount").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "有效晨跑补偿次数" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                cell.setCellStyle(contextstyle);
                cell.setCellValue( list.get(j-1).get("compensateMorningRunningCount").toString() );
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "有效晨跑总次数" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("morningRunningCountAll").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "有效晨跑总得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("morningRunningScore").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "自由跑有效次数" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("freeRunningCount").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "自由跑计分里程(公里)" );
            else
                cell.setCellValue( list.get(j-1).get("freeRunningLength").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "自由跑补偿计分里程(公里)" );
            else
                cell.setCellValue( list.get(j-1).get("compensateFreeRunningLength").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "自由跑计分总里程(公里)" );
            else
                cell.setCellValue( list.get(j-1).get("freeRunningLengthAll").toString() );
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "自由跑总得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("freeRunningScore").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "校方活动有效参与次数" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("schoolActivityCount").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "校方活动得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("schoolActivityScore").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "校方活动补偿得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("compensateActivityScore").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "校方活动总得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("compensateActivityScore").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "课程考勤次数" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("attendanceCount").toString());
            }
           /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "课程考勤得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("attendanceScore").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
            if(morningTrainFlag) {//如果学校关闭了学期的晨练功能
                cell = row.getCell( indexNum++ );
                if ( j == 0 )
                    cell.setCellValue( "有效晨练总次数" );
                else {
                    contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));//数据格式只显示整数
                    cell.setCellStyle(contextstyle);
                    cell.setCellValue(list.get(j - 1).get("morningTrainCount").toString());
                }
            /*-------------------------------------------------------------------------------------------------------*/
                cell = row.getCell( indexNum++ );
                if ( j == 0 )
                    cell.setCellValue( "有效晨练总得分" );
                else {
                    contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                    cell.setCellStyle(contextstyle);
                    cell.setCellValue(list.get(j - 1).get("morningTrainScore").toString());
                }
            }
            /*-------------------------------------------------------------------------------------------------------*/
            cell = row.getCell( indexNum++ );
            if ( j == 0 )
                cell.setCellValue( "综合得分" );
            else {
                contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));//保留1位小数
                cell.setCellStyle(contextstyle);
                cell.setCellValue(list.get(j - 1).get("score").toString());
            }
            /*-------------------------------------------------------------------------------------------------------*/
        }
        return ExcelExport.createExcel(response, request, fileName +" 学生成绩", wbarray);
    }


    public void createCourseIdString(JSONObject json, Long userId, Long semesterId) throws Exception {
        if(!ValidateUtil.jsonValidateWithKey(json, "courseArray")) {
            //如果没有课程ID查询参数，则根据登录用户(老师或学校管理员)加载相关默认课程ID数组进行查询
            List<Map<String, Object>> courseList = courseService.getAllCourseNameAndIdArray(userId, semesterId, null);
            if(!CollectionUtils.isEmpty(courseList)) {
                StringBuffer cIds = new StringBuffer();
                courseList.forEach(map -> {
                    cIds.append(map.get("courseId").toString());
                    cIds.append(",");
                });
                String courseIds = cIds.toString();
                courseIds = courseIds.substring(0, courseIds.length()-1);//去掉最后一个多余的逗号
                json.put("courseArray", courseIds);
            } else {
                json.put("courseArray", -1);//当前用户没有负责的课程或权限// meteor.wu 把以前定义的0修改为-1，因为0为待定
            }
        }
    }


    public void createCourseArrayString(JSONObject json, Long userId, Long semesterId) throws Exception {
        if(!ValidateUtil.jsonValidateWithKey(json, "courseArray")) {
            //如果没有课程ID查询参数，则根据登录用户(老师或学校管理员)加载相关默认课程ID数组进行查询
            List<Map<String, Object>> courseList = courseService.getAllCourseNameAndIdArray(userId, semesterId, null);
            if(!CollectionUtils.isEmpty(courseList)) {
                StringBuffer cIds = new StringBuffer();
                courseList.forEach(map -> {
                    if("0".equals(map.get("courseId").toString())) {
                        json.put("daiding", "待定");//包含key=daiding的表示需要查询没有选课的
                    } else {
                        cIds.append(map.get("courseId").toString());
                        cIds.append(",");
                    }
                });
                String courseIds = cIds.toString();
                if(StringUtil.isNotEmpty(courseIds) && courseIds.indexOf(",") > 0) {
                    courseIds = courseIds.substring(0, courseIds.length()-1);//去掉最后一个多余的逗号
                    json.put("courseArray", courseIds);
                }
            } else {
                json.put("courseArray", -1);//当前用户没有负责的课程或权限// meteor.wu 把以前定义的0修改为-1，因为0为待定
            }
        }
    }

    public void initCourseArrayString(Map<String, Object> paramsMap, long userId, Long semesterId) throws Exception {
        //根据登录用户(限定为老师)加载相关默认课程ID数组进行查询
        List<Map<String, Object>> courseList = courseService.getAllCourseNameAndIdArray(userId, semesterId, null);
        if(!CollectionUtils.isEmpty(courseList)) {
            StringBuffer cIds = new StringBuffer();
            courseList.forEach(map -> {
                if(!("0".equals(map.get("courseId").toString()))) {
                    cIds.append(map.get("courseId").toString());
                    cIds.append(",");
                }
            });
            String courseIds = cIds.toString();
            if(StringUtil.isEmpty(courseIds)) {
                paramsMap.put("courseArray", -1);//该学校当前学期没有课程
            } else {
                if(courseIds.indexOf(",") > 0) {
                    courseIds = courseIds.substring(0, courseIds.length()-1);//去掉最后一个多余的逗号
                    paramsMap.put("courseArray", courseIds);
                }
            }
        } else {
            paramsMap.put("courseArray", -1);//当前用户没有负责的课程或权限
        }

    }


}
