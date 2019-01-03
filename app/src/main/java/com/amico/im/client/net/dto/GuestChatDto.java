package com.amico.im.client.net.dto;

import java.util.Date;

/***
 * 陌生人聊天
 * 
 * @author huangqi
 *
 */
public class GuestChatDto {
	// 发送人ID
	private String userId;
	// 接收人ID
	private String receiveUserId;
	// 接收人图片名称
	private String receiveUserAvatarFileName;
	// 消息时间
	private Date messageTime;
	// 消息类型
	private String messageType;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getReceiveUserAvatarFileName() {
		return receiveUserAvatarFileName;
	}
	public void setReceiveUserAvatarFileName(String receiveUserAvatarFileName) {
		this.receiveUserAvatarFileName = receiveUserAvatarFileName;
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
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	// 消息内容
	private String content;
	// 消息编号
	private String messageId;
	
	
}
