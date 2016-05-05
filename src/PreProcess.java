import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.mmas.serenderp.util.SparseVector;

public class PreProcess {
	private static HashMap<String, SparseVector> IMDBmovies = new HashMap<>();
	private static ArrayList<String> indices = new ArrayList<>();

	private static String moviePattern = ".*?(?=\\(\\d{4}\\))\\(\\d{4}\\)";//".*\\d{4}\\)";
	private static Pattern pattern = Pattern.compile(moviePattern);
	private static String lastMovie = "";
	private static boolean ranBefore = false;

	private static void insertActorsActresses(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
			String line;// line = br.readLine();
			boolean added = true;
			int id = indices.size(); //TODO: Why?
			
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) { // No movie or actor
					continue;
				} else if (!line.startsWith("\t")) { // Actor with movie

					// check if previous actor was added to a movie, remove if
					// not
					if (!added) {
						indices.remove(id);
					}

					id = indices.size();
					try {
						indices.add(line.substring(0, line.indexOf("\t")));
					} catch (Exception e) {// ?
						new UnexpectedException("");
					}
					// add actor to movie
					String movieLine = line.substring(line.lastIndexOf("\t") + 1);
					if (insertActorToMovie(movieLine, id)) {
						added = true;
					}
				} else { // Just movie
					// add actor to movie
					String movieLine = line.substring(line.lastIndexOf("\t") + 1);
					if (insertActorToMovie(movieLine, id)) {
						added = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean insertActorToMovie(String movieLine, int id) {
		Matcher m = pattern.matcher(movieLine);
		if (m.find()) {
			String movie = m.group(0);
			//if (movie.startsWith("The")) {
				// Move "The" behind in the movie
				//movie = movie.substring(4, movie.length() - 7) + ", The " + movie.substring(movie.length() - 6);
			//}
			
			if (!(movie.equals(lastMovie))) {
				lastMovie = movie;
				if (IMDBmovies.containsKey(movie)) {
					SparseVector sv = IMDBmovies.get(movie);
					sv.addEntry(id, 1d);
					return true;
				}
			} else {
				return false;
			}
		}

		return false;
	}

	private static void insertActors() {
		insertActorsActresses("data/actors.list");
	}

	private static void insertActresses() {
		insertActorsActresses("data/actresses.list");
	}

	private static void insertGenres() {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/genres.txt")))) {
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
	 * 
	 * @return
	 */
	public static Map<String, SparseVector> getIMDBMovies() {
		if (ranBefore ) {
			return IMDBmovies;
		}
		insertGenres();

		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/imdb_movies.list")))) {
			String line;

			while ((line = br.readLine()) != null) {
				Matcher m = pattern.matcher(line);

				if (m.find()) {
					String movie = m.group(0);
					if (movie != lastMovie) {
						lastMovie = movie;
					}
					SparseVector sv = new SparseVector(Constants.getDimensions());
					IMDBmovies.put(movie, sv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		addGenresToMovies();
		insertActors();
		insertActresses();

		// Get ratings, 0 - 1
		ranBefore = true;
		return IMDBmovies;
	}

	private static void addGenresToMovies() {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/genres.txt")))) {
			String line;

			while ((line = br.readLine()) != null) {
				Matcher m = pattern.matcher(line);

				if (m.find()) {
					String movie = m.group(0);
					String genre = line.substring(line.lastIndexOf("\t") + 1);
					IMDBmovies.get(movie).addEntry(indices.indexOf(genre), 1d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static String getFromGlobalIndex(int i) {
		return indices.get(i);
	}
}
