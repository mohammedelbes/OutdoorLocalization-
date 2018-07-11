///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package localizationsim;
//
//import java.awt.MouseInfo;
//import java.awt.Point;
//import java.awt.Toolkit;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
///**
// * 
// * @author Admin
// */
//public class SimAreaHandlerClass implements MouseListener {
//
////    SimArea sa;
////    ArrayList<AccessPoint> accessPoints;
////    JFrame frame;
////    JPanel main;
////    LabelHandlerClass lhandler;
////    final ImageIcon icon = createImageIcon("a.gif");
////
////    public SimAreaHandlerClass(SimArea sa, ArrayList<AccessPoint> accessPoints, JFrame frame, JPanel main, LabelHandlerClass lhandler) {
////        this.sa = sa;
////        this.accessPoints = accessPoints;
////        this.frame = frame;
////        this.main = main;
////        this.lhandler = lhandler;
////    }
////
////    public void mouseClicked(MouseEvent e) {
////
////        if (!sa.isInsideSimArea(MouseInfo.getPointerInfo().getLocation()) || !sa.isClickable()) {
////            Toolkit.getDefaultToolkit().beep();
////            return;
////        }
////        JLabel l = new JLabel(icon);
////        l.addMouseListener(lhandler);
////        main = (JPanel) frame.getContentPane();
////        sa = (SimArea) main.getComponent(1);
////        Point mouseLocatoin = new Point(e.getX(), e.getY());// MouseInfo.getPointerInfo().getLocation();
////
////        l.setBounds(mouseLocatoin.x, mouseLocatoin.y, 50, 60);
////        l.setVisible(true);
////        l.setName("" + accessPoints.size());
////        l.setText("" + accessPoints.size());
////        l.setToolTipText("( " + mouseLocatoin.x + " , " + mouseLocatoin.y + " )");
////        AccessPoint ap = new AccessPoint(mouseLocatoin, accessPoints.size());
////        accessPoints.add(ap);
////        sa.add(l);
////        sa.validate();
////        frame.setContentPane(main);
////        frame.validate();
////
////    }
////
////    public void mousePressed(MouseEvent e) {
////    }
////
////    public void mouseReleased(MouseEvent e) {
////    }
////
////    public void mouseEntered(MouseEvent e) {
////    }
////
////    public void mouseExited(MouseEvent e) {
////    }
////
////    protected static ImageIcon createImageIcon(String path) {
////        java.net.URL imgURL = MainFrame.class.getResource(path);
////        if (imgURL != null) {
////            return new ImageIcon(imgURL);
////        } else {
////            System.err.println("Couldn't find file: " + path);
////            return null;
////        }
////    }
//}
