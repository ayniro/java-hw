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

    }
}
