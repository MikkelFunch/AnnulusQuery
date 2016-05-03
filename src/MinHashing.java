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
		p = BigInteger.probablePrime(32, r);
		
		for (int i = 0; i < randomVectors; i++) {
			do {
				a[i] = r.nextInt();
			} while (a[i] != 0);
			b[i] = r.nextInt();
		}
	}

	public static int hash(int i, int x) {
		//int dim = Constants.getDimensions();
		
		int result = (((a[i] * x) + b[i]) % p.intValue()) % m;
		
		return result;
		
		// en.wikipedia.org/wiki/Universal_hashing
		// ((ax + b) mod p) mod m
		// a = random tal % p. må ikke være 0
		// b = random tal % p. må gerne være 0
		// p = random primtal over m
		// m = antal inputs
		
		// Vi skal blot gemme a+b for hver hashing funktion samt p som static
		
	}
}
