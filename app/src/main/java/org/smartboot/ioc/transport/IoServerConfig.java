/*
 * Copyright (c) 2017, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: IoServerConfig.java
 * Date: 2017-11-25
 * Author: sandao
 */

package org.smartboot.ioc.transport;


import org.smartboot.ioc.Filter;
import org.smartboot.ioc.MessageProcessor;
import org.smartboot.ioc.Protocol;

import java.util.Arrays;

/**
 * Quickly服务端/客户端配置信息 T:解码后生成的对象类型
 *
 * @author 三刀
 * @version V1.0.0
 */
final class IoServerConfig<T> {

    public static final String BANNER = "                               _                       \n" +
            "                              ( )_     _               \n" +
            "  ___   ___ ___     _ _  _ __ | ,_)   (_)   _      ___ \n" +
            "/',__)/' _ ` _ `\\ /'_` )( '__)| |     | | /'_`\\  /'___)\n" +
            "\\__, \\| ( ) ( ) |( (_| || |   | |_    | |( (_) )( (___ \n" +
            "(____/(_) (_) (_)`\\__,_)(_)   `\\__)   (_)`\\___/'`\\____)";

    public static final String VERSION = "v1.3.10";
    /**
     * 消息队列缓存大小
     */
    private int writeQueueSize = 0;

    /**
     * 消息体缓存大小,字节
     */
    private int readBufferSize = 512;

    /**
     * 远程服务器IP
     */
    private String host;


    /**
     * 服务器消息拦截器
     */
    private Filter<T>[] filters = new Filter[0];

    /**
     * 服务器端口号
     */
    private int port = 8888;

    /**
     * 消息处理器
     */
    private MessageProcessor<T> processor;

    /**
     * 协议编解码
     */
    private Protocol<T> protocol;

    /**
     * 是否启用控制台banner
     */
    private boolean bannerEnabled = true;


    public final String getHost() {
        return host;
    }

    public final void setHost(String host) {
        this.host = host;
    }

    public final int getPort() {
        return port;
    }

    public final void setPort(int port) {
        this.port = port;
    }


    public final Filter<T>[] getFilters() {
        return filters;
    }

    public final void setFilters(Filter<T>[] filters) {
        if (filters != null) {
            this.filters = filters;
        }
    }

    public Protocol<T> getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol<T> protocol) {
        this.protocol = protocol;
    }

    public final MessageProcessor<T> getProcessor() {
        return processor;
    }

    public final void setProcessor(MessageProcessor<T> processor) {
        this.processor = processor;
    }

    public int getWriteQueueSize() {
        return writeQueueSize;
    }

    public void setWriteQueueSize(int writeQueueSize) {
        this.writeQueueSize = writeQueueSize;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }


    public boolean isBannerEnabled() {
        return bannerEnabled;
    }

    public void setBannerEnabled(boolean bannerEnabled) {
        this.bannerEnabled = bannerEnabled;
    }


    @Override
    public String toString() {
        return "IoServerConfig{" +
                "writeQueueSize=" + writeQueueSize +
                ", readBufferSize=" + readBufferSize +
                ", host='" + host + '\'' +
                ", filters=" + Arrays.toString(filters) +
                ", port=" + port +
                ", processor=" + processor +
                ", protocol=" + protocol +
                ", bannerEnabled=" + bannerEnabled +
                '}';
    }
}
