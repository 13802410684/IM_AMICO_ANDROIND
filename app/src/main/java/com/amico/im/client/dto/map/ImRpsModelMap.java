package com.amico.im.client.dto.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.amico.service.im.net.rps.dto.LoginRpsDto;
import com.amico.service.im.net.rps.dto.SingleChatRpsDto;


public class ImRpsModelMap {
	private static final Map<String, Class> dictMap = new ConcurrentHashMap<String, Class>();

	static {
		dictMap.put("LoginRpsDto", LoginRpsDto.class);
		dictMap.put("SingleChatRpsDto", SingleChatRpsDto.class);
	}

	public static Class getClassByName(String clsName) {
		if (clsName!=null&&clsName.equals("")==false) {
			return dictMap.get(clsName);
		} else {
			return null;
		}
	}
}
