package com.peipao.qdl.guide.dao;

import com.peipao.qdl.guide.model.Guide;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GuideDao {
    List<Map<String, Object>> getGuideList(Map<String, Object> params);

    Guide getGuideById(@Param("guideId") Long guideId);

    void updateGuide(Guide guide);

    void deleteGuide(@Param("guideId") Long guideId);

    void addGuide(Guide guide);

    Guide getGuideByGuide(Guide guide);
}
