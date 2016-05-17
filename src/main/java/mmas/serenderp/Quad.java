package main.java.mmas.serenderp;
import java.util.ListIterator;

import org.apache.commons.lang3.tuple.Pair;

import main.java.mmas.serenderp.util.SparseVector;

public class Quad implements Comparable<Quad> {
	private double dotProduct;
	private SparseVector vector;
	private ListIterator<Pair<Double, SparseVector>> list;
	private int randomVectorIndex;
	
	public Quad(double d, SparseVector v, ListIterator<Pair<Double, SparseVector>> l, int i){
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
	
	public int getRandomVectorIndex(){
		return randomVectorIndex;
	}

	public ListIterator<Pair<Double,SparseVector>> getPredecessor(){
		return list;
	}

	@Override
	public int compareTo(Quad o) {
		return Double.compare(dotProduct, o.getDotProduct()) * -1;
	}
}
