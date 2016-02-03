package PartA;

import java.util.Comparator;

/**
 * Created by jay on 1/30/16.
 */
public class User implements Comparator<User> {

    int userId;
    double score;
    double rating;
    int movieId;
    double predictedRating;
    double naiveRating;

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getPredictedRating() {
        return predictedRating;
    }

    public void setPredictedRating(double predictedRating) {
        this.predictedRating = predictedRating;
    }

    public double getNaiveRating() {
        return naiveRating;
    }

    public void setNaiveRating(double naiveRating) {
        this.naiveRating = naiveRating;
    }

    @Override
    public int compare(User o1, User o2) {
        if (o1.getScore() > o2.getScore()) {
            return 1;
        } else if (o1.getScore() < o2.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}
