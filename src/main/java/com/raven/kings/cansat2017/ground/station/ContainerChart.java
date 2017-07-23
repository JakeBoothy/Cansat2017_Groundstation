package com.raven.kings.cansat2017.ground.station;

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

public class ContainerChart
{  
    private XYSeries Altitude;
    private XYSeries Temperature;
    private XYSeries Voltage;
    private XYSeriesCollection altDataset;
    private XYSeriesCollection tempDataset;
    private XYSeriesCollection voltDataset;

    public ContainerChart()
    {
        Altitude = new XYSeries("Altitude");
        Temperature = new XYSeries("Temperature");
        Voltage = new XYSeries("Voltage");
    }
    
    public void addDataPoint(ContainerData c)
    {
        Altitude.add(c.getMissionTime(),c.getAltitude());
        Temperature.add(c.getMissionTime(),c.getTemperature());
        Voltage.add(c.getMissionTime(),c.getVoltage());
    }
    
    public JFreeChart createChart() 
    {
        altDataset = new XYSeriesCollection();
        altDataset.addSeries(Altitude);
        
        tempDataset = new XYSeriesCollection();
        tempDataset.addSeries(Temperature);
        
        voltDataset = new XYSeriesCollection();
        voltDataset.addSeries(Voltage);
        
        final JFreeChart result = ChartFactory.createXYAreaChart(
            "Container Data", 
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
        
        XYLineAndShapeRenderer altRenderer = new XYLineAndShapeRenderer();
        altRenderer.setSeriesFillPaint(0, Color.RED);
        altRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer tempRenderer = new XYLineAndShapeRenderer();
        tempRenderer.setSeriesFillPaint(0, Color.BLUE);
        tempRenderer.setBaseShapesFilled(false);
        
        XYLineAndShapeRenderer voltRenderer = new XYLineAndShapeRenderer();
        voltRenderer.setSeriesFillPaint(0, Color.GREEN);
        voltRenderer.setBaseShapesFilled(false);

        final XYPlot plot = result.getXYPlot();
        plot.setRenderer(0, altRenderer);
        plot.setRenderer(1, tempRenderer);
        plot.setRenderer(2, voltRenderer);

        
        plot.setDataset(0, altDataset);
        plot.setDataset(1, tempDataset);
        plot.setDataset(2, voltDataset);

        
        plot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);    
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);    
        plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);    

        plot.setRangeAxis(0, altAxis);
        plot.setRangeAxis(1, tempAxis);
        plot.setRangeAxis(2, voltAxis);

        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.mapDatasetToRangeAxis(2, 2);
        
        ValueAxis xAxis = plot.getDomainAxis();
        
        xAxis.setAutoRange(true);
        //xAxis.setFixedAutoRange(120);  // 60 seconds
        //axis = plot.getRangeAxis();
        //axis.setRange(0.0, 200.0); 

        return result;
    }

}
