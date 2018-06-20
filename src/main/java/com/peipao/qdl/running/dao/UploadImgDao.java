package com.peipao.qdl.running.dao;


import com.peipao.qdl.running.model.UploadImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 方法名称：UploadImgDao
 * 功能描述：UploadImgDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/28 11:15
 * 修订记录：
 */
@Repository
public interface UploadImgDao {

    List<UploadImg> getUploadImgListBySchoolIdAndType(@Param("schoolId") Long schoolId, @Param("modelType") int modelType, @Param("imgStatus") int imgStatus) throws Exception;

}
