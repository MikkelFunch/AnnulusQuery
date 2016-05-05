import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.SparseVector;

public class Engine {
	private static final double THRESHOLD = 0.3d;
	
	public static void main(String[] args) {
		//PreProcess.getMovieLensMovies();
	}

	public static Bucket[] buildQueryStructure() {
		init();

		//Pre process
		List<SparseVector> vectors = new ArrayList<>();

		Bucket[] buckets = new Bucket[Constants.getDimensions()];

		//For each point
		for (SparseVector sv : vectors) {
			for (int i = 0; i < Constants.getNumberOfHashFunctions(); i++) {
				for (int x = 0; x < Constants.getDimensions(); x++){
					if (sv.get(MinHashing.hash(i, x)) > THRESHOLD) {
						buckets[x].add(sv);
					}
				}
			}
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
		/*Constants.setAmountOfRandomVectors(amountOfRandomVectors);
		Constants.setDimensions(dimensions);
		Constants.setR(r);
		Constants.setW(w);*/
	}
}
