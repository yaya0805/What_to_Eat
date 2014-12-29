package domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2014/12/28.
 */
public class User {
    private String id;
    private Map<String,Double> userRate = new HashMap<String,Double>();
    public int hasRate = 0;
    public void user(){}

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
