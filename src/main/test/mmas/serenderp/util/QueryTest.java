package main.test.mmas.serenderp.util;

import java.util.Map;

import org.junit.Test;

import main.java.mmas.serenderp.Constants;
import main.java.mmas.serenderp.Engine;
import main.java.mmas.serenderp.IMDBReader;
import main.java.mmas.serenderp.util.SparseVector;

public class QueryTest {
	
	@Test
	public void testAmountOfRandomVectors() {
		Map<String, SparseVector> allMovies = IMDBReader.getIMDBMovies();
		final double c = 1, r = 1.339, w = 1.025;
		int serendipitousMoviesToFind = 1;
		final String movieName = "Cars (2006)";
		final SparseVector queryPoint = allMovies.get(movieName);
		
		final int bands = 20, bandSize = 1;
		final int[] amountOfRandomVectors = { 1, 5, 10, 25, 50, 100 };
		
		System.out.println("Amount of random vectors\tPoints evaluated");
		for(int randomVectors : amountOfRandomVectors) {
			Constants.setParameters(bands, bandSize, randomVectors);
//			System.out.println(String.format("Constants:\nBands: %d\nBand size: %d\nAmount of random vectors: %d", bands, bandSize, randomVectors));
			System.out.print(randomVectors + "\t");
			Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);
		}
	}
}