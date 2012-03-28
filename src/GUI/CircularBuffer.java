package GUI;

import java.util.Iterator;

/**
 * @author Jonas Wagner
 *
 * A bounded circular buffer. If full and an element is added, the oldest element will be overwritten
 */
public class CircularBuffer<T> implements Iterable<T> {

	// The buffer where our objects are stored
	private T[] buffer;
	
	// Points to the first element of the buffer
	private int begin;
	
	// Points past the last element of the buffer
	private int end;
	
	@SuppressWarnings("unchecked")
	public CircularBuffer(int size) {
		if (size <= 0) throw new IllegalArgumentException("Size must be larger than zero.");
		
		this.buffer = (T[]) new Object[size];
		this.begin = 0;
		this.end = 0;
	}

	public void add(T element) {
		if (end == 0) {
			// Special case: the list is empty
			buffer[end] = element;
			++end;
		} else {
			if (end == buffer.length) end = 0;
			buffer[end] = element;
			if (end == begin) {
				// buffer is full
				++begin;
				if (begin == buffer.length) begin = 0;
			}
			++end;
		}
	}

	public T get(int i) {
		if (i < 0 || i >= size()) {
			throw new ArrayIndexOutOfBoundsException("Requested element " + i + ", but size is " + size());
		}
		i = (begin + i) % buffer.length;
		return buffer[i];
	}

	public int size() {
		if (end == 0) return 0;
		if (end > begin) return end - begin;
		return buffer.length + end - begin;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			// The position of the element that next() will return
			private int pos = begin;
			
			@Override
			public boolean hasNext() {
				return (pos != end);
			}

			@Override
			public T next() {
				T result = buffer[pos];
				++pos;
				if (pos != end && pos == buffer.length) pos = 0;
				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
