package domain;

import java.util.Arrays;

/**
 * Created by user on 2015/1/18.
 */
public class Result {
    private String[] types;
    private String formatted_address;
    private Geometry geometry;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formattedAddress) {
        formatted_address = formattedAddress;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public void setGeometry(Geometry geometry) {this.geometry = geometry;}

    public Geometry getGeometry(){ return geometry;}

    @Override
    public String toString() {
        return "Result [formatted_address=" + formatted_address + ", types="
                + Arrays.toString(types) + "]";
    }
}
