package com.peipao.qdl.runningrule.service.utils;

import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.qdl.rule.model.enums.GetScoreType;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.runningrule.model.RailNode;
import com.peipao.qdl.runningrule.model.RunningRule;
import com.peipao.qdl.runningrule.model.enums.RailNodeTypeEnum;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.school.model.UserSchool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 方法名称：RuleUtilService
 * 功能描述：RuleUtilService
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2017/11/30 10:37
 * 修订记录：
 */

@Service
public class RuleUtilService {
    @Autowired
    private RuleCacheService ruleCacheService;

    public void initSignNodeList(List<RailNode> signNodeList, UserSchool userSchool) throws Exception {
        signNodeList.forEach(railNode -> {
            railNode.setType(RailNodeTypeEnum.MorningTraining.getValue());
            railNode.setSemesterId(userSchool.getSemesterId());
        });
    }

    public void initRunningRuleList(List<RunningRule> runningRuleList, UserSchool userSchool) throws Exception {
        Date now = new Date();//获得系统当前时间
        runningRuleList.forEach(runningRule -> {
            Long runningRuleId = 0L;
            int isDefaultFlag = 0;
            try {
                RunningRuleVo runningRuleVo = this.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), runningRule.getType());
                if(null != runningRuleVo && runningRuleVo.getIsDefaultFlag() > WebConstants.Boolean.FALSE.ordinal()) {
                    runningRuleId = runningRuleVo.getRunningRuleId();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(ResultMsg.QUERY_RULE_KEY_FAIL);//查询本学期运动规则主键失败
            }
            if(runningRuleId > 0) {
                runningRule.setRunningRuleId(runningRuleId);//如果本学期存在当前类型的运动规则，则家在主键，后续进行修改操作
            }
            runningRule.setCreateTime(now);
            runningRule.setSchoolId(userSchool.getSchoolId());
            runningRule.setSemesterId(userSchool.getSemesterId());
            runningRule.setCreateUser(userSchool.getUserId());
            if(RunningEnum.MORNINGRUNNING.getValue() == runningRule.getType()) {//晨跑，按次统计
                runningRule.setSportCountMax(1);//晨跑规定每天一次
                runningRule.setGetScoreType(GetScoreType.ByCount.getCode());
            } else if(RunningEnum.FREERUNNING.getValue() == runningRule.getType()) {//自由跑，按里程统计
                runningRule.setSportCountMax(99999);//自由跑不做限制
                runningRule.setGetScoreType(GetScoreType.ByKiometer.getCode());
            } else if(RunningEnum.MORNINGTRAINING.getValue() == runningRule.getType()) {//晨练，按次统计
                runningRule.setHasRail(WebConstants.Boolean.TRUE.ordinal());//晨练用开启围栏代表打卡点
                runningRule.setSportCountMax(1);//晨练规定每天一次
                runningRule.setGetScoreType(GetScoreType.ByCount.getCode());//按次得分
            } else {
                runningRule.setSportCountMax(99999);//未指定，默认不做限制
                runningRule.setGetScoreType(GetScoreType.ByKiometer.getCode());//未指定，默认按里程统计
            }
        });
    }


    //代替 getRunningRuleBySchool(双参数)  +  getRuleIdBySchoolAndType + getSchoolTargetBySemesterId
    public RunningRuleVo getRunningRuleBySemesterIdAndType(Long semesterId, int type) throws Exception {
        List<RunningRuleVo> tempList = ruleCacheService.getRunningRuleBySchool(semesterId);
        for(RunningRuleVo runningRuleVo : tempList) {
            if(runningRuleVo.getType() == type) {
                return runningRuleVo;
            }
        }
        return null;
    }

    //代替 getRunningRuleBySchool(单参数)
    public List<RunningRuleVo> getRunningRuleListBySemesterId(Long semesterId) throws Exception {
        return ruleCacheService.getRunningRuleBySchool(semesterId);
    }

    //代替 getSchoolTargetBySemesterId
    private JSONArray getSchoolTargetBySemesterId(Long semesterId) throws Exception {
        JSONArray jsonList = new JSONArray();
        List<RunningRuleVo> tempList = ruleCacheService.getRunningRuleBySchool(semesterId);
        for(RunningRuleVo runningRuleVo : tempList) {
            if(runningRuleVo.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
                JSONObject json = new JSONObject();
                json.accumulate("type", runningRuleVo.getType());
                json.accumulate("target", runningRuleVo.getTarget());
                jsonList.add(json);
            }
        }
        return jsonList;
    }

    //代替 getSportCountMaxByRunningType
    public Integer getSportCountMaxByRunningType(Long semesterId, int type) throws Exception {
        int sportCountMax = 0;
        List<RunningRuleVo> tempList = ruleCacheService.getRunningRuleBySchool(semesterId);
        for(RunningRuleVo runningRuleVo : tempList) {
            if(runningRuleVo.getType() == type) {
                sportCountMax = runningRuleVo.getSportCountMax();
            }
        }
        return sportCountMax;
    }

    public JSONObject getTargetValuesJson(Long semesterId) throws Exception {
        JSONArray jsonList = getSchoolTargetBySemesterId(semesterId);
        JSONObject resJson = new JSONObject();
        int morningRunningCountTarget = 0;
        int freeRunningLengthTarget = 0;
        int morningTrainingTarget = -1;//如果没有开启晨练就是(-1)
        for (Object obj : jsonList) {
            JSONObject json = (JSONObject) obj;
            if(json.getInt("type") == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
                morningRunningCountTarget = json.getInt("target");
            } else if(json.getInt("type") == RunningEnum.FREERUNNING.getValue()) {//自由跑
                freeRunningLengthTarget = json.getInt("target");
            } else if(json.getInt("type") == RunningEnum.MORNINGTRAINING.getValue()) {//晨练
                morningTrainingTarget = json.getInt("target");
            }
        }
        resJson.accumulate("morningRunningCountTarget", morningRunningCountTarget);
        resJson.accumulate("freeRunningLengthTarget", freeRunningLengthTarget);
        resJson.accumulate("morningTrainingTarget", morningTrainingTarget);
        return resJson;
    }


}
