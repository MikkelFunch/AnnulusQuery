package main.java.mmas.serenderp;
import static main.java.mmas.serenderp.util.SparseVector.add;
import static main.java.mmas.serenderp.util.SparseVector.distance;
import static main.java.mmas.serenderp.util.SparseVector.divide;
import static main.java.mmas.serenderp.util.SparseVector.multiply;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
}
