package com.zqw.mobile.grainfull.app.utils;


import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class ThreadUtil {

    final private ExecutorService threads = Executors.newFixedThreadPool(10);
    final private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    final private Map<String, Queue<Runnable>> runableMap = new ConcurrentHashMap<>();

    private static volatile ThreadUtil threadUtil;

    public static ThreadUtil getInstance() {
        if (threadUtil == null) {
            synchronized (ThreadUtil.class) {
                if (threadUtil == null) {
                    threadUtil = new ThreadUtil();
                }
            }
        }
        return threadUtil;
    }

    public void removeRunable(String key) {
        runableMap.remove(key);
    }

    /**
     * 添加单线程
     */
    public void addRunableToSingleThead(String key, Runnable runnable) {
        Queue<Runnable> runnableQueue = runableMap.get(key);
        if (runnableQueue == null) {
            runnableQueue = new ConcurrentLinkedQueue<>();
            runableMap.put(key, runnableQueue);
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                    Runnable r;
                    final Queue<Runnable> runnableQueue1 = runableMap.get(key);
                    if (runnableQueue1 == null) break;
                    while ((r = runnableQueue1.poll()) != null) {
                        r.run();
                    }
                }
            }).start();
            Timber.i("创建线程数%s", runableMap.size());
        }
        runnableQueue.offer(runnable);
    }

    public void runOnChildThread(Runnable runnable) {
        try {
            threads.submit(runnable).get();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public void schedule(Runnable runnable, long period) {
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 100, period, TimeUnit.MILLISECONDS);
    }
}
