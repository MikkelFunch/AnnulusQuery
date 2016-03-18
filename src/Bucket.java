import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.RealVector;

public class Bucket {
	
	private List<LinkedList<Pair<Integer, RealVector>>> randomVectorDistances;
	
	public Bucket(int numberOfRandomVectors) {
		randomVectorDistances = new ArrayList<LinkedList<Pair<Integer, RealVector>>>(numberOfRandomVectors);
	}
	
}