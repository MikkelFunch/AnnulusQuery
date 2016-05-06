import java.math.BigInteger;
import java.util.Random;

public class MinHashing {
	private static int a[];
	private static int b[];
	private static BigInteger p;
	private static int m;
	
	public static void init(){
		Random r = new Random();
		
		int randomVectors = Constants.getAmountOfRandomVectors();
		a = new int[randomVectors];
		b = new int[randomVectors];
		m = Constants.getDimensions();
		p = BigInteger.probablePrime(32, r); //TODO: Bigger than m
		
		for (int i = 0; i < randomVectors; i++) {
			a[i] = r.nextInt(Integer.MAX_VALUE) + 1;
			b[i] = r.nextInt(Integer.MAX_VALUE);
		}
	}

	public static int hash(int i, int x) {
		int result = (((a[i] * x) + b[i]) % p.intValue()) % m;
		
		return result;
	}
}
