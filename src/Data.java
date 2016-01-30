import java.io.*;
import java.util.*;

/**
 * Created by jay on 1/29/16.
 */
public class Data {
    public static void main(String[] args) {

        HashMap<Integer, HashMap<Integer, Integer>> ratingMap = new HashMap<>();
        makeUserRatingMap(ratingMap);
        ArrayList<User> similarUsers = findSimilarUsers(ratingMap, 1);

        printList(similarUsers);
    }

    private static void printList(ArrayList<User> similarUsers) {
        for (User similarUser : similarUsers) {
            System.out.println(similarUser.getUserId() + " ----> " + similarUser.getScore());
        }
    }

    private static ArrayList<User> findSimilarUsers(HashMap<Integer, HashMap<Integer, Integer>> ratingMap, int userId) {
        ArrayList<User> scoreList = new ArrayList<>();

        HashMap<Integer, Integer> maptoCompare = ratingMap.get(userId);

        for (int i = 1; i <= ratingMap.size(); i++) {
            if (i != userId) {
                HashMap<Integer, Integer> compareWith = ratingMap.get(i);
                double score = getEuclideanDistance(maptoCompare, compareWith);
                User user = new User();
                user.setUserId(i);
                user.setScore(score);
                scoreList.add(user);
            }

        }
        customSort(scoreList);
        return scoreList;
    }

    private static void customSort(ArrayList<User> scoreList) {
        Collections.sort(scoreList, new User());
    }

    private static double getEuclideanDistance(HashMap<Integer, Integer> mapToCompare, HashMap<Integer, Integer> compareWith) {
        Iterator it = mapToCompare.entrySet().iterator();
        double distance = 0;
        int matchCount = 0;

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            int ratingOne = pair.getValue();

            int movieId = pair.getKey();

            if (compareWith.containsKey(movieId)) {
                int ratingTwo = compareWith.get(movieId);
                distance += Math.pow(ratingOne - ratingTwo, 2);
                ++matchCount;
            }
        }

        if (matchCount == 0) {
            return Double.MAX_VALUE;
        }


        return Math.sqrt(distance);
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
