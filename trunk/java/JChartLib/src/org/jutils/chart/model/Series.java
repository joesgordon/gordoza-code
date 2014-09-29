package org.jutils.chart.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Series
{
    public String name;
    public ISeriesData data;
    public boolean visible;
    public final MarkerStyle marker = new MarkerStyle();
    public final MarkerStyle highlight = new MarkerStyle();
    public final LineStyle line = new LineStyle();
}
