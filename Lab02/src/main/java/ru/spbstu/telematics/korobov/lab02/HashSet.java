package ru.spbstu.telematics.korobov.lab02;

import java.lang.reflect.Array;
import java.util.*;

public class HashSet<T> implements Set<T>, Iterable<T> {
    static class Node<T> {
        final T key;

        Node(T key) {
            this.key = key;
        }
    }

    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final int DEFAULT_INITIAL_SIZE = 32;

    private Object[] entries;
    private boolean[] isDeleted;
    private int size = 0;
    private int capacity = DEFAULT_INITIAL_SIZE; // max
    private float loadFactor = DEFAULT_LOAD_FACTOR;

    public HashSet() {
        this(DEFAULT_INITIAL_SIZE);
    }

    public HashSet(int capacity) {
        if (capacity > 0) {
            this.capacity = ceilPowerOf2(capacity);
            entries = new Object[this.capacity];
            isDeleted = new boolean[this.capacity];
        } else {
            throw new IllegalArgumentException("Capacity should be > 0");
        }
    }

    public int size() {
        return size;
    }

    public boolean contains(Object item) {
        int iteration = 0;
        int keyHashCode = Objects.hashCode(item);
        int hashIndex = getElementIndex(keyHashCode, iteration++);

        while (isDeleted[hashIndex] || entries[hashIndex] != null) {
            if (entries[hashIndex] != null) {
                Node<?> currNode = (Node<?>)entries[hashIndex];
                if (Objects.equals(currNode.key, item)) {
                    return !isDeleted[hashIndex];
                }
            }
            hashIndex = getElementIndex(keyHashCode, iteration++);
        }

        return false;
    }

    public boolean add(T item) {
        int iteration = 0;
        int keyHashCode = Objects.hashCode(item);
        int hashIndex = getElementIndex(keyHashCode, iteration++);

        while (!isDeleted[hashIndex] && entries[hashIndex] != null && !Objects.equals(((Node<?>)entries[hashIndex]).key, item)) {
            hashIndex = getElementIndex(keyHashCode, iteration++);
        }

        if (isDeleted[hashIndex] || entries[hashIndex] == null) {
            size++;
            entries[hashIndex] = new Node<T>(item);
            isDeleted[hashIndex] = false;

            if (size > capacity * loadFactor) {
                resize(capacity * 2);
            }

            return true;
        }

        return false;
    }

    public boolean remove(Object item) {
        int iteration = 0;
        int keyHashCode = Objects.hashCode(item);
        int hashIndex = getElementIndex(keyHashCode, iteration++);

        while (isDeleted[hashIndex] || entries[hashIndex] != null) {
            if (entries[hashIndex] != null) {
                Node<?> currNode = (Node<?>)entries[hashIndex];
                if (Objects.equals(currNode.key, item)) {
                    size--;
                    isDeleted[hashIndex] = true;

                    if (size < capacity * loadFactor * loadFactor) {
                        resize(capacity / 2);
                    }

                    return true;
                }
            }
            hashIndex = getElementIndex(keyHashCode, iteration++);
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T o : c) {
            if (add(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < capacity; ++i) {
            if (entries[i] != null && !isDeleted[i]) {
                if (!c.contains(((Node<?>)(entries[i])).key)) {
                    isDeleted[i] = true;
                    size--;
                    changed = true;
                }
            }
        }

        if (size < capacity * loadFactor * loadFactor) {
            resize(capacity / 2);
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        capacity = DEFAULT_INITIAL_SIZE;
        Arrays.fill(entries, null);
        Arrays.fill(isDeleted, false);
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int iteratedElements = 0;
            private int currElement = 0;
            @Override
            public boolean hasNext() {
                return iteratedElements < size;
            }

            @Override
            public T next() {
                while (entries[currElement] == null || isDeleted[currElement]) {
                    currElement++;
                }
                iteratedElements++;
                @SuppressWarnings("unchecked")
                T returnValue = ((Node<T>)(entries[currElement])).key;
                return returnValue;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] oldEntries = new Object[size];
        int k = 0;
        for (int i = 0; i < entries.length && k < size; ++i) {
            if (entries[i] != null && !isDeleted[i]) {
                oldEntries[k++] = entries[i];
            }
        }
        return oldEntries;
    }

    @Override
    public <T1> T1[] toArray(T1[] array) {
        int size = size();
        if (array.length < size) {
            // If array is too small, allocate the new one with the same component type
            array = (T1[])Array.newInstance(array.getClass().getComponentType(), size);
        } else if (array.length > size) {
            // If array is to large, set the first unassigned element to null
            array[size] = null;
        }

        int i = 0;
        for (T e: this) {
            // No need for checked cast - ArrayStoreException will be thrown
            // if types are incompatible, just as required
            array[i] = (T1) e;
            i++;
        }
        return array;
    }

    private boolean resize(int newSize) {
        if (newSize < size) {
            return false;
        }

        Object[] oldEntries = toArray();

        capacity = ceilPowerOf2(newSize);
        size = 0;
        isDeleted = new boolean[capacity];
        entries = new Object[capacity];

        for (int i = 0; i < oldEntries.length; ++i) {
            @SuppressWarnings("unchecked")
            T item = ((Node<T>)oldEntries[i]).key;
            add(item);
        }

        return true;
    }

    private static int ceilPowerOf2(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("CeilPowerOf2: argument should be positive");
        }
        num--;
        num |= num >> 1;
        num |= num >> 2;
        num |= num >> 4;
        num |= num >> 8;
        num |= num >> 16;
        return num + 1;
    }

    private int getElementIndex(int key, int queryAttempt) {
        // if key is negative
        key = key & 0x7fffffff;
        // same as (key % capacity) because of capacity being 2^n
        int hash1 = key & (capacity - 1);
        // no real reason for choosing odd number
        int oddNumber = (capacity / 4) | 1;
        // hash2 is odd, therefore we can iterate over all entries
        int hash2 = (oddNumber - (key % oddNumber)) | 1;
        return (hash1 + queryAttempt * hash2) & (capacity - 1);
    }
}
