package ru.spbstu.telematics.korobov.lab02;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class HashSetTest
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

    private static final Random RANDOM = new Random();

    @Test
    public void addingIntsTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 1; i < 10000; ++i) {
            hashSet.add(i);
        }

        for (int i = 1; i < 10000; ++i) {
            Assert.assertTrue(hashSet.contains(i));
            Assert.assertFalse(hashSet.contains(-i));
        }
    }

    @Test
    public void addingSameValuesTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 0; i < 10000; ++i) {
            hashSet.add(i);
        }

        for (int i = 0; i < 10000; ++i) {
            Assert.assertFalse(hashSet.add(i));
        }

        for (int i = 0; i < 10000; ++i) {
            Assert.assertTrue(hashSet.contains(i));
        }
    }

    @Test
    public void addingAfterRemovalTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 0; i < 10000; ++i) {
            hashSet.add(i);
        }

        for (int i = 0; i < 10000; ++i) {
            Assert.assertTrue(hashSet.remove(i));
        }

        for (int i = 0; i < 10000; ++i) {
            Assert.assertFalse(hashSet.contains(i));
        }

        for (int i = 0; i < 10000; ++i) {
            Assert.assertTrue(hashSet.add(i));
        }

        for (int i = 0; i < 10000; ++i) {
            Assert.assertTrue(hashSet.contains(i));
        }
    }

    @Test
    public void emptyHashSetAddingNullTest() {
        HashSet<Integer> emptyHashSet = new HashSet<>();

        emptyHashSet.add(null);

        Assert.assertTrue(emptyHashSet.contains(null));
    }

    @Test
    public void notEmptyHashSetAddingNullTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 0; i < 10000; ++i) {
            hashSet.add(i);
            hashSet.add(null);
        }

        Assert.assertTrue(hashSet.contains(null));
    }

    @Test
    public void removeNullTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 0; i < 10000; ++i) {
            hashSet.add(i);
            hashSet.add(null);
        }
        hashSet.remove(null);

        Assert.assertFalse(hashSet.contains(null));
    }

    @Test
    public void doesNotContainTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        Assert.assertFalse(hashSet.contains(null));
        for (int i = 0; i < 1000; ++i) {
            Assert.assertFalse(hashSet.contains(i));
        }
    }

    @Test
    public void removeIntsTest() {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 0; i < 10; ++i) {
            hashSet.add(i);
        }

        for (int i = 0; i < 10; i += 2) {
            Assert.assertTrue(hashSet.remove(i));
        }

        for (int i = 0; i < 10; i += 2) {
            Assert.assertFalse(hashSet.contains(i));
            Assert.assertTrue(hashSet.contains(i + 1));
        }
    }

    @Test
    public void hashCodeCollisionsTest() {
        HashSet<IntPair> hashSet = new HashSet<>();

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                Assert.assertTrue(hashSet.contains(new IntPair(j, i)));
                Assert.assertFalse(hashSet.contains(new IntPair(i + 100, j + 100)));
            }
        }

        for (int i = 0; i < 100; ++i) {
            for (int j = 10; j < 100; ++j) {
                hashSet.remove(new IntPair(i, j));
            }
        }

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 10; ++j) {
                Assert.assertTrue(hashSet.contains(new IntPair(i, j)));
            }
        }
    }

    @Test
    public void removeNonExistentTest() {
        HashSet<IntPair> hashSet = new HashSet<>();

        for (int i = 0; i < 100; ++i) {
            for (int j = i; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }

        for (int i = 100; i >= 0; --i) {
            for (int j = 0; j < i; ++j) {
                Assert.assertFalse(hashSet.remove(new IntPair(i, j)));
            }
        }
    }

    @Test
    public void removeAddTest() {
        HashSet<IntPair> hashSet = new HashSet<>();

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }
        for (int i = 50; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.remove(new IntPair(i, j));
            }
        }
        for (int i = 50; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                Assert.assertTrue(hashSet.contains(new IntPair(i, j)));
            }
        }
    }

    @Test
    public void randomIntsTest() {
        java.util.HashSet<Integer> checkHashSet = new java.util.HashSet<>();
        ArrayList<Integer> values = new ArrayList<>();
        HashSet<Integer> testHashSet = new HashSet<>();

        for (int i = 0; i < 100000; ++i) {
            int rand = RANDOM.nextInt();
            checkHashSet.add(rand);
            testHashSet.add(rand);
            values.add(rand);
        }

        for (int i = 0; i < 10000; ++i) {
            int rand = RANDOM.nextInt();
            checkHashSet.remove(rand);
            testHashSet.remove(rand);
        }

        for (int i = 0; i < 100000; ++i) {
            Assert.assertEquals(checkHashSet.contains(values.get(i)), testHashSet.contains(values.get(i)));
        }
        Assert.assertEquals(checkHashSet.size(), testHashSet.size());
    }

    @Test
    public void randomIntPairsTest() {
        java.util.HashSet<IntPair> checkHashSet = new java.util.HashSet<>();
        ArrayList<IntPair> values = new ArrayList<>();
        HashSet<IntPair> testHashSet = new HashSet<>();

        for (int i = 0; i < 100000; ++i) {
            int randFirst = RANDOM.nextInt();
            int randSecond = RANDOM.nextInt();
            checkHashSet.add(new IntPair(randFirst, randSecond));
            testHashSet.add(new IntPair(randFirst, randSecond));
            values.add(new IntPair(randFirst, randSecond));
        }

        for (int i = 0; i < 10000; ++i) {
            int randFirst = RANDOM.nextInt();
            int randSecond = RANDOM.nextInt();
            checkHashSet.remove(new IntPair(randFirst, randSecond));
            testHashSet.remove(new IntPair(randFirst, randSecond));
        }

        for (int i = 0; i < 100000; ++i) {
            Assert.assertEquals(checkHashSet.contains(values.get(i)), testHashSet.contains(values.get(i)));
        }
        Assert.assertEquals(checkHashSet.size(), testHashSet.size());
    }

    @Test
    public void iteratorTest() {
        HashSet<IntPair> hashSet = new HashSet<>();

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }
        for (int i = 50; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.remove(new IntPair(i, j));
            }
        }
        for (int i = 50; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }

        int k = 0;
        for (IntPair pair : hashSet) {
            k++;
            Assert.assertTrue(hashSet.contains(pair));
        }
        Assert.assertEquals(k, hashSet.size());
    }

    @Test
    public void clearTest() {
        HashSet<IntPair> hashSet = new HashSet<>();

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                hashSet.add(new IntPair(i, j));
            }
        }

        hashSet.clear();

        Assert.assertEquals(0, hashSet.size());
    }
}
