package com.peipao.framework.model;


import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.exception.BusinessException;

public class Response<T> {

	private T body;
	private int code;
	private String message;
	private String exceptionDescription;

	public static <T> Response<T> success() {
		return new Response<T>(ReturnStatus.SUCCESS);
	}

	public static <T> Response<T> success(T body) {
		return new Response<T>(body, ReturnStatus.SUCCESS);
	}

	public static <T> Response<T> fail() {
		return new Response<T>(ReturnStatus.BUSINESS_ERROR);
	}

	public static <T> Response<T> fail(ReturnStatus status) {
		return new Response<T>(status);
	}

    public static <T> Response<T> fail(ResultMsg resultMsg) {
        return new Response<T>(resultMsg);
    }

	public static <T> Response<T> fail(ReturnStatus status, Exception e) {
		Response<T> response = new Response<T>(status);
		if (status.getValue() <= ReturnStatus.BUSINESS_ERROR.getValue()) {
			response.setExceptionDescription(e.getMessage());
		}
		return response;
	}

	public static <T> Response<T> customStatusAndValues(ReturnStatus status, T body) {
		return new Response<T>(body, status);
	}

	public static <T> Response<T> customStatus(ReturnStatus status) {
		return new Response<T>(status);
	}

	public static <T> Response<T> fail(BusinessException be, T body) {
		return new Response<T>(be, body);
	}

	private Response(ReturnStatus returnStatus) {
		this.code = returnStatus.getValue();
		this.message = returnStatus.getDescription();
	}

	private Response(ResultMsg resultMsg) {
		this.code = Integer.parseInt(resultMsg.getCode());
		this.message = resultMsg.getMsg();
	}

	private Response(T body, ReturnStatus returnStatus) {
		super();
		this.body = body;
		this.code = returnStatus.getValue();
		this.message = returnStatus.getDescription();
	}

	private Response(BusinessException be, T body) {
		this.code = be.getCode();
		this.message = be.getDescription();
		this.body = body;
	}

	public T getbody() {
		return body;
	}

	public int getCode() {
		if (code <= 0) {
			code = ReturnStatus.SUCCESS.getValue();
		}
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getExceptionDescription() {
		return exceptionDescription;
	}

	public void setExceptionDescription(String exceptionDescription) {
		this.exceptionDescription = exceptionDescription;
	}

}