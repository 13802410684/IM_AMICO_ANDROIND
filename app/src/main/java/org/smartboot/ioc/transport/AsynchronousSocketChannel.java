package org.smartboot.ioc.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;

/**
 * 模拟JDK7的AIO处理方式
 * @author 三刀
 * @version V1.0 , 2018/5/24
 */
class AsynchronousSocketChannel {
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;
    private CompletionHandler<Integer, Object> readCompletionHandler;
    private CompletionHandler<Integer, Object> writeCompletionHandler;
    private Object readAttachment;
    private Object writeAttachment;
    private SelectionKey selectionKey;
    private boolean writing;
    private boolean reading;

    private SocketChannel channel;

    public AsynchronousSocketChannel(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.channel = (SocketChannel) selectionKey.channel();
    }

    public final <A> void read(ByteBuffer dst,
                               A attachment,
                               CompletionHandler<Integer, ? super A> handler) {
        if (reading) {
            throw new RuntimeException("reading");
        }
        reading = true;
        this.readBuffer = dst;
        this.readAttachment = attachment;
        this.readCompletionHandler = (CompletionHandler<Integer, Object>) handler;
        interestOps(SelectionKey.OP_READ);
    }

    public final <A> void write(ByteBuffer src,
                                A attachment,
                                CompletionHandler<Integer, ? super A> handler) {
        if (writing) {
            throw new RuntimeException("writing");
        }
        writing = true;
        this.writeBuffer = src;
        this.writeAttachment = attachment;
        this.writeCompletionHandler = (CompletionHandler<Integer, Object>) handler;
        interestOps(SelectionKey.OP_WRITE);
    }

    public void close() throws IOException {
        channel.close();
        selectionKey.cancel();
    }

    public Future<Integer> read(ByteBuffer readBuffer) {
        throw new UnsupportedOperationException();
    }

    public void doRead() {
        try {
            if (!readBuffer.hasRemaining()) {
                throw new RuntimeException("full");
            }
            int readSize = channel.read(readBuffer);
            reading = false;
            readCompletionHandler.completed(readSize, readAttachment);
        } catch (IOException e) {
            readCompletionHandler.failed(e, readAttachment);
        }
    }

    public void doWrite() {
        try {
            int writeSize = channel.write(writeBuffer);
            writing = false;
            writeCompletionHandler.completed(writeSize, writeAttachment);
        } catch (IOException e) {
            writeCompletionHandler.failed(e, writeAttachment);
        }
    }

    public boolean isOpen() {
        return channel.isOpen();
    }

    public synchronized void interestOps(int opt) {
        selectionKey.interestOps(selectionKey.interestOps() | opt);
        selectionKey.selector().wakeup();
    }

    public synchronized void removeOps(int opt) {
        selectionKey.interestOps(selectionKey.interestOps() & ~opt);
        selectionKey.selector().wakeup();
    }

}
