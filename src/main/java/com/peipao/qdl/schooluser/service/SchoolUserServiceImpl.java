package com.peipao.qdl.schooluser.service;


import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.MD5Util;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.service.ActivityService;
import com.peipao.qdl.course.service.CourseService;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.schooluser.dao.SchoolUserDao;
import com.peipao.qdl.schooluser.model.Student;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/7/3
 **/
@Service
public class SchoolUserServiceImpl implements SchoolUserService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SchoolUserServiceImpl.class);

    @Autowired
    private SchoolUserDao schoolUserDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SchoolCacheService schoolCacheService;

    @Override
    @Transactional
    public Student addStudent2Library(Student student, Long userId) throws Exception {
        User teacher = validateSchoolManager(userId);

        student.setSchoolId(teacher.getSchoolId());
        Student studentTemp = schoolUserDao.queryByStudent(student);
        if (studentTemp != null) {
            throw new BusinessException(ReturnStatus.STUDENT_HAVE_LOAD);
        }

        schoolUserDao.insertStudentLibrary(student);

        student.setKey(student.getUserId());
        student.setStatus(WebConstants.Boolean.FALSE.ordinal());
        return student;
    }

    @Override
    public Map<String,Object> loadStudent2Library(Long userid, Workbook wb) throws Exception {
        User manager = validateSchoolManager(userid);
        Sheet sheet = wb.getSheetAt(0);

        StringBuffer errInfo = new StringBuffer();
        int count = 0;
        for(int i = 1,len = sheet.getLastRowNum(); i<=len; i++) {
            Row row = sheet.getRow(i);
            Student student = new Student();

            if (row == null || row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null) {
                continue;//3个必填项目
            }

            String classname = getValue(row.getCell(0));// 班级信息   选填   汉字字母数字   最大长度 18
            if (StringUtil.isNotEmpty(classname) ) {
                String className = StringUtils.deleteWhitespace(classname);
                if (className.length() <= 18 && ValidateUtil.checkClassName(className)) {
                    student.setClassname(className);//班级
                } else {
                    log.info("第{}行 第{}列 格式错误,", i+1, 1);
                    String str = "第"+(i+1)+"行 第1列 格式错误\n";
                    errInfo.append(str);
                    continue;
                }
            }

            String studentno = getValue(row.getCell(1));// 学号     必填     字母数字    最大长度20
            if (StringUtil.isNotEmpty(studentno)) {
                String studentNO = StringUtils.deleteWhitespace(studentno);
                if (ValidateUtil.checkStudentNO(studentNO) && studentNO.length() <= 20) {// 学号必须是字母数字，20位
                    student.setStudentNO(studentNO);
                } else {
                    log.info("第{}行 第{}列 格式错误,", i+1, 2);
                    String str = "第"+(i+1)+"行 第2列 格式错误\n";
                    errInfo.append(str);
                    continue;
                }
            } else {
                continue;
            }

            String username = getValue(row.getCell(2));// 姓名     必填     汉字字母数字    最大长度10
            if (StringUtil.isNotEmpty(username)) {
                String userName = StringUtils.deleteWhitespace(username);
                if (ValidateUtil.checkUserName(userName) && userName.length() <= 10) {// 姓名汉字字母数字，10位
                    student.setUsername(userName);//
                } else {
                    log.info("第{}行 第{}列 格式错误,", i+1, 3);
                    String str = "第"+(i+1)+"行 第3列 格式错误\n";
                    errInfo.append(str);
                    continue;
                }
            } else {
                continue;
            }

            String sex = getValue(row.getCell(3));//性别非空 男 or 女
            if (StringUtil.isNotEmpty(sex) && (sex.equals("男") || sex.equals("女"))) {
                student.setSex(sex.endsWith("女") ? (byte) 0 :1);
            } else {
                log.info("第{}行 第{}列 格式错误,", i+1, 4);
                String str = "第"+(i+1)+"行 第4列 格式错误\n";
                errInfo.append(str);
                continue;
            }

            String admission = getValue(row.getCell(4));// 入学时间非必填，不为空，数字&年&月：eg2017年11月
            if (StringUtil.isNotEmpty(admission)) {
                if (ValidateUtil.isChineseOrDigitalOrLetter(admission) && admission.length() <= 8) {
                    student.setAdmission(admission);
                }
            }

            student.setSchoolId(manager.getSchoolId());
            Student studentTemp = schoolUserDao.queryByStudent(student);
            if (studentTemp != null) {
                updateWhenLoad(studentTemp, student);
                continue;
            }
            schoolUserDao.insertStudentLibrary(student);
            count++;

        }

        log.info("错误信息 ={}", errInfo.toString());
        String info = String.format("已经成功导入%d条学生数据", count);
        Map<String,Object> msg = new HashMap<String,Object>();
        msg.put("count",count);
        msg.put("msg",info);
        if(!errInfo.toString().equals("")){
            msg.put("error",errInfo.toString());
        }
        return msg;
    }

    private void updateWhenLoad(Student student1, Student student2) throws Exception {
        if (student1.getUsername().equals(student2.getUsername())
                && student1.getStudentNO().equals(student2.getStudentNO())
                && student1.getAdmission() != null && student1.getAdmission().equals(student2.getAdmission())
                && student1.getSex() != null && student1.getSex().byteValue() == student2.getSex()
                && student1.getClassname() != null && student1.getClassname().equals(student2.getClassname())) {
            return;
        }

        student2.setUserId(student1.getUserId());
        updateStudent(student2);
        if (student1.getStatus() == WebConstants.Boolean.TRUE.ordinal()) {
            updateStudentInUser(student1, student2);
        }
    }

    /*
     * 更新学生对于的user表信息
     */
    private void updateStudentInUser(Student student1, Student student2) throws Exception {
        if (student1.getuId() != null) {
            User userTemp = new User();
            userTemp.setUserId(student1.getuId());
            userTemp.setUsername(student2.getUsername());
            userTemp.setSex(student2.getSex());
            userTemp.setAdmission(student2.getAdmission());
            userTemp.setClassname(student2.getClassname());
            userCacheService.updateUser(userTemp);
        }
    }

    private String getValue(Cell cell) {

        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            if (cell.getCellStyle().getDataFormat() == 57 || cell.getCellStyle().getDataFormat() == 31|| cell.getCellStyle().getDataFormat() == 14) {// 处理日期格式、时间格式
                // xxx年xxx月  2016-01-01  xxxx年xx月xx日
                Date date = cell.getDateCellValue();
                return sdf.format(date);
            } else if (cell.getCellStyle().getDataFormat() == 58) {
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                return sdf.format(date);
            } else {
                double value = cell.getNumericCellValue();
                CellStyle style = cell.getCellStyle();
                DecimalFormat format = new DecimalFormat();
                String temp = style.getDataFormatString();
                // 单元格设置成常规
                if (temp.equals("General")) {
                    format.applyPattern("#");
                }
                return format.format(value);
            }
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    @Override
    @Transactional
    public Long addTeacher(User user, Long userId) throws Exception {
        User schoolmanager = validateSchoolManager(userId);
        User temp = new User();
        temp.setNickname(user.getNickname());

        User userByUser = userService.getUserByUser(temp);
        if (null != userByUser && !userByUser.getUserId().equals(user.getUserId())) {//账号不允许重复，必须唯一，涉及登陆
            throw new BusinessException(ReturnStatus.USER_NICKNAME_EXIST);
        }
        if(schoolmanager.getUserType() == UserTypeEnum.SCHOOLMANAGER.getValue()) {
            user.setSchoolId(schoolmanager.getSchoolId());//只有本校管理员才能添加本校老师
        }
        user.setPassword(MD5Util.getMD5("000000"));
        Map<String, Object> map = userService.createUser(user);
        return Long.valueOf(map.get("userId").toString());
    }

    @Override
    public void updateTeacher(Long userId, User teacher) throws Exception {
        User[] teachers = validateSchoolManagerAndTeacher(userId, teacher.getUserId());
        if (!teacher.getNickname().equals(teachers[1].getNickname())) {//账号不允许重复，必须唯一，涉及登陆
            User temp = new User();
            temp.setNickname(teacher.getNickname());

            User userByUser = userService.getUserByUser(temp);
            if (null != userByUser && !userByUser.getUserId().equals(teacher.getUserId())) {
                throw new BusinessException(ReturnStatus.USER_NICKNAME_EXIST);
            }
        }
        userCacheService.updateUser(teacher);
    }

    @Override
    public Map<String, Object> user2Map(User user, boolean isNew) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", user.getUserId());
        map.put("userId", user.getUserId());
        map.put("sex", user.getSex());
        map.put("mobile", user.getMobile());
        map.put("username", user.getUsername());
        map.put("nickname", user.getNickname());
        map.put("userType", user.getUserType());

        String names = "";
        if (!isNew) {
            List<Map<String, Object>> courseNameAndId = courseService.getCourseNameAndId(user.getUserId());
            if (courseNameAndId.size() > 0) {
                for (Map<String, Object> map1 : courseNameAndId) {
                    names += map1.get("name").toString() + ",";
                }
                names = names.substring(0,names.length() -1);
            }
        }
        map.put("name", names);
        return map;
    }

    @Override
    public List<Map<String, Object>> getNumberGroupByClass(Map<String, Object> paramsMap) throws Exception {
        return schoolUserDao.getNumberGroupByClass(paramsMap);
    }

    /*
             * web端老师更新学生库里信息，包含判断等业务逻辑
             */
    @Transactional
    @Override
    public void updateStudent(Student student, Long userId) throws Exception {
        Map<String, Object> map = validateSchoolManagerAndStudent(userId, student.getUserId());
        Student studentTemp = (Student) map.get("student");
        student.setStatus(studentTemp.getStatus());

        if (StringUtil.isNotEmpty(student.getStudentNO())) {
            if (studentTemp.getStatus() == WebConstants.Boolean.TRUE.ordinal()
                    && !student.getStudentNO().equals(studentTemp.getStudentNO())) {// 已经认证，不能修改学号
                throw new BusinessException(ReturnStatus.REGISTERED_STUDENT_NO_MODIFY);
            }
            Student search = new Student();
            search.setSchoolId(studentTemp.getSchoolId());
            search.setStudentNO(student.getStudentNO());

            Student studentTemp1 = schoolUserDao.queryByStudent(search);
            if (studentTemp1 != null && studentTemp1.getUserId().longValue() != student.getUserId()) {//学号已经存在
                throw new BusinessException(ReturnStatus.STUDENTNO_EXIST);
            }

        }
        updateStudent(student);
        if (studentTemp.getStatus() == WebConstants.Boolean.TRUE.ordinal()) {// 如果已经认证，那么老实更新的信息需要同步到学生表t_user
            updateStudentInUser(studentTemp, student);
        }
        if (studentTemp.getuId() != null) {
            User user = userCacheService.getUserById(studentTemp.getuId());
            student.setMobile(user.getMobile());
        }

    }

    /**
     * 直接更新数据库-学术库表
     * @param student 学生信息
     */
    @Transactional
    @Override
    public void updateStudent(Student student) throws Exception {
        schoolUserDao.updateStudent(student);
    }

    @Override
    public void resetTeacherPassword(Long userId, Long teacherId) throws Exception {
        User[] teachers = validateSchoolManagerAndTeacher(userId, teacherId);
        String md5password = MD5Util.getMD5("000000");
        String password = StringUtil.decode(md5password);

        userService.updatePassword(teacherId, password, teachers[1].getPassword());
    }

    @Override
    public void deleteStudent(Long userId, Long studentId) throws Exception {
        Map<String, Object> map = validateTeacherAndStudent(userId, studentId);
        Student student = (Student)map.get("student");
        if (student.getStatus() == WebConstants.Boolean.TRUE.ordinal()) {
            throw new BusinessException(ReturnStatus.USER_ALREADY_AUTH);
        }
        schoolUserDao.deleteStudent(studentId);
    }

    @Transactional
    @Override
    public Student unbind(Long userId, Long studentId) throws Exception {
        Map<String, Object> map = validateTeacherAndStudent(userId, studentId);
        Student userTemp = (Student) map.get("student");

        Student unbindStudent = new Student();
        unbindStudent.setUserId(studentId);
        unbindStudent.setStatus(WebConstants.Boolean.FALSE.ordinal());
        updateStudent(unbindStudent);

        User user = new User();
        user.setSchoolId(userTemp.getSchoolId());
        user.setStudentNO(userTemp.getStudentNO());
        User temp = userService.getUserByUser(user);
        if (temp == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }

        user.setUserId(temp.getUserId());
        user.setToken("null");
        userService.setUserParamToNull(user);

        userTemp.setKey(userTemp.getUserId());
        userTemp.setMobile(temp.getMobile());
        userTemp.setStatus(WebConstants.Boolean.FALSE.ordinal());
        return userTemp;
    }

    @Override
    public Map<String, Object> getStudentList(Long userId, JSONObject json, int pageindex, int pagesize) throws Exception {
        User user = userService.validateUser(userId);
        int status = json.getInt("status");
        String username = json.containsKey("key")  && json.containsKey("params") && json.getInt("key") == 0 ? json.getString("params") : null;
        String studentNO = json.containsKey("key") && json.containsKey("params") && json.getInt("key") == 1 ? json.getString("params") : null;
        String mobile = json.containsKey("key") && json.containsKey("params") && json.getInt("key") == 2 ? json.getString("params") : null;
        String classname = json.containsKey("classname") && StringUtil.isNotEmpty(json.getString("classname")) ? "'"+json.getString("classname").replace(",","','")+"'" : null;
        String unselect = null;
        if (classname != null && classname.length() > 0 && classname.contains("未指定")) {
            unselect = "未指定";
        }

        List<Student> list =  schoolUserDao.getStudentList(user.getSchoolId(), status, username, studentNO, mobile,
                classname, unselect, (pageindex-1)*pagesize, pagesize);
        list.forEach(i->i.setKey(i.getUserId()));
        Long allcount = schoolUserDao.getStudentListCount(user.getSchoolId(), status, username, studentNO, mobile, classname, unselect);

        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Map<String, Object> downloadStudentList(Long userId, JSONObject json) throws Exception {
        User user = userService.validateUser(userId);
        int status = json.getInt("status");
        String username = json.containsKey("key") && json.containsKey("params") && json.getInt("key") == 0 ? json.getString("params") : null;
        String studentNO = json.containsKey("key") && json.containsKey("params") && json.getInt("key") == 1 ? json.getString("params") : null;
        String mobile = json.containsKey("key") && json.containsKey("params") && json.getInt("key") == 2 ? json.getString("params") : null;
        String classname = json.containsKey("classname") && StringUtil.isNotEmpty(json.getString("classname")) ? "'"+json.getString("classname").replace(",","','")+"'" : null;
        String unselect = null;
        if (classname != null && classname.length() > 0 && classname.contains("未指定")) {
            unselect = "未指定";
        }
        List<Map<String, Object>> list = schoolUserDao.getAllStudentList(user.getSchoolId(), status, username, studentNO, mobile, classname, unselect);

        for (Map<String, Object> map : list) {
            if (map.containsKey("sex") && map.get("sex") != null) {
                if (Integer.parseInt(map.get("sex").toString()) == 1) {
                    map.put("sex", "男");
                } else {
                    map.put("sex", "女");
                }
            }else {
                map.put("sex", "");
            }
            if (Integer.parseInt(map.get("status").toString()) == 1) {
                map.put("status", "已认证");
            } else {
                map.put("status", "未认证");
            }
        }
        School school = schoolCacheService.getSchoolById(user.getSchoolId());
        Map<String, Object> map = new HashMap();
        map.put("data", list);
        map.put("name", school.getName());
        return map;
    }

    @Override
    public void deleteTeacher(Long userId, Long teacherId) throws Exception {
        if (userId.longValue() == teacherId) {
            throw new BusinessException(ReturnStatus.CAN_NOT_DELETE_SELF);
        }
        User[] teachers = validateSchoolManagerAndTeacher(userId, teacherId);
        if(courseService.countCourseByUserId(teacherId) > 0) {
            throw new BusinessException(ReturnStatus.TEACHER_HAVE_BUSINESS);
        }
//        if (teachers[1].getCourseId() != null) {//此处获得老师负责的课程有问题。。。
//            throw new BusinessException(ReturnStatus.TEACHER_HAVE_BUSINESS);
//        }
        if (activityService.countActivityByUserId(teacherId) > 0) {
            throw new BusinessException(ReturnStatus.TEACHER_HAVE_BUSINESS);
        }

        userCacheService.deleteUserById(teacherId );
    }

    @Override
    public Map<String, Object> getTeacherList(Long userId, int pageindex, int pagesize) throws Exception {
        User user = validateSchoolManager(userId);
        Map<String, Object> map = userService.getTeacherBySchoolId(user.getSchoolId(), pageindex, pagesize);
        return map;
    }
    @Override
    public Map<String, Object> getTeacherInfoById(Long userId) throws Exception {
        User user = userService.validateSchoolUser(userId);
        Map<String, Object> map = userService.getTeacherInfoById(userId);
        return map;
    }

    @Override
    public User validateTeacher(Long userId) throws Exception {
        User teacher = userService.validateUser(userId);
        if (teacher.getUserType() != UserTypeEnum.TEACHER.getValue() && teacher.getUserType() != UserTypeEnum.SCHOOLMANAGER.getValue()
                && teacher.getUserType() != UserTypeEnum.OFFICIALMANAGER.getValue()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        return teacher;
    }

    @Override
    public User validateSchoolManager(Long userId) throws Exception {
        User teacher = userService.validateUser(userId);
        if ( teacher.getUserType() != UserTypeEnum.SCHOOLMANAGER.getValue() && teacher.getUserType() != UserTypeEnum.OFFICIALMANAGER.getValue()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        return teacher;
    }

    @Override
    public User validateOfficialManager(Long userId) throws Exception {
        User teacher = userService.validateUser(userId);
        if ( teacher.getUserType() != UserTypeEnum.OFFICIALMANAGER.getValue()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        return teacher;
    }

    @Override
    public Map<String, Object> validateTeacherAndStudent(Long teacherId, Long studentId) throws Exception{
        User teacher = validateTeacher(teacherId);
        Student student = validateStudent(studentId);
        if (teacher.getSchoolId() != student.getSchoolId().longValue()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("student", student);
        return map;
    }

    private User[] validateSchoolManagerAndTeacher(long userId, Long teacherId) throws Exception {
        User manager = validateSchoolManager(userId);
        User teacher = validateTeacher(teacherId);
        if(null != manager.getUserType() && manager.getUserType().intValue() != UserTypeEnum.OFFICIALMANAGER.getValue()) {
            if (manager.getSchoolId().longValue() != teacher.getSchoolId()) {
                throw new BusinessException(ReturnStatus.NO_PERMISSION);
            }
        }

        User[] ret = {manager, teacher};
        return ret;
    }

    private Map<String, Object> validateSchoolManagerAndStudent(long teacherId, long studentId) throws Exception {
        User manager = validateSchoolManager(teacherId);
        Student student = validateStudent(studentId);
        if (manager.getSchoolId() != student.getSchoolId().longValue()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("manager", manager);
        map.put("student", student);
        return map;
    }

    private Student validateStudent(Long studentId) throws Exception{
        Student student = new Student();
        student.setUserId(studentId);
        student = schoolUserDao.queryByStudent(student);
        if (student == null) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }
        return student;
    }

    @Override
    public List<Map<String, Object>> getTeacherNameList(Long userId) throws Exception {
        User user = validateSchoolManager(userId);
        return schoolUserDao.getTeacherNameList(user.getSchoolId());
    }

    @Override
    public Map<String, Object> getTeacherListBySchoolId(Long userId, Long schoolId, int pageindex, int pagesize) throws Exception {
        validateOfficialManager(userId);
        Map<String, Object> map = userService.getTeacherBySchoolId(schoolId, pageindex, pagesize);
        return map;
    }

    @Override
    public Student getByStudent(Student student) throws Exception {
        return schoolUserDao.queryByStudent(student);
    }

    @Override
    public List<String> getClassList(Long userId, String classname) throws Exception {
        User user = validateTeacher(userId);
        List<String> list = schoolUserDao.getClassList(user.getSchoolId(), classname);
        list.add(0,"未指定");
        return list;
    }
}
