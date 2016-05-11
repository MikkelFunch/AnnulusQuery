package main.java.mmas.serenderp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class RandomVectors {
	
	private static final String FILE_NAME = "RandomVector";
	
	//private static RandomVectorFunctions instance;
	private static RealVector[] randomVectors = new RealVector[Constants.getAmountOfRandomVectors()];
	
	private RandomVectors() {
		
	}
	
	public static RealVector getRandomVector(int index) {
		if (randomVectors[index] == null) {
			randomVectors[index] = createRandomVector(Constants.getDimensions());
		}
		return randomVectors[index];
	}
	
	/*
	private static RandomVectorFunctions getInstance() {
		if(instance == null) {
			instance = new RandomVectorFunctions();
		}
		return instance;
	}
	*/
	
	private static RealVector createRandomVector(int size){
		NormalDistribution nd = new NormalDistribution(1, 1);
		double[] values = nd.sample(size);
		
		RealVector r = new ArrayRealVector(values); //TODO: ARE THESE VALUES RIGHT
		return r;
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
}