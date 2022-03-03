package Work.Work4;

import java.util.concurrent.*;

public class MultithreadingExample {

    private static final Object mon = new Object();

    private static int order = 0;


    public static void main(String[] args) {
//        callableExample();
//        executorServiceExample();
//        scheduledExecutorExample();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (mon) {
                    try {
                        while (order != 0) {
                            mon.wait();
                        }
                        printHello();
                        Thread.sleep(150);
                        order = 1;
                        mon.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (mon) {
                    try {
                        while (order != 1) {
                            mon.wait();
                        }
                        printWorld();
                        Thread.sleep(150);
                        order = 2;
                        mon.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (mon) {
                    try {
                        while (order != 2) {
                            mon.wait();
                        }
                        printSign();
                        Thread.sleep(150);
                        order = 0;
                        mon.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private static void printHello() {
        System.out.print("Hello");
    }

    private static void printWorld() {
        System.out.print(" world");
    }

    private static void printSign() {
        System.out.println("!");
    }

    private static void scheduledExecutorExample() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> System.out.println("mkfkm"), 1, 1, TimeUnit.SECONDS);
    }

    private static void executorServiceExample() {
        //        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                var t = new Thread(r);
//                t.setName("THREAD_POOL");
//                return t;
//            }
//        });
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            var j = i;
            executorService.execute(() -> {
                System.out.printf("Task #%s started. Thread name: %s\n", j, Thread.currentThread().getName());
                try {
                    Thread.sleep((long) (1500 + 1500 * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.printf("Task #%s finished\n", j);
            });
        }

        var future = executorService.submit(() -> "Hello world");

        System.out.println("All tasks are given");

        executorService.shutdown();
//        executorService.shutdownNow();
        try {
            executorService.awaitTermination(20, TimeUnit.SECONDS);
            System.out.println(future.get());
            System.out.println("Finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void callableExample() {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                throw new RuntimeException("HAHAHA");
//                return "Hello world";
            }
        });

        new Thread(futureTask).start();

        try {
//            var result = futureTask.get();
            var result = futureTask.get(10, TimeUnit.MINUTES);
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}