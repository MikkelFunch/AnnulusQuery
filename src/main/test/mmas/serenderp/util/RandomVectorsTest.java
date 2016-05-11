package main.test.mmas.serenderp.util;

import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.RandomVectors;

public class RandomVectorsTest {
	
	@Test
	public void testOnTheFlyCreationIsConsistent() {
		final double delta = 1E-9;
		Constants.setAmountOfRandomVectors(2);
		Constants.setDimensions(3_649_941);
		
		RealVector v1 = RandomVectors.getGeneratedRandomVector(0);
		RealVector v2 = RandomVectors.getGeneratedRandomVector(0);
		
		for(int i = 0; i < Constants.getDimensions(); i++) {
			Assert.assertEquals(v1.getEntry(i), v2.getEntry(i), delta);
		}
	}
}