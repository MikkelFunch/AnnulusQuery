package main.java.mmas.serenderp.brute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import main.java.mmas.serenderp.IMDBReader;
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
		if (n == -1) {
			return stream.filter(m -> {
				double d = m.distanceTo(q);
				double wc = w * c;
				return (r / wc < d && d < r * wc);
			}).collect(Collectors.toList());
		} else {
			return stream.filter(m -> {
				double d = m.distanceTo(q);
				double wc = w * c;
				return (r / wc < d && d < r * wc);
			}).limit(n).collect(Collectors.toList());
		}
	}
	
	public static double percentageInAnnulus(Collection<SparseVector> movies,  SparseVector q, double r, double w, double c) {
		double wc = w*c;
		int pointsExamined = 0;
		int pointsWithinAnnulus = 0;

		for (SparseVector movie : movies) {
			pointsExamined++;
			
			double d = q.distanceTo(movie);
			
			if( r / wc < d && d < r * wc ) {
				pointsWithinAnnulus++;
			}
		}
		
		return (double)pointsWithinAnnulus/(double)pointsExamined;
	}
	
	public static ImmutablePair<List<SparseVector>, Integer> queryPointsExamined(Collection<SparseVector> movies,  SparseVector q, double r, double w, double c, int n) {
		double wc = w*c;
		int pointsExamined = 0;
		List<SparseVector> pointsFound = new ArrayList<SparseVector>();

		for (SparseVector movie : movies) {
			pointsExamined++;
			
			double d = q.distanceTo(movie);
			
			if( r / wc < d && d < r * wc ) {
				pointsFound.add(movie);
			} else {
				continue;
			}
			
			if(pointsFound.size() == n) {
				break;
			}
		}
		
		return new ImmutablePair<List<SparseVector>, Integer>(pointsFound, pointsExamined);
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
		
		// PRE PROCESS
		Long startTime = System.currentTimeMillis();
		
		Map<String, SparseVector> map = IMDBReader.getIMDBMovies();
		
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("Pre process duration: %d sec", (duration / 1000)));
//		for (int i = 0; i < 1_000_000; i++) {
//			RealVector rv = new ArrayRealVector();
//			for (int j = 0; j < 100; j++) {
//				rv = rv.append(r.nextDouble());
//			}
//			Movie m = new Movie(new Integer(r.nextInt()).toString(), rv);
//			movies.add(m);
//		}
		
		String s = "Zapruder (2000)";
		SparseVector q = map.get(s);
		map.remove(s);
		Collection<SparseVector> movies = map.values();
		
		startTime = System.currentTimeMillis();

		double percentageInAnnulus = LinearAnnulus.percentageInAnnulus(movies, q, 1.339, 1.025, 1);
		System.out.println("Percentage: " + percentageInAnnulus);
		
		String s2 = "Wine Tasting (2009)";
		q = map.get(s2);
		map.remove(s2);
		
		percentageInAnnulus = LinearAnnulus.percentageInAnnulus(movies, q, 1.339, 1.025, 1);
		System.out.println("Percentage: " + percentageInAnnulus);
//		
//		ImmutablePair<List<SparseVector>, Integer> queryPointsExamined = queryPointsExamined(movies, q, 1.339, 1.025, 1, Integer.MAX_VALUE);
//		System.out.println("Points examined " + queryPointsExamined.getRight());
//		//Collection<SparseVector> result = query(movies, q, 15d, 3d, 3d, Integer.MAX_VALUE);
//		
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println(String.format("Query time duration: %d sec", (duration / 1000)));

		
//		
//		if (result.isEmpty()) {
//			System.out.println("No result was found");
//		} else {
//			for (SparseVector sparseVector : result) {
//				System.out.println("Movie found: " + sparseVector.getMovieTitle() + " - With distance: " + q.distanceTo(sparseVector));
//			}
////			System.out.println(String.format("The movie \"%s\" was found as serendipitous", result.get(0).getMovieTitle()));
////			for (int i : result.iterator()) {
////				System.out.println(PreProcess.getFromGlobalIndex(i));
////			}
//		}
//		System.out.println("Done");
		//Collection<SparseVector> results = query(movies, movies.get(r.nextInt(movies.size())), Math.sqrt(11), Math.sqrt(12));
		//System.out.println(results.size());
	}
}
