package main.java.mmas.serenderp.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.java.mmas.serenderp.Constants;

public class MinHashing {
	private static int a[];
	private static int b[];
	private static int p = 2147483647;
	private static int m;
	
	public static void init(){
		Random r = new Random(1337);
		
		int numberOfHashFunctions = Constants.getNumberOfHashFunctions();
		a = new int[numberOfHashFunctions];
		b = new int[numberOfHashFunctions];
		m = Constants.getDimensions();
		
		for(int i = 0; i < numberOfHashFunctions; i++){
			a[i] = nextNonZeroInt(r) % p;
			b[i] = r.nextInt();
		}
	}

	private static int nextNonZeroInt(Random r) {
		int i = r.nextInt();
		if (i == 0) {
			i = nextNonZeroInt(r);
		}
		return i;
	}
	
	public static List<List<Integer>> minHash(SparseVector vector) {
		List<List<Integer>> bucketSignatures = new ArrayList<List<Integer>>();
		
		for(int band = 0; band < Constants.getNumberOfBands(); band++) {
			bucketSignatures.add(minHash(vector, band));
		}
		return bucketSignatures;
		
	}
	
	public static List<Integer> minHash(SparseVector vector, int bandIndex) {
		List<Integer> bandSignature = new ArrayList<Integer>();
		
		for(int hashFunction = 0; hashFunction < Constants.getHashFunctionsPerBand(); hashFunction++) {
			int hashValue = minHashWithHashFunction(vector, (bandIndex * Constants.getHashFunctionsPerBand()) + hashFunction);
			bandSignature.add(hashValue);
		}
		return bandSignature;
	}
	
	private static int minHashWithHashFunction(SparseVector sparseVector, int hashFunctionIndex) {
		int minIndex = Integer.MAX_VALUE;
		for(Map.Entry<Integer, Double> value : sparseVector.getNonZeroElements()) {
			int index = hash(hashFunctionIndex, value.getKey());
			if(index < minIndex) {
				minIndex = index;
			}
		}
		return minIndex;
	}
	
	private static int hash(int i, int x) {
		int result = (((a[i] * x) + b[i]) % p) % m;
		return result;
	}
}