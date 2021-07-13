package myCollection;

import exceptions.MyIndexOutOfBoundsException;
import lombok.extern.java.Log;

import java.util.*;

@Log
public class MyList<E> implements List {
	protected transient int modCount = 0;
	private int size;
	transient Object[] elementData;
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = new Object[0];

	public MyList() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}

	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) {
			throw new OutOfMemoryError();
		} else {
			return minCapacity > 2147483639 ? 2147483647 : 2147483639;
		}
	}

	private Object[] grow() {
		return this.grow(this.size + 1);
	}

	private Object[] grow(int minCapacity) {
		return this.elementData = Arrays.copyOf(this.elementData, this.newCapacity(minCapacity));
	}

	private int newCapacity(int minCapacity) {
		int oldCapacity = this.elementData.length;
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		if (newCapacity - minCapacity <= 0) {
			if (this.elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
				return Math.max(10, minCapacity);
			} else if (minCapacity < 0) {
				throw new OutOfMemoryError();
			} else {
				return minCapacity;
			}
		} else {
			return newCapacity - 2147483639 <= 0 ? newCapacity : hugeCapacity(minCapacity);
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public boolean contains(Object o) {
		return this.contains(o);
	}

	@Override
	public Iterator iterator() {
		return this.listIterator();
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(this.elementData, this.size);
	}

	private void add(E e, Object[] elementData, int s) {
		if (s == elementData.length) {
			elementData = this.grow();
		}

		elementData[s] = e;
		this.size = s + 1;
	}

	@Override
	public boolean add(Object e) {
		++this.modCount;
		this.add((E) e, this.elementData, this.size);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		final Object[] es = elementData;
		final int size = this.size;
		int i = 0;
		found: {
			if (o == null) {
				for (; i < size; i++)
					if (es[i] == null)
						break found;
			} else {
				for (; i < size; i++)
					if (o.equals(es[i]))
						break found;
			}
			return false;
		}
		fastRemove(es, i);
		return true;
	}

	private void fastRemove(Object[] es, int i) {
		modCount++;
		final int newSize;
		if ((newSize = size - 1) > i)
			System.arraycopy(es, i + 1, es, i, newSize - i);
		es[size = newSize] = null;
	}

	@Override
	public boolean addAll(Collection c) {
		Object[] a = c.toArray();
		modCount++;
		int numNew = a.length;
		if (numNew == 0)
			return false;
		Object[] elementData;
		final int s;
		if (numNew > (elementData = this.elementData).length - (s = size))
			elementData = grow(s + numNew);
		System.arraycopy(a, 0, elementData, s, numNew);
		size = s + numNew;
		return true;
	}

	@Override
	public boolean addAll(int index, Collection c) {
		rangeCheckForAdd(index);

		Object[] a = c.toArray();
		modCount++;
		int numNew = a.length;
		if (numNew == 0)
			return false;
		Object[] elementData;
		final int s;
		if (numNew > (elementData = this.elementData).length - (s = size))
			elementData = grow(s + numNew);

		int numMoved = s - index;
		if (numMoved > 0)
			System.arraycopy(elementData, index,
					elementData, index + numNew,
					numMoved);
		System.arraycopy(a, 0, elementData, index, numNew);
		size = s + numNew;
		return true;
	}

	private void rangeCheckForAdd(int index) {
		if (index > size || index < 0) {
			log.info("Checking range of the list...");
			throw new MyIndexOutOfBoundsException(outOfBoundsMsg(index));

		}
	}

	private String outOfBoundsMsg(int index) {
		return "Index: "+index+", Size: "+size;
	}

	@Override
	public void clear() {
		modCount++;
		final Object[] es = elementData;
		for (int to = size, i = size = 0; i < to; i++)
			es[i] = null;
	}

	@Override
	public Object get(int index) {
		Objects.checkIndex(index, size);
		return elementData(index);
	}

	@SuppressWarnings("unchecked")
	E elementData(int index) {
		return (E) elementData[index];
	}


	@Override
	public Object set(int index, Object element) {
		Objects.checkIndex(index, size);
		E oldValue = elementData(index);
		elementData[index] = element;
		return oldValue;
	}

	@Override
	public void add(int index, Object element) {
		rangeCheckForAdd(index);
		modCount++;
		final int s;
		Object[] elementData;
		if ((s = size) == (elementData = this.elementData).length)
			elementData = grow();
		System.arraycopy(elementData, index,
				elementData, index + 1,
				s - index);
		elementData[index] = element;
		size = s + 1;
	}

	@Override
	public Object remove(int index) {
		Objects.checkIndex(index, size);
		final Object[] es = elementData;

		@SuppressWarnings("unchecked") E oldValue = (E) es[index];
		fastRemove(es, index);

		return oldValue;
	}

	@Override
	public int indexOf(Object o) {
		return indexOfRange(o, 0, size);
	}

	int indexOfRange(Object o, int start, int end) {
		Object[] es = elementData;
		if (o == null) {
			for (int i = start; i < end; i++) {
				if (es[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = start; i < end; i++) {
				if (o.equals(es[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOfRange(o, 0, size);
	}

	int lastIndexOfRange(Object o, int start, int end) {
		Object[] es = elementData;
		if (o == null) {
			for (int i = end - 1; i >= start; i--) {
				if (es[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = end - 1; i >= start; i--) {
				if (o.equals(es[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public ListIterator listIterator() {
		return null;
	}

	@Override
	public ListIterator listIterator(int index) {
		return null;
	}

	@Override
	public List subList(int fromIndex, int toIndex) {
		return null;
	}

	@Override
	public boolean retainAll(Collection c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		return batchRemove(c, false, 0, size);
	}

	boolean batchRemove(Collection<?> c, boolean complement,
						final int from, final int end) {
		Objects.requireNonNull(c);
		final Object[] es = elementData;
		int r;
		// Optimize for initial run of survivors
		for (r = from;; r++) {
			if (r == end)
				return false;
			if (c.contains(es[r]) != complement)
				break;
		}
		int w = r++;
		try {
			for (Object e; r < end; r++)
				if (c.contains(e = es[r]) == complement)
					es[w++] = e;
		} catch (Throwable ex) {
			// Preserve behavioral compatibility with AbstractCollection,
			// even if c.contains() throws.
			System.arraycopy(es, r, es, w, end - r);
			w += end - r;
			throw ex;
		} finally {
			modCount += end - w;
			shiftTailOverGap(es, w, end);
		}
		return true;
	}

	private void shiftTailOverGap(Object[] es, int lo, int hi) {
		System.arraycopy(es, hi, es, lo, size - hi);
		for (int to = size, i = (size -= hi - lo); i < to; i++)
			es[i] = null;
	}

	@Override
	public boolean containsAll(Collection c) {
		return false;
	}

	@Override
	public Object[] toArray(Object[] a) {
		return new Object[0];
	}
}
