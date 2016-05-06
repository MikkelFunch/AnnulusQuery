import java.util.LinkedList;

import main.java.mmas.serenderp.util.PairOfDotProductAndVector;
import main.java.mmas.serenderp.util.SparseVector;

public class Quad implements Comparable<Quad> {
	private double dotProduct;
	private SparseVector vector;
	private LinkedList<PairOfDotProductAndVector> list;
	private int randomVectorIndex;
	
	public Quad(double d, SparseVector v, LinkedList<PairOfDotProductAndVector> l, int i){
		dotProduct = d;
		vector = v;
		list = l;
		randomVectorIndex = i;
	}
	
	public double getDotProduct(){
		return dotProduct;
	}
	
	public SparseVector getVector(){
		return vector;
	}
	
	public LinkedList<PairOfDotProductAndVector> getSortedLinkedList(){
		return list;
	}
	
	public int getRandomVectorIndex(){
		return randomVectorIndex;
	}
	
	@Override
	public int compareTo(Quad o) {
		return Double.compare(dotProduct, o.getDotProduct());
	}
}
