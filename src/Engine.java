import java.util.PriorityQueue;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.lang3.tuple.Pair;

public class Engine {
	public static void main(String[] args) {
		setConstants();
		
		//Parse input
		
		/* Preprocess
		for each point
			hash to buckets
		*/
		Bucket[] buckets = null; //set
		
		
		
		
		
		/* Querying */
		RealVector q = null; //set
		
		PriorityQueue<Pair<Double, RealVector>> pq = new PriorityQueue<>(Constants.getAmountOfRandomVectors());
		
		//Fill pq
		for (Bucket bucket : buckets) {
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				RealVector v = bucket.getHead(i);
				double priorityValue = RandomVectors.getRandomVector(i).dotProduct(q.subtract(v));
				pq.add(Pair.of(priorityValue, v));
			}
		}
		
		int r = Constants.getR();
		int w = Constants.getW();
		double value; //Like this?
		RealVector result = null;
		do{
			//ADD NEXT ELEMENT TO pq
			Pair<Double, RealVector> next = pq.poll();
			if (next == null) {
				result = null;
				break;
			}
			value = next.getLeft();
			result = next.getRight();
		} while(value > r*w || value < r/w);

		
		
		/*
		int size = 15_000_000;
		int runs = 100_00;
		RealVector a = new ArrayRealVector(size);
		RealVector b = new ArrayRealVector(size);
		RealVector c = new ArrayRealVector(size);
		RealVector d = new ArrayRealVector(size);
		RealVector e = new ArrayRealVector(size);
		RealVector f = new ArrayRealVector(size);
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < runs; i++) {
			a.dotProduct(b);
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("Total time: " + time + "\nAverage time: " + time / runs);
		*/
	}
	
	private static void setConstants(){
		
	}
}