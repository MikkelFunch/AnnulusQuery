import main.java.mmas.serenderp.util.SparseVector;
import static main.java.mmas.serenderp.util.SparseVector.*;
import java.util.List;
import java.util.Map.Entry;

public class Magic {
	private static void assessMagic(List<List<Entry<Integer,Double>>> users, List<SparseVector> movies) {
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

			//Calculate distances from center
			for(int i=0; i<user.size(); i++) {
				Entry<Integer,Double> rating = user.get(i);
				SparseVector m = movies.get(rating.getKey());
				m = multiply(m, rating.getValue());
				userAverageMovie = add(userAverageMovie, m);
			}
		}
	}
}
