package main.java.mmas.serenderp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import main.java.mmas.serenderp.util.Bucket;
import main.java.mmas.serenderp.util.SparseVector;

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

	public Iterator<Bucket> iterator() {
		LinkedList<Bucket> bucketList = new LinkedList<Bucket>();
		for(Map<Integer, Bucket> bucketsForHash : buckets.values()) {
			bucketList.addAll(bucketsForHash.values());
		}
		return bucketList.iterator();
	}
	
	public int getAmountOfBuckets(){
		return buckets.size();
	}
}
