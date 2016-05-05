import main.java.mmas.serenderp.util.SparseVector;
import static main.java.mmas.serenderp.util.SparseVector.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Magic {
	public static void assessMagic(List<List<Entry<Integer,Double>>> users, Map<Integer,SparseVector> movies) {
		int vectorSize = movies.get(0).size();
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
			System.out.println("avg movie: " + userAverageMovie);
			double[] distances = new double[user.size()];
			//Calculate distances from center
			for(int i=0; i<user.size(); i++) {
				Entry<Integer,Double> rating = user.get(i);
				SparseVector m = movies.get(rating.getKey());
				distances[i] = distance(userAverageMovie, m);
			}

			double avgDist=0;
			for (double d : distances){
				avgDist += d;
			}
			System.out.println("avg dist: " + avgDist/user.size());
		}
	}
}
