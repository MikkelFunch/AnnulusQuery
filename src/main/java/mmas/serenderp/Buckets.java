package main.java.mmas.serenderp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.java.mmas.serenderp.util.Bucket;
import main.java.mmas.serenderp.util.SparseVector;

public class Buckets implements Iterable<Bucket> {
	// Map from band index to band hash sequence to bucket
	private Map<Integer, Map<List<Integer>, Bucket>> buckets = new HashMap<Integer, Map<List<Integer>, Bucket>>();
	
	public Bucket getBucket(int bandIndex, List<Integer> bandHashSequence) {
		Map<List<Integer>, Bucket> hashSequenceToBucketMap = buckets.get(bandIndex);
		
		// If the map has not been initialized, do so and put the map into buckets
		if(hashSequenceToBucketMap == null) {
			hashSequenceToBucketMap = new HashMap<List<Integer>, Bucket>();
			buckets.put(bandIndex, hashSequenceToBucketMap);
		}
		
		// If the bucket has not been initialized, do so and put the bucket into the map
		Bucket bucket = hashSequenceToBucketMap.get(bandHashSequence);
		if(bucket == null) {
			bucket = new Bucket();
			hashSequenceToBucketMap.put(bandHashSequence, bucket);
		}
		
		return bucket;
	}

	public void add(int bandIndex, List<Integer> bandHashSequence, SparseVector vector) {
		getBucket(bandIndex, bandHashSequence).add(vector);
	}

	public Iterator<Bucket> iterator() {
		LinkedList<Bucket> bucketList = new LinkedList<Bucket>();
		for(Map<List<Integer>, Bucket> bucketsForHash : buckets.values()) {
			bucketList.addAll(bucketsForHash.values());
		}
		return bucketList.iterator();
	}
}
