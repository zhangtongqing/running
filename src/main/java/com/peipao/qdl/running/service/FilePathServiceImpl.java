package com.peipao.qdl.running.service;


import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：FilePathServiceImpl
 * 功能描述：FilePathServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/13 11:53
 * 修订记录：
 */
@Service
public class FilePathServiceImpl implements FilePathService {
    protected static final Logger LOG = LoggerFactory.getLogger(FilePathService.class);

    @Value("${file.server.num}")
    private String fsNum;
    @Value("${file.server.ip}")
    private String fsIp;//阿里云外网IP

    @Value("${file.server.ipnw}")
    private String fsIpnw;//阿里云内网IP

    @Value("${file.server.port}")
    private String fsPort;
    @Value("${file.server.method}")
    private String fsMethod;
    @Value("${file.server.dport}")
    private String fsdPort;

    @Override
    public String getPathFromFileServer(String runingRecordId) {
        String resBody = null;
        String postUrl = "http://" + fsIpnw + fsPort + fsMethod;
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("runningRecordId", runingRecordId);
        try {
            resBody = HttpClientUtil.post(postUrl, paramsMap);
        } catch (Exception e) {
            System.out.println(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                    " 文件服务器查询失败,地址：" + postUrl + "  参数:runningRecordId=" + runingRecordId);
            LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                    " 文件服务器查询失败,地址：" + postUrl + "  参数:runningRecordId=" + runingRecordId, e);
            throw new BusinessException(ReturnStatus.NODE_FILE_QUERY_ERROR);
        }
        return resBody;
    }

    @Override
    public String getFileServerUrl() throws Exception {
        return "http://" + fsIp + fsdPort + fsNum;
    }

    @Override
    public String getNodeJsonString(String url) throws Exception {
        String resBody = null;
        try {
            resBody = HttpClientUtil.get(url);
        } catch (Exception e) {
            System.out.println(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                    " 文件服务器查询失败,地址：" + url);
            LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                    " 文件服务器查询失败,地址：" + url, e);
            throw new BusinessException(ReturnStatus.NODE_FILE_QUERY_ERROR);
        }
        return resBody;
    }
}
