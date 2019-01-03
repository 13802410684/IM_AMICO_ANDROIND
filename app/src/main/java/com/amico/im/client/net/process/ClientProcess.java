package com.amico.im.client.net.process;

import org.smartboot.ioc.MessageProcessor;
import org.smartboot.ioc.transport.NioSession;
import org.smartboot.ioc.StateMachineEnum;
import org.smartboot.ioc.transport.NioSession;

import com.alibaba.fastjson.JSON;
import com.amico.im.client.dto.map.ImRpsModelMap;
import com.amico.im.client.net.IMClientService;
import com.amico.im.client.net.listener.ConnectListener;
import com.amico.im.client.net.process.req.ClientPacket;
import com.amico.service.im.net.req.protobuf.ResponseProto;
import com.amico.service.im.net.req.protobuf.ResponseProto.ResponseMessageProto;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class ClientProcess implements MessageProcessor<ClientPacket> {

	private NioSession<ClientPacket> session;
	private ConnectListener connectListener;
	private IMClientService imClientService;
	
	public ClientProcess(ConnectListener connectListener,IMClientService imClientService) {
		this.connectListener = connectListener;
		this.imClientService = imClientService;
	}
	
	public NioSession<ClientPacket> getSession() {
		if(session!=null) {
			return session;
		}else {
			return null;
		}
	}

	@Override
	public void process(NioSession<ClientPacket> session, ClientPacket msg) {
		// TODO Auto-generated method stub
		byte[] bodyByte = msg.getBody();
		try {
			ResponseMessageProto rpsMessage= ResponseProto.ResponseMessageProto.parseFrom(bodyByte);
			String errorInfo = rpsMessage.getErrorInfo();
			String errorNo = rpsMessage.getErrorNo();
			String className = rpsMessage.getClassName();
			ByteString message = rpsMessage.getData();
			byte [] contentByte = message.toByteArray();
			Class<?> clsMap = ImRpsModelMap.getClassByName(className);
			if(clsMap == null) {
				connectListener.onError(session, "-1","");
			}
			if("0".equals(errorNo)) {
				Object result = JSON.parseObject(contentByte, clsMap);
				connectListener.onSuccess(session, result);
			}else {
				connectListener.onError(session, errorNo, errorInfo);
			}
		} catch (InvalidProtocolBufferException e) {
		
		}
	}

	@Override
	public void stateEvent(NioSession<ClientPacket> session, StateMachineEnum stateMachineEnum, Throwable throwable) {
		// TODO Auto-generated method stub
		switch (stateMachineEnum) {
		case NEW_SESSION:
			this.session = session;
			this.session.setAttachment(this.imClientService);
			//当链接成功后通知
			connectListener.onConnected(session);
			break;
		case SESSION_CLOSED:
			connectListener.onClose(session);
			break;
		default:
			break;
		}
	}

}
