package main.java.mmas.serenderp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.Bucket;
import main.java.mmas.serenderp.util.MinHashing;
import main.java.mmas.serenderp.util.SparseVector;

public class Engine {

	public static void main(String[] args) {
		init();

		double c, r, w;
		c = Constants.getC();
		r = Constants.getR();
		w = Constants.getW();

		// PRE PROCESS
		Long startTime = System.currentTimeMillis();
		Map<String, SparseVector> movies = PreProcess.getIMDBMovies();
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("Pre process duration: %d sec", (duration / 1000)));

		// Test data
//		final String movieName = "Toy Story (1995)";
//		SparseVector q = movies.get(movieName);
//		movies.remove(movieName);

		// DATA STRUCTURE
		startTime = System.currentTimeMillis();
		Buckets buckets = buildQueryStructure(movies);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println(String.format("Build data structure duration: %d sec", (duration / 1000)));
		
		Scanner scanner = new Scanner(System.in);
		String movieName;
		SparseVector q;
		while(true) {
			System.out.println("What movie do you want to use as query point?");
			movieName = scanner.nextLine();
			
			if("exit".equals(movieName)) {
				break;
			}
			
			q = movies.get(movieName);
			System.out.println(q == null ? "Movie was not found" : "Movie was found");
			if(null == q) { 
				continue;
			}
			
			System.out.println("Enter value of c");
			c = scanner.nextDouble();
			System.out.println("Enter value of r");
			r = scanner.nextDouble();
			System.out.println("Enter value of w");
			w = scanner.nextDouble();
			// QUERY
			startTime = System.currentTimeMillis();
			List<SparseVector> result = query(buckets, c, r, w, q, 1);
			endTime = System.currentTimeMillis();
			duration = (endTime - startTime);

			System.out.println(String.format("Query time duration: %d sec", (duration / 1000)));

			if (result.isEmpty()) {
				System.out.println("No result was found");
			} else {
				System.out.println(String.format("The movie \"%s\" was found as serendipitous", result.get(0).getMovieTitle()));
				System.out.println(String.format("The distance between the query point and the serendipitous movie is: %d", result.get(0).distanceTo(q)));
//				for (int i : result.get(0).getMap().keySet()) {
//					System.out.println(PreProcess.getFromGlobalIndex(i));
//				}
			}
		}
		scanner.close();
	}

	private static Buckets buildQueryStructure(Map<String, SparseVector> movies) {
		Buckets buckets = new Buckets();

		// For each point
		for (SparseVector sv : movies.values()) {
			if (!sv.hasActors()) {
				continue;
			}
			
			List<List<Integer>> minHash = MinHashing.minHash(sv);
			for(int band = 0; band < Constants.getNumberOfBands(); band++) {
				buckets.add(band, minHash.get(band), sv);
			}
		}

		int largestBucketCount = 0;
		int count = 0;
		for (Bucket bucket : buckets) {
			if(largestBucketCount < bucket.getSize()) {
				largestBucketCount = bucket.getSize();
			}
			bucket.sortLists();
			if (bucket.getSize() > 40000) {
				System.out.println("Bucket count: " + bucket.getSize());
				count++;
			}
			
		}
		System.out.println("Big buckets: " + count);
		System.out.println(String.format("Largest bucket has %d elements", largestBucketCount));

		return buckets;
	}

	private static List<SparseVector> query(Buckets queryStructure, double c, double r, double w, SparseVector q, int n) {
		w *= c;

		PriorityQueue<Quad> pq = new PriorityQueue<>();
		// Fill pq
		
		for(int bandIndex = 0; bandIndex < Constants.getNumberOfBands(); bandIndex++) {
			Bucket bucket = queryStructure.getBucket(bandIndex, MinHashing.minHash(q, bandIndex));
//			System.out.println(String.format("Bucket has %d elements", bucket.getList(0).size()));
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				// NullPointerexception thrown here
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
		List<SparseVector> resultList = new ArrayList<>(n);
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
				if(++pointsEvaluated % 1000 == 0) {
					System.out.println(String.format("%d points evaluated", pointsEvaluated));
				}
			} while (!(r / w < distance && distance < r * w));
			resultList.add(tempResult);
		}
		
		//Check annulus correctness
		return resultList;
	}

	private static double calculatePriorityValue(SparseVector p, SparseVector q, int randomVectorIndex) {
		return SparseVector.dotProduct(p.subtract(q), RandomVectors.getRandomVector(randomVectorIndex));
	}

	private static void init() {
		setConstants();
		MinHashing.init();
	}

	public static void setConstants() {
		Constants.setAmountOfRandomVectors(5);
		Constants.setR(20);
		Constants.setW(1.85);
		Constants.setC(1.41);
		Constants.setDimensions(3_649_941);// + 2);
		Constants.setNumberOfBandsAndHashFunctionsPerBand(10, 2);
	}
}
