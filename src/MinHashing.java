import org.apache.commons.math3.linear.RealVector;

public class MinHashing {
	
	public static int hash(RealVector v) {
		int dim = Constants.getDimensions();
		
		// en.wikipedia.org/wiki/Universal_hashing
		// ((ax + b) mod p) mod m
		// a = random tal % p. må ikke være 0
		// b = random tal % p. må gerne være 0
		// p = random primtal over m
		// m = antal inputs
		
		// Vi skal blot gemme a+b for hver hashing funktion samt p som static
		
	}
}
