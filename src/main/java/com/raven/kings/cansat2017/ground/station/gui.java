package com.raven.kings.cansat2017.ground.station;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.ui.ApplicationFrame;

import com.digi.xbee.api.exceptions.XBeeException;

//import android.widget.GridLayout;

public class gui extends ApplicationFrame
{
    private static final long serialVersionUID = -2321887882811098291L;
    JMenu comSetting = new JMenu();
    JMenu Configuration = new JMenu();
    JMenuBar menuBar;
    JTextArea consoleTextArea = new JTextArea();
    JFrame frame = new JFrame("Cansat 2017 Groundstation");
    
    JLabel payloadConnected;
    JLabel containerConnected;

    public ContainerChart containerChart;
    public PayloadChart payloadChart;
    public GroundPlot groundPlot;

    TimeSeries containerSeries;
    TimeSeries payloadSeries;

    private workspaceLayout newLayout;
    private HashMap<ContainerDataKeys, JTextField> containerFields = new HashMap<ContainerDataKeys, JTextField>();
    private HashMap<PayloadDataKeys, JTextField> payloadFields = new HashMap<PayloadDataKeys, JTextField>();
    
    private BufferedImage iconImage;
    
    private ImageIcon disconnectedIcon = new ImageIcon();
    private ImageIcon connectedIcon = new ImageIcon();
    
    BufferedImage disconnectedImage;
    BufferedImage connectedImage;
    
   public void setPayloadConnected(boolean t)
   {
       if (t)
       {
           payloadConnected.setIcon(connectedIcon);
       }
       else
       {
           payloadConnected.setIcon(disconnectedIcon);
       }
   }
   
   public void setContainerConnected(boolean t)
   {
       if (t)
       {
           containerConnected.setIcon(connectedIcon);
       }
       else
       {
           containerConnected.setIcon(disconnectedIcon);
       }
   }

