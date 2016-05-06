import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.SparseVector;

public class Engine {

	public static void main(String[] args) {
		setConstants();
		//		Map<Integer, SparseVector> imdMovsByMlId = Constants.getMovies();
		//		System.out.println("loaded movies");
		//		List<List<Entry<Integer,Double>>>  users = MovieLensReader.loadUserRatings();
		//		System.out.println("loaded ratings");
		//		Magic.assessMagic(users, imdMovsByMlId);


		//PRE PROCESS
		Long startTime = System.currentTimeMillis();
		Map<String, SparseVector> movies = PreProcess.getIMDBMovies();
		Long endTime = System.currentTimeMillis();
		Long duration = (endTime - startTime);
		System.out.println(String.format("Pre process duration: %d sec", (duration / 1000)));

		//Test data
		SparseVector q = movies.get("Toy Story (1995)");
		movies.remove("Toy Story (1995)");

		//DATA STRUCTURE
		startTime = System.currentTimeMillis();
		Bucket[] buckets = buildQueryStructure(movies);
		endTime = System.currentTimeMillis();
		System.out.println(String.format("Build data structure duration: %d sec", (duration / 1000)));

		//QUERY
		startTime = System.currentTimeMillis();
		SparseVector result = query(buckets, q);
		endTime = System.currentTimeMillis();
		System.out.println("Query time duration: " + duration);


		for (int i : result.getMap().keySet()) {
			System.out.println(PreProcess.getFromGlobalIndex(i));
		}
		System.out.println("Done");
	}

	public static Bucket[] buildQueryStructure(Map<String, SparseVector> movies) {
		init();

		//Pre process
		Bucket[] buckets = new Bucket[Constants.getDimensions()]; //TODO: Bucket amounts
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = new Bucket();
		}

		//For each point
//		Long startTime = System.currentTimeMillis();
//		int count = 0;
		for (SparseVector sv : movies.values()) {
			if (!sv.hasActors()) {
				continue;
			}
			for (int hashFunctionIndex = 0; hashFunctionIndex < Constants.getNumberOfHashFunctions(); hashFunctionIndex++) {
				Bucket bucket = Bucket.getBucket(MinHashing.minHash(sv, hashFunctionIndex), hashFunctionIndex);
				bucket.add(sv);
			}
//			count++;
//			if (count % 500 == 0) {
//				Long endTime = System.currentTimeMillis();
//				Long duration = (endTime - startTime);
//				System.out.println(count + " - Duration: " + (duration / 1000));
//				startTime = System.currentTimeMillis();
//			}
		}
		return buckets;
	}

	public static SparseVector query(Bucket[] queryStructure, SparseVector q) {
		PriorityQueue<Quad> pq = new PriorityQueue<>();

		//Fill pq
		for (Bucket bucket : queryStructure) {
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				SparseVector v = bucket.poll(i).getRight();
				double priorityValue = SparseVector.dotProduct(SparseVector.subtract(v, q), RandomVectors.getRandomVector(i));
				pq.add(new Quad(priorityValue, v, bucket.getList(i), i));
			}
		}
		for(Bucket bucket : queryStructure) {
			bucket.sortLists();
		}

		int r = Constants.getR();
		int w = Constants.getW();

		double value;
		SparseVector result = null;
		Pair<Double, SparseVector> nextToPq;
		do{
			//ADD NEXT ELEMENT TO pq
			Quad next = pq.poll();
			if (next == null) {
				result = null;
				break;
			}
			value = next.getDotProduct();
			result = next.getVector();
			nextToPq = next.getSortedLinkedList().poll();
			if (nextToPq != null) {
				int vectorIndex = next.getRandomVectorIndex();
				double priorityValue = SparseVector.dotProduct(SparseVector.subtract(q, next.getVector()), RandomVectors.getRandomVector(vectorIndex));
				pq.add(new Quad(priorityValue, nextToPq.getRight(), next.getSortedLinkedList(), vectorIndex));
			}
		} while(!(r/w < value && value < r*w));

		return result;
	}

	private static void init() {
		setConstants();
		MinHashing.init();
	}

	private static void setConstants(){
		Constants.setAmountOfRandomVectors(5);
		Constants.setR(1/2);
		Constants.setW(2);
		Constants.setDimensions(3_649_941+2);
		Constants.setNumberOfHashFunctions(5);
	}
}
