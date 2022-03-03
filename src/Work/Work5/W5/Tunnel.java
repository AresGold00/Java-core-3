package Work.Work5.W5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private final Semaphore smp;

    public Tunnel(int length, int width) {
        this.length = length;
        this.description = "Тоннель " + length + " метров";
        this.smp = new Semaphore(width);
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                smp.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                smp.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}