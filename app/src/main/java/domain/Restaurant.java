package domain;

/**
 * Created by user on 2014/12/28.
 */
public class Restaurant {
    private String name;
    private double rate;
    public void setName(String name){
        this.name = name;
    }
    public void setRate(double rate){
        this.rate = rate ;
    }
    public String getName(){
        return name;
    }
    public double getRate(){
        return rate;
    }
}
