package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.Constants.NUMBER_OF_BANDS;

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
		for (int i = 0; i < Constants.NUMBER_OF_BANDS; i++) {
			bandStartTime = System.currentTimeMillis();
			for (SparseVector sv : movies.values()) {
				if (!sv.hasActors() || sv.getNonZeroElements().length < 10) {
					continue;
				}
				List<Integer> minHash = MinHashing.minHash(sv, i);
				buckets.add(i, minHash, sv);
			}
			buckets.persist(i);
			bandEndTime = System.currentTimeMillis();
			System.out.println(String.format("Processed band %d in %d sec", i+1, (bandEndTime - bandStartTime)/1000));
		}
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Processing all bands took %d sec", (endTime - startTime)/1000));
		
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