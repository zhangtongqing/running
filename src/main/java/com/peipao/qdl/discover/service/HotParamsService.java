package com.peipao.qdl.discover.service;


import com.peipao.qdl.discover.model.HotParams;

/**
 * 方法名称：HotParamssService
 * 功能描述：HotParamssService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/25 17:24
 * 修订记录：
 */
public interface HotParamsService {

    /**
     * 根据ID获取规则参数
     * @return
     * @throws Exception
     */
    HotParams getHotParams() throws Exception;

    /**
     * 添加或修改 热门规则参数
     * @param hotParams
     */
    void inserOrUpdateHotParams(HotParams hotParams) throws Exception;

}
