package main.test.mmas.serenderp.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.Engine;
import main.java.mmas.serenderp.IMDBReader;
import main.java.mmas.serenderp.Magic;
import main.java.mmas.serenderp.MovieLensReader;
import main.java.mmas.serenderp.PreProcess;
import main.java.mmas.serenderp.brute.LinearAnnulus;
import main.java.mmas.serenderp.util.SparseVector;

public class MagicTest {
	private static Map<String, SparseVector> allMovies;
	private static final double c = 1, r = 1.38606209756, w = 1.007;
	private static final int serendipitousMoviesToFind = -1;
	private static Map<String, Integer> imdbToMovieLens;

	@BeforeClass
	public static void beforeClass() {
		allMovies = IMDBReader.getIMDBMovies();
	}

	// MAGIC SERENDIPITY TESTING
	@Test
	public void serendipityTest() {
		List<List<Entry<Integer, Double>>> userRatings = MovieLensReader.loadUserRatings();

		List<List<Entry<Integer, Double>>> validUsers = new ArrayList<>();
		for (List<Entry<Integer, Double>> user : userRatings) {
			if (Magic.isUserEligible(user)) {
				validUsers.add(user);
			}
		}
		System.out.println("Amount of valid users: " + validUsers.size() + " - with at least 500 ratings");

		Map<Integer, String> movieLensToImdb = MovieLensReader.loadMovies();
		imdbToMovieLens = PreProcess.getImdbToMovieLensMap();
		
		List<SparseVector> movieLensVectors = new ArrayList<>();
		for (String name : movieLensToImdb.values()) {
			movieLensVectors.add(allMovies.get(name));
		}

//		final int amountOfBands = 60, bandSize = 2, randomVectors = 10;
//		Constants.setParameters(amountOfBands, bandSize, randomVectors);
		Long startTime = System.currentTimeMillis();

		SparseVector queryPoint = null;

		double allTotal = 0;
		double totalUsers = 0;
		int votedOnTotal = 0;
		int i = 0;
		for (List<Entry<Integer, Double>> user : validUsers) {
			if (i == 3)
				break;
			List<Integer> queryPoints = Magic.getUserQueryPoints(user);
			
			Set<SparseVector> sparseRecommendations = new HashSet<>();
			for (Integer qId : queryPoints) {
				queryPoint = allMovies.get(movieLensToImdb.get(qId));
				sparseRecommendations.addAll(LinearAnnulus.query(movieLensVectors, queryPoint, r, w, c, serendipitousMoviesToFind));
			}

			List<Integer> recommendations = getMovieLensIds(sparseRecommendations);
			double d = Magic.calculateSerendipityForUser(user, recommendations);
			if (d != -1) {
				allTotal += d;
				totalUsers++;
				i++;
			}
			votedOnTotal += Magic.moviesUserHasVotedOn(user, recommendations);
			System.out.println("Done with user: " + i);
		}
		allTotal /= totalUsers;
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("Time for " + i + " users: %d sec", (duration / 1000)));

		System.out.println("FINAL SERENDIPITY FOR " + i + " USERS: " + allTotal);
		System.out.println("Based on: " + votedOnTotal + " ratings");
	}

//	@Test
	public void randomSerendipityTest() {
		List<List<Entry<Integer, Double>>> userRatings = MovieLensReader.loadUserRatings();

		List<List<Entry<Integer, Double>>> validUsers = new ArrayList<>();
		for (List<Entry<Integer, Double>> user : userRatings) {
			if (Magic.isUserEligible(user)) {
				validUsers.add(user);
			}
		}
		System.out.println("Amount of valid users: " + validUsers.size() + " - with at least 500 ratings");

		Random rng = new Random();
		double total = 0;
		for (List<Entry<Integer, Double>> user : validUsers) {
			// Create recommendations
			Set<Integer> recommendations = new HashSet<>();
			while (recommendations.size() < 100) {
				recommendations.add(user.get(rng.nextInt(user.size())).getKey());
			}

			double d = Magic.calculateSerendipityForUser(user, new ArrayList<Integer>(recommendations));
			total += d;
		}
		System.out.println(
				"Average serendipity: " + total / validUsers.size() + " - For " + validUsers.size() + " users");
	}

	private List<Integer> getMovieLensIds(Collection<SparseVector> sparseRecommendations) {
		List<Integer> result = new ArrayList<>();
		for (SparseVector sv : sparseRecommendations) {
			Integer i = imdbToMovieLens.get(sv.getMovieTitle());
			if (i != null) {
				result.add(i);
			}
		}
		return result;
	}

//	@Test
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
