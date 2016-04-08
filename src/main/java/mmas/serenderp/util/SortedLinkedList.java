package main.java.mmas.serenderp.util;

import java.util.Iterator;

public class SortedLinkedList <E extends Comparable<E>> implements Iterable<E> {
	
	private Node<E> header = null;
	private int size = 0;
	
	public E first() {
		return header != null ? header.item : null;
	}
	
	public E last() {
		return size() > 0 ? get(size() - 1) : null;
	}
	
	public E poll() {
		if(isEmpty()) {
			return null;
		}
		E result = header.item;
		header = header.next;
		size--;
		return result;
	}
	
	public E get(int index) {
		if(size() <= index || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		Iterator<E> iterator = iterator();
		for(int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}
	
	public void add(E e) {
		if(isEmpty()) {
			header = new Node<E>(e, null);
		} else {
			if(shouldInsertBefore(e, header)) {
				// Create new node
				Node<E> newElement = new Node<E>(e, header);
				
				// Replace the header
				header = newElement;
			} else {
				Node<E> lastElement = header;
				
				// Skip until we should insert element
				while(lastElement.next != null && !shouldInsertBefore(e, lastElement.next)) {
					lastElement = lastElement.next;
				}
				
				// Create new node
				Node<E> newElement = new Node<E>(e, lastElement.next);
				
				// Link previous node to the new node
				lastElement.next = newElement;
			}
		}
		size++;
	}
	
	public boolean shouldInsertBefore(E e, Node<E> element) {
		return e.compareTo(element.item) > 0;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		Iterator<E> iterator = iterator();
		while(iterator.hasNext()) {
			E e = iterator.next();
			sb.append(e.toString());
			if(iterator.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
    private static class Node<E> {
         E item;
         Node<E> next;
 
         Node(E element, Node<E> next) {
             this.item = element;
             this.next = next;
         }
         
         @Override
         public String toString() {
     		return String.format("(item = %s, next = %s)", item, next == null ? null : next.item);
     	}
     }

	@Override
	public Iterator<E> iterator() {
		return new SortedLinkedListIterator();
	}
	
	private class SortedLinkedListIterator implements Iterator<E> {
		
		private Node<E> current = null;

		@Override
		public boolean hasNext() {
			if(size == 0) {
				return false;
			}
			if(current == null || current.next != null) {
				return true;
			}
			return false;
		}

		@Override
		public E next() {
			if(hasNext()) {
				if(current == null) {
					current = header;
				} else {
					current = current.next; 
				}
				return current.item;
			}
			return null;
		}
		
	}
}