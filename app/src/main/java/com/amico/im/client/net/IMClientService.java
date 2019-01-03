package com.amico.im.client.net;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.smartboot.ioc.transport.NioQuickClient;

import com.alibaba.fastjson.JSON;
import com.amico.im.client.net.consts.DtoConsts;
import com.amico.im.client.net.consts.EventConsts;
import com.amico.im.client.net.dto.AddFriendDto;
import com.amico.im.client.net.dto.GroupChatDto;
import com.amico.im.client.net.dto.LoginDto;
import com.amico.im.client.net.dto.SingleChatDto;
import com.amico.im.client.net.listener.ConnectListener;
import com.amico.im.client.net.process.ClientProcess;
import com.amico.im.client.net.process.req.ClientPacket;
import com.amico.im.client.net.protocol.ClientProtocol;
import com.amico.service.im.net.req.protobuf.RequestProto;
import com.amico.service.im.net.req.protobuf.RequestProto.RequestMessageProto.Builder;
import com.google.protobuf.ByteString;

public class IMClientService implements Runnable{

    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private ClientProcess clientProcess;
	private NioQuickClient<ClientPacket> aioQuickClient;
	private ConnectListener connectListener;
	
	public IMClientService(ConnectListener connectListener) {
		this.connectListener =connectListener ;

		clientProcess = new ClientProcess(this.connectListener,this);
	}
	
	public void connect(String ip ,int port) throws IOException, ExecutionException, InterruptedException {
        aioQuickClient = new NioQuickClient<ClientPacket>(ip, port, new ClientProtocol(), clientProcess);
        new Thread(this).start();
	}
	
	public void sendMessage(ClientPacket clentPacket) {
		if(clientProcess.getSession()!=null) {
			try {
				clientProcess.getSession().write(clentPacket);
			} catch (IOException e) {
				//记住LOG
                Log.e(this.getClass().getName(),e.getMessage());
			}
		}else {
            Log.e(this.getClass().getName(),"session is null");
		}
	}

    /***
     * 加好友
     */
    public void sendAddFriends(String userId,String friendId,String desc){
        ClientPacket packet = new ClientPacket();
        Builder rewMessage = RequestProto.RequestMessageProto.newBuilder();
        rewMessage.setEvent(EventConsts.BaseBiz);
        rewMessage.setClassName(DtoConsts.AddFriendDto);
        AddFriendDto addFriendDto = new AddFriendDto();
        addFriendDto.setDesc(desc);
        addFriendDto.setUserId(userId);
        addFriendDto.setFriendUserId(friendId);
        rewMessage.setData(ByteString.copyFrom(JSON.toJSONString(addFriendDto).getBytes()));
        packet.setBody(rewMessage.build().toByteArray());
        sendMessage(packet);
    }
	
	/***
	 * 登录IM
	 */
	public void loginIMService(String userId,String token) {
		ClientPacket packet = new ClientPacket();
		Builder rewMessage = RequestProto.RequestMessageProto.newBuilder();
		rewMessage.setEvent(EventConsts.BaseBiz);
		rewMessage.setClassName(DtoConsts.LoginDto);
		LoginDto loginDto = new LoginDto();
		loginDto.setToken(token);
		loginDto.setUserId(userId);
		rewMessage.setData(ByteString.copyFrom(JSON.toJSONString(loginDto).getBytes()));
		packet.setBody(rewMessage.build().toByteArray());
		sendMessage(packet);
	}
	
	/**
	 * 发送聊天消息
	 * @param singleChatDto
	 * @throws IOException
	 */
	public void sendChatMessage(SingleChatDto singleChatDto)  {
		ClientPacket packet = new ClientPacket();
		Builder rewMessage = RequestProto.RequestMessageProto.newBuilder();
		rewMessage.setEvent(EventConsts.Chat);
		rewMessage.setClassName(DtoConsts.SingleChatDto);
		rewMessage.setData(ByteString.copyFrom(JSON.toJSONString(singleChatDto).getBytes()));
		packet.setBody(rewMessage.build().toByteArray());
		sendMessage(packet);
	}
	
	/**
	 * 发送聊天消息
	 * @param
	 * @throws IOException
	 */
	public void sendGroupChatMessage(GroupChatDto groupChatDto) {
		ClientPacket packet = new ClientPacket();
		Builder rewMessage = RequestProto.RequestMessageProto.newBuilder();
		rewMessage.setEvent(EventConsts.Chat);
		rewMessage.setClassName(DtoConsts.SingleChatDto);
		rewMessage.setData(ByteString.copyFrom(JSON.toJSONString(groupChatDto).getBytes()));
		packet.setBody(rewMessage.build().toByteArray());
		sendMessage(packet);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
        aioQuickClient.start();
	}
	
} 
