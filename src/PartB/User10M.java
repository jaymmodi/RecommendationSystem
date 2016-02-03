package PartB;

/**
 * Created by jay on 2/3/16.
 */
public class User10M {

    int id;
    int age;                     //20-35 -> 0, 35-50 ->1, 50 > 2
    int sex;                    //0 - Male 1- Female
    int occupation;            //0-20

    public User10M(int id, int age, int sex, int occupation) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.occupation = occupation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }
}
