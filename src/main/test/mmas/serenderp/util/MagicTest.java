package main.test.mmas.serenderp.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;
import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.Magic;
import main.java.mmas.serenderp.util.SparseVector;

import org.junit.Test;

public class MagicTest {
	@Test
	public void testSerendipity() {
		double serendipity = Magic.calculateSerendipityForUser(getExampleUserRatings(), getExampleRecommendations());
		Assert.assertEquals(0.5, serendipity);
	}
	
	private List<Entry<Integer, Double>> getExampleUserRatings() {
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
	
	private List<Integer> getExampleRecommendations() {
		List<Integer> recommendations = new ArrayList<Integer>();
		recommendations.add(1);
		recommendations.add(2);
		recommendations.add(3);
		recommendations.add(5);
		recommendations.add(500);
		return recommendations;
	}
}
