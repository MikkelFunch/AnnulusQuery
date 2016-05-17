package main.java.mmas.serenderp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Statistics {
	public static double getStdDev(double[] values) {
		DescriptiveStatistics ds = new DescriptiveStatistics(values);
		return ds.getStandardDeviation();
	}
	
	public static double get50Percentile(double[] values) {
		DescriptiveStatistics ds = new DescriptiveStatistics(values);
		return ds.getPercentile(50.0);
	}
	
	public static double getMean(double[] values) {
		DescriptiveStatistics ds = new DescriptiveStatistics(values);
		return ds.getMean();
	}
	
	public static double getMeanWithoutOutliers(double[] values) {
		double mean = get50Percentile(values);
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
	
	public static List<double[]> readValues(String file) {
		List<double[]> result = new ArrayList<>();
		
		List<Double> doubles = new ArrayList<>();
		Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    while (sc.hasNextLine()){
	    	
	    	String[] split = sc.nextLine().split("\t");
	    	double[] array = Arrays.stream(split, 1, split.length).mapToDouble(s -> Double.parseDouble(s)).toArray();
	    	result.add(array);
	    }
	    
	    return result;
	}
	
	public static void main(String[] args) {
		String file1 = "data/query001.csv"; 
		List<double[]> values = readValues(file1);
		for(double[] doubles : values) {
			System.out.println("no outliers: " + getMeanWithoutOutliers(doubles));
			System.out.println("mean: " + getMean(doubles));
			System.out.println("50 percentile: " + get50Percentile(doubles));
			System.out.println("std dev: " + getStdDev(doubles));
		}

		
//		String file2 = "data/query001.csv"; 
//		values = readValues(file2);
//		System.out.println(getMeanWithoutOutliers(values));
//		System.out.println(getMean(values));
//		System.out.println(get50Percentile(values));
	}
	
	
}
