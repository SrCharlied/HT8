package com.srcharlied;

public interface PriorityQueue<E> {
    void add(E value);
    E remove();
    E getFirst();
    boolean isEmpty();
    int size();
    void clear();
}