package main.java.mmas.serenderp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.mmas.serenderp.util.Bucket;
import main.java.mmas.serenderp.util.SparseVector;
import static main.java.mmas.serenderp.Constants.*;

public class Buckets implements Iterable<Bucket> {
	// Map from band index to band hash sequence to bucket
	private Map<Integer, Map<List<Integer>, Bucket>> buckets = new HashMap<Integer, Map<List<Integer>, Bucket>>();

	public Bucket getBucket(int bandIndex, List<Integer> bandHashSequence) {
		Map<List<Integer>, Bucket> hashSequenceToBucketMap = buckets.get(bandIndex);

		// If the map has not been initialized, do so and put the map into
		// buckets
		if (hashSequenceToBucketMap == null) {
			hashSequenceToBucketMap = new HashMap<List<Integer>, Bucket>();
			buckets.put(bandIndex, hashSequenceToBucketMap);
		}

		// If the bucket has not been initialized, do so and put the bucket into
		// the map
		Bucket bucket = hashSequenceToBucketMap.get(bandHashSequence);
		if (bucket == null) {
			bucket = new Bucket();
			hashSequenceToBucketMap.put(bandHashSequence, bucket);
		}

		return bucket;
	}

	/**
	 * Adds a vector to a bucket
	 * @param bandIndex The band used to compute the hash sequence
	 * @param bandHashSequence The hash sequence computed by the bands
	 * @param vector The vector to store in the bucket
	 */
	public void add(int bandIndex, List<Integer> bandHashSequence, SparseVector vector) {
		getBucket(bandIndex, bandHashSequence).add(vector);
	}

	public Iterator<Bucket> iterator() {
		LinkedList<Bucket> bucketList = new LinkedList<Bucket>();
		for (Map<List<Integer>, Bucket> bucketsForHash : buckets.values()) {
			bucketList.addAll(bucketsForHash.values());
		}
		return bucketList.iterator();
	}

	private Set<List<Integer>> getBandSequences(int bandIndex) {
		return buckets.get(bandIndex).keySet();
	}

	public void persist(int bandIndex) {
		for (List<Integer> bandSequence : getBandSequences(bandIndex)) {
			File file = getFileName(bandIndex, bandSequence);
			file.getParentFile().mkdirs();
			try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
				out.writeObject(getBucket(bandIndex, bandSequence));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(13);
			}
		}
		buckets = new HashMap<>();
	}

	public static Bucket getBucketMemory(int bandIndex, List<Integer> bandSequence) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(getFileName(bandIndex, bandSequence)))) {
			return (Bucket) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(13);
			return null;
		}
	}

	private static File getFileName(int bandIndex, List<Integer> bandSequence) {
		return Paths.get("bucket", "numberofbands-" + NUMBER_OF_BANDS + "-hashesperband-" + HASH_FUNCTIONS_PER_BAND,"randomvector-" + AMOUNT_OF_RANDOM_VECTORS , "bandindex-" + bandIndex, "bandsequence-" + bandSequence).toFile();
//		return new File("buckets" + File.separator + "numberofbands-" + NUMBER_OF_BANDS + "hashesperband-" + HASH_FUNCTIONS_PER_BAND + File.separator + "bandindex-" + bandIndex + File.separator + "bandsequence-" + bandSequence);
	}
}
