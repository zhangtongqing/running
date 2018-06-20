package com.peipao.qdl.discover.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.MyPageConstants;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.qdl.discover.dao.DiscoverDao;
import com.peipao.qdl.discover.dao.DiscoverImgDao;
import com.peipao.qdl.discover.dao.DiscoverReportDetailDao;
import com.peipao.qdl.discover.dao.DiscoverUpvoteDao;
import com.peipao.qdl.discover.model.Discover;
import com.peipao.qdl.discover.model.DiscoverImg;
import com.peipao.qdl.user.model.UserSecret;
import com.peipao.qdl.user.service.UserSecretService;
import com.peipao.qdl.user.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;

/**
 * 方法名称：DiscoverServiceImpl
 * 功能描述：DiscoverServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Service
public class DiscoverServiceImpl implements DiscoverService {
    @Autowired
    private DiscoverDao discoverDao;
    @Autowired
    private DiscoverImgDao discoverImgDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSecretService userSecretService;
    @Autowired
    private DiscoverUpvoteDao discoverUpvoteDao;
    @Autowired
    private DiscoverReportDetailDao discoverReportDetailDao;
    @Autowired
    private DiscoverCommentService discoverCommentService;

    @Override
    public MyPageInfo getDiscoverList(Long userId, int trueVal, int falseVal, Integer isHot, int logicDelete, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = discoverDao.getDiscoverList(userId, trueVal, falseVal, isHot, logicDelete);//新
        initDiscoverListInfo(list, userId);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    public MyPageInfo getDiscoverSchoolList(Long schoolId, Long userId, int trueVal, int falseVal, int logicDelete, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = discoverDao.getDiscoverSchoolList(schoolId, userId, trueVal, falseVal, logicDelete);
        initDiscoverListInfo(list, userId);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    @Transactional
    public void saveDiscover(Discover discover, List<DiscoverImg> discoverImgList) throws Exception {
        discoverDao.insertDiscover(discover);
        if(!CollectionUtils.isEmpty(discoverImgList)) {
            discoverImgDao.insertDiscoverImgByBatch(discover.getDiscoverId(), discoverImgList);
        }
    }

    @Override
    public Discover getDiscoverById(Long discoverId) throws Exception {
        return discoverDao.getDiscoverById(discoverId);
    }

    private String[] createTheStringArray(List<Map<String, String>> list, String key) {
        String[] strArray = new String[list.size()];
        int i = 0;
        for (Map<String, String> map : list) {
            strArray[i] = map.get(key);
            i++;
        }
        return strArray;
    }

    private void initDiscoverListInfo(List<Map<String, Object>> list, Long userId) throws Exception {
        for (Map<String, Object> map : list) {
            Long discoverId = Long.parseLong(map.get("discoverId").toString());//本条动力圈id
            Long createUserId = Long.parseLong(map.get("userId").toString());//本条动力圈发布用户ID
            Date createTime = (Date)map.get("createTime");//本条动力圈发布时间
            map.put("createTime", DateUtil.getTimeFormatText(createTime.getTime()));
            if(userId == createUserId) {
                map.put("ifReport", WebConstants.Boolean.FALSE.ordinal());//自己发布的信息不展示举报按钮
            } else {
                int discoverReportCount = discoverReportDetailDao.getIfUserReportTheDiscover(discoverId, userId);
                if(0 == discoverReportCount) {
                    map.put("ifReport", WebConstants.Boolean.FALSE.ordinal());//当前登录用户,没有举报的，可以举报
                } else {
                    map.put("ifReport", WebConstants.Boolean.TRUE.ordinal());
                }
            }
            int discoverUpvoteCount = discoverUpvoteDao.getIfUserUpvoteTheDiscover(discoverId, userId);
            if(0 == discoverUpvoteCount) {
                map.put("ifUpvote", WebConstants.Boolean.FALSE.ordinal());//当前登录用户,没有点过赞的，可以点赞
            } else {
                map.put("ifUpvote", WebConstants.Boolean.TRUE.ordinal());
            }
            Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(createUserId);
            UserSecret userSecret = userSecretService.getUserSecretByUserId(createUserId);//用户是否设置了隐私保护
            map.put("schoolName", userInfoMap.get("schoolName"));
            map.put("userImg", userInfoMap.get("userImg"));
            if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
                map.put("username", userInfoMap.get("nickName"));
            } else {
                map.put("username", userInfoMap.get("username"));
            }

            List discoverImgList = discoverImgDao.getImgListByDiscoverId(discoverId);
            if(CollectionUtils.isEmpty(discoverImgList)) {
                map.put("imgUrls", new JSONArray());
            } else {
                map.put("imgUrls", createTheStringArray(discoverImgList, "imgUrl"));
            }

            List<Map<String, String>> userImgList = discoverUpvoteDao.getTop5UserImgs(discoverId);
            if(CollectionUtils.isEmpty(userImgList)) {
                map.put("userImgUrls", new JSONArray());
            } else {
                map.put("userImgUrls", createTheStringArray(userImgList, "userImg"));
            }
            //动力圈信息页面查询第一页评论列表 //默认每页查询15条
            int[] pageParams = {1, MyPageConstants.PAGE_SIZE_APP};
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("discoverId", discoverId);
            paramsMap.put("userId", userId);
            paramsMap.put("trueVal", WebConstants.Boolean.TRUE.ordinal());
            paramsMap.put("falseVal", WebConstants.Boolean.FALSE.ordinal());
            paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
            MyPageInfo pageInfo = discoverCommentService.getCommentListByDiscoverId(paramsMap, WebConstants.Boolean.TRUE.ordinal(), pageParams);
            if(null == pageInfo) {
                map.put("commentList", new JSONArray());
            } else {
                map.put("commentList", pageInfo.getData());
            }
        }
    }

    /**
     * 更新动力圈设为热门或取消热门
     * @param discoverId
     *              动力圈信息ID
     * @param isHot
     *              是否为热门状态值
     * @throws Exception
     */
    @Transactional
    @Override
    public void updatediscoverHot(Long discoverId, int isHot) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("discoverId",discoverId);
        map.put("isHot",isHot);
        discoverDao.updatediscoverHot(map);
    }

    /**
     * 更新动力圈禁上热门状态
     * @param discoverId
     *              动力圈信息ID
     * @param hotControl
     *              禁上热门状态
     * @throws Exception
     */
    @Transactional
    @Override
    public void updatediscoverHotControl(Long discoverId,int hotControl) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("discoverId",String.valueOf(discoverId));
        map.put("hotControl",hotControl);
        discoverDao.updatediscoverHotControl(map);
    }

    /**
     * 更新动力圈信息删除状态,此处为逻辑删除
     * @param discoverId
     *              动力圈ID
     * @param logicDelete
     *              信息状态
     * @throws Exception
     */
    @Transactional
    @Override
    public void updatediscoverDel(Long discoverId, int logicDelete) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("discoverId",discoverId);
        map.put("logicDelete",logicDelete);
        discoverDao.updatediscoverDel(map);
    }

    @Override
    public JSONObject createReturnBodyData(Discover discoverBean) throws Exception {
        JSONObject jSONObject = new JSONObject();
        if(null != discoverBean){
            jSONObject.put("key",discoverBean.getDiscoverId());
            jSONObject.put("discoverId",discoverBean.getDiscoverId());
            jSONObject.put("userId",discoverBean.getCreateUserId());
            Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(discoverBean.getCreateUserId());
            jSONObject.put("username", userInfoMap.get("username"));
            jSONObject.put("discoverContent",discoverBean.getDiscoverContent());
            jSONObject.put("upvoteAmount",discoverBean.getUpvoteAmount());
            jSONObject.put("commentAmount",discoverBean.getCommentAmount());
            jSONObject.put("reportAmount",discoverBean.getReportAmount());
            jSONObject.put("hotControl",discoverBean.getHotControl());
            jSONObject.put("isHot",discoverBean.getIsHot());
            Date createTime = (Date)discoverBean.getCreateTime();//本条动力圈发布时间
            jSONObject.put("createTime", DateUtil.dateToStr(createTime, "yyyy-MM-dd HH:mm:ss"));
            jSONObject.put("logicDelete", discoverBean.getLogicDelete());
        }
        return jSONObject;
    }


    @Override
    public Map<String, Object> queryDiscoverList(Map paramsMap, int[] pageParams) throws Exception {
        List<Map<String, Object>> list = null;
        Long total = discoverDao.queryDiscoverListTotal(paramsMap);
        if(total > 0) {
            paramsMap.put("from", (pageParams[0] - 1) * pageParams[1]);
            paramsMap.put("pagesize", pageParams[1]);
            list = discoverDao.queryDiscoverList(paramsMap);
            String[] keysFromUser = {"username"};
            initDiscoverListInfoPc(list, keysFromUser);
        }
        return MyPage.processPage(total, pageParams[1], pageParams[0], list);
    }

    @Override
    public Map<String, Object> getDiscoverInfoById(Long discoverId) throws Exception {
        Map<String, Object> map = discoverDao.getDiscoverInfoById(discoverId);
        if(!CollectionUtils.isEmpty(map)) {
            String[] keysFromUser = {"username","schoolName", "studentNo"};
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list.add(map);
            initDiscoverListInfoPc(list, keysFromUser);
        }
        return map;

    }

    private void initDiscoverListInfoPc(List<Map<String, Object>> list, String[] keysFromUser) throws Exception {
        for (Map<String, Object> map : list) {
            Long discoverId = Long.parseLong(map.get("discoverId").toString());//本条动力圈id
            map.put("key", discoverId);
            Long createUserId = Long.parseLong(map.get("userId").toString());//本条动力圈发布用户ID
            Date createTime = (Date)map.get("createTime");//本条动力圈发布时间
            map.put("createTime", DateUtil.dateToStr(createTime, "yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> userInfoMap = userService.getUserInfoForDiscoverPc(createUserId);
            for(String key : keysFromUser){
                map.put(key, userInfoMap.get(key));
            }
        }
    }

}
