package main.java.mmas.serenderp;

public class Constants {
	public static final int DIMENSIONS = 4_080_357;
//	public static final int AMOUNT_OF_RANDOM_VECTORS = 10;
	public static final double R = 20, W = 1.85, C = 1.41;
//	public static final int NUMBER_OF_BANDS = 20;
//	public static final int HASH_FUNCTIONS_PER_BAND = 1;
//	public static final int NUMBER_OF_HASH_FUNCTIONS = NUMBER_OF_BANDS * HASH_FUNCTIONS_PER_BAND;
	
	public static int NUMBER_OF_BANDS;
	public static int HASH_FUNCTIONS_PER_BAND;
	public static int NUMBER_OF_HASH_FUNCTIONS;
	public static int AMOUNT_OF_RANDOM_VECTORS;
	
	public static void setParameters(int numbersOfBands, int hashFuntionsPerBand, int amountOfRandomVectors) {
		NUMBER_OF_BANDS = numbersOfBands;
		HASH_FUNCTIONS_PER_BAND = hashFuntionsPerBand;
		NUMBER_OF_HASH_FUNCTIONS = NUMBER_OF_BANDS * HASH_FUNCTIONS_PER_BAND;
		AMOUNT_OF_RANDOM_VECTORS = amountOfRandomVectors;
//		RandomVectors.createRandomVectors();
	}
}
