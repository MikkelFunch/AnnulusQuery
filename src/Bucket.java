import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.PairOfDotProductAndVector;
import main.java.mmas.serenderp.util.SparseVector;

public class Bucket {
	private List<LinkedList<PairOfDotProductAndVector>> randomVectorDistances;
	
	public Bucket() {
		int amountOfRandomVectors = Constants.getAmountOfRandomVectors();
		randomVectorDistances = new ArrayList<LinkedList<PairOfDotProductAndVector>>();
		for (int i = 0; i < amountOfRandomVectors; i++) {
			randomVectorDistances.add(new LinkedList<PairOfDotProductAndVector>());
		}
	}
	
	public void add(SparseVector vector){
		for (int randomVectorIndex = 0; randomVectorIndex < Constants.getAmountOfRandomVectors(); randomVectorIndex++) {
			double dotProduct = SparseVector.dotProduct(vector, RandomVectors.getRandomVector(randomVectorIndex));
			randomVectorDistances.get(randomVectorIndex).add(PairOfDotProductAndVector.of(dotProduct, vector));
		}
	}
	
	public void sortLists() {
		for (LinkedList<PairOfDotProductAndVector> linkedList : randomVectorDistances) {
			Collections.sort(linkedList);
		}
		for (LinkedList<PairOfDotProductAndVector> linkedList : randomVectorDistances) {
			assert isSorted(linkedList);
		}
	}
	
	public SparseVector getHead(int i){
		return randomVectorDistances.get(i).peek().getRight();
	}

	public Pair<Double, SparseVector> poll(int i) {
		return randomVectorDistances.get(i).poll();
	}

	public LinkedList<PairOfDotProductAndVector> getList(int i) {
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
}