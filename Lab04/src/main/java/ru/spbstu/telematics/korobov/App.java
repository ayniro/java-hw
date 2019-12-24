package ru.spbstu.telematics.korobov;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        int totalTries = 2000000000;
        int threadsCount = 4;

        System.out.println(Runtime.getRuntime().availableProcessors());

        long start = System.currentTimeMillis();

        MonteCarloPiCounter[] counters = new MonteCarloPiCounter[threadsCount];

        for (int i = 0; i < threadsCount; ++i) {
            counters[i] = new MonteCarloPiCounter(totalTries / threadsCount);
        }

        for (int i = 1; i < threadsCount; ++i) {
            counters[i].start();
        }
        counters[0].run();

        try {
            for (int i = 1; i < threadsCount; ++i) {
                counters[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long timeConsumed = System.currentTimeMillis() - start;

        int totalInCircle = 0;
        int total = 0;
        for (int i = 0; i < counters.length; ++i) {
            totalInCircle += counters[i].getInCircle();
            total += counters[i].getTries();
            System.out.println(
                    counters[i].getCounterName() +
                            ": In circle: " +
                            counters[i].getInCircle() +
                            " Out of circle: " +
                            counters[i].getOutOfCircle());
        }

        System.out.println("Estimated pi value: " + ((double)totalInCircle) / total * 4);
        System.out.println("Time taken: " + timeConsumed + " ms");
    }
}
