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
			"Ernest Saves Christmas (1988)", "Ernest Goes to Camp (1987)",
			"Kickboxer (1989)", "Kickboxer 2: The Road Back (1991)",
			"Kill Bill: Vol. 1 (2003)", "Kill Bill: Vol. 2 (2004)",
			"Free Willy 2: The Adventure Home (1995)", "Free Willy 3: The Rescue (1997)",
			"Meet the Fockers (2004)", "Little Fockers (2010)",
			"The Cell (2000)", "The Cell 2 (2009)",
			"Lake Placid (1999)", "Lake Placid 2 (2007)",
			"Look Who's Talking Now (1993)", "Look Who's Talking Too (1990)"
	};
	
	public static void main(String[] args) {
		Map<String, SparseVector> movies = IMDBReader.getIMDBMovies();
		
		for(Entry<String, SparseVector> movie : movies.entrySet()) {
			movie.setValue(SparseVector.divide(movie.getValue(), movie.getValue().length()));
		}
		
		isInMovies(moviePairs, movies);
		System.out.println("The average distance of expected movie pairs was: " + suggestInnerAnnulusRadius(movies));
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
				System.out.println(SparseVector.distance(movies.get(movie), movies.get(moviePairs[i])));
				totalDistance += SparseVector.distance(movies.get(movie), movies.get(moviePairs[i]));
			}
		}
		
		return totalDistance / (((double)moviePairs.length)/2.0);
	}
}
