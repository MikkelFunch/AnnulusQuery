package main.java.mmas.serenderp.util;

import org.apache.commons.math3.distribution.NormalDistribution;


public class MathTool {
	private static double my = 3;
	private static double stdDiv = 1;
	private static double reductionFactor = my*2;
	private static NormalDistribution normalDistribution = new NormalDistribution(0, stdDiv); 
	
	public static double normalDistribution() {
		double sample = (normalDistribution.sample() + my)/reductionFactor;
		if(sample < 0) sample = 0;
		if(sample > 1) sample = 1;
		return sample;
	}
}
