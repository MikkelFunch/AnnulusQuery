package main.java.mmas.serenderp;

import java.util.HashMap;
import java.util.Map;

import main.java.mmas.serenderp.util.SparseVector;

public class Constants {
	private static int dimensions;
	private static int amountOfRandomVectors;
	private static double r, w, c;
	private static int numberOfHashFunctions;

	private static Map<Integer, SparseVector> movies;

	public static int getDimensions() {
		return dimensions;
	}

	public static void setDimensions(int dimensions) {
		Constants.dimensions = dimensions;
	}

	public static int getAmountOfRandomVectors() {
		return amountOfRandomVectors;
	}

	public static void setAmountOfRandomVectors(int amountOfRandomVectors) {
		Constants.amountOfRandomVectors = amountOfRandomVectors;
	}

	public static double getR() {
		return r;
	}

	public static void setR(double r) {
		Constants.r = r;
	}

	public static double getW() {
		return w;
	}

	public static void setW(double w) {
		Constants.w = w;
	}
	
	public static double getC(){
		return c;
	}
	
	public static void setC(double c) {
		Constants.c = c;
	}

	public static int getNumberOfHashFunctions() {
		return numberOfHashFunctions;
	}

	public static void setNumberOfHashFunctions(int i) {
		numberOfHashFunctions = i;
	}

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
