import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.RealVector;

public class Bucket {
	private List<LinkedList<Pair<Double, RealVector>>> randomVectorDistances;
	
	public Bucket(int numberOfRandomVectors) {
		randomVectorDistances = new ArrayList<LinkedList<Pair<Double, RealVector>>>(numberOfRandomVectors);
	}
	
	public void add(RealVector vector){
		for (int randomVectorIndex = 0; randomVectorIndex < Constants.getAmountOfRandomVectors(); randomVectorIndex++) {
			double dotProduct = vector.dotProduct(RandomVectors.getRandomVector(randomVectorIndex));
			randomVectorDistances.get(randomVectorIndex).add(Pair.of(dotProduct, vector));
		}
	}
}