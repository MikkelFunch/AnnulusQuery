package main.test.mmas.serenderp.util;

import org.junit.Test;
import org.junit.Assert;
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
		Assert.assertEquals(-1, sortedLinkedList.get(sortedLinkedList.size()-1).intValue());
		
		int last = Integer.MAX_VALUE;
		for(Integer i : sortedLinkedList) {
			Assert.assertTrue(i <= last);
			last = i;
		}
	}
}