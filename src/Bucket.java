import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.mmas.serenderp.util.SparseVector;

import org.apache.commons.lang3.tuple.Pair;

public class Bucket {
	private List<LinkedList<Pair<Double, SparseVector>>> randomVectorDistances;
	private static Map<Integer, Map<Integer, Bucket>> buckets;

	public Bucket() {
		int amountOfRandomVectors = Constants.getAmountOfRandomVectors();
		randomVectorDistances = new ArrayList<LinkedList<Pair<Double, SparseVector>>>();
		for (int i = 0; i < amountOfRandomVectors; i++) {
			randomVectorDistances.add(new LinkedList<Pair<Double, SparseVector>>());
		}
	}
	
	public void add(SparseVector vector){
		for (int randomVectorIndex = 0; randomVectorIndex < Constants.getAmountOfRandomVectors(); randomVectorIndex++) {
			double dotProduct = SparseVector.dotProduct(vector, RandomVectors.getRandomVector(randomVectorIndex));
			randomVectorDistances.get(randomVectorIndex).add(PairOfDotProductAndVector.of(dotProduct, vector)); //TODO: performance in sorting
		}
	}
	
	public void sortLists() {
		for (LinkedList<Pair<Double, SparseVector>> linkedList : randomVectorDistances) {
			Collections.sort(linkedList);
		}
		for (LinkedList<Pair<Double, SparseVector>> linkedList : randomVectorDistances) {
			assert isSorted(linkedList);
		}
	}
	
	public SparseVector getHead(int i){
		return randomVectorDistances.get(i).peek().getRight();
	}

	public Pair<Double, SparseVector> poll(int i) {
		return randomVectorDistances.get(i).poll();
	}

	public LinkedList<Pair<Double, SparseVector>> getList(int i) {
		return randomVectorDistances.get(i);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(Iterable<T> iterable) {
		Iterator<T> iter = iterable.iterator();
		if (!iter.hasNext()) {
			return true;
		}
		T t = iter.next();
		while (iter.hasNext()) {
			T t2 = iter.next();
			if (t.compareTo(t2) > 0) {
				return false;
			}
			t = t2;
		}
		return true;
	}
	/**
	 * Class used for sorting on the dot product
	 * @author andreas
	 */
	private static class PairOfDotProductAndVector extends Pair<Double, SparseVector> {
		
		private static final long serialVersionUID = 1641706246223715462L;
		private Double dotProduct;
		private SparseVector vector;
		
		private PairOfDotProductAndVector(Double dotProduct, SparseVector vector) {
			this.dotProduct = dotProduct;
			this.vector = vector;
		}

		@Override
		public SparseVector setValue(SparseVector value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Double getLeft() {
			return dotProduct;
		}

		@Override
		public SparseVector getRight() {
			return vector;
		}
		
		public static Pair<Double, SparseVector> of(Double dotProduct, SparseVector vector) {
			return new PairOfDotProductAndVector(dotProduct, vector);
		}
		
		@Override
		public int compareTo(Pair<Double, SparseVector> other) {
			return this.getLeft().compareTo(other.getLeft());
		}
	}

	public static Bucket getBucket(int bucketIndex, int hashfunctionIndex) { 
		if(buckets == null) {
			buckets = new HashMap<Integer, Map<Integer, Bucket>>();
			for(int i = 0; i < Constants.getNumberOfHashFunctions(); i++) {
				buckets.put(i, new HashMap<Integer, Bucket>());
			}
		}
		
		Map<Integer, Bucket> bucketsOfHashfunction = buckets.get(hashfunctionIndex);
		Bucket bucket = bucketsOfHashfunction.get(bucketIndex);
		if(bucket == null) {
			bucket = new Bucket();
			bucketsOfHashfunction.put(bucketIndex, bucket);
		}
		
		return bucket;
	}
}
