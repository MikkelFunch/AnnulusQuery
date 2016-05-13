package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.Constants.AMOUNT_OF_RANDOM_VECTORS;
import static main.java.mmas.serenderp.Constants.DIMENSIONS;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import main.java.mmas.serenderp.util.MathTool;

public class RandomVectors {
	
	private static final RealVector[] randomVectors = new RealVector[AMOUNT_OF_RANDOM_VECTORS];
	private static final int[] seeds = createSeeds();
	
	private RandomVectors() { /* Hide constructor */ }
	
	public static RealVector getRandomVector(int index) {
		if (randomVectors[index] == null) {
			randomVectors[index] = getGeneratedRandomVector(index);
		}
		return randomVectors[index];
	}
	
	public static RealVector getGeneratedRandomVector(int index) {
		return createRandomVector(DIMENSIONS, new JDKRandomGenerator(seeds[index]));
	}
	
	private static RealVector createRandomVector(int size, RandomGenerator rng) {
		double[] values = MathTool.normalDistribution(size, rng);
		return new ArrayRealVector(values);
	}
	
	private static RealVector createRandomVector(int size) {
		double[] values = new double[size];
		for (int i = 0; i < size; i++){
			values[i] = MathTool.normalDistribution();
		}
		
		return new ArrayRealVector(values);
	}
	
	private static int[] createSeeds() {
		int[] seeds = new int[AMOUNT_OF_RANDOM_VECTORS];
		Random rng = new Random(7189234);
		for(int i = 0; i < AMOUNT_OF_RANDOM_VECTORS; i++) {
			seeds[i] = rng.nextInt();
		}
		return seeds;
	}
}