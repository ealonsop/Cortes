/*
 * PcortesguiView.java
 */

package pcortesgui;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import java.awt.*;

import java.awt.print.*;
import java.io.*;


import edatos.*;


/**
 * The application's main frame.
 */
public class PcortesguiView extends FrameView {

    CPlancha cplancha;
    TRectangulo planchat;
    TLista   solucion;
    TListaO   Cortes;
    int nplan, pos, dim, max;
    PrinterJob job;
    boolean vertical, unir;
    int orden;
    String dir;
    // 0 Hor-Alto,
    // 1 Hor-Ancho
    // 2 Ver-Alto
    // 3 Ver-Ancho
    // 4 Hor-Ratio A/L
    // 5 Hor-Area

    int mm[][];

    private void AgregarCorte( TListaO LO, TRectangulo R )
    {
        if ( (orden == 0 || orden == 1 || orden == 4 || orden == 5 ) && R.obtLargo() > R.obtAncho() )
                R = R.Rotar();
        if ( (orden == 2 || orden == 3) && R.obtLargo() < R.obtAncho() )
                R = R.Rotar();
        LO.Insertar(R);
    }

    private void Reordena()
    {
        TListaO nvoCortes;

        if ( orden == 0 || orden == 2 ) //alto
            nvoCortes = new TListaOA( new cmpRectangulo(), false  );
        else
        if ( orden == 1 || orden == 3 ) // ancho
            nvoCortes = new TListaOA( new cmpRectangulo2(), false  );
        else // ratio
        if ( orden == 4 ) // ratio
            nvoCortes = new TListaOA( new cmpRectangulo3(), false  );
        else
        //if ( orden == 5 ) // area
            nvoCortes = new TListaOA( new cmpRectangulo4(), false  );
          

        if ( Cortes != null )
        for ( int i=0; i < Cortes.Cantidad(); i++ ) {
            TRectangulo R = (TRectangulo)Cortes.Obtener(i);
            AgregarCorte( nvoCortes, R );
        }
        Cortes = nvoCortes;
        switch ( orden ) {
            case 0: jRadioButton1.setSelected(true); break;
            case 1: jRadioButton2.setSelected(true); break;
            case 2: jRadioButton3.setSelected(true); break;
            case 3: jRadioButton4.setSelected(true); break;
            case 4: jRadioButton5.setSelected(true); break;
            case 5: jRadioButton6.setSelected(true); break;
        }
        llenaTabla();
    }

    private void DibujaPlancha( JPanel P, TRectangulo R, TLista S )
    {
        int w, h;
        CPlancha cp;

        if ( P.getComponentCount() == 0 )
        {
            cp = new CPlancha();

            Dimension D = cp.getPreferredSize();
            w = D.width;
            h = D.height;

            cp.setBounds(1, 1, w, h);
            Rectangle Re = P.getBounds();
            Re.setSize(w, h);
            P.setBounds(Re);
            //Border b;
            //b = LineBorder.createBlackLineBorder();
            //P.setBorder(b);
            P.add(cp);
        }
        else
            cp = (CPlancha)P.getComponent(0);
        cp.Actualiza(R, S,dim,pos);
        cp.cMax(max);
        P.repaint();
    }


