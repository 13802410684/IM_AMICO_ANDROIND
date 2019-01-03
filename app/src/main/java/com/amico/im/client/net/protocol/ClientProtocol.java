package com.amico.im.client.net.protocol;

import android.util.Log;

import java.nio.ByteBuffer;

import org.smartboot.ioc.Protocol;
import org.smartboot.ioc.transport.NioSession;

import com.amico.im.client.net.process.req.ClientPacket;

public class ClientProtocol implements Protocol<ClientPacket> {

	@Override
	public ClientPacket decode(ByteBuffer readBuffer, NioSession<ClientPacket> session,boolean eof) {
		// TODO Auto-generated method stub
		int readableLength = readBuffer.remaining();
		if (readableLength < ClientPacket.HEADER_LENGHT) {
			return null;
		}
		// 读取消息体的长度
		int bodyLength = readBuffer.getInt();
		// 数据不正确，则抛出AioDecodeException异常
		if (bodyLength < 0) {
			Log.e(getClass().getName(),"bodyLength [" + bodyLength + "] is not right, remote:" + session.getSessionID());
			return null;
		}
		// 计算本次需要的数据长度
		int neededLength = ClientPacket.HEADER_LENGHT + bodyLength;
		// 收到的数据是否足够组包
		int isDataEnough = readableLength - neededLength;
		// 不够消息体长度(剩下的buffe组不了消息体)
		if (isDataEnough < 0) {
			return null;
		} else // 组包成功
		{
			ClientPacket imPacket = new ClientPacket();
			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				readBuffer.get(dst);
				imPacket.setBody(dst);
			}
			return imPacket;
		}
	}

	@Override
	public ByteBuffer encode(ClientPacket imPacket, NioSession<ClientPacket> session) {
		// TODO Auto-generated method stub
		byte[] body = imPacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}
		// bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
		int allLen = ClientPacket.HEADER_LENGHT + bodyLen;
		// 创建一个新的bytebuffer
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		// 写入消息头----消息头的内容就是消息体的长度
		buffer.putInt(bodyLen);
		// 写入消息体
		if (body != null) {
			buffer.put(body);
		}
		buffer.flip();
		return buffer;
	}

}
