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
            String delimiter;


            ArrayList<String> trainFiles = new ArrayList<>();
            ArrayList<String> testFiles = new ArrayList<>();

            System.out.println("Please select a dataset :  1. 100k 2. 10M ");
            String dataSetType = br.readLine();
            dataSetType = validateDataSet(dataSetType);
            if (dataSetType.equals("exit")) {
                System.out.println("Invalid Inputs. Try again");
                System.exit(-1);
            }

            makePathArrays(trainFiles, testFiles, dataSetType);
            delimiter = getDelimiter(dataSetType);

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


            HashMap<Integer, HashMap<Integer, Integer>> userRatingMap = new HashMap<>();
            HashMap<Integer, List<Integer>> movieRatingMap = new HashMap<>();
            Recommendation recommendation = new Recommendation(userRatingMap, movieRatingMap, topSimilarUsers, metric, evalType);

            for (int i = 0; i < trainFiles.size(); i++) {
                makeUserRatingMap(userRatingMap, movieRatingMap, trainFiles.get(i), delimiter);
                readTestFile(recommendation, testFiles.get(i), delimiter);
            }

            printMAD(delimiter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMAD(String delimiter) {
        if(delimiter.equals("\t")){
            System.out.println("MAD " +  MAD/100000);
            System.out.println("NaiveMAD " +  SIMPLEMAD/100000);
        }else{
            System.out.println("MAD " +  MAD/10000000);
            System.out.println("NaiveMAD " +  SIMPLEMAD/10000000);
        }
    }

    private static String validateDataSet(String dataSetType) {
        if (dataSetType.equalsIgnoreCase("100k") || dataSetType.equals("1")) {
            return "paths.txt";
        } else if (dataSetType.equalsIgnoreCase("10M") || dataSetType.equals("2")) {
            return "millionPaths.txt";
        } else {
            return "exit";
        }
    }

    private static String getDelimiter(String dataSetType) {
        if (dataSetType.equalsIgnoreCase("paths.txt")) {
            return "\t";
        } else if (dataSetType.equalsIgnoreCase("millionPaths.txt")) {
            return "::";
        } else {
            return "exit";
        }
    }

    private static void makePathArrays(ArrayList<String> trainFiles, ArrayList<String> testFiles, String pathFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathFile));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("base") || line.contains("train")) {
                    trainFiles.add(line);
                } else if (line.contains("test")) {
                    testFiles.add(line);
                }
            }
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

    private static void readTestFile(Recommendation recommendation, String testFileName, String delimiter) {
        try {
            FileReader fileReader = new FileReader(new File(testFileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String dataFields[] = line.split(delimiter);

                User user = new User();
                user.setUserId(Integer.valueOf(dataFields[0]));
                user.setMovieId(Integer.valueOf(dataFields[1]));
                user.setRating(Integer.valueOf(dataFields[2]));

                user.setPredictedRating(getPredictedRating(recommendation, user.getUserId(), Integer.valueOf(dataFields[1])));
                user.setNaiveRating(getNaiveRating(recommendation, Integer.valueOf(dataFields[1])));

                System.out.println("True= " + user.getRating() + " Predicted= " + user.getPredictedRating());
                MAD += Math.abs(user.getRating() - user.getPredictedRating());
                SIMPLEMAD += Math.abs(user.getNaiveRating() - user.getRating());

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


    private static void makeUserRatingMap(HashMap<Integer, HashMap<Integer, Integer>> userRatingMap, HashMap<Integer, List<Integer>> movieRatingMap, String filePath, String delimiter) {
        try {
            FileReader fileReader = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fileReader);

            String line;

            while ((line = br.readLine()) != null) {
                String[] dataFields = line.split(delimiter);

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
