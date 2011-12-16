/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bao.panel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.Zoomable;

/**
 *
 * @author bnguyen39
 */
public class ChartPanel1 extends ChartPanel {
   JFreeChart chart; 
 //Zoom begin
/**
   * WZW override
   * ArrayList to store zoom data(in percentage)
   * alLowerX, alUpperX, alLowerY, alUpperY, alPlotInfo, alSelectOrigin
   * X-domainAxis, Y-rangeAxis
   */
  private ArrayList<Double> alLowerX = new ArrayList<Double>();
  private ArrayList<Double> alUpperX = new ArrayList<Double>();
  private ArrayList<Double> alLowerY = new ArrayList<Double>();
  private ArrayList<Double> alUpperY = new ArrayList<Double>();
  private ArrayList<PlotRenderingInfo> alPlotInfo = new ArrayList<PlotRenderingInfo>();
  private ArrayList<Point2D> alSelectOrigin = new ArrayList<Point2D>();

//Zoom end   
  
  public ChartPanel1(JFreeChart chart){
      super(chart, true,true,true,true,true);
  }
    /**
   * WZW override
   * Restore zoomPoints based on storing of 6 element
   * called by mouse Right-click
   */
  private void restoreZoomPoint(){
    int iLen = alLowerX.size();
    int iLast = iLen - 1; //minus 1
    //^Restore only if user had zoom before(store data in ArrayList)
    //^Reset to original chart at initial stage before looping zoom
  if(iLen>0){
    restoreAutoBounds();               
      for(int i=0; i<iLast; i++){
        //^Value restoring
        double hLower = alLowerX.get(i);
        double hUpper = alUpperX.get(i);
        double vLower = alLowerY.get(i);
        double vUpper = alUpperY.get(i);
        PlotRenderingInfo plotInfo = alPlotInfo.get(i);
        Point2D selectOrigin = alSelectOrigin.get(i);
        Plot p = this.chart.getPlot();           
        if (p instanceof Zoomable) {
            Zoomable z = (Zoomable) p;
            if (z.getOrientation() == PlotOrientation.HORIZONTAL) {
                z.zoomDomainAxes(vLower, vUpper, plotInfo, selectOrigin);
                z.zoomRangeAxes(hLower, hUpper, plotInfo, selectOrigin);
            }else {
                z.zoomDomainAxes(hLower, hUpper, plotInfo, selectOrigin);
                z.zoomRangeAxes(vLower, vUpper, plotInfo, selectOrigin);
            }
        }
      }
      //^Removing last(most recent) element
      alLowerX.remove(iLast);
      alUpperX.remove(iLast);
      alLowerY.remove(iLast);
      alUpperY.remove(iLast);
      alPlotInfo.remove(iLast);
      alSelectOrigin.remove(iLast);
    }
}
}
