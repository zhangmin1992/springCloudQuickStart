package com.zm.provider.util;


/**
 * 线程工厂管理
 * 
 * @author：kai.yang
 * @since：2016年7月28日 下午6:36:45
 * @version:
 */
public class ThreadFactory {

    /**
     * 单个守护线程开启,未池化线程
     * 
     * @param runnable
     */
    public static void startNewDaemonThread(Runnable runnable) {
        Thread td = new Thread(runnable);
        td.setDaemon(true);
        td.start();
    }

}
