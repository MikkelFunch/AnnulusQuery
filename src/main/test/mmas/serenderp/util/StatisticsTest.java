package main.test.mmas.serenderp.util;

import static main.java.mmas.serenderp.util.SparseVector.subtract;
import static org.junit.Assert.assertEquals;
import main.java.mmas.serenderp.Statistics;
import main.java.mmas.serenderp.util.SparseVector;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsTest {
	@Test
	public void testSubtractOperatorVectors() {
		double[] doubles = new double[5];
		doubles[0] = 1.0;
		doubles[1] = 90.0;
		doubles[2] = 1.0;
		doubles[3] = 2.0;
		doubles[4] = 1.0;
		
		double calculateAverageWithoutOutliers = Statistics.getMeanWithoutOutliers(doubles);
		Assert.assertEquals(1.25, calculateAverageWithoutOutliers, 0.00001);
	}
}
