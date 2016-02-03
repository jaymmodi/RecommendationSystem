package PartB;

import java.io.*;
import java.util.HashMap;

/**
 * Created by jay on 2/3/16.
 */
public class MillionDriver {

    public static void main(String[] args) {

        HashMap<Integer, User10M> userFeatureMap = new HashMap<>();
        HashMap<String, Integer> occupationMap = new HashMap<>();

        String occupationFileName = "u.occupation";
        makeOccupationMap(occupationMap, occupationFileName);

        String userFile = "u.user";
        makeUserMap(userFeatureMap, occupationMap, userFile);
    }

    private static void makeOccupationMap(HashMap<String, Integer> occupationMap, String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            int i = 0;
            while ((line = br.readLine()) != null) {
                occupationMap.put(line, i);
                ++i;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void makeUserMap(HashMap<Integer, User10M> userFeatureMap, HashMap<String, Integer> occupationMap, String fileName) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                String[] dataFields = line.split("|");

                Integer userId = Integer.valueOf(dataFields[0]);
                Integer age = discretizeAge(Integer.valueOf(dataFields[1]));
                Integer sex = getSex(dataFields[2]);
                Integer occupation = occupationMap.get(dataFields[3]);

                User10M user10M = new User10M(userId, age, sex, occupation);

                userFeatureMap.put(userId, user10M);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer getSex(String sex) {
        return (sex.equals("M")) ? 0 : 1;
    }

    private static Integer discretizeAge(Integer age) {
        if (age >= 20 && age < 35) {
            return 0;
        } else if (age >= 35 && age < 50) {
            return 1;
        } else {
            return 2;
        }

    }


}
