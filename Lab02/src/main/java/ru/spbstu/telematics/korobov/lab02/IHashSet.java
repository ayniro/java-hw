package ru.spbstu.telematics.korobov.lab02;

public interface IHashSet<T> {
    boolean add(T item);
    void clear();
    boolean contains(T item);
    boolean isEmpty();
    boolean remove(T item);
    int size();
}
