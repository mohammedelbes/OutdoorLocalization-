/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package localizationsim;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
import java.awt.Point;

/**
 *
 * @author Admin
 */
public class MobilityInfo {

    private double speed;
    private double distance;
    private Coordinates newActualPosition;
    private double direction;
    private double xIMUDistance;
    private double yIMUDistance;
    private Coordinates newIMUPosition;
    double [] noisyDistancesFromBSs;
    double [] realDistancesFromBSs;

    public MobilityInfo(int size) {
        noisyDistancesFromBSs = new double[size];
        realDistancesFromBSs= new double [size];
    }

    public MobilityInfo(double speed, double distance, Coordinates newPosition) {
        this.speed = speed;
        this.distance = distance;
        this.newActualPosition = newPosition;
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the newActualPosition
     */
    public Coordinates getNewActualPosition() {
        return newActualPosition;
    }

    /**
     * @param newActualPosition the newActualPosition to set
     */
    public void setNewActualPosition(Coordinates newActualPosition) {
        this.newActualPosition = newActualPosition;
    }

    /**
     * @return the direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }

    /**
     * @return the xIMUDistance
     */
    public double getxIMUDistance() {
        return xIMUDistance;
    }

    /**
     * @param xIMUDistance the xIMUDistance to set
     */
    public void setxIMUDistance(double xIMUDistance) {
        this.xIMUDistance = xIMUDistance;
    }

    /**
     * @return the yIMUDistance
     */
    public double getyIMUDistance() {
        return yIMUDistance;
    }

    /**
     * @param yIMUDistance the yIMUDistance to set
     */
    public void setyIMUDistance(double yIMUDistance) {
        this.yIMUDistance = yIMUDistance;
    }

    /**
     * @return the newIMUPosition
     */
    public Coordinates getNewIMUPosition() {
        return newIMUPosition;
    }

    /**
     * @param newIMUPosition the newIMUPosition to set
     */
    public void setNewIMUPosition(Coordinates newIMUPosition) {
        this.newIMUPosition = newIMUPosition;
    }
}
