package com.peipao.qdl.running.service;

/**
 * 方法名称：FilePathService
 * 功能描述：FilePathService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/7 14:35
 * 修订记录：
 */
public interface FilePathService {

      String getPathFromFileServer(String runingRecordId) throws Exception;

      String getFileServerUrl() throws Exception;

      String getNodeJsonString(String url) throws Exception;

}
