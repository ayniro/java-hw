package ru.spbstu.telematics.korobov;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AppTest 
{
    private int numberOfThreads = 4;
    private int amountOfPoints = 2000000000;
    private static final double DOUBLE_DELTA = 1e-3;

    @Test
    public void estimatePi()
    {
        MonteCarloPiCounter[] counters = new MonteCarloPiCounter[numberOfThreads];

        for (int i = 0; i < numberOfThreads; ++i) {
            counters[i] = new MonteCarloPiCounter(amountOfPoints / numberOfThreads);
        }

        for (int i = 1; i < numberOfThreads; ++i) {
            counters[i].start();
        }
        counters[0].run();

        try {
            for (int i = 1; i < numberOfThreads; ++i) {
                counters[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int totalInCircle = 0;
        int total = 0;
        for (int i = 0; i < counters.length; ++i) {
            totalInCircle += counters[i].getInCircle();
            total += counters[i].getTries();
        }

        double pi = ((double)totalInCircle) / total * 4;

        assertEquals(Math.PI, pi, DOUBLE_DELTA);
    }
}
