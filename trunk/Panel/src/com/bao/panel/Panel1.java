/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bao.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.SortedMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author bnguyen39
 */
public class Panel1 extends javax.swing.JPanel{
    ChartPanel chartPanel;
       public Panel1 (SortedMap<Double,Double> xyData) {
        //initComponents();
            XYSeries xySeries = new XYSeries("XY");
            for(Double x:xyData.keySet()){
                xySeries .add(x, xyData.get(x));
            }
            XYSeriesCollection collection = new XYSeriesCollection();
            collection.addSeries(xySeries );

            JFreeChart chart = createChart(collection);
           
            chartPanel = new ChartPanel(chart);
            Dimension d = new Dimension(200,100);
            chartPanel.setSize(d);
            chartPanel.setPreferredSize(d);
            BorderLayout layout = new BorderLayout();
            this.setLayout(layout);
            this.add(chartPanel,BorderLayout.CENTER);    
            //this.add(chartPanel); 
    }
    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot("TITLE",
                "X", "Y", dataset, PlotOrientation.VERTICAL, true,
        false, false);
 
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setNoDataMessage("NO DATA");
        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesOutlinePaint(0, Color.black);
        renderer.setUseOutlinePaint(true);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        domainAxis.setTickMarkInsideLength(2.0f);
        domainAxis.setTickMarkOutsideLength(0.0f);
       
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickMarkInsideLength(2.0f);
        rangeAxis.setTickMarkOutsideLength(0.0f);      
       
        return chart;
    }       
}
