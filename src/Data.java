import java.io.*;
import java.util.*;

/**
 * Created by jay on 1/29/16.
 */
public class Data {

    public static double MAD = 0;
    public static double SIMPLEMAD = 0;

    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int topSimilarUsers = 7;

            System.out.println("Please select a eval Type :  1. Average 2. Maximum Frequency ");
            String evalType = br.readLine();
            evalType = validateEvalType(evalType);
            if (evalType.equals("exit")) {
                System.out.println("Invalid Inputs. Try again");
                System.exit(-1);
            }

            System.out.println("Please select distance Metric :  1. Euclidean 2. Manhattan 3. Lmax");
            String metric = br.readLine();
            metric = validateMetric(metric);
            if (evalType.equals("exit")) {
                System.out.println("Invalid Inputs. Try again");
                System.exit(-1);
            }

            System.out.println("Calculation MAD for k =  " + topSimilarUsers + " whole 100K data points ");

            HashMap<Integer, HashMap<Integer, Integer>> userRatingMap = new HashMap<>();
            HashMap<Integer, List<Integer>> movieRatingMap = new HashMap<>();
            Recommendation recommendation = new Recommendation(userRatingMap, movieRatingMap, topSimilarUsers, metric, evalType);


            String fileNames[] = {"u1","u2","u3","u4","u5"};

            for (String fileName : fileNames) {
                makeUserRatingMap(userRatingMap, movieRatingMap, fileName + ".base");
                readTestFile(recommendation, fileName + ".test");
            }

            System.out.println("MAD = " + MAD / 100000);
            System.out.println("Naive MAD = " + SIMPLEMAD/ 100000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String validateMetric(String metric) {
        if (metric.equalsIgnoreCase("Euclidean") || metric.equals("1") || metric.equalsIgnoreCase("Manhattan") || metric.equals("2") || metric.equalsIgnoreCase("Lmax") || metric.equals("3")) {
            return metric;
        }

        return "exit";
    }

    private static String validateEvalType(String evalType) {
        if (evalType.equalsIgnoreCase("Average") || evalType.equals("1") || evalType.equalsIgnoreCase("Maximum Frequency") || evalType.equals("2")) {
            return evalType;
        }

        return "exit";
    }

    private static void readTestFile(Recommendation recommendation, String testFileName) {
        try {
            FileReader fileReader = new FileReader(new File(testFileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String dataFields[] = line.split("\t");

                User user = new User();
                user.setUserId(Integer.valueOf(dataFields[0]));
                user.setMovieId(Integer.valueOf(dataFields[1]));
                user.setRating(Integer.valueOf(dataFields[2]));

                user.setPredictedRating(getPredictedRating(recommendation, user.getUserId(), Integer.valueOf(dataFields[1])));
                user.setNaiveRating(getNaiveRating(recommendation, Integer.valueOf(dataFields[1])));

                System.out.println("True= " + user.getRating() + " Predicted= " + user.getPredictedRating());
                MAD += Math.abs(user.getRating() - user.getPredictedRating());
                SIMPLEMAD += Math.abs(user.getNaiveRating()-user.getRating());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static int getNaiveRating(Recommendation recommendation, Integer movieId) {
        return recommendation.getNaiveRating(movieId);
    }

    private static int getPredictedRating(Recommendation recommendation, int userId, Integer movieId) {

        return recommendation.getPrediction(userId, movieId);
    }

    private static void printList(ArrayList<User> similarUsers) {
        for (User similarUser : similarUsers) {
            System.out.println(similarUser.getUserId() + " ----> " + similarUser.getScore());
        }
    }


    private static void makeUserRatingMap(HashMap<Integer, HashMap<Integer, Integer>> userRatingMap, HashMap<Integer, List<Integer>> movieRatingMap, String trainFileName) {
        try {
            FileReader fileReader = new FileReader(new File(trainFileName));
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
