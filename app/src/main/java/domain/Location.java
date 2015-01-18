package domain;

/**
 * Created by user on 2015/1/18.
 */
public class Location {
    private Double lat;
    private Double lng;
    public void setLat(Double lat){ this.lat = lat; }
    public void setLng(Double lng){ this.lng = lng; }
    public Double getLat(){ return lat; }
    public Double getLng(){ return lng; }
}
