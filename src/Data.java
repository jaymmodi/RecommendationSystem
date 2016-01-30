import java.io.*;

/**
 * Created by jay on 1/29/16.
 */
public class Data {
    public static void main(String[] args) {
        System.out.println("Please give the path to training file");

        try {
            FileReader fileReader = new FileReader(new File("u1.base"));
            BufferedReader br = new BufferedReader(fileReader);

            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
