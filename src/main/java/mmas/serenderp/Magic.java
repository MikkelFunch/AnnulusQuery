package main.java.mmas.serenderp;
import main.java.mmas.serenderp.brute.LinearAnnulus;
import static main.java.mmas.serenderp.util.SparseVector.add;
import static main.java.mmas.serenderp.util.SparseVector.distance;
import static main.java.mmas.serenderp.util.SparseVector.divide;
import static main.java.mmas.serenderp.util.SparseVector.multiply;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Collection;

import main.java.mmas.serenderp.util.SparseVector;

public class Magic {
	public static void assessMagic(List<List<Entry<Integer,Double>>> users, Map<Integer,SparseVector> movies) {
		int vectorSize = Constants.getDimensions();
		for(List<Entry<Integer,Double>> user : users) {
			SparseVector userAverageMovie = new SparseVector(vectorSize);
			//Calculate the users movie-center, weighed by rating
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
		ArrayList<String> allMovieKeys = new ArrayList(movies.keySet());
		ArrayList<String> movieKeys = new ArrayList<String>();

		Random r = new Random();

		Long startTime = System.currentTimeMillis();

		for (int i = 0; i < 1; i++) {
			int idx = r.nextInt(allMovieKeys.size());
			String m = allMovieKeys.get(idx);
			allMovieKeys.remove(idx);
			movieKeys.add(m);
		}

		Collection<SparseVector> moviesAsSparseVector = movies.values();
		for(String s : movieKeys) {
			Collection<SparseVector> aRes;
			Collection<SparseVector> eRes;
			aRes = Engine.query(buckets, 4, 12, 2, movies.get(s), Integer.MAX_VALUE);
			eRes = LinearAnnulus.query(moviesAsSparseVector, movies.get(s), 2, 25, 2, Integer.MAX_VALUE);
			System.out.println("Annulus query points: " + aRes.size());
			System.out.println("exhaustive query points: " + eRes.size());
			System.out.println("successprobability: " + ((double)aRes.size())/eRes.size());
		}

		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("successprobability took: %d sec", (duration / 1000)));
	}
}
