import java.util.*;

/**
 * Created by jay on 1/30/16.
 */
public class Recommendation {

    public HashMap<Integer, List<Integer>> movieRatingMap;
    public String metric;
    public HashMap<Integer, HashMap<Integer, Integer>> ratingMap;
    String evalType;
    int topSimilarUsers;

    public Recommendation(HashMap<Integer, HashMap<Integer, Integer>> ratingMap, HashMap<Integer, List<Integer>> movieRatingMap, int topSimilarUsers, String evalType, String metric) {
        this.ratingMap = ratingMap;
        this.movieRatingMap = movieRatingMap;
        this.topSimilarUsers = topSimilarUsers;
        this.evalType = evalType;
        this.metric = metric;
    }

    public ArrayList<User> findSimilarUsers(int userId) {
        ArrayList<User> scoreList = new ArrayList<>();

        HashMap<Integer, Integer> maptoCompare = ratingMap.get(userId);

        for (int i = 1; i <= ratingMap.size(); i++) {
            if (i != userId) {
                HashMap<Integer, Integer> compareWith = ratingMap.get(i);
                double score = getDistance(maptoCompare, compareWith);
                User user = new User();
                user.setUserId(i);
                user.setScore(score);
                scoreList.add(user);
            }

        }
        if (scoreList.size() == 0) {
            System.out.println("empty hai");
        }
        customSort(scoreList);

        return scoreList;
    }

    private double getDistance(HashMap<Integer, Integer> mapToCompare, HashMap<Integer, Integer> compareWith) {
        if (this.metric.equalsIgnoreCase("Euclidean") || this.metric.equals("1")) {
            return getEuclideanDistance(mapToCompare, compareWith);
        } else if (this.metric.equalsIgnoreCase("Manhattan") || this.metric.equals("2")) {
            return getManhattanDistance(mapToCompare, compareWith);
        } else {
            return getLmaxDistance(mapToCompare, compareWith);
        }
    }

    private double getLmaxDistance(HashMap<Integer, Integer> mapToCompare, HashMap<Integer, Integer> compareWith) {
        return 0;
    }

    private double getManhattanDistance(HashMap<Integer, Integer> mapToCompare, HashMap<Integer, Integer> compareWith) {
        Iterator it = mapToCompare.entrySet().iterator();
        double distance = 0;
        int matchCount = 0;

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry) it.next();
            int ratingOne = pair.getValue();

            int movieId = pair.getKey();

            if (compareWith.containsKey(movieId)) {
                int ratingTwo = compareWith.get(movieId);
                distance += Math.abs(ratingOne - ratingTwo);
                ++matchCount;
            }
        }

        if (matchCount == 0) {
            return Double.MAX_VALUE;
        }


        return Math.sqrt(distance);
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
            HashMap<Integer, Integer> movieRatingMap = ratingMap.get(similarUser.getUserId());
            if (movieRatingMap.containsKey(movieId)) {
                if (similarRatings.size() <= this.topSimilarUsers) {
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
        double avg;

        for (Integer similarRating : similarRatings) {
            sum += similarRating;
        }

        if (similarRatings.size() > 0) {
            avg = sum / similarRatings.size();
        } else {
            avg = 3;
        }

        return (int) Math.round(avg);
    }

    public int getNaiveRating(Integer movieId) {
        List<Integer> userList = movieRatingMap.get(movieId);
        if (userList != null) {
            int sum = 0;
            double avg;

            for (Integer userID : userList) {
                sum += ratingMap.get(userID).get(movieId);
            }

            avg = sum / userList.size();

            return (int) Math.round(avg);
        } else {
            return 3;
        }
    }
}
