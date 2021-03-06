package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.Constants.AMOUNT_OF_RANDOM_VECTORS;
import static main.java.mmas.serenderp.Constants.NUMBER_OF_BANDS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.Bucket;
import main.java.mmas.serenderp.util.MinHashing;
import main.java.mmas.serenderp.util.SparseVector;

public class Engine {

	public static void main(String[] args) {
		Constants.setParameters(20, 2, 10);

		// Map<Integer, SparseVector> movies = PreProcess.getMovies();
		// System.out.println("Got movies");
		// List<List<Entry<Integer,Double>>> users =
		// MovieLensReader.loadUserRatings();
		// System.out.println("Got userratings");
		// Magic.intuitionPlots(users, movies);

		// PRE PROCESS
		Long startTime = System.currentTimeMillis();
		Map<String, SparseVector> movies = IMDBReader.getIMDBMovies();
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("IMDB reading duration: %d sec", (duration / 1000)));

		// Test data
		// final String movieName = "Toy Story (1995)";
		// SparseVector q = movies.get(movieName);
		// movies.remove(movieName);

		// DATA STRUCTURE
		// startTime = System.currentTimeMillis();
		// Buckets buckets = PreProcess.buildQueryStructure(movies);
		// endTime = System.currentTimeMillis();
		// duration = (endTime - startTime);
		// System.out.println(String.format("Build data structure duration: %d
		// sec", (duration / 1000)));
		//

		final int[] amountOfRandomVectors = { 10, 5, 1 };
		final int numbersOfBands = 20, hashFunctionsPerBand = 2;

		// DATA STRUCTURE MEMORY
		// startTime = System.currentTimeMillis();
		// PreProcess.buildQueryStructureMemory(movies);
		// endTime = System.currentTimeMillis();
		// duration = (endTime - startTime);
		// System.out.println(String.format("Build data structure duration: %d
		// sec", (duration / 1000)));

		// AMOUNT OF RANDOM VECTORS
		// final int[] amountOfRandomVectors = { 50, 25, 10, 5, 1 };
		// final int numbersOfBands = 5, hashFunctionsPerBand = 2;

		for (int randomVectors : amountOfRandomVectors) {
			Constants.setParameters(numbersOfBands, hashFunctionsPerBand, randomVectors);
			startTime = System.currentTimeMillis();
			PreProcess.buildQueryStructureMemory(movies);
			endTime = System.currentTimeMillis();
			duration = (endTime - startTime);
			System.out.println(String.format("Build data structure with %d random vectors took: %d sec", randomVectors,
					(duration / 1000)));
		}

		// consoleUi(null, movies);
		// consoleUi(buckets, movies);
		// Magic.testSuccessProbability(buckets, movies);

		//// PRE PROCESS
		// Long startTime = System.currentTimeMillis();
		// Map<String, SparseVector> movies = IMDBReader.getIMDBMovies();
		// Long endTime = System.currentTimeMillis();
		// Long duration = (endTime - startTime);
		// System.out.println(String.format("Pre process duration: %d sec",
		//// (duration / 1000)));

		//// Test data
		// //final String movieName = "Toy Story (1995)";
		// //SparseVector q = movies.get(movieName);
		// //movies.remove(movieName);

		//// DATA STRUCTURE
		// startTime = System.currentTimeMillis();
		// Buckets buckets = PreProcess.buildQueryStructure(movies);
		// endTime = System.currentTimeMillis();
		// duration = (endTime - startTime);
		// System.out.println(String.format("Build data structure duration: %d
		//// sec", (duration / 1000)));

