import java.util.Map;
import java.util.Random;

import main.java.mmas.serenderp.util.SparseVector;

public class MinHashing {
	private static int a[];
	private static int b[];
	private static int p = 2147483647;
	private static int m;
	
	public static void init(){
		Random r = new Random();
		
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
	
	public static int minHash(SparseVector sparseVector, int hashFunctionIndex) {
		int minIndex = Integer.MAX_VALUE;
		for(Map.Entry<Integer, Double> value : sparseVector.getNonZeroElements()) {
			int index = hash(hashFunctionIndex, value.getKey());
			if(index < minIndex) {
				minIndex = index;
			}
		}
		return minIndex;
	}
	
//	public static int minHash(SparseVector sparseVector, int hashFunctionIndex) {
//		for(int result = 1; result<m; result++) {
//			if(sparseVector.get(hash(hashFunctionIndex, result)) > 0) return result;
//		}
//		//TODO: should never get here (but does)
//		//System.out.println("Vector had no non-zeros by a permutation:\n" + sparseVector);
//		return 0;
//	}
	
	private static int hash(int i, int x) {
		//int dim = Constants.getDimensions();
		int result = (((a[i] * x) + b[i]) % p) % m;
		return result;
	}
}
