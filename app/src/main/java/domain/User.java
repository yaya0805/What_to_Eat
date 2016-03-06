package domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2014/12/28.
 */
public class User implements Serializable{
    private String id;
    private Map<String,Double> userRate ;
    public int hasRate = 0;

    public void user(){}

    public void setUserRate(Map<String,Double> map){
        this.userRate = map;
    }
    public Map<String ,Double> getUserRate(){
        return userRate;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
    public double getResRate(String name){
        return userRate.get(name);
    }
    public void setHasRate(int num){
        this.hasRate = num;
    }
    public int getHasRate(){
        return hasRate;
    }
}

