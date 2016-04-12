import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.RealVector;

import main.java.mmas.serenderp.util.SortedLinkedList;

public class Quad implements Comparable<Quad> {
	private double dotProduct;
	private RealVector vector;
	private SortedLinkedList<Pair<Double, RealVector>> list;
	private int randomVectorIndex;
	
	public Quad(double d, RealVector v, SortedLinkedList<Pair<Double, RealVector>> l, int i){
		dotProduct = d;
		vector = v;
		list = l;
		randomVectorIndex = i;
	}
	
	public double getDotProduct(){
		return dotProduct;
	}
	
	public RealVector getVector(){
		return vector;
	}
	
	public SortedLinkedList<Pair<Double, RealVector>> getSortedLinkedList(){
		return list;
	}
	
	public int getRandomVectorIndex(){
		return randomVectorIndex;
	}
	
	@Override
	public int compareTo(Quad o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
