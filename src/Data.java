import java.io.*;
import java.util.*;

/**
 * Created by jay on 1/29/16.
 */
public class Data {
    public static void main(String[] args) {

        HashMap<Integer, HashMap<Integer, Integer>> ratingMap = new HashMap<>();
        makeUserRatingMap(ratingMap);
        ArrayList<Integer> similarUsers = findSimilarUsers(ratingMap, 1);

        printList(similarUsers);
    }

    private static void printList(ArrayList<Integer> similarUsers) {
        for (Integer similarUser : similarUsers) {
            System.out.println(similarUser);
        }
    }

    private static ArrayList<Integer> findSimilarUsers(HashMap<Integer, HashMap<Integer, Integer>> ratingMap, int userId) {
        ArrayList<Integer> scoreList = new ArrayList<>();

        HashMap<Integer, Integer> maptoCompare = ratingMap.get(userId);

        for (int i = 1; i <= ratingMap.size(); i++) {
            if (i != userId) {
                HashMap<Integer, Integer> compareWith = ratingMap.get(i);
                int score = getEuclideanDistance(maptoCompare, compareWith);
                scoreList.add(score);
            }

        }
        Collections.sort(scoreList, Collections.reverseOrder());
        return scoreList;
    }

    private static int getEuclideanDistance(HashMap<Integer, Integer> mapToCompare, HashMap<Integer, Integer> compareWith) {
        Iterator it = mapToCompare.entrySet().iterator();
        int distance = 0;

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            int ratingOne = pair.getValue();

            int movieId = pair.getKey();

            if (compareWith.containsKey(movieId)) {
                int ratingTwo = compareWith.get(movieId);
                distance += Math.abs(ratingOne - ratingTwo);
            }
        }
        return distance;
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
