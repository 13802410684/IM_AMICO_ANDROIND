package org.smartboot.ioc;

import org.smartboot.ioc.transport.NioSession;

/**
 * 消息处理器
 *
 * @author 三刀
 * @version MessageProcessor.java, v 0.1 2015年3月13日 下午3:26:55 Seer Exp.
 */
public interface MessageProcessor<T> {

    /**
     * 处理接收到的消息
     *
     * @param session
     * @param msg
     * @throws Exception
     */
    public void process(NioSession<T> session, T msg);

    /**
     * 状态机事件,当枚举事件发生时由框架触发该方法
     *
     * @param session
     * @param stateMachineEnum 状态枚举
     * @param throwable        异常对象，如果存在的话
     */
    void stateEvent(NioSession<T> session, StateMachineEnum stateMachineEnum, Throwable throwable);
}
