package main.java.mmas.serenderp;

import java.util.Map;
import java.util.Map.Entry;

import main.java.mmas.serenderp.util.SparseVector;

public class Experiments {
	static String[] moviePairs = new String[]{
			"Toy Story (1995)", "Toy Story 2 (1999)",
			"Die Hard (1988)", "Die Hard 2 (1990)",
			"The Evil Dead (1981)", "Evil Dead II (1987)",
			"Lethal Weapon (1987)", "Lethal Weapon 2 (1989)",
			"Next Friday (2000)", "Friday (1995)",
			"The Transporter (2002)", "Transporter 2 (2005)",
			"Shrek (2001)", "Shrek 2 (2004)",
			"22 Jump Street (2014)", "21 Jump Street (2012)",
			"Ernest Saves Christmas (1988)", "Ernest Goes to Camp (1987)"
	};
	
	public static void main(String[] args) {
		Map<String, SparseVector> movies = IMDBReader.getIMDBMovies();
		
		for(Entry<String, SparseVector> movie : movies.entrySet()) {
			movie.setValue(SparseVector.divide(movie.getValue(), movie.getValue().length()));
		}
		
		isInMovies(moviePairs, movies);
		System.out.println(suggestInnerAnnulusRadius(movies));
	}
	
	public static void isInMovies(String[] movies, Map<String, SparseVector> allMovies) {
		for(String movieName : movies) {
			if(!allMovies.containsKey(movieName)) {
				System.out.println(movieName + " is not in movies.");
			}
		}
	}
	
	public static double suggestInnerAnnulusRadius(Map<String, SparseVector> movies) {
		double totalDistance = 0.0;

		if(moviePairs.length%2 != 0) throw new IllegalStateException();
		
		String movie = null;
		for(int i = 0; i < moviePairs.length; i++) {
			if(i%2 == 0) {
				movie = moviePairs[i];
			} else {
				totalDistance += SparseVector.distance(movies.get(movie), movies.get(moviePairs[i]));
			}
		}
		
		return totalDistance / (((double)moviePairs.length)/2.0);
	}
}
