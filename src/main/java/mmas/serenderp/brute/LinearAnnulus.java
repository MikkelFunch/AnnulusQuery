package main.java.mmas.serenderp.brute;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.java.mmas.serenderp.Engine;
import main.java.mmas.serenderp.PreProcess;
import main.java.mmas.serenderp.util.SparseVector;

//Brute force implementation of annulus query.
//Uses on a tentative internal movie representation, since a global one does not exist.
public class LinearAnnulus {
	public static Collection<SparseVector> query(Collection<SparseVector> searchSpace, SparseVector q, double r, double w, double c, int n) {
		return query(searchSpace.stream(), q, r, w, c, n);
	}

	public static Collection<SparseVector> queryParallel(Collection<SparseVector> searchSpace, SparseVector q, double r, double w, double c, int n) {
		return query(searchSpace.stream().parallel(), q, r, w, c, n);
	}

	private static Collection<SparseVector> query(Stream<SparseVector> stream, SparseVector q, double r, final double w, double c, int n) {
		
		return stream.filter(m -> {
			double d = m.distanceTo(q);
			double wc = w * c;
			return (r / wc < d && d < r * wc);
		}).limit(n).collect(Collectors.toList());
	}


//	private static class Movie {
//		private String name;
//		private RealVector v;
//
//		public Movie(String n, RealVector v){
//			this.name=n;
//			this.v=v;
//		}
//
//		public RealVector getVector() {
//			return v;
//		}
//	}

	//poor man's unit test (more like idiot's (hey! (well what did you expect?)))
	public static void main(String[] args) {
//		Random r = new Random(42);
		Engine.setConstants();
		
		Map<String, SparseVector> map = PreProcess.getIMDBMovies();
//		for (int i = 0; i < 1_000_000; i++) {
//			RealVector rv = new ArrayRealVector();
//			for (int j = 0; j < 100; j++) {
//				rv = rv.append(r.nextDouble());
//			}
//			Movie m = new Movie(new Integer(r.nextInt()).toString(), rv);
//			movies.add(m);
//		}
		
		SparseVector q = map.get("Toy Story (1995)");
		map.remove("Toy Story (1995)");
		Collection<SparseVector> movies = map.values();
		
		Long startTime = System.currentTimeMillis();

		Collection<SparseVector> result = query(movies, q, 3, 2, 1.4d, 1);
		
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("Query time duration: %d sec", (duration / 1000)));

		
		
		if (result.isEmpty()) {
			System.out.println("No result was found");
		} else {
			for (SparseVector sparseVector : result) {
				System.out.println("Movie found: " + sparseVector.getMovieTitle());
			}
//			System.out.println(String.format("The movie \"%s\" was found as serendipitous", result.get(0).getMovieTitle()));
//			for (int i : result.iterator()) {
//				System.out.println(PreProcess.getFromGlobalIndex(i));
//			}
		}
		System.out.println("Done");
		//Collection<SparseVector> results = query(movies, movies.get(r.nextInt(movies.size())), Math.sqrt(11), Math.sqrt(12));
		//System.out.println(results.size());
	}
}
