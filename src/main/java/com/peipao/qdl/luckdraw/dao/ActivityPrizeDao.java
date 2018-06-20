
package com.peipao.qdl.luckdraw.dao;


import com.peipao.qdl.luckdraw.model.ActivityPrize;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;


/**
 * 方法名称：ActivityPrizeDao
 * 功能描述：ActivityPrizeDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 13:54
 * 修订记录：
 */
@Repository
public interface ActivityPrizeDao {
    List<ActivityPrize> getActivityPrizeByActivityId(Map<String, Object> paramsMap) throws Exception;
    void logicDeleteActivityPrizeByPrizeId(Map<String, Object> paramsMap) throws Exception;
    void updatePrizeSendTotal(@Param("prizeId") Long prizeId) throws Exception;

    void addActivityPrize(ActivityPrize activityPrize) throws Exception;

    ActivityPrize getActivityPrizeById(@Param("prizeId") Long prizeId) throws Exception;

    void updateActivityPrize(ActivityPrize activityPrize) throws Exception;
}

