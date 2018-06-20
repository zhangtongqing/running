package com.peipao.qdl.running.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.running.dao.RecordDao;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：RecordServiceImpl
 * 功能描述：RecordServiceImpl
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 16:56
 * 修订记录：
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordDao recordDao;

    @Override
    public MyPageInfo getMyRunningRecord(Map<String, Object> paramsMap, int[] page) throws Exception {
        PageHelper.startPage(page[0], page[1]);
        List<Map<String, Object>> list = recordDao.getMyRunningRecord(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        int maxpage = pageInfo.getMaxpage();
        if(page[0] > maxpage) {
            pageInfo.setPageindex(page[0]);
            pageInfo.setData(new JSONArray());
        }
        return pageInfo;
    }

    @Override
    public MyPageInfo getStudentRunningList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = recordDao.getStudentRunningList(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    public MyPageInfo getStudentActivityRunningList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = recordDao.getStudentActivityRunningList(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    public MyPageInfo getMyMorningExercisesRecord(Map<String, Object> paramsMap, int[] page) {
        PageHelper.startPage(page[0], page[1]);
        List<Map<String, Object>> list = recordDao.getMyMorningExercisesRecord(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        int maxpage = pageInfo.getMaxpage();
        if(page[0] > maxpage) {
            pageInfo.setPageindex(page[0]);
            pageInfo.setData(new JSONArray());
        }
        return pageInfo;
    }
}
