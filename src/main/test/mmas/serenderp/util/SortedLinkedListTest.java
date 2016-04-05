package main.test.mmas.serenderp.util;

import org.junit.Assert;
import org.junit.Test;

import main.java.mmas.serenderp.util.SortedLinkedList;

public class SortedLinkedListTest {
	
	@Test
	public void testSortedLinkedList() {
		SortedLinkedList<Integer> sortedLinkedList = new SortedLinkedList<Integer>();
		
		sortedLinkedList.add(-1);
		for(int i = 0; i < 10; i += 2) {
			sortedLinkedList.add(i);
		}
		for(int i = 1; i < 10; i += 2) {
			sortedLinkedList.add(i);
		}
		
		sortedLinkedList.add(10);
		sortedLinkedList.add(-1);
		
		Assert.assertEquals(10, sortedLinkedList.first().intValue());
		
		Assert.assertEquals(10, sortedLinkedList.get(0).intValue());
		Assert.assertEquals(9, sortedLinkedList.get(1).intValue());
		Assert.assertEquals(0, sortedLinkedList.get(sortedLinkedList.size()-3).intValue());
		Assert.assertEquals(-1, sortedLinkedList.get(sortedLinkedList.size()-2).intValue());
		Assert.assertEquals(-1, sortedLinkedList.last().intValue());
		
		int last = Integer.MAX_VALUE;
		for(Integer i : sortedLinkedList) {
			Assert.assertTrue(i <= last);
			last = i;
		}
	}
	
	@Test
	public void testInsertLastEveryTime() {
		SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
		
		for(int i = 10; 0 <= i; i--) {
			list.add(i);
		}
		
		Assert.assertEquals(10, list.first().intValue());
		Assert.assertEquals(0, list.last().intValue());
	}
	
	@Test
	public void testInsertFirstEveryTime() {
		SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
		
		for(int i = 0; i <= 10; i++) {
			list.add(i);
		}
		
		Assert.assertEquals(10, list.first().intValue());
		Assert.assertEquals(0, list.last().intValue());
	}
	
	@Test
	public void testAlternateInsertFirstLast() {
		SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
		
		for(int i = 1; i <= 10; i++) {
			list.add(i);
			list.add(-i);
		}
		
		Assert.assertEquals(10, list.first().intValue());
		Assert.assertEquals(-10, list.last().intValue());
	}
	
	@Test
	public void testEmptyList() {
		SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
		
		Assert.assertNull(list.first());
		Assert.assertNull(list.last());
		
		try {
			list.get(0);
			Assert.fail("SortedLinkedList.get(0) on an empty list should fail with IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException exn) {
			// Expected
		}
	}
}