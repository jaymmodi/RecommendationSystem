import java.io.*;
import java.util.*;

/**
 * Created by jay on 1/29/16.
 */
public class Data {
    public static void main(String[] args) {

        HashMap<Integer, HashMap<Integer, Integer>> ratingMap = new HashMap<>();
        makeUserRatingMap(ratingMap);

        Recommendation recommendation = new Recommendation(ratingMap);
//        ArrayList<User> similarUsers = recommendation.findSimilarUsers(1);

        int topSimilarUsers = 50;
        readTestFile(recommendation,topSimilarUsers);


//        printList(similarUsers);
    }

    private static void readTestFile(Recommendation recommendation, int topSimilarUsers) {
        try {
            FileReader fileReader = new FileReader(new File("test"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String datafields[] = line.split("\t");

                User user = new User();
                user.setUserId(Integer.valueOf(datafields[0]));
                user.setMovieId(Integer.valueOf(datafields[1]));
                user.setRating(Integer.valueOf(datafields[2]));

                user.setPredictedRating(getPredictedRating(recommendation, user.getUserId(), Integer.valueOf(datafields[1])));


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int getPredictedRating(Recommendation recommendation, int userId, Integer movieId) {

        int rating = recommendation.getPrediction(userId, movieId);
        System.out.println("rating = " + rating);

        return rating;
    }

    private static void printList(ArrayList<User> similarUsers) {
        for (User similarUser : similarUsers) {
            System.out.println(similarUser.getUserId() + " ----> " + similarUser.getScore());
        }
    }


    private static void makeUserRatingMap(HashMap<Integer, HashMap<Integer, Integer>> ratingMap) {
        try {
            FileReader fileReader = new FileReader(new File("u1.base"));
            BufferedReader br = new BufferedReader(fileReader);

            String line;

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
