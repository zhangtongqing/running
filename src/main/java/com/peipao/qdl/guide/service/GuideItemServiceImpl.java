package com.peipao.qdl.guide.service;


import com.peipao.framework.constant.MyPageConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.qdl.guide.dao.GuideItemDao;
import com.peipao.qdl.guide.model.GuideItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指南子表业务逻辑
 *
 * @author Meteor.wu
 * @since 2018/1/11 13:15
 */
@Service
public class GuideItemServiceImpl implements GuideItemService {

    @Autowired
    private GuideItemDao guideItemDao;

    @Override
    public void addGuideItem(GuideItem guideItem) {
        guideItemDao.addGuideItem(guideItem);
    }

    @Override
    public void updateGuideItem(GuideItem guideItem) {
        guideItemDao.updateGuideItem(guideItem);
    }

    @Override
    public GuideItem getGuideItemById(Long guideItemId) {
        GuideItem guideItem = guideItemDao.getGuideItemById(guideItemId);
        if (guideItem == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        return  guideItem;
    }

    @Override
    public Map getGuideItemList(Long guideId) {
        Map<String, Object> map = new HashMap<>();
        map.put("guideId", guideId);
        List<Map<String, Object>> list = guideItemDao.getGuideItemList(map);
        if (list.size() == 0) {
            return MyPage.processPage((long)list.size(), MyPageConstants.PAGE_SIZE_PC, 1, list);
        }
        return MyPage.processPage((long)list.size(), list.size(), 1, list);
    }

    @Override
    public void deleteGuideItem(Long guideItemId) {
        guideItemDao.deleteGuideItem(guideItemId);
    }

    @Override
    public void deleteGuideItemByGuideId(Long guideId) {
        guideItemDao.deleteGuideItemByGuideId(guideId);
    }
}
