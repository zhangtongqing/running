package com.peipao.qdl.sms.dao;



import com.peipao.qdl.sms.model.Sms;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsDao {

	// 新增
	void insertSms(Sms sms) throws Exception;

	// 修改
	void updateSms(Sms sms) throws Exception;

	// get
	List<Sms> getSms(int status) throws Exception;

	// get ID
	String getId() throws Exception;

}
