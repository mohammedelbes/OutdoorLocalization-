/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;
 
import eduni.simjava.distributions.Sim_normal_obj;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * This class encapsulates a particle filter for which a mobile device will
 * use to estimate its position.
 * @author Andrew Hovingh
 */
public class Filter implements Iterable<Filter.Particle> {

    TrainingData trainingData;
    ArrayList<AccessPoint> accessPoints;
    MobilityInfo mInfo;
    MovingObject laptop;
    ArrayList<Integer> fp = new ArrayList<Integer>();
    int range; // 0 short , 1 medium , 2 long
    int SHORT_RANGE = 30;
    int MEDIUM_RANGE = 60;
    double alpha = .25;
    Sim_normal_obj mUNormalNoise;     // for the weight of particle
    Sim_normal_obj particlePosNormalNoise;
    Sim_normal_obj normalRandom = new Sim_normal_obj("Random", 0, .00000001);
    int scale;
    boolean firstAdd = true;
    /** Default number of particles to use in a particle filter
     * when estimating location. This is used on constructors that don't require
     * the number of particles. */
    public static int defaultNumParticles = 80;
    /**
     * Limit on the number of recursive calls to estimate position that can be
     * made before an estimate is decided on
     */
    Sim_normal_obj particlePosNormalNoiseFine, particlePosNormalNoiseCourse;
    public static int estimateRecursiveLimit = 5;
    /** Percentage of particles for which should have non-zero weights before
     * making an estimate, otherwise a recursive call to estimate position will
     * be made (if the limit has not been reached) */
    public static double nonzeroParticlesThreshold = 0.1;
    private Coordinates estimation;
    // NON-STATIC OR INSTANCE CONTENT
    //**************************************************************************
    /** The particles used by the filter to estimate location */
    private Particle[] particles;
    /** The estimated position of the device using the filter */
    private Coordinates position;
    //Coordinates estimation = null;
    double distanceNoise;
    SimArea sa;

    /** Object to supply random numbers */
    public Filter(TrainingData tariningData, ArrayList<AccessPoint> accessPoints, MovingObject laptop, int scale, SimArea sa) {
       
        this.trainingData = tariningData;
        this.accessPoints = accessPoints;
        this.scale = scale;
        this.laptop = laptop;
        position = new Coordinates(0, 0);
     
        mUNormalNoise = new Sim_normal_obj("normal", 0, 5);
        //  particlePosNormalNoise = new Sim_normal_obj("ppns", 0, 600);
        this.sa = sa;

        particlePosNormalNoiseFine = new Sim_normal_obj("ppnsf", 0, 10000);
        particlePosNormalNoiseCourse = new Sim_normal_obj("ppnsc", 0, 20000);
        particles = new Particle[defaultNumParticles];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }

        Random rnd = new Random();

        int multiplier;
        double temp = rnd.nextDouble();

        multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
        int x = (int) (sa.getBounds().width / 2 + multiplier * rnd.nextDouble() * sa.getBounds().width / 4);

        multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
        int y = (int) (sa.getBounds().height / 2 + multiplier * rnd.nextDouble() * sa.getBounds().height / 4);
        estimation = new Coordinates(x, y);
        // addEstimationLabel();
    }

    Coordinates GetEstimatedPosition(MobilityInfo mInfo, boolean useDE, double[] prob) throws InterruptedException {
        Coordinates estimate = new Coordinates();
        this.mInfo = mInfo;
        estimatePosition(estimateRecursiveLimit, mInfo, useDE, prob);
        estimate.x = getEstimation().x;
        estimate.y = getEstimation().y;
        return estimate;
    }

    public void estimatePosition(int recursionLimit, MobilityInfo mInfo, boolean useDE, double[] prob) throws InterruptedException {


        double totalWeights = 0;

        Random fineOrCourse = new Random();

        if (recursionLimit < 1) {

            return;
        }

        for (int i = 0; i < particles.length; i++) {


            if(!useDE)
            {
            if (fineOrCourse.nextDouble() > 0) {
                particles[i].coordinates.x = getEstimation().x + mInfo.getxIMUDistance() + particlePosNormalNoiseFine.sample();
                particles[i].coordinates.y = getEstimation().y + mInfo.getyIMUDistance() + particlePosNormalNoiseFine.sample();
            } else {
                particles[i].coordinates.x = getEstimation().x + mInfo.getxIMUDistance() + particlePosNormalNoiseCourse.sample();
                particles[i].coordinates.y = getEstimation().y + mInfo.getyIMUDistance() + particlePosNormalNoiseCourse.sample();

            }
            }
            else
            {
                    if (fineOrCourse.nextDouble() > 0.5) {
                particles[i].coordinates.x = getEstimation().x + particlePosNormalNoiseFine.sample();
                particles[i].coordinates.y = getEstimation().y + particlePosNormalNoiseFine.sample();
            } else {
                particles[i].coordinates.x = getEstimation().x + particlePosNormalNoiseCourse.sample();
                particles[i].coordinates.y = getEstimation().y + particlePosNormalNoiseCourse.sample();

            }
            }
            //
            if (particles[i].coordinates.x<0) particles[i].coordinates.x=0;
            if (particles[i].coordinates.y<0) particles[i].coordinates.y=0;
            
            int index = getDistanceFromtraining(particles[i]);
            //  System.out.println(index);
          
            double avg = 1 / calculateRSSIDistance(laptop.shifts, trainingData.getTrainingData().get(index));

            //avg = useDE ? avg * prob[index] : avg;

            particles[i].weight = avg;
            particles[i].closest=index;
            totalWeights += particles[i].weight;
        }
 

        // normalize the weights
        for (int i = 0; i < particles.length; i++) {
            particles[i].weight /= totalWeights;
          //  System.out.println(particles[i].weight);
        }
  //visualiseParticles(!useDE);
         //re-sample the particles based on the weights
        totalWeights = 0;
        int nonzeroparticleCount = 0;
        for (int i = 0; i < particles.length; i++) {
            particles[i].weight = Math.abs((particles[i].weight * particles.length));
            if (particles[i].weight < 1) {
                particles[i].weight = 0.0;
            } else {
                ++nonzeroparticleCount;
            }
            totalWeights += particles[i].weight;
        }

        double sum = 0;
        for (int i = 0; i < particles.length; i++) {
            particles[i].weight /= totalWeights;
            sum += particles[i].weight;
        }
        totalWeights -= sum;

        if (recursionLimit > 0 && ((double) nonzeroparticleCount / (double) particles.length) < nonzeroParticlesThreshold) {
            estimatePosition(recursionLimit - 1, mInfo, useDE, prob);
        } else {

            position.clear();
            for (int i = 0; i < particles.length; i++) {
                position.add(particles[i].coordinates.x * particles[i].weight, particles[i].coordinates.y * particles[i].weight);
       //     }

            getEstimation().mimic(position);
        }
    }
    }
    private int getDistanceFromtraining(Particle particle) {

        int ROWS = trainingData.getTrainingData().size();
         

        double distance = Double.POSITIVE_INFINITY;
        int smallestIndex = 0;

        // Calculating distance of each row
        for (int i = 0; i < ROWS; i++) {
            double d = Math.sqrt(Math.pow((particle.coordinates.x  - trainingData.getCoordinates()[i][0] ), 2) + Math.pow((particle.coordinates.y - trainingData.getCoordinates()[i][1]  ), 2));

            if (d< distance) {
                distance = d;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    private double calculateRSSIDistance(double[][] laptopshifts, double[][] closestTraining) {
        double distance = 0;
        int COLS = accessPoints.size();
        
        for (int j = 0; j < COLS; j++)  
          for(int k=0;k<COLS;k++) 
                        
            distance += Math.pow(laptopshifts[k][j] - closestTraining[k][j], 2);
        
        
        
        return Math.sqrt(distance);
    }

    private void visualiseParticles(boolean useDE) {

        if (!useDE) {
            return;
        }
        Component[] comps = sa.getComponents();
        int last = comps.length - defaultNumParticles;

        if (!firstAdd) {
            for (int j = last; j < comps.length; j++) {
                if (comps[j] instanceof JLabel) {

                    if (((JLabel) (comps[j])).getText().equals("p" + (j - last))) {
                        sa.remove(last);
                        //  sa.validate();

                    }

                }
            }

        }


        double minWeight = getMinPartivleWeight();
        double maxWeight = getMaxParticleWeight();
        double oldRange = maxWeight - minWeight;
        double newRange = 255 - 0;

        int[] rangedWeights = new int[defaultNumParticles];
        for (int i = 0; i < particles.length; i++) {
            rangedWeights[i] = (int) ((((particles[i].weight - minWeight) * newRange) / oldRange) + 0);

        }

        for (int i = 0; i < particles.length; i++) {

            JLabel p = new JLabel();
            Color c;
            p.setText("p"+i);
            p.setBorder(BorderFactory.createEtchedBorder());
           
            int xx = (int) particles[i].getpX()<0?0:(int) particles[i].getpX();
            
             int yy = (int) particles[i].getpY()<0?0:(int) particles[i].getpY();
            
            
            p.setBounds(xx, yy, 3, 3);
              
           
            if (rangedWeights[i] == 0) {
                c = new Color(0, 0, 0);
            } else {
                c = new Color(255 - rangedWeights[i], rangedWeights[i], 0);
            }

            p.setBackground(c);

            p.setVisible(true);
            sa.add(p);


        }
        sa.invalidate();
        sa.repaint();
        firstAdd = false;
    }

    private void visualliseEstimation() {

        JLabel est = (JLabel) sa.getComponent(sa.getComponentCount() - defaultNumParticles - 1);
        est.setBounds((int) getEstimation().x, (int) getEstimation().y, 25, 15);

    }

    private void addEstimationLabel() {

        JLabel p = new JLabel();
        p.setText("est");
        p.setBorder(BorderFactory.createEtchedBorder());
        p.setBounds((int) getEstimation().x, (int) getEstimation().y, 25, 15);
        p.setBackground(Color.blue);

        p.setVisible(true);
        sa.add(p);
    }

    private double getMinPartivleWeight() {

        double min = Double.POSITIVE_INFINITY;

        for (int i = 0; i < particles.length; i++) {
            if (particles[i].weight < min) {
                min = particles[i].weight;
            }
        }

        return min;
    }

    private double getMaxParticleWeight() {

        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < particles.length; i++) {
            if (particles[i].weight > max) {
                max = particles[i].weight;
            }
        }

        return max;
    }

    /**
     * @return the estimation
     */
    public Coordinates getEstimation() {
        return estimation;
    }

    /**
     * @param estimation the estimation to set
     */
    public void setEstimation(Coordinates estimation) {
        this.estimation = estimation;
    }

    // STATIC CONTENT
    //**************************************************************************
    /**
     * Encapsulates the components of a particular particle/component in the
     * filter system
     */
    public class Particle {

        /** The position of the particle */
        public Coordinates coordinates;
        /** The weight of the particle (range of 0-1)*/
        public double weight;
        private Row particleRSSI;
        int closest=0;
        /**
         * Copies the contents of another instance of particle to this one, so
         * that it is a clone of the other.
         * @param other The other particle to copy from, duplicate, or mimic
         */
        public void mimic(Particle other) {
            this.coordinates.mimic(other.coordinates);
            this.weight = other.weight;
        }

        /**
         * Gets the x position coordinate of the particle
         */
        public double getpX() {
            return coordinates.x;
        }

        /**
         * Gets the y position coordinate of the particle
         */
        public double getpY() {
            return coordinates.y;
        }

        public Particle() {
            coordinates = new Coordinates(0, 0);
            particleRSSI = new Row();
        }

        /**
         * @return the particleRSSI
         */
        public Row getParticleRSSI() {
            return particleRSSI;
        }

        /**
         * @param particleRSSI the particleRSSI to set
         */
        public void setParticleRSSI(Row particleRSSI) {
            this.particleRSSI = particleRSSI;
        }
    }

    /**
     * Gets the filter's current estimated position
     * @param coordinates The coordinates to set to the estimated position
     */
    public void getCoordinates(Coordinates coordinates) {
        coordinates.mimic(position);
    }

    /**
     * Gets a copy of the filter's current estimated position
     * @return The particle filter's current estimated position
     */
    public Coordinates getCoordinates() {
        return position.clone();
    }

    /**
     * Gets the estimated x position coordinate
     */
    public double getX() {
        return position.x;
    }

    /**
     * Gets the estimated y position coordinate
     */
    public double getY() {
        return position.y;
    }

    /**
     * Sets the coordinates of the estimated position.
     */
    public void setCoordinates(Coordinates newCoordinates) {
        position.mimic(newCoordinates);
    }

    /**
     * Get the number of particles in the filter.
     */
    public int getNumberOfParticles() {
        return particles.length;
    }

    /**
     * Allows iterating through all the particles in the filter.
     */
    public Iterator<Filter.Particle> iterator() {
        return new Iterator<Filter.Particle>() {

            private int index = 0;
            private Filter.Particle particleInstance;

            {
                index = 0;
                particleInstance = new Filter.Particle();
            }

            public boolean hasNext() {
                return index < particles.length;
            }

            /**
             * Returns a copy of the next particle, not the particle itself.
             * ONLY READ FROM THIS PARTICLE, as the particle will likely change
             * internally. This is meant to be a READ-ONLY variable. BAD THINGS
             * WILL HAPPEN IF YOU WRITE TO THE RETURNED VARIABLE
             */
            public Filter.Particle next() {
                particleInstance.mimic(particles[index++]);
                return particleInstance;
            }

            /**
             * NOT SUPPORTED. Client code may not alter the internal particles
             */
            public void remove() {
                throw new UnsupportedOperationException(
                        "remove() cannot be done with Particle iterator.");
            }
        };
    }
}
