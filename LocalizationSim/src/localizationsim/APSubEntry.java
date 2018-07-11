/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package localizationsim;
 
/**
 *
 * @author Admin
 */
public class APSubEntry
    {
        private int range;
        private int N;
        private double sigma;
        private double deltaRoverDeltaD;

        /**
         * @return the range
         */
        public int getRange() {
            return range;
        }

        /**
         * @param range the range to set
         */
        public void setRange(int range) {
            this.range = range;
        }

        /**
         * @return the N
         */
        public int getN() {
            return N;
        }

        /**
         * @param N the N to set
         */
        public void setN(int N) {
            this.N = N;
        }

        /**
         * @return the sigma
         */
        public double getSigma() {
            return sigma;
        }

        /**
         * @param sigma the sigma to set
         */
        public void setSigma(double sigma) {
            this.sigma = sigma;
        }

        /**
         * @return the deltaRoverDeltaD
         */
        public double getDeltaRoverDeltaD() {
            return deltaRoverDeltaD;
        }

        /**
         * @param deltaRoverDeltaD the deltaRoverDeltaD to set
         */
        public void setDeltaRoverDeltaD(double deltaRoverDeltaD) {
            this.deltaRoverDeltaD = deltaRoverDeltaD;
        }
    }

