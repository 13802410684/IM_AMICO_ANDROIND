package com.amico.service.im.net.rps.dto;

public class AddFriendRpsDto {
	/** 将要被添加的好友uid */
	protected String errorNo ;
	protected String errorInfo ;

	public String getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(String errorNo) {
		this.errorNo = errorNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
