import java.util.Comparator;

/**
 * Created by jay on 1/30/16.
 */
public class User implements Comparator<User> {

    int userId;
    double score;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compare(User o1, User o2) {
        if (o1.getScore() >= o2.getScore()) {
            return 1;
        } else {
            return -1;
        }
    }
}
