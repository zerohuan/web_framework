package com.yjh.test.example.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by yjh on 2015/9/11.
 */
public class ThreadSafeTest {
    private static Logger logger = LogManager.getLogger();

    static class TaskWithResult implements Callable<String> {
        protected static int i = 1;
        final protected int id = i++;

        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(10);
            return "[task#" + id  + "] after 10 seconds later complete.";
        }
    }

    /**
     * 有返回值的任务
     */
    public void test() {
        final ExecutorService executor = Executors.newCachedThreadPool();

        final ArrayList<Future<String>> results = new ArrayList<Future<String>>();

        for(int i = 0; i < 10; i++) {
            results.add(executor.submit(new TaskWithResult()));
        }

        new Thread(new Runnable() {
            public void run() {
                for(Future<String> fs : results) {
                    try {
                        logger.debug(fs.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } finally {
                        executor.shutdown();
                    }
                }
            }
        }).start();
    }

    class SleepTest extends TaskWithResult {
        private int count = 10;
        public String call() throws Exception {
            while(count-- > 0) {
                logger.debug(status());
                TimeUnit.MILLISECONDS.sleep(100);
            }
            return "#" + id + " end.";
        }

        public String status() {
            return "count: " + count;
        }
    }

    /**
     * 休眠
     */
    public void sleep() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++) {
            executor.submit(new SleepTest());
        }
        executor.shutdown();
    }

    static class SimplePriorities implements Runnable {
        private int countDown = 5;
        private volatile double d; //去掉优化，延长计算时间
        private int priority;

        public SimplePriorities(int priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return Thread.currentThread() + " " + countDown;
        }

        public void run() {
            Thread.currentThread().setPriority(priority);
            while(true) {
                for(int i = 0; i < 10000; i++) {
                    d += (Math.E + Math.PI) / (double)i;
                    if(i % 1000 == 0) {
                        Thread.yield();
                    }
                }
                logger.debug(this);
                if(countDown-- == 0) return;
            }
        }
    }

    public void testPriority() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++) {
            executorService.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        executorService.execute(new SimplePriorities(Thread.MAX_PRIORITY));
    }

    class SelfSetDaemon implements Runnable {
        public void run() {
            Thread.currentThread().setDaemon(true);
        }
    }

    public void testSelfSetDaemon() {
        Thread thread = new Thread(new SelfSetDaemon());
        thread.start();
        logger.debug(thread.isDaemon());
    }

    class SelfManagerRunnbale implements Runnable {
        private Thread thread = new Thread(this);

        public void run() {
            //...
        }
    }

    class Sleeper extends Thread {
        private int duration;

        public Sleeper(String name, int duration) {
            super(name);
            this.duration = duration;
            start();
        }

        @Override
        public void run() {
            try {
                sleep(duration);
            } catch (InterruptedException e) {
                logger.debug(getName() + "was interrupted. isInterrupted():" + isInterrupted());
                return;
            }
            logger.debug(getName() + "was awakened.");
        }
    }

    class Joiner extends  Thread {
        private Sleeper sleeper;

        public Joiner(String name, Sleeper sleeper) {
            super(name);
            this.sleeper = sleeper;
            start();
        }

        @Override
        public void run() {
            try {
                sleeper.join();
            } catch (InterruptedException e) {
                logger.debug("interrupted");
            }
            logger.debug(getName() + " complete.");
        }
    }

    public void testJoin() {
        Sleeper
                sleeper = new Sleeper("sleepy", 1500),
                grumpy = new Sleeper("grumpy", 1500);

        Joiner
                dopey = new Joiner("dopey", sleeper),
                doc = new Joiner("doc", grumpy);

        grumpy.interrupt();
    }

    /**
     * 捕获异常
     */
    class RuntimeExceptionTask implements Runnable {
        public void run() {
            Thread t = Thread.currentThread();
            logger.debug("getUncaughtExceptionHandler: " + t.getUncaughtExceptionHandler());
            throw new RuntimeException("runtimeException from another thread.");
        }
    }

    class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            logger.debug("Exception: " + e.getMessage());
        }
    }

    class UncaughtHandlerThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread t = new Thread();
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            return t;
        }
    }

    public void testUncaughtException() {
        ExecutorService executorService = Executors.newCachedThreadPool(new UncaughtHandlerThreadFactory());
        executorService.execute(new RuntimeExceptionTask());
        executorService.shutdown();
    }

    /**
     * 在其他对象上同步
     */
    static class Sync {
        public void g() throws Exception {};
        public void f() throws Exception {};
    }

    static class SyncA extends Sync {
        private final SyncB syncB;

        public SyncA(SyncB syncB) {
            this.syncB = syncB;
        }

        public void g() throws Exception{
            synchronized (syncB) {
                for(int i = 0; i < 5; i++) {
                    logger.debug("g_a" + " " + i);
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            }
        }

        public synchronized void f() throws Exception {
            for(int i = 0; i < 5; i++) {
                logger.debug("f_a" + " " + i);
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }
    }

    static class SyncB extends Sync {
        public synchronized void g() throws Exception {
            for(int i = 0; i < 5; i++) {
                logger.debug("g_b" + " " + i);
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }

    }

    static abstract class SyncTask implements Runnable {
        protected Sync sync;

        public SyncTask(Sync sync) {
            this.sync = sync;
        }

        public void run() {
            try {
                test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public abstract void test() throws Exception;
    }

    static class SyncTaskG extends SyncTask {
        public SyncTaskG(Sync sync) {
            super(sync);
        }

        @Override
        public void test() throws Exception {
            sync.g();
        }
    }

    static class SyncTaskF extends SyncTask {
        public SyncTaskF(Sync sync) {
            super(sync);
        }

        @Override
        public void test() throws Exception {
            sync.f();
        }
    }

    public static void main(String[] args) {
        SyncB syncB = new SyncB();
        SyncA syncA = new SyncA(syncB);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new SyncTaskG(syncA));
        executorService.execute(new SyncTaskG(syncB));
        executorService.execute(new SyncTaskF(syncA));
        executorService.execute(new SyncTaskF(syncB));
        executorService.shutdown();


    }
}
