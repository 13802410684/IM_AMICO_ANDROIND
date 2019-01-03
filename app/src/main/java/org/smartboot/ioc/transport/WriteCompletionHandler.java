/*
 * Copyright (c) 2017, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: WriteCompletionHandler.java
 * Date: 2017-11-25
 * Author: sandao
 */

package org.smartboot.ioc.transport;

import android.util.Log;

import org.smartboot.ioc.Filter;
import org.smartboot.ioc.StateMachineEnum;



/**
 * 读写事件回调处理类
 *
 * @author 三刀
 * @version V1.0.0
 */
class WriteCompletionHandler<T> implements CompletionHandler<Integer, NioSession<T>> {


    @Override
    public void completed(final Integer result, final NioSession<T> aioSession) {
        // 接收到的消息进行预处理
        for (Filter h : aioSession.getServerConfig().getFilters()) {
            h.writeFilter(aioSession, result);
        }
        aioSession.writeToChannel();
    }

    @Override
    public void failed(Throwable exc, NioSession<T> aioSession) {
        try {
            aioSession.getServerConfig().getProcessor().stateEvent(aioSession, StateMachineEnum.OUTPUT_EXCEPTION, exc);
        } catch (Exception e) {
        }
        try {
            aioSession.close();
        } catch (Exception e) {
        }
    }
}