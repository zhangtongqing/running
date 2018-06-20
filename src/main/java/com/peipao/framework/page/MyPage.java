package com.peipao.framework.page;

import com.peipao.framework.constant.MyPageConstants;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.ValidateUtil;
import net.sf.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/7/26
 **/
public class MyPage {
    public static int[] isValidParams(JSONObject json){
        if(null == json){
            throw new BusinessException(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!json.containsKey("pageindex") || !json.containsKey("pagesize")){
            throw new BusinessException(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("pageindex")) || !ValidateUtil.isDigits(json.getString("pagesize"))) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        int pageindex = json.getInt("pageindex");
        int pagesize = json.getInt("pagesize");
        if (pageindex < 0 || pagesize <= 0) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        int[] ret = {pageindex, pagesize};
        return ret;
    }

    public static int[] isValidAppParams(JSONObject json){
        if(null == json){
            throw new BusinessException(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!json.containsKey("from") || !json.containsKey("num")){
            throw new BusinessException(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("from")) || !ValidateUtil.isDigits(json.getString("num"))) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        int from = json.getInt("from");
        int num = json.getInt("num");
        if (from < 0 || num <= 0) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        int[] ret = {from, num};
        return ret;
    }

    public static Map<String, Object> processPage(Long allcount, int pagesize, int pageindex, List list){
        Map<String, Object> map = new HashMap<>();
        long maxpage = (allcount / pagesize + (allcount % pagesize == 0 ? 0 : 1));
        map.put("maxpage", maxpage);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        map.put("total", allcount);
        map.put("hasNext", maxpage == pageindex ? WebConstants.Boolean.FALSE.ordinal() : WebConstants.Boolean.TRUE.ordinal());
        map.put("data", list);
        if (allcount > 0 && (allcount + pagesize/2)/pagesize == 0) {
            map.put("maxpage", 1);
        }
        return map;
    }

    public static int[] checkPageParams(JSONObject json) {
        String[] params = new String[]{"pageindex"};//必填项
        int pageindex = 1;
        int pagesize = MyPageConstants.PAGE_SIZE_APP;//默认每页查询15条
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            throw new BusinessException(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        pageindex = json.getInt("pageindex");
        if (!ValidateUtil.isDigits(pageindex+"") || pageindex < 0) {
            pageindex = 1;//分页页码参数错误，默认查询第一页
        }
        if(ValidateUtil.jsonValidateWithKey(json, "pagesize")) {
            pagesize = json.getInt("pagesize");
            if(!ValidateUtil.isDigits(pagesize+"") || pagesize < 1) {
                pagesize = MyPageConstants.PAGE_SIZE_APP;//如果pagesize参数不对，还使用默认值
            }
        }
        int[] res = {pageindex, pagesize};
        return res;
    }

}
