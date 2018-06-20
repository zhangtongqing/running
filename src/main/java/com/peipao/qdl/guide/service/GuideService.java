package com.peipao.qdl.guide.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.guide.model.Guide;

public interface GuideService {
    MyPageInfo getGuideList(String title, int pageindex, int pagesize);

    Guide getGuideById(Long guideId);

    void updateGuide(Guide guide);

    void addGuide(Guide guide);

    void deleteGuide(Long guideId);

    Guide getGuideByGuide(Guide guide);
}
