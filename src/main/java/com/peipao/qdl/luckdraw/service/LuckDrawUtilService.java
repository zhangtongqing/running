package com.peipao.qdl.luckdraw.service;


import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ExcelExport;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.luckdraw.model.ActivityLuckRecord;
import com.peipao.qdl.luckdraw.model.ActivityPrize;
import com.peipao.qdl.luckdraw.model.LuckDrawRule;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 方法名称：LuckDrawUtilService
 * 功能描述：LuckDrawUtilService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 16:10
 * 修订记录：
 */
@Service
public class LuckDrawUtilService {
    private static final Logger log = LoggerFactory.getLogger(LuckDrawUtilService.class);
    @Autowired
    private ActivityLuckRecordService activityLuckRecordService;
    @Autowired
    private LuckDrawRuleService luckDrawRuleService;
    @Autowired
    private ActivityPrizeService activityPrizeService;

    /**
     * 抽奖
     *
     * @param activityPrizeList
     * @return 公式：A被抽中的概率 = A的权值*A的剩余量 / (A的权值*A的剩余量 + B的权值*B的剩余量 + C的权值*C的剩余量 + ...)
     */
    public int getPrizeIndex(List<ActivityPrize> activityPrizeList) {
        DecimalFormat df = new DecimalFormat("######0.00");
        int index = -1;
        //统计算法分母
        double sumWeight = 0;//this is 分母
        for (ActivityPrize activityPrize : activityPrizeList) {
            double sign = activityPrize.getPrizeWeight() * (activityPrize.getPrizeTotal() - activityPrize.getPrizeSendTotal());
            sumWeight = sumWeight + sign;
        }
        try {
            //产生随机数
            double randomNumber = Math.random();
            //根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for (int i = 0; i < activityPrizeList.size(); i++) {
                ActivityPrize activityPrize = activityPrizeList.get(i);
                double sign = activityPrize.getPrizeWeight() * (activityPrize.getPrizeTotal() - activityPrize.getPrizeSendTotal());
                d2 += sign / sumWeight;
                if (i == 0) {
                    d1 = 0;
                } else {
                    activityPrize = activityPrizeList.get(i - 1);
                    sign = activityPrize.getPrizeWeight() * (activityPrize.getPrizeTotal() - activityPrize.getPrizeSendTotal());
                    d1 += sign / sumWeight;
                }

                if (randomNumber >= d1 && randomNumber <= d2) {
                    index = i;
                    break;
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ResultMsg.GET_PRIZE_INDEX_ERROR);//抽奖出错，请检查
        }
        return index;
    }


    public String createluckRecordExcel(HttpServletResponse response, HttpServletRequest request, List<Map<String, Object>> list, String name) throws Exception {
        HSSFWorkbook wbarray = new HSSFWorkbook();
        HSSFSheet sheet = wbarray.createSheet("第1页");
        HSSFRow row = null;
        HSSFCell cell = null;

        int totalnum = list.size();// 总共多少条数据list
        for (int r = 0; r < totalnum + 1; r++) {
            row = sheet.createRow(r);
            row.setHeight((short) 350);
            for (int col = 0; col < 9; col++) {
                cell = row.createCell(col);
            }
        }

        for (int j = 0; j <= totalnum; j++) {
            row = sheet.getRow(j);
            cell = row.getCell(0);
            if (j == 0)
                cell.setCellValue("序号");
            else {
                cell.setCellValue(j);
            }

            cell = row.getCell(1);
            if (j == 0)
                cell.setCellValue("中奖纪录ID");
            else {
                cell.setCellValue(list.get(j - 1).get("recordId").toString());
            }

            cell = row.getCell(2);
            if (j == 0)
                cell.setCellValue("中奖人");
            else
                cell.setCellValue(list.get(j - 1).get("username").toString());

            cell = row.getCell(3);
            if (j == 0)
                cell.setCellValue("手机帐号");
            else
                cell.setCellValue(list.get(j - 1).get("mobile").toString());

            cell = row.getCell(4);
            if (j == 0)
                cell.setCellValue("所属院校");
            else {
                cell.setCellValue(list.get(j - 1).get("name").toString());
            }

            cell = row.getCell(5);
            if (j == 0)
                cell.setCellValue("奖品名称");
            else {
                if (list.get(j - 1).get("prizeTitle") != null) {
                    cell.setCellValue(list.get(j - 1).get("prizeTitle").toString());
                } else
                    cell.setCellValue("");
            }

            cell = row.getCell(6);
            if (j == 0)
                cell.setCellValue("中奖时间");
            else {
                cell.setCellValue(list.get(j - 1).get("createTime").toString());
            }

            cell = row.getCell(7);
            if (j == 0)
                cell.setCellValue("关联运动ID");
            else {
                cell.setCellValue(list.get(j - 1).get("runningRecordId").toString());
            }

            cell = row.getCell(8);
            if (j == 0)
                cell.setCellValue("兑奖码");
            else {
                cell.setCellValue(list.get(j - 1).get("prizeCode").toString());
            }
        }
        return ExcelExport.createExcel(response, request, name + "_抽奖记录", wbarray);
    }

    /**
     * 抽奖过程控制
     *
     * @param userId
     * @param activity
     * @param runningRecordId
     * @return Json
     * @throws Exception
     */
    public JSONObject cheakAndLuckDraw(long userId, Activity activity, String runningRecordId) throws Exception {
        JSONObject resJson = new JSONObject();
        resJson.put("isLuck", WebConstants.Boolean.FALSE.ordinal());
        resJson.put("prize", "很遗憾，没有中奖");
        resJson.put("prizeImg", "");
        long activityId = activity.getActivityId();
        int effectiveSignCount = activity.getEffectiveSignCount();//活动最多能参与的次数
        ActivityLuckRecord alr = new ActivityLuckRecord();
        alr.setActivityId(activityId);
        alr.setIsLuck(WebConstants.Boolean.FALSE.ordinal());
        alr.setCreateTime(new Date());
        alr.setPrizeId(null);
        alr.setRunningRecordId(runningRecordId);
        alr.setUserId(userId);

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        //1.判断当前 runningRecordId 是否已经参与过抽奖(查询抽奖记录)
        //测试时如果关闭这段验证了，生产时必须打开
        paramsMap.put("userId", userId);
        paramsMap.put("activityId", activityId);
        paramsMap.put("runningRecordId", runningRecordId);//一条跑步记录只能抽一次
        ActivityLuckRecord activityLuckRecord = activityLuckRecordService.getLuckRecordByActivity(paramsMap);
        if (null != activityLuckRecord) {
            System.out.println("很遗憾，没有中奖 (真实原因：本条跑步记录已经参与过抽奖，不能重复抽奖)");
            return resJson;//很遗憾，没有中奖 (真实原因：本条跑步记录已经参与过抽奖，不能重复抽奖)
//            throw new BusinessException(ResultMsg.RUNNINGRECORD_HAS_LUCK_DRAW);//本条跑步记录已经参与过抽奖，不能重复抽奖
        }

        if (effectiveSignCount > 0) {//如果活动设置了参与次数限制
            //判断活动最多能参与的次数是否已超限
            paramsMap = new HashMap<String, Object>();
            paramsMap.put("userId", userId);
            paramsMap.put("activityId", activityId);
            int myLuckDrawCount = activityLuckRecordService.getLuckCountByUser(paramsMap);//当前活动参与抽奖的总次数
            if (myLuckDrawCount >= effectiveSignCount) {
                //throw new BusinessException(ResultMsg.ACTIVITY_EFFECTIVESIGNCOUNT_ERROR);//您已达到活动可参与次数上限
                System.out.println("很遗憾，没有中奖 (真实原因：您已达到活动可参与次数上限)");
                return resJson;//很遗憾，没有中奖 (真实原因：您已达到活动可参与次数上限)
            }
        }

        //2.判断单日同一用户中奖次数是否达到上限
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        LuckDrawRule luckDrawRule = luckDrawRuleService.getLuckDrawRuleByActivityId(paramsMap);//规则：单日同一用户中奖次数上限
        if (null == luckDrawRule) {
            //throw new BusinessException(ResultMsg.LUCK_DRAW_RULE_NOT_EXIST);//当前活动抽奖规则不完整,无法抽奖
            System.out.println("很遗憾，没有中奖 (真实原因：当前活动抽奖规则不完整,无法抽奖)");
            return resJson;//很遗憾，没有中奖 (真实原因：当前活动抽奖规则不完整,无法抽奖)
        }
        int userLimit = luckDrawRule.getLuckLimit();//规则：同一用户中奖次数上限
        String currDay = DateUtil.getDateString(new Date(), "YYYY-MM-dd");
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", userId);
        paramsMap.put("activityId", activityId);
        paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        int myLuckCount = activityLuckRecordService.getLuckCountByUser(paramsMap);
        if (myLuckCount >= userLimit) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            System.out.println("很遗憾，没有中奖 (真实原因：中奖次数已达上限)");
            return resJson;//很遗憾，没有中奖 (真实原因：中奖次数已达当日上限)
        }
        //3.判断当前奖池内还有没有奖品(奖品数量，每个奖品可能有时段特性)
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        List<ActivityPrize> activityPrizeList = activityPrizeService.getActivityPrizeByActivityId(paramsMap);
        if (CollectionUtils.isEmpty(activityPrizeList)) {
            //throw new BusinessException(ResultMsg.ACTIVITY_PRIZE_NOT_EXIST);//当前活动奖品信息不完整,无法抽奖
            System.out.println("很遗憾，没有中奖 (真实原因：当前活动抽奖没有奖品信息,无法抽奖)");
            return resJson;//很遗憾，没有中奖 (真实原因：当前活动抽奖没有奖品信息,无法抽奖)
        }
        List<ActivityPrize> tempList = new ArrayList<>();
        Date currTime = new Date();
        for (ActivityPrize activityPrize : activityPrizeList) {
            int prizeTotal = activityPrize.getPrizeTotal();//奖品总数量
            int prizeSendTotal = activityPrize.getPrizeSendTotal();//已发放数量
            if ((prizeTotal - prizeSendTotal) > 0) {
                //if(currTime.compareTo(activityPrize.getStartTime()) < 0 || currTime.compareTo(activityPrize.getEndTime()) > 0)//不在派奖时间范围内
                if (currTime.compareTo(activityPrize.getStartTime()) >= 0 && currTime.compareTo(activityPrize.getEndTime()) <= 0) {
                    tempList.add(activityPrize);
                }
            }
        }
        activityPrizeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(tempList)) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            System.out.println("很遗憾，没有中奖 (真实原因：奖品没有了或奖品不在派奖时间范围内)");
            return resJson;//很遗憾，没有中奖 (真实原因：奖品没有了或奖品不在派奖时间范围内)
        }
        //4.判断当前奖池内的奖品当前用户是否有权获得
        for (ActivityPrize activityPrize : tempList) {
            int usrLimit = activityPrize.getUserLimit();//某个奖品，同一个用户能抽中的最大次数
            paramsMap = new HashMap<String, Object>();
            paramsMap.put("userId", userId);
            paramsMap.put("activityId", activityId);
            paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
            paramsMap.put("prizeId", activityPrize.getPrizeId());
            int prizeIhave = activityLuckRecordService.getPrizeCountByUser(paramsMap);//用户已经抽中了几次当前奖品
            //if(prizeIhave >= userLimit) //同一奖品用户获得数量不能超过规则限制
            if (prizeIhave < usrLimit) {
                activityPrizeList.add(activityPrize);
            }
        }
        tempList = null;
        if (CollectionUtils.isEmpty(activityPrizeList)) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            System.out.println("很遗憾，没有中奖 (真实原因：同一奖品用户获得数量不能超过规则限制)");
            return resJson;//很遗憾，没有中奖 (真实原因：同一奖品用户获得数量不能超过规则限制)
        }
        //5.按照综合中奖率给用户发抽奖资格
        int n = 0;
        float colligateRate = luckDrawRule.getColligateRate();//综合中奖率
        if (colligateRate != 100f) {
            n = (int) (Math.random() * 100);
        } else {
            n = 0;//如果设置百分百中奖, n 默认为0
        }


        /////////测试代码//////////////////////////////测试代码//////////////////////////////测试代码/////////////////////