    public gui(String applicationTitle)
    {
        super(applicationTitle);
       
        try
        {
            iconImage = ImageIO.read(new File("icons/ravenkings.png"));
            disconnectedImage = ImageIO.read(new File("icons/redBox.png"));
            connectedImage = ImageIO.read(new File("icons/greenBox.png"));
            
            connectedIcon.setImage(connectedImage);
            //connectedIcon.
            disconnectedIcon.setImage(disconnectedImage);
            
            frame.setIconImage(iconImage);
        } catch (IOException e)
        {
            iconImage = null;
            disconnectedIcon = null;
            connectedIcon = null;
        }
        // frame.setIconImage(image);
        redirectSystemStreams();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        newLayout = new workspaceLayout();
        BoxLayout b = new BoxLayout(newLayout, BoxLayout.Y_AXIS);
        newLayout.setLayout(b);
        newLayout.setBackground(Color.red.darker());

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(newLayout);
        makeMenu(frame);
        consoleTextArea = new JTextArea(5, 50);
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        contentPane.add(scrollPane);

        frame.setContentPane(contentPane);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public void updatePortList()
    {
        System.out.println("Refreshing Ports...");
        Main.availablePorts = comPortRadioButtons.getAvailablePorts();
        comPortRadioButtons newComButtons = new comPortRadioButtons();
        newComButtons.makeComPortRadioButtons(comSetting);

        // I dont know its not working how I want it too...
        // Oh well who gaf...
        // comSetting.
        // Configuration.add(comSetting);
        Configuration.revalidate();
        Configuration.repaint();
        menuBar.revalidate();
        menuBar.repaint();
        // comSetting.revalidate();
        // comSetting.repaint();
        revalidate();
        repaint();
    }

    public void updateGUI(ContainerData c)
    {
        containerFields.get(ContainerDataKeys.ALTITUDE).setText(String.format("%.2f", c.getAltitude()) + " m");
        containerFields.get(ContainerDataKeys.CONTAINER).setText(c.getVehicle());
        containerFields.get(ContainerDataKeys.MISSION_TIME).setText(String.format("%.2f", c.getMissionTime()) + " s");
        containerFields.get(ContainerDataKeys.PACKET_COUNT).setText("" + c.getPacketCount());
        containerFields.get(ContainerDataKeys.SOFTWARE_STATE).setText(c.getState() + "");
        containerFields.get(ContainerDataKeys.TEAMID).setText(c.getTeamID() + "");
        containerFields.get(ContainerDataKeys.TEMPERATURE).setText(String.format("%.2f", c.getTemperature()) + " C");
        containerFields.get(ContainerDataKeys.VOLTAGE).setText(String.format("%.2f", c.getVoltage()) + " V");

        containerChart.addDataPoint(c);
    }

    public void updateGUI(PayloadData p)
    {
        payloadFields.get(PayloadDataKeys.ALTITUDE).setText(String.format("%.2f", p.getAltitude()) + " m");
        payloadFields.get(PayloadDataKeys.PRESSURE).setText(String.format("%.2f", p.getPressure()) + " Pa");
        payloadFields.get(PayloadDataKeys.SPEED).setText(String.format("%.2f", p.getSpeed()) + " m/s");
        payloadFields.get(PayloadDataKeys.HEADING).setText(String.format("%.2f", p.getHeading()) + " deg");
        payloadFields.get(PayloadDataKeys.GLIDER).setText(p.getVehicle());
        payloadFields.get(PayloadDataKeys.MISSION_TIME).setText(String.format("%.2f", p.getMissionTime()) + " s");
        payloadFields.get(PayloadDataKeys.PACKET_COUNT).setText("" + p.getPacketCount());
        payloadFields.get(PayloadDataKeys.SOFTWARE_STATE).setText(p.getState() + "");
        payloadFields.get(PayloadDataKeys.TEAMID).setText(p.getTeamID() + "");
        payloadFields.get(PayloadDataKeys.TEMPERATURE).setText(String.format("%.2f", p.getTemperature()) + " C");
        
        payloadFields.get(PayloadDataKeys.VOLTAGE).setText(String.format("%.2f", p.getVoltage()) + " V");
        payloadFields.get(PayloadDataKeys.IMAGE_COUNT).setText(String.format("%d", p.getImageCount()) + "");
        payloadChart.addDataPoint((PayloadData)p);
    }

    public void updateGUI(GroundPlotData g)
    {
        groundPlot.updateGroundPlot(g);
    }

    public void makeMenu(JFrame frame)
    {
        // Makes a top menu
        JMenu File, baudSetting;
        JMenuItem saveas,  exit;
        
        menuBar = new JMenuBar();
        File = new JMenu("File");
        Configuration = new JMenu("Configuration");
        menuBar.add(File);
        menuBar.add(Configuration);

        //Save
        saveas = new JMenuItem("Set Logging Location");
        saveas.addActionListener(newLayout);

        //Exit
        exit = new JMenuItem("Exit");
        exit.addActionListener(newLayout);
        
        File.add(saveas);
        File.addSeparator();
        File.add(exit);
        
        // Baud Setting
        baudRateRadioButtons newBaudButtons = new baudRateRadioButtons();
        baudSetting = new JMenu("Baud");
        newBaudButtons.makeBaudRateRadioButtons(baudSetting);

        // Com Port Setting
        updatePortList();
        comPortRadioButtons newComButtons = new comPortRadioButtons();
        comSetting = new JMenu("Port");
        // comSetting.addActionListener(this);
        newComButtons.makeComPortRadioButtons(comSetting);
        Configuration.add(baudSetting);
        Configuration.add(comSetting);
        frame.setJMenuBar(menuBar);
    }

    private void updateTextArea(final String text)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                consoleTextArea.append(text);
            }
        });
    }

    private void redirectSystemStreams()
    {
        OutputStream out = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException
            {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException
            {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
    
    

    public class workspaceLayout extends JPanel implements ActionListener
    {
        private static final long serialVersionUID = 1L;

        public workspaceLayout()
        {
            add(createWorkSpace());
        }

        public void actionPerformed(ActionEvent e)
        {
            String cmd = e.getActionCommand();
            if (cmd == "Open")
            {
                Main.getMain().myXbee.connect(Main.getPort(), Main.getBaudRate());
            } else if (cmd == "Close")
            {
                Main.getMain().myXbee.close();
            } else if (cmd == "Discover Network")
            {
                Main.getMain().myXbee.startDiscovery();
            }else if  (cmd == "Deploy")
            {
                Main.getMain().myXbee.sendDeployCommand();
            }
            else if (cmd == "Set Logging Location")
            {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showDialog(frame, "Set Logging Location");
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    Main.getMain().loggingLocation = fc.getSelectedFile();
                }
            }
            else if (cmd == "Exit")
            {
                System.exit(0);
            }
        }

        protected JPanel createFeild(ContainerDataKeys feildName, String feildValue)
        {
            JPanel feild = new JPanel();
            feild.setPreferredSize(new Dimension(160, 20));
            BoxLayout feildMgr = new BoxLayout(feild, BoxLayout.X_AXIS);

            feild.setLayout(feildMgr);

            JLabel feildLabel = new JLabel(feildName.toString());
            feildLabel.setMaximumSize(new Dimension(100, 20));
            feildLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            feildLabel.setHorizontalAlignment(JTextField.LEFT);
            feild.add(feildLabel, feildMgr);

            feild.add(Box.createHorizontalGlue());

            JTextField data = new JTextField(5);

            data.setEditable(false);
            data.setText(feildValue);
            data.setHorizontalAlignment(JTextField.TRAILING);
            data.setAlignmentX(Component.RIGHT_ALIGNMENT);
            data.setSize(40, 20);
            data.setPreferredSize(new Dimension(40, 20));
            data.setMaximumSize(new Dimension(40, 20));
            feild.add(data);
            containerFields.put(feildName, data);
            return feild;
        }

        protected JPanel createFeild(PayloadDataKeys feildName, String feildValue)
        {
            JPanel feild = new JPanel();
            feild.setPreferredSize(new Dimension(160, 20));
            BoxLayout feildMgr = new BoxLayout(feild, BoxLayout.X_AXIS);

            feild.setLayout(feildMgr);

            JLabel feildLabel = new JLabel(feildName.toString());
            feildLabel.setMaximumSize(new Dimension(100, 20));
            feildLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            feildLabel.setHorizontalAlignment(JTextField.LEFT);
            feild.add(feildLabel, feildMgr);

            feild.add(Box.createHorizontalGlue());

            JTextField data = new JTextField(5);

            data.setEditable(false);
            data.setText(feildValue);
            data.setHorizontalAlignment(JTextField.TRAILING);
            data.setAlignmentX(Component.RIGHT_ALIGNMENT);
            data.setSize(40, 20);
            data.setPreferredSize(new Dimension(40, 20));
            data.setMaximumSize(new Dimension(40, 20));
            feild.add(data);
            payloadFields.put(feildName, data);
            return feild;
        }

        protected JPanel createWorkSpace()
        {
            JPanel pane = new JPanel();

            JPanel topButtons = new JPanel();
            topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.X_AXIS));

            JButton connectButton = new JButton("Open");
            connectButton.addActionListener(this);
            connectButton.setAlignmentX(LEFT_ALIGNMENT);
            topButtons.add(connectButton);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(this);
            closeButton.setAlignmentX(LEFT_ALIGNMENT);
            topButtons.add(closeButton);

            JButton discoverNetwork = new JButton("Discover Network");
            discoverNetwork.addActionListener(this);
            discoverNetwork.setAlignmentX(LEFT_ALIGNMENT);
            topButtons.add(discoverNetwork);

            topButtons.add(Box.createHorizontalGlue());
            
            payloadConnected = new JLabel("Payload");
            payloadConnected.setIcon(disconnectedIcon);
            topButtons.add(payloadConnected);
            topButtons.add(Box.createHorizontalStrut(5));

            containerConnected = new JLabel("Container");
            containerConnected.setIcon(disconnectedIcon);
            topButtons.add(containerConnected);
            topButtons.add(Box.createHorizontalStrut(5));
            
            JButton deployButton = new JButton("Deploy");
            deployButton.addActionListener(this);
            deployButton.setAlignmentX(CENTER_ALIGNMENT);
            topButtons.add(deployButton);

            topButtons.add(Box.createHorizontalGlue());

            JPanel containerDat = new JPanel(new GridBagLayout());
            containerDat.setBorder(BorderFactory.createLineBorder(Color.black));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.ipady = 2;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.weighty = 0f;

            JLabel titleLabel = new JLabel("Container");
            containerDat.add(titleLabel, gbc);

            gbc.gridy = 1;
            containerDat.add(createFeild(ContainerDataKeys.CONTAINER, "Container"), gbc);

            gbc.gridy = 2;
            containerDat.add(createFeild(ContainerDataKeys.TEAMID, "5555"), gbc);

            gbc.gridy = 3;
            containerDat.add(createFeild(ContainerDataKeys.MISSION_TIME, "00:00:00"), gbc);

            gbc.gridy = 4;
            containerDat.add(createFeild(ContainerDataKeys.PACKET_COUNT, "0"), gbc);

            gbc.gridy = 5;
            containerDat.add(createFeild(ContainerDataKeys.ALTITUDE, "0.0m"), gbc);

            gbc.gridy = 6;
            containerDat.add(createFeild(ContainerDataKeys.TEMPERATURE, "0 C"), gbc);

            gbc.gridy = 7;
            containerDat.add(createFeild(ContainerDataKeys.VOLTAGE, "0V"), gbc);

            gbc.gridy = 8;
            containerDat.add(createFeild(ContainerDataKeys.SOFTWARE_STATE, "LNC"), gbc);

            gbc.gridy = 9;
            gbc.weighty = 1f;
            containerDat.add(Box.createVerticalGlue(), gbc);

            JPanel payloadDat = new JPanel(new GridBagLayout());
            payloadDat.setBorder(BorderFactory.createLineBorder(Color.black));
            gbc = new GridBagConstraints();
            gbc.ipady = 2;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.weighty = 0f;

            titleLabel = new JLabel("Payload");
            payloadDat.add(titleLabel, gbc);
            int n = 0;
            for (; n < PayloadDataKeys.values().length; n++)
            {
                gbc.gridy = n + 1;
                payloadDat.add(createFeild(PayloadDataKeys.values()[n], "-1"), gbc);
            }

            gbc.gridy = 12;
            gbc.weighty = 1f;
            payloadDat.add(Box.createVerticalGlue(), gbc);

            JPanel chart = new JPanel();
            chart.setLayout(new BoxLayout(chart, BoxLayout.Y_AXIS));
            chart.setBorder(BorderFactory.createLineBorder(Color.black));

            // chart.setLayout(new BorderLayout());
            containerChart = new ContainerChart();
            JFreeChart containerData = containerChart.createChart();
            ChartPanel contChartPanal = new ChartPanel(containerData);
            contChartPanal.setLayout(new BorderLayout());
            chart.add(contChartPanal);

            payloadChart = new PayloadChart();
            JFreeChart payloadData = payloadChart.createChart();
            ChartPanel payChartPanal = new ChartPanel(payloadData);
            payChartPanal.setLayout(new BorderLayout());
            chart.add(payChartPanal);

            JPanel map = new JPanel();
            map.setLayout(new BorderLayout());
            map.setBorder(BorderFactory.createLineBorder(Color.black));
            map.setPreferredSize(new Dimension(640, 640));
            groundPlot = new GroundPlot();
            JFreeChart groundChart = groundPlot.createPlot();
            ChartPanel plotPanel = new ChartPanel(groundChart);
            plotPanel.setLayout(new BorderLayout());
            JPanel northMap = new JPanel();
            northMap.setLayout(new BoxLayout(northMap, BoxLayout.X_AXIS));

            map.add(northMap, BorderLayout.NORTH);
            map.add(plotPanel, BorderLayout.CENTER);

            GridBagLayout mainGBC = new GridBagLayout();
            pane.setLayout(mainGBC);
            GridBagConstraints gbcConstraints = new GridBagConstraints();

            gbcConstraints.anchor = GridBagConstraints.NORTH;
            gbcConstraints.fill = GridBagConstraints.HORIZONTAL;
            gbcConstraints.gridwidth = 4;
            gbcConstraints.weighty = 0.05f;
            gbcConstraints.weightx = 1f;
            gbcConstraints.gridx = 0;
            gbcConstraints.gridy = 0;
            pane.add(topButtons, gbcConstraints);

            gbcConstraints.anchor = GridBagConstraints.WEST;
            gbcConstraints.fill = GridBagConstraints.VERTICAL;
            gbcConstraints.gridwidth = 1;
            gbcConstraints.weighty = 1f;
            gbcConstraints.weightx = 0f;
            gbcConstraints.gridx = 0;
            gbcConstraints.gridy = 1;
            pane.add(containerDat, gbcConstraints);

            gbcConstraints.gridx = 1;
            gbcConstraints.gridy = 1;
            pane.add(payloadDat, gbcConstraints);

            gbcConstraints.weightx = 1f;
            gbcConstraints.fill = GridBagConstraints.BOTH;
            gbcConstraints.gridx = 2;
            gbcConstraints.gridy = 1;
            pane.add(chart, gbcConstraints);

            gbcConstraints.weightx = 0f;
            gbcConstraints.anchor = GridBagConstraints.WEST;
            gbcConstraints.fill = GridBagConstraints.VERTICAL;
            gbcConstraints.gridx = 3;
            gbcConstraints.gridy = 1;
            pane.add(map, gbcConstraints);

            return pane;
        }
    }
}
