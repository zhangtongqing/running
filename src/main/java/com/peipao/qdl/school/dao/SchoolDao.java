package com.peipao.qdl.school.dao;


import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Repository
public interface SchoolDao {
    School querySchoolBySchoolCode(@Param("schoolCode") String schoolCode) throws Exception;

    Semester querySemesterById(@Param("semesterId") Long semesterId) throws Exception;

    School querySchoolById(@Param("schoolId") Long schoolId) throws Exception;

    List<Map<String, Object>> querySchoolList(@Param("city") String city, @Param("isCon") Integer isCon, @Param("from") int from, @Param("num") int num, @Param("name") String name,@Param("sortName") String sortName,@Param("sortType") String sortType) throws Exception;

    Long querySchoolListCount(@Param("city") String city, @Param("isCon") Integer isCon, @Param("name") String name) throws Exception;

    void updateSemester(@Param("schoolId") Long schoolId, @Param("semesterYear") String semesterYear, @Param("semesterType") int semesterType, @Param("startTime") String startTime)throws Exception;
    List<Map<String, Object>> querySemesterTime(@Param("schoolId") Long schoolId, @Param("semesterYear") String semesterYear) throws Exception;

    List<Map<String, Object>> querySemesterListBySchoolId(@Param("schoolId") Long schoolId) throws Exception;

    void updateSchool(School school) throws Exception;

    Semester getSemesterBySchoolId(@Param("schoolId") Long schoolId) throws Exception;

    List<String> getSemesterYearList(@Param("schoolId") Long schoolId);
}
