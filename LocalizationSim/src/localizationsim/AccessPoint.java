package localizationsim;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Random;

/**
 *
 * @author Admin
 */
public class AccessPoint {

    private Coordinates location;
    private int ID;


    private double[][] phaseShifts;
    private double[] distances;
    private boolean noisy;
    private double weight=0;
    private int frequency=0;
    private double offset;
    double dcOffset;

    public AccessPoint(Coordinates location, int ID, int numberOfAccessPoints, Random noiseBehaviour) {

        phaseShifts = new double[numberOfAccessPoints - 1][numberOfAccessPoints - 1];
        distances = new double[numberOfAccessPoints];
        this.location = location;
        this.ID = ID;
        this.dcOffset=0.5;//+noiseBehaviour.nextDouble()*4;
        if (noiseBehaviour.nextDouble() >.2) {
            noisy = false;
        } else {
            noisy = true;
        }
    }
 public AccessPoint(Coordinates location, int ID, int numberOfAccessPoints) {

        phaseShifts = new double[numberOfAccessPoints - 1][numberOfAccessPoints - 1];
        distances = new double[numberOfAccessPoints];
        this.location = location;
        this.ID = ID;
        noisy = false;

    }

    AccessPoint() {

    }


    public Coordinates getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Coordinates location) {
        this.location = location;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }


 

    /**
     * @return the phaseShifts
     */
    public double[][] getPhaseShifts() {
        return phaseShifts;
    }

    /**
     * @param phaseShifts the phaseShifts to set
     */
    public void setPhaseShifts(double[][] phaseShifts) {
        this.phaseShifts = phaseShifts;
    }

    /**
     * @return the distances
     */
    public double[] getDistances() {
        return distances;
    }

    /**
     * @param distances the distances to set
     */
    public void setDistances(double[] distances) {
        this.distances = distances;
    }

    /**
     * @return the noisy
     */
    public boolean isNoisy() {
        return noisy;
    }

    /**
     * @param noisy the noisy to set
     */
    public void setNoisy(boolean noisy) {
        this.noisy = noisy;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the offset
     */
    public double getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(double offset) {
        this.offset = offset;
    }
}
