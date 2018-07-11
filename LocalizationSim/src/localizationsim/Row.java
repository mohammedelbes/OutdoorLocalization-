/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */ 
public class Row {

    private ArrayList<Double> values;
    private Point p;
     private String name;

    public Row() {
        values = new ArrayList<Double>();
    }

    /**
     * @return the values
     */
    public ArrayList<Double> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(ArrayList<Double> values) {
        this.setValues(values);
    }

    /**
     * @return the p
     */
    public Point getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(Point p) {
        this.p = p;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
