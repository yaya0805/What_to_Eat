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
    private float rate;
    private long commentPeople = 0;
    private long id ;
    private String address;
    private String phone;
    private boolean type_dinner;
    private boolean type_lunch;
    private boolean type_breakfast;
    private boolean type_night_snack;
    private boolean kind_noodle;
    private boolean kind_rice;
    private boolean kind_other;
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

    public void setRate(float rate){
        this.rate = rate ;
    }

    public String getName(){
        return name;
    }

    public float getRate(){
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

    public void setKind_noodle(boolean r){ kind_noodle = r; }

    public void setKind_rice(boolean r){ kind_other = r; }

    public void setKind_other(boolean r){ kind_noodle = r; }

    public boolean getKind_noodle(){ return kind_noodle; }

    public boolean getKind_rice(){ return kind_rice; }

    public boolean getKind_other(){ return kind_other; }

    public void updatingRating(float newRate){
        float total = rate*commentPeople;
        commentPeople += 1;
        this.rate = (total + newRate)/commentPeople;
    }

    public boolean equals(Restaurant res){
        if(res.getAddress().compareTo(this.address)==0)
            return true;
        else
            return false;
    }

    public float comment(float rating){
        this.rate = (this.rate*commentPeople + rating)/(commentPeople+1);
        commentPeople++;
        return this.rate;
    }
    public double getDistance(double Lat1, double Long1){
        double Lat1r = ConvertDegreeToRadians(Lat1);
        double Lat2r = ConvertDegreeToRadians(this.lat);
        double Long1r = ConvertDegreeToRadians(Long1);
        double Long2r = ConvertDegreeToRadians(this.lng);

        double R = 6371; // Earth's radius (km)
        double d = Math.acos(Math.sin(Lat1r) *
                Math.sin(Lat2r) + (Math.cos(Lat1r) *
                Math.cos(Lat2r) *
                Math.cos(Long2r - Long1r))) * R;
        return d;
    }
    private double ConvertDegreeToRadians(double degrees){
        return (Math.PI/180)*degrees;
    }

}