package com.yjh.test.example.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A simple example of thread cooperation
 *
 * Created by yjh on 2015/9/16.
 */
public class ThreadCooperation {
    private static Logger logger = LogManager.getLogger();

    static class Car {
        private boolean waxed = false;

        public synchronized void waxOn() {
            waxed = true;
            notifyAll(); //Maybe there are different task waiting for get lock, so, use notifyAll().
        }

        public synchronized void waxOff() {
            waxed = false;
            notifyAll();
        }

        public synchronized void waitingForBuffed() throws InterruptedException {
            while(waxed) {
                wait();
            }
            logger.info("check was the car buffed.");
        }

        public synchronized void waitingForWaxed() throws InterruptedException {
            while(!waxed) {
                wait();
            }
            logger.info("check is the car waked");
        }
    }

    static class WaxOn implements Runnable {
        private Car car;

        public WaxOn(Car car) {
            this.car = car;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    car.waitingForBuffed();
                    logger.info("Buffed, wax it on.");
                    TimeUnit.SECONDS.sleep(5);
                    car.waxOn();
                    logger.info("Waxed it, notifyAll other thread.");
                }
            } catch (InterruptedException e) {
                logger.error("WaxOn runnable is interrupted.");
            }
        }
    }

    static class WaxOff implements Runnable {
        private Car car;

        public WaxOff(Car car) {
            this.car = car;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    car.waitingForWaxed();
                    logger.info("Waxed, buff it off.");
                    TimeUnit.SECONDS.sleep(5);
                    car.waxOff();
                    logger.info("Buffed it off, notify other threads.");
                }
            } catch (InterruptedException e) {
                logger.error("WaxOff runnable is interrupted.");
            }
        }
    }

    @Test
    public void test() {
        try {
            Car car = new Car();
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new WaxOff(car));
            executorService.execute(new WaxOn(car));
            TimeUnit.SECONDS.sleep(20);
            executorService.shutdownNow();
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
