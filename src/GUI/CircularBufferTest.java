package GUI;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CircularBufferTest {

	@Test
	public void testConstruction() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		assertTrue(cb instanceof CircularBuffer<?>);
	}
	
	@Test
	public void testInsertion() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		cb.add(1);
		cb.add(2);
		cb.add(3);
	}

	@Test
	public void testEmpty() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		for (Integer i: cb) {
			fail("Empty cb returned element: " + i);
		}
	}

	@Test
	public void testIterationWithOneElement() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		cb.add(1);
		
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		for (Integer i: cb) {
			assertEquals(expected.remove(0), i);
		}
	}

	@Test
	public void testFullIteration() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		cb.add(1);
		cb.add(2);
		
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		expected.add(2);
		for (Integer i: cb) {
			assertEquals(expected.remove(0), i);
		}
	}

	@Test
	public void testOverfullIteration() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		cb.add(1);
		cb.add(2);
		cb.add(3);
		
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(2);
		expected.add(3);
		for (Integer i: cb) {
			assertEquals(expected.remove(0), i);
		}
	}
	
	@Test
	public void testGet() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		cb.add(1);
		cb.add(2);
		cb.add(3);

		assertEquals(new Integer(2), cb.get(0));
		assertEquals(new Integer(3), cb.get(1));
	}

	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetEmpty() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);

		cb.get(0);
	}

	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetOutOfBounds() {
		CircularBuffer<Integer> cb = new CircularBuffer<Integer>(2);
		cb.add(2);

		assertEquals(new Integer(2), cb.get(0));
		cb.get(1);
	}
}
