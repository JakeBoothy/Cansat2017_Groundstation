/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.kings.cansat2017.ground.station;

import java.lang.Integer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Main
{
    // TODO Replace with the port where your sender module is connected to.
    private static String PORT = "COM8";

    public static ArrayList<String> availablePorts;

    private ArrayList<ContainerData> containerData = new ArrayList<ContainerData>();
    private ArrayList<PayloadData> payloadData = new ArrayList<PayloadData>();
    private ArrayList<GroundPlotData> groundPlotData = new ArrayList<GroundPlotData>();

    // System.getProperty("user.dir");
    String tlmFileName = "\\CANSAT2017_TLM_6005_RavenKings.csv";
    String serialFileName = "\\CANSAT2017_SERIAL_6005_RavenKings.txt";

    public File loggingLocation;
    File tlmFile;
    File serialFile;

    public static String getPort()
    {
        return PORT;
    }

    public static void setPort(String s)
    {
        PORT = s;
    }

    public static void setPort(int a)
    {
        PORT = "COM" + a;
    }

    private static int BAUD_RATE = 230400;

    public static int getBaudRate()
    {
        return BAUD_RATE;
    }

    public static void setBaudRate(int s)
    {
        BAUD_RATE = s;
    }

    public static void setBaudRate(String s)
    {
        BAUD_RATE = Integer.parseInt(s);
    }

    static final int[] BAUD_RATE_OPTIONS = { 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400 };

    public static long startTime = System.currentTimeMillis();

    // Singleton pattern
    private static Main myMain;

    public static Main getMain()
    {
        return myMain;
    }

    public gui myGui;
    public Xbee myXbee;

    public Main()
    {
        myMain = this;
        myGui = new gui("Cansat 2017 Groundstation");
        myXbee = new Xbee();
        groundPlotData.add(new GroundPlotData(0, 0, 0, 0));// Add zero ground
                                                           // plot data as a
                                                           // starting point
        // System.out.println("new containerData Created");
    }

    public ContainerData getRecentData()
    {
        return containerData.get(containerData.size());
    }

    public void addContainerDataPoint(ContainerData c)
    {
        saveTlmData(c);
        containerData.add(c);
        myGui.updateGUI(c);
    }

    public void addPayloadDataPoint(PayloadData p)
    {
        saveTlmData(p);
        payloadData.add(p);
        addGroundPlotDataPoint(p);
        myGui.updateGUI((PayloadData)p);
    }

    private void addGroundPlotDataPoint(PayloadData p)
    {
        GroundPlotData lastPoint = groundPlotData.get(groundPlotData.size() - 1);
        float speed = p.getSpeed();
        float heading = (float) Math.toRadians(p.getHeading());
        double deltaTime = p.getMissionTime() - lastPoint.getMissionTime();
        float newX = (float) (lastPoint.getX() + deltaTime * speed * Math.cos(heading));
        float newY = (float) (lastPoint.getY() + deltaTime * speed * Math.sin(heading));
        GroundPlotData newPoint = new GroundPlotData(newX, newY, heading, p.getMissionTime());
        groundPlotData.add(newPoint);
        myGui.updateGUI(newPoint);
    }

    public int getContainerPacketCount()
    {
        return containerData.size();
    }

    public int getPayloadPacketCount()
    {
        return payloadData.size();
    }

    public void saveTlmData(ContainerData d)
    {
        try
        {
            tlmFile = new File(loggingLocation, tlmFileName);
            if (!tlmFile.exists())
            {
                tlmFile.createNewFile();
            }
            FileWriter fw = new FileWriter(tlmFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(d.getTeamID() + "," + d.getVehicle() + "," + d.getMissionTime() + "," + d.getPacketCount() + ","
                    + d.getAltitude() + "," + d.getTemperature() + "," + d.getVoltage() + "," + d.getState() + '\n');
            bw.close();
            fw.close();
        } catch (IOException e)
        {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void saveTlmData(PayloadData d)
    {
        try
        {
            tlmFile = new File(loggingLocation, tlmFileName);
            if (!tlmFile.exists())
            {
                tlmFile.createNewFile();
            }
            FileWriter fw = new FileWriter(tlmFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(d.getTeamID() + "," + d.getVehicle() + "," + d.getMissionTime() + "," + d.getPacketCount() + ","
                    + d.getAltitude() + "," + d.getPressure() + "," + d.getSpeed() + "," + d.getTemperature() + ","
                    + d.getVoltage() + "," + d.getHeading() + "," + d.getState() + "," + d.getImageCount() + '\n');
            bw.close();
            fw.close();
        } catch (IOException e)
        {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        new Main();
    }
}