package com.peipao.qdl.guide.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.guide.dao.GuideDao;
import com.peipao.qdl.guide.model.Guide;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指南业务
 *
 * @author Meteor.wu
 * @since 2018/1/11 11:10
 */
@Service
public class GuideServiceImpl implements GuideService {

    @Autowired
    private GuideDao guideDao;

    @Override
    public MyPageInfo getGuideList(String title, int pageindex, int pagesize) {
        PageHelper.startPage(pageindex, pagesize);
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        List<Map<String, Object>> list = guideDao.getGuideList(map);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        return new MyPageInfo(p);
    }

    @Override
    public Guide getGuideById(Long guideId) {
        Guide guide = guideDao.getGuideById(guideId);
        if (guide == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        return guide;
    }

    @Override
    public void updateGuide(Guide guide) {
        Guide guide1 = guideDao.getGuideById(guide.getGuideId());
        if (guide1 == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!guide.getKeyword().equals(guide1.getKeyword())) {// keyword改变
            Guide temp = new Guide();
            temp.setKeyword(guide.getKeyword());
            Guide guide2 = guideDao.getGuideByGuide(temp);// keyword是否存在
            if (guide2 != null) {
                throw new BusinessException(ResultMsg.GUIIDE_KEYWORD_EXIST);
            }
        }
        guideDao.updateGuide(guide);
    }

    @Override
    public void addGuide(Guide guide) {
        Guide temp = new Guide();
        temp.setKeyword(guide.getKeyword());
        Guide guide1 = guideDao.getGuideByGuide(temp);// keyword是否存在
        if (guide1 != null) {
            throw new BusinessException(ResultMsg.GUIIDE_KEYWORD_EXIST);
        }
        guideDao.addGuide(guide);
    }

    @Override
    public void deleteGuide(Long guideId) {
        guideDao.deleteGuide(guideId);
    }

    @Override
    public Guide getGuideByGuide(Guide guide) {
        return guideDao.getGuideByGuide(guide);
    }
}
