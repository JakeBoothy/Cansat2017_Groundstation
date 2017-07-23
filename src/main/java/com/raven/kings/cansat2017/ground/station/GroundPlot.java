package com.raven.kings.cansat2017.ground.station;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GroundPlot
{
    private XYImageAnnotation glider;
    private XYSeriesCollection positionDataset;
    private XYSeries Position;
    private XYPlot plot;
    private BufferedImage gliderImage;

    public GroundPlot()
    {
        Position = new XYSeries("Position (m)",false);
        
    }

    public void updateGroundPlot(GroundPlotData g)
    {
        double angle = -g.getAngle();
        double x = g.getX();
        double y = g.getY();
        
        plot.removeAnnotation(glider);
        /*//AffineTransform tx = AffineTransform.getRotateInstance(angle,60,
        //        31);//64x64 image, this is where I want to rotate it   
      
        AffineTransform at = new AffineTransform();
        at.rotate(angle,32,32);
        //at.tra
        
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        Image imageToDraw = null;
        
        imageToDraw = op.filter(gliderImage, (BufferedImage) imageToDraw);
        */
        
        //double degreesToRotate = 90;
        
        //TODO fix this...
        double locationX = gliderImage.getWidth() / 2 + 20;
        double locationY = gliderImage.getHeight() / 2;

        double diff = Math.abs(gliderImage.getWidth() - gliderImage.getHeight());

        //To correct the set of origin point and the overflow
        double rotationRequired = (angle);
        double unitX = Math.abs(Math.cos(rotationRequired));
        double unitY = Math.abs(Math.sin(rotationRequired));

        double correctUx = unitX;
        double correctUy = unitY;

        //if the height is greater than the width, so you have to 'change' the axis to correct the overflow
        if(gliderImage.getWidth() < gliderImage.getHeight()){
            correctUx = unitY;
            correctUy = unitX;
        }
        
        

        //translate the image center to same diff that dislocates the origin, to correct its point set
        AffineTransform objTrans = new AffineTransform();
        objTrans.scale(0.6, 0.6);
        objTrans.translate(correctUx*diff, correctUy*diff);
        objTrans.rotate(rotationRequired, locationX, locationY);

        AffineTransformOp op = new AffineTransformOp(objTrans, AffineTransformOp.TYPE_BILINEAR);
        Image imageToDraw = null;
        imageToDraw = op.filter(gliderImage, (BufferedImage) imageToDraw);
        
        glider = new XYImageAnnotation(x, y, imageToDraw);
       
        plot.addAnnotation(glider);
        Position.add(x, y);
    }

    public JFreeChart createPlot()
    {
        final JFreeChart result = ChartFactory.createXYLineChart("GroundPlot", "X", "Y", positionDataset);
        plot = result.getXYPlot();
       
        ValueAxis xAxis = plot.getDomainAxis();
        ValueAxis yAxis = plot.getRangeAxis();

        positionDataset = new XYSeriesCollection();
        positionDataset.addSeries(Position);

        try
        {
            gliderImage = ImageIO.read(new File("icons/glider.png"));
        } catch (IOException e)
        {
            System.out.println("Could not load image...");
            gliderImage = null;
        }

        glider = new XYImageAnnotation(0, 0, gliderImage);
        
        plot.setDataset(0, positionDataset);
        plot.addAnnotation(glider);

        xAxis.setRange(-100.0, 100.0);
        yAxis.setRange(-100, 100);

        Position.add(0,0);
        return result;
    }

}
