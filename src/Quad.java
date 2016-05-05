import org.apache.commons.lang3.tuple.Pair;
import main.java.mmas.serenderp.util.SortedLinkedList;
import main.java.mmas.serenderp.util.SparseVector;

public class Quad implements Comparable<Quad> {
	private double dotProduct;
	private SparseVector vector;
	private SortedLinkedList<Pair<Double, SparseVector>> list;
	private int randomVectorIndex;
	
	public Quad(double d, SparseVector v, SortedLinkedList<Pair<Double, SparseVector>> l, int i){
		dotProduct = d;
		vector = v;
		list = l;
		randomVectorIndex = i;
	}
	
	public double getDotProduct(){
		return dotProduct;
	}
	
	public SparseVector getVector(){
		return vector;
	}
	
	public SortedLinkedList<Pair<Double, SparseVector>> getSortedLinkedList(){
		return list;
	}
	
	public int getRandomVectorIndex(){
		return randomVectorIndex;
	}
	
	@Override
	public int compareTo(Quad o) {
		return Double.compare(dotProduct, o.getDotProduct());
	}
}
