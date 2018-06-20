package com.peipao.qdl.schooluser.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.ExcelExport;
import com.peipao.framework.util.HttpRequestUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.schooluser.model.Student;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.user.model.User;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * @author meteor.wu
 * @since 2017/6/30
 **/
@Api(value = "/schoolUser",description = "登录api")
@RestController
@RequestMapping("/schoolUser")
public class SchoolUserController {

    @Autowired
    private SchoolUserService schoolUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SchoolCacheService schoolCacheService;

    @Register
    @RequestMapping(value = "/pc/addStudentToLibrary", method = RequestMethod.POST)
    @SystemControllerLog(description = "添加学生到学生库")
    @ApiOperation("添加学生到学生库--40--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.STUDENT_HAVE_LOAD.value, message = ReturnConstant.STUDENT_HAVE_LOAD.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Student> addStudentToLibrary(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "token") @RequestParam Long userId,
            @ApiParam(required = true, value = "必须包含 username,studentNO") @RequestBody Student student,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (StringUtil.isEmpty(student.getStudentNO()) || StringUtil.isEmpty(student.getUsername())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        String userName = StringUtils.deleteWhitespace(student.getUsername()); //去除所有空格
        if(!ValidateUtil.checkUserName(userName)){
            return Response.fail(ReturnStatus.USERNAME_FORMAT_ERROR);
        }
        if(!StringUtil.isEmpty(student.getClassname())){
            String className = StringUtils.deleteWhitespace(student.getClassname());
            if(!ValidateUtil.checkClassName(className)){
                return Response.fail(ReturnStatus.CLASSNAME_FORMAT_ERROR);
            }
        }

        return Response.success(schoolUserService.addStudent2Library(student, userId));
    }

    @Register
    @RequestMapping(value = "/pc/loadStudentToLibrary", method = RequestMethod.POST)
    @SystemControllerLog(description = "导入学生到学生库")
    @ApiOperation("导入学生到学生库")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.STUDENT_HAVE_LOAD.value, message = ReturnConstant.STUDENT_HAVE_LOAD.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> loadStudentToLibrary(
            @RequestParam String token,
            @RequestParam Long userId,
            @RequestBody JSONObject json,
            @RequestParam String sign) throws Exception {

        String url = json.getString("url");
        String fileType = url.contains(".xlsx") ? "xlsx" : "xls";
        InputStream in = HttpRequestUtil.httpRequestIO(url);

        Workbook wb;
        if (fileType.toLowerCase().equals("xls")) {
            wb = new HSSFWorkbook(in);
        }else if(fileType.toLowerCase().equals("xlsx")) {
            wb = new XSSFWorkbook(in);
        }else {
            return Response.fail(ReturnStatus.FILE_TYPE_ERROR);
        }
        Map<String,Object> msgInfo = schoolUserService.loadStudent2Library(userId, wb);
        return Response.success(msgInfo);
    }

    @Register
    @RequestMapping(value = "/pc/localToLibrary", method = RequestMethod.POST)
    @SystemControllerLog(description = "本地接口测试-MultipartFile")
    @ApiOperation("本地接口测试")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.STUDENT_HAVE_LOAD.value, message = ReturnConstant.STUDENT_HAVE_LOAD.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> localToLibrary(
            @RequestParam String token,
            @RequestParam Long userId,
            @RequestParam MultipartFile file,
            @RequestParam String sign) throws Exception {

        String fileName = file.getOriginalFilename();
        String fileType = fileName.contains(".xlsx") ? "xlsx" : "xls";

        Workbook wb;
        if (fileType.toLowerCase().equals("xls")) {
            wb = new HSSFWorkbook(file.getInputStream());
        }else if(fileType.toLowerCase().equals("xlsx")) {
            wb = new XSSFWorkbook(file.getInputStream());
        }else {
            return Response.fail(ReturnStatus.FILE_TYPE_ERROR);
        }
        schoolUserService.loadStudent2Library(userId, wb);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/pc/getStudentList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询学生列表")
    @ApiOperation("查询学生列表(设置-学生库管理)--41--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getStudentList(@ApiParam(required = true,value = "token") @RequestParam String token,
                                      @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                      @ApiParam(required = true,value = "json:status,pageindex, pagesize") @RequestBody JSONObject json,
                                      @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        int[] page = MyPage.isValidParams(json);
        Map<String, Object> ret = schoolUserService.getStudentList(userId, json, page[0], page[1]);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/pc/getStudentItem"},method = {RequestMethod.POST})
    @ApiOperation("查询学生详情(设置-学生库管理)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> getStudentItem(@ApiParam(required = true,value = "token") @RequestParam String token,
                                      @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                      @ApiParam(required = true,value = "json:userId") @RequestBody Student student,
                                      @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        Student studentdb = schoolUserService.getByStudent(student);
        studentdb.setKey(studentdb.getUserId());
        return Response.success(studentdb);
    }

    @Register
    @RequestMapping(value = {"/pc/downloadStudentList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询学生列表")
    @ApiOperation("导出学生列表(设置-学生库管理)--41--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> downloadStudentList(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:status,pageindex, pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        Map<String, Object> map = schoolUserService.downloadStudentList(userId, json);
        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("data");
        if (list != null && list.size() > 0) {
            String path = createExcel(response, request, list, map.get("name").toString());
            json.put("path", path);
            json.put("size", list.size());
        } else {
            json.put("size", 0);
        }
        json.remove("key");
        json.remove("params");
        json.remove("status");
        return Response.success(json);
    }

    private String createExcel(HttpServletResponse response, HttpServletRequest request, List<Map<String, Object>> list, String name) throws Exception {
        HSSFWorkbook wbarray = new HSSFWorkbook();
        HSSFSheet sheet = wbarray.createSheet( "第1页" );
        HSSFRow row = null;
        HSSFCell cell = null;

        int totalnum = list.size();// 总共多少条数据list
        for ( int r = 0; r < totalnum + 1; r++ ) {
            row = sheet.createRow( r );
            row.setHeight( ( short ) 350 );
            for ( int col = 0; col < 8; col++ ) {
                cell = row.createCell( col );
            }
        }

        for ( int j = 0; j <= totalnum; j++ ) {
            row = sheet.getRow( j );
            cell = row.getCell( 0 );
            if ( j == 0 )
                cell.setCellValue( "序号" );
            else{
                cell.setCellValue(j);
            }

            cell = row.getCell( 1 );
            if ( j == 0 )
                cell.setCellValue( "班级" );
            else{
                if (list.get(j - 1).get("classname") != null) {
                    cell.setCellValue( list.get(j-1).get("classname").toString() );
                }
            }

            cell = row.getCell( 2 );
            if ( j == 0 )
                cell.setCellValue( "学号" );
            else
                cell.setCellValue( list.get(j-1).get("student_no").toString() );

            cell = row.getCell( 3 );
            if ( j == 0 )
                cell.setCellValue( "姓名" );
            else
                cell.setCellValue( list.get(j-1).get("username").toString() );

            cell = row.getCell( 4 );
            if ( j == 0 )
                cell.setCellValue( "性别" );
            else {
                if (list.get(j - 1).get("sex") != null) {
                    cell.setCellValue(list.get(j - 1).get("sex").toString());
                }
            }

            cell = row.getCell( 5 );
            if ( j == 0 )
                cell.setCellValue( "入学时间" );
            else {
                if (list.get(j - 1).get("admission") != null ) {
                    cell.setCellValue( list.get(j-1).get("admission").toString() );
                }
            }

            cell = row.getCell( 6 );
            if ( j == 0 )
                cell.setCellValue( "认证状态" );
            else {
                cell.setCellValue( list.get(j-1).get("status").toString() );
            }

            cell = row.getCell( 7 );
            if ( j == 0 )
                cell.setCellValue( "认证帐号" );
            else {
                if (list.get(j - 1).get("mobile") != null) {
                    cell.setCellValue( list.get(j-1).get("mobile").toString() );
                }
            }

        }
        return ExcelExport.createExcel(response, request, name +"_学生库成员", wbarray);
    }

    @Register
    @RequestMapping(value = "/pc/updateStudent", method = RequestMethod.POST)
    @SystemControllerLog(description = "编辑学生")
    @ApiOperation("编辑学生(设置-校园设置-学生设置-编辑)--43--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> updateStudent(@ApiParam(required = true, value = "token") @RequestParam String token,
                               @ApiParam(required = true, value = "token") @RequestParam Long userId,
                               @ApiParam(required = true, value = "userId必填，其他选题") @RequestBody Student student,
                               @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (student.getUserId() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }

        schoolUserService.updateStudent(student, userId);

        return Response.success(student);
    }

    @Register
    @RequestMapping(value = "/pc/deleteStudent", method = RequestMethod.POST)
    @SystemControllerLog(description = "删除学生")
    @ApiOperation("删除学生)--42--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> deleteStudent(@ApiParam(required = true, value = "token") @RequestParam String token,
                                  @ApiParam(required = true, value = "操作人的userId") @RequestParam Long userId,
                                  @ApiParam(required = true, value = "json:studentId") @RequestBody JSONObject json,
                                  @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        Long studentId = json.getLong("userId");
        schoolUserService.deleteStudent(userId, studentId);
        return Response.success(studentId);
    }
    @Register
    @RequestMapping(value = "/pc/unbind", method = RequestMethod.POST)
    @SystemControllerLog(description = "解除绑定")
    @ApiOperation("解除绑定)--42--ok")
    public Response<?> unbind(@ApiParam(required = true, value = "token") @RequestParam String token,
                                  @ApiParam(required = true, value = "操作人的userId") @RequestParam Long userId,
                                  @ApiParam(required = true, value = "json:studentId") @RequestBody JSONObject json,
                                  @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        Long studentId = json.getLong("userId");
        return Response.success(schoolUserService.unbind(userId, studentId));
    }

/************************************************ 以下学生库班级信息维护 ****************************************************/
    @Register
    @RequestMapping(value = "/pc/getClassList", method = RequestMethod.POST)
//    @SystemControllerLog(description = "专业班级检索")
    @ApiOperation("专业班级检索")
    public Response<?> getClassList(@ApiParam(required = true, value = "token") @RequestParam String token,
                                    @ApiParam(required = true, value = "操作人的userId") @RequestParam Long userId,
                                    @ApiParam(required = true, value = "json:studentId") @RequestBody JSONObject json,
                                    @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        String classname = json.containsKey("classname") ? json.getString("classname") : null;
        return Response.success(schoolUserService.getClassList(userId, classname));
    }


    @Register
    @RequestMapping(value = "/pc/getClassNameList", method = RequestMethod.POST)
//    @SystemControllerLog(description = "专业班级检索(带注册人数)")
    @ApiOperation("专业班级检索(带注册人数)")
    public Response<?> getClassNameList(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "操作人的userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = false, value = "json:classname") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        String classname = json.containsKey("classname") ? json.getString("classname") : null;
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        List<String> classNameArray = new ArrayList<>();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("schoolId", userSchool.getSchoolId());
        if(StringUtil.isNotEmpty(classname)) {
            paramsMap.put("classname", classname);
        }
        List<Map<String, Object>> classNameList = schoolUserService.getNumberGroupByClass(paramsMap);

        classNameList.forEach(map -> {
            StringBuffer classNameString = new StringBuffer();
            if(null == map.get("classname")) {
                classNameString.append("未指定");
            } else {
                classNameString.append(map.get("classname").toString());
            }
            classNameString.append("[");
            if(null == map.get("num")) {
                classNameString.append("0");
            } else {
                classNameString.append(map.get("num").toString());
            }
            classNameString.append("/");
            classNameString.append(map.get("numAll").toString());
            classNameString.append("]");
            classNameArray.add(classNameString.toString());
        });
        return Response.success(classNameArray);
    }

/************************************************ 以下老师信息维护 ****************************************************/
    @Register
    @RequestMapping(value = {"/pc/getTeachList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询老师列表")
    @ApiOperation("查询老师列表(设置-老师设置)--36--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> getTeachList(@ApiParam(required = true,value = "token") @RequestParam String token,
                                  @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                  @ApiParam(required = true,value = "pageindex, pagesize") @RequestBody JSONObject json,
                                  @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> map = schoolUserService.getTeacherList(userId, page[0], page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/pc/getTeacherNameList"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询老师名称列表")
    @ApiOperation("查询老师名称列表(课程设置-添加课程)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> getTeacherNameList(@ApiParam(required = true,value = "token") @RequestParam String token,
                                  @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                  @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        List<Map<String, Object>> ret = schoolUserService.getTeacherNameList(userId);
        return Response.success(ret);
    }


    @Register
    @RequestMapping(value = "/pc/addTeacher", method = RequestMethod.POST)
    @SystemControllerLog(description = "添加老师")
    @ApiOperation("添加老师--37--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
            @ApiResponse(code = ReturnConstant.USER_ALREADY_EXIST.value, message = ReturnConstant.USER_ALREADY_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> addTeacher(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "必须包含username,nickname,sex") @RequestBody User user
    ) throws Exception {
        if (StringUtil.isEmpty(user.getUsername())
                || StringUtil.isEmpty(user.getNickname())
                || null == user.getSex()
                || user.getMobile() == null) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isMobile(user.getMobile())) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }

        Long id = schoolUserService.addTeacher(user, userId);
        user.setUserId(id);
        return Response.success(schoolUserService.user2Map(user, true));
    }

    @Register
    @RequestMapping(value = "/pc/editTeacher", method = RequestMethod.POST)
    @SystemControllerLog(description = "编辑老师")
    @ApiOperation("添加老师--37--ok")
    public Response<Map<String, Object>> editTeacher(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "必须包含userId,username,nickname,sex") @RequestBody User user
    ) throws Exception {
        if (null == user.getUserId()
                || StringUtil.isEmpty(user.getUsername())
                || StringUtil.isEmpty(user.getNickname())
                || user.getSex() == null
                || user.getMobile() == null) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isMobile(user.getMobile())) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }

        schoolUserService.updateTeacher(userId, user);
        return Response.success(schoolUserService.user2Map(user, false));
    }

    @Register
    @RequestMapping(value = "/pc/resetTeacherPassword", method = RequestMethod.POST)
    @SystemControllerLog(description = "重置老师密码")
    @ApiOperation("重置老师密码(老师设置-重置密码)--39--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> resetTeacherPassword(@ApiParam(required = true, value = "token") @RequestParam String token,
                                            @ApiParam(required = true, value = "管理员ID") @RequestParam Long userId,
                                            @ApiParam(required = true, value = "userId") @RequestBody JSONObject json,
                                            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("userId") || !ValidateUtil.isDigits(json.getString("userId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long teacherId = json.getLong("userId");
        schoolUserService.resetTeacherPassword(userId, teacherId);
        return Response.success();
    }

    @Register
    @RequestMapping(value = "/pc/deleteTeacher", method = RequestMethod.POST)
    @SystemControllerLog(description = "删除老师")
    @ApiOperation("(删除老师)--38--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> deleteTeacher(@ApiParam(required = true, value = "token") @RequestParam String token,
                                     @ApiParam(required = true, value = "操作人的userId") @RequestParam Long userId,
                                     @ApiParam(required = true, value = "userId") @RequestBody JSONObject json,
                                     @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        Long teacherId = json.getLong("userId");
        // 只能删除本学校老师
        schoolUserService.deleteTeacher(userId, teacherId);
        return Response.success(teacherId);
    }

    /********************************************************************************************/
    @Register
    @RequestMapping(value = {"/pc/getTeachListBySchoolId"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "根据schoolId查询老师列表")
    @ApiOperation("根据schoolId查询老师列表(官方后台--校园管理--学校信息--教师用户)--5--ok")
    public Response<?> getTeachListBySchoolId(@ApiParam(required = true,value = "token") @RequestParam String token,
                                              @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                              @ApiParam(required = true,value = "pageindex, pagesize") @RequestBody JSONObject json,
                                              @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("schoolId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("schoolId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> map = schoolUserService.getTeacherListBySchoolId(userId, json.getLong("schoolId"), page[0], page[1]);
        return Response.success(map);
    }




    @Register
    @RequestMapping(value = {"/app/getTeacherInfo"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询登录老师信息")
    @ApiOperation("查询登录老师信息(我的-老师的个人信息)")
    public Response<?> getTeacherInfo(
        @ApiParam(required = true,value = "token") @RequestParam String token,
        @ApiParam(required = true,value = "userId") @RequestParam Long userId,
        @ApiParam(required = true,value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        School school = schoolCacheService.getSchoolById(userSchool.getSchoolId());
        String schoolName = school.getName();
        Map<String, Object> map = schoolUserService.getTeacherInfoById(userId);
        map.put("schoolName", schoolName);
        return Response.success(map);
    }
}
