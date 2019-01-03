package com.amico.im.client.net.dto;

import java.util.Date;

/****
 * 群聊天
 * @author 黄琦
 *
 */
public class GroupChatDto {
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
	//群组ID
	private String GroupId;
	
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
	
	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String groupId) {
		GroupId = groupId;
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
