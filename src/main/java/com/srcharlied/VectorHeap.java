package com.srcharlied;

import java.util.Vector;

public class VectorHeap<E extends Comparable<E>> implements PriorityQueue<E> {
    private Vector<E> data;

    public VectorHeap() {
        data = new Vector<>();
    }

    @Override
    public void add(E value) {
        data.add(value);
        percolateUp(data.size() - 1);
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new RuntimeException("La cola de prioridad está vacía");
        }
        E minVal = getFirst();
        data.set(0, data.get(data.size() - 1));
        data.setSize(data.size() - 1);
        if (!isEmpty()) {
            pushDownRoot(0);
        }
        return minVal;
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new RuntimeException("La cola de prioridad está vacía");
        }
        return data.get(0);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void clear() {
        data.clear();
    }

    private void percolateUp(int leaf) {
        int parent = (leaf - 1) / 2;
        E value = data.get(leaf);
        while (leaf > 0 && value.compareTo(data.get(parent)) < 0) {
            data.set(leaf, data.get(parent));
            leaf = parent;
            parent = (leaf - 1) / 2;
        }
        data.set(leaf, value);
    }

    private void pushDownRoot(int root) {
        int heapSize = data.size();
        E value = data.get(root);
        while (root < heapSize) {
            int childpos = 2 * root + 1;
            if (childpos < heapSize) {
                if (childpos + 1 < heapSize && data.get(childpos + 1).compareTo(data.get(childpos)) < 0) {
                    childpos++;
                }
                if (data.get(childpos).compareTo(value) < 0) {
                    data.set(root, data.get(childpos));
                    root = childpos;
                } else {
                    data.set(root, value);
                    return;
                }
            } else {
                data.set(root, value);
                return;
            }
        }
    }
}