package com.peipao.qdl.discover.dao;


import com.peipao.qdl.discover.model.HotParams;
import org.springframework.stereotype.Repository;

/**
 * 方法名称：HotParamsDao
 * 功能描述：HotParamsDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface HotParamsDao {

    /**
     * 根据ID获取规则参数
     * @return
     * @throws Exception
     */
    HotParams getHotParams() throws Exception;

    /**
     * 添加热门规则参数
     * @param hotParams
     */
    void inserHotParams(HotParams hotParams) throws Exception;

    /**
     * 修改热门规则参数
     * @param hotParams
     */
    void updateHotParams(HotParams hotParams) throws Exception;
}
