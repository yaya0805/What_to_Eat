package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2014/12/28.
 */
public class Information implements Serializable{
    public List<Restaurant> resList;
    public User user;
    public Information(List<Restaurant> map,User user){
        this.resList = map;
        this.user = user;
    }
    public void setResList(List<Restaurant> list){this.resList = list;}
    public void setUser(User user){this.user = user;}
    public List<Restaurant> getResList(){ return resList; }
    public User getUser(){return user;}
}
