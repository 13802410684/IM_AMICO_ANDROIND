package org.smartboot.ioc.transport;

interface CompletionHandler<V, A> {

    /**
     * Invoked when an operation has completed.
     *
     * @param result     The result of the I/O operation.
     * @param attachment The object attached to the I/O operation when it was initiated.
     */
    void completed(V result, A attachment);

    /**
     * Invoked when an operation fails.
     *
     * @param exc        The exception to indicate why the I/O operation failed
     * @param attachment The object attached to the I/O operation when it was initiated.
     */
    void failed(Throwable exc, A attachment);
}