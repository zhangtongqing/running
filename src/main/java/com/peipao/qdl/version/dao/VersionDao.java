package com.peipao.qdl.version.dao;


import com.peipao.qdl.version.model.Version;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：VersionDao
 * 功能描述：VersionDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/9/27 14:20
 * 修订记录：
 */
@Repository
public interface VersionDao {
    Version getMaxVersionByClient(@Param("clientType") int clientType, @Param("appType") int appType) throws Exception;

    Version getVersionById(@Param("versionId") long versionId) throws Exception;

    void insertVersion(@Param("version") Version version) throws Exception;

    void updateVersion(@Param("version") Version version) throws Exception;

    List<Map<String, Object>> getVersionList(Map<String, Object> paramsMap) throws Exception;
}
