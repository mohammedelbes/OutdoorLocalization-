/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;

import eduni.simjava.distributions.Sim_normal_obj;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
 
/**
 *
 * @author Admin
 */
public class MovingObject extends JLabel {

    private Row currentRSSI;
    private Row previousRSSI;
    private Coordinates myLocation;
    private Coordinates previousLocation;
    private ArrayList<AccessPoint> accessPoints;
    int scale;
double [][] shifts;
    MovingObject(ImageIcon img, ArrayList<AccessPoint> accessPoints, Coordinates Location, int scale) {
        this.setIcon(img);
        this.accessPoints = accessPoints;
        this.myLocation = Location;
        this.currentRSSI = new Row();
        previousRSSI = new Row();
       
        this.scale = scale;


    }

    /**
     * @return the myLocation
     */
    public Coordinates getMyLocation() {
        return myLocation;
    }

    /**
     * @param myLocation the myLocation to set
     */
    public void setMyLocation(Coordinates myLocation) {
        this.myLocation = myLocation;
    }

    /**
     * @return the currentRSSI
     */


    private double getDistance(Coordinates location, Coordinates p) {
         return Math.sqrt(Math.pow((location.x/scale - p.x/scale), 2) + Math.pow((location.y/scale - p.y/scale), 2));
      // return Math.sqrt(Math.pow((location.x - p.x), 2) + Math.pow((location.y - p.y), 2)/scale);
    }

    /**
     * @return the previousLocation
     */
    public Coordinates getPreviousLocation() {
        return previousLocation;
    }

    /**
     * @param previousLocation the previousLocation to set
     */
    public void setPreviousLocation(Coordinates previousLocation) {
        this.previousLocation = previousLocation;
    }

    /**
     * @return the previousRSSI
     */
    public Row getPreviousRSSI() {
        return previousRSSI;
    }

    /**
     * @param previousRSSI the previousRSSI to set
     */
    public void setPreviousRSSI(Row previousRSSI) {
        this.previousRSSI = previousRSSI;
    }
    
    
      void getPhaseShiftFromBaseStations(ArrayList<AccessPoint> APs, MobilityInfo mInfo) {

       this.shifts = new double[APs.size()][APs.size()];

        for (int i = 0; i < APs.size(); i++) {
            for (int j = 0; j < APs.size(); j++) {
                if (i == j) {
                    shifts[i][j] = 0.0;
                } else {

                    if (i > j) {
                        shifts[i][j] = -1 * shifts[j][i];
                        continue;
                    }
                    shifts[i][j] = getPHI(i, j,mInfo);

                }
            }
        }

        
    }

    private double getPHI(int i, int j , MobilityInfo mInfo) {

        double di = mInfo.noisyDistancesFromBSs[i];
        double dj =  mInfo.noisyDistancesFromBSs[j];

        double phaseShift = (di - dj);
        return (phaseShift);

    }

}
