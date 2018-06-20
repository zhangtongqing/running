package com.peipao.qdl.school.service;


import com.peipao.qdl.school.model.Level;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import net.sf.json.JSONObject;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public interface SchoolService {

    School getSchoolBySchoolCode(String schoolCode) throws Exception;

    UserSchool getParaByUserId(Long userId, Date currDate) throws Exception;

    Semester getSemesterById(Long semesterId) throws Exception;

    Map<String, Object> getSchoolBasic(Long userId) throws Exception;

    Map<String, Object> getSchoolBasicById(Long userId, Long schoolId) throws Exception;

    void updateSemester(Long userId, JSONObject array) throws Exception;

    List<Map<String, Object>> getCurrentSemesterDetail(Long userId, String semesterYear) throws Exception;

    List<Map<String, Object>> getCurrentSemesterDetailBySchoolId(Long userId, Long schoolId) throws Exception;

    List<Map<String, Object>> getSemesterList(Long userId) throws Exception;

    Map<String, Object> getSchoolList(Long userId, String city, Integer isCon, int pageindex, int pagesize, String s, String sortName, String name) throws Exception;

    void updateSchool(Long userId, School school) throws Exception;

    void updateSchoolBySchoolManager(Long userId, School school) throws Exception;

    List<String> getSemesterYearList(Long schoolId);

    JSONObject getLevelList(Long userId) throws Exception;

    void updateLevels(Long userId, JSONObject levels) throws Exception;
}
