package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.Constants.NUMBER_OF_BANDS;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.mmas.serenderp.util.Bucket;
import main.java.mmas.serenderp.util.MinHashing;
import main.java.mmas.serenderp.util.SparseVector;

public class PreProcess {

	private static Map<Integer, SparseVector> movies;

	public static Buckets buildQueryStructureMemory(Map<String, SparseVector> movies) {
		Buckets buckets = new Buckets();

		// For each point
		long startTime = System.currentTimeMillis();
		long bandStartTime, bandEndTime;
		for (int bandIndex = 0; bandIndex < Constants.NUMBER_OF_BANDS; bandIndex++) {
			File bandIndexFolder = Buckets.getFileName(bandIndex);
			if(bandIndexFolder.exists()) {
				System.out.println("Skipping " + bandIndexFolder.getAbsolutePath());
				continue;
			}
			
			bandStartTime = System.currentTimeMillis();
			for (SparseVector sv : movies.values()) {
				if (!sv.hasActors() || sv.getNonZeroElements().length < 10) {
					continue;
				}
				List<Integer> minHash = MinHashing.minHash(sv, bandIndex);
				buckets.add(bandIndex, minHash, sv);
			}
			buckets.persist(bandIndex);
			bandEndTime = System.currentTimeMillis();
			System.out.println(String.format("Processed band %d in %d ms", bandIndex+1, bandEndTime - bandStartTime));
		}
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Processing all bands took %d ms", endTime - startTime));
		
		/*
		int largestBucketCount = 0;
		int count = 0;
		for (Bucket bucket : buckets) {
			if (largestBucketCount < bucket.getSize()) {
				largestBucketCount = bucket.getSize();
			}
			bucket.sortLists();
			if (bucket.getSize() > 40000) {
				System.out.println("Bucket count: " + bucket.getSize());
				count++;
			}
		}
		System.out.println("Big buckets: " + count);
		System.out.println(String.format("Largest bucket has %d elements", largestBucketCount));
		*/
		return buckets;
	}

	public static Buckets buildQueryStructure(Map<String, SparseVector> movies) {
		Buckets buckets = new Buckets();

		// For each point
		for (SparseVector sv : movies.values()) {
			if (!sv.hasActors() || sv.getNonZeroElements().length < 10) {
				continue;
			}

			List<List<Integer>> minHash = MinHashing.minHash(sv);
			for (int band = 0; band < NUMBER_OF_BANDS; band++) {
				buckets.add(band, minHash.get(band), sv);
			}
		}

		int largestBucketCount = 0;
		int count = 0;
		for (Bucket bucket : buckets) {
			if (largestBucketCount < bucket.getSize()) {
				largestBucketCount = bucket.getSize();
			}
			bucket.sortLists();
			if (bucket.getSize() > 40000) {
				System.out.println("Bucket count: " + bucket.getSize());
				count++;
			}

		}
		System.out.println("Big buckets: " + count);
		System.out.println(String.format("Largest bucket has %d elements", largestBucketCount));

		return buckets;
	}

	public static Map<Integer, SparseVector> getMovies() {
		if (movies == null) {
			movies = new HashMap<Integer, SparseVector>();

			Map<String, SparseVector> imdbMovies = IMDBReader.getIMDBMovies();
			Map<Integer, String> movielensMovies = MovieLensReader.loadMovies();

			for (Map.Entry<Integer, String> movie : movielensMovies.entrySet()) {
				SparseVector vector = imdbMovies.get(movie.getValue());
				if (vector != null) {
					movies.put(movie.getKey(), vector);
				} else {
					System.out.println("Could not find movie with name: " + movie.getValue());
				}
			}
		}

		return movies;
	}
}