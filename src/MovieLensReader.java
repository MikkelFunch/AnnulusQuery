import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MovieLensReader {

	// Format of CSV file [userId, movieId, rating, timestamp]
	private static final String USER_ID = "userId";
	private static final String MOVIE_ID = "movieId";
	private static final String RATING = "rating";

	public static List<List<Entry<Integer, Double>>> load() {
		File csvData = new File("data/ratings.csv");
		
		// List of Users of Ratings
		List<List<Entry<Integer, Double>>> ratings = new ArrayList<List<Entry<Integer, Double>>>();
		
		try {
			CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader());

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
				currentUser.add(new AbstractMap.SimpleEntry<Integer, Double>(movieId, rating));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ratings;
	}
	
	public static void main(String[] args) {
		List<List<Entry<Integer, Double>>> data = load();
		System.out.println(data.size());
	}
}