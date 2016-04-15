package main.java.mmas.serenderp.util;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import org.apache.commons.math3.linear.RealVector;

public class SparseVector {
	private int size;
	private int nextAvailable=0;
	private Map<Integer,Double> vector;
	
	public SparseVector(int size) {
		this.size=size;
		this.vector = new TreeMap<Integer,Double>();
	}

	//add at last available index
	public void add(double d) {
		add(nextAvailable,d);
	}

	//Overloaded add that takes an integer.
	//Take care if mixing use of the two add functions, as they move the
	//nextAvailable index.
	public void add(int i, double d) {
		if (vector.containsKey(i)) {
			//this may be overkill
			throw new RuntimeException("vector overwrite not permitted");
		}
		if(nextAvailable >= size) {
			throw new RuntimeException("Vector size overrun");
		}
		nextAvailable = i+1;
		if (d == 0d) {
			return;
		}
		vector.put(i, d);
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

	public double dotProduct(SparseVector other) {
		Map.Entry<Integer,Double>[] typePar = new Map.Entry[0];
		Map.Entry<Integer,Double>[] vectorA = vector.entrySet().toArray(typePar);
		Map.Entry<Integer,Double>[] vectorB = other.vector.entrySet().toArray(typePar);

		double dotProduct = 0.0;
		int i = 0, j = 0;

		while(i < vectorA.length && j < vectorB.length) {
			Map.Entry<Integer,Double> pairA = vectorA[i];
			Map.Entry<Integer,Double> pairB = vectorB[j];

			int keyA = pairA.getKey(), keyB = pairB.getKey();
			double valueA = pairA.getValue(), valueB = pairB.getValue();
			if (keyA == keyB) {
				dotProduct += valueA * valueB;
				i++;
				j++;
			} else if (keyA < keyB) {
				i++;
			} else if (keyA > keyB) {
				j++;
			}
		}
		return dotProduct;
	}

	public double dotProduct(RealVector other) {
		double dotProduct = 0d;
		for(int i : vector.keySet()){
			dotProduct += get(i) * other.getEntry(i);
		}
		return dotProduct;
	}

	public Map<Integer,Double> getMap() {
		return vector;
	}
}
