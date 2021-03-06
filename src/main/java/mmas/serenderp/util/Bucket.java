package main.java.mmas.serenderp.util;

import static main.java.mmas.serenderp.Constants.AMOUNT_OF_RANDOM_VECTORS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.IMDBReader;
import main.java.mmas.serenderp.RandomVectors;

public class Bucket implements Serializable {

	private static final long serialVersionUID = -9014086210087898593L;
	// 
	private List<LinkedList<Pair<Double, SparseVector>>> randomVectorDistances;
	private int size;

	public Bucket() {
		int amountOfRandomVectors = AMOUNT_OF_RANDOM_VECTORS;
		randomVectorDistances = new ArrayList<LinkedList<Pair<Double, SparseVector>>>();
		for (int i = 0; i < amountOfRandomVectors; i++) {
			randomVectorDistances.add(new LinkedList<Pair<Double, SparseVector>>());
		}
	}
	
	public void add(SparseVector vector){
		for (int randomVectorIndex = 0; randomVectorIndex < AMOUNT_OF_RANDOM_VECTORS; randomVectorIndex++) {
			double dotProduct = SparseVector.dotProduct(vector, RandomVectors.getRandomVector(randomVectorIndex));
			randomVectorDistances.get(randomVectorIndex).add(PairOfDotProductAndVector.of(dotProduct, vector));
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
	
	public int getSize(){
		return size;
	}
	
//	public List<LinkedList<Pair<Double,SparseVector>>> getBucketContents(){
//		return randomVectorDistances;
//	}
	
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
		s.writeInt(size);
		s.writeObject(randomVectorDistances);
	}
	
    @SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
    	size = s.readInt();
    	randomVectorDistances = (List<LinkedList<Pair<Double, SparseVector>>>) s.readObject();
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

		@SuppressWarnings("unused") // Used by serializer
		public PairOfDotProductAndVector() { }

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
		
		private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
			s.writeDouble(dotProduct);
			s.writeInt(IMDBReader.getIndex(vector));
		}
		
	    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	    	dotProduct = s.readDouble();
	    	vector = IMDBReader.getSparseVector(s.readInt());
        }
	}
}
