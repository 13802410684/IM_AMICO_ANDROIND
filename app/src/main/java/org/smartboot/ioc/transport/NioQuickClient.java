package org.smartboot.ioc.transport;

import android.util.Log;

import org.smartboot.ioc.MessageProcessor;
import org.smartboot.ioc.Protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @version NioQuickClient.java, v 0.1 2015年3月20日 下午2:55:08 Seer Exp.
 * @author三刀
 */
public class NioQuickClient<T> {

    /**
     * Socket连接锁,用于监听连接超时
     */
    private final Object connectLock = new Object();
    /**
     * 服务配置
     */
    private IoServerConfig<T> config = new IoServerConfig<T>();
    private Selector selector;
    /**
     * 传输层Channel服务处理线程
     */
    private Thread serverThread;
    /**
     * 客户端会话信息
     */
    private NioSession<T> session;

    private AsynchronousSocketChannel asynchronousSocketChannel;

    public NioQuickClient(String host, int port, Protocol<T> protocol, MessageProcessor<T> messageProcessor) {
        config.setHost(host);
        config.setPort(port);
        config.setProtocol(protocol);
        config.setProcessor(messageProcessor);
    }

    /**
     * 接受并建立客户端与服务端的连接
     *
     * @param key
     * @throws IOException
     */
    private void acceptConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.finishConnect();
        key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT | SelectionKey.OP_READ);

        session = new NioSession<T>(asynchronousSocketChannel, config, new ReadCompletionHandler(), new WriteCompletionHandler(), false);
        session.initSession();
        Log.i(this.getClass().getName(),"success connect to " + channel.socket().getRemoteSocketAddress().toString());
        synchronized (connectLock) {
            connectLock.notifyAll();
        }
    }


    public final void shutdown() {
        try {
            selector.close();
            selector.wakeup();
        } catch (final IOException e) {
          //  logger.warn(e.getMessage(), e);
            Log.w(e.getMessage(), e);
        }
        session.close();
    }


    public final void start() {
        try {
            if (config.isBannerEnabled()) {
                System.out.println(IoServerConfig.BANNER);
            }
            selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);

            asynchronousSocketChannel = new AsynchronousSocketChannel(selectionKey);
            socketChannel.connect(new InetSocketAddress(config.getHost(), config.getPort()));
            serverThread = new Thread(new ClientTask(), "QuickClient-" + hashCode());
            serverThread.start();

            if (session != null) {
                return;
            }
            synchronized (connectLock) {
                if (session != null) {
                    return;
                }
                try {
                    connectLock.wait();
                } catch (final InterruptedException e) {
                    Log.w("", e);
                }
            }

        } catch (final IOException e) {
            Log.w("", e);
        }
    }

    /**
     * 设置输出队列缓冲区长度。输出缓冲区的内存大小取决于size个ByteBuffer的大小总和。
     *
     * @param size 缓冲区数组长度
     */
    public final NioQuickClient<T> setWriteQueueSize(int size) {
        this.config.setWriteQueueSize(size);
        return this;
    }

    class ClientTask implements Runnable {

        @Override
        public void run() {
            try {
                while (asynchronousSocketChannel.isOpen()) {
                    // 优先获取SelectionKey,若无关注事件触发则阻塞在selector.select(),减少select被调用次数
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    if (keySet.isEmpty()) {
                        selector.select();
                    }
                    Iterator<SelectionKey> keyIterator = keySet.iterator();
                    // 执行本次已触发待处理的事件
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        // 读取客户端数据
                        if (key.isReadable()) {
                            asynchronousSocketChannel.removeOps(SelectionKey.OP_READ);
                            asynchronousSocketChannel.doRead();
                        } else if (key.isWritable()) {// 输出数据至客户端
                            asynchronousSocketChannel.removeOps(SelectionKey.OP_WRITE);
                            asynchronousSocketChannel.doWrite();
                        } else if (key.isConnectable()) {// 建立新连接,Client触发Connect,Server触发Accept
                            acceptConnect(key);
                        } else {
                            Log.w("","error");
                        }
                        // 移除已处理的事件
                        keyIterator.remove();
                    }
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage());
                shutdown();
            }
            Log.i(this.getClass().getName(), "Channel is stop!");
        }
    }
}
