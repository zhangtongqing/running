package com.peipao.qdl.version.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.version.dao.VersionDao;
import com.peipao.qdl.version.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：VersionServiceImpl
 * 功能描述：VersionServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/31 13:58
 * 修订记录：
 */

@Service
public class VersionServiceImpl implements VersionService {
    @Autowired
    private VersionDao versionDao;

    @Override
    public Version getMaxVersionByClient(int clientType, int appType) throws Exception {
        return versionDao.getMaxVersionByClient(clientType, appType);
    }

    @Override
    public Version getVersionById(long versionId) throws Exception {
        return versionDao.getVersionById(versionId);
    }

    @Override
    @Transactional
    public void insertOrUpdateVersion(Version version) throws Exception {
        if(null == version.getVersionId() || 0L == version.getVersionId()) {
            this.versionDao.insertVersion(version);
        } else {
            this.versionDao.updateVersion(version);
        }
    }

    @Override
    public MyPageInfo getVersionList(int[] pageParams, Map<String, Object> paramsMap) throws Exception {
        PageHelper.startPage(pageParams[0], pageParams[1]);
        List<Map<String, Object>> list = this.versionDao.getVersionList(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

}
