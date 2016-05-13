package main.test.mmas.serenderp.util;

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
	private static final String[] movieNames = {
			"Cars (2006)",						"Titanic (1997)",
			"Toy Story (1995)",					"The Girl in Room 69 (1994)",
			"\"Mucho Gusto\" (2001)",			"\"Spy TV\" (2001)",
			"\"The Mighty B!\" (2008)",			"\"Kung ako'y iiwan mo\" (2012)",
			"\"Gumapang ka sa lusak\" (2010)",	"Cast Away (2000)",
			"\"Fox News Sunday\" (1996)",		"\"Zomergasten\" (1988)",
			"\"Eisai to tairi mou\" (2001)", 	"\"The Glen Campbell Music Show\" (1982)",
			"\"Bela ladja\" (2006)",			"Camino de Sacramento (1945)"
	};
	
	@BeforeClass
	public static void beforeClass() {
		allMovies = IMDBReader.getIMDBMovies();
	}
	
//	@Test
	public void testAmountOfRandomVectors() {
		final int bands = 20, bandSize = 1;
		final int[] amountOfRandomVectors = { 100, 50, 25, 10, 5, 1 };
		
		System.out.print("Amount of random vectors");
		printMovies();
		for(int randomVectors : amountOfRandomVectors) {
			Constants.setParameters(bands, bandSize, randomVectors);
			System.out.print(randomVectors);
			query();
		}
	}
	
	@Test
	public void testAmountOfBands(){
		final int amountOfBands[] = {50, 20, 10, 5, 2, 1};
		final int bandSize = 1, randomVectors = 10;
		
		System.out.println("Amound of bands");
		printMovies();
		for(int bands : amountOfBands) {
			Constants.setParameters(bands, bandSize, randomVectors);
			System.out.print(bands);
			query();
		}
	}
	
	private void query() {
		SparseVector queryPoint;
		for(String movieName : movieNames) {
			queryPoint = allMovies.get(movieName);
			if(queryPoint == null) {
				System.out.println(movieName);
				continue;
			}
			Assert.assertNotNull(queryPoint);
			List<SparseVector> result = Engine.queryMemory(c, r, w, queryPoint, serendipitousMoviesToFind);
			
			//linear
			Collection<SparseVector> linearResult = LinearAnnulus.query(allMovies.values(), queryPoint, Constants.R, Constants.W, 1, serendipitousMoviesToFind);
			if (result != null && linearResult != null) {
				System.out.print(linearResult.size() / result.size());
			}
		}
		System.out.println();
	}
	
	private void printMovies() {
		for(String movieName : movieNames) {
			System.out.print("\t" + movieName);
		}
		System.out.println();
	}
}