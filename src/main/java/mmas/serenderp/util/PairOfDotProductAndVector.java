package main.java.mmas.serenderp.util;

import org.apache.commons.lang3.tuple.Pair;

public class PairOfDotProductAndVector extends Pair<Double, SparseVector> {
	
	private static final long serialVersionUID = 1641706246223715462L;
	private Double dotProduct;
	private SparseVector vector;
	
	private PairOfDotProductAndVector(Double dotProduct, SparseVector vector) {
		this.dotProduct = dotProduct;
		this.vector = vector;
	}

	@Override
	public SparseVector setValue(SparseVector value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Double getLeft() {
		return dotProduct;
	}

	@Override
	public SparseVector getRight() {
		return vector;
	}
	
	public static PairOfDotProductAndVector of(Double dotProduct, SparseVector vector) {
		return new PairOfDotProductAndVector(dotProduct, vector);
	}
	
	@Override
	public int compareTo(Pair<Double, SparseVector> other) {
		return this.getLeft().compareTo(other.getLeft());
	}
}