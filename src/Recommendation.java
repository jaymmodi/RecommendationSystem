import java.util.*;

/**
 * Created by jay on 1/30/16.
 */
public class Recommendation {

    public HashMap<Integer, HashMap<Integer, Integer>> ratingMap;

    public Recommendation(HashMap<Integer, HashMap<Integer, Integer>> ratingMap) {
        this.ratingMap = ratingMap;
    }

    public ArrayList<User> findSimilarUsers(int userId) {
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

    private static void customSort(ArrayList<User> scoreList) {
        Collections.sort(scoreList, new User());
    }

    public int getPrediction(int userId, Integer movieId) {
        ArrayList<User> similarUsers = findSimilarUsers(userId);
        ArrayList<Integer> similarRatings = new ArrayList<>();

        for (User similarUser : similarUsers) {
//
            HashMap<Integer, Integer> movieRatingMap = ratingMap.get(similarUser.getUserId());
            if (movieRatingMap.containsKey(movieId)) {
                if (similarRatings.size() <= 50) {
                    similarRatings.add(movieRatingMap.get(movieId));
                } else {
                    break;
                }
            }
        }


        return getAvg(similarRatings);

    }

    private int getAvg(ArrayList<Integer> similarRatings) {
        int sum = 0;
        for (Integer similarRating : similarRatings) {
            sum += similarRating;
        }

        return sum / similarRatings.size();
    }
}
