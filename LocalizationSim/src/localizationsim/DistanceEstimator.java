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
public class DistanceEstimator {

    MovingObject laptop;
    ArrayList<AccessPoint> accessPoints;
    TrainingData trainingData;
    private double[] probabilities;
    ArrayList <Integer> execluded ;
    double execlusionRate;
    int [] total ;//=new int [accessPoints.size()];
    double [] prev;// = new double [ROWS];

    
    public DistanceEstimator(MovingObject laptop, ArrayList<AccessPoint> accessPoints, TrainingData trainingData) {
        this.laptop = laptop;
        this.accessPoints = accessPoints;
        this.trainingData = trainingData;
        execluded= new ArrayList<Integer>();
        total =new int [accessPoints.size()];
    }

   public Coordinates findXY(int technique, MovingObject laptop) {


        double xCoord = 0;
        double yCoord = 0;
        int ROWS = trainingData.getTrainingData().size();
        int COLS = accessPoints.size();
        double freq = 0, weights = 0;
        double[] distances = new double[ROWS];
        double[] weight = new double[ROWS];
        probabilities = new double[ROWS];

        if(technique==0) prev = new double [ROWS];

        execluded.clear();
        // Calculating distance of each row
        for (int i = 0; i < ROWS; i++) {
            distances[i] = Math.sqrt(calculateDistance(i));

            // Get a better estimate using dynamic exclusion heuristic
          //    distances[i] = (technique==0)? distances[i]: refineDistanceEstimate(distances[i], i);
//              for(int j=0;j<execluded.size();j++)
//                  total[execluded.get(j)]+=1;
            //   execluded.clear();
            // Handling a special case (i.e. division by zero in the next step)
            if (distances[i] == 0) {
                distances[i] = 0.0001;
            }
        }
double max= Double.NEGATIVE_INFINITY;
        // Calculating weights for each row
int index=0;        
for (int i = 0; i < ROWS; i++) {
            weight[i] = 1 / distances[i];
            if(weight[i]>max)
            {max=weight[i];
            index=i;}
        }
 
        // Calculating the frequency
        for (int i = 0; i < ROWS; i++) {
            weights += weight[i];
        }
        freq = 1 / weights;

        // Calculating probability for each row
        for (int k = 0; k < ROWS; k++) {
            probabilities[k] = freq * weight[k];
//            if(technique==0)
//                prev[k]=probabilities[k];
//            else
//                System.out.println(k+"  "+"EUC "+prev[k]+" DE  "+probabilities[k]);
        }

        // Finally, deduce the x and y coordinate, using weighted/probability sum
        for (int i = 0; i < ROWS; i++) {
            xCoord += (trainingData.getCoordinates()[i][0]* probabilities[i]);
            yCoord += (trainingData.getCoordinates()[i][1] * probabilities[i]);
        }
 
        double sum=0;
        if(technique==1)
        for(int i=0;i<accessPoints.size();i++)
        {
          // System.out.println(total[i]);
            sum += total[i];
       }
       execlusionRate = sum /accessPoints.size();
       
     //  System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Coordinates p = new Coordinates(xCoord, yCoord);


        return p;
    }



    
    ////////////////////

    private double calculateDistance(int row) {
        int ROWS = trainingData.getTrainingData().size();
        int COLS = accessPoints.size();
        double distance = 0;

     
         for (int j = 0; j < COLS; j++) {
         for (int i = 0; i < COLS; i++) {
       
             distance += Math.pow(trainingData.getTrainingData().get(row)[i][j] - laptop.shifts[i][j], 2);
        }}
        

        return distance;
    }

    /**
     * @return the probabilities
     */
    public double[] getProbabilities() {
        return probabilities;
    }

    /**
     * @param probabilities the probabilities to set
     */
    public void setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
    }



















    }



