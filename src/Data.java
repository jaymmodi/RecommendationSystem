import java.io.*;
import java.util.*;

/**
 * Created by jay on 1/29/16.
 */
public class Data {
    public static void main(String[] args) {

        HashMap<Integer, HashMap<Integer, Integer>> userRatingMap = new HashMap<>();
        HashMap<Integer, List<Integer>> movieRatingMap = new HashMap<>();

        makeUserRatingMap(userRatingMap, movieRatingMap);

        int topSimilarUsers = 50;
        Recommendation recommendation = new Recommendation(userRatingMap, topSimilarUsers);

        readTestFile(recommendation);

    }

    private static void readTestFile(Recommendation recommendation) {
        try {
            FileReader fileReader = new FileReader(new File("test"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String dataFields[] = line.split("\t");

                User user = new User();
                user.setUserId(Integer.valueOf(dataFields[0]));
                user.setMovieId(Integer.valueOf(dataFields[1]));
                user.setRating(Integer.valueOf(dataFields[2]));

                user.setPredictedRating(getPredictedRating(recommendation, user.getUserId(), Integer.valueOf(dataFields[1])));

                System.out.println("True= " + user.getRating() + " Predicted= " + user.getPredictedRating());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int getPredictedRating(Recommendation recommendation, int userId, Integer movieId) {

        return recommendation.getPrediction(userId, movieId);
    }

    private static void printList(ArrayList<User> similarUsers) {
        for (User similarUser : similarUsers) {
            System.out.println(similarUser.getUserId() + " ----> " + similarUser.getScore());
        }
    }


    private static void makeUserRatingMap(HashMap<Integer, HashMap<Integer, Integer>> userRatingMap, HashMap<Integer, List<Integer>> movieRatingMap) {
        try {
            FileReader fileReader = new FileReader(new File("u1.base"));
            BufferedReader br = new BufferedReader(fileReader);

            String line;

            while ((line = br.readLine()) != null) {
                String[] dataFields = line.split("\t");

                Integer userId = Integer.valueOf(dataFields[0]);
                Integer movieId = Integer.valueOf(dataFields[1]);
                Integer rating = Integer.valueOf(dataFields[2]);

                insertInUserRatingMap(userId, movieId, rating, userRatingMap);
                insertInMovieRatingMap(movieRatingMap, movieId, userId);
            }

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertInMovieRatingMap(HashMap<Integer, List<Integer>> movieRatingMap, Integer movieId, Integer userId) {
        if (movieRatingMap.containsKey(movieId)) {
            ArrayList<Integer> arrayList = (ArrayList<Integer>) movieRatingMap.get(movieId);
            arrayList.add(userId);
        } else {
            ArrayList<Integer> userList = new ArrayList<>();
            userList.add(userId);
            movieRatingMap.put(movieId, userList);
        }
    }

    private static void insertInUserRatingMap(Integer userId, Integer movieId, Integer rating, HashMap<Integer, HashMap<Integer, Integer>> ratingMap) {

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
