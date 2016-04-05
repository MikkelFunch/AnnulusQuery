import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.RealVector;

import main.java.mmas.serenderp.util.SortedLinkedList;

public class Bucket {
	private List<SortedLinkedList<Pair<Double, RealVector>>> randomVectorDistances;
	//private SortedLinkedList<RealVector> 
	
	public Bucket(int numberOfRandomVectors) {
		randomVectorDistances = new ArrayList<SortedLinkedList<Pair<Double, RealVector>>>(numberOfRandomVectors);
		for (int i = 0; i < numberOfRandomVectors; i++) {
			randomVectorDistances.add(new SortedLinkedList<>());
		}
	}
	
	public void add(RealVector vector){
		for (int randomVectorIndex = 0; randomVectorIndex < Constants.getAmountOfRandomVectors(); randomVectorIndex++) {
			double dotProduct = vector.dotProduct(RandomVectors.getRandomVector(randomVectorIndex));
			randomVectorDistances.get(randomVectorIndex).add(Pair.of(dotProduct, vector));
		}
	}
	
	public RealVector getHead(int i){
		return randomVectorDistances.get(i).first().getRight();
	}
}