    public PcortesguiView(SingleFrameApplication app) {
        super(app);

        initComponents();

        solucion = null;
        orden = 0;
        jRadioButton1.setSelected(true);
        Cortes = null;
        dir = System.getProperty("user.dir");
        Reordena();
        job = null;
        mm = new int[20][3];
        mm[0][0] = 390;  mm[0][1] = 490;  mm[0][2] = 6;
        mm[1][0] = 440;  mm[1][1] = 480;  mm[1][2] = 3;
        mm[2][0] = 400;  mm[2][1] = 450;  mm[2][2] = 1;
        mm[3][0] = 610;  mm[3][1] = 460;  mm[3][2] = 4;
        mm[4][0] = 440;  mm[4][1] = 430;  mm[4][2] = 1;
        mm[5][0] = 420;  mm[5][1] = 470;  mm[5][2] =2;
        mm[6][0] = 410;  mm[6][1] = 300;  mm[6][2] = 2;
        mm[7][0] = 490;  mm[7][1] = 500;  mm[7][2] = 4;
        mm[8][0] = 465;  mm[8][1] = 470;  mm[8][2] = 3;
        mm[9][0] = 670;  mm[9][1] = 460;  mm[9][2] = 4;
        mm[10][0] = 680;  mm[10][1] = 1020;  mm[10][2] = 2;
        mm[11][0] = 455;  mm[11][1] = 450;  mm[11][2] = 3;
        mm[12][0] = 590;  mm[12][1] = 470;  mm[12][2] = 3;
        mm[13][0] = 608;  mm[13][1] = 475;  mm[13][2] = 2;
        mm[14][0] = 600;  mm[14][1] = 460;  mm[14][2] = 2;
        mm[15][0] = 300;  mm[15][1] = 460;  mm[15][2] = 3;
        mm[16][0] = 250;  mm[16][1] = 500;  mm[16][2] = 1;
        mm[17][0] = 620;  mm[17][1] = 450;  mm[17][2] = 4;
        mm[18][0] = 610;  mm[18][1] = 480;  mm[18][2] = 1;
        mm[19][0] = 420;  mm[19][1] = 448;  mm[19][2] = 2;
      /*  mm = new int[22][3];
        mm[0][0] = 300;  mm[0][1] = 200;  mm[0][2] = 4;
        mm[1][0] = 400;  mm[1][1] = 500;  mm[1][2] = 3;
        mm[2][0] = 472;  mm[2][1] = 603;  mm[2][2] = 5;
        mm[3][0] = 328;  mm[3][1] = 499;  mm[3][2] = 4;
        mm[4][0] = 1000;  mm[4][1] = 700;  mm[4][2] =2;
        mm[5][0] = 490;  mm[5][1] = 498;  mm[5][2] = 2;
        mm[6][0] = 490;  mm[6][1] = 483;  mm[6][2] = 2;
        mm[7][0] = 480;  mm[7][1] = 484;  mm[7][2] = 4;
        mm[8][0] = 410;  mm[8][1] = 580;  mm[8][2] = 2;
        mm[9][0] = 340;  mm[9][1] = 530;  mm[9][2] = 5;
        mm[10][0] = 495;  mm[10][1] = 412;  mm[10][2] = 7;
        mm[11][0] = 595;  mm[11][1] = 495;  mm[11][2] = 10;
        mm[12][0] = 520;  mm[12][1] = 470;  mm[12][2] = 1;
        mm[13][0] = 145;  mm[13][1] = 180;  mm[13][2] = 5;
        mm[14][0] = 320;  mm[14][1] = 600;  mm[14][2] = 3;
        mm[15][0] = 350;  mm[15][1] = 770;  mm[15][2] = 2;
        mm[16][0] = 780;  mm[16][1] = 960;  mm[16][2] = 4;
        mm[17][0] = 300;  mm[17][1] = 960;  mm[17][2] = 4;
        mm[18][0] = 360;  mm[18][1] = 960;  mm[18][2] = 2;
        mm[19][0] = 485;  mm[19][1] = 380;  mm[19][2] = 4;
        mm[20][0] = 550;  mm[20][1] = 600;  mm[20][2] = 6;
        mm[21][0] = 520;  mm[21][1] = 483;  mm[21][2] = 2;
  */
        pos = -1;
        vertical=false;
        unir = false;

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PcortesguiApp.getApplication().getMainFrame();
            aboutBox = new PcortesguiAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        PcortesguiApp.getApplication().show(aboutBox);
    }

