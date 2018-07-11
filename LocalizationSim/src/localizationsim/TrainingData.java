/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import eduni.simjava.distributions.Sim_normal_obj;

/**
 *
 * @author Admin
 */
public class TrainingData {
  double LOW_NOISE_FACTOR = 1, HIGH_NOISE_FACTOR = 7;
    int NUM_OF_NOISY_STATIONS = 1;
    private int ROWS; // The number of training points
    private int COLS; // The number of access points (AP)
    private double[][] coordinates;
    private ArrayList<double[][]> trainingDataValues;
    int scale;
    private String name;



    public TrainingData(int ROWS, int COLS , int scale) {
        this.ROWS = ROWS;
        this.COLS = COLS;
        trainingDataValues = new ArrayList<double[][]>();
        coordinates = new double[ROWS][2];     
       this.scale=scale;

        
    }

    public void generateTrainingData(ArrayList<AccessPoint> accessPoints, Point lowerBounds, Point upperBounds, MobilityInfo mInfo) {

        Random rnd = new Random();
  
//
         Point[] trainingLocations= getTrainingDataLocations(lowerBounds,upperBounds);

        for (int i = 0; i < ROWS; i++) {
         
          

            Point pp =trainingLocations[i];
            Coordinates cc = new Coordinates(pp.x, pp.y);
            mInfo.setNewActualPosition(cc);
            getDistancesFromBaseStations(accessPoints,mInfo);
                     
            ///////////////////////
            
            
            double[][]  shifts = new double[COLS][COLS];


        for (int ii = 0; ii < COLS; ii++) {
            for (int j = 0; j < COLS; j++) {
                if (ii == j) {
                    shifts[ii][j] = 0.0;
                } else {

                    if (ii > j) {
                        shifts[ii][j] = -1 * shifts[j][ii];
                        continue;
                    }
                    shifts[ii][j] = getPHI(ii, j,mInfo);

                }
            }
        }
            ///////////////////////////
            
            this.getCoordinates()[i][0]=pp.x;
            this.getCoordinates()[i][1]=pp.y;
            
            trainingDataValues.add(shifts);
            
        }
        
        
    }

    private double getDistance(Coordinates location, Coordinates p) {
        return Math.sqrt(Math.pow((location.x/scale - p.x/scale), 2) + Math.pow((location.y/scale - p.y/scale), 2));
    }

    /**
     * @return the trainingData
     */
    public ArrayList<double[][]> getTrainingData() {
        return trainingDataValues;
    }

    /**
     * @param trainingData the trainingData to set
     */
    public void setTrainingData(ArrayList<double[][]> trainingData) {
        this.trainingDataValues = trainingData;
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

    private Point[] getTrainingDataLocations(Point lowerBounds, Point upperBounds) {

        //
        double totalWidth = upperBounds.x - lowerBounds.x;
        double totalHeight = upperBounds.y - lowerBounds.y;

        int countHor = (int) Math.floor(Math.sqrt(ROWS));
        int countVer = (int) Math.ceil((double) ROWS / (double) countHor);

        double subWidth = totalWidth / countHor;
        double subHeight = totalHeight / countVer;

        Point[] locations = new Point[countHor * countVer];

        for (int i = 0; i < countVer; i++) {
            for (int j = 0; j < countHor; j++) {
                Point p = new Point();
                p.x = (int) ((j * subWidth + subWidth / 2));
                p.y = (int) ((i * subHeight + subHeight / 2));
                locations[i * countHor + j] = p;
            }
        }

        return locations;

    }
    
    
        private double getPHI(int i, int j , MobilityInfo mInfo) {

        double di = mInfo.noisyDistancesFromBSs[i];
        double dj =  mInfo.noisyDistancesFromBSs[j];

        double phaseShift = (di - dj);
        return (phaseShift);

    }
        private void getDistancesFromBaseStations(ArrayList<AccessPoint> accessPoints,  MobilityInfo mInfo) {

        mInfo.noisyDistancesFromBSs = new double[accessPoints.size()];
        mInfo.realDistancesFromBSs = new double[accessPoints.size()];
Random phaseRandom = new Random();
        for (int i = 0; i < accessPoints.size(); i++) {
            double di = Math.sqrt(Math.pow(mInfo.getNewActualPosition().x - accessPoints.get(i).getLocation().x, 2) + Math.pow(mInfo.getNewActualPosition().y - accessPoints.get(i).getLocation().y, 2));

            mInfo.realDistancesFromBSs[i] = di;

            double factor =  phaseRandom.nextDouble() > .5 ? -1 : 1;

            if (accessPoints.get(i).isNoisy()) {

                double offset = accessPoints.get(i).dcOffset + HIGH_NOISE_FACTOR * factor * phaseRandom.nextDouble();
                di = di + offset;
                accessPoints.get(i).setOffset(offset);
            } else {
                double offset = 1 + LOW_NOISE_FACTOR * factor * phaseRandom.nextDouble();
                di += offset;
                accessPoints.get(i).setOffset(offset);
            }

            mInfo.noisyDistancesFromBSs[i] = di;

        }
    }

    /**
     * @return the coordinates
     */
    public double[][] getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }
}
