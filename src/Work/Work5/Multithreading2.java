package Work.Work5;


import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Multithreading2 {
    public static void main(String[] args) throws InterruptedException {
//        simpleReentrantLockExample();
//        reentrantReadWriteLockExample();
//        simpleCountDownLatchExample();
//        anotherCountDownLatchExample();
//        semaphoreExample();
//        simpleCyclicBarrierExample();
//        barrierWithRunnable();
//        concurrentCollectionsExample();

    }

    private static void concurrentCollectionsExample() {
        List<String> list = new ArrayList<>();
        List<String> syncListOld = new Vector<>();
        List<String> syncList = new CopyOnWriteArrayList<>();
        List<String> syncListFast = Collections.synchronizedList(list);

        Set<String> set = new HashSet<>();
        Set<String> syncSet = new CopyOnWriteArraySet<>();
        Set<String> syncSetFast = Collections.synchronizedSet(set);


        Map<String, String> map = new HashMap<>();
        Map<String, String> syncMapOld = new Hashtable<>();
        Map<String, String> syncMap = new ConcurrentHashMap<>();
        Map<String, String> syncMapFast = Collections.synchronizedMap(map);
    }

    private static void barrierWithRunnable() {
        var carsCount = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(carsCount, () -> {
            System.out.println("CHECKPOINT");
        });
        for (int i = 0; i < carsCount; i++) {
            var j = i + 1;

            new Thread(() -> {
                try {
                    System.out.printf("Car #%d preparing\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    System.out.printf("Car #%d on the start line\n", j);
                    cyclicBarrier.await();
                    System.out.printf("Car #%d riding\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    System.out.printf("Car #%d finished\n", j);
                    cyclicBarrier.await();
                    System.out.printf("Car #%d rides to garage\n", j);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void simpleCyclicBarrierExample() {
        var carsCount = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(carsCount);
        for (int i = 0; i < carsCount; i++) {
            var j = i + 1;

            new Thread(() -> {
                try {
                    System.out.printf("Car #%d preparing\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    System.out.printf("Car #%d on the start line\n", j);
                    cyclicBarrier.await();
                    System.out.printf("Car #%d riding\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    System.out.printf("Car #%d finished\n", j);
                    cyclicBarrier.await();
                    System.out.printf("Car #%d rides to garage\n", j);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void semaphoreExample() {
        //        Semaphore semaphore = new Semaphore(3);
        Semaphore semaphore = new Semaphore(3, true);
        for (int i = 0; i < 20; i++) {
            var j = i + 1;
            new Thread(() -> {
                try {
                    System.out.printf("Car #%d before the bridge\n", j);
                    semaphore.acquire();
                    System.out.printf("Car #%d riding for a long time on the bridge\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    System.out.printf("Car #%d finished the bridge\n", j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }

    private static void anotherCountDownLatchExample() throws InterruptedException {
        var threadCount = 10;
        CountDownLatch cdl = new CountDownLatch(threadCount + 1);
        System.out.println("Begin");

        for (int i = 0; i < threadCount; i++) {
            var j = i;

            new Thread(() -> {
                try {
                    System.out.printf("Thread #%d started\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    cdl.countDown();
                    cdl.await();
                    System.out.printf("Thread #%d done\n", j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }


        while (cdl.getCount() > 1) {
            Thread.sleep(50);
        }
        System.out.println("Finishing");
        cdl.countDown();
        Thread.sleep(50);
        System.out.println("ALL JOB DONE");
    }

    private static void simpleCountDownLatchExample() {
        var threadCount = 10;
        CountDownLatch cdl = new CountDownLatch(threadCount);
//        CountDownLatch cdl = new CountDownLatch(threadCount + 1);
//        CountDownLatch cdl = new CountDownLatch(threadCount / 2);
        System.out.println("Begin");

        for (int i = 0; i < threadCount; i++) {
            var j = i;

            new Thread(() -> {
                try {
                    System.out.printf("Thread #%d started\n", j);
                    Thread.sleep((long) (350 + 850 * Math.random()));
                    System.out.printf("Thread #%d done\n", j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            }).start();
        }

        try {
            cdl.await();
            System.out.println("ALL JOB DONE");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void reentrantReadWriteLockExample() {
        //Много потоков могут одновременно читать одни данные
        //Писать данные может только один поток в единицу времени
        //Когда один поток пишет - никто не читает, и не пишет

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        new Thread(() -> {
            try {
                lock.readLock().lock();
                System.out.println("Start READ 1");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finished READ 1");
                lock.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.readLock().lock();
                System.out.println("Start READ 2");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finished READ 2");
                lock.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.readLock().lock();
                System.out.println("Start READ 3");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finished READ 3");
                lock.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.writeLock().lock();
                System.out.println("Start WRITE 1");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finished WRITE 1");
                lock.writeLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.readLock().lock();
                System.out.println("Start READ 4");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finished READ 4");
                lock.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.writeLock().lock();
                System.out.println("Start WRITE 2");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finished WRITE 2");
                lock.writeLock().unlock();
            }
        }).start();
    }

    private static void simpleReentrantLockExample() {
        Lock lock = new ReentrantLock();

        new Thread(() -> {
            System.out.println("Before lock 1");
            try {
                lock.lock();
                System.out.println("Got lock 1");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Lock 1 released");
                lock.unlock();
            }
        }).start();

//        new Thread(() -> {
//            System.out.println("Before lock 2");
//            try {
//                lock.lock();
//                System.out.println("Got lock 2");
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                System.out.println("Lock 2 released");
//                lock.unlock();
//            }
//        }).start();

        new Thread(() -> {
            System.out.println("Before lock 3");
//            if (lock.tryLock()) {
            try {
                if (lock.tryLock(4, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Got lock 3");
                        Thread.sleep(3000);
                    } finally {
                        System.out.println("Lock 3 released");
                        lock.unlock();
                    }
                } else {
                    System.out.println("Lock 3 was busy");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}