/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bao.panel;

import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author Bao
 */
public class BarRender1 extends BarRenderer {
  private Paint m_posPaint;
  private Paint m_negPaint;

  // a null value for either the positive and/or negative paint returns to the series paint for those values.

  public BarRender1(Paint x_posPaint, Paint x_negPaint) {
    super();

    m_posPaint = x_posPaint;
    m_negPaint = x_negPaint;
  }

    @Override
  public Paint getItemPaint(int x_row, int x_col) {
    CategoryDataset l_jfcDataset = getPlot().getDataset();
    String l_rowKey = (String)l_jfcDataset.getRowKey(x_row);
    String l_colKey = (String)l_jfcDataset.getColumnKey(x_col);
    double l_value  = l_jfcDataset.getValue(l_rowKey, l_colKey).doubleValue();
    if (l_value < 3.0) {
      if (null == m_negPaint) { return getSeriesPaint(x_row); }
      else { return m_negPaint; }
    }else {
      if (null == m_posPaint) { return getSeriesPaint(x_row); }
      else { return m_posPaint; }
  }
}
}
