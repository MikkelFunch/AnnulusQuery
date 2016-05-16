package main.test.mmas.serenderp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.Engine;
import main.java.mmas.serenderp.IMDBReader;
import main.java.mmas.serenderp.brute.LinearAnnulus;
import main.java.mmas.serenderp.util.SparseVector;

public class QueryTest {
	private static Map<String, SparseVector> allMovies;
	private static final double c = 1, r = 1.339, w = 1.025;
	private static final int serendipitousMoviesToFind = 10;
	private static final List<String> movieNames = loadTestMoviesFromFile();

	@BeforeClass
	public static void beforeClass() {
		allMovies = IMDBReader.getIMDBMovies();
	}

	@Test
	public void testAmountOfRandomVectors() {
		final int bands = 5, bandSize = 2;
		final int[] amountOfRandomVectors = { 50, 25, 10, 5, 1 };

		System.out.println("Amount of random vectors");
		for (int randomVectors : amountOfRandomVectors) {
			Constants.setParameters(bands, bandSize, randomVectors);
			System.out.print(randomVectors);
			query();
		}
	}

	@Test
	public void testAmountOfBands() {
		// final int amountOfBands[] = { 60, 50, 40, 30, 20, 10, 5, 2, 1 };
		final int amountOfBands = 60;
		final int bandSize = 2, randomVectors = 10;

		System.out.println("Amount of bands");
		printMovies();

		// Test all bands
		List<Pair<Integer, Double>> resultList = new ArrayList<>(amountOfBands);
		for (int i = amountOfBands; i != 0; i--) {
			Constants.setParameters(i, bandSize, randomVectors);
			double result = queryBands();
			resultList.add(Pair.of(i, result));
			System.out.println("Finished band: " + i);
		}

		// Test band array
		// List<Pair<Integer, Double>> resultList = new
		// ArrayList<>(amountOfBands.length);
		// for (int band : amountOfBands) {
		// Constants.setParameters(band, bandSize, randomVectors);
		// double result = queryBands();
		// resultList.add(Pair.of(band, result));
		// System.out.println("Finished band: " + band);
		// }

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("bands\tsuccessrate");
		for (Pair<Integer, Double> pair : resultList) {
			System.out.println(pair.getLeft() + "\t" + pair.getRight());
		}
	}

	private double queryBands() {
		SparseVector queryPoint;

		double success = 0;

		for (String movieName : movieNames) {
			queryPoint = allMovies.get(movieName);
			Assert.assertNotNull(queryPoint);
			List<SparseVector> result = Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);

			// linear
			Collection<SparseVector> linearResult = LinearAnnulus.query(allMovies.values(), queryPoint, r, w, 1,
					serendipitousMoviesToFind);
			if (linearResult.size() == result.size()) {
				success++;
			}
		}

		System.out.println("Success: " + (success / movieNames.size()) * 100 + "%");
		System.out.println();

		return (success / movieNames.size()) * 100;
	}

	private static List<String> loadTestMoviesFromFile() {
		ArrayList<String> result = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/testmovies.txt")))) {
			String line;
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void query() {
		SparseVector queryPoint;
		for (String movieName : movieNames) {
			queryPoint = allMovies.get(movieName);
			if (queryPoint == null) {
				System.out.println(movieName);
				continue;
			}
			Assert.assertNotNull(queryPoint);
			Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);
		}
		System.out.println();
	}

	private void printMovies() {
		for (String movieName : movieNames) {
			System.out.print("\t" + movieName);
		}
		System.out.println();
	}
}