package com.peipao.qdl.discover.dao;


import com.peipao.qdl.discover.model.Discover;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverDao
 * 功能描述：发现-动力圈实体类
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface DiscoverDao {

    void insertDiscover(@Param("discover") Discover discover) throws Exception;

    List<Map<String, Object>> getDiscoverList(
            @Param("userId") Long userId,
            @Param("trueVal") int trueVal,
            @Param("falseVal") int falseVal,
            @Param("isHot") Integer isHot,
            @Param("logicDelete") int logicDelete
    ) throws Exception;

    List<Map<String, Object>> getDiscoverSchoolList(
            @Param("schoolId") Long schoolId,
            @Param("userId") Long userId,
            @Param("trueVal") int trueVal,
            @Param("falseVal") int falseVal,
            @Param("logicDelete") int logicDelete
    ) throws Exception;

    Discover getDiscoverById(@Param("discoverId") Long discoverId) throws Exception;

    void updateUpvoteAmountByDiscoverId(@Param("upvoteAmount") Integer upvoteAmount, @Param("discoverId") Long discoverId) throws Exception;

    void updateCommentAmountByDiscoverId(@Param("commentAmount") Integer commentAmount, @Param("discoverId") Long discoverId) throws Exception;

    void updateReportAmountByDiscoverId(@Param("reportAmount") Integer reportAmount, @Param("discoverId") Long discoverId) throws Exception;

    /**
     * 将某一条动力圈设为热门或取消热门
     * @param map
     * @throws Exception
     */
    void updatediscoverHot(Map<String, Object> map) throws Exception;


    /**
     * 更新动力圈禁上热门状态
     * @param map
     * @throws Exception
     */
    void updatediscoverHotControl(Map<String, Object> map) throws Exception;


    /**
     * 更新动力圈信息删除状态,此处为逻辑删除
     * @param map
     * @throws Exception
     */
    void updatediscoverDel(Map<String, Object> map) throws Exception;

    Long queryDiscoverListTotal(Map paramsMap) throws Exception;

    List<Map<String, Object>> queryDiscoverList(Map paramsMap) throws Exception;

    Map<String, Object> getDiscoverInfoById(@Param("discoverId") Long discoverId) throws Exception;

}
