import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class RandomVectors {
	
	//private static RandomVectorFunctions instance;
	private static RealVector[] randomVectors = new RealVector[Constants.getAmountOfRandomVectors()];
	
	private RandomVectors() {
		
	}
	
	public static RealVector getRandomVector(int index) {
		if (randomVectors[index] == null) {
			randomVectors[index] = createRandomVector(Constants.getDimensions());
		}
		return randomVectors[index];
	}
	
	/*
	private static RandomVectorFunctions getInstance() {
		if(instance == null) {
			instance = new RandomVectorFunctions();
		}
		return instance;
	}
	*/
	
	private static RealVector createRandomVector(int size){
		NormalDistribution nd = new NormalDistribution(1, 1);
		double[] values = nd.sample(size);
		
		RealVector r = new ArrayRealVector(values); //TODO: ARE THESE VALUES RIGHT
		return r;
	}
}