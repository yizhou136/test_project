package com.zy.nut.web.test.thread;


import java.util.concurrent.TimeUnit;

/**
 * Created by zhougb on 2017/4/13.
 */
public class NetWorkChecker extends Thread{
    private final static long MaxEscapeMs = 3000;//3s
    private volatile long  startReceiveRenderFrameMs = -1;
    private FFmpegPlayer ffmpegPlayer;
    private Bitmap lastBitMap;
    private boolean needToStop;
    private boolean isPaused;

    public NetWorkChecker(FFmpegPlayer fFmpegPlayer){
        super("NetWorkCheckerThread");
        this.ffmpegPlayer = fFmpegPlayer;
    }

    public void startReceiveRenderFrameMs(){//run as ffmplayer
        long tmpStartReceiveRenderFrameMs = startReceiveRenderFrameMs;
        if (tmpStartReceiveRenderFrameMs == -1)
            return;

        boolean needNotify = tmpStartReceiveRenderFrameMs == 0;
        startReceiveRenderFrameMs = System.currentTimeMillis();
        if (needNotify) {
            synchronized (this) {
                System.out.println("notify NetWorkCheckerThread");
                this.notify();
            }
        }
    }

    public void checkReceivedRenderFrame(FFmpegPlayer.RenderedFrame renderedFrame){//run as ffmplayer
        if (startReceiveRenderFrameMs == -1){
            if (renderedFrame.bitmap != null){
                initReceiveRenderFrameMs();
            }
        }else {
            if (renderedFrame.bitmap == null){//超时了
                initReceiveRenderFrameMs();
                doChangeRenderFrame(renderedFrame);
            }else {
                //startReceiveRenderFrameMs = System.currentTimeMillis();
            }
        }
    }

    public void doChangeRenderFrame(FFmpegPlayer.RenderedFrame renderedFrame){
        if (lastBitMap != null){
            System.out.println("NetWorkChecker" +"doChangeRenderFrame: renderedFrame:"+renderedFrame);
            renderedFrame.bitmap = new Bitmap(-1);
            lastBitMap = null;
        }
    }

    public void initReceiveRenderFrameMs(){
        startReceiveRenderFrameMs = 0;
    }

    public void doPause(){
        isPaused = true;
    }

    public void doResume(){
        isPaused = false;
        synchronized (this){
            startReceiveRenderFrameMs = System.currentTimeMillis();
            notify();
        }
    }

    public void destory(){
        needToStop = true;
        synchronized (this){
            notify();
        }
    }

    @Override
    public void run() {
        while (!needToStop) {
            try {
                long tmpStartReceiveRenderFrameMs = startReceiveRenderFrameMs;
                long now = System.currentTimeMillis();
                long escapeMs = now - tmpStartReceiveRenderFrameMs;
                System.out.println("NetWorkChecker" +" run  tmpStartReceiveRenderFrameMs:"+
                        tmpStartReceiveRenderFrameMs+" now:"+now+" escape:"+escapeMs);
                if (!isPaused && tmpStartReceiveRenderFrameMs > 0) {
                    if (escapeMs >= MaxEscapeMs){//超时了
                        lastBitMap = ffmpegPlayer.getRenderFrameBitmap();
                        synchronized (this) {
                            ffmpegPlayer.renderFrameInterrupt();
                            wait();
                        }
                    }else {
                        synchronized (this) {
                            wait(MaxEscapeMs - escapeMs);
                        }
                    }
                }else{
                    synchronized (this) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("xxxxxxxxxxxxxxxx destory");
    }
}