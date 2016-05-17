package main.java.mmas.serenderp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Statistics {
	public static double getStdDev(double[] values) {
		DescriptiveStatistics ds = new DescriptiveStatistics(values);
		return ds.getStandardDeviation();
	}
	
	public static double getMean(double[] values) {
		DescriptiveStatistics ds = new DescriptiveStatistics(values);
		return ds.getPercentile(50.0);
	}
	
	public static double calculateAverageWithoutOutliers(double[] values) {
		double mean = getMean(values);
		double standardDeviation = getStdDev(values);
		double lowerOutlierBound = mean - standardDeviation;
		double upperOutlierBound = mean + standardDeviation;
		
		List<Double> nonOutliers = new ArrayList<>();
		for(double d : values) {
			if(d < upperOutlierBound) {
				nonOutliers.add(d);
			}
		}
		
		double[] result = new double[nonOutliers.size()];
		for(int i = 0; i < nonOutliers.size(); i++) {
			result[i] = nonOutliers.get(i);
		}
		
		return new DescriptiveStatistics(result).getMean();
	}
}
