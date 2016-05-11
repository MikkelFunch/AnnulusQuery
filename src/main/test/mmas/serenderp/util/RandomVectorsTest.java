package main.test.mmas.serenderp.util;

import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

import main.java.mmas.serenderp.RandomVectors;
import static main.java.mmas.serenderp.Constants.*;

public class RandomVectorsTest {
	
	@Test
	public void testOnTheFlyCreationIsConsistent() {
		final double delta = 1E-9;
		
		RealVector v1 = RandomVectors.getGeneratedRandomVector(0);
		RealVector v2 = RandomVectors.getGeneratedRandomVector(0);
		
		for(int i = 0; i < DIMENSIONS; i++) {
			Assert.assertEquals(v1.getEntry(i), v2.getEntry(i), delta);
		}
	}
}