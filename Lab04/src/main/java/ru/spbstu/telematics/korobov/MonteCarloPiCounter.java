package ru.spbstu.telematics.korobov;

import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloPiCounter extends Thread {
    private int inCircle = 0;
    private int outOfCircle = 0;
    private int tries = 0;

    MonteCarloPiCounter(int triesCount) {
        tries = triesCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < tries; ++i) {
            double x = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            double y = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            double d = x * x + y * y;
            if (d < 1.0) {
                inCircle++;
            } else {
                outOfCircle++;
            }
        }
    }

    public int getInCircle() {
        return inCircle;
    }

    public int getOutOfCircle() {
        return outOfCircle;
    }

    public int getTries() {
        return tries;
    }

    public String getCounterName() {
        return this.getName();
    }
}
