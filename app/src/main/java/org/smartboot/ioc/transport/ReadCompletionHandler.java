/*
 * Copyright (c) 2017, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: ReadCompletionHandler.java
 * Date: 2017-11-25
 * Author: sandao
 */

package org.smartboot.ioc.transport;

import android.util.Log;

import org.smartboot.ioc.Filter;
import org.smartboot.ioc.StateMachineEnum;


import java.io.IOException;

/**
 * 读写事件回调处理类
 *
 * @author 三刀
 * @version V1.0.0
 */
class ReadCompletionHandler<T> implements CompletionHandler<Integer, NioSession<T>> {

    @Override
    public void completed(final Integer result, final NioSession<T> aioSession) {
        // 接收到的消息进行预处理
        for (Filter h : aioSession.getServerConfig().getFilters()) {
            h.readFilter(aioSession, result);
        }
        aioSession.readFromChannel(result == -1);
    }

    @Override
    public void failed(Throwable exc, NioSession<T> aioSession) {
        if (exc instanceof IOException) {
            Log.d("session error",exc.getMessage());
        } else {
            Log.d("session error",exc.getMessage());
        }

        try {
            aioSession.getServerConfig().getProcessor().stateEvent(aioSession, StateMachineEnum.INPUT_EXCEPTION, exc);
        } catch (Exception e) {
            Log.d("session error", e.getMessage());
        }
        try {
            aioSession.close();
        } catch (Exception e) {
            Log.d("session error", e.getMessage());
        }
    }
}