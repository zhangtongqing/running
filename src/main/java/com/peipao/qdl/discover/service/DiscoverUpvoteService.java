package com.peipao.qdl.discover.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.discover.model.DiscoverUpvote;

/**
 * 方法名称：DiscoverUpvoteService
 * 功能描述：DiscoverUpvoteService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/21 16:36
 * 修订记录：
 */
public interface DiscoverUpvoteService {

    MyPageInfo getUpvoteByDiscoverId(long discoverId, Enum ClientType, int[] pageParams) throws Exception;

    void upvote(DiscoverUpvote discoverUpvote) throws Exception;

    void upvoteCancel(Long discoverId, Long userId) throws Exception;
}
