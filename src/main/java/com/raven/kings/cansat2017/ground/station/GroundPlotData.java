package com.raven.kings.cansat2017.ground.station;

public class GroundPlotData
{

    private final double X;
    private final double Y;
    private final double angle;
    private final double missionTime;
    
    public GroundPlotData(double x, double y, double ang, double t)
    {
        X = x;
        Y = y;
        angle = ang;
        missionTime = t;
    }
    
    public double getX()
    {
        return X;
    }
    public double getY()
    {
        return Y;
    }
    public double getAngle()
    {
        return angle;
    }
    
    public double getMissionTime()
    {
        return missionTime;
    }

}
