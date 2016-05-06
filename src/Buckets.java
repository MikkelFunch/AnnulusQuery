import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import main.java.mmas.serenderp.util.SparseVector;
import java.util.LinkedList;
import java.util.Iterator;

public class Buckets implements Iterable<Bucket> {
	private Map<Integer, Map<Integer, Bucket>> buckets;

	public Bucket getBucket(int bucketIndex, int hashfunctionIndex) { 
		if(buckets == null) {
			buckets = new HashMap<Integer, Map<Integer, Bucket>>();
			for(int i = 0; i < Constants.getNumberOfHashFunctions(); i++) {
				buckets.put(i, new HashMap<Integer, Bucket>());
			}
		}

		Map<Integer, Bucket> bucketsOfHashfunction = buckets.get(hashfunctionIndex);
		Bucket bucket = bucketsOfHashfunction.get(bucketIndex);
		if(bucket == null) {
			bucket = new Bucket();
			bucketsOfHashfunction.put(bucketIndex, bucket);
		}

		return bucket;
	}

	public void add(int bucketIndex, int hashfunctionIndex, SparseVector sv) {
		Bucket b = getBucket(bucketIndex, hashfunctionIndex);
		b.add(sv);
	}

	//public void forEach(Consumer<? super Bucket> action) {
	//	for(Map<Integer, Bucket> bucketsForHash : buckets.values()) {
	//		for(Bucket b : bucketsForHash.values()) {
	//			action.accept(b);
	//		}
	//	}
	//}

	public Iterator<Bucket> iterator() {
		LinkedList<Bucket> bucketList = new LinkedList();
		for(Map<Integer, Bucket> bucketsForHash : buckets.values()) {
			bucketList.addAll(bucketsForHash.values());
		}
		return bucketList.iterator();
	}
}
