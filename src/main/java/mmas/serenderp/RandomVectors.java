package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.Constants.AMOUNT_OF_RANDOM_VECTORS;
import static main.java.mmas.serenderp.Constants.DIMENSIONS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;

import main.java.mmas.serenderp.util.MathTool;

public class RandomVectors {
	
	private static final String FILE_NAME = "RandomVector";
	
	private static RealVector[] randomVectors = new RealVector[AMOUNT_OF_RANDOM_VECTORS];
	private static int[] seeds = createSeeds();
	
	private RandomVectors() { /* Hide constructor */ }
	
	public static RealVector getRandomVector(int index) {
		if (randomVectors[index] == null) {
//			randomVectors[index] = createRandomVector(DIMENSIONS);
			randomVectors[index] = createRandomVector(DIMENSIONS, new JDKRandomGenerator(seeds[index]));
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
	
	private static void writeToFile(RealVector vector, int index) {
		File file = getFileName(index);
		file.getParentFile().mkdirs();
		try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
			fileWriter.write(String.valueOf(vector.getDimension()));
			fileWriter.write(System.lineSeparator());
			
			double[] values = ((ArrayRealVector) vector).toArray();
			
			for(double value : values) {
				fileWriter.write(String.valueOf(value));
				fileWriter.write(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static RealVector readFromFile(int index) {
		try (BufferedReader fileReader = new BufferedReader(new FileReader(getFileName(index)))) {
			
			int vectorSize = Integer.parseInt(fileReader.readLine());
			
			ArrayRealVector vector = new ArrayRealVector(vectorSize);
			
			for(int i = 0; ; i++) {
				String value = fileReader.readLine();
				if(null == value) {
					break;
				}
				vector.setEntry(i, Double.parseDouble(value));
			}
			
			return vector;
			
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	private static File getFileName(int index) {
		return new File("vectors" + File.separator + FILE_NAME + "_" + index);
	}
	
	public static void main(String[] args) {
//		RealVector v1 = getGeneratedRandomVector(0);
//		writeToFile(v1, 0);
		
		RealVector v1 = getGeneratedRandomVector(0);
		
		RealVector v2 = readFromFile(0);
		
		for(int i = 0; i < DIMENSIONS; i++) {
			assert v1.getEntry(i) == v2.getEntry(i);
		}
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