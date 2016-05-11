package main.java.mmas.serenderp.util;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;


public class MathTool {
	private static double my = 3;
	private static double stdDiv = 1;
	private static double reductionFactor = my*2;
	private static NormalDistribution normalDistribution = new NormalDistribution(0, stdDiv);
	
	public static double[] normalDistribution(int n, RandomGenerator rng) {
		double[] values = new double[n];
		NormalDistribution nd = new NormalDistribution(rng, 0, stdDiv);
		
		for (int i = 0; i < n; i++) {
			double sample = (nd.sample() + my)/reductionFactor;
			if(sample < 0) sample = 0;
			if(sample > 1) sample = 1;
			values[i] = sample;
		}
		
		return values;
	}
	
	public static double normalDistribution() {
		double sample = (normalDistribution.sample() + my)/reductionFactor;
		if(sample < 0) sample = 0;
		if(sample > 1) sample = 1;
		return sample;
	}
}
