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
		Buckets buckets = buildQueryStructure(movies);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println(String.format("Build data structure duration: %d sec", (duration / 1000)));

		//QUERY
		startTime = System.currentTimeMillis();
		SparseVector result = query(buckets, q);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println("Query time duration: " + duration);


		for (int i : result.getMap().keySet()) {
			System.out.println(PreProcess.getFromGlobalIndex(i));
		}
		System.out.println("Done");
	}

	public static Buckets buildQueryStructure(Map<String, SparseVector> movies) {
		init();
		Buckets buckets = new Buckets();

		//For each point
//		Long startTime = System.currentTimeMillis();
//		int count = 0;
		for (SparseVector sv : movies.values()) {
			if (!sv.hasActors()) {
				continue;
			}
			for (int hashFunctionIndex = 0; hashFunctionIndex < Constants.getNumberOfHashFunctions(); hashFunctionIndex++) {
				buckets.add(MinHashing.minHash(sv,hashFunctionIndex), hashFunctionIndex, sv);
			}
//			count++;
//			if (count % 500 == 0) {
//				Long endTime = System.currentTimeMillis();
//				Long duration = (endTime - startTime);
//				System.out.println(count + " - Duration: " + (duration / 1000));
//				startTime = System.currentTimeMillis();
//			}
		}
		
		for(Bucket bucket : buckets) {
			bucket.sortLists();
		}
		
		return buckets;
	}

	public static SparseVector query(Buckets queryStructure, SparseVector q) { //N movie recommendations
		PriorityQueue<Quad> pq = new PriorityQueue<>();
		//TODO: hash query point
		//Fill pq
		for (Bucket bucket : queryStructure) {
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				SparseVector p = bucket.poll(i).getRight();//TODO dont delete shit
				double priorityValue = calculatePriorityValue(p, q, i);
				pq.add(new Quad(priorityValue, p, bucket.getList(i), i)); //TODO add list index
			}
		}
		
		int r = Constants.getR(); //TODO: move, add c
		int w = Constants.getW();

		double distance;
		SparseVector result = null;
		Pair<Double, SparseVector> nextToPq;
		do{
			Quad next = pq.poll();
			if (next == null) {
				return null;
			}
			distance = q.distanceTo(next.getVector());
			result = next.getVector();
			nextToPq = next.getSortedLinkedList().poll();
			if (nextToPq != null) {
				int vectorIndex = next.getRandomVectorIndex();
				double priorityValue = calculatePriorityValue(next.getVector(), q, vectorIndex);
				pq.add(new Quad(priorityValue, nextToPq.getRight(), next.getSortedLinkedList(), vectorIndex));
			}
		} while(!(r/w < distance && distance < r*w)); //TODO: Approximate

		return result;
	}
	
	private static double calculatePriorityValue(SparseVector p, SparseVector q, int randomVectorIndex){
		return SparseVector.dotProduct(p.subtract(q), RandomVectors.getRandomVector(randomVectorIndex));
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
