package com.peipao.qdl.discover.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.discover.model.DiscoverComment;
import java.util.Map;

/**
 * 方法名称：DiscoverCommentService
 * 功能描述：DiscoverCommentService
 * 作者：Liu fan
 * 版本：1.0
 * 创建日期：2017/10/20 17:39
 * 修订记录：
 */
public interface DiscoverCommentService {
    //paramsMap = Long discoverId, Long userId, int trueVal, int falseVal, int logicDelete
    MyPageInfo getCommentListByDiscoverId(Map<String, Object> paramsMap, int isSecret, int[] pageParams) throws Exception;

    void updateForCommentDelete(Long discoverId, Map<String, Object> paramsMap) throws Exception;

    DiscoverComment getDiscoverCommentById(Long commentId) throws Exception;

    void commentSave(DiscoverComment discoverComment) throws Exception;
}
