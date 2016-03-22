import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class TheClass {
	public static void main(String[] args) {
		int size = 15_000_000;
		int runs = 100_00;
		RealVector a = new ArrayRealVector(size);
		RealVector b = new ArrayRealVector(size);
		RealVector c = new ArrayRealVector(size);
		RealVector d = new ArrayRealVector(size);
		RealVector e = new ArrayRealVector(size);
		RealVector f = new ArrayRealVector(size);
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < runs; i++) {
			a.dotProduct(b);
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("Total time: " + time + "\nAverage time: " + time / runs);
	}
}