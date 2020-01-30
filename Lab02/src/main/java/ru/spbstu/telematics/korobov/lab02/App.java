package ru.spbstu.telematics.korobov.lab02;

public class App
{
    static class IntPair {
        int a;
        int b;
        IntPair(int first, int second) {
            a = first;
            b = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntPair intPair = (IntPair) o;
            return a == intPair.a &&
                    b == intPair.b;
        }

        @Override
        public int hashCode() {
            return a * b;
        }
    }

    public static void main( String[] args )
    {
        ru.spbstu.telematics.korobov.lab02.HashSet<IntPair> hashSet = new HashSet<>();

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }
        for (int i = 5; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                hashSet.remove(new IntPair(i, j));
            }
        }
        for (int i = 5; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (!hashSet.contains(new IntPair(i, j))) {
                    System.out.println(i + " " + j);
                }
            }
        }
    }
}
