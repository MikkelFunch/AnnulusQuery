package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.util.SparseVector.add;
import static main.java.mmas.serenderp.util.SparseVector.distance;
import static main.java.mmas.serenderp.util.SparseVector.divide;
import static main.java.mmas.serenderp.util.SparseVector.multiply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import main.java.mmas.serenderp.util.SparseVector;
import static main.java.mmas.serenderp.Constants.*;

public class Magic {
	public static int numberOfRatedMoviesForEligibility = 50;
	
	public static void assessMagic(List<List<Entry<Integer,Double>>> users, Map<Integer,SparseVector> movies) {
		int vectorSize = DIMENSIONS;
		for(List<Entry<Integer,Double>> user : users) {
			SparseVector userAverageMovie = new SparseVector(vectorSize);
			//Calculate the user's movie-center, weighed by rating
			for(int i=0; i<user.size(); i++) {
				Entry<Integer,Double> rating = user.get(i);
				SparseVector m = movies.get(rating.getKey());
				m = multiply(m, rating.getValue());
				userAverageMovie = add(userAverageMovie, m);
			}
			userAverageMovie = divide(userAverageMovie, user.size());
			double[] distances = new double[user.size()];
			//Calculate distances from center
			for(int i=0; i<user.size(); i++) {
				Entry<Integer,Double> rating = user.get(i);
				SparseVector m = movies.get(rating.getKey());
				distances[i] = distance(userAverageMovie, m);
			}

			double avgDist=0;
			double maxDist=0;
			for (double d : distances){
				avgDist += d;
				maxDist = Math.max(d, maxDist);
			}
			System.out.println("User with " + distances.length + " rated movs");
			System.out.println("avg dist: " + avgDist/distances.length);
			System.out.println("max dist: " + maxDist);
		}
	}
	
	/***
	 * Returns less than zero if the user did not rate any of the recommendations
	 */
	public static double calculateSerendipityForUser(Map<Integer,Double> userRatings, List<Integer> recommendations) {
		double serendipity;
		double numberOfUsefulMovies = 0.0;
		double numberOfExaminedMovies = 0.0;
		double averageRating = calculateAverageRating(userRatings.entrySet());
		for(Integer movieId : recommendations) {
			if(userRatings.containsKey(movieId)) {
				if(userRatings.get(movieId) > averageRating) numberOfUsefulMovies++;
				numberOfExaminedMovies++;
			}
		}
		if(numberOfExaminedMovies == 0) {
			serendipity = -1.0;
		} else {
			serendipity = (double)numberOfUsefulMovies/(double)numberOfExaminedMovies;
		}
		return serendipity;
		
	}
	
	public static double calculateSerendipityForUser(List<Entry<Integer,Double>> userRatings, List<Integer> recommendations) {
		Map<Integer, Double> userRatingsAsMap = new HashMap<Integer, Double>();
		for(Entry<Integer, Double> rating : userRatings) {
			if(userRatingsAsMap.containsKey(rating.getKey())) {
				throw new IllegalStateException("The list of ratings contained two ratings for one movie");
			}
			userRatingsAsMap.put(rating.getKey(), rating.getValue());
		}
		return calculateSerendipityForUser(userRatingsAsMap, recommendations);
	}
	
	
	
	public boolean isUserEligible(List<Entry<Integer,Double>> userRatings) {
		return userRatings.size() > numberOfRatedMoviesForEligibility;
	}
	
	private static double calculateAverageRating(Set<Entry<Integer,Double>> userRatings) {
		double sumOfRatings = 0.0;
		for(Entry<Integer, Double> rating : userRatings) {
			sumOfRatings += rating.getKey();
		}
		return sumOfRatings/(double)userRatings.size();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
