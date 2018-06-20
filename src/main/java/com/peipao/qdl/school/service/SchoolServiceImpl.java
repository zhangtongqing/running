package com.peipao.qdl.school.service;


import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.DateUtils;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.school.dao.LevelDao;
import com.peipao.qdl.school.dao.SchoolDao;
import com.peipao.qdl.school.model.Level;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.service.UserCacheService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private LevelDao levelDao;

    @Autowired
    private SchoolUserService schoolUserService;

    @Autowired
    private SchoolCacheService schoolCacheService;

    @Autowired
    private UserCacheService cacheService;

    @Override
    public School getSchoolBySchoolCode(String schoolCode) throws Exception {
        return schoolDao.querySchoolBySchoolCode(schoolCode);
    }

    @Override
    public UserSchool getParaByUserId(Long userId, Date currDate) throws Exception {
        User user = cacheService.getUserById(userId);
        if (user == null || user.getSchoolId() == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        Semester semester = schoolCacheService.getSemesterBySchoolId(user.getSchoolId());
        if (semester == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }

        UserSchool userSchool = new UserSchool();
        userSchool.setUserId(userId);
        userSchool.setCourseId(user.getCourseId());
        userSchool.setSchoolId(user.getSchoolId());
        userSchool.setSemesterId(semester.getSemesterId());
        userSchool.setUserType(user.getUserType());
        return userSchool;
    }

    @Override
    public Map<String, Object> getSchoolBasic(Long userId) throws Exception {
        User user = schoolUserService.validateTeacher(userId);
        if (user == null || user.getSchoolId() == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        return getSchoolBasicById(user.getSchoolId());
    }

    @Override
    public Map<String, Object> getSchoolBasicById(Long userId, Long schoolId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        return getSchoolBasicById(schoolId);
    }

    private Map<String, Object> getSchoolBasicById(Long schoolId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        School school = schoolCacheService.getSchoolById(schoolId);
        map.put("logoURL", school.getLogoURL());
        map.put("name", school.getName());
        map.put("code", school.getCode());
        map.put("contact", school.getContact());
        map.put("mobile", school.getMobile());
        map.put("schoolId", school.getSchoolId());
        map.put("isAuth", school.getAuth());
        map.put("isCon", school.getIsCon());
        map.put("contractTime", null != school.getContractTime() ? DateUtils.getDateString(school.getContractTime(), DateUtils.PATTERN_DATE) : null);
        return map;
    }

    @Override
    public void updateSemester(Long userId, JSONObject json) throws Exception {
        schoolUserService.validateSchoolManager(userId);// TODO:METEOR.WU  这里有重复查询，可以优化
        UserSchool userSchool = getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }

        String semesterYear = json.getString("SemesterYear");
        String[] years = semesterYear.split("-");
        JSONArray array = json.getJSONArray("data");

        for (int i = 0; i < array.size(); i++) {
            int semesterType = array.getJSONObject(i).getInt("semesterType");
            if (semesterType == 1){
                String startTime = years[0] + "-" + array.getJSONObject(0).getInt("startMonth") + "-" + array.getJSONObject(0).getInt("startDay");
                schoolDao.updateSemester(userSchool.getSchoolId(), semesterYear, semesterType, startTime);
            }else{
                String startTime = years[1] + "-" + array.getJSONObject(1).getInt("startMonth") + "-" + array.getJSONObject(1).getInt("startDay");
                schoolDao.updateSemester(userSchool.getSchoolId(), semesterYear, semesterType, startTime);
            }
        }

        //TODO:METEOR.WU 如果更新的是当前学期，需要更新缓存
    }

    @Override
    public List<Map<String, Object>> getCurrentSemesterDetail(Long userId, String semesterYear) throws Exception {
        User user = schoolUserService.validateSchoolManager(userId);// TODO:METEOR.WU  这里有重复查询，可以优化
        if (StringUtil.isEmpty(semesterYear)) {
            Semester semester = schoolCacheService.getSemesterBySchoolId(user.getSchoolId());
            semesterYear = semester.getSemesterYear();
        }
        return getSchoolCurrentSemesterDetail(user.getSchoolId(), semesterYear);
    }

    private List<Map<String, Object>> getSchoolCurrentSemesterDetail(Long schoolId, String semseterYear) throws Exception {
        List<Map<String, Object>> time = schoolDao.querySemesterTime(schoolId, semseterYear);
        for (int i = 0; i < time.size(); i++) {
            Map<String, Object> map = time.get(i);
            String start = map.get("startTime").toString();
            String[] startArray = start.split("-");
            map.put("startMonth", startArray[1]);
            map.put("startDay", startArray[2]);

            map.remove("startTime");
        }
        return time;
    }

    @Override
    public List<Map<String, Object>> getCurrentSemesterDetailBySchoolId(Long userId, Long schoolId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        Semester semester = schoolCacheService.getSemesterBySchoolId(schoolId);
        return getSchoolCurrentSemesterDetail(semester.getSchoolId(), semester.getSemesterYear());
    }

    @Override
    public JSONObject getLevelList(Long userId) throws Exception {
        schoolUserService.validateOfficialManager(userId);//官方后台功能,验证用户身份
        List<Level> levels =  levelDao.queryLevelList(WebConstants.qdl_level_school_id);
        JSONObject json = new JSONObject();
        for (int i = 0; i < levels.size(); i++) {
            json.put("levelId" + i, levels.get(i).getLevelId());
            json.put("title" + i, levels.get(i).getTitle());
            json.put("name" + i, levels.get(i).getName());
            json.put("length" + i, levels.get(i).getLength());
        }
        json.put("count", levels.size());
        return json;
    }

    @Override
    public void updateLevels(Long userId, JSONObject levels) throws Exception {
//        schoolUserService.validateSchoolManager(userId);
        schoolUserService.validateOfficialManager(userId);//官方后台功能,验证用户身份
        List<Level> levelList = new ArrayList<>();
        int value = 0;
        for (int i = 0; i < 100; i++) {
            if (levels.containsKey("levelId"+i)) {
                Level level = new Level();
                level.setLevelId(levels.getLong("levelId" + i));
                level.setTitle(levels.getString("title" + i));
                level.setName(levels.getString("name" + i));

                if (value > levels.getInt("length" + i)) {
                    throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
                } else {
                    value = levels.getInt("length" + i);
                }
                level.setLength(levels.getInt("length" + i));
                levelList.add(level);
            }else{
                break;
            }
        }
        levelDao.updateLevels(levelList);
    }

    @Override
    public List<Map<String, Object>> getSemesterList(Long userId) throws Exception {
        User user = schoolUserService.validateTeacher(userId);
        List<Map<String, Object>> list = schoolDao.querySemesterListBySchoolId(user.getSchoolId());
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            map.put("startMonth", Integer.valueOf(map.get("startTime").toString().substring(5,7)));
            map.remove("startTime");
        }
        return list;
    }

    @Override
    public Map<String, Object> getSchoolList(Long userId, String city, Integer isCon, int pageindex, int pagesize, String name,String sortName,String sortType) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        Long allcount = schoolDao.querySchoolListCount(city, isCon,name);
        if (allcount == 0) {
            return MyPage.processPage(allcount, pagesize, pageindex, new ArrayList());
        }
        List<Map<String, Object>> list = schoolDao.querySchoolList(city, isCon, (pageindex - 1) * pagesize, pagesize, name,sortName,sortType);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public void updateSchool(Long userId, School school) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        School school1 = schoolCacheService.updateSchool(school);
        school.setCity(school1.getCity());
        school.setCode(school1.getCode());
        school.setName(school1.getName());
    }

    @Override
    public void updateSchoolBySchoolManager(Long userId, School school) throws Exception {
        User user = schoolUserService.validateSchoolManager(userId);
        if (school.getSchoolId().longValue() != user.getSchoolId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        School temp = schoolCacheService.getSchoolById(school.getSchoolId());
        school.setAuth(temp.getAuth());
        school.setMobile(temp.getMobile());
        school.setContractTime(temp.getContractTime());
        school.setContact(temp.getContact());
        schoolCacheService.updateSchool(school);
    }

    @Override
    public List<String> getSemesterYearList(Long schoolId) {
        return schoolDao.getSemesterYearList(schoolId);
    }


    public Semester getSemesterById(Long semesterId) throws Exception {
        return schoolDao.querySemesterById(semesterId);
    }
}
