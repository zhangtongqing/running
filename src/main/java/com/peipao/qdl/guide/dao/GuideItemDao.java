package com.peipao.qdl.guide.dao;

import com.peipao.qdl.guide.model.GuideItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GuideItemDao {
    List<Map<String, Object>> getGuideItemList(Map<String, Object> map);

    GuideItem getGuideItemById(@Param("guideItemId") Long guideItemId);

    void updateGuideItem(GuideItem guideItem);

    void addGuideItem(GuideItem guideItem);

    void deleteGuideItem(@Param("guideItemId") Long guideItemId);

    void deleteGuideItemByGuideId(@Param("guideId") Long guideId);
}
