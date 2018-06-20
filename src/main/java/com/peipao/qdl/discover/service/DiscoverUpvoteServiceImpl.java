package com.peipao.qdl.discover.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.qdl.discover.dao.DiscoverDao;
import com.peipao.qdl.discover.dao.DiscoverUpvoteDao;
import com.peipao.qdl.discover.model.DiscoverUpvote;
import com.peipao.qdl.user.model.UserSecret;
import com.peipao.qdl.user.service.UserSecretService;
import com.peipao.qdl.user.service.UserService;
import com.peipao.qdl.version.model.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverUpvoteServiceImpl
 * 功能描述：DiscoverUpvoteServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/21 16:37
 * 修订记录：
 */
@Service
public class DiscoverUpvoteServiceImpl implements DiscoverUpvoteService {
    @Autowired
    private DiscoverUpvoteDao discoverUpvoteDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSecretService userSecretService;
    @Autowired
    private DiscoverDao discoverDao;



    @Override
    public MyPageInfo getUpvoteByDiscoverId(long discoverId, Enum clientType, int[] pageParams) throws Exception {
        PageHelper.startPage(pageParams[0], pageParams[1]);
        List<Map<String, Object>> list = discoverUpvoteDao.getUpvoteByDiscoverId(discoverId);
        if(ClientType.PC.equals(clientType)) {
            initDiscoverUpvoteListInfoPc(list);
        } else {
            initDiscoverUpvoteListInfo(list);
        }
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    @Transactional
    public void upvote(DiscoverUpvote discoverUpvote) throws Exception {
        discoverUpvoteDao.insertDiscoverUpvote(discoverUpvote);
        discoverDao.updateUpvoteAmountByDiscoverId(1, discoverUpvote.getDiscoverId());
    }

    @Override
    @Transactional
    public void upvoteCancel(Long discoverId, Long userId) throws Exception {
        discoverUpvoteDao.deleteDiscoverUpvote(discoverId, userId);
        discoverDao.updateUpvoteAmountByDiscoverId(-1, discoverId);
    }


    private void initDiscoverUpvoteListInfo(List<Map<String, Object>> list) throws Exception {
        for (Map<String, Object> map : list) {
            Long upvoteUserId = Long.parseLong(map.get("userId").toString());//点赞用户ID
            Date createTime = (Date)map.get("createTime");//本条动力圈发布时间
            map.put("createTime", DateUtil.dateToStr(createTime, "yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(upvoteUserId);
            UserSecret userSecret = userSecretService.getUserSecretByUserId(upvoteUserId);//用户是否设置了隐私保护
            map.put("schoolName", userInfoMap.get("schoolName"));
            map.put("userImg", userInfoMap.get("userImg"));
            if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
                map.put("username", userInfoMap.get("nickName"));
            } else {
                map.put("username", userInfoMap.get("username"));
            }
            map.put("sex", userInfoMap.get("sex"));
        }
    }

    private void initDiscoverUpvoteListInfoPc(List<Map<String, Object>> list) throws Exception {
        for (Map<String, Object> map : list) {
            Long upvoteUserId = Long.parseLong(map.get("userId").toString());//点赞用户ID
            Date createTime = (Date)map.get("createTime");//本条动力圈发布时间
            map.put("createTime", DateUtil.getTimeFormatText(createTime.getTime()));
            Map<String, Object> userInfoMap = userService.getUserInfoForDiscoverPc(upvoteUserId);
            map.put("username", userInfoMap.get("username"));
        }
    }
}
