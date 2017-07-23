package com.raven.kings.cansat2017.ground.station;

import java.util.HashMap;

public class PayloadData
{
    
    private final int TeamID;
    private final String Glider;
    private final float MissionTime;
    private final int PacketCount;
    private final float Altitude;
    private final float Pressure;
    private final float Speed;
    private final float Temperature;
    private final float Voltage;
    private final float Heading;
    private PayloadState SoftwareState;
    private final int ImageCount;
    
    private HashMap<PayloadDataKeys,Object> data = new HashMap<PayloadDataKeys,Object>();
    
    //public ContainerChart chart;

    public PayloadData(int teamID, String glider, float missionTime, int packetCount, float alt,float pressure, float speed, float temperature, float voltage, float heading, int imageCount, PayloadState state)
    {
        //chart = Main.getMain().myGui.containerChart;
        TeamID = teamID;
        data.put(PayloadDataKeys.TEAMID, TeamID);
        
        Glider = glider;
        data.put(PayloadDataKeys.GLIDER, Glider);
        
        MissionTime = missionTime;
        data.put(PayloadDataKeys.MISSION_TIME, MissionTime);
        
        PacketCount = packetCount; 
        data.put(PayloadDataKeys.PACKET_COUNT, PacketCount);
        
        Altitude = alt;
        data.put(PayloadDataKeys.ALTITUDE, Altitude);
        
        Pressure = pressure;
        data.put(PayloadDataKeys.PRESSURE, Pressure);
        
        Speed = speed;
        data.put(PayloadDataKeys.SPEED, Speed);
        
        Temperature = temperature;
        data.put(PayloadDataKeys.TEMPERATURE, Temperature);
        
        Voltage = voltage;
        data.put(PayloadDataKeys.VOLTAGE, Voltage);
        
        Heading = heading;
        data.put(PayloadDataKeys.HEADING, Heading);
        
        SoftwareState = state;
        data.put(PayloadDataKeys.SOFTWARE_STATE, SoftwareState);
        
        ImageCount = imageCount;
        data.put(PayloadDataKeys.IMAGE_COUNT,ImageCount);
    }
    
    public HashMap<PayloadDataKeys, Object> getData()
    {
        return data;
    }
    
    public int getTeamID()
    {
        return (int)data.get(PayloadDataKeys.TEAMID);
    }
    
    public int getImageCount()
    {
        return (int)data.get(PayloadDataKeys.IMAGE_COUNT);
    }

    public String getVehicle()
    {
        return data.get(PayloadDataKeys.GLIDER).toString();
    }

    public float getMissionTime()
    {
        return (float) data.get(PayloadDataKeys.MISSION_TIME);
    }

    public int getPacketCount()
    {
        return (int)data.get(PayloadDataKeys.PACKET_COUNT);
    }

    public float getAltitude()
    {
        return (float)data.get(PayloadDataKeys.ALTITUDE);
    }
    
    public float getPressure()
    {
        return (float)data.get(PayloadDataKeys.PRESSURE);
    }
    
    public float getSpeed()
    {
        return (float)data.get(PayloadDataKeys.SPEED);
    }

    public float getTemperature()
    {
        return (float)data.get(PayloadDataKeys.TEMPERATURE);
    }

    public float getVoltage()
    {
        return (float)data.get(PayloadDataKeys.VOLTAGE);
    }
    
    public float getHeading()
    {
        return (float)data.get(PayloadDataKeys.HEADING);
    }
    
    public PayloadState getState()
    {
        return (PayloadState)data.get(PayloadDataKeys.SOFTWARE_STATE);
    }
}
