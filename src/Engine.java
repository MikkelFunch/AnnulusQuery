import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.SparseVector;

public class Engine {

	public static void main(String[] args) {
		setConstants();

		double c, r, w;
		c = Constants.getC();
		r = Constants.getR();
		w = Constants.getW();

		// Map<Integer, SparseVector> imdMovsByMlId = Constants.getMovies();
		// System.out.println("loaded movies");
		// List<List<Entry<Integer,Double>>> users =
		// MovieLensReader.loadUserRatings();
		// System.out.println("loaded ratings");
		// Magic.assessMagic(users, imdMovsByMlId);

		// System.exit(0);

		// PRE PROCESS
		Long startTime = System.currentTimeMillis();
		Map<String, SparseVector> movies = PreProcess.getIMDBMovies();
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("Pre process duration: %d sec", (duration / 1000)));

		// Test data
		SparseVector q = movies.get("Toy Story (1995)");
		movies.remove("Toy Story (1995)");

		// DATA STRUCTURE
		startTime = System.currentTimeMillis();
		Buckets buckets = buildQueryStructure(movies);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println(String.format("Build data structure duration: %d sec", (duration / 1000)));

		// QUERY
		startTime = System.currentTimeMillis();
		SparseVector result = query(buckets, c, r, w, q);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println(String.format("Query time duration: %d sec", (duration / 1000)));

		if (result == null) {
			System.out.println("No result was found");
		} else {
			System.out.println(String.format("The movie \"%s\" was found as serendipitous", result.getMovieTitle()));
			for (int i : result.getMap().keySet()) {
				System.out.println(PreProcess.getFromGlobalIndex(i));
			}
		}
		System.out.println("Done");
	}

	public static Buckets buildQueryStructure(Map<String, SparseVector> movies) {
		init();
		Buckets buckets = new Buckets();

		// For each point
		// Long startTime = System.currentTimeMillis();
		// int count = 0;
		for (SparseVector sv : movies.values()) {
			if (!sv.hasActors()) {
				continue;
			}
			for (int hashFunctionIndex = 0; hashFunctionIndex < Constants
					.getNumberOfHashFunctions(); hashFunctionIndex++) {
				buckets.add(MinHashing.minHash(sv, hashFunctionIndex), hashFunctionIndex, sv);
			}
			// count++;
			// if (count % 500 == 0) {
			// Long endTime = System.currentTimeMillis();
			// Long duration = (endTime - startTime);
			// System.out.println(count + " - Duration: " + (duration / 1000));
			// startTime = System.currentTimeMillis();
			// }
		}

		for (Bucket bucket : buckets) {
			bucket.sortLists();
		}

		return buckets;
	}

	public static SparseVector query(Buckets queryStructure, double c, double r, double w, SparseVector q) { // N
																										// movie
																										// recommendations
		w *= c;

		PriorityQueue<Quad> pq = new PriorityQueue<>();
		// Fill pq
		for (int hashIndex = 0; hashIndex < Constants.getNumberOfHashFunctions(); hashIndex++) {
			Bucket bucket = queryStructure.getBucket(MinHashing.minHash(q, hashIndex), hashIndex);
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				// NullPointerexception thrown here
				SparseVector p = bucket.getHead(i);
				double priorityValue = calculatePriorityValue(p, q, i);
				ListIterator<Pair<Double, SparseVector>> predLink = bucket.getList(i).listIterator(1);
				pq.add(new Quad(priorityValue, p, predLink, i));
			}
		}

		double distance;
		SparseVector result = null;
		do {
			if (pq.isEmpty()) {
				return null;
			}
			Quad currentPoint = pq.poll();
			distance = q.distanceTo(currentPoint.getVector());
			result = currentPoint.getVector();
			ListIterator<Pair<Double, SparseVector>> predLink = currentPoint.getPredecessor();
			if (predLink.hasNext()) {
				Pair<Double, SparseVector> next = predLink.next();
				int vectorIndex = currentPoint.getRandomVectorIndex();
				double priorityValue = calculatePriorityValue(next.getRight(), q, vectorIndex);
				pq.add(new Quad(priorityValue, next.getRight(), predLink, vectorIndex));
			}
		} while (!(r / w < distance && distance < r * w));

		return result;
	}

	private static double calculatePriorityValue(SparseVector p, SparseVector q, int randomVectorIndex) {
		return SparseVector.dotProduct(p.subtract(q), RandomVectors.getRandomVector(randomVectorIndex));
	}

	private static void init() {
		setConstants();
		MinHashing.init();
	}

	private static void setConstants() {
		Constants.setAmountOfRandomVectors(5);
		Constants.setR(3);
		Constants.setW(1.2);
		Constants.setC(1.4);
		Constants.setDimensions(3_649_941 + 2);
		Constants.setNumberOfHashFunctions(5);
	}
}
