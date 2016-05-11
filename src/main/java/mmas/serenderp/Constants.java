package main.java.mmas.serenderp;

import java.util.HashMap;
import java.util.Map;

import main.java.mmas.serenderp.util.SparseVector;

public class Constants {
	public static final int DIMENSIONS = 4_080_356;
	public static final int AMOUNT_OF_RANDOM_VECTORS = 4;
	public static final double R = 20, W = 1.85, C = 1.41;
	public static final int NUMBER_OF_BANDS = 2;
	public static final int HASH_FUNCTIONS_PER_BAND = 7;
	public static final int NUMBER_OF_HASH_FUNCTIONS = NUMBER_OF_BANDS * HASH_FUNCTIONS_PER_BAND;
	private static Map<Integer, SparseVector> movies;

	public static Map<Integer, SparseVector> getMovies() {
		if (movies == null) {
			movies = new HashMap<Integer, SparseVector>();

			Map<String, SparseVector> imdbMovies = PreProcess.getIMDBMovies();
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
