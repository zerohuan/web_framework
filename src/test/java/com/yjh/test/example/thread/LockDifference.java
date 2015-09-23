package com.yjh.test.example.thread;

/**
 * Created by yjh on 15-9-22.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * show difference between lock, tryLock and lockInterruptibly
 */
public class LockDifference {
    private static Logger logger = LogManager.getLogger();

    ReentrantLock lock = new ReentrantLock();

    class LockTest implements Runnable {
        @Override
        public void run() {
            try {
                lock.tryLock();

                logger.debug(Thread.currentThread() + " running");
                TimeUnit.SECONDS.sleep(4);
                logger.debug(Thread.currentThread() + " finished");
            } catch (InterruptedException e) {
                logger.debug(e);
            } finally {
                if(Thread.holdsLock(lock)) {
                    logger.debug("release lock");
                    lock.unlock();
                }
            }
        }
    }

    @Test
    public void test() throws Exception {
        Thread thread1 = new Thread(new LockTest());
        Thread thread2 = new Thread(new LockTest());

        thread1.start();
        thread2.start();

        thread2.interrupt();

        TimeUnit.SECONDS.sleep(9);

    }
}
