package main.java.mmas.serenderp;

import static main.java.mmas.serenderp.Constants.DIMENSIONS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.mmas.serenderp.util.SparseVector;

public class IMDBReader {
	private static HashMap<String, SparseVector> IMDBmovies = new HashMap<>();
	private static ArrayList<String> indices = new ArrayList<>();

	private static String moviePattern = ".*?(?=\\(\\d{4}\\))\\(\\d{4}\\)";
	private static Pattern pattern = Pattern.compile(moviePattern);
	private static String lastMovie = "";
	private static boolean ranBefore = false;
	
	private static Map<Integer, SparseVector> indexToVectorMap;
	private static Map<SparseVector, Integer> vectorToIndexMap;

	private static void insertActorsActresses(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
			String line;
			boolean added = true;
			int id = indices.size();
			
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
					indices.add(line.substring(0, line.indexOf("\t")));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean insertActorToMovie(String movieLine, int id) {
		Matcher m = pattern.matcher(movieLine);
		if (m.find()) {
			String movie = m.group(0);

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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
//		indices.add("average rating");
//		indices.add("year");
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
					SparseVector sv = new SparseVector(DIMENSIONS, movie);
//					sv.addEntry(1, Double.parseDouble(movie.substring(movie.length() - 5, movie.length() - 1)));//Insert year //TODO: normalize year
					IMDBmovies.put(movie, sv);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		insertRatings();
		addGenresToMovies();
		insertActors();
		insertActresses();
		insertDirectors();

		// Get ratings, 0 - 1
		ranBefore = true;
		return IMDBmovies;
	}

	private static void insertDirectors() {
		insertActorsActresses("data/directors.list");
	}

	private static void insertRatings() {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/ratings.list")))) {
			Pattern ratingsPattern = Pattern.compile("\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(" + moviePattern + ")");
			
			String line;

			while ((line = br.readLine()) != null) {
				Matcher m = ratingsPattern.matcher(line);
				if (m.find()) {
					double rating = Double.parseDouble(m.group(3));
					String movie = m.group(4);
					SparseVector sv = IMDBmovies.get(movie);
					if (sv != null) {
						sv.addEntry(0, rating/2); //normalized between 0.5 and 10
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static String getFromGlobalIndex(int i) {
		return indices.get(i);
	}

	public static int getGlobalIndicesSize() {
		return indices.size();
	}
	
	public static int getIndex(SparseVector sv){
		if (vectorToIndexMap == null) {
			initIndexToVectorMaps();
		}
		Integer i = vectorToIndexMap.get(sv);
		if (i == null) {
			throw new RuntimeException("Map didn't contain vector. Should not happen");
		}
		return i;
	}
	
	public static SparseVector getSparseVector(int i){
		if (indexToVectorMap == null) {
			initIndexToVectorMaps();
		}
		SparseVector sv = indexToVectorMap.get(i);
		if (sv == null) {
			throw new RuntimeException("Map didn't contain id. Should not happen");
		}
		return sv;
	}

	private static void initIndexToVectorMaps() {
		if (ranBefore) {
			vectorToIndexMap = new  HashMap<SparseVector, Integer>();
			indexToVectorMap = new HashMap<Integer, SparseVector>();
			int id = 0;
			for (String s : indices) {
				SparseVector sv = IMDBmovies.get(s);
				vectorToIndexMap.put(sv, id);
				indexToVectorMap.put(id, sv);
			}
		}
	}
}












