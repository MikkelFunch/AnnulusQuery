package main.java.mmas.serenderp.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.DoubleUnaryOperator;

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
	public void addEntry(double d) {
		addEntry(nextAvailable,d);
	}

	//Overloaded add that takes an integer.
	//Take care if mixing use of the two add functions, as they move the
	//nextAvailable index.
	public void addEntry(int i, double d) {
		if (vector.containsKey(i)) {
			//this may be overkill
			return;
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
		return size;
	}

	public static double dotProduct(SparseVector a, SparseVector b) {
		Map.Entry<Integer,Double>[] typePar = new Map.Entry[0];
		Map.Entry<Integer,Double>[] vectorA = a.vector.entrySet().toArray(typePar);
		Map.Entry<Integer,Double>[] vectorB = b.vector.entrySet().toArray(typePar);

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

	public static double dotProduct(SparseVector a, RealVector b) {
		double dotProduct = 0d;
		for(int i : a.vector.keySet()){
			dotProduct += a.get(i) * b.getEntry(i);
		}
		return dotProduct;
	}

	public static SparseVector add(SparseVector a, SparseVector b) {
		return pairwiseOp(a,(x,y) -> x+y ,b);
	}

	public static SparseVector subtract(SparseVector a, SparseVector b) {
		return pairwiseOp(a,(x,y) -> x-y ,b);
	}

	public static SparseVector multiply(SparseVector v, double k) {
		return mapIgnoreDefault(v, (x) -> x*k);
	}

	public static SparseVector divide(SparseVector v, double k) {
		return mapIgnoreDefault(v, (x) -> x/k);
	}

	public static double distance(SparseVector v1, SparseVector v2) {
		SparseVector v = subtract(v1,v2);
		v = mapIgnoreDefault(v1, (x) -> Math.pow(x,2));
		double sumOfSquares = foldIgnoreDefault(v, (x,y) -> x+y, 0d);
		return Math.sqrt(sumOfSquares);
	}

	public static <A> A foldIgnoreDefault (SparseVector v, BiFunction<Double,A,A> f, A def) {
		Map.Entry<Integer,Double>[] typePar = new Map.Entry[0];
		Map.Entry<Integer,Double>[] vector = v.vector.entrySet().toArray(typePar);

		for(int i = 0; i < vector.length;  i++) {
			Map.Entry<Integer,Double> pair = vector[i];
			Double value = pair.getValue();

			def = f.apply(value, def);
		}
		return def;
	}

	public static SparseVector mapIgnoreDefault(SparseVector v, DoubleUnaryOperator f) {
		Map.Entry<Integer,Double>[] typePar = new Map.Entry[0];
		Map.Entry<Integer,Double>[] vector = v.vector.entrySet().toArray(typePar);

		SparseVector res = new SparseVector(v.size());

		int i = 0;

		while(i < vector.length) {
			Map.Entry<Integer,Double> pair = vector[i];
			int key = pair.getKey();
			double value = pair.getValue();
			res.addEntry(key, f.applyAsDouble(value));
			i++;
		}
		return res;
	}

	public static SparseVector pairwiseOp(SparseVector a, BinaryOperator<Double> f, SparseVector b) {
		SparseVector res = new SparseVector(a.size());
		for (int i = 0; i < b.size(); i++) {
			res.addEntry(f.apply(a.get(i), b.get(i)));
		}
		return res;
	}

	public Map<Integer,Double> getMap() {
		return vector;
	}
	
	public boolean hasActors() {
		return nextAvailable <= 34;
	}
}
