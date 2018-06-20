package com.peipao.qdl.course.dao;


import com.peipao.qdl.course.model.CourseChooseRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author meteor.wu
 * @since 2017/7/14
 **/
@Repository
public interface CourseChooseRecordDao {
    void insertCourseChooseRecord(CourseChooseRecord courseChooseRecord) throws Exception;
    Integer queryRecordCount(@Param("userId") Long userId, @Param("semesterId") Long semesterId) throws Exception;
}
