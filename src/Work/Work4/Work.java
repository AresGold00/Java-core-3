package Work.Work4;

public class Work {
    private static final Object mon = new Object();
    private static char letter = 'A';

    public static void main(String[] args) throws InterruptedException {
        new Thread(Work::printA).start();
        new Thread(Work::printB).start();
        new Thread(Work::printC).start();
        Thread.sleep(4000);
        System.out.println();
        new Thread(() -> printUniversal('A')).start();
        new Thread(() -> printUniversal('B')).start();
        new Thread(() -> printUniversal('C')).start();
    }

    private static void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != 'A') {
                        mon.wait();
                    }
                    System.out.print(letter);
                    letter = 'B';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != 'B') {
                        mon.wait();
                    }
                    System.out.print(letter);
                    letter = 'C';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != 'C') {
                        mon.wait();
                    }
                    System.out.print(letter + "  ");
                    letter = 'A';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printUniversal (char symbol){
        synchronized (mon){
            try{
                for (int i = 0; i < 10; i++) {
                    while (letter != symbol){
                        mon.wait();
                    }
                    System.out.print(symbol);
                    switch (symbol){
                        case 'A':
                            letter ='B';
                            mon.notifyAll();
                            break;
                        case 'B':
                            letter = 'C';
                            mon.notifyAll();
                            break;
                        case 'C':
                            System.out.print("  ");
                            letter = 'A';
                            mon.notifyAll();
                            break;
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}