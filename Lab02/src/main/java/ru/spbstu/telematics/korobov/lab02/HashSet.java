package ru.spbstu.telematics.korobov.lab02;

import java.util.Iterator;

public class HashSet<T> implements Iterable<T> {
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
        this.capacity = ceilPowerOf2(capacity);
    }

    public int size() {
        return size;
    }

    public boolean contains(T item) {
        int iteration = 0;
        int keyHashCode = item.hashCode();
        int hashIndex = getElementIndex(keyHashCode, iteration++);

        while (isDeleted[hashIndex] || entries[hashIndex] != null)
        {
            if (entries[hashIndex] != null)
            {
                if (entries[hashIndex].equals(item)) {
                    return true;
                }
            }
            hashIndex = getElementIndex(keyHashCode, iteration++);
        }

        return false;
    }

    public boolean add(T item) {
        if (item == null) throw new NullPointerException("HashSet: add(T item), item can't be null");
        int iteration = 0;
        int keyHashCode = item.hashCode();
        int hashIndex = getElementIndex(keyHashCode, iteration++);

        while (!isDeleted[hashIndex] && entries[hashIndex] != null && !entries[hashIndex].equals(item))
        {
            hashIndex = getElementIndex(keyHashCode, iteration++);
        }

        if (isDeleted[hashIndex] || entries[hashIndex] == null)
        {
            size++;
            entries[hashIndex] = item;
            if (size > capacity * loadFactor)
            {
                resize(capacity * 2);
            }
            return true;
        }

        return false;
    }

    public boolean remove(T item) {
        return false;
        /*
        int iteration = 0;
        int keyHashCode = key.GetHashCode();
        int hashIndex = GetIndex(keyHashCode, iteration++);
        var returnValue = default(TValue);

        while (_isDeleted[hashIndex] || _entries[hashIndex] != null)
        {
            if (_entries[hashIndex] != null)
            {
                if (_comparer(_entries[hashIndex].Key, key))
                {
                    Size--;
                    returnValue = _entries[hashIndex].Value;
                    RemoveEntryFromList(_entries[hashIndex]);
                    _isDeleted[hashIndex] = true;

                    var args = new ElementRemovedEventArgs<KeyValueEntry<TKey, TValue>>
                    {
                        CollectionName = this.GetType().Name,
                                Data = _entries[hashIndex]
                    };
                    _entries[hashIndex] = null;
                    OnElementDeleted(args);

                    if (Size < Capacity * MaxLoadFactor * MaxLoadFactor)
                    {
                        Resize(Capacity / 2);
                    }

                    return returnValue;
                }
            }

            hashIndex = GetIndex(keyHashCode, iteration++);
        }

        return returnValue;
        */
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean clear() {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    private boolean resize(int newSize) {
        if (newSize < size) {
            return false;
        }

        Object[] oldEntries = new Object[size];
        for (int i = 0, k = 0; i < entries.length; ++i)
        {
            if (entries[i] != null) {
                oldEntries[k++] = entries[i];
            }
        }

        capacity = ceilPowerOf2(newSize);
        size = 0;
        isDeleted = new boolean[capacity];
        entries = new Object[capacity];

        for (Object oldEntry : oldEntries) {
            @SuppressWarnings("unchecked")
            T item = (T) oldEntry;
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
