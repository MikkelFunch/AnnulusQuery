package main.test.mmas.serenderp.util;

import static org.junit.Assert.*;
import org.junit.Test;
import main.java.mmas.serenderp.util.SparseVector;
import static main.java.mmas.serenderp.util.SparseVector.*;
import java.util.Map;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

public class SparseVectorTest {

	@Test
	public void testConstructionIsSparse() {
		SparseVector a = new SparseVector(6);
		a.add(0);
		a.add(1);
		a.add(0);
		a.add(1);
		a.add(2);
		a.add(1);

		Map<Integer,Double> m = a.getMap();
		Integer[] keys = m.keySet().toArray(new Integer[0]);
		//has no keys for zero
		assertArrayEquals(new Integer[]{1,3,4,5}, keys);
	}

	@Test
	public void testConstructionKVPairs() {
		SparseVector a = new SparseVector(6);
		a.add(0);
		a.add(1);
		a.add(0);
		a.add(1);
		a.add(2);
		a.add(1);

		Map<Integer,Double> m = a.getMap();
		Double[] values = m.values().toArray(new Double[0]);
		assertArrayEquals(new Double[]{1d,1d,2d,1d}, values);
	}

	@Test
	public void testDotProductOperatorOrthogonal() {
		SparseVector a = new SparseVector(3);
		SparseVector b = new SparseVector(3);
		a.add(0);
		a.add(1);

		b.add(1);
		b.add(0);
		b.add(1);

		assertEquals("dp of orthogonal vectors is 0", 0, dotProduct(a,b), 0);
	}

	@Test
	public void testDotProductOperator() {
		SparseVector a = new SparseVector(4);
		SparseVector b = new SparseVector(4);
		a.add(2);
		a.add(100);
		a.add(1);
		a.add(1);

		b.add(1);
		b.add(0);
		b.add(1);

		assertEquals("dp of vectors is 3", 3, dotProduct(a,b), 0);
	}
	@Test
	public void testDotProductOperatorRealOrthogonal() {
		SparseVector a = new SparseVector(3);
		RealVector b = new ArrayRealVector(new double[]{1,0,1});
		a.add(0);
		a.add(1);

		assertEquals("dp of orthogonal vectors is 0", 0, dotProduct(a,b), 0);
	}

	@Test
	public void testDotProductRealOperator() {
		SparseVector a = new SparseVector(4);
		RealVector b = new ArrayRealVector(new double[]{1,0,1,0});
		a.add(2);
		a.add(100);
		a.add(1);
		a.add(1);


		assertEquals("dp of vectors is 3", 3, dotProduct(a,b), 0);
	}

	@Test
	public void testSubtractOperatorZeroVectors() {
		SparseVector a = new SparseVector(4);
		SparseVector b = new SparseVector(4);
		a.add(0);
		a.add(0);
		a.add(0);
		a.add(0);

		SparseVector sub = subtract(a,b);
		assertEquals(4,sub.size());

		for (int i = 0; i < 4; i++){
			assertEquals("vectorelem is zero", 0, sub.get(i), 0);
		}
	}

	@Test
	public void testSubtractOperatorVectors() {
		SparseVector a = new SparseVector(4);
		SparseVector b = new SparseVector(4);
		b.add(0);
		b.add(4);
		b.add(-4);
		b.add(0);

		SparseVector sub = subtract(a,b);
		double[] subarr = new double[]{0,-4,4,0};

		for (int i = 0; i < 4; i++){
			assertEquals("vectorelem is correct", subarr[i], sub.get(i), 0);
		}
	}
}