/////////测试代码//////////////////////////////测试代码//////////////////////////////测试代码/////////////////////
        //n = 0;/////////测试代码//////////////////////////////测试代码//////////////////////////////测试代码/////////////////////
/////////测试代码//////////////////////////////测试代码//////////////////////////////测试代码/////////////////////
        /////////测试代码//////////////////////////////测试代码//////////////////////////////测试代码/////////////////////


        if (n > colligateRate) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            System.out.println("很遗憾，没有中奖 (真实原因：被综合中奖率过滤)");
            return resJson;//很遗憾，没有中奖 (真实原因：被综合中奖率过滤)
        }
        //6.按照抽奖算法抽奖
        int index = 0;
        if (activityPrizeList.size() > 1) {
            index = this.getPrizeIndex(activityPrizeList);
            if (index < 0) {
                System.out.println("很遗憾，没有中奖 (真实原因：抽奖出错，请检查01)");
                return resJson;//很遗憾，没有中奖 (真实原因：抽奖出错，请检查)
//                throw new BusinessException(ResultMsg.GET_PRIZE_INDEX_ERROR);//抽奖出错，请检查
            }
        }
        ActivityPrize activityPrize = activityPrizeList.get(index);//抽中奖品
        if (null == activityPrize) {
            System.out.println("很遗憾，没有中奖 (真实原因：抽奖出错，请检查02)");
            return resJson;//很遗憾，没有中奖 (真实原因：抽奖出错，请检查)
//            throw new BusinessException(ResultMsg.GET_PRIZE_INDEX_ERROR);//抽奖出错，请检查
        }
        alr.setIsLuck(WebConstants.Boolean.TRUE.ordinal());
        alr.setPrizeId(activityPrize.getPrizeId());
        alr.setPrizeCode(StringUtil.getTimeNum());//按系统时间生成8位16进制字符串
        activityLuckRecordService.insertActivityLuckRecord(alr);

        resJson.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        resJson.put("prize", activityPrize.getPrizeTitle());
