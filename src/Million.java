import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jay on 2/3/16.
 */
public class Million {

    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please provide the path to the train file");
            String trainFilePath = br.readLine();

            System.out.println("Please provide the path to the test file");
            String testFilePath = br.readLine();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
