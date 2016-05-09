package main.java.mmas.serenderp.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.RandomVectors;

public class Bucket {
	private List<LinkedList<Pair<Double, SparseVector>>> randomVectorDistances;
	private int size;
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
		size++;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sortLists() {
		for (LinkedList<Pair<Double, SparseVector>> linkedList : randomVectorDistances) {
			//inverted comparator
			Comparator cmp = new Comparator<Pair<Double,SparseVector>>(){
				@Override
				public int compare(Pair<Double,SparseVector> p1, Pair<Double,SparseVector> p2) {
					return p2.compareTo(p1);
				}
			};
			Collections.sort(linkedList, cmp);
		}

	}

	public SparseVector getHead(int i){
		if(null == randomVectorDistances) {
			throw new RuntimeException("wtf?");
		}
		if(null == randomVectorDistances.get(i)) {
			throw new RuntimeException("wtf?");
		}
		if(null == randomVectorDistances.get(i).peek()) {
			return null;
		}
		return randomVectorDistances.get(i).peek().getRight();
	}

	public Pair<Double, SparseVector> poll(int i) {
		return randomVectorDistances.get(i).peek();
	}

	public LinkedList<Pair<Double, SparseVector>> getList(int i) {
		return randomVectorDistances.get(i);
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
	
	public int getSize(){
		return size;
	}
}
