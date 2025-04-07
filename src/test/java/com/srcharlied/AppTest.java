package com.srcharlied;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    private VectorHeap<Integer> heap;

    @Before
    public void setUp() {
        heap = new VectorHeap<>();
    }

    @Test
    public void testAddAndRemove() {
        heap.add(5);
        heap.add(3);
        heap.add(8);
        heap.add(1);

        assertEquals(1, (int) heap.remove());
        assertEquals(3, (int) heap.remove());
        assertEquals(5, (int) heap.remove());
        assertEquals(8, (int) heap.remove());
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveOnEmptyHeap() {
        heap.remove();
    }

    @Test
    public void testGetFirst() {
        heap.add(5);
        heap.add(3);
        heap.add(8);

        assertEquals(3, (int) heap.getFirst());
        assertEquals(3, (int) heap.getFirst()); 
    }

    @Test(expected = RuntimeException.class)
    public void testGetFirstOnEmptyHeap() {
        heap.getFirst();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(heap.isEmpty());
        heap.add(1);
        assertFalse(heap.isEmpty());
        heap.remove();
        assertTrue(heap.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, heap.size());
        heap.add(1);
        assertEquals(1, heap.size());
        heap.add(2);
        assertEquals(2, heap.size());
        heap.remove();
        assertEquals(1, heap.size());
        heap.clear();
        assertEquals(0, heap.size());
    }

    @Test
    public void testClear() {
        heap.add(5);
        heap.add(3);
        heap.clear();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        try {
            heap.getFirst();
            fail("Debería lanzar una excepción al intentar getFirst en un heap vacío");
        } catch (RuntimeException e) {
        }
    }
}