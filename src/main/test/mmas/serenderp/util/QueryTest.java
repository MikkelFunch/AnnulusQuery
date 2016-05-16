package main.test.mmas.serenderp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
//	private static final String[] movieNames = { "Cars (2006)", "Titanic (1997)", "Toy Story (1995)",
//			"The Girl in Room 69 (1994)", "\"Mucho Gusto\" (2001)", "\"Spy TV\" (2001)", "\"The Mighty B!\" (2008)",
//			"\"Kung ako'y iiwan mo\" (2012)", "\"Gumapang ka sa lusak\" (2010)", "Cast Away (2000)",
//			"\"Fox News Sunday\" (1996)", "\"Zomergasten\" (1988)", "\"Eisai to tairi mou\" (2001)",
//			"\"The Glen Campbell Music Show\" (1982)", "\"Bela ladja\" (2006)", "Camino de Sacramento (1945)" };
	private static final String[] movieNames = getTestMoviesFromFile();

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
		final int amountOfBands[] = { 100, 75, 50, 30, 20, 10, 5, 4, 2, 1 };
		final int bandSize = 2, randomVectors = 10;

		System.out.println("Amount of bands");
		printMovies();

		for (String movieName : movieNames) { // TODO: Remove
			SparseVector queryPoint = allMovies.get(movieName);
			if (queryPoint == null) {
				System.out.println(movieName);
				continue;
			}
		}
		for (int bands : amountOfBands) {
			Constants.setParameters(bands, bandSize, randomVectors);
			System.out.print(bands);
			queryBands();
		}
	}

	private void queryBands() {
		SparseVector queryPoint;

		double success = 0;

		// int amountOfTestMovies = 200;
		// ArrayList<String> works = new ArrayList<>();
		//
		// Random rand = new Random();
		// movieNames = new String[amountOfTestMovies];
		//
		// while (works.size() < amountOfTestMovies) {
		// String[] allStrings = new String[allMovies.size()];
		// Collection<SparseVector> allMoviesStringsCollection =
		// allMovies.values();
		// int i = 0;
		// for (SparseVector sv : allMoviesStringsCollection) {
		// allStrings[i] = sv.getMovieTitle();
		//
		// i++;
		// }

		// String[] resultsss = allMovies.values().toArray(new
		// String[allMovies.size()]);
		// String[] all = new String[allMovies.size()];
		// for (int i = 0; i < all.length; i++) {
		//
		// all[i] =
		// }
		// Map.Entry[] entries = (Entry[]) allMovies.entrySet().toArray();
		// for (int j = 0; j < amountOfTestMovies; j++) {
		// movieNames[j] = allStrings[rand.nextInt(allStrings.length)];
		// }
		//
		// i = 0;
		// movieNames = getTestMoviesFromFile();
		for (String movieName : movieNames) {

			queryPoint = allMovies.get(movieName);
			if (queryPoint == null) {
				System.out.println(movieName);
				continue;
			}
			Assert.assertNotNull(queryPoint);
			// System.out.println(movieName);
			// try {
			List<SparseVector> result = Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);
			// linear
			Collection<SparseVector> linearResult = LinearAnnulus.query(allMovies.values(), queryPoint, r, w, 1,
					serendipitousMoviesToFind);
			if (linearResult.size() == result.size()) {
				success++;
			} else {
				System.out.print("");
			}

			// works.add(movieName);
			// i++;
			// } catch (Exception e) {
			// System.out.print("");
			// }
			// }
		}

		System.out.println("Success: " + (success / movieNames.length) * 100 + "%");
		if ((success / movieNames.length) * 100 != 100) {
			System.out.println();
		}

		System.out.println();
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

	private static String[] getTestMoviesFromFile() {
		ArrayList<String> result = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/testmovies.txt")))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toArray(new String[result.size()]);
	}
}