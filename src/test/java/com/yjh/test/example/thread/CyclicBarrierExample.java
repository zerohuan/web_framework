package com.yjh.test.example.thread;

import com.yjh.test.TestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 *
 * Created by yjh on 15-9-23.
 */
public class CyclicBarrierExample {
    private static Logger logger = LogManager.getLogger();

    static class House implements Runnable {
        private static Random random = new Random(47);
        private TestUtil.IdHolder idHolder = new TestUtil.IdHolder();
        private CyclicBarrier cyclicBarrier;
        private int strides;

        public House(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    synchronized (this) {
                        strides += random.nextInt(3);
                    }
                    cyclicBarrier.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                logger.error(e);
            }
        }

        public synchronized int getStrides() {
            return strides;
        }

        public TestUtil.IdHolder getIdHolder() {
            return idHolder;
        }

        @Override
        public String toString() {
            return "rouse " + idHolder + " ";
        }

        public String tracks() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < getStrides(); i++) {
                stringBuilder.append("*");
            }
            stringBuilder.append(idHolder.getId());

            return stringBuilder.toString();
        }
    }

    static class HouseRace {
        private static int FINISH_LINE = 50;
        private List<House> houses = new ArrayList<>();
        private CyclicBarrier cyclicBarrier;
        private ExecutorService executorService = Executors.newCachedThreadPool();

        public HouseRace(int nRouses, final int pause) {
            cyclicBarrier = new CyclicBarrier(nRouses, ()->{
                StringBuilder stringBuilder = new StringBuilder();

                for(int i = 0; i < FINISH_LINE; i++) {
                    stringBuilder.append("=");
                }
                logger.debug(stringBuilder);
                houses.stream().forEach(house->logger.debug(house.tracks()));
                for(House house : houses) {
                    if(house.getStrides() >= FINISH_LINE) {
                        logger.debug(house.getIdHolder() + " is winner!");
                        executorService.shutdownNow();
                        return; //here can not use stream api, because of this "return"
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    logger.error("shutdown " + e);
                }

            });

            for(int i = 0; i < nRouses; i++) {
                House house = new House(cyclicBarrier);
                houses.add(house);
                executorService.execute(house);
            }

        }

    }

    public static void main(String[] args) {
        new HouseRace(10, 500);
    }

}
