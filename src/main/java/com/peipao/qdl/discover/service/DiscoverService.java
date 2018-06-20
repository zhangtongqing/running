package com.peipao.qdl.discover.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.discover.model.Discover;
import com.peipao.qdl.discover.model.DiscoverImg;
import net.sf.json.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverService
 * 功能描述：DiscoverService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
public interface DiscoverService {
    /**
     * APP(热门、最新)查询动力圈列表
     * @param userId
     * @param trueVal
     * @param falseVal
     * @param isHot
     * @param logicDelete
     * @param pageindex
     * @param pagesize
     * @return MyPageInfo
     * @throws Exception
     */
    MyPageInfo getDiscoverList(Long userId, int trueVal, int falseVal, Integer isHot, int logicDelete, int pageindex, int pagesize) throws Exception;

    /**
     * APP(同校)查询动力圈列表
     * @param schoolId
     * @param userId
     * @param trueVal
     * @param falseVal
     * @param logicDelete
     * @param pageindex
     * @param pagesize
     * @return Map
     * @throws Exception
     */
    MyPageInfo getDiscoverSchoolList(Long schoolId, Long userId, int trueVal, int falseVal, int logicDelete, int pageindex, int pagesize) throws Exception;

    void saveDiscover(Discover discover, List<DiscoverImg> discoverImgList) throws Exception;

    Discover getDiscoverById(Long discoverId) throws Exception;

    /**
     * 更新动力圈设为热门状态
     * @param discoverId
     *              动力圈信息ID
     * @param isHot
     *              是否为热门状态值
     * @throws Exception
     */
    void updatediscoverHot(Long discoverId, int isHot) throws Exception;


    /**
     * 更新动力圈禁上热门状态
     * @param discoverId
     *              动力圈信息ID
     * @param hotControl
     *              禁上热门状态
     * @throws Exception
     */
    void updatediscoverHotControl(Long discoverId, int hotControl) throws Exception;

    /**
     * 更新动力圈信息删除状态,此处为逻辑删除
     * @param discoverId
     *              动力圈ID
     * @param logicDelete
     *              信息状态
     * @throws Exception
     */
    void updatediscoverDel(Long discoverId, int logicDelete) throws Exception;

    /**
     * 组织返回body数据
     * @param discoverBean
     *              动力圈信息对象
     * @throws Exception
     */
    JSONObject createReturnBodyData(Discover discoverBean) throws Exception;

    Map<String, Object> queryDiscoverList(Map paramsMap, int[] pageParams) throws Exception;

    Map<String, Object> getDiscoverInfoById(Long discoverId) throws Exception;


}
