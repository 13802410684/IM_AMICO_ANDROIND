package com.amico.im.client.net.listener;

import org.smartboot.ioc.transport.NioSession;

import com.amico.im.client.net.process.req.ClientPacket;

public interface ConnectListener {
	
	public void onConnected(NioSession<ClientPacket> session);
	
	public void onClose(NioSession<ClientPacket> session);
	
	public void onSuccess(NioSession<ClientPacket> session,Object result);
	
	public void onError(NioSession<ClientPacket> session,String errorNo,String errorInfo);
	
}
