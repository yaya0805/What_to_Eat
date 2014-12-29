package domain;

import java.util.Map;

/**
 * Created by user on 2014/12/28.
 */
public class Information {
    public Map<Long,Restaurant> map;
    public User user;
    public Information(Map<Long,Restaurant> map,User user){
        this.map = map;
        this.user = user;
    }
    public void setMap(Map<Long,Restaurant> map){this.map = map;}
    public void setUser(User user){this.user = user;}
    public Map<Long,Restaurant> getMap(){ return map; }
    public User getUser(){return user;}
}
