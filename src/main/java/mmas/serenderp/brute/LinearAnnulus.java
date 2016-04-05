package main.java.mmas.serenderp.brute;

import java.util.Random;

import java.util.Collection;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

//Brute force implementation of annulus query.
//Uses on a tentative internal movie representation, since a global one does not exist.
public class LinearAnnulus {
	public static Collection<Movie> query(Collection<Movie> searchSpace, Movie q, double innerRadius, double outerRadius) {
		return query(searchSpace.stream(), q, innerRadius, outerRadius);
	}

	public static Collection<Movie> queryParallel(Collection<Movie> searchSpace, Movie q, double innerRadius, double outerRadius) {
		return query(searchSpace.stream().parallel(), q, innerRadius, outerRadius);
	}

	private static Collection<Movie> query(Stream<Movie> searchSpace, Movie q, double innerRadius, double outerRadius) {
		return searchSpace.filter(m -> {
			double d = m.getVector().getDistance(q.getVector());
			return innerRadius <= d && d <= outerRadius;
		}).collect(Collectors.toList());
	}


	private static class Movie {
		private String name;
		private RealVector v;

		public Movie(String n, RealVector v){
			this.name=n;
			this.v=v;
		}

		public RealVector getVector() {
			return v;
		}
	}

	//poor man's unit test (more like idiot's (hey! (well what did you expect?)))
	public static void main(String[] args) {
		Random r = new Random(42);
		ArrayList<Movie> movies = new ArrayList<Movie>();
		for (int i = 0; i < 1_000_000; i++) {
			RealVector rv = new ArrayRealVector();
			for (int j = 0; j < 100; j++) {
				rv = rv.append(r.nextDouble());
			}
			Movie m = new Movie(new Integer(r.nextInt()).toString(), rv);
			movies.add(m);
		}

		Collection<Movie> results = query(movies, movies.get(r.nextInt(movies.size())), Math.sqrt(11), Math.sqrt(12));
		System.out.println(results.size());
	}
}
