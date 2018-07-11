 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package localizationsim;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */

    public class APEntry
    {
        private int AP_ID;

        private ArrayList<APSubEntry> apSubEntries;

        public APEntry(int AP_ID) {
            this.AP_ID = AP_ID;
            apSubEntries = new ArrayList<APSubEntry>();

        }

        /**
         * @return the AP_ID
         */
        public int getAP_ID() {
            return AP_ID;
        }

        /**
         * @param AP_ID the AP_ID to set
         */
        public void setAP_ID(int AP_ID) {
            this.AP_ID = AP_ID;
        }

        /**
         * @return the apSubEntries
         */
        public ArrayList<APSubEntry> getApSubEntries() {
            return apSubEntries;
        }

        /**
         * @param apSubEntries the apSubEntries to set
         */
        public void setApSubEntries(ArrayList<APSubEntry> apSubEntries) {
            this.apSubEntries = apSubEntries;
        }

    }