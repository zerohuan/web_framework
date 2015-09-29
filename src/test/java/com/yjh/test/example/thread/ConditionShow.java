package com.yjh.test.example.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * a example showing Condition's work
 *
 * Created by yjh on 15-9-22.
 */
public class ConditionShow {
    private static Logger logger = LogManager.getLogger();

    class Car {
        private boolean waxed = false;
        private ReentrantLock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        public void wax() {
            lock.lock();
            try {
                waxed = true;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void buff() {
            lock.lock();
            try {
                waxed = false;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void waitingForWaxed() throws InterruptedException {
            lock.lock();
            try {
                while(!waxed)
                    condition.await();
            } finally {
                lock.unlock();
            }
        }

        public void waitingForBuffed() throws InterruptedException {
            lock.lock();
            try {
                while (waxed) {
                    condition.await();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    class WaxOn implements Runnable {
        private Car car;

        public WaxOn(Car car) {
            this.car = car;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    logger.debug("Wax on");
                    TimeUnit.MILLISECONDS.sleep(200);
                    car.wax();

                    car.waitingForBuffed();
                    logger.debug("Buffed, to wax.");
                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    class WaxBuff implements Runnable {
        private Car car;

        public WaxBuff(Car car) {
            this.car = car;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    car.waitingForWaxed();

                    logger.debug("Waxed, to buff.");
                    TimeUnit.MILLISECONDS.sleep(200);
                    car.buff();
                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    @Test
    public void test() throws InterruptedException {
        Car car = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOn(car));
        executorService.execute(new WaxBuff(car));

        TimeUnit.SECONDS.sleep(10);

        executorService.shutdownNow();

    }

    class Tuple<T> {
        public void get(T t) {

        }
    }

    static class P {

    }

    static class P1 extends P {

    }

    public static void main(String[] args) throws Exception {

    }


}