//        resJson.put("prizeImg", luckDrawRule.getShareImg());
        resJson.put("prizeImg", activityPrize.getPrizeImg());

        return resJson;
    }


    /**
     * 轮盘抽奖
     * @param userId
     * @param activity
     * @param runningRecordId
     * @return
     * @throws Exception
     */
    public Map<String,Object> rouletteLuck(long userId, Activity activity, String runningRecordId) throws Exception {
        Map<String,Object> luckRes = new HashMap<>();
        luckRes.put("isLuck",WebConstants.Boolean.FALSE.ordinal()); //是否中奖
        luckRes.put("prize","很遗憾，没有中奖"); //奖品名称
        luckRes.put("prizeImg",""); //奖品图片
        luckRes.put("activityId",activity.getActivityId());
        luckRes.put("activityName",activity.getName());
        luckRes.put("prizeList",null); //轮盘面板信息

        long activityId = activity.getActivityId();
        int effectiveSignCount = activity.getEffectiveSignCount();//活动最多能参与的次数
        ActivityLuckRecord alr = new ActivityLuckRecord();
        alr.setActivityId(activityId);
        alr.setIsLuck(WebConstants.Boolean.FALSE.ordinal());
        alr.setCreateTime(new Date());
        alr.setPrizeId(null);
        alr.setRunningRecordId(runningRecordId);
        alr.setUserId(userId);

        Map<String, Object> paramsMap = new HashMap<String, Object>();

        //1.判断当前奖池内还有没有奖品(奖品数量，每个奖品可能有时段特性)
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        List<ActivityPrize> activityPrizeList = activityPrizeService.getActivityPrizeByActivityId(paramsMap);

        List<Map<String,Object>> prizeList = null; //轮盘面板信息
        if (CollectionUtils.isEmpty(activityPrizeList)) {
            log.info("很遗憾，没有中奖 (真实原因：当前活动抽奖没有奖品信息,无法抽奖)");
            luckRes.put("prizeList",null);
            return luckRes;
        }else {
            prizeList = new ArrayList<>();
            for (ActivityPrize activityPrize : activityPrizeList) {
                Map<String, Object> prize = new HashMap<>();
                prize.put("prizeId", activityPrize.getPrizeId());
                prize.put("prizeTitle", activityPrize.getPrizeTitle());
                prize.put("PrizeImg", activityPrize.getPrizeImg());
                prizeList.add(prize);
            }
            luckRes.put("prizeList",prizeList);
        }

        //2.判断当前 runningRecordId 是否已经参与过抽奖(查询抽奖记录)
        paramsMap.put("userId", userId);
        paramsMap.put("activityId", activityId);
        paramsMap.put("runningRecordId", runningRecordId);//一条跑步记录只能抽一次
        ActivityLuckRecord activityLuckRecord = activityLuckRecordService.getLuckRecordByActivity(paramsMap);
//        if (null != activityLuckRecord) {
//            log.info("很遗憾，没有中奖 (真实原因：本条跑步记录已经参与过抽奖，不能重复抽奖)");
//            return luckRes;
//        }

        if (effectiveSignCount > 0) {//如果活动设置了有效打卡次数限制
            paramsMap = new HashMap<String, Object>();
            paramsMap.put("userId", userId);
            paramsMap.put("activityId", activityId);
            int myLuckDrawCount = activityLuckRecordService.getLuckCountByUser(paramsMap);//当前活动参与抽奖的总次数
            log.info("判断活动最多能参与的次数是否已超限 myLuckDrawCount ={}，effectiveSignCount ={}",myLuckDrawCount,effectiveSignCount);
            if (myLuckDrawCount >= effectiveSignCount) {
                log.info("很遗憾，没有中奖 (真实原因：您已达到活动可参与次数上限)");
                return luckRes;
            }
        }

        //3.判断同一用户中奖次数是否达到上限
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        LuckDrawRule luckDrawRule = luckDrawRuleService.getLuckDrawRuleByActivityId(paramsMap);//规则：同一用户中奖次数上限
        if (null == luckDrawRule) {
            log.info("很遗憾，没有中奖 (真实原因：当前活动抽奖规则不完整,无法抽奖)");
            return luckRes;
        }


        int luckLimit = luckDrawRule.getLuckLimit();//规则：同一用户中奖次数上限
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", userId);
        paramsMap.put("activityId", activityId);
        paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        int myLuckCount = activityLuckRecordService.getLuckCountByUser(paramsMap);
        if (myLuckCount >= luckLimit && luckLimit != 0) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            log.info("很遗憾，没有中奖 (真实原因：中奖次数已达当日上限)");
            return luckRes;
        }

        List<ActivityPrize> tempList = new ArrayList<>();
        Date currTime = new Date();
        for (ActivityPrize activityPrize : activityPrizeList) {
            //组装轮盘信息
            int prizeTotal = activityPrize.getPrizeTotal();//奖品总数量
            int prizeSendTotal = activityPrize.getPrizeSendTotal();//已发放数量
            if ((prizeTotal - prizeSendTotal) > 5) {
                if (currTime.compareTo(activityPrize.getStartTime()) >= 0 && currTime.compareTo(activityPrize.getEndTime()) <= 0) {
                    tempList.add(activityPrize);
                }
            }else if((prizeTotal - prizeSendTotal) > 0 && (prizeTotal - prizeSendTotal) < 5){
                //高并发下，要考虑防止出现中奖数大于奖品数量，必要时，加缓存队列减少流量，这里用lock实现同步锁，synchronized 在大量访问的时候性能会低于lock
                log.info("奖品={},最后5个数量，进入同步状态，防止抽中但没奖品出现*",activityPrize.getPrizeTitle());
                Lock lock = new ReentrantLock();
                lock.lock();
                try {
                    log.info("**奖品={}，获取抽奖同步锁**",activityPrize.getPrizeTitle());
                    if (currTime.compareTo(activityPrize.getStartTime()) >= 0 && currTime.compareTo(activityPrize.getEndTime()) <= 0) {
                        tempList.add(activityPrize);
                    }
                }catch (Exception e){
                    log.info("抽奖同步异常 ={}",e);
                }finally {
                    lock.unlock();
                    log.info("**奖品={},释放抽奖同步锁**",activityPrize.getPrizeTitle());
                }
            }
        }

        if (CollectionUtils.isEmpty(tempList)) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            log.info("很遗憾，没有中奖 (真实原因：奖品没有了或奖品不在派奖时间范围内)");
            return luckRes;
        }
        //4.判断当前奖池内的奖品当前用户是否有权获得
        for (ActivityPrize activityPrize : tempList) {
            int userLimit = activityPrize.getUserLimit();//某个奖品，同一个用户能抽中的最大次数
            paramsMap = new HashMap<String, Object>();
            paramsMap.put("userId", userId);
            paramsMap.put("activityId", activityId);
            paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
            paramsMap.put("prizeId", activityPrize.getPrizeId());
            int prizeIhave = activityLuckRecordService.getPrizeCountByUser(paramsMap);//用户已经抽中了几次当前奖品
            log.info("用户userId = {} ,已经抽本奖品 prizeId = {}, 次数 ={} ",userId,activityPrize.getPrizeId(),prizeIhave);
            if (prizeIhave < userLimit) {
                activityPrizeList.add(activityPrize);
            }
        }
        tempList = null;
        if (CollectionUtils.isEmpty(activityPrizeList)) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            log.info("很遗憾，没有中奖 (真实原因：同一奖品用户获得数量不能超过规则限制)");
            return luckRes;
        }
        //5.按照综合中奖率给用户发抽奖资格
        int n = 0;
        float colligateRate = luckDrawRule.getColligateRate();//综合中奖率
        if (colligateRate != 100f) {
            n = (int) (Math.random() * 100);
        } else {
            n = 0;//如果设置百分百中奖, n 默认为0
        }

        if (n > colligateRate) {
            activityLuckRecordService.insertActivityLuckRecord(alr);
            log.info("很遗憾，没有中奖 (真实原因：被综合中奖率过滤)");
            return luckRes;
        }
        //6.按照抽奖算法抽奖
        int index = 0;
        if (activityPrizeList.size() > 1) {
            index = this.getPrizeIndex(activityPrizeList);
            if (index < 0) {
                log.info("很遗憾，没有中奖 (真实原因：抽奖出错，请检查01)");
                return luckRes;
            }
        }
        ActivityPrize activityPrize = activityPrizeList.get(index);//抽中奖品
        if (null == activityPrize) {
            log.info("很遗憾，没有中奖 (真实原因：抽奖出错，请检查02)");
            return luckRes;
        }
        alr.setIsLuck(WebConstants.Boolean.TRUE.ordinal());
        alr.setPrizeId(activityPrize.getPrizeId());
        alr.setPrizeCode(StringUtil.getTimeNum());//按系统时间生成8位16进制字符串
        activityLuckRecordService.insertActivityLuckRecord(alr);

        /*********************************轮盘，盘面信息*********************************/
        //Map<String,Object> luckRes = new HashMap<>();
        luckRes.put("isLuck",WebConstants.Boolean.TRUE.ordinal()); //是否中奖
        luckRes.put("prize",activityPrize.getPrizeTitle()); //奖品名称
        luckRes.put("prizeImg",activityPrize.getPrizeImg()); //奖品图片
        luckRes.put("prizeId",activityPrize.getPrizeId()); //奖品ID
        luckRes.put("prizeList",prizeList); //轮盘面板信息

        return luckRes;
    }
}
