package com.yjh.test.thread;

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
    static class SleepBlocked implements Runnable {
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted from Sleeping");
            }
            System.out.println("Exiting from Sleeping");
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
                    System.out.println("It's interrupted.");
                else
                    throw new RuntimeException(e);
            }
            System.out.println("Exiting from io blocked.");
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
            System.out.println("Try to call f()");
            f();
            System.out.println("Exiting from synchronized blocked.");
        }
    }

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void test(Runnable r) throws InterruptedException {
        Future<?> f1 = executorService.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);

        System.out.println("Interrupting " + r.getClass().getName());

        f1.cancel(true); //send true to interrupt if running

        System.out.println("Interrupting " + r.getClass().getName());
    }

    static class NIOBlocked implements Runnable {
        private SocketChannel socketChannel;

        public NIOBlocked(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void run() {
            try {
                System.out.println("try read by nio.");
                socketChannel.read(ByteBuffer.allocate(1));
            } catch (IOException e) {
                System.out.println(e);
            }
            System.out.println("Exiting from nio.");
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

        TimeUnit.SECONDS.sleep(1); //保证运行到read语句
        f1.cancel(true);

        TimeUnit.SECONDS.sleep(1); //看的更清楚一些
        sc2.close();
    }

    static class BlockMutex {
        private ReentrantLock lock = new ReentrantLock();

        public BlockMutex() {
            lock.lock();
        }

        public void f() {
            try {
//                lock.lockInterruptibly(); //请求获取锁，产生一个阻塞
                int count = 0;
                for(int i = 0; i < 1000000000; i++) {
                    if(Thread.interrupted()) {
                        System.out.println("interrupt from compute");
                        break;
                    }
                    count += (Math.PI + Math.E) / i;
                }

                System.out.println("lock acquired.");
            } catch (Exception e) {
                System.out.println("Interrupted from lock acquisition.");
            }
            System.out.println("Exiting from Thread2.");
        }
    }

    static class BM2 implements Runnable {
        private BlockMutex blockMutex = new BlockMutex(); //获得锁

        public void run() {
            System.out.println("block in f()");
            blockMutex.f();
            System.out.println("broken out of block call");
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
        System.out.println("After call interrupt");
    }
}
