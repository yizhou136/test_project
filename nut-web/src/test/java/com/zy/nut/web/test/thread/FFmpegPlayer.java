package com.zy.nut.web.test.thread;

import java.util.Random;

/**
 * @author by zy.
 */
public class FFmpegPlayer {

    private NetWorkChecker netWorkChecker;
    private RenderedFrame mRenderedFrame = new RenderedFrame();

    public FFmpegPlayer(){
        netWorkChecker = new NetWorkChecker(this);
        netWorkChecker.start();
    }

    public NetWorkChecker getNetWorkChecker() {
        return netWorkChecker;
    }

    public void setNetWorkChecker(NetWorkChecker netWorkChecker) {
        this.netWorkChecker = netWorkChecker;
    }

    static class RenderedFrame {
        public Bitmap bitmap;
        public int height;
        public int width;
        public int rotate;
        @Override
        public String toString() {
            return String.format("readeredFrame width:%d height:%d bitmap:%s rotate:%d",
                    width, height, bitmap, rotate);
        }
    }


    public void setDataSource(String url) {
    }

    RenderedFrame renderFrame() throws InterruptedException {
        int i = random.nextInt(5000);
        i = Math.max(i, 100);
        System.out.println("start renderFrame waitTime:"+i+" now:"+System.currentTimeMillis());
        netWorkChecker.startReceiveRenderFrameMs();
        mRenderedFrame.bitmap = this.renderFrameNative(i);
        netWorkChecker.checkReceivedRenderFrame(mRenderedFrame);
        System.out.println("end renderFrame mRenderedFrame:"+mRenderedFrame);
        return this.mRenderedFrame;
    }

    public Bitmap getRenderFrameBitmap(){
        return mRenderedFrame.bitmap;
    }

    public void renderFrameInterrupt(){
        synchronized (this) {
            this.notify();
        }
    }

    private Random random = new Random(System.currentTimeMillis());
    private Bitmap renderFrameNative(int i){
        try {
            synchronized (this) {
                this.wait(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (i > 3000){
            return null;
        }
        return new Bitmap(i);
    }


    private static RenderedFrame renderedFrame;
    public static void main(String argv[]) throws InterruptedException {
        FFmpegPlayer fFmpegPlayer = new FFmpegPlayer();
        int i=0;
        while (true){
            try {
                renderedFrame = fFmpegPlayer.renderFrame();
                //System.out.println(renderedFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (i == 3){
                System.out.println("getNetWorkChecker().destory()");
                fFmpegPlayer.getNetWorkChecker().destory();
                break;
            }

            i++;
        }

        Thread.sleep(1000);
        System.out.println("xxxxxxxxxxxxxxx restart");

        fFmpegPlayer.setDataSource("");

        while (true) {
            try {
                renderedFrame = fFmpegPlayer.renderFrame();
                //System.out.println(renderedFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
