package domain;

/**
 * Created by user on 2014/12/28.
 */
public class Restaurant {
    private String name;
    private double rate;
    private long commentPeople = 0;
    private long id ;
    private String address;

    //empty constructor
    public Restaurant(){}

    //init constructor
    public Restaurant(String name,double rate,long id,String address){
        this.name = name ;
        this.rate = rate ;
        this.id = -1;
        this.commentPeople = 0;
        this.address = address;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

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

    public void setCommentPeople(long num){
        commentPeople = num;
    }

    public long getCommentPeople(){
        return commentPeople;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }
    public void updatingRating(double newRate){
        double total = rate*commentPeople;
        commentPeople += 1;
        this.rate = (total + newRate)/commentPeople;
    }

}
