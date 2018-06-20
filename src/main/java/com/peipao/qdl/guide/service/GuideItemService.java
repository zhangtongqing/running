package com.peipao.qdl.guide.service;


import com.peipao.qdl.guide.model.GuideItem;

import java.util.Map;

public interface GuideItemService {
    void addGuideItem(GuideItem guideItem);

    void updateGuideItem(GuideItem guideItem);

    GuideItem getGuideItemById(Long guideItemId);

    Map getGuideItemList(Long guideId);

    void deleteGuideItem(Long guideItemId);

    void deleteGuideItemByGuideId(Long guideId);
}
