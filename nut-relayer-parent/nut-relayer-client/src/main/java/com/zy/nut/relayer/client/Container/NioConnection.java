package com.zy.nut.relayer.client.Container;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import com.zy.nut.relayer.common.transporter.netty.NettyChannel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhougb on 2016/11/9.
 */
public class NioConnection implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(NioConnection.class);
    private int receiveBufferSize = 6924;
    private int sendBufferSize = 1000;
    private int bufferedSendBufferSize = 200;
    private int soTimeOut = 1000;
    private int connectTimeOut = 3000;
    private int selectTimeOut = 60;
    private int maxReconnectCount = 3;
    private int tryReconnectCount;

    private List<HostPortPair> hostPortes;
    private AtomicInteger hostportIdx = new AtomicInteger();

    private static RelayerCodec codec = new RelayerCodec();
    private ChannelBuffer channelBuffer;

    private SocketChannel socketChannel;
    private Selector selector;
    private ByteBuffer bufferedSendBuffer;
    private Thread selectorThread;
    private boolean toClose;
    private ProccessCommandListener proccessCommandListener;

    private ExecutorService sendByteBufferToSocketWorker = Executors.newFixedThreadPool(1);

    public NioConnection() {
        channelBuffer = ChannelBuffers.buffer(1024);
        /*readByteBuffer = channelBuffer.toByteBuffer();
                //ByteBuffer.allocate(receiveBufferSize);
        readByteBuffer.order(ByteOrder.BIG_ENDIAN);*/
        bufferedSendBuffer = ByteBuffer.allocate(bufferedSendBufferSize);
        bufferedSendBuffer.order(ByteOrder.BIG_ENDIAN);
    }

    public NioConnection(List<HostPortPair> hostPortes) {
        this();
        this.hostPortes = hostPortes;
    }

    public int getHostPortSize() {
        if (hostPortes == null || hostPortes.isEmpty())
            return 0;
        return hostPortes.size();
    }

    public final SocketAddress getNextHostPort() {
        assert !hostPortes.isEmpty();
        SocketAddress socketAddress = null;
        if (!hostPortes.isEmpty()) {
            HostPortPair hostPortPair = hostPortes.get(hostportIdx.incrementAndGet() % hostPortes.size());
            logger.info(String.format("select the host to connected: %s:%d", hostPortPair.host, hostPortPair.port));
            socketAddress = new InetSocketAddress(hostPortPair.host, hostPortPair.port);
        }
        return socketAddress;
    }

    public HostPortPair getCurrentHostPortPair() {
        assert !hostPortes.isEmpty();
        return hostPortes.get(hostportIdx.get() % hostPortes.size());
    }

    public boolean isConnecting(){
        if (socketChannel != null){
            return socketChannel.isConnected();
        }
        return false;
    }

    public void initClient() {
        try {
            if (selector == null) {
                selector = Selector.open();
            }
            if (socketChannel == null) {
                maxReconnectCount = (int) ((float) getHostPortSize() * 1.5);
                maxReconnectCount = maxReconnectCount <= 2 ? 3 : maxReconnectCount;
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.socket().setKeepAlive(true);
                socketChannel.socket().setReuseAddress(true);
                socketChannel.socket().setReceiveBufferSize(receiveBufferSize);
                socketChannel.socket().setSendBufferSize(sendBufferSize);
                //socketChannel.socket().setSoLinger(true, 200);
                socketChannel.socket().setSoTimeout(soTimeOut);
                socketChannel.connect(getNextHostPort());
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                selector = null;
            }
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                socketChannel = null;
            }
        }
    }

    public void doConnection() {
        toClose = false;
        if (selectorThread == null) {
            selectorThread = new Thread(this);
            selectorThread.setDaemon(true);
            selectorThread.start();
        }
    }

    public void close() {
        if (selector == null || socketChannel == null)
            return;
        toClose = true;
        selector.wakeup();
    }

    public void reconnect() {
        if (selector != null) {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                    socketChannel = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            selector.wakeup();
        }
    }

    public void readOnly() {
        if (selector == null || socketChannel == null)
            return;
        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int selectn = 0;
        int connectEscapeTime = 0;
        while (!toClose) {
            try {
                initClient();
                if (selector == null) break;
                boolean hasConnect = socketIsValidate();
                if (hasConnect && bufferedSendBufferIsEmpty()) {
                    selectn = selector.select();
                } else {
                    selectn = selector.select(selectTimeOut);
                    if (hasConnect)
                        flushBufferedSendBuffer();
                    else if (selectn == 0) {
                        connectEscapeTime += selectTimeOut;
                    }
                }
                logger.info("select selectn:" + selectn + " connectTimeOut:" + connectTimeOut + " connectEscapeTime:" + connectEscapeTime);
                if (connectEscapeTime >= connectTimeOut) {
                    throw new ConnectException("made by connectTimeOut:" + connectTimeOut);
                }
                if (toClose) break;
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    it.remove();
                    if (selectionKey.isConnectable()) {
                        logger.info("isConnectable selectn:" + selectn);
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        if (socketChannel.isConnectionPending()) {
                            if (socketChannel.finishConnect()) {
                                logger.info("the client has connected the server");
                                connectEscapeTime = 0;
                                selectionKey.interestOps(SelectionKey.OP_READ);
                                //notifyConnectResult(true);
                            }
                        }
                    } else {
                        if (selectionKey.isReadable()) {
                            logger.info("isReadable   selectn:" + selectn);
                            readBufferedCommandReplyes();
                        }
                        /*if (selectionKey.isWritable()) {
                            logger.debug("isWritable   selectn:"+selectn);
                            writeBufferedCommandes();
                        }*/
                    }
                }
            } catch (ConnectException e) {
                //e.printStackTrace();
                logger.info("connect error and try reconnect:" + (tryReconnectCount + 1));
                if (tryReconnectCount == (maxReconnectCount - 1)) {
                    logger.info("connect error and try reach the maxReconnectCount:" + maxReconnectCount);
                    //return ;
                    toClose = true;
                    //notifyConnectResult(false);
                }
                try {
                    connectEscapeTime = 0;
                    socketChannel.close();
                    socketChannel = null;
                    tryReconnectCount++;
                } catch (IOException ign) {
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("msclientioIOException");
            }
        }
        try {
            if (selector != null) {
                selector.close();
                selector = null;
            }
            if (socketChannel != null) {
                socketChannel.close();
                socketChannel = null;
            }
            selectorThread = null;
        } catch (Exception ign) {
        }
    }

    private boolean socketIsValidate() {
        if (socketChannel == null) return false;

        return socketChannel.isConnected();
    }

    private void readBufferedCommandReplyes() {
        if (!socketIsValidate()) return;
        try {
            int readn = -1;
            while ((readn = channelBuffer.writeBytes(socketChannel, 1024)) < 0) {
                logger.info("readBufferedCommandReplyes error or socket closed:" + readn);
                reconnect();
                return;
            }
            logger.info("readBufferedCommandReplyes start parse readn:" + readn
                    + " readByteBuffer:" + channelBuffer);
            List<Object> out = new ArrayList<Object>();
            while(true) {
                int saveReaderIndex = channelBuffer.readerIndex();
                Object msg;
                try {
                    msg = codec.decode(null, channelBuffer);
                } catch (IOException var12) {
                    throw var12;
                }

                if(msg == Codec.DecodeResult.NEED_MORE_INPUT) {
                    channelBuffer.readerIndex(saveReaderIndex);
                } else {
                    if(saveReaderIndex == channelBuffer.readerIndex()) {
                        throw new IOException("Decode without read data.");
                    }
                    if(msg != null) {
                        out.add(msg);
                    }
                    if(channelBuffer.readable()) {
                        continue;
                    }
                }
                break;
            }
            if (proccessCommandListener != null)
                for (Object obj : out){
                    proccessCommandListener.processCommand(obj);
                }
            logger.info("readBufferedCommandReplyes end parse  channelBuffer:"+channelBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("readBufferedCommandReplyes error:" + e);
            reconnect();
        }
    }

    public boolean sendCommandByteBuffer(final ByteBuffer byteBuffer) {
        int len = byteBuffer.remaining();
        int available = bufferedSendBuffer.capacity() - bufferedSendBuffer.position();
        if (available < len) {
            synchronized (bufferedSendBuffer) {
                available = bufferedSendBuffer.capacity() - bufferedSendBuffer.position();
                if (available < len) {
                    logger.info("sendCommandByteBuffer not enough available < len " + available + " ," + len);
                    sendByteBufferToSocketWorker.execute(new Runnable() {
                        public void run() {
                            int towriten = byteBuffer.limit();
                            boolean result = towriten == writeToSocketChannel(byteBuffer);
                            logger.info("sendCommandByteBuffer by  sendByteBufferToSocketWorker result:" + result);
                        }
                    });
                    return true;
                }
            }
        }
        appendToBufferedSendBuffer(byteBuffer);
        if (selector != null)
            selector.wakeup();
        return true;
    }

    private int writeToSocketChannel(ByteBuffer byteBuffer) {
        int totalWriten = 0;
        try {
            int towriten = byteBuffer.limit();
            int retryCnt = 1;
            int writen = 0;
            while ((writen = socketChannel.write(byteBuffer)) != towriten) {
                towriten -= writen;
                totalWriten += writen;
                retryCnt++;
                if (retryCnt == 4)
                    break;
                Thread.sleep(50 * retryCnt);
                logger.info("socketchannel write not enough writen: "
                        + writen + " towriten:" + towriten + " and retry:" + retryCnt);
                continue;
            }
            totalWriten += writen;
            if (byteBuffer.remaining() > 0) {
                if (bufferedSendBufferIsEmpty())
                    appendToBufferedSendBuffer(byteBuffer.slice());
                else
                    logger.info("writeCommandToBuffer write not enouth and  bufferedSendBuffer Is not Empty writen:" + totalWriten
                            + " towriten:" + byteBuffer.limit() + " remaining:" + byteBuffer.remaining());
            }
            logger.info("writeCommandToBuffer totalWriten:" + totalWriten + " towriten:" + byteBuffer.limit());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("writeCommandToBuffer IOException error:" + e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            logger.error("writeCommandToBuffer NullPointerException maybe do reconnecting server. error:" + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("writeCommandToBuffer InterruptedException error:" + e);
        }
        return totalWriten;
    }

    private boolean bufferedSendBufferIsEmpty() {
        synchronized (bufferedSendBuffer) {
            return bufferedSendBuffer.position() == 0;
        }
    }

    private void appendToBufferedSendBuffer(ByteBuffer byteBuffer) {
        synchronized (bufferedSendBuffer) {
            bufferedSendBuffer.put(byteBuffer);
        }
    }

    private void flushBufferedSendBuffer() {
        boolean hasConnected = socketIsValidate();
        if (!hasConnected) return;
        synchronized (bufferedSendBuffer) {
            if (bufferedSendBuffer.position() > 0) {
                bufferedSendBuffer.flip();
                int writen = writeToSocketChannel(bufferedSendBuffer);
                bufferedSendBuffer.position(writen);
                ByteBuffer leftByteBuffer = bufferedSendBuffer.slice();
                bufferedSendBuffer.clear();
                bufferedSendBuffer.put(leftByteBuffer);
                logger.info("flushBufferedSendBuffer has writen:" + writen + " leftByteBuffer:" + leftByteBuffer);
            }
        }
    }

    //for command
    public List<HostPortPair> getHostPortes() {
        return hostPortes;
    }

    public void setHostPortes(List<HostPortPair> hostPortes) {
        this.hostPortes = hostPortes;
    }

    public static final class HostPortPair {
        public String host;
        public int port;

        public HostPortPair(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("host:").append(host).append(" port:").append(port);
            return stringBuffer.toString();
        }
    }

    public ProccessCommandListener getProccessCommandListener() {
        return proccessCommandListener;
    }

    public void setProccessCommandListener(ProccessCommandListener proccessCommandListener) {
        this.proccessCommandListener = proccessCommandListener;
    }

    public static interface ProccessCommandListener{
        void processCommand(Object obj);
    }
}
