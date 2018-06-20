package com.peipao.qdl.discover.service;


import com.peipao.qdl.discover.dao.HotParamsDao;
import com.peipao.qdl.discover.model.HotParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 方法名称：HotParamssServiceImpl
 * 功能描述：HotParamssServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/25 17:24
 * 修订记录：
 */
@Service
public class HotParamsServiceImpl implements HotParamsService{

    @Autowired
    private HotParamsDao hotParamsDao;

    public HotParams getHotParams() throws Exception {
        return this.hotParamsDao.getHotParams();
    }

    /**
     * 添加热门规则参数
     * @param hotParams
     */
    @Override
    @Transactional
    public void inserOrUpdateHotParams(HotParams hotParams) throws Exception {
        if(null != hotParams.getHotParamsId() && 0L != hotParams.getHotParamsId()) {
            this.hotParamsDao.updateHotParams(hotParams);
        } else {
            this.hotParamsDao.inserHotParams(hotParams);
        }

    }
}
