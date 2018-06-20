package com.peipao.qdl.sms.model;

import java.util.Date;

public class SmsVo {
	private String phone;// 手机号
	private Date expireTime;// 过期时间
	private int sendCount;// 发送计数
	private String code;// 验证码

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
