package main.java.mmas.serenderp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

public class RandomVectors {
	
	private static final String FILE_NAME = "RandomVector";
	
	private static RealVector[] randomVectors = new RealVector[Constants.getAmountOfRandomVectors()];
	private static RandomGenerator[] rngs = createRngs();
	
	private RandomVectors() { /* Hide constructor */ }
	
	public static RealVector getRandomVector(int index) {
		if (randomVectors[index] == null) {
			randomVectors[index] = createRandomVector(Constants.getDimensions());
		}
		return randomVectors[index];
	}
	
	public static RealVector getGeneratedRandomVector(int index) {
		return createRandomVector(Constants.getDimensions(), rngs[index]);
	}
	
	private static RealVector createRandomVector(int size, RandomGenerator rng) {
		NormalDistribution nd = new NormalDistribution(rng, 1, 1);
		return new ArrayRealVector(nd.sample(size));
	}
	
	private static RealVector createRandomVector(int size) {
		
		NormalDistribution nd = new NormalDistribution(1, 1);
		double[] values = nd.sample(size);
		
		return new ArrayRealVector(values); //TODO: ARE THESE VALUES RIGHT
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
		int runs = 20;
		int vectorSize = 3_649_941;
		long startTime, endTime;
		RealVector[] vectors = new RealVector[runs];
		
		startTime = System.currentTimeMillis();
		for(int i = 0; i < runs; i++) {
			vectors[i] = createRandomVector(vectorSize);
		}
		endTime = System.currentTimeMillis();
		System.out.println(String.format("Creating random vector with %d dimensions took % 5d ms", vectorSize, (endTime-startTime)/runs));
		
		for(int i = 0; i < runs; i++) {
			writeToFile(vectors[i], i);
		}
		
		startTime = System.currentTimeMillis();
		for(int i = 0; i < runs; i++) {
			readFromFile(i);
		}
		endTime = System.currentTimeMillis();
		System.out.println(String.format("Reading  random vector with %d dimensions took % 5d ms", vectorSize, (endTime-startTime)/runs));
		
	}
	
	private static RandomGenerator[] createRngs () {
			RandomGenerator[] rngs = new RandomGenerator[Constants.getAmountOfRandomVectors()];
			Random rng = new Random();
			for(int i = 0; i < Constants.getAmountOfRandomVectors(); i++) {
				rngs[i] = new JDKRandomGenerator(rng.nextInt());
			}
			return rngs;
	}
}