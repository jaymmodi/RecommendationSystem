import java.io.*;
import java.util.HashMap;

/**
 * Created by jay on 1/29/16.
 */
public class Data {
    public static void main(String[] args) {

        try {
            FileReader fileReader = new FileReader(new File("u1.base"));
            BufferedReader br = new BufferedReader(fileReader);

            String line;
            HashMap<Integer, HashMap<Integer, Integer>> ratingMap = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] dataFields = line.split("\t");

                Integer userId = Integer.valueOf(dataFields[0]);
                Integer movieId = Integer.valueOf(dataFields[1]);
                Integer rating = Integer.valueOf(dataFields[2]);

                insertInMap(userId, movieId, rating, ratingMap);
            }

            System.out.println(ratingMap.size());
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void insertInMap(Integer userId, Integer movieId, Integer rating, HashMap<Integer, HashMap<Integer, Integer>> ratingMap) {

        if (ratingMap.containsKey(userId)) {
            HashMap<Integer, Integer> alreadyPresentMap = ratingMap.get(userId);
            alreadyPresentMap.put(movieId, rating);

        } else {
            HashMap<Integer, Integer> movieRatingMap = new HashMap<>();
            movieRatingMap.put(movieId, rating);
            ratingMap.put(userId, movieRatingMap);
        }
    }

}