		consoleUi(null, movies);
	}

	private static void consoleUi(Buckets buckets, Map<String, SparseVector> movies) {
		Scanner scanner = new Scanner(System.in);
		String movieName = null;
		SparseVector q;
		double r = 1, w = 1, c = 1;
		while (true) {
			System.out.println("What movie do you want to use as query point?");
			String newMovieName = scanner.nextLine();
			if (!StringUtils.isEmpty(newMovieName)) {
				movieName = newMovieName;
			}

			if ("exit".equals(movieName)) {
				break;
			}

			q = movies.get(movieName);
			System.out.println(q == null ? "Movie was not found" : "Movie was found");
			if (null == q) {
				continue;
			}

			System.out.println("Enter value of c");
			String newC = scanner.nextLine();
			if (!StringUtils.isEmpty(newC)) {
				c = Double.parseDouble(newC);
			}
			System.out.println("Enter value of r");
			String newR = scanner.nextLine();
			if (!StringUtils.isEmpty(newR)) {
				r = Double.parseDouble(newR);
			}
			System.out.println("Enter value of w");
			String newW = scanner.nextLine();
			if (!StringUtils.isEmpty(newW)) {
				w = Double.parseDouble(newW);
			}
			// QUERY
			long startTime = System.currentTimeMillis();
			// List<SparseVector> result = query(buckets, c, r, w, q,
			// Integer.MAX_VALUE);
			List<SparseVector> result;
			result = queryMemory(c, r, w, q, Integer.MAX_VALUE).getLeft();
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime);

			System.out.println(String.format("Query time duration: %d sec", (duration / 1000)));

			if (result.isEmpty()) {
				System.out.println("No result was found");
			} else {
				Set<SparseVector> foundMovies = new HashSet<SparseVector>(result);
				for (SparseVector movie : foundMovies) {
					System.out.println(String.format(
							"The movie \"%s\" was found as serendipitous. The distance between the two movies are %f",
							movie.getMovieTitle(), movie.distanceTo(q)));
				}
				System.out.println(String.format("%d results was found", foundMovies.size()));
			}
			// Memory
		}
		scanner.close();
	}

	public static Pair<List<SparseVector>, Integer> queryMemory(double c, double r, double w, SparseVector q, int n) {
		w *= c;

		PriorityQueue<Quad> pq = new PriorityQueue<>();
		// Fill pq

		for (int bandIndex = 0; bandIndex < NUMBER_OF_BANDS; bandIndex++) {
			Bucket bucket = Buckets.getBucketMemory(bandIndex, MinHashing.minHash(q, bandIndex));
			if (bucket.getList(0).size() > 1) {
				// System.out.println("Number of movies in the same bucket was "
				// + bucket.getList(0).size());
			} else {
				continue;
			}
//			System.out.println(String.format("Bucket has %d elements", bucket.getList(0).size()));
			for (int i = 0; i < AMOUNT_OF_RANDOM_VECTORS; i++) {
				SparseVector p = bucket.getHead(i);
				if (p != null) {
					double priorityValue = calculatePriorityValue(p, q, i);
					ListIterator<Pair<Double, SparseVector>> predLink = bucket.getList(i).listIterator(1);
					pq.add(new Quad(priorityValue, p, predLink, i));
				}
			}
		}

		int pointsEvaluated = 0;

		double distance;
		SparseVector tempResult = null;
		Set<SparseVector> resultList = new HashSet<>();
		if (pq.isEmpty()) {
//			 System.out.println(String.format("PQ is empty before looking for results with query point being %s", q.getMovieTitle()));
		}
		search: while(resultList.size() < n) {
			do {
				if (pq.isEmpty()) {
					break search;
					// return resultList;
				}
				Quad currentPoint = pq.poll();
				tempResult = currentPoint.getVector();
				distance = q.distanceTo(tempResult);
				ListIterator<Pair<Double, SparseVector>> predLink = currentPoint.getPredecessor();
				if (predLink.hasNext()) {
					Pair<Double, SparseVector> next = predLink.next();
					int vectorIndex = currentPoint.getRandomVectorIndex();
					double priorityValue = calculatePriorityValue(next.getRight(), q, vectorIndex);
					pq.add(new Quad(priorityValue, next.getRight(), predLink, vectorIndex));
				}
				pointsEvaluated++;
				// if(++pointsEvaluated % 1000 == 0) {
				// System.out.println(String.format("%d points evaluated",
				// pointsEvaluated));
				// }
			} while (!(r / w < distance && distance < r * w));
			resultList.add(tempResult);
		}
//		System.out.print("\t" + pointsEvaluated);
		// Check annulus correctness
		
		return new ImmutablePair<List<SparseVector>, Integer>(new ArrayList<>(resultList), pointsEvaluated);
	}

	public static List<SparseVector> query(Buckets queryStructure, double c, double r, double w, SparseVector q,
			int n) {
		w *= c;

		PriorityQueue<Quad> pq = new PriorityQueue<>();
		// Fill pq

		for (int bandIndex = 0; bandIndex < NUMBER_OF_BANDS; bandIndex++) {
			Bucket bucket = queryStructure.getBucket(bandIndex, MinHashing.minHash(q, bandIndex));
			for (int i = 0; i < AMOUNT_OF_RANDOM_VECTORS; i++) {
				SparseVector p = bucket.getHead(i);
				if (p != null) {
					double priorityValue = calculatePriorityValue(p, q, i);
					ListIterator<Pair<Double, SparseVector>> predLink = bucket.getList(i).listIterator(1);
					pq.add(new Quad(priorityValue, p, predLink, i));
				}
			}
		}

		int pointsEvaluated = 0;

		double distance;
		SparseVector tempResult = null;
		List<SparseVector> resultList = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			do {
				if (pq.isEmpty()) {
					return resultList;
				}
				Quad currentPoint = pq.poll();
				tempResult = currentPoint.getVector();
				distance = q.distanceTo(tempResult);
				ListIterator<Pair<Double, SparseVector>> predLink = currentPoint.getPredecessor();
				if (predLink.hasNext()) {
					Pair<Double, SparseVector> next = predLink.next();
					int vectorIndex = currentPoint.getRandomVectorIndex();
					double priorityValue = calculatePriorityValue(next.getRight(), q, vectorIndex);
					pq.add(new Quad(priorityValue, next.getRight(), predLink, vectorIndex));
				}
				if (++pointsEvaluated % 1000 == 0) {
					System.out.println(String.format("%d points evaluated", pointsEvaluated));
				}
			} while (!(r / w < distance && distance < r * w));
			resultList.add(tempResult);
		}

		// Check annulus correctness
		return resultList;
	}

	private static double calculatePriorityValue(SparseVector p, SparseVector q, int randomVectorIndex) {
		return SparseVector.dotProduct(p.subtract(q), RandomVectors.getRandomVector(randomVectorIndex));
	}
}