package main.java.mmas.serenderp;
import static main.java.mmas.serenderp.Constants.DIMENSIONS;
import static main.java.mmas.serenderp.util.SparseVector.add;
import static main.java.mmas.serenderp.util.SparseVector.distance;
import static main.java.mmas.serenderp.util.SparseVector.divide;
import static main.java.mmas.serenderp.util.SparseVector.multiply;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import main.java.mmas.serenderp.brute.LinearAnnulus;
import main.java.mmas.serenderp.util.SparseVector;

public class Magic {
	public static int numberOfRatedMoviesForEligibility = 50;
	
	public static void main(String[] args) {
		Map<Integer, SparseVector> movies = PreProcess.getMovies();
		System.out.println("Got movies");
		List<List<Entry<Integer,Double>>> users = MovieLensReader.loadUserRatings();
		System.out.println("Got userratings");
		Magic.intuitionPlots(users, movies);
	}
	
	public static SparseVector getUserQueryPoint(List<Entry<Integer,Double>> userRatings, Map<Integer,SparseVector> movies, int numberOfMoviesToCalculateFrom) {
		SparseVector userAverageMovie = new SparseVector(Constants.DIMENSIONS);
		//Calculate the user's movie-center, weighed by rating
		List<Entry<Integer,Double>> topRatings = getTopMovies(userRatings, numberOfMoviesToCalculateFrom);
		double averageRating = calculateAverageRating(topRatings);
		for(Entry<Integer, Double> rating : topRatings) {
			SparseVector m = movies.get(rating.getKey());
			userAverageMovie = add(userAverageMovie, m);
		}
		return divide(userAverageMovie, topRatings.size());
	}
	
	private static List<Entry<Integer,Double>> getTopMovies(List<Entry<Integer,Double>> usersRatings, int numberOfMoviesInTop) {
		List<Entry<Integer, Double>> topMovies = new ArrayList<Entry<Integer, Double>>();
		usersRatings.sort(new Comparator<Entry<Integer, Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1,
					Entry<Integer, Double> o2) {
				if(o1.getValue() == o2.getValue()) return 0;
				if(o1.getValue() > o2.getValue()) return -1;
				return 1;
			} 
		});
		
		for(int i = 0; i < numberOfMoviesInTop; i++) {
			topMovies.add(usersRatings.get(i));
		}
		return topMovies;
	}
	
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

	public static void testSuccessProbability(Buckets buckets, Map<String, SparseVector> movies) {
		ArrayList<String> allMovieKeys = new ArrayList<String>(movies.keySet());
		ArrayList<String> movieKeys = new ArrayList<String>();

		Random r = new Random();

		Long startTime = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			int idx = r.nextInt(allMovieKeys.size());
			String m = allMovieKeys.get(idx);
			allMovieKeys.remove(idx);
			movieKeys.add(m);
		}

		Collection<SparseVector> moviesAsSparseVector = movies.values();
		for(String s : movieKeys) {
			Collection<SparseVector> aRes;
			Collection<SparseVector> eRes;
			aRes = Engine.query(buckets, 4, 12, 2, movies.get(s), 10);
			eRes = LinearAnnulus.query(moviesAsSparseVector, movies.get(s), 2, 25, 2, 10);
			System.out.println("Annulus query points: " + aRes.size());
			System.out.println("exhaustive query points: " + eRes.size());
			System.out.println("successprobability: " + ((double)aRes.size())/eRes.size());
		}

		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("successprobability took: %d sec", (duration / 1000)));
	}

	public static void intuitionPlots(List<List<Entry<Integer,Double>>> users, Map<Integer,SparseVector> movies) {
		Random rand = new Random();
		List<List<Entry<Integer,Double>>> userSample = new ArrayList<List<Entry<Integer,Double>>>();
		for (int i = 0; i < 10000; i++) {
			int idx = rand.nextInt(users.size());
			userSample.add(users.get(idx));
			users.remove(idx);
		}

		HashMap<Double, Double> ratingDistancesMap = new HashMap<>();
		HashMap<Double, Integer> ratingDistancesCountMap = new HashMap<>();

		for(List<Entry<Integer,Double>> user : userSample) {
			if(0 == user.size()) {
				continue;
			}
			//Get a max rated movie
			Collections.sort(user, (Entry<Integer,Double> a, Entry<Integer,Double> b) -> b.getValue().compareTo(a.getValue()));
			Stream<Entry<Integer,Double>> ratingStream = user.stream();
			Entry<Integer,Double> max = ratingStream.max((Entry<Integer,Double> a, Entry<Integer,Double> b) -> a.getValue().compareTo(b.getValue())).get();
			ratingStream = user.stream();
			ratingStream = ratingStream.filter((Entry<Integer,Double> x) -> x.getValue() == max.getValue());
			ArrayList<Entry<Integer,Double>> ratingsArray = new ArrayList<Entry<Integer,Double>>();
			ratingStream.forEach(ratingsArray::add);

			Entry<Integer,Double> qRating = ratingsArray.get(rand.nextInt(ratingsArray.size()));
			user.remove(qRating);
			SparseVector qMovie = movies.get(qRating.getKey());

			HashMap<Double, Double> userRatingDistancesMap = new HashMap<>();
			HashMap<Double, Integer> userRatingCountMap = new HashMap<>();
			for(Entry<Integer,Double> r : user) {
				SparseVector movie = movies.get(r.getKey()); //test
				double d = distance(qMovie, movies.get(r.getKey()));
				if(d<0.8) {
					int a = 0;  //test
				}
				userRatingDistancesMap.compute(r.getValue(), (k,v) ->(null == v) ? d : v+d);
				userRatingCountMap.compute(r.getValue(), (k,v) -> (null == v) ? 1 : v+1);
			}

			for(Double rating : userRatingDistancesMap.keySet()){
				double d = userRatingDistancesMap.get(rating);
				double avgDist = d/userRatingCountMap.get(rating);
				ratingDistancesMap.compute(rating, (k,v) -> (null == v) ? avgDist : v + avgDist);
				ratingDistancesCountMap.compute(rating, (k,v) -> (null == v) ? 1 : v+1);
			}
		}
		System.out.println("Radius vs rating:");
		for(double d = 1.0; d < 5.5; d += 0.5) {
			System.out.println(d + " " + ratingDistancesMap.get(d)/ratingDistancesCountMap.get(d));
		}
//		ratingDistancesMap.forEach((k,v) -> System.out.println(k + " " + v/userSample.size()));
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
	
	private static double calculateAverageRating(List<Entry<Integer,Double>> userRatings) {
		return calculateAverageRating(new HashSet<Entry<Integer, Double>>(userRatings));
	}
	
	private static double calculateAverageRating(Set<Entry<Integer,Double>> userRatings) {
		double sumOfRatings = 0.0;
		for(Entry<Integer, Double> rating : userRatings) {
			sumOfRatings += rating.getKey();
		}
		return sumOfRatings/(double)userRatings.size();
	}
}
