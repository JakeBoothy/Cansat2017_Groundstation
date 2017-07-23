package com.raven.kings.cansat2017.ground.station;

import java.util.HashMap;

public class ContainerData
{
    
    private final int TeamID;
    private final String Container;
    private final float MissionTime;
    private final int PacketCount;
    private final float Altitude;
    private final float Temperature;
    private final float Voltage;
    private ContainerState SoftwareState;
    
    private HashMap<ContainerDataKeys,Object> data = new HashMap<ContainerDataKeys,Object>();
    
    //public ContainerChart chart;

    public ContainerData(int teamID, String container, float missionTime, int packetCount, float alt, float temperature, float voltage, ContainerState state)
    {
        //chart = Main.getMain().myGui.containerChart;
        TeamID = teamID;
        data.put(ContainerDataKeys.TEAMID, TeamID);
        
        Container = container;
        data.put(ContainerDataKeys.CONTAINER, Container);
        
        MissionTime = missionTime;
        data.put(ContainerDataKeys.MISSION_TIME, MissionTime);
        
        PacketCount = packetCount; 
        data.put(ContainerDataKeys.PACKET_COUNT, PacketCount);
        
        Altitude = alt;
        data.put(ContainerDataKeys.ALTITUDE, Altitude);
        
        Temperature = temperature;
        data.put(ContainerDataKeys.TEMPERATURE, Temperature);
        
        Voltage = voltage;
        data.put(ContainerDataKeys.VOLTAGE, Voltage);
        
        SoftwareState = state;
        data.put(ContainerDataKeys.SOFTWARE_STATE, SoftwareState);
        
    }
    
    /*public void setData(ContainerDataKeys c, Object o)
    {
        data.put(c, o);
        Main.getMain().myGui.containerChart.addDataPoint(c, (float)o);
        
    }*/
    
    /*public void setField(ContainerDataKeys c, JTextField f)
    {
        fields.put(c, f);
    }*/
    public HashMap<ContainerDataKeys, Object> getData()
    {
        return data;
    }
    
    public int getTeamID()
    {
        return (int)data.get(ContainerDataKeys.TEAMID);
    }

    public String getVehicle()
    {
        return (String)data.get(ContainerDataKeys.CONTAINER);
    }

    public float getMissionTime()
    {
        return (float) data.get(ContainerDataKeys.MISSION_TIME);
    }

    public int getPacketCount()
    {
        return (int)data.get(ContainerDataKeys.PACKET_COUNT);
    }

    public float getAltitude()
    {
        return (float)data.get(ContainerDataKeys.ALTITUDE);
    }
    
    public float getTemperature()
    {
        return (float)data.get(ContainerDataKeys.TEMPERATURE);
    }

    public float getVoltage()
    {
        return (float)data.get(ContainerDataKeys.VOLTAGE);
    }
    
    public ContainerState getState()
    {
        return (ContainerState)data.get(ContainerDataKeys.SOFTWARE_STATE);
    }
}
