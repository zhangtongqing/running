package com.peipao.qdl.appeal.service;


import com.peipao.framework.page.MyPage;
import com.peipao.qdl.appeal.dao.AppealDao;
import com.peipao.qdl.appeal.model.FeedbackRecord;
import com.peipao.qdl.appeal.model.QA;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Service
public class AppealServiceImpl implements AppealService {
    @Autowired
    private AppealDao appealDao;

    @Autowired
    private SchoolUserService schoolUserService;

    @Override
    public void addFeekbackRecord(Long userId, FeedbackRecord feedbackRecord) throws Exception {
        feedbackRecord.setTime(Calendar.getInstance().getTime());
        appealDao.insertFeedbackRecord(feedbackRecord);
    }

    @Override
    public List<Map<String, Object>> getFeedbackRecord(Long userId) throws Exception {
        return appealDao.queryFeedbackRecord(userId);
    }

    @Transactional
    @Override
    public void addQA(Long userId,QA qa) throws Exception {
        User user = schoolUserService.validateOfficialManager(userId);
        qa.setUserId(userId);
        qa.setCreateTime(Calendar.getInstance().getTime());
        appealDao.insertQA(qa);

        qa.setUsername(user.getUsername());
    }

    @Transactional
    @Override
    public void updateQA(Long userId, QA qa) throws Exception {
        User user = schoolUserService.validateOfficialManager(userId);
        appealDao.updateQA(qa);
        qa.setUsername(user.getUsername());// some problem
    }

    @Override
    public Map<String, Object> getQAList(String title, int pageindex, int pagesize) throws Exception {
        Long allcount = appealDao.queryQAListCount(title);
        List<Map<String, Object>> list;
        if (allcount > 0) {
            list = appealDao.queryQAList(title, (pageindex - 1) * pagesize, pagesize);
        } else {
            list = new ArrayList<>();
        }

        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public String getQAContent(Long qaId) throws Exception {
        return appealDao.queryQAContent(qaId);
    }

    @Override
    public Map<String, Object> getFeedbackList(Byte operate, Byte platform, Byte userType, Long userId, int pageindex, int pagesize) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        List<Map<String, Object>>  list = appealDao.queryFeedbackRecordList(operate, platform, userType, (pageindex - 1) * pagesize, pagesize);
        Long allcount = appealDao.queryFeedbackRecordListCount(operate, platform, userType);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public void updateFeedback(Long userId, Long feedbackId, int operate) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        FeedbackRecord feedbackRecord = new FeedbackRecord();
        feedbackRecord.setFeedbackId(feedbackId);
        feedbackRecord.setOperate((byte)operate);
        appealDao.updateFeedbackRecord(feedbackRecord);
    }

    @Override
    public QA getQaById(Long qaId) throws Exception {
        return appealDao.queryQAById(qaId);
    }

    @Override
    public void deleteQA(Long userId, Long qaId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        appealDao.deleteQA(qaId);

    }
}
