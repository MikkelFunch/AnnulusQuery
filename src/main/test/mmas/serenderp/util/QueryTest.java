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
	private static final int[] serendipitousMoviesToFind = { 1, 10 };
	private static final List<String> movieNames = loadTestMoviesFromFile();

	@BeforeClass
	public static void beforeClass() {
		allMovies = IMDBReader.getIMDBMovies();
	}

	@Test
	public void testAmountOfRandomVectors() {
		final int bands = 20, bandSize = 2;
		final int[] amountOfRandomVectors = { 50, 25 };//, 10, 5, 1 };

		SparseVector queryPoint;
		for (int moviesToFind : serendipitousMoviesToFind) {
			System.out.println(String.format("Querying for %d movies\n", moviesToFind));
			System.out.println("Amount of random vectors\tAverage number of points examined");
			for (int randomVectors : amountOfRandomVectors) {
				Constants.setParameters(bands, bandSize, randomVectors);
				double numberOfMovies = 0;
				double pointsExamined = 0;
				System.out.print(randomVectors);
				for (String movieName : movieNames) {
					queryPoint = allMovies.get(movieName);
					Assert.assertNotNull(queryPoint);
					Pair<List<SparseVector>, Integer> results = Engine.queryMemory(c, r, w, queryPoint, moviesToFind);
					if (results.getLeft().size() == moviesToFind) {
						pointsExamined += results.getRight();
						numberOfMovies++;
					}
				}
				System.out.println(String.format("\t%.2f", pointsExamined / numberOfMovies));
			}
			System.out.println();
		}
	}
	
//	@Test
//	public void testAmountOfBands() {
////		final int amountOfBands[] = { 100, 75, 50, 30, 20, 10, 5, 4, 2, 1 };
//		final int amountOfBands[] = { 1 };
//		final int bandSize = 2, randomVectors = 10;
//
//		System.out.println("Amount of bands");
//		printMovies();
//
//		for (String movieName : movieNames) { // TODO: Remove
//			SparseVector queryPoint = allMovies.get(movieName);
//			if (queryPoint == null) {
//				System.out.println(movieName);
//				continue;
//			}
//		}
//		for (int bands : amountOfBands) {
//			Constants.setParameters(bands, bandSize, randomVectors);
//			System.out.print(bands);
//			queryBands();
//		}
//	}

//	private void queryBands() {
//		SparseVector queryPoint;
//
//		double success = 0;
//
//		 int amountOfTestMovies = 200;
//		// ArrayList<String> works = new ArrayList<>();
//		//
//		 Random rand = new Random();
//		// movieNames = new String[amountOfTestMovies];
//		//
//		// while (works.size() < amountOfTestMovies) {
//		String[] allStrings = new String[allMovies.size()];
//		Collection<SparseVector> allMoviesStringsCollection = allMovies.values();
//		int i = 0;
//		for (SparseVector sv : allMoviesStringsCollection) {
//			allStrings[i] = sv.getMovieTitle();
//
//			i++;
//		}
//		
//
//		// String[] all = new String[allMovies.size()];
//		// for (int i = 0; i < all.length; i++) {
//		//
//		// all[i] =
//		// }
//		// Map.Entry[] entries = (Entry[]) allMovies.entrySet().toArray();
//		movieNames = new String[amountOfTestMovies];
//		 for (int j = 0; j < amountOfTestMovies; j++) {
//		 movieNames[j] = allStrings[rand.nextInt(allStrings.length)];
//		 }
//		//
//		// i = 0;
//		// movieNames = getTestMoviesFromFile();
//		 
//		 movieNames = loadTestMoviesFromFile();
//		 
//		for (String movieName : movieNames) {
//
//			queryPoint = allMovies.get(movieName);
//			if (queryPoint == null) {
//				System.out.println(movieName);
//				continue;
//			}
//			Assert.assertNotNull(queryPoint);
//			// System.out.println(movieName);
//			// try {
//			List<SparseVector> result = Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);
//			// linear
//			Collection<SparseVector> linearResult = LinearAnnulus.query(allMovies.values(), queryPoint, r, w, 1,
//					serendipitousMoviesToFind);
//			if (linearResult.size() == result.size()) {
//				success++;
//			} else {
//				System.out.print("");
//			}
//
//			// works.add(movieName);
//			// i++;
//			// } catch (Exception e) {
//			// System.out.print("");
//			// }
//			// }
//		}
//		try(  PrintWriter out = new PrintWriter( "data/testMovies" )  ){
//			for (String m : movieNames) {
//		    out.println( m );
//			
//		}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println("Success: " + (success / movieNames.length) * 100 + "%");
//		if ((success / movieNames.length) * 100 != 100) {
//			System.out.println();
//		}
//
//		System.out.println();
//	}

	private static List<String> loadTestMoviesFromFile() {
		ArrayList<String> result = new ArrayList<>();
		 try (BufferedReader br = new BufferedReader(new FileReader(new File("data/testmovies.txt")))) {
				String line;

				while ((line = br.readLine()) != null) {
					result.add(line);
				}
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

//	private void query() {
//		SparseVector queryPoint;
//		for (String movieName : movieNames) {
//			queryPoint = allMovies.get(movieName);
//			Assert.assertNotNull(queryPoint);
//			Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);
//		}
//		System.out.println();
//	}

	private void printMovies() {
		for (String movieName : movieNames) {
			System.out.print("\t" + movieName);
		}
		System.out.println();
	}
}