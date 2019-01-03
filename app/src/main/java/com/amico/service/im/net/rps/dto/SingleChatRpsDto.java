package com.amico.service.im.net.rps.dto;

import java.util.Date;

/****
 * 1对1聊天
 * @author 黄琦
 *
 */
public class SingleChatRpsDto {
	//发送人ID
	private String userId;
	//消息时间
	private Date messageTime;
	//消息类型
	private String messageType;
	//消息内容
	private String content;
	//消息编号
	private String messageId;
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Date getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
