package com.peipao.qdl.school.dao;

import com.peipao.qdl.school.model.Level;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 方法名称：运动等级
 * 功能描述：LevelDao
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/17 15:36
 * 修订记录：
 */
@Repository
public interface LevelDao {
//    void insertLevles(List<Level> levels) throws Exception;
//
    List<Level> queryLevelList(@Param("schoolId") Long schoolId) throws Exception;
//
    void updateLevels(@Param("levels") List<Level> levels) throws Exception;

    Level findUserNewLevel(@Param("schoolId") Long schoolId, @Param("length") Float length) throws Exception;

    Level findZeroLevelBySchoolId(@Param("schoolId") Long schoolId) throws Exception;

    String getMyLevelTitle(@Param("levelId") Long levelId) throws Exception;
}
