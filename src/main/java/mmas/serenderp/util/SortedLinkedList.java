package main.java.mmas.serenderp.util;

import java.util.Iterator;

public class SortedLinkedList <E extends Comparable<E>> implements Iterable<E> {
	
	private Node<E> header = null;
	private int size = 0;
	
	public E first() {
		return header != null ? header.item : null;
	}
	
	public E get(int index) {
		if(size() < index) {
			throw new IndexOutOfBoundsException();
		}
		Iterator<E> iterator = iterator();
		for(int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}
	
	public void add(E e) {
		if(size() == 0) {
			header = new Node<E>(null, e, null);
		} else {
			Node<E> previousElement = null;
			Node<E> currentElement = header;
			
			// Skip until e is larger than the next element
			while(currentElement != null && e.compareTo(currentElement.item) < 0) {
				previousElement = currentElement;
				currentElement = currentElement.next;
			}
			
			// Create new node
			Node<E> newElement = new Node<E>(previousElement, e, currentElement);
			
			// Link the previous node to the new node
			if(previousElement != null) {
				previousElement.next = newElement;
			}
			
			// Link the next node the new node
			if(currentElement != null) {
				currentElement.prev = newElement;
			}
			
			if(header.equals(currentElement)) {
				header = newElement;
			}
		}
		size++;
	}
	
	public boolean shouldInsertBefore(E e, Node<E> element) {
		return e.compareTo(element.item) > 0;
	}
	
	public int size() {
		return size;
	}
	
    private static class Node<E> {
         E item;
         Node<E> next;
         Node<E> prev;
 
         Node(Node<E> prev, E element, Node<E> next) {
             this.item = element;
             this.next = next;
             this.prev = prev;
         }
         
         @Override
         public String toString() {
     		return String.format("(prev = %s, item = %s, next = %s)", prev == null ? null : prev.item, item, next == null ? null : next.item);
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