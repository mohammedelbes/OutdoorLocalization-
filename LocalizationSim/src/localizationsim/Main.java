/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;

import javax.swing.JFrame;
 
/**
 *
 * @author Admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        MainFrame mainFrame = new MainFrame();
        Thread visual = new Thread(mainFrame);
        visual.start();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1400, 1000);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

    }
}
