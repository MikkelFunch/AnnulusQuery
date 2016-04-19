package main.java.mmas.serenderp.util;
import java.util.HashMap;

public class SparseVector {
	private int size;
	private int nextAvailable=0;
	private HashMap<Integer,Double> vector;
	
	public SparseVector(int size) {
		this.size=size;
		this.vector = new HashMap<Integer,Double>(size);
	}

	//add at last available index
	public void add(double d) {
		add(nextAvailable,d);
	}

	//Overloaded add that takes an integer.
	//Take care if mixing use of the two add functions, as they move the
	//nextAvailable index.
	public void add(int i, double d) {
		if (null == vector.get(i)) {
			//this may be overkill
			throw new RuntimeException("vector overwrite not permitted");
		}
		if (d == 0) {
			return;
		}
		vector.put(i, d);
		nextAvailable = i+1;
	}

	public double get(int i) {
		if(i >= size) {
			//this may be overkill
			throw new RuntimeException("Vector size overrun");
		}
		Double d = vector.get(i);
		return null == d ? 0 : d;
	}

	public int size() {
		return nextAvailable;
	}
}
