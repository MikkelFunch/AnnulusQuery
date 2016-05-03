import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.mmas.serenderp.util.SparseVector;

public class PreProcess {
	private static HashMap<String, SparseVector> movieLensmovies = new HashMap<>();
	private static HashMap<String, SparseVector> IMDBmovies = new HashMap<>();
	private static ArrayList<String> indices = new ArrayList<>();
	
	private static String moviePattern = ".*\\d{4}\\)";
    private static Pattern pattern = Pattern.compile(moviePattern);
    private static String lastMovie = "";
	
    /***
     * Get movielens movies
     * @return
     */
	/*public static List<SparseVector> getMovieLensMovies(){
		insertGenres();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File("textfiles/movielens_movies.csv")))) {
		    String line; line = br.readLine();
		    
		    while ((line = br.readLine()) != null) {
		    	String[] columns;
				columns = line.split(",");
				if (columns.length > 3) {
					for (int i = 2; i < columns.length - 1; i++) {
						if (columns[i].startsWith(" The")) {
							columns[1] += "," + columns[i];
						} else {
							columns[1] += columns[i];
						}
					}
					columns[2] = columns[columns.length - 1];
				}
				if(columns[1].startsWith("\"")){
					columns[1] = columns[1].substring(1, columns[1].length() - 1);
				}
		    	
		    	SparseVector sv = new SparseVector(99999); // SIZE?
		    	String[] genres = columns[2].split("\\|");
		    	for (String g : genres) {
		    		if (g != "(no genres listed)") {
		    			sv.add(indices.indexOf(g));
					}
				}
		    	
		    	movieLensmovies.put(columns[1], sv);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		insertActors();
		insertActresses();
		
		
		//Get movies
		//Get genres
		//Get Actors
		//Get ratings, 0 - 1
		//Global indexes
		return new ArrayList<SparseVector>(movieLensmovies.values());
	}*/
	
	private static void insertActorsActresses(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
		    String line;// line = br.readLine();
		    boolean added = true;
		    int id = indices.size();
		    
		    while ((line = br.readLine()) != null) {
		    	if(line.isEmpty()) { //No movie or actor
					continue;
				} else if (!line.startsWith("\t")) { //Actor with movie
		    		//check if previous actor was added to a movie, remove if not
					
		    		if (!added) {
						indices.remove(id);
					}
		    		
		    		id = indices.size();
		    		try{
		    		indices.add(line.substring(0, line.indexOf("\t")));
		    		} catch(Exception e){//?
		    			new UnexpectedException("");
		    		}
		    		
		    		//add actor to movie
		    		String movieLine = line.substring(line.lastIndexOf("\t") + 1);
		    		if(insertActorToMovie(movieLine, id)){
		    			added = true;
		    		}
				} else { //Just movie
					//add actor to movie
					String movieLine = line.substring(line.lastIndexOf("\t") + 1);
					if(insertActorToMovie(movieLine, id)){
		    			added = true;
		    		}
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean insertActorToMovie(String movieLine, int id){
		Matcher m = pattern.matcher(movieLine);
	    if (m.find()){
	    	String movie = m.group(0);
	    	if (movie.startsWith("The")) {
	    		//Move "The" behind in the movie
				movie = movie.substring(4, movie.length() - 7) + ", The " + movie.substring(movie.length() - 6);;
			}
	    	if (movie != lastMovie) {
				lastMovie = movie;
				if (movieLensmovies.containsKey(movie)) {
					SparseVector sv = movieLensmovies.get(movie);
					sv.add(id);
					return true;
				}
			} else {
				return false;
			}
	    }
	    
		return false;
	}

	private static void insertActors() {
		insertActorsActresses("textfiles/actors.list");
	}
	
	private static void insertActresses() {
		insertActorsActresses("textfiles/actresses.list");
	}
	
	private static void insertGenres() {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("textfiles/genres.txt")))) {
			String line;
		    while ((line = br.readLine()) != null) {
		       indices.add(line);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * Get imdb movies
	 * @return
	 */
	public static List<SparseVector> getIMDBMovies(){
		insertGenres();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File("textfiles/imdb_movies.csv")))) {
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	Matcher m = pattern.matcher(line);
		    	
		    	if (m.find()){
		    		String movie = m.group(0);
		    		if (movie != lastMovie) {
		    			lastMovie = movie;
		    		}
					SparseVector sv = new SparseVector(99999); // SIZE?
					movieLensmovies.put(movie, sv);
		    	}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//addgenres();
		insertActors();
		insertActresses();
		
		
		
		//Get movies
		//Get genres
		//Get Actors
		//Get ratings, 0 - 1
		//Global indexes
		return new ArrayList<SparseVector>(movieLensmovies.values());
	}
}
