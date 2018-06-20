package com.peipao.qdl.runningrule.service.utils;

import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.qdl.runningrule.dao.RailNodeDao;
import com.peipao.qdl.runningrule.dao.RuleDao;
import com.peipao.qdl.runningrule.model.RailNode;
import com.peipao.qdl.runningrule.model.RunningRule;
import com.peipao.qdl.runningrule.model.enums.RailNodeTypeEnum;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：RuleCacheService
 * 功能描述：RuleCacheService
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/1/23 17:13
 * 修订记录：
 */

@Service
public class RuleCacheService {
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private RailNodeDao railNodeDao;
    @Autowired
    private RedisTemplate redisTemplate;


    @Cacheable(value = "peipao", key = "'runningRule'+#semesterId")
    public List<RunningRuleVo> getRunningRuleBySchool(Long semesterId) throws Exception {
        List<RunningRuleVo> runningRuleVoList = ruleDao.getRunningRuleBySchool(semesterId);
        if (CollectionUtils.isEmpty(runningRuleVoList)) {
            //学校本学期没有设置规则时，查询默认规则
            return ruleDao.getRunningRuleBySchool(0L);
        }
        return ruleDao.getRunningRuleBySchool(semesterId);
    }

    @Transactional
    @CachePut(value = "peipao", key = "'runningRule'+#semesterId")
    public List<RunningRuleVo> insertOrUpdateRunningRule(Long semesterId, List<RunningRule> runningRuleList) throws Exception {
        runningRuleList.forEach(runningRule -> {
            if (null == runningRule.getRunningRuleId() || runningRule.getRunningRuleId().equals(0L) || runningRule.getSemesterId().equals(0L)) {
                ruleDao.insertRunningRule(runningRule);
            } else {
                ruleDao.updateRunningRule(runningRule);
            }
        });
        return ruleDao.getRunningRuleBySchool(semesterId);
    }


    @Transactional
    public  Response<?>  updateRailNode(Long semesterId, String railName, List<RailNode> runningRuleNodeList, String oldRailName) throws Exception {
        if(null != oldRailName && oldRailName.trim() != ""){
            List<RailNode> railNode = railNodeDao.getRailNodeListBySemesterIdAndRailName(semesterId,RailNodeTypeEnum.SchoolRail.getValue(),oldRailName);
            if(null != railNode  && railNode.size() > 0){
                railNodeDao.deleteRailNodeBySemesterIdAndRailName(semesterId, oldRailName, RailNodeTypeEnum.SchoolRail.getValue());
                if(!CollectionUtils.isEmpty(runningRuleNodeList)) {
                    railNodeDao.insertRailNodesByName(semesterId, railName, runningRuleNodeList);
                }
            }else {
              return  Response.fail(ResultMsg.REIL_NOT_EXIST);
            }

        }else{
            railNodeDao.deleteRailNodeBySemesterIdAndRailName(semesterId, railName, RailNodeTypeEnum.SchoolRail.getValue());
            if(!CollectionUtils.isEmpty(runningRuleNodeList)) {
                railNodeDao.insertRailNodesByName(semesterId, railName, runningRuleNodeList);
            }
        }
        /**
         * 这里更新缓存，不能用@Cacheable来实现，原因：不会起作用，因为Spring cache是基于动态生成的 proxy 代理机制来对方法的调用进行切面
         * 这里内部调用，会导致proxy 失效，但是方法要返回错误码，必须这样做，那么就不能用Spring cache,采用redisTemplate来操作缓存
         */
        updateRailNodeCache(semesterId); //更新缓存数据
        return Response.success(ResultMsg.SUCCESS);
    }

    /**
     * 这里用@CachePut
     * @param semesterId
     * @return
     * @throws Exception
     */
    //@CachePut(value = "peipao", key = "'runningRuleNode:runningRuleNodeList'+#semesterId")
    public  List<Map<String,Object>> updateRailNodeCache(Long semesterId) throws Exception {
        //更新缓存内容
        List<Map<String,Object>> runningRuleList = new ArrayList<>();
        List<Map<String,Object>> railNams = railNodeDao.getRailNodeListByRailName(semesterId, RailNodeTypeEnum.SchoolRail.getValue());
        if(railNams.size() > 0){
            for(Map<String,Object> railNames : railNams){
                List<RailNode> railNode = railNodeDao.getRailNodeListBySemesterIdAndRailName(semesterId,RailNodeTypeEnum.SchoolRail.getValue(),railNames.get("rail_name") == null ? null : railNames.get("rail_name").toString());
                if(railNode.size() > 0){
                    Map<String,Object> rail = new HashMap<>();
                    rail.put("railName",railNames.get("rail_name") == null ? "" :railNames.get("rail_name").toString()); //校区名
                    rail.put("data",railNode); //校区围栏数据
                    runningRuleList.add(rail);
                }
            }
        }
        redisTemplate.opsForValue().set("runningRuleNode:runningRuleNodeList" + semesterId, runningRuleList);
        return runningRuleList;
    }

