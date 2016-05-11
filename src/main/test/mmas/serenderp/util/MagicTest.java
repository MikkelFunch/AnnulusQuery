package main.test.mmas.serenderp.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.Magic;
import main.java.mmas.serenderp.util.SparseVector;

import org.junit.Test;

public class MagicTest {
	@Test
	public void testUserQueryPoint() {
		Constants.setDimensions(4);
		Map<Integer, SparseVector> movies = getExampleMovies();
		List<Entry<Integer, Double>> userRatings = getUserRatings();
		
		Magic.getUserQueryPoint(userRatings, movies, 2);
		
	}
	
	private List<Entry<Integer, Double>> getUserRatings() {
		List<Entry<Integer, Double>> result = new ArrayList<Entry<Integer, Double>>();
		
		Entry rating1 = new AbstractMap.SimpleEntry<Integer, Double>(1, 5.0);
		Entry rating2 = new AbstractMap.SimpleEntry<Integer, Double>(2, 1.0);
		Entry rating3 = new AbstractMap.SimpleEntry<Integer, Double>(3, 4.0);
		Entry rating4 = new AbstractMap.SimpleEntry<Integer, Double>(4, 2.0);
		Entry rating5 = new AbstractMap.SimpleEntry<Integer, Double>(5, 2.0);
		
		
		result.add(rating1);
		result.add(rating2);
		result.add(rating3);
		result.add(rating4);
		result.add(rating5);
		
		return result;
	}
	
	private Map<Integer, SparseVector> getExampleMovies() {
		SparseVector a = new SparseVector(4);
		SparseVector b = new SparseVector(4);
		SparseVector c = new SparseVector(4);
		SparseVector d = new SparseVector(4);
		SparseVector e = new SparseVector(4);
		a.addEntry(1);
		a.addEntry(0);
		a.addEntry(0);
		a.addEntry(0);
		
		b.addEntry(1);
		b.addEntry(1);
		b.addEntry(0);
		b.addEntry(0);
		
		c.addEntry(1);
		c.addEntry(1);
		c.addEntry(1);
		c.addEntry(0);
		
		d.addEntry(1);
		d.addEntry(1);
		d.addEntry(1);
		d.addEntry(1);
		
		e.addEntry(0);
		e.addEntry(1);
		e.addEntry(1);
		e.addEntry(1);
		
		Map<Integer, SparseVector> movies = new HashMap<Integer, SparseVector>();
		movies.put(1, a);
		movies.put(2, b);
		movies.put(3, c);
		movies.put(4, d);
		movies.put(5, e);
		
		return movies;
	}
}
