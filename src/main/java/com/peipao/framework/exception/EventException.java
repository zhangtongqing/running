package com.peipao.framework.exception;

import com.peipao.framework.constant.ResultMsg;


@SuppressWarnings("serial")
public class EventException extends RuntimeException {
	private int code;
	private String eventKey;
	private String description;
	private String params;
	private ResultMsg resultMsg;

	public EventException(ResultMsg resultMsg, String eventKey, String params) {
		this.resultMsg = resultMsg;
		this.code = Integer.parseInt(resultMsg.getCode());
		this.description = resultMsg.getMsg();
		this.params = params;
		this.eventKey = eventKey;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public ResultMsg getResultMsg() {
        return resultMsg;
    }
}
