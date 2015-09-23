package com.yjh.test.example.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * show difference of notify and notifyAll
 *
 * Created by yjh on 15-9-21.
 */
public class NotifyAndNotifyAll {
    private static Logger logger = LogManager.getLogger();

    static class Blocker {
        public synchronized void waitCalling() {
            try {
                while(!Thread.interrupted()) {
                    wait();
                    logger.debug(Thread.currentThread() + " ");
                }
            } catch (InterruptedException e) {
                logger.error(Thread.currentThread() + " interrupt exception");
            }
        }

        public synchronized void prod() {
            notify();
        }

        public synchronized void prodAll() {
            notifyAll();
        }

    }

    static class Task1 implements Runnable {
        static Blocker blocker = new Blocker();

        @Override
        public void run() {
            blocker.waitCalling();
        }

    }

    /**
     * show difference lock of threads
     */
    static class Task2 implements Runnable {
        static Blocker blocker = new Blocker();

        @Override
        public void run() {
            blocker.waitCalling();
        }
    }

    @Test
    public void test() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++) {
            executorService.execute(new Task1());
        }

        executorService.execute(new Task2());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private boolean prod = true;
            @Override
            public void run() {
                if(prod) {
                    logger.debug("task.notify()");
                    Task1.blocker.prod();
                    prod = false;
                } else {
                    logger.debug("task.notifyAll()");
                    Task1.blocker.prodAll();
                    prod = true;
                }
            }
        }, 400, 400);

        TimeUnit.SECONDS.sleep(5);
        timer.cancel();

        logger.debug("\n Timer canceled.");

        logger.debug("Task2.blocker.prodAll()");
        TimeUnit.MILLISECONDS.sleep(500);
        Task2.blocker.prodAll();

        logger.debug("shutdownNow");
        TimeUnit.MILLISECONDS.sleep(500);
        executorService.shutdownNow();
    }

    /**
     * 一个原始生产者和消费者的例子，厨师和服务员
     */
    class Meal {
        private final int orderNum;

        public Meal(int orderNum) {
            this.orderNum = orderNum;
        }

        @Override
        public String toString() {
            return "Meal{" +
                    "orderNum=" + orderNum +
                    '}';
        }
    }

    class Waitress implements Runnable {
        private final Restaurant restaurant;

        public Waitress(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    synchronized (this) {
                        while(restaurant.meal == null) {
                            wait();
                        }
                    }
                    //got a meal
                    logger.debug("got meal " + restaurant.meal);
                    synchronized (restaurant.cook) {
                        restaurant.meal = null;
                        restaurant.cook.notifyAll();
                    }
                    synchronized (restaurant.busBoys) {
                        restaurant.needClean = true;
                        restaurant.busBoys.notifyAll();
                    }
                    synchronized (this) {
                        while(restaurant.needClean) {
                            wait(); //waiting for cleaning
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    class Cook implements Runnable {
        private final Restaurant restaurant;

        public Cook(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal != null) {
                            wait();
                        }
                    }

                    //check count
                    if(++restaurant.count == 10) {
                        logger.debug("out of food, stop");
                        restaurant.executorService.shutdownNow();
                    }

                    synchronized (restaurant.waitress) {
                        restaurant.meal = new Meal(restaurant.count);
                        logger.debug("cooking " + restaurant.meal);
                        TimeUnit.MILLISECONDS.sleep(500); //here, throw a InterruptedException when shutdownNow
                        restaurant.waitress.notifyAll();
                    }

                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }

    }

    class BusBoys implements Runnable {
        private final Restaurant restaurant;

        public BusBoys(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    synchronized (this) {
                        while(!restaurant.needClean) {
                            wait();
                        }
                    }
                    logger.debug("clean up");
                    synchronized (restaurant.waitress) {
                        restaurant.needClean = false;
                        restaurant.waitress.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    class Restaurant {
        final Cook cook = new Cook(this);
        int count;
        Meal meal;
        boolean needClean;
        final Waitress waitress = new Waitress(this);
        final BusBoys busBoys = new BusBoys(this);
        ExecutorService executorService = Executors.newCachedThreadPool();
    }

    @Test
    public void testConsumerAndProducer() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.executorService.execute(restaurant.cook);
        restaurant.executorService.execute(restaurant.waitress);
        restaurant.executorService.execute(restaurant.busBoys);

        TimeUnit.SECONDS.sleep(50);
    }

}
