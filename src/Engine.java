import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.RealVector;

public class Engine {
	public static void main(String[] args) {
		setConstants();
		
		List<RealVector> vectors = new ArrayList<>();
		//Parse input
		
		/* Preprocess
		for each point
			hash to buckets
		*/
		for (RealVector v : vectors) {
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				
			}
		}
		
		Bucket[] buckets = null; //set
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/* Querying */
		RealVector q = null; //set
		
		PriorityQueue<Quad> pq = new PriorityQueue<>();
		
		//Fill pq
		for (Bucket bucket : buckets) {
			for (int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				RealVector v = bucket.poll(i).getRight();
				double priorityValue = RandomVectors.getRandomVector(i).dotProduct(q.subtract(v));
				pq.add(new Quad(priorityValue, v, bucket.getList(i), i));
			}
		}
		
		int r = Constants.getR();
		int w = Constants.getW();
		
		double value;
		RealVector result = null;
		Pair<Double, RealVector> nextToPq;
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
				double priorityValue = RandomVectors.getRandomVector(vectorIndex).dotProduct(q.subtract(next.getVector()));
				pq.add(new Quad(priorityValue, nextToPq.getRight(), next.getSortedLinkedList(), vectorIndex));
			}
		} while(!(r/w < value && value < r*w));

		
		
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