package localizationsim;

import java.util.Random;
import eduni.simjava.distributions.Sim_normal_obj;
 
/**
 *
 * @author Admin
 */
public class MobilityModel {

    private double timeInterval;
    private double direction;
    Random rnd = new Random();
    SimArea sa;
    Sim_normal_obj normalRandom;
    int scale;

    public MobilityModel(double timeInterval, double minSpeed, double maxSpeed, SimArea sa, int scale) {
        this.timeInterval = timeInterval;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.sa = sa;
        this.scale = scale;
        normalRandom = new Sim_normal_obj("normal", 0, .009);

    }
    private double minSpeed;
    private double maxSpeed;

    /**
     * @return the timeInterval
     */
    public double getTimeInterval() {
        return timeInterval;
    }

    /**
     * @param timeInterval the timeInterval to set
     */
    public void setTimeInterval(double timeInterval) {
        this.timeInterval = timeInterval;
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
     * @return the minSpeed
     */
    public double getMinSpeed() {
        return minSpeed;
    }

    /**
     * @param minSpeed the minSpeed to set
     */
    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    /**
     * @return the maxSpeed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param maxSpeed the maxSpeed to set
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public MobilityInfo updateModel(Coordinates currentLocation) {

        double speed = minSpeed + rnd.nextDouble() * (maxSpeed - minSpeed);// +normalRandom.sample();
        double distance = speed * timeInterval;

        direction = rnd.nextDouble() * 360 * Math.PI / 180;

        Coordinates pActual;
        Coordinates pIMU;
        MobilityInfo mInfo = null;

        if (direction < 0 || direction > 2 * Math.PI) {
            System.out.println();
        }

        if (direction >= 0 && direction <= Math.PI / 2) {
            double x = distance * Math.cos(direction);
            double y = distance * Math.sin(direction);
            double xIMU = x + normalRandom.sample();
            double yIMU = y + normalRandom.sample();


            pActual = new Coordinates((x + currentLocation.x), (currentLocation.y - y));
            pIMU = new Coordinates((xIMU + currentLocation.x), (currentLocation.y - yIMU));


            if (sa.isInsideSimArea(pActual) && sa.isInsideSimArea(pIMU)) {
                mInfo = new MobilityInfo(speed, distance, currentLocation);
                mInfo.setNewActualPosition(pActual);
                mInfo.setNewIMUPosition(pIMU);
                mInfo.setDirection(direction);
                mInfo.setxIMUDistance(xIMU);
                mInfo.setyIMUDistance(-1 * yIMU);

                return mInfo;
            }

        }

        if (direction > (Math.PI / 2) && direction <= Math.PI) {

            direction = Math.PI - direction;
            direction = Math.PI / 2 - direction;
            double x = distance * Math.sin(direction);
            double y = distance * Math.cos(direction);
            double xIMU = x + normalRandom.sample();
            double yIMU = y + normalRandom.sample();

            pActual = new Coordinates((currentLocation.x - x), (currentLocation.y - y));
            pIMU = new Coordinates((currentLocation.x - xIMU), (currentLocation.y - yIMU));

            if (sa.isInsideSimArea(pActual) && sa.isInsideSimArea(pIMU)) {
                mInfo = new MobilityInfo(speed, distance, currentLocation);
                mInfo.setNewActualPosition(pActual);
                mInfo.setNewIMUPosition(pIMU);
                mInfo.setDirection(direction);
                mInfo.setxIMUDistance(-1 * xIMU);
                mInfo.setyIMUDistance(-1 * yIMU);
                return mInfo;
            }
        }

        if (direction > Math.PI && direction <= 3 * Math.PI / 2) {

            direction -= Math.PI;

            double x = distance * Math.cos(direction);
            double y = distance * Math.sin(direction);
            double xIMU = x + normalRandom.sample();
            double yIMU = y + normalRandom.sample();


            pActual = new Coordinates((currentLocation.x - x), (currentLocation.y + y));
            pIMU = new Coordinates((currentLocation.x - xIMU), (currentLocation.y + yIMU));

            if (sa.isInsideSimArea(pActual) && sa.isInsideSimArea(pIMU)) {
                mInfo = new MobilityInfo(speed, distance, currentLocation);
                mInfo.setNewActualPosition(pActual);
                mInfo.setNewIMUPosition(pIMU);
                mInfo.setDirection(direction);
                mInfo.setxIMUDistance(-1 * xIMU);
                mInfo.setyIMUDistance(yIMU);
                return mInfo;
            }

        }


        if (direction > 3 * Math.PI / 2 && direction <= 2 * Math.PI) {

            direction = 2 * Math.PI - direction;
            direction = Math.PI / 2 - direction;
            double x = distance * Math.sin(direction);
            double y = distance * Math.cos(direction);
            double xIMU = x + normalRandom.sample();
            double yIMU = y + normalRandom.sample();

            pActual = new Coordinates((currentLocation.x + x), (currentLocation.y + y));
            pIMU = new Coordinates((currentLocation.x + xIMU), (currentLocation.y + yIMU));


            if (sa.isInsideSimArea(pActual) && sa.isInsideSimArea(pIMU)) {
                mInfo = new MobilityInfo(speed, distance, currentLocation);
                mInfo.setNewActualPosition(pActual);
                mInfo.setNewIMUPosition(pIMU);
                mInfo.setDirection(direction);
                mInfo.setxIMUDistance(xIMU);
                mInfo.setyIMUDistance(yIMU);
                return mInfo;

            }
        }

        return null;
    }
}
