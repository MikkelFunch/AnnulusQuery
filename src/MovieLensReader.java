import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import main.java.mmas.serenderp.util.SparseVector;

public class MovieLensReader {

	private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withHeader();
	private static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

	private static final String RATING_FILE = "data/ratings.csv";
	private static final String MOVIE_FILE = "data/movielens_movies.csv";

	// Format of ratings.csv [userId, movieId, rating, timestamp]
	private static final String USER_ID = "userId";
	private static final String MOVIE_ID = "movieId";
	private static final String RATING = "rating";

	// Format of movielens_movies.csv [movieId, title, genres]
	private static final String TITLE = "title";

	// Cache
	private static List<List<Entry<Integer, Double>>> userRatingsCache;
	private static Map<Integer, String> moviesCache;

	public static List<List<Entry<Integer, Double>>> loadUserRatings() {
		if (userRatingsCache == null) {
			
			Map<Integer, String> movies = loadMovies();
			
			File csvData = new File(RATING_FILE);

			// List of Users of Ratings
			List<List<Entry<Integer, Double>>> ratings = new ArrayList<List<Entry<Integer, Double>>>();

			try {
				CSVParser parser = CSVParser.parse(csvData, FILE_CHARSET, CSV_FORMAT);

				int currentUserId = 0;
				List<Entry<Integer, Double>> currentUser = null;

				for (CSVRecord csvRecord : parser) {
					int userId = Integer.parseInt(csvRecord.get(USER_ID));
					int movieId = Integer.parseInt(csvRecord.get(MOVIE_ID));
					double rating = Double.parseDouble(csvRecord.get(RATING));

					if (userId != currentUserId) {
						currentUserId = userId;
						currentUser = new ArrayList<Entry<Integer, Double>>();
						ratings.add(currentUser);
					}
					
					if(movies.containsKey(movieId)) {
						currentUser.add(new AbstractMap.SimpleEntry<Integer, Double>(movieId, rating));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			userRatingsCache = ratings;
		}
		return userRatingsCache;
	}

	public static Map<Integer, String> loadMovies() {
		if (moviesCache == null) {
			
			Map<String, SparseVector> imdbMovies = PreProcess.getIMDBMovies();
			
			File csvData = new File(MOVIE_FILE);

			// Map from movieId to movie
			Map<Integer, String> movies = new HashMap<Integer, String>();

			try {
				CSVParser parser = CSVParser.parse(csvData, FILE_CHARSET, CSV_FORMAT);

				for (CSVRecord record : parser) {
					int movieId = Integer.parseInt(record.get(MOVIE_ID));
					String title = record.get(TITLE);

					if (title.matches("(.*), The[ ]+\\((.*)")) {
						title = title.replaceAll(", The[ ]+", " ");
						title = "The " + title;
					}
					
					if(imdbMovies.containsKey(title)) {
						movies.put(movieId, title);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			moviesCache = movies;
		}
		return moviesCache;
	}

	public static void main(String[] args) {
		loadUserRatings();
		loadMovies();
	}
}