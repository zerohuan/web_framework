package com.yjh.test.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yjh on 2015/9/13.
 */
public class InterruptTest {
    private static Logger logger = LogManager.getLogger();

    static class SleepBlocked implements Runnable {
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                logger.debug("Interrupted from Sleeping");
            }
            logger.debug("Exiting from Sleeping");
        }
    }

    static class BIOBlocked implements Runnable {
        private InputStream in;

        public BIOBlocked(InputStream in) {
            this.in = in;
        }

        public void run() {
            try {
                in.read();
            } catch (IOException e) {
                if(Thread.currentThread().isInterrupted())
                    logger.debug("It's interrupted.");
                else
                    throw new RuntimeException(e);
            }
            logger.debug("Exiting from io blocked.");
        }
    }

    static class SynchronizedBlocked implements Runnable {
        public SynchronizedBlocked() {
            new Thread() {
                @Override
                public void run() {
                    f();
                }
            }.start();
        }

        public synchronized void f() {
            while(true) {
                Thread.yield();
            }
        }

        public void run() {
            logger.debug("Try to call f()");
            f();
            logger.debug("Exiting from synchronized blocked.");
        }
    }

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void test(Runnable r) throws InterruptedException {
        Future<?> f1 = executorService.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);

        logger.debug("Interrupting " + r.getClass().getName());

        f1.cancel(true); //send true to interrupt if running

        logger.debug("Interrupting " + r.getClass().getName());
    }

    static class NIOBlocked implements Runnable {
        private SocketChannel socketChannel;

        public NIOBlocked(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void run() {
            try {
                logger.debug("try read by nio.");
                socketChannel.read(ByteBuffer.allocate(1));
            } catch (IOException e) {
                logger.debug(e);
            }
            logger.debug("Exiting from nio.");
        }
    }

    public static void testNIO() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        SocketChannel sc1 = SocketChannel.open(address);
        SocketChannel sc2 = SocketChannel.open(address);

        Future<?> f1 = executorService.submit(new NIOBlocked(sc1));
        executorService.submit(new NIOBlocked(sc2));

        executorService.shutdown();

        TimeUnit.SECONDS.sleep(1); //��֤���е�read���
        f1.cancel(true);

        TimeUnit.SECONDS.sleep(1); //���ĸ����һЩ
        sc2.close();
    }

    static class BlockMutex {
        private ReentrantLock lock = new ReentrantLock();

        public BlockMutex() {
            lock.lock();
        }

        public void f() {
            try {
//                lock.lockInterruptibly(); //�����ȡ��������һ������
                int count = 0;
                for(int i = 0; i < 1000000000; i++) {
                    if(Thread.interrupted()) {
                        logger.debug("interrupt from compute");
                        break;
                    }
                    count += (Math.PI + Math.E) / i;
                }

                logger.debug("lock acquired.");
            } catch (Exception e) {
                logger.debug("Interrupted from lock acquisition.");
            }
            logger.debug("Exiting from Thread2.");
        }
    }

    static class BM2 implements Runnable {
        private BlockMutex blockMutex = new BlockMutex(); //�����

        public void run() {
            logger.debug("block in f()");
            blockMutex.f();
            logger.debug("broken out of block call");
        }
    }



    public static void main(String[] args) throws Exception {
//        test(new SleepBlocked());
//        test(new BIOBlocked(System.in));
//        test(new SynchronizedBlocked());

//        testNIO();

        Thread t = new Thread(new BM2());
        t.start();

        TimeUnit.MILLISECONDS.sleep(200);

        t.interrupt();
        logger.debug("After call interrupt");
    }
}
