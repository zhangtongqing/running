package com.peipao.qdl.discover.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.discover.dao.DiscoverCommentDao;
import com.peipao.qdl.discover.dao.DiscoverDao;
import com.peipao.qdl.discover.model.DiscoverComment;
import com.peipao.qdl.user.model.UserSecret;
import com.peipao.qdl.user.service.UserSecretService;
import com.peipao.qdl.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverCommentServiceImpl
 * 功能描述：DiscoverCommentServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/20 17:39
 * 修订记录：
 */
@Service
public class DiscoverCommentServiceImpl implements DiscoverCommentService {
    @Autowired
    private DiscoverCommentDao discoverCommentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSecretService userSecretService;
    @Autowired
    private DiscoverDao discoverDao;

    @Override
    public MyPageInfo getCommentListByDiscoverId(Map<String, Object> paramsMap, int isSecret, int[] pageParams) throws Exception {
        PageHelper.startPage(pageParams[0], pageParams[1]);
        List<Map<String, Object>> list = discoverCommentDao.getCommentListByDiscoverId(paramsMap);
        for (Map<String, Object> map : list) {
            Long createUserId = Long.parseLong(map.get("userId").toString());//本条评论发布用户ID
            Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(createUserId);
            if(isSecret == WebConstants.Boolean.TRUE.ordinal()) {//如果需要控制隐私信息展示
                UserSecret userSecret = userSecretService.getUserSecretByUserId(createUserId);//用户是否设置了隐私保护
                if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
                    map.put("username", userInfoMap.get("nickName"));
                } else {
                    map.put("username", userInfoMap.get("username"));
                }
            } else {
                map.put("username", userInfoMap.get("username"));
            }
        }
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    @Transactional
    public void updateForCommentDelete(Long discoverId, Map<String, Object> paramsMap) throws Exception {
        discoverCommentDao.updateForCommentDelete(paramsMap);
        discoverDao.updateCommentAmountByDiscoverId(-1, discoverId);
    }

    @Override
    public DiscoverComment getDiscoverCommentById(Long commentId) throws Exception {
        return discoverCommentDao.getDiscoverCommentById(commentId);
    }

    @Override
    @Transactional
    public void commentSave(DiscoverComment discoverComment) throws Exception {
        discoverCommentDao.insertDiscoverComment(discoverComment);
        discoverDao.updateCommentAmountByDiscoverId(1, discoverComment.getDiscoverId());
    }
}
