/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package localizationsim;
 
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public  class LabelHandlerClass implements MouseListener {

    SimArea sa;
    ArrayList<AccessPoint> accessPoints;
    JFrame frame;
    JPanel main;


    public LabelHandlerClass(SimArea sa, ArrayList<AccessPoint> accessPoints, JFrame frame ,JPanel main) {
        this.sa = sa;
        this.accessPoints = accessPoints;
        this.frame = frame;
        this.main=main;
    }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JLabel l = (JLabel) e.getComponent();
                sa = (SimArea) frame.getContentPane().getComponent(1);

                String ID = l.getName();

                for (int i = 0; i < accessPoints.size(); i++) {
                    if (accessPoints.get(i).getID() == Integer.parseInt(ID)) {
                        accessPoints.remove(i);
                    }
                }
                sa.remove(l);

                Component[] comps = sa.getComponents();


                for (int i = 0; i < accessPoints.size(); i++) {
                    ((JLabel) comps[i]).setName("" + i);
                    ((JLabel) comps[i]).setText("" + i);
                    accessPoints.get(i).setID(i);
                }
                main.validate();
                frame.setContentPane(main);
                frame.validate();
            }


        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }