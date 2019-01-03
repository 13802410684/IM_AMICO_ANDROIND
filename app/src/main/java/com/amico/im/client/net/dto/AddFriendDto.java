package com.amico.im.client.net.dto;

public class AddFriendDto {
	/** 发起请求的好友uid（本地用户） */
	protected String userId ;
	/** 将要被添加的好友uid */
	protected String friendUserId ;	
	/** 加好友时的验证说明（由请求发出方填写，像QQ一样） */
	protected String desc;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendUserId() {
		return friendUserId;
	}
	public void setFriendUserId(String friendUserId) {
		this.friendUserId = friendUserId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
