package com.peipao.qdl.running.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.qdl.running.model.RankTypeEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.running.service.utils.RunningUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.UserStatisticCacheService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/runing/app",description = "跑步")
@RestController
@RequestMapping({"/runing/app"})
public class RunningControllerAPP {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RunningService runningService;
    @Autowired
    private RunningUtilService runningUtilService;
    @Autowired
    private UserStatisticCacheService userStatisticCacheService;


    @Register
    @RequestMapping(value = {"/getLocalCourseRank"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询本部排名-app")
    @ApiOperation(value = "查询本部排名--app--32", notes = "学生移动端")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<Map> getLocalCourseRank(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "courseId:课程Id[非必填,不填默认该学生当前学期所报课程]") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        Long courseId = 0L;
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null != json && null != json.get("courseId")) {
            courseId = json.getLong("courseId");
        } else {
            if(null == userSchool || null == userSchool.getCourseId() || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
                return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
            }
            courseId = userSchool.getCourseId();
        }
        List<Map<String, Object>> rankingList = userStatisticCacheService.getRankingInCourse(userSchool.getSchoolId(), userSchool.getSemesterId(), courseId);
        Map<String, Object> map = getMyRankingMap(rankingList, userId);
        if(!map.containsKey("myRanking")) {//排名在1000名以后
            map.put("myRanking", runningUtilService.getMyRankingIfNull(userId, RankTypeEnum.RankingInCourse.getCode(), RunningEnum.FREERUNNING.getValue()));
        }
        return Response.success(map);
    }

    /**
     * 自由跑 -本校排行
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/getLocalSchoolRank"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询本校排名-app")
    @ApiOperation(value = "查询本校排名--app--33", notes = "学生移动端")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<Map> getLocalSchoolRank(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        List<Map<String, Object>> rankingList = userStatisticCacheService.getRankingInSchool(userSchool.getSchoolId());
        Map<String, Object> map = getMyRankingMap(rankingList, userId);
        if(!map.containsKey("myRanking")) {//排名在1000名以后
            map.put("myRanking", runningUtilService.getMyRankingIfNull(userId, RankTypeEnum.RankingInSchool.getCode(), RunningEnum.FREERUNNING.getValue()));
        }
        return Response.success(map);
    }


    /**
     * 全国排行榜-自由跑
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/getGlobalRank"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询全国排名-app")
    @ApiOperation(value = "查询全国排名--app--34")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<Map> getGlobalRank(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        List<Map<String, Object>>  rankingList = userStatisticCacheService.getRankingInCountry();
        Map<String, Object> map = getMyRankingMap(rankingList, userId);
        if(!map.containsKey("myRanking")) {//排名在1000名以后
            map.put("myRanking", runningUtilService.getMyRankingIfNull(userId, RankTypeEnum.RankingInCountry.getCode(), RunningEnum.FREERUNNING.getValue()));
        }
        return Response.success(map);
    }


    /**
     * 本校排行-晨跑
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/getLocalSchoolRankByMorningRun"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询本校排名--app--根据晨跑", notes = "学生移动端")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<Map> getLocalSchoolRankByMorningRun(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        List<Map<String, Object>> rankingList = userStatisticCacheService.getRankingInSchoolByMorningRun(userSchool.getSchoolId());
        Map<String, Object> map = getMyRankingMap(rankingList, userId);
        if(!map.containsKey("myRanking")) {//排名在1000名以后
            map.put("myRanking", runningUtilService.getMyRankingIfNull(userId, RankTypeEnum.RankingInSchool.getCode(), RunningEnum.MORNINGRUNNING.getValue()));
        }
        return Response.success(map);
    }


    /**
     * 同课排行榜-晨跑
     * @param token
     * @param userId
     * @param sign
     * @param json
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/getLocalCourseRankByMorningRun"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询本部排名-晨跑-app--32", notes = "学生移动端")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<Map> getLocalCourseRankByMorningRun(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "courseId:课程Id[非必填,不填默认该学生当前学期所报课程]") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        Long courseId = 0L;
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null != json && null != json.get("courseId")) {
            courseId = json.getLong("courseId");
        } else {
            if(null == userSchool || null == userSchool.getCourseId() || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
                return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
            }
            courseId = userSchool.getCourseId();
        }
        List<Map<String, Object>> rankingList = userStatisticCacheService.getRankingInCourseByMorningRun(userSchool.getSchoolId(), userSchool.getSemesterId(), courseId);
        Map<String, Object> map = getMyRankingMap(rankingList, userId);
        if(!map.containsKey("myRanking")) {//排名在1000名以后
            map.put("myRanking", runningUtilService.getMyRankingIfNull(userId, RankTypeEnum.RankingInCourse.getCode(), RunningEnum.MORNINGRUNNING.getValue()));
        }
        return Response.success(map);
    }


    /**
     * 全国排行榜-自由跑
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/getGlobalRankByMorningRun"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询全国排名.晨跑--app--ok")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<Map> getGlobalRankByMorningRun(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        List<Map<String, Object>>  rankingList = userStatisticCacheService.getRankingInCountryByMorningRun();
        Map<String, Object> map = getMyRankingMap(rankingList, userId);
        if(!map.containsKey("myRanking")) {//排名在1000名以后
            map.put("myRanking", runningUtilService.getMyRankingIfNull(userId, RankTypeEnum.RankingInCountry.getCode(), RunningEnum.MORNINGRUNNING.getValue()));
        }
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/getRunningRecordByIdWithUsernameAndImage"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "跑步成绩分享-app")
    @ApiOperation(value = "跑步成绩分享")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response getRunningRecordByIdWithUsernameAndImage(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: runningRecordId") @RequestBody JSONObject json
    ) throws Exception {
        if(null == json || null == json.get("runningRecordId")) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);
        }
        String runningRecordId = json.getString("runningRecordId");
        Map<String, Object> map = runningService.getRunningRecordByIdWithUsernameAndImage(runningRecordId);
        return Response.success(map);
    }

    private Map<String, Object> getMyRankingMap(List<Map<String, Object>> rankingList, Long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(!CollectionUtils.isEmpty(rankingList)) {
            for(Map<String, Object> myRanking : rankingList) {
                if(userId.equals(Long.parseLong(myRanking.get("userId").toString()))) {
                    map.put("myRanking", myRanking);
                    break;
                }
            }
            if(rankingList.size() < 20) {
                map.put("rankingList", rankingList);
            } else {
                List resList = rankingList.subList(0, 20);
                map.put("rankingList", resList);
            }
        }
        return map;
    }

}

