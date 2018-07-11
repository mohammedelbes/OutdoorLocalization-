/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class SimArea extends JPanel {

    private int xLeftBound = 220;
    private int xRightBound = 1330;
    private int yUpperBound = 730;
    private int yLowerBound = 115;
    private boolean clickable = true;
    private ArrayList<JLabel> trainingLabels;
    private int offset = 100;
    

    public SimArea() {

        this.setLayout(null);

    }

    public boolean isInsideSimArea(Coordinates p) {
        return (p.x > this.getBounds().x+offset && p.x < (this.getBounds().x + this.getBounds().width-2*offset) && p.y > (this.getBounds().y+offset) && p.y <= (this.getBounds().y + this.getBounds().height-2*offset)) ? true : false;
    }

    /**
     * @return the xLeftBound
     */
    public int getxLeftBound() {
        return xLeftBound;
    }

    /**
     * @param xLeftBound the xLeftBound to set
     */
    public void setxLeftBound(int xLeftBound) {
        this.xLeftBound = xLeftBound;
    }

    /**
     * @return the xRightBound
     */
    public int getxRightBound() {
        return xRightBound;
    }

    /**
     * @param xRightBound the xRightBound to set
     */
    public void setxRightBound(int xRightBound) {
        this.xRightBound = xRightBound;
    }

    /**
     * @return the yUpperBound
     */
    public int getyUpperBound() {
        return yUpperBound;
    }

    /**
     * @param yUpperBound the yUpperBound to set
     */
    public void setyUpperBound(int yUpperBound) {
        this.yUpperBound = yUpperBound;
    }

    /**
     * @return the yLowerBound
     */
    public int getyLowerBound() {
        return yLowerBound;
    }

    /**
     * @param yLowerBound the yLowerBound to set
     */
    public void setyLowerBound(int yLowerBound) {
        this.yLowerBound = yLowerBound;
    }

//     public void paintComponent(Graphics g)
//    {
//    super.paintComponent(g);
//
//
//    }
    public void drawThing() {
        Graphics g = super.getGraphics();
        g.setColor(Color.red);
        g.fillRect(0, 0, 100, 100);
        g.drawLine(100, 100, 200, 200);

    }

    /**
     * @return the clickable
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * @param clickable the clickable to set
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    /**
     * @return the trainingLabels
     */
    public ArrayList<JLabel> getTrainingLabels() {
        return trainingLabels;
    }

    /**
     * @param trainingLabels the trainingLabels to set
     */
    public void setTrainingLabels(ArrayList<JLabel> trainingLabels) {
        this.trainingLabels = trainingLabels;
    }

}