    private void llenaTabla()
    {

        int filas, i;
        Object [][] model;

        filas = Cortes.Cantidad();

        if ( filas > 0 )
            model = new String[filas][3];
        else
            model = new Object[1][3];

        String [] headers = new String[3];
        headers[0] = "Nro";
        headers[1] = "Alto";
        headers[2] = "Ancho";
        for ( i = 0; i < filas; i++ )
        {
            TRectangulo R = (TRectangulo)Cortes.Obtener(i);
            model[i][0] = String.valueOf(i+1);
            model[i][1] = String.valueOf(R.obtLargo());
            model[i][2] = String.valueOf(R.obtAncho());
        }
        jTCortes.setModel(new javax.swing.table.DefaultTableModel(
            model, headers
            ));
    }

    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jpPlancha = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTFaltop = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTFanchop = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFaltoc = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTCortes = new javax.swing.JTable();
        jTFanchoc = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jTFmax = new javax.swing.JTextField();
        jTFcant = new javax.swing.JTextField();
        jTFnplan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLplancha = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jCBvertical = new javax.swing.JCheckBox();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jCBunir = new javax.swing.JCheckBox();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMICargar = new javax.swing.JMenuItem();
        jMIGuardar = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        buttonGroup1 = new javax.swing.ButtonGroup();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pcortesgui.PcortesguiApp.class).getContext().getResourceMap(PcortesguiView.class);
        mainPanel.setForeground(resourceMap.getColor("mainPanel.foreground")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        mainPanel.setLayout(null);

        jpPlancha.setForeground(resourceMap.getColor("jpPlancha.foreground")); // NOI18N
        jpPlancha.setName("jpPlancha"); // NOI18N
        jpPlancha.setPreferredSize(new java.awt.Dimension(600, 600));

        javax.swing.GroupLayout jpPlanchaLayout = new javax.swing.GroupLayout(jpPlancha);
        jpPlancha.setLayout(jpPlanchaLayout);
        jpPlanchaLayout.setHorizontalGroup(
            jpPlanchaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jpPlanchaLayout.setVerticalGroup(
            jpPlanchaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        mainPanel.add(jpPlancha);
        jpPlancha.setBounds(390, 10, 700, 600);

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton1);
        jButton1.setBounds(10, 480, 73, 23);

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton2);
        jButton2.setBounds(90, 480, 85, 23);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        mainPanel.add(jLabel1);
        jLabel1.setBounds(160, 10, 23, 14);

        jTFaltop.setText(resourceMap.getString("jTFaltop.text")); // NOI18N
        jTFaltop.setName("jTFaltop"); // NOI18N
        mainPanel.add(jTFaltop);
        jTFaltop.setBounds(160, 30, 40, 20);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        mainPanel.add(jLabel2);
        jLabel2.setBounds(10, 10, 129, 14);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        mainPanel.add(jLabel3);
        jLabel3.setBounds(210, 10, 34, 14);

        jTFanchop.setText(resourceMap.getString("jTFanchop.text")); // NOI18N
        jTFanchop.setName("jTFanchop"); // NOI18N
        mainPanel.add(jTFanchop);
        jTFanchop.setBounds(210, 30, 40, 20);

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        mainPanel.add(jLabel5);
        jLabel5.setBounds(10, 80, 23, 14);

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        mainPanel.add(jLabel6);
        jLabel6.setBounds(90, 80, 34, 14);

        jTFaltoc.setText(resourceMap.getString("jTFaltoc.text")); // NOI18N
        jTFaltoc.setName("jTFaltoc"); // NOI18N
        mainPanel.add(jTFaltoc);
        jTFaltoc.setBounds(40, 80, 40, 20);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTCortes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jTCortes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTCortes.setName("jTCortes"); // NOI18N
        jTCortes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTCortes.getTableHeader().setResizingAllowed(false);
        jTCortes.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTCortes);

        mainPanel.add(jScrollPane1);
        jScrollPane1.setBounds(10, 140, 240, 330);

        jTFanchoc.setName("jTFanchoc"); // NOI18N
        mainPanel.add(jTFanchoc);
        jTFanchoc.setBounds(130, 80, 40, 20);

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton5);
        jButton5.setBounds(260, 430, 73, 23);

        jTFmax.setText(resourceMap.getString("jTFmax.text")); // NOI18N
        jTFmax.setName("jTFmax"); // NOI18N
        mainPanel.add(jTFmax);
        jTFmax.setBounds(340, 430, 30, 20);

        jTFcant.setText(resourceMap.getString("jTFcant.text")); // NOI18N
        jTFcant.setName("jTFcant"); // NOI18N
        mainPanel.add(jTFcant);
        jTFcant.setBounds(190, 80, 30, 20);

        jTFnplan.setEditable(false);
        jTFnplan.setText(resourceMap.getString("jTFnplan.text")); // NOI18N
        jTFnplan.setName("jTFnplan"); // NOI18N
        mainPanel.add(jTFnplan);
        jTFnplan.setBounds(110, 30, 30, 20);

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        mainPanel.add(jLabel7);
        jLabel7.setBounds(10, 30, 85, 14);

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton6);
        jButton6.setBounds(260, 400, 75, 23);

        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton7);
        jButton7.setBounds(10, 510, 43, 23);

        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton8);
        jButton8.setBounds(60, 510, 43, 23);

        jLplancha.setText(resourceMap.getString("jLplancha.text")); // NOI18N
        jLplancha.setName("jLplancha"); // NOI18N
        mainPanel.add(jLplancha);
        jLplancha.setBounds(170, 510, 30, 20);

        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setName("jButton9"); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton9);
        jButton9.setBounds(110, 510, 51, 23);

        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton10);
        jButton10.setBounds(210, 510, 73, 23);

        jCBvertical.setText(resourceMap.getString("jCBvertical.text")); // NOI18N
        jCBvertical.setName("jCBvertical"); // NOI18N
        jCBvertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBverticalActionPerformed(evt);
            }
        });
        mainPanel.add(jCBvertical);
        jCBvertical.setBounds(260, 310, 91, 23);

        jButton11.setText(resourceMap.getString("jButton11.text")); // NOI18N
        jButton11.setName("jButton11"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton11);
        jButton11.setBounds(90, 540, 73, 23);

        jButton12.setText(resourceMap.getString("jButton12.text")); // NOI18N
        jButton12.setName("jButton12"); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton12);
        jButton12.setBounds(10, 540, 73, 23);

        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setName("jButton13"); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton13);
        jButton13.setBounds(260, 30, 61, 23);

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton3);
        jButton3.setBounds(10, 110, 79, 23);

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        mainPanel.add(jButton4);
        jButton4.setBounds(180, 110, 71, 23);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        mainPanel.add(jLabel4);
        jLabel4.setBounds(10, 60, 127, 14);

        jCBunir.setText(resourceMap.getString("jCBunir.text")); // NOI18N
        jCBunir.setName("jCBunir"); // NOI18N
        jCBunir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBunirActionPerformed(evt);
            }
        });
        mainPanel.add(jCBunir);
        jCBunir.setBounds(260, 340, 81, 23);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText(resourceMap.getString("jRadioButton1.text")); // NOI18N
        jRadioButton1.setName("jRadioButton1"); // NOI18N
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        mainPanel.add(jRadioButton1);
        jRadioButton1.setBounds(260, 160, 73, 23);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText(resourceMap.getString("jRadioButton2.text")); // NOI18N
        jRadioButton2.setName("jRadioButton2"); // NOI18N
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        mainPanel.add(jRadioButton2);
        jRadioButton2.setBounds(260, 180, 83, 23);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText(resourceMap.getString("jRadioButton3.text")); // NOI18N
        jRadioButton3.setName("jRadioButton3"); // NOI18N
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        mainPanel.add(jRadioButton3);
        jRadioButton3.setBounds(260, 200, 69, 23);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText(resourceMap.getString("jRadioButton4.text")); // NOI18N
        jRadioButton4.setName("jRadioButton4"); // NOI18N
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        mainPanel.add(jRadioButton4);
        jRadioButton4.setBounds(260, 220, 79, 23);

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText(resourceMap.getString("jRadioButton5.text")); // NOI18N
        jRadioButton5.setName("jRadioButton5"); // NOI18N
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        mainPanel.add(jRadioButton5);
        jRadioButton5.setBounds(260, 240, 100, 23);

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText(resourceMap.getString("jRadioButton6.text")); // NOI18N
        jRadioButton6.setName("jRadioButton6"); // NOI18N
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });
        mainPanel.add(jRadioButton6);
        jRadioButton6.setBounds(260, 260, 77, 23);

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        mainPanel.add(jLabel8);
        jLabel8.setBounds(260, 140, 100, 20);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N
        mainPanel.add(jSeparator1);
        jSeparator1.setBounds(380, 10, 10, 570);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        mainPanel.add(jLabel9);
        jLabel9.setBounds(190, 60, 50, 14);

        menuBar.setName("menuBar"); // NOI18N
        menuBar.setOpaque(false);

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMICargar.setText(resourceMap.getString("jMICargar.text")); // NOI18N
        jMICargar.setName("jMICargar"); // NOI18N
        jMICargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICargarActionPerformed(evt);
            }
        });
        fileMenu.add(jMICargar);

        jMIGuardar.setText(resourceMap.getString("jMIGuardar.text")); // NOI18N
        jMIGuardar.setName("jMIGuardar"); // NOI18N
        jMIGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIGuardarActionPerformed(evt);
            }
        });
        fileMenu.add(jMIGuardar);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(pcortesgui.PcortesguiApp.class).getContext().getActionMap(PcortesguiView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 2705, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2531, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int i;
        TCorte A;

        //TListaO Cortes;
        TLista L;
        TRectangulo planchas[];



//        Cortes = new TListaOA( new cmpRectangulo(), false );
/*
        Cortes.Insertar( new TRectangulo(300,300,0,0));
        Cortes.Insertar( new TRectangulo(500,600,0,0));
        Cortes.Insertar( new TRectangulo(400,400,0,0));
        Cortes.Insertar( new TRectangulo(400,400,0,0));
        Cortes.Insertar( new TRectangulo(200,700,0,0));
        Cortes.Insertar( new TRectangulo(200,300,0,0));
        Cortes.Insertar( new TRectangulo(100,200,0,0));
        Cortes.Insertar( new TRectangulo(200,200,0,0));
*/
/*
        Cortes.Insertar( new TRectangulo(100,300,0,0));
        Cortes.Insertar( new TRectangulo(450,500,0,0));
        Cortes.Insertar( new TRectangulo(200,400,0,0));
        Cortes.Insertar( new TRectangulo(50,300,0,0));
        Cortes.Insertar( new TRectangulo(100,500,0,0)); // 480
        Cortes.Insertar( new TRectangulo(200,700,0,0));
        Cortes.Insertar( new TRectangulo(300,500,0,0));
        Cortes.Insertar( new TRectangulo(200,200,0,0));
        Cortes.Insertar( new TRectangulo(100,100,0,0));

        Cortes.Insertar( new TRectangulo(200,350,0,0));
        Cortes.Insertar( new TRectangulo(100,400,0,0));
        Cortes.Insertar( new TRectangulo(500,600,0,0));
        Cortes.Insertar( new TRectangulo(250,540,0,0));
 */
        int alto, ancho;
        alto = Integer.valueOf(jTFaltop.getText()).intValue();
        ancho = Integer.valueOf(jTFanchop.getText()).intValue();

        //nplan = Integer.valueOf(jTFnplan.getText()).intValue();
        nplan = 0;
        do {
        if ( nplan == 0 )
            nplan = 1;
        int j,k, nplant;

        i = 1;
        while ( nplan / (i*i) != 0 )
            i++;
        i = i - (nplan == ((i-1)*(i-1))?1:0);
        nplant = i*i;
        planchas = new TRectangulo[nplan];
        dim = i;
        for (j=0;j<nplan;j++)
        {
            int f,c;
            f = j/i;
            c = j%i;
            planchas[j] = new TRectangulo(alto,ancho,ancho*c,alto*f,j);
        }
        planchat = new TRectangulo(alto*i,ancho*i,0,0,0);

        A = new TCorte( Cortes, vertical, unir );
        L = new TListaA();
        for (i=0;i<nplan;i++)
          L.Adicionar(planchas[i]);
        solucion = A.BuscarSolucionPP(
               A.AdicionarEstado(0,(TRectangulo)Cortes.Obtener(0),-1,L),
               A.AdicionarEstado(Cortes.Cantidad(),planchat,0,L)
              );
            /*
            for ( i = 0; i < solucion.Cantidad(); i++ )
            {

              TNodoAB nodo = (TNodoAB)solucion.Obtener(i);
              System.out.println(i+" - "+nodo.estado);
            }*/
        pos=0;
        max = solucion.Cantidad();
        if ( max == 0 ) {
            nplan++;
        }
        jTFnplan.setText( String.valueOf(nplan) );
        }
        while ( max == 0 && nplan < 100 );

        DibujaPlancha( jpPlancha, planchat, solucion );
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if ( solucion == null )
            return;
        JFrame mainFrame = PcortesguiApp.getApplication().getMainFrame();
        UDetalles D = new UDetalles(mainFrame,false,solucion);
        //UDetalles.setLocationRelativeTo(mainFrame);
        //

        PcortesguiApp.getApplication().show(D);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int alto, ancho, cant;
        alto = Integer.valueOf(jTFaltoc.getText()).intValue();
        ancho = Integer.valueOf(jTFanchoc.getText()).intValue();
        cant = Integer.valueOf(jTFcant.getText()).intValue();
        while ( cant-- > 0 ) {
            TRectangulo R = new TRectangulo(alto,ancho,0,0,0);
            AgregarCorte( Cortes, R );
        }
        llenaTabla();
        jTFcant.setText("1");
        jTFaltoc.requestFocus();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int fila = jTCortes.getSelectedRow();
        if ( fila < 0 )
            return;
        Cortes.Eliminar(fila);
        llenaTabla();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int alto, ancho, MaxAl, MaxAn, N;

        MaxAl = Integer.valueOf(jTFaltoc.getText()).intValue();
        MaxAn = Integer.valueOf(jTFanchoc.getText()).intValue();
        N =  Integer.valueOf(jTFmax.getText()).intValue();

        while ( !Cortes.Vacia() )
            Cortes.Eliminar(0);

        for ( int i = 0; i < N; i++ ) {
            alto = (int)(Math.random()*100) % MaxAl + 1;
            ancho = (int)(Math.random()*100) % MaxAn + 1;
            TRectangulo R = new TRectangulo(alto,ancho,0,0,0);
            AgregarCorte( Cortes, R );
        }
        llenaTabla();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        while ( !Cortes.Vacia() )
            Cortes.Eliminar(0);

        for ( int i = 0; i < 20; i++ ) {
            int alto = mm[i][0];
            int ancho = mm[i][1];
            int cant = mm[i][2];
            while ( cant-- > 0 ) {
               TRectangulo R = new TRectangulo(alto,ancho,0,0,0);
               AgregarCorte( Cortes, R );
            }
        }
        llenaTabla();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if ( pos == -1 ) return;
        pos = pos-1;
        if ( pos < 0 ) pos = nplan;
        DibujaPlancha( jpPlancha, planchat, solucion );
        jLplancha.setText(String.valueOf(pos).toString());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        if ( pos == -1 ) return;
        pos = pos+1;
        if ( pos > nplan ) pos = 0;
        DibujaPlancha( jpPlancha, planchat, solucion );
        jLplancha.setText(String.valueOf(pos).toString());
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        if ( pos == -1 ) return;
        pos = 0;
        DibujaPlancha( jpPlancha, planchat, solucion );
        jLplancha.setText(String.valueOf(pos).toString());

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        if ( pos == -1 ) return;

        if ( job == null )
           job = PrinterJob.getPrinterJob();
        job.setPrintable(new printplancha(planchat, solucion,dim,pos));
        boolean ok = job.printDialog();
        if (ok) {
             try {
                  job.print();
             } catch (PrinterException ex) {
              /* The job did not successfully complete */
               }
        }

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jCBverticalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBverticalActionPerformed
        // TODO add your handling code here:
        vertical = jCBvertical.isSelected();
        
    }//GEN-LAST:event_jCBverticalActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        if ( pos == -1 ) return;
        max = (max + 1);
        if ( max > solucion.Cantidad() )
            max = 1;
        DibujaPlancha( jpPlancha, planchat, solucion );
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        if ( pos == -1 ) return;
        max = (max - 1);
        if ( max < 1 )
            max = solucion.Cantidad();
        DibujaPlancha( jpPlancha, planchat, solucion );

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        String tmp;
        tmp = jTFaltop.getText();
        jTFaltop.setText(jTFanchop.getText());
        jTFanchop.setText(tmp);

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jCBunirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBunirActionPerformed
        // TODO add your handling code here:
        unir = jCBunir.isSelected();
        
    }//GEN-LAST:event_jCBunirActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        orden = 0;
        Reordena();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        orden = 1;
        Reordena();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        orden = 2;
        Reordena();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        orden = 3;
        Reordena();
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
        orden = 4;
        Reordena();
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
        orden = 5;
        Reordena();
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private String SaltaSep( String cadena )
    {
        while ( !cadena.isEmpty() && (cadena.codePointAt(0) < 48 ||
                cadena.codePointAt(0) > 48+9) )
        {
            cadena = cadena.substring(1);
        }
        return cadena;
    }
    
    private String ExtraeNum( String cadena, int num[] )
    {
        String r;
        if ( cadena == null || cadena.isEmpty() )
            return null;
        r = "";
        while ( !cadena.isEmpty() && cadena.codePointAt(0) >= 48 &&
                cadena.codePointAt(0) <= 48+9 )
        {
            r = r + cadena.substring(0,1);
            cadena = cadena.substring(1);
        }
        if ( r.isEmpty() )
            return null;
        num[0] = Integer.valueOf(r).intValue();
        return SaltaSep(cadena);
    }

    private void jMICargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMICargarActionPerformed
        JFileChooser fC = new JFileChooser();

        File dirf = new File(dir);
        fC.setCurrentDirectory(dirf);
        fC.setDialogTitle("Ingrese el archivo de datos a leer");
        int res = fC.showOpenDialog(null);
        if ( res == JFileChooser.APPROVE_OPTION ) {
           File ff = fC.getSelectedFile();
           String archivo = ff.getAbsolutePath();
           dir = archivo;
           InputStream in;
           BufferedReader bf;
           String datos;
           datos = null;
           try {
              in = new FileInputStream(archivo);
              bf = new BufferedReader(new InputStreamReader(in));
              String linea;
              do {
                  linea = bf.readLine();
                  if ( linea != null )
                      if  ( datos != null )
                          datos = datos + "," + linea;
                      else
                          datos = linea;
              }
              while ( linea != null );
              bf.close();
           }
           catch ( IOException ein ) {
              datos = null;
           }
           if ( datos != null ) {
               int num[];
               num = new int[1];
               int alto, ancho, tipo, cant;

               datos = ExtraeNum( datos, num );
               alto = num[0];
               jTFaltop.setText(String.valueOf(alto));

               datos = ExtraeNum( datos, num );
               ancho = num[0];
               jTFanchop.setText(String.valueOf(ancho));

               datos = ExtraeNum( datos, num );
               tipo = num[0];
               orden = (tipo % 10) % 6;
               System.out.println("tipo y orden: "+ tipo + " " + orden);
               tipo = tipo - tipo%10;
               tipo = tipo / 10;
               vertical = unir = false;
               if ( tipo == 1 )
                   vertical = true;
               if ( tipo == 2 )
                   unir = true;
               if ( tipo == 3 )
                   vertical = unir = true;
               jCBvertical.setSelected(vertical);
               jCBunir.setSelected(unir);
               Cortes = null;
               Reordena();

               do {
                 datos = ExtraeNum( datos, num ); 
                 if ( datos != null )
                    alto = num[0];
                 datos = ExtraeNum( datos, num );
                 if ( datos != null )
                     ancho = num[0];
                 datos = ExtraeNum( datos, num ); 
                 if ( datos != null )
                     cant = num[0];
                 else
                     cant = 0;
                 while ( cant-- > 0 ) {
                   TRectangulo R = new TRectangulo(alto,ancho,0,0,0);
                   AgregarCorte( Cortes, R );
                 }
               }
               while ( datos != null );
               llenaTabla();
               //     System.out.println("numero: " + num[0]);
           }
        }


    }//GEN-LAST:event_jMICargarActionPerformed

    private void jMIGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIGuardarActionPerformed
        // TODO add your handling code here:
        JFileChooser fC = new JFileChooser();

        File dirf = new File(dir);
        fC.setCurrentDirectory(dirf);
        fC.setDialogTitle("Ingrese el archivo de datos a guardar");
        int res = fC.showSaveDialog(null);
        if ( res == JFileChooser.APPROVE_OPTION ) {
           File ff = fC.getSelectedFile();
           String archivo = ff.getAbsolutePath();
           System.out.println("archivo: "+ archivo);
           dir = archivo;
           OutputStream out;
           BufferedWriter bfout;
           String datos;
           try {
              out = new FileOutputStream(archivo);
              bfout = new BufferedWriter(new OutputStreamWriter(out));
              datos = jTFaltop.getText()+","+jTFanchop.getText()+",";
              int valor=0;
              if ( vertical && !unir )
                 valor = 10;
              if ( !vertical && unir )
                  valor = 20;
              if ( vertical && unir )
                  valor = 30;
              valor = valor + orden;
              datos = datos + valor;
              bfout.write(datos);
              bfout.newLine();
              for (int i=0; i<Cortes.Cantidad();i++) {
                  TRectangulo R = (TRectangulo)Cortes.Obtener(i);
                  datos = R.obtLargo()+","+R.obtAncho()+",1";
                  bfout.write(datos);
                  bfout.newLine();
              }
              bfout.close();
           }
           catch ( IOException eout ) {
           }
        }

    }//GEN-LAST:event_jMIGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCBunir;
    private javax.swing.JCheckBox jCBvertical;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLplancha;
    private javax.swing.JMenuItem jMICargar;
    private javax.swing.JMenuItem jMIGuardar;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTCortes;
    private javax.swing.JTextField jTFaltoc;
    private javax.swing.JTextField jTFaltop;
    private javax.swing.JTextField jTFanchoc;
    private javax.swing.JTextField jTFanchop;
    private javax.swing.JTextField jTFcant;
    private javax.swing.JTextField jTFmax;
    private javax.swing.JTextField jTFnplan;
    private javax.swing.JPanel jpPlancha;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
