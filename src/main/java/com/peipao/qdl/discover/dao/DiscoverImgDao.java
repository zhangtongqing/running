package com.peipao.qdl.discover.dao;


import com.peipao.qdl.discover.model.DiscoverImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverImgDao
 * 功能描述：DiscoverImgDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface DiscoverImgDao {
    void insertDiscoverImgByBatch(@Param("discoverId") Long discoverId, @Param("discoverImgList") List<DiscoverImg> discoverImgList) throws Exception;

    List<Map<String, String>> getImgListByDiscoverId(@Param("discoverId") Long discoverId) throws Exception;
}
