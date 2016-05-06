import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.PairOfDotProductAndVector;
import main.java.mmas.serenderp.util.SortedLinkedList;
import main.java.mmas.serenderp.util.SparseVector;

public class Bucket {
	private List<SortedLinkedList<Pair<Double, SparseVector>>> randomVectorDistances;
	//private SortedLinkedList<RealVector> 
	
	public Bucket() {
		int amountOfRandomVectors = Constants.getAmountOfRandomVectors();
		randomVectorDistances = new ArrayList<SortedLinkedList<Pair<Double, SparseVector>>>(amountOfRandomVectors);
		for (int i = 0; i < amountOfRandomVectors; i++) {
			randomVectorDistances.add(new SortedLinkedList<>());
		}
	}
	
	public void add(SparseVector vector){
		for (int randomVectorIndex = 0; randomVectorIndex < Constants.getAmountOfRandomVectors(); randomVectorIndex++) {
			double dotProduct = SparseVector.dotProduct(vector, RandomVectors.getRandomVector(randomVectorIndex));
			randomVectorDistances.get(randomVectorIndex).add(PairOfDotProductAndVector.of(dotProduct, vector));
		}
	}
	
	public SparseVector getHead(int i){
		return randomVectorDistances.get(i).first().getRight();
	}

	public Pair<Double, SparseVector> poll(int i) {
		return randomVectorDistances.get(i).poll();
	}

	public SortedLinkedList<Pair<Double, SparseVector>> getList(int i) {
		return randomVectorDistances.get(i);
	}
}