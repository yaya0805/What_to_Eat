package domain;

import java.io.Serializable;

/**
 * Created by user on 2014/12/28.
 */
public class Restaurant implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private double rate;
    private long commentPeople = 0;
    private long id ;
    private String address;
    private String phone;
    private boolean type_dinner;
    private boolean type_lunch;
    private boolean type_breakfast;
    private boolean type_night_snack;
    private Double lng;
    private Double lat;

    //empty constructor
    public Restaurant(){}

    //init constructor
    public Restaurant(String name,long id,String address,String phone){
        this.name = name ;
        this.rate = 0 ;
        this.id = -1;
        this.commentPeople = 0;
        this.address = address;
        this.phone = phone;
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

    public void setPhone(String phone){this.phone = phone;}

    public void setType_dinner(boolean r) { type_dinner = r; }

    public boolean getType_dinner() { return type_dinner; }

    public void setType_lunch(boolean r){ type_lunch = r; }

    public boolean getType_lucnch(){ return type_lunch;}

    public void setType_breakfast(boolean r){ type_breakfast =r; }

    public boolean getType_breakfast(){ return type_breakfast; }

    public void setType_night_snack(boolean r){ type_night_snack = r; }

    public boolean getType_night_snack(){ return type_night_snack; }

    public String getPhone(){ return phone; }

    public void setLat(Double lat){ this.lat = lat; }

    public void setLng(Double lng){ this.lng = lng; }

    public Double getLat(){  return lat; }

    public Double getLng(){ return lng; }

    public void updatingRating(double newRate){
        double total = rate*commentPeople;
        commentPeople += 1;
        this.rate = (total + newRate)/commentPeople;
    }

}