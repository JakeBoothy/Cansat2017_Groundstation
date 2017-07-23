package com.raven.kings.cansat2017.ground.station;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PayloadChart
{
    
    private XYSeries Altitude;
    private XYSeries Temperature;
    private XYSeries Voltage;
    private XYSeries Pressure;
    private XYSeries Speed;
    private XYSeries Heading;

    private XYSeriesCollection altDataset;
    private XYSeriesCollection tempDataset;
    private XYSeriesCollection voltDataset;
    private XYSeriesCollection pressureDataset;
    private XYSeriesCollection speedDataset;
    private XYSeriesCollection headingDataset;



    public PayloadChart()
    {
        Altitude = new XYSeries("Altitude");
        Temperature = new XYSeries("Temperature");
        Voltage = new XYSeries("Voltage");
        Pressure = new XYSeries("Pressure");
        Speed = new XYSeries("Speed");
        Heading = new XYSeries("Heading");
    }
    
    public void addDataPoint(PayloadData p)
    {
        Altitude.add(p.getMissionTime(), p.getAltitude());
        Voltage.add(p.getMissionTime(), p.getVoltage());
        Temperature.add(p.getMissionTime(), p.getTemperature());
        Pressure.add(p.getMissionTime(), p.getPressure());
        Speed.add(p.getMissionTime(), p.getSpeed());
        Heading.add(p.getMissionTime(), p.getHeading());
    }
    
    public JFreeChart createChart() 
    {
        altDataset = new XYSeriesCollection();
        altDataset.addSeries(Altitude);
        
        tempDataset = new XYSeriesCollection();
        tempDataset.addSeries(Temperature);
        
        voltDataset = new XYSeriesCollection();
        voltDataset.addSeries(Voltage);
        
        speedDataset = new XYSeriesCollection();
        speedDataset.addSeries(Speed);

        pressureDataset = new XYSeriesCollection();
        pressureDataset.addSeries(Pressure);
        
        headingDataset = new XYSeriesCollection();
        headingDataset.addSeries(Heading);
        
        final JFreeChart result = ChartFactory.createXYAreaChart(
            "Payload Data", 
            "Mission Time (s)", 
            "Value",
            altDataset
        );
        
        NumberAxis altAxis = new NumberAxis("Altitude (m)");
        altAxis.setRange(0,700);
        NumberAxis tempAxis = new NumberAxis("Temperatre (C)");
        tempAxis.setRange(10, 40);
        NumberAxis voltAxis = new NumberAxis("Voltage (V)");
        voltAxis.setRange(0,4);
        NumberAxis speedAxis = new NumberAxis("Speed (m/s)");
        speedAxis.setRange(0,30);
        NumberAxis pressureAxis = new NumberAxis("Pressure (Pa)");
        pressureAxis.setRange(90000,110000);
        NumberAxis headingAxis = new NumberAxis("Heading (deg)");
        headingAxis.setRange(0,360);
        
        BasicStroke thickStroke = new BasicStroke(2);
        
        XYLineAndShapeRenderer altRenderer = new XYLineAndShapeRenderer();
        altRenderer.setSeriesFillPaint(0, Color.RED);
        altRenderer.setSeriesStroke(0, thickStroke);
        altRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer tempRenderer = new XYLineAndShapeRenderer();
        tempRenderer.setSeriesFillPaint(0, Color.BLUE.darker());
        tempRenderer.setSeriesStroke(0, thickStroke);
        tempRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer voltRenderer = new XYLineAndShapeRenderer();
        voltRenderer.setSeriesFillPaint(0, Color.GREEN.darker().darker());
        voltRenderer.setSeriesStroke(0, thickStroke);
        voltRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer speedRenderer = new XYLineAndShapeRenderer();
        speedRenderer.setSeriesFillPaint(0, Color.black);
        speedRenderer.setSeriesStroke(0, thickStroke);
        speedRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer pressureRenderer = new XYLineAndShapeRenderer();
        pressureRenderer.setSeriesFillPaint(0, Color.CYAN.darker().darker());
        pressureRenderer.setSeriesStroke(0, thickStroke);
        pressureRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer headingRenderer = new XYLineAndShapeRenderer();
        headingRenderer.setSeriesFillPaint(0, Color.pink.darker().darker());
        headingRenderer.setSeriesStroke(0, thickStroke);
        headingRenderer.setBaseShapesFilled(false);

        final XYPlot plot = result.getXYPlot();
        
        plot.setRenderer(0, altRenderer);
        plot.setRenderer(1, tempRenderer);
        plot.setRenderer(2, voltRenderer);
        plot.setRenderer(3, speedRenderer);
        plot.setRenderer(4, pressureRenderer);
        plot.setRenderer(5, headingRenderer);
        
        plot.setDataset(0, altDataset);
        plot.setDataset(1, tempDataset);
        plot.setDataset(2, voltDataset);
        plot.setDataset(3, speedDataset);
        plot.setDataset(4, pressureDataset);
        plot.setDataset(5, headingDataset);
        
        plot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);    
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);    
        plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);    
        plot.setRangeAxisLocation(3, AxisLocation.BOTTOM_OR_LEFT); 
        plot.setRangeAxisLocation(4, AxisLocation.BOTTOM_OR_LEFT); 
        plot.setRangeAxisLocation(5, AxisLocation.BOTTOM_OR_LEFT); 
        
        plot.setRangeAxis(0, altAxis);
        plot.setRangeAxis(1, tempAxis);
        plot.setRangeAxis(2, voltAxis);
        plot.setRangeAxis(3, speedAxis);
        plot.setRangeAxis(4, pressureAxis);
        plot.setRangeAxis(5, headingAxis);

        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.mapDatasetToRangeAxis(2, 2);
        plot.mapDatasetToRangeAxis(3, 3);
        plot.mapDatasetToRangeAxis(4, 4);
        plot.mapDatasetToRangeAxis(5, 5);
        
        ValueAxis xAxis = plot.getDomainAxis();
        
        xAxis.setAutoRange(true);
        //xAxis.setFixedAutoRange(120);  // 60 seconds

        return result;
    }

}
