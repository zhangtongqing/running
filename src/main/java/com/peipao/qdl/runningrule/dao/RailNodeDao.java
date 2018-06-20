package com.peipao.qdl.runningrule.dao;

import com.peipao.qdl.runningrule.model.RailNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 方法名称：RunningRailNodeDao
 * 功能描述：RunningRailNodeDao
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 15:18
 * 修订记录：
 */
@Repository
public interface RailNodeDao {
    List<RailNode> getRailNodeListBySemesterId(@Param("semesterId") Long semesterId, @Param("type") int type) throws Exception;

    /**
     * @Param List<RailNode> runningRuleNodeList, Long runningRuleId
     * @throws Exception
     */
    void insertRailNodeByList(@Param("semesterId") Long semesterId, @Param("railNodeList") List<RailNode> railNodeList) throws Exception;

    void insertRailNodesByName(@Param("semesterId") Long semesterId, @Param("railName") String railName, @Param("railNodeList") List<RailNode> railNodeList) throws Exception;

    void deleteRailNodeBySemesterId(@Param("semesterId") Long semesterId, @Param("type") int type) throws Exception;

    Integer deleteRailNodeBySemesterIdAndRailName(@Param("semesterId") Long semesterId, @Param("railName") String railName, @Param("type") int type);

    List<Map<String,Object>> getRailNodeListByRailName(@Param("semesterId") Long semesterId, @Param("type") int type);

    List<RailNode> getRailNodeListBySemesterIdAndRailName(@Param("semesterId") Long semesterId,  @Param("type") int type, @Param("railName") String railName);

    List<Map<String, Object>> searchRailByName(@Param("semesterId") Long semesterId, @Param("railName") String railName, @Param("type") int type);
}