    @Cacheable(value = "peipao", key = "'runningRuleNode:runningRuleNodeList'+#semesterId")
    public List<Map<String,Object>> getRailNodeListByRailName(Long semesterId) throws Exception {
        List<Map<String,Object>> runningRuleNodeList = new ArrayList<>();
         List<Map<String,Object>> railNams = railNodeDao.getRailNodeListByRailName(semesterId, RailNodeTypeEnum.SchoolRail.getValue());
        if(railNams.size() > 0){
            for(Map<String,Object> railName : railNams){
                List<RailNode> railNode = railNodeDao.getRailNodeListBySemesterIdAndRailName(semesterId,RailNodeTypeEnum.SchoolRail.getValue(),railName.get("rail_name") == null ? null : railName.get("rail_name").toString());
                if(railNode.size() > 0){
                    Map<String,Object> rail = new HashMap<>();
                    rail.put("railName",railName.get("rail_name") == null ? "" :railName.get("rail_name").toString()); //校区名
                    rail.put("data",railNode); //校区围栏数据
                    runningRuleNodeList.add(rail);
                }
            }
        }

        return runningRuleNodeList;
    }

    @Cacheable(value = "peipao", key = "'morningSignNode'+#semesterId")
    public List<RailNode> getMorningSignNodeListBySemesterId(Long semesterId) throws Exception {
        return railNodeDao.getRailNodeListBySemesterId(semesterId, RailNodeTypeEnum.MorningTraining.getValue());
    }

    @Transactional
    @CachePut(value = "peipao", key = "'morningSignNode'+#semesterId")
    public List<RailNode> updateMorningSignNodeNode(Long semesterId, List<RailNode> runningRuleNodeList) throws Exception {
        railNodeDao.deleteRailNodeBySemesterId(semesterId, RailNodeTypeEnum.MorningTraining.getValue());
        if(!CollectionUtils.isEmpty(runningRuleNodeList)) {
            railNodeDao.insertRailNodeByList(semesterId, runningRuleNodeList);
        }
        return railNodeDao.getRailNodeListBySemesterId(semesterId, RailNodeTypeEnum.MorningTraining.getValue());
    }

    /**
     * 删除围栏 根据名字
     * 删除后，查询剩余围栏信息，并更新缓存
     */

    @CachePut(value = "peipao", key = "'runningRuleNode:runningRuleNodeList'+#semesterId")
    public List<Map<String,Object>> deleteRailByName(Long semesterId, String railName) {
        List<Map<String,Object>> runningRuleNodeList = new ArrayList<>();
        int indexSize = railNodeDao.deleteRailNodeBySemesterIdAndRailName(semesterId, railName, RailNodeTypeEnum.SchoolRail.getValue());
        if(indexSize > 0){
            //清理缓存数据
            List<Map<String,Object>> railNams = railNodeDao.getRailNodeListByRailName(semesterId, RailNodeTypeEnum.SchoolRail.getValue());
            if(railNams.size() > 0){
                for(Map<String,Object> railNames : railNams){
                    List<RailNode> railNode = railNodeDao.getRailNodeListBySemesterIdAndRailName(semesterId,RailNodeTypeEnum.SchoolRail.getValue(),railNames.get("rail_name") == null ? null : railNames.get("rail_name").toString());
                    if(railNode.size() > 0){
                        Map<String,Object> rail = new HashMap<>();
                        rail.put("railName",railNames.get("rail_name") == null ? "" :railNames.get("rail_name").toString()); //校区名
                        rail.put("data",railNode); //校区围栏数据
                        runningRuleNodeList.add(rail);
                    }
                }
            }
        }
        return runningRuleNodeList;
    }

    public Boolean searchRailByName(Long semesterId, String railName) {
        Boolean flag = false;
        List<Map<String,Object>> maplist = railNodeDao.searchRailByName(semesterId, railName, RailNodeTypeEnum.SchoolRail.getValue());
        if(null != maplist && maplist.size() > 0 ){
            flag = true;
        }
        return flag;
    }
}
