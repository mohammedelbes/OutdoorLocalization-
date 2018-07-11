/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizationsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class MainFrame extends JFrame implements Runnable {

    double LOW_NOISE_FACTOR = 1, HIGH_NOISE_FACTOR = 7;
    int NUM_OF_NOISY_STATIONS = 1;
    ArrayList<Integer> picked = new ArrayList<Integer>();
    ArrayList<Integer> noisyGuys = new ArrayList<Integer>();
    final ImageIcon noisy = createImageIcon("noisy.gif");
    int i = 0;
    JPanel simArea = new JPanel();
    JPanel main = new JPanel();
    JPanel up = new JPanel();
    JPanel down = new JPanel();
    JPanel left = new JPanel();
    JPanel subLeftNorth = new JPanel();
    JPanel subLeftCenter = new JPanel();
    JPanel subLeftSouth = new JPanel();
    JButton train = new JButton("Train");
    JButton start = new JButton("start");
    JTextField numberOfTrainData = new JTextField();
    JTextField numberofAP = new JTextField();
    int scaleFactor;
    final ImageIcon icon = createImageIcon("ap.gif");
    final ImageIcon laptopImg = createImageIcon("actual.gif");
    final ImageIcon tdImg = createImageIcon("td.gif");
    final ImageIcon pf = createImageIcon("a.gif");
    LabelHandlerClass lhandler;
    ArrayList<AccessPoint> accessPoints;
    TrainingData trainingData;
    SimArea sa = new SimArea();
    JButton init = new JButton();
    JButton setupAP = new JButton("Setup AP");
    MovingObject laptop;
    JComboBox technique = new JComboBox();
    JComboBox simSpeed = new JComboBox();
    JLabel actualPosion = new JLabel(" Actual position");
    JLabel estimatedEUC = new JLabel("Estimated EUC");
    JLabel estimatedRAWPFLABEL = new JLabel("Estimated DE");
    JLabel estimatedPF = new JLabel(" Estimared PF");
    JLabel errorEUC = new JLabel("EUC Error");
    JLabel errorPF = new JLabel("PF Error");
    JLabel errorDE = new JLabel("DE Error");
    DistanceEstimator distanceEstimator;
    JLabel estimatedLabelEUC = new JLabel("EUC");
    JLabel estimatedRAWPF = new JLabel("RAWPF");
    JLabel estimatedLabelPF = new JLabel("PF");
    boolean firstRun;
    Filter particleFilter, particleFilterDE;
    boolean simulationStarted = false;
    boolean trained = false;
    MobilityModel mobilityModel;
    int iterationCount = 0;
    double eucError = 0;
    double rawPFERROR = 0;
    double pfError = 0;
   
    MobilityInfo mInfo;
    double[] eucErros, rawPFERRORs, pfErros, pfOnlyErrors, EUC, RAWPF, PF;
    int counter = 0, gcounter = 0;
    double eucCounter20 = 0, RAWPFCounter20 = 0, pfCounter20 = 0, eucCounter40 = 0, RAWPFCounter40 = 0, pfCounter40 = 0, eucCounter60 = 0, RAWPFCounter60 = 0, pfCounter60 = 0, eucCounter100 = 0, RAWPFCounter100 = 0, pfCounter100 = 0, eucCounter200 = 0, RAWPFCounter200 = 0, pfCounter200 = 0, eucCounter300 = 0, RAWPFCounter300 = 0, pfCounter300 = 0, eucCounter500 = 0, RAWPFCounter500 = 0, pfCounter500 = 0, eucCounter400 = 0, RAWPFCounter400 = 0, pfCounter400 = 0;
 int pftime =0;
    public MainFrame() {
        super("RSSiSIM");
        accessPoints = new ArrayList<AccessPoint>();
        firstRun = true;
        buildVisuals();
        eucErros = new double[5000];
        rawPFERRORs = new double[5000];
        pfErros = new double[5000];
        EUC = new double[5000];
        RAWPF = new double[5000];
        PF = new double[5000];
        pfOnlyErrors = new double[5000];
        
    }

    public void visualizeTrainingData() {

        main = (JPanel) getContentPane();
        sa = (SimArea) main.getComponent(0);
        for (int i = 0; i < trainingData.getTrainingData().size(); i++) {

            JLabel td = new JLabel();

            td.setName("td");
            td.setText("" + i);

            td.setBorder(BorderFactory.createEtchedBorder());
            td.setBackground(Color.pink);
            td.setBounds((int) trainingData.getCoordinates()[i][0], (int) trainingData.getCoordinates()[i][1], 8, 8);
            //  td.setToolTipText("( " + td.getLocation().x + "," + td.getLocation().y + " )");
            td.setVisible(true);
            sa.add(td);

        }
        setContentPane(main);

    }

    public Coordinates generateRandomObject() {

        Random rnd = new Random(System.currentTimeMillis());

        int multiplier = (rnd.nextDouble() > .5) ? 1 : -1;


        int x = (int) (sa.getBounds().width / 2 + multiplier * rnd.nextDouble() * sa.getBounds().width / 2);

        multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
        int y = (int) (sa.getBounds().height / 2 + multiplier * rnd.nextDouble() * sa.getBounds().height / 3);
        Coordinates location = new Coordinates(x, y);

        if (!sa.isInsideSimArea(location)) {
            return null;
        }

        laptop = new MovingObject(laptopImg, accessPoints, location, scaleFactor);
        main = (JPanel) getContentPane();
        sa = (SimArea) main.getComponent(0);

        laptop.setMyLocation(location);
        mInfo.setNewActualPosition(new Coordinates(location.x, location.y));
        laptop.setName("laptop");
        laptop.setBounds(x, y, 30, 30);
        laptop.setToolTipText("(" + laptop.getMyLocation().x + "," + laptop.getMyLocation().y + ")");
        sa.add(laptop);
        //      laptop.generateRSSIforMovingObject();
        //    sa.validate();
        setContentPane(main);
        // validate();
        return location;
    }

    public void run() {
//setupAP.doClick();
train.doClick();
start.doClick();
        while (iterationCount <= 5100) {
            System.out.println("");
            
            if (simulationStarted) {

                try {

                    if (simSpeed.getSelectedIndex() == 0) {
                        Thread.sleep(3000);
                    }

                    if (simSpeed.getSelectedIndex() == 1) {
                        Thread.sleep(100);
                    }


                    if (simSpeed.getSelectedIndex() == 2) {
                        Thread.sleep(0);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                //    MobilityInfo mInfo = mobilityModel.updateModel(laptop.getMyLocation());

                MobilityInfo temp = mobilityModel.updateModel(mInfo.getNewActualPosition());

                if (temp == null) {
                    continue;
                } else {
                    mInfo = temp;
                }
//

                laptop.setPreviousLocation(laptop.getMyLocation());
                laptop.setMyLocation(mInfo.getNewActualPosition());


                getDistancesFromBaseStations();
                laptop.getPhaseShiftFromBaseStations(accessPoints, mInfo);

//                if (firstRun) {
//                    laptop.getPreviousRSSI().getValues().clear();
//                    for (int i = 0; i < accessPoints.size(); i++) {
//                        laptop.getPreviousRSSI().getValues().add(laptop.getCurrentRSSI().getValues().get(i));
//                    }
//                    firstRun = false;
//                }

                laptop.setBounds((int) mInfo.getNewActualPosition().x, (int) mInfo.getNewActualPosition().y, 30, 30);

                Coordinates euc = distanceEstimator.findXY(0, laptop);

                Coordinates pf = new Coordinates(0, 0);
                Coordinates pfRAW= new Coordinates(0, 0);
                try {

                    if (iterationCount == 0) {
                        particleFilter.setEstimation(new Coordinates(laptop.getMyLocation().x, laptop.getMyLocation().y));
                         
                       particleFilterDE.setEstimation(new Coordinates(laptop.getMyLocation().x, laptop.getMyLocation().y));
                    }
                    pf = particleFilter.GetEstimatedPosition(mInfo, false, null);

                    pfRAW = particleFilterDE.GetEstimatedPosition(mInfo, true, distanceEstimator.getProbabilities());
                      update(euc, euc, pf, pfRAW);
//                    if (!sa.isInsideSimArea(pf))
//                    {
//                        pftime++;
//                        do{
//                        
//                    Random rnd = new Random(System.currentTimeMillis());
//
//                    int multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
//
//
//                    int x = (int) (sa.getBounds().width / 2 + multiplier * rnd.nextDouble() * sa.getBounds().width / 2);
//
//                    multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
//                    int y = (int) (sa.getBounds().height / 2 + multiplier * rnd.nextDouble() * sa.getBounds().height / 3);
//                    Coordinates location = new Coordinates(x, y);
// 
//                    pf.x=x;
//                    pf.y=y;
//                     particleFilter.setEstimation( pf);
//                        } while (!sa.isInsideSimArea(pf)) ; 
//                                                    
//                                }      
// 
//                    if (!sa.isInsideSimArea(pfRAW))
//                      do{
//                        
//                    Random rnd = new Random(System.currentTimeMillis());
//
//                    int multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
//
//
//                    int x = (int) (sa.getBounds().width / 2 + multiplier * rnd.nextDouble() * sa.getBounds().width / 2);
//
//                    multiplier = (rnd.nextDouble() > .5) ? 1 : -1;
//                    int y = (int) (sa.getBounds().height / 2 + multiplier * rnd.nextDouble() * sa.getBounds().height / 3);
//                  
//                    pfRAW.x=x;
//                    pfRAW.y=y;
//                     particleFilterDE.setEstimation( pfRAW);
//                        } while (!sa.isInsideSimArea(pfRAW)) ; 
//                           
                    
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

              
                repaint();
                iterationCount++;
            }

        }
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainFrame.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private Coordinates[] getAccessPointsLocations(int num, Point lowerBounds, Point upperBounds) {

        //
        double totalWidth = upperBounds.x - lowerBounds.x;
        double totalHeight = upperBounds.y - lowerBounds.y;

        int countHor = (int) Math.floor(Math.sqrt(num));
        int countVer = (int) Math.ceil((double) num / (double) countHor);

        double subWidth = totalWidth / countHor;
        double subHeight = totalHeight / countVer;

        Coordinates[] locations = new Coordinates[countHor * countVer];


        //
        for (int i = 0; i < countVer; i++) {
            for (int j = 0; j < countHor; j++) {
                Coordinates p = new Coordinates();
                p.x = (int) ((j * subWidth + subWidth / 2));
                p.y = (int) ((i * subHeight + subHeight / 2));
                locations[i * countHor + j] = p;
            }
        }

        return locations;

    }

    private void buildActionListeners() {

        setupAP.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (technique.getSelectedIndex() == 0) {
                    scaleFactor = 1;
                }

                if (technique.getSelectedIndex() == 1) {
                    scaleFactor = 10;
                }

                if (technique.getSelectedIndex() == 2) {
                    scaleFactor = 100;
                }
                //   mobilityModel = new MobilityModel(1, .5, 2, sa, scaleFactor);
                int num = Integer.parseInt(numberofAP.getText());

                AccessPoint ap;
                Random rnd = new Random();
                Coordinates[] locations = getAccessPointsLocations(num, new Point(sa.getBounds().x, sa.getBounds().y), new Point(sa.getBounds().x + sa.getBounds().width, sa.getBounds().y + sa.getBounds().height));
                JLabel l = new JLabel();

                /**
                 * *************************************************************************************************************************
                 * ******************************** change noise percent here
                 * ****************************************************************************************************
                 * ***************************************************************************************************************************
                 */
                int maxnumOfNoisyBSs = 0;
                for (int i = 0; i < num; i++) {

                    if (i >= maxnumOfNoisyBSs) {
                        ap = new AccessPoint(locations[i], accessPoints.size(), num);
                        ap.setNoisy(false);
                        l = new JLabel(icon);
                    } else {
                        ap = new AccessPoint(locations[i], accessPoints.size(), num, rnd);
                        ap.setNoisy(true);
                        l = new JLabel(icon);
//                           picked.add(i);
                        noisyGuys.add(i);
                    }


                    l.setBounds((int) locations[i].x, (int) locations[i].y, 50, 60);
                    l.setVisible(true);
                    l.setName("" + accessPoints.size());
                    l.setText("");
                    l.setToolTipText("( " + locations[i].x + " , " + locations[i].y + " )");

                    accessPoints.add(ap);
                    sa.add(l);
                }
                //////////////

                mInfo = new MobilityInfo(accessPoints.size());
                //  sa.validate();
                setContentPane(main);
                //  validate();
                setupAP.setEnabled(false);
                mInfo = new MobilityInfo(accessPoints.size());


            }
        });

        train.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (numberOfTrainData.getText().isEmpty() || accessPoints.isEmpty() || technique.getSelectedIndex() == -1) {
                    Toolkit.getDefaultToolkit().beep();
                    numberOfTrainData.setBackground(Color.red);
                    return;
                } else {

                    if (technique.getSelectedIndex() == 0) {
                        scaleFactor = 1;
                    }

                    if (technique.getSelectedIndex() == 1) {
                        scaleFactor = 10;
                    }

                    if (technique.getSelectedIndex() == 2) {
                        scaleFactor = 100;
                    }
                    mobilityModel = new MobilityModel(1, 0, 100, sa, scaleFactor);


                    numberOfTrainData.setBackground(Color.gray);
                    int number = Integer.parseInt(numberOfTrainData.getText());
                    trainingData = new TrainingData(number, accessPoints.size(), scaleFactor);

                    trainingData.generateTrainingData(accessPoints, new Point(sa.getBounds().x, sa.getBounds().y), new Point(sa.getBounds().x + sa.getBounds().width, sa.getBounds().y + sa.getBounds().height), mInfo);
                    visualizeTrainingData();
                    train.setBackground(new Color(238, 238, 238));
                    train.setEnabled(false);
                    trained = true;


                }

            }
        });


        start.addActionListener(new ActionListener() {

            Coordinates euc;
            Coordinates de;

            public void actionPerformed(ActionEvent e) {
                if (!trained) {
                    train.setBackground(Color.red);
                    return;
                }

                Coordinates done = generateRandomObject();

                while (done == null) {
                    done = generateRandomObject();
                }

                distanceEstimator = new DistanceEstimator(laptop, accessPoints, trainingData);

//                euc = distanceEstimator.findXY(0, laptop);
//                de = distanceEstimator.findXY(1, laptop);
//                Coordinates pfimgine = new Coordinates(0, 0);
//                update(euc, de, pfimgine);
                sa.setClickable(false);

                particleFilter = new Filter(trainingData, accessPoints, laptop, scaleFactor, sa);
                particleFilterDE = new Filter(trainingData, accessPoints, laptop, scaleFactor, sa);
                simulationStarted = true;
                start.setEnabled(false);
                technique.setEnabled(false);

                //    System.out.println("Coordinate = (" + p.x + ", " + p.y + ")");

            }
        });


    }

    public void update(Coordinates euc, Coordinates de, Coordinates pf, Coordinates rawpf) {

        String s1 = " Actual position";
        String s2 = "Estimated EUC";
        String s3 = "Estimated DE";
        String s4 = "EUC Error";
        String s5 = "DE Error";
        String s6 = "Estimated PF";
        String s7 = "PF Error";
        NumberFormat form = NumberFormat.getInstance();
        form.setMinimumFractionDigits(0);

        estimatedRAWPF.setBounds((int) rawpf.x, (int) rawpf.y, 40, 40);
        estimatedLabelPF.setBounds((int) pf.x, (int) pf.y, 40, 40);
        estimatedLabelEUC.setBounds((int) euc.x, (int) euc.y, 40, 40);


        main = (JPanel) getContentPane();
        sa = (SimArea) main.getComponent(0);

        estimatedEUC.setText("");
        estimatedRAWPFLABEL.setText("");
        estimatedPF.setText("");
        actualPosion.setText("");

        errorDE.setText("");
        errorEUC.setText("");
        errorPF.setText("");

        estimatedEUC.setText(s2 + " ( " + (int) (euc.x) + "," + (int) (euc.y) + " )");
        estimatedRAWPFLABEL.setText(s3 + " ( " + (int) (rawpf.x) + "," + (int) (rawpf.y) + " )");
        estimatedPF.setText(s6 + " ( " + (int) (pf.x) + "," + (int) (pf.y) + " )");
        actualPosion.setText(s1 + " ( " + (int) laptop.getMyLocation().x + "," + (int) laptop.getMyLocation().y + " ) ");


        eucError = Math.sqrt((Math.pow(laptop.getMyLocation().x / scaleFactor - euc.x / scaleFactor, 2) + Math.pow(laptop.getMyLocation().y / scaleFactor - euc.y / scaleFactor, 2)));
       // deError = Math.sqrt((Math.pow(laptop.getMyLocation().x / scaleFactor - de.x / scaleFactor, 2) + Math.pow(laptop.getMyLocation().y / scaleFactor - de.y / scaleFactor, 2)));
        pfError = Math.sqrt((Math.pow(laptop.getMyLocation().x / scaleFactor - pf.x / scaleFactor, 2) + Math.pow(laptop.getMyLocation().y / scaleFactor - pf.y / scaleFactor, 2)));
        rawPFERROR = Math.sqrt((Math.pow(laptop.getMyLocation().x / scaleFactor - rawpf.x / scaleFactor, 2) + Math.pow(laptop.getMyLocation().y / scaleFactor - rawpf.y / scaleFactor, 2)));

       
//
//   if (iterationCount > 0) {
//            System.out.println(form.format(pfError)+"   "+form.format(pfrawPFERROR));
//        }
        gcounter++;

        if (gcounter < 100) {
            return;
        }

        if (counter < 5000) {

            double eucDis = getDistance(euc);
            double rawPFDIS = getDistance(rawpf);
            double pfDis = getDistance(pf);



            EUC[counter] = eucDis;
            RAWPF[counter] = rawPFDIS;
            PF[counter] = pfDis;
            //counter++;
            eucCounter20 = eucError < .2 ? eucCounter20 + 1 : eucCounter20;
            RAWPFCounter20 = rawPFERROR < .2 ? RAWPFCounter20 + 1 : RAWPFCounter20;
            pfCounter20 = pfError < .2 ? pfCounter20 + 1 : pfCounter20;

            eucCounter40 = eucError < .4 ? eucCounter40 + 1 : eucCounter40;
            RAWPFCounter40 = rawPFERROR < .4 ? RAWPFCounter40 + 1 : RAWPFCounter40;
            pfCounter40 = pfError < .4 ? pfCounter40 + 1 : pfCounter40;

            eucCounter60 = eucError < .6 ? eucCounter60 + 1 : eucCounter60;
            RAWPFCounter60 = rawPFERROR < .6 ? RAWPFCounter60 + 1 : RAWPFCounter60;
            pfCounter60 = pfError < .6 ? pfCounter60 + 1 : pfCounter60;

            eucCounter100 = eucError < 1 ? eucCounter100 + 1 : eucCounter100;
            RAWPFCounter100 = rawPFERROR < 1 ? RAWPFCounter100 + 1 : RAWPFCounter100;
            pfCounter100 = pfError < 1 ? pfCounter100 + 1 : pfCounter100;

            eucCounter200 = eucError < 2 ? eucCounter200 + 1 : eucCounter200;
            RAWPFCounter200 = rawPFERROR < 2 ? RAWPFCounter200 + 1 : RAWPFCounter200;
            pfCounter200 = pfError < 2 ? pfCounter200 + 1 : pfCounter200;

            eucCounter300 = eucError < 3 ? eucCounter300 + 1 : eucCounter300;
            RAWPFCounter300 = rawPFERROR < 3 ? RAWPFCounter300 + 1 : RAWPFCounter300;
            pfCounter300 = pfError < 3 ? pfCounter300 + 1 : pfCounter300;

            eucCounter400 = eucError < 4 ? eucCounter400 + 1 : eucCounter400;
            RAWPFCounter400 = rawPFERROR < 4 ? RAWPFCounter400 + 1 : RAWPFCounter400;
            pfCounter400 = pfError < 4 ? pfCounter400 + 1 : pfCounter400;

            eucCounter500 = eucError < 50 ? eucCounter500 + 1 : eucCounter500;
            RAWPFCounter500 = rawPFERROR < 50 ? RAWPFCounter500 + 1 : RAWPFCounter500;
            pfCounter500 = pfError < 50 ? pfCounter500 + 1 : pfCounter500;


            eucErros[counter] = eucError;
            rawPFERRORs[counter] = rawPFERROR;
            pfErros[counter] = pfError;
       

            counter++;
        }

//        if (iterationCount > 0) {
//            System.out.println(iterationCount);
//        }
        if (counter == 5000) {
            double eucmean = mean(eucErros);
            double rawPFMEAN = mean(rawPFERRORs);
            double pfmean = mean(pfErros);


            double EUCM = mean(EUC);
            double DEM = mean(RAWPF);
            double PFM = mean(PF);
            
            
            double eucstd = std(eucErros);
            double rawPFstd= std(rawPFERRORs);
            double pfonlystd = std(pfOnlyErrors);
            double pfstd = std(pfErros);

            double t90 =  1.2817 , t80 = 0.8417 , t60 = .2534;

            double eucPlusMinus90 = t90*eucstd/Math.sqrt(5000) ;
            double dePlusMinus90 = t90*rawPFstd/Math.sqrt(5000) ;
            double pfOnlyPlusMinus90 = t90*pfonlystd/Math.sqrt(5000) ;
            double pfPlusMinus90 = t90*pfstd/Math.sqrt(5000) ;

            double eucPlusMinus80 = t80*eucstd/Math.sqrt(5000) ;
            double dePlusMinus80 = t80*rawPFstd/Math.sqrt(5000) ;
            double pfOnlyPlusMinus80 = t80*pfonlystd/Math.sqrt(5000) ;
            double pfPlusMinus80 = t80*pfstd/Math.sqrt(5000) ;

            double eucPlusMinus60 = t60*eucstd/Math.sqrt(5000) ;
            double dePlusMinus60 = t60*rawPFstd/Math.sqrt(5000) ;
            double pfOnlyPlusMinus60 = t60*pfonlystd/Math.sqrt(5000) ;
            double pfPlusMinus60 = t60*pfstd/Math.sqrt(5000) ;

           //    System.out.println("EUC "+" "+eucmean+" "+eucstd+"  "+eucPlusMinus60+"  "+eucPlusMinus80+"  "+eucPlusMinus90+"  DE "+" "+rawPFMEAN+" "+rawPFstd+"  "+dePlusMinus60+"  "+dePlusMinus80+"  "+dePlusMinus90+"    PFONLY "+" "+pfonlymean+" "+pfonlystd+"  "+pfOnlyPlusMinus60+"  "+pfOnlyPlusMinus80+"  "+pfOnlyPlusMinus90+"   PF "+" "+pfmean+" "+pfstd+"  "+pfPlusMinus60+"  "+pfPlusMinus80+"  "+pfPlusMinus90);

            double euc20 = eucCounter20 / counter;
            double euc40 = eucCounter40 / counter;
            double euc60 = eucCounter60 / counter;
            double euc100 = eucCounter100 / counter;
            double euc200 = eucCounter200 / counter;
            double euc300 = eucCounter300 / counter;
            double euc400 = eucCounter400 / counter;
            double euc500 = eucCounter500 / counter;


            double de20 = RAWPFCounter20 / counter;
            double de40 = RAWPFCounter40 / counter;
            double de60 = RAWPFCounter60 / counter;
            double de100 = RAWPFCounter100 / counter;
            double de200 = RAWPFCounter200 / counter;
            double de300 = RAWPFCounter300 / counter;
            double de400 = RAWPFCounter400 / counter;
            double de500 = RAWPFCounter500 / counter;




            double pf20 = pfCounter20 / counter;
            double pf40 = pfCounter40 / counter;
            double pf60 = pfCounter60 / counter;
            double pf100 = pfCounter100 / counter;
            double pf200 = pfCounter200 / counter;
            double pf300 = pfCounter300 / counter;
            double pf400 = pfCounter400 / counter;
            double pf500 = pfCounter500 / counter;
           System.out.println("EUC " + eucmean + " " + euc20 + "   " + euc40 + "   " + euc60 + "   " + euc100 + "  " + euc200 + "   " + euc300 + "   " + euc400 + "   " + euc500 + "  " + "DE   " + rawPFMEAN + "  " + de20 + "    " + de40 + "    " + de60 + "    " + de100 + "   " + de200 + "    " + de300 + "    " + de400 + "    " + de500 + "   " + "PF   " + pfmean + "  " + pf20 + "    " + pf40 + "    " + pf60 + "    " + pf100 + " " + pf200 + "    " + pf300 + "    " + pf400 + "    " + pf500);
        System.out.println(   form.format(eucmean)  + "    "+form.format(rawPFMEAN) + "    "+ form.format(pfmean));
     //System.out.println("times "+pftime);
       
       counter++;
        }


        form.setMinimumFractionDigits(2);
        errorDE.setText(s5 + " " + form.format(rawPFERROR));
        errorEUC.setText(s4 + " " + form.format(eucError));
        errorPF.setText(s7 + " " + form.format(pfError));
 
        // sa.validate();
        //  validate();


    }

    private double getDistance(Coordinates euc) {
        double minD = Double.POSITIVE_INFINITY;

        for (int i = 0; i < trainingData.getTrainingData().size(); i++) {
            double x = trainingData.getCoordinates()[i][0];
            double y = trainingData.getCoordinates()[i][1];

            double d = Math.sqrt(Math.pow((x - euc.x), 2) + Math.pow((y - euc.y), 2));
            if (d < minD) {
                minD = d;
            }

        }
        return minD;

    }
    Random phaseRandom = new Random();

    private void getDistancesFromBaseStations() {

        mInfo.noisyDistancesFromBSs = new double[accessPoints.size()];
        mInfo.realDistancesFromBSs = new double[accessPoints.size()];

        for (int i = 0; i < accessPoints.size(); i++) {
            double di = Math.sqrt(Math.pow(mInfo.getNewActualPosition().x - accessPoints.get(i).getLocation().x, 2) + Math.pow(mInfo.getNewActualPosition().y - accessPoints.get(i).getLocation().y, 2));

            mInfo.realDistancesFromBSs[i] = di;

            double factor = phaseRandom.nextDouble() > .5 ? -1 : 1;

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

    public void buildVisuals() {

        lhandler = new LabelHandlerClass(sa, accessPoints, this, main);
        //   SimAreaHandlerClass handler = new SimAreaHandlerClass(sa, accessPoints, this, main, lhandler);
        technique.insertItemAt("1 meter: 1 pixel", 0);
        technique.insertItemAt("1 meter: 10 pixels", 1);
        technique.insertItemAt("1 meter: 100 pixels", 2);
        technique.setSelectedIndex(2);
        main = (JPanel) getContentPane();
        main.setBorder(BorderFactory.createTitledBorder(""));
        // sa.addMouseListener(handler);
        sa.setName("");
        sa.setLayout(null);
        sa.setBorder(BorderFactory.createTitledBorder("Sim area"));
        up.setBorder(BorderFactory.createTitledBorder(""));
        up.setName("");
        down.setBorder(BorderFactory.createTitledBorder(""));
        down.setName("");
        left.setBorder(BorderFactory.createTitledBorder(""));
        left.setName("");
        left.setLayout(new BorderLayout());
        subLeftNorth.setBorder(BorderFactory.createTitledBorder(""));
        subLeftCenter.setBorder(BorderFactory.createTitledBorder(""));
        subLeftSouth.setBorder(BorderFactory.createTitledBorder(""));
        subLeftNorth.setLayout(new BorderLayout());



        //////////////////
        numberofAP.setText("15");
        numberOfTrainData.setText("30");
///////////////////////////////////////////


        JButton b3 = new JButton("b3");

        JLabel text = new JLabel("Enter number of training data");
        JLabel ap = new JLabel("Enter number of Access Points");
        subLeftNorth.setLayout(new GridLayout(4, 2));

        subLeftNorth.add(ap);
        subLeftNorth.add(numberofAP);
        subLeftNorth.add(setupAP);
        subLeftCenter.setLayout(new GridLayout(15, 2));

        JLabel header = new JLabel(" Scale factor");
        subLeftCenter.add(header);
        subLeftCenter.add(technique);

        subLeftCenter.add(text, BorderLayout.NORTH);
        subLeftCenter.add(numberOfTrainData, BorderLayout.CENTER);
        subLeftCenter.add(train, BorderLayout.SOUTH);

        subLeftCenter.add(start);
        subLeftCenter.add(actualPosion);
        subLeftCenter.add(estimatedEUC);
        subLeftCenter.add(estimatedRAWPFLABEL);
        subLeftCenter.add(estimatedPF);
        subLeftCenter.add(errorEUC);
        subLeftCenter.add(errorDE);
        subLeftCenter.add(errorPF);

            sa.setBackground(Color.white);


        sa.add(estimatedLabelEUC);
        sa.add(estimatedRAWPF);
        sa.add(estimatedLabelPF);
 
        estimatedRAWPF.setName("de");
        estimatedLabelPF.setName("pf");
        estimatedLabelEUC.setName("euc");

        subLeftSouth.setLayout(new GridLayout(4, 2));
        JLabel speed = new JLabel("Simulation speed");
        subLeftSouth.add(speed);
        subLeftSouth.add(simSpeed);
        simSpeed.insertItemAt("Slow", 0);
        simSpeed.insertItemAt("Fast", 1);
        simSpeed.insertItemAt("Top Speed", 2);
        simSpeed.setSelectedIndex(2);

        left.add(subLeftNorth, BorderLayout.NORTH);
        left.add(subLeftCenter, BorderLayout.CENTER);
        left.add(subLeftSouth, BorderLayout.SOUTH);
        // left.validate();

        buildActionListeners();


    
        main.add(sa, BorderLayout.CENTER);
    
        main.add(left, BorderLayout.EAST);

        setContentPane(main);
        // left.validate();
        subLeftSouth.validate();
    
 


    }

    public double mean(double[] data) {
        // sd is sqrt of sum of (values-mean) squared divided by n - 1
        // Calculate the mean
        double mean = 0;
        final int n = data.length;
        if (n < 2) {
            return Double.NaN;
        }
        for (int i = 0; i < n; i++) {
            mean += data[i];
        }
        mean /= n;

        // Change to ( n - 1 ) to n if you have complete data instead of a sample.
        return mean;
    }

    public double std(double[] data) {
        // sd is sqrt of sum of (values-mean) squared divided by n - 1
        // Calculate the mean
        double mean = 0;
        final int n = data.length;
        if (n < 2) {
            return Double.NaN;
        }
        for (int i = 0; i < n; i++) {
            mean += data[i];
        }
        mean /= n;
        // calculate the sum of squares
        double sum = 0;
        for (int i = 0; i < n; i++) {
            final double v = data[i] - mean;
            sum += v * v;
        }
        // Change to ( n - 1 ) to n if you have complete data instead of a sample.
        return Math.sqrt(sum / (n));
    }
}
