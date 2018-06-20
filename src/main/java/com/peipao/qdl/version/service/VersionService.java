package com.peipao.qdl.version.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.version.model.Version;

import java.util.Map;

/**
 * 方法名称：VersionService
 * 功能描述：VersionService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/31 13:57
 * 修订记录：
 */
public interface VersionService {

    Version getMaxVersionByClient(int clientType, int appType) throws Exception;
    Version getVersionById(long versionId) throws Exception;
    void insertOrUpdateVersion(Version version) throws Exception;
    MyPageInfo getVersionList(int[] pageParams, Map<String, Object> paramsMap) throws Exception;

}
